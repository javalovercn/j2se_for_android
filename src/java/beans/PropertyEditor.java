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

/**
 * A PropertyEditor class provides support for GUIs that want to
 * allow users to edit a property value of a given type.
 * <p>
 * PropertyEditor supports a variety of different kinds of ways of
 * displaying and updating property values.  Most PropertyEditors will
 * only need to support a subset of the different options available in
 * this API.
 * <P>
 * Simple PropertyEditors may only support the getAsText and setAsText
 * methods and need not support (say) paintValue or getCustomEditor.  More
 * complex types may be unable to support getAsText and setAsText but will
 * instead support paintValue and getCustomEditor.
 * <p>
 * Every propertyEditor must support one or more of the three simple
 * display styles.  Thus it can either (1) support isPaintable or (2)
 * both return a non-null String[] from getTags() and return a non-null
 * value from getAsText or (3) simply return a non-null String from
 * getAsText().
 * <p>
 * Every property editor must support a call on setValue when the argument
 * object is of the type for which this is the corresponding propertyEditor.
 * In addition, each property editor must either support a custom editor,
 * or support setAsText.
 * <p>
 * Each PropertyEditor should have a null constructor.
 */

public interface PropertyEditor {

	void setValue(Object value);

	Object getValue();

	boolean isPaintable();

	void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box);

	String getJavaInitializationString();

	String getAsText();

	void setAsText(String text) throws java.lang.IllegalArgumentException;

	String[] getTags();

	java.awt.Component getCustomEditor();

	boolean supportsCustomEditor();

	void addPropertyChangeListener(PropertyChangeListener listener);

	void removePropertyChangeListener(PropertyChangeListener listener);

}