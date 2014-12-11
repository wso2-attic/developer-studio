package org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.AbstractMediatorCompartmentEditPart;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.AbstractMediatorFlowCompartmentEditPart.Complexity;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.editpolicy.FeedbackIndicateDragDropEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.policies.MediatorFlowMediatorFlowCompartment7CanonicalEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.policies.MediatorFlowMediatorFlowCompartment7ItemSemanticEditPolicy;

/**
 * @generated NOT
 */
public class MediatorFlowMediatorFlowCompartment7EditPart extends
		AbstractMediatorCompartmentEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 7022;

	/**
	 * @generated NOT
	 */
	public MediatorFlowMediatorFlowCompartment7EditPart(View view) {
		super(view);
		complexity = Complexity.DOUBLE;
	}

	/**
	 * @generated NOT
	 */
	public String getCompartmentName() {
		//return Messages.MediatorFlowMediatorFlowCompartment7EditPart_title;
		return null; //"Then";
	}

	public IFigure createFigure() {
		ResizableCompartmentFigure result = (ResizableCompartmentFigure) super
				.createFigure();
		result.setTitle("Then");
		result.setTitleVisibility(true);
		/*
		 * Override default border.
		 * Fixing TOOLS-1864.
		 */
		LineBorder border = new LineBorder(new Color(null, 0, 0, 204), 1,
				SWT.BORDER_DASH);
		result.setBorder(border);
		result.setToolTip("Then");
		ConstrainedToolbarLayout layoutManager = new ConstrainedToolbarLayout(
				false);
		layoutManager.setSpacing(-15);
		result.setLayoutManager(layoutManager);
		return result;
	}

	/**
	 * @generated NOT
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(
				EditPolicyRoles.SEMANTIC_ROLE,
				new MediatorFlowMediatorFlowCompartment7ItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.CREATION_ROLE,
				new CreationEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE,
				new DragDropEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE,
				new FeedbackIndicateDragDropEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new MediatorFlowMediatorFlowCompartment7CanonicalEditPolicy());
	}

	public boolean isSelectable() {
		// TODO This or using ResizableEditpolicy?
		return false;
	}

	/**
	 * @generated
	 */
	protected void setRatio(Double ratio) {
		if (getFigure().getParent().getLayoutManager() instanceof ConstrainedToolbarLayout) {
			super.setRatio(ratio);
		}
	}

}
