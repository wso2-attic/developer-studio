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
package org.wso2.developerstudio.eclipse.platform.ui.welcome;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.platform.ui.Activator;

public class OpenIDEWizardFunction extends BrowserFunction {

	private static IDeveloperStudioLog log = Logger.getLog(Activator.PLUGIN_ID);

	public OpenIDEWizardFunction(Browser browser) {
		super(browser, "OpenIDEWizard");
	}

	@Override
	public Object function(Object[] arguments) {
		if (arguments != null && arguments.length > 0) {
			String wizardID = (String) arguments[0];
			try {
				openWizard(wizardID);
				return true;
			} catch (CoreException e) {
				log.error("Error while openning wizard " + wizardID, e);
				return false;
			}
		}
		return false;
	}

	public void openWizard(String id) throws CoreException {
		// First see if this is a "new wizard".
		IWizardDescriptor descriptor = PlatformUI.getWorkbench().getNewWizardRegistry().findWizard(id);
		// If not check if it is an "import wizard".
		if (descriptor == null) {
			descriptor = PlatformUI.getWorkbench().getImportWizardRegistry().findWizard(id);
		}
		// Or maybe an export wizard
		if (descriptor == null) {
			descriptor = PlatformUI.getWorkbench().getExportWizardRegistry().findWizard(id);
		}
		// Then if we have a wizard, open it.
		if (descriptor != null) {
			IWizard wizard = descriptor.createWizard();
			WizardDialog wd = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
			wd.setTitle(wizard.getWindowTitle());
			wd.open();
		}
	}
}
