/*
 * Copyright (c) 2012 -2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.developerstudio.eclipse.artifact.bpel.ui.wizard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.wso2.developerstudio.eclipse.artifact.bpel.Activator;
import org.wso2.developerstudio.eclipse.artifact.bpel.model.BpelModel;
import org.wso2.developerstudio.eclipse.artifact.bpel.utils.BPELArtifactConstants;
import org.wso2.developerstudio.eclipse.artifact.bpel.utils.BPELImageUtils;
import org.wso2.developerstudio.eclipse.artifact.bpel.utils.BPELTemplateUtils;
import org.wso2.developerstudio.eclipse.artifact.bpel.utils.Messages;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.maven.util.MavenUtils;
import org.wso2.developerstudio.eclipse.platform.core.utils.XMLUtil;
import org.wso2.developerstudio.eclipse.platform.ui.wizard.AbstractWSO2ProjectCreationWizard;
import org.wso2.developerstudio.eclipse.utils.archive.ArchiveManipulator;
import org.wso2.developerstudio.eclipse.utils.data.ITemporaryFileTag;
import org.wso2.developerstudio.eclipse.utils.file.FileUtils;
import org.wso2.developerstudio.eclipse.utils.project.ProjectUtils;

public class BPELProjectCreationWizard extends AbstractWSO2ProjectCreationWizard {
	private static IDeveloperStudioLog log = Logger.getLog(Activator.PLUGIN_ID);
	
	private static final String LINE_SEPERATOR = "line.separator"; //$NON-NLS-1$
	private static final String BPEL_CONTENT = "bpelContent"; //$NON-NLS-1$
	
	private BpelModel bpelModel;
	private IProject project;
	private String processName;
	private String namespace;
	private IConfigurationElement configElement;

	public BPELProjectCreationWizard() {
		this.bpelModel = new BpelModel();
		setModel(this.bpelModel);
		setWindowTitle(Messages.PROJECT_WIZARD_WINDOW_TITLE);
		setDefaultPageImageDescriptor(BPELImageUtils.getInstance().getImageDescriptor("bpel-wizard.png")); //$NON-NLS-1$
	}

	public boolean performFinish() {
		try {
			project = createNewProject();
			if (getModel().getSelectedOption().equals("import.bpelproject")) { //$NON-NLS-1$
				extractImportFile(project);
				extractBPELSettingsTemplate(project);
				replaceAndUpdateSettingsFiles();
			} else if (getModel().getSelectedOption().equals("new.bpelproject")) { //$NON-NLS-1$
				if (!project.getFile("deploy.xml").exists()) { //$NON-NLS-1$
					addBPELTemplate(project);
				}
				replaceAndUpdateNewBpelProject();
			} else {

			}
			addCommonConfigs();
			getModel().addToWorkingSet(project);
			project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			List<File> bpelFiles = new ArrayList<File>();

			File[] allExactMatchingFiles = FileUtils.getAllExactMatchingFiles(project.getLocation().toOSString(), null,
					"bpel", bpelFiles); //$NON-NLS-1$
			String relativePath = FileUtils.getRelativePath(project.getLocation().toFile(), allExactMatchingFiles[0]);
			try {
				refreshDistProjects();
				IFile resourceFile = project.getFile(relativePath);
				IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), resourceFile);
			} catch (Exception e) {
				log.error("Cannot open file in editor", e); //$NON-NLS-1$
			}
			BasicNewProjectResourceWizard.updatePerspective(configElement);

		} catch (CoreException e) {
			log.error("CoreException has occurred", e); //$NON-NLS-1$
		} catch (Exception e) {
			log.error("An unexpected error has occurred", e); //$NON-NLS-1$
		}
		return true;
	}

	@Override
	public void setInitializationData(IConfigurationElement configElement, String arg1, Object arg2)
			throws CoreException {
		super.setInitializationData(configElement, arg1, arg2);
		this.configElement = configElement;
	}

	private void addCommonConfigs() throws Exception, CoreException {
		File pomfile = project.getFile("pom.xml").getLocation().toFile(); //$NON-NLS-1$
		getModel().getMavenInfo().setPackageName("bpel/workflow"); //$NON-NLS-1$
		createPOM(pomfile);
		MavenProject mavenProject = MavenUtils.getMavenProject(pomfile);
		MavenUtils.addMavenBpelPlugin(mavenProject);
		MavenUtils.saveMavenProject(mavenProject, pomfile);
		ProjectUtils.addNatureToProject(project, false, "org.eclipse.jem.workbench.JavaEMFNature", //$NON-NLS-1$
				"org.eclipse.wst.common.modulecore.ModuleCoreNature", //$NON-NLS-1$
				"org.eclipse.wst.common.project.facet.core.nature", //$NON-NLS-1$
				"org.wso2.developerstudio.eclipse.bpel.project.nature"); //$NON-NLS-1$

		// add buildspacifications to .project file
		ProjectUtils.addBuildSpecificationsToProject(project, "org.eclipse.wst.common.project.facet.core.builder", //$NON-NLS-1$
				"org.eclipse.wst.validation.validationbuilder", "org.eclipse.bpel.validator.builder"); //$NON-NLS-1$ //$NON-NLS-2$
		MavenUtils.updateWithMavenEclipsePlugin(pomfile, new String[] {
				"org.eclipse.wst.common.project.facet.core.builder", "org.eclipse.wst.validation.validationbuilder", //$NON-NLS-1$ //$NON-NLS-2$
				"org.eclipse.bpel.validator.builder" }, new String[] { "org.eclipse.jem.workbench.JavaEMFNature", //$NON-NLS-1$ //$NON-NLS-2$
				"org.eclipse.wst.common.modulecore.ModuleCoreNature", //$NON-NLS-1$
				"org.eclipse.wst.common.project.facet.core.nature", //$NON-NLS-1$
				"org.wso2.developerstudio.eclipse.bpel.project.nature" }); //$NON-NLS-1$
	}

	public void replaceAndUpdateSettingsFiles() throws IOException {

		File settingsFile = project.getFile("/.settings/org.eclipse.wst.common.component").getLocation().toFile(); //$NON-NLS-1$
		File newSettingsFile = project.getFile("/.settings/org.eclipse.wst.common.component").getLocation().toFile(); //$NON-NLS-1$
		String settingFileAsString = FileUtils.getContentAsString(settingsFile);
		String replacedSettingContent = replaceFileContent(settingFileAsString, "sample", project.getName()); //$NON-NLS-1$
		FileUtils.createFile(newSettingsFile, replacedSettingContent);

	}

	public void replaceAndUpdateNewBpelProject() throws IOException {

		processName = ((BpelModel) getModel()).getProcessName().trim();
		namespace = ((BpelModel) getModel()).getProcessNS();

		File processFile = project.getFolder(BPEL_CONTENT).getFile("HelloWorldBPELProcess.bpel").getLocation() //$NON-NLS-1$
				.toFile();
		File wsdlfile = project.getFolder(BPEL_CONTENT).getFile("HelloWorldBPELProcessArtifacts.wsdl").getLocation() //$NON-NLS-1$
				.toFile();
		File deployfile = project.getFolder(BPEL_CONTENT).getFile("deploy.xml").getLocation().toFile(); //$NON-NLS-1$
		File settingsFile = project.getFolder(BPEL_CONTENT).getFile("/.settings/org.eclipse.wst.common.component") //$NON-NLS-1$
				.getLocation().toFile();
		File seriveXML = project.getFolder(BPEL_CONTENT).getFile("service.xml").getLocation().toFile(); //$NON-NLS-1$

		File newProcessFile = project.getFolder(BPEL_CONTENT).getFile(processName + ".bpel").getLocation().toFile(); //$NON-NLS-1$
		File newWSDLFile = project.getFolder(BPEL_CONTENT).getFile(processName + "Artifacts.wsdl").getLocation() //$NON-NLS-1$
				.toFile();
		File newSettingsFile = project.getFile("/.settings/org.eclipse.wst.common.component").getLocation().toFile(); //$NON-NLS-1$

		String processFileAsString = FileUtils.getContentAsString(processFile);
		String wsdlFileAsString = FileUtils.getContentAsString(wsdlfile);
		String deployFileAsString = FileUtils.getContentAsString(deployfile);
		String settingFileAsString = FileUtils.getContentAsString(settingsFile);
		String serviceXMLAsString = FileUtils.getContentAsString(seriveXML);

		String replacedProcessContent = replaceFileContent(processFileAsString, "HelloWorldBPELProcess", processName); //$NON-NLS-1$
		String replacedWSDLContent = replaceFileContent(wsdlFileAsString, "HelloWorldBPELProcess", processName); //$NON-NLS-1$
		String replacedDeployContent = replaceFileContent(deployFileAsString, "HelloWorldBPELProcess", processName); //$NON-NLS-1$
		String replacedSettingContent = replaceFileContent(settingFileAsString, "HelloWorldBPELProcess", //$NON-NLS-1$
				project.getName());

		replacedProcessContent = replaceFileContent(replacedProcessContent, "http://eclipse.org/bpel/sample", namespace); //$NON-NLS-1$
		replacedWSDLContent = replaceFileContent(replacedWSDLContent, "http://eclipse.org/bpel/sample", namespace); //$NON-NLS-1$
		replacedDeployContent = replaceFileContent(replacedDeployContent, "http://eclipse.org/bpel/sample", namespace); //$NON-NLS-1$

		String newServiceXMLContent = null;
		try {
			newServiceXMLContent = createServiceXMLContent(serviceXMLAsString);
		} catch (Exception e) {
			log.error("Error in creating service.xml file",e); //$NON-NLS-1$
		}

		FileUtils.createFile(newProcessFile, replacedProcessContent);
		FileUtils.createFile(deployfile, replacedDeployContent);
		FileUtils.createFile(newSettingsFile, replacedSettingContent);
		FileUtils.createFile(seriveXML, newServiceXMLContent);

		if (!BPELArtifactConstants.EMPTY_BPEL_PROCESS.equals(bpelModel.getSelectedTemplate())) {
			FileUtils.createFile(newWSDLFile, replacedWSDLContent);
		} else {
			boolean deleteQuietly = org.apache.commons.io.FileUtils.deleteQuietly(deployfile);
			if (!deleteQuietly) {
				deployfile.delete();
			}
		}

		boolean deleteQuietly = org.apache.commons.io.FileUtils.deleteQuietly(processFile);
		if (!deleteQuietly) {
			processFile.delete();
		}
		boolean deleteQuietly1 = org.apache.commons.io.FileUtils.deleteQuietly(wsdlfile);
		if (!deleteQuietly1) {
			wsdlfile.delete();
		}

	}

	/**
	 * Creates service.xml content
	 * 
	 * @param serviceXMLAsString
	 * 
	 * @return new Service.xml content
	 * @throws Exception
	 */
	private String createServiceXMLContent(String serviceXMLAsString) throws Exception {
		String eol = System.getProperty(LINE_SEPERATOR);
		ITemporaryFileTag serviceTempTag = FileUtils.createNewTempTag();

		serviceXMLAsString = serviceXMLAsString.replaceAll("\\{", "<"); //$NON-NLS-1$ //$NON-NLS-2$
		serviceXMLAsString = serviceXMLAsString.replaceAll("\\}", ">"); //$NON-NLS-1$ //$NON-NLS-2$

		StringBuffer sbService = new StringBuffer();
		sbService.append("<service name=\"").append(processName).append("\">").append("</service>\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		serviceXMLAsString = serviceXMLAsString.replaceAll("<services>", sbService.toString()); //$NON-NLS-1$
		StringBuffer sbCallback = new StringBuffer();
		sbCallback.append("<service name=\"").append(processName + "Callback").append("\">").append("</service>\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		serviceXMLAsString = serviceXMLAsString.replaceAll("<callbackService>", sbCallback.toString()); //$NON-NLS-1$
		serviceXMLAsString = XMLUtil.prettify(serviceXMLAsString);
		serviceXMLAsString = serviceXMLAsString.replaceAll("^" + eol, ""); //$NON-NLS-1$ //$NON-NLS-2$
		serviceTempTag.clearAndEnd();

		return serviceXMLAsString;
	}

	public String replaceFileContent(String sourceString, String replacer, String toreplace) {
		String replacedContent = sourceString.replaceAll(replacer, toreplace);
		return replacedContent;
	}

	public void extractImportFile(IProject importProject) throws IOException {
		File importFile = getModel().getImportFile();
		IFolder bpelContent = importProject.getFolder("bpelContent"); //$NON-NLS-1$
		bpelContent.getLocation().toFile().mkdirs();
		ArchiveManipulator archiveManupulator = new ArchiveManipulator();
		archiveManupulator.extract(importFile, bpelContent.getLocation().toFile());
	}

	public IResource getCreatedResource() {
		return project;
	}

	private void extractBPELSettingsTemplate(IProject importProject) throws IOException {
		ITemporaryFileTag bpelTempTag = FileUtils.createNewTempTag();
		File bpelTemplateFile = new BPELTemplateUtils().getResourceFile("templates/bpel-settings.zip"); //$NON-NLS-1$
		ArchiveManipulator archiveManipulator = new ArchiveManipulator();
		archiveManipulator.extract(bpelTemplateFile, importProject.getLocation().toFile());
		bpelTempTag.clearAndEnd();
	}

	private void addBPELTemplate(IProject newProject) throws IOException {
		File bpelTemplateFile = null;
		ITemporaryFileTag bpelTempTag = FileUtils.createNewTempTag();
		if (BPELArtifactConstants.ASYNCHRONOUS_BPEL_PROCESS.equals(bpelModel.getSelectedTemplate())) {
			bpelTemplateFile = new BPELTemplateUtils().getResourceFile("templates/asynchronous-bpel-template.zip"); //$NON-NLS-1$
		} else if (BPELArtifactConstants.SYNCHRONOUS_BPEL_PROCESS.equals(bpelModel.getSelectedTemplate())) {
			bpelTemplateFile = new BPELTemplateUtils().getResourceFile("templates/synchronous-bpel-template.zip"); //$NON-NLS-1$
		} else if (BPELArtifactConstants.EMPTY_BPEL_PROCESS.equals(bpelModel.getSelectedTemplate())) {
			bpelTemplateFile = new BPELTemplateUtils().getResourceFile("templates/empty-bpel-template.zip"); //$NON-NLS-1$
		}
		IFolder bpelContent = newProject.getFolder("bpelContent"); //$NON-NLS-1$
		bpelContent.getLocation().toFile().mkdirs();
		ArchiveManipulator archiveManipulator = new ArchiveManipulator();
		archiveManipulator.extract(bpelTemplateFile, bpelContent.getLocation().toFile());
		bpelTempTag.clearAndEnd();
	}

}
