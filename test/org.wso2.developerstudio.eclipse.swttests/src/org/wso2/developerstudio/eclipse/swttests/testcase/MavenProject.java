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
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;

@RunWith(SWTBotJunit4ClassRunner.class)
public class MavenProject extends AbstractClass {

    private String projectName = "first";

    // create a new maven project
    @Test
    public void createANewMavenProject() throws Exception {

        openProjectFromMenu("Maven Multi Module Project");

        SWTBotShell newMavenProject = bot.shell("Maven Modules Creation Wizard");
        newMavenProject.bot().button("Finish").click();

        bot.tree().getTreeItem("MavenParentProject").expand();
        bot.tree().getTreeItem("MavenParentProject").select();
        bot.tree().getTreeItem("MavenParentProject").select();
        bot.tree().getTreeItem("MavenParentProject").getNode("pom.xml").select();
        bot.tree().getTreeItem("MavenParentProject").getNode("pom.xml").select();

        // Double clicking pom.xml is not working
        // bot.editorByTitle("MavenParentProject/pom.xml").show();
        bot.viewByTitle("Project Explorer").show();
        bot.tree().getTreeItem("MavenParentProject").select();
        bot.tree().getTreeItem("MavenParentProject").contextMenu("New").menu("Gadget").click();

        this.createGadgetProject(projectName);

        SWTBotEditor gadgetEditor = bot.editorByTitle("gadget.xml");
        gadgetEditor.show();
        gadgetEditor.bot().tree().getTreeItem("Module").getNode("ModulePrefs").expand();

    }

}
