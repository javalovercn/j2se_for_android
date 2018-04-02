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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.Transparency;

/**
 * VolatileImage is an image which can lose its contents at any time due to
 * circumstances beyond the control of the application (e.g., situations caused
 * by the operating system or by other applications). Because of the potential
 * for hardware acceleration, a VolatileImage object can have significant
 * performance benefits on some platforms.
 * <p>
 * The drawing surface of an image (the memory where the image contents actually
 * reside) can be lost or invalidated, causing the contents of that memory to go
 * away. The drawing surface thus needs to be restored or recreated and the
 * contents of that surface need to be re-rendered. VolatileImage provides an
 * interface for allowing the user to detect these problems and fix them when
 * they occur.
 * <p>
 * When a VolatileImage object is created, limited system resources such as
 * video memory (VRAM) may be allocated in order to support the image. When a
 * VolatileImage object is no longer used, it may be garbage-collected and those
 * system resources will be returned, but this process does not happen at
 * guaranteed times. Applications that create many VolatileImage objects (for
 * example, a resizing window may force recreation of its back buffer as the
 * size changes) may run out of optimal system resources for new VolatileImage
 * objects simply because the old objects have not yet been removed from the
 * system. (New VolatileImage objects may still be created, but they may not
 * perform as well as those created in accelerated memory). The flush method may
 * be called at any time to proactively release the resources used by a
 * VolatileImage so that it does not prevent subsequent VolatileImage objects
 * from being accelerated. In this way, applications can have more control over
 * the state of the resources taken up by obsolete VolatileImage objects.
 * <p>
 * This image should not be subclassed directly but should be created by using
 * the {@link java.awt.Component#createVolatileImage(int, int)
 * Component.createVolatileImage} or
 * {@link java.awt.GraphicsConfiguration#createCompatibleVolatileImage(int, int)
 * GraphicsConfiguration.createCompatibleVolatileImage(int, int)} methods.
 * <P>
 * An example of using a VolatileImage object follows:
 * 
 * <pre>
 * // image creation
 * VolatileImage vImg = createVolatileImage(w, h);
 *
 *
 * // rendering to the image
 * void renderOffscreen() {
 *      do {
 *          if (vImg.validate(getGraphicsConfiguration()) ==
 *              VolatileImage.IMAGE_INCOMPATIBLE)
 *          {
 *              // old vImg doesn't work with new GraphicsConfig; re-create it
 *              vImg = createVolatileImage(w, h);
 *          }
 *          Graphics2D g = vImg.createGraphics();
 *          //
 *          // miscellaneous rendering commands...
 *          //
 *          g.dispose();
 *      } while (vImg.contentsLost());
 * }
 *
 *
 * // copying from the image (here, gScreen is the Graphics
 * // object for the onscreen window)
 * do {
 *      int returnCode = vImg.validate(getGraphicsConfiguration());
 *      if (returnCode == VolatileImage.IMAGE_RESTORED) {
 *          // Contents need to be restored
 *          renderOffscreen();      // restore contents
 *      } else if (returnCode == VolatileImage.IMAGE_INCOMPATIBLE) {
 *          // old vImg doesn't work with new GraphicsConfig; re-create it
 *          vImg = createVolatileImage(w, h);
 *          renderOffscreen();
 *      }
 *      gScreen.drawImage(vImg, 0, 0, this);
 * } while (vImg.contentsLost());
 * </pre>
 * <P>
 * Note that this class subclasses from the {@link Image} class, which includes
 * methods that take an {@link ImageObserver} parameter for asynchronous
 * notifications as information is received from a potential
 * {@link ImageProducer}. Since this <code>VolatileImage</code> is not loaded
 * from an asynchronous source, the various methods that take an
 * <code>ImageObserver</code> parameter will behave as if the data has already
 * been obtained from the <code>ImageProducer</code>. Specifically, this means
 * that the return values from such methods will never indicate that the
 * information is not yet available and the <code>ImageObserver</code> used in
 * such methods will never need to be recorded for an asynchronous callback
 * notification.
 * 
 * @since 1.4
 */
public abstract class VolatileImage extends Image implements Transparency {
	public static final int IMAGE_OK = 0;
	public static final int IMAGE_RESTORED = 1;
	public static final int IMAGE_INCOMPATIBLE = 2;

	public abstract BufferedImage getSnapshot();

	public abstract int getWidth();

	public abstract int getHeight();

	public ImageProducer getSource() {
		return null;
	}

	public Graphics getGraphics() {
		return createGraphics();
	}

	public abstract Graphics2D createGraphics();

	public abstract int validate(GraphicsConfiguration gc);

	public abstract boolean contentsLost();

	public abstract ImageCapabilities getCapabilities();

	protected int transparency = TRANSLUCENT;

	public int getTransparency() {
		return transparency;
	}
}