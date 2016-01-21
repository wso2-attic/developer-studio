package org.wso2.developerstudio.eclipse.swtfunctionalframework.art.carbon.util;

import static org.junit.Assert.fail;

import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.art.carbon.util.constants.CarbonUICons;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonCons;

public class CarbonUtils extends Util {

	public static void createCarbonUI(String projectName) {

		checkShellLoading(CarbonUICons.PROJECT_WIZARD_WINDOW_TITLE);
		SWTBotShell newCarbonUIBundle = bot
				.shell(CarbonUICons.PROJECT_WIZARD_WINDOW_TITLE);
		checkButton(CommonCons.NEXT, newCarbonUIBundle);
		newCarbonUIBundle.bot().button(CommonCons.NEXT).click();
		try {
			newCarbonUIBundle.bot().textWithLabel(CarbonUICons.PROJECT_NAME)
					.setText(projectName);
		} catch (WidgetNotFoundException e) {
			log.error("Problem with the Lable");
			fail();
		}
		checkButton(CommonCons.NEXT, newCarbonUIBundle);
		newCarbonUIBundle.bot().button(CommonCons.NEXT).click();
		checkButton(CommonCons.FINISH, newCarbonUIBundle);
		newCarbonUIBundle.bot().button(CommonCons.FINISH).click();
		try {
			AbstractClass.changePerspective();
		} catch (AssertionError e) {

		}
		bot.waitUntil(Conditions.shellCloses(newCarbonUIBundle));
	}

}
