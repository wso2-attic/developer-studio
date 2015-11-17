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
import org.junit.Order;
import org.junit.OrderedRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;

/* Testing data Service Project
 * Create Data Service project using new menu
 * Create Synapse Configuration using right click
 * Expand the tree
 */

@RunWith(OrderedRunner.class)
public class DataServiceProject extends Setup {

    // Creating Data Service Project
    @Test
    @Order(order = 1)
    public void canCreateANewJavaProject() throws Exception {

        String projectName = "first";
        String[] path = {"dataservice"};

        AbstractClass.openProjectFromMenu("Data Service Project");
        AbstractClass.createDataService(projectName);

        
        AbstractClass.openProjectFromRightClick(projectName, path, "Project...");
/*        bot.tree().getTreeItem(projectName).expand();
        bot.tree().getTreeItem(projectName).getNode("dataservice").select();
        bot.tree().getTreeItem(projectName).getNode("dataservice").contextMenu("New").menu("Project...").click();s
        bot.sleep(1000);*/

        SWTBotShell nextNewProject = bot.shell("New Project");
        nextNewProject.bot().button("Cancel").click();

        
        AbstractClass.openProjectFromRightClick(projectName, path, "Example...");
/*        bot.tree().getTreeItem(projectName).select();
        bot.tree().getTreeItem(projectName).getNode("dataservice").select();
        bot.tree().getTreeItem(projectName).getNode("dataservice").contextMenu("New").menu("Example...").click();*/

        SWTBotShell newExample = bot.shell("New Example");
        newExample.bot().tree().getTreeItem("Synapse Configuration").select();
        newExample.bot().button("Next >").click();
        bot.sleep(1000);

        SWTBotShell newDiag = bot.shell("New ESB Diagram");
        newDiag.bot().tree().getTreeItem(projectName).expand();
        newDiag.bot().tree().getTreeItem(projectName).getNode("dataservice").select();
        newDiag.bot().button("< Back").click();

        newExample = bot.shell("New Example");
        newExample.bot().tree().getTreeItem("XML").getNode("Editing and validating XML files").select();
        bot.sleep(1000);
        newExample.bot().button("Next >").click();
        bot.sleep(1000);

        SWTBotShell newXML = bot.shell("Editing and validating XML files");
        newXML.bot().button("Finish").click();
        bot.sleep(1000);

        bot.editorByTitle("readme.html").show();
        bot.viewByTitle("Project Explorer").show();
        bot.tree().getTreeItem("XMLExamples").getNode("PublicationCatalogue").select();
        bot.tree().getTreeItem("XMLExamples").getNode("SpaceWarGame").select();
        bot.tree().getTreeItem("XMLExamples").getNode("PublicationCatalogue").expand();
        bot.tree().getTreeItem("XMLExamples").getNode("SpaceWarGame").expand();
        bot.tree().getTreeItem("XMLExamples").getNode("PhoneBanking").expand();
        bot.tree().getTreeItem("XMLExamples").getNode("Invoice").expand();
        bot.tree().getTreeItem("XMLExamples").getNode("GolfCountryClub").expand();

    }
}
