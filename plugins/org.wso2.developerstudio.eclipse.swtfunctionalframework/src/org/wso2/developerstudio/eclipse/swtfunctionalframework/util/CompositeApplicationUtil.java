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

import static org.junit.Assert.fail;

import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonCons;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CompositeApllicationCons;

public class CompositeApplicationUtil extends Util {

    public static void createCompositeApllication(String projectName) {

        checkShellLoading(CompositeApllicationCons.NEW_Composite_Apllication);
        bot.sleep(3000);
        SWTBotShell newCompositeApllication = bot.shell(CompositeApllicationCons.NEW_Composite_Apllication);
        setLableText(newCompositeApllication, CompositeApllicationCons.PROJECT_NAME, projectName);
/*        try {
            newCompositeApllication.bot().textWithLabel(CompositeApllicationCons.PROJECT_NAME).setText(projectName);
        } catch (Exception e) {
            log.error("Problem with the Lable", e);
            fail();
        }*/
        bot.sleep(1000);
        checkButton(CommonCons.NEXT, newCompositeApllication);
        newCompositeApllication.bot().button(CommonCons.NEXT).click();
        bot.sleep(1000);
        checkButton(CommonCons.FINISH, newCompositeApllication);
        newCompositeApllication.bot().button(CommonCons.FINISH).click();
        try {
            bot.waitUntil(Conditions.shellCloses(newCompositeApllication));
        } catch (TimeoutException e) {
            //log.error("Wizard closing faliure", e);
            fail();
            }
    }
}
