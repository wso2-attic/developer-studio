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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.wso2.developerstudio.eclipse.swttests.testcase.TestApiManager;
import org.wso2.developerstudio.eclipse.swttests.testcase.TestAppFactory;
import org.wso2.developerstudio.eclipse.swttests.testcase.TestAxis2ServiceProject;
import org.wso2.developerstudio.eclipse.swttests.testcase.TestBPELWorkflow;
import org.wso2.developerstudio.eclipse.swttests.testcase.TestBusinessRulesServer;
import org.wso2.developerstudio.eclipse.swttests.testcase.TestCarbonUIBundleProject;
import org.wso2.developerstudio.eclipse.swttests.testcase.TestESBConfigProject;
import org.wso2.developerstudio.eclipse.swttests.testcase.TestMediatorProject;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 */
@RunWith(Suite.class)
@SuiteClasses({ TestBPELWorkflow.class, TestApiManager.class, TestAppFactory.class,
        TestCarbonUIBundleProject.class, TestBusinessRulesServer.class, TestESBConfigProject.class, TestMediatorProject.class, TestAxis2ServiceProject.class })
public class AllTests {

}
