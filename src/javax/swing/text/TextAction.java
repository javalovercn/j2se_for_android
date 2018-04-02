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
package javax.swing.text;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * An Action implementation useful for key bindings that are shared across a
 * number of different text components. Because the action is shared, it must
 * have a way of getting it's target to act upon. This class provides support to
 * try and find a text component to operate on. The preferred way of getting the
 * component to act upon is through the ActionEvent that is received. If the
 * Object returned by getSource can be narrowed to a text component, it will be
 * used. If the action event is null or can't be narrowed, the last focused text
 * component is tried. This is determined by being used in conjunction with a
 * JTextController which arranges to share that information with a TextAction.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Timothy Prinzing
 */
public abstract class TextAction extends AbstractAction {

	public TextAction(String name) {
		super(name);
	}

	protected final JTextComponent getTextComponent(ActionEvent e) {
		return null;
	}

	public static final Action[] augmentList(Action[] list1, Action[] list2) {
		return null;
	}

	protected final JTextComponent getFocusedComponent() {
		return null;
	}
}
