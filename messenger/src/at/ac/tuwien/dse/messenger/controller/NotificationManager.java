package at.ac.tuwien.dse.messenger.controller;

import java.util.Date;

import at.ac.tuwien.dse.core.db.DAOFactory;
import at.ac.tuwien.dse.core.db.DoctorDAO;
import at.ac.tuwien.dse.core.db.HospitalDAO;
import at.ac.tuwien.dse.core.db.HospitalEmployeeDAO;
import at.ac.tuwien.dse.core.db.NotificationDAO;
import at.ac.tuwien.dse.core.db.OperationTypeDAO;
import at.ac.tuwien.dse.core.db.PatientDAO;
import at.ac.tuwien.dse.core.db.TimeSlotDAO;
import at.ac.tuwien.dse.core.message.NotificationMessage;
import at.ac.tuwien.dse.core.model.Doctor;
import at.ac.tuwien.dse.core.model.Hospital;
import at.ac.tuwien.dse.core.model.HospitalEmployee;
import at.ac.tuwien.dse.core.model.Notification;
import at.ac.tuwien.dse.core.model.OperationType;
import at.ac.tuwien.dse.core.model.Patient;
import at.ac.tuwien.dse.core.model.TimeSlot;
import at.ac.tuwien.dse.messenger.util.NotificationStrings;

import com.mongodb.DB;

public class NotificationManager {
	private DoctorDAO docDAO;
	private PatientDAO patDAO;
	private TimeSlotDAO tsDAO;
	private HospitalDAO hosDAO;
	private OperationTypeDAO otDAO;
	private NotificationDAO notDAO;
	private HospitalEmployeeDAO heDAO;
	
	public NotificationManager(DB db) {
		DAOFactory fact = new DAOFactory(db);
		docDAO = fact.getDoctorDAO();
		patDAO = fact.getPatientDAO();
		tsDAO = fact.getTimeSlotDAO();
		hosDAO = fact.getHospitalDAO();
		otDAO = fact.getOperationTypeDAO();
		notDAO = fact.getNotificationDAO();
		heDAO = fact.getHospitalEmployeeDAO();
	}
	
	public boolean notifyUsers(NotificationMessage msg) {
		Doctor d = docDAO.findById(msg.getDoctorId());
		Patient p = patDAO.findById(msg.getPatientId());
		TimeSlot ts = tsDAO.findById(msg.getTimeSlotId());
		String operationTypeName = "", hospitalName = "";
		Date start = new Date(), end = new Date();
		Hospital h = null;
		if(ts != null) {
			h = hosDAO.findById(ts.getHospitalId());
			OperationType ot = otDAO.findById(ts.getOperationTypeId());
			hospitalName = h.getName();
			operationTypeName = ot.getName();
			start = ts.getStart();
			end = ts.getEnd();
		}
		NotificationStrings ns = new NotificationStrings(d.getName(), p.getName(), hospitalName, operationTypeName, start, end);
		if(msg.isSuccessful()) {
			// successfull reservation deletion
			if(msg.isDelete()) {
				// notify doctor
				notifyUser(d.getId(), ns.getDeletedTitleDoctor(), ns.getDeletedMsgDoctor());
				// notify patient
				notifyUser(p.getId(), ns.getDeletedTitlePatient(), ns.getDeletedMsgPatient());
				// notify hospital employees
				for(HospitalEmployee he : heDAO.findByHospitalId(h.getId())) {
					notifyUser(he.getId(), ns.getDeletedTitleHospital(), ns.getDeletedMsgHospital());
				}
			} else {
				notifyUser(d.getId(), ns.getSuccessTitleDoctor(), ns.getSuccessMsgDoctor());
				notifyUser(p.getId(), ns.getSuccessTitlePatient(), ns.getSuccessMsgPatient());
				for(HospitalEmployee he : heDAO.findByHospitalId(h.getId())) {
					notifyUser(he.getId(), ns.getSuccessTitleHospital(), ns.getSuccessMsgHospital());
				}
			}
		} else {
			notifyUser(d.getId(), ns.getFailedTitleDoctor(), ns.getFailedMsgDoctor());
		}
		return true;
	}
	
	private boolean notifyUser(Integer userId, String title, String message) {
		Notification not = new Notification();
		not.setCreatedAt(new Date());
		not.setDisplayed(false);
		not.setMessage(message);
		not.setTitle(title);
		not.setUpdatedAt(null);
		notDAO.persist(not);
		return true;
	}
}
