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
package javax.imageio;

import hc.App;
import hc.android.ActivityManager;
import hc.android.AndroidClassUtil;
import hc.android.ImageUtil;
import hc.android.J2SEInitor;
import hc.util.ResourceUtil;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * A class containing static convenience methods for locating
 * <code>ImageReader</code>s and <code>ImageWriter</code>s, and performing
 * simple encoding and decoding.
 *
 */
public final class ImageIO {
	static final Iterator<Object> EMPTY_ITERATOR = new Iterator() {
		public boolean hasNext() {
			return false;
		}

		public Object next() {
			throw new NoSuchElementException();
		}

		public void remove() {
			throw new IllegalStateException();
		}
	};

	private ImageIO() {
	}

	private static String getTempDir() {
		return "java.io.tmpdir";
	}

	public static void setUseCache(boolean useCache) {
	}

	public static boolean getUseCache() {
		return false;
	}

	public static void setCacheDirectory(File cacheDirectory) {
	}

	public static File getCacheDirectory() {
		return new File(ResourceUtil.getBaseDir(), "");
	}

	public static ImageInputStream createImageInputStream(Object input) throws IOException {
		return null;
	}

	public static ImageOutputStream createImageOutputStream(Object output) throws IOException {
		return null;
	}

	public static String[] getReaderFormatNames() {
		return new String[0];
	}

	public static String[] getReaderMIMETypes() {
		return new String[0];
	}

	public static String[] getReaderFileSuffixes() {
		String[] ext = { "png", "jpg" };
		return ext;
	}

	public static Iterator getImageReaders(Object input) {
		return EMPTY_ITERATOR;
	}

	public static Iterator getImageReadersByFormatName(String formatName) {
		return EMPTY_ITERATOR;
	}

	public static Iterator getImageReadersBySuffix(String fileSuffix) {
		return EMPTY_ITERATOR;
	}

	public static Iterator getImageReadersByMIMEType(String MIMEType) {
		return EMPTY_ITERATOR;
	}

	public static String[] getWriterFormatNames() {
		return new String[0];
	}

	public static String[] getWriterMIMETypes() {
		return new String[0];
	}

	public static String[] getWriterFileSuffixes() {
		return new String[0];
	}

	private static boolean contains(String[] names, String name) {
		for (int i = 0; i < names.length; i++) {
			if (name.equalsIgnoreCase(names[i])) {
				return true;
			}
		}

		return false;
	}

	public static Iterator getImageWritersByFormatName(String formatName) {
		return EMPTY_ITERATOR;
	}

	public static Iterator getImageWritersBySuffix(String fileSuffix) {
		return EMPTY_ITERATOR;
	}

	public static Iterator getImageWritersByMIMEType(String MIMEType) {
		return EMPTY_ITERATOR;
	}

	public static BufferedImage read(File input) throws IOException {
		final String path = input.getPath();

		Vector<ClassLoader> packageLoader = J2SEInitor.getPackagesPath();
		int size = packageLoader.size();
		for (int i = 0; i < size; i++) {
			ClassLoader loader = packageLoader.get(i);
			URL url = loader.getResource(path);
			try {
				BufferedImage bi = read(url);
				if (bi != null) {
					return bi;
				}
			} catch (Exception e) {
			}
		}

		BufferedImage bi = read(new FileInputStream(input));
		return bi;
	}

	public static BufferedImage read(InputStream input) throws IOException {
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			BufferedImage bi = new BufferedImage(bitmap);
			return bi;
		} catch (Throwable e) {
		} finally {
			try {
				input.close();
			} catch (Throwable e) {
			}
		}
		return null;
	}

	public static BufferedImage read(URL url) throws IOException {
		InputStream is = null;
		try {
			is = url.openStream();
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			BufferedImage bi = new BufferedImage(bitmap);
			return bi;
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Throwable e) {
			}
		}
		return null;
	}

	public static BufferedImage read(ImageInputStream stream) throws IOException {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public static boolean write(RenderedImage im, String formatName, ImageOutputStream output)
			throws IOException {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public static boolean write(RenderedImage im, String formatName, File file) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("output == null!");
		}

		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file);
			return write(im, formatName, os);
		} catch (Exception e) {
		} finally {
			try {
				os.close();
			} catch (Throwable e) {
			}
		}
		return false;
	}

	public static boolean write(RenderedImage im, String formatName, OutputStream output)
			throws IOException {
		try {
			Bitmap.CompressFormat format = null;
			if (formatName.equalsIgnoreCase("png")) {
				format = Bitmap.CompressFormat.PNG;
			} else if (formatName.equalsIgnoreCase("jpg")) {
				format = Bitmap.CompressFormat.JPEG;
			} else {
				throw new IllegalArgumentException("illegal format : " + formatName);
			}

			im.getBitmapAdAPI().compress(format, 100, output);
			return true;
		} catch (Exception e) {
			try {
				output.close();
			} catch (Exception ex) {
			}
		}
		return false;
	}

}
