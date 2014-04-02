/*
 * Copyright WSO2, Inc. (http://wso2.com)
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
import org.apache.synapse.mediators.builtin.SendMediator;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;
import org.wso2.developerstudio.eclipse.gmf.esb.AddressEndPoint;
import org.wso2.developerstudio.eclipse.gmf.esb.EndPoint;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbNode;
import org.wso2.developerstudio.eclipse.gmf.esb.HTTPEndpoint;
import org.wso2.developerstudio.eclipse.gmf.esb.InputConnector;
import org.wso2.developerstudio.eclipse.gmf.esb.Sequence;
import org.wso2.developerstudio.eclipse.gmf.esb.SequenceInputConnector;
import org.wso2.developerstudio.eclipse.gmf.esb.persistence.TransformationInfo;

import com.damnhandy.uri.template.UriTemplate;

public class HTTPEndPointTransformer extends AbstractEndpointTransformer {

	public void transform(TransformationInfo information, EsbNode subject) throws Exception {
		// Check subject.
		Assert.isTrue(subject instanceof HTTPEndpoint, "Invalid subject");
		HTTPEndpoint visualEndPoint = (HTTPEndpoint) subject;
		Endpoint synapseEP = create(visualEndPoint, visualEndPoint.getEndPointName());
		setEndpointToSendCallOrProxy(information, visualEndPoint, synapseEP);
		
		if (!information.isEndPointFound) {
			information.isEndPointFound = true;
			information.firstEndPoint = visualEndPoint;
		}

		if(visualEndPoint.getOutputConnector()!=null){
			if(visualEndPoint.getOutputConnector().getOutgoingLink() !=null){
			InputConnector nextInputConnector=visualEndPoint.getOutputConnector().getOutgoingLink().getTarget();
			if((!(nextInputConnector instanceof SequenceInputConnector))||
					((((Sequence)nextInputConnector.eContainer()).getOutputConnector().get(0).getOutgoingLink()!=null)&&(!(((Sequence)nextInputConnector.eContainer()).getOutputConnector().get(0).getOutgoingLink().getTarget().eContainer() instanceof EndPoint)))){
				information.setParentSequence(information.getOriginOutSequence());
				information.setTraversalDirection(TransformationInfo.TRAVERSAL_DIRECTION_OUT);
			}else if((visualEndPoint.getInputConnector().getIncomingLinks().get(0).getSource().eContainer() instanceof Sequence)){
				information.setParentSequence(information.getCurrentReferredSequence());
			}
			}
		}		

		List<EsbNode> transformedMediators = information.getTransformedMediators();
		if (visualEndPoint.getOutputConnector() != null	&& visualEndPoint.getOutputConnector().getOutgoingLink() != null) {
			EsbNode nextElement = (EsbNode) visualEndPoint.getOutputConnector().getOutgoingLink().getTarget().eContainer();
			if (transformedMediators.contains(nextElement)) {
				return;
			}
			transformedMediators.add(nextElement);
		}

		
		// Transform endpoint output data flow.
		doTransform(information, visualEndPoint.getOutputConnector());

	}
	
	public org.apache.synapse.endpoints.HTTPEndpoint create(HTTPEndpoint visualEndPoint,String name){ 
		HTTPEndpoint httpEndPoint = visualEndPoint;	
		org.apache.synapse.endpoints.HTTPEndpoint synapseHttpEP = new org.apache.synapse.endpoints.HTTPEndpoint();
		
		if(StringUtils.isNotBlank(name)){
			synapseHttpEP.setName(name);
		}
		
		createAdvanceOptions(httpEndPoint,synapseHttpEP);
		if (httpEndPoint.getURITemplate() != null ) {
			UriTemplate template = UriTemplate.fromTemplate(httpEndPoint.getURITemplate());
			synapseHttpEP.setUriTemplate(template);
		}
		
		if (visualEndPoint.getHttpMethod() != null) {
			synapseHttpEP.setHttpMethod(visualEndPoint.getHttpMethod().getName().toLowerCase());
		}

		return synapseHttpEP;
	} 

	public void createSynapseObject(TransformationInfo info, EObject subject,
			List<Endpoint> endPoints) {

		Assert.isTrue(subject instanceof HTTPEndpoint, "Invalid subject");
		HTTPEndpoint httpEndPoint = (HTTPEndpoint) subject;	
		org.apache.synapse.endpoints.HTTPEndpoint synapseHttpEP = new org.apache.synapse.endpoints.HTTPEndpoint();
		
		createAdvanceOptions(httpEndPoint,synapseHttpEP);
		UriTemplate template = UriTemplate.fromTemplate(httpEndPoint.getURITemplate());
		synapseHttpEP.setUriTemplate(template);
		
		Endpoint endPoint = (Endpoint) synapseHttpEP;
		endPoints.add(endPoint);

		// Next node may be a Failover endPoint. So that this should be edited
		// to be compatible with that also.
/*		info.setParentSequence(info.getOriginOutSequence());
		info.setTraversalDirection(TransformationInfo.TRAVERSAL_DIRECTION_OUT);*/

		transformEndpointOutflow(info);
	}

	public void transformWithinSequence(TransformationInfo information, EsbNode subject,
			SequenceMediator sequence) throws Exception {

		Assert.isTrue(subject instanceof HTTPEndpoint, "Invalid subject");
		HTTPEndpoint visualEndPoint = (HTTPEndpoint) subject;
		Endpoint synapseEP = create(visualEndPoint, visualEndPoint.getEndPointName());
		setEndpointToSendOrCallMediator(sequence, synapseEP);
	}

}
