package db;

import com.mongodb.DB;

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
	
	public OperationTypeDAO getOperationTypeDAO() {
		return new OperationTypeDAO(db);
	}
	
	public DoctorDAO getDoctorDAO() {
		return new DoctorDAO(db);
	}
}
