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

import hc.android.ImageUtil;
import hc.android.J2SEInitor;
import hc.android.AndroidUIUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * A table of defaults for Swing components. Applications can set/get default
 * values via the <code>UIManager</code>.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @see UIManager
 * @author Hans Muller
 */
public class UIDefaults extends Hashtable<Object, Object> {
	private static final Object PENDING = "Pending";

	private Vector<String> resourceBundles;

	private Locale defaultLocale = Locale.getDefault();

	private Map<Locale, Map<String, Object>> resourceCache;
	private final HashMap<Object, Object> keyValues = new HashMap<Object, Object>();

	public static final String FILE_VIEW_DIRECTORY_ICON = "FileView.directoryIcon";

	public static final String FILE_VIEW_FILE_ICON = "FileView.fileIcon";

	public UIDefaults() {
		this(700, .75f);
	}

	public UIDefaults(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		resourceCache = new HashMap<Locale, Map<String, Object>>();
	}

	public UIDefaults(Object[] keyValueList) {
		super(keyValueList.length / 2);
		for (int i = 0; i < keyValueList.length; i += 2) {
			super.put(keyValueList[i], keyValueList[i + 1]);
		}
	}

	public Object get(Object key) {
		Object value = getFromHashtable(key);
		return (value != null) ? value : getFromResourceBundle(key, null);
	}

	private Object getFromHashtable(Object key) {
		if (key.equals(UIDefaults.FILE_VIEW_DIRECTORY_ICON)) {
			return getFileIcon("/hc/android/res/folder_64.png", key);
		} else if (key.equals(UIDefaults.FILE_VIEW_FILE_ICON)) {
			return getFileIcon("/hc/android/res/file_64.png", key);
		} else if (key.equals("OptionPane.errorIcon")) {
			return getSysIcon(AndroidUIUtil.DRAW_ERROR_ID, key);
		} else if (key.equals("OptionPane.informationIcon")) {
			return getSysIcon(AndroidUIUtil.DRAW_INFO_ID, key);
		} else if (key.equals("OptionPane.questionIcon")) {
			return getSysIcon(AndroidUIUtil.DRAW_HELP_ID, key);
		} else if (key.equals("OptionPane.warningIcon")) {
			return getSysIcon(AndroidUIUtil.DRAW_WARNING_ID, key);
		}
		return null;
	}

	private Object getSysIcon(int res_id, Object key) {
		return new ImageIcon(new BufferedImage(AndroidUIUtil.getSystemDefaultDrawable(res_id)), "");
		// Object out = keyValues.get(key);
		// if(out == null){
		// out = new ImageIcon(new
		// BufferedImage(UIUtil.getSystemDefaultDrawable(res_id)), "");
		// keyValues.put(key, out);
		// }
		// return out;
	}

	private Object getFileIcon(String fileName, Object key) {
		Object out = keyValues.get(key);
		if (out == null) {
			InputStream is = J2SEInitor.class.getResourceAsStream(fileName);
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			BufferedImage bi = new BufferedImage(bitmap);
			bi.initZoom = 0.375F;// 缺省zoom= 0.25，即16。 0.375F,即24
			out = new ImageIcon(bi);
			keyValues.put(key, out);
		}
		return out;
	}

	public Object get(Object key, Locale l) {
		Object value = getFromHashtable(key);
		return (value != null) ? value : getFromResourceBundle(key, l);
	}

	private Object getFromResourceBundle(Object key, Locale l) {
		if (resourceBundles == null || resourceBundles.isEmpty() || !(key instanceof String)) {
			return null;
		}

		if (l == null) {
			if (defaultLocale == null)
				return null;
			else
				l = defaultLocale;
		}

		synchronized (this) {
			return getResourceCache(l).get(key);
		}
	}

	private Map<String, Object> getResourceCache(Locale l) {
		Map<String, Object> values = resourceCache.get(l);
		return values;
	}

	public Object put(Object key, Object value) {
		Object oldValue = (value == null) ? super.remove(key) : super.put(key, value);
		if (key instanceof String) {
			firePropertyChange((String) key, oldValue, value);
		}
		return oldValue;
	}

	public void putDefaults(Object[] keyValueList) {
	}

	public Font getFont(Object key) {
		Object value = get(key);
		return (value instanceof Font) ? (Font) value : null;
	}

	public Font getFont(Object key, Locale l) {
		Object value = get(key, l);
		return (value instanceof Font) ? (Font) value : null;
	}

	public Color getColor(Object key) {
		Object value = get(key);
		return (value instanceof Color) ? (Color) value : null;
	}

	public Color getColor(Object key, Locale l) {
		Object value = get(key, l);
		return (value instanceof Color) ? (Color) value : null;
	}

	public Icon getIcon(Object key) {
		Object value = get(key);
		return (value instanceof Icon) ? (Icon) value : null;
	}

	public Icon getIcon(Object key, Locale l) {
		Object value = get(key, l);
		return (value instanceof Icon) ? (Icon) value : null;
	}

	public Border getBorder(Object key) {
		Object value = get(key);
		return (value instanceof Border) ? (Border) value : null;
	}

	public Border getBorder(Object key, Locale l) {
		Object value = get(key, l);
		return (value instanceof Border) ? (Border) value : null;
	}

	public String getString(Object key) {
		Object value = get(key);
		return (value instanceof String) ? (String) value : null;
	}

	public String getString(Object key, Locale l) {
		Object value = get(key, l);
		return (value instanceof String) ? (String) value : null;
	}

	public int getInt(Object key) {
		Object value = get(key);
		return (value instanceof Integer) ? ((Integer) value).intValue() : 0;
	}

	public int getInt(Object key, Locale l) {
		Object value = get(key, l);
		return (value instanceof Integer) ? ((Integer) value).intValue() : 0;
	}

	public boolean getBoolean(Object key) {
		Object value = get(key);
		return (value instanceof Boolean) ? ((Boolean) value).booleanValue() : false;
	}

	public boolean getBoolean(Object key, Locale l) {
		Object value = get(key, l);
		return (value instanceof Boolean) ? ((Boolean) value).booleanValue() : false;
	}

	public Insets getInsets(Object key) {
		Object value = get(key);
		return (value instanceof Insets) ? (Insets) value : null;
	}

	public Insets getInsets(Object key, Locale l) {
		Object value = get(key, l);
		return (value instanceof Insets) ? (Insets) value : null;
	}

	public Dimension getDimension(Object key) {
		Object value = get(key);
		return (value instanceof Dimension) ? (Dimension) value : null;
	}

	public Dimension getDimension(Object key, Locale l) {
		Object value = get(key, l);
		return (value instanceof Dimension) ? (Dimension) value : null;
	}

	public Class<? extends ComponentUI> getUIClass(String uiClassID, ClassLoader uiClassLoader) {
		return null;
	}

	public Class<? extends ComponentUI> getUIClass(String uiClassID) {
		return getUIClass(uiClassID, null);
	}

	protected void getUIError(String msg) {
		System.err.println("UIDefaults.getUI() failed: " + msg);
		try {
			throw new Error();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public ComponentUI getUI(JComponent target) {
		return null;
	}

	public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
	}

	public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
	}

	public synchronized PropertyChangeListener[] getPropertyChangeListeners() {
		return new PropertyChangeListener[0];
	}

	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
	}

	public synchronized void addResourceBundle(String bundleName) {
	}

	public synchronized void removeResourceBundle(String bundleName) {
	}

	public void setDefaultLocale(Locale l) {
		defaultLocale = l;
	}

	public Locale getDefaultLocale() {
		return defaultLocale;
	}

	public interface LazyValue {
		Object createValue(UIDefaults table);
	}

	public interface ActiveValue {
		Object createValue(UIDefaults table);
	}

	public static class ProxyLazyValue implements LazyValue {
		private AccessControlContext acc;
		private String className;
		private String methodName;
		private Object[] args;

		public ProxyLazyValue(String c) {
			this(c, (String) null);
		}

		public ProxyLazyValue(String c, String m) {
			this(c, m, null);
		}

		public ProxyLazyValue(String c, Object[] o) {
			this(c, null, o);
		}

		public ProxyLazyValue(String c, String m, Object[] o) {
			acc = AccessController.getContext();
			className = c;
			methodName = m;
			if (o != null) {
				args = o.clone();
			}
		}

		public Object createValue(final UIDefaults table) {
			return null;
		}

		private Class[] getClassArray(Object[] args) {
			return null;
		}

		private String printArgs(Object[] array) {
			return "";
		}
	}

	public static class LazyInputMap implements LazyValue {
		public LazyInputMap(Object[] bindings) {
		}

		public Object createValue(UIDefaults table) {
			return null;
		}
	}

	private static class TextAndMnemonicHashMap extends HashMap<String, Object> {
		@Override
		public Object get(Object key) {
			return null;
		}

		String composeKey(String key, int reduce, String sufix) {
			return key.substring(0, key.length() - reduce) + sufix;
		}

		String getTextFromProperty(String text) {
			return text.replace("&", "");
		}

		String getMnemonicFromProperty(String text) {
			int index = text.indexOf('&');
			if (0 <= index && index < text.length() - 1) {
				char c = text.charAt(index + 1);
				return Integer.toString((int) Character.toUpperCase(c));
			}
			return null;
		}

		String getIndexFromProperty(String text) {
			int index = text.indexOf('&');
			return (index == -1) ? null : Integer.toString(index);
		}
	}
}