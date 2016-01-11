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

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.forms.finder.SWTFormsBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.*;

public class AbstractClass extends Util {

    public static void openProjectFromMenu(String projectType) {
        bot.sleep(5000);
        try {
            bot.toolbarDropDownButtonWithTooltip(CommonCons.NEW).click();
            checkShellLoading(CommonCons.NEW);
            final SWTBotShell newProject = bot.shell(CommonCons.NEW);
            newProject.bot().textWithLabel(CommonCons.WIZARDS).setText(projectType);
            bot.sleep(2000);
            checkButton(CommonCons.NEXT, newProject);
            newProject.bot().button(CommonCons.NEXT).click();
            log.error("Openning project from menu sucssecfull");
        } catch (Exception e) {
            log.error("Opening project from menu failed", e);
            fail();
        } catch (Error erro) {
            log.error("Opening project from menu failed", erro);
            fail();
        }
    }

    public static void openFromDash(String project) throws Exception {
        bot.sleep(2000);
        try {
            bot.menu(CommonCons.DEVELOPER_STUDIO).menu(CommonCons.OPEN_DASHBOARD).click();
        } catch (Exception e) {
            log.error("Problem in openning dashboard", e);
        }
        try {
            SWTBotEditor dashBoard = bot.editorByTitle(CommonCons.DEVELOPER_STUDIO_DASHBOARD);
            dashBoard.show();
            SWTFormsBot form = new SWTFormsBot();
            form.imageHyperlink(project).click();
            log.error("Openning project from dashboard sucssecfull");
        } catch (Exception e) {
            log.error("Problem with opennig the project", e);
        }

    }

    public static void openEditor(String projectName, String[] path, String fileName) {
        SWTBotTreeItem tree = bot.tree().getTreeItem(projectName);
        try {
            bot.viewByTitle("Project Explorer").show();
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
        	log.error("Cannot open the editor", e);
            fail();

        }

    }

    public static void validateJavaClass(String packageName, String className) {

        actual = bot.editorByTitle(className + CommonCons.JAVA).bot().styledText().getText();
        expect = "package " + packageName + ";\n\npublic class " + className + "{\n\n}";
        assertContains(actual, expect);
        log.error("Validation sucsessful");
    }

    public static void changePerspective() {
        try {
            changePerspective = bot.shell(CommonCons.OPEN_ASSOCIATED_PERSPECTIVE);
            if (changePerspective.isActive()) {
                changePerspective.bot().button(CommonCons.YES).click();
                bot.waitUntil(Conditions.shellCloses(changePerspective));
            }
        } catch (WidgetNotFoundException e) {
            log.error("Project unsucsessful", e);
            fail();
        }
    }


    public static void createSqlFile(String filename) {

        checkShellLoading(SqlFileCons.NEW_SQL_FILE);
        SWTBotShell newSql = bot.shell(SqlFileCons.NEW_SQL_FILE);
        newSql.bot().textWithLabel(SqlFileCons.FILE_NAME).setText(filename);
        newSql.bot().button(CommonCons.FINISH).click();
        bot.waitUntil(Conditions.shellCloses(newSql));
        try {
            bot.editorByTitle(filename + CommonCons.SQL).show();
            log.error("Create Sql File Sucsessful");
        } catch (WidgetNotFoundException e) {
            log.error("Editor didnt load", e);
            fail();
        }
    }
    
    public static void expandProject(String projectName, String[] path){
        
        bot.sleep(2000);
        try {
            SWTBotTreeItem tree = bot.tree().getTreeItem(projectName);
            bot.viewByTitle("Project Explorer").show();
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
            log.error("Cannot open the project", e);
            fail();
        }
        
    }

    public static void openProjectFromRightClick(String projectName, String[] path, String projectType) {

        bot.sleep(2000);
        try {
            SWTBotTreeItem tree = bot.tree().getTreeItem(projectName);
            bot.viewByTitle("Project Explorer").show();
            if (!tree.isExpanded()) {
                tree.select();
                tree.expand();
            }
            for (String element : path) {
                tree.getNode(element).expand();
                tree.getNode(element).select();
                tree = tree.getNode(element);
            }
            tree.contextMenu("New").menu(projectType).click();
        } catch (WidgetNotFoundException e) {
            log.error("Cannot open the project", e);
            fail();

        } catch (Error erro) {
            log.error("Cannot open the project", erro);
            fail();
        }
    }

    public static void openProjectFromRightClick(String projectName, String projectType) {
        bot.sleep(2000);
        try {
            bot.viewByTitle("Project Explorer").show();
            bot.tree().getTreeItem(projectName).select();
            bot.tree().getTreeItem(projectName).contextMenu("New").menu(projectType).click();
        } catch (WidgetNotFoundException e) {
            log.error("Cannot open the project", e);
            System.out.println("Cannot open the project");
            fail();

        }
    }

    public static void createJavaClass(String className) {
        bot.sleep(2000);
        checkShellLoading(JavaCons.NEW_JAVA_CLASS);
        SWTBotShell newClass = bot.shell(JavaCons.NEW_JAVA_CLASS);
        newClass.bot().textWithLabel(JavaCons.NA_ME).setText(className);
        newClass.bot().button(CommonCons.FINISH).click();
        bot.waitUntil(Conditions.shellCloses(newClass));
    }

    public static void openPerspective(String perspective) {
        bot.sleep(2000);
        try {
            bot.toolbarButtonWithTooltip(CommonCons.OPEN_PERSPECTIVE).click();
            checkShellLoading(CommonCons.OPEN_PERSPECTIVE);
            SWTBotShell openPerspectiveShell = bot.shell(CommonCons.OPEN_PERSPECTIVE);
            openPerspectiveShell.bot().table().select(perspective);
            openPerspectiveShell.bot().button(CommonCons.OK).click();
            bot.waitUntil(Conditions.shellCloses(openPerspectiveShell));
        }

        catch (WidgetNotFoundException e) {
            log.error("Widget cannot be load", e);
            fail();
        }
    }

    public static void deleteWithContent(String projectName) {
    	try{
    		bot.saveAllEditors();
    		bot.closeAllEditors();
    	}catch(Exception e){
    		
    	}
        try {
        	bot.viewByTitle("Project Explorer").setFocus();
            
            bot.tree().getTreeItem(projectName).select();
            bot.tree().getTreeItem(projectName).contextMenu("Delete").click();
            bot.sleep(2000);
        } catch (WidgetNotFoundException e) {
            log.error("Project not found", e);
            fail();
        }
        try {
            SWTBotShell deleteShell = bot.shell(CommonCons.DELETE_RESOURCES);
            deleteShell.bot().checkBox(CommonCons.DELETE_PROJECT_CONTENTS_ON_DISK_CANNOT_BE_UNDONE).click();
            deleteShell.bot().button(CommonCons.OK).click();
            bot.button(CommonCons.CONTINUE).click();
            log.error("Delete successful");
        } catch (Exception e) {
            log.error("Delete unsuccessful", e);
            fail();
        }
    }

    public static void deleteWithoutContent(String projectName) {

        try {
            bot.saveAllEditors();
            bot.tree().getTreeItem(projectName).contextMenu("Delete").click();
        } catch (WidgetNotFoundException e) {
            log.error("Project not found", e);
            fail();
        }
        try {
            checkShellLoading(CommonCons.DELETE_RESOURCES);
            SWTBotShell deleteShell = bot.shell(CommonCons.DELETE_RESOURCES);
            deleteShell.bot().button(CommonCons.OK).click();
            bot.button(CommonCons.CONTINUE).click();
            log.error("Delete sucsessful");
        } catch (Exception e) {
            log.error("Delete unsucsessful", e);
            fail();
        }
    }

    public static SWTBotTreeItem projectExplorer(String projectName, String packageName) {
        try {
            bot.tree().getTreeItem(projectName).expand();
            bot.tree().getTreeItem(projectName).getNode("src/main/java").expand();
            bot.tree().getTreeItem(projectName).getNode("src/main/java").getNode(packageName).select();
            log.info("Tree selection sucsessful");
            return bot.tree().getTreeItem(projectName).getNode("src/main/java").getNode(packageName);
        } catch (Exception e) {
            log.error("Problem with tree selection", e);
            fail();
            return null;
        }
    }

    public static void activateCtab(String tabName) {
        try {
            bot.cTabItem(tabName).activate();
        } catch (WidgetNotFoundException e) {
            log.error("Ctab cannot be found", e);
            fail();
        }
    }

    public static void closeView(String viewName) {
    	try {
        bot.viewByTitle(viewName).close();
    	} catch(Exception e){
    		log.error("Fail to close the view", e);
    		fail();
    	}
    }

}
