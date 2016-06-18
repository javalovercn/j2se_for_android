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

import java.io.Serializable;

/**
 * The <code>Rectangle2D</code> class describes a rectangle
 * defined by a location {@code (x,y)} and dimension
 * {@code (w x h)}.
 * <p>
 * This class is only the abstract superclass for all objects that
 * store a 2D rectangle.
 * The actual storage representation of the coordinates is left to
 * the subclass.
 *
 * @author      Jim Graham
 * @since 1.2
 */
public abstract class Rectangle2D extends RectangularShape {
    public static final int OUT_LEFT = 1;
    public static final int OUT_TOP = 2;
    public static final int OUT_RIGHT = 4;
    public static final int OUT_BOTTOM = 8;

    public static class Float extends Rectangle2D implements Serializable {
        public float x;
        public float y;
        public float width;
        public float height;
        public Float() {
        }

        public Float(float x, float y, float w, float h) {
            setRect(x, y, w, h);
        }
        public double getX() {
            return (double) x;
        }
        public double getY() {
            return (double) y;
        }
        public double getWidth() {
            return (double) width;
        }
        public double getHeight() {
            return (double) height;
        }
        public boolean isEmpty() {
            return (width <= 0.0f) || (height <= 0.0f);
        }
        public void setRect(float x, float y, float w, float h) {
            this.x = x;
            this.y = y;
            this.width = w;
            this.height = h;
        }
        public void setRect(double x, double y, double w, double h) {
            this.x = (float) x;
            this.y = (float) y;
            this.width = (float) w;
            this.height = (float) h;
        }
        public void setRect(Rectangle2D r) {
            this.x = (float) r.getX();
            this.y = (float) r.getY();
            this.width = (float) r.getWidth();
            this.height = (float) r.getHeight();
        }
        public int outcode(double x, double y) {
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
        public Rectangle2D getBounds2D() {
            return new Float(x, y, width, height);
        }
        public Rectangle2D createIntersection(Rectangle2D r) {
            Rectangle2D dest;
            if (r instanceof Float) {
                dest = new Rectangle2D.Float();
            } else {
                dest = new Rectangle2D.Double();
            }
            Rectangle2D.intersect(this, r, dest);
            return dest;
        }
        public Rectangle2D createUnion(Rectangle2D r) {
            Rectangle2D dest;
            if (r instanceof Float) {
                dest = new Rectangle2D.Float();
            } else {
                dest = new Rectangle2D.Double();
            }
            Rectangle2D.union(this, r, dest);
            return dest;
        }
        public String toString() {
            return getClass().getName()
                + "[x=" + x +
                ",y=" + y +
                ",w=" + width +
                ",h=" + height + "]";
        }
    }
    
    public static class Double extends Rectangle2D implements Serializable {
        public double x;

        public double y;

        public double width;

        public double height;

        public Double() {
        }

        public Double(double x, double y, double w, double h) {
            setRect(x, y, w, h);
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

        public boolean isEmpty() {
            return (width <= 0.0) || (height <= 0.0);
        }

        public void setRect(double x, double y, double w, double h) {
            this.x = x;
            this.y = y;
            this.width = w;
            this.height = h;
        }

        public void setRect(Rectangle2D r) {
            this.x = r.getX();
            this.y = r.getY();
            this.width = r.getWidth();
            this.height = r.getHeight();
        }

        public int outcode(double x, double y) {
            return 0;
        }

        public Rectangle2D getBounds2D() {
            return new Double(x, y, width, height);
        }

        public Rectangle2D createIntersection(Rectangle2D r) {
            Rectangle2D dest = new Rectangle2D.Double();
            Rectangle2D.intersect(this, r, dest);
            return dest;
        }

        public Rectangle2D createUnion(Rectangle2D r) {
            Rectangle2D dest = new Rectangle2D.Double();
            Rectangle2D.union(this, r, dest);
            return dest;
        }

        public String toString() {
            return getClass().getName() + "[x=" + x + ",y=" + y + ",w=" + width + ",h=" + height + "]";
        }

    }

    protected Rectangle2D() {
    }

    public abstract void setRect(double x, double y, double w, double h);

    public void setRect(Rectangle2D r) {
        setRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public boolean intersectsLine(double x1, double y1, double x2, double y2) {
        return false;
    }

    public boolean intersectsLine(Line2D l) {
        return intersectsLine(l.getX1(), l.getY1(), l.getX2(), l.getY2());
    }

    public abstract int outcode(double x, double y);

    public int outcode(Point2D p) {
        return outcode(p.getX(), p.getY());
    }

    public void setFrame(double x, double y, double w, double h) {
        setRect(x, y, w, h);
    }

    public Rectangle2D getBounds2D() {
        return (Rectangle2D) clone();
    }

    public boolean contains(double x, double y) {
        double x0 = getX();
        double y0 = getY();
        return (x >= x0 &&
                y >= y0 &&
                x < x0 + getWidth() &&
                y < y0 + getHeight());
    }

    public boolean intersects(double x, double y, double w, double h) {
        if (isEmpty() || w <= 0 || h <= 0) {
            return false;
        }
        double x0 = getX();
        double y0 = getY();
        return (x + w > x0 &&
                y + h > y0 &&
                x < x0 + getWidth() &&
                y < y0 + getHeight());
    }

    public boolean contains(double x, double y, double w, double h) {
        if (isEmpty() || w <= 0 || h <= 0) {
            return false;
        }
        double x0 = getX();
        double y0 = getY();
        return (x >= x0 &&
                y >= y0 &&
                (x + w) <= x0 + getWidth() &&
                (y + h) <= y0 + getHeight());
    }

    public abstract Rectangle2D createIntersection(Rectangle2D r);

    public static void intersect(Rectangle2D src1,
                                 Rectangle2D src2,
                                 Rectangle2D dest) {
        double x1 = Math.max(src1.getMinX(), src2.getMinX());
        double y1 = Math.max(src1.getMinY(), src2.getMinY());
        double x2 = Math.min(src1.getMaxX(), src2.getMaxX());
        double y2 = Math.min(src1.getMaxY(), src2.getMaxY());
        dest.setFrame(x1, y1, x2-x1, y2-y1);
    }

    public abstract Rectangle2D createUnion(Rectangle2D r);

    public static void union(Rectangle2D src1,
                             Rectangle2D src2,
                             Rectangle2D dest) {
        double x1 = Math.min(src1.getMinX(), src2.getMinX());
        double y1 = Math.min(src1.getMinY(), src2.getMinY());
        double x2 = Math.max(src1.getMaxX(), src2.getMaxX());
        double y2 = Math.max(src1.getMaxY(), src2.getMaxY());
        dest.setFrameFromDiagonal(x1, y1, x2, y2);
    }

    public void add(double newx, double newy) {
        double x1 = Math.min(getMinX(), newx);
        double x2 = Math.max(getMaxX(), newx);
        double y1 = Math.min(getMinY(), newy);
        double y2 = Math.max(getMaxY(), newy);
        setRect(x1, y1, x2 - x1, y2 - y1);
    }

    public void add(Point2D pt) {
        add(pt.getX(), pt.getY());
    }

    public void add(Rectangle2D r) {
        double x1 = Math.min(getMinX(), r.getMinX());
        double x2 = Math.max(getMaxX(), r.getMaxX());
        double y1 = Math.min(getMinY(), r.getMinY());
        double y2 = Math.max(getMaxY(), r.getMaxY());
        setRect(x1, y1, x2 - x1, y2 - y1);
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return new RectIterator(this, at);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new RectIterator(this, at);
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Rectangle2D) {
            Rectangle2D r2d = (Rectangle2D) obj;
            return ((getX() == r2d.getX()) &&
                    (getY() == r2d.getY()) &&
                    (getWidth() == r2d.getWidth()) &&
                    (getHeight() == r2d.getHeight()));
        }
        return false;
    }
}
