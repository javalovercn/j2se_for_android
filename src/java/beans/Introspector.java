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
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

/**
 * The Introspector class provides a standard way for tools to learn about the
 * properties, events, and methods supported by a target Java Bean.
 * <p>
 * For each of those three kinds of information, the Introspector will
 * separately analyze the bean's class and superclasses looking for either
 * explicit or implicit information and use that information to build a BeanInfo
 * object that comprehensively describes the target bean.
 * <p>
 * For each class "Foo", explicit information may be available if there exists a
 * corresponding "FooBeanInfo" class that provides a non-null value when queried
 * for the information. We first look for the BeanInfo class by taking the full
 * package-qualified name of the target bean class and appending "BeanInfo" to
 * form a new class name. If this fails, then we take the final classname
 * component of this name, and look for that class in each of the packages
 * specified in the BeanInfo package search path.
 * <p>
 * Thus for a class such as "sun.xyz.OurButton" we would first look for a
 * BeanInfo class called "sun.xyz.OurButtonBeanInfo" and if that failed we'd
 * look in each package in the BeanInfo search path for an OurButtonBeanInfo
 * class. With the default search path, this would mean looking for
 * "sun.beans.infos.OurButtonBeanInfo".
 * <p>
 * If a class provides explicit BeanInfo about itself then we add that to the
 * BeanInfo information we obtained from analyzing any derived classes, but we
 * regard the explicit information as being definitive for the current class and
 * its base classes, and do not proceed any further up the superclass chain.
 * <p>
 * If we don't find explicit BeanInfo on a class, we use low-level reflection to
 * study the methods of the class and apply standard design patterns to identify
 * property accessors, event sources, or public methods. We then proceed to
 * analyze the class's superclass and add in the information from it (and
 * possibly on up the superclass chain).
 * <p>
 * For more information about introspection and design patterns, please consult
 * the <a href=
 * "http://java.sun.com/products/javabeans/docs/index.html">JavaBeans&trade;
 * specification</a>.
 */

public class Introspector {
	public final static int USE_ALL_BEANINFO = 1;
	public final static int IGNORE_IMMEDIATE_BEANINFO = 2;
	public final static int IGNORE_ALL_BEANINFO = 3;

	private Class beanClass;

	private boolean propertyChangeSource = false;
	private static Class eventListenerType = EventListener.class;

	private String defaultEventName;
	private String defaultPropertyName;
	private int defaultEventIndex = -1;
	private int defaultPropertyIndex = -1;

	private Map methods;

	private Map properties;
	private Map events;

	static final String ADD_PREFIX = "add";
	static final String REMOVE_PREFIX = "remove";
	static final String GET_PREFIX = "get";
	static final String SET_PREFIX = "set";
	static final String IS_PREFIX = "is";

	public static BeanInfo getBeanInfo(Class<?> beanClass) throws IntrospectionException {
		return null;
	}

	public static BeanInfo getBeanInfo(Class<?> beanClass, int flags)
			throws IntrospectionException {
		return getBeanInfo(beanClass, null, flags);
	}

	public static BeanInfo getBeanInfo(Class<?> beanClass, Class<?> stopClass)
			throws IntrospectionException {
		return getBeanInfo(beanClass, stopClass, USE_ALL_BEANINFO);
	}

	public static BeanInfo getBeanInfo(Class<?> beanClass, Class<?> stopClass, int flags)
			throws IntrospectionException {
		return null;
	}

	public static String decapitalize(String name) {
		return "";
	}

	public static String[] getBeanInfoSearchPath() {
		return new String[0];
	}

	public static void setBeanInfoSearchPath(String[] path) {
	}

	public static void flushCaches() {
	}

	public static void flushFromCaches(Class<?> clz) {
	}

	private Introspector(Class beanClass, Class stopClass, int flags)
			throws IntrospectionException {
	}

	private BeanInfo getBeanInfo() throws IntrospectionException {
		return null;
	}

	private static BeanInfo findExplicitBeanInfo(Class beanClass) {
		return null;
	}

	private PropertyDescriptor[] getTargetPropertyInfo() {
		return new PropertyDescriptor[0];
	}

	private HashMap pdStore = new HashMap();

	private void addPropertyDescriptor(PropertyDescriptor pd) {
	}

	private void addPropertyDescriptors(PropertyDescriptor[] descriptors) {
	}

	private PropertyDescriptor[] getPropertyDescriptors(BeanInfo info) {
		return new PropertyDescriptor[0];
	}

	private void processPropertyDescriptors() {
	}

	private PropertyDescriptor mergePropertyDescriptor(IndexedPropertyDescriptor ipd,
			PropertyDescriptor pd) {
		return null;
	}

	private PropertyDescriptor mergePropertyDescriptor(PropertyDescriptor pd1,
			PropertyDescriptor pd2) {
		return new PropertyDescriptor(pd2, pd1);
	}

	private PropertyDescriptor mergePropertyDescriptor(IndexedPropertyDescriptor ipd1,
			IndexedPropertyDescriptor ipd2) {
		return null;
	}

	private EventSetDescriptor[] getTargetEventInfo() throws IntrospectionException {
		return new EventSetDescriptor[0];
	}

	private void addEvent(EventSetDescriptor esd) {
	}

	private MethodDescriptor[] getTargetMethodInfo() {
		return new MethodDescriptor[0];
	}

	private void addMethod(MethodDescriptor md) {
	}

	private static String makeQualifiedMethodName(String name, String[] params) {
		return "";
	}

	private int getTargetDefaultEventIndex() {
		return defaultEventIndex;
	}

	private int getTargetDefaultPropertyIndex() {
		return defaultPropertyIndex;
	}

	private BeanDescriptor getTargetBeanDescriptor() {
		return null;
	}

	private boolean isEventHandler(Method m) {
		return false;
	}

	private static Method[] getPublicDeclaredMethods(Class clz) {
		return new Method[0];
	}

	private static Method internalFindMethod(Class start, String methodName, int argCount,
			Class args[]) {
		Method method = null;
		return method;
	}

	static Method findMethod(Class cls, String methodName, int argCount) {
		return findMethod(cls, methodName, argCount, null);
	}

	static Method findMethod(Class cls, String methodName, int argCount, Class args[]) {
		if (methodName == null) {
			return null;
		}
		return internalFindMethod(cls, methodName, argCount, args);
	}

	static boolean isSubclass(Class a, Class b) {
		return false;
	}

	private boolean throwsException(Method method, Class exception) {
		return false;
	}

	static Object instantiate(Class sibling, String className)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return null;
	}

}

class GenericBeanInfo extends SimpleBeanInfo {

	private BeanDescriptor beanDescriptor;
	private EventSetDescriptor[] events;
	private int defaultEvent;
	private PropertyDescriptor[] properties;
	private int defaultProperty;
	private MethodDescriptor[] methods;
	private Reference<BeanInfo> targetBeanInfoRef;

	public GenericBeanInfo(BeanDescriptor beanDescriptor, EventSetDescriptor[] events,
			int defaultEvent, PropertyDescriptor[] properties, int defaultProperty,
			MethodDescriptor[] methods, BeanInfo targetBeanInfo) {
	}

	GenericBeanInfo(GenericBeanInfo old) {
	}

	public PropertyDescriptor[] getPropertyDescriptors() {
		return properties;
	}

	public int getDefaultPropertyIndex() {
		return defaultProperty;
	}

	public EventSetDescriptor[] getEventSetDescriptors() {
		return events;
	}

	public int getDefaultEventIndex() {
		return defaultEvent;
	}

	public MethodDescriptor[] getMethodDescriptors() {
		return methods;
	}

	public BeanDescriptor getBeanDescriptor() {
		return beanDescriptor;
	}

	public java.awt.Image getIcon(int iconKind) {
		BeanInfo targetBeanInfo = getTargetBeanInfo();
		if (targetBeanInfo != null) {
			return targetBeanInfo.getIcon(iconKind);
		}
		return super.getIcon(iconKind);
	}

	private BeanInfo getTargetBeanInfo() {
		return null;
	}
}