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
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonConstants;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.Wso2PluginConstants;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;

public class Wso2PluginUtils {

    public static void createWso2Plugin(String projectName, String projectType) {

        SWTBotShell newPluginProject;
        SWTBotTable selection;
        List<String> expectedFiles = new ArrayList<String>();
        String[] path = { "src" };
        Util.bot.sleep(2000);
        Util.checkShellLoading(Wso2PluginConstants.WSO2_PLUGIN_PROJECT_WINDOW_TITLE);
        newPluginProject = Util.bot.shell(Wso2PluginConstants.WSO2_PLUGIN_PROJECT_WINDOW_TITLE);
        try {
            newPluginProject.bot().textWithLabel(Wso2PluginConstants.PLUGIN_Id).setText(projectName);
        }

        catch (WidgetNotFoundException e) {
            Util.log.error("Problem with the Lable", e);
            fail();
        }
        Util.bot.sleep(2000);
        Util.checkButton(CommonConstants.NEXT, newPluginProject);
        newPluginProject.bot().button(CommonConstants.NEXT).click();
        Util.bot.sleep(1000);
        try {
            selection = Util.bot.table();
            selection.select(projectType);
        } catch (Exception e) {
            Util.log.error("Problem with project type selection table", e);
            fail();
        }
        Util.checkButton(CommonConstants.FINISH, newPluginProject);
        newPluginProject.bot().button(CommonConstants.FINISH).click();
        Util.bot.sleep(1000);
        Util.bot.waitUntil(Conditions.shellCloses(newPluginProject));
        Util.bot.sleep(2000);

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
        CommonUtil.projectValidation(projectName, expectedFiles);

        expectedFiles.removeAll(expectedFiles);
        expectedFiles.add("org.wso2.developerstudio.eclipse.artifact");
        expectedFiles.add("org.wso2.developerstudio.eclipse.artifact.model");
        expectedFiles.add("org.wso2.developerstudio.eclipse.artifact.project.export");
        expectedFiles.add("org.wso2.developerstudio.eclipse.artifact.project.nature");
        expectedFiles.add("org.wso2.developerstudio.eclipse.artifact.template");
        expectedFiles.add("org.wso2.developerstudio.eclipse.artifact.ui.wizard");
        expectedFiles.add("org.wso2.developerstudio.eclipse.artifact.utils");
        expectedFiles.add("org.wso2.developerstudio.eclipse.artifact.validators");
        CommonUtil.projectValidation(projectName, path, expectedFiles);
    }

    public static void validateProjectWiard(String projectName) {

        Util.expectedBuffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        Util.main = Util.bot.tree().getTreeItem(projectName).expand();
        Util.main.getNode("project_wizard.xml").doubleClick();
        Util.bot.cTabItem("Source").activate();
        Util.actual = Util.bot.editorByTitle("project_wizard.xml").bot().styledText().getText();
        Util.expectedBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        Util.expectedBuffer.append("<wizard>\n");
        Util.expectedBuffer.append(" <projectOptions title=\"Artifact Creation Wizard \"\n");
        Util.expectedBuffer
                .append("                 description=\"Select how you would like to create your new artifact\"\n");
        Util.expectedBuffer.append("                 error=\"Please select a method to create the project\">\n");
        Util.expectedBuffer.append("    <option id=\"new.artifact\" default=\"true\">Create New Artifact</option>\n");
        Util.expectedBuffer.append("    <option id=\"import.artifact\">Import From Workspace</option>\n");
        Util.expectedBuffer.append(" </projectOptions>\n");
        Util.expectedBuffer.append(" <projectOptionSettings>\n");
        Util.expectedBuffer.append("    <settings optionId=\"new.artifact\"\n");
        Util.expectedBuffer.append("                    title=\"Artifact\"\n");
        Util.expectedBuffer.append("                    description=\"Create a new Artifact\"\n");
        Util.expectedBuffer.append("                    error=\"Please give a name to create the Artifact\">\n");
        Util.expectedBuffer.append("        <data modelProperty=\"project.name\"\n");
        Util.expectedBuffer.append("                    type=\"string\" \n");
        Util.expectedBuffer
                .append("                    fieldController=\"org.wso2.developerstudio.eclipse.artifact.validators.ArtifactFieldsController\">Project Name   </data>\n");
        Util.expectedBuffer.append("        <data modelProperty=\"artifactClass.package.name\" \n");
        Util.expectedBuffer.append("                    type=\"string\" \n");
        Util.expectedBuffer
                .append("                    fieldController=\"org.wso2.developerstudio.eclipse.artifact.validators.ArtifactFieldsController\" group=\"testid\">Package Name</data>\n");
        Util.expectedBuffer.append("        <data modelProperty=\"artifactClass.name\" \n");
        Util.expectedBuffer.append("                    type=\"string\" \n");
        Util.expectedBuffer
                .append("                    fieldController=\"org.wso2.developerstudio.eclipse.artifact.validators.ArtifactFieldsController\" group=\"testid\">Class Name</data>     \n");
        Util.expectedBuffer.append("        <group id=\"testid\"></group>\n\n");
        Util.expectedBuffer.append("        <projectNatures>\n");
        Util.expectedBuffer.append("            <nature>org.eclipse.jdt.core.javanature</nature>\n");
        Util.expectedBuffer.append("        </projectNatures>\n\n");
        Util.expectedBuffer.append("    </settings>\n");
        Util.expectedBuffer.append("        <settings optionId=\"import.artifact\"\n");
        Util.expectedBuffer.append("                            title=\"Artifact\"\n");
        Util.expectedBuffer
                .append("                        description=\"Create an Artifact using a existing artifact file\"\n");
        Util.expectedBuffer.append("                        error=\"Please select the project\">    \n");
        Util.expectedBuffer.append("            <data modelProperty=\"import.project.list\" type=\"list\" \n");
        Util.expectedBuffer
                .append("                    fieldController=\"org.wso2.developerstudio.eclipse.artifact.validators.ArtifactFieldsController\"\n");
        Util.expectedBuffer
                .append("                    controlData=\"select=single;class=org.wso2.developerstudio.eclipse.artifact.utils.ProjectData\">Artifact Project</data>\n");
        Util.expectedBuffer.append("        <projectNatures>\n");
        Util.expectedBuffer.append("            <nature>org.eclipse.jdt.core.javanature</nature>\n");
        Util.expectedBuffer.append("        </projectNatures>\n\n");
        Util.expectedBuffer.append("    </settings>\n");
        Util.expectedBuffer.append(" </projectOptionSettings>\n");
        Util.expectedBuffer.append("</wizard>\n");

        Util.expect = Util.expectedBuffer.toString();
        assertContains(Util.expect, Util.actual);

    }
}
