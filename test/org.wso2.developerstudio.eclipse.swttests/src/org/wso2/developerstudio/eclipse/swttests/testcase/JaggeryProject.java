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
import org.junit.runner.RunWith;
import org.junit.Test;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;

@RunWith(SWTBotJunit4ClassRunner.class)
public class JaggeryProject extends AbstractClass {

    @Test
    public void createANewJaggeryProject() throws Exception {
        bot.toolbarDropDownButtonWithTooltip("New").click();

        SWTBotShell newProject = bot.shell("New");
        newProject.bot().tree().getTreeItem("WSO2").expand();
        newProject.bot().tree().getTreeItem("WSO2").expand();
        newProject.bot().tree().getTreeItem("WSO2").getNode("Jaggery").expand();
        newProject.bot().tree().getTreeItem("WSO2").getNode("Jaggery").getNode("Jaggery Project").select();

        bot.button("Next >").click();
        bot.sleep(1000);
        bot.textWithLabel("&Project name:").setText("helloJaggery");
        bot.sleep(1000);
        bot.button("Next >").click();
        bot.sleep(1000);
        bot.tabItem("&Projects").activate();
        bot.sleep(1000);
        bot.tabItem("&Libraries").activate();
        bot.sleep(1000);
        bot.tabItem("&Order").activate();
        bot.sleep(1000);
        bot.tabItem("&Source").activate();
        bot.sleep(1000);
        bot.tree().getTreeItem("helloJaggery").select();
        bot.sleep(1000);
        bot.button("Next >").click();
        bot.sleep(1000);
        bot.tree().getTreeItem("helloJaggery").expand();
        bot.sleep(1000);
        bot.button("Finish").click();
        bot.sleep(1000);
        bot.tree().getTreeItem("helloJaggery").expand();
        bot.sleep(1000);
        bot.tree().getTreeItem("helloJaggery").contextMenu("Copy").click();
        bot.sleep(1000);
        bot.tree().getTreeItem("helloJaggery").contextMenu("New").menu("Jaggery File").click();
        bot.sleep(1000);
        bot.button("Finish").click();
        bot.sleep(1000);
        bot.editorByTitle("newjagfile.jag").show();
        bot.sleep(1000);
        bot.viewByTitle("Project Explorer").show();
        bot.sleep(1000);

        // doubleClick added manually only track .select()
        bot.tree().getTreeItem("helloJaggery").getNode("index.jag").doubleClick();
        bot.editorByTitle("index.jag").show();
        bot.viewByTitle("Project Explorer").show();
        bot.editorByTitle("newjagfile.jag").show();
        bot.editorByTitle("index.jag").show();

    }

}
