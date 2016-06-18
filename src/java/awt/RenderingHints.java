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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The {@code RenderingHints} class defines and manages collections of
 * keys and associated values which allow an application to provide input
 * into the choice of algorithms used by other classes which perform
 * rendering and image manipulation services.
 * The {@link java.awt.Graphics2D} class, and classes that implement
 * {@link java.awt.image.BufferedImageOp} and
 * {@link java.awt.image.RasterOp} all provide methods to get and
 * possibly to set individual or groups of {@code RenderingHints}
 * keys and their associated values.
 * When those implementations perform any rendering or image manipulation
 * operations they should examine the values of any {@code RenderingHints}
 * that were requested by the caller and tailor the algorithms used
 * accordingly and to the best of their ability.
 * <p>
 * Note that since these keys and values are <i>hints</i>, there is
 * no requirement that a given implementation supports all possible
 * choices indicated below or that it can respond to requests to
 * modify its choice of algorithm.
 * The values of the various hint keys may also interact such that
 * while all variants of a given key are supported in one situation,
 * the implementation may be more restricted when the values associated
 * with other keys are modified.
 * For example, some implementations may be able to provide several
 * types of dithering when the antialiasing hint is turned off, but
 * have little control over dithering when antialiasing is on.
 * The full set of supported keys and hints may also vary by destination
 * since runtimes may use different underlying modules to render to
 * the screen, or to {@link java.awt.image.BufferedImage} objects,
 * or while printing.
 * <p>
 * Implementations are free to ignore the hints completely, but should
 * try to use an implementation algorithm that is as close as possible
 * to the request.
 * If an implementation supports a given algorithm when any value is used
 * for an associated hint key, then minimally it must do so when the
 * value for that key is the exact value that specifies the algorithm.
 * <p>
 * The keys used to control the hints are all special values that
 * subclass the associated {@link RenderingHints.Key} class.
 * Many common hints are expressed below as static constants in this
 * class, but the list is not meant to be exhaustive.
 * Other hints may be created by other packages by defining new objects
 * which subclass the {@code Key} class and defining the associated values.
 */
public class RenderingHints implements Map<Object, Object>, Cloneable {
	public abstract static class Key {
		private int privatekey;

		public Key(int privatekey) {
			this.privatekey = privatekey;
		}
		protected final int intKey() {
			return privatekey;
		}
		@Override
		public final int hashCode() {
			return super.hashCode();
		}
		@Override
		public final boolean equals(Object o) {
			return this == o;
		}
		public boolean isCompatibleValue(Object val) {
			return true;//强制允许全部数据
		}
	}

	HashMap hintmap = new HashMap(7);

	public static final Key KEY_ANTIALIASING = new RenderingHints.Key(1) {};
	public static final Object VALUE_ANTIALIAS_ON = "ANTIALIAS_ON";
	public static final Object VALUE_ANTIALIAS_OFF = "ANTIALIAS_OFF";
	public static final Object VALUE_ANTIALIAS_DEFAULT = VALUE_ANTIALIAS_OFF;
	public static final Key KEY_RENDERING = new RenderingHints.Key(2) {};
	public static final Object VALUE_RENDER_SPEED = "RENDER_SPEED";
	public static final Object VALUE_RENDER_QUALITY = "RENDER_QUALITY";
	public static final Object VALUE_RENDER_DEFAULT = VALUE_RENDER_SPEED;
	public static final Key KEY_DITHERING = new RenderingHints.Key(3) {};
	public static final Object VALUE_DITHER_DISABLE = "DITHER_DISABLE";
	public static final Object VALUE_DITHER_ENABLE = "DITHER_ENABLE";
	public static final Object VALUE_DITHER_DEFAULT = VALUE_DITHER_DISABLE;
	public static final Key KEY_TEXT_ANTIALIASING = new RenderingHints.Key(4) {};
	public static final Object VALUE_TEXT_ANTIALIAS_ON = "TEXT_ANTIALIAS_ON";
	public static final Object VALUE_TEXT_ANTIALIAS_OFF = "TEXT_ANTIALIAS_OFF";
	public static final Object VALUE_TEXT_ANTIALIAS_DEFAULT = VALUE_TEXT_ANTIALIAS_OFF;
	public static final Object VALUE_TEXT_ANTIALIAS_GASP = "TEXT_ANTIALIAS_GASP";
	public static final Object VALUE_TEXT_ANTIALIAS_LCD_HRGB = "TEXT_ANTIALIAS_LCD_HRGB";
	public static final Object VALUE_TEXT_ANTIALIAS_LCD_HBGR = "TEXT_ANTIALIAS_LCD_HBGR";
	public static final Object VALUE_TEXT_ANTIALIAS_LCD_VRGB = "TEXT_ANTIALIAS_LCD_VRGB";
	public static final Object VALUE_TEXT_ANTIALIAS_LCD_VBGR = "TEXT_ANTIALIAS_LCD_VBGR";
	public static final Key KEY_TEXT_LCD_CONTRAST = new RenderingHints.Key(5) {};
	public static final Object VALUE_FRACTIONALMETRICS_OFF = "FRACTIONALMETRICS_OFF";
	public static final Object VALUE_FRACTIONALMETRICS_ON = "FRACTIONALMETRICS_ON";
	public static final Object VALUE_FRACTIONALMETRICS_DEFAULT = VALUE_FRACTIONALMETRICS_OFF;
	public static final Key KEY_INTERPOLATION = new RenderingHints.Key(6) {};
	public static final Object VALUE_INTERPOLATION_NEAREST_NEIGHBOR = "INTERPOLATION_NEAREST_NEIGHBOR";
	public static final Object VALUE_INTERPOLATION_BILINEAR = "INTERPOLATION_BILINEAR";
	public static final Object VALUE_INTERPOLATION_BICUBIC = "INTERPOLATION_BICUBIC";
	public static final Key KEY_ALPHA_INTERPOLATION = new RenderingHints.Key(7) {};
	public static final Object VALUE_ALPHA_INTERPOLATION_SPEED = "ALPHA_INTERPOLATION_SPEED";
	public static final Object VALUE_ALPHA_INTERPOLATION_QUALITY = "ALPHA_INTERPOLATION_QUALITY";
	public static final Object VALUE_ALPHA_INTERPOLATION_DEFAULT = VALUE_ALPHA_INTERPOLATION_SPEED;
	public static final Key KEY_COLOR_RENDERING = new RenderingHints.Key(8) {};
	public static final Object VALUE_COLOR_RENDER_SPEED = "COLOR_RENDER_SPEED";
	public static final Object VALUE_COLOR_RENDER_QUALITY = "COLOR_RENDER_QUALITY";
	public static final Object VALUE_COLOR_RENDER_DEFAULT = VALUE_COLOR_RENDER_SPEED;
	public static final Key KEY_STROKE_CONTROL = new RenderingHints.Key(9) {};
	public static final Object VALUE_STROKE_NORMALIZE = "STROKE_NORMALIZE";
	public static final Object VALUE_STROKE_PURE = "STROKE_PURE";
	public static final Object VALUE_STROKE_DEFAULT = VALUE_STROKE_NORMALIZE;
	public static final Key KEY_FRACTIONALMETRICS = new RenderingHints.Key(5) {};

	public RenderingHints(Map<Key, ?> init) {
		if (init != null) {
			hintmap.putAll(init);
		}
	}

	public RenderingHints(Key key, Object value) {
		hintmap.put(key, value);
	}

	public int size() {
		return hintmap.size();
	}

	public boolean isEmpty() {
		return hintmap.isEmpty();
	}

	public boolean containsKey(Object key) {
		return hintmap.containsKey((Key) key);
	}

	public boolean containsValue(Object value) {
		return hintmap.containsValue(value);
	}

	public Object get(Object key) {
		return hintmap.get((Key) key);
	}

	public Object put(Object key, Object value) {
		if (!((Key) key).isCompatibleValue(value)) {
			throw new IllegalArgumentException(value + " incompatible with "
					+ key);
		}
		return hintmap.put((Key) key, value);
	}

	public void add(RenderingHints hints) {
		hintmap.putAll(hints.hintmap);
	}

	public void clear() {
		hintmap.clear();
	}

	public Object remove(Object key) {
		return hintmap.remove((Key) key);
	}

	public void putAll(Map<?, ?> m) {
		if (RenderingHints.class.isInstance(m)) {
			for (Map.Entry<?, ?> entry : m.entrySet())
				hintmap.put(entry.getKey(), entry.getValue());
		} else {
			for (Map.Entry<?, ?> entry : m.entrySet())
				put(entry.getKey(), entry.getValue());
		}
	}

	public Set<Object> keySet() {
		return hintmap.keySet();
	}

	public Collection<Object> values() {
		return hintmap.values();
	}

	public Set<Map.Entry<Object, Object>> entrySet() {
		return Collections.unmodifiableMap(hintmap).entrySet();
	}

	public boolean equals(Object o) {
		if (o instanceof RenderingHints) {
			return hintmap.equals(((RenderingHints) o).hintmap);
		} else if (o instanceof Map) {
			return hintmap.equals(o);
		}
		return false;
	}

	public int hashCode() {
		return hintmap.hashCode();
	}

	public Object clone() {
		return null;
	}

	public String toString() {
		if (hintmap == null) {
			return getClass().getName() + "@" + Integer.toHexString(hashCode())
					+ " (0 hints)";
		}

		return hintmap.toString();
	}
}