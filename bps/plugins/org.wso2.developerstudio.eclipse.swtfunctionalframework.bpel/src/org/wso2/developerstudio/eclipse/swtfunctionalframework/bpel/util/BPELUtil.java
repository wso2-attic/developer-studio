package org.wso2.developerstudio.eclipse.swtfunctionalframework.bpel.util;

import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import static org.junit.Assert.fail;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.bpel.util.constants.BPELProjectConstants;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.CommonUtil;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonConstants;

public class BPELUtil{
	
	private static SWTBotShell newWorkflow;
	
    public static void createNewBPELProject(String projectName) {

        try {
            Util.checkShellLoading(BPELProjectConstants.NEW_BPEL_PROJECT);
            newWorkflow = Util.bot.shell(BPELProjectConstants.NEW_BPEL_PROJECT);
            Util.checkButton(CommonConstants.NEXT, newWorkflow);
            newWorkflow.bot().button(CommonConstants.NEXT).click();
            Util.setLableText(newWorkflow, BPELProjectConstants.PROJECT_NAME, projectName);
            newWorkflow.bot().checkBox(CommonConstants.USE_DEFAULT_LOCATION).click();
            newWorkflow.bot().checkBox(CommonConstants.USE_DEFAULT_LOCATION).click();
            Util.checkButton(CommonConstants.NEXT, newWorkflow);
            newWorkflow.bot().button(CommonConstants.NEXT).click();
            newWorkflow.bot().checkBox(CommonConstants.SPECIFY_PARENT_FROM_WORKSPACE).click();
            newWorkflow.bot().checkBox(CommonConstants.SPECIFY_PARENT_FROM_WORKSPACE).click();
            Util.checkButton(CommonConstants.FINISH, newWorkflow);
            newWorkflow.bot().button(CommonConstants.FINISH).click();
            try {
                CommonUtil.switchPerspectiveInCreatingProjects();
            } catch (AssertionError e) {

            }
            try {
                Util.bot.editorByTitle(projectName + CommonConstants.BPEL).show();
                Util.log.info("Create New BPEL Project Sucsessful");
            } catch (WidgetNotFoundException e) {
                Util.log.error("Editor didnt load", e);
                fail();
            }
        } catch (Exception e) {
            Util.log.error("Create New BPEL Project unsucsessful", e);
            fail();
        }

    }
    
    public void importBPELProject(String path) {

        Util.checkShellLoading(BPELProjectConstants.NEW_BPEL_PROJECT);
        newWorkflow = Util.bot.shell(BPELProjectConstants.NEW_BPEL_PROJECT);
        newWorkflow.bot().radio(BPELProjectConstants.IMPORT_BPEL_WORKFLOW).click();
        Util.setLableText(newWorkflow, BPELProjectConstants.BPEL_ARCHIVE_FILE, path);
        Util.checkButton(CommonConstants.FINISH, newWorkflow);
        newWorkflow.bot().button(CommonConstants.FINISH).click();
        Util.bot.waitUntil(Conditions.shellCloses(newWorkflow));
    }

}
