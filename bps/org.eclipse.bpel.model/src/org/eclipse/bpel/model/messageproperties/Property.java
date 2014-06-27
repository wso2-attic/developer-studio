/**
 * <copyright>
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * </copyright>
 *
 * $Id: Property.java,v 1.1 2005/11/29 18:50:26 james Exp $
 */
package org.eclipse.bpel.model.messageproperties;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsdl.ExtensibilityElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.bpel.model.messageproperties.Property#getQName <em>QName</em>}</li>
 *   <li>{@link org.eclipse.bpel.model.messageproperties.Property#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.bpel.model.messageproperties.Property#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.bpel.model.messageproperties.Property#getID <em>ID</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.bpel.model.messageproperties.MessagepropertiesPackage#getProperty()
 * @model
 * @generated
 */
public interface Property extends ExtensibilityElement {
	/**
	 * Returns the value of the '<em><b>QName</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>QName</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>QName</em>' attribute.
	 * @see #setQName(QName)
	 * @see org.eclipse.bpel.model.messageproperties.MessagepropertiesPackage#getProperty_QName()
	 * @model dataType="org.eclipse.wst.wsdl.QName"
	 * @generated
	 */
	QName getQName();

	/**
	 * Sets the value of the '{@link org.eclipse.bpel.model.messageproperties.Property#getQName <em>QName</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>QName</em>' attribute.
	 * @see #getQName()
	 * @generated
	 */
	void setQName(QName value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.bpel.model.messageproperties.MessagepropertiesPackage#getProperty_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.bpel.model.messageproperties.Property#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(Object)
	 * @see org.eclipse.bpel.model.messageproperties.MessagepropertiesPackage#getProperty_Type()
	 * @model
	 * @generated
	 */
	Object getType();

	/**
	 * Sets the value of the '{@link org.eclipse.bpel.model.messageproperties.Property#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(Object value);

	/**
	 * Returns the value of the '<em><b>ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>ID</em>' attribute.
	 * @see org.eclipse.bpel.model.messageproperties.MessagepropertiesPackage#getProperty_ID()
	 * @model id="true" transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	String getID();

} // Property
