package org.wso2.developerstudio.eclipse.capp.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.junit.Order;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.junit.OrderedRunner;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.CommonUtil;
//import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.MavenMultiModuleUtil;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Setup;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CompositeApllicationConstants;
//import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.MavenMultiModuleCons;


@RunWith(OrderedRunner.class)
public class TestMavenMultiModule extends Setup{
	
	@Test
	@Order(order = 1)
	public void open() throws Exception {
		//AbstractClass.openProjectFromMenu(MavenMultiModuleCons.MENU_NAME);
		//MavenMultiModuleUtil.createMavenProject("hello", "myfirst");
	}
	
	@Test
	@Order(order = 2)
	public void delete() throws Exception {
		//AbstractClass.deleteWithContent("myfirst");
	}

}
