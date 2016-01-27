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
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.CommonUtil;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonConstants;

public class ESBUtils {

    public static void esbRclick(String projectName, String artiactName) {
        Util.bot.tree().getTreeItem(projectName).select();
        Util.bot.tree().getTreeItem(projectName).contextMenu("New").menu(artiactName).click();

    }

    public static void createESBProject(String projectName) {
        Util.bot.sleep(2000);
        SWTBotShell newESBConf = Util.bot.shell("New ESB Config Project");
        newESBConf.bot().button(CommonConstants.NEXT).click();
        Util.bot.sleep(1000);
        Util.setLableText(newESBConf, ESBProjectConstants.PROJECT_NAME, projectName);
        Util.bot.sleep(1000);
        newESBConf.bot().button(CommonConstants.NEXT).click();
        Util.bot.sleep(1000);
        newESBConf.bot().button(CommonConstants.FINISH).click();
        Util.bot.sleep(1000);
    }

    public static void createNewSequence(String sequenceName, String esbProjectToSave) {

        Util.checkShellLoading(SequenceConstants.NEW_SEQUENCE_ARTIFACT);
        SWTBotShell newSeqeunce = Util.bot.shell(SequenceConstants.NEW_SEQUENCE_ARTIFACT);
        Util.checkButton(CommonConstants.NEXT, newSeqeunce);
        newSeqeunce.bot().button(CommonConstants.NEXT).click();
        Util.bot.sleep(1000);
        Util.setLableText(newSeqeunce, SequenceConstants.SEQUENCE_NAME, sequenceName);
        newSeqeunce.bot().button(CommonConstants.BROWSE).click();

        Util.checkShellLoading(CommonConstants.SELECT_FOLDER);
        Util.saveIn = Util.bot.shell(CommonConstants.SELECT_FOLDER);
        Util.saveIn.bot().tree().getTreeItem(esbProjectToSave).doubleClick();
        Util.bot.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses(Util.saveIn));
        newSeqeunce = Util.bot.shell(SequenceConstants.NEW_SEQUENCE_ARTIFACT);
        Util.checkButton(CommonConstants.FINISH, newSeqeunce);
        newSeqeunce.bot().button(CommonConstants.FINISH).click();
        Util.bot.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses(newSeqeunce));
        try {
            Util.bot.editorByTitle(sequenceName + CommonConstants.XML).show();
            Util.log.info("Create New Sequence Sucsessful");

        } catch (WidgetNotFoundException e) {
            Util.log.error("Editor didnt load");
            fail();
        }
    }

    public static void createProxyService(String serviceName) {

        Util.checkShellLoading(ProxyServiceConstants.NEW_PROXY_SERVICE);
        SWTBotShell newProxy = Util.bot.shell(ProxyServiceConstants.NEW_PROXY_SERVICE);
        Util.checkButton(CommonConstants.NEXT, newProxy);
        newProxy.bot().button(CommonConstants.NEXT).click();
        Util.setLableText(newProxy, ProxyServiceConstants.PROXY_SERVICE_NAME, serviceName);
        newProxy.bot().comboBox().setSelection(ProxyServiceConstants.CUSTOM_PROXY);
        Util.checkButton(CommonConstants.FINISH, newProxy);
        newProxy.bot().button(CommonConstants.FINISH).click();
        Util.bot.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses(newProxy));
        try {
            Util.bot.editorByTitle(serviceName + CommonConstants.XML).show();
            Util.log.info("Create New Proxy Service Sucsessful");

        } catch (WidgetNotFoundException e) {
            Util.log.error("Editor didnt load");
            fail();
        }

    }

    public static void createNewEndpointWithESB(String endpoint, String esbProject) {
        Util.checkShellLoading(EndPoinConstants.NEW_ENDPOINT_ARTIFACT);
        SWTBotShell newEndpoint = Util.bot.shell(EndPoinConstants.NEW_ENDPOINT_ARTIFACT);
        Util.checkButton(CommonConstants.NEXT, newEndpoint);
        newEndpoint.bot().button(CommonConstants.NEXT).click();
        newEndpoint.bot().label(EndPoinConstants.ENDPOINT_CONFIGURATION).click();
        Util.bot.sleep(1000);
        newEndpoint.bot().label(EndPoinConstants.ENDPOINT_CONFIGURATION).click();
        Util.setLableText(newEndpoint, EndPoinConstants.ENDPOINT_NAME, endpoint);
        newEndpoint.bot().comboBox().setSelection(EndPoinConstants.DEFAULT_ENDPOINT);
        Util.bot.link().click();

        createESBProject(esbProject);

        newEndpoint.bot().button(CommonConstants.BROWSE).click();

        Util.checkShellLoading(CommonConstants.SELECT_FOLDER);
        Util.saveIn = Util.bot.shell(CommonConstants.SELECT_FOLDER);
        Util.saveIn = Util.bot.shell(CommonConstants.SELECT_FOLDER);
        Util.saveIn.bot().tree().getTreeItem(esbProject).doubleClick();
        Util.bot.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses(Util.saveIn));

        newEndpoint = Util.bot.shell(EndPoinConstants.NEW_ENDPOINT_ARTIFACT);
        Util.checkButton(CommonConstants.FINISH, newEndpoint);
        newEndpoint.bot().button(CommonConstants.FINISH).click();
        try {
            CommonUtil.switchPerspectiveInCreatingProjects();
        } catch (AssertionError e) {

        }
        Util.bot.waitUntil(org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses(newEndpoint));
        try {
            Util.bot.editorByTitle(endpoint + CommonConstants.XML).show();
            Util.log.info("Create New Endpoint Sucsessful");

        } catch (WidgetNotFoundException e) {
            Util.log.error("Editor didnt load");
            fail();
        }
    }

    public static void validateProxyServicexml(String proxyService) {

        String editorName;
        Util.expectedBuffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        Util.expectedBuffer.append("<proxy name=\"" + proxyService + "\" startOnLoad=\"true\" trace=\"disable\"\n");
        Util.expectedBuffer.append("  transports=\"http https\" xmlns=\"http://ws.apache.org/ns/synapse\">\n");
        Util.expectedBuffer.append("  <target>\n");
        Util.expectedBuffer.append("    <inSequence/>\n");
        Util.expectedBuffer.append("    <outSequence/>\n");
        Util.expectedBuffer.append("    <faultSequence/>\n");
        Util.expectedBuffer.append("  </target>\n");
        Util.expectedBuffer.append("</proxy>");
        Util.expect = Util.expectedBuffer.toString();

        Util.bot.cTabItem(CommonConstants.SOURCE).activate();
        editorName = proxyService + CommonConstants.XML;
        CommonUtil.contentValidation(editorName, Util.expect);

    }

}
