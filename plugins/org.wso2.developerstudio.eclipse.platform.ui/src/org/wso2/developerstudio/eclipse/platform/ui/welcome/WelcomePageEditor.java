package org.wso2.developerstudio.eclipse.platform.ui.welcome;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.osgi.framework.Bundle;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.platform.ui.Activator;

public class WelcomePageEditor extends EditorPart {
	
	private static final String INDEX_HTML = "index.html";
	private static final String WELCOME_PAGE_WEB_SITE_FOLDER = "welcomePageWebSite";
	
	private static IDeveloperStudioLog log = Logger.getLog(Activator.PLUGIN_ID);
	
	protected Browser browser;

	@Override
	public void doSave(IProgressMonitor arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(IEditorSite arg0, IEditorInput arg1) throws PartInitException {
		setSite(arg0);
		setInput(arg1);	
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		browser = createBrowser(parent);
		try {
			new OpenIDEWizardFunction(browser);
			new GetDashboardWizardContributionsFunction(browser);
			new GetWizardDescriptionFunction(browser);
			new GetWizardIconDataFunction(browser);
			browser.setUrl(getWelcomePage());
			browser.addProgressListener(new ProgressListener() {	
				@Override
				public void completed(ProgressEvent arg0) {
					browser.execute("setTimeout(function(){setViewPortFullScreen(400);},500);");
				}

				@Override
				public void changed(ProgressEvent arg0) {
					// TODO Auto-generated method stub	
				}
			});
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
