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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.forms.finder.SWTFormsBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.*;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;

public class CommonUtil {

    protected static SWTBotTreeItem tree;

    /**
     * This method will open the a specified project creation
     * wizard from the new project button on the tool bar.
     * 
     * @param projectType Project type that want to create
     */
    public static void openProjectFromMenu(String projectType) {
        Util.bot.sleep(5000);
        try {
            Util.bot.toolbarDropDownButtonWithTooltip(CommonConstants.NEW).click();
            Util.checkShellLoading(CommonConstants.NEW);
            final SWTBotShell newProject = Util.bot.shell(CommonConstants.NEW);
            newProject.bot().textWithLabel(CommonConstants.WIZARDS).setText(projectType);
            Util.bot.sleep(2000);
            Util.checkButton(CommonConstants.NEXT, newProject);
            newProject.bot().button(CommonConstants.NEXT).click();
            Util.log.info("Openning project from menu sucssecfull");
        } catch (Exception e) {
            Util.log.error("Opening project from menu failed", e);
            fail();
        } catch (Error erro) {
            Util.log.error("Opening project from menu failed", erro);
            fail();
        }
    }

    /**
     * This method will open the a specified project creation
     * wizard from the Dash board of Developer Studio.
     * 
     * @param projectType Project type that want to create
     */
    public static void openFromDash(String project) throws Exception {
        Util.bot.sleep(6000);
        try {
            Util.bot.menu(CommonConstants.DEVELOPER_STUDIO).menu(CommonConstants.OPEN_DASHBOARD).click();
        } catch (Exception e) {
            Util.log.error("Problem in openning dashboard", e);
        }
        try {
            SWTBotEditor dashBoard = Util.bot.editorByTitle(CommonConstants.DEVELOPER_STUDIO_DASHBOARD);
            dashBoard.show();
            SWTFormsBot form = new SWTFormsBot();
            form.imageHyperlink(project).click();
            Util.log.info("Openning project from dashboard sucssecfull");
        } catch (Exception e) {
            Util.log.error("Problem with opennig the project", e);
        }

    }

    /**
     * This method will open a file in a already created project
     * in a editor.
     * 
     * @param projectName Name of the project that the file is in.
     * @param path Path to the file excluding the project name and the file name.
     * @param fileName Name of the file that need to be opened.
     */
    public static void openEditor(String projectName, String[] path, String fileName) {
        tree = Util.bot.tree().getTreeItem(projectName);
        try {
            Util.bot.viewByTitle(CommonConstants.PROJECT_EXPLORER).show();
            if (!tree.isExpanded()) {
                tree.select();
                tree.expand();
            }
            for (String element : path) {
                tree.getNode(element).expand();
                tree.getNode(element).select();
                tree = tree.getNode(element);
            }
            tree.getNode(fileName).doubleClick();
        } catch (WidgetNotFoundException e) {
            Util.log.error("Cannot open the editor", e);
            fail();

        }

    }

    /**
     * This method will validate the content of a source.
     * 
     * @param editorName Name of the editor that must validate
     * @param expectedContent expected content in the source
     */
    public static void contentValidation(String editorName, String expectedContent) {
        Util.actual = Util.bot.editorByTitle(editorName).bot().styledText().getText();
        Util.expect = expectedContent;
        try {
            assertContains(Util.actual, Util.expect);
            Util.log.info("Validation sucsessful");
        } catch (AssertionError e) {
            Util.log.error("Content dosen't match", e);
        }
    }

    /**
     * This method will change the perspective if the change perspective wizard
     * appear after creating a project.
     */
    public static void changePerspective() {
        try {
            Util.changePerspective = Util.bot.shell(CommonConstants.OPEN_ASSOCIATED_PERSPECTIVE);
            if (Util.changePerspective.isActive()) {
                Util.changePerspective.bot().button(CommonConstants.YES).click();
                Util.bot.waitUntil(Conditions.shellCloses(Util.changePerspective));
            }
        } catch (WidgetNotFoundException e) {
            Util.log.error("Project unsucsessful", e);
            fail();
        }
    }

    public static void createSqlFile(String filename) {

        Util.checkShellLoading(SqlFileConstants.NEW_SQL_FILE);
        SWTBotShell newSql = Util.bot.shell(SqlFileConstants.NEW_SQL_FILE);
        newSql.bot().textWithLabel(SqlFileConstants.FILE_NAME).setText(filename);
        newSql.bot().button(CommonConstants.FINISH).click();
        Util.bot.waitUntil(Conditions.shellCloses(newSql));
        try {
            Util.bot.editorByTitle(filename + CommonConstants.SQL).show();
            Util.log.info("Create Sql File Sucsessful");
        } catch (WidgetNotFoundException e) {
            Util.log.error("Editor didnt load", e);
            fail();
        }
    }

    /**
     * This method will expand a path from the project tree that user specifies.
     * 
     * @param projectName Name of the project that has the path to expand
     * @param path Path to expand excluding project name
     */
    public static void expandProject(String projectName, String[] path) {

        Util.bot.sleep(2000);
        try {
            tree = Util.bot.tree().getTreeItem(projectName);
            Util.bot.viewByTitle(CommonConstants.PROJECT_EXPLORER).show();
            if (!tree.isExpanded()) {
                tree.select();
                tree.expand();
            }
            for (String element : path) {
                if (!tree.getNode(element).isExpanded()) {
                    tree.getNode(element).expand();
                    tree.getNode(element).select();
                }
                tree = tree.getNode(element);
            }
        } catch (WidgetNotFoundException | Error e) {
            Util.log.error("Cannot open the project", e);
            fail();
        }

    }

    /**
     * This method will expand the specified path and return the last node of that path
     * 
     * @param projectName Name of the project that has the path to expand
     * @param path Path to expand excluding project name
     * @return SWTBotTreeItem Last node will be returned
     */
    public static SWTBotTreeItem getexpandProjecttree(String projectName, String[] path) {

        Util.bot.sleep(2000);
        try {
            tree = Util.bot.tree().getTreeItem(projectName);
            Util.bot.viewByTitle(CommonConstants.PROJECT_EXPLORER).show();
            if (!tree.isExpanded()) {
                tree.select();
                tree.expand();
            }
            for (String element : path) {
                if (!tree.getNode(element).isExpanded()) {
                    tree.getNode(element).expand();
                    tree.getNode(element).select();
                }
                tree = tree.getNode(element);
            }
            return tree;
        } catch (WidgetNotFoundException | Error e) {
            Util.log.error("Cannot open the project", e);
            fail();
            return null;
        }

    }

    /**
     * This method will open a specified project by right clicking a given path.
     * 
     * @param projectName Name of the project that already created.
     * @param path Path that new project must be created excluding the main project name.
     * @param projectType Type of project that should be created.
     */
    public static void openProjectFromRightClick(String projectName, String[] path, String projectType) {

        Util.bot.sleep(2000);
        tree = getexpandProjecttree(projectName, path);
        try {
            tree.contextMenu(CommonConstants.NEW).menu(projectType).click();
        } catch (WidgetNotFoundException e) {
            Util.log.error("Cannot open the project", e);
            fail();

        } catch (Error erro) {
            Util.log.error("Cannot open the project", erro);
            fail();
        }
    }

    public static void openProjectFromRightClick(String projectName, String projectType) {
        Util.bot.sleep(2000);
        try {
            Util.bot.viewByTitle(CommonConstants.PROJECT_EXPLORER).show();
            Util.bot.tree().getTreeItem(projectName).select();
            Util.bot.tree().getTreeItem(projectName).contextMenu(CommonConstants.NEW).menu(projectType).click();
        } catch (WidgetNotFoundException e) {
            Util.log.error("Cannot open the project", e);
            fail();

        }
    }

    public static void createJavaClass(String className) {
        Util.bot.sleep(2000);
        Util.checkShellLoading(JavaConstants.NEW_JAVA_CLASS);
        SWTBotShell newClass = Util.bot.shell(JavaConstants.NEW_JAVA_CLASS);
        newClass.bot().textWithLabel(JavaConstants.NA_ME).setText(className);
        newClass.bot().button(CommonConstants.FINISH).click();
        Util.bot.waitUntil(Conditions.shellCloses(newClass));
    }

    /**
     * This method will open a specified perspective
     * 
     * @param perspective Name of the perspective
     */
    public static void openPerspective(String perspective) {
        Util.bot.sleep(2000);
        try {
            Util.bot.toolbarButtonWithTooltip(CommonConstants.OPEN_PERSPECTIVE).click();
            Util.checkShellLoading(CommonConstants.OPEN_PERSPECTIVE);
            SWTBotShell openPerspectiveShell = Util.bot.shell(CommonConstants.OPEN_PERSPECTIVE);
            openPerspectiveShell.bot().table().select(perspective);
            openPerspectiveShell.bot().button(CommonConstants.OK).click();
            Util.bot.waitUntil(Conditions.shellCloses(openPerspectiveShell));
        }

        catch (WidgetNotFoundException e) {
            Util.log.error("Widget cannot be load", e);
            fail();
        }
    }

    /**
     * This method will delete a project with its content in the hard disk
     * 
     * @param projectName Name of the project that you want to delete
     */
    public static void deleteWithContent(String projectName) {
        try {
            Util.bot.saveAllEditors();
            Util.bot.closeAllEditors();
        } catch (Exception e) {

        }
        try {
            Util.bot.viewByTitle(CommonConstants.PROJECT_EXPLORER).setFocus();

            Util.bot.tree().getTreeItem(projectName).select();
            Util.bot.tree().getTreeItem(projectName).contextMenu(CommonConstants.DELETE).click();
            Util.bot.sleep(2000);
        } catch (WidgetNotFoundException e) {
            Util.log.error("Project not found", e);
            fail();
        }
        try {
            SWTBotShell deleteShell = Util.bot.shell(CommonConstants.DELETE_RESOURCES);
            deleteShell.bot().checkBox(CommonConstants.DELETE_PROJECT_CONTENTS_ON_DISK_CANNOT_BE_UNDONE).click();
            deleteShell.bot().button(CommonConstants.OK).click();
            Util.bot.button(CommonConstants.CONTINUE).click();
            Util.log.info("Delete successful");
        } catch (Exception e) {
            Util.log.error("Delete unsuccessful", e);
            fail();
        }
    }

    /**
     * This method will delete a specified project without its content in the hard disk
     * 
     * @param projectName Name of the project that you want to delete
     */
    public static void deleteWithoutContent(String projectName) {
        try {
            Util.bot.saveAllEditors();
            Util.bot.tree().getTreeItem(projectName).contextMenu(CommonConstants.DELETE).click();
        } catch (WidgetNotFoundException e) {
            Util.log.error("Project not found", e);
            fail();
        }
        try {
            Util.checkShellLoading(CommonConstants.DELETE_RESOURCES);
            SWTBotShell deleteShell = Util.bot.shell(CommonConstants.DELETE_RESOURCES);
            deleteShell.bot().button(CommonConstants.OK).click();
            Util.bot.button(CommonConstants.CONTINUE).click();
            Util.log.info("Delete sucsessful");
        } catch (Exception e) {
            Util.log.error("Delete unsucsessful", e);
            fail();
        }
    }

    public static SWTBotTreeItem projectExplorer(String projectName, String packageName) {
        try {
            Util.bot.tree().getTreeItem(projectName).expand();
            Util.bot.tree().getTreeItem(projectName).getNode("src/main/java").expand();
            Util.bot.tree().getTreeItem(projectName).getNode("src/main/java").getNode(packageName).select();
            Util.log.info("Tree selection sucsessful");
            return Util.bot.tree().getTreeItem(projectName).getNode("src/main/java").getNode(packageName);
        } catch (Exception e) {
            Util.log.error("Problem with tree selection", e);
            fail();
            return null;
        }
    }

    /**
     * This method will set focus to specified ctab in the editor
     * 
     * @param tabName Name of the tab
     */
    public static void activateCtab(String tabName) {
        try {
            Util.bot.cTabItem(tabName).activate();
        } catch (WidgetNotFoundException e) {
            Util.log.error("Ctab cannot be found", e);
            fail();
        }
    }

    /**
     * This method will close a already open view
     * 
     * @param viewName Name of the view that want to be closed
     */
    public static void closeView(String viewName) {
        try {
            Util.bot.viewByTitle(viewName).close();
        } catch (Exception e) {
            Util.log.error("Fail to close the view", e);
            fail();
        }
    }

    /**
     * This method will save a already open editor
     * 
     * @param editorName Name of the editor that wanted to be saved
     */
    public static void saveEditor(String editorName) {
        Util.bot.editorByTitle(editorName).save();
    }

    /**
     * This method will validate the main project structure
     * 
     * @param projectName The name of the project that should be validated
     * @param expectedFiles Expected project structure
     */
    public static void projectValidation(String projectName, List<String> expectedFiles) {

        Util.main = Util.bot.tree().getTreeItem(projectName);
        Util.bot.sleep(5000);
        Util.main.expand();
        Util.bot.sleep(5000);
        Util.actualFiles = Util.main.expand().getNodes();
        Util.bot.sleep(5000);
        assertNotSame("Main project creation faliure", expectedFiles, Util.actualFiles);

    }

    /**
     * This method will validate structure inside of a specific node in the project
     * 
     * @param projectName Name of the project that has to validate
     * @param path Path that want to validate
     * @param expectedFiles Expected structure
     */
    public static void projectValidation(String projectName, String[] path, List<String> expectedFiles) {

        Util.main = CommonUtil.getexpandProjecttree(projectName, path);
        Util.bot.sleep(5000);
        Util.actualFiles = Util.main.getNodes();
        Util.bot.sleep(5000);
        assertNotSame("Inside folder creation faliure", expectedFiles, Util.actualFiles);
    }

}
