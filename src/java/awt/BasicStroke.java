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

import hc.android.AndroidClassUtil;

/**
 * The <code>BasicStroke</code> class defines a basic set of rendering
 * attributes for the outlines of graphics primitives, which are rendered
 * with a {@link Graphics2D} object that has its Stroke attribute set to
 * this <code>BasicStroke</code>.
 * The rendering attributes defined by <code>BasicStroke</code> describe
 * the shape of the mark made by a pen drawn along the outline of a
 * {@link Shape} and the decorations applied at the ends and joins of
 * path segments of the <code>Shape</code>.
 * These rendering attributes include:
 * <dl compact>
 * <dt><i>width</i>
 * <dd>The pen width, measured perpendicularly to the pen trajectory.
 * <dt><i>end caps</i>
 * <dd>The decoration applied to the ends of unclosed subpaths and
 * dash segments.  Subpaths that start and end on the same point are
 * still considered unclosed if they do not have a CLOSE segment.
 * See {@link java.awt.geom.PathIterator#SEG_CLOSE SEG_CLOSE}
 * for more information on the CLOSE segment.
 * The three different decorations are: {@link #CAP_BUTT},
 * {@link #CAP_ROUND}, and {@link #CAP_SQUARE}.
 * <dt><i>line joins</i>
 * <dd>The decoration applied at the intersection of two path segments
 * and at the intersection of the endpoints of a subpath that is closed
 * using {@link java.awt.geom.PathIterator#SEG_CLOSE SEG_CLOSE}.
 * The three different decorations are: {@link #JOIN_BEVEL},
 * {@link #JOIN_MITER}, and {@link #JOIN_ROUND}.
 * <dt><i>miter limit</i>
 * <dd>The limit to trim a line join that has a JOIN_MITER decoration.
 * A line join is trimmed when the ratio of miter length to stroke
 * width is greater than the miterlimit value.  The miter length is
 * the diagonal length of the miter, which is the distance between
 * the inside corner and the outside corner of the intersection.
 * The smaller the angle formed by two line segments, the longer
 * the miter length and the sharper the angle of intersection.  The
 * default miterlimit value of 10.0f causes all angles less than
 * 11 degrees to be trimmed.  Trimming miters converts
 * the decoration of the line join to bevel.
 * <dt><i>dash attributes</i>
 * <dd>The definition of how to make a dash pattern by alternating
 * between opaque and transparent sections.
 * </dl>
 * All attributes that specify measurements and distances controlling
 * the shape of the returned outline are measured in the same
 * coordinate system as the original unstroked <code>Shape</code>
 * argument.  When a <code>Graphics2D</code> object uses a
 * <code>Stroke</code> object to redefine a path during the execution
 * of one of its <code>draw</code> methods, the geometry is supplied
 * in its original form before the <code>Graphics2D</code> transform
 * attribute is applied.  Therefore, attributes such as the pen width
 * are interpreted in the user space coordinate system of the
 * <code>Graphics2D</code> object and are subject to the scaling and
 * shearing effects of the user-space-to-device-space transform in that
 * particular <code>Graphics2D</code>.
 * For example, the width of a rendered shape's outline is determined
 * not only by the width attribute of this <code>BasicStroke</code>,
 * but also by the transform attribute of the
 * <code>Graphics2D</code> object.  Consider this code:
 * <blockquote><tt>
 *      // sets the Graphics2D object's Tranform attribute
 *      g2d.scale(10, 10);
 *      // sets the Graphics2D object's Stroke attribute
 *      g2d.setStroke(new BasicStroke(1.5f));
 * </tt></blockquote>
 * Assuming there are no other scaling transforms added to the
 * <code>Graphics2D</code> object, the resulting line
 * will be approximately 15 pixels wide.
 * As the example code demonstrates, a floating-point line
 * offers better precision, especially when large transforms are
 * used with a <code>Graphics2D</code> object.
 * When a line is diagonal, the exact width depends on how the
 * rendering pipeline chooses which pixels to fill as it traces the
 * theoretical widened outline.  The choice of which pixels to turn
 * on is affected by the antialiasing attribute because the
 * antialiasing rendering pipeline can choose to color
 * partially-covered pixels.
 * <p>
 * For more information on the user space coordinate system and the
 * rendering process, see the <code>Graphics2D</code> class comments.
 * @see Graphics2D
 * @author Jim Graham
 */
public class BasicStroke implements Stroke {
	public final static int JOIN_MITER = 0;
	public final static int JOIN_ROUND = 1;
	public final static int JOIN_BEVEL = 2;
	public final static int CAP_BUTT = 0;
	public final static int CAP_ROUND = 1;
	public final static int CAP_SQUARE = 2;

	float width;
	int join;
	int cap;
	float miterlimit;

	float dash[];
	float dash_phase;

	public BasicStroke(float width, int cap, int join, float miterlimit,
			float dash[], float dash_phase) {
		this.width = width;
		this.cap = cap;
		this.join = join;
		this.miterlimit = miterlimit;
		this.dash = dash;
		this.dash_phase = dash_phase;
	}

	public BasicStroke(float width, int cap, int join, float miterlimit) {
		this(width, cap, join, miterlimit, null, 0.0f);
	}

	public BasicStroke(float width, int cap, int join) {
		this(width, cap, join, 10.0f, null, 0.0f);
	}

	public BasicStroke(float width) {
		this(width, CAP_SQUARE, JOIN_MITER, 10.0f, null, 0.0f);
	}

	public BasicStroke() {
		this(1.0f, CAP_SQUARE, JOIN_MITER, 10.0f, null, 0.0f);
	}

	public Shape createStrokedShape(Shape s) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public float getLineWidth() {
		return width;
	}

	public int getEndCap() {
		return cap;
	}

	public int getLineJoin() {
		return join;
	}

	public float getMiterLimit() {
		return miterlimit;
	}

	public float[] getDashArray() {
		if (dash == null) {
			return null;
		}

		return dash.clone();
	}

	public float getDashPhase() {
		return dash_phase;
	}

	public int hashCode() {
		int hash = Float.floatToIntBits(width);
        hash = hash * 31 + join;
        hash = hash * 31 + cap;
        hash = hash * 31 + Float.floatToIntBits(miterlimit);
        if (dash != null) {
            hash = hash * 31 + Float.floatToIntBits(dash_phase);
            for (int i = 0; i < dash.length; i++) {
                hash = hash * 31 + Float.floatToIntBits(dash[i]);
            }
        }
        return hash;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof BasicStroke)) {
            return false;
        }

        BasicStroke bs = (BasicStroke) obj;
        if (width != bs.width) {
            return false;
        }

        if (join != bs.join) {
            return false;
        }

        if (cap != bs.cap) {
            return false;
        }

        if (miterlimit != bs.miterlimit) {
            return false;
        }

        if (dash != null) {
            if (dash_phase != bs.dash_phase) {
                return false;
            }

            if (!java.util.Arrays.equals(dash, bs.dash)) {
                return false;
            }
        }
        else if (bs.dash != null) {
            return false;
        }

        return true;
	}
}
