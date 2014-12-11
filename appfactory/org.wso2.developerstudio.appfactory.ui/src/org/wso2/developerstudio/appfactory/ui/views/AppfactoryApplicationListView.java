/*
 * Copyright (c) 2013, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.developerstudio.appfactory.ui.views;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.wso2.developerstudio.appfactory.core.authentication.Authenticator;
import org.wso2.developerstudio.appfactory.core.authentication.UserPasswordCredentials;
import org.wso2.developerstudio.appfactory.core.client.HttpsJaggeryClient;
import org.wso2.developerstudio.appfactory.core.client.HttpsJenkinsClient;
import org.wso2.developerstudio.appfactory.core.jag.api.JagApiProperties;
import org.wso2.developerstudio.appfactory.core.model.AppListModel;
import org.wso2.developerstudio.appfactory.core.model.AppVersionGroup;
import org.wso2.developerstudio.appfactory.core.model.AppVersionInfo;
import org.wso2.developerstudio.appfactory.core.model.ApplicationInfo;
import org.wso2.developerstudio.appfactory.core.repository.JgitRepoManager;
import org.wso2.developerstudio.appfactory.ui.Activator;
import org.wso2.developerstudio.appfactory.ui.actions.LoginAction;
import org.wso2.developerstudio.appfactory.ui.utils.Messages;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.platform.core.utils.SWTResourceManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class AppfactoryApplicationListView extends ViewPart {
	
	public static final String ID = "org.wso2.developerstudio.appfactory.ui.views.AppfactoryView"; //$NON-NLS-1$
	public static final String REPO_WIZARD_ID = "org.eclipse.egit.ui.internal.clone.GitCloneWizard"; //$NON-NLS-1$
	public static final String FORKED_REPO_SUFFIX = "_forked";
	public static final String MAIN_REPO_SUFFIX = "_main";
	private static final String MAVEN_CMD_INSTALL = "install";
	private static final String MAVEN_CMD_CLEAN = "clean";
	private static final String MAVEN_CMD_ECLIPSE = "eclipse:eclipse";
	private static final String PROJECT_DESCRIPTOR = ".project";
	

	private static IDeveloperStudioLog log=Logger.getLog(Activator.PLUGIN_ID);
	
	private static AppfactoryApplicationDetailsView appDetailView;
	
	@Inject UISynchronize uISync;
	
	private TreeViewer viewer;
	//private Composite parent; 
	private AppListModel model;
	private AppListLabelProvider labelProvider;
	private AppListContentProvider contentProvider;
	private UserPasswordCredentials credentials;
	private List<ApplicationInfo> appLists;
	private MenuManager menuMgr;
	private IEventBroker broker;
	private EventHandler buildhandler;
	private EventHandler ErrorLoghandler;
	private EventHandler infoLoghandler;
	private EventHandler apphandler;
	private EventHandler appVersionhandler;
	private EventHandler projectOpenhandler;
	private AppfactoryConsoleView console;
	private MessageConsoleStream infoOut;
	private MessageConsoleStream errorOut;
	private MessageConsoleStream buildOut;
	private IToolBarManager toolBarmgr;
	@SuppressWarnings("restriction")
	@Override
	public void init(IViewSite site) throws PartInitException {

		super.init(site);
		appLists = new ArrayList<ApplicationInfo>();
		console = new AppfactoryConsoleView();
		infoOut = console.getOut();
		errorOut = console.getNewMsgStream();
		buildOut = console.getNewMsgStream();
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		 
		IEclipseContext eclipseContext = EclipseContextFactory
				.getServiceContext(bundle.getBundleContext());
		eclipseContext.set(org.eclipse.e4.core.services.log.Logger.class, null);
		broker = eclipseContext.get(IEventBroker.class);
		doSubscribe();/*Subscribe UIhandler with EventBroker*/
		Authenticator.getInstance().setLoaded(true);
	}

	@Override
	public void dispose() {
	   	doUnSubscribe();
	   	Authenticator.getInstance().setLoaded(false);
		super.dispose();
	}
	
	

	public void createPartControl(Composite parent) {
      //  this.parent = parent;
        contentProvider = new AppListContentProvider(appLists);
        labelProvider = new AppListLabelProvider();
        createToolbar();
        model =new AppListModel();
		  viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		  viewer.setContentProvider(contentProvider);
		  viewer.setLabelProvider(labelProvider);
		  viewer.setInput(model);
		  viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
			try {
					final IStructuredSelection selection = (IStructuredSelection) viewer
							.getSelection();
					Object selectedNode = selection.getFirstElement();
					ApplicationInfo appInfo = null;
					
					if (selectedNode instanceof ApplicationInfo) {
						appInfo = (ApplicationInfo) selection
								.getFirstElement();						
					}
					else if(selectedNode instanceof AppVersionGroup)					
					{
						appInfo = ((AppVersionGroup)selection.getFirstElement()).getApplication();
					}
					else if(selectedNode instanceof AppVersionInfo)					
					{
						appInfo = ((AppVersionInfo)selection.getFirstElement()).getVersionGroup().getApplication();
					}
					
					if (!appInfo.getappVersionList().isEmpty()) {
						appDetailView.updateView(appInfo);
					}else{
						appDetailView.clear();
					}
				} catch (Throwable e) {
				  /*safe to ignore*/
				} 
			}
			 
		});
		  viewer.addDoubleClickListener(new IDoubleClickListener() {

		      @Override
		      public void doubleClick(DoubleClickEvent event) {
				try {
					TreeViewer viewer = (TreeViewer) event.getViewer();
					IStructuredSelection thisSelection = (IStructuredSelection) event
							.getSelection();
					Object selectedNode = thisSelection.getFirstElement();
					if (selectedNode instanceof AppVersionInfo) {
						viewer.setExpandedState(selectedNode,
								!viewer.getExpandedState(selectedNode));
					}else if (selectedNode instanceof AppVersionGroup) {
						viewer.setExpandedState(selectedNode,
								!viewer.getExpandedState(selectedNode));
					}
				} catch (Throwable e) {
					  /*safe to ignore*/
				 } 
		      }
		    });
		    
		    menuMgr = new MenuManager();
	        Menu menu = menuMgr.createContextMenu(viewer.getControl());
	        menu.setVisible(true);
	        menuMgr.addMenuListener(new IMenuListener() {
	            @Override
	            public void menuAboutToShow(final IMenuManager manager) {
	                try {
						if (viewer.getSelection().isEmpty()) {
						    return;
						}

						if (viewer.getSelection() instanceof IStructuredSelection) {
						    IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
						    if(selection.getFirstElement() instanceof AppVersionInfo){
						       final AppVersionInfo appVersionInfo = (AppVersionInfo) selection.getFirstElement();
						        
						        manager.add(checkOutAndImportAction(appVersionInfo));
						        manager.add(importAction(appVersionInfo));
						    	manager.add(checkOutAction(appVersionInfo));
						    	manager.add(repoDeployAction(appVersionInfo));
						    	manager.add(buildInfoAction(appVersionInfo)); 
						    	
						    	
						    	
						    }else if (selection.getFirstElement() instanceof ApplicationInfo){
						    	ApplicationInfo appInfo = (ApplicationInfo) selection.getFirstElement();
						    	String title =""; //$NON-NLS-1$
						    	
						        if(appInfo.getappVersionList().isEmpty()){
						        	title = "Open  "; //$NON-NLS-1$
						        }else{
						        	title = "Update"; //$NON-NLS-1$
						        }
						        
						      
						    	manager.add(appOpenAction(appInfo,title));
						        manager.add(repoSettingsAction(appInfo));
						    }else if (selection.getFirstElement() instanceof AppVersionGroup){
						    	
						    //	AppVersionGroup group = (AppVersionGroup) selection.getFirstElement();
						    	// TODO     	
						    }
						}
					} catch (Throwable e) {
						 /*safe to ignore*/
					}
	            }

			
	        });
	        menuMgr.setRemoveAllWhenShown(true);
	        viewer.getControl().setMenu(menu);
	        updateApplicationView();
	}

	public static AppfactoryApplicationDetailsView getAppDetailView() {
	    return appDetailView;
    }

	public static void setAppDetailView(AppfactoryApplicationDetailsView appDetailView) {
	    AppfactoryApplicationListView.appDetailView = appDetailView;
    }
	
	@Override
	public void setFocus() {
	
	}
	

	
	private void doSubscribe() {
		 
		buildhandler = getBuildLogEventHandler();
		broker.subscribe("update", buildhandler); //$NON-NLS-1$
			
		apphandler = getAppListHandler();
		broker.subscribe("Appupdate", apphandler); //$NON-NLS-1$
 
		appVersionhandler = getAppVersionEventHandler();
		broker.subscribe("Appversionupdate", appVersionhandler); //$NON-NLS-1$
		
		ErrorLoghandler = getErrorLogEventHandler();
		broker.subscribe("Errorupdate",ErrorLoghandler); //$NON-NLS-1$
		
		infoLoghandler = getInfoLogEventHandler();
		broker.subscribe("Infoupdate",infoLoghandler); //$NON-NLS-1$
		
		projectOpenhandler = getPorjectcheckedOUtHandler();
		broker.subscribe("Projectupdate",projectOpenhandler); //$NON-NLS-1$
	}
	
	private void doUnSubscribe() {
		broker.unsubscribe(buildhandler); //$NON-NLS-1$
		broker.unsubscribe(apphandler); //$NON-NLS-1$
		broker.unsubscribe(appVersionhandler); //$NON-NLS-1$
		broker.unsubscribe(ErrorLoghandler); //$NON-NLS-1$
		broker.unsubscribe(infoLoghandler); //$NON-NLS-1$
		broker.unsubscribe(projectOpenhandler); //$NON-NLS-1$
	}
	private void printErrorLog(String msg){
		 broker.send("Errorupdate", "\n"+"["+new Timestamp(new Date().getTime()) +"] : "+msg); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
	
	private void printInfoLog(String msg){
		 broker.send("Infoupdate", "\n"+"["+new Timestamp(new Date().getTime()) +"] : "+msg); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	private EventHandler getAppListHandler() {
		return new EventHandler() {
			public void handleEvent(final Event event) {
 
				Display.getDefault().asyncExec(new Runnable() {
					@SuppressWarnings({ "unchecked" })
					@Override
					public void run() {
            			List<ApplicationInfo> oldappLists = appLists;
						appLists =  (List<ApplicationInfo>) event.getProperty(IEventBroker.DATA);
            			contentProvider.inputChanged(viewer, oldappLists, appLists);
            			viewer.refresh();
            		 
					}
				});
			}
		};
	}
	
	private EventHandler getPorjectcheckedOUtHandler() {
		return new EventHandler() {
			public void handleEvent(final Event event) {
 
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						try {
							contentProvider.inputChanged(viewer, model, model);
							viewer.refresh();				
						} catch (Exception e) {
							 log.error("checkedoutError", e); //$NON-NLS-1$
						}

					}
				});
			}
		};
	}

	private EventHandler getAppVersionEventHandler() {
		return new EventHandler() {
			public void handleEvent(final Event event) {
 
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						AppListModel refModel = (AppListModel) event.getProperty(IEventBroker.DATA);
						contentProvider.inputChanged(viewer, model, refModel);
						viewer.refresh();
					}
				});
			}
		};
	}
	
	private EventHandler getBuildLogEventHandler() {
		return new EventHandler() {
			public void handleEvent(final Event event) {
				@SuppressWarnings("unused")
				final Display display = Display.getDefault();
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						buildOut.setColor(SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN));
						buildOut.println(""+event.getProperty(IEventBroker.DATA)); //$NON-NLS-1$
					}
				});
			}
		};
	}
	
	private EventHandler getErrorLogEventHandler() {
		return new EventHandler() {
			public void handleEvent(final Event event) {
				
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						errorOut.setColor(SWTResourceManager.getColor(SWT.COLOR_RED));
						errorOut.println("\n\n**********[ERROR]**********" + event.getProperty(IEventBroker.DATA)); //$NON-NLS-1$
					}
				});
			}
		};
	}
	
	private EventHandler getInfoLogEventHandler() {
		return new EventHandler() {
			public void handleEvent(final Event event) {
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						infoOut.setColor(SWTResourceManager.getColor(SWT.COLOR_BLACK));
						infoOut.println("\n\n**********[INFO]**********" + event.getProperty(IEventBroker.DATA)); //$NON-NLS-1$
					}
				});
			}
		};
	}
	
	private void ShowLoginDialog(){
		 credentials = Authenticator.getInstance().getCredentials();
		 try{
		 if(credentials==null){
			 printErrorLog(Messages.AppfactoryApplicationListView_ShowLoginDialog_plog_msg_1);
			 LoginAction loginAction = new LoginAction();
			 printInfoLog(Messages.AppfactoryApplicationListView_ShowLoginDialog_plog_msg_2);
			 if(loginAction.login(false,false)){
				 printInfoLog(Messages.AppfactoryApplicationListView_ShowLoginDialog_plog_msg_3);
				 credentials = Authenticator.getInstance().getCredentials();
			 }
		 } 
		 }catch(Exception e){
			 /*safe to ignore*/
		 } 
	}
	
	private List<ApplicationInfo> getApplist(){
		 if(Authenticator.getInstance().getCredentials()!=null){
		 Map<String,String> params = new HashMap<String,String>();
		 params.put("action",JagApiProperties.USER_APP_LIST__ACTION); //$NON-NLS-1$
		 params.put("userName",Authenticator.getInstance().getCredentials().getUser());  //$NON-NLS-1$
		 printInfoLog(Messages.AppfactoryApplicationListView_getApplist_plog_msg_001);
		 String respond = HttpsJaggeryClient.httpPost(JagApiProperties.getAppInfoUrl(), params);
		 if("false".equals(respond)){ //$NON-NLS-1$
		  printErrorLog(Messages.AppfactoryApplicationListView_getApplist_plog_msg_01);	 
	      boolean val = Authenticator.getInstance().reLogin();
	      if(val){
	       printInfoLog(Messages.AppfactoryApplicationListView_getApplist_plog_msg_0);	  
	       respond = HttpsJaggeryClient.httpPost(JagApiProperties.getAppInfoUrl(), params);
	      }else{
	    	printErrorLog(Messages.AppfactoryApplicationListView_getApplist_plog_msg_1 +
	    			Messages.AppfactoryApplicationListView_getApplist_plog_msg_2);  
	      }
		 }
		 if(!"false".equals(respond)){ //$NON-NLS-1$
	     printInfoLog(Messages.AppfactoryApplicationListView_getApplist_plog_msg_3);	 
		 Gson gson = new Gson();
		 Type collectionType = new TypeToken<java.util.List<ApplicationInfo>>(){}.getType();
		 return gson.fromJson(respond, collectionType);
		 }
		}
		 return new ArrayList<ApplicationInfo>();
	}
 
    private void createToolbar() {
    	toolBarmgr = getViewSite().getActionBars().getToolBarManager();
             
    	toolBarmgr.add(new Action() {
              	 @Override
              	public void run() {
        			 LoginAction loginAction;
					try {
						 loginAction = new LoginAction();
	        			 if(loginAction.login(false,true)){
	        				 printInfoLog(Messages.AppfactoryApplicationListView_ShowLoginDialog_plog_msg_3);
	        				 credentials = Authenticator.getInstance().getCredentials();
	        				 updateApplicationView();
	        				 setText(credentials.getUser());
	        			 }
					} catch (Exception e) {
						 /*safe to ignore*/
					}
              	}
              	 
              	 public String getText() {
       				return "Re-login";
       			}
              	 
             	@Override
    			public ImageDescriptor getImageDescriptor() {
    				ImageDescriptor imageDescriptorFromPlugin = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
    						 "/icons/users.gif"); //$NON-NLS-1$
    				return  imageDescriptorFromPlugin;
    			}
             	
             	@Override
             	public String getToolTipText() {
             		    if(Authenticator.getInstance().getCredentials()!=null){
             		return Authenticator.getInstance().getCredentials().getUser();
             		    }else{
             		    	return "Login";
             		    }
             	}
             	
             	
   			});
    	toolBarmgr.add(new Action() {
           	 @Override
           	public void run() {
           		updateApplicationView();
           	}
 
           	public ImageDescriptor getImageDescriptor() {
				ImageDescriptor imageDescriptorFromPlugin = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						 "/icons/refresh.gif"); //$NON-NLS-1$
				return  imageDescriptorFromPlugin;
			}
         	
         	@Override
         	public String getToolTipText() {
         		return Messages.AppfactoryApplicationListView_createToolbar_refresh_menu;
         	}
           	 
           	 
			});
            
            
    }
	
	private void  updateApplicationView(){
		Job job = new Job(Messages.AppfactoryApplicationListView_updateApplicationView_job_title) {
		   @Override
			  protected IStatus run(final IProgressMonitor monitor) {
				monitor.beginTask(Messages.AppfactoryApplicationListView_updateApplicationView_monitor_text_0, 100);
				  UISynchronize uiSynchronize = new UISynchronize() {
						@Override
						public void syncExec(Runnable runnable) {
							
						}
						
						@Override
						public void asyncExec(Runnable runnable) {
							   monitor.subTask(Messages.AppfactoryApplicationListView_updateApplicationView_monitor_text_1);
							   monitor.worked(10);
							   List<ApplicationInfo> applist = getApplist();
							   monitor.worked(40);
							   if(applist!=null){
								monitor.subTask(Messages.AppfactoryApplicationListView_updateApplicationView_monitor_text_2);
								monitor.worked(60);	   
							   broker.send("Appupdate", applist); //$NON-NLS-1$
							   monitor.worked(90);	
							   }else{
							     monitor.subTask(Messages.AppfactoryApplicationListView_updateApplicationView_monitor_text_3);
							     monitor.worked(30);
								 monitor.worked(0);	      
							   }
						}
					};
					uiSynchronize.asyncExec(new Runnable() {
						@Override
						public void run() {
						
						}
					});
					
			    return Status.OK_STATUS;
			  }
			};
		job.schedule();
	}
	
	
	private void updateUI(final ApplicationInfo appInfo){
		
		Display.getDefault().asyncExec(new Runnable() {
		      public void run() {
		         viewer.refresh();
		         appDetailView.updateView(appInfo);
		      }
		});
			
	}
	
	private void  getAppVersions(final ApplicationInfo appInfo){
		
		Job job = new Job(Messages.AppfactoryApplicationListView_getAppVersions_job_title) {
			  @Override
			  protected IStatus run(final IProgressMonitor monitor) {
				monitor.beginTask(Messages.AppfactoryApplicationListView_getAppVersions_monitor_text_1, 100);

				appInfo.setLableState(1);
				broker.send("Appversionupdate", model); //$NON-NLS-1$
				if(getVersionInfo(appInfo, monitor)){
					getForkedVersionsInfo(appInfo, monitor);
					getTeamInfo(appInfo, monitor);
					//getDbInfo(appInfo, monitor);/*currently not supporting*/
					getDSInfo(appInfo, monitor);
					appInfo.setLableState(2);
					
				}else{
					appInfo.setLableState(0);
				}			
				updateUI(appInfo);
				
			    return Status.OK_STATUS;
			  }
			  
			  private boolean getVersionInfo(
						final ApplicationInfo appInfo,
						final IProgressMonitor monitor) {
					monitor.subTask(Messages.AppfactoryApplicationListView_getVersionInfo_monitor_text_2);
					monitor.worked(20);	   
					boolean result = model.setversionInfo(appInfo);
					if(!result){
						boolean reLogin = Authenticator.getInstance().reLogin();
						if(reLogin){
							result = model.setversionInfo(appInfo);
						}
					}
					if(result){
					monitor.subTask(Messages.AppfactoryApplicationListView_getVersionInfo_monitor_text_3);
					monitor.worked(30);	 
					broker.send("Appversionupdate", model); //$NON-NLS-1$
					monitor.worked(32);
					return true;
					}else{

						return false;
					}
				}
				
				private boolean getForkedVersionsInfo(
						final ApplicationInfo appInfo,
						final IProgressMonitor monitor) {
					monitor.subTask(Messages.AppfactoryApplicationListView_getForkedAppVersions_monitor_text_1);
					monitor.worked(38);	   
					boolean result = model.setForkedRepoInfo(appInfo);
					if(!result){
						boolean reLogin = Authenticator.getInstance().reLogin();
						if(reLogin){
							result = model.setForkedRepoInfo(appInfo);
						}
					}
					if(result){
					monitor.subTask(Messages.AppfactoryApplicationListView_getVersionInfo_monitor_text_3);
					monitor.worked(42);	 
					broker.send("Appversionupdate", model); //$NON-NLS-1$
					monitor.worked(50);
					return true;
					}else{

						return false;
					}
				}
				
				private boolean getTeamInfo(final ApplicationInfo appInfo,
						final IProgressMonitor monitor) {
					monitor.subTask(Messages.AppfactoryApplicationListView_getTeamInfo_monitor_text_1);
					monitor.worked(60);	   
					boolean result = model.setRoleInfomation(appInfo);
					if(!result){
						boolean reLogin = Authenticator.getInstance().reLogin();
						if(reLogin){
							result = model.setRoleInfomation(appInfo);
						}
					}
					if(result){
					monitor.subTask(Messages.AppfactoryApplicationListView_getTeamInfo_monitor_text_2);
					monitor.worked(60);	 
					broker.send("Appversionupdate", model); //$NON-NLS-1$
					monitor.worked(90);
					return true;
					}else{
						return false;
					}
				}
				/*Currently not supporting*/
				@SuppressWarnings("unused")
				private boolean getDbInfo(final ApplicationInfo appInfo,
						final IProgressMonitor monitor) {
					monitor.subTask(Messages.AppfactoryApplicationListView_getDbInfo_monitor_text_1);
					monitor.worked(60);	   
					boolean result = model.setDBInfomation(appInfo);
					if(!result){
						boolean reLogin = Authenticator.getInstance().reLogin();
						if(reLogin){
							result = model.setDBInfomation(appInfo);
						}
					}
					if(result){
					monitor.subTask(Messages.AppfactoryApplicationListView_getDbInfo_monitor_text_2);
					monitor.worked(60);	 
					broker.send("Appversionupdate", model); //$NON-NLS-1$
					monitor.worked(90);
					return true;
					}else{
						return false;
					}
				}
				
				private boolean getDSInfo(final ApplicationInfo appInfo,
						final IProgressMonitor monitor) {
					monitor.subTask(Messages.AppfactoryApplicationListView_getDSInfo_monitor_text_2);
					monitor.worked(60);	   
					boolean result = model.setDSInfomation(appInfo);
					if(!result){
						boolean reLogin = Authenticator.getInstance().reLogin();
						if(reLogin){
							result = model.setDSInfomation(appInfo);
						}
					}
					if(result){
					monitor.subTask(Messages.AppfactoryApplicationListView_getDSInfo_monitor_text_3);
					monitor.worked(60);	 
					broker.send("Appversionupdate", model); //$NON-NLS-1$
					monitor.worked(90);
					return true;
					}else{
						return false;
					}
				}
				
			};
		job.schedule();
	}
 
	private void getbuildLogsJob(final AppVersionInfo appInfo,final boolean deploy) {
		 
		Job job = new Job(Messages.AppfactoryApplicationListView_getbuildLogsJob_title) {
		 
			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				UISynchronize uiSynchronize = new UISynchronize() {

					@Override
					public void syncExec(Runnable runnable) {

					}

					@Override
					public void asyncExec(Runnable runnable) {
						try {
							/*Getting last build ID*/
						    int buildId = getLastBuildId(appInfo);
						    if(deploy){
						     int newbuildId = deploy(appInfo, buildId);
						     if(newbuildId>buildId){
						    	 while(true){
						    		 buildId = getLastBuildId(appInfo); 
						    		 if(buildId>=newbuildId){
						    			 break;
						    		 }
						    		 printErrorLog(Messages.AppfactoryApplicationListView_getbuildLogsJob_plog_1+newbuildId+Messages.AppfactoryApplicationListView_getbuildLogsJob_plog_2 +
												Messages.AppfactoryApplicationListView_getbuildLogsJob_plog_3);	 
										Thread.sleep(2000); 
										if(monitor.isCanceled()){
											printInfoLog(Messages.AppfactoryApplicationListView_getbuildLogsJob_pInfo_1);	  
											break;
										} 
						    	 }
						     }
						    }
							/*Getting Jenkins BuilderUrl by providing the buildId*/
							String builderBaseUrl = getBuilderUrl(appInfo,buildId);
							/*Getting jenkins log using above url*/
							printJenkinsBuildLogs(appInfo, buildId,builderBaseUrl,monitor);

						} catch (Exception e) {
							printErrorLog(Messages.AppfactoryApplicationListView_getbuildLogsJob_pError_1+e.getMessage());	 
							log.error("BuildLogs Error :",e); //$NON-NLS-1$
						}

					}

					private int deploy(final AppVersionInfo appInfo, int buildId) {
						Map<String, String> params = new HashMap<String, String>();
						params.put("action", //$NON-NLS-1$
								JagApiProperties.App_BUILD_ACTION);
						params.put("stage", "Development"); //$NON-NLS-1$ //$NON-NLS-2$
						params.put("applicationKey", appInfo.getAppName()); //$NON-NLS-1$
						params.put("version", appInfo.getVersion()); //$NON-NLS-1$
						params.put("doDeploy", "true"); //$NON-NLS-1$ //$NON-NLS-2$
						printInfoLog("Deploying application");	 //$NON-NLS-1$
						String httpPostrespond = HttpsJaggeryClient.httpPost(
								JagApiProperties.getBuildApplication(),
								params); 
						if(!"false".equals(httpPostrespond)){ //$NON-NLS-1$
							printInfoLog(Messages.AppfactoryApplicationListView_deploy_printInfoLog_2);
							buildId++;
						}else{
							printErrorLog(Messages.AppfactoryApplicationListView_deploy_printErrorLog_3);
							printInfoLog(Messages.AppfactoryApplicationListView_deploy_printInfoLog_3);
						}

						return buildId;
					}

					private String getBuilderUrl(final AppVersionInfo appInfo,
							int buidNo) {
						Map<String, String> params;
						params = new HashMap<String, String>();
						params.put("action",JagApiProperties.App_BUILD_URL_ACTIONL); //$NON-NLS-1$
						params.put("lastBuildNo", "" + buidNo); //$NON-NLS-1$ //$NON-NLS-2$
						params.put("applicationVersion",appInfo.getVersion()); //$NON-NLS-1$
						params.put("applicationKey",appInfo.getAppName()); //$NON-NLS-1$
						params.put("userName",Authenticator.getInstance().getCredentials().getUser()); //$NON-NLS-1$
						String builderBaseUrl = ""; //$NON-NLS-1$
						builderBaseUrl = HttpsJaggeryClient.httpPost(JagApiProperties.getBuildInfoUrl(),params);
						return builderBaseUrl;
					}

					private void printJenkinsBuildLogs(final AppVersionInfo appInfo, int buidNo,
							String builderBaseUrl,IProgressMonitor monitor) throws IOException,InterruptedException {
						printInfoLog(Messages.AppfactoryApplicationListView_printJenkinsBuildLogs_printInfoLog_0+buidNo);	  
						while(true){
						HttpResponse response = HttpsJenkinsClient
								.getBulildinfo(
										appInfo.getAppName(),
										appInfo.getVersion(),
										builderBaseUrl, buidNo);
						if(response.getStatusLine().getStatusCode()==200){
						HttpEntity entity = response.getEntity();
						BufferedReader rd = new BufferedReader(
								new InputStreamReader(response
										.getEntity().getContent()));
						String line;
						while ((line = rd.readLine()) != null) {
							broker.send("update", line.toString()); //$NON-NLS-1$
							Thread.sleep(100);
						}
						EntityUtils.consume(entity);
						break;
						}else if(response.getStatusLine().getStatusCode()==404){
							printErrorLog(Messages.AppfactoryApplicationListView_printJenkinsBuildLogs_printInfoLog_1 +
									Messages.AppfactoryApplicationListView_printJenkinsBuildLogs_printInfoLog_2);	 
							Thread.sleep(2000); 
							if(monitor.isCanceled()){
								printInfoLog(Messages.AppfactoryApplicationListView_printJenkinsBuildLogs_printInfoLog_3);	  
								break;
							}
						}else{
							printErrorLog(Messages.AppfactoryApplicationListView_printJenkinsBuildLogs_printErrorLog_3+response.getStatusLine().getStatusCode());
							 break;
						}
					}	
				}

					private int getLastBuildId(final AppVersionInfo appInfo) {
						credentials = Authenticator.getInstance().getCredentials();
						Map<String, String> params = new HashMap<String, String>();
						params.put("action",JagApiProperties.App_BUILD_INFO_ACTION); //$NON-NLS-1$
						params.put("stage", "Development"); //$NON-NLS-1$ //$NON-NLS-2$
						params.put("applicationKey",appInfo.getAppName()); //$NON-NLS-1$
						params.put("version", appInfo.getVersion()); //$NON-NLS-1$
						params.put("buildable", "true"); //$NON-NLS-1$ //$NON-NLS-2$
						params.put("isRoleBasedPermissionAllowed","false"); //$NON-NLS-1$ //$NON-NLS-2$
						params.put("metaDataNeed", "false"); //$NON-NLS-1$ //$NON-NLS-2$
						params.put("userName",credentials.getUser()); //$NON-NLS-1$
						printInfoLog(Messages.AppfactoryApplicationListView_getLastBuildId_printInfoLog_0);	 
						String respond = HttpsJaggeryClient.httpPost(JagApiProperties.getBuildLastSucessfullBuildUrl(),params);
						if("false".equals(respond)){ //$NON-NLS-1$
						printErrorLog(Messages.AppfactoryApplicationListView_getLastBuildId_printErrorLog_0);	 
					    boolean val = Authenticator.getInstance().reLogin();
					      if(val){
					    	  printInfoLog(Messages.AppfactoryApplicationListView_getLastBuildId_printInfoLog_1);	  
					    	  respond =HttpsJaggeryClient.httpPost(JagApiProperties.getBuildLastSucessfullBuildUrl(),params);
					      }else{
					    	printErrorLog(Messages.AppfactoryApplicationListView_getLastBuildId_printErrorLog_1 +
					    			Messages.AppfactoryApplicationListView_getLastBuildId_printErrorLog_2);  
					      }
						}
						if(!"false".equals(respond)){ //$NON-NLS-1$
							JsonElement jelement = new JsonParser().parse(respond);
							JsonArray buildInfoArray;
							int buildId = 0;
								buildInfoArray = jelement.getAsJsonArray();
								for (JsonElement jsonElement : buildInfoArray) 
								{
									JsonObject asJsonObject = jsonElement
											.getAsJsonObject();
									JsonElement jsonElement2 = asJsonObject
											.get("version"); //$NON-NLS-1$
									JsonObject asJsonObject2 = jsonElement2
											.getAsJsonObject();
									String asString = asJsonObject2.get("current") //$NON-NLS-1$
											.getAsString();
									if (asString.equals(appInfo.getVersion())) {
										JsonElement jsonElement3 = asJsonObject
												.get("build"); //$NON-NLS-1$
										JsonObject asJsonObject3 = jsonElement3
												.getAsJsonObject();
										buildId = asJsonObject3.get("lastBuildId") //$NON-NLS-1$
												.getAsInt();
										break;
									}
								}
								printInfoLog(Messages.AppfactoryApplicationListView_getLastBuildId_plog_Lastbuild+buildId);
								return buildId;	
						}
						return 0;
					}
				
			   };
				uiSynchronize.asyncExec(new Runnable() {
					@Override
					public void run() {

					}
				});

				return Status.OK_STATUS;
			}

		};
			
		job.schedule();
	}
	
	private Action appOpenAction(final ApplicationInfo appInfo,final String title) {
		Action reposettings = new Action() {
			public void run() {
				try {
					getAppVersions(appInfo);
				} catch (Exception e) {
					appInfo.setLableState(0);
					log.error(e);
				}
			}
			public String getText() {
				return title;
			}

			public ImageDescriptor getImageDescriptor() {
				ImageDescriptor imageDescriptorFromPlugin = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						 "/icons/open.gif"); //$NON-NLS-1$
				return  imageDescriptorFromPlugin;
			}
		};
		return reposettings;
	}
 
	private Action buildInfoAction(final AppVersionInfo appInfo) {

		Action reposetetings = new Action() {
			@Override
			public void run() {
				getbuildLogsJob(appInfo,false);
			}

			public String getText() {
				return Messages.AppfactoryApplicationListView_buildInfoAction_BuildLogs_menu_name;
			}
			public ImageDescriptor getImageDescriptor() {
				ImageDescriptor imageDescriptorFromPlugin = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						 "/icons/buildLog.gif"); //$NON-NLS-1$
				return  imageDescriptorFromPlugin;
			}
		};

		return reposetetings;
	}	
	
	private Action repoSettingsAction(final ApplicationInfo appInfo) {
		Action reposettings = new Action() {
			public void run() {
				try {
					    DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent()
								.getActiveShell());
					   dialog.setText(Messages.AppfactoryApplicationListView_repoSettingsAction_DirectoryDialog_title);
					    if(dialog.open()!=null){
					    	appInfo.setLocalrepoLocation(dialog.getFilterPath());
					    	appInfo.updateVersions();
					    }
				} catch (Exception e) {
					log.error("", e); //$NON-NLS-1$
				}
			};
       
			public String getText() {
				return Messages.AppfactoryApplicationListView_repoSettingsAction_changeRepoLocation_menu_name;
			}
			@Override
			public ImageDescriptor getImageDescriptor() {
				ImageDescriptor imageDescriptorFromPlugin = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						 "/icons/repoLocation.gif"); //$NON-NLS-1$
				return  imageDescriptorFromPlugin;
			}
		};
		return reposettings;
	} 
 
	private Action repoDeployAction(final AppVersionInfo info) {
		Action reposettings = new Action() {
			public void run() {
				try {
					getbuildLogsJob(info,true);
				} catch (Exception e) {
					log.error("Deploying Error", e); //$NON-NLS-1$
				}
			};
			public String getText() {
				return Messages.AppfactoryApplicationListView_repoDeployAction_menu_name;
			}
			@Override
			public ImageDescriptor getImageDescriptor() {
				ImageDescriptor imageDescriptorFromPlugin = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						 "/icons/deploy.gif"); //$NON-NLS-1$
				return  imageDescriptorFromPlugin;
			}
		};
		return reposettings;
	}
	
	private Action checkOutAction(final AppVersionInfo info) {
		Action reposettings = new Action() {
			public void run() {				
				getcheckoutJob(info);
			};

			public String getText() {
				return Messages.AppfactoryApplicationListView_checkOutAction_menu_name;
			}
			@Override
			public ImageDescriptor getImageDescriptor() {
				ImageDescriptor imageDescriptorFromPlugin = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						 "/icons/checkout.gif"); //$NON-NLS-1$
				return  imageDescriptorFromPlugin;
			}
		};
		return reposettings;
	}
	
	private Action importAction(final AppVersionInfo info) {
		Action reposettings = new Action() {
			public void run() {	
			    ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
				progressMonitorDialog.create();
				progressMonitorDialog.open();
				try {
					progressMonitorDialog.run(true, false, new AppImportJobJob(info));
				} catch (InvocationTargetException e) {
					 log.error("project open", e); //$NON-NLS-1$
				} catch (InterruptedException e) {
					log.error("project open", e); //$NON-NLS-1$
				}
			};

			public String getText() {
				return Messages.AppfactoryApplicationListView_importAction_menu_name;
			}
			@Override
			public ImageDescriptor getImageDescriptor() {
				ImageDescriptor imageDescriptorFromPlugin = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						 "/icons/import.gif"); //$NON-NLS-1$
				return  imageDescriptorFromPlugin;
			}
			
			@Override
			public boolean isEnabled() {
				// TODO Auto-generated method stub
				return info.isCheckedout();
			}
		};
		return reposettings;
	}
	
	private Action checkOutAndImportAction(final AppVersionInfo info) {
		Action reposettings = new Action() {
			public void run() {		
				try {
			    ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
				progressMonitorDialog.create();
				progressMonitorDialog.open();
				progressMonitorDialog.run(true, true, new AppCheckoutAndImportJobJob(info));
				} catch (InvocationTargetException e) {
					 log.error("project open", e); //$NON-NLS-1$
					 
				} catch (InterruptedException e) {
					log.error("project open", e); //$NON-NLS-1$
					printErrorLog(e.getMessage());
				}
				
			};

			public String getText() {
				return Messages.AppfactoryApplicationListView_checkOutAndImportAction_menu_name;
			}
			@Override
			public ImageDescriptor getImageDescriptor() {
				ImageDescriptor imageDescriptorFromPlugin = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
						 "/icons/checkout.gif"); //$NON-NLS-1$
				return  imageDescriptorFromPlugin;
			}
		};
		return reposettings;
	}
	
	private void getcheckoutJob(final AppVersionInfo info){
		Job job = new Job(Messages.AppfactoryApplicationListView_getcheckoutJob_title) {
		   @Override
			  protected IStatus run(final IProgressMonitor monitor) {
				monitor.beginTask(Messages.AppfactoryApplicationListView_getcheckoutJob_monitor_msg_1, 100);
				  UISynchronize uiSynchronize = new UISynchronize() {
						@Override
						public void syncExec(Runnable runnable) {
						}
						
						@Override
						public void asyncExec(Runnable runnable) {
						 try{
							 checkout(info, monitor);
							 }catch(Exception e){
								 monitor.worked(0);
								 monitor.subTask(Messages.AppfactoryApplicationListView_getcheckoutJob_monitor_msg_2);
								 printErrorLog(Messages.AppfactoryApplicationListView_getcheckoutJob_plog_msg_2+e.getMessage()); 
								 log.error("Cloning :", e); //$NON-NLS-1$
						   }
						}
					};
					uiSynchronize.asyncExec(new Runnable() {
						@Override
						public void run() {
						
						}
					});
					
			    return Status.OK_STATUS;
			  }
			};
		job.schedule();
	}	
	
	private void checkout(final AppVersionInfo info,
			final IProgressMonitor monitor)
			throws IOException, InvalidRemoteException,
			TransportException, GitAPIException,
			RefAlreadyExistsException,
			RefNotFoundException, InvalidRefNameException,
			CheckoutConflictException {
		monitor.subTask(Messages.AppfactoryApplicationListView_checkout_moniter_msg_1);
		printInfoLog(Messages.AppfactoryApplicationListView_checkout_plog_msg_1);
		monitor.worked(5);	 
		String localRepo = "";
		if (info.getLocalRepo() == null || info.getLocalRepo().equals("")) { //$NON-NLS-1$
			String workspace = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
			localRepo = workspace + File.separator + info.getAppName();
			info.setLocalRepo(localRepo);
		}
		
		monitor.worked(10);	
		JgitRepoManager manager = new JgitRepoManager(localRepo,info.getRepoURL());
		
		if(!manager.isCloned()){
			manager.gitClone();
			if(!"trunk".equals(info.getVersion())){	    //$NON-NLS-1$
					manager.checkout(info.getVersion());
					monitor.worked(15);
					monitor.subTask(Messages.AppfactoryApplicationListView_checkout_moniter_msg_2);
					printInfoLog(Messages.AppfactoryApplicationListView_checkout_plog_msg_2);
				}
		}else {
			manager.checkout(info.getVersion());
			monitor.worked(15);
			monitor.subTask(Messages.AppfactoryApplicationListView_checkout_moniter_msg_3);
			printInfoLog(Messages.AppfactoryApplicationListView_checkout_plog_msg_3);
		}
         info.setCheckedout(true);
         broker.send("Projectupdate", null); //$NON-NLS-1$
	}

	private class AppImportJobJob implements IRunnableWithProgress {
		
	AppVersionInfo appInfo;
	 public AppImportJobJob(AppVersionInfo appInfo) {
		    this.appInfo = appInfo;
    	}
	  
		@Override
		public void run(IProgressMonitor monitor) {
			String operationText = Messages.AppfactoryApplicationListView_AppImportJob_opMSG_1;
			monitor.beginTask(operationText, 100);
			try {
				operationText = Messages.AppfactoryApplicationListView_AppImportJob_opMSG_2;
				monitor.subTask(operationText);
				monitor.worked(10);

				File pomFile = new File(appInfo.getLocalRepo() + File.separator + "pom.xml");

				if (pomFile.exists()) {
					executeMavenCommands(pomFile, monitor);
				}

				IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(new Path(
				                                                                                  appInfo.getLocalRepo() +
				                                                                                          File.separator +
				                                                                                          PROJECT_DESCRIPTOR));

				operationText = Messages.AppfactoryApplicationListView_AppImportJob_opMSG_3;
				monitor.subTask(operationText);
				monitor.worked(10);

				final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
				if (!project.exists()) {
					project.create(monitor);
					project.open(monitor);
				}
				ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, monitor);
				monitor.worked(80);

			} catch (Throwable e) {
				operationText = Messages.AppfactoryApplicationListView_AppImportJob_opMSG_4;
				monitor.subTask(operationText);
				monitor.worked(10);
				log.error("importing failed", e); //$NON-NLS-1$
			}

			monitor.worked(100);
			monitor.done();
		}
	}  

	private class AppCheckoutAndImportJobJob implements IRunnableWithProgress {
	
	AppVersionInfo appInfo;
	 public AppCheckoutAndImportJobJob(AppVersionInfo appInfo) {
		    this.appInfo = appInfo;
    	}
	  
		@Override
		public void run(IProgressMonitor monitor) {
			String operationText=Messages.AppfactoryApplicationListView_AppCheckoutAndImportJob_opMSG_1;
			monitor.beginTask(operationText, 100);
			try {
				checkout(appInfo, monitor);
				operationText =
				                Messages.AppfactoryApplicationListView_AppCheckoutAndImportJob_opMSG_2;
				monitor.subTask(operationText);
				monitor.worked(5);

				IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(new Path(
				                                                                                  appInfo.getLocalRepo() +
				                                                                                          File.separator +
				                                                                                          PROJECT_DESCRIPTOR)); //$NON-NLS-1$

				operationText = Messages.AppfactoryApplicationListView_AppCheckoutAndImportJob_opMSG_3;
				monitor.subTask(operationText);
				monitor.worked(5);

				final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
				if (!project.exists()) {
					project.create(new SubProgressMonitor(monitor, 10));
					project.open(new SubProgressMonitor(monitor, 10));
				}
				ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE,
				                                                      new SubProgressMonitor(monitor, 10));

				File pomFile = new File(appInfo.getLocalRepo() + File.separator + "pom.xml");

				if (monitor.isCanceled()) {
					throw new InterruptedException(Messages.ImportingCancelled_Error);
				}

				if (pomFile.exists()) {
					executeMavenCommands(pomFile, monitor);
				}

			} catch(OperationCanceledException e) {
				
				 printErrorLog(e.getMessage());
				 log.error("importing failed", e); //$NON-NLS-1$
			
			}catch(Throwable e){
				 operationText=Messages.AppfactoryApplicationListView_AppCheckoutAndImportJob_Faild;
				 monitor.subTask(operationText);
				 monitor.worked(60); 
				 printErrorLog(e.getMessage());
				 log.error("importing failed", e); //$NON-NLS-1$
			}
			monitor.done();
		}
	}  
	
	public boolean executeMavenCommands(File pomFile, IProgressMonitor monitor) throws InterruptedException{
		
		monitor.worked(10);
		
		try {
			String operationText = Messages.AppfactoryApplicationListView_executeMavenCommands_text;
			monitor.subTask(operationText);
			printInfoLog(operationText);

			InvocationResult result = mavenInstall(pomFile, monitor);

			if (result.getExitCode() != 0) {

				printErrorLog(Messages.AppfactoryApplicationListView_executeMavenCommands_errorlog_text);
			}

			monitor.worked(30);

		} catch (MavenInvocationException e) {

		}

		if (monitor.isCanceled()) {
			throw new InterruptedException(Messages.ImportingCancelled_Error);
		}

		try {

			String operationText = Messages.AppfactoryApplicationListView_executeMavenCommands_text2;
			monitor.subTask(operationText);
			printInfoLog(operationText);

			InvocationResult result = mavenEclipse(pomFile, monitor);

			if (result.getExitCode() != 0) {

				printErrorLog(Messages.AppfactoryApplicationListView_executeMavenCommands_errorlog_text2);
			}
			monitor.worked(20);

		} catch (MavenInvocationException e) {
			monitor.worked(50);
		}
		
		return true;
	}
	
	private InvocationResult mavenInstall(File pomFile, IProgressMonitor monitor) throws MavenInvocationException{
		
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile( pomFile );		
		request.setGoals(Collections.singletonList( MAVEN_CMD_INSTALL) );
		Invoker invoker = new DefaultInvoker();		
		InvocationResult result =  invoker.execute( request );
		
		request.setGoals(Collections.singletonList( MAVEN_CMD_CLEAN ));		
		invoker.execute(request);
		
		return result;
	}
	
	private static InvocationResult mavenEclipse(File pomFile, IProgressMonitor monitor) throws MavenInvocationException{
		
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile( pomFile );
		request.setGoals( Collections.singletonList( MAVEN_CMD_ECLIPSE ) );
		Invoker invoker = new DefaultInvoker();
		
		return invoker.execute( request );
	}

}
