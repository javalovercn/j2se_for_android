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
 * Capabilities and properties of buffers.
 *
 * @see java.awt.image.BufferStrategy#getCapabilities()
 * @see GraphicsConfiguration#getBufferCapabilities
 * @author Michael Martak
 * @since 1.4
 */
public class BufferCapabilities implements Cloneable {
	private ImageCapabilities frontCaps;
	private ImageCapabilities backCaps;
	private FlipContents flipContents;

	public BufferCapabilities(ImageCapabilities frontCaps,
			ImageCapabilities backCaps, FlipContents flipContents) {
		this.frontCaps = frontCaps;
		this.backCaps = backCaps;
		this.flipContents = flipContents;
	}

	public ImageCapabilities getFrontBufferCapabilities() {
		return frontCaps;
	}

	public ImageCapabilities getBackBufferCapabilities() {
		return backCaps;
	}

	public boolean isPageFlipping() {
		return (getFlipContents() != null);
	}

	public FlipContents getFlipContents() {
		return flipContents;
	}

	public boolean isFullScreenRequired() {
		return false;
	}

	public boolean isMultiBufferAvailable() {
		return false;
	}

	public Object clone() {
		return null;
	}

	public static final class FlipContents extends AttributeValue {

		private static int I_UNDEFINED = 0;
		private static int I_BACKGROUND = 1;
		private static int I_PRIOR = 2;
		private static int I_COPIED = 3;

		private static final String NAMES[] = { "undefined", "background",
				"prior", "copied" };

		public static final FlipContents UNDEFINED = new FlipContents(
				I_UNDEFINED);
		public static final FlipContents BACKGROUND = new FlipContents(
				I_BACKGROUND);
		public static final FlipContents PRIOR = new FlipContents(I_PRIOR);
		public static final FlipContents COPIED = new FlipContents(I_COPIED);

		private FlipContents(int type) {
			super(type, NAMES);
		}
	}
}