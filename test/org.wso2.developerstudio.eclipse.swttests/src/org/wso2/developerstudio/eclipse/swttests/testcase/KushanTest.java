package org.wso2.developerstudio.eclipse.swttests.testcase;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;

@RunWith(SWTBotJunit4ClassRunner.class)
public class KushanTest extends Setup{

	//static SWTWorkbenchBot bot;

	/*@BeforeClass
	public static void beforeClass() throws Exception {

		bot = new SWTWorkbenchBot();
	}*/

	@Test
	public void test() {
		AbstractClass.openProjectFromMenu("wso2");
                bot.sleep(3000);
	}

}
