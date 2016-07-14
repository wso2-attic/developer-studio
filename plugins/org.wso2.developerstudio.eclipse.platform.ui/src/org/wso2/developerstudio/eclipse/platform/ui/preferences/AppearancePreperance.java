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

package org.wso2.developerstudio.eclipse.platform.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.wso2.developerstudio.eclipse.platform.ui.WorkbenchToolkit;

public class AppearancePreperance extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{
	public static final String SWT_VIEW = "swt";
	public static final String JS_VIEW = "js";
	public static final String DASHBOARD_PREFERNCES = "Dashboard_prefernces";
	public static final String CLASSIC = "Classic";
	public static final String ANIMATED = "Animated";
	private IPreferenceStore preferenceStore;
	
	@Override
	public void init(IWorkbench arg0) {
		preferenceStore = WorkbenchToolkit.getPreferenceStore();
		setPreferenceStore(preferenceStore);
		setDescription("Set Preferences for WSO2 Developer Studio Dashboard ");
		//setPreferenceDefaults(preferenceStore);
	}

	@Override
	protected void createFieldEditors() {
		Composite fieldEditorParent = getFieldEditorParent();
		//Composite enableDisableSet = new Composite(fieldEditorParent, SWT.LEFT);
		RadioGroupFieldEditor updateInstallationRadioBttn = new RadioGroupFieldEditor(DASHBOARD_PREFERNCES,
				"Specify Dashboard view Preference", 1,
				new String[][] { {ANIMATED, JS_VIEW },
						{CLASSIC, SWT_VIEW} },
				fieldEditorParent);
		addField(updateInstallationRadioBttn);
		boolean isUpdatesEnabled=true;
		updateInstallationRadioBttn.setEnabled(isUpdatesEnabled, fieldEditorParent);
		setSeparator(fieldEditorParent);
		addBlankSeparator(fieldEditorParent);
	}
	
	private void setSeparator(Composite parent) {
		Label label = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
	}

	private void addBlankSeparator(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 3, 1));
	}

}
