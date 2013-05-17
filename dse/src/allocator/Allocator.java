package allocator;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;

import com.mongodb.DB;
import com.mongodb.Mongo;

import model.Hospital;
import model.Operation;
import model.TimeSlot;

import db.DAOFactory;
import db.HospitalDAO;
import db.OperationDAO;
import db.TimeSlotDAO;

import util.Config;

public class Allocator {
	public static void main(String []args) {
		Mongo mongo;
		try {
			mongo = new Mongo(Config.DB_HOST);
		} catch (UnknownHostException e1) {
			System.out.println("conn failed");
			return;
		}
		DB db = mongo.getDB(Config.DB_NAME);
		DAOFactory fact;
		fact = new DAOFactory(db);
		HospitalDAO hDao = fact.getHospitalDAO();

		Hospital h = new Hospital();
		h.setLatitude(48.52);
		h.setLongitude(15.23);
		h.setName("testHospital");
		
		hDao.persist(h);
		
		h = hDao.findById(h.getId());
		System.out.println(h.getName());
		System.out.println(h.getLatitude());
		System.out.println(h.getLongitude());
		
		Thread t = null;
		
		// restart if worker thread stops for some reason
		while(true) {
			if(t == null || !t.isAlive()) {
				try {
					t = new Thread(new Worker(Config.MQ_HOST, db));
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
				t.start();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
