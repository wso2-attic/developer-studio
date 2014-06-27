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

package org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.deserializer;

import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__PROCESSOR_TYPE;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__PROCESSOR_NAME;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__MESSAGE_STORE;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__RETRY_INTERVAL;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__MAX_DELIVERY_ATTEMPTS;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__AXIS2_CLIENT_REPOSITORY;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__AXIS2_CONFIGURATION;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__REPLY_SEQUENCE_NAME;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__FAULT_SEQUENCE_NAME;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__QUARTZ_CONFIG_FILE_PATH;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__CRON_EXPRESSION;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__PINNED_SERVERS;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__SEQUENCE;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__MESSAGE_PROCESSOR_PROVIDER;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__FORWARDING_INTERVAL;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__PROCESSOR_STATE;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__ENDPOINT_NAME;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__SAMPLING_INTERVAL;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__SAMPLING_CONCURRENCY;
import static org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage.Literals.MESSAGE_PROCESSOR__NON_RETRY_HTTP_STATUS_CODES;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.synapse.message.processor.MessageProcessor;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbFactory;
import org.wso2.developerstudio.eclipse.gmf.esb.MessageProcessorParameter;
import org.wso2.developerstudio.eclipse.gmf.esb.MessageProcessorType;
import org.wso2.developerstudio.eclipse.gmf.esb.ProcessorState;
import org.wso2.developerstudio.eclipse.gmf.esb.RegistryKeyProperty;
import org.wso2.developerstudio.eclipse.gmf.esb.diagram.providers.EsbElementTypes;
import org.wso2.developerstudio.eclipse.gmf.esb.internal.persistence.custom.DummyMessageProcessor;

/**
 * Deserializes a message-processor configuration to a graphical message
 * processor object
 */
public class MessageProcessorDeserializer
extends
AbstractEsbNodeDeserializer<MessageProcessor, org.wso2.developerstudio.eclipse.gmf.esb.MessageProcessor> {

	// Fixing TOOLS-2026.
	private static final String scheduledMessageForwardingProcessorOld = "org.apache.synapse.message.processors.forward.ScheduledMessageForwardingProcessor";
	private static final String messageSamplingProcessorOld = "org.apache.synapse.message.processors.sampler.SamplingProcessor";
	private static final String scheduledMessageForwardingProcessor = "org.apache.synapse.message.processor.impl.forwarder.ScheduledMessageForwardingProcessor";
	private static final String messageSamplingProcessor = "org.apache.synapse.message.processor.impl.sampler.SamplingProcessor";
	
	@Override
	public org.wso2.developerstudio.eclipse.gmf.esb.MessageProcessor createNode(
			IGraphicalEditPart part, MessageProcessor processor) {
		org.wso2.developerstudio.eclipse.gmf.esb.MessageProcessor messageProcessor = (org.wso2.developerstudio.eclipse.gmf.esb.MessageProcessor) DeserializerUtils
				.createNode(part, EsbElementTypes.MessageProcessor_3701);
		setElementToEdit(messageProcessor);

		if (processor instanceof DummyMessageProcessor) {
			DummyMessageProcessor dummyMessageProcessor = (DummyMessageProcessor) processor;
			if (StringUtils.isNotBlank(dummyMessageProcessor.getClassName())) {
				if (dummyMessageProcessor.getClassName()
						.equals(scheduledMessageForwardingProcessor)
						|| dummyMessageProcessor.getClassName().equals(
								scheduledMessageForwardingProcessorOld)) {
					// Scheduled Message Forwarding Processor
					executeSetValueCommand(MESSAGE_PROCESSOR__PROCESSOR_TYPE,
							MessageProcessorType.SCHEDULED_MSG_FORWARDING);
					Map<String, Object> parameters = dummyMessageProcessor.getParameters();

					// Endpoint name.
					if (StringUtils.isNotBlank(processor.getTargetEndpoint())) {
						RegistryKeyProperty endpointName = EsbFactory.eINSTANCE
								.createRegistryKeyProperty();
						endpointName.setKeyValue(processor.getTargetEndpoint());
						executeSetValueCommand(MESSAGE_PROCESSOR__ENDPOINT_NAME, endpointName);
					}

					// Parameters.
					if (parameters.containsKey("client.retry.interval")) {
						Object value = parameters.get("client.retry.interval");
						if (value != null && StringUtils.isNumeric(value.toString())) {
							executeSetValueCommand(MESSAGE_PROCESSOR__RETRY_INTERVAL, new Long(
									value.toString()));
						}
					}
					if (parameters.containsKey("interval")) {
						Object value = parameters.get("interval");
						if (value != null && StringUtils.isNumeric(value.toString())) {
							executeSetValueCommand(MESSAGE_PROCESSOR__FORWARDING_INTERVAL,
									new Long(value.toString()));
						}
					}
					if (parameters.containsKey("max.delivery.attempts")) {
						Object value = parameters.get("max.delivery.attempts");
						if (value != null) {
							try {
								executeSetValueCommand(MESSAGE_PROCESSOR__MAX_DELIVERY_ATTEMPTS,
										new Integer(value.toString()));
							} catch(NumberFormatException e) {
								//set default value -1
								executeSetValueCommand(MESSAGE_PROCESSOR__MAX_DELIVERY_ATTEMPTS, -1);
							}
							
						}
					}
					if (parameters.containsKey("axis2.repo")) {
						Object value = parameters.get("axis2.repo");
						if (StringUtils.isNotBlank(value.toString())) {
							executeSetValueCommand(MESSAGE_PROCESSOR__AXIS2_CLIENT_REPOSITORY,
									value.toString());
						}
					}
					if (parameters.containsKey("axis2.config")) {
						Object value = parameters.get("axis2.config");
						if (StringUtils.isNotBlank(value.toString())) {
							executeSetValueCommand(MESSAGE_PROCESSOR__AXIS2_CONFIGURATION,
									value.toString());
						}
					}
					if (parameters.containsKey("message.processor.reply.sequence")) {
						Object value = parameters.get("message.processor.reply.sequence");
						if (StringUtils.isNotBlank(value.toString())) {
							RegistryKeyProperty replaySequence = EsbFactory.eINSTANCE
									.createRegistryKeyProperty();
							replaySequence.setKeyValue(value.toString());
							executeSetValueCommand(MESSAGE_PROCESSOR__REPLY_SEQUENCE_NAME,
									replaySequence);
						}
					}
					if (parameters.containsKey("message.processor.fault.sequence")) {
						Object value = parameters.get("message.processor.fault.sequence");
						if (StringUtils.isNotBlank(value.toString())) {
							RegistryKeyProperty faultSequence = EsbFactory.eINSTANCE
									.createRegistryKeyProperty();
							faultSequence.setKeyValue(value.toString());
							executeSetValueCommand(MESSAGE_PROCESSOR__FAULT_SEQUENCE_NAME,
									faultSequence);
						}
					}
					if (parameters.containsKey("quartz.conf")) {
						Object value = parameters.get("quartz.conf");
						if (StringUtils.isNotBlank(value.toString())) {
							executeSetValueCommand(MESSAGE_PROCESSOR__QUARTZ_CONFIG_FILE_PATH,
									value.toString());
						}
					}
					if (parameters.containsKey("cronExpression")) {
						Object value = parameters.get("cronExpression");
						if (StringUtils.isNotBlank(value.toString())) {
							executeSetValueCommand(MESSAGE_PROCESSOR__CRON_EXPRESSION,
									value.toString());
						}
					}
					if (parameters.containsKey("pinnedServers")) {
						Object value = parameters.get("pinnedServers");
						if (StringUtils.isNotBlank(value.toString())) {
							executeSetValueCommand(MESSAGE_PROCESSOR__PINNED_SERVERS,
									value.toString());
						}
					}
					if (parameters.containsKey("is.active")) {
						Object value = parameters.get("is.active");
						if (StringUtils.isNotBlank(value.toString())) {
							if ("true".equals(value)) {
								executeSetValueCommand(MESSAGE_PROCESSOR__PROCESSOR_STATE,
										ProcessorState.ACTIVATE);
							} else {
								executeSetValueCommand(MESSAGE_PROCESSOR__PROCESSOR_STATE,
										ProcessorState.DEACTIVATE);
							}
						}
					}
					if (parameters.containsKey("non.retry.status.codes")) {
						Object value = parameters.get("non.retry.status.codes");
						if (StringUtils.isNotBlank(value.toString())) {
							executeSetValueCommand(MESSAGE_PROCESSOR__NON_RETRY_HTTP_STATUS_CODES,
									value.toString());
						}
					}
				} else if (dummyMessageProcessor.getClassName().equals(messageSamplingProcessor)
						|| dummyMessageProcessor.getClassName().equals(messageSamplingProcessorOld)) {
					// Message Sampling Processor
					executeSetValueCommand(MESSAGE_PROCESSOR__PROCESSOR_TYPE,
							MessageProcessorType.MSG_SAMPLING);
					Map<String, Object> parameters = dummyMessageProcessor.getParameters();

					if (parameters.containsKey("sequence")) {
						Object value = parameters.get("sequence");
						if (StringUtils.isNotBlank(value.toString())) {
							RegistryKeyProperty sequence = EsbFactory.eINSTANCE
									.createRegistryKeyProperty();
							sequence.setKeyValue(value.toString());
							executeSetValueCommand(MESSAGE_PROCESSOR__SEQUENCE, sequence);
						}
					}
					if (parameters.containsKey("interval")) {
						Object value = parameters.get("interval");
						if (value != null && StringUtils.isNumeric(value.toString())) {
							executeSetValueCommand(MESSAGE_PROCESSOR__SAMPLING_INTERVAL, new Long(
									value.toString()));
						}
					}
					if (parameters.containsKey("concurrency")) {
						Object value = parameters.get("concurrency");
						if (value != null && StringUtils.isNumeric(value.toString())) {
							executeSetValueCommand(MESSAGE_PROCESSOR__SAMPLING_CONCURRENCY,
									new Integer(value.toString()));
						}
					}
					if (parameters.containsKey("quartz.conf")) {
						Object value = parameters.get("quartz.conf");
						if (StringUtils.isNotBlank(value.toString())) {
							executeSetValueCommand(MESSAGE_PROCESSOR__QUARTZ_CONFIG_FILE_PATH,
									value.toString());
						}
					}
					if (parameters.containsKey("cronExpression")) {
						Object value = parameters.get("cronExpression");
						if (StringUtils.isNotBlank(value.toString())) {
							executeSetValueCommand(MESSAGE_PROCESSOR__CRON_EXPRESSION,
									value.toString());
						}
					}
					if (parameters.containsKey("pinnedServers")) {
						Object value = parameters.get("pinnedServers");
						if (StringUtils.isNotBlank(value.toString())) {
							executeSetValueCommand(MESSAGE_PROCESSOR__PINNED_SERVERS,
									value.toString());
						}
					}
					if (parameters.containsKey("is.active")) {
						Object value = parameters.get("is.active");
						if (StringUtils.isNotBlank(value.toString())) {
							if ("true".equals(value)) {
								executeSetValueCommand(MESSAGE_PROCESSOR__PROCESSOR_STATE,
										ProcessorState.ACTIVATE);
							} else {
								executeSetValueCommand(MESSAGE_PROCESSOR__PROCESSOR_STATE,
										ProcessorState.DEACTIVATE);
							}
						}
					}
				} else {
					// Custom Message Processor
					executeSetValueCommand(MESSAGE_PROCESSOR__PROCESSOR_TYPE,
							MessageProcessorType.CUSTOM);
					executeSetValueCommand(MESSAGE_PROCESSOR__MESSAGE_PROCESSOR_PROVIDER,
							dummyMessageProcessor.getClassName());

					for (Entry<String, Object> parameter : dummyMessageProcessor.getParameters()
							.entrySet()) {
						MessageProcessorParameter processorParameter = EsbFactory.eINSTANCE
								.createMessageProcessorParameter();
						processorParameter.setParameterName(parameter.getKey());
						processorParameter.setParameterValue(parameter.getValue().toString());
						executeAddValueCommand(messageProcessor.getParameters(), processorParameter);
					}
				}
			}
		}

		executeSetValueCommand(MESSAGE_PROCESSOR__PROCESSOR_NAME, processor.getName());
		executeSetValueCommand(MESSAGE_PROCESSOR__MESSAGE_STORE, processor.getMessageStoreName());

		return messageProcessor;
	}
}
