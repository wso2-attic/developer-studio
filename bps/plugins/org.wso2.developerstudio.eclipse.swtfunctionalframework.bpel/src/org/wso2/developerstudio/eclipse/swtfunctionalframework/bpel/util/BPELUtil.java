package org.wso2.developerstudio.eclipse.swtfunctionalframework.bpel.util;

import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

import static org.junit.Assert.fail;

import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.bpel.Activator;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.bpel.util.constants.BPELProjectCons;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonCons;

public class BPELUtil extends Util{
	
	private static SWTBotShell newWorkflow;
	
    public static void createNewBPELProject(String projectName) {

        try {
            checkShellLoading(BPELProjectCons.NEW_BPEL_PROJECT);
            newWorkflow = bot.shell(BPELProjectCons.NEW_BPEL_PROJECT);
            checkButton(CommonCons.NEXT, newWorkflow);
            newWorkflow.bot().button(CommonCons.NEXT).click();
            setLableText(newWorkflow, BPELProjectCons.PROJECT_NAME, projectName);
            newWorkflow.bot().checkBox(CommonCons.USE_DEFAULT_LOCATION).click();
            newWorkflow.bot().checkBox(CommonCons.USE_DEFAULT_LOCATION).click();
            checkButton(CommonCons.NEXT, newWorkflow);
            newWorkflow.bot().button(CommonCons.NEXT).click();
            newWorkflow.bot().checkBox(CommonCons.SPECIFY_PARENT_FROM_WORKSPACE).click();
            newWorkflow.bot().checkBox(CommonCons.SPECIFY_PARENT_FROM_WORKSPACE).click();
            checkButton(CommonCons.FINISH, newWorkflow);
            newWorkflow.bot().button(CommonCons.FINISH).click();
            try {
                AbstractClass.changePerspective();
            } catch (AssertionError e) {

            }
            try {
                bot.editorByTitle(projectName + CommonCons.BPEL).show();
                log.error("Create New BPEL Project Sucsessful");
            } catch (WidgetNotFoundException e) {
                log.error("Editor didnt load");
                fail();
            }
        } catch (Exception e) {
            log.error("Create New BPEL Project unsucsessful");
            fail();
        }

    }
    
    public void importBPELProject(String path) {

        checkShellLoading(BPELProjectCons.NEW_BPEL_PROJECT);
        newWorkflow = bot.shell(BPELProjectCons.NEW_BPEL_PROJECT);
        newWorkflow.bot().radio(BPELProjectCons.IMPORT_BPEL_WORKFLOW).click();
        setLableText(newWorkflow, BPELProjectCons.BPEL_ARCHIVE_FILE, path);
        checkButton(CommonCons.FINISH, newWorkflow);
        newWorkflow.bot().button(CommonCons.FINISH).click();
        bot.waitUntil(Conditions.shellCloses(newWorkflow));
    }

}
