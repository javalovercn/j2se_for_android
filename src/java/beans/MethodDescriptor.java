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
import java.util.List;

/**
 * A MethodDescriptor describes a particular method that a Java Bean supports
 * for external access from other components.
 */

public class MethodDescriptor extends FeatureDescriptor {

	private Reference<Method> methodRef;

	private String[] paramNames;

	private List params;

	private ParameterDescriptor parameterDescriptors[];

	public MethodDescriptor(Method method) {
		this(method, null);
	}

	public MethodDescriptor(Method method, ParameterDescriptor parameterDescriptors[]) {
		setName(method.getName());
		setMethod(method);
		this.parameterDescriptors = parameterDescriptors;
	}

	public synchronized Method getMethod() {
		return null;
	}

	private synchronized void setMethod(Method method) {
	}

	private Method getMethod0() {
		return null;
	}

	private synchronized void setParams(Class[] param) {
	}

	String[] getParamNames() {
		return paramNames;
	}

	private synchronized Class[] getParams() {
		return null;
	}

	public ParameterDescriptor[] getParameterDescriptors() {
		return parameterDescriptors;
	}

	MethodDescriptor(MethodDescriptor x, MethodDescriptor y) {
		super(x, y);

		methodRef = x.methodRef;
		if (y.methodRef != null) {
			methodRef = y.methodRef;
		}
		params = x.params;
		if (y.params != null) {
			params = y.params;
		}
		paramNames = x.paramNames;
		if (y.paramNames != null) {
			paramNames = y.paramNames;
		}

		parameterDescriptors = x.parameterDescriptors;
		if (y.parameterDescriptors != null) {
			parameterDescriptors = y.parameterDescriptors;
		}
	}

	MethodDescriptor(MethodDescriptor old) {
		super(old);

		methodRef = old.methodRef;
		params = old.params;
		paramNames = old.paramNames;

		if (old.parameterDescriptors != null) {
			int len = old.parameterDescriptors.length;
			parameterDescriptors = new ParameterDescriptor[len];
			for (int i = 0; i < len; i++) {
				parameterDescriptors[i] = new ParameterDescriptor(old.parameterDescriptors[i]);
			}
		}
	}

	void appendTo(StringBuilder sb) {
		appendTo(sb, "method", this.methodRef);
		if (this.parameterDescriptors != null) {
			sb.append("; parameterDescriptors={");
			for (ParameterDescriptor pd : this.parameterDescriptors) {
				sb.append(pd).append(", ");
			}
			sb.setLength(sb.length() - 2);
			sb.append("}");
		}
	}
}
