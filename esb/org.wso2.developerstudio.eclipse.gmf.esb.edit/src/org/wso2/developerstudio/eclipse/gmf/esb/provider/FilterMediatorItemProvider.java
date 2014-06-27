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
import org.wso2.developerstudio.eclipse.gmf.esb.FilterMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.FilterMediatorConditionType;

/**
 * This is the item provider adapter for a {@link org.wso2.developerstudio.eclipse.gmf.esb.FilterMediator} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class FilterMediatorItemProvider
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
	public FilterMediatorItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		FilterMediator filterMediator = (FilterMediator) object;
		if (itemPropertyDescriptors != null) {
			itemPropertyDescriptors.clear();
		}
		super.getPropertyDescriptors(object);

		addConditionTypePropertyDescriptor(object);
		if (filterMediator.getConditionType().equals(FilterMediatorConditionType.SOURCE_REGEX)) {
			addSourcePropertyDescriptor(object);
			addRegexPropertyDescriptor(object);
		} else {
			addXpathPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Condition Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addConditionTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_FilterMediator_conditionType_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_FilterMediator_conditionType_feature", "_UI_FilterMediator_type"),
				 EsbPackage.Literals.FILTER_MEDIATOR__CONDITION_TYPE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
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
				 getString("_UI_FilterMediator_source_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_FilterMediator_source_feature", "_UI_FilterMediator_type"),
				 EsbPackage.Literals.FILTER_MEDIATOR__SOURCE,
				 true,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Regex feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRegexPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_FilterMediator_regex_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_FilterMediator_regex_feature", "_UI_FilterMediator_type"),
				 EsbPackage.Literals.FILTER_MEDIATOR__REGEX,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Xpath feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addXpathPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_FilterMediator_xpath_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_FilterMediator_xpath_feature", "_UI_FilterMediator_type"),
				 EsbPackage.Literals.FILTER_MEDIATOR__XPATH,
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
			childrenFeatures.add(EsbPackage.Literals.FILTER_MEDIATOR__INPUT_CONNECTOR);
			childrenFeatures.add(EsbPackage.Literals.FILTER_MEDIATOR__OUTPUT_CONNECTOR);
			childrenFeatures.add(EsbPackage.Literals.FILTER_MEDIATOR__PASS_OUTPUT_CONNECTOR);
			childrenFeatures.add(EsbPackage.Literals.FILTER_MEDIATOR__FAIL_OUTPUT_CONNECTOR);
			childrenFeatures.add(EsbPackage.Literals.FILTER_MEDIATOR__FILTER_CONTAINER);
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
	 * This returns FilterMediator.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/FilterMediator"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	
	@Override
	public String getText(Object object) {
		String label = ((FilterMediator)object).getDescription();
		return label == null || label.length() == 0 ?
			getString("_UI_FilterMediator_type") :
			getString("_UI_FilterMediator_type") + " " + label;
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

		switch (notification.getFeatureID(FilterMediator.class)) {
			case EsbPackage.FILTER_MEDIATOR__CONDITION_TYPE:
			case EsbPackage.FILTER_MEDIATOR__REGEX:
			case EsbPackage.FILTER_MEDIATOR__XPATH:
			case EsbPackage.FILTER_MEDIATOR__SOURCE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case EsbPackage.FILTER_MEDIATOR__INPUT_CONNECTOR:
			case EsbPackage.FILTER_MEDIATOR__OUTPUT_CONNECTOR:
			case EsbPackage.FILTER_MEDIATOR__PASS_OUTPUT_CONNECTOR:
			case EsbPackage.FILTER_MEDIATOR__FAIL_OUTPUT_CONNECTOR:
			case EsbPackage.FILTER_MEDIATOR__FILTER_CONTAINER:
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
				(EsbPackage.Literals.FILTER_MEDIATOR__INPUT_CONNECTOR,
				 EsbFactory.eINSTANCE.createFilterMediatorInputConnector()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.FILTER_MEDIATOR__OUTPUT_CONNECTOR,
				 EsbFactory.eINSTANCE.createFilterMediatorOutputConnector()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.FILTER_MEDIATOR__PASS_OUTPUT_CONNECTOR,
				 EsbFactory.eINSTANCE.createFilterMediatorPassOutputConnector()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.FILTER_MEDIATOR__FAIL_OUTPUT_CONNECTOR,
				 EsbFactory.eINSTANCE.createFilterMediatorFailOutputConnector()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.FILTER_MEDIATOR__FILTER_CONTAINER,
				 EsbFactory.eINSTANCE.createFilterContainer()));
	}

}
