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

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.APIMCons;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonCons;

public class APIMUtils extends Util {

    private static SWTBotTreeItem mainTree;

    public static SWTBotTreeItem apimMainTree(String userName) {
        try {
            SWTBotView apimRegistry = bot.viewByTitle(APIMCons.WSO2_API_MANAGER);
            apimRegistry.setFocus();
            SWTBotTreeItem mainTree = apimRegistry.bot().tree().getTreeItem(userName + "@https://localhost:9443/")
                    .getNode(APIMCons.REPOSITORY).getNode(APIMCons.CUSTOMSEQUENCES);
            return mainTree;
        } catch (WidgetNotFoundException e) {
            System.out.println("Cannot select the tree");
            return null;
        }
    }

    public static void expandTree(String userName) {
        mainTree = APIMUtils.apimMainTree(userName);
        mainTree.getNode("fault").expand();
        mainTree.getNode("in").expand();
        mainTree.getNode("out").expand();
    }

    public static void contextMenuCreat(String userName, String nodeName) {

    }

    public static void createSequenceAPIM(String userName, String sequenceType, String sequenceName) {

        try {
            mainTree = APIMUtils.apimMainTree(userName);
            mainTree.getNode(sequenceType).contextMenu(APIMCons.CREATE).click();
        } catch (WidgetNotFoundException e) {
            System.out.println("Problem with the tree");
            fail();
        }

        checkShellLoading(APIMCons.CREATE_SEQUENCE);
        SWTBotShell createSequenceShell = bot.shell(APIMCons.CREATE_SEQUENCE);
        try {
            createSequenceShell.bot().textWithLabel(APIMCons.SEQUENCE_NAME).setText(sequenceName);
            createSequenceShell.bot().button(CommonCons.OK).click();
            bot.waitUntil(Conditions.shellCloses(createSequenceShell));
        } catch (WidgetNotFoundException e) {
            System.out.println("");
            fail();
        } catch (TimeoutException e) {
            System.out.println("Shell didn't close correctly.");
            fail();
        }

        try {
            bot.editorByTitle(sequenceName + CommonCons.XML).show();
            bot.editorByTitle(sequenceName + CommonCons.XML).close();
        } catch (WidgetNotFoundException e) {
        }

    }

    public static void renameSequenceAPIM(String userName, String sequenceType, String sequenceName, String newName) {
        try {
            mainTree = APIMUtils.apimMainTree(userName);
            mainTree.getNode(sequenceType).getNode(sequenceName + CommonCons.XML).contextMenu("Rename   ").click();
            checkShellLoading(APIMCons.RENAME_RESOURCE);
            SWTBotShell renameShell = bot.shell(APIMCons.RENAME_RESOURCE);
            renameShell.bot().textWithLabel(APIMCons.NEW_NAME).setText(newName);
            renameShell.bot().button(CommonCons.OK).click();
            bot.waitUntil(Conditions.shellCloses(renameShell));
        } catch (WidgetNotFoundException e) {
            System.out.println("Rename Fail");
            fail();

        } catch (TimeoutException e) {
            System.out.println("Rename Fail");
            fail();

        }
    }

    public static void commitFile(String userName, String sequenceType, String sequenceName) {

        try {
            mainTree = APIMUtils.apimMainTree(userName);
            mainTree.getNode(sequenceType).getNode(sequenceName + CommonCons.XML).contextMenu(APIMCons.COMMIT_FILE)
                    .click();

            bot.button(CommonCons.YES).click();
            bot.sleep(1000);
            bot.button(CommonCons.OK).click();
            bot.sleep(1000);
        } catch (WidgetNotFoundException e) {
            System.out.println("Rename Fail");
            fail();

        } catch (TimeoutException e) {
            System.out.println("Rename Fail");
            fail();

        }

    }

    public static void discardAllChanges() {
        bot.toolbarButtonWithTooltip("Discard all local changes and synchronize with server").click();
        bot.button(CommonCons.YES).click();
    }

    public static void clickPushAllChanges() {

        bot.toolbarButtonWithTooltip(APIMCons.PUSH_ALL_CHANGES_TO_THE_SERVER).click();
        bot.button(CommonCons.YES).click();
        bot.button(CommonCons.OK).click();
    }

    public static void deleteSequenceAPIM(String userName, String sequenceType, String sequenceName) {
        mainTree = APIMUtils.apimMainTree(userName);
        mainTree.getNode(sequenceType).getNode(sequenceName + CommonCons.XML).contextMenu(APIMCons.DELETE).click();
        bot.button(CommonCons.YES).click();

    }

    public static void copyPasteSequence(String userName, String from, String to, String sequenceName) {
        mainTree = APIMUtils.apimMainTree(userName);
        mainTree.getNode(from).expand();
        mainTree.getNode(from).getNode(sequenceName + CommonCons.XML).contextMenu(APIMCons.COPY).click();
        mainTree.getNode(to).expand();
        mainTree.getNode(to).contextMenu(APIMCons.PASTE).click();
        bot.button(CommonCons.OK).click();
        bot.sleep(2000);
    }

}
