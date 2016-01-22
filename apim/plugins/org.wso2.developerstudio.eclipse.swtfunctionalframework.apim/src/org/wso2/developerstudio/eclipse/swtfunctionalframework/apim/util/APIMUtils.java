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

package org.wso2.developerstudio.eclipse.swtfunctionalframework.apim.util;

import static org.junit.Assert.fail;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.PerspectiveLoginUtil;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.apim.util.constants.APIMConstants;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonConstants;

public class APIMUtils extends PerspectiveLoginUtil{

    private static SWTBotTreeItem mainTree;
    
    public static SWTBotTreeItem apimMainTree(String userName) {
        try {
            SWTBotView apimRegistry = Util.bot.viewByTitle(APIMConstants.WSO2_API_MANAGER);
            apimRegistry.setFocus();
            SWTBotTreeItem mainTree = apimRegistry.bot().tree().getTreeItem(userName + "@https://localhost:9443/")
                    .getNode(APIMConstants.REPOSITORY).getNode(APIMConstants.CUSTOMSEQUENCES);
            return mainTree;
        } catch (WidgetNotFoundException e) {
            Util.log.error("Cannot select the tree");
            return null;
        }
    }

    public static void expandTree(String userName) {
        mainTree = APIMUtils.apimMainTree(userName);
        if (!mainTree.getNode("fault").isExpanded()){
            mainTree.getNode("fault").expand();
        }
        if (!mainTree.getNode("in").isExpanded()){
            mainTree.getNode("in").expand();
        }
        if (!mainTree.getNode("out").isExpanded()){
            mainTree.getNode("out").expand();
        }  
    }

    public static void contextMenuCreat(String userName, String nodeName) {

    }

    public static void createSequenceAPIM(String userName, String sequenceType, String sequenceName) {

        try {
            mainTree = APIMUtils.apimMainTree(userName);
            mainTree.getNode(sequenceType).contextMenu(APIMConstants.CREATE).click();
        } catch (WidgetNotFoundException e) {
            Util.log.error("Problem with the tree");
            fail();
        }

        Util.checkShellLoading(APIMConstants.CREATE_SEQUENCE);
        SWTBotShell createSequenceShell = Util.bot.shell(APIMConstants.CREATE_SEQUENCE);
        try {
            createSequenceShell.bot().textWithLabel(APIMConstants.SEQUENCE_NAME).setText(sequenceName);
            createSequenceShell.bot().button(CommonConstants.OK).click();
            Util.bot.waitUntil(Conditions.shellCloses(createSequenceShell));
        } catch (WidgetNotFoundException e) {
            System.out.println("");
            fail();
        } catch (TimeoutException e) {
            Util.log.error("Shell didn't close correctly.");
            fail();
        }

        try {
            Util.bot.editorByTitle(sequenceName + CommonConstants.XML).show();
            Util.bot.editorByTitle(sequenceName + CommonConstants.XML).close();
        } catch (WidgetNotFoundException e) {
        }

    }

    public static void renameSequenceAPIM(String userName, String sequenceType, String sequenceName, String newName) {
        try {
            mainTree = APIMUtils.apimMainTree(userName);
            mainTree.getNode(sequenceType).getNode(sequenceName + CommonConstants.XML).contextMenu("Rename   ").click();
            Util.checkShellLoading(APIMConstants.RENAME_RESOURCE);
            SWTBotShell renameShell = Util.bot.shell(APIMConstants.RENAME_RESOURCE);
            renameShell.bot().textWithLabel(APIMConstants.NEW_NAME).setText(newName);
            renameShell.bot().button(CommonConstants.OK).click();
            Util.bot.waitUntil(Conditions.shellCloses(renameShell));
        } catch (WidgetNotFoundException e) {
            Util.log.error("Rename Fail");
            fail();

        } catch (TimeoutException e) {
            Util.log.error("Rename Fail");
            fail();

        }
    }

    public static void commitFile(String userName, String sequenceType, String sequenceName) {

        try {
            mainTree = APIMUtils.apimMainTree(userName);
            mainTree.getNode(sequenceType).getNode(sequenceName + CommonConstants.XML).contextMenu(APIMConstants.COMMIT_FILE)
                    .click();

            Util.bot.button(CommonConstants.YES).click();
            Util.bot.sleep(1000);
            Util.bot.button(CommonConstants.OK).click();
            Util.bot.sleep(1000);
        } catch (WidgetNotFoundException e) {
            Util.log.error("Rename Fail");
            fail();

        } catch (TimeoutException e) {
            Util.log.error("Rename Fail");
            fail();

        }

    }

    public static void discardAllChanges() {
        Util.bot.toolbarButtonWithTooltip("Discard all local changes and synchronize with server").click();
        Util.bot.button(CommonConstants.YES).click();
    }

    public static void clickPushAllChanges() {

        Util.bot.toolbarButtonWithTooltip(APIMConstants.PUSH_ALL_CHANGES_TO_THE_SERVER).click();
        Util.bot.button(CommonConstants.YES).click();
        Util.bot.button(CommonConstants.OK).click();
    }

    public static void deleteSequenceAPIM(String userName, String sequenceType, String sequenceName) {
        mainTree = APIMUtils.apimMainTree(userName);
        mainTree.getNode(sequenceType).getNode(sequenceName + CommonConstants.XML).contextMenu(APIMConstants.DELETE).click();
        Util.bot.button(CommonConstants.YES).click();

    }

    public static void copyPasteSequence(String userName, String from, String to, String sequenceName) {
        mainTree = APIMUtils.apimMainTree(userName);
        if (!mainTree.getNode(from).isExpanded()) {
            mainTree.getNode(from).expand();
        }
        mainTree.getNode(from).getNode(sequenceName + CommonConstants.XML).contextMenu(APIMConstants.COPY).click();
        if (!mainTree.getNode(to).isExpanded()) {
            mainTree.getNode(to).expand();
        }
        mainTree.getNode(to).contextMenu(APIMConstants.PASTE).click();
        Util.bot.button(CommonConstants.OK).click();
        Util.bot.sleep(2000);
    }

    public void login(String userName, String password) {
        try {
            Util.bot.toolbarButtonWithTooltip(APIMConstants.LOGIN2).click();
            Util.checkShellLoading(APIMConstants.LOGIN_TO_API_MANAGER_REGISTRY);
            SWTBotShell login = Util.bot.shell(APIMConstants.LOGIN_TO_API_MANAGER_REGISTRY);
            login.bot().textWithLabel(APIMConstants.USER_NAME).setText(userName);
            login.bot().textWithLabel(APIMConstants.PASSWORD2).setText(password);
            Util.bot.button(CommonConstants.OK).click();
            Util.bot.waitUntil(Conditions.shellCloses(login));
            Util.log.info("Login successful");

        } catch (TimeoutException e) {
            Util.log.error("Fail to login");
            fail();
        } catch (WidgetNotFoundException e) {
            Util.log.error("Problem with the login widget");
            fail();
        }

    }

}