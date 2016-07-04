/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.developerstudio.eclipse.platform.ui.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.platform.core.Activator;
import org.wso2.developerstudio.eclipse.platform.ui.WorkbenchToolkit;
import org.wso2.developerstudio.eclipse.platform.ui.preferences.AppearancePreperance;

public class OpenDashboardAction extends Action implements IIntroAction {
	
	private static IDeveloperStudioLog log = Logger.getLog(Activator.PLUGIN_ID);
	static final String INTRO_VIEW_ID = "org.eclipse.ui.internal.introview";
	static String DASHBOARD_VIEW_ID = "org.wso2.developerstudio.eclipse.ui.welcome.WelcomePage";
	static final String J2EE_PERSPECTIVE_ID = "org.eclipse.jst.j2ee.J2EEPerspective";

	@Override
	public void run(IIntroSite arg0, Properties arg1) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		try {
			IPreferenceStore preferenceStore = WorkbenchToolkit.getPreferenceStore();
			if (preferenceStore.getString(AppearancePreperance.DASHBOARD_PREFERNCES)
					.equals(AppearancePreperance.SWT_VIEW)) {
				OpenDashboardAction.DASHBOARD_VIEW_ID="org.wso2.developerstudio.eclipse.dashboard";
			}
			hideIntroView();
			hideDashboards();
			PlatformUI.getWorkbench().showPerspective(J2EE_PERSPECTIVE_ID, window);
			
			page.openEditor(new NullEditorInput(), DASHBOARD_VIEW_ID);
		} catch (Exception e) {
			log.error("Cannot open dashboard", e);
		}
	}

	/**
	 * hide eclipse welcome page
	 */
	private void hideIntroView() {
		try {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IWorkbenchPage page = window.getActivePage();
			IViewReference ref = page.findViewReference(INTRO_VIEW_ID);
			page.hideView(ref);
		} catch (Exception e) {
			log.error("Error occured while hiding the eclipse welcome page", e);
		}
	}

	/**
	 * hide open dashboards
	 */
	private void hideDashboards() {
		try {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IWorkbenchPage page = window.getActivePage();
			List<IEditorReference> openEditors = new ArrayList<IEditorReference>();
			IEditorReference[] editorReferences =
			                                      PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
			                                                .getEditorReferences();
			for (IEditorReference iEditorReference : editorReferences) {
				if (DASHBOARD_VIEW_ID.equals(iEditorReference.getId())) {
					openEditors.add(iEditorReference);
				}
			}
			if (openEditors.size() > 0) {
				page.closeEditors(openEditors.toArray(new IEditorReference[] {}), false);
			}
		} catch (Exception e) {
			log.error("Error occured while hiding the dashboards", e);
		}
	}

	class NullEditorInput implements IEditorInput {

		public boolean exists() {
			return true;
		}

		public ImageDescriptor getImageDescriptor() {
			return ImageDescriptor.getMissingImageDescriptor();
		}

		public String getName() {
			return "Dashboard";
		}

		public IPersistableElement getPersistable() {
			return null;
		}

		public String getToolTipText() {
			return "Developer Studio Dashboard";
		}

		public Object getAdapter(Class adapter) {
			return null;
		}
	}

	

}
