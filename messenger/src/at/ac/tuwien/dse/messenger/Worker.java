package at.ac.tuwien.dse.messenger;

import java.io.IOException;

import at.ac.tuwien.dse.core.message.AllocationMessage;
import at.ac.tuwien.dse.core.message.NotificationMessage;
import at.ac.tuwien.dse.core.messagequeue.MessageQueueHelper;
import at.ac.tuwien.dse.core.util.Config;
import at.ac.tuwien.dse.messenger.controller.NotificationManager;

import com.mongodb.DB;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

public class Worker implements Runnable {
	
	private MessageQueueHelper queueAllocator;
	private NotificationManager notMan;

	public Worker(DB db) throws IOException {
		queueAllocator = new MessageQueueHelper(Config.MQ_HOST, Config.MQ_PORT, Config.MQ_USER, Config.MQ_PASS, Config.MQ_NAME_ALLOCATOR_MESSENGER);
		notMan = new NotificationManager(db);
	}

	public void run() {
		while (true) {
			NotificationMessage msg;
			try {
				msg = (NotificationMessage) queueAllocator.consume();
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
			
			if(notMan.notifyUsers(msg))
				queueAllocator.ackLastMessage();
		}
	}

}
