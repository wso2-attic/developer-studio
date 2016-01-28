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
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonConstants;

public class BusinessRulesUtil {

	private static SWTBotEditor serviceEditor;
	private static SWTBotShell newServiceProject;

	public static void serviceEditorText(String option, String value) {
		try {
			serviceEditor = Util.bot.editorByTitle("service.rsl");
			serviceEditor.bot().textWithLabel(option).setText(value);
			serviceEditor.save();
		} catch (Exception e) {
			Util.log.error("Editor cannot be found", e);
			fail();
		}

	}

	public static void closeServiceEditor() {
		try {
			serviceEditor = Util.bot.editorByTitle("service.rsl");
			serviceEditor.close();
		} catch (Exception e) {
			Util.log.error("Editor cannot be found", e);
			fail();
		}
	}

	public static void createBusinessRulesService(String projectName,
			String serviceName) {

		Util.bot.sleep(2000);
		Util.checkShellLoading(BusinessRulesServiceConstants.NEW_BUSINESS_RULES_SERVICE_PROJECT);
		newServiceProject = Util.bot
				.shell(BusinessRulesServiceConstants.NEW_BUSINESS_RULES_SERVICE_PROJECT);
		Util.checkButton(CommonConstants.NEXT, newServiceProject);
		newServiceProject.bot().button(CommonConstants.NEXT).click();
		Util.setLableText(newServiceProject,
				BusinessRulesServiceConstants.PROJECT_NAME, projectName);
		Util.setLableText(newServiceProject,
				BusinessRulesServiceConstants.SERVICE_NAME, serviceName);
		Util.checkButton(CommonConstants.NEXT, newServiceProject);
		newServiceProject.bot().button(CommonConstants.NEXT).click();
		Util.checkButton(CommonConstants.FINISH, newServiceProject);
		newServiceProject.bot().button(CommonConstants.FINISH).click();
		Util.bot.waitUntil(Conditions.shellCloses(newServiceProject));
		try {
			Util.bot.editorByTitle(BusinessRulesServiceConstants.SERVICE_RSL)
					.show();
			Util.log.info("Create Business Rules Service Sucsessful");

		} catch (WidgetNotFoundException e) {
			Util.log.error("Editor didnt load",e);
			fail();
		}
	}

	public static void importBusinessRulesService(String path) {

		Util.checkShellLoading(BusinessRulesServiceConstants.NEW_BUSINESS_RULES_SERVICE_PROJECT);
		newServiceProject = Util.bot
				.shell(BusinessRulesServiceConstants.NEW_BUSINESS_RULES_SERVICE_PROJECT);
		newServiceProject
				.bot()
				.radio(BusinessRulesServiceConstants.IMPORT_BUSINESS_RULE_SERVICE)
				.click();
		Util.checkButton(CommonConstants.NEXT, newServiceProject);
		newServiceProject.bot().button(CommonConstants.NEXT).click();
		Util.setLableText(
				newServiceProject,
				BusinessRulesServiceConstants.BUSINESS_RULE_SERVICE_DESCRIPTOR_FILE,
				path);
		Util.checkButton(CommonConstants.NEXT, newServiceProject);
		newServiceProject.bot().button(CommonConstants.NEXT).click();
		Util.checkButton(CommonConstants.FINISH, newServiceProject);
		newServiceProject.bot().button(CommonConstants.FINISH).click();
		Util.bot.waitUntil(Conditions.shellCloses(newServiceProject));
	}

}