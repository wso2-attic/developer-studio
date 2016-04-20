package org.wso2.developerstudio.eclipse.swtfunctionalframework.bpel.util;

import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

import static org.junit.Assert.fail;

import org.wso2.developerstudio.eclipse.swtfunctionalframework.bpel.util.constants.BPELProjectConstants;
import org.wso2.developerstudio.eclipse.test.automation.framework.element.validator.WorkbenchElementsValidator;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.CommonConstants;
import org.wso2.developerstudio.eclipse.test.automation.utils.functional.FunctionalUtil;

public class BPELUtil {

	private static SWTBotShell newWorkflow;

	public static void createNewBPELProject(String projectName) {

		try {
			WorkbenchElementsValidator
					.checkShellLoading(BPELProjectConstants.NEW_BPEL_PROJECT);
			newWorkflow = WorkbenchElementsValidator.bot
					.shell(BPELProjectConstants.NEW_BPEL_PROJECT);
			WorkbenchElementsValidator.checkButton(CommonConstants.NEXT,
					newWorkflow);
			newWorkflow.bot().button(CommonConstants.NEXT).click();
			WorkbenchElementsValidator.setLableText(newWorkflow,
					BPELProjectConstants.PROJECT_NAME, projectName);
			newWorkflow.bot().checkBox(CommonConstants.USE_DEFAULT_LOCATION)
					.click();
			newWorkflow.bot().checkBox(CommonConstants.USE_DEFAULT_LOCATION)
					.click();
			WorkbenchElementsValidator.checkButton(CommonConstants.NEXT,
					newWorkflow);
			newWorkflow.bot().button(CommonConstants.NEXT).click();
			newWorkflow.bot()
					.checkBox(CommonConstants.SPECIFY_PARENT_FROM_WORKSPACE)
					.click();
			newWorkflow.bot()
					.checkBox(CommonConstants.SPECIFY_PARENT_FROM_WORKSPACE)
					.click();
			WorkbenchElementsValidator.checkButton(CommonConstants.FINISH,
					newWorkflow);
			newWorkflow.bot().button(CommonConstants.FINISH).click();
			try {
				FunctionalUtil.switchPerspectiveInProjectCreation();
			} catch (AssertionError e) {

			}
			try {
				WorkbenchElementsValidator.bot.editorByTitle(
						projectName + CommonConstants.BPEL).show();
				WorkbenchElementsValidator.log
						.info("Create New BPEL Project Sucsessful");
			} catch (WidgetNotFoundException e) {
				WorkbenchElementsValidator.log.error("Editor didnt load", e);
				fail();
			}
		} catch (Exception e) {
			WorkbenchElementsValidator.log.error(
					"Create New BPEL Project unsucsessful", e);
			fail();
		}

	}

	public void importBPELProject(String path) {

		WorkbenchElementsValidator
				.checkShellLoading(BPELProjectConstants.NEW_BPEL_PROJECT);
		newWorkflow = WorkbenchElementsValidator.bot
				.shell(BPELProjectConstants.NEW_BPEL_PROJECT);
		newWorkflow.bot().radio(BPELProjectConstants.IMPORT_BPEL_WORKFLOW)
				.click();
		WorkbenchElementsValidator.setLableText(newWorkflow,
				BPELProjectConstants.BPEL_ARCHIVE_FILE, path);
		WorkbenchElementsValidator.checkButton(CommonConstants.FINISH,
				newWorkflow);
		newWorkflow.bot().button(CommonConstants.FINISH).click();
		WorkbenchElementsValidator.bot.waitUntil(Conditions
				.shellCloses(newWorkflow));
	}

}
