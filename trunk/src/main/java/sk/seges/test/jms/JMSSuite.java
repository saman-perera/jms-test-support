package sk.seges.test.jms;

import java.util.List;

import org.junit.internal.runners.InitializationError;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;

import sk.seges.test.jms.manager.JMSProviderManager;

public abstract class JMSSuite extends Suite {
	protected List<JMSProviderManager> managers;
	
	protected boolean restartAfterEachTest = false;
	
	public JMSSuite(Class<?> klass, Class<?>[] annotatedClasses)
			throws InitializationError {
		super(klass, annotatedClasses);
		configure(klass);
		setManagers(klass);
	}

	public JMSSuite(Class<?> klass) throws InitializationError {
		super(klass);
		configure(klass);
		setManagers(klass);
	}

	private void configure(Class<?> klass) {
		ProviderConfiguration configuration = klass.getAnnotation(ProviderConfiguration.class);
		
		if(configuration != null) {
			restartAfterEachTest = configuration.restartAfterEachTest();
		}
	}
	
	protected void runChildren(RunNotifier notifier) {
		for(JMSProviderManager manager : managers) {
			manager.start();
			for (Runner each : getRunners()) {
				each.run(notifier);
				if(restartAfterEachTest) {
					manager.restart();
				}
			}
			manager.stop();
		}
	}
	
	protected abstract void setManagers(Class<?> klass);
}
