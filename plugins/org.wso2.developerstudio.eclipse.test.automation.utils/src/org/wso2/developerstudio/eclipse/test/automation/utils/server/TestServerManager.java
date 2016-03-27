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

package org.wso2.developerstudio.eclipse.test.automation.utils.server;

import javax.xml.xpath.XPathExpressionException;

import org.wso2.developerstudio.eclipse.test.automation.framework.element.validator.WorkbenchElementsValidator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestServerManager {
    protected CarbonServerManager carbonServer;
    protected String carbonZip = System.getProperty(FrameworkConstants.SYSTEM_PROPERTY_BASEDIR_LOCATION, ".")
            + File.separator + "Resources";
    protected int portOffset;
    protected Map<String, String> commandMap = new HashMap<String, String>();
    protected String carbonHome;
    private Process process;

    public TestServerManager(String carbonZip) {
        carbonServer = new CarbonServerManager();
        this.carbonZip = this.carbonZip + File.separator + carbonZip;
    }

    public String getCarbonZip() {
        return carbonZip;
    }

    public String getCarbonHome() {
        return carbonHome;
    }

    public int getPortOffset() {
        return portOffset;
    }

    public void configureServer() throws AutomationFrameworkException {

    }

    public Map<String, String> getCommands() {
        return commandMap;
    }

    /**
     * This method is called for starting a Carbon server in preparation for execution of a
     * TestSuite
     * <p/>
     * Add the @BeforeSuite TestNG annotation in the method overriding this method
     *
     * @return The CARBON_HOME
     * @throws java.io.IOException If an error occurs while copying the deployment artifacts into the
     *             Carbon server
     */
    public Process startServer() throws AutomationFrameworkException, IOException, XPathExpressionException {
        if (carbonHome == null) {
            if (carbonZip == null) {
                carbonZip = System.getProperty(FrameworkConstants.SYSTEM_PROPERTY_CARBON_ZIP_LOCATION);
            }
            if (carbonZip == null) {
                throw new IllegalArgumentException("carbon zip file cannot find in the given location");
            }
            carbonHome = carbonServer.setUpCarbonHome(carbonZip);
            configureServer();
        }
        if (commandMap.get(ExtensionConstants.SERVER_STARTUP_PORT_OFFSET_COMMAND) != null) {
            this.portOffset = Integer.parseInt(commandMap.get(ExtensionConstants.SERVER_STARTUP_PORT_OFFSET_COMMAND));
        } else {
            this.portOffset = 0;
        }

        try {
            MutualSSLClient.setKeyStores();
        } catch (Exception e1) {

            WorkbenchElementsValidator.log.error("Key store loading problem", e1);
        }
        try {
            carbonServer.startServerUsingCarbonHome(carbonHome, commandMap);
        } catch (Exception e) {
            WorkbenchElementsValidator.log.error("Server strating problem", e);
        }
        return process;
    }

    /**
     * Restarting server already started by the method startServer
     * 
     * @throws AutomationFrameworkException
     */
    public void restartGracefully() throws AutomationFrameworkException {
        if (carbonHome == null) {
            throw new AutomationFrameworkException("No Running Server found to restart. "
                    + "Please make sure whether server is started");
        }
        // carbonServer.restartGracefully();
    }

    /**
     * This method is called for stopping a Carbon server
     * <p/>
     * Add the @AfterSuite annotation in the method overriding this method
     *
     * @throws AutomationFrameworkException If an error occurs while shutting down the server
     */
    public void stopServer() throws AutomationFrameworkException {
        carbonServer.serverShutdown(portOffset);
    }

}
