package allocator;

import java.io.IOException;

import message.AllocationMessage;
import message.NotificationMessage;
import messagequeue.IMessageQueueHelper;
import messagequeue.MessageQueueHelper;
import model.TimeSlot;
import util.Config;

import com.mongodb.DB;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

import controller.SlotAllocator;

public class Worker implements Runnable {
	private IMessageQueueHelper queueUI, queueMessenger;
	SlotAllocator slotAllocator;

	public Worker(DB db) throws IOException {
		
		queueMessenger = new MessageQueueHelper(Config.MQ_HOST, Config.MQ_PORT, Config.MQ_USER, Config.MQ_PASS, Config.MQ_NAME_ALLOCATOR_MESSENGER); 
		queueUI = new MessageQueueHelper(Config.MQ_HOST, Config.MQ_PORT, Config.MQ_USER, Config.MQ_PASS, Config.MQ_NAME_UI_ALLOCATOR);
		 
		slotAllocator = new SlotAllocator(db);
		AllocationMessage msg = new AllocationMessage();
		msg.setDoctorId(1);
		msg.setPatientId(1);
		msg.setOperationTypeId(1);
		msg.setMaxDistance(150);
		msg.setLengthInMin(60);
		queueUI.publish(msg);

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

			TimeSlot nearest = slotAllocator.getNearestSlot(msg.getPatientId(),
					msg.getOperationTypeId(), msg.getMaxDistance(), msg.getLengthInMin());

			NotificationMessage nMsg = new NotificationMessage();
			nMsg.setDoctorId(msg.getDoctorId());
			nMsg.setPatientId(msg.getPatientId());
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
