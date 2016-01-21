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

package org.wso2.developerstudio.eclipse.swtfunctionalframework.brs.util;

import static org.junit.Assert.fail;

import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.brs.util.constants.BusinessRulesServiceCons;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonCons;

public class BusinessRulesUtil extends Util {

    private static SWTBotEditor serviceEditor;

    public static void serviceEditorText(String option, String value) {
        try {
            serviceEditor = bot.editorByTitle("service.rsl");
            serviceEditor.bot().textWithLabel(option).setText(value);
            serviceEditor.save();
        } catch (WidgetNotFoundException | TimeoutException e) {
            System.out.println("Editor cannot be found");
            fail();
        }

    }

    public static void closeServiceEditor() {
        try {
            serviceEditor = bot.editorByTitle("service.rsl");
            serviceEditor.close();
        } catch (WidgetNotFoundException | TimeoutException e) {
            System.out.println("Editor cannot be found");
            fail();
        }
    }
    
    public static void createBusinessRulesService(String projectName, String serviceName) {

        bot.sleep(2000);
        checkShellLoading(BusinessRulesServiceCons.NEW_BUSINESS_RULES_SERVICE_PROJECT);
        newServiceProject = bot.shell(BusinessRulesServiceCons.NEW_BUSINESS_RULES_SERVICE_PROJECT);
        checkButton(CommonCons.NEXT, newServiceProject);
        newServiceProject.bot().button(CommonCons.NEXT).click();
        try {
            newServiceProject.bot().textWithLabel(BusinessRulesServiceCons.PROJECT_NAME).setText(projectName);
            newServiceProject.bot().textWithLabel(BusinessRulesServiceCons.SERVICE_NAME).setText(serviceName);
        }

        catch (WidgetNotFoundException e) {
            //log.error("Problem with the Lable");
            fail();
        }
        checkButton(CommonCons.NEXT, newServiceProject);
        newServiceProject.bot().button(CommonCons.NEXT).click();
        checkButton(CommonCons.FINISH, newServiceProject);
        newServiceProject.bot().button(CommonCons.FINISH).click();
        bot.waitUntil(Conditions.shellCloses(newServiceProject));
        //log.error("Business Rules Service sucssecfully created");
        try {
            bot.editorByTitle(BusinessRulesServiceCons.SERVICE_RSL).show();
            System.out.println("Create Business Rules Service Sucsessful");

        } catch (WidgetNotFoundException e) {
            //log.error("Editor didnt load");
            fail();
        }
    }

    public static void importBusinessRulesService(String path) {

        checkShellLoading(BusinessRulesServiceCons.NEW_BUSINESS_RULES_SERVICE_PROJECT);
        newServiceProject = bot.shell(BusinessRulesServiceCons.NEW_BUSINESS_RULES_SERVICE_PROJECT);
        newServiceProject.bot().radio(BusinessRulesServiceCons.IMPORT_BUSINESS_RULE_SERVICE).click();
        checkButton(CommonCons.NEXT, newServiceProject);
        newServiceProject.bot().button(CommonCons.NEXT).click();
        // browse dosen't work
        try {
            newServiceProject.bot().textWithLabel(BusinessRulesServiceCons.BUSINESS_RULE_SERVICE_DESCRIPTOR_FILE)
                    .setText(path);
        } catch (WidgetNotFoundException e) {
            //log.error("Problem with the Lable");
            fail();
        }
        checkButton(CommonCons.NEXT, newServiceProject);
        newServiceProject.bot().button(CommonCons.NEXT).click();
        checkButton(CommonCons.FINISH, newServiceProject);
        newServiceProject.bot().button(CommonCons.FINISH).click();
        bot.waitUntil(Conditions.shellCloses(newServiceProject));
    }

}