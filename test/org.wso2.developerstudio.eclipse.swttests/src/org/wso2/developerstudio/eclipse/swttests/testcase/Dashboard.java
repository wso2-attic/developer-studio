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

package org.wso2.developerstudio.eclipse.swttests.testcase;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.forms.finder.SWTFormsBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
//import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;
//import org.wso2.developerstudio.eclipse.utils.Activator;

@RunWith(SWTBotJunit4ClassRunner.class)
public class Dashboard extends Setup{

    private static SWTFormsBot fo = new SWTFormsBot();
    //private static IDeveloperStudioLog log = Logger.getLog(Activator.PLUGIN_ID);
    // Create a new Wso2 Dashboard
    @Test
    public void createANewBusinesRulesServer() throws Exception {

        bot.menu("Developer Studio").menu("Open Dashboard").click();
        SWTBotEditor dashBoard = bot.editorByTitle("Developer Studio Dashboard");
        dashBoard.show();
        // fo.hyperlink("Maven Multi Module Project").click();
        // fo.button("Create").click();
        dashBoard.bot().label("Create").click();
        bot.sleep(2000);
        //log.error("sdfsfdsds");
        //fo.imageHyperlink("Maven Multi Module Project").click();

    }

}
