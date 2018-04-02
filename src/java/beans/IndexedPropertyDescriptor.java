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
 * An IndexedPropertyDescriptor describes a property that acts like an array and
 * has an indexed read and/or indexed write method to access specific elements
 * of the array.
 * <p>
 * An indexed property may also provide simple non-indexed read and write
 * methods. If these are present, they read and write arrays of the type
 * returned by the indexed read method.
 */

public class IndexedPropertyDescriptor extends PropertyDescriptor {

	private Reference<Class> indexedPropertyTypeRef;
	private Reference<Method> indexedReadMethodRef;
	private Reference<Method> indexedWriteMethodRef;

	public IndexedPropertyDescriptor(String propertyName, Class<?> beanClass)
			throws IntrospectionException {
		this(propertyName, beanClass, "", "", "", "");
	}

	public IndexedPropertyDescriptor(String propertyName, Class<?> beanClass, String readMethodName,
			String writeMethodName, String indexedReadMethodName, String indexedWriteMethodName)
			throws IntrospectionException {
		super(propertyName, beanClass, readMethodName, writeMethodName);
	}

	public IndexedPropertyDescriptor(String propertyName, Method readMethod, Method writeMethod,
			Method indexedReadMethod, Method indexedWriteMethod) throws IntrospectionException {
		super(propertyName, readMethod, writeMethod);
	}

	IndexedPropertyDescriptor(Class<?> bean, String base, Method read, Method write,
			Method readIndexed, Method writeIndexed) throws IntrospectionException {
		super(bean, base, read, write);
	}

	public synchronized Method getIndexedReadMethod() {
		return null;
	}

	public synchronized void setIndexedReadMethod(Method readMethod) throws IntrospectionException {
	}

	private void setIndexedReadMethod0(Method readMethod) {
	}

	public synchronized Method getIndexedWriteMethod() {
		return null;
	}

	public synchronized void setIndexedWriteMethod(Method writeMethod)
			throws IntrospectionException {
	}

	private void setIndexedWriteMethod0(Method writeMethod) {
	}

	public synchronized Class<?> getIndexedPropertyType() {
		return null;
	}

	private void setIndexedPropertyType(Class type) {
	}

	private Class getIndexedPropertyType0() {
		return null;
	}

	private Method getIndexedReadMethod0() {
		return null;
	}

	private Method getIndexedWriteMethod0() {
		return null;
	}

	private Class findIndexedPropertyType(Method indexedReadMethod, Method indexedWriteMethod)
			throws IntrospectionException {
		return null;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		return false;
	}

	IndexedPropertyDescriptor(PropertyDescriptor x, PropertyDescriptor y) {
		super(x, y);
	}

	IndexedPropertyDescriptor(IndexedPropertyDescriptor old) {
		super(old);
	}

	void updateGenericsFor(Class<?> type) {
	}

	public int hashCode() {
		return 0;
	}

	void appendTo(StringBuilder sb) {
		super.appendTo(sb);
		appendTo(sb, "indexedPropertyType", this.indexedPropertyTypeRef);
		appendTo(sb, "indexedReadMethod", this.indexedReadMethodRef);
		appendTo(sb, "indexedWriteMethod", this.indexedWriteMethodRef);
	}
}