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

package org.wso2.developerstudio.eclipse.capp.core.manifest;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMDocument;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.wso2.developerstudio.eclipse.capp.core.Activator;
import org.wso2.developerstudio.eclipse.capp.core.data.Bundle;
import org.wso2.developerstudio.eclipse.capp.core.data.ImportFeature;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;

public class CARArtifactFeatureManifest extends AbstractXMLDoc {

	private static final String REQUIRE = "require";
	private static final String LICENCE = "licence";
	private static final String DESCRIPTION = "description";
	private static final String UNPACK = "unpack";
	private static final String LABEL = "label";
	private static final String EQUIVALENT = "equivalent";
	private static final String MATCH = "match";
	private static final String ID = "id";
	private static final String PLUGIN = "plugin";
	private static final String FEATURE = "feature";
	private static final String IMPORT = "import";
	private static final String FEATURE_XML = "feature.xml";

	private static IDeveloperStudioLog log = Logger.getLog(Activator.PLUGIN_ID);

	private String featureId;
	private String version;
	private Artifact artifact;

	private List<ImportFeature> importFeatures;
	private List<Bundle> bundles;

	private static OMFactory factory = OMAbstractFactory.getOMFactory();

	public CARArtifactFeatureManifest(Artifact artifact) {
		setArtifact(artifact);
	}

	/**
	 * @return the featureId
	 */
	public String getFeatureId() {
		return featureId;
	}

	/**
	 * @param featureId
	 *            the featureId to set
	 */
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the importedFeatures
	 */
	public List<ImportFeature> getImportFeatures() {
		if (importFeatures == null) {
			importFeatures = new ArrayList<ImportFeature>();
		}
		return importFeatures;
	}

	public void addImportFeature(ImportFeature importFeature) {
		getImportFeatures().add(importFeature);
	}

	public void addImportFeature(String id, String version, String compatibility) {
		ImportFeature importFeature = new ImportFeature();
		importFeature.setFeatureId(id);
		importFeature.setVersion(version);
		importFeature.setCompatibility(compatibility);
		addImportFeature(importFeature);
	}

	private OMElement getDocumentElement() {
		OMElement featureElement = getElement(FEATURE, "");
		addAttribute(featureElement, ID, getFeatureId());
		addAttribute(featureElement, LABEL, getFeatureId());
		addAttribute(featureElement, ArtifactConstants.VERSION, getVersion());

		// FIXME provider name should be parameterised
		addAttribute(featureElement, "provider-name", ArtifactConstants.WSO2);

		featureElement.addChild(getDescriptionElement());
		featureElement.addChild(getCopyrightElement());
		featureElement.addChild(getLicenceElement());

		OMElement[] pluginElements = getPluginElements();
		for (OMElement element : pluginElements) {
			featureElement.addChild(element);
		}
		featureElement.addChild(getRequireElement());
		return featureElement;
	}

	private OMElement getDescriptionElement() {
		return getElement(DESCRIPTION, getFeatureId());
	}

	private OMElement getCopyrightElement() {
		return getElement(DESCRIPTION, "%copyright");
	}

	private OMElement getLicenceElement() {
		return addAttribute(getElement(LICENCE, "%licence"), "url",
				"%licenceURL");
	}

	private OMElement getRequireElement() {
		OMElement requireElement = getElement(REQUIRE, "");
		OMElement[] importFeatureElements = getImportFeatureElements();
		for (OMElement element : importFeatureElements) {
			requireElement.addChild(element);
		}
		return requireElement;
	}

	private OMElement[] getImportFeatureElements() {
		List<OMElement> list = new ArrayList<OMElement>();
		for (ImportFeature importFeature : getImportFeatures()) {
			list.add(getImportFeatureElement(importFeature));
		}
		return list.toArray(new OMElement[] {});
	}

	private OMElement[] getPluginElements() {
		List<OMElement> list = new ArrayList<OMElement>();
		for (Bundle bundle : getBundles()) {
			OMElement element = getElement(PLUGIN, "");
			addAttribute(element, ID, bundle.getName());
			addAttribute(element, ArtifactConstants.VERSION, bundle.getVersion());
			addAttribute(element, UNPACK, Boolean.toString(bundle.isUnpack()));
			list.add(element);
		}
		return list.toArray(new OMElement[] {});
	}

	private OMElement getImportFeatureElement(ImportFeature importFeature) {
		OMElement importElement = getElement(IMPORT, "");
		addAttribute(importElement, FEATURE, importFeature.getFeatureId());
		addAttribute(importElement, ArtifactConstants.VERSION, importFeature.getVersion());
		addAttribute(importElement, MATCH, importFeature.getCompatibility());
		return importElement;
	}

	public void setArtifact(Artifact artifact) {
		this.artifact = artifact;
		setFeatureId(artifact.getName());
		setVersion(artifact.getVersion());
		List<ArtifactDependency> dependencies = artifact.getDependencies();
		for (ArtifactDependency dependency : dependencies) {
			ImportFeature f = new ImportFeature();
			f.setFeatureId(dependency.getName());
			f.setVersion(dependency.getVersion());
			f.setCompatibility(EQUIVALENT);
			getImportFeatures().add(f);
		}
	}

	public Artifact getArtifact() {
		return artifact;
	}

	public void setBundles(List<Bundle> bundles) {
		this.bundles = bundles;
	}

	public List<Bundle> getBundles() {
		if (bundles == null) {
			bundles = new ArrayList<Bundle>();
		}
		return bundles;
	}

	protected String getDefaultName() {
		return FEATURE_XML;
	}

	protected void deserialize(OMElement documentElement) {

	}

	protected String serialize() {
		OMDocument document = factory.createOMDocument();
		document.addChild(getDocumentElement());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			document.serialize(outputStream);
		} catch (XMLStreamException e) {
			log.error("Error while serializing", e);
			return null;
		}
		return outputStream.toString();
	}
}
