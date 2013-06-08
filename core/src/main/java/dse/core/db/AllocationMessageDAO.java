package dse.core.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;

import dse.core.message.AllocationMessage;
import dse.core.util.Config;

public class AllocationMessageDAO extends BasicDAO {
	public AllocationMessageDAO(DB db) {
		collection = db.getCollection(Config.DB_COLLECTION_ALLOCATION_MESSAGE);
		collection.ensureIndex("id");
	}

	public void persist(AllocationMessage m) {
		BasicDBObject obj = new BasicDBObject("id", getNewIndex())
			.append("doctorId", m.getDoctorId())
			.append("operationTypeId", m.getOperationTypeId())
			.append("patientId", m.getPatientId())
			.append("lengthInMin", m.getLengthInMin())
			.append("maxDistance", m.getMaxDistance())
			.append("timestamp", System.currentTimeMillis());

		collection.insert(obj);
	}
}
