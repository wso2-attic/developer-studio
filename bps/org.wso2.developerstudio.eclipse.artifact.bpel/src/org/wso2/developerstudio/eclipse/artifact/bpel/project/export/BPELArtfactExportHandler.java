/*
 * Copyright (c) 2011, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.developerstudio.eclipse.artifact.bpel.project.export;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.wso2.developerstudio.eclipse.platform.core.project.export.ProjectArtifactHandler;
import org.wso2.developerstudio.eclipse.utils.archive.ArchiveManipulator;
import org.wso2.developerstudio.eclipse.utils.file.FileUtils;
import org.wso2.developerstudio.eclipse.utils.file.TempFileUtils;

public class BPELArtfactExportHandler extends ProjectArtifactHandler {
	private static List<String> excludeList=new ArrayList<String>();

	static {
		excludeList.add("pom.xml");
		excludeList.add("build.xml");
		excludeList.add(".project");
		excludeList.add(".classpath");
		excludeList.add(".svn");
		excludeList.add("target");
		excludeList.add(".settings");
	}

	public List<IResource> exportArtifact(IProject project) throws Exception {
		List<IResource> exportResources = new ArrayList<IResource>();

		ArchiveManipulator archiveManipulator = new ArchiveManipulator();

		clearTarget(project);
		
		// get bpelContent files location
		IPath bpelContent = project.getFolder("bpelContent").getLocation();
		
		File tempProject = createTempProject();
		File bpelResources = createTempDir(tempProject, "bpel_resources");
		/*org.apache.commons.io.FileUtils.copyDirectory(project.getLocation().toFile(),
				bpelResources, new FileFilter() {
			
					@Override
					public boolean accept(File pathname) {
						if(pathname.isDirectory()){
							if(excludeList.contains(pathname.getName())) {
								return false;
							}
							File[] listFiles = pathname.listFiles();
							if(listFiles.length==1){
								if(excludeList.contains(listFiles[0].getName())){
									return false;
								}
							} else if (listFiles.length==0){
								return false;
							}
						} else {
							if(excludeList.contains(pathname.getName())) {
								return false;
							}
						}
						return true;
					}
				});*/
		
		// Fixing TOOLS-2327.
		if (bpelContent.toFile().exists()) {
			// copy bpelContent files
			FileUtils.copyDirectoryContentsWithoutDotSettingsDir(bpelContent.toFile(), bpelResources);
		}
		
		File tmpArchive = new File(tempProject, project.getName().concat(".zip"));
		archiveManipulator.archiveDir(tmpArchive.toString(),
				bpelResources.toString());
		IFile bpelArchive = getTargetArchive(project, "zip");
		FileUtils.copy(tmpArchive, bpelArchive.getLocation().toFile());
		exportResources.add((IResource) bpelArchive);

		TempFileUtils.cleanUp();

		return exportResources;
	}

}
