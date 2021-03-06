package dse.core.db;


import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;

import dse.core.model.Hospital;
import dse.core.util.Config;

public class HospitalDAO extends BasicDAO {
	public HospitalDAO(DB db) {
		collection = db.getCollection(Config.DB_COLLECTION_HOSPITAL);
		collection.ensureIndex("id");
		collection.ensureIndex(new BasicDBObject("location", "2d"));
	}

	public Hospital findById(int id) {
		BasicDBObject example = new BasicDBObject("id", id);
		DBObject obj = collection.findOne(example);
		if (obj != null) {
			Hospital h = new Hospital();
			h.setId(id);
			BasicDBList loc = (BasicDBList) obj.get("location");
			h.setLatitude((Double) loc.get(1));
			h.setLongitude((Double) loc.get(0));
			h.setName((String) obj.get("name"));
			return h;
		}
		return null;
	}

	public void persist(Hospital h) {
		BasicDBObject obj = new BasicDBObject("id", getNewIndex())
				.append("name", h.getName())
				.append("location", new Double[] {h.getLongitude(), h.getLatitude()});
		h.setId(obj.getInt("id"));
		collection.insert(obj);
	}

	public void update(Hospital h) {
		BasicDBObject example = new BasicDBObject("id", h.getId());
		BasicDBObject update = new BasicDBObject("id", h.getId())
				.append("name", h.getName())
				.append("location", new Double[] {h.getLongitude(), h.getLatitude()});
		collection.findAndModify(example, update);
	}
}
