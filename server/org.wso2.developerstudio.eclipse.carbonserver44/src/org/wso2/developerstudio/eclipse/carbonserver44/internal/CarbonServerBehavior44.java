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

package org.wso2.developerstudio.eclipse.carbonserver44.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jst.server.generic.core.internal.CorePlugin;
import org.eclipse.jst.server.generic.core.internal.GenericServerCoreMessages;
import org.eclipse.jst.server.generic.core.internal.GenericServerRuntime;
import org.eclipse.jst.server.generic.core.internal.Trace;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerPort;
import org.eclipse.wst.server.core.internal.DeletedModule;
import org.w3c.dom.Document;
import org.wso2.developerstudio.eclipse.carbonserver.base.impl.CarbonServerBehaviour;
import org.wso2.developerstudio.eclipse.carbonserver.base.manager.CarbonServerManager;
import org.wso2.developerstudio.eclipse.carbonserver.base.service.util.CarbonUploadServiceRequestUtil;
import org.wso2.developerstudio.eclipse.carbonserver44.Activator;
import org.wso2.developerstudio.eclipse.carbonserver44.operations.CommonOperations;
import org.wso2.developerstudio.eclipse.carbonserver44.util.CarbonServer44Utils;
import org.wso2.developerstudio.eclipse.carbonserver44.util.CarbonServerConstants;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.server.base.core.ServerController;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class CarbonServerBehavior44 extends CarbonServerBehaviour{

	private static final String EMPTY_STRING = "";
	private static IDeveloperStudioLog log = Logger.getLog(Activator.PLUGIN_ID);

	private void checkClosed(IModule[] module) throws CoreException {
    	for( int i=0; i < module.length; i++ ){
    		if( module[i] instanceof DeletedModule ){	
                IStatus status = new Status(IStatus.ERROR,CorePlugin.PLUGIN_ID,0, NLS.bind(GenericServerCoreMessages.canNotPublishDeletedModule,module[i].getName()),null);
                throw new CoreException(status);
    		}
    	}
    }
    public void publishModule(int kind, int deltaKind, IModule[] module,
            IProgressMonitor monitor) throws CoreException {
    	IModule m = module[module.length - 1];
    	if(deltaKind == ADDED){
    		checkClosed(module);
    		ServerController.getInstance().getServerManager().publishServiceModule(getServer().getId(), "",m.getName());
    	}else if(deltaKind == REMOVED){
    		ServerController.getInstance().getServerManager().unpublishServiceModule(getServer().getId(), "",m.getName());
        }else{
    		checkClosed(module);
//    		To add any new services
//    		ServerController.getInstance().getServerManager().publishServiceModule(getServer().getId(), "",m.getName());
    		ServerController.getInstance().getServerManager().hotUpdateServiceModule(getServer().getId(), "",m.getName());
    	}
        setModulePublishState( module, null );
    }
    private void setModulePublishState( IModule[] module, IStatus[] status ) throws CoreException {
        if( module==null )
            return;
        for( int i=0; i < module.length; i++)
        {
            if(status == null ||
                    status.length < i ||
                    status[i]==null || 
                    status[i].getSeverity() == IStatus.OK )
            {
            setModulePublishState(module, IServer.PUBLISH_STATE_NONE);    
            }
            else
            {
                if ( IStatus.ERROR == status[i].getSeverity() ){
                    setModulePublishState( module, IServer.PUBLISH_STATE_UNKNOWN );
                    throw new CoreException( status[i] );
                }
            }
        }
    }
    
    protected void doServerStartedTasks(){
    	super.doServerStartedTasks();
 		doBrowserPopup();
    }
    
    protected void doServerStoppedTasks(){
    }
    
    private void doBrowserPopup(){
    	Boolean popupBrowser = CarbonServer44Utils.isServerStartBrowserPopup(getServer());
    	if (popupBrowser!=null && popupBrowser){
    		CarbonUploadServiceRequestUtil uoloadServiceRequestUtil = CarbonUploadServiceRequestUtil.getInstance();
    		uoloadServiceRequestUtil.popupExternalBrowser(CommonOperations.getLocalServerPort(getServer())+ CarbonServer44Utils.getWebContextRoot(getServer()) + "/carbon");
    		//uoloadServiceRequestUtil.popupInternalBrowser(CommonOperations.getLocalServerPort(getServer())+"/carbon");
    	}
    		
    }
    
    protected String getVmArguments() {
    	String vmArguments = super.getVmArguments();
    	Boolean enableOSGIConsole=CarbonServer44Utils.isServerStartWithOSGiConsole(getServer());
//    	String serverLocalWorkspacePath = WSASServerManager.getServerLocalWorkspacePath(getServer());
//    	IPath serverHome = WSASServerManager.getServerHome(getServer());
//    	vmArguments=vmArguments+" -Dcarbon.home=\""+serverHome.toOSString()+"\"";
    	if (enableOSGIConsole!=null && enableOSGIConsole){
    		vmArguments=vmArguments+" -DosgiConsole";
    	}
        return vmArguments;
    }
    
    protected String[] getPingURLList()
    {
    	try {
    		setServerisStillStarting(true);
    		String url = "http://"+getServer().getHost();
    		List<String> urls=new ArrayList<String>();
    	  	ServerPort[] ports=getServerPorts(getServer());
        	ServerPort sp = null;
        	int port=0;
        	int offSet=0;
        	
    	    for(int i=0;i<ports.length;i++){
    	    	int j = CarbonServerConstants.portCaptions.indexOf(ports[i].getName());
    	    	if(j!=-1 && CarbonServerConstants.portIds.get(j).equals("carbon.http")){
    	    		sp=ports[i];
    	        	port = sp.getPort();
    	    	}else if(j!=-1 && CarbonServerConstants.portIds.get(j).equals("carbon.offset")){
    	    		sp=ports[i];
    	        	offSet = sp.getPort();
    	    	} 
    	    }
    	    
    	    String newUrl = url;
    	    if (port != 80){
    	    	newUrl = newUrl + ":" + (port+offSet); //$NON-NLS-1$
    	    }
    	    newUrl=newUrl + CarbonServer44Utils.getWebContextRoot(getServer()) + "/carbon";
    	    urls.add(newUrl);
    	    
    		return urls.toArray(new String[]{});
    	} catch (Exception e) {
    		Trace.trace(Trace.SEVERE, "Can't ping for server startup."); 
    	}  	
    	return null;
    }
    
    protected List getStartClasspath() {
    	List startClasspath = super.getStartClasspath();
        GenericServerRuntime runtime = getRuntimeDelegate();

        IVMInstall vmInstall = runtime.getVMInstall();
    	File jdkLib = new File(vmInstall.getInstallLocation(),"lib");
    	
    	if (jdkLib.exists() && jdkLib.isDirectory()) {
	        for (String cpath : jdkLib.list()) {
		        Path newCPath = new Path(new File(jdkLib, cpath).toString());
		        String fileExtension = newCPath.getFileExtension();
		        if (fileExtension != null && fileExtension.equalsIgnoreCase("jar"))
			        startClasspath.add(JavaRuntime.newArchiveRuntimeClasspathEntry(newCPath));
	        }
        }
		return startClasspath;
    }
    
	protected String getCarbonXmlFilePath() {
		IPath serverHome = CarbonServerManager.getServerHome(getServer());
    	return CarbonServer44Utils.getServerXmlPathFromLocalWorkspaceRepo(serverHome.toOSString());
	}
	
	protected String getCatelinaXmlFilePath() {
		IPath serverHome = CarbonServerManager.getServerHome(getServer());
    	return CarbonServer44Utils.getCatelinaXmlPathFromLocalWorkspaceRepo(serverHome.toOSString());
	}
	
	
	protected String getTransportXmlFilePath() {
		IPath serverHome = CarbonServerManager.getServerHome(getServer());
    	String transportsXmlPath = CarbonServer44Utils.getTransportsXmlPathFromLocalWorkspaceRepo(serverHome.toOSString());
		return transportsXmlPath;
	}
	protected String getAxis2FilePath() {
		return CarbonServer44Utils.getAxis2FilePath(getServer());
	}
	
	protected Integer[] getAllPortsServerWillUse(IServer server) {
		List<Integer> ports = new ArrayList<Integer>();

		String axis2FilePath = getAxis2FilePath();
		String carbonXmlPath = getCarbonXmlFilePath();
		String catelinaXmlFilePath = getCatelinaXmlFilePath();

		addServletTransportPorts(ports, carbonXmlPath, catelinaXmlFilePath);
		addAxis2XmlPorts(ports, axis2FilePath);

		return ports.toArray(new Integer[] {});
	}
	
	protected void addServletTransportPorts(List<Integer> ports,
			String carbonXmlPath, String catelinaXmlPath) {
		int port = 0;
		XPathFactory factory = XPathFactory.newInstance();
		NamespaceContext cntx = CarbonServer44Utils.getCarbonNamespace();
		File xmlDocument = new File(carbonXmlPath);
		File catelineXmlDocument = new File(catelinaXmlPath);
		try {
			InputSource inputSource = new InputSource(new FileInputStream(
					xmlDocument));
			InputSource catelineSource = new InputSource(new FileInputStream(
					catelineXmlDocument));
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			DocumentBuilder catelineBuilder = DocumentBuilderFactory
					.newInstance().newDocumentBuilder();
			Document document = builder.parse(xmlDocument);
			Document catelinaDocument = catelineBuilder
					.parse(catelineXmlDocument);
			XPath xPath = factory.newXPath();
			XPath catelineXPath = factory.newXPath();
			xPath.setNamespaceContext(cntx);

			int offSet = Integer.parseInt((String) xPath.evaluate(
					CarbonServerConstants.SERVER_PORTS_OFFSET_XPATH, document,
					XPathConstants.STRING));
			String evaluate = (String) catelineXPath
					.evaluate(
							CarbonServerConstants.CATALINA_XPATH_EXPRESSION_FOR_SSL_ENABLED_PORT,
							catelinaDocument, XPathConstants.STRING);

			if (!evaluate.equals(EMPTY_STRING)) {
				port = Integer.parseInt(evaluate) + offSet;
			} else {
				port = getPortfromTransportXML(CarbonServerConstants.HTTPS_NAME_TAG);
			}
			ports.add(port);
			inputSource = new InputSource(new FileInputStream(xmlDocument));
			evaluate = (String) catelineXPath.evaluate(
					CarbonServerConstants.SERVER_SERVICE_CONNECTOR_XPATH,
					catelinaDocument, XPathConstants.STRING);

			if (!evaluate.equals(EMPTY_STRING)) {
				port = Integer.parseInt(evaluate) + offSet;
			} else {
				port = getPortfromTransportXML(CarbonServerConstants.HTTP_NAME_TAG);
			}
			ports.add(port);

		} catch (NumberFormatException | XPathExpressionException
				| ParserConfigurationException | SAXException | IOException e) {
			log.warn("Error occured while adding server transport ports", e);
		}
	}

	private int getPortfromTransportXML(String protocolType) {
		int port = 0;
		String transportsXmlPath = getTransportXmlFilePath();
		XPathFactory factory = XPathFactory.newInstance();
		File xmlDocument = new File(transportsXmlPath);
		try {
			InputSource inputSource = new InputSource(new FileInputStream(
					xmlDocument));
			XPath xPath = factory.newXPath();
			XPathExpression xPathExpression = xPath
					.compile("/transports/transport[@name='" + protocolType
							+ "']/parameter[@name='port']");
			String evaluate = xPathExpression.evaluate(inputSource);
			port = Integer.parseInt(evaluate);
		} catch (NumberFormatException | FileNotFoundException
				| XPathExpressionException e) {
			log.warn("Error occured while getting port from transport XML", e);
		}
		return port;
	}
}
