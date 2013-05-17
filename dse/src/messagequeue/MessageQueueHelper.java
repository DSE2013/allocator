package messagequeue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import message.Message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class MessageQueueHelper implements IMessageQueueHelper {
	
	private final String queueName;
	private final Channel channel;
	private QueueingConsumer consumer;
	
	public MessageQueueHelper(String host, String queueName) throws IOException {
		this.queueName = queueName;
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		Connection connection = factory.newConnection();
		channel = connection.createChannel();
		channel.queueDeclare(queueName, true, false, false, null);
	}
	
	public void publish(Message message) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(message);
		channel.basicPublish("", queueName, null, baos.toByteArray());
	}
	
	public Message consume() throws ShutdownSignalException, ConsumerCancelledException, InterruptedException, IOException {
		if(consumer == null)
			consumer = new QueueingConsumer(channel);
		QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		ByteArrayInputStream bis = new ByteArrayInputStream(delivery.getBody());
		ObjectInputStream ois = new ObjectInputStream(bis);
		try {
			return (Message)ois.readObject();
		} catch(ClassNotFoundException cnfe) {
			return null;
		}
	}
}
