/*
 * Copyright (c) 2012, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.developerstudio.eclipse.artifact.cep.stream.project.nature;

import java.io.File;
import java.util.List;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;
import org.wso2.developerstudio.eclipse.artifact.cep.stream.Activator;
import org.wso2.developerstudio.eclipse.capp.maven.utils.MavenConstants;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.maven.util.MavenUtils;
import org.wso2.developerstudio.eclipse.platform.core.nature.AbstractWSO2ProjectNature;
import org.wso2.developerstudio.eclipse.utils.file.FileUtils;
import org.wso2.developerstudio.eclipse.utils.ide.FileExtensionResourcevisitor;

public class StreamProjectNature extends AbstractWSO2ProjectNature {
	private static IDeveloperStudioLog log = Logger.getLog(Activator.PLUGIN_ID);

	public void configure() throws CoreException, JavaModelException {
		try {
			updatePom();
		} catch (Exception e) {
			log.error("Failed to update POM file", e);
		}
	}

	public void deconfigure() throws CoreException {

	}

	public void updatePom() throws Exception {
		File mavenProjectPomLocation = getProject().getFile("pom.xml")
				.getLocation().toFile();
		MavenProject mavenProject = MavenUtils
				.getMavenProject(mavenProjectPomLocation);
		Plugin pluginEntry = MavenUtils.createPluginEntry(mavenProject,
				"org.wso2.maven", "maven-cep-plugin",
				MavenConstants.WSO2_CEP_STREAM_VERSION, true);
		Xpp3Dom configurationNode = MavenUtils
				.createMainConfigurationNode(pluginEntry);
		Xpp3Dom artifactNode = MavenUtils.createXpp3Node(configurationNode,
				"artifact");
		if (getcepFile() != null) {
			String fileName = FileUtils.getRelativePath(getProject()
					.getLocation().toFile(), getcepFile().getLocation()
					.toFile());
			artifactNode.setValue(fileName);
		}
		MavenUtils.saveMavenProject(mavenProject, mavenProjectPomLocation);
	}

	private IFile getcepFile() throws CoreException {
		getProject().refreshLocal(IResource.DEPTH_INFINITE,
				new NullProgressMonitor());
		FileExtensionResourcevisitor fileExtensionResourceVisitor = new FileExtensionResourcevisitor(
				".xml", IResource.FILE);
		getProject().accept(fileExtensionResourceVisitor);
		List<IResource> resources = fileExtensionResourceVisitor
				.getResourceList();
		return resources.size() == 0 ? null : (IFile) resources.get(0);
	}

}
