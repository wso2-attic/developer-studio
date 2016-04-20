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

package org.wso2.developerstudio.eclipse.registry.apim.perspective.test;

import java.io.IOException;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.wso2.developerstudio.eclipse.test.automation.framework.executor.Executor;
import org.wso2.developerstudio.eclipse.test.automation.framework.runner.*;
import org.wso2.developerstudio.eclipse.test.automation.utils.functional.FunctionalUtil;
import org.wso2.developerstudio.eclipse.test.automation.utils.server.AutomationFrameworkException;
import org.wso2.developerstudio.eclipse.test.automation.utils.server.TestServerManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.apim.util.APIMUtils;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.apim.util.constants.APIMConstants;

/* Testing APIM perspective
 * Open perspective and login
 * Create a newsequence
 * Commit it
 * Rename it
 * Discard all changes
 * Create another sequence
 * Delete it without commiting
 * Create two more and click push all changes
 * delete them both and discard changes
 * Copy and paste a sequence
 * Commit it
 * delete other file except the first one and push all changes
 */

@RunWith(OrderedRunner.class)
public class TestApiManager extends Executor {

    private String url = "https://localhost:9443/";
    private String userName = "admin";
    private String passWord = "admin";
    private String firstSequence = "newSequnce";
    private String secondSequence = "newSequnce2";
    private String thirdSequence = "newSequnce3";
    private APIMUtils apimUtils = new APIMUtils();
    private static TestServerManager testServer = new TestServerManager("wso2am-1.8.0.zip");


    @BeforeClass
    public static void  serverStartup(){

        try {
            testServer.startServer();
        } catch (XPathExpressionException | AutomationFrameworkException
                | IOException e) {
        }
    }
    @Test
    @Order(order = 1)
    public void login() throws Exception {

        FunctionalUtil.openPerspective(APIMConstants.WSO2_API_MANAGER);
        apimUtils.login(userName, passWord, url, null);
        try{
            APIMUtils.expandTree(userName, url);
        } catch(Exception e){
        }

    }

    @Test
    @Order(order = 2)
    public void creatFirstSequence() {

        APIMUtils.expandTree(userName, url);
        APIMUtils.createSequenceAPIM(userName, url, APIMConstants.SEQUENCE_TYPE_IN, firstSequence);
    }

    @Test
    @Order(order = 3)
    public void commitFirstSequence() {

        APIMUtils.commitFile(userName, url, APIMConstants.SEQUENCE_TYPE_IN, firstSequence);
    }
    
    @Test
    @Order(order = 4)
    public void renameFirstSequenceAndDiscard() {
        APIMUtils.renameSequenceAPIM(userName, url, APIMConstants.SEQUENCE_TYPE_IN, firstSequence, secondSequence);
        APIMUtils.discardAllChanges();
    }
    
    @Test
    @Order(order = 5)
    public void creatSecondSequenceAndDelete() {
        APIMUtils.createSequenceAPIM(userName, url, APIMConstants.SEQUENCE_TYPE_IN, secondSequence);
        APIMUtils.deleteSequenceAPIM(userName, url, APIMConstants.SEQUENCE_TYPE_IN, secondSequence);
    }
    
    @Test
    @Order(order = 6)
    public void creatTwoSequencesAndPushChanges() {
        APIMUtils.createSequenceAPIM(userName, url, APIMConstants.SEQUENCE_TYPE_IN, secondSequence);
        APIMUtils.createSequenceAPIM(userName, url, APIMConstants.SEQUENCE_TYPE_IN, thirdSequence);
        APIMUtils.clickPushAllChanges();
    }
    
    @Test
    @Order(order = 7)
    public void deleteTwoSequencesAndDiscardChanges() {
    APIMUtils.deleteSequenceAPIM(userName, url, APIMConstants.SEQUENCE_TYPE_IN, secondSequence);
    APIMUtils.deleteSequenceAPIM(userName, url, APIMConstants.SEQUENCE_TYPE_IN, thirdSequence);
    APIMUtils.discardAllChanges();
    }
    
    @Test
    @Order(order = 8)
    public void copyAndPasteSequence() {
    APIMUtils.copyPasteSequence(userName, url, APIMConstants.SEQUENCE_TYPE_OUT, APIMConstants.SEQUENCE_TYPE_IN,
            "log_out_message");
    APIMUtils.commitFile(userName, url, APIMConstants.SEQUENCE_TYPE_IN, "log_out_message");

    }

    @Test
    @Order(order = 9)
    public void deleteSequence() {
    
        APIMUtils.deleteSequenceAPIM(userName, url, APIMConstants.SEQUENCE_TYPE_IN, firstSequence);
        APIMUtils.deleteSequenceAPIM(userName, url, APIMConstants.SEQUENCE_TYPE_IN, "log_out_message");
        APIMUtils.deleteSequenceAPIM(userName, url, APIMConstants.SEQUENCE_TYPE_IN, secondSequence);
        APIMUtils.deleteSequenceAPIM(userName, url, APIMConstants.SEQUENCE_TYPE_IN, thirdSequence);
        APIMUtils.clickPushAllChanges();
        APIMUtils.expandTree(userName, url);
    }

    @AfterClass
    public static void severShutdown(){
        try {
            testServer.stopServer();
        } catch (AutomationFrameworkException e) {
        }
    }
}
