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

import hc.android.ActivityManager;
import hc.android.AndroidUIUtil;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.Serializable;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

/**
 * A class which provides an empty, transparent border which takes up space but
 * does no drawing.
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
public class EmptyBorder extends AbstractBorder implements Serializable {
	protected int left, right, top, bottom;
	protected ViewGroup borderView;
	protected View componentView;

	public EmptyBorder(int top, int left, int bottom, int right) {
		this.top = top;
		this.left = left;
		this.right = right;
		this.bottom = bottom;

		borderView = new LinearLayout(ActivityManager.applicationContext);
		borderView.setBackgroundColor(0);
	}

	public EmptyBorder(Insets borderInsets) {
		this(borderInsets.top, borderInsets.right, borderInsets.bottom, borderInsets.left);
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = top;
		insets.left = left;
		insets.right = right;
		insets.bottom = bottom;
		return insets;
	}

	public Insets getBorderInsets() {
		return new Insets(top, left, bottom, right);
	}

	public boolean isBorderOpaque() {
		return false;
	}

	@Override
	public View getBorderViewAdAPI() {
		return borderView;
	}

	@Override
	public void setComponentViewAdAPI(final View view, final Component component) {
		AndroidUIUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (componentView != null) {
					borderView.removeView(componentView);
				}

				{
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					lp.setMargins(left, top, right, bottom);
					lp.gravity = (Gravity.TOP
							| (component.getComponentOrientation().isLeftToRight() ? Gravity.LEFT
									: Gravity.RIGHT));
					borderView.addView(view, lp);
				}
				componentView = view;
			}
		});
	}

}