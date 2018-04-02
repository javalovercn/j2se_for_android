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
package java.awt.geom;

/**
 * The <code>PathIterator</code> interface provides the mechanism for objects
 * that implement the {@link java.awt.Shape Shape} interface to return the
 * geometry of their boundary by allowing a caller to retrieve the path of that
 * boundary a segment at a time. This interface allows these objects to retrieve
 * the path of their boundary a segment at a time by using 1st through 3rd order
 * B&eacute;zier curves, which are lines and quadratic or cubic B&eacute;zier
 * splines.
 * <p>
 * Multiple subpaths can be expressed by using a "MOVETO" segment to create a
 * discontinuity in the geometry to move from the end of one subpath to the
 * beginning of the next.
 * <p>
 * Each subpath can be closed manually by ending the last segment in the subpath
 * on the same coordinate as the beginning "MOVETO" segment for that subpath or
 * by using a "CLOSE" segment to append a line segment from the last point back
 * to the first. Be aware that manually closing an outline as opposed to using a
 * "CLOSE" segment to close the path might result in different line style
 * decorations being used at the end points of the subpath. For example, the
 * {@link java.awt.BasicStroke BasicStroke} object uses a line "JOIN" decoration
 * to connect the first and last points if a "CLOSE" segment is encountered,
 * whereas simply ending the path on the same coordinate as the beginning
 * coordinate results in line "CAP" decorations being used at the ends.
 *
 * @see java.awt.Shape
 * @see java.awt.BasicStroke
 *
 * @author Jim Graham
 */
public interface PathIterator {
	public static final int WIND_EVEN_ODD = 0;
	public static final int WIND_NON_ZERO = 1;
	public static final int SEG_MOVETO = 0;
	public static final int SEG_LINETO = 1;
	public static final int SEG_QUADTO = 2;
	public static final int SEG_CUBICTO = 3;
	public static final int SEG_CLOSE = 4;

	public int getWindingRule();

	public boolean isDone();

	public void next();

	public int currentSegment(float[] coords);

	public int currentSegment(double[] coords);
}
