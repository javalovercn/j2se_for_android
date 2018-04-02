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

import hc.android.CanvasGraphics;
import hc.android.ScreenAdapter;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.util.Hashtable;
import java.util.Vector;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 *
 * The <code>BufferedImage</code> subclass describes an {@link java.awt.Image
 * Image} with an accessible buffer of image data. A <code>BufferedImage</code>
 * is comprised of a {@link ColorModel} and a {@link Raster} of image data. The
 * number and types of bands in the {@link SampleModel} of the
 * <code>Raster</code> must match the number and types required by the
 * <code>ColorModel</code> to represent its color and alpha components. All
 * <code>BufferedImage</code> objects have an upper left corner coordinate of
 * (0,&nbsp;0). Any <code>Raster</code> used to construct a
 * <code>BufferedImage</code> must therefore have minX=0 and minY=0.
 *
 * <p>
 * This class relies on the data fetching and setting methods of
 * <code>Raster</code>, and on the color characterization methods of
 * <code>ColorModel</code>.
 *
 * @see ColorModel
 * @see Raster
 * @see WritableRaster
 */
public class BufferedImage extends java.awt.Image implements WritableRenderedImage, Transparency {
	int imageType = TYPE_INT_ARGB;
	public static final int TYPE_CUSTOM = 0;

	public static final int TYPE_INT_RGB = 1;

	public static final int TYPE_INT_ARGB = 2;

	public static final int TYPE_INT_ARGB_PRE = 3;

	public static final int TYPE_INT_BGR = 4;

	public static final int TYPE_3BYTE_BGR = 5;

	public static final int TYPE_4BYTE_ABGR = 6;

	public static final int TYPE_4BYTE_ABGR_PRE = 7;

	public static final int TYPE_USHORT_565_RGB = 8;

	public static final int TYPE_USHORT_555_RGB = 9;

	public static final int TYPE_BYTE_GRAY = 10;

	public static final int TYPE_USHORT_GRAY = 11;

	public static final int TYPE_BYTE_BINARY = 12;

	public static final int TYPE_BYTE_INDEXED = 13;

	private static final int DCM_RED_MASK = 0x00ff0000;
	private static final int DCM_GREEN_MASK = 0x0000ff00;
	private static final int DCM_BLUE_MASK = 0x000000ff;
	private static final int DCM_ALPHA_MASK = 0xff000000;
	private static final int DCM_565_RED_MASK = 0xf800;
	private static final int DCM_565_GRN_MASK = 0x07E0;
	private static final int DCM_565_BLU_MASK = 0x001F;
	private static final int DCM_555_RED_MASK = 0x7C00;
	private static final int DCM_555_GRN_MASK = 0x03E0;
	private static final int DCM_555_BLU_MASK = 0x001F;
	private static final int DCM_BGR_RED_MASK = 0x0000ff;
	private static final int DCM_BGR_GRN_MASK = 0x00ff00;
	private static final int DCM_BGR_BLU_MASK = 0xff0000;

	public BufferedImage(Drawable drawable) {
		this.bitmapDrawableAdapter = drawable;
	}

	public BufferedImage(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	WritableRaster raster;

	public BufferedImage(int width, int height, int imageType) {
		switch (imageType) {
		case TYPE_INT_ARGB: {
			colorModel = ColorModel.getRGBdefault();
			raster = colorModel.createCompatibleWritableRaster(width, height);
			break;
		}
		case TYPE_INT_RGB: {
			colorModel = ColorModel.getRGBdefault();
			raster = colorModel.createCompatibleWritableRaster(width, height);
			break;
		}
		default:
			throw new IllegalArgumentException("Unknown image type " + imageType);
		}

		this.imageType = imageType;
		bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	}

	public BufferedImage(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied,
			Hashtable<?, ?> properties) {
		this.bitmap = null;

	}

	public int getType() {
		return imageType;
	}

	// public ColorModel getColorModel() {
	// return colorModel;
	// }

	// public WritableRaster getRaster() {
	// return raster;
	// }

	// public WritableRaster getAlphaRaster() {
	// return colorModel.getAlphaRaster(raster);
	// }

	public int getRGB(int x, int y) {
		return bitmap.getPixel(x, y);
	}

	public int[] getRGB(int startX, int startY, int w, int h, int[] rgbArray, int offset,
			int scansize) {
		if (rgbArray == null) {
			rgbArray = new int[offset + scansize * h];
		}
		bitmap.getPixels(rgbArray, offset, scansize, startX, startY, w, h);
		return rgbArray;
	}

	// public synchronized void setRGB(int x, int y, int rgb) {
	// }

	// public void setRGB(int startX, int startY, int w, int h,
	// int[] rgbArray, int offset, int scansize) {
	// }

	public Graphics2D createGraphics() {
		return (Graphics2D) getGraphics();
	}

	public synchronized void setRGB(int x, int y, int rgb) {
		raster.setDataElements(x, y, colorModel.getDataElements(rgb, null));
	}

	public void setRGB(int startX, int startY, int w, int h, int[] rgbArray, int offset,
			int scansize) {
		// int yoff = offset;
		// int off;
		// Object pixel = null;
		//
		// for (int y = startY; y < startY+h; y++, yoff+=scansize) {
		// off = yoff;
		// for (int x = startX; x < startX+w; x++) {
		// pixel = colorModel.getDataElements(rgbArray[off++], pixel);xx
		// raster.setDataElements(x, y, pixel);
		// }
		// }
		if (imageType == TYPE_INT_RGB) {
			for (int i = 0; i < rgbArray.length; i++) {
				rgbArray[i] |= 0xFF000000;
			}
		}

		bitmap.setPixels(rgbArray, offset, scansize, startX, startY, w, h);
	}

	public int getWidth() {
		return bitmap.getWidth();
	}

	public int getHeight() {
		return bitmap.getHeight();
	}

	public int getWidth(ImageObserver observer) {
		return this.getWidth();
	}

	public int getHeight(ImageObserver observer) {
		return this.getHeight();
	}

	// public Object getProperty(String name, ImageObserver observer) {
	// return getProperty(name);
	// }

	// public Object getProperty(String name) {
	// return null;
	// }

	// public java.awt.Graphics getGraphics() {
	// return createGraphics();
	// }

	// public Graphics2D createGraphics() {
	// return null;
	// }

	// public BufferedImage getSubimage (int x, int y, int w, int h) {
	// return null;
	// }

	// public boolean isAlphaPremultiplied() {
	// return colorModel.isAlphaPremultiplied();
	// }

	public String toString() {
		return "BufferedImage";
	}

	@Override
	public Vector<RenderedImage> getSources() {
		return null;
	}

	@Override
	public Object getProperty(String name) {
		return null;
	}

	@Override
	public String[] getPropertyNames() {
		return null;
	}

	ColorModel colorModel;

	@Override
	public ColorModel getColorModel() {
		if (colorModel == null) {
			colorModel = new DirectColorModel(24, 0xFF0000, 0xFF00, 0xFF);
		}
		return colorModel;
	}

	@Override
	public SampleModel getSampleModel() {
		return null;
	}

	@Override
	public int getMinX() {
		return 0;
	}

	@Override
	public int getMinY() {
		return 0;
	}

	@Override
	public int getNumXTiles() {
		return 0;
	}

	@Override
	public int getNumYTiles() {
		return 0;
	}

	@Override
	public int getMinTileX() {
		return 0;
	}

	@Override
	public int getMinTileY() {
		return 0;
	}

	@Override
	public int getTileWidth() {
		return 0;
	}

	@Override
	public int getTileHeight() {
		return 0;
	}

	@Override
	public int getTileGridXOffset() {
		return 0;
	}

	@Override
	public int getTileGridYOffset() {
		return 0;
	}

	@Override
	public Raster getTile(int tileX, int tileY) {
		return null;
	}

	@Override
	public Raster getData() {
		return null;
	}

	@Override
	public Raster getData(Rectangle rect) {
		return null;
	}

	@Override
	public WritableRaster copyData(WritableRaster raster) {
		return null;
	}

	@Override
	public int getTransparency() {
		return 0;
	}

	@Override
	public void addTileObserver(TileObserver to) {
	}

	@Override
	public void removeTileObserver(TileObserver to) {
	}

	@Override
	public WritableRaster getWritableTile(int tileX, int tileY) {
		return null;
	}

	@Override
	public void releaseWritableTile(int tileX, int tileY) {
	}

	@Override
	public boolean isTileWritable(int tileX, int tileY) {
		return false;
	}

	@Override
	public Point[] getWritableTileIndices() {
		return null;
	}

	@Override
	public boolean hasTileWriters() {
		return false;
	}

	@Override
	public void setData(Raster r) {
	}

	@Override
	public Bitmap getBitmapAdAPI() {
		return bitmap;
	}

	@Override
	public ImageProducer getSource() {
		return null;
	}

	Graphics graphics;

	@Override
	public Graphics getGraphics() {
		if (graphics == null) {
			Object[] para = { bitmap, new Canvas(bitmap) };
			graphics = new CanvasGraphics(para, ScreenAdapter.initScreenAdapterFromContext(false));
		}
		return graphics;
	}

	@Override
	public Object getProperty(String name, ImageObserver observer) {
		return null;
	}
}