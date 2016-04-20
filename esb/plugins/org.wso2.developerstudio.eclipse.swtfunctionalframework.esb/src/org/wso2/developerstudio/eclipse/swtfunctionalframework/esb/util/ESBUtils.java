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

package org.wso2.developerstudio.eclipse.swtfunctionalframework.esb.util;

import static org.junit.Assert.fail;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.esb.util.constants.ESBProjectConstants;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.esb.util.constants.EndPoinConstants;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.esb.util.constants.ProxyServiceConstants;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.esb.util.constants.SequenceConstants;
import org.wso2.developerstudio.eclipse.test.automation.framework.element.validator.WorkbenchElementsValidator;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.CommonConstants;
import org.wso2.developerstudio.eclipse.test.automation.utils.functional.FunctionalUtil;
import org.wso2.developerstudio.eclipse.test.automation.utils.validator.ValidatorUtil;

public class ESBUtils {

    public static void createESBProject(String projectName) {
        WorkbenchElementsValidator.bot.sleep(2000);
        SWTBotShell newESBConf = WorkbenchElementsValidator.bot.shell(ESBProjectConstants.NEW_ESB_PROJECT);
        newESBConf.bot().button(CommonConstants.NEXT).click();
        WorkbenchElementsValidator.bot.sleep(1000);
        WorkbenchElementsValidator.setLableText(newESBConf, ESBProjectConstants.PROJECT_NAME, projectName);
        WorkbenchElementsValidator.bot.sleep(1000);
        newESBConf.bot().button(CommonConstants.NEXT).click();
        WorkbenchElementsValidator.bot.sleep(1000);
        newESBConf.bot().button(CommonConstants.FINISH).click();
        WorkbenchElementsValidator.bot.sleep(1000);
    }

    public static void createNewSequence(String sequenceName, String esbProjectToSave) {

        WorkbenchElementsValidator.checkShellLoading(SequenceConstants.NEW_SEQUENCE_ARTIFACT);
        SWTBotShell newSeqeunce = WorkbenchElementsValidator.bot.shell(SequenceConstants.NEW_SEQUENCE_ARTIFACT);
        WorkbenchElementsValidator.checkButton(CommonConstants.NEXT, newSeqeunce);
        newSeqeunce.bot().button(CommonConstants.NEXT).click();
        WorkbenchElementsValidator.bot.sleep(1000);
        WorkbenchElementsValidator.setLableText(newSeqeunce, SequenceConstants.SEQUENCE_NAME, sequenceName);
        newSeqeunce.bot().button(CommonConstants.BROWSE).click();

        WorkbenchElementsValidator.checkShellLoading(CommonConstants.SELECT_FOLDER);
        WorkbenchElementsValidator.saveIn = WorkbenchElementsValidator.bot.shell(CommonConstants.SELECT_FOLDER);
        WorkbenchElementsValidator.saveIn.bot().tree().getTreeItem(esbProjectToSave).doubleClick();
        WorkbenchElementsValidator.bot.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses(WorkbenchElementsValidator.saveIn));
        newSeqeunce = WorkbenchElementsValidator.bot.shell(SequenceConstants.NEW_SEQUENCE_ARTIFACT);
        WorkbenchElementsValidator.checkButton(CommonConstants.FINISH, newSeqeunce);
        newSeqeunce.bot().button(CommonConstants.FINISH).click();
        WorkbenchElementsValidator.bot.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses(newSeqeunce));
        try {
            WorkbenchElementsValidator.bot.editorByTitle(sequenceName + CommonConstants.XML).show();
            WorkbenchElementsValidator.log.info("Create New Sequence Sucsessful");

        } catch (WidgetNotFoundException e) {
            WorkbenchElementsValidator.log.error("Editor didnt load", e);
            fail();
        }
    }

    public static void createProxyService(String serviceName) {

        WorkbenchElementsValidator.checkShellLoading(ProxyServiceConstants.NEW_PROXY_SERVICE);
        SWTBotShell newProxy = WorkbenchElementsValidator.bot.shell(ProxyServiceConstants.NEW_PROXY_SERVICE);
        WorkbenchElementsValidator.checkButton(CommonConstants.NEXT, newProxy);
        newProxy.bot().button(CommonConstants.NEXT).click();
        WorkbenchElementsValidator.setLableText(newProxy, ProxyServiceConstants.PROXY_SERVICE_NAME, serviceName);
        newProxy.bot().comboBox().setSelection(ProxyServiceConstants.CUSTOM_PROXY);
        WorkbenchElementsValidator.checkButton(CommonConstants.FINISH, newProxy);
        newProxy.bot().button(CommonConstants.FINISH).click();
        WorkbenchElementsValidator.bot.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses(newProxy));
        try {
            WorkbenchElementsValidator.bot.editorByTitle(serviceName + CommonConstants.XML).show();
            WorkbenchElementsValidator.log.info("Create New Proxy Service Sucsessful");

        } catch (WidgetNotFoundException e) {
            WorkbenchElementsValidator.log.error("Editor didnt load", e);
            fail();
        }

    }

    public static void createNewEndpointWithESB(String endpoint, String esbProject) {
        WorkbenchElementsValidator.checkShellLoading(EndPoinConstants.NEW_ENDPOINT_ARTIFACT);
        SWTBotShell newEndpoint = WorkbenchElementsValidator.bot.shell(EndPoinConstants.NEW_ENDPOINT_ARTIFACT);
        WorkbenchElementsValidator.checkButton(CommonConstants.NEXT, newEndpoint);
        newEndpoint.bot().button(CommonConstants.NEXT).click();
        newEndpoint.bot().label(EndPoinConstants.ENDPOINT_CONFIGURATION).click();
        WorkbenchElementsValidator.bot.sleep(1000);
        newEndpoint.bot().label(EndPoinConstants.ENDPOINT_CONFIGURATION).click();
        WorkbenchElementsValidator.setLableText(newEndpoint, EndPoinConstants.ENDPOINT_NAME, endpoint);
        newEndpoint.bot().comboBox().setSelection(EndPoinConstants.DEFAULT_ENDPOINT);
        WorkbenchElementsValidator.bot.link().click();

        createESBProject(esbProject);

        newEndpoint.bot().button(CommonConstants.BROWSE).click();

        WorkbenchElementsValidator.checkShellLoading(CommonConstants.SELECT_FOLDER);
        WorkbenchElementsValidator.saveIn = WorkbenchElementsValidator.bot.shell(CommonConstants.SELECT_FOLDER);
        WorkbenchElementsValidator.saveIn = WorkbenchElementsValidator.bot.shell(CommonConstants.SELECT_FOLDER);
        WorkbenchElementsValidator.saveIn.bot().tree().getTreeItem(esbProject).doubleClick();
        WorkbenchElementsValidator.bot.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses(WorkbenchElementsValidator.saveIn));

        newEndpoint = WorkbenchElementsValidator.bot.shell(EndPoinConstants.NEW_ENDPOINT_ARTIFACT);
        WorkbenchElementsValidator.checkButton(CommonConstants.FINISH, newEndpoint);
        newEndpoint.bot().button(CommonConstants.FINISH).click();
        try {
        	FunctionalUtil.switchPerspectiveInProjectCreation();
        } catch (AssertionError e) {

        }
        WorkbenchElementsValidator.bot.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses(newEndpoint));
        try {
            WorkbenchElementsValidator.bot.editorByTitle(endpoint + CommonConstants.XML).show();
            WorkbenchElementsValidator.log.info("Create New Endpoint Sucsessful");

        } catch (WidgetNotFoundException e) {
            WorkbenchElementsValidator.log.error("Editor didnt load", e);
            fail();
        }
    }

    public static void validateProxyServicexml(String proxyService) {

        String editorName;
        WorkbenchElementsValidator.expectedBuffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        WorkbenchElementsValidator.expectedBuffer.append("<proxy name=\"" + proxyService + "\" startOnLoad=\"true\" trace=\"disable\"\n");
        WorkbenchElementsValidator.expectedBuffer.append("  transports=\"http https\" xmlns=\"http://ws.apache.org/ns/synapse\">\n");
        WorkbenchElementsValidator.expectedBuffer.append("  <target>\n");
        WorkbenchElementsValidator.expectedBuffer.append("    <inSequence/>\n");
        WorkbenchElementsValidator.expectedBuffer.append("    <outSequence/>\n");
        WorkbenchElementsValidator.expectedBuffer.append("    <faultSequence/>\n");
        WorkbenchElementsValidator.expectedBuffer.append("  </target>\n");
        WorkbenchElementsValidator.expectedBuffer.append("</proxy>");
        WorkbenchElementsValidator.expect = WorkbenchElementsValidator.expectedBuffer.toString();

        WorkbenchElementsValidator.bot.cTabItem(CommonConstants.SOURCE).activate();
        editorName = proxyService + CommonConstants.XML;
		ValidatorUtil.contentValidation(editorName, WorkbenchElementsValidator.expect);

    }

}
