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
 * An EventSetDescriptor describes a group of events that a given Java
 * bean fires.
 * <P>
 * The given group of events are all delivered as method calls on a single
 * event listener interface, and an event listener object can be registered
 * via a call on a registration method supplied by the event source.
 */
public class EventSetDescriptor extends FeatureDescriptor {

	private MethodDescriptor[] listenerMethodDescriptors;
	private MethodDescriptor addMethodDescriptor;
	private MethodDescriptor removeMethodDescriptor;
	private MethodDescriptor getMethodDescriptor;

	private Reference<Method[]> listenerMethodsRef;
	private Reference<Class> listenerTypeRef;

	private boolean unicast;
	private boolean inDefaultEventSet = true;

	public EventSetDescriptor(Class<?> sourceClass, String eventSetName,
			Class<?> listenerType, String listenerMethodName)
			throws IntrospectionException {
		this(
				sourceClass,
				eventSetName,
				listenerType,
				new String[] { listenerMethodName },
				Introspector.ADD_PREFIX + getListenerClassName(listenerType),
				Introspector.REMOVE_PREFIX + getListenerClassName(listenerType),
				Introspector.GET_PREFIX + getListenerClassName(listenerType)
						+ "s");
	}

	private static String getListenerClassName(Class cls) {
		String className = cls.getName();
		return className.substring(className.lastIndexOf('.') + 1);
	}

	public EventSetDescriptor(Class<?> sourceClass, String eventSetName,
			Class<?> listenerType, String listenerMethodNames[],
			String addListenerMethodName, String removeListenerMethodName)
			throws IntrospectionException {
		this(sourceClass, eventSetName, listenerType, listenerMethodNames,
				addListenerMethodName, removeListenerMethodName, null);
	}

	public EventSetDescriptor(Class<?> sourceClass, String eventSetName,
			Class<?> listenerType, String listenerMethodNames[],
			String addListenerMethodName, String removeListenerMethodName,
			String getListenerMethodName) throws IntrospectionException {
	}

	private static Method getMethod(Class cls, String name, int args)
			throws IntrospectionException {
		return null;
	}

	public EventSetDescriptor(String eventSetName, Class<?> listenerType,
			Method listenerMethods[], Method addListenerMethod,
			Method removeListenerMethod) throws IntrospectionException {
		this(eventSetName, listenerType, listenerMethods, addListenerMethod,
				removeListenerMethod, null);
	}

	public EventSetDescriptor(String eventSetName, Class<?> listenerType,
			Method listenerMethods[], Method addListenerMethod,
			Method removeListenerMethod, Method getListenerMethod)
			throws IntrospectionException {
	}

	public EventSetDescriptor(String eventSetName, Class<?> listenerType,
			MethodDescriptor listenerMethodDescriptors[],
			Method addListenerMethod, Method removeListenerMethod)
			throws IntrospectionException {
	}

	public Class<?> getListenerType() {
		return null;
	}

	private void setListenerType(Class cls) {
	}

	public synchronized Method[] getListenerMethods() {
		return new Method[0];
	}

	private void setListenerMethods(Method[] methods) {
	}

	private Method[] getListenerMethods0() {
		return null;
	}

	public synchronized MethodDescriptor[] getListenerMethodDescriptors() {
		return listenerMethodDescriptors;
	}

	public synchronized Method getAddListenerMethod() {
		return getMethod(this.addMethodDescriptor);
	}

	private synchronized void setAddListenerMethod(Method method) {
	}

	public synchronized Method getRemoveListenerMethod() {
		return getMethod(this.removeMethodDescriptor);
	}

	private synchronized void setRemoveListenerMethod(Method method) {
	}

	public synchronized Method getGetListenerMethod() {
		return getMethod(this.getMethodDescriptor);
	}

	private synchronized void setGetListenerMethod(Method method) {
	}

	public void setUnicast(boolean unicast) {
		this.unicast = unicast;
	}

	public boolean isUnicast() {
		return unicast;
	}

	public void setInDefaultEventSet(boolean inDefaultEventSet) {
		this.inDefaultEventSet = inDefaultEventSet;
	}

	public boolean isInDefaultEventSet() {
		return inDefaultEventSet;
	}

	EventSetDescriptor(EventSetDescriptor x, EventSetDescriptor y) {
		super(x, y);
	}

	EventSetDescriptor(EventSetDescriptor old) {
		super(old);
	}

	void appendTo(StringBuilder sb) {
		appendTo(sb, "unicast", this.unicast);
		appendTo(sb, "inDefaultEventSet", this.inDefaultEventSet);
		appendTo(sb, "listenerType", this.listenerTypeRef);
		appendTo(sb, "getListenerMethod", getMethod(this.getMethodDescriptor));
		appendTo(sb, "addListenerMethod", getMethod(this.addMethodDescriptor));
		appendTo(sb, "removeListenerMethod",
				getMethod(this.removeMethodDescriptor));
	}

	private static Method getMethod(MethodDescriptor descriptor) {
		return (descriptor != null) ? descriptor.getMethod() : null;
	}
}
