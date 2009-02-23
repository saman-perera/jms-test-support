/*
 * Copyright 2009 Seges Ltd. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
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

/**
 * @author ladislav.gazo
 */
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
