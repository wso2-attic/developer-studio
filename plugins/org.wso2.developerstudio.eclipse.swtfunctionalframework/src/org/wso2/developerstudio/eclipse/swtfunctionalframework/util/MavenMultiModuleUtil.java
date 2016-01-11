package org.wso2.developerstudio.eclipse.swtfunctionalframework.util;

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertContains;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonCons;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.MavenMultiModuleCons;

public class MavenMultiModuleUtil extends Util{
    
/*    public static void createMavenProject(String groupID, String artifactID, String  projectName){
        checkShellLoading(MavenMultiModuleCons.PROJECT_CREATION_WIZARD_TITLE);
        bot.sleep(1000);
        SWTBotShell newMavenProject = bot.shell(MavenMultiModuleCons.PROJECT_CREATION_WIZARD_TITLE);
        setLableText(newMavenProject, MavenMultiModuleCons.GROUP_ID, groupID); 
        setLableText(newMavenProject, MavenMultiModuleCons.ARTIFACT_ID, artifactID);
        bot.tableWithLabel("Projects").select(projectName);
    }*/
    
    public static void createMavenProject(String groupID, String artifactID){
        checkShellLoading(MavenMultiModuleCons.PROJECT_CREATION_WIZARD_TITLE);
        bot.sleep(1000);
        SWTBotShell newMavenProject = bot.shell(MavenMultiModuleCons.PROJECT_CREATION_WIZARD_TITLE);
        setLableText(newMavenProject, MavenMultiModuleCons.GROUP_ID, groupID); 
        setLableText(newMavenProject, MavenMultiModuleCons.ARTIFACT_ID, artifactID);
        newMavenProject.bot().button(CommonCons.FINISH).click();
        bot.sleep(3000);
        validatepom(groupID, artifactID);
    }
    
    public static void validatepom(String groupID, String artifactID){
        
        SWTBotTreeItem main;
        
        String expected;
        String actual;
        main = bot.tree().getTreeItem(artifactID).expand();
        main.getNode("pom.xml").doubleClick();
        bot.sleep(3000);
        AbstractClass.activateCtab("pom.xml");
        actual = bot.editorByTitle(artifactID + "/pom.xml").bot().styledText().getText();
        expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        expected = expected + "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\" xmlns=\"http://maven.apache.org/POM/4.0.0\"\n";
        expected = expected + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
        expected = expected + "  <modelVersion>4.0.0</modelVersion>";
        expected = expected + "  <groupId>" + groupID + "</groupId>";
        expected = expected + "  <artifactId>" + artifactID + "</artifactId>";
        expected = expected + "  <version>1.0.0</version>";
        expected = expected + "  <packaging>pom</packaging>";
        expected = expected + "  <name>MavenParentProject</name>";
        expected = expected + "  <description>MavenParentProject</description>";
        expected = expected + "  <build>";
        expected = expected + "    <plugins>";
        expected = expected + "      <plugin>";
        expected = expected + "        <artifactId>maven-eclipse-plugin</artifactId>";
        expected = expected + "        <version>2.9</version>";
        expected = expected + "        <configuration>";
        expected = expected + "          <buildcommands />";
        expected = expected + "          <projectnatures>";
        expected = expected + "            <projectnature>org.wso2.developerstudio.eclipse.mavenmultimodule.project.nature</projectnature>";
        expected = expected + "          </projectnatures>";
        expected = expected + "        </configuration>";
        expected = expected + "      </plugin>";
        expected = expected + "    </plugins>";
        expected = expected + "  </build>";
        expected = expected + "</project>";
        assertContains(expected, actual);
        
    }

}
