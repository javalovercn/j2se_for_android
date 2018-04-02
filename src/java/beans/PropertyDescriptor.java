/*
 * Copyright (c) 1999, 2007, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package java.beans;

import java.lang.ref.Reference;
import java.lang.reflect.Method;

/**
 * A PropertyDescriptor describes one property that a Java Bean exports via a
 * pair of accessor methods.
 */
public class PropertyDescriptor extends FeatureDescriptor {

	private Reference<Class> propertyTypeRef;
	private Reference<Method> readMethodRef;
	private Reference<Method> writeMethodRef;
	private Reference<Class> propertyEditorClassRef;

	private boolean bound;
	private boolean constrained;

	private String baseName;

	private String writeMethodName;
	private String readMethodName;

	public PropertyDescriptor(String propertyName, Class<?> beanClass)
			throws IntrospectionException {
		this(propertyName, beanClass, "", "");
	}

	public PropertyDescriptor(String propertyName, Class<?> beanClass, String readMethodName,
			String writeMethodName) throws IntrospectionException {
	}

	public PropertyDescriptor(String propertyName, Method readMethod, Method writeMethod)
			throws IntrospectionException {
	}

	PropertyDescriptor(Class<?> bean, String base, Method read, Method write)
			throws IntrospectionException {
	}

	public synchronized Class<?> getPropertyType() {
		return null;
	}

	private void setPropertyType(Class type) {
	}

	private Class getPropertyType0() {
		return null;
	}

	public synchronized Method getReadMethod() {
		return null;
	}

	public synchronized void setReadMethod(Method readMethod) throws IntrospectionException {
	}

	public synchronized Method getWriteMethod() {
		return null;
	}

	public synchronized void setWriteMethod(Method writeMethod) throws IntrospectionException {
	}

	private Method getReadMethod0() {
		return null;
	}

	private Method getWriteMethod0() {
		return null;
	}

	void setClass0(Class clz) {
	}

	public boolean isBound() {
		return bound;
	}

	public void setBound(boolean bound) {
		this.bound = bound;
	}

	public boolean isConstrained() {
		return constrained;
	}

	public void setConstrained(boolean constrained) {
		this.constrained = constrained;
	}

	public void setPropertyEditorClass(Class<?> propertyEditorClass) {
	}

	public Class<?> getPropertyEditorClass() {
		return null;
	}

	public PropertyEditor createPropertyEditor(Object bean) {
		return null;
	}

	public boolean equals(Object obj) {
		return false;
	}

	boolean compareMethods(Method a, Method b) {
		return false;
	}

	PropertyDescriptor(PropertyDescriptor x, PropertyDescriptor y) {
		super(x, y);
	}

	PropertyDescriptor(PropertyDescriptor old) {
		super(old);
	}

	void updateGenericsFor(Class<?> type) {
	}

	private Class findPropertyType(Method readMethod, Method writeMethod)
			throws IntrospectionException {
		return null;
	}

	public int hashCode() {
		return 0;
	}

	String getBaseName() {
		return baseName;
	}

	void appendTo(StringBuilder sb) {
		appendTo(sb, "bound", this.bound);
		appendTo(sb, "constrained", this.constrained);
		appendTo(sb, "propertyEditorClass", this.propertyEditorClassRef);
		appendTo(sb, "propertyType", this.propertyTypeRef);
		appendTo(sb, "readMethod", this.readMethodRef);
		appendTo(sb, "writeMethod", this.writeMethodRef);
	}

	private boolean isAssignable(Method m1, Method m2) {
		return false;
	}
}
