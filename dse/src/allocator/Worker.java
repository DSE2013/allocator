package allocator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import message.AllocationMessage;
import message.Message;
import messagequeue.IMessageQueueHelper;
import model.Patient;
import model.TimeSlot;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import util.Config;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

import db.DAOFactory;
import db.PatientDAO;
import db.TimeSlotDAO;

public class Worker implements Runnable {
	private IMessageQueueHelper queueUI, queueMessenger;
	private PatientDAO pDao;
	private TimeSlotDAO tsDao;
	private DB db;

	public Worker(String host, DB db) throws IOException {
		/*queueMessenger = new MessageQueueHelper(host,
				Config.ALLOCATOR_MESSENGER);
		queueUI = new MessageQueueHelper(host, Config.UI_ALLOCATOR);
		*/
		this.db = db;
		DAOFactory fact = new DAOFactory(db);
		pDao = fact.getPatientDAO();
		tsDao = fact.getTimeSlotDAO();
		queueMessenger = mock(IMessageQueueHelper.class);
		queueUI = mock(IMessageQueueHelper.class);
		
		try {
			when(queueUI.consume()).thenAnswer(new Answer<Message>() {
				@Override
				public Message answer(InvocationOnMock invocation) throws Throwable {
					Thread.sleep(5000);
					AllocationMessage msg = new AllocationMessage();
					msg.setDoctorId(1);
					msg.setPatientId(2);
					msg.setOperationTypeId(1);
					msg.setMaxDistance(5);

					return msg;
				}
			});
		} catch (ShutdownSignalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true) {
			AllocationMessage msg;
			try {
				msg = (AllocationMessage) queueUI.consume();
				// TODO: handle catch clauses
			} catch (ShutdownSignalException e) {
				e.printStackTrace();
				break;
			} catch (ConsumerCancelledException e) {
				e.printStackTrace();
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			
			TimeSlot nearest = getNearestSlot(msg.getPatientId(), msg.getOperationTypeId(), msg.getMaxDistance());
			System.out.println(nearest);

			System.out.println(msg.toString());
		}
	}
	
	private TimeSlot getNearestSlot(Integer patientId, Integer operationTypeId,
			Double maxDistance) {
		Patient p = pDao.findById(patientId);
		if (p == null) {
			System.out.println("patient not found");
			return null;
		}
		
		DBCollection hospitalColl = db.getCollection(Config.DB_COLLECTION_HOSPITAL);
		BasicDBObject filter = new BasicDBObject("$near", new double[] {
				p.getLatitude(), p.getLongitude() })
				.append("$spherical", true)
				.append("$distanceMultiplier", 6371)
				.append("$maxDistance", maxDistance / 6371);

		BasicDBObject query = new BasicDBObject("location", filter);
		
		DBCursor cursor = hospitalColl.find(query);
		while(cursor.hasNext()) {
			DBObject obj = cursor.next();
			TimeSlot example = new TimeSlot();
			example.setHospitalId((Integer)obj.get("id"));
			List<TimeSlot> tsList = tsDao.findByExample(example);
			for(TimeSlot ts : tsList) {
				// if operation type fits and no operation is already assigned -> return timeslot
				if(ts.getOperationTypeId() == operationTypeId && ts.getOperationId() == null) {
					return ts;
				}
			}
		}
		return null;
	}
}
