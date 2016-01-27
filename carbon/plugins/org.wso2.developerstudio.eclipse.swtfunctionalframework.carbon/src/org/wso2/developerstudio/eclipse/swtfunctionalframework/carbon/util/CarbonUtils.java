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

package org.wso2.developerstudio.eclipse.swtfunctionalframework.carbon.util;

import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.carbon.util.constants.CarbonUICons;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.CommonUtil;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonConstants;

public class CarbonUtils {

	public static void createCarbonUI(String projectName) {

		Util.checkShellLoading(CarbonUICons.PROJECT_WIZARD_WINDOW_TITLE);
		SWTBotShell newCarbonUIBundle = Util.bot
				.shell(CarbonUICons.PROJECT_WIZARD_WINDOW_TITLE);
		Util.checkButton(CommonConstants.NEXT, newCarbonUIBundle);
		newCarbonUIBundle.bot().button(CommonConstants.NEXT).click();
		Util.setLableText(newCarbonUIBundle, CarbonUICons.PROJECT_NAME,
				projectName);
		Util.checkButton(CommonConstants.NEXT, newCarbonUIBundle);
		newCarbonUIBundle.bot().button(CommonConstants.NEXT).click();
		Util.checkButton(CommonConstants.FINISH, newCarbonUIBundle);
		newCarbonUIBundle.bot().button(CommonConstants.FINISH).click();
		try {
			CommonUtil.switchPerspectiveInCreatingProjects();
		} catch (AssertionError e) {

		}
		Util.bot.waitUntil(Conditions.shellCloses(newCarbonUIBundle));
	}

}
