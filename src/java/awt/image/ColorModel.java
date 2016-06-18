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
 * The <code>ColorModel</code> abstract class encapsulates the
 * methods for translating a pixel value to color components
 * (for example, red, green, and blue) and an alpha component.
 * In order to render an image to the screen, a printer, or another
 * image, pixel values must be converted to color and alpha components.
 * As arguments to or return values from methods of this class,
 * pixels are represented as 32-bit ints or as arrays of primitive types.
 * The number, order, and interpretation of color components for a
 * <code>ColorModel</code> is specified by its <code>ColorSpace</code>.
 * A <code>ColorModel</code> used with pixel data that does not include
 * alpha information treats all pixels as opaque, which is an alpha
 * value of 1.0.
 * <p>
 * This <code>ColorModel</code> class supports two representations of
 * pixel values.  A pixel value can be a single 32-bit int or an
 * array of primitive types.  The Java(tm) Platform 1.0 and 1.1 APIs
 * represented pixels as single <code>byte</code> or single
 * <code>int</code> values.  For purposes of the <code>ColorModel</code>
 * class, pixel value arguments were passed as ints.  The Java(tm) 2
 * Platform API introduced additional classes for representing images.
 * With {@link BufferedImage} or {@link RenderedImage}
 * objects, based on {@link Raster} and {@link SampleModel} classes, pixel
 * values might not be conveniently representable as a single int.
 * Consequently, <code>ColorModel</code> now has methods that accept
 * pixel values represented as arrays of primitive types.  The primitive
 * type used by a particular <code>ColorModel</code> object is called its
 * transfer type.
 * <p>
 * <code>ColorModel</code> objects used with images for which pixel values
 * are not conveniently representable as a single int throw an
 * {@link IllegalArgumentException} when methods taking a single int pixel
 * argument are called.  Subclasses of <code>ColorModel</code> must
 * specify the conditions under which this occurs.  This does not
 * occur with {@link DirectColorModel} or {@link IndexColorModel} objects.
 * <p>
 * Currently, the transfer types supported by the Java 2D(tm) API are
 * DataBuffer.TYPE_BYTE, DataBuffer.TYPE_USHORT, DataBuffer.TYPE_INT,
 * DataBuffer.TYPE_SHORT, DataBuffer.TYPE_FLOAT, and DataBuffer.TYPE_DOUBLE.
 * Most rendering operations will perform much faster when using ColorModels
 * and images based on the first three of these types.  In addition, some
 * image filtering operations are not supported for ColorModels and
 * images based on the latter three types.
 * The transfer type for a particular <code>ColorModel</code> object is
 * specified when the object is created, either explicitly or by default.
 * All subclasses of <code>ColorModel</code> must specify what the
 * possible transfer types are and how the number of elements in the
 * primitive arrays representing pixels is determined.
 * <p>
 * For <code>BufferedImages</code>, the transfer type of its
 * <code>Raster</code> and of the <code>Raster</code> object's
 * <code>SampleModel</code> (available from the
 * <code>getTransferType</code> methods of these classes) must match that
 * of the <code>ColorModel</code>.  The number of elements in an array
 * representing a pixel for the <code>Raster</code> and
 * <code>SampleModel</code> (available from the
 * <code>getNumDataElements</code> methods of these classes) must match
 * that of the <code>ColorModel</code>.
 * <p>
 * The algorithm used to convert from pixel values to color and alpha
 * components varies by subclass.  For example, there is not necessarily
 * a one-to-one correspondence between samples obtained from the
 * <code>SampleModel</code> of a <code>BufferedImage</code> object's
 * <code>Raster</code> and color/alpha components.  Even when
 * there is such a correspondence, the number of bits in a sample is not
 * necessarily the same as the number of bits in the corresponding color/alpha
 * component.  Each subclass must specify how the translation from
 * pixel values to color/alpha components is done.
 * <p>
 * Methods in the <code>ColorModel</code> class use two different
 * representations of color and alpha components - a normalized form
 * and an unnormalized form.  In the normalized form, each component is a
 * <code>float</code> value between some minimum and maximum values.  For
 * the alpha component, the minimum is 0.0 and the maximum is 1.0.  For
 * color components the minimum and maximum values for each component can
 * be obtained from the <code>ColorSpace</code> object.  These values
 * will often be 0.0 and 1.0 (e.g. normalized component values for the
 * default sRGB color space range from 0.0 to 1.0), but some color spaces
 * have component values with different upper and lower limits.  These
 * limits can be obtained using the <code>getMinValue</code> and
 * <code>getMaxValue</code> methods of the <code>ColorSpace</code>
 * class.  Normalized color component values are not premultiplied.
 * All <code>ColorModels</code> must support the normalized form.
 * <p>
 * In the unnormalized
 * form, each component is an unsigned integral value between 0 and
 * 2<sup>n</sup> - 1, where n is the number of significant bits for a
 * particular component.  If pixel values for a particular
 * <code>ColorModel</code> represent color samples premultiplied by
 * the alpha sample, unnormalized color component values are
 * also premultiplied.  The unnormalized form is used only with instances
 * of <code>ColorModel</code> whose <code>ColorSpace</code> has minimum
 * component values of 0.0 for all components and maximum values of
 * 1.0 for all components.
 * The unnormalized form for color and alpha components can be a convenient
 * representation for <code>ColorModels</code> whose normalized component
 * values all lie
 * between 0.0 and 1.0.  In such cases the integral value 0 maps to 0.0 and
 * the value 2<sup>n</sup> - 1 maps to 1.0.  In other cases, such as
 * when the normalized component values can be either negative or positive,
 * the unnormalized form is not convenient.  Such <code>ColorModel</code>
 * objects throw an {@link IllegalArgumentException} when methods involving
 * an unnormalized argument are called.  Subclasses of <code>ColorModel</code>
 * must specify the conditions under which this occurs.
 *
 * @see IndexColorModel
 * @see ComponentColorModel
 * @see PackedColorModel
 * @see DirectColorModel
 * @see java.awt.Image
 * @see BufferedImage
 * @see RenderedImage
 * @see java.awt.color.ColorSpace
 * @see SampleModel
 * @see Raster
 * @see DataBuffer
 */
public abstract class ColorModel implements Transparency{
    private long pData; 

    protected int pixel_bits;
    int nBits[];
    int transparency = Transparency.TRANSLUCENT;
    boolean supportsAlpha = true;
    boolean isAlphaPremultiplied = false;
    int numComponents = -1;
    int numColorComponents = -1;
    int maxBits;
    boolean is_sRGB = true;
    protected int transferType;
    private static ColorModel RGBdefault;

    public static ColorModel getRGBdefault() {
        if (RGBdefault == null) {
            RGBdefault = new DirectColorModel(32,
                                              0x00ff0000, 
                                              0x0000ff00, 
                                              0x000000ff, 
                                              0xff000000   
                                              );
        }
        return RGBdefault;
    }

    static int getDefaultTransferType(int pixel_bits) {
        if (pixel_bits <= 8) {
            return DataBuffer.TYPE_BYTE;
        } else if (pixel_bits <= 16) {
            return DataBuffer.TYPE_USHORT;
        } else if (pixel_bits <= 32) {
            return DataBuffer.TYPE_INT;
        } else {
            return DataBuffer.TYPE_UNDEFINED;
        }
    }
    
    public ColorModel(int bits) {
        pixel_bits = bits;
        if (bits < 1) {
            throw new IllegalArgumentException("Number of bits must be > 0");
        }
        numComponents = 4;
        numColorComponents = 3;
        maxBits = bits;
    }
    
    protected ColorModel(int pixel_bits, int[] bits, ColorSpace cspace,
            boolean hasAlpha,
            boolean isAlphaPremultiplied,
            int transparency,
            int transferType) {
    }

    public WritableRaster createCompatibleWritableRaster(int w, int h) {
        throw new UnsupportedOperationException
            ("This method is not supported by this color model");
    }
    
    /**
     * Creates a <code>SampleModel</code> with the specified width and
     * height that has a data layout compatible with this
     * <code>ColorModel</code>.
     * Since <code>ColorModel</code> is an abstract class,
     * any instance is an instance of a subclass.  Subclasses must
     * override this method since the implementation in this abstract
     * class throws an <code>UnsupportedOperationException</code>.
     * @param w the width to apply to the new <code>SampleModel</code>
     * @param h the height to apply to the new <code>SampleModel</code>
     * @return a <code>SampleModel</code> object with the specified
     * width and height.
     * @throws UnsupportedOperationException if this
     *          method is not supported by this <code>ColorModel</code>
     * @see SampleModel
     */
    public SampleModel createCompatibleSampleModel(int w, int h) {
        throw new UnsupportedOperationException
            ("This method is not supported by this color model");
    }
    
    /** Checks if the <code>SampleModel</code> is compatible with this
     * <code>ColorModel</code>.
     * Since <code>ColorModel</code> is an abstract class,
     * any instance is an instance of a subclass.  Subclasses must
     * override this method since the implementation in this abstract
     * class throws an <code>UnsupportedOperationException</code>.
     * @param sm the specified <code>SampleModel</code>
     * @return <code>true</code> if the specified <code>SampleModel</code>
     * is compatible with this <code>ColorModel</code>; <code>false</code>
     * otherwise.
     * @throws UnsupportedOperationException if this
     *          method is not supported by this <code>ColorModel</code>
     * @see SampleModel
     */
    public boolean isCompatibleSampleModel(SampleModel sm) {
        throw new UnsupportedOperationException
            ("This method is not supported by this color model");
    }
    
    final public boolean hasAlpha() {
        return supportsAlpha;
    }

    final public boolean isAlphaPremultiplied() {
        return isAlphaPremultiplied;
    }

    final public int getTransferType() {
        return transferType;
    }

    public int getPixelSize() {
        return pixel_bits;
    }

    public int getComponentSize(int componentIdx) {
        if (nBits == null) {
            throw new NullPointerException("Number of bits array is null.");
        }

        return nBits[componentIdx];
    }

    public int[] getComponentSize() {
        if (nBits != null) {
            return (int[]) nBits.clone();
        }

        return null;
    }

    public int getTransparency() {
        return transparency;
    }

    public int getNumComponents() {
        return numComponents;
    }

    public int getNumColorComponents() {
        return numColorComponents;
    }

    public abstract int getRed(int pixel);

    public abstract int getGreen(int pixel);

    public abstract int getBlue(int pixel);

    public abstract int getAlpha(int pixel);

    public int getRGB(int pixel) {
        return (getAlpha(pixel) << 24)
            | (getRed(pixel) << 16)
            | (getGreen(pixel) << 8)
            | (getBlue(pixel) << 0);
    }

    public int getRed(Object inData) {
        int pixel=0,length=0;
               byte bdata[] = (byte[])inData;
               pixel = bdata[0] & 0xff;
               length = bdata.length;
        if (length == 1) {
            return getRed(pixel);
        }
        else {
            throw new UnsupportedOperationException
                ("This method is not supported by this color model");
        }
    }

    public int getGreen(Object inData) {
        int pixel=0,length=0;
       byte bdata[] = (byte[])inData;
       pixel = bdata[0] & 0xff;
       length = bdata.length;
        if (length == 1) {
            return getGreen(pixel);
        }
        else {
            throw new UnsupportedOperationException
                ("This method is not supported by this color model");
        }
    }

    public int getBlue(Object inData) {
        int pixel=0,length=0;
               byte bdata[] = (byte[])inData;
               pixel = bdata[0] & 0xff;
               length = bdata.length;
        if (length == 1) {
            return getBlue(pixel);
        }
        else {
            throw new UnsupportedOperationException
                ("This method is not supported by this color model");
        }
    }

    public int getAlpha(Object inData) {
        int pixel=0,length=0;
               byte bdata[] = (byte[])inData;
               pixel = bdata[0] & 0xff;
               length = bdata.length;
        if (length == 1) {
            return getAlpha(pixel);
        }
        else {
            throw new UnsupportedOperationException
                ("This method is not supported by this color model");
        }
    }

    public int getRGB(Object inData) {
        return (getAlpha(inData) << 24)
            | (getRed(inData) << 16)
            | (getGreen(inData) << 8)
            | (getBlue(inData) << 0);
    }

    public Object getDataElements(int rgb, Object pixel) {
        throw new UnsupportedOperationException
            ("This method is not supported by this color model.");
    }

    public int[] getComponents(int pixel, int[] components, int offset) {
        throw new UnsupportedOperationException
            ("This method is not supported by this color model.");
    }

    public int[] getComponents(Object pixel, int[] components, int offset) {
        throw new UnsupportedOperationException
            ("This method is not supported by this color model.");
    }

    public int[] getUnnormalizedComponents(float[] normComponents,
                                           int normOffset,
                                           int[] components, int offset) {
        return components;
    }

    public float[] getNormalizedComponents(int[] components, int offset,
                                           float[] normComponents,
                                           int normOffset) {
        return normComponents;
    }

    public int getDataElement(int[] components, int offset) {
        throw new UnsupportedOperationException("This method is not supported "+
                                    "by this color model.");
    }

    public Object getDataElements(int[] components, int offset, Object obj) {
        throw new UnsupportedOperationException("This method has not been implemented "+
                                    "for this color model.");
    }

    public int getDataElement(float[] normComponents, int normOffset) {
        int components[] = getUnnormalizedComponents(normComponents,
                                                     normOffset, null, 0);
        return getDataElement(components, 0);
    }

    public Object getDataElements(float[] normComponents, int normOffset,
                                  Object obj) {
        int components[] = getUnnormalizedComponents(normComponents,
                                                     normOffset, null, 0);
        return getDataElements(components, 0, obj);
    }

    public float[] getNormalizedComponents(Object pixel,
                                           float[] normComponents,
                                           int normOffset) {
        int components[] = getComponents(pixel, null, 0);
        return getNormalizedComponents(components, 0,
                                       normComponents, normOffset);
    }

    public boolean equals(Object obj) {
        return false;
    }

    public int hashCode() {
        int result = 0;

        return result;
    }

    public void finalize() {
    }


    public String toString() {
    	return "unknow ColorModel";
    }

}
