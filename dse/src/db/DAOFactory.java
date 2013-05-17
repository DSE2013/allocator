package db;

import java.net.UnknownHostException;

import util.Config;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class DAOFactory {
	private DB db;
	
	public DAOFactory(DB db) {
		this.db = db;
	}
	
	public TimeSlotDAO getTimeSlotDAO() {
		return new TimeSlotDAO(db);
	}
	
	public OperationDAO getOperationDAO() {
		return new OperationDAO(db);
	}
	
	public HospitalDAO getHospitalDAO() {
		return new HospitalDAO(db);
	}
	
	public PatientDAO getPatientDAO() {
		return new PatientDAO(db);
	}
}
