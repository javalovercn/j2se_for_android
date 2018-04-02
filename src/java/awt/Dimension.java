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

import java.awt.geom.Dimension2D;

/**
 * The <code>Dimension</code> class encapsulates the width and height of a
 * component (in integer precision) in a single object. The class is associated
 * with certain properties of components. Several methods defined by the
 * <code>Component</code> class and the <code>LayoutManager</code> interface
 * return a <code>Dimension</code> object.
 * <p>
 * Normally the values of <code>width</code> and <code>height</code> are
 * non-negative integers. The constructors that allow you to create a dimension
 * do not prevent you from setting a negative value for these properties. If the
 * value of <code>width</code> or <code>height</code> is negative, the behavior
 * of some methods defined by other objects is undefined.
 *
 * @author Sami Shaio
 * @author Arthur van Hoff
 * @see java.awt.Component
 * @see java.awt.LayoutManager
 * @since 1.0
 */
public class Dimension extends Dimension2D implements java.io.Serializable {

	public int width;

	public int height;

	public Dimension() {
		this(0, 0);
	}

	public Dimension(Dimension d) {
		this(d.width, d.height);
	}

	public Dimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public void setSize(double width, double height) {
		this.width = (int) Math.ceil(width);
		this.height = (int) Math.ceil(height);
	}

	public Dimension getSize() {
		return new Dimension(width, height);
	}

	public void setSize(Dimension d) {
		setSize(d.width, d.height);
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Dimension) {
			Dimension d = (Dimension) obj;
			return (width == d.width) && (height == d.height);
		}
		return false;
	}

	public int hashCode() {
		int sum = width + height;
		return sum * (sum + 1) / 2 + width;
	}

	public String toString() {
		return getClass().getName() + "[width=" + width + ",height=" + height + "]";
	}

	public Object clone() {
		Dimension out = new Dimension();
		out.width = width;
		out.height = height;
		return out;
	}
}