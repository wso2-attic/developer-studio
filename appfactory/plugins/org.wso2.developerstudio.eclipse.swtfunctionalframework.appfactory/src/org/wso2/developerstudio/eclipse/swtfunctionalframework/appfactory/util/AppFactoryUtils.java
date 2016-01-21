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

package org.wso2.developerstudio.eclipse.swtfunctionalframework.appfactory.util;

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertContains;
import static org.junit.Assert.fail;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.appfactory.util.constants.AppFactoryCons;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonCons;

public class AppFactoryUtils extends Util {

    public static SWTBotTreeItem appFactoryTree() {
        SWTBotView appList = bot.viewByTitle(AppFactoryCons.APPLICATIONS_LIST);
        try {
            appList.bot().tree().getTreeItem("TestApplicationOne").select();
            appList.bot().tree().getTreeItem("TestApplicationOne").contextMenu("Open  ").click();
            while (!appList.bot().tree().getTreeItem("TestApplicationOne (Opened)").isVisible()) {
                bot.sleep(1000);
            }
        } catch (WidgetNotFoundException | TimeoutException e) {
            try {
                appList.bot().tree().getTreeItem("TestApplicationOne (Opened)").isVisible();
            } catch (WidgetNotFoundException | TimeoutException erro) {
                log.error("Application list loading failiure");
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
            log.error(action + " is not available");
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
        bot.cTabItem("Console").setFocus();
        String styledText = bot.styledText().getText();
        try {
            assertContains(text, styledText);
        } catch (AssertionError e) {
            log.error("Action execution unsucssesful");
            fail();
        }

    }

    public static void appFactoryLogin(String emailUserName, String domainName, String password) {
        try {
            bot.viewByTitle(AppFactoryCons.APPLICATIONS_LIST).show();
        } catch (WidgetNotFoundException e) {
            bot.menu("Window").menu("Show View").menu("Other...").click();
            SWTBotShell showView = bot.shell("Show View");
            showView.bot().text().setText(AppFactoryCons.APPLICATIONS_LIST);
            showView.bot().button("OK").click();
        }
        try {
            bot.toolbarButtonWithTooltip(AppFactoryCons.LOGIN2).click();
            checkShellLoading(AppFactoryCons.APP_CLOUD_APP_FACTORY_LOGIN);
            SWTBotShell login = bot.shell(AppFactoryCons.APP_CLOUD_APP_FACTORY_LOGIN);
            login.bot().textWithLabel(AppFactoryCons.EMAIL).setText(emailUserName + "@" + domainName);
            login.bot().textWithLabel(AppFactoryCons.PASSWORD2).setText(password);
            login.bot().button(CommonCons.OK).click();
            bot.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses(login));
            bot.viewByTitle(AppFactoryCons.APPLICATIONS_LIST).show();
            log.error("Login successful");
        } catch (TimeoutException e) {
            log.error("Fail to login");
            fail();
        } catch (WidgetNotFoundException e) {
            log.error("Problem with the login widget");
            fail();
        }

    }


}