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

package org.wso2.developerstudio.eclipse.test.automation.utils.functional;

import static org.junit.Assert.fail;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.forms.finder.SWTFormsBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.wso2.developerstudio.eclipse.test.automation.framework.element.validator.WorkbenchElementsValidator;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.CommonConstants;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.JavaConstants;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.SqlFileConstants;

public class FunctionalUtil {

	protected static SWTBotTreeItem tree;

	/**
	 * This method will open the a specified project creation wizard from the
	 * new project button on the tool bar.
	 * 
	 * @param projectType
	 *            Project type that want to create
	 */
	public static void openProjectCreationWizardFromMenu(String projectType) {
		WorkbenchElementsValidator.bot.sleep(5000);
		try {
			WorkbenchElementsValidator.bot.toolbarDropDownButtonWithTooltip(
					CommonConstants.NEW).click();
			WorkbenchElementsValidator.checkShellLoading(CommonConstants.NEW);
			final SWTBotShell newProject = WorkbenchElementsValidator.bot
					.shell(CommonConstants.NEW);
			newProject.bot().textWithLabel(CommonConstants.WIZARDS)
					.setText(projectType);
			WorkbenchElementsValidator.bot.sleep(2000);
			WorkbenchElementsValidator.checkButton(CommonConstants.NEXT,
					newProject);
			newProject.bot().button(CommonConstants.NEXT).click();
			WorkbenchElementsValidator.log
					.info("Openning project from menu successfull");
		} catch (Exception e) {
			WorkbenchElementsValidator.log.error(
					"Opening project from menu failed", e);
			fail();
		} catch (Error erro) {
			WorkbenchElementsValidator.log.error(
					"Opening project from menu failed", erro);
			fail();
		}
	}

	/**
	 * This method will open the a specified project creation wizard from the
	 * Dash board of Developer Studio.
	 * 
	 * @param projectType
	 *            Project type that want to create
	 */
	public static void openProjectCreationWizardFromDashboard(String project)
			throws Exception {
		WorkbenchElementsValidator.bot.sleep(6000);
		try {
			WorkbenchElementsValidator.bot
					.menu(CommonConstants.DEVELOPER_STUDIO)
					.menu(CommonConstants.OPEN_DASHBOARD).click();
		} catch (Exception e) {
			WorkbenchElementsValidator.log.error(
					"Problem in openning dashboard", e);
		}
		try {
			SWTBotEditor dashBoard = WorkbenchElementsValidator.bot
					.editorByTitle(CommonConstants.DEVELOPER_STUDIO_DASHBOARD);
			dashBoard.show();
			SWTFormsBot form = new SWTFormsBot();
			form.imageHyperlink(project).click();
			WorkbenchElementsValidator.log
					.info("Openning project from dashboard successfull");
		} catch (Exception e) {
			WorkbenchElementsValidator.log.error(
					"Problem with opennig the project", e);
		}

	}

	/**
	 * This method will open a file in a already created project in a editor.
	 * 
	 * @param projectName
	 *            Name of the project that the file is in.
	 * @param path
	 *            Path to the file excluding the project name and the file name.
	 * @param fileName
	 *            Name of the file that need to be opened.
	 */
	public static void openEditor(String projectName, String[] path,
			String fileName) {
		tree = WorkbenchElementsValidator.bot.tree().getTreeItem(projectName);
		try {
			WorkbenchElementsValidator.bot.viewByTitle(
					CommonConstants.PROJECT_EXPLORER).show();
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
			WorkbenchElementsValidator.log.error("Cannot open the editor", e);
			fail();

		}

	}

	/**
	 * This method will change the perspective if the change perspective wizard
	 * appear after creating a project.
	 */
	public static void switchPerspectiveInProjectCreation() {
		try {
			WorkbenchElementsValidator.changePerspective = WorkbenchElementsValidator.bot
					.shell(CommonConstants.OPEN_ASSOCIATED_PERSPECTIVE);
			if (WorkbenchElementsValidator.changePerspective.isActive()) {
				WorkbenchElementsValidator.changePerspective.bot()
						.button(CommonConstants.YES).click();
				WorkbenchElementsValidator.bot
						.waitUntil(Conditions
								.shellCloses(WorkbenchElementsValidator.changePerspective));
			}
		} catch (WidgetNotFoundException e) {
			WorkbenchElementsValidator.log.error("Switching unsuccessful", e);
			fail();
		}
	}

	/**
	 * This methods created a sql file
	 * 
	 * @param filename
	 *            file name
	 */
	public static void createSqlFile(String filename) {

		WorkbenchElementsValidator
				.checkShellLoading(SqlFileConstants.NEW_SQL_FILE);
		SWTBotShell newSql = WorkbenchElementsValidator.bot
				.shell(SqlFileConstants.NEW_SQL_FILE);
		newSql.bot().textWithLabel(SqlFileConstants.FILE_NAME)
				.setText(filename);
		newSql.bot().button(CommonConstants.FINISH).click();
		WorkbenchElementsValidator.bot
				.waitUntil(Conditions.shellCloses(newSql));
		try {
			WorkbenchElementsValidator.bot.editorByTitle(
					filename + CommonConstants.SQL).show();
			WorkbenchElementsValidator.log.info("Create Sql File successfully");
		} catch (WidgetNotFoundException e) {
			WorkbenchElementsValidator.log.error("Editor didnt load", e);
			fail();
		}
	}

	/**
	 * This method will expand the specified path and return the last node of
	 * that path
	 * 
	 * @param projectName
	 *            Name of the project that has the path to expand
	 * @param path
	 *            Path to expand excluding project name
	 * @return SWTBotTreeItem Last node will be returned
	 */
	public static SWTBotTreeItem getExpandProjectTree(String projectName,
			String[] path) {

		WorkbenchElementsValidator.bot.sleep(2000);
		try {
			tree = WorkbenchElementsValidator.bot.tree().getTreeItem(
					projectName);
			WorkbenchElementsValidator.bot.viewByTitle(
					CommonConstants.PROJECT_EXPLORER).show();
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
			WorkbenchElementsValidator.log.error("Cannot open the project", e);
			fail();
			return null;
		}

	}

	/**
	 * This method will open a specified project by right clicking a given path.
	 * 
	 * @param projectName
	 *            Name of the project that already created.
	 * @param path
	 *            Path that new project must be created excluding the main
	 *            project name.
	 * @param projectType
	 *            Type of project that should be created.
	 */
	public static void openProjectCreationWizardFromRightClick(String projectName,
			String[] path, String projectType) {

		WorkbenchElementsValidator.bot.sleep(2000);
		if (path == null) {
			tree = WorkbenchElementsValidator.bot.tree().getTreeItem(
					projectName);
			tree.select();

		} else {
			tree = getExpandProjectTree(projectName, path);
		}
		try {
			tree.contextMenu(CommonConstants.NEW).menu(projectType).click();
		} catch (WidgetNotFoundException e) {
			WorkbenchElementsValidator.log.error("Cannot open the project", e);
			fail();

		} catch (Error erro) {
			WorkbenchElementsValidator.log.error("Cannot open the project",
					erro);
			fail();
		}
	}

	/**
	 * This method will open a specified project by right clicking a given
	 * project.
	 * 
	 * @param projectName
	 *            Name of the project that already created.
	 * 
	 * @param projectType
	 *            Type of project that should be created.
	 */
	public static void openProjectCreationWizardFromRightClick(String projectName,
			String projectType) {
		WorkbenchElementsValidator.bot.sleep(2000);
		openProjectCreationWizardFromRightClick(projectName, null, projectType);
	}

	/**
	 * This method will create a Java Class
	 * 
	 * @param className
	 *            class name
	 */
	public static void createJavaClass(String className) {
		WorkbenchElementsValidator.bot.sleep(2000);
		WorkbenchElementsValidator
				.checkShellLoading(JavaConstants.NEW_JAVA_CLASS);
		SWTBotShell newClass = WorkbenchElementsValidator.bot
				.shell(JavaConstants.NEW_JAVA_CLASS);
		newClass.bot().textWithLabel(JavaConstants.NA_ME).setText(className);
		newClass.bot().button(CommonConstants.FINISH).click();
		WorkbenchElementsValidator.bot.waitUntil(Conditions
				.shellCloses(newClass));
	}

	/**
	 * This method will open a specified perspective
	 * 
	 * @param perspective
	 *            Name of the perspective
	 */
	public static void openPerspective(String perspective) {
		WorkbenchElementsValidator.bot.sleep(2000);
		try {
			WorkbenchElementsValidator.bot.toolbarButtonWithTooltip(
					CommonConstants.OPEN_PERSPECTIVE).click();
			WorkbenchElementsValidator
					.checkShellLoading(CommonConstants.OPEN_PERSPECTIVE);
			SWTBotShell openPerspectiveShell = WorkbenchElementsValidator.bot
					.shell(CommonConstants.OPEN_PERSPECTIVE);
			openPerspectiveShell.bot().table().select(perspective);
			openPerspectiveShell.bot().button(CommonConstants.OK).click();
			WorkbenchElementsValidator.bot.waitUntil(Conditions
					.shellCloses(openPerspectiveShell));
		}

		catch (WidgetNotFoundException e) {
			WorkbenchElementsValidator.log.error("Widget cannot be load", e);
			fail();
		}
	}

	/**
	 * This method will delete a project with its content in the hard disk
	 * 
	 * @param projectName
	 *            Name of the project that you want to delete
	 */
	public static void deleteProjectWithContent(String projectName) {
		try {
			WorkbenchElementsValidator.bot.saveAllEditors();
			WorkbenchElementsValidator.bot.closeAllEditors();
		} catch (Exception e) {

		}
		try {
			WorkbenchElementsValidator.bot.viewByTitle(
					CommonConstants.PROJECT_EXPLORER).setFocus();

			WorkbenchElementsValidator.bot.tree().getTreeItem(projectName)
					.select();
			WorkbenchElementsValidator.bot.tree().getTreeItem(projectName)
					.contextMenu(CommonConstants.DELETE).click();
			WorkbenchElementsValidator.bot.sleep(2000);
		} catch (WidgetNotFoundException e) {
			WorkbenchElementsValidator.log.error("Project not found", e);
			fail();
		}
		try {
			SWTBotShell deleteShell = WorkbenchElementsValidator.bot
					.shell(CommonConstants.DELETE_RESOURCES);
			deleteShell
					.bot()
					.checkBox(
							CommonConstants.DELETE_PROJECT_CONTENTS_ON_DISK_CANNOT_BE_UNDONE)
					.click();
			deleteShell.bot().button(CommonConstants.OK).click();
			WorkbenchElementsValidator.bot.button(CommonConstants.CONTINUE)
					.click();
			WorkbenchElementsValidator.log.info("Delete successful");
		} catch (Exception e) {
			WorkbenchElementsValidator.log.error("Delete unsuccessful", e);
			fail();
		}
	}

	/**
	 * This method will delete a specified project without its content in the
	 * hard disk
	 * 
	 * @param projectName
	 *            Name of the project that you want to delete
	 */
	public static void deleteProjectWithoutContent(String projectName) {
		try {
			WorkbenchElementsValidator.bot.saveAllEditors();
			WorkbenchElementsValidator.bot.tree().getTreeItem(projectName)
					.contextMenu(CommonConstants.DELETE).click();
		} catch (WidgetNotFoundException e) {
			WorkbenchElementsValidator.log.error("Project not found", e);
			fail();
		}
		try {
			WorkbenchElementsValidator
					.checkShellLoading(CommonConstants.DELETE_RESOURCES);
			SWTBotShell deleteShell = WorkbenchElementsValidator.bot
					.shell(CommonConstants.DELETE_RESOURCES);
			deleteShell.bot().button(CommonConstants.OK).click();
			WorkbenchElementsValidator.bot.button(CommonConstants.CONTINUE)
					.click();
			WorkbenchElementsValidator.log.info("Delete sucsessful");
		} catch (Exception e) {
			WorkbenchElementsValidator.log.error("Delete unsucsessful", e);
			fail();
		}
	}

	/**
	 * This method will set focus to specified ctab in the editor
	 * 
	 * @param tabName
	 *            Name of the tab
	 */
	public static void activateCtab(String tabName) {
		try {
			WorkbenchElementsValidator.bot.cTabItem(tabName).activate();
		} catch (WidgetNotFoundException e) {
			WorkbenchElementsValidator.log.error("Ctab cannot be found", e);
			fail();
		}
	}

	/**
	 * This method will close a already open view
	 * 
	 * @param viewName
	 *            Name of the view that want to be closed
	 */
	public static void closeView(String viewName) {
		try {
			WorkbenchElementsValidator.bot.viewByTitle(viewName).close();
		} catch (Exception e) {
			WorkbenchElementsValidator.log.error("Fail to close the view", e);
			fail();
		}
	}

	/**
	 * This method will save a already open editor
	 * 
	 * @param editorName
	 *            Name of the editor that wanted to be saved
	 */
	public static void saveEditor(String editorName) {
		WorkbenchElementsValidator.bot.editorByTitle(editorName).save();
	}

}
