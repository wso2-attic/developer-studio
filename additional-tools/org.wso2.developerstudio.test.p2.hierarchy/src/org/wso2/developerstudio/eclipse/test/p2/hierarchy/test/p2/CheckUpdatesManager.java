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

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

public class CheckUpdatesManager {

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
	public boolean checkForAvailableUpdates(IProgressMonitor monitor, String updateURL, String updatedFeatureID)
			throws Exception {
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

		SubMonitor progress1 = SubMonitor.convert(monitor, "", 10);

		Map<String, String> allFeaturesInUpdateRepo = filterInstallableUnits(WSO2_FEATURE_PREFIX, FEATURE_JAR_IU_ID_SFX,
				allIUQueryResult, progress1.newChild(1));

		if (progress.isCanceled()) {
			throw new OperationCanceledException();
		}
		updatedFeatureID = updatedFeatureID + "." + FEATURE_JAR_IU_ID_SFX;
		if (allFeaturesInUpdateRepo.containsKey(updatedFeatureID)) {
			System.out.println("\n===========================xxxxxxxxxxxxxxxxxxx==================================\n");
			System.out.println("-------------------------P2 Structure is valid----------------- \n \n ");
			System.out.println("\n=====================================================================\n");
			System.out.println("\n=====================================================================\n");
			System.out.println("===========          " + updatedFeatureID + "   is Available in the Updates Repository          =============\n");
			System.out.println("\n=====================================================================\n");
			System.exit(0);
		} else {
			System.exit(1);
		}

		return false;

	}

	private Map<String, String> filterInstallableUnits(String idPrefix, String idSuffix,
			IQueryResult<IInstallableUnit> queryResult, IProgressMonitor monitor) {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		Iterator<IInstallableUnit> iterator = queryResult.iterator();
		SubMonitor progress = SubMonitor.convert(monitor, "", queryResult.toSet().size());
		Map<String, String> loadedFeatureMap = new HashMap<>();

		while (iterator.hasNext()) {
			if (progress.isCanceled()) {
				throw new OperationCanceledException();
			}
			IInstallableUnit iu = iterator.next();
			String versionedID = iu.getId();
			progress.subTask("" + versionedID);
			if (versionedID != null && versionedID.startsWith(idPrefix) && versionedID.endsWith(idSuffix)) {
				loadedFeatureMap.put(iu.getId(), iu.PROP_DESCRIPTION);
			}
			progress.worked(1);
		}
		return loadedFeatureMap;
	}

}
