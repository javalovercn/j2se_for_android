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

import hc.android.AndroidClassUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Locale;
import java.util.Properties;

import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLookAndFeel;

/**
 * {@code UIManager} manages the current look and feel, the set of available
 * look and feels, {@code PropertyChangeListeners} that are notified when the
 * look and feel changes, look and feel defaults, and convenience methods for
 * obtaining various default values.
 *
 * <h3>Specifying the look and feel</h3>
 *
 * The look and feel can be specified in two distinct ways: by specifying the
 * fully qualified name of the class for the look and feel, or by creating an
 * instance of {@code LookAndFeel} and passing it to {@code setLookAndFeel}. The
 * following example illustrates setting the look and feel to the system look
 * and feel:
 * 
 * <pre>
 * UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
 * </pre>
 * 
 * The following example illustrates setting the look and feel based on class
 * name:
 * 
 * <pre>
 * UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
 * </pre>
 * 
 * Once the look and feel has been changed it is imperative to invoke
 * {@code updateUI} on all {@code JComponents}. The method
 * {@link SwingUtilities#updateComponentTreeUI} makes it easy to apply {@code
 * updateUI} to a containment hierarchy. Refer to it for details. The exact
 * behavior of not invoking {@code
 * updateUI} after changing the look and feel is unspecified. It is very
 * possible to receive unexpected exceptions, painting problems, or worse.
 *
 * <h3>Default look and feel</h3>
 *
 * The class used for the default look and feel is chosen in the following
 * manner:
 * <ol>
 * <li>If the system property <code>swing.defaultlaf</code> is {@code non-null},
 * use its value as the default look and feel class name.
 * <li>If the {@link java.util.Properties} file <code>swing.properties</code>
 * exists and contains the key <code>swing.defaultlaf</code>, use its value as
 * the default look and feel class name. The location that is checked for
 * <code>swing.properties</code> may vary depending upon the implementation of
 * the Java platform. In Sun's implementation the location is
 * <code>${java.home}/lib/swing.properties</code>. Refer to the release notes of
 * the implementation being used for further details.
 * <li>Otherwise use the cross platform look and feel.
 * </ol>
 *
 * <h3>Defaults</h3>
 *
 * {@code UIManager} manages three sets of {@code UIDefaults}. In order, they
 * are:
 * <ol>
 * <li>Developer defaults. With few exceptions Swing does not alter the
 * developer defaults; these are intended to be modified and used by the
 * developer.
 * <li>Look and feel defaults. The look and feel defaults are supplied by the
 * look and feel at the time it is installed as the current look and feel
 * ({@code setLookAndFeel()} is invoked). The look and feel defaults can be
 * obtained using the {@code
 *       getLookAndFeelDefaults()} method.
 * <li>Sytem defaults. The system defaults are provided by Swing.
 * </ol>
 * Invoking any of the various {@code get} methods results in checking each of
 * the defaults, in order, returning the first {@code non-null} value. For
 * example, invoking {@code UIManager.getString("Table.foreground")} results in
 * first checking developer defaults. If the developer defaults contain a value
 * for {@code "Table.foreground"} it is returned, otherwise the look and feel
 * defaults are checked, followed by the system defaults.
 * <p>
 * It's important to note that {@code getDefaults} returns a custom instance of
 * {@code UIDefaults} with this resolution logic built into it. For example,
 * {@code UIManager.getDefaults().getString("Table.foreground")} is equivalent
 * to {@code UIManager.getString("Table.foreground")}. Both resolve using the
 * algorithm just described. In many places the documentation uses the word
 * defaults to refer to the custom instance of {@code UIDefaults} with the
 * resolution logic as previously described.
 * <p>
 * When the look and feel is changed, {@code UIManager} alters only the look and
 * feel defaults; the developer and system defaults are not altered by the
 * {@code UIManager} in any way.
 * <p>
 * The set of defaults a particular look and feel supports is defined and
 * documented by that look and feel. In addition, each look and feel, or
 * {@code ComponentUI} provided by a look and feel, may access the defaults at
 * different times in their life cycle. Some look and feels may agressively look
 * up defaults, so that changing a default may not have an effect after
 * installing the look and feel. Other look and feels may lazily access defaults
 * so that a change to the defaults may effect an existing look and feel.
 * Finally, other look and feels might not configure themselves from the
 * defaults table in any way. None-the-less it is usually the case that a look
 * and feel expects certain defaults, so that in general a {@code ComponentUI}
 * provided by one look and feel will not work with another look and feel.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Thomas Ball
 * @author Hans Muller
 */
public class UIManager implements Serializable {

	private static final Object classLock = new Object();

	private static final String defaultLAFKey = "swing.defaultlaf";
	private static final String auxiliaryLAFsKey = "swing.auxiliarylaf";
	private static final String multiplexingLAFKey = "swing.plaf.multiplexinglaf";
	private static final String installedLAFsKey = "swing.installedlafs";
	private static final String disableMnemonicKey = "swing.disablenavaids";

	private static String makeInstalledLAFKey(String laf, String attr) {
		return "swing.installedlaf." + laf + "." + attr;
	}

	private static String makeSwingPropertiesFilename() {
		return "";
	}

	public static class LookAndFeelInfo {
		private String name;
		private String className;

		public LookAndFeelInfo(String name, String className) {
			this.name = name;
			this.className = className;
		}

		public String getName() {
			return name;
		}

		public String getClassName() {
			return className;
		}

		public String toString() {
			return getClass().getName() + "[" + getName() + " " + getClassName() + "]";
		}
	}

	private static LookAndFeelInfo[] installedLAFs;

	public static LookAndFeelInfo[] getInstalledLookAndFeels() {
		LookAndFeelInfo[] rv = new LookAndFeelInfo[0];
		return rv;
	}

	public static void setInstalledLookAndFeels(LookAndFeelInfo[] infos) throws SecurityException {
	}

	public static void installLookAndFeel(LookAndFeelInfo info) {
	}

	public static void installLookAndFeel(String name, String className) {
	}

	final static BasicLookAndFeel laf = new BasicLookAndFeel() {
		@Override
		public boolean isSupportedLookAndFeel() {
			return false;
		}

		@Override
		public boolean isNativeLookAndFeel() {
			return false;
		}

		@Override
		public String getName() {
			return "BasicLookAndFeel";
		}

		@Override
		public String getID() {
			return "";
		}

		@Override
		public String getDescription() {
			return "";
		}
	};

	public static LookAndFeel getLookAndFeel() {
		return laf;
	}

	public static void setLookAndFeel(LookAndFeel newLookAndFeel)
			throws UnsupportedLookAndFeelException {
		AndroidClassUtil.callEmptyMethod();
	}

	public static void setLookAndFeel(String className) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		AndroidClassUtil.callEmptyMethod();
	}

	public static String getSystemLookAndFeelClassName() {
		AndroidClassUtil.callEmptyMethod();
		return "";
	}

	public static String getCrossPlatformLookAndFeelClassName() {
		AndroidClassUtil.callEmptyMethod();
		return "";
	}

	final static UIDefaults uiDefaults = new UIDefaults();

	public static UIDefaults getDefaults() {
		return uiDefaults;
	}

	public static Font getFont(Object key) {
		return getDefaults().getFont(key);
	}

	public static Font getFont(Object key, Locale l) {
		return getDefaults().getFont(key, l);
	}

	public static Color getColor(Object key) {
		return getDefaults().getColor(key);
	}

	public static Color getColor(Object key, Locale l) {
		return getDefaults().getColor(key, l);
	}

	public static Icon getIcon(Object key) {
		return getDefaults().getIcon(key);
	}

	public static Icon getIcon(Object key, Locale l) {
		return getDefaults().getIcon(key, l);
	}

	public static Border getBorder(Object key) {
		return getDefaults().getBorder(key);
	}

	public static Border getBorder(Object key, Locale l) {
		return getDefaults().getBorder(key, l);
	}

	public static String getString(Object key) {
		return getDefaults().getString(key);
	}

	public static String getString(Object key, Locale l) {
		return getDefaults().getString(key, l);
	}

	static String getString(Object key, Component c) {
		Locale l = (c == null) ? Locale.getDefault() : c.getLocale();
		return getString(key, l);
	}

	public static int getInt(Object key) {
		return getDefaults().getInt(key);
	}

	public static int getInt(Object key, Locale l) {
		return getDefaults().getInt(key, l);
	}

	public static boolean getBoolean(Object key) {
		return getDefaults().getBoolean(key);
	}

	public static boolean getBoolean(Object key, Locale l) {
		return getDefaults().getBoolean(key, l);
	}

	public static Insets getInsets(Object key) {
		return getDefaults().getInsets(key);
	}

	public static Insets getInsets(Object key, Locale l) {
		return getDefaults().getInsets(key, l);
	}

	public static Dimension getDimension(Object key) {
		return getDefaults().getDimension(key);
	}

	public static Dimension getDimension(Object key, Locale l) {
		return getDefaults().getDimension(key, l);
	}

	public static Object get(Object key) {
		return getDefaults().get(key);
	}

	public static Object get(Object key, Locale l) {
		return getDefaults().get(key, l);
	}

	public static Object put(Object key, Object value) {
		return getDefaults().put(key, value);
	}

	public static ComponentUI getUI(JComponent target) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public static UIDefaults getLookAndFeelDefaults() {
		return uiDefaults;
	}

	static public void addAuxiliaryLookAndFeel(LookAndFeel laf) {
	}

	static public boolean removeAuxiliaryLookAndFeel(LookAndFeel laf) {
		return false;
	}

	static public LookAndFeel[] getAuxiliaryLookAndFeels() {
		return null;
	}

	public static void addPropertyChangeListener(PropertyChangeListener listener) {
	}

	public static void removePropertyChangeListener(PropertyChangeListener listener) {
	}

	public static PropertyChangeListener[] getPropertyChangeListeners() {
		return null;
	}

	private static Properties loadSwingProperties() {
		return new Properties();
	}

	private static void checkProperty(Properties props, String key) {
	}

	private static void initializeInstalledLAFs(Properties swingProps) {
	}

	private static void initializeDefaultLAF(Properties swingProps) {
	}

	private static void initializeAuxiliaryLAFs(Properties swingProps) {
	}

	private static void initializeSystemDefaults(Properties swingProps) {
	}

	private static void maybeInitialize() {
	}

	private static void maybeInitializeFocusPolicy(JComponent comp) {
	}

	private static void initialize() {
	}
}
