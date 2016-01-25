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
import org.wso2.developerstudio.eclipse.swtfunctionalframework.junit.*;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.CommonUtil;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.CompositeApplicationUtil;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonConstants;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CompositeApllicationConstants;

@RunWith(OrderedRunner.class)
public class TestCompositeApplication extends Setup {

	private String projectName = "firstApp2";

	@Test
	@Order(order = 1)
	public void open() throws Exception {
		CommonUtil.openProjectFromMenu(CompositeApllicationConstants.MENU_NAME);

	}

	@Test
	@Order(order = 2)
	public void createANewCompositeApplication() throws Exception {

		CompositeApplicationUtil.createCompositeApllication(projectName);
		CommonUtil.activateCtab(CommonConstants.SOURCE);

	}

	@Test
	@Order(order = 3)
	public void deleteApplication() throws Exception {
		CommonUtil.deleteWithContent(projectName);
	}
}
