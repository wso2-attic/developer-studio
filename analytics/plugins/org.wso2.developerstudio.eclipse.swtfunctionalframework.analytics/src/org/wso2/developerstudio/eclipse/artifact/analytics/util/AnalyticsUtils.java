package org.wso2.developerstudio.eclipse.artifact.analytics.util;

import static org.junit.Assert.fail;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.wso2.developerstudio.eclipse.artifact.analytics.util.constants.AnalyticsConstants;
import org.wso2.developerstudio.eclipse.test.automation.framework.element.validator.WorkbenchElementsValidator;
import org.wso2.developerstudio.eclipse.test.automation.utils.constants.CommonConstants;

public class AnalyticsUtils {

	private static SWTBotShell projectCreationWizard;

	public static void createNewAnalyticsProject(String projectName) {
		try {
			WorkbenchElementsValidator
					.checkShellLoading(AnalyticsConstants.NEW_ANALYTICS_PROJECT);
			projectCreationWizard = WorkbenchElementsValidator.bot
					.shell(AnalyticsConstants.NEW_ANALYTICS_PROJECT);
			WorkbenchElementsValidator.checkButton(CommonConstants.NEXT,
					projectCreationWizard);
			projectCreationWizard.bot().button(CommonConstants.NEXT).click();
			WorkbenchElementsValidator.setLableText(projectCreationWizard,
					AnalyticsConstants.ANALYTICS_PROJECT_NAME, projectName);
			WorkbenchElementsValidator.checkButton(CommonConstants.FINISH,
					projectCreationWizard);
			projectCreationWizard.bot().button(CommonConstants.FINISH).click();
		} catch (Exception e) {
			WorkbenchElementsValidator.log.error(
					"Create New Project unsucsessful", e);
			fail();
		}
	}
	
	public static void createAtifact(String artifactType, String artifactName){
		try {
			WorkbenchElementsValidator
					.checkShellLoading("New "+ artifactType + " Artifact");
			projectCreationWizard = WorkbenchElementsValidator.bot
					.shell("New "+ artifactType + " Artifact");
			WorkbenchElementsValidator.checkButton(CommonConstants.NEXT,
					projectCreationWizard);
			projectCreationWizard.bot().button(CommonConstants.NEXT).click();
			projectCreationWizard.bot().text(0).setText(artifactName);
			WorkbenchElementsValidator.checkButton(CommonConstants.FINISH,
					projectCreationWizard);
			projectCreationWizard.bot().button(CommonConstants.FINISH).click();
		} catch (Exception e) {
			WorkbenchElementsValidator.log.error(
					"Create New Project unsucsessful", e);
			fail();
		}
	}

}
