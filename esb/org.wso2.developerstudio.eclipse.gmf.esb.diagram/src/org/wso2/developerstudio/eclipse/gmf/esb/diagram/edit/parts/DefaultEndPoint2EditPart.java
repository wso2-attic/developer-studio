package org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.parts;

import java.awt.Rectangle;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.AbstractBorderedShapeEditPart;
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
import org.eclipse.gmf.tooling.runtime.edit.policies.reparent.CreationEditPolicyWithCustomReparent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.wso2.developerstudio.eclipse.gmf.esb.DefaultEndPoint;
import org.wso2.developerstudio.eclipse.gmf.esb.FailoverEndPoint;
import org.wso2.developerstudio.eclipse.gmf.esb.LoadBalanceEndPoint;
import org.wso2.developerstudio.eclipse.gmf.esb.SendMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.AbstractEndpoint2;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.EsbGraphicalShape;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.FixedBorderItemLocator;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.policies.DefaultEndPoint2CanonicalEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.policies.DefaultEndPoint2ItemSemanticEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.part.EsbVisualIDRegistry;

/**
 * @generated NOT
 */
public class DefaultEndPoint2EditPart extends AbstractEndpoint2 {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 3643;

	/**
	 * @generated
	 */
	protected IFigure contentPane;

	/**
	 * @generated
	 */
	public DefaultEndPoint2EditPart(View view) {
		super(view);
	}

	/**
	 * @generated NOT
	 */
	protected void createDefaultEditPolicies() {
		installEditPolicy(EditPolicyRoles.CREATION_ROLE,
				new CreationEditPolicyWithCustomReparent(
						EsbVisualIDRegistry.TYPED_INSTANCE));
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new DefaultEndPoint2ItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE,
				new DragDropEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new DefaultEndPoint2CanonicalEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
	}

	/**
	 * @generated
	 */
	protected LayoutEditPolicy createLayoutEditPolicy() {
		org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy lep = new org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy() {

			protected EditPolicy createChildEditPolicy(EditPart child) {
				View childView = (View) child.getModel();
				switch (EsbVisualIDRegistry.getVisualID(childView)) {
				case DefaultEndPointInputConnector2EditPart.VISUAL_ID:
				case DefaultEndPointOutputConnector2EditPart.VISUAL_ID:
					return new BorderItemSelectionEditPolicy();
				}
				EditPolicy result = child
						.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
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
	 * @generated
	 */
	protected IFigure createNodeShape() {
		return primaryShape = new DefaultEndPointFigure();
	}

	/**
	 * @generated
	 */
	public DefaultEndPointFigure getPrimaryShape() {
		return (DefaultEndPointFigure) primaryShape;
	}

	/**
	 * @generated NOT
	 */
	protected boolean addFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof DefaultEndPointEndPointName2EditPart) {
			((DefaultEndPointEndPointName2EditPart) childEditPart)
					.setLabel(getPrimaryShape()
							.getFigureDefaultEndPointNamePropertyLabel());
			return true;
		}
		if (childEditPart instanceof DefaultEndPointInputConnector2EditPart) {
			double position;
			EObject parentEndpoint = ((org.eclipse.gmf.runtime.notation.impl.NodeImpl) (childEditPart
					.getParent()).getModel()).getElement();
			if (((DefaultEndPoint) parentEndpoint).getInputConnector()
					.getIncomingLinks().size() != 0) {
				EObject source = ((DefaultEndPoint) parentEndpoint)
						.getInputConnector().getIncomingLinks().get(0)
						.getSource().eContainer();
				position = ((source instanceof LoadBalanceEndPoint)
						|| (source instanceof FailoverEndPoint) || (source instanceof SendMediator)) ? 0.5
						: 0.25;
			} else {
				position = 0.25;
			}
			IFigure borderItemFigure = ((DefaultEndPointInputConnector2EditPart) childEditPart)
					.getFigure();
			BorderItemLocator locator = new FixedBorderItemLocator(
					getMainFigure(), borderItemFigure, PositionConstants.WEST,
					position);
			getBorderedFigure().getBorderItemContainer().add(borderItemFigure,
					locator);
			return true;
		}
		if (childEditPart instanceof DefaultEndPointOutputConnector2EditPart) {
			IFigure borderItemFigure = ((DefaultEndPointOutputConnector2EditPart) childEditPart)
					.getFigure();
			BorderItemLocator locator = new FixedBorderItemLocator(
					getMainFigure(), borderItemFigure, PositionConstants.WEST,
					0.75);
			getBorderedFigure().getBorderItemContainer().add(borderItemFigure,
					locator);
			return true;
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected boolean removeFixedChild(EditPart childEditPart) {
		if (childEditPart instanceof DefaultEndPointEndPointName2EditPart) {
			return true;
		}
		if (childEditPart instanceof DefaultEndPointInputConnector2EditPart) {
			getBorderedFigure().getBorderItemContainer().remove(
					((DefaultEndPointInputConnector2EditPart) childEditPart)
							.getFigure());
			return true;
		}
		if (childEditPart instanceof DefaultEndPointOutputConnector2EditPart) {
			getBorderedFigure().getBorderItemContainer().remove(
					((DefaultEndPointOutputConnector2EditPart) childEditPart)
							.getFigure());
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
	 * @generated
	 */
	protected NodeFigure createMainFigure() {
		NodeFigure figure = createNodePlate();
		figure.setLayoutManager(new StackLayout());
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
	 * @generated
	 */
	public EditPart getPrimaryChildEditPart() {
		return getChildBySemanticHint(EsbVisualIDRegistry
				.getType(DefaultEndPointEndPointName2EditPart.VISUAL_ID));
	}

	/**
	 * @generated
	 */
	public class DefaultEndPointFigure extends EsbGraphicalShape {

		/**
		 * @generated
		 */
		private WrappingLabel fFigureDefaultEndPointNamePropertyLabel;

		/**
		 * @generated
		 */
		public DefaultEndPointFigure() {
			this.setBackgroundColor(THIS_BACK);
			createContents();
		}

		/**
		 * @generated NOT
		 */
		private void createContents() {

			fFigureDefaultEndPointNamePropertyLabel = new WrappingLabel();
			fFigureDefaultEndPointNamePropertyLabel.setText("<...>");
			fFigureDefaultEndPointNamePropertyLabel.setAlignment(SWT.CENTER);
			this.getPropertyValueRectangle1().add(
					fFigureDefaultEndPointNamePropertyLabel);

		}

		/**
		 * @generated
		 */
		public WrappingLabel getFigureDefaultEndPointNamePropertyLabel() {
			return fFigureDefaultEndPointNamePropertyLabel;
		}

		public String getIconPath() {
			return "icons/ico20/defalut-endpoint.gif";
		}

		public String getNodeName() {
			return "Def-EP";
		}

		public Color getBackgroundColor() {
			return THIS_BACK;
		}

	}

	/**
	 * @generated
	 */
	static final Color THIS_BACK = new Color(null, 255, 255, 255);

}