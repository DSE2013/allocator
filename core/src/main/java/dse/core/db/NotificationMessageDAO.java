package dse.core.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;

import dse.core.message.NotificationMessage;
import dse.core.util.Config;

public class NotificationMessageDAO extends BasicDAO {
	public NotificationMessageDAO(DB db) {
		collection = db.getCollection(Config.DB_COLLECTION_NOTIFICATION_MESSAGE);
		collection.ensureIndex("id");
	}

	public void persist(NotificationMessage m) {
		BasicDBObject obj = new BasicDBObject("id", getNewIndex())
			.append("doctorId", m.getDoctorId())
			.append("timeSlotId", m.getTimeSlotId())
			.append("patientId", m.getPatientId())
			.append("timestamp", System.currentTimeMillis());

		collection.insert(obj);
	}
}
