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

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author ladislav.gazo
 */
@RunWith(SpringJMSSuite.class)
@SuiteClasses({DummyFirstTest.class, DummySecondTest.class})
@ContextConfiguration(locations = {"/sk/seges/test/jms/test-context.xml"})
@SpringProviderConfiguration(managersListName = "jmsSuiteTestManagers")
public class MockedSpringProvidersSuiteTest {
}
