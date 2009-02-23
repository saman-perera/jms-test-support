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

import org.junit.internal.runners.InitializationError;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;

import sk.seges.test.jms.manager.JMSProviderManager;

/**
 * A JMS suite responsible for wrapping a test case with JMS provider management
 * tasks. For each test case that is going to be executed within the suite a JMS
 * provider must be running. After all test cases in one run for one provider
 * are finished, provider has to be stopped. New run of test cases for different
 * provider will follow.
 * 
 * There is possibility to restart a provider after each test case was executed
 * (@see {@link ProviderConfiguration}).
 * 
 * @author ladislav.gazo
 */
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
		ProviderConfiguration configuration = klass
				.getAnnotation(ProviderConfiguration.class);

		if (configuration != null) {
			restartAfterEachTest = configuration.restartAfterEachTest();
		}
	}

	protected void runChildren(RunNotifier notifier) {
		for (JMSProviderManager manager : managers) {
			manager.start();
			for (Runner each : getRunners()) {
				each.run(notifier);
				if (restartAfterEachTest) {
					manager.restart();
				}
			}
			manager.stop();
		}
	}

	/**
	 * Configure JMS provider managers for the suite.
	 * 
	 * @param klass A class that is run with this suite (where the annotation RunWith is put).
	 */
	protected abstract void setManagers(Class<?> klass);
}