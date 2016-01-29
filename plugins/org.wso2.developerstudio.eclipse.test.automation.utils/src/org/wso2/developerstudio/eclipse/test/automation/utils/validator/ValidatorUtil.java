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

package org.wso2.developerstudio.eclipse.test.automation.utils.validator;

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertContains;
import static org.junit.Assert.assertNotSame;

import java.util.List;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.wso2.developerstudio.eclipse.test.automation.utils.functional.FunctionalUtil;
import org.wso2.developerstudio.eclipse.test.automation.framework.element.validator.WorkbenchElementsValidator;

public class ValidatorUtil {

	protected static SWTBotTreeItem tree;

	/**
	 * This method will validate the content of a source.
	 * 
	 * @param editorName
	 *            Name of the editor that must validate
	 * @param expectedContent
	 *            expected content in the source
	 */
	public static void contentValidation(String editorName,
			String expectedContent) {
		WorkbenchElementsValidator.actual = WorkbenchElementsValidator.bot
				.editorByTitle(editorName).bot().styledText().getText();
		WorkbenchElementsValidator.expect = expectedContent;
		try {
			assertContains(WorkbenchElementsValidator.actual,
					WorkbenchElementsValidator.expect);
			WorkbenchElementsValidator.log.info("Validation sucsessful");
		} catch (AssertionError e) {
			WorkbenchElementsValidator.log.error("Content dosen't match", e);
		}
	}

	/**
	 * This method will validate the main project structure
	 * 
	 * @param projectName
	 *            The name of the project that should be validated
	 * @param expectedFiles
	 *            Expected project structure
	 */
	public static void projectValidation(String projectName,
			List<String> expectedFiles) {
		projectValidation(projectName, null, expectedFiles);
	}

	/**
	 * This method will validate structure inside of a specific node in the
	 * project
	 * 
	 * @param projectName
	 *            Name of the project that has to validate
	 * @param path
	 *            Path that want to validate
	 * @param expectedFiles
	 *            Expected structure
	 */
	public static void projectValidation(String projectName, String[] path,
			List<String> expectedFiles) {
		if (path == null) {
			WorkbenchElementsValidator.main = WorkbenchElementsValidator.bot
					.tree().getTreeItem(projectName);
			WorkbenchElementsValidator.bot.sleep(5000);
			WorkbenchElementsValidator.main.expand();
		} else {
			WorkbenchElementsValidator.main = FunctionalUtil
					.getExpandProjectTree(projectName, path);
		}
		WorkbenchElementsValidator.bot.sleep(5000);
		WorkbenchElementsValidator.actualFiles = WorkbenchElementsValidator.main
				.getNodes();
		WorkbenchElementsValidator.bot.sleep(5000);
		assertNotSame("Project validation faliure", expectedFiles,
				WorkbenchElementsValidator.actualFiles);
	}

}
