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
package java.awt.print;

import java.awt.geom.Rectangle2D;

/**
 * The <code>Paper</code> class describes the physical characteristics of
 * a piece of paper.
 * <p>
 * When creating a <code>Paper</code> object, it is the application's
 * responsibility to ensure that the paper size and the imageable area
 * are compatible.  For example, if the paper size is changed from
 * 11 x 17 to 8.5 x 11, the application might need to reduce the
 * imageable area so that whatever is printed fits on the page.
 * <p>
 * @see #setSize(double, double)
 * @see #setImageableArea(double, double, double, double)
 */
public class Paper implements Cloneable {
	    private static final int INCH = 72;
	    private static final double LETTER_WIDTH = 8.5 * INCH;
	    private static final double LETTER_HEIGHT = 11 * INCH;
	    private double mHeight;
	    private double mWidth;
	    private Rectangle2D mImageableArea;
	    public Paper() {
	    }

	    public Object clone() {
	    	return null;
	    }

	    public double getHeight() {
	        return mHeight;
	    }

	    public void setSize(double width, double height) {
	    }

	    public double getWidth() {
	        return mWidth;
	    }

	    public void setImageableArea(double x, double y,
	                                 double width, double height) {
	    }

	    public double getImageableX() {
	        return 0;
	    }

	    public double getImageableY() {
	        return 0;
	    }

	    public double getImageableWidth() {
	        return 0;
	    }

	    public double getImageableHeight() {
	        return 0;
	    }
	}