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
package java.awt.image;

import java.awt.Transparency;
import java.awt.color.ColorSpace;

/**
 * The <code>DirectColorModel</code> class is a <code>ColorModel</code> class
 * that works with pixel values that represent RGB color and alpha information
 * as separate samples and that pack all samples for a single pixel into a
 * single int, short, or byte quantity. This class can be used only with
 * ColorSpaces of type ColorSpace.TYPE_RGB. In addition, for each component of
 * the ColorSpace, the minimum normalized component value obtained via the
 * <code>getMinValue()</code> method of ColorSpace must be 0.0, and the maximum
 * value obtained via the <code>getMaxValue()</code> method must be 1.0 (these
 * min/max values are typical for RGB spaces). There must be three color samples
 * in the pixel values and there can be a single alpha sample. For those methods
 * that use a primitive array pixel representation of type
 * <code>transferType</code>, the array length is always one. The transfer types
 * supported are DataBuffer.TYPE_BYTE, DataBuffer.TYPE_USHORT, and
 * DataBuffer.TYPE_INT. Color and alpha samples are stored in the single element
 * of the array in bits indicated by bit masks. Each bit mask must be contiguous
 * and masks must not overlap. The same masks apply to the single int pixel
 * representation used by other methods. The correspondence of masks and
 * color/alpha samples is as follows:
 * <ul>
 * <li>Masks are identified by indices running from 0 through 2 if no alpha is
 * present, or 3 if an alpha is present.
 * <li>The first three indices refer to color samples; index 0 corresponds to
 * red, index 1 to green, and index 2 to blue.
 * <li>Index 3 corresponds to the alpha sample, if present.
 * </ul>
 * <p>
 * The translation from pixel values to color/alpha components for display or
 * processing purposes is a one-to-one correspondence of samples to components.
 * A <code>DirectColorModel</code> is typically used with image data which uses
 * masks to define packed samples. For example, a <code>DirectColorModel</code>
 * can be used in conjunction with a <code>SinglePixelPackedSampleModel</code>
 * to construct a {@link BufferedImage}. Normally the masks used by the
 * {@link SampleModel} and the <code>ColorModel</code> would be the same.
 * However, if they are different, the color interpretation of pixel data will
 * be done according to the masks of the <code>ColorModel</code>.
 * <p>
 * A single int pixel representation is valid for all objects of this class,
 * since it is always possible to represent pixel values used with this class in
 * a single int. Therefore, methods which use this representation will not throw
 * an <code>IllegalArgumentException</code> due to an invalid pixel value.
 * <p>
 * This color model is similar to an X11 TrueColor visual. The default RGB
 * ColorModel specified by the {@link ColorModel#getRGBdefault() getRGBdefault}
 * method is a <code>DirectColorModel</code> with the following parameters:
 * 
 * <pre>
 * Number of bits:        32
 * Red mask:              0x00ff0000
 * Green mask:            0x0000ff00
 * Blue mask:             0x000000ff
 * Alpha mask:            0xff000000
 * Color space:           sRGB
 * isAlphaPremultiplied:  False
 * Transparency:          Transparency.TRANSLUCENT
 * transferType:          DataBuffer.TYPE_INT
 * </pre>
 * <p>
 * Many of the methods in this class are final. This is because the underlying
 * native graphics code makes assumptions about the layout and operation of this
 * class and those assumptions are reflected in the implementations of the
 * methods here that are marked final. You can subclass this class for other
 * reasons, but you cannot override or modify the behavior of those methods.
 *
 * @see ColorModel
 * @see ColorSpace
 * @see SinglePixelPackedSampleModel
 * @see BufferedImage
 * @see ColorModel#getRGBdefault
 *
 */
public class DirectColorModel extends PackedColorModel {
	private int red_mask;
	private int green_mask;
	private int blue_mask;
	private int alpha_mask;
	private boolean is_LinearRGB;

	public DirectColorModel(int bits, int rmask, int gmask, int bmask) {
		this(bits, rmask, gmask, bmask, 0);
	}

	public DirectColorModel(int bits, int rmask, int gmask, int bmask, int amask) {
		super(ColorSpace.getInstance(ColorSpace.CS_sRGB), bits, rmask, gmask, bmask, amask, false,
				amask == 0 ? Transparency.OPAQUE : Transparency.TRANSLUCENT,
				ColorModel.getDefaultTransferType(bits));
		setFields();
	}

	public DirectColorModel(ColorSpace space, int bits, int rmask, int gmask, int bmask, int amask,
			boolean isAlphaPremultiplied, int transferType) {
		super(space, bits, rmask, gmask, bmask, amask, isAlphaPremultiplied,
				amask == 0 ? Transparency.OPAQUE : Transparency.TRANSLUCENT, transferType);
		setFields();
	}

	final public int getRedMask() {
		return maskArray[0];
	}

	final public int getGreenMask() {
		return maskArray[1];
	}

	final public int getBlueMask() {
		return maskArray[2];
	}

	final public int getAlphaMask() {
		if (supportsAlpha) {
			return maskArray[3];
		} else {
			return 0;
		}
	}

	private float[] getDefaultRGBComponents(int pixel) {
		return new float[0];
	}

	private int getsRGBComponentFromsRGB(int pixel, int idx) {
		return 0;
	}

	private int getsRGBComponentFromLinearRGB(int pixel, int idx) {
		return 0;
	}

	final public int getRed(int pixel) {
		if (is_sRGB) {
			return getsRGBComponentFromsRGB(pixel, 0);
		} else if (is_LinearRGB) {
			return getsRGBComponentFromLinearRGB(pixel, 0);
		}
		float rgb[] = getDefaultRGBComponents(pixel);
		return (int) (rgb[0] * 255.0f + 0.5f);
	}

	final public int getGreen(int pixel) {
		if (is_sRGB) {
			return getsRGBComponentFromsRGB(pixel, 1);
		} else if (is_LinearRGB) {
			return getsRGBComponentFromLinearRGB(pixel, 1);
		}
		float rgb[] = getDefaultRGBComponents(pixel);
		return (int) (rgb[1] * 255.0f + 0.5f);
	}

	final public int getBlue(int pixel) {
		if (is_sRGB) {
			return getsRGBComponentFromsRGB(pixel, 2);
		} else if (is_LinearRGB) {
			return getsRGBComponentFromLinearRGB(pixel, 2);
		}
		float rgb[] = getDefaultRGBComponents(pixel);
		return (int) (rgb[2] * 255.0f + 0.5f);
	}

	final public int getAlpha(int pixel) {
		if (!supportsAlpha)
			return 255;
		int a = ((pixel & maskArray[3]) >>> maskOffsets[3]);
		if (scaleFactors[3] != 1.0f) {
			a = (int) (a * scaleFactors[3] + 0.5f);
		}
		return a;
	}

	final public int getRGB(int pixel) {
		if (is_sRGB || is_LinearRGB) {
			return (getAlpha(pixel) << 24) | (getRed(pixel) << 16) | (getGreen(pixel) << 8)
					| (getBlue(pixel) << 0);
		}
		float rgb[] = getDefaultRGBComponents(pixel);
		return (getAlpha(pixel) << 24) | (((int) (rgb[0] * 255.0f + 0.5f)) << 16)
				| (((int) (rgb[1] * 255.0f + 0.5f)) << 8) | (((int) (rgb[2] * 255.0f + 0.5f)) << 0);
	}

	public int getRed(Object inData) {
		int pixel = 0;
		switch (transferType) {
		case DataBuffer.TYPE_BYTE:
			byte bdata[] = (byte[]) inData;
			pixel = bdata[0] & 0xff;
			break;
		case DataBuffer.TYPE_USHORT:
			short sdata[] = (short[]) inData;
			pixel = sdata[0] & 0xffff;
			break;
		case DataBuffer.TYPE_INT:
			int idata[] = (int[]) inData;
			pixel = idata[0];
			break;
		default:
			throw new UnsupportedOperationException("unknow transferType " + transferType);
		}
		return getRed(pixel);
	}

	public int getGreen(Object inData) {
		int pixel = 0;
		switch (transferType) {
		case DataBuffer.TYPE_BYTE:
			byte bdata[] = (byte[]) inData;
			pixel = bdata[0] & 0xff;
			break;
		case DataBuffer.TYPE_USHORT:
			short sdata[] = (short[]) inData;
			pixel = sdata[0] & 0xffff;
			break;
		case DataBuffer.TYPE_INT:
			int idata[] = (int[]) inData;
			pixel = idata[0];
			break;
		default:
			throw new UnsupportedOperationException("unknow transferType " + transferType);
		}
		return getGreen(pixel);
	}

	public int getBlue(Object inData) {
		int pixel = 0;
		switch (transferType) {
		case DataBuffer.TYPE_BYTE:
			byte bdata[] = (byte[]) inData;
			pixel = bdata[0] & 0xff;
			break;
		case DataBuffer.TYPE_USHORT:
			short sdata[] = (short[]) inData;
			pixel = sdata[0] & 0xffff;
			break;
		case DataBuffer.TYPE_INT:
			int idata[] = (int[]) inData;
			pixel = idata[0];
			break;
		default:
			throw new UnsupportedOperationException("unknow transferType " + transferType);
		}
		return getBlue(pixel);
	}

	public int getAlpha(Object inData) {
		int pixel = 0;
		switch (transferType) {
		case DataBuffer.TYPE_BYTE:
			byte bdata[] = (byte[]) inData;
			pixel = bdata[0] & 0xff;
			break;
		case DataBuffer.TYPE_USHORT:
			short sdata[] = (short[]) inData;
			pixel = sdata[0] & 0xffff;
			break;
		case DataBuffer.TYPE_INT:
			int idata[] = (int[]) inData;
			pixel = idata[0];
			break;
		default:
			throw new UnsupportedOperationException("unknow transferType " + transferType);
		}
		return getAlpha(pixel);
	}

	public int getRGB(Object inData) {
		int pixel = 0;
		switch (transferType) {
		case DataBuffer.TYPE_BYTE:
			byte bdata[] = (byte[]) inData;
			pixel = bdata[0] & 0xff;
			break;
		case DataBuffer.TYPE_USHORT:
			short sdata[] = (short[]) inData;
			pixel = sdata[0] & 0xffff;
			break;
		case DataBuffer.TYPE_INT:
			int idata[] = (int[]) inData;
			pixel = idata[0];
			break;
		default:
			throw new UnsupportedOperationException("unknow transferType " + transferType);
		}
		return getRGB(pixel);
	}

	public Object getDataElements(int rgb, Object pixel) {
		int intpixel[] = null;
		ColorModel defaultCM = ColorModel.getRGBdefault();
		if (this == defaultCM || equals(defaultCM)) {
			intpixel[0] = rgb;
			return intpixel;
		}

		switch (transferType) {
		case DataBuffer.TYPE_INT:
			return intpixel;
		}
		throw new UnsupportedOperationException("unknow transferType " + transferType);
	}

	final public int[] getComponents(int pixel, int[] components, int offset) {
		if (components == null) {
			components = new int[offset + numComponents];
		}

		for (int i = 0; i < numComponents; i++) {
			components[offset + i] = (pixel & maskArray[i]) >>> maskOffsets[i];
		}

		return components;
	}

	final public int[] getComponents(Object pixel, int[] components, int offset) {
		int intpixel = 0;
		switch (transferType) {
		case DataBuffer.TYPE_INT:
			int idata[] = (int[]) pixel;
			intpixel = idata[0];
			break;
		default:
			throw new UnsupportedOperationException("unknow transferType " + transferType);
		}
		return getComponents(intpixel, components, offset);
	}

	final public WritableRaster createCompatibleWritableRaster(int w, int h) {
		if ((w <= 0) || (h <= 0)) {
			throw new IllegalArgumentException(
					"Width (" + w + ") and height (" + h + ") cannot be <= 0");
		}
		int[] bandmasks;
		if (supportsAlpha) {
			bandmasks = new int[4];
			bandmasks[3] = alpha_mask;
		} else {
			bandmasks = new int[3];
		}
		bandmasks[0] = red_mask;
		bandmasks[1] = green_mask;
		bandmasks[2] = blue_mask;

		if (pixel_bits > 16) {
			return Raster.createPackedRaster(DataBuffer.TYPE_INT, w, h, bandmasks, null);
		} else {
			return null;
		}
	}

	public int getDataElement(int[] components, int offset) {
		int pixel = 0;
		for (int i = 0; i < numComponents; i++) {
			pixel |= ((components[offset + i] << maskOffsets[i]) & maskArray[i]);
		}
		return pixel;
	}

	public Object getDataElements(int[] components, int offset, Object obj) {
		int pixel = 0;
		for (int i = 0; i < numComponents; i++) {
			pixel |= ((components[offset + i] << maskOffsets[i]) & maskArray[i]);
		}
		switch (transferType) {
		case DataBuffer.TYPE_INT:
			if (obj instanceof int[]) {
				int idata[] = (int[]) obj;
				idata[0] = pixel;
				return idata;
			} else {
				int idata[] = { pixel };
				return idata;
			}
		default:
			throw new ClassCastException("unknow transferType " + transferType);
		}
	}

	final public ColorModel coerceData(WritableRaster raster, boolean isAlphaPremultiplied) {
		return null;
	}

	public boolean isCompatibleRaster(Raster raster) {
		return false;
	}

	private void setFields() {
	}

	public String toString() {
		return new String("DirectColorModel: rmask=" + Integer.toHexString(red_mask) + " gmask="
				+ Integer.toHexString(green_mask) + " bmask=" + Integer.toHexString(blue_mask)
				+ " amask=" + Integer.toHexString(alpha_mask));
	}
}