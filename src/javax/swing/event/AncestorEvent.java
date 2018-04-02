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
package javax.swing.event;

import java.awt.AWTEvent;
import java.awt.Container;
import javax.swing.JComponent;

/**
 * An event reported to a child component that originated from an ancestor in
 * the component hierarchy.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Dave Moore
 */
public class AncestorEvent extends AWTEvent {
	public static final int ANCESTOR_ADDED = 1;
	public static final int ANCESTOR_REMOVED = 2;
	public static final int ANCESTOR_MOVED = 3;

	Container ancestor;
	Container ancestorParent;

	public AncestorEvent(JComponent source, int id, Container ancestor, Container ancestorParent) {
		super(source, id);
		this.ancestor = ancestor;
		this.ancestorParent = ancestorParent;
	}

	public Container getAncestor() {
		return ancestor;
	}

	public Container getAncestorParent() {
		return ancestorParent;
	}

	public JComponent getComponent() {
		return (JComponent) getSource();
	}
}
