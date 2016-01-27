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

package org.wso2.developerstudio.eclipse.swtfunctionalframework.util;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonConstants;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;

public class Setup {

    @BeforeClass
    public static void beforeClass() throws Exception {
        Util.bot = new SWTWorkbenchBot();
        Util.bot.sleep(5000);
        SWTBotPreferences.PLAYBACK_DELAY = 30;

        try {
            Util.bot.viewByTitle(CommonConstants.WELCOME).close();
        } catch (WidgetNotFoundException e) {

        }
        Util.bot.sleep(2000);

    }

    @AfterClass
    public static void sleep() {
        try {
            Util.bot.closeAllEditors();
            for (int i = Util.bot.shells().length - 1; i > 0; i--) {
                Util.bot.shells()[i].close();
                Util.bot.button(CommonConstants.OK).click();
            }
        } catch (Exception e) {
        }

    }
}