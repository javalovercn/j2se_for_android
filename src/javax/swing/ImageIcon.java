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
package javax.swing;

import hc.android.ActivityManager;
import hc.android.AndroidClassUtil;
import hc.android.ImageUtil;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.IllegalComponentStateException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Locale;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleIcon;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleStateSet;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * An implementation of the Icon interface that paints Icons from Images. Images
 * that are created from a URL, filename or byte array are preloaded using
 * MediaTracker to monitor the loaded state of the image.
 *
 * <p>
 * For further information and examples of using image icons, see
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/icon.html">How
 * to Use Icons</a> in <em>The Java Tutorial.</em>
 *
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Jeff Dinkins
 * @author Lynn Monsanto
 */
public class ImageIcon implements Icon, Serializable, Accessible {
	transient Image image;
	transient int loadStatus = 0;
	ImageObserver imageObserver;
	String description = null;

	public ImageIcon(String filename, String description) {
		image = Toolkit.getDefaultToolkit().getImage(filename);
		if (image == null) {
			return;
		}
		this.description = description;
		loadImage(image);
	}

	/**
	 * for example : /hc/android/res/hc_128.png
	 * 
	 * @param filename
	 */
	public ImageIcon(String filename) {
		this(filename, filename);
	}

	public ImageIcon(URL location, String description) {
		image = Toolkit.getDefaultToolkit().getImage(location);
		if (image == null) {
			return;
		}
		this.description = description;
		loadImage(image);
	}

	public ImageIcon(URL location) {
		this(location, location.toExternalForm());
	}

	public ImageIcon(Image image, String description) {
		this(image);
		this.description = description;
	}

	public ImageIcon(Image image) {
		this.image = image;
		description = "";
		loadImage(image);
	}

	public ImageIcon(byte[] imageData, String description) {
		this.image = Toolkit.getDefaultToolkit().createImage(imageData);
		if (image == null) {
			return;
		}
		this.description = description;
		loadImage(image);
	}

	public ImageIcon(byte[] imageData) {
		this.image = Toolkit.getDefaultToolkit().createImage(imageData);
		if (image == null) {
			return;
		}
		Object o = image.getProperty("comment", imageObserver);
		if (o instanceof String) {
			description = (String) o;
		}
		loadImage(image);
	}

	public ImageIcon() {
	}

	protected void loadImage(Image image) {
	}

	public int getImageLoadStatus() {
		return MediaTracker.COMPLETE;
	}

	@Transient
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
		loadImage(image);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
		if (imageObserver == null) {
			g.drawImage(image, x, y, c);
		} else {
			g.drawImage(image, x, y, imageObserver);
		}
	}

	public int getIconWidth() {
		return image.getWidth(null);
	}

	public int getIconHeight() {
		return image.getHeight(null);
	}

	public void setImageObserver(ImageObserver observer) {
		imageObserver = observer;
	}

	@Transient
	public ImageObserver getImageObserver() {
		return imageObserver;
	}

	public String toString() {
		if (description != null) {
			return description;
		}
		return super.toString();
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private AccessibleContext accessibleContext = null;

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

	protected class AccessibleImageIcon extends AccessibleContext
			implements AccessibleIcon, Serializable {

		public AccessibleRole getAccessibleRole() {
			return AccessibleRole.ICON;
		}

		public AccessibleStateSet getAccessibleStateSet() {
			return null;
		}

		public Accessible getAccessibleParent() {
			return null;
		}

		public int getAccessibleIndexInParent() {
			return -1;
		}

		public int getAccessibleChildrenCount() {
			return 0;
		}

		public Accessible getAccessibleChild(int i) {
			return null;
		}

		public Locale getLocale() throws IllegalComponentStateException {
			return null;
		}

		public String getAccessibleIconDescription() {
			return ImageIcon.this.getDescription();
		}

		public void setAccessibleIconDescription(String description) {
			ImageIcon.this.setDescription(description);
		}

		public int getAccessibleIconHeight() {
			return ImageIcon.this.getIconHeight();
		}

		public int getAccessibleIconWidth() {
			return ImageIcon.this.getIconWidth();
		}

		private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
		}

		private void writeObject(ObjectOutputStream s) throws IOException {
		}
	}

	BitmapDrawable drawable;

	public static Bitmap getBitmapAdAPI(final ImageIcon imageIcon) {
		return imageIcon.image.getBitmapAdAPI();
	}

	public static ImageIcon toGrayAdAPI(final ImageIcon imageIcon) {
		Bitmap gray = ImageUtil.toGray(imageIcon.image.getBitmapAdAPI());
		final BufferedImage bi = new BufferedImage(gray);
		bi.initZoom = imageIcon.image.initZoom;
		ImageIcon out = new ImageIcon(bi);
		return out;
	}

	// @Override
	// public BitmapDrawable getOriBitmapDrawableAdAPI() {
	// Bitmap bitmap = image.getBitmapAdAPI();//一个图片被多处使用，所以要生成新实例
	// return new BitmapDrawable(ActivityManager.applicationContext.getResources(),
	// bitmap);
	// }

	public static Drawable getAdapterBitmapDrawableAdAPI(final ImageIcon imageIcon,
			final Component component) {
		return imageIcon.image.getAdapterBitmapDrawableAdAPI(component);
	}
}