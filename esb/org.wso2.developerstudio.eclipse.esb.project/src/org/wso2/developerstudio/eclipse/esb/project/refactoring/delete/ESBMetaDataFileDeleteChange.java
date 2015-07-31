/*
 * Copyright (c) 2012-2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.developerstudio.eclipse.esb.project.refactoring.delete;

import org.eclipse.core.resources.IFile;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.MultiTextEdit;
import org.wso2.developerstudio.eclipse.esb.project.Activator;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ESBMetaDataFileDeleteChange extends TextFileChange {
	IDeveloperStudioLog log= Logger.getLog(Activator.PLUGIN_ID);
	
	private IFile metaDataFile;
	private IFile originalFile;

	public ESBMetaDataFileDeleteChange(String name, IFile file, IFile originalFile) {
		super(name, file);
		metaDataFile = file;
		this.originalFile = originalFile;

		addTextEdits();
	}

	public ESBMetaDataFileDeleteChange(String name, IFile file, List<IFile> fileList) {
		super(name, file);
		setEdit(new MultiTextEdit());
		for (IFile iFile : fileList) {
			metaDataFile = file;
			this.originalFile = iFile;
			addTextEdits();
		}

	}

	private void addTextEdits() {
		if ("artifact.xml".equalsIgnoreCase(metaDataFile.getName())) {
			try {
				identifyReplaces();
			} catch (IOException e) {
				log.error("Error occured while generating the Refactoring", e);
			}
		}
	}

	private void identifyReplaces() throws IOException {
		String artifactsStart = "<artifacts>";
		String artifactsEnd = "</artifacts>";
		String artifactStart = "<artifact";
		String artifactEnd = "</artifact>";
		String nameProperty = "name=\"";

		List<String> artifactEntry = new ArrayList<String>();
		boolean isArtifact = false;
		boolean isArtifacts = false;
		boolean isArtifactMatch = false;
		boolean isArtifactLine=false;

		int fullIndex = 0;
		int startIndex = 0;
		int spaceCount = 0;
		BufferedReader reader =
		                        new BufferedReader(new FileReader(metaDataFile.getLocation()
		                                                                      .toFile()));
		String line = reader.readLine();
		String fileName =
		                  originalFile.getName().substring(0,
		                                                   originalFile.getName().length() -
		                                                       originalFile.getFileExtension()
		                                                                   .length() - 1);
		while (line != null) {
			if (!isArtifacts && line.contains(artifactsStart)) {
				isArtifacts = true;
			}

			if (isArtifacts && line.contains(artifactsEnd)) {
				isArtifacts = false;
			}

			if (isArtifacts) {
				isArtifactLine=false;
				if (!isArtifact && line.trim().contains(artifactStart)) {
					int artifactTagIndex = line.indexOf(artifactStart);
					spaceCount =0;
					for (int stringIndex = artifactTagIndex-1; stringIndex >= 0 ; stringIndex--) {
						if(line.charAt(stringIndex)==' '){
							spaceCount++;
						}else{
							break;
						}
					}
					startIndex = fullIndex + artifactTagIndex-spaceCount;
					if (line.contains(nameProperty + fileName + "\"")) {
						isArtifact = true;
						artifactEntry.add(line.substring(artifactTagIndex));
						isArtifactLine=true;
					} else {
						isArtifact = false;
						artifactEntry.clear();
						startIndex = 0;
					}
				}

				if (isArtifact) {
					if (!isArtifactLine && !artifactEntry.contains(line)) {
						artifactEntry.add(line);
					}
					if (line.trim().startsWith(artifactEnd)) {
						isArtifact = false;
						isArtifactMatch = true;
					}
				}

				if (isArtifactMatch) {
					int length = 0;
					for (String string : artifactEntry) {
						length += charsOnTheLine(string);
					}
					addEdit(new DeleteEdit(startIndex, length + spaceCount));
					break;
				}

			}

			fullIndex += charsOnTheLine(line);
			line = reader.readLine();
		}
		reader.close();
	}

	private int charsOnTheLine(String line) {
		// Here we need to add one to represent the newline character
		String lineModified = line;
		lineModified += System.getProperty("line.separator");
		return lineModified.length();
	}

}
