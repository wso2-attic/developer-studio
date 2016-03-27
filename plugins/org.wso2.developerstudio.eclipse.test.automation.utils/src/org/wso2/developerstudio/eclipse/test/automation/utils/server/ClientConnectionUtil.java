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

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.wso2.developerstudio.eclipse.test.automation.framework.element.validator.WorkbenchElementsValidator;

public class ClientConnectionUtil {

    private static final long TIMEOUT = 120000;

    public static void sendForcefulShutDownRequest(String backendURL, String userName, String password)
            throws AutomationFrameworkException {
        try {
            ServiceClient serviceClient = new ServiceClient();
            Options opts = new Options();
            opts.setManageSession(true);
            opts.setTo(new EndpointReference(backendURL + FrameworkConstants.SERVER_ADMIN_SERVICE_NAME));
            opts.setAction("urn:shutdown");
            HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
            auth.setUsername(userName);
            auth.setPassword(password);
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE, auth);

            serviceClient.setOptions(opts);
            serviceClient.sendReceive(ClientConnectionUtil.createPayLoadShutDownServerForcefully());

        } catch (AxisFault e) {
            WorkbenchElementsValidator.log.error("Unable to shutdown carbon server forcefully..", e);
            throw new AutomationFrameworkException("Unable to shutdown carbon server forcefully..", e);
        }
    }

    public static OMElement createPayLoadShutDownServerForcefully() {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://org.apache.axis2/xsd", "xsd");
        return fac.createOMElement("shutdown", omNs);
    }

    public static boolean isPortOpen(int port) {
        Socket socket = null;
        boolean isPortOpen = false;
        try {
            InetAddress address = InetAddress.getLocalHost();
            socket = new Socket(address, port);
            isPortOpen = socket.isConnected();
            if (isPortOpen) {
                WorkbenchElementsValidator.log.info("Successfully connected to the server on port " + port);
            }
        } catch (IOException e) {
            WorkbenchElementsValidator.log.info("Port " + port + " is closed and available for use");
            isPortOpen = false;
        } finally {
            try {
                if ((socket != null) && (socket.isConnected())) {
                    socket.close();
                }
            } catch (IOException e) {
                WorkbenchElementsValidator.log.error(
                        "Can not close the socket with is used to check the server status ", e);
            }
        }
        return isPortOpen;
    }

    public static void waitForLogin(String backendUrl, String userName, String password)
            throws AutomationFrameworkException {

        long startTime = System.currentTimeMillis();
        boolean loginSuccess = false;
        String superAdminName = userName;
        String superAdminPassword = password;
        String hostName;

        // try to get the current machine IP address to send with authentication
        // request as parameter
        try {
            hostName = InetAddress.getLocalHost().getHostAddress();

        } catch (UnknownHostException e) {
            WorkbenchElementsValidator.log.warn("Can not retrieve machine IP address. Setting it to 127.0.0.1");
            hostName = "127.0.0.1";
        }

        while (((System.currentTimeMillis() - startTime) < TIMEOUT) && !loginSuccess) {
            WorkbenchElementsValidator.log.info("Waiting for user login...");
            try {
                loginSuccess = checkAuthenticationAdminService(
                        createPayLoad(superAdminName, superAdminPassword, hostName), backendUrl);
                if (!loginSuccess) {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                WorkbenchElementsValidator.log.error("Login failed after server startup", e);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                    // ignored because of login attempts which could happen
                    // before proper carbon
                    // server startup
                }
            }
        }

        if (!loginSuccess) {
            throw new AutomationFrameworkException("Login failed for user " + superAdminName
                    + " while verifying server startup"
                    + ". Please make sure that server is up and running or user is a valid user");
        }
    }

    public static OMElement createPayLoad(String userNameOfAdmin, String passwordOfAdmin, String hostName) {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://authentication.services.core.carbon.wso2.org", "aut");
        OMElement loginRoot = fac.createOMElement("login", omNs);
        OMElement username = fac.createOMElement("username", omNs);
        OMElement password = fac.createOMElement("password", omNs);
        OMElement remoteAddress = fac.createOMElement("remoteAddress", omNs);

        username.setText(userNameOfAdmin);
        password.setText(passwordOfAdmin);
        remoteAddress.setText(hostName);

        loginRoot.addChild(username);
        loginRoot.addChild(password);
        loginRoot.addChild(remoteAddress);

        return loginRoot;
    }

    public static boolean checkAuthenticationAdminService(OMElement payload, String endpointURL)
            throws AutomationFrameworkException {

        try {
            ServiceClient serviceClient = new ServiceClient();
            Options opts = new Options();
            opts.setTo(new EndpointReference(endpointURL + FrameworkConstants.AUTHENTICATE_ADMIN_SERVICE_NAME));
            WorkbenchElementsValidator.log.info(endpointURL);
            opts.setAction("urn:login");
            serviceClient.setOptions(opts);
            OMElement result = serviceClient.sendReceive(payload);

            if (result.toString().contains("<ns:return>true</ns:return>")) {
                WorkbenchElementsValidator.log.info("Login was successful..");
                String sessionCookie = (String) serviceClient.getServiceContext().getProperty(
                        HTTPConstants.COOKIE_STRING);
                WorkbenchElementsValidator.log.info(sessionCookie);
                return true;
            }

        } catch (AxisFault e) {
            WorkbenchElementsValidator.log.error("Unable to login as user..");
            throw new AutomationFrameworkException("Unable to login as user..", e);
        }
        return false;
    }

}
