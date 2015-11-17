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
import org.junit.runner.RunWith;
import org.junit.Order;
import org.junit.OrderedRunner;
import org.junit.Test;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;

/* Testing ESB Configuration Project
 * Create ESB Config Project using new menu
 * Create ESB Config Project using Dash board
 * Create a sequence using right click menu in first project and assigning second project
 * Create a Proxy service
 * Create Endpoint using Dash board and a new project by it and assign that project
 */

@RunWith(OrderedRunner.class)
public class TestESBConfigProject extends Setup {

    private String projectNameMenu = "CreatedFromMenu";
    private String projectNameDash = "CreatedFromDash";
    private String projectNameSecondary = "CreatedFromSecondary";
    private String proxyService = "Hello";
    private String sequenceName = "firstSequence";
    private String endpointName = "lastEndpoint";

    @Test
    @Order(order = 1)
    public void createANewESBProjectFromMenu() throws Exception {

        AbstractClass.openProjectFromMenu("ESB Config Project");
        AbstractClass.createESBProject(projectNameMenu);
        AbstractClass.openFromDash("ESB Config Project");
        AbstractClass.createESBProject(projectNameDash);
    }

    @Test
    @Order(order = 2)
    public void createNewSequence() throws Exception {
        // Creating new sequence for the ESB project
        bot.tree().getTreeItem(projectNameMenu).select();
        bot.tree().getTreeItem(projectNameMenu).contextMenu("New").menu("Sequence").click();

        AbstractClass.createNewSequence(sequenceName, projectNameDash);

        bot.tree().getTreeItem(projectNameDash).expand();
        bot.tree().getTreeItem(projectNameDash).getNode("src").expand();
        bot.tree().getTreeItem(projectNameDash).getNode("src").getNode("main").expand();
        bot.tree().getTreeItem(projectNameDash).getNode("src").getNode("main").getNode("synapse-config").expand();
        bot.tree().getTreeItem(projectNameDash).getNode("src").getNode("main").getNode("synapse-config")
                .getNode("sequences").expand();
    }

    @Test
    @Order(order = 3)
    public void createProxy() throws Exception {
        AbstractClass.openFromDash("Proxy Service");
        AbstractClass.createProxyService(proxyService);

        AbstractClass.validateProxyServicexml(proxyService);
    }

    @Test
    @Order(order = 4)
    public void createEndpoint() throws Exception {
        bot.tree().getTreeItem(projectNameMenu).select();
        bot.tree().getTreeItem(projectNameMenu).contextMenu("New").menu("Endpoint").click();

        AbstractClass.createNewEndpointWithESB(endpointName, projectNameSecondary);

        bot.tree().getTreeItem(projectNameSecondary).expand();
        bot.tree().getTreeItem(projectNameSecondary).getNode("src").expand();
        bot.tree().getTreeItem(projectNameSecondary).getNode("src").getNode("main").expand();
        bot.tree().getTreeItem(projectNameSecondary).getNode("src").getNode("main").getNode("synapse-config").expand();
        bot.tree().getTreeItem(projectNameSecondary).getNode("src").getNode("main").getNode("synapse-config")
                .getNode("endpoints").expand();
        bot.sleep(3000);

    }
    
    @Test
    @Order(order = 5)
    public void deleteProjects() throws Exception {
        AbstractClass.deleteWithContent(projectNameMenu);
        AbstractClass.deleteWithContent(projectNameDash);
        AbstractClass.deleteWithContent(projectNameSecondary);
    }

}
