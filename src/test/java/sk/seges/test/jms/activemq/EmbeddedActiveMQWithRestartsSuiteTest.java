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

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.ContextConfiguration;

import sk.seges.test.jms.ProviderConfiguration;
import sk.seges.test.jms.SpringJMSSuite;
import sk.seges.test.jms.SpringProviderConfiguration;

/**
 * @author ladislav.gazo
 */
@RunWith(SpringJMSSuite.class)
@SuiteClasses({SimpleActiveMQQueueSendTest.class, SimpleActiveMQQueueReceiveFailTest.class})
@ProviderConfiguration(restartAfterEachTest = true)
@ContextConfiguration(locations = {"/sk/seges/test/jms/activemq/jts-context-activemq.xml"})
@SpringProviderConfiguration(managersListName = "onlyEmbeddedActiveMQManager")
public class EmbeddedActiveMQWithRestartsSuiteTest {
}
