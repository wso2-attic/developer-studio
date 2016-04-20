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

package org.wso2.developerstudio.eclipse.esb.test;

import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.esb.util.ESBUtils;
import org.junit.Test;
import org.wso2.developerstudio.eclipse.test.automation.framework.executor.Executor;
import org.wso2.developerstudio.eclipse.test.automation.framework.runner.*;
import org.wso2.developerstudio.eclipse.test.automation.utils.functional.FunctionalUtil;

/* Testing ESB Configuration Project
 * Create ESB Config Project using new menu
 * Create ESB Config Project using Dash board
 * Create a sequence using right click menu in first project and assigning second project
 * Create a Proxy service
 * Create Endpoint using Dash board and a new project by it and assign that project
 */

@RunWith(OrderedRunner.class)
public class TestESBConfigProject extends Executor {

	private String projectNameMenu = "CreatedFromMenu";
	private String projectNameDash = "CreatedFromDash";
	private String projectNameSecondary = "CreatedFromSecondary";
	private String proxyService = "Hello";
	private String sequenceName = "firstSequence";
	private String endpointName = "lastEndpoint";

	@Test
	@Order(order = 1)
	public void createANewESBProjectFromMenu() throws Exception {

		FunctionalUtil.openProjectCreationWizardFromMenu("ESB Config Project");
		ESBUtils.createESBProject(projectNameMenu);
	}

	@Test
	@Order(order = 2)
	public void createANewESBProjectFromDash() throws Exception {

		FunctionalUtil
				.openProjectCreationWizardFromDashboard("ESB Config Project");
		ESBUtils.createESBProject(projectNameDash);
	}

	@Test
	@Order(order = 3)
	public void createNewSequence() throws Exception {

		String[] path = { "src", "main", "synapse-config", "sequences" };
		FunctionalUtil.openProjectCreationWizardFromRightClick(projectNameMenu,
				"Sequence");
		ESBUtils.createNewSequence(sequenceName, projectNameDash);
		FunctionalUtil.getExpandProjectTree(projectNameDash, path);
	}

	@Test
	@Order(order = 4)
	public void createProxy() throws Exception {

		FunctionalUtil.openProjectCreationWizardFromDashboard("Proxy Service");
		ESBUtils.createProxyService(proxyService);
		ESBUtils.validateProxyServicexml(proxyService);
	}

	@Test
	@Order(order = 5)
	public void createEndpoint() throws Exception {

		String[] path = { "src", "main", "synapse-config", "endpoints" };
		FunctionalUtil.openProjectCreationWizardFromRightClick(projectNameMenu,
				"Endpoint");
		ESBUtils.createNewEndpointWithESB(endpointName, projectNameSecondary);
		FunctionalUtil.getExpandProjectTree(projectNameSecondary, path);

	}

	@Test
	@Order(order = 6)
	public void deleteProjects() throws Exception {
		FunctionalUtil.deleteProjectWithContent(projectNameMenu);
		FunctionalUtil.deleteProjectWithContent(projectNameDash);
		FunctionalUtil.deleteProjectWithContent(projectNameSecondary);
	}

}