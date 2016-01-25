package org.wso2.developerstudio.eclipse.swtfunctionalframework.util;

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertContains;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.CommonConstants;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.constants.MavenMultiModuleConstants;
import org.wso2.developerstudio.eclipse.swtfunctionalframework.util.Util;

public class MavenMultiModuleUtil {

    public static void createMavenProject(String groupID, String artifactID) {
        Util.checkShellLoading(MavenMultiModuleConstants.PROJECT_CREATION_WIZARD_TITLE);
        Util.bot.sleep(1000);
        SWTBotShell newMavenProject = Util.bot.shell(MavenMultiModuleConstants.PROJECT_CREATION_WIZARD_TITLE);
        Util.setLableText(newMavenProject, MavenMultiModuleConstants.GROUP_ID, groupID);
        Util.setLableText(newMavenProject, MavenMultiModuleConstants.ARTIFACT_ID, artifactID);
        newMavenProject.bot().button(CommonConstants.FINISH).click();
        Util.bot.sleep(3000);
        validatepom(groupID, artifactID);
    }

    public static void validatepom(String groupID, String artifactID) {

        SWTBotTreeItem main;

        String expected;
        String actual;
        StringBuffer expectedBuffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

        main = Util.bot.tree().getTreeItem(artifactID).expand();
        main.getNode("pom.xml").doubleClick();
        Util.bot.sleep(3000);
        CommonUtil.activateCtab("pom.xml");
        actual = Util.bot.editorByTitle(artifactID + "/pom.xml").bot().styledText().getText();
        expectedBuffer
                .append("<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\" xmlns=\"http://maven.apache.org/POM/4.0.0\"\n");
        expectedBuffer.append("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
        expectedBuffer.append("  <modelVersion>4.0.0</modelVersion>\n");
        expectedBuffer.append("  <groupId>" + groupID + "</groupId>\n");
        expectedBuffer.append("  <artifactId>" + artifactID + "</artifactId>\n");
        expectedBuffer.append("  <version>1.0.0</version>\n");
        expectedBuffer.append("  <packaging>pom</packaging>\n");
        expectedBuffer.append("  <name>MavenParentProject</name>\n");
        expectedBuffer.append("  <description>MavenParentProject</description>\n");
        expectedBuffer.append("  <build>\n");
        expectedBuffer.append("    <plugins>\n");
        expectedBuffer.append("      <plugin>\n");
        expectedBuffer.append("        <artifactId>maven-eclipse-plugin</artifactId>\n");
        expectedBuffer.append("        <version>2.9</version>\n");
        expectedBuffer.append("        <configuration>\n");
        expectedBuffer.append("          <buildcommands />\n");
        expectedBuffer.append("          <projectnatures>\n");
        expectedBuffer
                .append("            <projectnature>org.wso2.developerstudio.eclipse.mavenmultimodule.project.nature</projectnature>\n");
        expectedBuffer.append("          </projectnatures>\n");
        expectedBuffer.append("        </configuration>\n");
        expectedBuffer.append("      </plugin>\n");
        expectedBuffer.append("    </plugins>\n");
        expectedBuffer.append("  </build>\n");
        expectedBuffer.append("</project>\n");
        expected = expectedBuffer.toString();
        assertContains(expected, actual);

    }

}
