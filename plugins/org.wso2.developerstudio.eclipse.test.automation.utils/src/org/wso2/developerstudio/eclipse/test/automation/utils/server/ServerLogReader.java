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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.wso2.developerstudio.eclipse.test.automation.framework.element.validator.WorkbenchElementsValidator;

public class ServerLogReader implements Runnable {
    private String streamType;
    private InputStream inputStream;
    private StringBuilder stringBuilder;
    private static final String STREAM_TYPE_IN = "inputStream";
    private static final String STREAM_TYPE_ERROR = "errorStream";
    private final Object lock = new Object();
    private Thread thread;
    private volatile boolean running = true;

    public ServerLogReader(String name, InputStream is) {
        this.streamType = name;
        this.inputStream = is;
        this.stringBuilder = new StringBuilder();
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        running = false;
    }

    public void run() {
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, Charset.defaultCharset());
            bufferedReader = new BufferedReader(inputStreamReader);
            while (running) {
                if (bufferedReader.ready()) {
                    String s = bufferedReader.readLine();
                    stringBuilder.setLength(0);
                    if (s == null) {
                        break;
                    }
                    if (STREAM_TYPE_IN.equals(streamType)) {
                        stringBuilder.append(s).append("\n");
                        WorkbenchElementsValidator.log.info(s);
                    } else if (STREAM_TYPE_ERROR.equals(streamType)) {
                        stringBuilder.append(s).append("\n");
                        WorkbenchElementsValidator.log.error(s);
                    }
                }
            }
        } catch (Exception ex) {
            WorkbenchElementsValidator.log.error("Problem reading the [" + streamType + "] due to: " + ex.getMessage(),
                    ex);
        } finally {
            if (inputStreamReader != null) {
                try {
                    inputStream.close();
                    inputStreamReader.close();
                } catch (IOException e) {
                    WorkbenchElementsValidator.log.error(
                            "Error occurred while closing the server log stream: " + e.getMessage(), e);
                }
            }

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    WorkbenchElementsValidator.log.error(
                            "Error occurred while closing the server log stream: " + e.getMessage(), e);
                }
            }
        }
    }

    public String getOutput() {
        synchronized (lock) {
            return stringBuilder.toString();
        }
    }
}
