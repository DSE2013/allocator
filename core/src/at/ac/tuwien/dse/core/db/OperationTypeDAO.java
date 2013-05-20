package at.ac.tuwien.dse.core.db;


import at.ac.tuwien.dse.core.model.OperationType;
import at.ac.tuwien.dse.core.util.Config;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;

public class OperationTypeDAO extends BasicDAO {
	public OperationTypeDAO(DB db) {
		collection = db.getCollection(Config.DB_COLLECTION_OPERATION_TYPE);
		collection.ensureIndex("id");
	}

	public OperationType findById(int id) {
		BasicDBObject example = new BasicDBObject("id", id);
		DBObject obj = collection.findOne(example);
		if (obj != null) {
			OperationType o = new OperationType();
			o.setId(id);
			o.setName((String) obj.get("name"));
			return o;
		}
		return null;
	}

	public void persist(OperationType o) {
		BasicDBObject obj = new BasicDBObject("id", getNewIndex())
				.append("name", o.getName());
		o.setId((Integer) obj.get("id"));
		collection.insert(obj);
	}

	public void update(OperationType o) {
		BasicDBObject example = new BasicDBObject("id", o.getId());
		BasicDBObject update = new BasicDBObject("id", o.getId())
			.append("name", o.getName());
		collection.findAndModify(example, update);
	}
}
