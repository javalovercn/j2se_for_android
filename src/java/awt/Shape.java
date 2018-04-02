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

import java.awt.geom.Rectangle2D;

/**
 * The <code>Shape</code> interface provides definitions for objects that
 * represent some form of geometric shape. The <code>Shape</code> is described
 * by a {@link PathIterator} object, which can express the outline of the
 * <code>Shape</code> as well as a rule for determining how the outline divides
 * the 2D plane into interior and exterior points. Each <code>Shape</code>
 * object provides callbacks to get the bounding box of the geometry, determine
 * whether points or rectangles lie partly or entirely within the interior of
 * the <code>Shape</code>, and retrieve a <code>PathIterator</code> object that
 * describes the trajectory path of the <code>Shape</code> outline.
 * <p>
 * <a name="def_insideness"><b>Definition of insideness:</b></a> A point is
 * considered to lie inside a <code>Shape</code> if and only if:
 * <ul>
 * <li>it lies completely inside the<code>Shape</code> boundary <i>or</i>
 * <li>it lies exactly on the <code>Shape</code> boundary <i>and</i> the space
 * immediately adjacent to the point in the increasing <code>X</code> direction
 * is entirely inside the boundary <i>or</i>
 * <li>it lies exactly on a horizontal boundary segment <b>and</b> the space
 * immediately adjacent to the point in the increasing <code>Y</code> direction
 * is inside the boundary.
 * </ul>
 * <p>
 * The <code>contains</code> and <code>intersects</code> methods consider the
 * interior of a <code>Shape</code> to be the area it encloses as if it were
 * filled. This means that these methods consider unclosed shapes to be
 * implicitly closed for the purpose of determining if a shape contains or
 * intersects a rectangle or if a shape contains a point.
 *
 * @see java.awt.geom.PathIterator
 * @see java.awt.geom.AffineTransform
 * @see java.awt.geom.FlatteningPathIterator
 * @see java.awt.geom.GeneralPath
 *
 * @author Jim Graham
 * @since 1.2
 */
public interface Shape {
	public Rectangle getBounds();

	public boolean contains(double x, double y);

	public boolean intersects(double x, double y, double w, double h);

	/**
	 * Tests if the interior of the <code>Shape</code> intersects the interior
	 * of a specified <code>Rectangle2D</code>. The {@code Shape.intersects()}
	 * method allows a {@code Shape} implementation to conservatively return
	 * {@code true} when:
	 * <ul>
	 * <li>there is a high probability that the <code>Rectangle2D</code> and the
	 * <code>Shape</code> intersect, but
	 * <li>the calculations to accurately determine this intersection are
	 * prohibitively expensive.
	 * </ul>
	 * This means that for some {@code Shapes} this method might return
	 * {@code true} even though the {@code Rectangle2D} does not intersect the
	 * {@code Shape}. The {@link java.awt.geom.Area Area} class performs more
	 * accurate computations of geometric intersection than most {@code Shape}
	 * objects and therefore can be used if a more precise answer is required.
	 *
	 * @param r
	 *            the specified <code>Rectangle2D</code>
	 * @return <code>true</code> if the interior of the <code>Shape</code> and
	 *         the interior of the specified <code>Rectangle2D</code> intersect,
	 *         or are both highly likely to intersect and intersection
	 *         calculations would be too expensive to perform;
	 *         <code>false</code> otherwise.
	 * @see #intersects(double, double, double, double)
	 * @since 1.2
	 */
	public boolean intersects(Rectangle2D r);

	public boolean contains(double x, double y, double w, double h);

	public Rectangle2D getBounds2D();
}
