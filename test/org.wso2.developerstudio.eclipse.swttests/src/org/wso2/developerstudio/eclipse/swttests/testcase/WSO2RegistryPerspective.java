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

import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;

/* Testing registry Perspective both server is online and offline
 * Open registry perspective
 * Add a registry
 * Delete the registry
 */

@RunWith(SWTBotJunit4ClassRunner.class)
public class WSO2RegistryPerspective extends AbstractClass {

    @Test
    public void testCase() throws Exception {

        openPerspective("WSO2 Registry");
        bot.toolbarButtonWithTooltip("Add Registry...").click();

        SWTBotShell addRegistryShell = bot.shell("Add Registry");
        addRegistryShell.bot().button("OK").click();

        bot.tree().getTreeItem("admin@https://localhost:9443/").select();
        bot.tree().getTreeItem("admin@https://localhost:9443/").contextMenu("Delete..").click();
        bot.button("Yes").click();

    }

}
