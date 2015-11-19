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
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonCons;

public class Wso2PluginUtils extends Util {

    private static SWTBotTreeItem main;

    public static void createWso2Plugin(String projectName, String projectType) {

        SWTBotShell newPluginProject;
        SWTBotTable selection;

        bot.sleep(20000);
        checkShellLoading("New WSO2 Plugin Project");
        newPluginProject = bot.shell("New WSO2 Plugin Project");
        try {
            newPluginProject.bot().textWithLabel("Project Name   ").setText(projectName);
        }

        catch (WidgetNotFoundException e) {
            log.error("Problem with the Lable");
            fail();
        }
        bot.sleep(2000);
        checkButton(CommonCons.NEXT, newPluginProject);
        newPluginProject.bot().button(CommonCons.NEXT).click();
        try {
            selection = bot.table();
            selection.select(projectType);
        } catch (Exception e) {
            log.error("Problem with project type selection table");
        }
        //checkButton(CommonCons.FINISH, newPluginProject);
        newPluginProject.bot().button(CommonCons.FINISH).click();
        bot.waitUntil(Conditions.shellCloses(newPluginProject));
        projectValidation(projectName);
        //validateProjectWiard(projectName);
    }

    public static void projectValidation(String projectName) {

        List<String> actual;
        List<String> expected = new ArrayList<String>();
        expected.add("sr");
        expected.add("icons");
        expected.add("META-INF");
        expected.add("build.properties");
        expected.add("mediator.properties");
        expected.add("plugin.xml");
        expected.add("pom.xml");
        expected.add("project_wizard.xml");
        main = bot.tree().getTreeItem(projectName);
        bot.sleep(1000);
        actual = main.expand().getNodes();
        actual.remove(0);
        actual.remove(0);
        assertNotSame("Main project creation faliure", expected, actual);
        expected.removeAll(expected);
        actual.removeAll(actual);
        actual = main.getNode("src").expand().getNodes();
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
        main = bot.tree().getTreeItem(projectName).expand();
        main.getNode("project_wizard.xml").doubleClick();
        bot.cTabItem("Source").activate();
        actual = bot.editorByTitle("project_wizard.xml").bot().styledText().getText();
        expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>/n";
        expected = expected + "<wizard>/n";
        expected = expected + " <projectOptions title=\"Artifact Creation Wizard \"/n";
        expected = expected + "                 description=\"Select how you would like to create your new artifact\"/n";
        expected = expected + "                 error=\"Please select a method to create the project\">/n";
        expected = expected + "    <option id=\"new.artifact\" default=\"true\">Create New Artifact</option>/n";
        expected = expected + "    <option id=\"import.artifact\">Import From Workspace</option>/n";
        expected = expected + " </projectOptions>/n";
        expected = expected + " <projectOptionSettings>/n";
        expected = expected + "    <settings optionId=\"new.artifact\"/n";
        expected = expected + "                    title=\"Artifact\"/n";
        expected = expected + "                    description=\"Create a new Artifact\"/n";
        expected = expected + "                    error=\"Please give a name to create the Artifact\">/n";
        expected = expected + "        <data modelProperty=\"project.name\"/n";
        expected = expected + "                    type=\"string\" /n";
        expected = expected + "                    fieldController=\"org.wso2.developerstudio.eclipse.artifact.validators.ArtifactFieldsController\">Project Name   </data>/n";
        expected = expected + "        <data modelProperty=\"artifactClass.package.name\" /n";
        expected = expected + "                    type=\"string\" /n";
        expected = expected + "                    fieldController=\"org.wso2.developerstudio.eclipse.artifact.validators.ArtifactFieldsController\" group=\"testid\">Package Name</data>/n";
        expected = expected + "        <data modelProperty=\"artifactClass.name\" /n";
        expected = expected + "                    type=\"string\" /n";
        expected = expected + "                    fieldController=\"org.wso2.developerstudio.eclipse.artifact.validators.ArtifactFieldsController\" group=\"testid\">Class Name</data>     /n";
        expected = expected + "        <group id=\"testid\"></group>/n";
        expected = expected + "/n";
        expected = expected + "        <projectNatures>/n";
        expected = expected + "            <nature>org.eclipse.jdt.core.javanature</nature>/n";
        expected = expected + "        </projectNatures>/n";
        expected = expected + "/n";
        expected = expected + "    </settings>/n";
        expected = expected + "        <settings optionId=\"import.artifact\"/n";
        expected = expected + "                            title=\"Artifact\"/n";
        expected = expected + "                        description=\"Create an Artifact using a existing artifact file\"/n";
        expected = expected + "                        error=\"Please select the project\">    /n";
        expected = expected + "            <data modelProperty=\"import.project.list\" type=\"list\" /n";
        expected = expected + "                    fieldController=\"org.wso2.developerstudio.eclipse.artifact.validators.ArtifactFieldsController\"/n";
        expected = expected + "                    controlData=\"select=single;class=org.wso2.developerstudio.eclipse.artifact.utils.ProjectData\">Artifact Project</data>/n";
        expected = expected + "        <projectNatures>/n";
        expected = expected + "            <nature>org.eclipse.jdt.core.javanature</nature>/n";
        expected = expected + "        </projectNatures>/n";
        expected = expected + "/n";
        expected = expected + "    </settings>/n";
        expected = expected + " </projectOptionSettings>/n";
        expected = expected + "</wizard>/n";
        
        assertContains(expected, actual);

    }
}
