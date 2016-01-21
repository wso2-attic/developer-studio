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

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.apim.util.APIMUtils;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;

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
public class TestApiManager extends Setup {

    String userName = "admin";
    String passWord = "admin";
    String sequence1 = "newSequnce";
    String sequence2 = "newSequnce2";
    String sequence3 = "newSequnce3";
    SWTBotTreeItem mainTree;

    @Test
    @Order(order = 1)
    public void login() throws Exception {

        AbstractClass.openPerspective("WSO2 APIManager");

        APIMUtils.apiManagerLogin(userName, passWord);

    }

    @Test
    @Order(order = 2)
    public void creatFirstSequence() {

        APIMUtils.expandTree(userName);

        APIMUtils.createSequenceAPIM(userName, "in", sequence1);

    }

    @Test
    @Order(order = 3)
    public void creatNew() {

        APIMUtils.commitFile(userName, "in", sequence1);

        APIMUtils.renameSequenceAPIM(userName, "in", sequence1, sequence2);

        APIMUtils.discardAllChanges();

        APIMUtils.createSequenceAPIM(userName, "in", sequence2);

        APIMUtils.deleteSequenceAPIM(userName, "in", sequence2);

        APIMUtils.createSequenceAPIM(userName, "in", sequence2);

        APIMUtils.createSequenceAPIM(userName, "in", sequence3);

        APIMUtils.clickPushAllChanges();

        APIMUtils.deleteSequenceAPIM(userName, "in", sequence2);

        APIMUtils.deleteSequenceAPIM(userName, "in", sequence3);

        APIMUtils.discardAllChanges();
        APIMUtils.copyPasteSequence(userName, "out", "in", "log_out_message");

        APIMUtils.commitFile(userName, "in", "log_out_message");

        APIMUtils.deleteSequenceAPIM(userName, "in", "log_out_message");

        APIMUtils.deleteSequenceAPIM(userName, "in", sequence2);

        APIMUtils.deleteSequenceAPIM(userName, "in", sequence3);

        APIMUtils.clickPushAllChanges();
        APIMUtils.expandTree(userName);

    }

    @Test
    @Order(order = 4)
    public void deleteSequence() {
        APIMUtils.deleteSequenceAPIM(userName, "in", sequence1);

        APIMUtils.clickPushAllChanges();
    }
    /*
     *
     * @Test
     * public void hsdk(){
     * String userName = "admin";
     * String passWord = "admin";
     *
     * this.openPerspective("WSO2 APIManager");
     *
     * SWTBotView apimRegistry = bot.viewByTitle("WSO2 APIManager");
     * apimRegistry.show();
     *
     *
     * this.apiManagerLogin(userName , passWord);
     *
     * SWTBotTreeItem mainTree = apimRegistry.bot().tree().getTreeItem(userName +
     * "@https://localhost:9443/").getNode("Repository").getNode("customsequences");
     * mainTree.getNode("in").expand();
     * mainTree.getNode("in").getNode(sequence1 + ".xml").doubleClick();
     *
     * SWTGefBot bot1=new SWTGefBot();
     * //bot1.gefEditor(sequence1 + ".xml").click("Links");
     * bot1.gefEditor(sequence1 + ".xml").show();
     * //bot1.gefEditor(sequence1 + ".xml").click(20,60);
     * bot1.gefEditor(sequence1 + ".xml").getEditPart("Hide palette").click(); //getEditPart("Links"));
     * bot.sleep(2000);
     * //bot1.gefEditor(sequence1 + ".xml").toolbarButton("Hide palette").click();
     * //bot1.gefEditor(sequence1 + ".xml").bot().toolbarButtonWithTooltip("Hide palette").click();
     * bot1.gefEditor(sequence1 + ".xml").close();//toolbarButton("Hide palette").click();
     * //gmfEditor.bot(). //= bot.editorByTitle("newSequence.xml");
     *
     * }
     */

}