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

import org.apache.log4j.Logger;
import org.junit.internal.runners.InitializationError;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
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
	private static final Logger log = Logger.getLogger(JMSSuite.class);
	protected List<JMSProviderManager> managers;

	protected boolean restartAfterEachTest = false;
	
	private JMSProviderManager currentRunning = null;

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
		notifier.addListener(new RunListener() {
			@Override
			public void testFinished(Description description) throws Exception {
				if(log.isDebugEnabled()) {
					log.debug("FINISHING Test " + description.getDisplayName());
				}
				super.testFinished(description);
				if(log.isDebugEnabled()) {
					log.debug("FINISHED Test " + description.getDisplayName());
				}

				if (restartAfterEachTest) {
					currentRunning.restart();
					if(log.isInfoEnabled()) {
						log.info("RESTARTED Manager " + currentRunning);
					}
				}
			}
			
			@Override
			public void testStarted(Description description) throws Exception {
				if(log.isDebugEnabled()) {
					log.debug("STARTING Test " + description.getDisplayName());
				}
				super.testStarted(description);
				if(log.isDebugEnabled()) {
					log.debug("STARTED Test " + description.getDisplayName());
				}
			}
			
			@Override
			public void testRunFinished(Result result) throws Exception {
				super.testRunFinished(result);
				if(log.isDebugEnabled()) {
					log.debug("FINISHED Test run, count = " + result.getRunCount());
				}
			}
			
			@Override
			public void testRunStarted(Description description)
					throws Exception {
				super.testRunStarted(description);
				if(log.isDebugEnabled()) {
					log.debug("STARTED Test run " + description.getDisplayName());
				}
			}
		});
		
		for (JMSProviderManager manager : managers) {
			manager.start();
			currentRunning = manager;
			if(log.isInfoEnabled()) {
				log.info("STARTED Manager " + currentRunning);
			}
			for (Runner each : getRunners()) {
				each.run(notifier);
			}
			manager.stop();
			if(log.isInfoEnabled()) {
				log.info("STOPPED Manager " + currentRunning);
			}			
		}
	}

	/**
	 * Configure JMS provider managers for the suite.
	 * 
	 * @param klass A class that is run with this suite (where the annotation RunWith is put).
	 */
	protected abstract void setManagers(Class<?> klass);
}
