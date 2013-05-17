package db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.Config;
import model.TimeSlot;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class TimeSlotDAO extends BasicDAO {
	public TimeSlotDAO(DB db) {
		collection = db.getCollection(Config.DB_COLLECTION_SLOT);
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
		BasicDBObject obj = new BasicDBObject("id", getHighestIndex())
				.append("start", ts.getStart()).append("end", ts.getEnd())
				.append("hospitalId", ts.getHospitalId())
				.append("operationTypeId", ts.getOperationTypeId())
				.append("operationId", ts.getOperationId());
		collection.insert(obj);
		collection.ensureIndex("id");
	}

	public void update(TimeSlot ts) {
		BasicDBObject example = new BasicDBObject("id", ts.getId());
		BasicDBObject update = new BasicDBObject("id", ts.getId())
				.append("start", ts.getStart()).append("end", ts.getEnd())
				.append("hospitalId", ts.getHospitalId())
				.append("operationTypeId", ts.getOperationTypeId())
				.append("operationId", ts.getOperationId());
		collection.findAndModify(example, update);
		collection.ensureIndex("id");
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
				Integer i = (Integer) obj1.get("id");
				Date s = (Date) obj1.get("start");
				Date e = (Date) obj1.get("end");
				Integer hId = (Integer) obj1.get("hospitalId");
				Integer otId = (Integer) obj1.get("operationTypeId");
				Integer oId = (Integer) obj1.get("operationId");
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


}
