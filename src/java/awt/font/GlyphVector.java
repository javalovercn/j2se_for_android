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
 * @author Charlton Innovations, Inc.
 */

package java.awt.font;

import java.awt.Font;
import java.awt.Polygon; // remind - need a floating point version
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.Shape;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphJustificationInfo;

/**
 * A <code>GlyphVector</code> object is a collection of glyphs containing
 * geometric information for the placement of each glyph in a transformed
 * coordinate space which corresponds to the device on which the
 * <code>GlyphVector</code> is ultimately displayed.
 * <p>
 * The <code>GlyphVector</code> does not attempt any interpretation of the
 * sequence of glyphs it contains. Relationships between adjacent glyphs in
 * sequence are solely used to determine the placement of the glyphs in the
 * visual coordinate space.
 * <p>
 * Instances of <code>GlyphVector</code> are created by a {@link Font}.
 * <p>
 * In a text processing application that can cache intermediate representations
 * of text, creation and subsequent caching of a <code>GlyphVector</code> for
 * use during rendering is the fastest method to present the visual
 * representation of characters to a user.
 * <p>
 * A <code>GlyphVector</code> is associated with exactly one <code>Font</code>,
 * and can provide data useful only in relation to this <code>Font</code>. In
 * addition, metrics obtained from a <code>GlyphVector</code> are not generally
 * geometrically scaleable since the pixelization and spacing are dependent on
 * grid-fitting algorithms within a <code>Font</code>. To facilitate accurate
 * measurement of a <code>GlyphVector</code> and its component glyphs, you must
 * specify a scaling transform, anti-alias mode, and fractional metrics mode
 * when creating the <code>GlyphVector</code>. These characteristics can be
 * derived from the destination device.
 * <p>
 * For each glyph in the <code>GlyphVector</code>, you can obtain:
 * <ul>
 * <li>the position of the glyph
 * <li>the transform associated with the glyph
 * <li>the metrics of the glyph in the context of the <code>GlyphVector</code>.
 * The metrics of the glyph may be different under different transforms,
 * application specified rendering hints, and the specific instance of the glyph
 * within the <code>GlyphVector</code>.
 * </ul>
 * <p>
 * Altering the data used to create the <code>GlyphVector</code> does not alter
 * the state of the <code>GlyphVector</code>.
 * <p>
 * Methods are provided to adjust the positions of the glyphs within the
 * <code>GlyphVector</code>. These methods are most appropriate for applications
 * that are performing justification operations for the presentation of the
 * glyphs.
 * <p>
 * Methods are provided to transform individual glyphs within the
 * <code>GlyphVector</code>. These methods are primarily useful for special
 * effects.
 * <p>
 * Methods are provided to return both the visual, logical, and pixel bounds of
 * the entire <code>GlyphVector</code> or of individual glyphs within the
 * <code>GlyphVector</code>.
 * <p>
 * Methods are provided to return a {@link Shape} for the
 * <code>GlyphVector</code>, and for individual glyphs within the
 * <code>GlyphVector</code>.
 * 
 * @see Font
 * @see GlyphMetrics
 * @see TextLayout
 * @author Charlton Innovations, Inc.
 */

public abstract class GlyphVector implements Cloneable {
	public abstract Font getFont();

	public abstract FontRenderContext getFontRenderContext();

	public abstract void performDefaultLayout();

	public abstract int getNumGlyphs();

	public abstract int getGlyphCode(int glyphIndex);

	public abstract int[] getGlyphCodes(int beginGlyphIndex, int numEntries, int[] codeReturn);

	public int getGlyphCharIndex(int glyphIndex) {
		return glyphIndex;
	}

	public int[] getGlyphCharIndices(int beginGlyphIndex, int numEntries, int[] codeReturn) {
		if (codeReturn == null) {
			codeReturn = new int[numEntries];
		}
		for (int i = 0, j = beginGlyphIndex; i < numEntries; ++i, ++j) {
			codeReturn[i] = getGlyphCharIndex(j);
		}
		return codeReturn;
	}

	public abstract Rectangle2D getLogicalBounds();

	public abstract Rectangle2D getVisualBounds();

	public Rectangle getPixelBounds(FontRenderContext renderFRC, float x, float y) {
		Rectangle2D rect = getVisualBounds();
		int l = (int) Math.floor(rect.getX() + x);
		int t = (int) Math.floor(rect.getY() + y);
		int r = (int) Math.ceil(rect.getMaxX() + x);
		int b = (int) Math.ceil(rect.getMaxY() + y);
		return new Rectangle(l, t, r - l, b - t);
	}

	public abstract Shape getOutline();

	public abstract Shape getOutline(float x, float y);

	public abstract Shape getGlyphOutline(int glyphIndex);

	public Shape getGlyphOutline(int glyphIndex, float x, float y) {
		Shape s = getGlyphOutline(glyphIndex);
		AffineTransform at = AffineTransform.getTranslateInstance(x, y);
		return at.createTransformedShape(s);
	}

	public abstract Point2D getGlyphPosition(int glyphIndex);

	public abstract void setGlyphPosition(int glyphIndex, Point2D newPos);

	public abstract AffineTransform getGlyphTransform(int glyphIndex);

	public abstract void setGlyphTransform(int glyphIndex, AffineTransform newTX);

	public int getLayoutFlags() {
		return 0;
	}

	public static final int FLAG_HAS_TRANSFORMS = 1;
	public static final int FLAG_HAS_POSITION_ADJUSTMENTS = 2;
	public static final int FLAG_RUN_RTL = 4;
	public static final int FLAG_COMPLEX_GLYPHS = 8;
	public static final int FLAG_MASK = FLAG_HAS_TRANSFORMS | FLAG_HAS_POSITION_ADJUSTMENTS
			| FLAG_RUN_RTL | FLAG_COMPLEX_GLYPHS;

	public abstract float[] getGlyphPositions(int beginGlyphIndex, int numEntries,
			float[] positionReturn);

	public abstract Shape getGlyphLogicalBounds(int glyphIndex);

	public abstract Shape getGlyphVisualBounds(int glyphIndex);

	public Rectangle getGlyphPixelBounds(int index, FontRenderContext renderFRC, float x, float y) {
		Rectangle2D rect = getGlyphVisualBounds(index).getBounds2D();
		int l = (int) Math.floor(rect.getX() + x);
		int t = (int) Math.floor(rect.getY() + y);
		int r = (int) Math.ceil(rect.getMaxX() + x);
		int b = (int) Math.ceil(rect.getMaxY() + y);
		return new Rectangle(l, t, r - l, b - t);
	}

	public abstract GlyphMetrics getGlyphMetrics(int glyphIndex);

	public abstract GlyphJustificationInfo getGlyphJustificationInfo(int glyphIndex);

	public abstract boolean equals(GlyphVector set);
}