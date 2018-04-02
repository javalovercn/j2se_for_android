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

import hc.android.J2SEInitor;
import hc.android.ScreenAdapter;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.Serializable;

/**
 * A class that implements an empty border with no size. This provides a
 * convenient base class from which other border classes can be easily derived.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author David Kloba
 */
public abstract class AbstractBorder implements Border, Serializable {
	public ScreenAdapter screenAdapter = J2SEInitor.initAdapter();

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	}

	public Insets getBorderInsets(Component c) {
		return getBorderInsets(c, new Insets(0, 0, 0, 0));
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		insets.left = insets.top = insets.right = insets.bottom = 0;
		return insets;
	}

	public boolean isBorderOpaque() {
		return false;
	}

	public Rectangle getInteriorRectangle(Component c, int x, int y, int width, int height) {
		return getInteriorRectangle(c, this, x, y, width, height);
	}

	public static Rectangle getInteriorRectangle(Component c, Border b, int x, int y, int width,
			int height) {
		Insets insets;
		if (b != null)
			insets = b.getBorderInsets(c);
		else
			insets = new Insets(0, 0, 0, 0);
		return new Rectangle(x + insets.left, y + insets.top, width - insets.right - insets.left,
				height - insets.top - insets.bottom);
	}

	public int getBaseline(Component c, int width, int height) {
		if (width < 0 || height < 0) {
			throw new IllegalArgumentException("Width and height must be >= 0");
		}
		return -1;
	}

	public Component.BaselineResizeBehavior getBaselineResizeBehavior(Component c) {
		if (c == null) {
			throw new NullPointerException("Component must be non-null");
		}
		return Component.BaselineResizeBehavior.OTHER;
	}

	static boolean isLeftToRight(Component c) {
		return c.getComponentOrientation().isLeftToRight();
	}

}
