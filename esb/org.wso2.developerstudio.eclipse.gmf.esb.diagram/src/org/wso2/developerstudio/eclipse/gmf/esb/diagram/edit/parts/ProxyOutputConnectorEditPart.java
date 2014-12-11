package org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.parts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.AbstractBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.AbstractMediatorOutputConnectorEditPart;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.AbstractOutputConnectorEditPart;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.AbstractPointerShape;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.DefaultSizePointerNodeFigure;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.EastPointerShape;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.AbstractMediatorOutputConnectorEditPart.EastPointerFigure;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.AbstractMediatorOutputConnectorEditPart.WestPointerFigure;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.policies.ProxyOutputConnectorItemSemanticEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.part.EsbDiagramEditorPlugin;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.providers.EsbElementTypes;

/**
 * @generated NOT
 */
public class ProxyOutputConnectorEditPart extends
		AbstractOutputConnectorEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 3002;

	/**
	 * @generated
	 */
	protected IFigure contentPane;

	/**
	 * @generated
	 */
	protected IFigure primaryShape;

	protected IFigure primaryShapeForward;

	public final boolean isInput = false;

	/**
	 * @generated
	 */
	public ProxyOutputConnectorEditPart(View view) {
		super(view);
	}

	public NodeFigure figure_;

	public NodeFigure getNodeFigureOutput() {
		return figure_;
	}

	public IFigure createNodeShapeForward() {
		return primaryShapeForward = new EastPointerFigure();
	}

	/**
	 * @generated NOT
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE,
				getPrimaryDragEditPolicy());
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new ProxyOutputConnectorItemSemanticEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
		// XXX need an SCR to runtime to have another abstract superclass that would let children add reasonable editpolicies
		removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CONNECTION_HANDLES_ROLE);
	}

	/**
	 * @generated
	 */
	protected LayoutEditPolicy createLayoutEditPolicy() {
		org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy lep = new org.eclipse.gmf.runtime.diagram.ui.editpolicies.LayoutEditPolicy() {

			protected EditPolicy createChildEditPolicy(EditPart child) {
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
		return primaryShape = new EastPointerFigure();
	}

	/**
	 * @generated
	 */
	public EastPointerFigure getPrimaryShape() {
		return (EastPointerFigure) primaryShape;
	}

	/**
	 * @generated
	 */
	protected NodeFigure createNodePlate() {
		DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(12, 10);

		//FIXME: workaround for #154536
		result.getBounds().setSize(result.getPreferredSize());
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
	protected NodeFigure createNodeFigure() {
		NodeFigure figure = createNodePlate();
		figure.setLayoutManager(new StackLayout());
		IFigure shape = createNodeShape();
		figure.add(shape);
		contentPane = setupContentPane(shape);
		figure_ = figure;

		return figure;
	}

	/**
	 * Default implementation treats passed figure as content pane.
	 * Respects layout one may have set for generated figure.
	 * @param nodeShape instance of generated figure class
	 * @generated
	 */
	protected IFigure setupContentPane(IFigure nodeShape) {
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
	public List<IElementType> getMARelTypesOnSource() {
		ArrayList<IElementType> types = new ArrayList<IElementType>(1);
		types.add(EsbElementTypes.EsbLink_4001);
		return types;
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMARelTypesOnSourceAndTarget(
			IGraphicalEditPart targetEditPart) {
		LinkedList<IElementType> types = new LinkedList<IElementType>();
		if (targetEditPart instanceof ProxyInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof MessageInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof DefaultEndPointInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof AddressEndPointInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof DropMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof FilterMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof MergeNodeFirstInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof MergeNodeSecondInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof LogMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof PropertyMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof EnrichMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof XSLTMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof SwitchMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof SequenceInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof EventMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof EntitlementMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof ClassMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof SpringMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof ScriptMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof FaultMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof XQueryMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof CommandMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof DBLookupMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof DBReportMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof SmooksMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		if (targetEditPart instanceof SendMediatorInputConnectorEditPart) {
			types.add(EsbElementTypes.EsbLink_4001);
		}
		return types;
	}

	/**
	 * @generated
	 */
	public List<IElementType> getMATypesForTarget(IElementType relationshipType) {
		LinkedList<IElementType> types = new LinkedList<IElementType>();
		if (relationshipType == EsbElementTypes.EsbLink_4001) {
			types.add(EsbElementTypes.ProxyInputConnector_3003);
			types.add(EsbElementTypes.MessageInputConnector_3046);
			types.add(EsbElementTypes.DefaultEndPointInputConnector_3021);
			types.add(EsbElementTypes.AddressEndPointInputConnector_3030);
			types.add(EsbElementTypes.DropMediatorInputConnector_3008);
			types.add(EsbElementTypes.FilterMediatorInputConnector_3010);
			types.add(EsbElementTypes.MergeNodeFirstInputConnector_3014);
			types.add(EsbElementTypes.MergeNodeSecondInputConnector_3015);
			types.add(EsbElementTypes.LogMediatorInputConnector_3018);
			types.add(EsbElementTypes.PropertyMediatorInputConnector_3033);
			types.add(EsbElementTypes.EnrichMediatorInputConnector_3036);
			types.add(EsbElementTypes.XSLTMediatorInputConnector_3039);
			types.add(EsbElementTypes.SwitchMediatorInputConnector_3042);
			types.add(EsbElementTypes.SequenceInputConnector_3049);
			types.add(EsbElementTypes.EventMediatorInputConnector_3052);
			types.add(EsbElementTypes.EntitlementMediatorInputConnector_3055);
			types.add(EsbElementTypes.ClassMediatorInputConnector_3058);
			types.add(EsbElementTypes.SpringMediatorInputConnector_3061);
			types.add(EsbElementTypes.ScriptMediatorInputConnector_3064);
			types.add(EsbElementTypes.FaultMediatorInputConnector_3067);
			types.add(EsbElementTypes.XQueryMediatorInputConnector_3070);
			types.add(EsbElementTypes.CommandMediatorInputConnector_3073);
			types.add(EsbElementTypes.DBLookupMediatorInputConnector_3076);
			types.add(EsbElementTypes.DBReportMediatorInputConnector_3079);
			types.add(EsbElementTypes.SmooksMediatorInputConnector_3082);
			types.add(EsbElementTypes.SendMediatorInputConnector_3085);
		}
		return types;
	}

	/**
	 * @generated NOT
	 */
	public class EastPointerFigure extends RoundedRectangle {

		/**
		 * @generated NOT
		 */
		public EastPointerFigure() {

			GridLayout layoutThis = new GridLayout();
			layoutThis.numColumns = 1;
			layoutThis.makeColumnsEqualWidth = true;
			layoutThis.marginHeight = 0;
			layoutThis.marginWidth = 0;
			this.setLayoutManager(layoutThis);

			this.setCornerDimensions(new Dimension(1, 1));
			this.setFill(false);
			this.setOutline(false);
			// this.setBackgroundColor(get);
			this.setPreferredSize(new Dimension(22, 18));

			this.addMouseMotionListener(new MouseMotionListener() {

				public void mouseMoved(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}

				public void mouseHover(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}

				public void mouseExited(MouseEvent arg0) {
					if (getEditDomain().getPaletteViewer().getActiveTool()
							.getId().equals("createEsbLink1CreationTool")) {
						getEditDomain().getPaletteViewer().setActiveTool(null);
					}
				}

				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					if (getEditDomain().getPaletteViewer().getActiveTool()
							.getId().equals("selectionTool")) {
						getEditDomain()
								.getPaletteViewer()
								.setActiveTool(
										(ToolEntry) (((PaletteContainer) getEditDomain()
												.getPaletteViewer()
												.getPaletteRoot().getChildren()
												.get(4)).getChildren().get(0)));

					}
				}

				public void mouseDragged(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}
			});
			createContents();
		}

		public void createContents() {
			GridData constraintImageRectangle11 = new GridData();
			constraintImageRectangle11.verticalAlignment = GridData.FILL;
			constraintImageRectangle11.horizontalAlignment = GridData.FILL;
			constraintImageRectangle11.horizontalIndent = 0;
			constraintImageRectangle11.horizontalSpan = 1;
			constraintImageRectangle11.verticalSpan = 2;
			constraintImageRectangle11.grabExcessHorizontalSpace = true;
			constraintImageRectangle11.grabExcessVerticalSpace = true;

			ImageDescriptor imgDesc1 = EsbDiagramEditorPlugin
					.getBundledImageDescriptor("icons/ico20/arrowEast.png");
			ImageFigure img1 = new ImageFigure(imgDesc1.createImage());
			img1.setSize(new Dimension(22, 18));

			RectangleFigure imageRectangle11 = new RectangleFigure();
			imageRectangle11.setOutline(false);
			imageRectangle11.setBackgroundColor(new Color(null, 255, 255, 255));
			imageRectangle11.setPreferredSize(new Dimension(22, 18));
			imageRectangle11.add(img1);

			this.add(imageRectangle11, constraintImageRectangle11);

		}

	}

	/**
	 * @generated
	 */
	static final Color THIS_BACK = new Color(null, 50, 50, 50);

	@Override
	public IFigure createNodeShapeReverse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

}
