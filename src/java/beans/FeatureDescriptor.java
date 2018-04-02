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
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The FeatureDescriptor class is the common baseclass for PropertyDescriptor,
 * EventSetDescriptor, and MethodDescriptor, etc.
 * <p>
 * It supports some common information that can be set and retrieved for any of
 * the introspection descriptors.
 * <p>
 * In addition it provides an extension mechanism so that arbitrary
 * attribute/value pairs can be associated with a design feature.
 */

public class FeatureDescriptor {
	private static final String TRANSIENT = "transient";

	private Reference<Class> classRef;

	public FeatureDescriptor() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		if (displayName == null) {
			return getName();
		}
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isExpert() {
		return expert;
	}

	public void setExpert(boolean expert) {
		this.expert = expert;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isPreferred() {
		return preferred;
	}

	public void setPreferred(boolean preferred) {
		this.preferred = preferred;
	}

	public String getShortDescription() {
		if (shortDescription == null) {
			return getDisplayName();
		}
		return shortDescription;
	}

	public void setShortDescription(String text) {
		shortDescription = text;
	}

	public void setValue(String attributeName, Object value) {
		getTable().put(attributeName, value);
	}

	public Object getValue(String attributeName) {
		return (this.table != null) ? this.table.get(attributeName) : null;
	}

	public Enumeration<String> attributeNames() {
		return getTable().keys();
	}

	FeatureDescriptor(FeatureDescriptor x, FeatureDescriptor y) {
		expert = x.expert | y.expert;
		hidden = x.hidden | y.hidden;
		preferred = x.preferred | y.preferred;
		name = y.name;
		shortDescription = x.shortDescription;
		if (y.shortDescription != null) {
			shortDescription = y.shortDescription;
		}
		displayName = x.displayName;
		if (y.displayName != null) {
			displayName = y.displayName;
		}
		classRef = x.classRef;
		if (y.classRef != null) {
			classRef = y.classRef;
		}
		addTable(x.table);
		addTable(y.table);
	}

	FeatureDescriptor(FeatureDescriptor old) {
		expert = old.expert;
		hidden = old.hidden;
		preferred = old.preferred;
		name = old.name;
		shortDescription = old.shortDescription;
		displayName = old.displayName;
		classRef = old.classRef;

		addTable(old.table);
	}

	private void addTable(Hashtable<String, Object> table) {
		if ((table != null) && !table.isEmpty()) {
			getTable().putAll(table);
		}
	}

	private Hashtable<String, Object> getTable() {
		if (this.table == null) {
			this.table = new Hashtable<String, Object>();
		}
		return this.table;
	}

	void setTransient(Transient annotation) {
		if ((annotation != null) && (null == getValue(TRANSIENT))) {
			setValue(TRANSIENT, annotation.value());
		}
	}

	boolean isTransient() {
		Object value = getValue(TRANSIENT);
		return (value instanceof Boolean) ? (Boolean) value : false;
	}

	void setClass0(Class cls) {
		this.classRef = getWeakReference(cls);
	}

	Class getClass0() {
		return (this.classRef != null) ? this.classRef.get() : null;
	}

	static <T> Reference<T> getSoftReference(T object) {
		return (object != null) ? new SoftReference<T>(object) : null;
	}

	static <T> Reference<T> getWeakReference(T object) {
		return (object != null) ? new WeakReference<T>(object) : null;
	}

	static Class getReturnType(Class base, Method method) {
		return null;
	}

	static Class[] getParameterTypes(Class base, Method method) {
		return new Class[0];
	}

	private boolean expert;
	private boolean hidden;
	private boolean preferred;
	private String shortDescription;
	private String name;
	private String displayName;
	private Hashtable<String, Object> table;

	public String toString() {
		return "";
	}

	void appendTo(StringBuilder sb) {
	}

	static void appendTo(StringBuilder sb, String name, Reference reference) {
		if (reference != null) {
			appendTo(sb, name, reference.get());
		}
	}

	static void appendTo(StringBuilder sb, String name, Object value) {
		if (value != null) {
			sb.append("; ").append(name).append("=").append(value);
		}
	}

	static void appendTo(StringBuilder sb, String name, boolean value) {
		if (value) {
			sb.append("; ").append(name);
		}
	}
}
