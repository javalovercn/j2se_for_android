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
package javax.swing.plaf;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * The base class for all UI delegate objects in the Swing pluggable look and
 * feel architecture. The UI delegate object for a Swing component is
 * responsible for implementing the aspects of the component that depend on the
 * look and feel. The <code>JComponent</code> class invokes methods from this
 * class in order to delegate operations (painting, layout calculations, etc.)
 * that may vary depending on the look and feel installed. <b>Client programs
 * should not invoke methods on this class directly.</b>
 *
 * @see javax.swing.JComponent
 * @see javax.swing.UIManager
 *
 */
public abstract class ComponentUI {
	public ComponentUI() {
	}

	public void installUI(JComponent c) {
	}

	public void uninstallUI(JComponent c) {
	}

	public void paint(Graphics g, JComponent c) {
	}

	public void update(Graphics g, JComponent c) {
	}

	public Dimension getPreferredSize(JComponent c) {
		return null;
	}

	public Dimension getMinimumSize(JComponent c) {
		return getPreferredSize(c);
	}

	public Dimension getMaximumSize(JComponent c) {
		return getPreferredSize(c);
	}

	public boolean contains(JComponent c, int x, int y) {
		return c.inside(x, y);
	}

	public static ComponentUI createUI(JComponent c) {
		throw new Error("ComponentUI.createUI not implemented.");
	}

	public int getBaseline(JComponent c, int width, int height) {
		if (c == null) {
			throw new NullPointerException("Component must be non-null");
		}
		if (width < 0 || height < 0) {
			throw new IllegalArgumentException("Width and height must be >= 0");
		}
		return -1;
	}

	public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent c) {
		if (c == null) {
			throw new NullPointerException("Component must be non-null");
		}
		return Component.BaselineResizeBehavior.OTHER;
	}

	public int getAccessibleChildrenCount(JComponent c) {
		return SwingUtilities.getAccessibleChildrenCount(c);
	}

	public Accessible getAccessibleChild(JComponent c, int i) {
		return SwingUtilities.getAccessibleChild(c, i);
	}
}
