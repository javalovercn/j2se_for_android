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

import java.awt.geom.Point2D;

/**
 * A point representing a location in {@code (x,y)} coordinate space, specified
 * in integer precision.
 *
 * @author Sami Shaio
 * @since 1.0
 */
public class Point extends Point2D implements java.io.Serializable {
	public int x;

	public int y;

	public Point() {
		this(0, 0);
	}

	public Point(Point p) {
		this(p.x, p.y);
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Point getLocation() {
		return new Point(x, y);
	}

	public void setLocation(Point p) {
		setLocation(p.x, p.y);
	}

	public void setLocation(int x, int y) {
		move(x, y);
	}

	public void setLocation(double x, double y) {
		this.x = (int) Math.floor(x + 0.5);
		this.y = (int) Math.floor(y + 0.5);
	}

	public void move(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void translate(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Point) {
			Point pt = (Point) obj;
			return (x == pt.x) && (y == pt.y);
		}
		return super.equals(obj);
	}

	public String toString() {
		return getClass().getName() + "[x=" + x + ",y=" + y + "]";
	}
}
