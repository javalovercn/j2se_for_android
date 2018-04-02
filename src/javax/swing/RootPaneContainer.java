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
package javax.swing;

import java.awt.Component;
import java.awt.Container;

/**
 * This interface is implemented by components that have a single JRootPane
 * child: JDialog, JFrame, JWindow, JApplet, JInternalFrame. The methods in this
 * interface are just <i>covers</i> for the JRootPane properties, e.g.
 * <code>getContentPane()</code> is generally implemented like this:
 * 
 * <pre>
 * public Container getContentPane() {
 * 	return getRootPane().getContentPane();
 * }
 * </pre>
 * 
 * This interface serves as a <i>marker</i> for Swing GUI builders that need to
 * treat components like JFrame, that contain a single JRootPane, specially. For
 * example in a GUI builder, dropping a component on a RootPaneContainer would
 * be interpreted as <code>frame.getContentPane().add(child)</code>.
 * <p>
 * For conveniance <code>JFrame</code>, <code>JDialog</code>,
 * <code>JWindow</code>, <code>JApplet</code> and <code>JInternalFrame</code>,
 * by default, forward, by default, all calls to the <code>add</code>,
 * <code>remove</code> and <code>setLayout</code> methods, to the
 * <code>contentPane</code>. This means you can call:
 * 
 * <pre>
 * rootPaneContainer.add(component);
 * </pre>
 * 
 * instead of:
 * 
 * <pre>
 * rootPaneContainer.getContentPane().add(component);
 * </pre>
 * <p>
 * The behavior of the <code>add</code> and <code>setLayout</code> methods for
 * <code>JFrame</code>, <code>JDialog</code>, <code>JWindow</code>,
 * <code>JApplet</code> and <code>JInternalFrame</code> is controlled by the
 * <code>rootPaneCheckingEnabled</code> property. If this property is true (the
 * default), then calls to these methods are forwarded to the
 * <code>contentPane</code>; if false, these methods operate directly on the
 * <code>RootPaneContainer</code>. This property is only intended for
 * subclasses, and is therefore protected.
 *
 * @see JRootPane
 * @see JFrame
 * @see JDialog
 * @see JWindow
 * @see JApplet
 * @see JInternalFrame
 *
 * @author Hans Muller
 */
public interface RootPaneContainer {
	JRootPane getRootPane();

	void setContentPane(Container contentPane);

	Container getContentPane();

	void setLayeredPane(JLayeredPane layeredPane);

	JLayeredPane getLayeredPane();

	void setGlassPane(Component glassPane);

	Component getGlassPane();
}
