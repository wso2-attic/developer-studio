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

package org.wso2.developerstudio.eclipse.test.automation.framework.element.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.wso2.developerstudio.eclipse.test.automation.framework.Activator;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;

public class WorkbenchElementsValidator {
    public static SWTWorkbenchBot bot;
    public static SWTBotShell newServiceProject;
    public static SWTBotShell newWorkflow;
    public static SWTBotShell changePerspective;
    public static String actual;
    public static String expect;
    public static StringBuffer expectedBuffer;
    public static SWTBotShell saveIn;
    public static List<String> actualFiles;
    public static SWTBotTreeItem main;
    public static IDeveloperStudioLog log = Logger.getLog(Activator.PLUGIN_ID);

    public static void checkShellLoading(final String shellName) {
        bot.waitUntil(new DefaultCondition() {
            public String getFailureMessage() {
                log.error("Shell loading failure");
                fail();
                return "Unable to load the shell";

            }

            public boolean test() throws Exception {
                return bot.shell(shellName).isActive();
            }
        });
    }

    public static void checkButton(final String buttonName, final SWTBotShell botShell) {

        bot.waitUntil(new DefaultCondition() {
            public String getFailureMessage() {
                log.error(buttonName + " Button is not enabled");
                fail();
                return "Button is not enabled";
            }

            public boolean test() throws Exception {
                return botShell.bot().button(buttonName).isEnabled();
            }

        });

    }

    public static void setLableText(SWTBotShell botShell, String lableName, String text) {

        String actual = "";
        try {
            botShell.bot().textWithLabel(lableName).setText(text);
            bot.sleep(2000);
            actual = botShell.bot().textWithLabel(lableName).getText().trim().toString();
        } catch (WidgetNotFoundException e) {
            log.error("Problem with the Lable", e);
            fail();
        }
        try {
            assertEquals(actual, text);
        } catch (AssertionError e) {
            log.error("Entered text is not the same", e);
            fail();
        }
    }

}
