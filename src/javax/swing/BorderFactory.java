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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.StrokeBorder;
import javax.swing.border.TitledBorder;

/**
 * Factory class for vending standard <code>Border</code> objects. Wherever
 * possible, this factory will hand out references to shared <code>Border</code>
 * instances. For further information and examples see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/misc/border.html">How to Use
 * Borders</a>, a section in <em>The Java Tutorial</em>.
 *
 * @author David Kloba
 */
public class BorderFactory {

	private BorderFactory() {
	}

	public static Border createLineBorder(Color color) {
		return new LineBorder(color, 1);
	}

	public static Border createLineBorder(Color color, int thickness) {
		return new LineBorder(color, thickness);
	}

	public static Border createLineBorder(Color color, int thickness, boolean rounded) {
		return new LineBorder(color, thickness, rounded);
	}

	public static Border createRaisedBevelBorder() {
		return createSharedBevel(BevelBorder.RAISED);
	}

	public static Border createLoweredBevelBorder() {
		return createSharedBevel(BevelBorder.LOWERED);
	}

	public static Border createBevelBorder(int type) {
		return createSharedBevel(type);
	}

	public static Border createBevelBorder(int type, Color highlight, Color shadow) {
		return new BevelBorder(type, highlight, shadow);
	}

	public static Border createBevelBorder(int type, Color highlightOuter, Color highlightInner,
			Color shadowOuter, Color shadowInner) {
		return new BevelBorder(type, highlightOuter, highlightInner, shadowOuter, shadowInner);
	}

	static Border createSharedBevel(int type) {
		if (type == BevelBorder.RAISED) {
			return new BevelBorder(BevelBorder.RAISED);
		} else if (type == BevelBorder.LOWERED) {
			return new BevelBorder(BevelBorder.LOWERED);
		}
		return null;
	}

	public static Border createRaisedSoftBevelBorder() {
		return new SoftBevelBorder(BevelBorder.RAISED);
	}

	public static Border createLoweredSoftBevelBorder() {
		return new SoftBevelBorder(BevelBorder.LOWERED);
	}

	public static Border createSoftBevelBorder(int type) {
		if (type == BevelBorder.RAISED) {
			return createRaisedSoftBevelBorder();
		}
		if (type == BevelBorder.LOWERED) {
			return createLoweredSoftBevelBorder();
		}
		return null;
	}

	public static Border createSoftBevelBorder(int type, Color highlight, Color shadow) {
		return new SoftBevelBorder(type, highlight, shadow);
	}

	public static Border createSoftBevelBorder(int type, Color highlightOuter, Color highlightInner,
			Color shadowOuter, Color shadowInner) {
		return new SoftBevelBorder(type, highlightOuter, highlightInner, shadowOuter, shadowInner);
	}

	public static Border createEtchedBorder() {
		return new EtchedBorder();
	}

	public static Border createEtchedBorder(Color highlight, Color shadow) {
		return new EtchedBorder(highlight, shadow);
	}

	public static Border createEtchedBorder(int type) {
		switch (type) {
		case EtchedBorder.RAISED:
			return new EtchedBorder(EtchedBorder.RAISED);
		case EtchedBorder.LOWERED:
			return new EtchedBorder();
		default:
			throw new IllegalArgumentException(
					"type must be one of EtchedBorder.RAISED or EtchedBorder.LOWERED");
		}
	}

	public static Border createEtchedBorder(int type, Color highlight, Color shadow) {
		return new EtchedBorder(type, highlight, shadow);
	}

	public static TitledBorder createTitledBorder(String title) {
		return new TitledBorder(title);
	}

	public static TitledBorder createTitledBorder(Border border) {
		return new TitledBorder(border);
	}

	public static TitledBorder createTitledBorder(Border border, String title) {
		return new TitledBorder(border, title);
	}

	public static TitledBorder createTitledBorder(Border border, String title,
			int titleJustification, int titlePosition) {
		return new TitledBorder(border, title, titleJustification, titlePosition);
	}

	public static TitledBorder createTitledBorder(Border border, String title,
			int titleJustification, int titlePosition, Font titleFont) {
		return new TitledBorder(border, title, titleJustification, titlePosition, titleFont);
	}

	public static TitledBorder createTitledBorder(Border border, String title,
			int titleJustification, int titlePosition, Font titleFont, Color titleColor) {
		return new TitledBorder(border, title, titleJustification, titlePosition, titleFont,
				titleColor);
	}

	public static Border createEmptyBorder() {
		return new EmptyBorder(0, 0, 0, 0);
	}

	public static Border createEmptyBorder(int top, int left, int bottom, int right) {
		return new EmptyBorder(top, left, bottom, right);
	}

	public static CompoundBorder createCompoundBorder() {
		return new CompoundBorder();
	}

	public static CompoundBorder createCompoundBorder(Border outsideBorder, Border insideBorder) {
		return new CompoundBorder(outsideBorder, insideBorder);
	}

	public static MatteBorder createMatteBorder(int top, int left, int bottom, int right,
			Color color) {
		return new MatteBorder(top, left, bottom, right, color);
	}

	public static MatteBorder createMatteBorder(int top, int left, int bottom, int right,
			Icon tileIcon) {
		return new MatteBorder(top, left, bottom, right, tileIcon);
	}

	public static Border createStrokeBorder(BasicStroke stroke) {
		return new StrokeBorder(stroke);
	}

	public static Border createStrokeBorder(BasicStroke stroke, Paint paint) {
		return new StrokeBorder(stroke, paint);
	}

	public static Border createDashedBorder(Paint paint) {
		return createDashedBorder(paint, 1.0f, 1.0f, 1.0f, false);
	}

	public static Border createDashedBorder(Paint paint, float length, float spacing) {
		return createDashedBorder(paint, 1.0f, length, spacing, false);
	}

	public static Border createDashedBorder(Paint paint, float thickness, float length,
			float spacing, boolean rounded) {
		Border border = createStrokeBorder(
				new BasicStroke(thickness, 0, 0, thickness * 2.0f, null, 0.0f), paint);
		return border;
	}
}
