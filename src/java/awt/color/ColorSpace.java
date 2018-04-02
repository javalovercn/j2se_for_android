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

/**********************************************************************
 **********************************************************************
 **********************************************************************
 *** COPYRIGHT (c) Eastman Kodak Company, 1997                      ***
 *** As  an unpublished  work pursuant to Title 17 of the United    ***
 *** States Code.  All rights reserved.                             ***
 **********************************************************************
 **********************************************************************
 **********************************************************************/

package java.awt.color;

/**
 * This abstract class is used to serve as a color space tag to identify the
 * specific color space of a Color object or, via a ColorModel object, of an
 * Image, a BufferedImage, or a GraphicsDevice. It contains methods that
 * transform colors in a specific color space to/from sRGB and to/from a
 * well-defined CIEXYZ color space.
 * <p>
 * For purposes of the methods in this class, colors are represented as arrays
 * of color components represented as floats in a normalized range defined by
 * each ColorSpace. For many ColorSpaces (e.g. sRGB), this range is 0.0 to 1.0.
 * However, some ColorSpaces have components whose values have a different
 * range. Methods are provided to inquire per component minimum and maximum
 * normalized values.
 * <p>
 * Several variables are defined for purposes of referring to color space types
 * (e.g. TYPE_RGB, TYPE_XYZ, etc.) and to refer to specific color spaces (e.g.
 * CS_sRGB and CS_CIEXYZ). sRGB is a proposed standard RGB color space. For more
 * information, see
 * <A href="http://www.w3.org/pub/WWW/Graphics/Color/sRGB.html">
 * http://www.w3.org/pub/WWW/Graphics/Color/sRGB.html </A>.
 * <p>
 * The purpose of the methods to transform to/from the well-defined CIEXYZ color
 * space is to support conversions between any two color spaces at a reasonably
 * high degree of accuracy. It is expected that particular implementations of
 * subclasses of ColorSpace (e.g. ICC_ColorSpace) will support high performance
 * conversion based on underlying platform color management systems.
 * <p>
 * The CS_CIEXYZ space used by the toCIEXYZ/fromCIEXYZ methods can be described
 * as follows:
 * 
 * <pre>

&nbsp;     CIEXYZ
&nbsp;     viewing illuminance: 200 lux
&nbsp;     viewing white point: CIE D50
&nbsp;     media white point: "that of a perfectly reflecting diffuser" -- D50
&nbsp;     media black point: 0 lux or 0 Reflectance
&nbsp;     flare: 1 percent
&nbsp;     surround: 20percent of the media white point
&nbsp;     media description: reflection print (i.e., RLAB, Hunt viewing media)
&nbsp;     note: For developers creating an ICC profile for this conversion
&nbsp;           space, the following is applicable.  Use a simple Von Kries
&nbsp;           white point adaptation folded into the 3X3 matrix parameters
&nbsp;           and fold the flare and surround effects into the three
&nbsp;           one-dimensional lookup tables (assuming one uses the minimal
&nbsp;           model for monitors).
 * 
 * </pre>
 *
 * <p>
 * 
 * @see ICC_ColorSpace
 */
public abstract class ColorSpace implements java.io.Serializable {
	private int type;
	private int numComponents;
	private transient String[] compName = null;

	public static final int TYPE_RGB = 5;
	public static final int TYPE_GRAY = 6;
	public static final int TYPE_HSV = 7;

	public static final int CS_sRGB = 1000;
	public static final int CS_GRAY = 1003;

	protected ColorSpace(int type, int numcomponents) {
		this.type = type;
		this.numComponents = numcomponents;
	}

	public static ColorSpace getInstance(int colorspace) {
		return null;
	}

	public abstract float[] toRGB(float[] colorvalue);

	public abstract float[] fromRGB(float[] rgbvalue);

	public abstract float[] toCIEXYZ(float[] colorvalue);

	public abstract float[] fromCIEXYZ(float[] colorvalue);

	public int getType() {
		return type;
	}

	public int getNumComponents() {
		return numComponents;
	}

	public String getName(int idx) {
		if ((idx < 0) || (idx > numComponents - 1)) {
			throw new IllegalArgumentException("Component index out of range: " + idx);
		}

		if (compName == null) {
			switch (type) {
			case ColorSpace.TYPE_RGB:
				compName = new String[] { "Red", "Green", "Blue" };
				break;
			case ColorSpace.TYPE_GRAY:
				compName = new String[] { "Gray" };
				break;
			case ColorSpace.TYPE_HSV:
				compName = new String[] { "Hue", "Saturation", "Value" };
				break;
			default:
				String[] tmp = new String[numComponents];
				for (int i = 0; i < tmp.length; i++) {
					tmp[i] = "Unknow(" + i + ")";
				}
				compName = tmp;
			}
		}
		return compName[idx];
	}

	public float getMinValue(int component) {
		if ((component < 0) || (component > numComponents - 1)) {
			throw new IllegalArgumentException("out of " + component);
		}
		return 0.0f;
	}

	public float getMaxValue(int component) {
		if ((component < 0) || (component > numComponents - 1)) {
			throw new IllegalArgumentException("out of " + component);
		}
		return 1.0f;
	}
}