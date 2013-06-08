package dse.allocator.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;

import dse.core.dao.Test_AbstractTest;
import dse.core.db.HospitalDAO;
import dse.core.model.Hospital;
import dse.core.model.TimeSlot;


public class Test_SlotAllocator extends Test_AbstractTest {
	private static SlotAllocator sa;
	
	@Before
	public void init() {
		sa = new SlotAllocator(db);
	}

	@Test
	public void testGetNearestSlot() {
		// get slot for patientId 1, operationTypeId 1, maxDistance 80km, minimum length 60min 
		TimeSlot ts = sa.getNearestSlot(1, 1, 80d, 60);
		assertNotNull(ts);
		// slot is longer than 60 min
		assertTrue((ts.getEnd().getTime() - ts.getStart().getTime())/1000/60 >= 60);
		// operationType is 1
		assertEquals((int)ts.getOperationTypeId(), 1);
		HospitalDAO hDao = fact.getHospitalDAO();
		Hospital h = hDao.findById(ts.getHospitalId());
		// nearest hospital is KH St. Poelten
		assertEquals(h.getName(), "KH St. Poelten");
	}
	
	@Test
	public void testGetNearestSlot_isEarliest() throws ParseException {
		TimeSlot ts = sa.getNearestSlot(4, 1, 10d, 30);
		assertNotNull(ts);
		assertTrue((ts.getEnd().getTime() - ts.getStart().getTime())/1000/60 >= 30);
		assertEquals((int)ts.getOperationTypeId(), 1);
		HospitalDAO hDao = fact.getHospitalDAO();
		Hospital h = hDao.findById(ts.getHospitalId());
		assertEquals(h.getName(), "LKH Tulln");
		SimpleDateFormat sdf = new SimpleDateFormat("d.M.y H:m");
		assertEquals(sdf.parse("30.06.2013 10:00"), ts.getStart());
		assertEquals(sdf.parse("30.06.2013 10:30"), ts.getEnd());
	}
	
	@Test
	public void testGetNearestSlot_noAppropriateSlotAvailable() {
		TimeSlot ts = sa.getNearestSlot(2, 0, 150d, 30);
		assertNull(ts);
	}
	
	@Test
	public void testGetNearestSlot_maxDistance() {
		// KH St. Poelten is about 40.2km away from patient
		// first query should return no result (40km)
		TimeSlot ts = sa.getNearestSlot(1, 1, 40d, 30);
		assertNull(ts);
		// second query should return a result (41km)
		ts = sa.getNearestSlot(1, 1, 41d, 30);
		assertNotNull(ts);
		HospitalDAO hDao = fact.getHospitalDAO();
		Hospital h = hDao.findById(ts.getHospitalId());
		assertEquals(h.getName(), "KH St. Poelten");
	}
	
	@Test
	public void testGetNearestSlot_invalidParameters() {
		assertNull(sa.getNearestSlot(1, 1, 40d, -20));
		assertNull(sa.getNearestSlot(1, 1, -40d, 30));
		assertNull(sa.getNearestSlot(1, -1, 40d, 30));
		assertNull(sa.getNearestSlot(-1, 1, 40d, 30));
	}
}
