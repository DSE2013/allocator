package allocator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import message.AllocationMessage;
import message.Message;
import message.NotificationMessage;
import messagequeue.IMessageQueueHelper;
import model.TimeSlot;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mongodb.DB;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

import controller.SlotAllocator;

public class Worker implements Runnable {
	private IMessageQueueHelper queueUI, queueMessenger;
	SlotAllocator slotAllocator;

	public Worker(String queueHost, DB db) throws IOException {
		/*
		 * queueMessenger = new MessageQueueHelper(queueHost,
		 * Config.ALLOCATOR_MESSENGER); queueUI = new MessageQueueHelper(queueHost,
		 * Config.UI_ALLOCATOR);
		 */
		slotAllocator = new SlotAllocator(db);

		queueMessenger = mock(IMessageQueueHelper.class);
		queueUI = mock(IMessageQueueHelper.class);

		try {
			when(queueUI.consume()).thenAnswer(new Answer<Message>() {
				@Override
				public Message answer(InvocationOnMock invocation)
						throws Throwable {
					Thread.sleep(5000);
					AllocationMessage msg = new AllocationMessage();
					msg.setDoctorId(1);
					msg.setPatientId(1);
					msg.setOperationTypeId(1);
					msg.setMaxDistance(100);

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
					msg.getOperationTypeId(), msg.getMaxDistance());

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
			} catch (IOException e) {
				e.printStackTrace();
			}
			continue;
		}
	}

}
