package org.wso2.developerstudio.eclipse.greg.registry.test;

import java.io.IOException;

import javax.xml.xpath.XPathExpressionException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.registry.util.RegistryUtil;
import org.wso2.developerstudio.eclipse.test.automation.framework.executor.Executor;
import org.wso2.developerstudio.eclipse.test.automation.framework.runner.Order;
import org.wso2.developerstudio.eclipse.test.automation.framework.runner.OrderedRunner;
import org.wso2.developerstudio.eclipse.test.automation.utils.functional.FunctionalUtil;
import org.wso2.developerstudio.eclipse.test.automation.utils.server.AutomationFrameworkException;
import org.wso2.developerstudio.eclipse.test.automation.utils.server.TestServerManager;

@RunWith(OrderedRunner.class)
public class TestRegistry extends Executor {

	private RegistryUtil regUtil = new RegistryUtil();
	private static TestServerManager testServer = new TestServerManager(
			"wso2am-1.8.0.zip");

	@BeforeClass
	public static void serverStartup() {

		try {
			testServer.startServer();
		} catch (XPathExpressionException | AutomationFrameworkException
				| IOException e) {

		}
	}

	@Test
	@Order(order = 1)
	public void login() {
		FunctionalUtil.openPerspective("WSO2 Registry");
		regUtil.login("admin", "admin", null, null);
		RegistryUtil.expandTree("admin", "https://localhost:9443/");
	}

	@AfterClass
	public static void severShutdown() {
		try {
			testServer.stopServer();
		} catch (AutomationFrameworkException e) {
		}
	}
}
