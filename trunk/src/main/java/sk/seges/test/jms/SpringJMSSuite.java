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

import java.util.List;

import javax.naming.NamingException;

import org.junit.internal.runners.InitializationError;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jndi.JndiTemplate;
import org.springframework.test.context.ContextConfiguration;

import sk.seges.test.jms.manager.JMSProviderManager;

/**
 * Extended JMS suite with support to configure all aspects using Spring
 * framework.
 * 
 * Specific provider behaviour configuration is done using
 * {@link SpringProviderConfiguration}. Default bean name for list of JMS
 * provider managers is defined in {@link SpringJMSSuite.JMS_PROVIDER_MANAGERS} constant (currently it
 * is "jmsProviderManagers").
 * 
 * @author ladislav.gazo
 */
public class SpringJMSSuite extends JMSSuite {
	/** Default bean name for JMS provider managers. */
	public static final String JMS_PROVIDER_MANAGERS = "jmsProviderManagers";
	private ApplicationContext context;

	public SpringJMSSuite(Class<?> klass, Class<?>[] annotatedClasses)
			throws InitializationError {
		super(klass, annotatedClasses);
	}

	public SpringJMSSuite(Class<?> klass) throws InitializationError {
		super(klass);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setManagers(Class<?> klass) {
		ContextConfiguration annotation = klass
				.getAnnotation(ContextConfiguration.class);
		if (annotation != null) {
			context = new ClassPathXmlApplicationContext(annotation.locations());
		} else {
			context = new ClassPathXmlApplicationContext("context.xml");
		}

		SpringProviderConfiguration providerConfiguration = klass
				.getAnnotation(SpringProviderConfiguration.class);
		if (providerConfiguration != null) {
			managers = (List<JMSProviderManager>) context
					.getBean(providerConfiguration.managersListName());
		} else {
			managers = (List<JMSProviderManager>) context
					.getBean(JMS_PROVIDER_MANAGERS);
		}
	}

	@Override
	protected void setJNDIContext() {
		JndiTemplate template = (JndiTemplate) context.getBean("jndiCtx");
		try {
			jndiContext = template.getContext();
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void rebindJNDI(JMSProviderManager manager) {
		String tmp;
		try {
			tmp = manager.getId() + "TestConnectionFactory";
			jndiContext.rebind(tmp, context.getBean(tmp));
			tmp = manager.getId() + "TestQueueA";
			jndiContext.rebind(tmp, context.getBean(tmp));			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
