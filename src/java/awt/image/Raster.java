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

/* ****************************************************************
 ******************************************************************
 ******************************************************************
 *** COPYRIGHT (c) Eastman Kodak Company, 1997
 *** As  an unpublished  work pursuant to Title 17 of the United
 *** States Code.  All rights reserved.
 ******************************************************************
 ******************************************************************
 ******************************************************************/


package java.awt.image;
import java.awt.Rectangle;
import java.awt.Point;

/**
 * A class representing a rectangular array of pixels.  A Raster
 * encapsulates a DataBuffer that stores the sample values and a
 * SampleModel that describes how to locate a given sample value in a
 * DataBuffer.
 * <p>
 * A Raster defines values for pixels occupying a particular
 * rectangular area of the plane, not necessarily including (0, 0).
 * The rectangle, known as the Raster's bounding rectangle and
 * available by means of the getBounds method, is defined by minX,
 * minY, width, and height values.  The minX and minY values define
 * the coordinate of the upper left corner of the Raster.  References
 * to pixels outside of the bounding rectangle may result in an
 * exception being thrown, or may result in references to unintended
 * elements of the Raster's associated DataBuffer.  It is the user's
 * responsibility to avoid accessing such pixels.
 * <p>
 * A SampleModel describes how samples of a Raster
 * are stored in the primitive array elements of a DataBuffer.
 * Samples may be stored one per data element, as in a
 * PixelInterleavedSampleModel or BandedSampleModel, or packed several to
 * an element, as in a SinglePixelPackedSampleModel or
 * MultiPixelPackedSampleModel.  The SampleModel is also
 * controls whether samples are sign extended, allowing unsigned
 * data to be stored in signed Java data types such as byte, short, and
 * int.
 * <p>
 * Although a Raster may live anywhere in the plane, a SampleModel
 * makes use of a simple coordinate system that starts at (0, 0).  A
 * Raster therefore contains a translation factor that allows pixel
 * locations to be mapped between the Raster's coordinate system and
 * that of the SampleModel.  The translation from the SampleModel
 * coordinate system to that of the Raster may be obtained by the
 * getSampleModelTranslateX and getSampleModelTranslateY methods.
 * <p>
 * A Raster may share a DataBuffer with another Raster either by
 * explicit construction or by the use of the createChild and
 * createTranslatedChild methods.  Rasters created by these methods
 * can return a reference to the Raster they were created from by
 * means of the getParent method.  For a Raster that was not
 * constructed by means of a call to createTranslatedChild or
 * createChild, getParent will return null.
 * <p>
 * The createTranslatedChild method returns a new Raster that
 * shares all of the data of the current Raster, but occupies a
 * bounding rectangle of the same width and height but with a
 * different starting point.  For example, if the parent Raster
 * occupied the region (10, 10) to (100, 100), and the translated
 * Raster was defined to start at (50, 50), then pixel (20, 20) of the
 * parent and pixel (60, 60) of the child occupy the same location in
 * the DataBuffer shared by the two Rasters.  In the first case, (-10,
 * -10) should be added to a pixel coordinate to obtain the
 * corresponding SampleModel coordinate, and in the second case (-50,
 * -50) should be added.
 * <p>
 * The translation between a parent and child Raster may be
 * determined by subtracting the child's sampleModelTranslateX and
 * sampleModelTranslateY values from those of the parent.
 * <p>
 * The createChild method may be used to create a new Raster
 * occupying only a subset of its parent's bounding rectangle
 * (with the same or a translated coordinate system) or
 * with a subset of the bands of its parent.
 * <p>
 * All constructors are protected.  The correct way to create a
 * Raster is to use one of the static create methods defined in this
 * class.  These methods create instances of Raster that use the
 * standard Interleaved, Banded, and Packed SampleModels and that may
 * be processed more efficiently than a Raster created by combining
 * an externally generated SampleModel and DataBuffer.
 * @see java.awt.image.DataBuffer
 * @see java.awt.image.SampleModel
 * @see java.awt.image.PixelInterleavedSampleModel
 * @see java.awt.image.BandedSampleModel
 * @see java.awt.image.SinglePixelPackedSampleModel
 * @see java.awt.image.MultiPixelPackedSampleModel
 */
public class Raster {
    protected SampleModel sampleModel;
    protected DataBuffer dataBuffer;
    protected int minX;
    protected int minY;
    protected int width;
    protected int height;
    protected int sampleModelTranslateX;
    protected int sampleModelTranslateY;
    protected int numBands;
    protected int numDataElements;
    protected Raster parent;

    public static WritableRaster createInterleavedRaster(int dataType,
                                                         int w, int h,
                                                         int bands,
                                                         Point location) {
    	return null;
    }

    public static WritableRaster createInterleavedRaster(int dataType,
                                                         int w, int h,
                                                         int scanlineStride,
                                                         int pixelStride,
                                                         int bandOffsets[],
                                                         Point location) {
    	return null;
    }

    public static WritableRaster createBandedRaster(int dataType,
                                                    int w, int h,
                                                    int bands,
                                                    Point location) {
    	return null;
    }

    public static WritableRaster createBandedRaster(int dataType,
                                                    int w, int h,
                                                    int scanlineStride,
                                                    int bankIndices[],
                                                    int bandOffsets[],
                                                    Point location) {
    	return null;
    }

    public static WritableRaster createPackedRaster(int dataType,
                                                    int w, int h,
                                                    int bandMasks[],
                                                    Point location) {
    	return null;
    }

    public static WritableRaster createPackedRaster(int dataType,
                                                    int w, int h,
                                                    int bands,
                                                    int bitsPerBand,
                                                    Point location) {
    	return null;
    }

    public static WritableRaster createInterleavedRaster(DataBuffer dataBuffer,
                                                         int w, int h,
                                                         int scanlineStride,
                                                         int pixelStride,
                                                         int bandOffsets[],
                                                         Point location) {
    	return null;
    }

    public static WritableRaster createBandedRaster(DataBuffer dataBuffer,
                                                    int w, int h,
                                                    int scanlineStride,
                                                    int bankIndices[],
                                                    int bandOffsets[],
                                                    Point location) {
    	return null;
    }

    public static WritableRaster createPackedRaster(DataBuffer dataBuffer,
                                                    int w, int h,
                                                    int scanlineStride,
                                                    int bandMasks[],
                                                    Point location) {
    	return null;
    }

    public static WritableRaster createPackedRaster(DataBuffer dataBuffer,
                                                    int w, int h,
                                                    int bitsPerPixel,
                                                    Point location) {
    	return null;
    }

    public static Raster createRaster(SampleModel sm,
                                      DataBuffer db,
                                      Point location) {
        return null;
    }

    public static WritableRaster createWritableRaster(SampleModel sm,
                                                      Point location) {
        return null;
    }

    public static WritableRaster createWritableRaster(SampleModel sm,
                                                      DataBuffer db,
                                                      Point location) {
        return null;
    }

    protected Raster(SampleModel sampleModel,
                     Point origin) {
        this(sampleModel,
             sampleModel.createDataBuffer(),
             new Rectangle(origin.x,
                           origin.y,
                           sampleModel.getWidth(),
                           sampleModel.getHeight()),
             origin,
             null);
    }

    protected Raster(SampleModel sampleModel,
                     DataBuffer dataBuffer,
                     Point origin) {
        this(sampleModel,
             dataBuffer,
             new Rectangle(origin.x,
                           origin.y,
                           sampleModel.getWidth(),
                           sampleModel.getHeight()),
             origin,
             null);
    }

    protected Raster(SampleModel sampleModel,
                     DataBuffer dataBuffer,
                     Rectangle aRegion,
                     Point sampleModelTranslate,
                     Raster parent) {
    }

    public Raster getParent() {
        return parent;
    }

    final public int getSampleModelTranslateX() {
        return sampleModelTranslateX;
    }

    final public int getSampleModelTranslateY() {
        return sampleModelTranslateY;
    }

    public WritableRaster createCompatibleWritableRaster() {
        return null;
    }

    public WritableRaster createCompatibleWritableRaster(int w, int h) {
        return null;
    }

    public WritableRaster createCompatibleWritableRaster(Rectangle rect) {
        return null;
    }

    public WritableRaster createCompatibleWritableRaster(int x, int y,
                                                         int w, int h) {
        return null;
    }

    public Raster createTranslatedChild(int childMinX, int childMinY) {
        return null;
    }

    public Raster createChild(int parentX, int parentY,
                              int width, int height,
                              int childMinX, int childMinY,
                              int bandList[]) {
    	return null;
    }

    public Rectangle getBounds() {
        return new Rectangle(minX, minY, width, height);
    }

    final public int getMinX() {
        return minX;
    }

    final public int getMinY() {
        return minY;
    }

    final public int getWidth() {
        return width;
    }

    final public int getHeight() {
        return height;
    }

    final public int getNumBands() {
        return numBands;
    }

    final public int getNumDataElements() {
        return sampleModel.getNumDataElements();
    }

    final public int getTransferType() {
        return sampleModel.getTransferType();
    }

    public DataBuffer getDataBuffer() {
        return dataBuffer;
    }

    public SampleModel getSampleModel() {
        return sampleModel;
    }

    public Object getDataElements(int x, int y, Object outData) {
        return null;
    }

    public Object getDataElements(int x, int y, int w, int h, Object outData) {
        return null;
    }

    public int[] getPixel(int x, int y, int iArray[]) {
        return null;
    }

    public float[] getPixel(int x, int y, float fArray[]) {
        return null;
    }

    public double[] getPixel(int x, int y, double dArray[]) {
        return null;
    }

    public int[] getPixels(int x, int y, int w, int h, int iArray[]) {
        return null;
    }

    public float[] getPixels(int x, int y, int w, int h,
                             float fArray[]) {
        return null;
    }

    public double[] getPixels(int x, int y, int w, int h,
                              double dArray[]) {
        return null;
    }

    public int getSample(int x, int y, int b) {
        return 0;
    }

    public float getSampleFloat(int x, int y, int b) {
        return 0;
    }

    public double getSampleDouble(int x, int y, int b) {
        return 0;
    }

    public int[] getSamples(int x, int y, int w, int h, int b,
                            int iArray[]) {
        return null;
    }

    public float[] getSamples(int x, int y, int w, int h, int b,
                              float fArray[]) {
        return null;
    }

    public double[] getSamples(int x, int y, int w, int h, int b,
                               double dArray[]) {
         return null;
    }

}