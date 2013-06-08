package dse.core.messagequeue;

import java.io.IOException;

import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

import dse.core.message.Message;

public interface IMessageQueueHelper {
	public void publish(Message message) throws IOException;
	public Message consume() throws ShutdownSignalException, ConsumerCancelledException, InterruptedException, IOException;
	public boolean ackLastMessage();
}
