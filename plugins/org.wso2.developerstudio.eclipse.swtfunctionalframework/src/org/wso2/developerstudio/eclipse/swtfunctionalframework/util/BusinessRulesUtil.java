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

package org.wso2.developerstudio.eclipse.swtfunctionalframework.util;

import static org.junit.Assert.fail;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;

public class BusinessRulesUtil extends Util {

    private static SWTBotEditor serviceEditor;

    public static void serviceEditorText(String option, String value) {
        try {
            serviceEditor = bot.editorByTitle("service.rsl");
            serviceEditor.bot().textWithLabel(option).setText(value);
            serviceEditor.save();
        } catch (WidgetNotFoundException /* | TimeoutException */e) {
            System.out.println("Editor cannot be found");
            fail();
        }

    }

    public static void closeServiceEditor() {
        try {
            serviceEditor = bot.editorByTitle("service.rsl");
            serviceEditor.close();
        } catch (WidgetNotFoundException /* | TimeoutException */e) {
            System.out.println("Editor cannot be found");
            fail();
        }
    }

}