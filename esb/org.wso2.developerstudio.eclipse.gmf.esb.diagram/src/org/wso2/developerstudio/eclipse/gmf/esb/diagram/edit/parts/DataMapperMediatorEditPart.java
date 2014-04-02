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

package org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.parts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.BorderItemSelectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemLocator;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.gmf.tooling.runtime.edit.policies.reparent.CreationEditPolicyWithCustomReparent;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbFactory;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage;
import org.wso2.developerstudio.eclipse.gmf.esb.RegistryKeyProperty;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.EsbGraphicalShapeWithLabel;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.FixedBorderItemLocator;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.FixedSizedAbstractMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.OpenSeparatelyEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.provider.DeveloperStudioElementProviderDialog;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.policies.DataMapperMediatorCanonicalEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.policies.DataMapperMediatorItemSemanticEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.part.EsbVisualIDRegistry;
import org.wso2.developerstudio.eclipse.gmf.esb.impl.DataMapperMediatorImpl;
import org.wso2.developerstudio.eclipse.greg.core.interfaces.IRegistryFile;
import org.wso2.developerstudio.eclipse.platform.core.utils.CSProviderConstants;
import org.wso2.developerstudio.eclipse.platform.ui.startup.DataMapperEditor;

/**
 * @generated NOT
 */
public class DataMapperMediatorEditPart extends FixedSizedAbstractMediator {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 3761;

	/**
	 * @generated
	 */
	protected IFigure contentPane;

	/**
	 * @generated
	 */
	public DataMapperMediatorEditPart(View view) {
		super(view);
	}

	/**
	 * @generated NOT
	 */
	protected void createDefaultEditPolicies() {
		installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicyWithCustomReparent(
				EsbVisualIDRegistry.TYPED_INSTANCE));
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new DataMapperMediatorItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new DataMapperMediatorCanonicalEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
		
		// For handle Double click Event.
		installEditPolicy(EditPolicyRoles.OPEN_ROLE, new OpenSeparatelyEditPolicy());
		// XXX need an SCR to runtime to have another abstract superclass that would let children add reasonable editpolicies
		// removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CONNECTION_HANDLES_ROLE);
	}

	/**
	 * @generated NOT
	 */
	protected LayoutEditPolicy createLayoutEditPolicy() {
		org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy lep = new org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy() {

			protected EditPolicy createChildEditPolicy(EditPart child) {
				View childView = (View) child.getModel();
				switch (EsbVisualIDRegistry.getVisualID(childView)) {
				case DataMapperMediatorInputConnectorEditPart.VISUAL_ID:
				case DataMapperMediatorOutputConnectorEditPart.VISUAL_ID:
					return new BorderItemSelectionEditPolicy();
				}
				EditPolicy result = child.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
				if (result == null) {
					result = new NonResizableEditPolicy();
				}
				return result;
			}

			protected Command getMoveChildrenCommand(Request request) {
				return null;
			}

			protected Command getCreateCommand(CreateRequest request) {
				return null;
			}
		};
		return lep;
	}

	/**
	 * @generated NOT
	 */
	protected IFigure createNodeShape() {
		return primaryShape = new DataMapperMediatorFigure() {
			public void setBounds(org.eclipse.draw2d.geometry.Rectangle rect) {
				super.setBounds(rect);
				if (this.getBounds().getLocation().x != 0 && this.getBounds().getLocation().y != 0) {
					connectToMostSuitableElement();
					reAllocate(rect);
				}
			};
		};
	}

	/**
	 * @generated
	 */
	public DataMapperMediatorFigure getPrimaryShape() {
		return (DataMapperMediatorFigure) primaryShape;
	}

	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof DataMapperMediatorDescriptionEditPart) {
			((DataMapperMediatorDescriptionEditPart) childEditPart).setLabel(getPrimaryShape()
					.getDataMapperDescriptionLabel());
			return true;
		}
		if (childEditPart instanceof DataMapperMediatorInputConnectorEditPart) {
			IFigure borderItemFigure = ((DataMapperMediatorInputConnectorEditPart) childEditPart)
					.getFigure();
			BorderItemLocator locator = new FixedBorderItemLocator(getMainFigure(),
					borderItemFigure, PositionConstants.WEST, 0.5);
			getBorderedFigure().getBorderItemContainer().add(borderItemFigure, locator);
			return true;
		}
		if (childEditPart instanceof DataMapperMediatorOutputConnectorEditPart) {
			IFigure borderItemFigure = ((DataMapperMediatorOutputConnectorEditPart) childEditPart)
					.getFigure();
			BorderItemLocator locator = new FixedBorderItemLocator(getMainFigure(),
					borderItemFigure, PositionConstants.EAST, 0.5);
			getBorderedFigure().getBorderItemContainer().add(borderItemFigure, locator);
			return true;
		}
		return false;
	}

	protected boolean removeFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof LogMediatorDescriptionEditPart) {
			return true;
		}
		if (childEditPart instanceof DataMapperMediatorInputConnectorEditPart) {
			getBorderedFigure().getBorderItemContainer().remove(
					((DataMapperMediatorInputConnectorEditPart) childEditPart).getFigure());
			return true;
		}
		if (childEditPart instanceof DataMapperMediatorOutputConnectorEditPart) {
			getBorderedFigure().getBorderItemContainer().remove(
					((DataMapperMediatorOutputConnectorEditPart) childEditPart).getFigure());
			return true;
		}
		return false;
	}

	protected void addChildVisual(EditPart childEditPart, int index) {
		if (addFixedChild(childEditPart)) {
			return;
		}
		super.addChildVisual(childEditPart, -1);
	}

	protected void removeChildVisual(EditPart childEditPart) {
		if (removeFixedChild(childEditPart)) {
			return;
		}
		super.removeChildVisual(childEditPart);
	}

	protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
		if (editPart instanceof IBorderItemEditPart) {
			return getBorderedFigure().getBorderItemContainer();
		}
		return getContentPane();
	}

	protected NodeFigure createMainFigure() {
		NodeFigure figure = createNodePlate();
		figure.setLayoutManager(new ToolbarLayout(true));
		IFigure shape = createNodeShape();
		figure.add(shape);
		contentPane = setupContentPane(shape);
		return figure;
	}

	/**
	 * Default implementation treats passed figure as content pane.
	 * Respects layout one may have set for generated figure.
	 * @param nodeShape instance of generated figure class
	 * @generated
	 */
	protected IFigure setupContentPane(IFigure nodeShape) {
		if (nodeShape.getLayoutManager() == null) {
			ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
			layout.setSpacing(5);
			nodeShape.setLayoutManager(layout);
		}
		return nodeShape; // use nodeShape itself as contentPane
	}

	/**
	 * @generated
	 */
	public IFigure getContentPane() {
		if (contentPane != null) {
			return contentPane;
		}
		return super.getContentPane();
	}

	/**
	 * @generated
	 */
	protected void setForegroundColor(Color color) {
		if (primaryShape != null) {
			primaryShape.setForegroundColor(color);
		}
	}

	/**
	 * @generated
	 */
	protected void setBackgroundColor(Color color) {
		if (primaryShape != null) {
			primaryShape.setBackgroundColor(color);
		}
	}

	/**
	 * @generated
	 */
	protected void setLineWidth(int width) {
		if (primaryShape instanceof Shape) {
			((Shape) primaryShape).setLineWidth(width);
		}
	}

	/**
	 * @generated
	 */
	protected void setLineType(int style) {
		if (primaryShape instanceof Shape) {
			((Shape) primaryShape).setLineStyle(style);
		}
	}

	/**
	 * @generated NOT
	 */
	public class DataMapperMediatorFigure extends EsbGraphicalShapeWithLabel {

		/**
		 * @generated
		 */
		private WrappingLabel fFigureDataMapperMediatorDescriptionFigure;

		private WrappingLabel fFigureDataMapperDescriptionLabel;

		/**
		 * @generated
		 */
		public DataMapperMediatorFigure() {

			this.setBackgroundColor(THIS_BACK);
			createContents();
		}

		/**
		 * @generated NOT
		 */
		private void createContents() {
			fFigureDataMapperDescriptionLabel = getPropertyNameLabel();
		}

		/**
		 * @generated
		 */
		public WrappingLabel getFigureDataMapperMediatorDescriptionFigure() {
			return fFigureDataMapperMediatorDescriptionFigure;
		}

		public WrappingLabel getDataMapperDescriptionLabel() {
			return fFigureDataMapperDescriptionLabel;
		}

		public String getIconPath() {
			return "icons/ico20/conditionalRouter-mediator.gif";
		}

		public String getNodeName() {
			return "DataMapper";
		}

		public IFigure getToolTip() {
			return new Label("Data Mapping");
		}

	}

	/**
	 * @generated
	 */
	static final Color THIS_BACK = new Color(null, 230, 230, 230);
	
	public void openDataMapperDiagram() {
		
		NodeImpl eobject = ((NodeImpl)this.getModel());
		final DataMapperMediatorImpl datamapper = (DataMapperMediatorImpl)eobject.getElement();


		if (datamapper.getConfiguration().getKeyValue().isEmpty()) {
			
			Shell shell = new Shell(Display.getDefault());
			final RegistryKeyProperty registryKeyProperty = EsbFactory.eINSTANCE.createRegistryKeyProperty();
			
			Map<String, List<String>> filters = new HashMap<String, List<String>>();
			
			String mediaTypeKey = CSProviderConstants.FILTER_MEDIA_TYPE;
			List<String> types = new ArrayList<String>();
			types.add("application/javascript"); //FIXME we need to give this our mediatype like vnd.wso2.esb.datamapper
			filters.put(mediaTypeKey, types);

			//final DeveloperStudioElementProviderDialog dialog = null;
			//boolean noExistingResources = true;
			//dialog = new CreateNewConfigurationDialog(shell,filters);
			//dialog.create();
			//dialog.getShell().setText("New Datamapper Configuration");
			
			final DeveloperStudioElementProviderDialog dialog = new DeveloperStudioElementProviderDialog(shell, new Class[]{IRegistryFile.class}, filters);
			dialog.configureDialog("Specify DataMapper Configuration.", false, true);
			

			if (dialog.open() == Dialog.OK) {
				String selectedPath = formatRegistryPath(dialog.getSelectedPath());
				registryKeyProperty.setKeyValue(selectedPath);
				
				Display.getDefault().asyncExec(new Runnable() {

				public void run() {
						
						TransactionalEditingDomain editingDomain = getEditingDomain();
					
						SetCommand setCmd = new SetCommand(editingDomain, datamapper, EsbPackage.Literals.DATA_MAPPER_MEDIATOR__CONFIGURATION, registryKeyProperty);

						if (setCmd.canExecute()) {
							getEditingDomain().getCommandStack().execute(setCmd);
						}
						
						String localPath = dialog.getIPathOfSelection();
						
						SetCommand setCmd2 = new SetCommand(editingDomain, datamapper, EsbPackage.Literals.DATA_MAPPER_MEDIATOR__CONFIGURATION_LOCAL_PATH, localPath);
						if (setCmd2.canExecute()) {
							editingDomain.getCommandStack().execute(setCmd2);
						}
						
						openDataMapperEditor(datamapper);

					}
				});
			}

		} else {
			openDataMapperEditor(datamapper);
		}
 
	}

	private String formatRegistryPath(String selectedPath) {
		final String G_REG_PATH_PREFIX = "/_system/governance/";
		final String C_REG_PATH_PREFIX = "/_system/config/";
		String formattedPath = selectedPath;
		if (selectedPath.startsWith(G_REG_PATH_PREFIX)) {
			formattedPath = String.format("gov:%s", selectedPath.substring(G_REG_PATH_PREFIX.length()));
		} else if (selectedPath.startsWith(C_REG_PATH_PREFIX)) {
			formattedPath = String.format("conf:%s", selectedPath.substring(C_REG_PATH_PREFIX.length()));
		}
		return formattedPath;
	}

	private void openDataMapperEditor(final DataMapperMediatorImpl datamapper) {
		if (!datamapper.getConfiguration().getKeyValue().isEmpty()) {
			
			String localPath = datamapper.getConfigurationLocalPath();
			if (localPath != null && !localPath.isEmpty()) {
				Path path = new Path(localPath);
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				DataMapperEditor.open(file);
			}
		}
	}

}
