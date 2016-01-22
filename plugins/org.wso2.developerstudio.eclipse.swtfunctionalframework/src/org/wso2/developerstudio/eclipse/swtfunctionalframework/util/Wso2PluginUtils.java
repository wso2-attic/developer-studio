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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonConstants;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.Wso2PluginConstants;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;

public class Wso2PluginUtils{

    private static SWTBotTreeItem main;

    public static void createWso2Plugin(String projectName, String projectType) {

        SWTBotShell newPluginProject;
        SWTBotTable selection;
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
        projectValidation(projectName);
    }

    public static void projectValidation(String projectName) {

        List<String> actual;
        List<String> expected = new ArrayList<String>();
        expected.add("src");
        expected.add("icons");
        expected.add("META-INF");
        expected.add("build.properties");
        expected.add("mediator.properties");
        expected.add("plugin.xml");
        expected.add("pom.xml");
        expected.add("project_wizard.xml");
        main = Util.bot.tree().getTreeItem(projectName);
        Util.bot.sleep(5000);
        main.expand();
        Util.bot.sleep(5000);
        actual = main.expand().getNodes();
        Util.bot.sleep(5000);
        try {
            actual.remove(0);
            actual.remove(0);
        } catch (IndexOutOfBoundsException e) {
            Util.log.error("Getting files faliure", e);
            fail();
        }
        assertNotSame("Main project creation faliure", expected, actual);
        expected.removeAll(expected);
        actual.removeAll(actual);
        main.getNode("src").expand();
        Util.bot.sleep(5000);
        actual = main.getNode("src").expand().getNodes();
        Util.bot.sleep(5000);
        expected.add("org.wso2.developerstudio.eclipse.artifact");
        expected.add("org.wso2.developerstudio.eclipse.artifact.model");
        expected.add("org.wso2.developerstudio.eclipse.artifact.project.export");
        expected.add("org.wso2.developerstudio.eclipse.artifact.project.nature");
        expected.add("org.wso2.developerstudio.eclipse.artifact.template");
        expected.add("org.wso2.developerstudio.eclipse.artifact.ui.wizard");
        expected.add("org.wso2.developerstudio.eclipse.artifact.utils");
        expected.add("org.wso2.developerstudio.eclipse.artifact.validators");
        assertNotSame("Suorce folder creation faliure", expected, actual);

    }

    public static void validateProjectWiard(String projectName) {

        String expected;
        String actual;
        StringBuffer expectedBuffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        main = Util.bot.tree().getTreeItem(projectName).expand();
        main.getNode("project_wizard.xml").doubleClick();
        Util.bot.cTabItem("Source").activate();
        actual = Util.bot.editorByTitle("project_wizard.xml").bot().styledText().getText();
        expectedBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        expectedBuffer.append("<wizard>\n");
        expectedBuffer.append(" <projectOptions title=\"Artifact Creation Wizard \"\n");
        expectedBuffer.append("                 description=\"Select how you would like to create your new artifact\"\n");
        expectedBuffer.append("                 error=\"Please select a method to create the project\">\n");
        expectedBuffer.append("    <option id=\"new.artifact\" default=\"true\">Create New Artifact</option>\n");
        expectedBuffer.append("    <option id=\"import.artifact\">Import From Workspace</option>\n");
        expectedBuffer.append(" </projectOptions>\n");
        expectedBuffer.append(" <projectOptionSettings>\n");
        expectedBuffer.append("    <settings optionId=\"new.artifact\"\n");
        expectedBuffer.append("                    title=\"Artifact\"\n");
        expectedBuffer.append("                    description=\"Create a new Artifact\"\n");
        expectedBuffer.append("                    error=\"Please give a name to create the Artifact\">\n");
        expectedBuffer.append("        <data modelProperty=\"project.name\"\n");
        expectedBuffer.append("                    type=\"string\" \n");
        expectedBuffer.append("                    fieldController=\"org.wso2.developerstudio.eclipse.artifact.validators.ArtifactFieldsController\">Project Name   </data>\n");
        expectedBuffer.append("        <data modelProperty=\"artifactClass.package.name\" \n");
        expectedBuffer.append("                    type=\"string\" \n");
        expectedBuffer.append("                    fieldController=\"org.wso2.developerstudio.eclipse.artifact.validators.ArtifactFieldsController\" group=\"testid\">Package Name</data>\n");
        expectedBuffer.append("        <data modelProperty=\"artifactClass.name\" \n");
        expectedBuffer.append("                    type=\"string\" \n");
        expectedBuffer.append("                    fieldController=\"org.wso2.developerstudio.eclipse.artifact.validators.ArtifactFieldsController\" group=\"testid\">Class Name</data>     \n");
        expectedBuffer.append("        <group id=\"testid\"></group>\n\n");
        expectedBuffer.append("        <projectNatures>\n");
        expectedBuffer.append("            <nature>org.eclipse.jdt.core.javanature</nature>\n");
        expectedBuffer.append("        </projectNatures>\n\n");
        expectedBuffer.append("    </settings>\n");
        expectedBuffer.append("        <settings optionId=\"import.artifact\"\n");
        expectedBuffer.append("                            title=\"Artifact\"\n");
        expectedBuffer.append("                        description=\"Create an Artifact using a existing artifact file\"\n");
        expectedBuffer.append("                        error=\"Please select the project\">    \n");
        expectedBuffer.append("            <data modelProperty=\"import.project.list\" type=\"list\" \n");
        expectedBuffer.append("                    fieldController=\"org.wso2.developerstudio.eclipse.artifact.validators.ArtifactFieldsController\"\n");
        expectedBuffer.append("                    controlData=\"select=single;class=org.wso2.developerstudio.eclipse.artifact.utils.ProjectData\">Artifact Project</data>\n");
        expectedBuffer.append("        <projectNatures>\n");
        expectedBuffer.append("            <nature>org.eclipse.jdt.core.javanature</nature>\n");
        expectedBuffer.append("        </projectNatures>\n\n");
        expectedBuffer.append("    </settings>\n");
        expectedBuffer.append(" </projectOptionSettings>\n");
        expectedBuffer.append("</wizard>\n");

        expected = expectedBuffer.toString();
        assertContains(expected, actual);

    }
}
