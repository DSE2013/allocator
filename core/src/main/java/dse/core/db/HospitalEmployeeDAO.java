package at.ac.tuwien.dse.core.db;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.dse.core.model.HospitalEmployee;
import at.ac.tuwien.dse.core.util.Config;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class HospitalEmployeeDAO extends BasicDAO {
	private UserDAO userDAO;

	public HospitalEmployeeDAO(DB db) {
		collection = db.getCollection(Config.DB_COLLECTION_HOSPITAL_EMPLOYEE);
		collection.ensureIndex("id");
		userDAO = new UserDAO(db);
	}

	public HospitalEmployee findById(int id) {
		HospitalEmployee he = new HospitalEmployee();
		he.setId(id);
		he = userDAO.findById(he);
		BasicDBObject example = new BasicDBObject("id", id);
		DBObject obj = collection.findOne(example);
		if (obj != null) {
			he.setHospitalId((Integer) obj.get("hospitalId"));
			return he;
		}
		return null;
	}
	
	public List<HospitalEmployee> findByHospitalId(int hospitalId) {
		List<HospitalEmployee> heList = new ArrayList<HospitalEmployee>();
		BasicDBObject example = new BasicDBObject("hospitalId", hospitalId);
		DBCursor cur = collection.find(example);
		while(cur.hasNext()) {
			DBObject dbo = cur.next();
			heList.add(findById((Integer) dbo.get("id")));
		}
		return heList;
	}

	public void persist(HospitalEmployee he) {
		userDAO.persist(he);
		BasicDBObject obj = new BasicDBObject("id", he.getId())
				.append("hospitalId", he.getHospitalId());
		he.setId(obj.getInt("id"));
		collection.insert(obj);
	}

	public void update(HospitalEmployee he) {
		userDAO.update(he);
		BasicDBObject example = new BasicDBObject("id", he.getId());
		BasicDBObject update = new BasicDBObject("id", he.getId())
				.append("hospitalId", he.getHospitalId());
		collection.findAndModify(example, update);
	}
}
