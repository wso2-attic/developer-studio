/*******************************************************************************
 * Copyright (c) 2005, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.ui.dialogs;

import java.text.MessageFormat;

import org.eclipse.bpel.model.resource.BPELResourceSetImpl;
import org.eclipse.bpel.model.util.BPELUtils;
import org.eclipse.bpel.ui.BPELUIPlugin;
import org.eclipse.bpel.ui.IBPELUIConstants;
import org.eclipse.bpel.ui.Messages;
import org.eclipse.bpel.ui.details.providers.ModelLabelProvider;
import org.eclipse.bpel.ui.details.providers.ModelTreeLabelProvider;
import org.eclipse.bpel.ui.details.providers.PartnerLinkTypeTreeContentProvider;
import org.eclipse.bpel.ui.details.providers.VariableTypeTreeContentProvider;
import org.eclipse.bpel.ui.details.providers.WSILContentProvider;
import org.eclipse.bpel.ui.util.filedialog.FileSelectionGroup;
import org.eclipse.bpel.wsil.model.inspection.Description;
import org.eclipse.bpel.wsil.model.inspection.Inspection;
import org.eclipse.bpel.wsil.model.inspection.Link;
import org.eclipse.bpel.wsil.model.inspection.Name;
import org.eclipse.bpel.wsil.model.inspection.Service;
import org.eclipse.bpel.wsil.model.inspection.TypeOfAbstract;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.SelectionStatusDialog;
import org.eclipse.ui.part.DrillDownComposite;

/**
 * Browse for complex/simple types available in the process and choose that
 * simple type.
 * 
 */

public class SchemaImportDialog extends SelectionStatusDialog {

	/** Browse local resource */
	protected final static int BID_BROWSE_RESOURCE = IDialogConstants.CLIENT_ID + 1;

	/* Button id for browsing URLs */
	protected final static int BID_BROWSE_URL = IDialogConstants.CLIENT_ID + 2;

	/* Button id for browse files */
	protected final static int BID_BROWSE_FILE = IDialogConstants.CLIENT_ID + 3;

	/* Browse button */
	protected static final int BID_BROWSE = IDialogConstants.CLIENT_ID + 4;

	/* button id for browsing WSIL */
	protected static final int BID_BROWSE_WSIL = IDialogConstants.CLIENT_ID + 5;

	/* The import source setting, remembered in the dialog settings */
	private static final String IMPORT_KIND = "ImportKind"; //$NON-NLS-1$

	private static final String EMPTY = ""; //$NON-NLS-1$

	private String[] FILTER_EXTENSIONS = {
			IBPELUIConstants.EXTENSION_STAR_DOT_XSD,
			IBPELUIConstants.EXTENSION_STAR_DOT_WSDL,
			IBPELUIConstants.EXTENSION_STAR_DOT_ANY };

	private String[] FILTER_NAMES = { IBPELUIConstants.EXTENSION_XSD_NAME,
			IBPELUIConstants.EXTENSION_WSDL_NAME,
			IBPELUIConstants.EXTENSION_ANY_NAME };

	private String resourceFilter = IBPELUIConstants.EXTENSION_DOT_XSD;

	protected EObject modelObject;

	protected Tree fTree;
	protected TreeViewer fTreeViewer;

	Text fLocation;

	private Composite fLocationComposite;
	FileSelectionGroup fResourceComposite;

	// Import from WSIL constructs
	private Composite fWSILComposite;
	protected TreeViewer fWSILTreeViewer;
	protected Tree fWSILTree;
	protected Text filterText;
	String fFilter = ""; //$NON-NLS-1$

	Button fBrowseButton;

	private Group fGroup;

	private IDialogSettings fSettings;

	private int KIND = BID_BROWSE_RESOURCE;

	private String fStructureTitle;

	private ITreeContentProvider fTreeContentProvider;

	protected Object fInput;

	protected BPELResourceSetImpl fHackedResourceSet;

	protected String fResourceKind = IBPELUIConstants.EXTENSION_XSD;

	long fRunnableStart;
	URI fRunnableLoadURI;
	Job fLoaderJob;

	IPreferenceStore fPrefStore = BPELUIPlugin.INSTANCE.getPreferenceStore();
	String fBasePath = fPrefStore.getString(IBPELUIConstants.PREF_WSIL_URL);

	// The WSIL radio box is turned off if the WSIL document is not set in the
	// preferences.
	Button fBtnWSIL;

	/**
	 * Create a brand new shiny Schema Import Dialog.
	 * 
	 * @param parent
	 */
	public SchemaImportDialog(Shell parent) {

		super(parent);
		setStatusLineAboveButtons(true);
		int shellStyle = getShellStyle();
		setShellStyle(shellStyle | SWT.MAX | SWT.RESIZE);

		fSettings = BPELUIPlugin.INSTANCE.getDialogSettingsFor(this);

		try {
			KIND = fSettings.getInt(IMPORT_KIND);
		} catch (java.lang.NumberFormatException nfe) {
			KIND = BID_BROWSE_RESOURCE;
		}

		setDialogBoundsSettings(fSettings, getDialogBoundsStrategy());

		configureAsSchemaImport();
	}

	/**
	 * Create a brand new shiny Schema Import Dialog
	 * 
	 * @param parent
	 *            shell to use
	 * @param eObject
	 *            the model object to use as reference
	 */
	public SchemaImportDialog(Shell parent, EObject eObject) {
		this(parent);

		this.modelObject = eObject;
		setTitle(Messages.SchemaImportDialog_2);

		fHackedResourceSet = BPELUtils.slightlyHackedResourceSet(eObject);
	}

	/**
	 * 
	 * @see Dialog#createDialogArea(Composite)
	 * 
	 * @param parent
	 *            the parent composite to use
	 * @return the composite it created to be used in the dialog area.
	 */

	@Override
	public Control createDialogArea(Composite parent) {

		Composite contents = (Composite) super.createDialogArea(parent);

		createImportLocation(contents);
		createImportStructure(contents);

		buttonPressed(KIND, true);
		return contents;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case BID_BROWSE:
			FileDialog fileDialog = new FileDialog(getShell());
			fileDialog.setFilterExtensions(FILTER_EXTENSIONS);
			fileDialog.setFilterNames(FILTER_NAMES);
			String path = fileDialog.open();
			if (path == null) {
				return;
			}
			fLocation.setText(path);
			attemptLoad(path);
			break;

		case IDialogConstants.CANCEL_ID:
			if (fLoaderJob != null) {
				if (fLoaderJob.getState() == Job.RUNNING) {
					fLoaderJob.cancel();
				}
			}
			break;
		}

		super.buttonPressed(buttonId);
	}

	protected void buttonPressed(int id, boolean checked) {

		if (id == BID_BROWSE_FILE || id == BID_BROWSE_RESOURCE
				|| id == BID_BROWSE_URL || id == BID_BROWSE_WSIL) {
			if (checked == false) {
				return;
			}

			setVisibleControl(fResourceComposite, id == BID_BROWSE_RESOURCE);
			setVisibleControl(fLocationComposite, id == BID_BROWSE_URL
					|| id == BID_BROWSE_FILE);

			setVisibleControl(fWSILComposite, id == BID_BROWSE_WSIL);

			fBrowseButton.setEnabled((id == BID_BROWSE_FILE)
					|| (id == BID_BROWSE_WSIL));
			fLocation.setText(EMPTY);

			markEmptySelection();
			KIND = id;

			fSettings.put(IMPORT_KIND, KIND);

			fGroup.getParent().layout(true);
		}

	}

	protected void setVisibleControl(Control c, boolean b) {
		Object layoutData = c.getLayoutData();

		if (layoutData instanceof GridData) {
			GridData data = (GridData) layoutData;
			data.exclude = !b;
		}
		c.setVisible(b);
	}

	/**
	 * Create the dialog.
	 * 
	 */

	@Override
	public void create() {
		super.create();
		buttonPressed(KIND, true);
	}

	protected Button createRadioButton(Composite parent, String label, int id,
			boolean checked) {

		Button button = new Button(parent, SWT.RADIO);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData( Integer.valueOf( id ));
		button.setSelection(checked);

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Button b = (Button) event.widget;
				int bid = ((Integer) b.getData()).intValue();

				buttonPressed(bid, b.getSelection());
			}
		});

		return button;

	}

	protected void createImportLocation(Composite parent) {

		fGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
		fGroup.setText(Messages.SchemaImportDialog_4);
		GridLayout layout = new GridLayout(1, true);
		GridData data = new GridData();
		data.grabExcessVerticalSpace = false;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;

		fGroup.setLayout(layout);
		fGroup.setLayoutData(data);

		Composite container = new Composite(fGroup, SWT.NONE);

		layout = new GridLayout();
		layout.makeColumnsEqualWidth = true;
		layout.numColumns = 4;
		container.setLayout(layout);
		data = new GridData();
		data.grabExcessVerticalSpace = false;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.CENTER;
		container.setLayoutData(data);

		createRadioButton(container, Messages.SchemaImportDialog_5,
				BID_BROWSE_RESOURCE, KIND == BID_BROWSE_RESOURCE);
		createRadioButton(container, Messages.SchemaImportDialog_6,
				BID_BROWSE_FILE, KIND == BID_BROWSE_FILE);
		createRadioButton(container, Messages.SchemaImportDialog_7,
				BID_BROWSE_URL, KIND == BID_BROWSE_URL);

		// Add WSIL option
		fBtnWSIL = createRadioButton(container, Messages.SchemaImportDialog_15,
				BID_BROWSE_WSIL, KIND == BID_BROWSE_WSIL);

		// Create location variant
		fLocationComposite = new Composite(fGroup, SWT.NONE);

		layout = new GridLayout();
		layout.numColumns = 3;
		fLocationComposite.setLayout(layout);
		data = new GridData();
		data.grabExcessVerticalSpace = true;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		fLocationComposite.setLayoutData(data);

		Label location = new Label(fLocationComposite, SWT.NONE);
		location.setText(Messages.SchemaImportDialog_8);

		fLocation = new Text(fLocationComposite, SWT.BORDER);
		fLocation.setText(EMPTY);
		data = new GridData();
		data.grabExcessVerticalSpace = false;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		fLocation.setLayoutData(data);
		fLocation.addListener(SWT.FocusOut, new Listener() {

			public void handleEvent(Event event) {
				String loc = fLocation.getText();
				if (loc.length() > 0) {
					attemptLoad(loc);
				}
			}
		});
		fLocation.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent event) {
				if (event.keyCode == SWT.CR) {
					attemptLoad(fLocation.getText());
					event.doit = false;
				}
			}

			public void keyReleased(KeyEvent e) {
				return;
			}

		});

		fBrowseButton = createButton(fLocationComposite, BID_BROWSE,
				Messages.SchemaImportDialog_9, false);

		// End of location variant

		// Start Resource Variant
		fResourceComposite = new FileSelectionGroup(fGroup, new Listener() {
			public void handleEvent(Event event) {
				IResource resource = fResourceComposite.getSelectedResource();
				if (resource != null && resource.getType() == IResource.FILE) {
					// only attempt to load a resource which is not a container
					attemptLoad((IFile) resource);
					return;
				}
				markEmptySelection();
			}
		}, Messages.SchemaImportDialog_10, resourceFilter); //$NON-NLS-1$

		TreeViewer viewer = fResourceComposite.getTreeViewer();
		viewer.setAutoExpandLevel(2);

		// End resource variant

		// create WSIL UI widgets
		createWSILStructure(fGroup);

	}

	protected Object createWSILStructure(Composite parent) {
		
        fWSILComposite = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        fWSILComposite.setLayout(layout);
        
		GridData data = new GridData();        
        data.grabExcessVerticalSpace = true;
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.minimumHeight = 220;
        fWSILComposite.setLayoutData(data);
        
		Label location = new Label(fWSILComposite, SWT.NONE);
	    location.setText( Messages.SchemaImportDialog_16 );
	    
	    data = new GridData();
	    data.grabExcessHorizontalSpace = true;
	    data.horizontalAlignment = SWT.LEFT;
	    location.setLayoutData(data);
	    
	    filterText = new Text(fWSILComposite, SWT.BORDER);
	    data = new GridData(GridData.FILL_HORIZONTAL);
	    filterText.setLayoutData(data);
	    
    	filterText.addKeyListener(new KeyListener() {
    		
    		public void keyPressed(KeyEvent e) {
    			
    		}
    		
    		public void keyReleased(KeyEvent e) {
    			// set the value of the filter.
    			fFilter = filterText.getText().trim().toLowerCase();
    			    		
       			if (fFilter.length() > 0) {
       				/* for the time being, only filter 3 levels deep 
       				 * since link references within WSIL are rare at 
       				 * this time.  when adoption of WSIL directories
       				 * take off, this needs to be rehashed */ 
       				fWSILTreeViewer.expandToLevel(3);
       			}
       			fWSILTreeViewer.refresh();
       			e.doit = false;
			}	
    	});
	    
	    DrillDownComposite wsilTreeComposite = new DrillDownComposite(fWSILComposite, SWT.BORDER);
		
		layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = 0;
        wsilTreeComposite.setLayout(layout);
        
        data = new GridData();        
        data.grabExcessVerticalSpace = true;
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        wsilTreeComposite.setLayoutData(data);
	        
		//	Tree viewer for variable structure ...
		fWSILTree = new Tree(wsilTreeComposite, SWT.NONE );
		data = new GridData();        
        data.grabExcessVerticalSpace = true;
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.minimumHeight = 200;
        fWSILTree.setLayoutData(data);
		  		
		fWSILTreeViewer = new TreeViewer( fWSILTree );
		fWSILTreeViewer.setContentProvider( new WSILContentProvider() );
		fWSILTreeViewer.setLabelProvider( new ModelLabelProvider() );
		
		Object wsilDoc = attemptLoad(URI.createURI(fBasePath),IBPELUIConstants.EXTENSION_WSIL);
		fWSILTreeViewer.setInput ( 	wsilDoc ) ;
		if (wsilDoc == null || wsilDoc instanceof Throwable  ) {
			fBtnWSIL.setEnabled(false);
			// that's always available.
			// delete KIND = BID_BROWSE_RESOURCE; by Grid.Qian
			// because if not, the dialog always display the resource Control
			// regardless last time if user choose the resource button
				
			/*// that's always available.
			KIND = BID_BROWSE_RESOURCE;*/
		}
		
		
		// set default tree expansion to the 2nd level
		fWSILTreeViewer.expandToLevel(2);
		fWSILTreeViewer.addFilter(new TreeFilter());
		fWSILTreeViewer.setComparator(new WSILViewerComparator());

		wsilTreeComposite.setChildTree(fWSILTreeViewer);

		fWSILTreeViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						// TODO Auto-generated method stub
						IStructuredSelection sel = (IStructuredSelection) event
								.getSelection();
						if (sel.getFirstElement() instanceof Service) {
							Service serv = (Service) sel.getFirstElement();
							Description descr = serv.getDescription().get(0);
							attemptLoad(descr.getLocation());
						} else {
							markEmptySelection();
						}
					}
				});
		// end tree viewer for variable structure

		return fWSILComposite;
	}

	protected Object createImportStructure(Composite parent) {

		Label location = new Label(parent, SWT.NONE);
		location.setText(fStructureTitle);

		// Tree viewer for variable structure ...
		fTree = new Tree(parent, SWT.BORDER);

		VariableTypeTreeContentProvider variableContentProvider = new VariableTypeTreeContentProvider(
				true, true);
		fTreeViewer = new TreeViewer(fTree);
		fTreeViewer
				.setContentProvider(fTreeContentProvider != null ? fTreeContentProvider
						: variableContentProvider);
		fTreeViewer.setLabelProvider(new ModelTreeLabelProvider());
		fTreeViewer.setInput(null);
		fTreeViewer.setAutoExpandLevel(3);
		// end tree viewer for variable structure
		GridData data = new GridData();
		data.grabExcessVerticalSpace = true;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.minimumHeight = 200;
		fTree.setLayoutData(data);

		return fTree;
	}

	Object attemptLoad(URI uri, String kind) {

		Resource resource = null;
		try {
			resource = fHackedResourceSet.getResource(uri, true, kind);
		} catch (Throwable t) {
			// BPELUIPlugin.log(t);
			return t;
		}

		// Bugzilla 324164
		if (resource!=null && resource.getErrors().isEmpty() && resource.isLoaded()) {
			return resource.getContents().get(0);
		}
		return null;
	}
	
	Object  attemptLoad ( URI uri ) {
		return attemptLoad (uri, fResourceKind );
	}
	
	
	void attemptLoad ( IFile file ) {
		attemptLoad ( file.getFullPath().toString() );
	}
	
		
	void attemptLoad ( String path ) {
		
		if (fLoaderJob != null) {			
			if (fLoaderJob.getState() == Job.RUNNING) {
				fLoaderJob.cancel();
			}			
		}
		
		updateStatus ( Status.OK_STATUS );		

		// empty paths are ignored
		if (path.length() == 0) {
			return ;
		}
		

		URI uri = convertToURI ( path );
		if (uri == null) {
			return ;
		}
		
		
		if (uri.isRelative()) {
			// construct absolute path	          
			String absolutepath = fBasePath.substring(0, fBasePath.lastIndexOf('/')+1) + path;
			uri = URI.createURI(absolutepath);
		}
				
		
		
		fRunnableLoadURI = uri;		
		final String msg = MessageFormat.format(Messages.SchemaImportDialog_17,fRunnableLoadURI);		 	    
		fLoaderJob = new Job(msg) {

			@Override
			protected IStatus run (IProgressMonitor monitor) {
				monitor.beginTask(msg, 1);				
				// Bug 290090 - move this to asyncExec() as below because the method will
				// modify UI element, if not, will have a invalid access error.

				/* fInput = attemptLoad(fRunnableLoadURI); */

				monitor.worked(1);
				if (fBrowseButton != null
						&& fBrowseButton.isDisposed() == false) {
					fBrowseButton.getDisplay().asyncExec(new Runnable() {
						public void run() {
							fInput = attemptLoad(fRunnableLoadURI);
							loadDone();
						}
					});
				}
				
				return Status.OK_STATUS;
			}			 		
		 };	
		 
		 fLoaderJob.schedule();		 
		 fRunnableStart = System.currentTimeMillis();

		 
		 updateStatus ( new Status(IStatus.INFO, BPELUIPlugin.INSTANCE.getID(),0,msg,null));
	}

	
	 
	@SuppressWarnings("boxing")
	void loadDone () {				
		
		long elapsed = System.currentTimeMillis() - fRunnableStart;
		
		if (fInput == null || fInput instanceof Throwable) {
			markEmptySelection();
			
			updateStatus( new Status(IStatus.ERROR,BPELUIPlugin.INSTANCE.getID(),0,
					MessageFormat.format(Messages.SchemaImportDialog_19,fRunnableLoadURI,elapsed),(Throwable) fInput) );
			fInput = null;
			
		} else {
			
			updateStatus ( new Status(IStatus.INFO, BPELUIPlugin.INSTANCE.getID(),0,
					MessageFormat.format(Messages.SchemaImportDialog_18,fRunnableLoadURI,elapsed),null)) ;
				
			fTreeViewer.setInput(fInput);				
			fTree.getVerticalBar().setSelection(0);			
		}
	}
	
	
	
	void markEmptySelection () {
		updateStatus ( Status.OK_STATUS );
		updateOK(false);
		fTreeViewer.setInput(null);
	}
	
	
	private URI convertToURI (String path ) {
		
		try {
			switch (KIND) {
			case BID_BROWSE_FILE : 
				return URI.createFileURI( path );				
			
			case BID_BROWSE_RESOURCE :
				return URI.createPlatformResourceURI(path);				
			
			case BID_BROWSE_WSIL :
				//return URI.createFileURI( path );
			case BID_BROWSE_URL :
				return URI.createURI(path);
				

				
			default :
				return null;
			}
			
		} catch (Exception ex) {
			updateStatus ( new Status(IStatus.ERROR,BPELUIPlugin.INSTANCE.getID(),0,Messages.SchemaImportDialog_13,ex) );			
			return null;
		}
	}

	/**
	 * Update the state of the OK button to the state indicated.
	 * 
	 * @param state
	 *            false to disable, true to enable.
	 */

	public void updateOK(boolean state) {
		Button okButton = getOkButton();
		if (okButton != null && !okButton.isDisposed()) {
			okButton.setEnabled(state);
		}
	}

	/**
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#computeResult()
	 */

	@Override
	protected void computeResult() {
		Object object = fTreeViewer.getInput();
		if (object == null) {
			return;
		}
		setSelectionResult(new Object[] { object });
	}

	/**
	 * Configure the dialog as a schema import dialog. Set the title and the
	 * structure pane message.
	 * 
	 */

	public void configureAsSchemaImport() {
		setTitle(Messages.SchemaImportDialog_2);
		fStructureTitle = Messages.SchemaImportDialog_11;
		fResourceKind = IBPELUIConstants.EXTENSION_XSD;
	}

	/**
	 * Configure the dialog as a WSDL import dialog. Set the title and the
	 * structure pane message.
	 * 
	 */

	public void configureAsWSDLImport() {

		setTitle(Messages.SchemaImportDialog_0);
		fStructureTitle = Messages.SchemaImportDialog_14;
		fTreeContentProvider = new PartnerLinkTypeTreeContentProvider(true);
		fResourceKind = IBPELUIConstants.EXTENSION_WSDL;

		String[] wsdl_FILTER_EXTENSIONS = {
				IBPELUIConstants.EXTENSION_STAR_DOT_WSDL,
				IBPELUIConstants.EXTENSION_STAR_DOT_XSD,
				IBPELUIConstants.EXTENSION_STAR_DOT_ANY };
		FILTER_EXTENSIONS = wsdl_FILTER_EXTENSIONS;
		String[] wsdl_FILTER_NAMES = { IBPELUIConstants.EXTENSION_WSDL_NAME,
				IBPELUIConstants.EXTENSION_XSD_NAME,
				IBPELUIConstants.EXTENSION_ANY_NAME };
		FILTER_NAMES = wsdl_FILTER_NAMES;

		resourceFilter = IBPELUIConstants.EXTENSION_DOT_WSDL;

	}

	/**
	 * 
	 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
	 * @date May 4, 2007
	 * 
	 */
	public class TreeFilter extends ViewerFilter {

		/**
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {

			if (fFilter == null || fFilter.length() == 0) {
				return true;
			}

			if (element instanceof Service) {
				String text = ""; //$NON-NLS-1$
				Service service = (Service) element;
				if (service.getName().size() > 0) {
					Name name = service.getName().get(0);
					text += name.getValue();
				}
				if (service.getAbstract().size() > 0) {
					TypeOfAbstract abst = service.getAbstract().get(0);
					text += abst.getValue();
				}
				return (text.toLowerCase().indexOf(fFilter) > -1);
			}

			return true;
		}
	}

	/**
	 * 
	 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
	 * @date May 10, 2007
	 * 
	 */
	public class WSILViewerComparator extends ViewerComparator {

		/**
		 * @see org.eclipse.jface.viewers.ViewerComparator#category(java.lang.Object)
		 */
		@Override
		public int category(Object element) {
			if (element instanceof Inspection)
				return 1;
			if (element instanceof Link)
				return 2;
			if (element instanceof Service)
				return 3;

			return 0;
		}
	}
}
