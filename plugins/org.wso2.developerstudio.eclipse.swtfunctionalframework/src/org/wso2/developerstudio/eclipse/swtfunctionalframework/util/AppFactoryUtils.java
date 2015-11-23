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

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertContains;
import static org.junit.Assert.fail;
//import org.eclipse.core.runtime.CoreException;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.AppFactoryCons;

public class AppFactoryUtils extends Util {

    public static SWTBotTreeItem appFactoryTree() {
        SWTBotView appList = bot.viewByTitle(AppFactoryCons.APPLICATIONS_LIST);
        try {
            appList.bot().tree().getTreeItem("TestApplicationOne").select();
            appList.bot().tree().getTreeItem("TestApplicationOne").contextMenu("Open  ").click();
            while (!appList.bot().tree().getTreeItem("TestApplicationOne (Opened)").isVisible()) {
                bot.sleep(1000);
            }
        } catch (WidgetNotFoundException /* | TimeoutException */e) {
            try {
                appList.bot().tree().getTreeItem("TestApplicationOne (Opened)").isVisible();
            } catch (WidgetNotFoundException /* | TimeoutException */erro) {
                System.out.println("Application list loading failiure");
                fail();
            }

        }

        SWTBotTreeItem opened = appList.bot().tree().getTreeItem("TestApplicationOne (Opened)");
        return opened;
    }

    public static void appFactoryApplicationAction(String path, String action) throws Exception {

        String text = "";
        String[] op = path.split("/");
        SWTBotTreeItem opened = AppFactoryUtils.appFactoryTree();
        bot.sleep(2000);
        if (!opened.isExpanded()) {
            opened.expand();
        }
        if (!opened.getNode(op[0]).isExpanded()) {
            opened.getNode(op[0]).expand();
        }

        opened.getNode(op[0]).getNode(op[1]).select();
        try {
            opened.getNode(op[0]).getNode(op[1]).contextMenu(action).click();
        } catch (WidgetNotFoundException e) {
            System.out.println(action + " is not available");
            System.exit(0);
        }
        bot.sleep(2000);

        if (action == AppFactoryCons.BUILD) {
            bot.sleep(2000);
            text = "Build invoked successfully.";
        } else if (action == AppFactoryCons.DEPLOY) {
            bot.sleep(2000);
            text = "Successfully deployed";
        } else if (action == AppFactoryCons.BUILD_LOGS) {
            bot.sleep(10000);
            text = "[INFO] BUILD SUCCESS";
        } else if (action == AppFactoryCons.CHECK_OUT) {
            bot.sleep(3000);
            text = "Cloning completed successfully";
        } else if (action == AppFactoryCons.CHECK_OUT_AND_IMPORT) {

        }

        else {
            fail();

        }
        /*
         * switch (action) {
         * case AppFactoryCons.BUILD:
         * text = "Build invoked successfully.";
         * break;
         *
         * case AppFactoryCons.DEPLOY:
         * text = "Successfully deployed";
         * break;
         *
         * case AppFactoryCons.BUILD_LOGS:
         * bot.sleep(6000);
         * text = "[INFO] BUILD SUCCESS";
         * break;
         *
         * case AppFactoryCons.CHECK_OUT:
         * text = "Build invoked successfully.";
         * break;
         *
         * default:
         * fail();
         * break;
         * }
         */

        bot.cTabItem("Console").setFocus();
        String styledText = bot.styledText().getText();
        try {
            assertContains(text, styledText);
        } catch (AssertionError e) {
            System.out.println(styledText);
            fail();
        }

    }

}