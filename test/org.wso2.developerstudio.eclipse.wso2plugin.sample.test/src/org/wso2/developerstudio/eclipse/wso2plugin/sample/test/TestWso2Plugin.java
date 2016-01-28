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
import org.wso2.developerstudio.eclipse.test.automation.framework.executor.Executor;
import org.wso2.developerstudio.eclipse.test.automation.framework.runner.*;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.PluginConstants;
import org.wso2.developerstudio.eclipse.test.automation.utils.functional.FunctionalUtil;
import org.wso2.developerstudio.eclipse.test.automation.utils.kernel.WSO2PluginUtils;

@RunWith(OrderedRunner.class)
public class TestWso2Plugin extends Executor {

    private String projectName = "FirstPlugin1";

    @Test
    @Order(order = 1)
    public void open() {
        FunctionalUtil.openProjectCreationWizardFromMenu(PluginConstants.MENU_NAME);
    }

    @Test
    @Order(order = 2)
    public void createPluginPorject() {
        String projectType = PluginConstants.SINGLE_MODULE_PROJECT;
        WSO2PluginUtils.createWso2Plugin(projectName, projectType);
    }

    @Test
    @Order(order = 3)
    public void deleteProject() {
        FunctionalUtil.deleteProjectWithContent(projectName);
    }

}
