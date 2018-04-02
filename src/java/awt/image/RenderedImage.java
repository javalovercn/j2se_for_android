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
import android.graphics.Bitmap;
import java.util.Vector;

/**
 * RenderedImage is a common interface for objects which contain or can produce
 * image data in the form of Rasters. The image data may be stored/produced as a
 * single tile or a regular array of tiles.
 */

public interface RenderedImage {
	public Bitmap getBitmapAdAPI();

	Vector<RenderedImage> getSources();

	Object getProperty(String name);

	String[] getPropertyNames();

	ColorModel getColorModel();

	SampleModel getSampleModel();

	int getWidth();

	int getHeight();

	int getMinX();

	int getMinY();

	int getNumXTiles();

	int getNumYTiles();

	int getMinTileX();

	int getMinTileY();

	int getTileWidth();

	int getTileHeight();

	int getTileGridXOffset();

	int getTileGridYOffset();

	Raster getTile(int tileX, int tileY);

	Raster getData();

	Raster getData(Rectangle rect);

	WritableRaster copyData(WritableRaster raster);
}