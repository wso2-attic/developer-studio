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

import static org.junit.Assert.fail;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.Order;
import org.junit.OrderedRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;

/* Testing Axis2ServiceProject
 * Create a new Axis2ServiceProject with a service class using New button
 *  In second test case creating a new service class using right click
 */

@RunWith(OrderedRunner.class)
public class TestAxis2ServiceProject extends Setup {

    String projectName = "firstAxis2";
    String packageName = "hello";
    String className = "HelloWorld";
    String fileName = "HelloWorld.java";
    String className2 = "serviceClass";
    String fileName2 = "serviceClass.java";
    String[] path = { "src/main/java", packageName }; // + packageName;
    SWTBotTreeItem packageExplorer;

    // Creating Axis2 Service project without WSDL
    @Test
    @Order(order = 1)
    public void createAnAxisProject() throws Exception {
        try {
            AbstractClass.openProjectFromMenu("Axis2 Service Project");
            AbstractClass.createAxis2Project(projectName, packageName, className);
        } catch (Exception e) {
            System.out.println("Create Axis2 Project failed");
            fail();
        } catch (Error erro) {
            System.out.println("Create Axis2 Project failed");
            fail();
        }
    }

    @Test
    @Order(order = 2)
    public void creatASecondJavaProject() throws Exception {
        AbstractClass.openProjectFromRightClick(projectName, path, "Axis2 Service Class");

        try {
            AbstractClass.createAxis2Class(className2);
            AbstractClass.openEditor(projectName, path, fileName2);
            /*
             * bot.tree().getTreeItem(projectName).getNode("src/main/java").getNode(packageName).expand();
             * bot.tree().getTreeItem(projectName).getNode("src/main/java").getNode(packageName)
             * .getNode(className2 + ".java").doubleClick();
             */
            AbstractClass.validateJAxis2ServiceClass(packageName, className2);
        }

        catch (Exception e) {
            System.out.println("Second class hasn't created correctly");
            fail();
        } catch (Error erro) {
            System.out.println("Second class hasn't created correctly");
            fail();
        }

    }

    @Test
    @Order(order = 3)
    public void deleteProject() throws Exception {
        try {
            AbstractClass.deleteWithContent(projectName);
        } catch (Error erro) {
            System.out.println("Project deleting failed");
            fail();
        }
    }

}
