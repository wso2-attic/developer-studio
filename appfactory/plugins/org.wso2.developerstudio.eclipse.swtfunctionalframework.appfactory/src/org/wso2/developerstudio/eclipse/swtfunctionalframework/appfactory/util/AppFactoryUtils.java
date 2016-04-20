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
import org.wso2.developerstudio.eclipse.test.automation.framework.element.validator.WorkbenchElementsValidator;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.CommonConstants;
import org.wso2.developerstudio.eclipse.test.automation.utils.functional.PerspectiveLoginUtil;

public class AppFactoryUtils implements PerspectiveLoginUtil {

	public static SWTBotTreeItem appFactoryTree() {
		SWTBotView appList = WorkbenchElementsValidator.bot
				.viewByTitle(AppFactoryConstants.APPLICATIONS_LIST);
		try {
			appList.bot().tree()
					.getTreeItem(AppFactoryConstants.TEST_APPLICATION_ONE)
					.select();
			appList.bot().tree()
					.getTreeItem(AppFactoryConstants.TEST_APPLICATION_ONE)
					.contextMenu(AppFactoryConstants.OPEN).click();
			while (!appList
					.bot()
					.tree()
					.getTreeItem(
							AppFactoryConstants.TEST_APPLICATION_ONE_OPENED)
					.isVisible()) {
				WorkbenchElementsValidator.bot.sleep(1000);
			}
		} catch (WidgetNotFoundException | TimeoutException e) {
			try {
				appList.bot()
						.tree()
						.getTreeItem(
								AppFactoryConstants.TEST_APPLICATION_ONE_OPENED)
						.isVisible();
			} catch (WidgetNotFoundException | TimeoutException erro) {
				WorkbenchElementsValidator.log.error(
						"Application list loading failiure", e);
				fail();
			}

		}

		SWTBotTreeItem opened = appList.bot().tree()
				.getTreeItem(AppFactoryConstants.TEST_APPLICATION_ONE_OPENED);
		return opened;
	}

	public static void appFactoryApplicationAction(String path, String action)
			throws Exception {

		String text = "";
		String[] op = path.split("/");
		SWTBotTreeItem opened = AppFactoryUtils.appFactoryTree();
		WorkbenchElementsValidator.bot.sleep(2000);
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
			WorkbenchElementsValidator.log.error(action + " is not available",
					e);
		}
		WorkbenchElementsValidator.bot.sleep(2000);

		if (action == AppFactoryConstants.BUILD) {
			WorkbenchElementsValidator.bot.sleep(2000);
			text = "Build invoked successfully.";
		} else if (action == AppFactoryConstants.DEPLOY) {
			WorkbenchElementsValidator.bot.sleep(2000);
			text = "Successfully deployed";
		} else if (action == AppFactoryConstants.BUILD_LOGS) {
			WorkbenchElementsValidator.bot.sleep(10000);
			text = "[INFO] BUILD SUCCESS";
		} else if (action == AppFactoryConstants.CHECK_OUT) {
			WorkbenchElementsValidator.bot.sleep(3000);
			text = "Cloning completed successfully";
		} else {
			fail();

		}
		WorkbenchElementsValidator.bot.cTabItem("Console").setFocus();
		String styledText = WorkbenchElementsValidator.bot.styledText()
				.getText();
		try {
			assertContains(text, styledText);
		} catch (AssertionError e) {
			WorkbenchElementsValidator.log.error(
					"Action execution unsucssesful", e);
			fail();
		}

	}

	public void appFactoryLogin(String email, String password,
			String connectTo, String url, String path) {
		try {
			WorkbenchElementsValidator.bot.viewByTitle(
					AppFactoryConstants.APPLICATIONS_LIST).show();
		} catch (WidgetNotFoundException | TimeoutException e) {
			WorkbenchElementsValidator.bot.menu("Window").menu("Show View")
					.menu("Other...").click();
			SWTBotShell showView = WorkbenchElementsValidator.bot
					.shell("Show View");
			showView.bot().text()
					.setText(AppFactoryConstants.APPLICATIONS_LIST);
			WorkbenchElementsValidator
					.checkButton(CommonConstants.OK, showView);
			showView.bot().button("OK").click();
		}

		try {
			WorkbenchElementsValidator.bot.toolbarButtonWithTooltip(
					AppFactoryConstants.LOGIN2).click();
			WorkbenchElementsValidator
					.checkShellLoading(AppFactoryConstants.APP_CLOUD_APP_FACTORY_LOGIN);
			SWTBotShell login = WorkbenchElementsValidator.bot
					.shell(AppFactoryConstants.APP_CLOUD_APP_FACTORY_LOGIN);
			login.bot().radio(connectTo).click();
			if (connectTo != AppFactoryConstants.APP_CLOUD) {
				login(email, password, null, null);
			} else {
				login(email, password, url, null);
			}
		} catch (TimeoutException e) {
			WorkbenchElementsValidator.log.error("Fail to login", e);
			fail();
		} catch (WidgetNotFoundException e) {
			WorkbenchElementsValidator.log.error(
					"Problem with the login widget", e);
			fail();
		}

	}

	public void login(String email, String password, String url, String path) {

		try {
			SWTBotShell login = WorkbenchElementsValidator.bot
					.shell(AppFactoryConstants.APP_CLOUD_APP_FACTORY_LOGIN);
			WorkbenchElementsValidator.setLableText(login,
					AppFactoryConstants.EMAIL, email);
			WorkbenchElementsValidator.setLableText(login,
					AppFactoryConstants.PASSWORD2, password);
			if (url != null) {
				WorkbenchElementsValidator.setLableText(login,
						AppFactoryConstants.URL, password);
			}
			WorkbenchElementsValidator.checkButton(CommonConstants.OK, login);
			login.bot().button(CommonConstants.OK).click();
			WorkbenchElementsValidator.bot
					.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions
							.shellCloses(login));
			WorkbenchElementsValidator.bot.viewByTitle(
					AppFactoryConstants.APPLICATIONS_LIST).show();
			WorkbenchElementsValidator.log.info("Login successful");
		} catch (TimeoutException e) {
			WorkbenchElementsValidator.log.error("Fail to login", e);
			fail();
		} catch (WidgetNotFoundException e) {
			WorkbenchElementsValidator.log.error(
					"Problem with the login widget", e);
			fail();
		}

	}

}