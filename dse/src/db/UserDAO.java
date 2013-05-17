package db;

import model.Hospital;
import model.Operation;
import model.TimeSlot;
import model.User;
import util.Config;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class UserDAO extends BasicDAO {
	public UserDAO(DB db) {
		collection = db.getCollection(Config.DB_COLLECTION_USER);
		collection.ensureIndex("id");
	}

	public <T extends User> T findById(T u) {
		if(u == null)
			return null;
		BasicDBObject example = new BasicDBObject("id", u.getId());
		DBObject obj = collection.findOne(example);
		if (obj != null) {
			u.setEmail((String) obj.get("email"));
			u.setName((String) obj.get("name"));
			u.setPassword((byte[]) obj.get("password"));
			return u;
		}
		return null;
	}

	public void persist(User u) {
		BasicDBObject obj = new BasicDBObject("id", getHighestIndex())
				.append("name", u.getName())
				.append("email", u.getEmail())
				.append("password", u.getPassword());
		u.setId(obj.getInt("id"));
		collection.insert(obj);
	}

	public void update(User u) {
		BasicDBObject example = new BasicDBObject("id", u.getId());
		BasicDBObject update = new BasicDBObject("id", u.getId())
				.append("name", u.getName())
				.append("email", u.getEmail())
				.append("password", u.getPassword());
		collection.findAndModify(example, update);
	}
}
