package db;

import model.Patient;
import util.Config;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;

public class PatientDAO extends BasicDAO {
	private UserDAO userDAO;
	
	public PatientDAO(DB db) {
		collection = db.getCollection(Config.DB_COLLECTION_PATIENT);
		collection.ensureIndex("id");
		collection.ensureIndex(new BasicDBObject("location", "2d"));
		userDAO = new UserDAO(db);
	}

	public Patient findById(int id) {
		BasicDBObject example = new BasicDBObject("id", id);
		DBObject obj = collection.findOne(example);
		Patient p = new Patient();
		p.setId(id);
		p = userDAO.findById(p);
		if (obj != null && p != null) {
			BasicDBList loc = (BasicDBList) obj.get("location");
			p.setLatitude((Double) loc.get(0));
			p.setLongitude((Double) loc.get(1));
			p.setSsn((String) obj.get("ssn"));
			return p;
		}
		return null;
	}

	public void persist(Patient p) {
		userDAO.persist(p);
		BasicDBObject obj = new BasicDBObject("id", p.getId())
				.append("location", new Double[] {p.getLatitude(), p.getLongitude()})
				.append("ssn", p.getSsn());
		p.setId(obj.getInt("id"));
		collection.insert(obj);
	}

	public void update(Patient p) {
		userDAO.update(p);
		BasicDBObject example = new BasicDBObject("id", p.getId());
		BasicDBObject update = new BasicDBObject("id", p.getId())
				.append("ssn", p.getSsn())
				.append("location", new Double[] {p.getLatitude(), p.getLongitude()});
		collection.findAndModify(example, update);
	}
}
