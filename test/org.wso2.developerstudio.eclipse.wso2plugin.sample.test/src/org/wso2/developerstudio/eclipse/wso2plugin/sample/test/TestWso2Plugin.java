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

package org.wso2.developerstudio.eclipse.wso2plugin.sample.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.junit.*;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Wso2PluginUtils;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.Wso2PluginCons;

@RunWith(OrderedRunner.class)
public class TestWso2Plugin extends Setup {

	
	private String projectName = "FirstPlugin1";

	@Test
	@Order(order = 1)
	public void open() {
		AbstractClass.openProjectFromMenu(Wso2PluginCons.MENU_NAME);
	}

	@Test
	@Order(order = 2)
	public void createPluginPorject() {
		String projectType = Wso2PluginCons.SINGLE_MODULE_PROJECT;
		Wso2PluginUtils.createWso2Plugin(projectName, projectType);
	}

	@Test
	@Order(order = 3)
	public void deleteProject() {
		AbstractClass.deleteWithContent(projectName);
	}

}
