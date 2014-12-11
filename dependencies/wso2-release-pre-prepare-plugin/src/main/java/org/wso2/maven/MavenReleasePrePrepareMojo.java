/*
 * Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.xml.stream.FactoryConfigurationError;

import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.manager.BasicScmManager;
import org.apache.maven.scm.manager.NoSuchScmProviderException;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.provider.svn.svnexe.SvnExeScmProvider;
import org.apache.maven.scm.repository.ScmRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.wso2.maven.esb.ESBArtifact;
import org.wso2.maven.esb.ESBProjectArtifact;
import org.wso2.maven.registry.GeneralProjectArtifact;
import org.wso2.maven.registry.RegistryArtifact;

/**
 * This is the Maven Mojo used for do pre prepare tasks such as update version
 * of artifacts in Artifact.xml etc.
 * 
 * @goal pre-prepare
 * 
 */
public class MavenReleasePrePrepareMojo extends AbstractMojo {

	private static final String PROJECTNATURES = "projectnatures";
	private static final String MAVEN_ECLIPSE_PLUGIN = "maven-eclipse-plugin";
	private static final String POM_XML = "pom.xml";
	private static final String ORG_WSO2_DEVELOPERSTUDIO_ECLIPSE_GENERAL_PROJECT_NATURE =
	                                                                                      "org.wso2.developerstudio.eclipse.general.project.nature";
	private static final String ORG_WSO2_DEVELOPERSTUDIO_ECLIPSE_ESB_PROJECT_NATURE =
	                                                                                  "org.wso2.developerstudio.eclipse.esb.project.nature";
	private static final String DEPENDENCY = "dependency.";
	private static final String ARTIFACT_XML_REGEX = "**/artifact.xml";
	private static final String DEVELOPMENT = "development";
	private static final String RELEASE = "release";
	private static final String SCM = "scm:";
	private static final String SCM_URL = "scm.url";
	private static final String SCM_TAG_BASE = "scm.tagBase";
	private static final String SCM_TAG = "scm.tag";
	private static final String POM = "pom";
	private static final String RELEASE_PROPERTIES = "release.properties";
	private static final String ARTIFACT_XML = "artifact.xml";

	private final Log log = getLog();

	/**
	 * @parameter default-value="${project}"
	 */
	private MavenProject project;

	/**
	 * O
	 * 
	 * @parameter expression="${dryRun}" default-value="false"
	 */
	private boolean dryRun;

	public void execute() throws MojoExecutionException, MojoFailureException {

		if (dryRun) {
			log.warn(" **** wso2-relase-pre-prepare-plugin does not support for dryRun mode **** ");
			return;
		}

		Properties prop = new Properties();
		String baseDirPath = project.getBasedir().getPath();
		File artifactXml = new File(baseDirPath, ARTIFACT_XML);
		String packagingType = project.getPackaging();

		if (!artifactXml.exists() && packagingType.equals(POM)) {
			InputStream input = null;
			try {
				input = new FileInputStream(new File(baseDirPath, RELEASE_PROPERTIES));
				prop.load(input);
				String scmTag = prop.getProperty(SCM_TAG);
				String scmTagBase = prop.getProperty(SCM_TAG_BASE);
				String scmUrl = prop.getProperty(SCM_URL);
				if (scmUrl == null) {
					scmUrl = project.getScm().getConnection();
				}
				ScmManager scmManager = new BasicScmManager();
				String scmProvider = scmUrl.split(":")[1];
				scmManager.setScmProvider(scmProvider, new SvnExeScmProvider());
				String checkoutUrl =
				                     SCM +
				                             scmProvider +
				                             ":" +
				                             scmTagBase.replaceAll("/$", "").concat("/")
				                                       .concat(scmTag);
				// checkout and commit tag
				checkoutAndCommit(scmManager, prop, checkoutUrl, RELEASE);
				scmTagBase = project.getScm().getConnection();
				// checkout and commit trunk
				checkoutAndCommit(scmManager, prop, scmTagBase, DEVELOPMENT);

				ScmFileSet scmFileSet =
				                        new ScmFileSet(new File(baseDirPath), ARTIFACT_XML_REGEX,
				                                       null);
				ScmRepository scmRepository = scmManager.makeScmRepository(scmUrl);
				// update the local repo with modified artifact.xml file
				scmManager.update(scmRepository, scmFileSet);

			} catch (FileNotFoundException e) {
				log.error("Cannot find the release.properties file", e);
			} catch (IOException e) {
				log.error("error occure while loading properties from release.properties file", e);
			} catch (ScmRepositoryException e) {
				log.error("error occure while loading ScmRepository", e);
			} catch (NoSuchScmProviderException e) {
				log.error("Cannot find Scm Provider", e);
			} catch (FactoryConfigurationError e) {
				log.error("error occure while reading factory configurations", e);
			} catch (ScmException e) {
				log.error("error occure while loading ScmRepository", e);
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						log.error("error occure while closing input stream", e);
					}
				}
			}
		}
	}

	/**
	 * Check out the artifact.xml file and change the artifact version and
	 * commit the modified file
	 * 
	 * @param scmManager
	 *            ScmManager
	 * @param prop
	 *            properties in release.properties file
	 * @param checkoutUrl
	 *            url of the repository
	 * @param repoType
	 *            type of the repository
	 * @throws FactoryConfigurationError
	 * @throws IOException
	 * @throws ScmException
	 * @throws Exception
	 */
	private void checkoutAndCommit(ScmManager scmManager, Properties prop, String checkoutUrl,
	                               String repoType) throws FactoryConfigurationError, IOException,
	                                               ScmException {

		ScmRepository scmRepository = scmManager.makeScmRepository(checkoutUrl);
		String scmBaseDir = project.getBuild().getDirectory();
		// create temp directory for checkout
		File targetFile = new File(scmBaseDir, repoType);
		targetFile.mkdirs();
		ScmFileSet scmFileSet = new ScmFileSet(targetFile, ARTIFACT_XML_REGEX, null);
		CheckOutScmResult checkOut = scmManager.checkOut(scmRepository, scmFileSet);
		List<ScmFile> checkedOutFiles = checkOut.getCheckedOutFiles();

		for (ScmFile scmFile : checkedOutFiles) {
			String scmFilePath = scmFile.getPath();
			if (scmFilePath.endsWith(ARTIFACT_XML)) {
				File artifactXml = new File(scmBaseDir, scmFilePath);
				File pomFile =
				               new File(scmBaseDir, scmFilePath.replaceAll(ARTIFACT_XML + "$",
				                                                           POM_XML));
				if (!pomFile.exists()) {
					log.warn(" skiping an artifact.xml does not belongs to a maven project ");
					continue;
				}
				try {

					if (hasNature(pomFile, ORG_WSO2_DEVELOPERSTUDIO_ECLIPSE_ESB_PROJECT_NATURE)) {
						setESBArtifactVersion(prop, repoType, artifactXml);
					} else if (hasNature(pomFile,
					                     ORG_WSO2_DEVELOPERSTUDIO_ECLIPSE_GENERAL_PROJECT_NATURE)) {
						setRegArtifactVersion(prop, repoType, artifactXml);
					}
				} catch (Exception e) {
					log.error("error occure while set artifact version", e);
				}

			}
		}
		ScmFileSet scmCheckInFileSet =
		                               new ScmFileSet(new File(scmBaseDir), ARTIFACT_XML_REGEX,
		                                              null);
		// commit modified artifact.xml files
		scmManager.checkIn(scmRepository, scmCheckInFileSet,
		                   "  commited modified artifact.xml file ... ");
	}

	/**
	 * set Registry artifact version in articact.xml file
	 * 
	 * @param prop
	 *            properties in release.properties file
	 * @param repoType
	 *            type of the repository
	 * @param artifactXml
	 * @throws FactoryConfigurationError
	 * @throws Exception
	 */

	private void setRegArtifactVersion(Properties prop, String repoType, File artifactXml)
	                                                                                      throws FactoryConfigurationError,
	                                                                                      Exception {
		GeneralProjectArtifact projectArtifact = new GeneralProjectArtifact();
		projectArtifact.fromFile(artifactXml);
		for (RegistryArtifact artifact : projectArtifact.getAllESBArtifacts()) {
			if (artifact.getVersion() != null && artifact.getType() != null) {
				String releaseVersion =
				                        prop.getProperty(DEPENDENCY + artifact.getGroupId() + ":" +
				                                         artifact.getName() + "." + repoType);
				if (releaseVersion != null) {
					artifact.setVersion(releaseVersion);
				}
			}
		}
		projectArtifact.toFile();
	}

	/**
	 * set ESB artifact version in articact.xml file
	 * 
	 * @param prop
	 *            properties in release.properties file
	 * @param repoType
	 *            type of the repository
	 * @param artifactXml
	 * @throws FactoryConfigurationError
	 * @throws Exception
	 */

	private void setESBArtifactVersion(Properties prop, String repoType, File artifactXml)
	                                                                                      throws FactoryConfigurationError,
	                                                                                      Exception {
		ESBProjectArtifact projectArtifact = new ESBProjectArtifact();
		projectArtifact.fromFile(artifactXml);
		for (ESBArtifact artifact : projectArtifact.getAllESBArtifacts()) {
			if (artifact.getVersion() != null && artifact.getType() != null) {
				String releaseVersion =
				                        prop.getProperty(DEPENDENCY + artifact.getGroupId() + ":" +
				                                         artifact.getName() + "." + repoType);
				if (releaseVersion != null) {
					artifact.setVersion(releaseVersion);
				}
			}
		}
		projectArtifact.toFile();
	}

	/**
	 * check the project nature
	 * 
	 * @param pomFile
	 *            pom file of the project
	 * @param nature
	 *            project nature
	 * @return
	 */

	private boolean hasNature(final File pomFile, final String nature) {
		Model model = null;
		FileReader reader = null;
		MavenXpp3Reader mavenreader = new MavenXpp3Reader();
		try {
			reader = new FileReader(pomFile);
			model = mavenreader.read(reader);
			MavenProject project = new MavenProject(model);
			@SuppressWarnings("unchecked")
			List<Plugin> plugins = project.getBuild().getPlugins();
			for (Plugin plugin : plugins) {
				if (plugin.getArtifactId().equals(MAVEN_ECLIPSE_PLUGIN)) {
					Xpp3Dom configurationNode = (Xpp3Dom) plugin.getConfiguration();
					Xpp3Dom projectnatures = configurationNode.getChild(PROJECTNATURES);
					Xpp3Dom[] natures = projectnatures.getChildren();
					for (Xpp3Dom xpp3Dom : natures) {
						if (nature.equals(xpp3Dom.getValue())) {
							return true;
						}
					}
					break;
				}
			}
		} catch (FileNotFoundException e) {
			log.error("Cannot find the pom file", e);
		} catch (IOException e) {
			log.error("error occure while reading the pom file", e);
		} catch (XmlPullParserException e) {
			log.error("error occure while parse the pom file", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					log.error("error occure while closing input stream", e);
				}
			}
		}

		return false;
	}

}
