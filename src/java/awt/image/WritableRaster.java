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
 * This class extends Raster to provide pixel writing capabilities. Refer to the
 * class comment for Raster for descriptions of how a Raster stores pixels.
 *
 * <p>
 * The constructors of this class are protected. To instantiate a
 * WritableRaster, use one of the createWritableRaster factory methods in the
 * Raster class.
 */
public class WritableRaster extends Raster {
	protected WritableRaster(SampleModel sampleModel, Point origin) {
		this(sampleModel, sampleModel.createDataBuffer(),
				new Rectangle(origin.x, origin.y, sampleModel.getWidth(), sampleModel.getHeight()),
				origin, null);
	}

	protected WritableRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point origin) {
		this(sampleModel, dataBuffer,
				new Rectangle(origin.x, origin.y, sampleModel.getWidth(), sampleModel.getHeight()),
				origin, null);
	}

	protected WritableRaster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle aRegion,
			Point sampleModelTranslate, WritableRaster parent) {
		super(sampleModel, dataBuffer, aRegion, sampleModelTranslate, parent);
	}

	public WritableRaster getWritableParent() {
		return (WritableRaster) parent;
	}

	public WritableRaster createWritableTranslatedChild(int childMinX, int childMinY) {
		return null;
	}

	public WritableRaster createWritableChild(int parentX, int parentY, int w, int h, int childMinX,
			int childMinY, int bandList[]) {
		return null;
	}

	public void setDataElements(int x, int y, Object inData) {
	}

	public void setDataElements(int x, int y, Raster inRaster) {
	}

	public void setDataElements(int x, int y, int w, int h, Object inData) {
	}

	public void setRect(Raster srcRaster) {
	}

	public void setRect(int dx, int dy, Raster srcRaster) {
	}

	public void setPixel(int x, int y, int iArray[]) {
	}

	public void setPixel(int x, int y, float fArray[]) {
	}

	public void setPixel(int x, int y, double dArray[]) {
	}

	public void setPixels(int x, int y, int w, int h, int iArray[]) {
	}

	public void setPixels(int x, int y, int w, int h, float fArray[]) {
	}

	public void setPixels(int x, int y, int w, int h, double dArray[]) {
	}

	public void setSample(int x, int y, int b, int s) {
	}

	public void setSample(int x, int y, int b, float s) {
	}

	public void setSample(int x, int y, int b, double s) {
	}

	public void setSamples(int x, int y, int w, int h, int b, int iArray[]) {
	}

	public void setSamples(int x, int y, int w, int h, int b, float fArray[]) {
	}

	public void setSamples(int x, int y, int w, int h, int b, double dArray[]) {
	}

}
