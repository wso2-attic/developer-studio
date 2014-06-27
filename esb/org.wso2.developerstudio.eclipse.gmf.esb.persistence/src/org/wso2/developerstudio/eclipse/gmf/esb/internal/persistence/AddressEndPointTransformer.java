/*
 * Copyright 2009-2010 WSO2, Inc. (http://wso2.com)
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
package org.wso2.developerstudio.eclipse.gmf.esb.internal.persistence;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.synapse.endpoints.AddressEndpoint;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.mediators.base.SequenceMediator;
import org.apache.synapse.mediators.builtin.CallMediator;
import org.apache.synapse.mediators.builtin.SendMediator;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;
import org.wso2.developerstudio.eclipse.gmf.esb.AddressEndPoint;
import org.wso2.developerstudio.eclipse.gmf.esb.EndPoint;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbNode;
import org.wso2.developerstudio.eclipse.gmf.esb.InputConnector;
import org.wso2.developerstudio.eclipse.gmf.esb.Sequence;
import org.wso2.developerstudio.eclipse.gmf.esb.SequenceInputConnector;
import org.wso2.developerstudio.eclipse.gmf.esb.persistence.EsbNodeTransformer;
import org.wso2.developerstudio.eclipse.gmf.esb.persistence.TransformationInfo;

/**
 * {@link EsbNodeTransformer} responsible for transforming
 * {@link org.wso2.developerstudio.eclipse.gmf.esb.EndPoint} model objects into
 * corresponding synapse artifact(s).
 */
public class AddressEndPointTransformer extends AbstractEndpointTransformer {
	/**
	 * {@inheritDoc}
	 */
	public void transform(TransformationInfo info, EsbNode subject)
			throws Exception {
		// Check subject.
		Assert.isTrue(subject instanceof AddressEndPoint, "Invalid subject");
		AddressEndPoint visualEndPoint = (AddressEndPoint) subject;
		Endpoint synapseEP = create(visualEndPoint, visualEndPoint.getEndPointName());
		setEndpointToSendCallOrProxy(info, visualEndPoint, synapseEP);

		if (!info.isEndPointFound) {
			info.isEndPointFound = true;
			info.firstEndPoint = visualEndPoint;
		}

		if(visualEndPoint.getOutputConnector()!=null){
			if(visualEndPoint.getOutputConnector().getOutgoingLink() !=null){
			InputConnector nextInputConnector=visualEndPoint.getOutputConnector().getOutgoingLink().getTarget();
			if((!(nextInputConnector instanceof SequenceInputConnector))||
					((((Sequence)nextInputConnector.eContainer()).getOutputConnector().get(0).getOutgoingLink()!=null)&&(!(((Sequence)nextInputConnector.eContainer()).getOutputConnector().get(0).getOutgoingLink().getTarget().eContainer() instanceof EndPoint)))){
				info.setParentSequence(info.getOriginOutSequence());
				info.setTraversalDirection(TransformationInfo.TRAVERSAL_DIRECTION_OUT);
			}else if((visualEndPoint.getInputConnector().getIncomingLinks().get(0).getSource().eContainer() instanceof Sequence)){
				info.setParentSequence(info.getCurrentReferredSequence());
			}
			}
		}		

		List<EsbNode> transformedMediators = info.getTransformedMediators();
		if (visualEndPoint.getOutputConnector() != null	&& visualEndPoint.getOutputConnector().getOutgoingLink() != null) {
			EsbNode nextElement = (EsbNode) visualEndPoint.getOutputConnector().getOutgoingLink().getTarget().eContainer();
			if (transformedMediators.contains(nextElement)) {
				return;
			}
			transformedMediators.add(nextElement);
		}
		
		// Transform endpoint output data flow.
		doTransform(info, visualEndPoint.getOutputConnector());
	}
	

	public void createSynapseObject(TransformationInfo info, EObject subject,
			List<Endpoint> endPoints) {

		Assert.isTrue(subject instanceof AddressEndPoint, "Invalid subject");
		AddressEndPoint addressEndPoint = (AddressEndPoint) subject;	
		AddressEndpoint synapseAddEP = new AddressEndpoint();
		
		createAdvanceOptions(addressEndPoint,synapseAddEP);
		synapseAddEP.getDefinition().setAddress(addressEndPoint.getURI());
		
		if (StringUtils.isNotBlank(addressEndPoint.getEndPointName())) {
			synapseAddEP.setName(addressEndPoint.getEndPointName());
		}
		
		/*EndpointDefinition synapseEPDef = new EndpointDefinition();
		synapseEPDef.setAddress(addressEndPoint.getURI());
		// TODO: Configure endpoint with values extracted from the visual model.

		// synapseEPDef.setCharSetEncoding(charSetEncoding);
		if (addressEndPoint.isAddressingEnabled()) {
			synapseEPDef.setAddressingOn(true);
			synapseEPDef.setUseSeparateListener(addressEndPoint
					.isAddressingSeparateListener());
			synapseEPDef
					.setAddressingVersion((addressEndPoint
							.getAddressingVersion() == EndPointAddressingVersion.FINAL) ? "final"
							: "submission");
		}
		if (addressEndPoint.isReliableMessagingEnabled()) {
			synapseEPDef.setReliableMessagingOn(addressEndPoint
					.isReliableMessagingEnabled());
			// synapseEPDef.setWsRMPolicyKey(visualEndPoint.getReliableMessagingPolicy().getKeyValue());
		}

		if (addressEndPoint.isSecurityEnabled()) {
			synapseEPDef.setSecurityOn(true);
			// synapseEPDef.setWsSecPolicyKey(visualEndPoint.getSecurityPolicy().getKeyValue());
		}

		synapseEPDef.setRetryDurationOnTimeout((int) (addressEndPoint
				.getRetryDelay()));
		if (ValidationUtil.isInt(addressEndPoint.getRetryErrorCodes()))
			synapseEPDef.addRetryDisabledErrorCode(ValidationUtil
					.getInt(addressEndPoint.getRetryErrorCodes()));
		if (ValidationUtil.isInt(addressEndPoint.getSuspendErrorCodes()))
			synapseEPDef.addSuspendErrorCode(ValidationUtil
					.getInt(addressEndPoint.getSuspendErrorCodes()));

		synapseAddEP.setDefinition(synapseEPDef);*/
		// sendMediator.setEndpoint(synapseAddEP);

		Endpoint endPoint = (Endpoint) synapseAddEP;
		endPoints.add(endPoint);

		// Next node may be a Failover endPoint. So that this should be edited
		// to be compatible with that also.
/*		info.setParentSequence(info.getOriginOutSequence());
		info.setTraversalDirection(TransformationInfo.TRAVERSAL_DIRECTION_OUT);*/

		// Transform endpoint output data flow.

		transformEndpointOutflow(info);

		// return synapseAddEP;
	}

	
	
	public AddressEndpoint create(AddressEndPoint visualEndPoint, String name){ 
		AddressEndPoint addressEndPoint = visualEndPoint;	
		AddressEndpoint synapseAddEP = new AddressEndpoint();
		
		if(StringUtils.isNotBlank(name)){
			synapseAddEP.setName(name);
		}
		
		createAdvanceOptions(addressEndPoint,synapseAddEP);
		synapseAddEP.getDefinition().setAddress(addressEndPoint.getURI());
		
/*		
		EndpointDefinition synapseEPDef = new EndpointDefinition();
		synapseEPDef.setAddress(addressEndPoint.getURI());
		// TODO: Configure endpoint with values extracted from the visual model.

		// synapseEPDef.setCharSetEncoding(charSetEncoding);
		if (addressEndPoint.isAddressingEnabled()) {
			synapseEPDef.setAddressingOn(true);
			synapseEPDef.setUseSeparateListener(addressEndPoint
					.isAddressingSeparateListener());
			synapseEPDef
					.setAddressingVersion((addressEndPoint
							.getAddressingVersion() == EndPointAddressingVersion.FINAL) ? "final"
							: "submission");
		}
		if (addressEndPoint.isReliableMessagingEnabled()) {
			synapseEPDef.setReliableMessagingOn(addressEndPoint
					.isReliableMessagingEnabled());
			// synapseEPDef.setWsRMPolicyKey(visualEndPoint.getReliableMessagingPolicy().getKeyValue());
		}

		if (addressEndPoint.isSecurityEnabled()) {
			synapseEPDef.setSecurityOn(true);
			// synapseEPDef.setWsSecPolicyKey(visualEndPoint.getSecurityPolicy().getKeyValue());
		}

		synapseEPDef.setRetryDurationOnTimeout((int) (addressEndPoint
				.getRetryDelay()));
		if (ValidationUtil.isInt(addressEndPoint.getRetryErrorCodes()))
			synapseEPDef.addRetryDisabledErrorCode(ValidationUtil
					.getInt(addressEndPoint.getRetryErrorCodes()));
		if (ValidationUtil.isInt(addressEndPoint.getSuspendErrorCodes()))
			synapseEPDef.addSuspendErrorCode(ValidationUtil
					.getInt(addressEndPoint.getSuspendErrorCodes()));

		synapseAddEP.setDefinition(synapseEPDef);
		*/
		return synapseAddEP;
	} 

	public void transformWithinSequence(TransformationInfo information,
			EsbNode subject, SequenceMediator sequence) throws Exception {
		
		Assert.isTrue(subject instanceof AddressEndPoint, "Invalid subject");
		AddressEndPoint visualEndPoint = (AddressEndPoint) subject;
		Endpoint synapseEP = create(visualEndPoint, visualEndPoint.getEndPointName());
		setEndpointToSendOrCallMediator(sequence, synapseEP);
	}

}
