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
package sk.seges.test.jms.activemq;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ladislav.gazo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/sk/seges/test/jms/activemq/test-activemq-context.xml"})
public class SimpleActiveMQQueueSendReceiveTest {
	@Autowired
	private ConnectionFactory activeMQConnectionFactory;
	@Autowired
	private Queue activeMQTestQueueA;
	
	@Before
	public void setUp() {
	}
	
	@Test
	public void testSend() throws Exception {
		Connection connection = activeMQConnectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(activeMQTestQueueA);
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		TextMessage message = session.createTextMessage("test text");
		producer.send(message);
		connection.close();
	}
	
	@Test
	public void testReceive() throws Exception {
		Connection connection = activeMQConnectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer consumer = session.createConsumer(activeMQTestQueueA);
		Message rawMessage = consumer.receive();
		assertTrue(rawMessage instanceof TextMessage);
		
		TextMessage message = (TextMessage) rawMessage;
		assertEquals("test text", message.getText());
		connection.close();		
	}
}
