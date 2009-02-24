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
package sk.seges.test.jms.manager;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.BrokerServiceAware;
import org.apache.activemq.xbean.XBeanBrokerService;
import org.springframework.context.ApplicationContext;

/**
 * Manager for an embedded ActiveMQ JMS provider.
 * 
 * @author ladislav.gazo
 */
public class EmbeddedActiveMQJMSProviderManager implements JMSProviderManager, BrokerServiceAware {
	private BrokerService broker;
	private String brokerServiceFactoryName;
	
	public void setBrokerService(BrokerService broker) {
		this.broker = broker;
	}
	
	public void setBrokerServiceFactoryName(String brokerServiceFactoryName) {
		this.brokerServiceFactoryName = brokerServiceFactoryName;
	}

	public void start() {
    	try {
			broker.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

    public void stop() {
    	try {
			broker.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

    public void restart() {
    	try {
    		if(broker instanceof XBeanBrokerService) {
    			ApplicationContext context = ((XBeanBrokerService) broker).getApplicationContext();
    			broker.stop();
    			if(brokerServiceFactoryName != null) {
    				broker = (BrokerService) context.getBean(brokerServiceFactoryName);
    			}
    			broker.start();
    		}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}
