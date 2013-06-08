package dse.allocator.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBObject;

import dse.core.db.DAOFactory;
import dse.core.db.OperationDAO;
import dse.core.db.PatientDAO;
import dse.core.db.TimeSlotDAO;
import dse.core.model.Operation;
import dse.core.model.Patient;
import dse.core.model.TimeSlot;
import dse.core.util.Config;

public class SlotAllocator {
	private DAOFactory daoFact;
	private PatientDAO pDao;
	private TimeSlotDAO tsDao;
	private OperationDAO oDao;
	private DB db;

	public SlotAllocator(DB db) {
		this.db = db;
		daoFact = new DAOFactory(db);
		tsDao = daoFact.getTimeSlotDAO();
		oDao = daoFact.getOperationDAO();
		pDao = daoFact.getPatientDAO();
	}

	public TimeSlot getNearestSlot(Integer patientId, Integer operationTypeId,
			Double maxDistance, Integer lengthInMin) {
		Patient p = pDao.findById(patientId);

		if (p == null) {
			System.out.println("patient not found " + patientId);
			return null;
		}

		System.out.println(p.getName());
		System.out.println(p.getLatitude() + ", " + p.getLongitude());

		BasicDBObject filter = new BasicDBObject("geoNear",
				Config.DB_COLLECTION_HOSPITAL)
				.append("near",
						new double[] { p.getLongitude(), p.getLatitude() })
				.append("spherical", true).append("distanceMultiplier", 6371)
				.append("maxDistance", maxDistance / 6371);

		CommandResult cr = db.command(filter);

		System.out.println(cr.get("results"));
		BasicDBList list = (BasicDBList) cr.get("results");
		if (list.size() == 0)
			return null;
		for (Object obj : list) {
			DBObject dbObj = (DBObject) ((DBObject) obj).get("obj");
			TimeSlot example = new TimeSlot();
			example.setHospitalId((Integer) dbObj.get("id"));
			List<TimeSlot> tsList = tsDao.findByExample(example);
			Collections.sort(tsList, new Comparator<TimeSlot>() {
				public int compare(TimeSlot o1, TimeSlot o2) {
					return (int) (o1.getStart().getTime() - o2.getStart().getTime());
				}
			});
			for (TimeSlot ts : tsList) {
				// if operation type fits and no operation is already assigned
				// -> return timeslot
				long lengthInMinTs = (ts.getEnd().getTime()-ts.getStart().getTime())/1000/60;
				if (ts.getOperationTypeId() == operationTypeId
						&& ts.getOperationId() == null && lengthInMinTs >= lengthInMin) {
					return ts;
				}
			}
		}
		return null;
	}

	// TODO solve concurrency issues
	public boolean reserveSlot(TimeSlot slot, Integer patientId,
			Integer doctorId) {
		Operation o = new Operation();
		o.setDoctorId(doctorId);
		o.setOperationTypeId(slot.getOperationTypeId());
		o.setPatientId(patientId);
		oDao.persist(o);
		slot.setOperationId(o.getId());
		tsDao.update(slot);
		return true;
	}
	
	public boolean deleteReservation(int i) {
		TimeSlot ts = tsDao.findById(i);
		if(ts == null)
			return false;
		Operation o = oDao.findById(ts.getOperationId());
		if(o == null)
			return false;
		oDao.delete(o.getId());
		ts.setOperationId(null);
		tsDao.update(ts);
		return true;
	}
}
