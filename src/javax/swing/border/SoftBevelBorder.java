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

import hc.android.CanvasGraphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * A class which implements a raised or lowered bevel with softened corners.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Amy Fowler
 * @author Chester Rose
 */
public class SoftBevelBorder extends BevelBorder {

	public SoftBevelBorder(int bevelType) {
		super(bevelType);
		init();
	}

	public SoftBevelBorder(int bevelType, Color highlight, Color shadow) {
		super(bevelType, highlight, shadow);
		init();
	}

	public SoftBevelBorder(int bevelType, Color highlightOuterColor, Color highlightInnerColor,
			Color shadowOuterColor, Color shadowInnerColor) {
		super(bevelType, highlightOuterColor, highlightInnerColor, shadowOuterColor,
				shadowInnerColor);
		init();
	}

	private void init() {
		insets.top = 3;
		insets.left = 3;
		insets.right = 3;
		insets.bottom = 3;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	}

	public Insets getBorderInsets(Component c, Insets is) {
		is.set(insets.top, insets.left, insets.bottom, insets.right);
		return is;
	}

	public boolean isBorderOpaque() {
		return false;
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
				g.drawLine(0, 0, width - 2, 0);
				g.drawLine(0, 0, 0, height - 2);
				g.drawLine(1, 1, 1, 1);

				g.setColor(bevelType == RAISED ? highlightOuter : shadowInner);
				g.drawLine(2, 1, width - 2, 1);
				g.drawLine(1, 2, 1, height - 2);
				g.drawLine(2, 2, 2, 2);
				g.drawLine(0, height - 1, 0, height - 2);
				g.drawLine(width - 1, 0, width - 1, 0);

				g.setColor(bevelType == RAISED ? shadowInner : highlightOuter);
				g.drawLine(2, height - 1, width - 1, height - 1);
				g.drawLine(width - 1, 2, width - 1, height - 1);

				g.setColor(bevelType == RAISED ? shadowInner : highlightOuter);
				g.drawLine(width - 2, height - 2, width - 2, height - 2);
			}
		};
	}
}
