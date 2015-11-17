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

import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;

/* Testing ESB Graphical perspective
 * Open Perspective
 * Create a project
 */

@RunWith(SWTBotJunit4ClassRunner.class)
public class ESBGraphical extends AbstractClass {

    @Test
    public void canCreateANewJavaProject() throws Exception {

        this.openPerspective("WSO2 ESB Graphical");
        bot.tree().contextMenu("New").menu("Application Client Project").click();

        SWTBotShell newClientApp = bot.shell("New Application Client Project");
        newClientApp.bot().textWithLabel("Project na&me:").setText("appClient");
        newClientApp.bot().button("Finish").click();

        // bot..click("shapename");

        SWTBotShell changePerspective = bot.shell("Open Associated Perspective?");
        changePerspective.bot().button("No").click();

        bot.viewByTitle("Project Explorer").show();
        bot.tree().getTreeItem("appClient").expand();
        bot.tree().getTreeItem("appClient").expand();
        bot.viewByTitle("Properties").show();
    }

}
