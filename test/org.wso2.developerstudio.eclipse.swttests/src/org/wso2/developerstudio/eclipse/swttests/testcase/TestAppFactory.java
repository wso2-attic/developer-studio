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

package org.wso2.developerstudio.eclipse.swttests.testcase;

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertContains;
import static org.junit.Assert.fail;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotStyledText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.Order;
import org.junit.OrderedRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
//import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Login;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AppFactoryUtils;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.AppFactoryCons;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonCons;

/* Testing app factory prospective
 * Open the app factory prospective
 * login using login button
 * right click and open in "testapplicationone"
 * wait "testapplicationone(Loading)" to be "testapplicationone(Opened)"
 * "build ,Deploy ,Build ,Logs ,Check Out ,Check Out and Import" to the main
 */

@RunWith(OrderedRunner.class)
public class TestAppFactory extends Setup {

    protected String emailUserName = "kaviththiranga";
    protected String domainName = "gmail.com";

    @Test
    @Order(order = 1)
    public void openAppFactoryPerspective() throws Exception {
        AbstractClass.openPerspective(AppFactoryCons.WSO2_APP_FACTORY);

    }

    @Test
    @Order(order = 2)
    public void loginToAppFactory() throws Exception {

        String password = "123456";

        AbstractClass.appFactoryLogin(emailUserName, domainName, password);

    }

    @Test
    @Order(order = 3)
    public void buildTest() throws Exception {

        AppFactoryUtils.appFactoryApplicationAction("trunk/main", AppFactoryCons.BUILD);

        /*
         * bot.toolbarButtonWithTooltip(emailUserName + "." + domainName + "@developerstudio").click();
         * bot.button(CommonCons.CANCEL).click();
         * bot.toolbarButtonWithTooltip(CommonCons.REFRESH).click();
         */

    }

    @Test
    @Order(order = 4)
    public void deployTest() throws Exception {
        AppFactoryUtils.appFactoryApplicationAction("trunk/main", AppFactoryCons.DEPLOY);
    }

    @Test
    @Order(order = 5)
    public void buildLogsTest() throws Exception {

        AppFactoryUtils.appFactoryApplicationAction("trunk/main", AppFactoryCons.BUILD_LOGS);
    }
    
    @Test
    @Order(order = 6)
    public void cancel()throws Exception{
        AbstractClass.closeView(AppFactoryCons.APPLICATIONS_LIST);
    }

}