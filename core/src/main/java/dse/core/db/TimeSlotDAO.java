package dse.core.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import dse.core.model.TimeSlot;
import dse.core.util.Config;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class TimeSlotDAO extends BasicDAO {
	public TimeSlotDAO(DB db) {
		collection = db.getCollection(Config.DB_COLLECTION_SLOT);
		collection.ensureIndex("id");
	}

	public TimeSlot findById(int id) {
		BasicDBObject example = new BasicDBObject("id", (Integer) id);
		DBObject obj = collection.findOne(example);
		if (obj != null) {
			Integer i = (Integer) obj.get("id");
			Date s = (Date) obj.get("start");
			Date e = (Date) obj.get("end");
			Integer hId = (Integer) obj.get("hospitalId");
			Integer otId = (Integer) obj.get("operationTypeId");
			Integer oId = (Integer) obj.get("operationId");
			TimeSlot ts = new TimeSlot();
			ts.setId(i);
			ts.setStart(s);
			ts.setEnd(e);
			ts.setHospitalId(hId);
			ts.setOperationId(oId);
			ts.setOperationTypeId(otId);
			return ts;
		}
		return null;
	}

	public void persist(TimeSlot ts) {
		BasicDBObject obj = new BasicDBObject("id", getNewIndex())
				.append("start", ts.getStart()).append("end", ts.getEnd())
				.append("hospitalId", ts.getHospitalId())
				.append("operationTypeId", ts.getOperationTypeId())
				.append("operationId", ts.getOperationId());
		ts.setId((Integer) obj.get("id"));
		collection.insert(obj);
	}

	public void update(TimeSlot ts) {
		BasicDBObject example = new BasicDBObject("id", ts.getId());
		BasicDBObject update = new BasicDBObject("id", ts.getId())
				.append("start", ts.getStart()).append("end", ts.getEnd())
				.append("hospitalId", ts.getHospitalId())
				.append("operationTypeId", ts.getOperationTypeId())
				.append("operationId", ts.getOperationId());
		collection.findAndModify(example, update);
	}

	public List<TimeSlot> findAll() {
		List<TimeSlot> tsList = new ArrayList<TimeSlot>();
		DBCursor cur = collection.find();
		while (cur.hasNext()) {
			DBObject obj = cur.next();
			if (obj != null) {
				Integer i = (Integer) obj.get("id");
				Date s = (Date) obj.get("start");
				Date e = (Date) obj.get("end");
				Integer hId = (Integer) obj.get("hospitalId");
				Integer otId = (Integer) obj.get("operationTypeId");
				Integer oId = (Integer) obj.get("operationId");
				TimeSlot ts = new TimeSlot();
				ts.setId(i);
				ts.setStart(s);
				ts.setEnd(e);
				ts.setHospitalId(hId);
				ts.setOperationId(oId);
				ts.setOperationTypeId(otId);
				tsList.add(ts);
			}
		}
		return tsList;
	}

	public List<TimeSlot> findByExample(TimeSlot example) {
		List<TimeSlot> tsList = new ArrayList<TimeSlot>();
		BasicDBObject obj = new BasicDBObject();
		if (example.getId() != null)
			obj.append("id", example.getId());
		if (example.getStart() != null)
			obj.append("start", example.getStart());
		if (example.getEnd() != null)
			obj.append("end", example.getEnd());
		if (example.getHospitalId() != null)
			obj.append("hospitalId", example.getHospitalId());
		if (example.getOperationTypeId() != null)
			obj.append("operationTypeId", example.getOperationTypeId());
		if (example.getOperationId() != null)
			obj.append("operationId", example.getOperationId());
		DBCursor cur = collection.find(obj);
		while (cur.hasNext()) {
			DBObject obj1 = cur.next();
			if (obj1 != null) {
				TimeSlot ts = new TimeSlot();
				ts.setId((Integer) obj1.get("id"));
				ts.setStart((Date) obj1.get("start"));
				ts.setEnd((Date) obj1.get("end"));
				ts.setHospitalId((Integer) obj1.get("hospitalId"));
				ts.setOperationId((Integer) obj1.get("operationId"));
				ts.setOperationTypeId((Integer) obj1.get("operationTypeId"));
				tsList.add(ts);
			}
		}
		return tsList;
	}


}
