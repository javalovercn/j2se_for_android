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

/**
 * The <code>PageFormat</code> class describes the size and
 * orientation of a page to be printed.
 */
public class PageFormat implements Cloneable
{
    public static final int LANDSCAPE = 0;

    public static final int PORTRAIT = 1;

    public static final int REVERSE_LANDSCAPE = 2;

    private Paper mPaper;

    private int mOrientation = PORTRAIT;

    public PageFormat()
    {
    }

    public Object clone() {
        return null;
    }

    public double getWidth() {
        return 0;
    }

    public double getHeight() {
        return 0;
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

    public Paper getPaper() {
        return null;
    }

     public void setPaper(Paper paper) {
     }

    public void setOrientation(int orientation) throws IllegalArgumentException
    {
    }

    public int getOrientation() {
        return mOrientation;
    }

    public double[] getMatrix() {
        double[] matrix = new double[6];
        return matrix;
    }
}
