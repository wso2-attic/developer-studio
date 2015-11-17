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

package org.wso2.developerstudio.eclipse.swttests.testcase;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.junit.Order;
import org.junit.OrderedRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;

@RunWith(OrderedRunner.class)
public class CompositeApplication extends Setup {

    // Create a new Business Rules Server
    @Test
    @Order(order = 1)
    public void createANewBusinesRulesServer() throws Exception {

        String projectName = "first";

        AbstractClass.openProjectFromMenu("Composite Application Project");
        AbstractClass.createCompositeApllication(projectName);

        SWTBotEditor app = bot.editorByTitle(projectName + "/pom.xml");
        app.bot().label("Dependencies").click();
        bot.sleep(1000);
        app.bot().toolbarButtonWithTooltip("Refresh").click();

        // Dosent work because native swt message box
        // app.bot().toolbarButtonWithTooltip("Create Archive").click();

        // SWTBotShell notSelected = bot.shell("WSO2 Platform Distribution");
        // notSelected.close();
        // bot.button("OK").click();

        app.bot().label("Dependencies").click();
        bot.sleep(1000);
        app.bot().label("Dependencies").click();

        app.close();
        bot.tree().getTreeItem(projectName).contextMenu("Delete").click();

        AbstractClass.deleteWithoutContent(projectName);

    }
}
