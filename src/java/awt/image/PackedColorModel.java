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

import java.awt.color.ColorSpace;

/**
 * The <code>PackedColorModel</code> class is an abstract
 * {@link ColorModel} class that works with pixel values which represent
 * color and alpha information as separate samples and which pack all
 * samples for a single pixel into a single int, short, or byte quantity.
 * This class can be used with an arbitrary {@link ColorSpace}.  The number of
 * color samples in the pixel values must be the same as the number of color
 * components in the <code>ColorSpace</code>.  There can be a single alpha
 * sample.  The array length is always 1 for those methods that use a
 * primitive array pixel representation of type <code>transferType</code>.
 * The transfer types supported are DataBuffer.TYPE_BYTE,
 * DataBuffer.TYPE_USHORT, and DataBuffer.TYPE_INT.
 * Color and alpha samples are stored in the single element of the array
 * in bits indicated by bit masks.  Each bit mask must be contiguous and
 * masks must not overlap.  The same masks apply to the single int
 * pixel representation used by other methods.  The correspondence of
 * masks and color/alpha samples is as follows:
 * <ul>
 * <li> Masks are identified by indices running from 0 through
 * {@link ColorModel#getNumComponents() getNumComponents}&nbsp;-&nbsp;1.
 * <li> The first
 * {@link ColorModel#getNumColorComponents() getNumColorComponents}
 * indices refer to color samples.
 * <li> If an alpha sample is present, it corresponds the last index.
 * <li> The order of the color indices is specified
 * by the <code>ColorSpace</code>.  Typically, this reflects the name of
 * the color space type (for example, TYPE_RGB), index 0
 * corresponds to red, index 1 to green, and index 2 to blue.
 * </ul>
 * <p>
 * The translation from pixel values to color/alpha components for
 * display or processing purposes is a one-to-one correspondence of
 * samples to components.
 * A <code>PackedColorModel</code> is typically used with image data
 * that uses masks to define packed samples.  For example, a
 * <code>PackedColorModel</code> can be used in conjunction with a
 * {@link SinglePixelPackedSampleModel} to construct a
 * {@link BufferedImage}.  Normally the masks used by the
 * {@link SampleModel} and the <code>ColorModel</code> would be the same.
 * However, if they are different, the color interpretation of pixel data is
 * done according to the masks of the <code>ColorModel</code>.
 * <p>
 * A single <code>int</code> pixel representation is valid for all objects
 * of this class since it is always possible to represent pixel values
 * used with this class in a single <code>int</code>.  Therefore, methods
 * that use this representation do not throw an
 * <code>IllegalArgumentException</code> due to an invalid pixel value.
 * <p>
 * A subclass of <code>PackedColorModel</code> is {@link DirectColorModel},
 * which is similar to an X11 TrueColor visual.
 *
 * @see DirectColorModel
 * @see SinglePixelPackedSampleModel
 * @see BufferedImage
 */

public abstract class PackedColorModel extends ColorModel {
    int[] maskArray;
    int[] maskOffsets;
    float[] scaleFactors;

    public PackedColorModel (ColorSpace space, int bits,
                             int[] colorMaskArray, int alphaMask,
                             boolean isAlphaPremultiplied,
                             int trans, int transferType) {
        super(bits, null,
              space, (alphaMask == 0 ? false : true),
              isAlphaPremultiplied, trans, transferType);
    }

    public PackedColorModel(ColorSpace space, int bits, int rmask, int gmask,
                            int bmask, int amask,
                            boolean isAlphaPremultiplied,
                            int trans, int transferType) {
        super (bits, null,
               space, (amask == 0 ? false : true),
               isAlphaPremultiplied, trans, transferType);
    }

    final public int getMask(int index) {
        return maskArray[index];
    }

    final public int[] getMasks() {
        return (int[]) maskArray.clone();
    }

    private void DecomposeMask(int mask,  int idx, String componentName) {
    }

    public SampleModel createCompatibleSampleModel(int w, int h) {
        return null;
    }

    public boolean isCompatibleSampleModel(SampleModel sm) {
            return false;
    }

    public WritableRaster getAlphaRaster(WritableRaster raster) {
            return null;
    }

    public boolean equals(Object obj) {
            return false;
    }

    private final static int[] createBitsArray(int[]colorMaskArray,
                                               int alphaMask) {
        return null;
    }

    private final static int[] createBitsArray(int rmask, int gmask, int bmask,
                                         int amask) {
        return null;
    }

    private final static int countBits(int mask) {
        int count = 0;
        if (mask != 0) {
            while ((mask & 1) == 0) {
                mask >>>= 1;
            }
            while ((mask & 1) == 1) {
                mask >>>= 1;
                count++;
            }
        }
        if (mask != 0) {
            return -1;
        }
        return count;
    }

}