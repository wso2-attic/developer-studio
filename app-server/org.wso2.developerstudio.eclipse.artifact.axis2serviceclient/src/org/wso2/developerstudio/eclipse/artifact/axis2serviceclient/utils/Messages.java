package org.wso2.developerstudio.eclipse.artifact.axis2serviceclient.utils;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.wso2.developerstudio.eclipse.artifact.axis2serviceclient.utils.messages"; //$NON-NLS-1$
	public static String CLIENT_AXIS2_WIZARD_WINDOW_TITLE;
	public static String SKELETON_AXIS2_WIZARD_WINDOW_TITLE;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
