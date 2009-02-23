package sk.seges.test.jms;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.ContextConfiguration;

@RunWith(SpringJMSSuite.class)
@SuiteClasses({DummyFirstTest.class, DummySecondTest.class})
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@SpringProviderConfiguration(managersListName = "jmsSuiteTestManagers")
public class MockedSpringProvidersSuiteTest {
}
