package org.wso2.developerstudio.rcp.plugin;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.equinox.app.IApplicationContext;

public class LifeCycleManager {
	
	@PostContextCreate
	void postContextCreate(final IEventBroker eventBroker, IApplicationContext context) {
	}
	
	@ProcessAdditions
	public void setPerspective(){
		
	}
}
