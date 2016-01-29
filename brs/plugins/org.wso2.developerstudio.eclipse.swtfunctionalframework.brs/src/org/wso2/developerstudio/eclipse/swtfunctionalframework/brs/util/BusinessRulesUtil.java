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

package org.wso2.developerstudio.eclipse.swtfunctionalframework.brs.util;

import static org.junit.Assert.fail;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.brs.util.constants.BusinessRulesServiceConstants;
import org.wso2.developerstudio.eclipse.test.automation.framework.element.validator.WorkbenchElementsValidator;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.CommonConstants;

public class BusinessRulesUtil {

	private static SWTBotEditor serviceEditor;
	private static SWTBotShell newServiceProject;

	public static void serviceEditorText(String option, String value) {
		try {
			serviceEditor = WorkbenchElementsValidator.bot
					.editorByTitle("service.rsl");
			serviceEditor.bot().textWithLabel(option).setText(value);
			serviceEditor.save();
		} catch (Exception e) {
			WorkbenchElementsValidator.log.error("Editor cannot be found", e);
			fail();
		}

	}

	public static void closeServiceEditor() {
		try {
			serviceEditor = WorkbenchElementsValidator.bot
					.editorByTitle("service.rsl");
			serviceEditor.close();
		} catch (Exception e) {
			WorkbenchElementsValidator.log.error("Editor cannot be found", e);
			fail();
		}
	}

	public static void createBusinessRulesService(String projectName,
			String serviceName) {

		WorkbenchElementsValidator.bot.sleep(2000);
		WorkbenchElementsValidator
				.checkShellLoading(BusinessRulesServiceConstants.NEW_BUSINESS_RULES_SERVICE_PROJECT);
		newServiceProject = WorkbenchElementsValidator.bot
				.shell(BusinessRulesServiceConstants.NEW_BUSINESS_RULES_SERVICE_PROJECT);
		WorkbenchElementsValidator.checkButton(CommonConstants.NEXT,
				newServiceProject);
		newServiceProject.bot().button(CommonConstants.NEXT).click();
		WorkbenchElementsValidator.setLableText(newServiceProject,
				BusinessRulesServiceConstants.PROJECT_NAME, projectName);
		WorkbenchElementsValidator.setLableText(newServiceProject,
				BusinessRulesServiceConstants.SERVICE_NAME, serviceName);
		WorkbenchElementsValidator.checkButton(CommonConstants.NEXT,
				newServiceProject);
		newServiceProject.bot().button(CommonConstants.NEXT).click();
		WorkbenchElementsValidator.checkButton(CommonConstants.FINISH,
				newServiceProject);
		newServiceProject.bot().button(CommonConstants.FINISH).click();
		WorkbenchElementsValidator.bot.waitUntil(Conditions
				.shellCloses(newServiceProject));
		try {
			WorkbenchElementsValidator.bot.editorByTitle(
					BusinessRulesServiceConstants.SERVICE_RSL).show();
			WorkbenchElementsValidator.log
					.info("Create Business Rules Service Sucsessful");

		} catch (WidgetNotFoundException e) {
			WorkbenchElementsValidator.log.error("Editor didnt load", e);
			fail();
		}
	}

	public static void importBusinessRulesService(String path) {

		WorkbenchElementsValidator
				.checkShellLoading(BusinessRulesServiceConstants.NEW_BUSINESS_RULES_SERVICE_PROJECT);
		newServiceProject = WorkbenchElementsValidator.bot
				.shell(BusinessRulesServiceConstants.NEW_BUSINESS_RULES_SERVICE_PROJECT);
		newServiceProject
				.bot()
				.radio(BusinessRulesServiceConstants.IMPORT_BUSINESS_RULE_SERVICE)
				.click();
		WorkbenchElementsValidator.checkButton(CommonConstants.NEXT,
				newServiceProject);
		newServiceProject.bot().button(CommonConstants.NEXT).click();
		WorkbenchElementsValidator
				.setLableText(
						newServiceProject,
						BusinessRulesServiceConstants.BUSINESS_RULE_SERVICE_DESCRIPTOR_FILE,
						path);
		WorkbenchElementsValidator.checkButton(CommonConstants.NEXT,
				newServiceProject);
		newServiceProject.bot().button(CommonConstants.NEXT).click();
		WorkbenchElementsValidator.checkButton(CommonConstants.FINISH,
				newServiceProject);
		newServiceProject.bot().button(CommonConstants.FINISH).click();
		WorkbenchElementsValidator.bot.waitUntil(Conditions
				.shellCloses(newServiceProject));
	}

}