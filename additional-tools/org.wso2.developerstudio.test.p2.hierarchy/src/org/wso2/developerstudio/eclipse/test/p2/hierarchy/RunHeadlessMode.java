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

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.wso2.developerstudio.eclipse.test.p2.hierarchy.test.p2.CheckUpdatesManager;

public class RunHeadlessMode implements IPlatformRunnable {

	public Object run(Object arg0) throws Exception {
		String [] cmdArgs = (String []) arg0;
		CheckUpdatesManager checkUpdatesManager = new CheckUpdatesManager();
		System.out.println("Arguments Received : ------------------------------------------\n");
		System.out.println(cmdArgs[0] + " , " + cmdArgs[1]);
			System.out.println("\n \n Arguments Computed : ------------------------------------------\n");
			System.out.println(cmdArgs[0] + " and " + cmdArgs[1]);
			if (cmdArgs[1] == null || cmdArgs[0] == null) {
				System.out.println(
						"\n \n Invalid Arguments, arguments passed should be P2 repository URL , updated feature name \n");
				System.exit(1);
			}
			checkUpdatesManager.checkForAvailableUpdates(new IProgressMonitor() {

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
			}, cmdArgs[0], cmdArgs[1]);
		return cmdArgs;

	}
}
