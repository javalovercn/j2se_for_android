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

/*
 * (C) Copyright Taligent, Inc. 1996 - 1997, All Rights Reserved
 * (C) Copyright IBM Corp. 1996 - 1998, All Rights Reserved
 *
 * The original version of this source code and documentation is
 * copyrighted and owned by Taligent, Inc., a wholly-owned subsidiary
 * of IBM. These materials are provided under terms of a License
 * Agreement between Taligent and Sun. This technology is protected
 * by multiple US and International patents.
 *
 * This notice and attribution to Taligent may not be removed.
 * Taligent is a registered trademark of Taligent, Inc.
 *
 */

package java.awt.font;

import java.awt.geom.Rectangle2D;

/**
 * The <code>GlyphMetrics</code> class represents infomation for a single glyph.
 * A glyph is the visual representation of one or more characters. Many
 * different glyphs can be used to represent a single character or combination
 * of characters. <code>GlyphMetrics</code> instances are produced by
 * {@link java.awt.Font Font} and are applicable to a specific glyph in a
 * particular <code>Font</code>.
 * <p>
 * Glyphs are either STANDARD, LIGATURE, COMBINING, or COMPONENT.
 * <ul>
 * <li>STANDARD glyphs are commonly used to represent single characters.
 * <li>LIGATURE glyphs are used to represent sequences of characters.
 * <li>COMPONENT glyphs in a {@link GlyphVector} do not correspond to a
 * particular character in a text model. Instead, COMPONENT glyphs are added for
 * typographical reasons, such as Arabic justification.
 * <li>COMBINING glyphs embellish STANDARD or LIGATURE glyphs, such as accent
 * marks. Carets do not appear before COMBINING glyphs.
 * </ul>
 * <p>
 * Other metrics available through <code>GlyphMetrics</code> are the components
 * of the advance, the visual bounds, and the left and right side bearings.
 * <p>
 * Glyphs for a rotated font, or obtained from a <code>GlyphVector</code> which
 * has applied a rotation to the glyph, can have advances that contain both X
 * and Y components. Usually the advance only has one component.
 * <p>
 * The advance of a glyph is the distance from the glyph's origin to the origin
 * of the next glyph along the baseline, which is either vertical or horizontal.
 * Note that, in a <code>GlyphVector</code>, the distance from a glyph to its
 * following glyph might not be the glyph's advance, because of kerning or other
 * positioning adjustments.
 * <p>
 * The bounds is the smallest rectangle that completely contains the outline of
 * the glyph. The bounds rectangle is relative to the glyph's origin. The
 * left-side bearing is the distance from the glyph origin to the left of its
 * bounds rectangle. If the left-side bearing is negative, part of the glyph is
 * drawn to the left of its origin. The right-side bearing is the distance from
 * the right side of the bounds rectangle to the next glyph origin (the origin
 * plus the advance). If negative, part of the glyph is drawn to the right of
 * the next glyph's origin. Note that the bounds does not necessarily enclose
 * all the pixels affected when rendering the glyph, because of rasterization
 * and pixel adjustment effects.
 * <p>
 * Although instances of <code>GlyphMetrics</code> can be directly constructed,
 * they are almost always obtained from a <code>GlyphVector</code>. Once
 * constructed, <code>GlyphMetrics</code> objects are immutable.
 * <p>
 * <strong>Example</strong>:
 * <p>
 * Querying a <code>Font</code> for glyph information <blockquote>
 * 
 * <pre>
 * Font font = ...;
 * int glyphIndex = ...;
 * GlyphMetrics metrics = GlyphVector.getGlyphMetrics(glyphIndex);
 * int isStandard = metrics.isStandard();
 * float glyphAdvance = metrics.getAdvance();
 * </pre>
 * 
 * </blockquote>
 * 
 * @see java.awt.Font
 * @see GlyphVector
 */
public final class GlyphMetrics {
	private boolean horizontal;
	private float advanceX;
	private float advanceY;
	private Rectangle2D.Double bounds;
	private byte glyphType;

	public static final byte STANDARD = 0;
	public static final byte LIGATURE = 1;
	public static final byte COMBINING = 2;
	public static final byte COMPONENT = 3;
	public static final byte WHITESPACE = 4;

	public GlyphMetrics(float advance, Rectangle2D bounds, byte glyphType) {
		this.horizontal = true;
		this.advanceX = advance;
		this.advanceY = 0;
		this.bounds = new Rectangle2D.Double();
		this.bounds.setRect(bounds);
		this.glyphType = glyphType;
	}

	public GlyphMetrics(boolean horizontal, float advanceX, float advanceY, Rectangle2D bounds,
			byte glyphType) {

		this.horizontal = horizontal;
		this.advanceX = advanceX;
		this.advanceY = advanceY;
		this.bounds = new Rectangle2D.Double();
		this.bounds.setRect(bounds);
		this.glyphType = glyphType;
	}

	public float getAdvance() {
		return horizontal ? advanceX : advanceY;
	}

	public float getAdvanceX() {
		return advanceX;
	}

	public float getAdvanceY() {
		return advanceY;
	}

	public Rectangle2D getBounds2D() {
		return new Rectangle2D.Double(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	public float getLSB() {
		return (float) (horizontal ? bounds.x : bounds.y);
	}

	public float getRSB() {
		return (float) (horizontal ? advanceX - bounds.x - bounds.width
				: advanceY - bounds.y - bounds.height);
	}

	public int getType() {
		return glyphType;
	}

	public boolean isStandard() {
		return (glyphType & 0x3) == STANDARD;
	}

	public boolean isLigature() {
		return (glyphType & 0x3) == LIGATURE;
	}

	public boolean isCombining() {
		return (glyphType & 0x3) == COMBINING;
	}

	public boolean isComponent() {
		return (glyphType & 0x3) == COMPONENT;
	}

	public boolean isWhitespace() {
		return (glyphType & 0x4) == WHITESPACE;
	}
}
