package org.wso2.developerstudio.eclipse.swttests.testcase;

import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.AbstractClass;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;

@RunWith(SWTBotJunit4ClassRunner.class)
public class TestWso2Plugin extends Setup {

	@Test
	public void test() {
		AbstractClass.openProjectFromMenu("wso2");
		bot.sleep(3000);
		AbstractClass.createWso2Plugin("FirstPlugin");
	}

}
