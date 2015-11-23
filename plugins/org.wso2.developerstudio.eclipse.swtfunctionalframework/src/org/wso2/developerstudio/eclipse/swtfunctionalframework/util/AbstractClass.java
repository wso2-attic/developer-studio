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
import static org.eclipse.swtbot.swt.finder.SWTBotAssert.pass;
import static org.junit.Assert.fail;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.forms.finder.SWTFormsBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.*;

public class AbstractClass extends Util {

    public static void openProjectFromMenu(String projectType) {
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
            log.error("Opening project from menu failed");
            fail();
        } catch (Error erro) {
            log.error("Opening project from menu failed");
            fail();
        }
    }

    public static void openFromDash(String project) throws Exception {

        try {
            bot.menu(CommonCons.DEVELOPER_STUDIO).menu(CommonCons.OPEN_DASHBOARD).click();
        } catch (Exception e) {
            log.error("Problem in openning dashboard");
        }
        try {
            SWTBotEditor dashBoard = bot.editorByTitle(CommonCons.DEVELOPER_STUDIO_DASHBOARD);
            dashBoard.show();
            SWTFormsBot form = new SWTFormsBot();
            form.imageHyperlink(project).click();
            log.error("Openning project from dashboard sucssecfull");
        } catch (Exception e) {
            log.error("Problem with opennig the project");
        }

    }

    public static void createBusinessRulesService(String projectName, String serviceName) {

        bot.sleep(2000);
        checkShellLoading(BusinessRulesServiceCons.NEW_BUSINESS_RULES_SERVICE_PROJECT);
        newServiceProject = bot.shell(BusinessRulesServiceCons.NEW_BUSINESS_RULES_SERVICE_PROJECT);
        checkButton(CommonCons.NEXT, newServiceProject);
        newServiceProject.bot().button(CommonCons.NEXT).click();
        try {
            newServiceProject.bot().textWithLabel(BusinessRulesServiceCons.PROJECT_NAME).setText(projectName);
            newServiceProject.bot().textWithLabel(BusinessRulesServiceCons.SERVICE_NAME).setText(serviceName);
        }

        catch (WidgetNotFoundException e) {
            log.error("Problem with the Lable");
            fail();
        }
        checkButton(CommonCons.NEXT, newServiceProject);
        newServiceProject.bot().button(CommonCons.NEXT).click();
        checkButton(CommonCons.FINISH, newServiceProject);
        newServiceProject.bot().button(CommonCons.FINISH).click();
        bot.waitUntil(Conditions.shellCloses(newServiceProject));
        log.error("Business Rules Service sucssecfully created");
        try {
            bot.editorByTitle(BusinessRulesServiceCons.SERVICE_RSL).show();
            System.out.println("Create Business Rules Service Sucsessful");

        } catch (WidgetNotFoundException e) {
            log.error("Editor didnt load");
            fail();
        }
    }

    public static void importBusinessRulesService(String path) {

        checkShellLoading(BusinessRulesServiceCons.NEW_BUSINESS_RULES_SERVICE_PROJECT);
        newServiceProject = bot.shell(BusinessRulesServiceCons.NEW_BUSINESS_RULES_SERVICE_PROJECT);
        newServiceProject.bot().radio(BusinessRulesServiceCons.IMPORT_BUSINESS_RULE_SERVICE).click();
        checkButton(CommonCons.NEXT, newServiceProject);
        newServiceProject.bot().button(CommonCons.NEXT).click();
        // browse dosen't work
        try {
            newServiceProject.bot().textWithLabel(BusinessRulesServiceCons.BUSINESS_RULE_SERVICE_DESCRIPTOR_FILE)
                    .setText(path);
        } catch (WidgetNotFoundException e) {
            log.error("Problem with the Lable");
            fail();
        }
        checkButton(CommonCons.NEXT, newServiceProject);
        newServiceProject.bot().button(CommonCons.NEXT).click();
        checkButton(CommonCons.FINISH, newServiceProject);
        newServiceProject.bot().button(CommonCons.FINISH).click();
        bot.waitUntil(Conditions.shellCloses(newServiceProject));
    }

    public static void createCarbonUI(String projectName) {

        checkShellLoading(CarbonUICons.NEW_CARBON_UI_BUNDLE);
        SWTBotShell newCarbonUIBundle = bot.shell(CarbonUICons.NEW_CARBON_UI_BUNDLE);
        checkButton(CommonCons.NEXT, newCarbonUIBundle);
        newCarbonUIBundle.bot().button(CommonCons.NEXT).click();
        try {
            newCarbonUIBundle.bot().textWithLabel(CarbonUICons.PROJECT_NAME).setText(projectName);
        } catch (WidgetNotFoundException e) {
            log.error("Problem with the Lable");
            fail();
        }
        checkButton(CommonCons.NEXT, newCarbonUIBundle);
        newCarbonUIBundle.bot().button(CommonCons.NEXT).click();
        checkButton(CommonCons.FINISH, newCarbonUIBundle);
        newCarbonUIBundle.bot().button(CommonCons.FINISH).click();
        try {
            changePerspective();
        } catch (AssertionError e) {

        }
        bot.waitUntil(Conditions.shellCloses(newCarbonUIBundle));
    }

    public static void createDataService(String projectName) {

        checkShellLoading(DataServiceCons.NEW_DATA_SERVICE_PROJECT);
        SWTBotShell newDataService = bot.shell(DataServiceCons.NEW_DATA_SERVICE_PROJECT);
        try {
            newDataService.bot().textWithLabel(DataServiceCons.PROJECT_NAME).setText(projectName);
        } catch (WidgetNotFoundException e) {
            System.out.println("Problem with the Lable");
            fail();
        }
        checkButton(CommonCons.NEXT, newDataService);
        newDataService.bot().button(CommonCons.NEXT).click();
        checkButton(CommonCons.FINISH, newDataService);
        newDataService.bot().button(CommonCons.FINISH).click();
        bot.waitUntil(Conditions.shellCloses(newDataService));
    }

    public static void createESBProject(String projectName) {
        // SWTBotShell newESBConf= bot.shell("");
        // newESBConf.activate();
        bot.button(CommonCons.NEXT).click();
        bot.sleep(1000);
        // newESBConf.bot()
        try {
            bot.textWithLabel(ESBProjectCons.PROJECT_NAME).setText(projectName);
        } catch (WidgetNotFoundException e) {
            System.out.println("Problem with the Lable");
            fail();
        }
        bot.sleep(1000);
        // newESBConf.bot()
        bot.button(CommonCons.NEXT).click();
        bot.sleep(1000);
        // newESBConf.bot()
        bot.button(CommonCons.FINISH).click();
        bot.sleep(1000);
    }

    public static void createAxis2Project(String projectName, String packageName, String className) {

        checkShellLoading(Axis2Cons.NEW_AXIS2_SERVICE_PROJECT);
        SWTBotShell newProject = bot.shell(Axis2Cons.NEW_AXIS2_SERVICE_PROJECT);
        checkButton(CommonCons.NEXT, newProject);
        newProject.bot().button(CommonCons.NEXT).click();
        bot.sleep(1000);
        try {
            newProject.bot().textWithLabel(Axis2Cons.PROJECT_NAME).setText(projectName);
            bot.sleep(1000);
            newProject.bot().textWithLabel(Axis2Cons.PACKAGE_NAME).setText(packageName);
            bot.sleep(1000);
            newProject.bot().textWithLabel(Axis2Cons.CLASS_NAME).setText(className);
            bot.sleep(1000);
        } catch (WidgetNotFoundException e) {
            System.out.println("Problem with the Lable");
            fail();
        }
        checkButton(CommonCons.NEXT, newProject);
        newProject.bot().button(CommonCons.NEXT).click();
        checkButton(CommonCons.FINISH, newProject);
        newProject.bot().button(CommonCons.FINISH).click();
        bot.waitUntil(Conditions.shellCloses(newProject));
        try {
            bot.editorByTitle(className + CommonCons.JAVA).show();
            System.out.println("Create Axis2 Project Sucsessful");
        } catch (WidgetNotFoundException e) {
            System.out.println("Editor didnt load");
            fail();
        }
        validateJavaClass(packageName, className);

    }

    public static void createAxis2Class(String className) {

        checkShellLoading(Axis2Cons.NEW_AXIS2_CLASS);
        SWTBotShell newClass = bot.shell(Axis2Cons.NEW_AXIS2_CLASS);
        try {
            newClass.bot().textWithLabel(Axis2Cons.NA_ME).setText(className);

        } catch (WidgetNotFoundException e) {
            System.out.println("Problem with the Lable");
            fail();
        }
        checkButton(CommonCons.FINISH, newClass);
        newClass.bot().button(CommonCons.FINISH).click();
        bot.waitUntil(Conditions.shellCloses(newClass));
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
                System.out.println("hrekos");
                tree.getNode(element).select();
                tree = tree.getNode(element);
            }
            tree.getNode(fileName).doubleClick();
        } catch (WidgetNotFoundException e) {
            System.out.println("Cannot open the editor");
            fail();

        }

    }

    public static void createNewSequence(String sequenceName, String esbProjectToSave) {

        checkShellLoading(SequenceCons.NEW_SEQUENCE_ARTIFACT);
        SWTBotShell newSeqeunce = bot.shell(SequenceCons.NEW_SEQUENCE_ARTIFACT);
        checkButton(CommonCons.NEXT, newSeqeunce);
        newSeqeunce.bot().button(CommonCons.NEXT).click();
        bot.sleep(1000);
        try {
            newSeqeunce.bot().textWithLabel(SequenceCons.SEQUENCE_NAME).setText(sequenceName);

        } catch (WidgetNotFoundException e) {
            System.out.println("Problem with the Lable");
            fail();
        }
        newSeqeunce.bot().button(CommonCons.BROWSE).click();

        checkShellLoading(CommonCons.SELECT_FOLDER);
        saveIn = bot.shell(CommonCons.SELECT_FOLDER);
        saveIn.bot().tree().getTreeItem(esbProjectToSave).doubleClick();
        bot.waitUntil(Conditions.shellCloses(saveIn));
        newSeqeunce = bot.shell(SequenceCons.NEW_SEQUENCE_ARTIFACT);
        checkButton(CommonCons.FINISH, newSeqeunce);
        newSeqeunce.bot().button(CommonCons.FINISH).click();
        bot.waitUntil(Conditions.shellCloses(newSeqeunce));
        try {
            bot.editorByTitle(sequenceName + CommonCons.XML).show();
            System.out.println("Create New Sequence Sucsessful");

        } catch (WidgetNotFoundException e) {
            System.out.println("Editor didnt load");
            fail();
        }
    }

    public static void createProxyService(String serviceName) {

        checkShellLoading(ProxyServiceCons.NEW_PROXY_SERVICE);
        SWTBotShell newProxy = bot.shell(ProxyServiceCons.NEW_PROXY_SERVICE);
        checkButton(CommonCons.NEXT, newProxy);
        newProxy.bot().button(CommonCons.NEXT).click();
        try {
            newProxy.bot().textWithLabel(ProxyServiceCons.PROXY_SERVICE_NAME).setText(serviceName);

        } catch (WidgetNotFoundException e) {
            System.out.println("Problem with the Lable");
            fail();
        }
        newProxy.bot().comboBox().setSelection(ProxyServiceCons.CUSTOM_PROXY);
        checkButton(CommonCons.FINISH, newProxy);
        newProxy.bot().button(CommonCons.FINISH).click();
        bot.waitUntil(Conditions.shellCloses(newProxy));
        try {
            bot.editorByTitle(serviceName + CommonCons.XML).show();
            System.out.println("Create New Proxy Service Sucsessful");

        } catch (WidgetNotFoundException e) {
            System.out.println("Editor didnt load");
            fail();
        }

    }

    public static void createNewEndpointWithESB(String endpoint, String esbProject) {
        checkShellLoading(EndPoinCons.NEW_ENDPOINT_ARTIFACT);
        SWTBotShell newEndpoint = bot.shell(EndPoinCons.NEW_ENDPOINT_ARTIFACT);
        checkButton(CommonCons.NEXT, newEndpoint);
        newEndpoint.bot().button(CommonCons.NEXT).click();
        newEndpoint.bot().label(EndPoinCons.ENDPOINT_CONFIGURATION).click();
        bot.sleep(1000);
        newEndpoint.bot().label(EndPoinCons.ENDPOINT_CONFIGURATION).click();
        try {
            newEndpoint.bot().textWithLabel(EndPoinCons.ENDPOINT_NAME).setText(endpoint);

        } catch (WidgetNotFoundException e) {
            System.out.println("Problem with the Lable");
            fail();
        }
        newEndpoint.bot().comboBox().setSelection(EndPoinCons.DEFAULT_ENDPOINT);
        bot.link().click();

        createESBProject(esbProject);

        newEndpoint.bot().button(CommonCons.BROWSE).click();

        checkShellLoading(CommonCons.SELECT_FOLDER);
        saveIn = bot.shell(CommonCons.SELECT_FOLDER);
        saveIn = bot.shell(CommonCons.SELECT_FOLDER);
        saveIn.bot().tree().getTreeItem(esbProject).doubleClick();
        bot.waitUntil(Conditions.shellCloses(saveIn));

        newEndpoint = bot.shell(EndPoinCons.NEW_ENDPOINT_ARTIFACT);
        checkButton(CommonCons.FINISH, newEndpoint);
        newEndpoint.bot().button(CommonCons.FINISH).click();
        try {
            changePerspective();
        } catch (AssertionError e) {

        }
        bot.waitUntil(Conditions.shellCloses(newEndpoint));
        try {
            bot.editorByTitle(endpoint + CommonCons.XML).show();
            System.out.println("Create New Endpoint Sucsessful");

        } catch (WidgetNotFoundException e) {
            System.out.println("Editor didnt load");
            fail();
        }
    }

    public static void validateProxyServicexml(String proxyService) {

        try {
            bot.cTabItem(CommonCons.SOURCE).activate();
            actual = bot.editorByTitle(proxyService + CommonCons.XML).bot().styledText().getText();
            expect = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
            expect = expect + "<proxy name=\"" + proxyService + "\" startOnLoad=\"true\" trace=\"disable\"\n";
            expect = expect + "  transports=\"http https\" xmlns=\"http://ws.apache.org/ns/synapse\">\n";
            expect = expect + "  <target>\n";
            expect = expect + "    <inSequence/>\n";
            expect = expect + "    <outSequence/>\n";
            expect = expect + "    <faultSequence/>\n";
            expect = expect + "  </target>\n";
            expect = expect + "</proxy>";
            assertContains(expect, actual);
        } catch (Error e) {
            log.error("Validation failiure");
            fail();
        }
    }

    public static void validateJavaClass(String packageName, String className) {

        actual = bot.editorByTitle(className + CommonCons.JAVA).bot().styledText().getText();
        expect = "package " + packageName + ";\n\npublic class " + className + "{\n\n}";
        assertContains(actual, expect);
        log.error("Validation sucsessful");
    }

    public static void validateAxis2ServiceClass(String packageName, String className) {

        actual = bot.editorByTitle(className + CommonCons.JAVA).bot().styledText().getText();
        expect = "package " + packageName + ";\n\npublic class " + className + " {\n\n}\n";
        assertContains(actual, expect);
        log.error("Validation sucsessful");
    }

    public static void createGadgetProject(String projectName) {

        checkShellLoading(GadgetProjectCons.NEW_GADGET_PROJECT);
        try {
            SWTBotShell newGadgetProject = bot.shell(GadgetProjectCons.NEW_GADGET_PROJECT);
            checkButton(CommonCons.NEXT, newGadgetProject);
            newGadgetProject.bot().button(CommonCons.NEXT).click();
            try {
                newGadgetProject.bot().textWithLabel(GadgetProjectCons.PROJECT_NAME).setText(projectName);
            } catch (WidgetNotFoundException e) {
                log.error("Problem with the Lable");
                fail();
            }
            checkButton(CommonCons.NEXT, newGadgetProject);
            newGadgetProject.bot().button(CommonCons.NEXT).click();
            checkButton(CommonCons.FINISH, newGadgetProject);
            newGadgetProject.bot().button(CommonCons.FINISH).click();
            try {
                changePerspective();
            } catch (Exception e) {

            }
            bot.waitUntil(Conditions.shellCloses(newGadgetProject));
            log.error("Create Gadget Project was sucsessful");
        } catch (Exception e) {
            log.error("Create Gadget Project was unsucsessful");
            fail();
        }
    }

    public static void createNewBPELProject(String projectName) {

        try {
            checkShellLoading(BPELProjectCons.NEW_BPEL_PROJECT);
            newWorkflow = bot.shell(BPELProjectCons.NEW_BPEL_PROJECT);
            checkButton(CommonCons.NEXT, newWorkflow);
            newWorkflow.bot().button(CommonCons.NEXT).click();
            try {
                newWorkflow.bot().textWithLabel(BPELProjectCons.PROJECT_NAME).setText(projectName);

            } catch (WidgetNotFoundException e) {
                log.error("Problem with the Lable");
                fail();
            }
            newWorkflow.bot().checkBox(CommonCons.USE_DEFAULT_LOCATION).click();
            newWorkflow.bot().checkBox(CommonCons.USE_DEFAULT_LOCATION).click();
            checkButton(CommonCons.NEXT, newWorkflow);
            newWorkflow.bot().button(CommonCons.NEXT).click();
            newWorkflow.bot().checkBox(CommonCons.SPECIFY_PARENT_FROM_WORKSPACE).click();
            newWorkflow.bot().checkBox(CommonCons.SPECIFY_PARENT_FROM_WORKSPACE).click();
            checkButton(CommonCons.FINISH, newWorkflow);
            newWorkflow.bot().button(CommonCons.FINISH).click();
            // bot.waitUntil(Conditions.shellCloses(newWorkflow));
            try {
                changePerspective();
            } catch (AssertionError e) {

            }
            try {
                bot.editorByTitle(projectName + CommonCons.BPEL).show();
                log.error("Create New BPEL Project Sucsessful");
            } catch (WidgetNotFoundException e) {
                log.error("Editor didnt load");
                fail();
            }
        } catch (Exception e) {
            log.error("Create New BPEL Project unsucsessful");
            fail();
        }

    }

    public static void changePerspective() {
        try {
            changePerspective = bot.shell(CommonCons.OPEN_ASSOCIATED_PERSPECTIVE);
            if (changePerspective.isActive()) {
                changePerspective.bot().button(CommonCons.YES).click();
                bot.waitUntil(Conditions.shellCloses(changePerspective));
            }
        } catch (WidgetNotFoundException e) {
            log.error("Project unsucsessful");
            fail();
        }
    }

    public void importBPELProject(String path) {

        checkShellLoading(BPELProjectCons.NEW_BPEL_PROJECT);
        newWorkflow = bot.shell(BPELProjectCons.NEW_BPEL_PROJECT);
        newWorkflow.bot().radio(BPELProjectCons.IMPORT_BPEL_WORKFLOW).click();
        try {
            newWorkflow.bot().textWithLabel(BPELProjectCons.BPEL_ARCHIVE_FILE).setText(path);

        } catch (WidgetNotFoundException e) {
            log.error("Problem with the Lable");
            fail();
        }
        checkButton(CommonCons.FINISH, newWorkflow);
        newWorkflow.bot().button(CommonCons.FINISH).click();
        bot.waitUntil(Conditions.shellCloses(newWorkflow));
    }

    public static void createMediatorProject(String projectName, String packageName, String className) {

        checkShellLoading(MediatorProjectCons.NEW_MEDIATOR_ARTIFACT);
        SWTBotShell newMediator = bot.shell(MediatorProjectCons.NEW_MEDIATOR_ARTIFACT);
        checkButton(CommonCons.NEXT, newMediator);
        newMediator.bot().button(CommonCons.NEXT).click();
        try {
            newMediator.bot().textWithLabel(MediatorProjectCons.PROJECT_NAME).setText(projectName);
            newMediator.bot().textWithLabel(MediatorProjectCons.PACKAGE_NAME).setText(packageName);
            newMediator.bot().textWithLabel(MediatorProjectCons.CLASS_NAME).setText(className);

        } catch (WidgetNotFoundException e) {
            log.error("Problem with the Lable");
            fail();
        }
        checkButton(CommonCons.FINISH, newMediator);
        newMediator.bot().button(CommonCons.FINISH).click();
        bot.waitUntil(Conditions.shellCloses(newMediator));
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
            log.error("Editor didnt load");
            fail();
        }
    }

    public static void openProjectFromRightClick(String projectName, String[] path, String projectType) {

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
            log.error("Cannot open the project");
            fail();

        } catch (Error erro) {
            log.error("Cannot open the project");
            fail();

        }
    }

    public static void openProjectFromRightClick(String projectName, String projectType) {
        try {
            bot.viewByTitle("Project Explorer").show();
            bot.tree().getTreeItem(projectName).select();
            bot.tree().getTreeItem(projectName).contextMenu("New").menu(projectType).click();
        } catch (WidgetNotFoundException /* | TimeoutException */e) {
            log.error("Cannot open the project");
            fail();

        }
    }

    public static void createJavaClass(String className) {

        checkShellLoading(JavaCons.NEW_JAVA_CLASS);
        SWTBotShell newClass = bot.shell(JavaCons.NEW_JAVA_CLASS);
        newClass.bot().textWithLabel(JavaCons.NA_ME).setText(className);
        newClass.bot().button(CommonCons.FINISH).click();
        bot.waitUntil(Conditions.shellCloses(newClass));
    }

    public static void openPerspective(String perspective) {
        try {
            bot.toolbarButtonWithTooltip(CommonCons.OPEN_PERSPECTIVE).click();
            checkShellLoading(CommonCons.OPEN_PERSPECTIVE);
            SWTBotShell openPerspectiveShell = bot.shell(CommonCons.OPEN_PERSPECTIVE);
            openPerspectiveShell.bot().table().select(perspective);
            openPerspectiveShell.bot().button(CommonCons.OK).click();
            bot.waitUntil(Conditions.shellCloses(openPerspectiveShell));
        }

        catch (WidgetNotFoundException e) {
            log.error("Widget cannot be load");
            fail();
        }
    }

    public static void appFactoryLogin(String emailUserName, String domainName, String password) {
        try {
            bot.viewByTitle(AppFactoryCons.APPLICATIONS_LIST).show();
        } catch (WidgetNotFoundException e) {
            bot.menu("Window").menu("Show View").menu("Other...").click();
            SWTBotShell showView = bot.shell("Show View");
            showView.bot().text().setText(AppFactoryCons.APPLICATIONS_LIST);
            showView.bot().button("OK").click();
        }
        try {
            bot.toolbarButtonWithTooltip(AppFactoryCons.LOGIN2).click();
            checkShellLoading(AppFactoryCons.APP_CLOUD_APP_FACTORY_LOGIN);
            SWTBotShell login = bot.shell(AppFactoryCons.APP_CLOUD_APP_FACTORY_LOGIN);
            login.bot().textWithLabel(AppFactoryCons.EMAIL).setText(emailUserName + "@" + domainName);
            login.bot().textWithLabel(AppFactoryCons.PASSWORD2).setText(password);
            login.bot().button(CommonCons.OK).click();
            bot.waitUntil(Conditions.shellCloses(login));
            bot.viewByTitle(AppFactoryCons.APPLICATIONS_LIST).show();
            log.error("Login successful");
        } catch (TimeoutException e) {
            log.error("Fail to login");
            fail();
        } catch (WidgetNotFoundException e) {
            log.error("Problem with the login widget");
            fail();
        }

    }

    public static void apiManagerLogin(String userName, String password) {
        try {
            bot.toolbarButtonWithTooltip(APIMCons.LOGIN2).click();
            checkShellLoading(APIMCons.LOGIN_TO_API_MANAGER_REGISTRY);
            SWTBotShell login = bot.shell(APIMCons.LOGIN_TO_API_MANAGER_REGISTRY);
            login.bot().textWithLabel(APIMCons.USER_NAME).setText(userName);
            login.bot().textWithLabel(APIMCons.PASSWORD2).setText(password);
            bot.button(CommonCons.OK).click();
            bot.waitUntil(Conditions.shellCloses(login));
            log.error("Login successful");
            pass();

        } catch (TimeoutException e) {
            log.error("Fail to login");
            fail();
        } catch (WidgetNotFoundException e) {
            log.error("Problem with the login widget");
            fail();
        }

    }

    public static void deleteWithContent(String projectName) {
        try {
            bot.saveAllEditors();
            bot.tree().getTreeItem(projectName).select();
            bot.tree().getTreeItem(projectName).contextMenu("Delete").click();
            bot.sleep(2000);
        } catch (WidgetNotFoundException e) {
            log.error("Project not found");
            fail();
        }
        try {
            SWTBotShell deleteShell = bot.shell(CommonCons.DELETE_RESOURCES);
            deleteShell.bot().checkBox(CommonCons.DELETE_PROJECT_CONTENTS_ON_DISK_CANNOT_BE_UNDONE).click();
            deleteShell.bot().button(CommonCons.OK).click();
            bot.button(CommonCons.CONTINUE).click();
            log.error("Delete successful");
        } catch (Exception e) {
            log.error("Delete unsuccessful");
            fail();
        }
    }

    public static void deleteWithoutContent(String projectName) {

        try {
            bot.saveAllEditors();
            bot.tree().getTreeItem(projectName).contextMenu("Delete").click();
        } catch (WidgetNotFoundException e) {
            log.error("Project not found");
            fail();
        }
        try {
            checkShellLoading(CommonCons.DELETE_RESOURCES);
            SWTBotShell deleteShell = bot.shell(CommonCons.DELETE_RESOURCES);
            deleteShell.bot().button(CommonCons.OK).click();
            bot.button(CommonCons.CONTINUE).click();
            log.error("Delete sucsessful");
        } catch (Exception e) {
            log.error("Delete unsucsessful");
            fail();
        }
    }

    public static SWTBotTreeItem packageExplorer(String projectName, String packageName) {
        try {
            bot.tree().getTreeItem(projectName).expand();
            bot.tree().getTreeItem(projectName).getNode("src/main/java").expand();
            bot.tree().getTreeItem(projectName).getNode("src/main/java").getNode(packageName).select();
            log.error("Tree selection sucsessful");
            return bot.tree().getTreeItem(projectName).getNode("src/main/java").getNode(packageName);
        } catch (Exception e) {
            log.error("Problem with tree selection");
            fail();
            return null;
        }
    }

    public static void activateCtab(String tabName) {
        try {
            bot.cTabItem(tabName).activate();
        } catch (WidgetNotFoundException e) {
            log.error("Ctab cannot be found");
            fail();
        }
    }

    public static void closeView(String viewName) {
        bot.viewByTitle(viewName).close();
    }

}