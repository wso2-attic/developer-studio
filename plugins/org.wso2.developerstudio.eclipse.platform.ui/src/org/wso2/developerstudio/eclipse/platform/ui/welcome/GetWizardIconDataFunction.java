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

import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.platform.ui.Activator;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;

public class GetWizardIconDataFunction extends BrowserFunction {
private static IDeveloperStudioLog log = Logger.getLog(Activator.PLUGIN_ID);
	
	public GetWizardIconDataFunction(Browser browser) {
		super(browser, "GetWizardIconData");
	}

	@Override
	public Object function(Object[] arguments) {
		if (arguments != null && arguments.length > 0) {
			String wizardID = (String) arguments[0];
			try {
				return getImagaeData(wizardID);
			} catch (CoreException e) {
				log.error("Error while searching for wizard icon " + wizardID, e);
				return false;
			}
		}
		return false;
	}

	public String getImagaeData(String id) throws CoreException {
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
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageLoader loader = new ImageLoader();
			loader.data = new ImageData[] { descriptor.getImageDescriptor().getImageData() };
			loader.save(out, SWT.IMAGE_PNG);
			String base64 = Base64.encodeBase64String(out.toByteArray());
			return base64;
		}
		return null;
	}
}
