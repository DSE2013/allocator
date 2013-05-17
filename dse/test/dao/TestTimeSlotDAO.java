package dao;

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;
import java.util.Date;

import model.TimeSlot;

import org.junit.BeforeClass;
import org.junit.Test;

import util.Config;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.Mongo;

import db.DAOFactory;
import db.TimeSlotDAO;

public class TestTimeSlotDAO {
	private static DAOFactory fact;
	private static DB db;
	@BeforeClass
	public static void setUp() {
		Mongo mongo;
		try {
			mongo = new Mongo(Config.DB_HOST);
		} catch (UnknownHostException e1) {
			System.out.println("conn failed");
			return;
		}
		db = mongo.getDB(Config.DB_NAME);
		fact = new DAOFactory(db);
		db.getCollection(Config.DB_COLLECTION_SLOT).remove(new BasicDBObject());
	}
	
	@Test
	public void testPersist() {
		TimeSlotDAO tsDao = fact.getTimeSlotDAO();
		TimeSlot ts = new TimeSlot();
		Date now = new Date();
		ts.setEnd(now);
		ts.setStart(now);
		ts.setHospitalId(1);
		ts.setOperationId(2);
		ts.setOperationTypeId(3);
		tsDao.persist(ts);
		
		TimeSlot check = tsDao.findById(ts.getId());
		assertEquals(ts.getId(), check.getId());
		assertEquals(ts.getStart(), check.getStart());
		assertEquals(ts.getEnd(), check.getEnd());
		assertEquals(ts.getHospitalId(), check.getHospitalId());
		assertEquals(ts.getOperationId(), check.getOperationId());
		assertEquals(ts.getOperationTypeId(), check.getOperationTypeId());
		db.getCollection(Config.DB_COLLECTION_SLOT).remove(new BasicDBObject());
	}
}
