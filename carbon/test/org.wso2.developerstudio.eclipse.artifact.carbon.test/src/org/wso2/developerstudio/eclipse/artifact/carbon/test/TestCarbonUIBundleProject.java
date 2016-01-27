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

package org.wso2.developerstudio.eclipse.artifact.carbon.test;

import org.wso2.developerstudio.eclipse.swtfunctionalframework.carbon.util.CarbonUtils;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.CommonUtil;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;

/* Testing Carbon UI Bundle Project
 * Create Carbon UI Bundle Project using new menu
 * Expand the tree
 * Create a new class using right click
 * Click on runtime and extension on the editor
 */

@RunWith(OrderedRunner.class)
public class TestCarbonUIBundleProject extends Setup {

	private String projectName = "firstUi";

	// Create a new Carbon UI Bundle Project
	@Test
	@Order(order = 1)
	public void createCarbonUIBundleProject() throws Exception {

		CommonUtil.openProjectCreationWizardFromMenu("Carbon UI Bundle Project");
		CarbonUtils.createCarbonUI(projectName);
		CommonUtil.activateCtab("Runtime");
		CommonUtil.activateCtab("Extensions");

	}

	@Test
	@Order(order = 2)
	public void createNewClass() throws Exception {
		String className = "HelloWorld";

		CommonUtil.openProjectCreationWizardFromRightClick(projectName, "Class");
		CommonUtil.createJavaClass(className);

	}

	@Test
	@Order(order = 3)
	public void changePerspective() throws Exception {
		CommonUtil.openPerspective("Java EE");
	}

	@Test
	@Order(order = 4)
	public void deleteProject() throws Exception {
		CommonUtil.deleteWithContent(projectName);
	}
}
