package org.wso2.developerstudio.eclipse.artifact.analytics.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.artifact.analytics.util.AnalyticsUtils;
import org.wso2.developerstudio.eclipse.test.automation.framework.executor.Executor;
import org.wso2.developerstudio.eclipse.test.automation.framework.runner.Order;
import org.wso2.developerstudio.eclipse.test.automation.framework.runner.OrderedRunner;
import org.wso2.developerstudio.eclipse.test.automation.utils.functional.FunctionalUtil;

@RunWith(OrderedRunner.class)
public class TestAnalytics extends Executor {

	String[] path = { "src", "main", "execution-plan" };

	@Test
	@Order(order = 1)
	public void create() {
		FunctionalUtil.openProjectCreationWizardFromMenu("Analytics Project");
		AnalyticsUtils.createNewAnalyticsProject("mainProject");
		FunctionalUtil.openProjectCreationWizardFromRightClick("mainProject",
				path, "Execution-plan");
		AnalyticsUtils.createAtifact("Execution-plan", "artifact");
	}
}
