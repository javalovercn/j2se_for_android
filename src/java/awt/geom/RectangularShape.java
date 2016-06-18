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

import java.awt.Rectangle;
import java.awt.Shape;

/**
 * <code>RectangularShape</code> is the base class for a number of
 * {@link Shape} objects whose geometry is defined by a rectangular frame.
 * This class does not directly specify any specific geometry by
 * itself, but merely provides manipulation methods inherited by
 * a whole category of <code>Shape</code> objects.
 * The manipulation methods provided by this class can be used to
 * query and modify the rectangular frame, which provides a reference
 * for the subclasses to define their geometry.
 *
 * @author      Jim Graham
 * @since 1.2
 */
public abstract class RectangularShape implements Shape, Cloneable {

    protected RectangularShape() {
    }

    public abstract double getX();

    public abstract double getY();

    public abstract double getWidth();

    public abstract double getHeight();

    public double getMinX() {
        return getX();
    }

    public double getMinY() {
        return getY();
    }

    public double getMaxX() {
        return getX() + getWidth();
    }

    public double getMaxY() {
        return getY() + getHeight();
    }

    public double getCenterX() {
        return getX() + getWidth() / 2.0;
    }

    public double getCenterY() {
        return getY() + getHeight() / 2.0;
    }

    public Rectangle2D getFrame() {
        return new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
    }

    public abstract boolean isEmpty();

    public abstract void setFrame(double x, double y, double w, double h);

    public void setFrame(Point2D loc, Dimension2D size) {
        setFrame(loc.getX(), loc.getY(), size.getWidth(), size.getHeight());
    }

    public void setFrame(Rectangle2D r) {
        setFrame(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public void setFrameFromDiagonal(double x1, double y1,
                                     double x2, double y2) {
        if (x2 < x1) {
            double t = x1;
            x1 = x2;
            x2 = t;
        }
        if (y2 < y1) {
            double t = y1;
            y1 = y2;
            y2 = t;
        }
        setFrame(x1, y1, x2 - x1, y2 - y1);
    }

    public void setFrameFromDiagonal(Point2D p1, Point2D p2) {
        setFrameFromDiagonal(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    public void setFrameFromCenter(double centerX, double centerY,
                                   double cornerX, double cornerY) {
        double halfW = Math.abs(cornerX - centerX);
        double halfH = Math.abs(cornerY - centerY);
        setFrame(centerX - halfW, centerY - halfH, halfW * 2.0, halfH * 2.0);
    }

    public void setFrameFromCenter(Point2D center, Point2D corner) {
        setFrameFromCenter(center.getX(), center.getY(),
                           corner.getX(), corner.getY());
    }

    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    public boolean intersects(Rectangle2D r) {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public boolean contains(Rectangle2D r) {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public Rectangle getBounds() {
        double width = getWidth();
        double height = getHeight();
        if (width < 0 || height < 0) {
            return new Rectangle();
        }
        double x = getX();
        double y = getY();
        double x1 = Math.floor(x);
        double y1 = Math.floor(y);
        double x2 = Math.ceil(x + width);
        double y2 = Math.ceil(y + height);
        return new Rectangle((int) x1, (int) y1,
                                      (int) (x2 - x1), (int) (y2 - y1));
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return null;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }
}
