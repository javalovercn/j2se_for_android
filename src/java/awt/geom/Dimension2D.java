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
 * The <code>Dimension2D</code> class is to encapsulate a width and a height
 * dimension.
 * <p>
 * This class is only the abstract superclass for all objects that store a 2D
 * dimension. The actual storage representation of the sizes is left to the
 * subclass.
 *
 * @author Jim Graham
 * @since 1.2
 */
public abstract class Dimension2D implements Cloneable {
	protected Dimension2D() {
	}

	public abstract double getWidth();

	public abstract double getHeight();

	public abstract void setSize(double width, double height);

	public void setSize(Dimension2D d) {
		setSize(d.getWidth(), d.getHeight());
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}
}
