package dse.core.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import dse.core.model.Notification;
import dse.core.util.Config;

public class NotificationDAO extends BasicDAO {
	public NotificationDAO(DB db) {
		collection = db.getCollection(Config.DB_COLLECTION_NOTIFICATION);
		collection.ensureIndex("id");
	}
	
	public List<Notification> findAll() {
		List<Notification> tsList = new ArrayList<Notification>();
		DBCursor cur = collection.find();
		while (cur.hasNext()) {
			DBObject obj = cur.next();
			Notification n = new Notification();
			if (obj != null) {
				n.setId((Integer) obj.get("id"));
				n.setMessage((String) obj.get("message"));
				n.setTitle((String) obj.get("title"));
				n.setDisplayed((Boolean) obj.get("displayed"));
				n.setCreatedAt((Date) obj.get("createdAt"));
				n.setUpdatedAt((Date) obj.get("updatedAt"));
				n.setUserId((Integer) obj.get("userId"));
				tsList.add(n);
			}
		}
		return tsList;
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
