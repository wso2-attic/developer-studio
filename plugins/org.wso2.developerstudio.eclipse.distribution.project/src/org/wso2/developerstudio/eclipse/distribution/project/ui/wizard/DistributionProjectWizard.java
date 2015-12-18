/*
 * Copyright (c) 2011, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.developerstudio.eclipse.distribution.project.ui.wizard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Repository;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.wso2.developerstudio.eclipse.capp.maven.utils.MavenConstants;
import org.wso2.developerstudio.eclipse.distribution.project.Activator;
import org.wso2.developerstudio.eclipse.distribution.project.model.DependencyData;
import org.wso2.developerstudio.eclipse.distribution.project.model.DistributionProjectModel;
import org.wso2.developerstudio.eclipse.distribution.project.util.ArtifactTypeMapping;
import org.wso2.developerstudio.eclipse.distribution.project.util.DistProjectUtils;
import org.wso2.developerstudio.eclipse.distribution.project.util.DistributionProjectImageUtils;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.maven.util.MavenUtils;
import org.wso2.developerstudio.eclipse.platform.ui.wizard.AbstractWSO2ProjectCreationWizard;
import org.wso2.developerstudio.eclipse.utils.project.ProjectUtils;

public class DistributionProjectWizard extends
		AbstractWSO2ProjectCreationWizard {
	private static final String DISTRIBUTION_PROJECT_NATURE = "org.wso2.developerstudio.eclipse.distribution.project.nature";
	private IProject project;
	private static IDeveloperStudioLog log=Logger.getLog(Activator.PLUGIN_ID);


	public DistributionProjectWizard() {
		setModel(new DistributionProjectModel());
		setDefaultPageImageDescriptor(DistributionProjectImageUtils.getInstance().getImageDescriptor("distribution-project-wizard.png"));
		setWindowTitle("New Composite Application Project");
	}

	public IResource getCreatedResource() {
		return project;
	}

	public boolean performFinish() {
		try {
			log.info("DistributionProjectModel creation");
			DistributionProjectModel projectModel = (DistributionProjectModel) getModel();
			log.info("Creating new project");
			project = createNewProject();
			log.info("Getting the pom");
			File pomfile = project.getFile("pom.xml").getLocation().toFile();
			log.info("pom creation");
			createPOM(pomfile);
			log.info("pom creation sucssesful");
			ProjectUtils
					.addNatureToProject(project, false,
							DISTRIBUTION_PROJECT_NATURE);
			log.info("Nature applying sucssesful");
			MavenUtils
			.updateWithMavenEclipsePlugin(
					pomfile,
					new String[] { },
					new String[] { DISTRIBUTION_PROJECT_NATURE });
			project.refreshLocal(IResource.DEPTH_INFINITE,
					new NullProgressMonitor());
			log.info("update With Maven Eclipse sucssesful");

			MavenProject mavenProject = MavenUtils.getMavenProject(pomfile);
			log.info("get maven project sucssesful");
			mavenProject.getModel().setPackaging("carbon/application");
			log.info("set packaging sucssesful");
			Plugin plugin = MavenUtils.createPluginEntry(mavenProject,
					"org.wso2.maven", "maven-car-plugin", MavenConstants.MAVEN_CAR_VERSION, true);
			log.info("plugin object created sucssesful");
			PluginExecution pluginExecution;
			

			pluginExecution = new PluginExecution();
			log.info("plugin Execution object");
			pluginExecution.addGoal("car");
			log.info("plugin Execution goal car");
			pluginExecution.setPhase("package");
			log.info("plugin Execution package");
			pluginExecution.setId("car");
			log.info("plugin Executionid car");
			plugin.addExecution(pluginExecution);
			log.info("plugin Execution created sucssesful");
			
			Plugin carDeployPlugin = MavenUtils.createPluginEntry(mavenProject, "org.wso2.maven", "maven-car-deploy-plugin", MavenConstants.MAVEN_CAR_DEPLOY_VERSION, true);
			Xpp3Dom carDeployConfElement = MavenUtils.createMainConfigurationNode(carDeployPlugin);
			Xpp3Dom serversElement = MavenUtils.createXpp3Node(carDeployConfElement, "carbonServers");
			Xpp3Dom carbonServer = MavenUtils.createXpp3Node(serversElement, "CarbonServer");
			Xpp3Dom trustStore = MavenUtils.createXpp3Node(carbonServer, "trustStorePath");
			trustStore.setValue("${basedir}/src/main/resources/security/wso2carbon.jks");
			Xpp3Dom trustStorePW = MavenUtils.createXpp3Node(carbonServer, "trustStorePassword");
			trustStorePW.setValue("wso2carbon");
			Xpp3Dom trustStoreType = MavenUtils.createXpp3Node(carbonServer, "trustStoreType");
			trustStoreType.setValue("JKS");
			Xpp3Dom serverUrl = MavenUtils.createXpp3Node(carbonServer, "serverUrl");
			serverUrl.setValue("https://localhost:9443");
			Xpp3Dom serverUserName = MavenUtils.createXpp3Node(carbonServer, "userName");
			serverUserName.setValue("admin");
			Xpp3Dom serverPW = MavenUtils.createXpp3Node(carbonServer, "password");
			serverPW.setValue("admin");
			Xpp3Dom serverOperation = MavenUtils.createXpp3Node(carbonServer, "operation");
			serverOperation.setValue("deploy");

			log.info("1");
			Repository repo = new Repository();
			log.info("2");
			repo.setUrl("http://dist.wso2.org/maven2");
			log.info("3");
			repo.setId("wso2-maven2-repository-1");
			log.info("4");
			Repository repo1 = new Repository();
			log.info("5");
			repo1.setUrl("http://maven.wso2.org/nexus/content/groups/wso2-public/");
			log.info("6");
			repo1.setId("wso2-nexus-repository-1");

			log.info("7");
			mavenProject.getModel().addRepository(repo);
			log.info("8");
			mavenProject.getModel().addPluginRepository(repo);
			log.info("9");
			mavenProject.getModel().addRepository(repo1);
			log.info("10");
			mavenProject.getModel().addPluginRepository(repo1);
			log.info("11");
			List<Dependency> dependencyList = new ArrayList<Dependency>();
			Properties properties = mavenProject.getModel().getProperties();
			for (DependencyData dependencyData : projectModel.getSelectedProjects()) {
				Dependency dependency = dependencyData.getDependency();
				dependencyList.add(dependency);
				properties.put(DistProjectUtils.getArtifactInfoAsString(dependency), dependencyData.getServerRole());
			}
			ArtifactTypeMapping artifactTypeMapping = new ArtifactTypeMapping();
			log.info("12");
			properties.put("artifact.types", artifactTypeMapping.getArtifactTypes());
			log.info("13");
			mavenProject.getModel().setProperties(properties);
            log.info("adding maven depn");
            
			MavenUtils.addMavenDependency(mavenProject, dependencyList);
			log.info("saving maven pro");
			MavenUtils.saveMavenProject(mavenProject, pomfile);
			project.refreshLocal(IResource.DEPTH_INFINITE,
					new NullProgressMonitor());
			openEditor();
		} catch (Exception e) {
			log.error("An error occurred generating a project: ", e);
			return false;
		}
		return true;
	}
	
	public void openEditor(){
		try {
			 IFile pom = project.getFile("pom.xml");
		     IWorkbenchWindow window=PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	         IWorkbenchPage page = window.getActivePage();
	      	 page.openEditor(new FileEditorInput(pom), "org.wso2.developerstudio.eclipse.distribution.project.editor.DistProjectEditor"); 
		} catch (Exception e) { /* ignore */}
	}

}
