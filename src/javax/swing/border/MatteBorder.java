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
import hc.android.AndroidClassUtil;
import hc.android.AndroidUIUtil;
import hc.android.CanvasGraphics;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.Icon;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

/**
 * A class which provides a matte-like border of either a solid color or a tiled
 * icon.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Amy Fowler
 */
public class MatteBorder extends EmptyBorder {
	protected Color color;
	protected Icon tileIcon;

	public MatteBorder(final int top, final int left, final int bottom, final int right,
			Color matteColor) {
		super(top, left, bottom, right);
		this.color = matteColor;

		borderView = new FrameLayout(ActivityManager.applicationContext);
		borderView.setBackgroundColor(0);

		final View boxView = new View(ActivityManager.applicationContext);
		boxView.setBackgroundDrawable(new Drawable() {
			@Override
			public void draw(Canvas canvas) {
				CanvasGraphics g = new CanvasGraphics(canvas, screenAdapter);
				int width = boxView.getWidth();
				int height = boxView.getHeight();

				g.setColor(color);
				g.fillRect(0, 0, width, top);// 上
				g.fillRect(0, 0, left, height);// 左
				g.fillRect(width - right, 0, right, height);
				g.fillRect(0, height - bottom, width, bottom);
			}

			@Override
			public int getOpacity() {
				return 0;
			}

			@Override
			public void setAlpha(int arg0) {
			}

			@Override
			public void setColorFilter(ColorFilter arg0) {
			}
		});

		{
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			borderView.addView(boxView, lp);
		}
	}

	public MatteBorder(Insets borderInsets, Color matteColor) {
		super(borderInsets);
		this.color = matteColor;
	}

	public MatteBorder(int top, int left, int bottom, int right, Icon tileIcon) {
		super(top, left, bottom, right);
		this.tileIcon = tileIcon;
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public MatteBorder(Insets borderInsets, Icon tileIcon) {
		this(borderInsets.top, borderInsets.left, borderInsets.bottom, borderInsets.right,
				tileIcon);
	}

	public MatteBorder(Icon tileIcon) {
		this(-1, -1, -1, -1, tileIcon);
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	}

	private void paintEdge(Component c, Graphics g, int x, int y, int width, int height, int tileW,
			int tileH) {
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		return computeInsets(insets);
	}

	public Insets getBorderInsets() {
		return computeInsets(new Insets(0, 0, 0, 0));
	}

	private Insets computeInsets(Insets insets) {
		if (tileIcon != null && top == -1 && bottom == -1 && left == -1 && right == -1) {
			int w = tileIcon.getIconWidth();
			int h = tileIcon.getIconHeight();
			insets.top = h;
			insets.right = w;
			insets.bottom = h;
			insets.left = w;
		} else {
			insets.left = left;
			insets.top = top;
			insets.right = right;
			insets.bottom = bottom;
		}
		return insets;
	}

	public Color getMatteColor() {
		return color;
	}

	public Icon getTileIcon() {
		return tileIcon;
	}

	public boolean isBorderOpaque() {
		return color != null;
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
					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
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