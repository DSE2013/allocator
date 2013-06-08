package dse.core.db;


import dse.core.model.Operation;
import dse.core.util.Config;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;

public class OperationDAO extends BasicDAO {
	public OperationDAO(DB db) {
		collection = db.getCollection(Config.DB_COLLECTION_OPERATION);
	}

	public Operation findById(int id) {
		BasicDBObject example = new BasicDBObject("id", id);
		DBObject obj = collection.findOne(example);
		if (obj != null) {
			Operation o = new Operation();
			o.setId(id);
			o.setOperationTypeId((Integer) obj.get("operationTypeId"));
			o.setDoctorId((Integer) obj.get("doctorId"));
			o.setPatientId((Integer) obj.get("patientId"));
			return o;
		}
		return null;
	}

	public void persist(Operation o) {
		BasicDBObject obj = new BasicDBObject("id", getNewIndex())
				.append("operationTypeId", o.getOperationTypeId())
				.append("doctorId", o.getDoctorId())
				.append("patientId", o.getPatientId());
		collection.insert(obj);
		o.setId((Integer) obj.get("id"));
		collection.ensureIndex("id");
	}

	public void update(Operation o) {
		BasicDBObject example = new BasicDBObject("id", o.getId());
		BasicDBObject update = new BasicDBObject("id", o.getId())
				.append("operationTypeId", o.getOperationTypeId())
				.append("doctorId", o.getDoctorId())
				.append("patientId", o.getPatientId());
		collection.findAndModify(example, update);
		collection.ensureIndex("id");
	}
	
	public void delete(int id) {
		BasicDBObject example = new BasicDBObject("id", id);
		collection.remove(example);
	}
}
