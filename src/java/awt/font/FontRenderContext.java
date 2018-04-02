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
package java.awt.font;

import java.awt.geom.AffineTransform;

/**
 * The <code>FontRenderContext</code> class is a container for the information
 * needed to correctly measure text. The measurement of text can vary because of
 * rules that map outlines to pixels, and rendering hints provided by an
 * application.
 * <p>
 * One such piece of information is a transform that scales typographical points
 * to pixels. (A point is defined to be exactly 1/72 of an inch, which is
 * slightly different than the traditional mechanical measurement of a point.) A
 * character that is rendered at 12pt on a 600dpi device might have a different
 * size than the same character rendered at 12pt on a 72dpi device because of
 * such factors as rounding to pixel boundaries and hints that the font designer
 * may have specified.
 * <p>
 * Anti-aliasing and Fractional-metrics specified by an application can also
 * affect the size of a character because of rounding to pixel boundaries.
 * <p>
 * Typically, instances of <code>FontRenderContext</code> are obtained from a
 * {@link java.awt.Graphics2D Graphics2D} object. A
 * <code>FontRenderContext</code> which is directly constructed will most likely
 * not represent any actual graphics device, and may lead to unexpected or
 * incorrect results.
 * <p>
 * 
 * @see java.awt.RenderingHints#KEY_TEXT_ANTIALIASING
 * @see java.awt.RenderingHints#KEY_FRACTIONALMETRICS
 * @see java.awt.Graphics2D#getFontRenderContext()
 * @see java.awt.font.LineMetrics
 */

public class FontRenderContext {

	protected FontRenderContext() {
	}

	public FontRenderContext(AffineTransform tx, boolean isAntiAliased,
			boolean usesFractionalMetrics) {
	}

	public FontRenderContext(AffineTransform tx, Object aaHint, Object fmHint) {
	}

	public boolean isTransformed() {
		return false;
	}

	public int getTransformType() {
		return 0;
	}

	public AffineTransform getTransform() {
		return new AffineTransform();
	}

	public boolean isAntiAliased() {
		return true;
	}

	public boolean usesFractionalMetrics() {
		return false;
	}

	public Object getAntiAliasingHint() {
		return null;
	}

	public Object getFractionalMetricsHint() {
		return null;
	}

	public boolean equals(Object obj) {
		return false;
	}

	public boolean equals(FontRenderContext rhs) {
		return false;
	}

	public int hashCode() {
		return 0;
	}
}
