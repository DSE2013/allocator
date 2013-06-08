package dse.messenger.controller;

import java.util.Date;

import dse.core.db.DAOFactory;
import dse.core.db.DoctorDAO;
import dse.core.db.HospitalDAO;
import dse.core.db.HospitalEmployeeDAO;
import dse.core.db.NotificationDAO;
import dse.core.db.OperationTypeDAO;
import dse.core.db.PatientDAO;
import dse.core.db.TimeSlotDAO;
import dse.core.db.UserDAO;
import dse.core.message.NotificationMessage;
import dse.core.model.Doctor;
import dse.core.model.Hospital;
import dse.core.model.HospitalEmployee;
import dse.core.model.Notification;
import dse.core.model.OperationType;
import dse.core.model.Patient;
import dse.core.model.TimeSlot;
import dse.core.model.User;
import dse.messenger.util.Config;
import dse.messenger.util.NotificationStrings;

import com.mongodb.DB;

public class NotificationManager {
	private DoctorDAO docDAO;
	private PatientDAO patDAO;
	private TimeSlotDAO tsDAO;
	private HospitalDAO hosDAO;
	private OperationTypeDAO otDAO;
	private NotificationDAO notDAO;
	private HospitalEmployeeDAO heDAO;
	private UserDAO uDAO;
	private MailSender mailSender;
	
	
	public NotificationManager(DB db) {
		DAOFactory fact = new DAOFactory(db);
		docDAO = fact.getDoctorDAO();
		patDAO = fact.getPatientDAO();
		tsDAO = fact.getTimeSlotDAO();
		hosDAO = fact.getHospitalDAO();
		otDAO = fact.getOperationTypeDAO();
		notDAO = fact.getNotificationDAO();
		heDAO = fact.getHospitalEmployeeDAO();
		uDAO = fact.getUserDAO();
		mailSender = new MailSender(Config.SMTP_HOST, Config.SMTP_USER, Config.SMTP_PASS, Config.MAIL_ADDRESS, Config.MAIL_NAME);
	}
	
	public boolean notifyUsers(NotificationMessage msg) {
		Doctor d = docDAO.findById(msg.getDoctorId());
		Patient p = patDAO.findById(msg.getPatientId());
		TimeSlot ts = tsDAO.findById(msg.getTimeSlotId());
		String operationTypeName = "", hospitalName = "", doctorName = "", patientName = "";
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
		if(d != null)
			doctorName = d.getName();
		if(p != null)
			patientName = p.getName();
		NotificationStrings ns = new NotificationStrings(doctorName, patientName, hospitalName, operationTypeName, start, end);
		if(msg.isSuccessful()) {
			// successful reservation deletion
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
		User u = new Patient();
		u.setId(userId);
		u = uDAO.findById(u);
		if(u != null)
			mailSender.sendMail(u.getEmail(), u.getName(), title, message);
		Notification not = new Notification();
		not.setCreatedAt(new Date());
		not.setDisplayed(false);
		not.setMessage(message);
		not.setTitle(title);
		not.setUpdatedAt(null);
		not.setUserId(userId);
		notDAO.persist(not);
		return true;
	}
}
