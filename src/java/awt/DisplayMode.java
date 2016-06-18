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

/**
 * The <code>DisplayMode</code> class encapsulates the bit depth, height,
 * width, and refresh rate of a <code>GraphicsDevice</code>. The ability to
 * change graphics device's display mode is platform- and
 * configuration-dependent and may not always be available
 * (see {@link GraphicsDevice#isDisplayChangeSupported}).
 * <p>
 * For more information on full-screen exclusive mode API, see the
 * <a href="http://java.sun.com/docs/books/tutorial/extra/fullscreen/index.html">
 * Full-Screen Exclusive Mode API Tutorial</a>.
 *
 * @see GraphicsDevice
 * @see GraphicsDevice#isDisplayChangeSupported
 * @see GraphicsDevice#getDisplayModes
 * @see GraphicsDevice#setDisplayMode
 * @author Michael Martak
 * @since 1.4
 */
public final class DisplayMode {

	private Dimension size;
	private int bitDepth;
	private int refreshRate;

	public DisplayMode(int width, int height, int bitDepth, int refreshRate) {
		this.size = new Dimension(width, height);
		this.bitDepth = bitDepth;
		this.refreshRate = refreshRate;
	}

	public int getHeight() {
		return size.height;
	}

	public int getWidth() {
		return size.width;
	}

	public final static int BIT_DEPTH_MULTI = -1;

	public int getBitDepth() {
		return bitDepth;
	}

	public final static int REFRESH_RATE_UNKNOWN = 0;

	public int getRefreshRate() {
		return refreshRate;
	}

	public boolean equals(DisplayMode dm) {
		if (dm == null) {
			return false;
		}
		return (getHeight() == dm.getHeight() && getWidth() == dm.getWidth()
				&& getBitDepth() == dm.getBitDepth() && getRefreshRate() == dm
					.getRefreshRate());
	}

	public boolean equals(Object dm) {
		if (dm instanceof DisplayMode) {
			return equals((DisplayMode) dm);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return getWidth() + getHeight() + getBitDepth() * 7 + getRefreshRate()
				* 13;
	}

}