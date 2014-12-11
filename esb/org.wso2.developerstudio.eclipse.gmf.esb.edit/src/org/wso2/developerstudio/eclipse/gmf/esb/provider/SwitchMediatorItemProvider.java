/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.wso2.developerstudio.eclipse.gmf.esb.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.wso2.developerstudio.eclipse.gmf.esb.EsbFactory;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbPackage;
import org.wso2.developerstudio.eclipse.gmf.esb.SwitchMediator;

/**
 * This is the item provider adapter for a {@link org.wso2.developerstudio.eclipse.gmf.esb.SwitchMediator} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SwitchMediatorItemProvider
	extends MediatorItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SwitchMediatorItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		SwitchMediator switchMediator = (SwitchMediator) object;
		if (itemPropertyDescriptors != null) {
			itemPropertyDescriptors.clear();
		}
		super.getPropertyDescriptors(object);

		addSourceXPathPropertyDescriptor(object);
		addConfigureCaseBranchesPropertyDescriptor(object);
		addDescriptionPropertyDescriptor(object);

		return itemPropertyDescriptors;
	}
	
	/**
	 * This adds a property descriptor for the Source feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSourcePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwitchMediator_source_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwitchMediator_source_feature", "_UI_SwitchMediator_type"),
				 EsbPackage.Literals.SWITCH_MEDIATOR__SOURCE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Namespace feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNamespacePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwitchMediator_namespace_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwitchMediator_namespace_feature", "_UI_SwitchMediator_type"),
				 EsbPackage.Literals.SWITCH_MEDIATOR__NAMESPACE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Namespace Prefix feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNamespacePrefixPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwitchMediator_namespacePrefix_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwitchMediator_namespacePrefix_feature", "_UI_SwitchMediator_type"),
				 EsbPackage.Literals.SWITCH_MEDIATOR__NAMESPACE_PREFIX,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for configuring case branches.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected void addConfigureCaseBranchesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwitchMediator_caseBranches_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XSLTMediator_xsltKey_feature", "_UI_SwitchMediator_type"),
				 EsbPackage.Literals.SWITCH_MEDIATOR__CASE_BRANCHES,
				 true,
				 false,
				 false,
				 null,
				 null,
				 null));
	}
	
	/**
	 * This adds a property descriptor for the Source XPath feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected void addSourceXPathPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwitchMediator_sourceXpath_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwitchMediator_sourceXpath_feature", "_UI_SwitchMediator_type"),
				 EsbPackage.Literals.SWITCH_MEDIATOR__SOURCE_XPATH,
				 true,
				 false,
				 false,
				 null,
				 null,
				 null));
	}
	

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(EsbPackage.Literals.SWITCH_MEDIATOR__SOURCE_XPATH);
			childrenFeatures.add(EsbPackage.Literals.SWITCH_MEDIATOR__CASE_BRANCHES);
			childrenFeatures.add(EsbPackage.Literals.SWITCH_MEDIATOR__DEFAULT_BRANCH);
			childrenFeatures.add(EsbPackage.Literals.SWITCH_MEDIATOR__INPUT_CONNECTOR);
			childrenFeatures.add(EsbPackage.Literals.SWITCH_MEDIATOR__OUTPUT_CONNECTOR);
			childrenFeatures.add(EsbPackage.Literals.SWITCH_MEDIATOR__SWITCH_CONTAINER);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns SwitchMediator.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/SwitchMediator"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	
	@Override
	public String getText(Object object) {
		String label = ((SwitchMediator)object).getNamespace();
		return label == null || label.length() == 0 ?
			getString("_UI_SwitchMediator_type") :
			getString("_UI_SwitchMediator_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(SwitchMediator.class)) {
			case EsbPackage.SWITCH_MEDIATOR__SOURCE:
			case EsbPackage.SWITCH_MEDIATOR__NAMESPACE:
			case EsbPackage.SWITCH_MEDIATOR__NAMESPACE_PREFIX:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case EsbPackage.SWITCH_MEDIATOR__SOURCE_XPATH:
			case EsbPackage.SWITCH_MEDIATOR__CASE_BRANCHES:
			case EsbPackage.SWITCH_MEDIATOR__DEFAULT_BRANCH:
			case EsbPackage.SWITCH_MEDIATOR__INPUT_CONNECTOR:
			case EsbPackage.SWITCH_MEDIATOR__OUTPUT_CONNECTOR:
			case EsbPackage.SWITCH_MEDIATOR__SWITCH_CONTAINER:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.SWITCH_MEDIATOR__SOURCE_XPATH,
				 EsbFactory.eINSTANCE.createNamespacedProperty()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.SWITCH_MEDIATOR__CASE_BRANCHES,
				 EsbFactory.eINSTANCE.createSwitchCaseBranchOutputConnector()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.SWITCH_MEDIATOR__DEFAULT_BRANCH,
				 EsbFactory.eINSTANCE.createSwitchDefaultBranchOutputConnector()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.SWITCH_MEDIATOR__INPUT_CONNECTOR,
				 EsbFactory.eINSTANCE.createSwitchMediatorInputConnector()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.SWITCH_MEDIATOR__OUTPUT_CONNECTOR,
				 EsbFactory.eINSTANCE.createSwitchMediatorOutputConnector()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.SWITCH_MEDIATOR__SWITCH_CONTAINER,
				 EsbFactory.eINSTANCE.createSwitchMediatorContainer()));
	}

}
