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

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.utils.Activator;

public class Util {
    public static SWTWorkbenchBot bot;
    protected static SWTBotShell newServiceProject;
    protected static SWTBotShell newWorkflow;
    protected static SWTBotShell changePerspective;
    protected static String actual;
    protected static String expect;
    protected static SWTBotShell saveIn;
    protected static IDeveloperStudioLog log = Logger.getLog(Activator.PLUGIN_ID);

    public static void checkShellLoading(final String shellName) {
        bot.waitUntil(new DefaultCondition() {
            //@Override
            public String getFailureMessage() {
                log.error("Shell loading failure");
                fail();
                return "Unable to load the shell";
                
            }

            //@Override
            public boolean test() throws Exception {
                return bot.shell(shellName).isActive();
            }
        });
    }
    
    public static void checkButton(final String buttonName, final SWTBotShell shellName){
        
        bot.waitUntil(new DefaultCondition() {
            //@Override
            public String getFailureMessage() {
                log.error(buttonName + " Button is not enabled");
                fail();
                return "Button is not enabled";
            }

            //@Override
            public boolean test() throws Exception {
                return shellName.bot().button(buttonName).isEnabled();
            }

        });
        
    }
}