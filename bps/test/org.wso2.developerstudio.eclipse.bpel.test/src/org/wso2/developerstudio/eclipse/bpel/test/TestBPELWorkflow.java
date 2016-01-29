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
import org.wso2.developerstudio.eclipse.test.automation.framework.executor.Executor;
import org.wso2.developerstudio.eclipse.test.automation.framework.runner.*;
import org.wso2.developerstudio.eclipse.test.automation.utils.functional.FunctionalUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

/* Testing BPEL Workflow
 * Create a new BPEL Workflow using new menu
 * Expand the tree
 * Save the project
 * Delete the project
 */

@RunWith(OrderedRunner.class)
public class TestBPELWorkflow extends Executor {

	private String projectName = "first";
	private String[] path = { "bpelContent" };

	@Test
	@Order(order = 1)
	public void createBPELProject() throws Exception {

		FunctionalUtil.openProjectCreationWizardFromMenu("BPEL Workflow");
		BPELUtil.createNewBPELProject(projectName);

		FunctionalUtil.getExpandProjectTree(projectName, path);
		FunctionalUtil.saveEditor(projectName + ".bpel");

	}

	@Test
	@Order(order = 2)
	public void deleteProject() throws Exception {
		FunctionalUtil.deleteProjectWithContent(projectName);
	}

}
