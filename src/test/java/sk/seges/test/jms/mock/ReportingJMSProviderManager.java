package sk.seges.test.jms.mock;

import sk.seges.test.jms.manager.JMSProviderManager;

public class ReportingJMSProviderManager implements JMSProviderManager {
	public void restart() {
		System.out.println("restart");
	}

	public void start() {
		System.out.println("start");
	}

	public void stop() {
		System.out.println("stop");
	}
}
