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

package org.wso2.developerstudio.appfactory.test;

import org.wso2.developerstudio.eclipse.swtfunctionalframework.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Login;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.CommonUtil;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.appfactory.util.AppFactoryUtils;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.appfactory.util.constants.AppFactoryConstants;

/* Testing app factory prospective
 * Open the app factory prospective
 * login using login button
 * right click and open in "testapplicationone"
 * wait "testapplicationone(Loading)" to be "testapplicationone(Opened)"
 * "build ,Deploy ,Build ,Logs ,Check Out ,Check Out and Import" to the main
 */

@RunWith(OrderedRunner.class)
public class TestAppFactory extends Setup {

    protected String email = "kaviththiranga@gmail.com";
    AppFactoryUtils appFactoryUtil = new AppFactoryUtils();

    @Test
    @Order(order = 1)
    public void openAppFactoryPerspective() throws Exception {
    	CommonUtil.openPerspective(AppFactoryConstants.WSO2_APP_FACTORY);

    }

    @Test
    @Order(order = 2)
    public void loginToAppFactory() throws Exception {

        String password = "123456";

        appFactoryUtil.login(email, password);

    }

    @Test
    @Order(order = 3)
    public void buildTest() throws Exception {

        AppFactoryUtils.appFactoryApplicationAction("trunk/main", AppFactoryConstants.BUILD);

    }

    @Test
    @Order(order = 4)
    public void deployTest() throws Exception {
        AppFactoryUtils.appFactoryApplicationAction("trunk/main", AppFactoryConstants.DEPLOY);
    }

    @Test
    @Order(order = 5)
    public void buildLogsTest() throws Exception {

        AppFactoryUtils.appFactoryApplicationAction("trunk/main", AppFactoryConstants.BUILD_LOGS);
    }
    
    @Test
    @Order(order = 6)
    public void cancel()throws Exception{
    	CommonUtil.closeView(AppFactoryConstants.APPLICATIONS_LIST);
    }

}