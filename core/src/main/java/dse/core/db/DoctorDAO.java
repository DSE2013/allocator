package dse.core.db;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;

import dse.core.model.Doctor;
import dse.core.util.Config;

public class DoctorDAO extends BasicDAO {
	private UserDAO userDAO;
	
	public DoctorDAO(DB db) {
		collection = db.getCollection(Config.DB_COLLECTION_DOCTOR);
		collection.ensureIndex("id");
		userDAO = new UserDAO(db);
	}

	public Doctor findById(int id) {
		Doctor d = new Doctor();
		d.setId(id);
		return userDAO.findById(d);
	}

	public void persist(Doctor d) {
		userDAO.persist(d);
		BasicDBObject obj = new BasicDBObject("id", d.getId());
		d.setId(obj.getInt("id"));
		collection.insert(obj);
	}

	public void update(Doctor d) {
		userDAO.update(d);
	}
}
