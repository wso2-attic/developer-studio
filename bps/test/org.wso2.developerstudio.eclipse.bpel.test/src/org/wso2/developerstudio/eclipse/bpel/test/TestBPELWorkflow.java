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

package org.wso2.developerstudio.eclipse.bpel.test;

import org.wso2.developerstudio.eclipse.swtfunctionalframework.bpel.util.BPELUtil;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.CommonUtil;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;

/* Testing BPEL Workflow
 * Create a new BPEL Workflow using new menu
 * Expand the tree
 * Save the project
 * Delete the project
 */

@RunWith(OrderedRunner.class)
public class TestBPELWorkflow extends Setup {

    private String projectName = "first";
    private String[] path = { "bpelContent" };

    @Test
    @Order(order = 1)
    public void createBPELProject() throws Exception {

        CommonUtil.openProjectCreationWizardFromMenu("BPEL Workflow");
        BPELUtil.createNewBPELProject(projectName);

        CommonUtil.getexpandProjecttree(projectName, path);
        CommonUtil.saveEditor(projectName + ".bpel");

    }

    @Test
    @Order(order = 2)
    public void deleteProject() throws Exception {
        CommonUtil.deleteWithContent(projectName);
    }

}
