package sk.seges.test.jms;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(SpringJMSSuite.class)
@SuiteClasses({DummyFirstTest.class, DummySecondTest.class})
@SpringProviderConfiguration(managersListName = "jmsSuiteTestManagers")
public class MockedSpringProvidersSuiteTest {
}
