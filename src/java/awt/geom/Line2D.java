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
import java.io.Serializable;

/**
 * This <code>Line2D</code> represents a line segment in {@code (x,y)}
 * coordinate space.  This class, like all of the Java 2D API, uses a
 * default coordinate system called <i>user space</i> in which the y-axis
 * values increase downward and x-axis values increase to the right.  For
 * more information on the user space coordinate system, see the
 * <a href="http://java.sun.com/j2se/1.3/docs/guide/2d/spec/j2d-intro.fm2.html#61857">
 * Coordinate Systems</a> section of the Java 2D Programmer's Guide.
 * <p>
 * This class is only the abstract superclass for all objects that
 * store a 2D line segment.
 * The actual storage representation of the coordinates is left to
 * the subclass.
 *
 * @author      Jim Graham
 * @since 1.2
 */
public abstract class Line2D implements Shape, Cloneable {
    public static class Float extends Line2D implements Serializable {
        public float x1;
        public float y1;
        public float x2;
        public float y2;
        public Float() {
        }

        public Float(float x1, float y1, float x2, float y2) {
            setLine(x1, y1, x2, y2);
        }
        public Float(Point2D p1, Point2D p2) {
            setLine(p1, p2);
        }
        public double getX1() {
            return (double) x1;
        }
        public double getY1() {
            return (double) y1;
        }
        public Point2D getP1() {
            return new Point2D.Float(x1, y1);
        }
        public double getX2() {
            return (double) x2;
        }
        public double getY2() {
            return (double) y2;
        }
        public Point2D getP2() {
            return new Point2D.Float(x2, y2);
        }
        public void setLine(double x1, double y1, double x2, double y2) {
            this.x1 = (float) x1;
            this.y1 = (float) y1;
            this.x2 = (float) x2;
            this.y2 = (float) y2;
        }
        public void setLine(float x1, float y1, float x2, float y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
        public Rectangle2D getBounds2D() {
            float x, y, w, h;
            if (x1 < x2) {
                x = x1;
                w = x2 - x1;
            } else {
                x = x2;
                w = x1 - x2;
            }
            if (y1 < y2) {
                y = y1;
                h = y2 - y1;
            } else {
                y = y2;
                h = y1 - y2;
            }
            return new Rectangle2D.Double(x, y, w, h);
        }
    }

    public static class Double extends Line2D implements Serializable {
        public double x1;
        public double y1;
        public double x2;
        public double y2;

        public Double() {
        }

        public Double(double x1, double y1, double x2, double y2) {
            setLine(x1, y1, x2, y2);
        }

        public Double(Point2D p1, Point2D p2) {
            setLine(p1, p2);
        }

        public double getX1() {
            return x1;
        }

        public double getY1() {
            return y1;
        }

        public Point2D getP1() {
            return new Point2D.Double(x1, y1);
        }

        public double getX2() {
            return x2;
        }

        public double getY2() {
            return y2;
        }

        public Point2D getP2() {
            return new Point2D.Double(x2, y2);
        }

        public void setLine(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public Rectangle2D getBounds2D() {
            double x, y, w, h;
            if (x1 < x2) {
                x = x1;
                w = x2 - x1;
            } else {
                x = x2;
                w = x1 - x2;
            }
            if (y1 < y2) {
                y = y1;
                h = y2 - y1;
            } else {
                y = y2;
                h = y1 - y2;
            }
            return new Rectangle2D.Double(x, y, w, h);
        }

    }

    protected Line2D() {
    }

    public abstract double getX1();

    public abstract double getY1();

    public abstract Point2D getP1();

    public abstract double getX2();

    public abstract double getY2();

    public abstract Point2D getP2();

    public abstract void setLine(double x1, double y1, double x2, double y2);

    public void setLine(Point2D p1, Point2D p2) {
        setLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    public void setLine(Line2D l) {
        setLine(l.getX1(), l.getY1(), l.getX2(), l.getY2());
    }

    public static int relativeCCW(double x1, double y1,
                                  double x2, double y2,
                                  double px, double py)
    {
    	return 0;
    }

    public int relativeCCW(double px, double py) {
        return relativeCCW(getX1(), getY1(), getX2(), getY2(), px, py);
    }

    public int relativeCCW(Point2D p) {
        return relativeCCW(getX1(), getY1(), getX2(), getY2(),
                           p.getX(), p.getY());
    }

    public static boolean linesIntersect(double x1, double y1,
                                         double x2, double y2,
                                         double x3, double y3,
                                         double x4, double y4)
    {
        return ((relativeCCW(x1, y1, x2, y2, x3, y3) *
                 relativeCCW(x1, y1, x2, y2, x4, y4) <= 0)
                && (relativeCCW(x3, y3, x4, y4, x1, y1) *
                    relativeCCW(x3, y3, x4, y4, x2, y2) <= 0));
    }

    public boolean intersectsLine(double x1, double y1, double x2, double y2) {
        return linesIntersect(x1, y1, x2, y2,
                              getX1(), getY1(), getX2(), getY2());
    }

    public boolean intersectsLine(Line2D l) {
        return linesIntersect(l.getX1(), l.getY1(), l.getX2(), l.getY2(),
                              getX1(), getY1(), getX2(), getY2());
    }

    public static double ptSegDistSq(double x1, double y1,
                                     double x2, double y2,
                                     double px, double py)
    {
        return 0;
    }

    public static double ptSegDist(double x1, double y1,
                                   double x2, double y2,
                                   double px, double py)
    {
        return Math.sqrt(ptSegDistSq(x1, y1, x2, y2, px, py));
    }

    public double ptSegDistSq(double px, double py) {
        return ptSegDistSq(getX1(), getY1(), getX2(), getY2(), px, py);
    }

    public double ptSegDistSq(Point2D pt) {
        return ptSegDistSq(getX1(), getY1(), getX2(), getY2(),
                           pt.getX(), pt.getY());
    }

    public double ptSegDist(double px, double py) {
        return ptSegDist(getX1(), getY1(), getX2(), getY2(), px, py);
    }

    public double ptSegDist(Point2D pt) {
        return ptSegDist(getX1(), getY1(), getX2(), getY2(),
                         pt.getX(), pt.getY());
    }

    public static double ptLineDistSq(double x1, double y1,
                                      double x2, double y2,
                                      double px, double py)
    {
        return 0;
    }

    public static double ptLineDist(double x1, double y1,
                                    double x2, double y2,
                                    double px, double py)
    {
        return Math.sqrt(ptLineDistSq(x1, y1, x2, y2, px, py));
    }

    public double ptLineDistSq(double px, double py) {
        return ptLineDistSq(getX1(), getY1(), getX2(), getY2(), px, py);
    }

    public double ptLineDistSq(Point2D pt) {
        return ptLineDistSq(getX1(), getY1(), getX2(), getY2(),
                            pt.getX(), pt.getY());
    }

    public double ptLineDist(double px, double py) {
        return ptLineDist(getX1(), getY1(), getX2(), getY2(), px, py);
    }

    public double ptLineDist(Point2D pt) {
        return ptLineDist(getX1(), getY1(), getX2(), getY2(),
                         pt.getX(), pt.getY());
    }

    public boolean contains(double x, double y) {
        return false;
    }

    public boolean contains(Point2D p) {
        return false;
    }

    public boolean intersects(double x, double y, double w, double h) {
        return intersects(new Rectangle2D.Double(x, y, w, h));
    }

    public boolean intersects(Rectangle2D r) {
        return r.intersectsLine(getX1(), getY1(), getX2(), getY2());
    }

    public boolean contains(double x, double y, double w, double h) {
        return false;
    }

    public boolean contains(Rectangle2D r) {
        return false;
    }

    public Rectangle getBounds() {
        return getBounds2D().getBounds();
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return null;
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return null;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
