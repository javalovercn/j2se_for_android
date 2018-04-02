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

import hc.android.AndroidUIUtil;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import android.view.View;

/**
 * A composite Border class used to compose two Border objects into a single
 * border by nesting an inside Border object within the insets of an outside
 * Border object.
 *
 * For example, this class may be used to add blank margin space to a component
 * with an existing decorative border:
 * <p>
 * <code><pre>
 *    Border border = comp.getBorder();
 *    Border margin = new EmptyBorder(10,10,10,10);
 *    comp.setBorder(new CompoundBorder(border, margin));
 * </pre></code>
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
public class CompoundBorder extends AbstractBorder {
	protected Border outsideBorder;
	protected Border insideBorder;
	private View componentView;

	public CompoundBorder() {
		this.outsideBorder = null;
		this.insideBorder = null;
	}

	public CompoundBorder(Border outsideBorder, Border insideBorder) {
		this.outsideBorder = outsideBorder;
		this.insideBorder = insideBorder;
	}

	@Override
	public boolean isBorderOpaque() {
		return (outsideBorder == null || outsideBorder.isBorderOpaque())
				&& (insideBorder == null || insideBorder.isBorderOpaque());
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = insets.left = insets.right = insets.bottom = 0;
		if (outsideBorder != null) {
			Insets comput = outsideBorder.getBorderInsets(c);
			insets.top += comput.top;
			insets.left += comput.left;
			insets.right += comput.right;
			insets.bottom += comput.bottom;
		}
		if (insideBorder != null) {
			Insets comput = insideBorder.getBorderInsets(c);
			insets.top += comput.top;
			insets.left += comput.left;
			insets.right += comput.right;
			insets.bottom += comput.bottom;
		}
		return insets;
	}

	public Border getOutsideBorder() {
		return outsideBorder;
	}

	public Border getInsideBorder() {
		return insideBorder;
	}

	@Override
	public View getBorderViewAdAPI() {
		if (outsideBorder == null) {
			if (insideBorder == null) {
				return componentView;
			} else {
				return insideBorder.getBorderViewAdAPI();
			}
		}
		return outsideBorder.getBorderViewAdAPI();
	}

	@Override
	public void setComponentViewAdAPI(final View view, final Component component) {
		AndroidUIUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				componentView = view;
				if (insideBorder != null) {
					insideBorder.setComponentViewAdAPI(view, component);
				}
				if (outsideBorder != null) {
					outsideBorder.setComponentViewAdAPI(insideBorder.getBorderViewAdAPI(),
							component);
				}
			}
		});
	}
}
