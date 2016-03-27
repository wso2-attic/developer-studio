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

package org.wso2.developerstudio.eclipse.test.automation.framework.executor;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.wso2.developerstudio.eclipse.test.automation.framework.constants.ExecutorConstants;
import org.wso2.developerstudio.eclipse.test.automation.framework.element.validator.WorkbenchElementsValidator;

public class Executor {
    @BeforeClass
    public static void beforeClass() throws Exception {
        WorkbenchElementsValidator.bot = new SWTWorkbenchBot();
        WorkbenchElementsValidator.bot.sleep(5000);
        SWTBotPreferences.PLAYBACK_DELAY = 30;

        try {
            WorkbenchElementsValidator.bot.viewByTitle(ExecutorConstants.WELCOME).close();
        } catch (WidgetNotFoundException e) {

        }
        WorkbenchElementsValidator.bot.sleep(2000);

    }

    @AfterClass
    public static void sleep() {
        try {
            WorkbenchElementsValidator.bot.closeAllEditors();
            for (int i = WorkbenchElementsValidator.bot.shells().length - 1; i > 0; i--) {
                WorkbenchElementsValidator.bot.shells()[i].close();
                WorkbenchElementsValidator.bot.button("OK").click();
            }
        } catch (Exception e) {
        }

    }
}
