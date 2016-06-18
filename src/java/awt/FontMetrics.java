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
package java.awt;

import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.text.CharacterIterator;
import android.graphics.Rect;

/**
 * The <code>FontMetrics</code> class defines a font metrics object, which
 * encapsulates information about the rendering of a particular font on a
 * particular screen.
 * <p>
 * <b>Note to subclassers</b>: Since many of these methods form closed,
 * mutually recursive loops, you must take care that you implement
 * at least one of the methods in each such loop to prevent
 * infinite recursion when your subclass is used.
 * In particular, the following is the minimal suggested set of methods
 * to override in order to ensure correctness and prevent infinite
 * recursion (though other subsets are equally feasible):
 * <ul>
 * <li>{@link #getAscent()}
 * <li>{@link #getLeading()}
 * <li>{@link #getMaxAdvance()}
 * <li>{@link #charWidth(char)}
 * <li>{@link #charsWidth(char[], int, int)}
 * </ul>
 * <p>
 * <img src="doc-files/FontMetrics-1.gif" alt="The letter 'p' showing its 'reference point'" border=15 align
 * ALIGN=right HSPACE=10 VSPACE=7>
 * Note that the implementations of these methods are
 * inefficient, so they are usually overridden with more efficient
 * toolkit-specific implementations.
 * <p>
 * When an application asks to place a character at the position
 * (<i>x</i>,&nbsp;<i>y</i>), the character is placed so that its
 * reference point (shown as the dot in the accompanying image) is
 * put at that position. The reference point specifies a horizontal
 * line called the <i>baseline</i> of the character. In normal
 * printing, the baselines of characters should align.
 * <p>
 * In addition, every character in a font has an <i>ascent</i>, a
 * <i>descent</i>, and an <i>advance width</i>. The ascent is the
 * amount by which the character ascends above the baseline. The
 * descent is the amount by which the character descends below the
 * baseline. The advance width indicates the position at which AWT
 * should place the next character.
 * <p>
 * An array of characters or a string can also have an ascent, a
 * descent, and an advance width. The ascent of the array is the
 * maximum ascent of any character in the array. The descent is the
 * maximum descent of any character in the array. The advance width
 * is the sum of the advance widths of each of the characters in the
 * character array.  The advance of a <code>String</code> is the
 * distance along the baseline of the <code>String</code>.  This
 * distance is the width that should be used for centering or
 * right-aligning the <code>String</code>.
 * <p>Note that the advance of a <code>String</code> is not necessarily
 * the sum of the advances of its characters measured in isolation
 * because the width of a character can vary depending on its context.
 * For example, in Arabic text, the shape of a character can change
 * in order to connect to other characters.  Also, in some scripts,
 * certain character sequences can be represented by a single shape,
 * called a <em>ligature</em>.  Measuring characters individually does
 * not account for these transformations.
 * <p>Font metrics are baseline-relative, meaning that they are
 * generally independent of the rotation applied to the font (modulo
 * possible grid hinting effects).  See {@link java.awt.Font Font}.
 *
 * @author      Jim Graham
 * @see         java.awt.Font
 * @since       JDK1.0
 */
public abstract class FontMetrics implements java.io.Serializable {
	protected Font font;

	protected FontMetrics(Font font) {
		this.font = font;
	}

	public Font getFont() {
		return font;
	}

	public FontRenderContext getFontRenderContext() {
		return null;// DEFAULT_FRC;
	}

	public int getLeading() {
		return 0;
	}

	public int getAscent() {
		return font.getSize();
	}

	public int getDescent() {
		return 0;
	}

	public int getHeight() {
		return getLeading() + getAscent() + getDescent();
	}

	public int getMaxAscent() {
		return getAscent();
	}

	public int getMaxDescent() {
		return getDescent();
	}

	public int getMaxDecent() {
		return getMaxDescent();
	}

	public int getMaxAdvance() {
		return -1;
	}

	public int charWidth(int codePoint) {
		if (codePoint < 256) {
			return getWidths()[codePoint];
		} else {
			char[] buffer = new char[2];
			int len = Character.toChars(codePoint, buffer, 0);
			return charsWidth(buffer, 0, len);
		}
	}

	public int charWidth(char ch) {
		char data[] = { ch };
		return charsWidth(data, 0, 1);
	}

	public int stringWidth(String str) {
		Rect rect = font.getStrRect(str);
		return rect.width();
	}

	public int charsWidth(char data[], int off, int len) {
		return stringWidth(new String(data, off, len));
	}

	public int bytesWidth(byte data[], int off, int len) {
		return stringWidth(new String(data, 0, off, len));
	}

	public int[] getWidths() {
		int widths[] = new int[256];
		for (char ch = 0; ch < 256; ch++) {
			widths[ch] = charWidth(ch);
		}
		return widths;
	}

	public boolean hasUniformLineMetrics() {
		return false;
	}

	public LineMetrics getLineMetrics(String str, Graphics context) {
		return null;
	}

	public LineMetrics getLineMetrics(String str, int beginIndex, int limit,
			Graphics context) {
		return null;
	}

	public LineMetrics getLineMetrics(char[] chars, int beginIndex, int limit,
			Graphics context) {
		return null;
	}

	public LineMetrics getLineMetrics(CharacterIterator ci, int beginIndex,
			int limit, Graphics context) {
		return null;
	}

	public Rectangle2D getStringBounds(String str, Graphics context) {
		return font.getStringBounds(str, null);
	}

	public Rectangle2D getStringBounds(String str, int beginIndex, int limit,
			Graphics context) {
		return font.getStringBounds(str, beginIndex, limit, myFRC(context));
	}

	public Rectangle2D getStringBounds(char[] chars, int beginIndex, int limit,
			Graphics context) {
		return font.getStringBounds(chars, beginIndex, limit, myFRC(context));
	}

	public Rectangle2D getStringBounds(CharacterIterator ci, int beginIndex,
			int limit, Graphics context) {
		return font.getStringBounds(ci, beginIndex, limit, myFRC(context));
	}

	public Rectangle2D getMaxCharBounds(Graphics context) {
		return null;
	}

	private FontRenderContext myFRC(Graphics context) {
		return null;
	}

	public String toString() {
		return getClass().getName() + "[font=" + getFont() + ", height="
				+ getHeight() + "]";
	}

}
