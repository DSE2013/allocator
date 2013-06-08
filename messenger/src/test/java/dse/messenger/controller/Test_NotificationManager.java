package dse.messenger.controller;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.Mongo;

import dse.core.db.DAOFactory;
import dse.core.db.HospitalEmployeeDAO;
import dse.core.db.NotificationDAO;
import dse.core.db.TimeSlotDAO;
import dse.core.message.NotificationMessage;
import dse.core.model.HospitalEmployee;
import dse.core.model.Notification;
import dse.core.model.TimeSlot;
import dse.core.util.Config;

import static org.junit.Assert.*;

public class Test_NotificationManager extends Test_AbstractTest {
	private static NotificationManager notMan;
	private static DAOFactory daoFact;
	private static DB db;
	private static NotificationDAO notDAO;
	
	@BeforeClass
	public static void init() throws UnknownHostException {
		db = Mongo.connect(new DBAddress(Config.DB_HOST));
		notMan = new NotificationManager(db);
		daoFact = new DAOFactory(db);
		notDAO = daoFact.getNotificationDAO();
	}
	
	@Before
	public void clearDB() {
		db.getCollection(Config.DB_COLLECTION_NOTIFICATION).remove(new BasicDBObject());
	}
	
	@Test
	public void testInvalidData() {
		NotificationMessage nMsg = new NotificationMessage();
		nMsg.setSuccessful(true);
		nMsg.setDoctorId(1);
		nMsg.setPatientId(2);
		nMsg.setTimeSlotId(-1);
		nMsg.setDelete(true);
		assertFalse(notMan.notifyUsers(nMsg));
		
		nMsg.setTimeSlotId(1);
		nMsg.setPatientId(-1);
		assertFalse(notMan.notifyUsers(nMsg));
		
		nMsg.setPatientId(1);
		nMsg.setDoctorId(-1);
		assertFalse(notMan.notifyUsers(nMsg));
	}
	
	@Test
	public void testSuccessfulDeletion() {
		int timeSlotId = 3;
		NotificationMessage nMsg = new NotificationMessage();
		nMsg.setSuccessful(true);
		nMsg.setDoctorId(1);
		nMsg.setPatientId(2);
		nMsg.setTimeSlotId(timeSlotId);
		nMsg.setDelete(true);
		notMan.notifyUsers(nMsg);
		
		// check in db
		TimeSlotDAO tsDao = daoFact.getTimeSlotDAO();
		HospitalEmployeeDAO heDao = daoFact.getHospitalEmployeeDAO();
		TimeSlot ts = tsDao.findById(timeSlotId);
		assertNotNull(ts);
		
		List<Integer> expectedIds = new ArrayList<Integer>();
		expectedIds.add(1);
		expectedIds.add(2);
		List<HospitalEmployee> heList = heDao.findByHospitalId(ts.getHospitalId());
		for(HospitalEmployee he : heList) {
			expectedIds.add(he.getId());
		}
		
		List<Notification> ln = notDAO.findAll();
		// 2 (+ #hospitalEmployees) notifications: patient, x hospital employees, doctor
		assertEquals(2 + heList.size(), ln.size());
		for(int i = 0; i < ln.size(); i++) {
			assertTrue(expectedIds.remove(ln.get(i).getUserId()));
		}
	}
	
	@Test
	public void testUnsuccessfulReservation() {
		NotificationMessage nMsg = new NotificationMessage();
		nMsg.setSuccessful(false);
		nMsg.setDoctorId(1);
		nMsg.setPatientId(1);
		nMsg.setTimeSlotId(1);
		nMsg.setDelete(false);
		notMan.notifyUsers(nMsg);
		
		// check entries in db
		List<Notification> ln = notDAO.findAll();
		assertEquals(1, ln.size());
		Notification docNot = ln.get(0);
		// 1 notification: doctor
		assertEquals(1, docNot.getUserId().longValue());
		assertFalse(docNot.getDisplayed());
	}
	
	@Test
	public void testSuccessfulReservation() {
		int timeSlotId = 4;
		NotificationMessage nMsg = new NotificationMessage();
		nMsg.setSuccessful(true);
		nMsg.setDoctorId(1);
		nMsg.setPatientId(2);
		nMsg.setTimeSlotId(timeSlotId);
		nMsg.setDelete(false);
		notMan.notifyUsers(nMsg);
		
		// check in db
		TimeSlotDAO tsDao = daoFact.getTimeSlotDAO();
		HospitalEmployeeDAO heDao = daoFact.getHospitalEmployeeDAO();
		TimeSlot ts = tsDao.findById(timeSlotId);
		assertNotNull(ts);
		
		List<Integer> expectedIds = new ArrayList<Integer>();
		expectedIds.add(1);
		expectedIds.add(2);
		List<HospitalEmployee> heList = heDao.findByHospitalId(ts.getHospitalId());
		for(HospitalEmployee he : heList) {
			expectedIds.add(he.getId());
		}
		
		List<Notification> ln = notDAO.findAll();
		// 2 (+ #hospitalEmployees) notifications: patient, x hospital employees, doctor
		assertEquals(2 + heList.size(), ln.size());
		for(int i = 0; i < ln.size(); i++) {
			assertTrue(expectedIds.remove(ln.get(i).getUserId()));
		}
	}
}
