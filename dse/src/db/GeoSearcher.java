package db;

import model.Patient;
import model.TimeSlot;

import com.mongodb.BasicDBObject;

public class GeoSearcher {
	private DAOFactory daoFact;
	private PatientDAO pDao;

	public GeoSearcher(DAOFactory daoFactory) {
		daoFact = daoFactory;
		pDao = daoFactory.getPatientDAO();
	}


}
