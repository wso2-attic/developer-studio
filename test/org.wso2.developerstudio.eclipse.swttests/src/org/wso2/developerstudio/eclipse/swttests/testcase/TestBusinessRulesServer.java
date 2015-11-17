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

import org.junit.Order;
import org.junit.OrderedRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.BusinessRulesUtil;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;

@RunWith(OrderedRunner.class)
public class TestBusinessRulesServer extends Setup {

    // Create a new Business Rules Server
    private String projectName = "businessRule";
    private String serviceName = "myFirstService";
    private String className = "HelloWorld";
    private String[] path = {"src/main/java"};
    private String servicepath = "/home/kushan/myFirst.rsl";

    @Test
    @Order(order = 1)
    public void createANewBusinesRulesServer() throws Exception {

        AbstractClass.openProjectFromMenu("Business Rules Service Project");
        AbstractClass.createBusinessRulesService(projectName, serviceName);
    }

    @Test
    @Order(order = 2)
    public void accessesCtabs() throws Exception {

        BusinessRulesUtil.serviceEditorText("Target Namespace", "home");
        BusinessRulesUtil.serviceEditorText("Service Name", serviceName);
        AbstractClass.activateCtab("Source");
        AbstractClass.activateCtab("Design");
        BusinessRulesUtil.closeServiceEditor();
    }

    @Test
    @Order(order = 3)
    public void createNewClass() throws Exception {

        AbstractClass.openProjectFromRightClick(projectName, path, "Class");
        AbstractClass.createJavaClass(className);

    }

    @Test
    @Order(order = 4)
    public void importARule() {

        AbstractClass.openProjectFromMenu("Business Rules Service Project");
        AbstractClass.importBusinessRulesService(servicepath);

    }
    
    @Test
    @Order(order = 5)
    public void deleteProject(){
        AbstractClass.deleteWithContent(projectName);
    }
    
    @Test
    @Order(order = 5)
    public void deleteNextProject(){
        AbstractClass.deleteWithContent("myFirst");
    }
    /*
     * @Test
     * public void testToFail() throws Exception{
     * bot.toolbarDropDownButtonWithTooltip("New").click();
     * 
     * newProject = bot.shell("New");
     * newProject.bot().tree().getTreeItem("WSO2").expand();
     * //newProject.bot().tree().getTreeItem("WSO2").expand();
     * newProject.bot().tree().getTreeItem("WSO2").getNode("Business Rules Server").expand();
     * newProject.bot().tree().getTreeItem("WSO2").getNode("Business Rules Server").getNode("Business Rules Service Project"
     * ).select();
     * newProject.bot().button("Next >").click();
     * 
     * newServiceProject = bot.shell("New Business Rules Service Project");
     * newServiceProject.bot().button("Next >").click();
     * newServiceProject.bot().textWithLabel("Project Name*");
     * newServiceProject.bot().textWithLabel("Service Name*");
     * newServiceProject.bot().button("Next >").click();
     * newServiceProject.bot().button("Finish").click();
     * 
     * }
     */

}
