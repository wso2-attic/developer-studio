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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.platform.ui.Activator;

public class WelcomePage extends ViewPart {

	private static final String INDEX_HTML = "index.html";
	private static final String WELCOME_PAGE_WEB_SITE_FOLDER = "welcomePageWebSite";

	private static IDeveloperStudioLog log = Logger.getLog(Activator.PLUGIN_ID);

	private Browser browser;

	@Override
	public void createPartControl(Composite parent) {
		browser = createBrowser(parent);
		try {
			new OpenIDEWizardFunction(browser);
			new GetDashboardWizardContributionsFunction(browser);
			new GetWizardDescriptionFunction(browser);
			new GetWizardIconDataFunction(browser);
			browser.setUrl(getWelcomePage());
		} catch (URISyntaxException e) {
			log.error("Error while intializing Welcome Page", e);
		} catch (IOException e) {
			log.error("Error while intializing Welcome Page", e);
		}
	}

	private String getWelcomePage() throws URISyntaxException, IOException {
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		URL webAppURL = bundle.getEntry(WELCOME_PAGE_WEB_SITE_FOLDER);
		URL resolvedFolderURL = FileLocator.toFileURL(webAppURL);
		URI resolvedFolderURI = new URI(resolvedFolderURL.getProtocol(), resolvedFolderURL.getPath(), null);
		File resolvedWebAppFolder = new File(resolvedFolderURI);
		File resolvedWebAppIndex = new File(resolvedWebAppFolder, INDEX_HTML);
		return resolvedWebAppIndex.getAbsolutePath();
	}

	private Browser createBrowser(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		parent.setLayout(gridLayout);
		Browser browser = new Browser(parent, SWT.NONE);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.horizontalSpan = 1;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		browser.setLayoutData(data);
		return browser;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

}
