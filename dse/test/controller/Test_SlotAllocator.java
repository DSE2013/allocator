package controller;

import static org.junit.Assert.*;
import model.Hospital;
import model.TimeSlot;

import org.junit.Test;

import dao.Test_InsertTestData;
import db.HospitalDAO;

public class Test_SlotAllocator extends Test_InsertTestData {
	SlotAllocator sa;
	
	@Test
	public void testGetNearestSlot() {
		sa = new SlotAllocator(db);
		TimeSlot ts = sa.getNearestSlot(1, 1, 80d, 60);
		assertNotNull(ts);
		assertTrue((ts.getEnd().getTime() - ts.getStart().getTime())/1000/60 >= 60);
		assertEquals((int)ts.getOperationTypeId(), 1);
		HospitalDAO hDao = fact.getHospitalDAO();
		Hospital h = hDao.findById(ts.getHospitalId());
		assertEquals(h.getName(), "KH St. Poelten");
		
		ts = sa.getNearestSlot(3, 1, 20d, 90);
		assertNotNull(ts);
		assertTrue((ts.getEnd().getTime() - ts.getStart().getTime())/1000/60 >= 90);
		assertEquals((int)ts.getOperationTypeId(), 1);
		h = hDao.findById(ts.getHospitalId());
		assertEquals(h.getName(), "Barmherzige Brüder Wien");
		
		ts = sa.getNearestSlot(2, 0, 150d, 30);
		assertNull(ts);
	}
	
	@Test
	public void testGetNearestSlot_earliest() {
		
	}
}
