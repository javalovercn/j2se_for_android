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
import java.util.Arrays;

/**
 * The <code>Polygon</code> class encapsulates a description of a
 * closed, two-dimensional region within a coordinate space. This
 * region is bounded by an arbitrary number of line segments, each of
 * which is one side of the polygon. Internally, a polygon
 * comprises of a list of {@code (x,y)}
 * coordinate pairs, where each pair defines a <i>vertex</i> of the
 * polygon, and two successive pairs are the endpoints of a
 * line that is a side of the polygon. The first and final
 * pairs of {@code (x,y)} points are joined by a line segment
 * that closes the polygon.  This <code>Polygon</code> is defined with
 * an even-odd winding rule.  See
 * {@link java.awt.geom.PathIterator#WIND_EVEN_ODD WIND_EVEN_ODD}
 * for a definition of the even-odd winding rule.
 * This class's hit-testing methods, which include the
 * <code>contains</code>, <code>intersects</code> and <code>inside</code>
 * methods, use the <i>insideness</i> definition described in the
 * {@link Shape} class comments.
 *
 * @author      Sami Shaio
 * @see Shape
 * @author      Herb Jellinek
 * @since       1.0
 */
public class Polygon implements Shape, java.io.Serializable {

	public int npoints;

	public int xpoints[];

	public int ypoints[];

	protected Rectangle bounds;

	private static final int MIN_LENGTH = 4;

	public Polygon() {
		xpoints = new int[MIN_LENGTH];
		ypoints = new int[MIN_LENGTH];
	}

	public Polygon(int xpoints[], int ypoints[], int npoints) {
		if (npoints > xpoints.length || npoints > ypoints.length) {
			throw new IndexOutOfBoundsException("npoints > xpoints.length || "
					+ "npoints > ypoints.length");
		}
		if (npoints < 0) {
			throw new NegativeArraySizeException("npoints < 0");
		}
		this.npoints = npoints;
		this.xpoints = Arrays.copyOf(xpoints, npoints);
		this.ypoints = Arrays.copyOf(ypoints, npoints);
	}

	public void reset() {
		npoints = 0;
		bounds = null;
	}

	public void invalidate() {
		bounds = null;
	}

	public void translate(int deltaX, int deltaY) {
		for (int i = 0; i < npoints; i++) {
			xpoints[i] += deltaX;
			ypoints[i] += deltaY;
		}
		if (bounds != null) {
			bounds.translate(deltaX, deltaY);
		}
	}

	void calculateBounds(int xpoints[], int ypoints[], int npoints) {
		int boundsMinX = Integer.MAX_VALUE;
		int boundsMinY = Integer.MAX_VALUE;
		int boundsMaxX = Integer.MIN_VALUE;
		int boundsMaxY = Integer.MIN_VALUE;

		for (int i = 0; i < npoints; i++) {
			int x = xpoints[i];
			boundsMinX = Math.min(boundsMinX, x);
			boundsMaxX = Math.max(boundsMaxX, x);
			int y = ypoints[i];
			boundsMinY = Math.min(boundsMinY, y);
			boundsMaxY = Math.max(boundsMaxY, y);
		}
		bounds = new Rectangle(boundsMinX, boundsMinY, boundsMaxX - boundsMinX,
				boundsMaxY - boundsMinY);
	}

	void updateBounds(int x, int y) {
		if (x < bounds.x) {
			bounds.width = bounds.width + (bounds.x - x);
			bounds.x = x;
		} else {
			bounds.width = Math.max(bounds.width, x - bounds.x);
			// bounds.x = bounds.x;
		}

		if (y < bounds.y) {
			bounds.height = bounds.height + (bounds.y - y);
			bounds.y = y;
		} else {
			bounds.height = Math.max(bounds.height, y - bounds.y);
			// bounds.y = bounds.y;
		}
	}

	public void addPoint(int x, int y) {
		if (npoints >= xpoints.length || npoints >= ypoints.length) {
			int newLength = npoints * 2;
			if (newLength < MIN_LENGTH) {
				newLength = MIN_LENGTH;
			} else if ((newLength & (newLength - 1)) != 0) {
				newLength = Integer.highestOneBit(newLength);
			}

			xpoints = Arrays.copyOf(xpoints, newLength);
			ypoints = Arrays.copyOf(ypoints, newLength);
		}
		xpoints[npoints] = x;
		ypoints[npoints] = y;
		npoints++;
		if (bounds != null) {
			updateBounds(x, y);
		}
	}

	public Rectangle getBounds() {
		return getBoundingBox();
	}

	public Rectangle getBoundingBox() {
		if (npoints == 0) {
			return new Rectangle();
		}
		if (bounds == null) {
			calculateBounds(xpoints, ypoints, npoints);
		}
		return bounds.getBounds();
	}

	public boolean contains(Point p) {
		return contains(p.x, p.y);
	}

	public boolean contains(int x, int y) {
		return contains((double) x, (double) y);
	}

	public boolean inside(int x, int y) {
		return contains((double) x, (double) y);
	}

	public boolean contains(double x, double y) {
		if (npoints <= 2 || !getBoundingBox().contains((int) x, (int) y)) {
			return false;
		}
		int hits = 0;

		int lastx = xpoints[npoints - 1];
		int lasty = ypoints[npoints - 1];
		int curx, cury;

		for (int i = 0; i < npoints; lastx = curx, lasty = cury, i++) {
			curx = xpoints[i];
			cury = ypoints[i];

			if (cury == lasty) {
				continue;
			}

			int leftx;
			if (curx < lastx) {
				if (x >= lastx) {
					continue;
				}
				leftx = curx;
			} else {
				if (x >= curx) {
					continue;
				}
				leftx = lastx;
			}

			double test1, test2;
			if (cury < lasty) {
				if (y < cury || y >= lasty) {
					continue;
				}
				if (x < leftx) {
					hits++;
					continue;
				}
				test1 = x - curx;
				test2 = y - cury;
			} else {
				if (y < lasty || y >= cury) {
					continue;
				}
				if (x < leftx) {
					hits++;
					continue;
				}
				test1 = x - lastx;
				test2 = y - lasty;
			}

			if (test1 < (test2 / (lasty - cury) * (lastx - curx))) {
				hits++;
			}
		}

		return ((hits & 1) != 0);
	}

    /**
     * {@inheritDoc}
     * @since 1.2
     */
    public boolean intersects(Rectangle2D r) {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }
    
	public boolean intersects(double x, double y, double w, double h) {
		if (npoints <= 0
				|| !getBoundingBox().intersects((int) x, (int) y, (int) w,
						(int) h)) {
			return false;
		}

		// Crossings cross = getCrossings(x, y, x+w, y+h);
		// return (cross == null || !cross.isEmpty());
		return true;
	}

	public boolean contains(double x, double y, double w, double h) {
		if (npoints <= 0
				|| !getBoundingBox().intersects((int) x, (int) y, (int) w,
						(int) h)) {
			return false;
		}

		// Crossings cross = getCrossings(x, y, x+w, y+h);
		// return (cross != null && cross.covers(y, y+h));
		return true;
	}

	@Override
	public Rectangle2D getBounds2D() {
		return null;
	}
}
