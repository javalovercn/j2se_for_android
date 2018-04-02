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
import hc.android.CanvasGraphics;
import hc.android.AndroidUIUtil;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

/**
 * A class which implements a line border of arbitrary thickness and of a single
 * color.
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
public class LineBorder extends AbstractBorder {
	protected int thickness;
	protected Color lineColor;
	protected boolean roundedCorners;
	FrameLayout borderView;
	private View componentView;

	public static Border createBlackLineBorder() {
		return new LineBorder(Color.black, 1);
	}

	public static Border createGrayLineBorder() {
		return new LineBorder(AndroidUIUtil.getDarkGrayColor(), 1);
	}

	public LineBorder(Color color) {
		this(color, 1, false);
	}

	public LineBorder(Color color, int thickness) {
		this(color, thickness, false);
	}

	public LineBorder(Color color, int thickness, boolean roundedCorners) {
		lineColor = color;
		this.thickness = thickness;
		this.roundedCorners = roundedCorners;

		build();
	}

	private void build() {
		FrameLayout framelayout = new FrameLayout(ActivityManager.applicationContext);
		framelayout.setBackgroundColor(0);

		final View boxView = new View(ActivityManager.applicationContext);
		boxView.setBackgroundDrawable(new Drawable() {
			@Override
			public void draw(Canvas canvas) {
				CanvasGraphics g = new CanvasGraphics(canvas, screenAdapter);
				int width = boxView.getWidth();
				int height = boxView.getHeight();

				g.setColor(lineColor);
				g.setStroke(new BasicStroke(thickness));
				if (roundedCorners) {
					int arc = AndroidUIUtil.BORDER_RADIUS;
					g.drawRoundRect(thickness / 2, thickness / 2,
							width - (int) Math.ceil(thickness * 1.0F / 2.0F),
							height - (int) Math.ceil(thickness * 1.0F / 2.0F), arc * 2, arc * 2);
				} else {
					g.drawRect((int) Math.ceil(thickness * 1.0F / 2.0F),
							(int) Math.ceil(thickness * 1.0F / 2.0F),
							width - (int) Math.ceil(thickness * 1.0F / 2.0F) * 2,
							height - (int) Math.ceil(thickness * 1.0F / 2.0F) * 2);
				}
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
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			// lp.setMargins(thickness, thickness, thickness, thickness);

			framelayout.addView(boxView, lp);
		}

		borderView = framelayout;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		insets.set(thickness, thickness, thickness, thickness);
		return insets;
	}

	public Color getLineColor() {
		return lineColor;
	}

	public int getThickness() {
		return thickness;
	}

	public boolean getRoundedCorners() {
		return roundedCorners;
	}

	public boolean isBorderOpaque() {
		return !roundedCorners;
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
					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					lp.setMargins(thickness, thickness, thickness, thickness);
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
