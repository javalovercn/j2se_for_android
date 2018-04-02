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
package java.awt;

import java.awt.image.ColorModel;

/**
 * The <code>GraphicsDevice</code> class describes the graphics devices that
 * might be available in a particular graphics environment. These include screen
 * and printer devices. Note that there can be many screens and many printers in
 * an instance of {@link GraphicsEnvironment}. Each graphics device has one or
 * more {@link GraphicsConfiguration} objects associated with it. These objects
 * specify the different configurations in which the <code>GraphicsDevice</code>
 * can be used.
 * <p>
 * In a multi-screen environment, the <code>GraphicsConfiguration</code> objects
 * can be used to render components on multiple screens. The following code
 * sample demonstrates how to create a <code>JFrame</code> object for each
 * <code>GraphicsConfiguration</code> on each screen device in the
 * <code>GraphicsEnvironment</code>:
 * 
 * <pre>
 * GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
 * GraphicsDevice[] gs = ge.getScreenDevices();
 * for (int j = 0; j < gs.length; j++) {
 * 	GraphicsDevice gd = gs[j];
 * 	GraphicsConfiguration[] gc = gd.getConfigurations();
 * 	for (int i = 0; i < gc.length; i++) {
 * 		JFrame f = new JFrame(gs[j].getDefaultConfiguration());
 * 		Canvas c = new Canvas(gc[i]);
 * 		Rectangle gcBounds = gc[i].getBounds();
 * 		int xoffs = gcBounds.x;
 * 		int yoffs = gcBounds.y;
 * 		f.getContentPane().add(c);
 * 		f.setLocation((i * 50) + xoffs, (i * 60) + yoffs);
 * 		f.show();
 * 	}
 * }
 * </pre>
 * <p>
 * For more information on full-screen exclusive mode API, see the <a href=
 * "http://java.sun.com/docs/books/tutorial/extra/fullscreen/index.html">
 * Full-Screen Exclusive Mode API Tutorial</a>.
 *
 * @see GraphicsEnvironment
 * @see GraphicsConfiguration
 */
public abstract class GraphicsDevice {

	private Window fullScreenWindow;
	private final Object fsAppContextLock = new Object();
	private Rectangle windowedModeBounds;

	protected GraphicsDevice() {
	}

	public final static int TYPE_RASTER_SCREEN = 0;
	public final static int TYPE_PRINTER = 1;
	public final static int TYPE_IMAGE_BUFFER = 2;

	public static enum WindowTranslucency {
		PERPIXEL_TRANSPARENT, TRANSLUCENT, PERPIXEL_TRANSLUCENT;
	}

	public abstract int getType();

	public abstract String getIDstring();

	public abstract GraphicsConfiguration[] getConfigurations();

	public abstract GraphicsConfiguration getDefaultConfiguration();

	public GraphicsConfiguration getBestConfiguration(GraphicsConfigTemplate gct) {
		GraphicsConfiguration[] configs = getConfigurations();
		return gct.getBestConfiguration(configs);
	}

	public boolean isFullScreenSupported() {
		return false;
	}

	public void setFullScreenWindow(Window w) {
	}

	public Window getFullScreenWindow() {
		return null;
	}

	public boolean isDisplayChangeSupported() {
		return false;
	}

	public void setDisplayMode(DisplayMode dm) {
		throw new UnsupportedOperationException("Cannot change display mode");
	}

	public DisplayMode getDisplayMode() {
		return null;
	}

	public DisplayMode[] getDisplayModes() {
		return new DisplayMode[] { getDisplayMode() };
	}

	public int getAvailableAcceleratedMemory() {
		return -1;
	}

	public boolean isWindowTranslucencySupported(WindowTranslucency translucencyKind) {
		switch (translucencyKind) {
		case PERPIXEL_TRANSPARENT:
			return isWindowShapingSupported();
		case TRANSLUCENT:
			return isWindowOpacitySupported();
		case PERPIXEL_TRANSLUCENT:
			return isWindowPerpixelTranslucencySupported();
		}
		return false;
	}

	static boolean isWindowShapingSupported() {
		return false;
	}

	static boolean isWindowOpacitySupported() {
		return false;
	}

	boolean isWindowPerpixelTranslucencySupported() {
		return false;
	}

	GraphicsConfiguration getTranslucencyCapableGC() {
		return null;
	}
}