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

import java.util.NoSuchElementException;

/**
 * A utility class to iterate over the path segments of a rectangle
 * through the PathIterator interface.
 *
 * @author      Jim Graham
 */
class RectIterator implements PathIterator {
    double x, y, w, h;
    AffineTransform affine;
    int index;

    RectIterator(Rectangle2D r, AffineTransform at) {
        this.x = r.getX();
        this.y = r.getY();
        this.w = r.getWidth();
        this.h = r.getHeight();
        this.affine = at;
        if (w < 0 || h < 0) {
            index = 6;
        }
    }

    public int getWindingRule() {
        return WIND_NON_ZERO;
    }

    public boolean isDone() {
        return index > 5;
    }

    public void next() {
        index++;
    }

    public int currentSegment(float[] coords) {
        if (isDone()) {
            throw new NoSuchElementException("rect iterator out of bounds");
        }
        if (index == 5) {
            return SEG_CLOSE;
        }
        coords[0] = (float) x;
        coords[1] = (float) y;
        if (index == 1 || index == 2) {
            coords[0] += (float) w;
        }
        if (index == 2 || index == 3) {
            coords[1] += (float) h;
        }
        if (affine != null) {
            affine.transform(coords, 0, coords, 0, 1);
        }
        return (index == 0 ? SEG_MOVETO : SEG_LINETO);
    }

    public int currentSegment(double[] coords) {
        if (isDone()) {
            throw new NoSuchElementException("rect iterator out of bounds");
        }
        if (index == 5) {
            return SEG_CLOSE;
        }
        coords[0] = x;
        coords[1] = y;
        if (index == 1 || index == 2) {
            coords[0] += w;
        }
        if (index == 2 || index == 3) {
            coords[1] += h;
        }
        if (affine != null) {
            affine.transform(coords, 0, coords, 0, 1);
        }
        return (index == 0 ? SEG_MOVETO : SEG_LINETO);
    }
}