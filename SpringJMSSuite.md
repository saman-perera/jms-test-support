# Introduction #

Let's assume we have two test cases: _DummyFirstTest_ and _DummySecondTest_.

Also assume that we have two JMS provider managers configured in Spring context file _test-context.xml_.
```
	<util:list id="jmsSuiteTestManagers">
		<ref local="reportingProviderManager"/>
		<ref local="reportingProviderManager"/>
	</util:list>
```

# Our test suite #

To be able to invoke each test case against defined set of JMS providers we need to define separate class - JMS test suite.
For JUnit 4 we use ` @RunWith ` annotation.

```
@RunWith(SpringJMSSuite.class)
```

Now we want to define which test cases have to be run within the suite with annotation ` @SuiteClasses ` which is standard for JUnit 4 suites. Example for our two test cases looks like this:
```
@SuiteClasses({DummyFirstTest.class, DummySecondTest.class})
```

Spring support uses Spring Test framework facilities and for the suite applies ` @ContextConfiguration ` annotation specifically. Default context file name is _context.xml_. When we need to define another location of Spring context file on class path we use the annotation in the standard manner:

```
@ContextConfiguration(locations = {"/test-context.xml"})
```

We need to instruct JMS test suite which provider manager it has to use. Because we are trying to do everything in Spring we have defined the list of managers with _jmsSuiteTestManagers_ ID.

The suite expects a bean with default ID _jmsProviderManagers_. To define something else we use ` @SpringProviderConfiguration ` annotation.

```
@SpringProviderConfiguration(managersListName = "jmsSuiteTestManagers")
```

And that's it! One suite with at most 4 annotations ;)