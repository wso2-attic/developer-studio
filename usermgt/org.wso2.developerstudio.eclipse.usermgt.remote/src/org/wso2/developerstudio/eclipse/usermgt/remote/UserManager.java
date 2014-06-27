/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.developerstudio.eclipse.usermgt.remote;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.wso2.carbon.core.services.authentication.AuthenticationAdminStub;
import org.wso2.carbon.core.services.authentication.AuthenticationExceptionException;
//import org.wso2.carbon.user.mgt.stub.GetUsersOfRoleUserAdminExceptionException;
import org.wso2.carbon.user.mgt.stub.UserAdminStub;
import org.wso2.carbon.user.mgt.stub.UserAdminUserAdminException;
import org.wso2.carbon.user.mgt.stub.types.carbon.FlaggedName;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;

public class UserManager {
	private static IDeveloperStudioLog log=Logger.getLog(Activator.PLUGIN_ID);

	private String url;
	private String username;
	private String password;
	private UserAdminStub stub;

	public UserManager(String url, String username, String password) {
		init();
		setUrl(url);
		setUsername(username);
		setPassword(password);
	}

	private static void init() {
		System.setProperty("javax.net.ssl.trustStore", getJKSPath());
		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
		System.setProperty("javax.net.ssl.trustStoreType", "JKS");
	}

	private static String getJKSPath() {
		extractFilesToLocation("resources/security",
				new File(getMetaDataPath()));
		String path = getMetaDataPath() + File.separator + "security"
				+ File.separator + "wso2carbon.jks";
		return path;
	}

	public static void extractFilesToLocation(String resourcePath,
			File destination) {
		URL resource = Activator.getDefault().getBundle().getResource(
				resourcePath);

		if (isResourceFolder(resource)) {
			Enumeration confFolder = Activator.getDefault().getBundle()
					.getEntryPaths(resourcePath);
			String[] filePath = resource.getFile().split("/");
			File newDestinationPath = new File(destination,
					filePath[filePath.length - 1]);
			newDestinationPath.mkdirs();
			while (confFolder != null && confFolder.hasMoreElements()) {
				String newResourcefile = confFolder.nextElement().toString();
				extractFilesToLocation(newResourcefile, newDestinationPath);
			}

		} else {
			copyResourceToDestination(resource, destination);
		}
	}

	private static boolean isResourceFolder(URL url) {
		boolean result = true;
		try {
			InputStream openStream = url.openStream();
			if (openStream.available() > 0)
				result = false;
			openStream.close();
		} catch (IOException e) {
			// error
		}
		return result;
	}

	private static void copyResourceToDestination(URL url, File destination) {
		String[] filePath = url.getFile().split("/");
		File destinationFile = new File(destination,
				filePath[filePath.length - 1]);

		InputStream confOpenStream;
		try {
			confOpenStream = url.openStream();
			BufferedInputStream confBufferedInStream = null;
			confBufferedInStream = new BufferedInputStream(confOpenStream);
			FileOutputStream out = new FileOutputStream(destinationFile);
			byte[] buf = new byte[1024];
			int len;
			while ((len = confBufferedInStream.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			confOpenStream.close();
			out.close();
		} catch (IOException e) {
			log.error(e);
		}
	}

	public static String getMetaDataPath() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IPath location = root.getLocation();
		String metadataPath = location.toOSString() + File.separator
				+ ".metadata";
		return metadataPath;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public String[] getUsers() {
		try {
			FlaggedName[] usersOfRole = getStub().getUsersOfRole("", "*", -1);
			List<String> list = new ArrayList<String>();
			for (FlaggedName flaggedName : usersOfRole) {
				list.add(flaggedName.getItemName());
			}
			return list.toArray(new String[] {});
		} catch (RemoteException e) {
			log.error(e);
		} catch (AuthenticationExceptionException e) {
			log.error(e);
		} /*catch (GetUsersOfRoleUserAdminExceptionException e) {
			log.error(e);
		}*/ catch (UserAdminUserAdminException e) {
			log.error(e);
		}
		return new String[] {};
	}

	public String[] getRoles() {
		try {
			FlaggedName[] roles = getStub().getAllRolesNames("*", -1);
			List<String> list = new ArrayList<String>();
			for (FlaggedName flaggedName : roles) {
				list.add(flaggedName.getItemName());
			}
			return list.toArray(new String[] {});
		} catch (Exception e) {
			log.error(e);
		}
		return new String[] {};
	}

	public String[] getUsersForRoles(String role) {
		List<String> list = new ArrayList<String>();
		FlaggedName[] usersOfRole;
		if("Internal/everyone".equals(role)){
			return list.toArray(new String[] {});
		}
		try {
			usersOfRole = getStub().getUsersOfRole(role, "*", -1);
			for (FlaggedName ur : usersOfRole) {
				if (ur.getSelected())
					list.add(ur.getItemName());
			}
		} catch (Exception e) {
			log.error(e);
		}
		return list.toArray(new String[] {});

	}

	private UserAdminStub getStub() throws RemoteException,
			AuthenticationExceptionException {
		if (stub != null) {
			try {
				stub.getAllRolesNames("*", -1);
			} catch (Exception e) {
				stub = null;
			}
		}
		if (stub == null) {
			URL url = null;
			try {
				url = new URL(getUrl());
			} catch (MalformedURLException e) {
				log.error(e);
			}
			AuthenticationAdminStub authenticationStub = new AuthenticationAdminStub(
					getUrl() + "services/AuthenticationAdmin");

			authenticationStub._getServiceClient().getOptions()
					.setManageSession(true);
			boolean loginStatus = authenticationStub.login(getUsername(),
					getPassword(), url.getHost());
			
			if (!loginStatus) {
				//TODO: Throw the {@link:InvalidAuthenticationException}
			}
			ServiceContext serviceContext = authenticationStub
					._getServiceClient().getLastOperationContext()
					.getServiceContext();
			String sessionCookie = (String) serviceContext
					.getProperty(HTTPConstants.COOKIE_STRING);

			stub = new UserAdminStub(getUrl() + "services/UserAdmin");
			stub._getServiceClient().getOptions().setManageSession(true);
			stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(
					60000000);
			stub._getServiceClient().getOptions().setProperty(
					HTTPConstants.COOKIE_STRING, sessionCookie);

		}
		return stub;
	}

	public void setPermissionPerUser(String[] permissoonList, String username)
			throws RemoteException, AuthenticationExceptionException, Exception {
		getStub().updateRolesOfUser(username, permissoonList);
		
	}

	public void setPermissionPerResource(String role, String[] resourceList)
			throws RemoteException, Exception,
			AuthenticationExceptionException {
		getStub().setRoleUIPermission(role, resourceList);

	}

	public void addUser(String userName, String pwd, String[] roles)
			throws RemoteException, Exception,
			AuthenticationExceptionException {
		getStub().addUser(userName, pwd, roles, null, null);

	}

	public String[] getRolesPerUser(String username) throws Exception {

		FlaggedName[] ur = getStub().getRolesOfUser(username, "*", -1);
		String[] roles = new String[ur.length];
		int count = 0;
		for (int i = 0; i < ur.length; i++) {
			if (ur[i].getSelected()) {
				roles[count] = ur[i].getItemName();
				count++;
			}
		}
		return roles;
	}

	public void deleteUSer(String userName) throws Exception {
		getStub().deleteUser(userName);
	}

	public void changePWD(String username, String pwd) throws Exception {
		getStub().changePassword(username, pwd);
	}

	public void updateUserRoles(String userName, String[] role)
			throws Exception {
		getStub().updateRolesOfUser(userName, role);

	}
	
	public void addRole(String roleName, String[] userList,String[] permissions) throws RemoteException, Exception, AuthenticationExceptionException{
		getStub().addRole(roleName, userList, permissions, false);
	}
	
	public void deleteRole(String roleName) throws RemoteException, Exception, AuthenticationExceptionException{
		getStub().deleteRole(roleName);
	}

}
