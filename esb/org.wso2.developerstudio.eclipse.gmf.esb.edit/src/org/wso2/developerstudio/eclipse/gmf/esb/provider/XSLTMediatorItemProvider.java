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
import org.wso2.developerstudio.eclipse.gmf.esb.XSLTMediator;

/**
 * This is the item provider adapter for a {@link org.wso2.developerstudio.eclipse.gmf.esb.XSLTMediator} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class XSLTMediatorItemProvider
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
	public XSLTMediatorItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */	
	
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		XSLTMediator xsltMediator = (XSLTMediator) object;
		if (itemPropertyDescriptors != null) {
			itemPropertyDescriptors.clear();
		}
		super.getPropertyDescriptors(object);

			addXsltSchemaKeyTypePropertyDescriptor(object);
			switch (xsltMediator.getXsltSchemaKeyType()) {
			case STATIC:
				addXsltStaticSchemaKeyPropertyDescriptor(object);
				break;
			case DYNAMIC:
				addXsltDynamicSchemaKeyPropertyDescriptor(object);
				break;
			}

			addSourceXPathPropertyDescriptor(object);
			addPropertiesPropertyDescriptor(object);
			addResourcesPropertyDescriptor(object);
			addFeaturesPropertyDescriptor(object);
			addDescriptionPropertyDescriptor(object);
		
		return itemPropertyDescriptors;
	}	
	
	/**
	 * This adds a property descriptor for the Xslt Schema Key Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addXsltSchemaKeyTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XSLTMediator_xsltSchemaKeyType_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XSLTMediator_xsltSchemaKeyType_feature", "_UI_XSLTMediator_type"),
				 EsbPackage.Literals.XSLT_MEDIATOR__XSLT_SCHEMA_KEY_TYPE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Xslt Key feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected void addXsltKeyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XSLTMediator_xsltKey_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XSLTMediator_xsltKey_feature", "_UI_XSLTMediator_type"),
				 EsbPackage.Literals.XSLT_MEDIATOR__XSLT_KEY,
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
				 getString("_UI_XSLTMediator_sourceXPath_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XSLTMediator_sourceXPath_feature", "_UI_XSLTMediator_type"),
				 EsbPackage.Literals.XSLT_MEDIATOR__SOURCE_XPATH,
				 true,
				 false,
				 false,
				 null,
				 "Basic",
				 null));
	}
	
	/**
	 * This adds a property descriptor for the Source XPath feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected void addPropertiesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XSLTMediator_properties_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XSLTMediator_properties_feature", "_UI_XSLTMediator_type"),
				 EsbPackage.Literals.XSLT_MEDIATOR__PROPERTIES,
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
	protected void addResourcesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XSLTMediator_resources_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XSLTMediator_resources_feature", "_UI_XSLTMediator_type"),
				 EsbPackage.Literals.XSLT_MEDIATOR__RESOURCES,
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
	protected void addFeaturesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_XSLTMediator_features_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_XSLTMediator_resources_feature", "_UI_XSLTMediator_type"),
				 EsbPackage.Literals.XSLT_MEDIATOR__FEATURES,
				 true,
				 false,
				 false,
				 null,
				 null,
				 null));
	}
	
	protected void addXsltStaticSchemaKeyPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_XSLTMediator_xsltStaticSchemaKey_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_XSLTMediator_xsltStaticSchemaKey_feature", "_UI_XSLTMediator_type"),
                 EsbPackage.Literals.XSLT_MEDIATOR__XSLT_STATIC_SCHEMA_KEY,
                 true,
                 false,
                 true,
                 null,
                 "Basic",
                 null));
    }
	
	protected void addXsltDynamicSchemaKeyPropertyDescriptor(Object object) {
        itemPropertyDescriptors.add
            (createItemPropertyDescriptor
                (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                 getResourceLocator(),
                 getString("_UI_XSLTMediator_xsltDynamicSchemaKey_feature"),
                 getString("_UI_PropertyDescriptor_description", "_UI_XSLTMediator_xsltDynamicSchemaKey_feature", "_UI_XSLTMediator_type"),
                 EsbPackage.Literals.XSLT_MEDIATOR__XSLT_DYNAMIC_SCHEMA_KEY,
                 true,
                 false,
                 true,
                 null,
                 "Basic",
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
			childrenFeatures.add(EsbPackage.Literals.XSLT_MEDIATOR__INPUT_CONNECTOR);
			childrenFeatures.add(EsbPackage.Literals.XSLT_MEDIATOR__OUTPUT_CONNECTOR);
			childrenFeatures.add(EsbPackage.Literals.XSLT_MEDIATOR__XSLT_STATIC_SCHEMA_KEY);
			childrenFeatures.add(EsbPackage.Literals.XSLT_MEDIATOR__XSLT_DYNAMIC_SCHEMA_KEY);
			childrenFeatures.add(EsbPackage.Literals.XSLT_MEDIATOR__XSLT_KEY);
			childrenFeatures.add(EsbPackage.Literals.XSLT_MEDIATOR__SOURCE_XPATH);
			childrenFeatures.add(EsbPackage.Literals.XSLT_MEDIATOR__PROPERTIES);
			childrenFeatures.add(EsbPackage.Literals.XSLT_MEDIATOR__FEATURES);
			childrenFeatures.add(EsbPackage.Literals.XSLT_MEDIATOR__RESOURCES);
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
	 * This returns XSLTMediator.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/XSLTMediator"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	
	@Override
	public String getText(Object object) {
		String label = ((XSLTMediator)object).getDescription();
		return label == null || label.length() == 0 ?
			getString("_UI_XSLTMediator_type") :
			getString("_UI_XSLTMediator_type") + " " + label;
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

		switch (notification.getFeatureID(XSLTMediator.class)) {
			case EsbPackage.XSLT_MEDIATOR__XSLT_SCHEMA_KEY_TYPE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case EsbPackage.XSLT_MEDIATOR__INPUT_CONNECTOR:
			case EsbPackage.XSLT_MEDIATOR__OUTPUT_CONNECTOR:
			case EsbPackage.XSLT_MEDIATOR__XSLT_STATIC_SCHEMA_KEY:
			case EsbPackage.XSLT_MEDIATOR__XSLT_DYNAMIC_SCHEMA_KEY:
			case EsbPackage.XSLT_MEDIATOR__XSLT_KEY:
			case EsbPackage.XSLT_MEDIATOR__SOURCE_XPATH:
			case EsbPackage.XSLT_MEDIATOR__PROPERTIES:
			case EsbPackage.XSLT_MEDIATOR__FEATURES:
			case EsbPackage.XSLT_MEDIATOR__RESOURCES:
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
				(EsbPackage.Literals.XSLT_MEDIATOR__INPUT_CONNECTOR,
				 EsbFactory.eINSTANCE.createXSLTMediatorInputConnector()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.XSLT_MEDIATOR__OUTPUT_CONNECTOR,
				 EsbFactory.eINSTANCE.createXSLTMediatorOutputConnector()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.XSLT_MEDIATOR__XSLT_STATIC_SCHEMA_KEY,
				 EsbFactory.eINSTANCE.createRegistryKeyProperty()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.XSLT_MEDIATOR__XSLT_DYNAMIC_SCHEMA_KEY,
				 EsbFactory.eINSTANCE.createNamespacedProperty()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.XSLT_MEDIATOR__XSLT_KEY,
				 EsbFactory.eINSTANCE.createRegistryKeyProperty()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.XSLT_MEDIATOR__SOURCE_XPATH,
				 EsbFactory.eINSTANCE.createNamespacedProperty()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.XSLT_MEDIATOR__PROPERTIES,
				 EsbFactory.eINSTANCE.createXSLTProperty()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.XSLT_MEDIATOR__FEATURES,
				 EsbFactory.eINSTANCE.createXSLTFeature()));

		newChildDescriptors.add
			(createChildParameter
				(EsbPackage.Literals.XSLT_MEDIATOR__RESOURCES,
				 EsbFactory.eINSTANCE.createXSLTResource()));
	}

	/**
	 * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
		Object childFeature = feature;
		Object childObject = child;

		boolean qualify =
			childFeature == EsbPackage.Literals.XSLT_MEDIATOR__XSLT_STATIC_SCHEMA_KEY ||
			childFeature == EsbPackage.Literals.XSLT_MEDIATOR__XSLT_KEY ||
			childFeature == EsbPackage.Literals.XSLT_MEDIATOR__XSLT_DYNAMIC_SCHEMA_KEY ||
			childFeature == EsbPackage.Literals.XSLT_MEDIATOR__SOURCE_XPATH;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

}
