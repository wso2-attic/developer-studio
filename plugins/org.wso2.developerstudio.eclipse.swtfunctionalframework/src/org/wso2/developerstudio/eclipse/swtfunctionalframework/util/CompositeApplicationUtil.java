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
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonConstants;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CompositeApllicationConstants;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;

public class CompositeApplicationUtil{

    public static void createCompositeApllication(String projectName) {

        Util.checkShellLoading(CompositeApllicationConstants.NEW_Composite_Apllication);
        Util.bot.sleep(1000);
        SWTBotShell newCompositeApllication = Util.bot.shell(CompositeApllicationConstants.NEW_Composite_Apllication);
        Util.setLableText(newCompositeApllication, CompositeApllicationConstants.PROJECT_NAME, projectName);
        Util.bot.sleep(1000);
        Util.checkButton(CommonConstants.NEXT, newCompositeApllication);
        newCompositeApllication.bot().button(CommonConstants.NEXT).click();
        Util.bot.sleep(1000);
        Util.checkButton(CommonConstants.FINISH, newCompositeApllication);
        newCompositeApllication.bot().button(CommonConstants.FINISH).click();
        try {
            Util.bot.waitUntil(Conditions.shellCloses(newCompositeApllication));
        } catch (TimeoutException e) {
            Util.log.error("Wizard closing faliure", e);
            fail();
            }
    }
}
