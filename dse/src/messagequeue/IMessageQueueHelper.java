package messagequeue;

import java.io.IOException;

import message.Message;

import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

public interface IMessageQueueHelper {
	public void publish(Message message) throws IOException;
	public Message consume() throws ShutdownSignalException, ConsumerCancelledException, InterruptedException, IOException;
	public boolean ackLastMessage();
}
