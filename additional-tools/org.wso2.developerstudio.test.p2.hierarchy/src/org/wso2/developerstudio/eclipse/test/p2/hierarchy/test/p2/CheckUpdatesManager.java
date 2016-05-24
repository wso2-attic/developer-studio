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

package org.wso2.developerstudio.eclipse.test.p2.hierarchy.test.p2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Iterator;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.wso2.developerstudio.eclipse.test.p2.hierarchy.RunHeadlessMode;
import org.eclipse.equinox.p2.metadata.Version;

public class CheckUpdatesManager {

	private static final String INSTALLER_SCRIPT_SH = "/home/developerstudio/p2_tools/featureInstallerScript.sh";

	public CheckUpdatesManager() {
		initProvisioningAgent();
		// TODO Auto-generated constructor stub
	}

	private static final String FEATURE_JAR_IU_ID_SFX = "feature.jar"; //$NON-NLS-1$
	private static final String WSO2_FEATURE_PREFIX = "org.wso2"; //$NON-NLS-1$

	@Inject
	protected IProvisioningAgentProvider agentProvider;
	protected IProvisioningAgent p2Agent;
	protected ProvisioningSession session;

	// Repository Managers
	protected IArtifactRepositoryManager artifactRepoManager;
	protected IMetadataRepositoryManager metadataRepoManager;
	protected UpdateOperation updateOperation;
	protected Collection<IInstallableUnit> repoUpdates;

	protected void initProvisioningAgent() {
		try {
			// Inject references
			BundleContext bundleContext = FrameworkUtil.getBundle(CheckUpdatesManager.class).getBundleContext();
			IEclipseContext serviceContext = EclipseContextFactory.getServiceContext(bundleContext);
			ContextInjectionFactory.inject(this, serviceContext);
			// get p2 agent for current system(Eclipse instance in this
			// case)
			// the location for the currently running system is null (see
			// docs)
			p2Agent = agentProvider.createAgent(null);
			session = new ProvisioningSession(p2Agent);
			artifactRepoManager = (IArtifactRepositoryManager) p2Agent
					.getService(IArtifactRepositoryManager.class.getName());
			metadataRepoManager = (IMetadataRepositoryManager) p2Agent
					.getService(IMetadataRepositoryManager.class.getName());

		} catch (Exception e) {
			System.exit(1);
		}
	}

	/**
	 * Finds available WSO2 features in current profile and search for updates
	 * to them in WSO2 p2 repository for updates.
	 * 
	 * @param monitor
	 * @throws Exception
	 */
	public void checkForAvailableUpdates(IProgressMonitor monitor, String updateURL, String featureID)
			throws Exception {
		Iterator<IInstallableUnit> allFeaturesInUpdateRepo = queryP2Repository(monitor, updateURL);
		System.out.println("Sending Feature ID with Version : " + featureID);
		if (!checkIfFeatureExist(featureID, allFeaturesInUpdateRepo)) {
			System.out.println("\n============================   FAILED =========================================\n");
			System.out.println("\n===================" + featureID
					+ " does not exist =================================================\n");
			System.exit(1);
		} else {
			installAvailableFeature();
		}
		System.exit(0);

	}

	public void checkForAvailableUpdates(IProgressMonitor monitor, String updateURL, String[] featureIDs)
			throws OperationCanceledException, URISyntaxException {

		Iterator<IInstallableUnit> allFeaturesInUpdateRepo = queryP2Repository(monitor, updateURL);
		for (String featureID : featureIDs) {
			System.out.println("Sending Feature ID with Version : " + featureID);
			if (!checkIfFeatureExist(featureID, allFeaturesInUpdateRepo)) {
				System.out
						.println("\n============================   FAILED =========================================\n");
				System.out.println("\n===================" + featureID
						+ " does not exist, other features will not be checked =================================================\n");
				System.exit(1);
			}
		}
		installAvailableFeature();
		// try installing all features since they have passed the initial check.
		System.exit(0);
	}

	private void installAvailableFeature() {
		String url = RunHeadlessMode.RELEASES_REPO_URL.trim();
		System.out.println("\n=============================== Installing the feautures from " + url
				+ " using script ======================================\n");
		String[] cmd = {INSTALLER_SCRIPT_SH};
		BufferedReader stdError = null;
		BufferedReader stdInput = null;
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			System.out.println("Waiting for script to end ...");
			stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String s = null;
			// read the output from the command
			System.out.println("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
				if (s.contains("failed.")) {
					failureExit(s);
				}
			}
			// read any errors from the attempted command
			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
				if (s.contains("failed.")) {
					failureExit(s);
				}
			}
			p.waitFor();
			if (p.exitValue() == 0) {
				System.out
						.println("\n=============================== SUCCESS ======================================\n");
			} else {
				System.out.println(
						"\n=============================== ERROR OCCURRED DURING FEATURE INSTALLATION ======================================\n");
				System.exit(p.exitValue());
			}
		} catch (IOException e) {
			System.out.print("\n IO exception in executing the script!! \n " + e);
			System.exit(1);
		} catch (InterruptedException e) {
			System.out.println(
					"\n=============================== ERROR OCCURRED DURING FEATURE INSTALLATION ======================================\n");
			System.exit(1);
		} finally {
			try {
				stdError.close();
				stdInput.close();
			} catch (IOException e) {
				// not necessary to print this error
			}
		}
	}

	private void failureExit(String s) {
		if (s.contains("failed.")) {
			System.out.println("\n=============================== FAILED ======================================\n");
			System.out.println("Installation of features at the repository \n " + RunHeadlessMode.RELEASES_REPO_URL
					+ "has failed. Exiting system with error code.");
			System.exit(1);
		}
	}

	private Iterator<IInstallableUnit> queryP2Repository(IProgressMonitor monitor, String updateURL)
			throws URISyntaxException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		SubMonitor progress = SubMonitor.convert(monitor, "", 6);
		// get all available IUs in update repository
		IMetadataRepository metadataRepo = null;
		try {
			metadataRepo = metadataRepoManager.loadRepository(new URI(updateURL), progress.newChild(1));
		} catch (ProvisionException e) {
			System.exit(1);
		}
		IQuery<IInstallableUnit> allIUQuery = QueryUtil.createIUAnyQuery();
		IQueryResult<IInstallableUnit> allIUQueryResult = metadataRepo.query(allIUQuery, progress.newChild(1));

		Iterator<IInstallableUnit> iterator = allIUQueryResult.iterator();

		if (progress.isCanceled()) {
			throw new OperationCanceledException();
		}
		return iterator;
	}

	private boolean checkIfFeatureExist(String featureID, Iterator<IInstallableUnit> iterator) {
		System.out.println("Received Feature ID with Version : " + featureID);
		String[] featureInfo = featureID.split("_");
		String updatedFeatureID = featureInfo[0];
		System.out.println("The version being tested : " + featureInfo[1]);
		Version featureVersion = generateOSGIVersion(featureInfo[1]);
		updatedFeatureID = updatedFeatureID + "." + FEATURE_JAR_IU_ID_SFX;
		System.out.println("Checking Availability of " + updatedFeatureID + "Version  : " + featureVersion
				+ " in the given repository");
		while (iterator.hasNext()) {
			IInstallableUnit iu = iterator.next();
			String featureIuId = iu.getId();
			Version IUVersion = iu.getVersion();
			if (featureIuId != null && featureIuId.startsWith(WSO2_FEATURE_PREFIX)
					&& featureIuId.endsWith(FEATURE_JAR_IU_ID_SFX)) {
				if (featureIuId.equals(updatedFeatureID)) {
					if (IUVersion.compareTo(featureVersion) == 0) {
						System.out.println(
								"\n===========================xxxxxxxxxxxxxxxxxxx==================================\n");
						System.out.println("-------------------------P2 Structure is valid----------------- \n \n ");
						System.out.println("\n=====================================================================\n");
						System.out.println("\n=====================================================================\n");
						System.out.println("===========          " + featureID + " Version  : " + featureVersion
								+ "   is Available in the Updates Repository          =============\n");
						System.out.println("\n=====================================================================\n");
						return true;
					}
				}
			}
		}
		System.out.println("\n=====================================================================\n");
		System.out.println("\n=====================================================================\n");
		System.out.println("=========== " + updatedFeatureID + "Version  : " + featureVersion
				+ " is not available in the given repository, Exiting system with error code ======================");
		return false;
	}

	private Version generateOSGIVersion(String installedVersion) {
		System.out.println("\nGenerationg version Object with " + installedVersion);
		String[] majorMinorUpperLmit = installedVersion.split("\\.");
		Version upperLimit = Version.createOSGi(Integer.parseInt(majorMinorUpperLmit[0]),
				Integer.parseInt(majorMinorUpperLmit[1]), Integer.parseInt(majorMinorUpperLmit[2]),
				majorMinorUpperLmit[3]);
		System.out.println("\nGenrated version : \n" + upperLimit.toString());
		return upperLimit;
	}

}
