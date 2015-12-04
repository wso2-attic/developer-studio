/*
 * Copyright (c) 2014-2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import static org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.parts.EditPartConstants.CLONE_MEDIATOR_ICON_PATH;
import static org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.parts.EditPartConstants.DEFAULT_PROPERTY_VALUE_TEXT;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
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
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemLocator;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;
import org.wso2.developerstudio.eclipse.gmf.esb.CloneMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.CloneMediatorGraphicalShape;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.FixedBorderItemLocator;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.MultipleCompartmentComplexFiguredAbstractMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.ShowPropertyViewEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.editpolicy.FeedbackIndicateDragDropEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.utils.CloneMediatorUtils;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.policies.CloneMediatorCanonicalEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.policies.CloneMediatorItemSemanticEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.part.EsbVisualIDRegistry;

/**
 * @generated NOT
 */
public class CloneMediatorEditPart extends MultipleCompartmentComplexFiguredAbstractMediator {

	private int activeCount = 0;

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 3517;

	/**
	 * @generated
	 */
	protected IFigure contentPane;

	private List<IFigure> outputConnectors = new ArrayList<IFigure>();
	private List<BorderItemLocator> outputLocators = new ArrayList<BorderItemLocator>();
	private boolean reorderdOnUndo = false;

	public ArrayList<IFigure> targetOutputConnectors = new ArrayList<IFigure>();

	/**
	 * @generated
	 */
	public CloneMediatorEditPart(View view) {
		super(view);
	}

	public void activate() {
		CloneMediatorUtils.addTargetInitially(this, getEditingDomain());
		super.activate();
		//This method is called twice at the startup. To avoid that we use this 'if' check.
		if (activeCount == 1 && !reversed) {
			CloneMediatorUtils.reorderWhenForward(this);
		}
		++activeCount;
	}

	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		// Fixing TOOLS-1839.
		if (notification.getEventType() == Notification.SET && activeCount == 1 && !reorderdOnUndo && !reversed) {
			EObject parentContainer = ((org.eclipse.gmf.runtime.notation.impl.NodeImpl) (this).getModel()).getElement();
			if (((CloneMediator) parentContainer).getTargetsOutputConnector().size() > 1) {
				CloneMediatorUtils.reorderWhenForward(this);
				reorderdOnUndo = true;
			}
		}
	}

	/**
	 * @generated NOT
	 */
	protected void createDefaultEditPolicies() {
		installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy());
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new CloneMediatorItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new FeedbackIndicateDragDropEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new CloneMediatorCanonicalEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
		// For handle Double click Event.
		installEditPolicy(EditPolicyRoles.OPEN_ROLE, new ShowPropertyViewEditPolicy());
		// XXX need an SCR to runtime to have another abstract superclass that would let children add reasonable editpolicies
		// removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CONNECTION_HANDLES_ROLE);
	}

	/**
	 * @generated
	 */
	protected LayoutEditPolicy createLayoutEditPolicy() {
		org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy lep = new org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy() {

			protected EditPolicy createChildEditPolicy(EditPart child) {
				View childView = (View) child.getModel();
				switch (EsbVisualIDRegistry.getVisualID(childView)) {
				case CloneMediatorInputConnectorEditPart.VISUAL_ID:
				case CloneMediatorOutputConnectorEditPart.VISUAL_ID:
				case CloneMediatorTargetOutputConnectorEditPart.VISUAL_ID:
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
		return primaryShape = new CloneMediatorFigure() {
			public void setBounds(org.eclipse.draw2d.geometry.Rectangle rect) {
				super.setBounds(rect);
				if ((!connected) && (this.getBounds().getLocation().x != 0 && this.getBounds().getLocation().y != 0)) {
					connectToMostSuitableElement();
					reAllocate(rect);
					connected = true;
				}
			};
		};
	}

	/**
	 * @generated
	 */
	public CloneMediatorFigure getPrimaryShape() {
		return (CloneMediatorFigure) primaryShape;
	}

	/**
	 * @generated NOT
	 */
	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof CloneMediatorCloneIDEditPart) {
			((CloneMediatorCloneIDEditPart) childEditPart).setLabel(getPrimaryShape()
					.getFigureCloneMediatorPropertyValue());
			return true;
		}
		if (childEditPart instanceof CloneMediatorInputConnectorEditPart) {
			IFigure borderItemFigure = ((CloneMediatorInputConnectorEditPart) childEditPart).getFigure();
			BorderItemLocator locator = new FixedBorderItemLocator(getMainFigure(), borderItemFigure,
					PositionConstants.WEST, 0.5);
			getBorderedFigure().getBorderItemContainer().add(borderItemFigure, locator);
			return true;
		}
		if (childEditPart instanceof CloneMediatorOutputConnectorEditPart) {
			IFigure borderItemFigure = ((CloneMediatorOutputConnectorEditPart) childEditPart).getFigure();
			BorderItemLocator locator = new FixedBorderItemLocator(getMainFigure(), borderItemFigure,
					PositionConstants.EAST, 0.5);
			getBorderedFigure().getBorderItemContainer().add(borderItemFigure, locator);
			return true;
		}

		if (childEditPart instanceof CloneMediatorTargetOutputConnectorEditPart) {
			IFigure targetOutputConnector = ((CloneMediatorTargetOutputConnectorEditPart) childEditPart).getFigure();
			targetOutputConnector.setEnabled(false);
			targetOutputConnectors.add(targetOutputConnector);
		}
		/*		if (childEditPart instanceof CloneMediatorTargetOutputConnectorEditPart) {
		 IFigure borderItemFigure = ((CloneMediatorTargetOutputConnectorEditPart) childEditPart)
		 .getFigure();

		 if (!this.getIsForward()) {
		 NodeFigure figureOutput = ((AbstractOutputConnector) this
		 .getChildren().get(this.getChildren().size() - 1))
		 .getNodeFigureOutput();
		 figureOutput.removeAll();
		 figureOutput.add(((AbstractOutputConnector) this.getChildren()
		 .get(this.getChildren().size() - 1))
		 .getPrimaryShapeReverse());
		 }

		 BorderItemLocator locator = new FixedBorderItemLocator(
		 getMainFigure(), borderItemFigure, PositionConstants.EAST,
		 0.5);
		 getBorderedFigure().getBorderItemContainer().add(borderItemFigure,
		 locator);
		 for (int i = 0; i < this.getChildren().size(); ++i) {
		 if (this.getChildren().get(i) instanceof AbstractOutputConnector) {
		 ++outputCount;
		 }
		 }

		 for (int i = 0; i < this.getChildren().size(); ++i) {
		 if (this.getChildren().get(i) instanceof AbstractOutputConnector) {

		 outputConnector = ((AbstractOutputConnector) this
		 .getChildren().get(i)).getFigure();
		 outputConnectors.add(outputConnector);
		 outputPosition = outputPosition + (1 / (outputCount + 1));

		 if (this.getIsForward()) {

		 BorderItemLocator outputLocator = new FixedBorderItemLocator(
		 this.getMainFigure(), outputConnector,
		 PositionConstants.EAST, outputPosition);

		 outputLocators.add(outputLocator);

		 }
		 if (!this.getIsForward()) {

		 BorderItemLocator outputLocator = new FixedBorderItemLocator(
		 this.getMainFigure(), outputConnector,
		 PositionConstants.WEST, outputPosition);

		 outputLocators.add(outputLocator);

		 }
		 }
		 }

		 for (int j = 0; j < outputConnectors.size(); ++j) {
		 this.getBorderedFigure().getBorderItemContainer()
		 .remove(outputConnectors.get(j));
		 this.getBorderedFigure().getBorderItemContainer()
		 .add(outputConnectors.get(j), outputLocators.get(j));

		 }
		 return true;
		 }*/
		return false;
	}

	/**
	 * @generated
	 */
	protected boolean removeFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof CloneMediatorCloneIDEditPart) {
			return true;
		}
		if (childEditPart instanceof CloneMediatorInputConnectorEditPart) {
			getBorderedFigure().getBorderItemContainer().remove(
					((CloneMediatorInputConnectorEditPart) childEditPart).getFigure());
			return true;
		}
		if (childEditPart instanceof CloneMediatorOutputConnectorEditPart) {
			getBorderedFigure().getBorderItemContainer().remove(
					((CloneMediatorOutputConnectorEditPart) childEditPart).getFigure());
			return true;
		}
		if (childEditPart instanceof CloneMediatorTargetOutputConnectorEditPart) {
			getBorderedFigure().getBorderItemContainer().remove(
					((CloneMediatorTargetOutputConnectorEditPart) childEditPart).getFigure());
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected void addChildVisual(EditPart childEditPart, int index) {
		if (addFixedChild(childEditPart)) {
			return;
		}
		super.addChildVisual(childEditPart, -1);
	}

	/**
	 * @generated
	 */
	protected void removeChildVisual(EditPart childEditPart) {
		if (removeFixedChild(childEditPart)) {
			return;
		}
		super.removeChildVisual(childEditPart);
	}

	/**
	 * @generated
	 */
	protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
		if (editPart instanceof IBorderItemEditPart) {
			return getBorderedFigure().getBorderItemContainer();
		}
		return getContentPane();
	}

	/**
	 * @generated
	 */
	protected NodeFigure createNodePlate() {
		DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(40, 40);
		return result;
	}

	/**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model
	 * so you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated NOT
	 */
	protected NodeFigure createMainFigure() {
		NodeFigure figure = createNodePlate();
		figure.setLayoutManager(new StackLayout());
		IFigure shape = createNodeShape();
		figure.add(shape);
		contentPane = setupContentPane(shape);
		addLayoutListner(figure);
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
	 * @generated
	 */
	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(EsbVisualIDRegistry.getType(CloneMediatorCloneIDEditPart.VISUAL_ID));
	}

	/**
	 * @generated
	 */
	public class CloneMediatorFigure extends CloneMediatorGraphicalShape {

		/**
		 * @generated
		 */
		private WrappingLabel fFigureCloneMediatorPropertyValue;

		/**
		 * @generated NOT
		 */
		public CloneMediatorFigure() {

			ToolbarLayout layoutThis = new ToolbarLayout();
			layoutThis.setStretchMinorAxis(true);
			layoutThis.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
			layoutThis.setSpacing(0);
			layoutThis.setVertical(false);
			this.setLayoutManager(layoutThis);
			this.setPreferredSize(new Dimension(getMapMode().DPtoLP(170), getMapMode().DPtoLP(100)));
			this.setOutline(false);
			this.setBackgroundColor(THIS_BACK);
			createContents();
		}

		public void add(IFigure figure, Object constraint, int index) {
			if (figure instanceof DefaultSizeNodeFigure) {
				GridData layoutData = new GridData();
				layoutData.grabExcessHorizontalSpace = true;
				layoutData.grabExcessVerticalSpace = true;
				layoutData.horizontalAlignment = GridData.FILL;
				layoutData.verticalAlignment = GridData.FILL;
				super.add(figure, layoutData, index);
			} else if (figure instanceof RoundedRectangle) {
				GridData layoutData = new GridData();
				layoutData.grabExcessHorizontalSpace = true;
				layoutData.grabExcessVerticalSpace = true;
				layoutData.horizontalAlignment = GridData.FILL;
				layoutData.verticalAlignment = GridData.FILL;
				super.add(figure, layoutData, index);
			}

			else {
				super.add(figure, constraint, index);
			}
		}

		/**
		 * @generated NOT
		 */
		private void createContents() {

			fFigureCloneMediatorPropertyValue = new WrappingLabel();
			fFigureCloneMediatorPropertyValue
					.setText(DEFAULT_PROPERTY_VALUE_TEXT);

		}

		/**
		 * @generated NOT
		 */
		public WrappingLabel getFigureCloneMediatorPropertyValue() {
			return fFigureCloneMediatorPropertyValue;
		}

		public String getIconPath() {
			return CLONE_MEDIATOR_ICON_PATH;
		}

		public String getNodeName() {
			return Messages.CloneMediatorEditPart_NodeName;
		}

		public IFigure getToolTip() {
			if (StringUtils.isEmpty(toolTipMessage)) {
				return new Label(Messages.CloneMediatorEditPart_ToolTipMessage);
			} else {
				return new Label(toolTipMessage);
			}
		}
	}

	/**
	 * @generated NOT
	 */
	static final Color THIS_BACK = new Color(null, 255, 255, 255);

}
