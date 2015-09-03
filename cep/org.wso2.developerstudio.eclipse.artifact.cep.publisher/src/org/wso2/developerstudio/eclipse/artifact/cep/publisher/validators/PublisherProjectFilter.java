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
package org.wso2.developerstudio.eclipse.artifact.cep.publisher.validators;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class PublisherProjectFilter extends ViewerFilter {
	private static final String GENERAL_PROJECT_NATURE = "org.wso2.developerstudio.eclipse.general.project.nature";
	private static final String CEP_PROJECT_NATURE = "org.wso2.developerstudio.eclipse.artifact.cep.project.nature";
	private static boolean showGeneralProjects;
	
	public PublisherProjectFilter() {
	}
	
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(showGeneralProjects){
			if (element instanceof IContainer) {
				try {
					IContainer iContainer = (IContainer) element;
					IProject project = iContainer.getProject();
					if (!iContainer.getName().matches("^target$|^bin$|^[.].*")) {
						if (project.hasNature(GENERAL_PROJECT_NATURE)) {
							return true;
						}
					}else{
						return false;
					}
				} catch (Exception e) {/*ignore*/}
			}
		} else{
			if (element instanceof IProject) {
				try {
					if (((IProject) element)
							.hasNature(CEP_PROJECT_NATURE)) {
						return true;
					}
				} catch (Exception e) {/*ignore*/}
			}
		}
		
		return false;
	}
	
	public static void setShowGeneralProjects(boolean showGeneralProjects) {
		PublisherProjectFilter.showGeneralProjects = showGeneralProjects;
	}
}
	
