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

package org.wso2.developerstudio.eclipse.test.automation.utils.kernel;

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertContains;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.CommonConstants;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.PluginConstants;
import org.wso2.developerstudio.eclipse.test.automation.framework.element.validator.WorkbenchElementsValidator;
import org.wso2.developerstudio.eclipse.test.automation.utils.validator.ValidatorUtil;

public class WSO2PluginUtils {

	public static void createWso2Plugin(String projectName, String projectType) {

		SWTBotShell newPluginProject;
		SWTBotTable selection;
		List<String> expectedFiles = new ArrayList<String>();
		String[] path = { "src" };
		WorkbenchElementsValidator.bot.sleep(2000);
		WorkbenchElementsValidator
				.checkShellLoading(PluginConstants.WSO2_PLUGIN_PROJECT_WINDOW_TITLE);
		newPluginProject = WorkbenchElementsValidator.bot
				.shell(PluginConstants.WSO2_PLUGIN_PROJECT_WINDOW_TITLE);
		try {
			newPluginProject.bot().textWithLabel(PluginConstants.PLUGIN_Id)
					.setText(projectName);
		}

		catch (WidgetNotFoundException e) {
			WorkbenchElementsValidator.log.error("Problem with the Lable", e);
			fail();
		}
		WorkbenchElementsValidator.bot.sleep(2000);
		WorkbenchElementsValidator.checkButton(CommonConstants.NEXT,
				newPluginProject);
		newPluginProject.bot().button(CommonConstants.NEXT).click();
		WorkbenchElementsValidator.bot.sleep(1000);
		try {
			selection = WorkbenchElementsValidator.bot.table();
			selection.select(projectType);
		} catch (Exception e) {
			WorkbenchElementsValidator.log.error(
					"Problem with project type selection table", e);
			fail();
		}
		WorkbenchElementsValidator.checkButton(CommonConstants.FINISH,
				newPluginProject);
		newPluginProject.bot().button(CommonConstants.FINISH).click();
		WorkbenchElementsValidator.bot.sleep(1000);
		WorkbenchElementsValidator.bot.waitUntil(Conditions
				.shellCloses(newPluginProject));
		WorkbenchElementsValidator.bot.sleep(2000);

		expectedFiles.add("JRE System Library");
		expectedFiles.add("Plug-in Dependencies");
		expectedFiles.add("src");
		expectedFiles.add("icons");
		expectedFiles.add("META-INF");
		expectedFiles.add("build.properties");
		expectedFiles.add("mediator.properties");
		expectedFiles.add("plugin.xml");
		expectedFiles.add("pom.xml");
		expectedFiles.add("project_wizard.xml");
		ValidatorUtil.projectValidation(projectName, expectedFiles);

		expectedFiles.removeAll(expectedFiles);
		expectedFiles.add("org.wso2.developerstudio.eclipse.artifact");
		expectedFiles.add("org.wso2.developerstudio.eclipse.artifact.model");
		expectedFiles
				.add("org.wso2.developerstudio.eclipse.artifact.project.export");
		expectedFiles
				.add("org.wso2.developerstudio.eclipse.artifact.project.nature");
		expectedFiles.add("org.wso2.developerstudio.eclipse.artifact.template");
		expectedFiles
				.add("org.wso2.developerstudio.eclipse.artifact.ui.wizard");
		expectedFiles.add("org.wso2.developerstudio.eclipse.artifact.utils");
		expectedFiles
				.add("org.wso2.developerstudio.eclipse.artifact.validators");
		ValidatorUtil.projectValidation(projectName, path, expectedFiles);
	}

	public static void validateProjectWiard(String projectName) {

		WorkbenchElementsValidator.expectedBuffer = new StringBuffer(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		WorkbenchElementsValidator.main = WorkbenchElementsValidator.bot.tree()
				.getTreeItem(projectName).expand();
		WorkbenchElementsValidator.main.getNode("project_wizard.xml")
				.doubleClick();
		WorkbenchElementsValidator.bot.cTabItem("Source").activate();
		WorkbenchElementsValidator.actual = WorkbenchElementsValidator.bot
				.editorByTitle("project_wizard.xml").bot().styledText()
				.getText();
		WorkbenchElementsValidator.expectedBuffer
				.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		WorkbenchElementsValidator.expectedBuffer.append("<wizard>\n");
		WorkbenchElementsValidator.expectedBuffer
				.append(" <projectOptions title=\"Artifact Creation Wizard \"\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("                 description=\"Select how you would like to create your new artifact\"\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("                 error=\"Please select a method to create the project\">\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("    <option id=\"new.artifact\" default=\"true\">Create New Artifact</option>\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("    <option id=\"import.artifact\">Import From Workspace</option>\n");
		WorkbenchElementsValidator.expectedBuffer
				.append(" </projectOptions>\n");
		WorkbenchElementsValidator.expectedBuffer
				.append(" <projectOptionSettings>\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("    <settings optionId=\"new.artifact\"\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("                    title=\"Artifact\"\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("                    description=\"Create a new Artifact\"\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("                    error=\"Please give a name to create the Artifact\">\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("        <data modelProperty=\"project.name\"\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("                    type=\"string\" \n");
		WorkbenchElementsValidator.expectedBuffer
				.append("                    fieldController=\"org.wso2.developerstudio.eclipse.artifact.validators.ArtifactFieldsController\">Project Name   </data>\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("        <data modelProperty=\"artifactClass.package.name\" \n");
		WorkbenchElementsValidator.expectedBuffer
				.append("                    type=\"string\" \n");
		WorkbenchElementsValidator.expectedBuffer
				.append("                    fieldController=\"org.wso2.developerstudio.eclipse.artifact.validators.ArtifactFieldsController\" group=\"testid\">Package Name</data>\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("        <data modelProperty=\"artifactClass.name\" \n");
		WorkbenchElementsValidator.expectedBuffer
				.append("                    type=\"string\" \n");
		WorkbenchElementsValidator.expectedBuffer
				.append("                    fieldController=\"org.wso2.developerstudio.eclipse.artifact.validators.ArtifactFieldsController\" group=\"testid\">Class Name</data>     \n");
		WorkbenchElementsValidator.expectedBuffer
				.append("        <group id=\"testid\"></group>\n\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("        <projectNatures>\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("            <nature>org.eclipse.jdt.core.javanature</nature>\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("        </projectNatures>\n\n");
		WorkbenchElementsValidator.expectedBuffer.append("    </settings>\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("        <settings optionId=\"import.artifact\"\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("                            title=\"Artifact\"\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("                        description=\"Create an Artifact using a existing artifact file\"\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("                        error=\"Please select the project\">    \n");
		WorkbenchElementsValidator.expectedBuffer
				.append("            <data modelProperty=\"import.project.list\" type=\"list\" \n");
		WorkbenchElementsValidator.expectedBuffer
				.append("                    fieldController=\"org.wso2.developerstudio.eclipse.artifact.validators.ArtifactFieldsController\"\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("                    controlData=\"select=single;class=org.wso2.developerstudio.eclipse.artifact.utils.ProjectData\">Artifact Project</data>\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("        <projectNatures>\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("            <nature>org.eclipse.jdt.core.javanature</nature>\n");
		WorkbenchElementsValidator.expectedBuffer
				.append("        </projectNatures>\n\n");
		WorkbenchElementsValidator.expectedBuffer.append("    </settings>\n");
		WorkbenchElementsValidator.expectedBuffer
				.append(" </projectOptionSettings>\n");
		WorkbenchElementsValidator.expectedBuffer.append("</wizard>\n");

		WorkbenchElementsValidator.expect = WorkbenchElementsValidator.expectedBuffer
				.toString();
		assertContains(WorkbenchElementsValidator.expect,
				WorkbenchElementsValidator.actual);

	}
}
