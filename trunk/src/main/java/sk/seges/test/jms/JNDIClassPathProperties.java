package sk.seges.test.jms;

import java.util.Properties;

import javax.naming.Context;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

public class JNDIClassPathProperties implements FactoryBean {
	private Resource targetJNDIFile;
	
	public void setTargetJNDIFile(Resource targetJNDIFile) {
		this.targetJNDIFile = targetJNDIFile;
	}
	
	public Object getObject() throws Exception {
		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
		props.put(Context.PROVIDER_URL, targetJNDIFile.getFile().getAbsoluteFile());
		return props;
	}

	@SuppressWarnings("unchecked")
	public Class getObjectType() {
		return Properties.class;
	}

	public boolean isSingleton() {
		return true;
	}
}
