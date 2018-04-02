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

/**
 * This abstract class defines an interface for extracting samples of pixels in
 * an image. All image data is expressed as a collection of pixels. Each pixel
 * consists of a number of samples. A sample is a datum for one band of an image
 * and a band consists of all samples of a particular type in an image. For
 * example, a pixel might contain three samples representing its red, green and
 * blue components. There are three bands in the image containing this pixel.
 * One band consists of all the red samples from all pixels in the image. The
 * second band consists of all the green samples and the remaining band consists
 * of all of the blue samples. The pixel can be stored in various formats. For
 * example, all samples from a particular band can be stored contiguously or all
 * samples from a single pixel can be stored contiguously.
 * <p>
 * Subclasses of SampleModel specify the types of samples they can represent
 * (e.g. unsigned 8-bit byte, signed 16-bit short, etc.) and may specify how the
 * samples are organized in memory. In the Java 2D(tm) API, built-in image
 * processing operators may not operate on all possible sample types, but
 * generally will work for unsigned integral samples of 16 bits or less. Some
 * operators support a wider variety of sample types.
 * <p>
 * A collection of pixels is represented as a Raster, which consists of a
 * DataBuffer and a SampleModel. The SampleModel allows access to samples in the
 * DataBuffer and may provide low-level information that a programmer can use to
 * directly manipulate samples and pixels in the DataBuffer.
 * <p>
 * This class is generally a fall back method for dealing with images. More
 * efficient code will cast the SampleModel to the appropriate subclass and
 * extract the information needed to directly manipulate pixels in the
 * DataBuffer.
 *
 * @see java.awt.image.DataBuffer
 * @see java.awt.image.Raster
 * @see java.awt.image.ComponentSampleModel
 * @see java.awt.image.PixelInterleavedSampleModel
 * @see java.awt.image.BandedSampleModel
 * @see java.awt.image.MultiPixelPackedSampleModel
 * @see java.awt.image.SinglePixelPackedSampleModel
 */
public abstract class SampleModel {
	protected int width;
	protected int height;
	protected int numBands;
	protected int dataType;

	public SampleModel(int dataType, int w, int h, int numBands) {
		this.dataType = dataType;
		this.width = w;
		this.height = h;
		this.numBands = numBands;
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

	public abstract int getNumDataElements();

	final public int getDataType() {
		return dataType;
	}

	public int getTransferType() {
		return dataType;
	}

	public int[] getPixel(int x, int y, int iArray[], DataBuffer data) {
		return null;
	}

	public abstract Object getDataElements(int x, int y, Object obj, DataBuffer data);

	public Object getDataElements(int x, int y, int w, int h, Object obj, DataBuffer data) {
		return null;
	}

	public abstract void setDataElements(int x, int y, Object obj, DataBuffer data);

	public void setDataElements(int x, int y, int w, int h, Object obj, DataBuffer data) {
	}

	public float[] getPixel(int x, int y, float fArray[], DataBuffer data) {
		return null;
	}

	public double[] getPixel(int x, int y, double dArray[], DataBuffer data) {
		return null;
	}

	public int[] getPixels(int x, int y, int w, int h, int iArray[], DataBuffer data) {
		return null;
	}

	public float[] getPixels(int x, int y, int w, int h, float fArray[], DataBuffer data) {
		return null;
	}

	public double[] getPixels(int x, int y, int w, int h, double dArray[], DataBuffer data) {
		return null;
	}

	public abstract int getSample(int x, int y, int b, DataBuffer data);

	public float getSampleFloat(int x, int y, int b, DataBuffer data) {
		return 0;
	}

	public double getSampleDouble(int x, int y, int b, DataBuffer data) {
		return 0;
	}

	public int[] getSamples(int x, int y, int w, int h, int b, int iArray[], DataBuffer data) {
		return null;
	}

	public float[] getSamples(int x, int y, int w, int h, int b, float fArray[], DataBuffer data) {
		return null;
	}

	public double[] getSamples(int x, int y, int w, int h, int b, double dArray[],
			DataBuffer data) {
		return null;
	}

	public void setPixel(int x, int y, int iArray[], DataBuffer data) {
	}

	public void setPixel(int x, int y, float fArray[], DataBuffer data) {
	}

	public void setPixel(int x, int y, double dArray[], DataBuffer data) {
	}

	public void setPixels(int x, int y, int w, int h, int iArray[], DataBuffer data) {
	}

	public void setPixels(int x, int y, int w, int h, float fArray[], DataBuffer data) {
	}

	public void setPixels(int x, int y, int w, int h, double dArray[], DataBuffer data) {
	}

	public abstract void setSample(int x, int y, int b, int s, DataBuffer data);

	public void setSample(int x, int y, int b, float s, DataBuffer data) {
	}

	public void setSample(int x, int y, int b, double s, DataBuffer data) {
	}

	public void setSamples(int x, int y, int w, int h, int b, int iArray[], DataBuffer data) {
	}

	public void setSamples(int x, int y, int w, int h, int b, float fArray[], DataBuffer data) {
	}

	public void setSamples(int x, int y, int w, int h, int b, double dArray[], DataBuffer data) {
	}

	public abstract SampleModel createCompatibleSampleModel(int w, int h);

	public abstract SampleModel createSubsetSampleModel(int bands[]);

	public abstract DataBuffer createDataBuffer();

	public abstract int[] getSampleSize();

	public abstract int getSampleSize(int band);

}