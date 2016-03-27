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

package org.wso2.developerstudio.eclipse.test.automation.utils.kernel;

import static org.junit.Assert.fail;

import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.CommonConstants;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.CompositeApllicationConstants;
import org.wso2.developerstudio.eclipse.test.automation.framework.element.validator.WorkbenchElementsValidator;

public class CompositeApplicationUtil {

	public static void createCompositeApllication(String projectName) {

		WorkbenchElementsValidator
				.checkShellLoading(CompositeApllicationConstants.NEW_Composite_Apllication);
		WorkbenchElementsValidator.bot.sleep(1000);
		SWTBotShell newCompositeApllication = WorkbenchElementsValidator.bot
				.shell(CompositeApllicationConstants.NEW_Composite_Apllication);
		WorkbenchElementsValidator.setLableText(newCompositeApllication,
				CompositeApllicationConstants.PROJECT_NAME, projectName);
		WorkbenchElementsValidator.bot.sleep(1000);
		WorkbenchElementsValidator.checkButton(CommonConstants.NEXT,
				newCompositeApllication);
		newCompositeApllication.bot().button(CommonConstants.NEXT).click();
		WorkbenchElementsValidator.bot.sleep(1000);
		WorkbenchElementsValidator.checkButton(CommonConstants.FINISH,
				newCompositeApllication);
		newCompositeApllication.bot().button(CommonConstants.FINISH).click();
		try {
			WorkbenchElementsValidator.bot.waitUntil(Conditions
					.shellCloses(newCompositeApllication));
		} catch (TimeoutException e) {
			WorkbenchElementsValidator.log.error("Wizard closing faliure", e);
			fail();
		}
	}
}
