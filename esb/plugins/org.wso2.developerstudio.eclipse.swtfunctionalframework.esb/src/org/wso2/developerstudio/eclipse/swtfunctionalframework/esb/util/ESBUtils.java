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

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertContains;
import static org.junit.Assert.fail;

import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.esb.util.constants.ESBProjectCons;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.esb.util.constants.EndPoinCons;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.esb.util.constants.ProxyServiceCons;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.esb.util.constants.SequenceCons;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonCons;

public class ESBUtils extends Util{
	
	
	public static void esbRclick(String projectName, String artiactName){
		bot.tree().getTreeItem(projectName).select();
        bot.tree().getTreeItem(projectName).contextMenu("New").menu(artiactName).click();
		
	}
	
	
    public static void createESBProject(String projectName) {
    	bot.sleep(2000);
        SWTBotShell newESBConf= bot.shell("New ESB Config Project");
        newESBConf.bot().button(CommonCons.NEXT).click();
        bot.sleep(1000);
        //bot.textWithLabel(ESBProjectCons.PROJECT_NAME).setText(projectName);
        setLableText(newESBConf, ESBProjectCons.PROJECT_NAME, projectName);
        bot.sleep(1000);
        newESBConf.bot().button(CommonCons.NEXT).click();
        bot.sleep(1000);
        //log.error("All upto Finish is fine");
        newESBConf.bot().button(CommonCons.FINISH).click();
        bot.sleep(1000);
    }
	
    public static void createNewSequence(String sequenceName, String esbProjectToSave) {

        checkShellLoading(SequenceCons.NEW_SEQUENCE_ARTIFACT);
        SWTBotShell newSeqeunce = bot.shell(SequenceCons.NEW_SEQUENCE_ARTIFACT);
        checkButton(CommonCons.NEXT, newSeqeunce);
        newSeqeunce.bot().button(CommonCons.NEXT).click();
        bot.sleep(1000);
        setLableText(newSeqeunce, SequenceCons.SEQUENCE_NAME, sequenceName);
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
        setLableText(newProxy, ProxyServiceCons.PROXY_SERVICE_NAME, serviceName);
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
        setLableText(newEndpoint, EndPoinCons.ENDPOINT_NAME, endpoint);
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
            AbstractClass.changePerspective();
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
            //log.error("Validation failiure");
            fail();
        }
    }



}
