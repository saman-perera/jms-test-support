package sk.seges.test.jms;

import java.util.List;

import org.junit.internal.runners.InitializationError;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import sk.seges.test.jms.manager.JMSProviderManager;

public class SpringJMSSuite extends JMSSuite {
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
		ContextConfiguration annotation = klass.getAnnotation(ContextConfiguration.class);
		if(annotation != null) {
			context = new ClassPathXmlApplicationContext(annotation.locations());
		} else {
			context = new ClassPathXmlApplicationContext("context.xml");
		}
		
		SpringProviderConfiguration providerConfiguration = klass.getAnnotation(SpringProviderConfiguration.class);
		if(providerConfiguration != null) {
			managers = (List<JMSProviderManager>) context.getBean(providerConfiguration.managersListName());	
		} else {
			managers = (List<JMSProviderManager>) context.getBean("managers");
		}
	}

}
