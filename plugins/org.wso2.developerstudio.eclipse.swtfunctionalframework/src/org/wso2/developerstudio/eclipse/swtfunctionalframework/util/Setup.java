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
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonCons;

public class Setup extends Util {

    @BeforeClass
    public static void beforeClass() throws Exception {
        bot = new SWTWorkbenchBot();
        SWTBotPreferences.PLAYBACK_DELAY = 30;

        try {
            bot.viewByTitle(CommonCons.WELCOME).close();
        } catch (WidgetNotFoundException e) {

        }

    }

    @AfterClass
    public static void sleep() {
        try {
            bot.closeAllEditors();
            for (int i = bot.shells().length - 1; i > 0; i--) {
                bot.shells()[i].close();
                bot.button("OK").click();
            }
        } catch (Exception e) {

        }

    }
}
