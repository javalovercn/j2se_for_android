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
 * A <code>Rectangle</code> specifies an area in a coordinate space that is
 * enclosed by the <code>Rectangle</code> object's upper-left point
 * {@code (x,y)} in the coordinate space, its width, and its height.
 * <p>
 * A <code>Rectangle</code> object's <code>width</code> and <code>height</code>
 * are <code>public</code> fields. The constructors that create a
 * <code>Rectangle</code>, and the methods that can modify one, do not prevent
 * setting a negative value for width or height.
 * <p>
 * <a name="Empty"> A {@code Rectangle} whose width or height is exactly zero
 * has location along those axes with zero dimension, but is otherwise
 * considered empty. The {@link #isEmpty} method will return true for such a
 * {@code Rectangle}. Methods which test if an empty {@code Rectangle} contains
 * or intersects a point or rectangle will always return false if either
 * dimension is zero. Methods which combine such a {@code Rectangle} with a
 * point or rectangle will include the location of the {@code Rectangle} on that
 * axis in the result as if the {@link #add(Point)} method were being called.
 * </a>
 * <p>
 * <a name="NonExistant"> A {@code Rectangle} whose width or height is negative
 * has neither location nor dimension along those axes with negative dimensions.
 * Such a {@code Rectangle} is treated as non-existant along those axes. Such a
 * {@code Rectangle} is also empty with respect to containment calculations and
 * methods which test if it contains or intersects a point or rectangle will
 * always return false. Methods which combine such a {@code Rectangle} with a
 * point or rectangle will ignore the {@code Rectangle} entirely in generating
 * the result. If two {@code Rectangle} objects are combined and each has a
 * negative dimension, the result will have at least one negative dimension.
 * </a>
 * <p>
 * Methods which affect only the location of a {@code Rectangle} will operate on
 * its location regardless of whether or not it has a negative or zero dimension
 * along either axis.
 * <p>
 * Note that a {@code Rectangle} constructed with the default no-argument
 * constructor will have dimensions of {@code 0x0} and therefore be empty. That
 * {@code Rectangle} will still have a location of {@code (0,0)} and will
 * contribute that location to the union and add operations. Code attempting to
 * accumulate the bounds of a set of points should therefore initially construct
 * the {@code Rectangle} with a specifically negative width and height or it
 * should use the first point in the set to construct the {@code Rectangle}. For
 * example:
 * 
 * <pre>
 * Rectangle bounds = new Rectangle(0, 0, -1, -1);
 * for (int i = 0; i < points.length; i++) {
 * 	bounds.add(points[i]);
 * }
 * </pre>
 * 
 * or if we know that the points array contains at least one point:
 * 
 * <pre>
 * Rectangle bounds = new Rectangle(points[0]);
 * for (int i = 1; i < points.length; i++) {
 * 	bounds.add(points[i]);
 * }
 * </pre>
 * <p>
 * This class uses 32-bit integers to store its location and dimensions.
 * Frequently operations may produce a result that exceeds the range of a 32-bit
 * integer. The methods will calculate their results in a way that avoids any
 * 32-bit overflow for intermediate results and then choose the best
 * representation to store the final results back into the 32-bit fields which
 * hold the location and dimensions. The location of the result will be stored
 * into the {@link #x} and {@link #y} fields by clipping the true result to the
 * nearest 32-bit value. The values stored into the {@link #width} and
 * {@link #height} dimension fields will be chosen as the 32-bit values that
 * encompass the largest part of the true result as possible. Generally this
 * means that the dimension will be clipped independently to the range of 32-bit
 * integers except that if the location had to be moved to store it into its
 * pair of 32-bit fields then the dimensions will be adjusted relative to the
 * "best representation" of the location. If the true result had a negative
 * dimension and was therefore non-existant along one or both axes, the stored
 * dimensions will be negative numbers in those axes. If the true result had a
 * location that could be represented within the range of 32-bit integers, but
 * zero dimension along one or both axes, then the stored dimensions will be
 * zero in those axes.
 *
 * @author Sami Shaio
 * @since 1.0
 */
public class Rectangle extends Rectangle2D implements Shape, java.io.Serializable {
	public int x;

	public int y;

	public int width;

	public int height;

	public Rectangle() {
		this(0, 0, 0, 0);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.2
	 */
	public Rectangle2D createIntersection(Rectangle2D r) {
		if (r instanceof Rectangle) {
			return intersection((Rectangle) r);
		}
		Rectangle2D dest = new Rectangle2D.Double();
		Rectangle2D.intersect(this, r, dest);
		return dest;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.2
	 */
	public Rectangle2D createUnion(Rectangle2D r) {
		if (r instanceof Rectangle) {
			return union((Rectangle) r);
		}
		Rectangle2D dest = new Rectangle2D.Double();
		Rectangle2D.union(this, r, dest);
		return dest;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.2
	 */
	public boolean isEmpty() {
		return (width <= 0) || (height <= 0);
	}

	/**
	 * Computes the intersection of this <code>Rectangle</code> with the
	 * specified <code>Rectangle</code>. Returns a new <code>Rectangle</code>
	 * that represents the intersection of the two rectangles. If the two
	 * rectangles do not intersect, the result will be an empty rectangle.
	 *
	 * @param r
	 *            the specified <code>Rectangle</code>
	 * @return the largest <code>Rectangle</code> contained in both the
	 *         specified <code>Rectangle</code> and in this
	 *         <code>Rectangle</code>; or if the rectangles do not intersect, an
	 *         empty rectangle.
	 */
	public Rectangle intersection(Rectangle r) {
		int tx1 = this.x;
		int ty1 = this.y;
		int rx1 = r.x;
		int ry1 = r.y;
		long tx2 = tx1;
		tx2 += this.width;
		long ty2 = ty1;
		ty2 += this.height;
		long rx2 = rx1;
		rx2 += r.width;
		long ry2 = ry1;
		ry2 += r.height;
		if (tx1 < rx1)
			tx1 = rx1;
		if (ty1 < ry1)
			ty1 = ry1;
		if (tx2 > rx2)
			tx2 = rx2;
		if (ty2 > ry2)
			ty2 = ry2;
		tx2 -= tx1;
		ty2 -= ty1;
		// tx2,ty2 will never overflow (they will never be
		// larger than the smallest of the two source w,h)
		// they might underflow, though...
		if (tx2 < Integer.MIN_VALUE)
			tx2 = Integer.MIN_VALUE;
		if (ty2 < Integer.MIN_VALUE)
			ty2 = Integer.MIN_VALUE;
		return new Rectangle(tx1, ty1, (int) tx2, (int) ty2);
	}

	public void setLocation(Point p) {
		setLocation(p.x, p.y);
	}

	public void setLocation(int x, int y) {
		move(x, y);
	}

	/**
	 * Moves this <code>Rectangle</code> to the specified location.
	 * <p>
	 * 
	 * @param x
	 *            the X coordinate of the new location
	 * @param y
	 *            the Y coordinate of the new location
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>setLocation(int, int)</code>.
	 */
	@Deprecated
	public void move(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the bounds of this {@code Rectangle} to the integer bounds which
	 * encompass the specified {@code x}, {@code y}, {@code width}, and
	 * {@code height}. If the parameters specify a {@code Rectangle} that
	 * exceeds the maximum range of integers, the result will be the best
	 * representation of the specified {@code Rectangle} intersected with the
	 * maximum integer bounds.
	 * 
	 * @param x
	 *            the X coordinate of the upper-left corner of the specified
	 *            rectangle
	 * @param y
	 *            the Y coordinate of the upper-left corner of the specified
	 *            rectangle
	 * @param width
	 *            the width of the specified rectangle
	 * @param height
	 *            the new height of the specified rectangle
	 */
	public void setRect(double x, double y, double width, double height) {
		int newx, newy, neww, newh;

		if (x > 2.0 * Integer.MAX_VALUE) {
			// Too far in positive X direction to represent...
			// We cannot even reach the left side of the specified
			// rectangle even with both x & width set to MAX_VALUE.
			// The intersection with the "maximal integer rectangle"
			// is non-existant so we should use a width < 0.
			// REMIND: Should we try to determine a more "meaningful"
			// adjusted value for neww than just "-1"?
			newx = Integer.MAX_VALUE;
			neww = -1;
		} else {
			newx = clip(x, false);
			if (width >= 0)
				width += x - newx;
			neww = clip(width, width >= 0);
		}

		if (y > 2.0 * Integer.MAX_VALUE) {
			// Too far in positive Y direction to represent...
			newy = Integer.MAX_VALUE;
			newh = -1;
		} else {
			newy = clip(y, false);
			if (height >= 0)
				height += y - newy;
			newh = clip(height, height >= 0);
		}

		reshape(newx, newy, neww, newh);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.2
	 */
	public int outcode(double x, double y) {
		/*
		 * Note on casts to double below. If the arithmetic of x+w or y+h is
		 * done in int, then we may get integer overflow. By converting to
		 * double before the addition we force the addition to be carried out in
		 * double to avoid overflow in the comparison.
		 *
		 * See bug 4320890 for problems that this can cause.
		 */
		int out = 0;
		if (this.width <= 0) {
			out |= OUT_LEFT | OUT_RIGHT;
		} else if (x < this.x) {
			out |= OUT_LEFT;
		} else if (x > this.x + (double) this.width) {
			out |= OUT_RIGHT;
		}
		if (this.height <= 0) {
			out |= OUT_TOP | OUT_BOTTOM;
		} else if (y < this.y) {
			out |= OUT_TOP;
		} else if (y > this.y + (double) this.height) {
			out |= OUT_BOTTOM;
		}
		return out;
	}

	// Return best integer representation for v, clipped to integer
	// range and floor-ed or ceiling-ed, depending on the boolean.
	private static int clip(double v, boolean doceil) {
		if (v <= Integer.MIN_VALUE) {
			return Integer.MIN_VALUE;
		}
		if (v >= Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}
		return (int) (doceil ? Math.ceil(v) : Math.floor(v));
	}

	/**
	 * Constructs a new <code>Rectangle</code> whose upper-left corner is
	 * specified by the {@link Point} argument, and whose width and height are
	 * specified by the {@link Dimension} argument.
	 * 
	 * @param p
	 *            a <code>Point</code> that is the upper-left corner of the
	 *            <code>Rectangle</code>
	 * @param d
	 *            a <code>Dimension</code>, representing the width and height of
	 *            the <code>Rectangle</code>
	 */
	public Rectangle(Point p, Dimension d) {
		this(p.x, p.y, d.width, d.height);
	}

	public Rectangle(Dimension d) {
		this(0, 0, d.width, d.height);
	}

	public Rectangle(Rectangle r) {
		this(r.x, r.y, r.width, r.height);
	}

	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Rectangle(int width, int height) {
		this(0, 0, width, height);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public void setBounds(Rectangle r) {
		setBounds(r.x, r.y, r.width, r.height);
	}

	public void setBounds(int x, int y, int width, int height) {
		reshape(x, y, width, height);
	}

	public void reshape(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Dimension getSize() {
		return new Dimension(width, height);
	}

	public void translate(int dx, int dy) {
		int oldv = this.x;
		int newv = oldv + dx;
		if (dx < 0) {
			if (newv > oldv) {
				if (width >= 0) {
					width += newv - Integer.MIN_VALUE;
				}
				newv = Integer.MIN_VALUE;
			}
		} else {
			if (newv < oldv) {
				if (width >= 0) {
					width += newv - Integer.MAX_VALUE;
					if (width < 0)
						width = Integer.MAX_VALUE;
				}
				newv = Integer.MAX_VALUE;
			}
		}
		this.x = newv;

		oldv = this.y;
		newv = oldv + dy;
		if (dy < 0) {
			if (newv > oldv) {
				if (height >= 0) {
					height += newv - Integer.MIN_VALUE;
				}
				newv = Integer.MIN_VALUE;
			}
		} else {
			if (newv < oldv) {
				if (height >= 0) {
					height += newv - Integer.MAX_VALUE;
					if (height < 0)
						height = Integer.MAX_VALUE;
				}
				newv = Integer.MAX_VALUE;
			}
		}
		this.y = newv;
	}

	public Point getLocation() {
		return new Point(x, y);
	}

	public boolean contains(int X, int Y, int W, int H) {
		int w = this.width;
		int h = this.height;
		if ((w | h | W | H) < 0) {
			return false;
		}
		int x = this.x;
		int y = this.y;
		if (X < x || Y < y) {
			return false;
		}
		w += x;
		W += X;
		if (W <= X) {
			if (w >= x || W > w)
				return false;
		} else {
			if (w >= x && W > w)
				return false;
		}
		h += y;
		H += Y;
		if (H <= Y) {
			if (h >= y || H > h)
				return false;
		} else {
			if (h >= y && H > h)
				return false;
		}
		return true;
	}

	public boolean contains(Rectangle r) {
		return contains(r.x, r.y, r.width, r.height);
	}

	public boolean contains(Point p) {
		return contains(p.x, p.y);
	}

	public boolean contains(int x, int y) {
		return inside(x, y);
	}

	public boolean inside(int X, int Y) {
		int w = this.width;
		int h = this.height;
		if ((w | h) < 0) {
			return false;
		}
		int x = this.x;
		int y = this.y;
		if (X < x || Y < y) {
			return false;
		}
		w += x;
		h += y;
		return ((w < x || w > X) && (h < y || h > Y));
	}

	public boolean intersects(int rx, int ry, int rw, int rh) {
		return intersects(new Rectangle(rx, ry, rw, rh));
	}

	public boolean intersects(Rectangle r) {
		int tw = this.width;
		int th = this.height;
		int rw = r.width;
		int rh = r.height;
		if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
			return false;
		}
		int tx = this.x;
		int ty = this.y;
		int rx = r.x;
		int ry = r.y;
		rw += rx;
		rh += ry;
		tw += tx;
		th += ty;
		return ((rw < rx || rw > tx) && (rh < ry || rh > ty) && (tw < tx || tw > rx)
				&& (th < ty || th > ry));
	}

	public Rectangle union(Rectangle r) {
		long tx2 = this.width;
		long ty2 = this.height;
		if ((tx2 | ty2) < 0) {
			return new Rectangle(r);
		}
		long rx2 = r.width;
		long ry2 = r.height;
		if ((rx2 | ry2) < 0) {
			return new Rectangle(this);
		}
		int tx1 = this.x;
		int ty1 = this.y;
		tx2 += tx1;
		ty2 += ty1;
		int rx1 = r.x;
		int ry1 = r.y;
		rx2 += rx1;
		ry2 += ry1;
		if (tx1 > rx1)
			tx1 = rx1;
		if (ty1 > ry1)
			ty1 = ry1;
		if (tx2 < rx2)
			tx2 = rx2;
		if (ty2 < ry2)
			ty2 = ry2;
		tx2 -= tx1;
		ty2 -= ty1;
		if (tx2 > Integer.MAX_VALUE)
			tx2 = Integer.MAX_VALUE;
		if (ty2 > Integer.MAX_VALUE)
			ty2 = Integer.MAX_VALUE;
		return new Rectangle(tx1, ty1, (int) tx2, (int) ty2);
	}

	public String toString() {
		return getClass().getName() + "[x=" + x + ",y=" + y + ",width=" + width + ",height="
				+ height + "]";
	}
}
