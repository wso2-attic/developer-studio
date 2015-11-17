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
import org.junit.runners.BlockJUnit4ClassRunner;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;

/* Testing BPEL Workflow
 * Create a new BPEL Workflow using new menu
 * Expand the tree
 * Save the project
 * Delete the project
 */

@RunWith(OrderedRunner.class)
public class TestBPELWorkflow extends Setup {

    // create a new BPEL Workflow
    private String projectName = "first";

    @Test
    @Order(order = 1)
    public void createBPELProject() throws Exception {

        AbstractClass.openProjectFromMenu("BPEL Workflow");
        AbstractClass.createNewBPELProject(projectName);

        bot.tree().getTreeItem(projectName).expand();
        bot.tree().getTreeItem(projectName).getNode("bpelContent").expand();
        bot.tree().getTreeItem(projectName).getNode("bpelContent").expand();
        bot.tree().getTreeItem(projectName).getNode("bpelContent").select();
        bot.editorByTitle(projectName + ".bpel").save();
        // bot.tree().getTreeItem(projectName).contextMenu("Delete").click();

        // bot.editorByTitle(projectName + ".bpel").close();
        // bot.closeAllEditors();

    }

    @Test
    @Order(order = 2)
    public void deleteProject() throws Exception {
        AbstractClass.deleteWithContent(projectName);
    }

}
