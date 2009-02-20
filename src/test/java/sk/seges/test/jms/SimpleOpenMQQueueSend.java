package sk.seges.test.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Before;
import org.junit.Test;

public class SimpleOpenMQQueueSend {
	private ConnectionFactory factory;
	private Queue queue;
	
	@Before
	public void setUp() throws JMSException {
		factory = new com.sun.messaging.ConnectionFactory();
		com.sun.messaging.ConnectionFactory sunFactory = (com.sun.messaging.ConnectionFactory) factory;
		sunFactory.setProperty("imqAddressList", "mq://localhost:7676");
		
		queue = new com.sun.messaging.Queue("testQueue");
	}
	
	@Test
	public void testSend() throws Exception {
		Connection connection = factory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(queue);
		TextMessage message = session.createTextMessage("test text");
		producer.send(message);
		connection.close();
	}
}
