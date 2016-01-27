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
import org.wso2.developerstudio.eclipse.swtfunctionalframework.appfactory.util.constants.AppFactoryConstants;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.PerspectiveLoginUtil;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonConstants;

public class AppFactoryUtils extends PerspectiveLoginUtil{

    public static SWTBotTreeItem appFactoryTree() {
        SWTBotView appList = Util.bot.viewByTitle(AppFactoryConstants.APPLICATIONS_LIST);
        try {
            appList.bot().tree().getTreeItem("TestApplicationOne").select();
            appList.bot().tree().getTreeItem("TestApplicationOne").contextMenu("Open  ").click();
            while (!appList.bot().tree().getTreeItem("TestApplicationOne (Opened)").isVisible()) {
                Util.bot.sleep(1000);
            }
        } catch (WidgetNotFoundException | TimeoutException e) {
            try {
                appList.bot().tree().getTreeItem("TestApplicationOne (Opened)").isVisible();
            } catch (WidgetNotFoundException | TimeoutException erro) {
                Util.log.error("Application list loading failiure",e);
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
        Util.bot.sleep(2000);
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
            Util.log.error(action + " is not available",e);
        }
        Util.bot.sleep(2000);

        if (action == AppFactoryConstants.BUILD) {
            Util.bot.sleep(2000);
            text = "Build invoked successfully.";
        } else if (action == AppFactoryConstants.DEPLOY) {
            Util.bot.sleep(2000);
            text = "Successfully deployed";
        } else if (action == AppFactoryConstants.BUILD_LOGS) {
            Util.bot.sleep(10000);
            text = "[INFO] BUILD SUCCESS";
        } else if (action == AppFactoryConstants.CHECK_OUT) {
            Util.bot.sleep(3000);
            text = "Cloning completed successfully";
        }
        else {
            fail();

        }
        Util.bot.cTabItem("Console").setFocus();
        String styledText = Util.bot.styledText().getText();
        try {
            assertContains(text, styledText);
        } catch (AssertionError e) {
            Util.log.error("Action execution unsucssesful",e);
            fail();
        }

    }

    public void login(String email, String password) {
        try {
            Util.bot.viewByTitle(AppFactoryConstants.APPLICATIONS_LIST).show();
        } catch (WidgetNotFoundException e) {
            Util.bot.menu("Window").menu("Show View").menu("Other...").click();
            SWTBotShell showView = Util.bot.shell("Show View");
            showView.bot().text().setText(AppFactoryConstants.APPLICATIONS_LIST);
            showView.bot().button("OK").click();
        }
        try {
            Util.bot.toolbarButtonWithTooltip(AppFactoryConstants.LOGIN2).click();
            Util.checkShellLoading(AppFactoryConstants.APP_CLOUD_APP_FACTORY_LOGIN);
            SWTBotShell login = Util.bot.shell(AppFactoryConstants.APP_CLOUD_APP_FACTORY_LOGIN);
            login.bot().textWithLabel(AppFactoryConstants.EMAIL).setText(email);
            login.bot().textWithLabel(AppFactoryConstants.PASSWORD2).setText(password);
            login.bot().button(CommonConstants.OK).click();
            Util.bot.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses(login));
            Util.bot.viewByTitle(AppFactoryConstants.APPLICATIONS_LIST).show();
            Util.log.info("Login successful");
        } catch (TimeoutException e) {
            Util.log.error("Fail to login",e);
            fail();
        } catch (WidgetNotFoundException e) {
            Util.log.error("Problem with the login widget",e);
            fail();
        }

    }


}