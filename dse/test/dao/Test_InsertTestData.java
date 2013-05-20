package dao;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import model.Doctor;
import model.Hospital;
import model.OperationType;
import model.Patient;
import model.TimeSlot;

import org.junit.BeforeClass;
import org.junit.Test;

import util.Config;

import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.Mongo;

import db.DAOFactory;
import db.DoctorDAO;
import db.HospitalDAO;
import db.OperationTypeDAO;
import db.PatientDAO;
import db.TimeSlotDAO;

public class Test_InsertTestData {
	protected static DAOFactory fact;
	protected static DB db;
	
	@BeforeClass
	public static void setUp() throws ParseException {
		try {
			db = Mongo.connect(new DBAddress(Config.DB_HOST));
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			return;
		}
		fact = new DAOFactory(db);
		db.dropDatabase();
		
		PatientDAO pDao = fact.getPatientDAO();
		Patient p1 = new Patient();
		p1.setEmail("asdf");
		p1.setLatitude(48.3175373);
		p1.setLongitude(15.1228989);
		p1.setName("Franz Meier");
		p1.setPassword("pw".getBytes());
		p1.setSsn("1234");
		pDao.persist(p1);
		
		Patient p2 = new Patient();
		p2.setEmail("asdf1");
		p2.setLatitude(48.21524);
		p2.setLongitude(16.34799);
		p2.setName("Hermine Müller");
		p2.setPassword("pw1".getBytes());
		p2.setSsn("1233");
		pDao.persist(p2);
		
		Patient p3 = new Patient();
		p3.setEmail("asdf2");
		p3.setLatitude(48.2222);
		p3.setLongitude(16.38231);
		p3.setName("Markus Moser");
		p3.setPassword("pw2".getBytes());
		p3.setSsn("1235");
		pDao.persist(p3);
		
		DoctorDAO dDao = fact.getDoctorDAO();
		Doctor d = new Doctor();
		d.setEmail("qwer");
		d.setName("Dr. Aufmesser");
		d.setPassword("dr".getBytes());
		dDao.persist(d);
		
		Doctor d1 = new Doctor();
		d1.setEmail("qwer1");
		d1.setName("Dr. Gott");
		d1.setPassword("dr1".getBytes());
		dDao.persist(d1);
		
		OperationTypeDAO oDao = fact.getOperationTypeDAO();
		OperationType o = new OperationType();
		o.setName("Augenheilkunde");
		oDao.persist(o);
		
		OperationType o1 = new OperationType();
		o1.setName("Orthopädie");
		oDao.persist(o1);
		
		OperationType o2 = new OperationType();
		o2.setName("HNO");
		oDao.persist(o2);
		
		OperationType o3 = new OperationType();
		o3.setName("Neurochirurgie");
		oDao.persist(o3);
		
		OperationType o4 = new OperationType();
		o4.setName("Kardiologie");
		oDao.persist(o4);
		
		HospitalDAO hDao = fact.getHospitalDAO();
		
		Hospital h = new Hospital();
		h.setLatitude(48.30768);
		h.setLongitude(16.32373);
		h.setName("LKH Klosterneuburg");
		hDao.persist(h);
		
		Hospital h1 = new Hospital();
		h1.setLatitude(48.21511);
		h1.setLongitude(16.3804);
		h1.setName("Barmherzige Brüder Wien");
		hDao.persist(h1);
		
		Hospital h2 = new Hospital();
		h2.setLatitude(48.20353);
		h2.setLongitude(15.63817);
		h2.setName("KH St. Poelten");
		hDao.persist(h2);
		
		Hospital h3 = new Hospital();
		h3.setLatitude(48.22058);
		h3.setLongitude(16.34705);
		h3.setName("AKH Wien");
		hDao.persist(h3);
		
		Hospital h4 = new Hospital();
		h4.setLatitude(48.30329);
		h4.setLongitude(14.30547);
		h4.setName("AKH Linz");
		hDao.persist(h4);
		
		Hospital h5 = new Hospital();
		h5.setLatitude(48.33158);
		h5.setLongitude(16.06069);
		h5.setName("LKH Tulln");
		hDao.persist(h5);
		
		SimpleDateFormat sdf = new SimpleDateFormat("d.M.y H:m");
		
		TimeSlotDAO tsDao = fact.getTimeSlotDAO();
		TimeSlot ts = new TimeSlot();
		ts.setHospitalId(h1.getId());
		ts.setOperationTypeId(o.getId());
		ts.setStart(sdf.parse("30.06.2013 09:00"));
		ts.setEnd(sdf.parse("30.06.2013 11:00"));
		tsDao.persist(ts);
		
		TimeSlot ts1 = new TimeSlot();
		ts1.setHospitalId(h2.getId());
		ts1.setOperationTypeId(o.getId());
		ts1.setStart(sdf.parse("29.06.2013 11:00"));
		ts1.setEnd(sdf.parse("29.06.2013 13:00"));
		tsDao.persist(ts1);
		
		TimeSlot ts2 = new TimeSlot();
		ts2.setHospitalId(h3.getId());
		ts2.setOperationTypeId(o.getId());
		ts2.setStart(sdf.parse("29.06.2013 12:00"));
		ts2.setEnd(sdf.parse("29.06.2013 15:00"));
		tsDao.persist(ts2);
		
		TimeSlot ts3 = new TimeSlot();
		ts3.setHospitalId(h4.getId());
		ts3.setOperationTypeId(o.getId());
		ts3.setStart(sdf.parse("29.06.2013 13:00"));
		ts3.setEnd(sdf.parse("29.06.2013 17:00"));
		tsDao.persist(ts3);
		
		TimeSlot ts4 = new TimeSlot();
		ts4.setHospitalId(h.getId());
		ts4.setOperationTypeId(o.getId());
		ts4.setStart(sdf.parse("30.06.2013 09:00"));
		ts4.setEnd(sdf.parse("30.06.2013 10:00"));
		tsDao.persist(ts4);
	}
	
	@Test
	public void test() {
		
	}
}
