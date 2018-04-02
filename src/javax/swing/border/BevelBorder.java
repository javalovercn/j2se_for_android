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
import hc.android.CanvasGraphics;
import hc.android.AndroidUIUtil;

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
 * A class which implements a simple two-line bevel border.
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
public class BevelBorder extends AbstractBorder {
	/**
	 * 上凸
	 */
	public static final int RAISED = 0;
	/**
	 * 下凹
	 */
	public static final int LOWERED = 1;

	protected int bevelType;
	protected Color highlightOuter;
	protected Color highlightInner;
	protected Color shadowInner;
	protected Color shadowOuter;

	FrameLayout borderView;
	private View componentView;
	protected final Insets insets = new Insets(2, 2, 2, 2);
	// private RelativeLayout relativeView;
	// private View componentView;

	public BevelBorder(int bevelType) {
		this(bevelType, AndroidUIUtil.WIN_BODY_BACK.brighter(),
				AndroidUIUtil.WIN_BODY_BACK.darker());
	}

	public BevelBorder(int bevelType, Color highlight, Color shadow) {
		this(bevelType, highlight, highlight, shadow, shadow);
	}

	public BevelBorder(int bevelType, Color highlightOuterColor, Color highlightInnerColor,
			Color shadowOuterColor, Color shadowInnerColor) {
		this.bevelType = bevelType;
		this.highlightOuter = highlightOuterColor;
		this.highlightInner = highlightInnerColor;
		this.shadowOuter = shadowOuterColor;
		this.shadowInner = shadowInnerColor;

		buildBevelBorder();
	}

	private void buildBevelBorder() {
		FrameLayout framelayout = new FrameLayout(ActivityManager.applicationContext);
		framelayout.setBackgroundColor(0);

		final View boxView = new View(ActivityManager.applicationContext);

		// upgrade API
		boxView.setBackgroundDrawable(buildDrawable(boxView));

		{
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			// lp.setMargins(insets.left, insets.top, insets.right,
			// insets.bottom);

			framelayout.addView(boxView, lp);
		}

		borderView = framelayout;
	}

	protected Drawable buildDrawable(final View boxView) {
		return new Drawable() {
			@Override
			public void setColorFilter(ColorFilter cf) {
			}

			@Override
			public void setAlpha(int alpha) {
			}

			@Override
			public int getOpacity() {
				return 0;
			}

			@Override
			public void draw(Canvas canvas) {
				CanvasGraphics g = new CanvasGraphics(canvas, screenAdapter);
				int width = boxView.getWidth();
				int height = boxView.getHeight();

				g.setColor(bevelType == RAISED ? highlightOuter : shadowInner);
				g.drawLine(0, 0, width - 1, 0);// 上外
				g.drawLine(0, 0, 0, height - 1);// 左外

				g.setColor(bevelType == RAISED ? highlightInner : shadowOuter);
				g.drawLine(1, 1, width - 2, 1);// 上内
				g.drawLine(1, 1, 1, height - 2);// 左内

				g.setColor(bevelType == RAISED ? shadowInner : highlightOuter);
				g.drawLine(width - 2, 1, width - 2, height - 2);// 右内
				g.drawLine(1, height - 2, width - 2, height - 2);// 底上

				g.setColor(bevelType == RAISED ? shadowOuter : highlightInner);
				g.drawLine(width - 1, 0, width - 1, height - 1);// 右外
				g.drawLine(0, height - 1, width - 1, height - 1);// 底下
			}
		};
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	}

	public Insets getBorderInsets(Component c, Insets in) {
		in.set(insets.top, insets.left, insets.bottom, insets.right);
		return in;
	}

	public Color getHighlightOuterColor(Component c) {
		Color highlight = getHighlightOuterColor();
		return highlight != null ? highlight : c.getBackground().brighter().brighter();
	}

	public Color getHighlightInnerColor(Component c) {
		Color highlight = getHighlightInnerColor();
		return highlight != null ? highlight : c.getBackground().brighter();
	}

	public Color getShadowInnerColor(Component c) {
		Color shadow = getShadowInnerColor();
		return shadow != null ? shadow : c.getBackground().darker();
	}

	public Color getShadowOuterColor(Component c) {
		Color shadow = getShadowOuterColor();
		return shadow != null ? shadow : c.getBackground().darker().darker();
	}

	public Color getHighlightOuterColor() {
		return highlightOuter;
	}

	public Color getHighlightInnerColor() {
		return highlightInner;
	}

	public Color getShadowInnerColor() {
		return shadowInner;
	}

	public Color getShadowOuterColor() {
		return shadowOuter;
	}

	public int getBevelType() {
		return bevelType;
	}

	public boolean isBorderOpaque() {
		return true;
	}

	protected void paintRaisedBevel(Component c, Graphics g, int x, int y, int width, int height) {
		AndroidClassUtil.callEmptyMethod();
	}

	protected void paintLoweredBevel(Component c, Graphics g, int x, int y, int width, int height) {
		AndroidClassUtil.callEmptyMethod();
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
					lp.setMargins(insets.left, insets.top, insets.right, insets.bottom);
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
