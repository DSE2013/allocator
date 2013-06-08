package dse.core.messagequeue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import dse.core.message.Message;
import dse.core.message.NotificationMessage;

public class MessageQueueHelper implements IMessageQueueHelper {
	
	private final String queueName;
	private final Channel channel;
	private QueueingConsumer consumer;
	private Long lastDeliveryTag;
	
	public MessageQueueHelper(String host, int port, String username, String password, String queueName) throws IOException {
		this.queueName = queueName;
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		factory.setUsername(username);
		factory.setPassword(password);
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
		if(consumer == null) {
			consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, false, consumer);
		}
		QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		lastDeliveryTag = delivery.getEnvelope().getDeliveryTag();
		ByteArrayInputStream bis = new ByteArrayInputStream(delivery.getBody());
		ObjectInputStream ois = new ObjectInputStream(bis);
		try {
			new NotificationMessage();
			return (Message)ois.readObject();
		} catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			return null;
		}
	}
	
	public boolean ackLastMessage() {
		if(lastDeliveryTag != null) {
			try {
				channel.basicAck(lastDeliveryTag, false);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
}
