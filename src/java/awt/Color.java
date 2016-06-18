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
 * The <code>Color</code> class is used to encapsulate colors in the default
 * sRGB color space or colors in arbitrary color spaces identified by a
 * {@link ColorSpace}.  Every color has an implicit alpha value of 1.0 or
 * an explicit one provided in the constructor.  The alpha value
 * defines the transparency of a color and can be represented by
 * a float value in the range 0.0&nbsp;-&nbsp;1.0 or 0&nbsp;-&nbsp;255.
 * An alpha value of 1.0 or 255 means that the color is completely
 * opaque and an alpha value of 0 or 0.0 means that the color is
 * completely transparent.
 * When constructing a <code>Color</code> with an explicit alpha or
 * getting the color/alpha components of a <code>Color</code>, the color
 * components are never premultiplied by the alpha component.
 * <p>
 * The default color space for the Java 2D(tm) API is sRGB, a proposed
 * standard RGB color space.  For further information on sRGB,
 * see <A href="http://www.w3.org/pub/WWW/Graphics/Color/sRGB.html">
 * http://www.w3.org/pub/WWW/Graphics/Color/sRGB.html
 * </A>.
 * <p>
 * @version     10 Feb 1997
 * @author      Sami Shaio
 * @author      Arthur van Hoff
 * @see         ColorSpace
 * @see         AlphaComposite
 */
public class Color {
	public final int toAndroid() {
		return value;
	}

	public final static Color white = new Color(255, 255, 255);

	public final static Color WHITE = white;

	public final static Color lightGray = new Color(192, 192, 192);

	public final static Color LIGHT_GRAY = lightGray;

	public final static Color gray = new Color(128, 128, 128);

	public final static Color GRAY = gray;

	public final static Color darkGray = new Color(64, 64, 64);

	public final static Color DARK_GRAY = darkGray;

	public final static Color black = new Color(0, 0, 0);

	public final static Color BLACK = black;

	public final static Color red = new Color(255, 0, 0);

	public final static Color RED = red;

	public final static Color pink = new Color(255, 175, 175);

	public final static Color PINK = pink;

	public final static Color orange = new Color(255, 200, 0);

	public final static Color ORANGE = orange;

	public final static Color yellow = new Color(255, 255, 0);

	public final static Color YELLOW = yellow;

	public final static Color green = new Color(0, 255, 0);

	public final static Color GREEN = green;

	public final static Color magenta = new Color(255, 0, 255);

	public final static Color MAGENTA = magenta;

	public final static Color cyan = new Color(0, 255, 255);

	public final static Color CYAN = cyan;

	public final static Color blue = new Color(0, 0, 255);

	public final static Color BLUE = blue;

	int value;

	public Color(int r, int g, int b) {
		this(r, g, b, 255);
	}

	public Color(int r, int g, int b, int a) {
		value = ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8)
				| ((b & 0xFF) << 0);
	}

	public Color(int rgb) {
		value = 0xff000000 | rgb;
	}

	public Color(int rgba, boolean hasalpha) {
		if (hasalpha) {
			value = rgba;
		} else {
			value = 0xff000000 | rgba;
		}
	}

	public Color(float r, float g, float b) {
		this((int) (r * 255 + 0.5), (int) (g * 255 + 0.5),
				(int) (b * 255 + 0.5));
	}

	public Color(float r, float g, float b, float a) {
		this((int) (r * 255 + 0.5), (int) (g * 255 + 0.5),
				(int) (b * 255 + 0.5), (int) (a * 255 + 0.5));
	}

	public int getRed() {
		return (getRGB() >> 16) & 0xFF;
	}

	public int getGreen() {
		return (getRGB() >> 8) & 0xFF;
	}

	public int getBlue() {
		return (getRGB() >> 0) & 0xFF;
	}

	public int getAlpha() {
		return (getRGB() >> 24) & 0xff;
	}

	public int getRGB() {
		return value;
	}

	public int hashCode() {
		return value;
	}

	public boolean equals(Object obj) {
		return obj instanceof Color && ((Color) obj).getRGB() == this.getRGB();
	}

	public String toString() {
		return getClass().getName() + "[r=" + getRed() + ",g=" + getGreen()
				+ ",b=" + getBlue() + "]";
	}

	public static Color decode(String nm) throws NumberFormatException {
		Integer intval = Integer.decode(nm);
		int i = intval.intValue();
		return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
	}

	public static Color getColor(String nm) {
		return getColor(nm, null);
	}

	public static Color getColor(String nm, Color v) {
		Integer intval = Integer.getInteger(nm);
		if (intval == null) {
			return v;
		}
		int i = intval.intValue();
		return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
	}

	public static Color getColor(String nm, int v) {
		Integer intval = Integer.getInteger(nm);
		int i = (intval != null) ? intval.intValue() : v;
		return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, (i >> 0) & 0xFF);
	}

	private static final double FACTOR = 0.7;

	public Color brighter() {
		int r = getRed();
		int g = getGreen();
		int b = getBlue();
		int alpha = getAlpha();

		int i = (int) (1.0 / (1.0 - FACTOR));
		if (r == 0 && g == 0 && b == 0) {
			return new Color(i, i, i, alpha);
		}
		if (r > 0 && r < i)
			r = i;
		if (g > 0 && g < i)
			g = i;
		if (b > 0 && b < i)
			b = i;

		return new Color(Math.min((int) (r / FACTOR), 255), Math.min(
				(int) (g / FACTOR), 255), Math.min((int) (b / FACTOR), 255),
				alpha);
	}

	public Color darker() {
		return new Color(Math.max((int) (getRed() * FACTOR), 0), Math.max(
				(int) (getGreen() * FACTOR), 0), Math.max(
				(int) (getBlue() * FACTOR), 0), getAlpha());
	}

}
