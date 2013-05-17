package controller;

import java.util.List;

import model.Operation;
import model.Patient;
import model.TimeSlot;
import util.Config;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBObject;

import db.DAOFactory;
import db.OperationDAO;
import db.PatientDAO;
import db.TimeSlotDAO;


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
			Double maxDistance) {
		Patient p = pDao.findById(patientId);

		if (p == null) {
			System.out.println("patient not found");
			return null;
		}

		System.out.println(p.getName());
		System.out.println(p.getLatitude() + ", " + p.getLongitude());

		BasicDBObject filter = new BasicDBObject("geoNear",
				Config.DB_COLLECTION_HOSPITAL)
				.append("near",
						new double[] { p.getLatitude(), p.getLongitude() })
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
			for (TimeSlot ts : tsList) {
				// if operation type fits and no operation is already assigned
				// -> return timeslot
				if (ts.getOperationTypeId() == operationTypeId
						&& ts.getOperationId() == null) {
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

}
