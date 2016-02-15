package org.wso2.developerstudio.eclipse.swtfunctionalframework.registry.util;

import static org.junit.Assert.fail;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.registry.constants.RegistryConstants;
import org.wso2.developerstudio.eclipse.test.automation.framework.element.validator.WorkbenchElementsValidator;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.CommonConstants;
import org.wso2.developerstudio.eclipse.test.automation.utils.functional.PerspectiveLoginUtil;

public class RegistryUtil implements PerspectiveLoginUtil {

    private static SWTBotTreeItem mainTree;

    @Override
    public void login(String userName, String password, String url, String path) {
        try {
            WorkbenchElementsValidator.bot.toolbarButtonWithTooltip(RegistryConstants.ADD_REGISTRY).click();
            SWTBotShell login = WorkbenchElementsValidator.bot.shell(RegistryConstants.ADD_REGISTRY2);
            WorkbenchElementsValidator.setLableText(login, RegistryConstants.USER_NAME, userName);
            WorkbenchElementsValidator.setLableText(login, RegistryConstants.PASSWORD2, password);
            if (url != null) {
                WorkbenchElementsValidator.setLableText(login, RegistryConstants.URL2, url);
            }
            if (path != null) {
                WorkbenchElementsValidator.setLableText(login, RegistryConstants.PATH2, path);
            }
            WorkbenchElementsValidator.checkButton(CommonConstants.OK, login);
            login.bot().button(CommonConstants.OK).click();
            WorkbenchElementsValidator.bot.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses(login));
            WorkbenchElementsValidator.log.info("Login successful");
        } catch (TimeoutException e) {
            WorkbenchElementsValidator.log.error("Fail to login", e);
            fail();
        } catch (WidgetNotFoundException e) {
            WorkbenchElementsValidator.log.error("Problem with the login widget", e);
            fail();
        }

    }

    public static SWTBotTreeItem regMainTree(String userName, String url) {
        try {
            SWTBotView apimRegistry = WorkbenchElementsValidator.bot
                    .viewByTitle(RegistryConstants.WSO2_REGISTRY_BROWSER);
            apimRegistry.setFocus();
            SWTBotTreeItem mainTree = apimRegistry.bot().tree().getTreeItem(userName + "@" + url);
            return mainTree;
        } catch (WidgetNotFoundException e) {
            WorkbenchElementsValidator.log.error("Cannot select the tree", e);
            return null;
        }
    }

    public static void expandTree(String userName, String url) {
        mainTree = RegistryUtil.regMainTree(userName, url);
        mainTree.expand();
    }

}
