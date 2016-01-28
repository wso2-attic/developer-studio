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

package org.wso2.developerstudio.eclipse.capp.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.test.automation.framework.executor.Executor;
import org.wso2.developerstudio.eclipse.test.automation.framework.runner.*;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.CommonConstants;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.CompositeApllicationConstants;
import org.wso2.developerstudio.eclipse.test.automation.utils.functional.FunctionalUtil;
import org.wso2.developerstudio.eclipse.test.automation.utils.kernel.CompositeApplicationUtil;

@RunWith(OrderedRunner.class)
public class TestCompositeApplication extends Executor {

	private String projectName = "firstApp2";

	@Test
	@Order(order = 1)
	public void open() throws Exception {
		FunctionalUtil.openProjectCreationWizardFromMenu(CompositeApllicationConstants.MENU_NAME);

	}

	@Test
	@Order(order = 2)
	public void createANewCompositeApplication() throws Exception {

		CompositeApplicationUtil.createCompositeApllication(projectName);
		FunctionalUtil.activateCtab(CommonConstants.SOURCE);

	}

	@Test
	@Order(order = 3)
	public void deleteApplication() throws Exception {
		FunctionalUtil.deleteProjectWithContent(projectName);
	}
}
