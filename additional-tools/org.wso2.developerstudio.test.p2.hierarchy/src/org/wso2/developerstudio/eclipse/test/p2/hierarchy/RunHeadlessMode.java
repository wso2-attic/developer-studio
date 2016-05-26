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

package org.wso2.developerstudio.eclipse.test.p2.hierarchy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.wso2.developerstudio.eclipse.test.p2.hierarchy.test.p2.CheckUpdatesManager;

public class RunHeadlessMode implements IApplication {
	

	@Override
	public Object start(IApplicationContext arg0) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, Object> contextArguments = arg0.getArguments();
		String [] cmdArgs = (String[]) contextArguments.get(IApplicationContext.APPLICATION_ARGS);
		CheckUpdatesManager checkUpdatesManager = new CheckUpdatesManager();
		if (cmdArgs.length == 2) {
		System.out.println("Arguments Received : ------------------------------------------\n");
		System.out.println("\n URL of the Repository tested  : " + cmdArgs[0] + "\n Feature Being Checked :" + cmdArgs[1]);
			System.out.println("\n \n Arguments Computed : ------------------------------------------\n");
			System.out.println(cmdArgs[0] + " and " + cmdArgs[1]);
			if (cmdArgs[1] == null || cmdArgs[0] == null) {
				System.out.println(
						"\n \n Invalid Arguments, arguments passed should be P2 repository URL and updated feature ID \n");
				System.exit(1);
			}
			checkUpdatesManager.checkForAvailableUpdates(generateNewProgressMonitor(), cmdArgs[0], cmdArgs[1]);
		} else if (cmdArgs.length > 2) {
			List<String> comdArgArray = new ArrayList<String>(Arrays.asList(cmdArgs));
		    comdArgArray.remove(0);
		    String[] array = comdArgArray.toArray(new String[comdArgArray.size()]);
		    checkUpdatesManager.checkForAvailableUpdates(generateNewProgressMonitor(), cmdArgs[0], array);
		    System.out.println("Arguments Received : ------------------------------------------\n");
		    System.out.println(
					"\n \n Received Arguments, with multiple feature IDs  \n");
		} else {
			System.out.println(
					"\n \n Invalid Arguments, arguments passed should be P2 repository URL and updated feature ID \n");
			System.exit(1);
		}
		return cmdArgs;

	}

	private IProgressMonitor generateNewProgressMonitor() {
		return new IProgressMonitor() {

			@Override
			public void worked(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void subTask(String arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setTaskName(String arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setCanceled(boolean arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isCanceled() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void internalWorked(double arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void done() {
				// TODO Auto-generated method stub

			}

			@Override
			public void beginTask(String arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		};
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
