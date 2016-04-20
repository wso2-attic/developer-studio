/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.developerstudio.eclipse.brs.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.brs.util.BusinessRulesUtil;
import org.wso2.developerstudio.eclipse.test.automation.framework.executor.Executor;
import org.wso2.developerstudio.eclipse.test.automation.framework.runner.*;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.CommonConstants;
import org.wso2.developerstudio.eclipse.test.automation.utils.functional.FunctionalUtil;

@RunWith(OrderedRunner.class)
public class TestBusinessRulesServer extends Executor {

	private String projectName = "businessRule";
	private String serviceName = "myFirstService";
	private String className = "HelloWorld";
	private String[] path = { "src/main/java" };
	private String servicepath = "/home/kushan/myFirst.rsl";

	@Test
	@Order(order = 1)
	public void createANewBusinesRulesServer() throws Exception {

		FunctionalUtil.openProjectCreationWizardFromMenu("Business Rules Service Project");
		BusinessRulesUtil.createBusinessRulesService(projectName, serviceName);
	}

	@Test
	@Order(order = 2)
	public void accessesCtabs() throws Exception {

		BusinessRulesUtil.serviceEditorText("Target Namespace", "home");
		BusinessRulesUtil.serviceEditorText("Service Name", serviceName);
		FunctionalUtil.activateCtab(CommonConstants.SOURCE);
		BusinessRulesUtil.closeServiceEditor();
	}

	@Test
	@Order(order = 3)
	public void createNewClass() throws Exception {

		FunctionalUtil.openProjectCreationWizardFromRightClick(projectName, path, "Class");
		FunctionalUtil.createJavaClass(className);

	}

	@Test
	@Order(order = 4)
	public void importARule() {

		FunctionalUtil.openProjectCreationWizardFromMenu("Business Rules Service Project");
		BusinessRulesUtil.importBusinessRulesService(servicepath);

	}

	@Test
	@Order(order = 5)
	public void deleteProject() {
		FunctionalUtil.deleteProjectWithContent(projectName);
	}

	@Test
	@Order(order = 5)
	public void deleteNextProject() {
		FunctionalUtil.deleteProjectWithContent("myFirst");
	}

}
