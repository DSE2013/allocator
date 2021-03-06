package dse.allocator;

import java.io.IOException;

import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.Mongo;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

import dse.allocator.controller.SlotAllocator;
import dse.core.db.DAOFactory;
import dse.core.db.NotificationMessageDAO;
import dse.core.message.AllocationMessage;
import dse.core.message.DeletionMessage;
import dse.core.message.Message;
import dse.core.message.NotificationMessage;
import dse.core.messagequeue.IMessageQueueHelper;
import dse.core.messagequeue.MessageQueueHelper;
import dse.core.model.TimeSlot;
import dse.core.util.Config;


public class Worker implements Runnable {
	private IMessageQueueHelper queueUI, queueMessenger;
	private SlotAllocator slotAllocator;
	private NotificationMessageDAO notificationMessageDAO;

	public Worker() throws IOException {
		DB db = Mongo.connect(new DBAddress(Config.DB_HOST));
		queueMessenger = new MessageQueueHelper(Config.MQ_HOST, Config.MQ_PORT, Config.MQ_USER, Config.MQ_PASS, Config.MQ_NAME_ALLOCATOR_MESSENGER); 
		queueUI = new MessageQueueHelper(Config.MQ_HOST, Config.MQ_PORT, Config.MQ_USER, Config.MQ_PASS, Config.MQ_NAME_UI_ALLOCATOR);
		
		DAOFactory fact = new DAOFactory(db);
		notificationMessageDAO = fact.getNotificationMessageDAO();
		 
		slotAllocator = new SlotAllocator(db);
		AllocationMessage msg = new AllocationMessage();
		msg.setDoctorId(7);
		msg.setPatientId(1);
		msg.setOperationTypeId(1);
		msg.setMaxDistance(150);
		msg.setLengthInMin(60);
		
		queueUI.publish(msg);
		
		DeletionMessage dMsg = new DeletionMessage();
		dMsg.setTimeSlotId(2);
		dMsg.setDoctorId(7);
		dMsg.setPatientId(1);
		
		queueUI.publish(dMsg);

//		queueMessenger = mock(IMessageQueueHelper.class);
//		queueUI = mock(IMessageQueueHelper.class);
//
//		try {
//			when(queueUI.consume()).thenAnswer(new Answer<Message>() {
//				@Override
//				public Message answer(InvocationOnMock invocation)
//						throws Throwable {
//					Thread.sleep(5000);
//					AllocationMessage msg = new AllocationMessage();
//					msg.setDoctorId(1);
//					msg.setPatientId(1);
//					msg.setOperationTypeId(1);
//					msg.setMaxDistance(100);
//
//					return msg;
//				}
//			});
//		} catch (ShutdownSignalException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ConsumerCancelledException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public void run() {
		while (true) {
			Message msg;
			try {
				msg = queueUI.consume();
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
			
			NotificationMessage nMsg = new NotificationMessage();
			nMsg.setDoctorId(msg.getDoctorId());
			nMsg.setPatientId(msg.getPatientId());
			
			if(msg.getClass().equals(AllocationMessage.class)) {
				AllocationMessage aMsg = (AllocationMessage)msg;
				TimeSlot nearest = slotAllocator.getNearestSlot(msg.getPatientId(),
						aMsg.getOperationTypeId(), aMsg.getMaxDistance(), aMsg.getLengthInMin());
	
				nMsg.setDelete(false);
				if (nearest == null) {
					nMsg.setSuccessful(false);
				} else {
					nMsg.setTimeSlotId(nearest.getId());
					if (slotAllocator.reserveSlot(nearest, msg.getPatientId(), msg.getDoctorId())) {
						nMsg.setSuccessful(true);
					} else {
						nMsg.setSuccessful(false);
					}
				}
			} else if(msg.getClass().equals(DeletionMessage.class)) {
				DeletionMessage dMsg = (DeletionMessage)msg;
				nMsg.setDelete(true);
				nMsg.setTimeSlotId(dMsg.getTimeSlotId());
				if(slotAllocator.deleteReservation(dMsg.getTimeSlotId())) {
					nMsg.setSuccessful(true);
				} else {
					nMsg.setSuccessful(false);
				}
			}
			
			notificationMessageDAO.persist(nMsg);
			
			try {
				queueMessenger.publish(nMsg);
				System.out.println("published: doc:" + nMsg.getDoctorId() + " pat: " + nMsg.getPatientId() + " ts: " + nMsg.getTimeSlotId() + " succ: " + nMsg.isSuccessful());
				if(!queueUI.ackLastMessage()) {
					System.out.println("sending ack failed");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
