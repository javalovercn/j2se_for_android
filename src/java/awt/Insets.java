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

/**
 * An <code>Insets</code> object is a representation of the borders
 * of a container. It specifies the space that a container must leave
 * at each of its edges. The space can be a border, a blank space, or
 * a title.
 *
 * @author      Arthur van Hoff
 * @author      Sami Shaio
 * @see         java.awt.LayoutManager
 * @see         java.awt.Container
 * @since       JDK1.0
 */
public class Insets implements Cloneable, java.io.Serializable {

	public int top;
	public int left;
	public int bottom;
	public int right;

	public Insets(int top, int left, int bottom, int right) {
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

	public void set(int top, int left, int bottom, int right) {
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Insets) {
			Insets insets = (Insets) obj;
			return ((top == insets.top) && (left == insets.left)
					&& (bottom == insets.bottom) && (right == insets.right));
		}
		return false;
	}

	public int hashCode() {
		int sum1 = left + bottom;
		int sum2 = right + top;
		int val1 = sum1 * (sum1 + 1) / 2 + left;
		int val2 = sum2 * (sum2 + 1) / 2 + top;
		int sum3 = val1 + val2;
		return sum3 * (sum3 + 1) / 2 + val2;
	}

	public Object clone() {
		Insets clone = new Insets(top, left, bottom, right);
		return clone;
	}

	public String toString() {
		return getClass().getName() + "[top=" + top + ",left=" + left
				+ ",bottom=" + bottom + ",right=" + right + "]";
	}
}