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
import java.io.FileNotFoundException;
import org.apache.commons.io.FilenameUtils;

public class TestFrameworkUtils {

    /**
     * Filter start up script name from extracted distribution.
     */
    public static String getStartupScriptFileName(String carbonHome) throws FileNotFoundException {
        File[] allScripts = new File(carbonHome + File.separator + "bin").listFiles();
        String scriptName = null;
        if (allScripts != null) {
            for (File scriptFileName : allScripts) {
                if (scriptFileName.getName().contains(ExtensionConstants.SEVER_STARTUP_SCRIPT_NAME)) {
                    scriptName = scriptFileName.getAbsoluteFile().getName();
                    break;
                } else if (scriptFileName.getName().contains("server")) {
                    scriptName = scriptFileName.getName();
                    break;
                }
            }
        } else {
            throw new FileNotFoundException("Server startup script not found at " + carbonHome + File.separator + "bin");
        }
        return FilenameUtils.removeExtension(scriptName);
    }
}
