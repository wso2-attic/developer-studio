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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.ArrayUtils;
import org.wso2.carbon.utils.FileManipulator;
import org.wso2.developerstudio.eclipse.test.automation.framework.element.validator.WorkbenchElementsValidator;

public class CarbonServerManager {

    private Process process;
    private String carbonHome;
    private ServerLogReader inputStreamHandler;
    private ServerLogReader errorStreamHandler;
    private int portOffset = 0;
    private static final String SERVER_SHUTDOWN_MESSAGE = "Halting JVM";
    private static final String SERVER_STARTUP_MESSAGE = "Mgt Console URL";
    private static final long DEFAULT_START_STOP_WAIT_MS = 1000 * 60 * 5;
    private static final String CMD_ARG = "cmdArg";

    private static int defaultHttpPort = Integer.parseInt(FrameworkConstants.SERVER_DEFAULT_HTTP_PORT);
    private static int defaultHttpsPort = Integer.parseInt(FrameworkConstants.SERVER_DEFAULT_HTTPS_PORT);

    public synchronized String setUpCarbonHome(String carbonServerZipFile) throws IOException,
            AutomationFrameworkException {
        if (process != null) { // An instance of the server is running
            return carbonHome;
        }
        int indexOfZip = carbonServerZipFile.lastIndexOf(".zip");
        if (indexOfZip == -1) {
            throw new IllegalArgumentException(carbonServerZipFile + " is not a zip file");
        }
        String fileSeparator = File.separator.equals("\\") ? "\\" : "/";
        if (fileSeparator.equals("\\")) {
            carbonServerZipFile = carbonServerZipFile.replace("/", "\\");
        }
        String extractedCarbonDir = carbonServerZipFile.substring(carbonServerZipFile.lastIndexOf(fileSeparator) + 1,
                indexOfZip);
        FileManipulator.deleteDir(extractedCarbonDir);
        String extractDir = "carbontmp" + System.currentTimeMillis();
        String baseDir = System.getProperty(FrameworkConstants.SYSTEM_PROPERTY_BASEDIR_LOCATION, ".") + File.separator
                + "target";
        WorkbenchElementsValidator.log.info("Extracting carbon zip file.. ");

        // Changed here
        extractFile(carbonServerZipFile, baseDir + File.separator + extractDir);
        carbonHome = new File(baseDir).getAbsolutePath() + File.separator + extractDir + File.separator
                + extractedCarbonDir;

        return carbonHome;
    }

    public void extractFile(String sourceFilePath, String extractedDir) throws IOException {
        FileOutputStream fileoutputstream = null;

        String fileDestination = extractedDir + File.separator;
        byte[] buf = new byte[1024];
        ZipInputStream zipinputstream = null;
        ZipEntry zipentry;
        try {
            zipinputstream = new ZipInputStream(new FileInputStream(sourceFilePath));

            zipentry = zipinputstream.getNextEntry();

            while (zipentry != null) {
                // for each entry to be extracted
                String entryName = fileDestination + zipentry.getName();
                entryName = entryName.replace('/', File.separatorChar);
                entryName = entryName.replace('\\', File.separatorChar);
                int n;

                File newFile = new File(entryName);
                boolean fileCreated = false;
                if (zipentry.isDirectory()) {
                    if (!newFile.exists()) {
                        fileCreated = newFile.mkdirs();
                    }
                    zipentry = zipinputstream.getNextEntry();
                    continue;
                } else {
                    File resourceFile = new File(entryName.substring(0, entryName.lastIndexOf(File.separator)));
                    if (!resourceFile.exists()) {
                        if (!resourceFile.mkdirs()) {
                            break;
                        }
                    }
                }

                fileoutputstream = new FileOutputStream(entryName);

                while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                    fileoutputstream.write(buf, 0, n);
                }

                fileoutputstream.close();
                zipinputstream.closeEntry();
                zipentry = zipinputstream.getNextEntry();

            }
            zipinputstream.close();
        } catch (IOException e) {
            WorkbenchElementsValidator.log.error("Error on archive extraction ", e);
            throw new IOException("Error on archive extraction ", e);

        } finally {
            if (fileoutputstream != null) {
                fileoutputstream.close();
            }
            if (zipinputstream != null) {
                zipinputstream.close();
            }
        }
    }

    public synchronized void startServerUsingCarbonHome(String carbonHome, Map<String, String> commandMap)
            throws Exception {
        if (process != null) { // An instance of the server is running
            return;
        }
        portOffset = checkPortAvailability(commandMap);
        process = null;
        try {
            if (!commandMap.isEmpty()) {
                if (getPortOffsetFromCommandMap(commandMap) == 0) {
                    System.setProperty(ExtensionConstants.CARBON_HOME, carbonHome);
                }
            }
            File commandDir = new File(carbonHome);

            WorkbenchElementsValidator.log.info("Starting carbon server............. ");
            String scriptName = TestFrameworkUtils.getStartupScriptFileName(carbonHome);
            String[] parameters = expandServerStartupCommandList(commandMap);
            String[] cmdArray;

            System.out.println("getting the os");
            if (System.getProperty(FrameworkConstants.SYSTEM_PROPERTY_OS_NAME).toLowerCase().contains("windows")) {
                commandDir = new File(carbonHome + File.separator + "bin");
                cmdArray = new String[] { "cmd.exe", "/c", scriptName + ".bat" };
                cmdArray = mergePropertiesToCommandArray(parameters, cmdArray);
                process = Runtime.getRuntime().exec(cmdArray, null, commandDir);

            } else {
                cmdArray = new String[] { "sh", "bin/" + scriptName + ".sh" };
                cmdArray = mergePropertiesToCommandArray(parameters, cmdArray);
                process = Runtime.getRuntime().exec(cmdArray, null, commandDir);
            }
            errorStreamHandler = new ServerLogReader("errorStream", process.getErrorStream());
            inputStreamHandler = new ServerLogReader("inputStream", process.getInputStream());
            // start the stream readers
            inputStreamHandler.start();
            errorStreamHandler.start();

            // register shutdown hook

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        serverShutdown(portOffset);
                    } catch (Exception e) {
                        WorkbenchElementsValidator.log.error("Error while server shutdown ..", e);
                    }
                }
            });

            // wait until Mgt console url printed.
            long time = System.currentTimeMillis() + 60 * 1000;
            while (!inputStreamHandler.getOutput().contains(SERVER_STARTUP_MESSAGE)
                    && System.currentTimeMillis() < time) {
                // wait until server startup is completed
            }

            int httpsPort = defaultHttpsPort + portOffset;
            // considering the port offset
            String backendURL = FrameworkConstants.DEFAULT_BACKEND_URL;
            backendURL.replaceAll("(:\\d+)", ":" + httpsPort);
            ClientConnectionUtil.waitForLogin(backendURL, "admin", "admin");
            WorkbenchElementsValidator.log.info("Server started successfully.");

        } catch (IOException e) {
            throw new IllegalStateException("Unable to start server", e);
        }
    }

    private String[] mergePropertiesToCommandArray(String[] parameters, String[] cmdArray) {
        if (parameters != null) {
            cmdArray = mergerArrays(cmdArray, parameters);
        }
        return cmdArray;
    }

    private String[] mergerArrays(String[] array1, String[] array2) {
        return ArrayUtils.addAll(array1, array2);
    }

    private String[] expandServerStartupCommandList(Map<String, String> commandMap) {
        if (commandMap == null || commandMap.size() == 0) {
            return null;
        }
        String[] cmdParaArray = null;
        String cmdArg = null;
        if (commandMap.containsKey(CMD_ARG)) {
            cmdArg = commandMap.get(CMD_ARG);
            cmdParaArray = cmdArg.trim().split("\\s+");
            commandMap.remove(CMD_ARG);
        }
        String[] parameterArray = new String[commandMap.size()];
        int arrayIndex = 0;
        Set<Map.Entry<String, String>> entries = commandMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String parameter;
            String key = entry.getKey();
            String value = entry.getValue();
            if (value == null || value.isEmpty()) {
                parameter = key;
            } else {
                parameter = key + "=" + value;
            }
            parameterArray[arrayIndex++] = parameter;
        }
        // setting cmdArg again
        if (cmdArg != null) {
            commandMap.put(CMD_ARG, cmdArg);
        }
        if (cmdParaArray == null || cmdParaArray.length == 0) {
            return parameterArray;
        } else {
            return ArrayUtils.addAll(parameterArray, cmdParaArray);
        }
    }

    private int getPortOffsetFromCommandMap(Map<String, String> commandMap) {
        if (commandMap.containsKey(FrameworkConstants.SERVER_STARTUP_PORT_OFFSET_COMMAND)) {
            return Integer.parseInt(commandMap.get(FrameworkConstants.SERVER_STARTUP_PORT_OFFSET_COMMAND));
        } else {
            return 0;
        }
    }

    public synchronized void serverShutdown(int portOffset) throws AutomationFrameworkException {
        if (process != null) {
            System.out.println(portOffset);
            WorkbenchElementsValidator.log.info("Shutting down server..");
            if (ClientConnectionUtil.isPortOpen(Integer.parseInt(ExtensionConstants.SERVER_DEFAULT_HTTPS_PORT)
                    + portOffset)) {

                int httpsPort = defaultHttpsPort + portOffset;
                String backendURL = FrameworkConstants.DEFAULT_BACKEND_URL;
                backendURL.replaceAll("(:\\d+)", ":" + httpsPort);
                inputStreamHandler = new ServerLogReader("inputStream", process.getInputStream());

                try {
                    ClientConnectionUtil.sendForcefulShutDownRequest(backendURL, "admin", "admin");
                } catch (Exception e) {
                    try {
                        throw new Exception("Get context failed", e);
                    } catch (Exception e1) {
                        WorkbenchElementsValidator.log.error(e1);
                    }
                }

                long time = System.currentTimeMillis() + DEFAULT_START_STOP_WAIT_MS;
                while (!inputStreamHandler.getOutput().contains(SERVER_SHUTDOWN_MESSAGE)
                        && System.currentTimeMillis() < time) {
                    // wait until server shutdown is completed
                }
                WorkbenchElementsValidator.log.info("Server stopped successfully...");
            }
            inputStreamHandler.stop();
            errorStreamHandler.stop();
            process.destroy();
            process = null;
            if (portOffset == 0) {
                System.clearProperty(ExtensionConstants.CARBON_HOME);
            }
        }

    }

    private int checkPortAvailability(Map<String, String> commandMap) throws AutomationFrameworkException {
        final int portOffset = getPortOffsetFromCommandMap(commandMap);

        // check whether http port is already occupied
        if (ClientConnectionUtil.isPortOpen(defaultHttpPort + portOffset)) {
            throw new AutomationFrameworkException("Unable to start carbon server on port "
                    + (defaultHttpPort + portOffset) + " : Port already in use");
        }
        // check whether https port is already occupied
        if (ClientConnectionUtil.isPortOpen(defaultHttpsPort + portOffset)) {
            throw new AutomationFrameworkException("Unable to start carbon server on port "
                    + (defaultHttpsPort + portOffset) + " : Port already in use");
        }
        return portOffset;
    }

}
