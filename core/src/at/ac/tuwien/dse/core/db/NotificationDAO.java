package at.ac.tuwien.dse.core.db;

import java.util.Date;


import at.ac.tuwien.dse.core.model.Notification;
import at.ac.tuwien.dse.core.util.Config;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;

public class NotificationDAO extends BasicDAO {
	public NotificationDAO(DB db) {
		collection = db.getCollection(Config.DB_COLLECTION_NOTIFICATION);
		collection.ensureIndex("id");
	}

	public Notification findById(int id) {
		BasicDBObject example = new BasicDBObject("id", id);
		DBObject obj = collection.findOne(example);
		Notification n = new Notification();
		n.setId(id);
		if (obj != null) {
			n.setMessage((String) obj.get("message"));
			n.setTitle((String) obj.get("title"));
			n.setDisplayed((Boolean) obj.get("displayed"));
			n.setCreatedAt((Date) obj.get("createdAt"));
			n.setUpdatedAt((Date) obj.get("updatedAt"));
			n.setUserId((Integer) obj.get("userId"));
			return n;
		}
		return null;
	}

	public void persist(Notification n) {
		BasicDBObject obj = new BasicDBObject("id", getNewIndex())
				.append("title", n.getTitle())
				.append("message", n.getMessage())
				.append("displayed", n.getDisplayed())
				.append("createdAt", n.getCreatedAt())
				.append("updatedAt", n.getUpdatedAt())
				.append("userId", n.getUserId());
		n.setId(obj.getInt("id"));
		collection.insert(obj);
	}

	public void update(Notification n) {
		BasicDBObject example = new BasicDBObject("id", n.getId());
		BasicDBObject update = new BasicDBObject("id", n.getId())
				.append("title", n.getTitle())
				.append("message", n.getMessage())
				.append("displayed", n.getDisplayed())
				.append("createdAt", n.getCreatedAt())
				.append("updatedAt", n.getUpdatedAt())
				.append("userId", n.getUserId());
		collection.findAndModify(example, update);
	}
}
