package org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.parts;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.AbstractBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderedNodeFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeConnectionRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.edit.policies.reparent.CreationEditPolicyWithCustomReparent;
import org.eclipse.swt.widgets.Display;
import org.wso2.developerstudio.eclipse.gmf.esb.ComplexEndpoints;
import org.wso2.developerstudio.eclipse.gmf.esb.ComplexEndpointsOutputConnector;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbFactory;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.AbstractConnectorEditPart;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.AbstractEndpoint;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.AbstractMediatorFlowCompartmentEditPart;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.ConnectionUtils;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.EditorUtils;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.SlidingBorderItemLocator;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.connections.ConnectionCalculator;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.layout.XYRepossition;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.policies.MediatorFlowMediatorFlowCompartment18CanonicalEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.edit.policies.MediatorFlowMediatorFlowCompartment18ItemSemanticEditPolicy;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.part.EsbDiagramEditor;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.part.EsbVisualIDRegistry;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.part.Messages;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.providers.EsbElementTypes;

/**
 * @generated
 */
public class MediatorFlowMediatorFlowCompartment18EditPart extends
		ShapeCompartmentEditPart {

	BorderedNodeFigure borderedNodeFigure;
	AbstractBorderItemEditPart sourceOutputConnector = null;
	AbstractBorderItemEditPart outputConnectorEditPart = null;
	ShapeNodeEditPart sourceEditPart = null;
	Map<ComplexEndpointsOutputConnector, AbstractEndpoint> connectorAndEndpointMap = new HashMap<ComplexEndpointsOutputConnector, AbstractEndpoint>();
	private ComplexEndpoints complexEndpoints;
	private MediatorFlowMediatorFlowCompartment18EditPart instance;

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 7045;

	/**
	 * @generated
	 */
	public MediatorFlowMediatorFlowCompartment18EditPart(View view) {
		super(view);
	}

	/**
	 * @generated NOT
	 */
	public String getCompartmentName() {
		//return Messages.MediatorFlowMediatorFlowCompartment18EditPart_title;
		return null;
	}

	protected IFigure createFigure() {
		ResizableCompartmentFigure result = (ResizableCompartmentFigure) super
				.createFigure();
		result.setTitleVisibility(true);
		// Override default border.
		result.setBorder(new MarginBorder(0, 0, 0, 0));
		borderedNodeFigure = new BorderedNodeFigure(result);
		result.setToolTip(getCompartmentName());
		//return result;
		return borderedNodeFigure;
	}

	public ResizableCompartmentFigure getCompartmentFigure() {
		return (ResizableCompartmentFigure) borderedNodeFigure.getMainFigure();
	}

	protected void addChildVisual(EditPart childEditPart, int index) {
		IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
		if (childEditPart instanceof AbstractEndpoint) {
			addEndpointChildVisual((AbstractEndpoint) childEditPart);
		} else {
			index = Math.min(getContentPane().getChildren().size(), index);
			getContentPane().add(child, index);
		}
	}

	private <T extends AbstractEndpoint> void addEndpointChildVisual(
			T childEditPart) {
		borderedNodeFigure.getBorderItemContainer().add(
				((T) childEditPart).getFigure(),
				new SlidingBorderItemLocator(
						borderedNodeFigure.getMainFigure(), ((T) childEditPart)
								.getFigure(), PositionConstants.EAST, 10, 5));
	}

	protected void removeChildVisual(EditPart child) {
		IFigure childFigure = ((GraphicalEditPart) child).getFigure();
		if ((child instanceof AbstractEndpoint)) {
			borderedNodeFigure.getBorderItemContainer().remove(childFigure);
		} else {
			getContentPane().remove(childFigure);
		}
	}

	/**
	 * @generated
	 */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(
				EditPolicyRoles.SEMANTIC_ROLE,
				new MediatorFlowMediatorFlowCompartment18ItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.CREATION_ROLE,
				new CreationEditPolicyWithCustomReparent(
						EsbVisualIDRegistry.TYPED_INSTANCE));
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE,
				new DragDropEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new MediatorFlowMediatorFlowCompartment18CanonicalEditPolicy());
	}

	/**
	 * @generated
	 */
	protected void setRatio(Double ratio) {
		if (getFigure().getParent().getLayoutManager() instanceof ConstrainedToolbarLayout) {
			super.setRatio(ratio);
		}
	}

	/**
	 * @generated
	 */
	public EditPart getTargetEditPart(Request request) {
		if (request instanceof CreateViewAndElementRequest) {
			CreateElementRequestAdapter adapter = ((CreateViewAndElementRequest) request)
					.getViewAndElementDescriptor()
					.getCreateElementRequestAdapter();
			IElementType type = (IElementType) adapter
					.getAdapter(IElementType.class);
			if (type == EsbElementTypes.DropMediator_3491) {
				return this;
			}
			if (type == EsbElementTypes.PropertyMediator_3492) {
				return this;
			}
			if (type == EsbElementTypes.ThrottleMediator_3493) {
				return this;
			}
			if (type == EsbElementTypes.FilterMediator_3494) {
				return this;
			}
			if (type == EsbElementTypes.LogMediator_3495) {
				return this;
			}
			if (type == EsbElementTypes.EnrichMediator_3496) {
				return this;
			}
			if (type == EsbElementTypes.XSLTMediator_3497) {
				return this;
			}
			if (type == EsbElementTypes.SwitchMediator_3498) {
				return this;
			}
			if (type == EsbElementTypes.Sequence_3503) {
				return this;
			}
			if (type == EsbElementTypes.EventMediator_3504) {
				return this;
			}
			if (type == EsbElementTypes.EntitlementMediator_3505) {
				return this;
			}
			if (type == EsbElementTypes.ClassMediator_3506) {
				return this;
			}
			if (type == EsbElementTypes.SpringMediator_3507) {
				return this;
			}
			if (type == EsbElementTypes.ScriptMediator_3508) {
				return this;
			}
			if (type == EsbElementTypes.FaultMediator_3509) {
				return this;
			}
			if (type == EsbElementTypes.XQueryMediator_3510) {
				return this;
			}
			if (type == EsbElementTypes.CommandMediator_3511) {
				return this;
			}
			if (type == EsbElementTypes.DBLookupMediator_3512) {
				return this;
			}
			if (type == EsbElementTypes.DBReportMediator_3513) {
				return this;
			}
			if (type == EsbElementTypes.SmooksMediator_3514) {
				return this;
			}
			if (type == EsbElementTypes.SendMediator_3515) {
				return this;
			}
			if (type == EsbElementTypes.HeaderMediator_3516) {
				return this;
			}
			if (type == EsbElementTypes.CloneMediator_3517) {
				return this;
			}
			if (type == EsbElementTypes.CacheMediator_3518) {
				return this;
			}
			if (type == EsbElementTypes.IterateMediator_3519) {
				return this;
			}
			if (type == EsbElementTypes.CalloutMediator_3520) {
				return this;
			}
			if (type == EsbElementTypes.TransactionMediator_3521) {
				return this;
			}
			if (type == EsbElementTypes.RMSequenceMediator_3522) {
				return this;
			}
			if (type == EsbElementTypes.RuleMediator_3523) {
				return this;
			}
			if (type == EsbElementTypes.OAuthMediator_3524) {
				return this;
			}
			if (type == EsbElementTypes.AggregateMediator_3525) {
				return this;
			}
			if (type == EsbElementTypes.StoreMediator_3588) {
				return this;
			}
			if (type == EsbElementTypes.BuilderMediator_3591) {
				return this;
			}
			if (type == EsbElementTypes.CallTemplateMediator_3594) {
				return this;
			}
			if (type == EsbElementTypes.PayloadFactoryMediator_3597) {
				return this;
			}
			if (type == EsbElementTypes.EnqueueMediator_3600) {
				return this;
			}
			if (type == EsbElementTypes.URLRewriteMediator_3620) {
				return this;
			}
			if (type == EsbElementTypes.ValidateMediator_3623) {
				return this;
			}
			if (type == EsbElementTypes.RouterMediator_3628) {
				return this;
			}
			if (type == EsbElementTypes.ConditionalRouterMediator_3635) {
				return this;
			}
			if (type == EsbElementTypes.BAMMediator_3680) {
				return this;
			}
			if (type == EsbElementTypes.BeanMediator_3683) {
				return this;
			}
			if (type == EsbElementTypes.EJBMediator_3686) {
				return this;
			}
			if (type == EsbElementTypes.DefaultEndPoint_3609) {
				return this;
			}
			if (type == EsbElementTypes.AddressEndPoint_3610) {
				return this;
			}
			if (type == EsbElementTypes.FailoverEndPoint_3611) {
				return this;
			}
			if (type == EsbElementTypes.RecipientListEndPoint_3692) {
				return this;
			}
			if (type == EsbElementTypes.WSDLEndPoint_3612) {
				return this;
			}
			if (type == EsbElementTypes.NamedEndpoint_3660) {
				return this;
			}
			if (type == EsbElementTypes.LoadBalanceEndPoint_3613) {
				return this;
			}
			if (type == EsbElementTypes.APIResourceEndpoint_3674) {
				return this;
			}
			if (type == EsbElementTypes.AddressingEndpoint_3689) {
				return this;
			}
			if (type == EsbElementTypes.HTTPEndpoint_3709) {
				return this;
			}
			if (type == EsbElementTypes.TemplateEndpoint_3716) {
				return this;
			}
			if (type == EsbElementTypes.CloudConnector_3719) {
				return this;
			}
			if (type == EsbElementTypes.CloudConnectorOperation_3722) {
				return this;
			}
			if (type == EsbElementTypes.LoopBackMediator_3736) {
				return this;
			}
			if (type == EsbElementTypes.RespondMediator_3739) {
				return this;
			}
			if (type == EsbElementTypes.CallMediator_3742) {
				return this;
			}
			if (type == EsbElementTypes.DataMapperMediator_3761) {
				return this;
			}
			if (type == EsbElementTypes.FastXSLTMediator_3764) {
				return this;
			}
			return getParent().getTargetEditPart(request);
		}
		if (request instanceof CreateUnspecifiedTypeConnectionRequest) {
			return getParent().getTargetEditPart(request);
		}
		return super.getTargetEditPart(request);
	}

	protected void removeChild(EditPart child) {
		super.removeChild(child);
		EditingDomain editingDomain = TransactionUtil
				.getEditingDomain(complexEndpoints);
		RemoveCommand removeCmd = new RemoveCommand(editingDomain,
				complexEndpoints,
				EsbPackage.Literals.COMPLEX_ENDPOINTS__OUTPUT_CONNECTOR,
				connectorAndEndpointMap.get(child));
		if (removeCmd.canExecute()) {
			editingDomain.getCommandStack().execute(removeCmd);
		}

		Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				XYRepossition.resizeContainers((IGraphicalEditPart) instance);
			}
		});
	}

	protected void addChild(EditPart child, int index) {
		super.addChild(child, index);
		/*		((MediatorFlow18EditPart) child.getParent().getParent())
		 .refreshConnector(this.getParent().getParent());*/

		/*		if (this.getParent().getParent().getChildren().size() < 2) {
		 return;
		 }*/

		if (child instanceof AbstractEndpoint) {
			ComplexEndpointsEditPart complexEndpointsEditPart = (ComplexEndpointsEditPart) this
					.getParent().getParent();
			complexEndpoints = (ComplexEndpoints) ((Node) complexEndpointsEditPart
					.getModel()).getElement();
			if (EditorUtils.getEndpointInputConnector((AbstractEndpoint) child)
					.getTargetConnections().size() == 0) {
				addConnectorAndLink(child);
			}
		}

		/*		if (child instanceof FailoverEndPointEditPart) {
		 FailoverEndPointEditPart endpointEditPart = (FailoverEndPointEditPart) child;
		 EObject parentEndpoint = ((org.eclipse.gmf.runtime.notation.impl.NodeImpl) (endpointEditPart)
		 .getModel()).getElement();
		 if (((FailoverEndPoint) parentEndpoint).getInputConnector()
		 .getIncomingLinks().size() == 0) {
		 if (((FailoverEndPoint) parentEndpoint)
		 .getWestOutputConnector() == null) {
		 SetCommand addCmd = new SetCommand(
		 getEditingDomain(),
		 parentEndpoint,
		 EsbPackage.Literals.FAILOVER_END_POINT__WEST_OUTPUT_CONNECTOR,
		 EsbFactory.eINSTANCE
		 .createFailoverEndPointWestOutputConnector());
		 if (addCmd.canExecute()) {
		 getEditingDomain().getCommandStack().execute(addCmd);
		 }
		 addConnectorAndLink();
		 }
		 }
		 } else if (child instanceof LoadBalanceEndPointEditPart) {
		 LoadBalanceEndPointEditPart endpointEditPart = (LoadBalanceEndPointEditPart) child;
		 EObject parentEndpoint = ((org.eclipse.gmf.runtime.notation.impl.NodeImpl) (endpointEditPart)
		 .getModel()).getElement();
		 if (((LoadBalanceEndPoint) parentEndpoint).getInputConnector()
		 .getIncomingLinks().size() == 0) {
		 if (((LoadBalanceEndPoint) parentEndpoint)
		 .getWestOutputConnector() == null) {
		 SetCommand addCmd = new SetCommand(
		 getEditingDomain(),
		 parentEndpoint,
		 EsbPackage.Literals.LOAD_BALANCE_END_POINT__WEST_OUTPUT_CONNECTOR,
		 EsbFactory.eINSTANCE
		 .createLoadBalanceEndPointWestOutputConnector());
		 if (addCmd.canExecute()) {
		 getEditingDomain().getCommandStack().execute(addCmd);
		 }
		 addConnectorAndLink();
		 }
		 }

		 } else if (child instanceof DefaultEndPointEditPart) {
		 DefaultEndPointEditPart endpointEditPart = (DefaultEndPointEditPart) child;
		 EObject parentEndpoint = ((org.eclipse.gmf.runtime.notation.impl.NodeImpl) (endpointEditPart)
		 .getModel()).getElement();
		 if (((DefaultEndPoint) parentEndpoint).getInputConnector()
		 .getIncomingLinks().size() == 0) {
		 if (((DefaultEndPoint) parentEndpoint).getOutputConnector() == null) {
		 SetCommand addCmd = new SetCommand(
		 getEditingDomain(),
		 parentEndpoint,
		 EsbPackage.Literals.DEFAULT_END_POINT__OUTPUT_CONNECTOR,
		 EsbFactory.eINSTANCE
		 .createDefaultEndPointOutputConnector());
		 if (addCmd.canExecute()) {
		 getEditingDomain().getCommandStack().execute(addCmd);
		 }
		 addConnectorAndLink();
		 }
		 }
		 } else if (child instanceof AddressEndPointEditPart) {
		 AddressEndPointEditPart endpointEditPart = (AddressEndPointEditPart) child;
		 EObject parentEndpoint = ((org.eclipse.gmf.runtime.notation.impl.NodeImpl) (endpointEditPart)
		 .getModel()).getElement();
		 if (((AddressEndPoint) parentEndpoint).getInputConnector()
		 .getIncomingLinks().size() == 0) {
		 if (((AddressEndPoint) parentEndpoint).getOutputConnector() == null) {
		 SetCommand addCmd = new SetCommand(
		 getEditingDomain(),
		 parentEndpoint,
		 EsbPackage.Literals.ADDRESS_END_POINT__OUTPUT_CONNECTOR,
		 EsbFactory.eINSTANCE
		 .createAddressEndPointOutputConnector());
		 if (addCmd.canExecute()) {
		 getEditingDomain().getCommandStack().execute(addCmd);
		 }
		 addConnectorAndLink();
		 }
		 }

		 } else if (child instanceof WSDLEndPointEditPart) {
		 WSDLEndPointEditPart endpointEditPart = (WSDLEndPointEditPart) child;
		 EObject parentEndpoint = ((org.eclipse.gmf.runtime.notation.impl.NodeImpl) (endpointEditPart)
		 .getModel()).getElement();
		 if (((WSDLEndPoint) parentEndpoint).getInputConnector()
		 .getIncomingLinks().size() == 0) {
		 if (((WSDLEndPoint) parentEndpoint).getOutputConnector() == null) {
		 SetCommand addCmd = new SetCommand(
		 getEditingDomain(),
		 parentEndpoint,
		 EsbPackage.Literals.WSDL_END_POINT__OUTPUT_CONNECTOR,
		 EsbFactory.eINSTANCE
		 .createWSDLEndPointOutputConnector());
		 if (addCmd.canExecute()) {
		 getEditingDomain().getCommandStack().execute(addCmd);
		 }
		 addConnectorAndLink();
		 }
		 }
		 } else if (child instanceof NamedEndpointEditPart) {
		 NamedEndpointEditPart endpointEditPart = (NamedEndpointEditPart) child;
		 EObject parentEndpoint = ((org.eclipse.gmf.runtime.notation.impl.NodeImpl) (endpointEditPart)
		 .getModel()).getElement();
		 if (((NamedEndpoint) parentEndpoint).getInputConnector()
		 .getIncomingLinks().size() == 0) {
		 if (((NamedEndpoint) parentEndpoint).getOutputConnector() == null) {
		 SetCommand addCmd = new SetCommand(
		 getEditingDomain(),
		 parentEndpoint,
		 EsbPackage.Literals.NAMED_ENDPOINT__OUTPUT_CONNECTOR,
		 EsbFactory.eINSTANCE
		 .createNamedEndpointOutputConnector());
		 if (addCmd.canExecute()) {
		 getEditingDomain().getCommandStack().execute(addCmd);
		 }
		 addConnectorAndLink();
		 }
		 }
		 }*/

	}

	/*	public void connectNormally(EditPart child) {
	 AbstractBorderItemEditPart inputConnector = null;

	 if ((child instanceof AbstractMediator)
	 || (child instanceof AbstractEndpoint)) {

	 for (int i = 0; i < child.getChildren().size(); ++i) {
	 if (child.getChildren().get(i) instanceof AbstractMediatorInputConnectorEditPart) {
	 inputConnector = (AbstractMediatorInputConnectorEditPart) child
	 .getChildren().get(i);
	 }
	 if (child.getChildren().get(i) instanceof AbstractMediatorOutputConnectorEditPart) {
	 sourceOutputConnector = (AbstractMediatorOutputConnectorEditPart) child
	 .getChildren().get(i);
	 }
	 if (child.getChildren().get(i) instanceof AbstractEndpointInputConnectorEditPart) {
	 inputConnector = (AbstractEndpointInputConnectorEditPart) child
	 .getChildren().get(i);
	 }
	 if (child.getChildren().get(i) instanceof AbstractEndpointOutputConnectorEditPart) {
	 sourceOutputConnector = (AbstractEndpointOutputConnectorEditPart) child
	 .getChildren().get(i);
	 }

	 }
	 if (outputConnectorEditPart == null) {
	 outputConnectorEditPart = (AbstractOutputConnectorEditPart) this
	 .getParent().getParent().getChildren().get(2);
	 }
	 if (sourceEditPart == null || sourceEditPart.getRoot() == null) {
	 sourceEditPart = (AbstractBorderedShapeEditPart) this
	 .getParent().getParent();
	 }

	 CompoundCommand cc = new CompoundCommand("Create Link");

	 if (inputConnector != null) {
	 ICommand createSubTopicsCmd = new DeferredCreateConnectionViewAndElementCommand(
	 new CreateConnectionViewAndElementRequest(
	 EsbElementTypes.EsbLink_4001,
	 ((IHintedType) EsbElementTypes.EsbLink_4001)
	 .getSemanticHint(),
	 sourceEditPart.getDiagramPreferencesHint()),
	 new EObjectAdapter((EObject) outputConnectorEditPart
	 .getModel()), new EObjectAdapter(
	 (EObject) (inputConnector).getModel()),
	 sourceEditPart.getViewer());

	 cc.add(new ICommandProxy(createSubTopicsCmd));

	 getDiagramEditDomain().getDiagramCommandStack().execute(cc);
	 }

	 outputConnectorEditPart = sourceOutputConnector;
	 sourceEditPart = (ShapeNodeEditPart) child;

	 }
	 }*/

	private void addConnectorAndLink(EditPart child) {
		ComplexEndpointsOutputConnector connector = EsbFactory.eINSTANCE
				.createComplexEndpointsOutputConnector();
		EditingDomain editingDomain = ((IGraphicalEditPart) child)
				.getEditingDomain();
		AddCommand addCmd = new AddCommand(editingDomain, complexEndpoints,
				EsbPackage.Literals.COMPLEX_ENDPOINTS__OUTPUT_CONNECTOR,
				connector);
		if (addCmd.canExecute()) {
			editingDomain.getCommandStack().execute(addCmd);
			connectorAndEndpointMap.put(connector, (AbstractEndpoint) child);
		}
	}
}
