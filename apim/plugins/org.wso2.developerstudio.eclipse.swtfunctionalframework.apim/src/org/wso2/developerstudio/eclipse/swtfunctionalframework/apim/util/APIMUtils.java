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

public class APIMUtils implements PerspectiveLoginUtil {

    private static SWTBotTreeItem mainTree;

    public static SWTBotTreeItem apimMainTree(String userName, String url) {
        try {
            SWTBotView apimRegistry = Util.bot.viewByTitle(APIMConstants.WSO2_API_MANAGER);
            apimRegistry.setFocus();
            SWTBotTreeItem mainTree = apimRegistry.bot().tree().getTreeItem(userName + "@" + url)
                    .getNode(APIMConstants.REPOSITORY).getNode(APIMConstants.CUSTOMSEQUENCES);
            return mainTree;
        } catch (WidgetNotFoundException e) {
            Util.log.error("Cannot select the tree", e);
            return null;
        }
    }

    public static void expandTree(String userName, String url) {
        mainTree = APIMUtils.apimMainTree(userName, url);
        if (!mainTree.getNode(APIMConstants.SEQUENCE_TYPE_FAULT).isExpanded()) {
            mainTree.getNode(APIMConstants.SEQUENCE_TYPE_FAULT).expand();
        }
        if (!mainTree.getNode(APIMConstants.SEQUENCE_TYPE_IN).isExpanded()) {
            mainTree.getNode(APIMConstants.SEQUENCE_TYPE_IN).expand();
        }
        if (!mainTree.getNode(APIMConstants.SEQUENCE_TYPE_OUT).isExpanded()) {
            mainTree.getNode(APIMConstants.SEQUENCE_TYPE_OUT).expand();
        }
    }

    /**
     * This method will create a sequence
     *
     * @param String
     *            userName Username that used to login
     * @param String
     *            sequenceType Sequence type want to create
     * @param String
     *            sequenceName Name of the sequence
     */
    public static void createSequenceAPIM(String userName, String url, String sequenceType, String sequenceName) {

        try {
            mainTree = APIMUtils.apimMainTree(userName, url);
            mainTree.getNode(sequenceType).contextMenu(APIMConstants.CREATE).click();
        } catch (WidgetNotFoundException e) {
            Util.log.error("Problem with the tree", e);
            fail();
        }

        Util.checkShellLoading(APIMConstants.CREATE_SEQUENCE);
        SWTBotShell createSequenceShell = Util.bot.shell(APIMConstants.CREATE_SEQUENCE);
        try {
            Util.setLableText(createSequenceShell, APIMConstants.SEQUENCE_NAME, sequenceName);
            Util.checkButton(CommonConstants.OK, createSequenceShell);
            createSequenceShell.bot().button(CommonConstants.OK).click();
            Util.bot.waitUntil(Conditions.shellCloses(createSequenceShell));
        } catch (WidgetNotFoundException e) {
            Util.log.error("Problem with the shell", e);
            fail();
        } catch (TimeoutException e) {
            Util.log.error("Shell didn't close correctly.", e);
            fail();
        }

        try {
            Util.bot.editorByTitle(sequenceName + CommonConstants.XML).show();
            Util.bot.editorByTitle(sequenceName + CommonConstants.XML).close();
        } catch (WidgetNotFoundException e) {
        }

    }

    /**
     * This method will rename a sequence
     *
     * @param String
     *            userName Username that used to login
     * @param String
     *            sequenceType Sequence type want to create
     * @param String
     *            sequenceName Current name of the sequence
     * @param String
     *            sequenceName New name of the sequence
     */
    public static void renameSequenceAPIM(String userName, String url, String sequenceType, String sequenceName,
            String newName) {
        try {
            mainTree = APIMUtils.apimMainTree(userName, url);
            mainTree.getNode(sequenceType).getNode(sequenceName + CommonConstants.XML)
                    .contextMenu(APIMConstants.RENAME).click();
            Util.checkShellLoading(APIMConstants.RENAME_RESOURCE);
            SWTBotShell renameShell = Util.bot.shell(APIMConstants.RENAME_RESOURCE);
            Util.setLableText(renameShell, APIMConstants.NEW_NAME,newName);
            Util.checkButton(CommonConstants.OK, renameShell);
            renameShell.bot().button(CommonConstants.OK).click();
            Util.bot.waitUntil(Conditions.shellCloses(renameShell));
        } catch (WidgetNotFoundException e) {
            Util.log.error("Rename Fail", e);
            fail();

        } catch (TimeoutException e) {
            Util.log.error("Rename Fail", e);
            fail();

        }
    }

    /**
     * This method will commit a file that has uncommit changes
     *
     * @param userName
     *            userName User name that use to login
     * @param sequenceType
     *            Type of the sequence want to commit
     * @param sequenceName
     *            Name of the sequence want to commit
     */
    public static void commitFile(String userName, String url, String sequenceType, String sequenceName) {

        try {
            mainTree = APIMUtils.apimMainTree(userName, url);
            mainTree.getNode(sequenceType).getNode(sequenceName + CommonConstants.XML)
                    .contextMenu(APIMConstants.COMMIT_FILE).click();
            Util.bot.button(CommonConstants.YES).click();
            Util.bot.sleep(1000);
            Util.bot.button(CommonConstants.OK).click();
            Util.bot.sleep(1000);
        } catch (WidgetNotFoundException e) {
            Util.log.error("Commiting Fail", e);
            fail();

        } catch (TimeoutException e) {
            Util.log.error("Commiting Fail", e);
            fail();

        }

    }

    /**
     * This method will discard all the unsaved changes
     */
    public static void discardAllChanges() {
        Util.bot.toolbarButtonWithTooltip(APIMConstants.DISCARD_ALL_LOCAL_CHANGES_AND_SYNCHRONIZE_WITH_SERVER).click();
        Util.bot.button(CommonConstants.YES).click();
    }

    /**
     * This method will click push all changes button save all the changes that
     * have done
     */
    public static void clickPushAllChanges() {

        Util.bot.toolbarButtonWithTooltip(APIMConstants.PUSH_ALL_CHANGES_TO_THE_SERVER).click();
        Util.bot.button(CommonConstants.YES).click();
        Util.bot.button(CommonConstants.OK).click();
    }

    /**
     * This method will delete a specified sequence
     *
     * @param userName
     *            User name that use to login
     * @param sequenceType
     *            What is the type of the specified sequence
     * @param sequenceName
     *            Name of the sequence that want to delete
     */
    public static void deleteSequenceAPIM(String userName, String url, String sequenceType, String sequenceName) {
        mainTree = APIMUtils.apimMainTree(userName, url);
        mainTree.getNode(sequenceType).getNode(sequenceName + CommonConstants.XML).contextMenu(APIMConstants.DELETE)
                .click();
        Util.bot.button(CommonConstants.YES).click();

    }

    /**
     * This method will copy a specific sequence and paste it to a specific
     * location
     *
     * @param userName
     *            User name that use to login
     * @param from
     *            From where the sequence should be copied
     * @param to
     *            To where the copied sequence must be pasted to
     * @param sequenceName
     *            The name of the sequence that should copy and paste
     */
    public static void copyPasteSequence(String userName, String url, String from, String to, String sequenceName) {
        mainTree = APIMUtils.apimMainTree(userName, url);
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

    @Override
    public void login(String userName, String password, String url, String path) {
        try {
            Util.bot.toolbarButtonWithTooltip(APIMConstants.LOGIN2).click();
            Util.checkShellLoading(APIMConstants.LOGIN_TO_API_MANAGER_REGISTRY);
            SWTBotShell login = Util.bot.shell(APIMConstants.LOGIN_TO_API_MANAGER_REGISTRY);
            Util.setLableText(login, APIMConstants.USER_NAME, userName);
            Util.setLableText(login, APIMConstants.PASSWORD2, password);
            if (url != null) {
                Util.setLableText(login, APIMConstants.URL, url);
            }
            Util.checkButton(CommonConstants.OK, login);
            Util.bot.button(CommonConstants.OK).click();
            Util.bot.waitUntil(Conditions.shellCloses(login));
            Util.log.info("Login successful");

        } catch (TimeoutException e) {
            Util.log.error("Fail to login", e);
            fail();
        } catch (WidgetNotFoundException e) {
            Util.log.error("Problem with the login widget", e);
            fail();
        }

    }

}