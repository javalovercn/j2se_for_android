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

import java.awt.Shape;

/**
 * The <code>AffineTransform</code> class represents a 2D affine transform that
 * performs a linear mapping from 2D coordinates to other 2D coordinates that
 * preserves the "straightness" and "parallelness" of lines. Affine
 * transformations can be constructed using sequences of translations, scales,
 * flips, rotations, and shears.
 * <p>
 * Such a coordinate transformation can be represented by a 3 row by 3 column
 * matrix with an implied last row of [ 0 0 1 ]. This matrix transforms source
 * coordinates {@code (x,y)} into destination coordinates {@code (x',y')} by
 * considering them to be a column vector and multiplying the coordinate vector
 * by the matrix according to the following process:
 * 
 * <pre>
 *      [ x']   [  m00  m01  m02  ] [ x ]   [ m00x + m01y + m02 ]
 *      [ y'] = [  m10  m11  m12  ] [ y ] = [ m10x + m11y + m12 ]
 *      [ 1 ]   [   0    0    1   ] [ 1 ]   [         1         ]
 * </pre>
 * <p>
 * <a name="quadrantapproximation">
 * <h4>Handling 90-Degree Rotations</h4></a>
 * <p>
 * In some variations of the <code>rotate</code> methods in the
 * <code>AffineTransform</code> class, a double-precision argument specifies the
 * angle of rotation in radians. These methods have special handling for
 * rotations of approximately 90 degrees (including multiples such as 180, 270,
 * and 360 degrees), so that the common case of quadrant rotation is handled
 * more efficiently. This special handling can cause angles very close to
 * multiples of 90 degrees to be treated as if they were exact multiples of 90
 * degrees. For small multiples of 90 degrees the range of angles treated as a
 * quadrant rotation is approximately 0.00000121 degrees wide. This section
 * explains why such special care is needed and how it is implemented.
 * <p>
 * Since 90 degrees is represented as <code>PI/2</code> in radians, and since PI
 * is a transcendental (and therefore irrational) number, it is not possible to
 * exactly represent a multiple of 90 degrees as an exact double precision value
 * measured in radians. As a result it is theoretically impossible to describe
 * quadrant rotations (90, 180, 270 or 360 degrees) using these values. Double
 * precision floating point values can get very close to non-zero multiples of
 * <code>PI/2</code> but never close enough for the sine or cosine to be exactly
 * 0.0, 1.0 or -1.0. The implementations of <code>Math.sin()</code> and
 * <code>Math.cos()</code> correspondingly never return 0.0 for any case other
 * than <code>Math.sin(0.0)</code>. These same implementations do, however,
 * return exactly 1.0 and -1.0 for some range of numbers around each multiple of
 * 90 degrees since the correct answer is so close to 1.0 or -1.0 that the
 * double precision significand cannot represent the difference as accurately as
 * it can for numbers that are near 0.0.
 * <p>
 * The net result of these issues is that if the <code>Math.sin()</code> and
 * <code>Math.cos()</code> methods are used to directly generate the values for
 * the matrix modifications during these radian-based rotation operations then
 * the resulting transform is never strictly classifiable as a quadrant rotation
 * even for a simple case like <code>rotate(Math.PI/2.0)</code>, due to minor
 * variations in the matrix caused by the non-0.0 values obtained for the sine
 * and cosine. If these transforms are not classified as quadrant rotations then
 * subsequent code which attempts to optimize further operations based upon the
 * type of the transform will be relegated to its most general implementation.
 * <p>
 * Because quadrant rotations are fairly common, this class should handle these
 * cases reasonably quickly, both in applying the rotations to the transform and
 * in applying the resulting transform to the coordinates. To facilitate this
 * optimal handling, the methods which take an angle of rotation measured in
 * radians attempt to detect angles that are intended to be quadrant rotations
 * and treat them as such. These methods therefore treat an angle <em>theta</em>
 * as a quadrant rotation if either <code>Math.sin(<em>theta</em>)</code> or
 * <code>Math.cos(<em>theta</em>)</code> returns exactly 1.0 or -1.0. As a rule
 * of thumb, this property holds true for a range of approximately 0.0000000211
 * radians (or 0.00000121 degrees) around small multiples of
 * <code>Math.PI/2.0</code>.
 *
 * @author Jim Graham
 * @since 1.2
 */
public class AffineTransform implements Cloneable, java.io.Serializable {
	// In Android android.graphics.Matrix

	private static final int TYPE_UNKNOWN = -1;
	public static final int TYPE_IDENTITY = 0;
	public static final int TYPE_TRANSLATION = 1;
	public static final int TYPE_UNIFORM_SCALE = 2;
	public static final int TYPE_GENERAL_SCALE = 4;
	public static final int TYPE_MASK_SCALE = (TYPE_UNIFORM_SCALE | TYPE_GENERAL_SCALE);
	public static final int TYPE_FLIP = 64;
	public static final int TYPE_QUADRANT_ROTATION = 8;
	public static final int TYPE_GENERAL_ROTATION = 16;
	public static final int TYPE_MASK_ROTATION = (TYPE_QUADRANT_ROTATION | TYPE_GENERAL_ROTATION);
	public static final int TYPE_GENERAL_TRANSFORM = 32;

	static final int APPLY_IDENTITY = 0;
	static final int APPLY_TRANSLATE = 1;
	static final int APPLY_SCALE = 2;
	static final int APPLY_SHEAR = 4;

	private static final int HI_SHIFT = 3;
	private static final int HI_IDENTITY = APPLY_IDENTITY << HI_SHIFT;
	private static final int HI_TRANSLATE = APPLY_TRANSLATE << HI_SHIFT;
	private static final int HI_SCALE = APPLY_SCALE << HI_SHIFT;
	private static final int HI_SHEAR = APPLY_SHEAR << HI_SHIFT;

	double m00;
	double m10;
	double m01;
	double m11;
	double m02;
	double m12;

	transient int state;

	private transient int type;

	private AffineTransform(double m00, double m10, double m01, double m11, double m02, double m12,
			int state) {
		this.m00 = m00;
		this.m10 = m10;
		this.m01 = m01;
		this.m11 = m11;
		this.m02 = m02;
		this.m12 = m12;
		this.state = state;
		this.type = TYPE_UNKNOWN;
	}

	public AffineTransform() {
		m00 = m11 = 1.0;
	}

	public AffineTransform(AffineTransform Tx) {
		this.m00 = Tx.m00;
		this.m10 = Tx.m10;
		this.m01 = Tx.m01;
		this.m11 = Tx.m11;
		this.m02 = Tx.m02;
		this.m12 = Tx.m12;
		this.state = Tx.state;
		this.type = Tx.type;
	}

	public AffineTransform(float m00, float m10, float m01, float m11, float m02, float m12) {
		this.m00 = m00;
		this.m10 = m10;
		this.m01 = m01;
		this.m11 = m11;
		this.m02 = m02;
		this.m12 = m12;
		updateState();
	}

	public AffineTransform(float[] flatmatrix) {
		m00 = flatmatrix[0];
		m10 = flatmatrix[1];
		m01 = flatmatrix[2];
		m11 = flatmatrix[3];
		if (flatmatrix.length > 5) {
			m02 = flatmatrix[4];
			m12 = flatmatrix[5];
		}
		updateState();
	}

	public AffineTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
		this.m00 = m00;
		this.m10 = m10;
		this.m01 = m01;
		this.m11 = m11;
		this.m02 = m02;
		this.m12 = m12;
		updateState();
	}

	public AffineTransform(double[] flatmatrix) {
		m00 = flatmatrix[0];
		m10 = flatmatrix[1];
		m01 = flatmatrix[2];
		m11 = flatmatrix[3];
		if (flatmatrix.length > 5) {
			m02 = flatmatrix[4];
			m12 = flatmatrix[5];
		}
		updateState();
	}

	public static AffineTransform getTranslateInstance(double tx, double ty) {
		AffineTransform Tx = new AffineTransform();
		Tx.setToTranslation(tx, ty);
		return Tx;
	}

	public static AffineTransform getRotateInstance(double theta) {
		AffineTransform Tx = new AffineTransform();
		Tx.setToRotation(theta);
		return Tx;
	}

	public static AffineTransform getRotateInstance(double theta, double anchorx, double anchory) {
		AffineTransform Tx = new AffineTransform();
		Tx.setToRotation(theta, anchorx, anchory);
		return Tx;
	}

	public static AffineTransform getRotateInstance(double vecx, double vecy) {
		AffineTransform Tx = new AffineTransform();
		Tx.setToRotation(vecx, vecy);
		return Tx;
	}

	public static AffineTransform getRotateInstance(double vecx, double vecy, double anchorx,
			double anchory) {
		AffineTransform Tx = new AffineTransform();
		Tx.setToRotation(vecx, vecy, anchorx, anchory);
		return Tx;
	}

	public static AffineTransform getQuadrantRotateInstance(int numquadrants) {
		AffineTransform Tx = new AffineTransform();
		Tx.setToQuadrantRotation(numquadrants);
		return Tx;
	}

	public static AffineTransform getQuadrantRotateInstance(int numquadrants, double anchorx,
			double anchory) {
		AffineTransform Tx = new AffineTransform();
		Tx.setToQuadrantRotation(numquadrants, anchorx, anchory);
		return Tx;
	}

	public static AffineTransform getScaleInstance(double sx, double sy) {
		AffineTransform Tx = new AffineTransform();
		Tx.setToScale(sx, sy);
		return Tx;
	}

	public static AffineTransform getShearInstance(double shx, double shy) {
		AffineTransform Tx = new AffineTransform();
		Tx.setToShear(shx, shy);
		return Tx;
	}

	public int getType() {
		if (type == TYPE_UNKNOWN) {
			calculateType();
		}
		return type;
	}

	private void calculateType() {
	}

	public double getDeterminant() {
		return 1.0;
	}

	void updateState() {
	}

	private void stateError() {
		throw new InternalError("missing case in transform state switch");
	}

	public void getMatrix(double[] flatmatrix) {
		flatmatrix[0] = m00;
		flatmatrix[1] = m10;
		flatmatrix[2] = m01;
		flatmatrix[3] = m11;
		if (flatmatrix.length > 5) {
			flatmatrix[4] = m02;
			flatmatrix[5] = m12;
		}
	}

	public double getScaleX() {
		return m00;
	}

	public double getScaleY() {
		return m11;
	}

	public double getShearX() {
		return m01;
	}

	public double getShearY() {
		return m10;
	}

	public double getTranslateX() {
		return m02;
	}

	public double getTranslateY() {
		return m12;
	}

	public void translate(double tx, double ty) {
	}

	private final void rotate90() {
	}

	private final void rotate180() {
	}

	private final void rotate270() {
	}

	public void rotate(double theta) {
	}

	public void rotate(double theta, double anchorx, double anchory) {
	}

	public void rotate(double vecx, double vecy) {
	}

	public void rotate(double vecx, double vecy, double anchorx, double anchory) {
	}

	public void quadrantRotate(int numquadrants) {
	}

	public void quadrantRotate(int numquadrants, double anchorx, double anchory) {
	}

	public void scale(double sx, double sy) {
	}

	public void shear(double shx, double shy) {
	}

	public void setToIdentity() {
	}

	public void setToTranslation(double tx, double ty) {
	}

	public void setToRotation(double theta) {
	}

	public void setToRotation(double theta, double anchorx, double anchory) {
	}

	public void setToRotation(double vecx, double vecy) {
	}

	public void setToRotation(double vecx, double vecy, double anchorx, double anchory) {
	}

	public void setToQuadrantRotation(int numquadrants) {
	}

	public void setToQuadrantRotation(int numquadrants, double anchorx, double anchory) {
	}

	public void setToScale(double sx, double sy) {
	}

	public void setToShear(double shx, double shy) {
	}

	public void setTransform(AffineTransform Tx) {
	}

	public void setTransform(double m00, double m10, double m01, double m11, double m02,
			double m12) {
	}

	public void concatenate(AffineTransform Tx) {
	}

	public void preConcatenate(AffineTransform Tx) {
	}

	public AffineTransform createInverse() throws NoninvertibleTransformException {
		return null;
	}

	public void invert() throws NoninvertibleTransformException {
	}

	public Point2D transform(Point2D ptSrc, Point2D ptDst) {
		return null;
	}

	public void transform(Point2D[] ptSrc, int srcOff, Point2D[] ptDst, int dstOff, int numPts) {
	}

	public void transform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) {
		return;
	}

	public void transform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
		return;
	}

	public void transform(float[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
		return;
	}

	public void transform(double[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) {
		return;
	}

	public Point2D inverseTransform(Point2D ptSrc, Point2D ptDst)
			throws NoninvertibleTransformException {
		return null;
	}

	public void inverseTransform(double[] srcPts, int srcOff, double[] dstPts, int dstOff,
			int numPts) throws NoninvertibleTransformException {
		return;
	}

	public Point2D deltaTransform(Point2D ptSrc, Point2D ptDst) {
		return null;
	}

	public void deltaTransform(double[] srcPts, int srcOff, double[] dstPts, int dstOff,
			int numPts) {
		return;
	}

	public Shape createTransformedShape(Shape pSrc) {
		return null;
	}

	private static double _matround(double matval) {
		return Math.rint(matval * 1E15) / 1E15;
	}

	public String toString() {
		return ("AffineTransform[[" + _matround(m00) + ", " + _matround(m01) + ", " + _matround(m02)
				+ "], [" + _matround(m10) + ", " + _matround(m11) + ", " + _matround(m12) + "]]");
	}

	public boolean isIdentity() {
		return (state == APPLY_IDENTITY || (getType() == TYPE_IDENTITY));
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	public int hashCode() {
		long bits = Double.doubleToLongBits(m00);
		bits = bits * 31 + Double.doubleToLongBits(m01);
		bits = bits * 31 + Double.doubleToLongBits(m02);
		bits = bits * 31 + Double.doubleToLongBits(m10);
		bits = bits * 31 + Double.doubleToLongBits(m11);
		bits = bits * 31 + Double.doubleToLongBits(m12);
		return (((int) bits) ^ ((int) (bits >> 32)));
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof AffineTransform)) {
			return false;
		}

		AffineTransform a = (AffineTransform) obj;

		return ((m00 == a.m00) && (m01 == a.m01) && (m02 == a.m02) && (m10 == a.m10)
				&& (m11 == a.m11) && (m12 == a.m12));
	}

	private void writeObject(java.io.ObjectOutputStream s)
			throws java.lang.ClassNotFoundException, java.io.IOException {
	}

	private void readObject(java.io.ObjectInputStream s)
			throws java.lang.ClassNotFoundException, java.io.IOException {
	}
}