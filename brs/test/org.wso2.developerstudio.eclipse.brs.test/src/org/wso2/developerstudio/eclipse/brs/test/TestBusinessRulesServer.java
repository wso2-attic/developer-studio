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

import org.wso2.developerstudio.eclipse.swtfunctionalframework.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.CommonUtil;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.brs.util.BusinessRulesUtil;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonConstants;

@RunWith(OrderedRunner.class)
public class TestBusinessRulesServer extends Setup {

	private String projectName = "businessRule";
	private String serviceName = "myFirstService";
	private String className = "HelloWorld";
	private String[] path = { "src/main/java" };
	private String servicepath = "/home/kushan/myFirst.rsl";

	@Test
	@Order(order = 1)
	public void createANewBusinesRulesServer() throws Exception {

		CommonUtil.openProjectCreationWizardFromMenu("Business Rules Service Project");
		BusinessRulesUtil.createBusinessRulesService(projectName, serviceName);
	}

	@Test
	@Order(order = 2)
	public void accessesCtabs() throws Exception {

		BusinessRulesUtil.serviceEditorText("Target Namespace", "home");
		BusinessRulesUtil.serviceEditorText("Service Name", serviceName);
		CommonUtil.activateCtab(CommonConstants.SOURCE);
		BusinessRulesUtil.closeServiceEditor();
	}

	@Test
	@Order(order = 3)
	public void createNewClass() throws Exception {

		CommonUtil.openProjectCreationWizardFromRightClick(projectName, path, "Class");
		CommonUtil.createJavaClass(className);

	}

	@Test
	@Order(order = 4)
	public void importARule() {

		CommonUtil.openProjectCreationWizardFromMenu("Business Rules Service Project");
		BusinessRulesUtil.importBusinessRulesService(servicepath);

	}

	@Test
	@Order(order = 5)
	public void deleteProject() {
		CommonUtil.deleteWithContent(projectName);
	}

	@Test
	@Order(order = 5)
	public void deleteNextProject() {
		CommonUtil.deleteWithContent("myFirst");
	}

}
