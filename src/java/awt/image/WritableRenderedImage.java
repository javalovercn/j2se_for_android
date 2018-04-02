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

import java.awt.Point;

/**
 * WriteableRenderedImage is a common interface for objects which contain or can
 * produce image data in the form of Rasters and which can be modified and/or
 * written over. The image data may be stored/produced as a single tile or a
 * regular array of tiles.
 * <p>
 * WritableRenderedImage provides notification to other interested objects when
 * a tile is checked out for writing (via the getWritableTile method) and when
 * the last writer of a particular tile relinquishes its access (via a call to
 * releaseWritableTile). Additionally, it allows any caller to determine whether
 * any tiles are currently checked out (via hasTileWriters), and to obtain a
 * list of such tiles (via getWritableTileIndices, in the form of a Vector of
 * Point objects).
 * <p>
 * Objects wishing to be notified of changes in tile writability must implement
 * the TileObserver interface, and are added by a call to addTileObserver.
 * Multiple calls to addTileObserver for the same object will result in multiple
 * notifications. An existing observer may reduce its notifications by calling
 * removeTileObserver; if the observer had no notifications the operation is a
 * no-op.
 * <p>
 * It is necessary for a WritableRenderedImage to ensure that notifications
 * occur only when the first writer acquires a tile and the last writer releases
 * it.
 *
 */
public interface WritableRenderedImage extends RenderedImage {
	public void addTileObserver(TileObserver to);

	public void removeTileObserver(TileObserver to);

	public WritableRaster getWritableTile(int tileX, int tileY);

	public void releaseWritableTile(int tileX, int tileY);

	public boolean isTileWritable(int tileX, int tileY);

	public Point[] getWritableTileIndices();

	public boolean hasTileWriters();

	public void setData(Raster r);

}