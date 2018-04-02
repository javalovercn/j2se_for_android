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
package javax.swing.border;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import android.view.View;

/**
 * Interface describing an object capable of rendering a border around the edges
 * of a swing component. For examples of using borders see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/misc/border.html">How to Use
 * Borders</a>, a section in <em>The Java Tutorial.</em>
 * <p>
 * In the Swing component set, borders supercede Insets as the mechanism for
 * creating a (decorated or plain) area around the edge of a component.
 * <p>
 * Usage Notes:
 * <ul>
 * <li>Use EmptyBorder to create a plain border (this mechanism replaces its
 * predecessor, <code>setInsets</code>).
 * <li>Use CompoundBorder to nest multiple border objects, creating a single,
 * combined border.
 * <li>Border instances are designed to be shared. Rather than creating a new
 * border object using one of border classes, use the BorderFactory methods,
 * which produces a shared instance of the common border types.
 * <li>Additional border styles include BevelBorder, SoftBevelBorder,
 * EtchedBorder, LineBorder, TitledBorder, and MatteBorder.
 * <li>To create a new border class, subclass AbstractBorder.
 * </ul>
 *
 * @author David Kloba
 * @author Amy Fowler
 * @see javax.swing.BorderFactory
 * @see EmptyBorder
 * @see CompoundBorder
 */
public interface Border {
	void paintBorder(Component c, Graphics g, int x, int y, int width, int height);

	Insets getBorderInsets(Component c);

	boolean isBorderOpaque();

	public View getBorderViewAdAPI();

	public void setComponentViewAdAPI(View view, Component component);
}