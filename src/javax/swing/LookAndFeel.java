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

import java.awt.Component;

import javax.swing.text.JTextComponent;

/**
 * {@code LookAndFeel}, as the name implies, encapsulates a look and feel.
 * Beyond installing a look and feel most developers never need to interact
 * directly with {@code LookAndFeel}. In general only developers creating a
 * custom look and feel need to concern themselves with this class.
 * <p>
 * Swing is built upon the foundation that each {@code JComponent} subclass has
 * an implementation of a specific {@code ComponentUI} subclass. The
 * {@code ComponentUI} is often referred to as "the ui", "component ui", or
 * "look and feel delegate". The {@code ComponentUI} subclass is responsible for
 * providing the look and feel specific functionality of the component. For
 * example, {@code JTree} requires an implementation of the {@code ComponentUI}
 * subclass {@code
 * TreeUI}. The implementation of the specific {@code
 * ComponentUI} subclass is provided by the {@code LookAndFeel}. Each
 * {@code JComponent} subclass identifies the {@code ComponentUI} subclass it
 * requires by way of the {@code JComponent} method {@code
 * getUIClassID}.
 * <p>
 * Each {@code LookAndFeel} implementation must provide an implementation of the
 * appropriate {@code ComponentUI} subclass by specifying a value for each of
 * Swing's ui class ids in the {@code
 * UIDefaults} object returned from {@code getDefaults}. For example,
 * {@code BasicLookAndFeel} uses {@code BasicTreeUI} as the concrete
 * implementation for {@code TreeUI}. This is accomplished by {@code
 * BasicLookAndFeel} providing the key-value pair {@code
 * "TreeUI"-"javax.swing.plaf.basic.BasicTreeUI"}, in the {@code UIDefaults}
 * returned from {@code getDefaults}. Refer to
 * {@link UIDefaults#getUI(JComponent)} for defails on how the implementation of
 * the {@code ComponentUI} subclass is obtained.
 * <p>
 * When a {@code LookAndFeel} is installed the {@code UIManager} does not check
 * that an entry exists for all ui class ids. As such, random exceptions will
 * occur if the current look and feel has not provided a value for a particular
 * ui class id and an instance of the {@code JComponent} subclass is created.
 *
 * <h2>Recommendations for Look and Feels</h2>
 *
 * As noted in {@code UIManager} each {@code LookAndFeel} has the opportunity to
 * provide a set of defaults that are layered in with developer and system
 * defaults. Some of Swing's components require the look and feel to provide a
 * specific set of defaults. These are documented in the classes that require
 * the specific default.
 *
 * <h3><a name="#defaultRecommendation">ComponentUIs and defaults</a></h2>
 *
 * All {@code ComponentUIs} typically need to set various properties on the
 * {@code JComponent} the {@code ComponentUI} is providing the look and feel
 * for. This is typically done when the {@code
 * ComponentUI} is installed on the {@code JComponent}. Setting a property
 * should only be done if the developer has not set the property. For
 * non-primitive values it is recommended that the {@code ComponentUI} only
 * change the property on the {@code
 * JComponent} if the current value is {@code null} or implements
 * {@code UIResource}. If the current value is {@code null} or implements
 * {@code UIResource} it indicates the property has not been set by the
 * developer, and the ui is free to change it. For example,
 * {@code BasicButtonUI.installDefaults} only changes the font on the
 * {@code JButton} if the return value from {@code
 * button.getFont()} is {@code null} or implements {@code
 * UIResource}. On the other hand if {@code button.getFont()} returned a
 * {@code non-null} value that did not implement {@code UIResource} then
 * {@code BasicButtonUI.installDefaults} would not change the {@code JButton}'s
 * font.
 * <p>
 * For primitive values, such as {@code opaque}, the method {@code
 * installProperty} should be invoked. {@code installProperty} only changes the
 * correspoding property if the value has not been changed by the developer.
 * <p>
 * {@code ComponentUI} implementations should use the various install methods
 * provided by this class as they handle the necessary checking and install the
 * property using the recommended guidelines.
 * <p>
 * <h3><a name="exceptions"></a>Exceptions</h3>
 *
 * All of the install methods provided by {@code LookAndFeel} need to access the
 * defaults if the value of the property being changed is {@code null} or a
 * {@code UIResource}. For example, installing the font does the following:
 * 
 * <pre>
 * JComponent c;
 * Font font = c.getFont();
 * if (font == null || (font instanceof UIResource)) {
 * 	c.setFont(UIManager.getFont("fontKey"));
 * }
 * </pre>
 * 
 * If the font is {@code null} or a {@code UIResource}, the defaults table is
 * queried with the key {@code fontKey}. All of {@code UIDefault's} get methods
 * throw a {@code
 * NullPointerException} if passed in {@code null}. As such, unless otherwise
 * noted each of the various install methods of {@code
 * LookAndFeel} throw a {@code NullPointerException} if the current value is
 * {@code null} or a {@code UIResource} and the supplied defaults key is
 * {@code null}. In addition, unless otherwise specified all of the
 * {@code install} methods throw a {@code NullPointerException} if a
 * {@code null} component is passed in.
 *
 * @author Tom Ball
 * @author Hans Muller
 */
public abstract class LookAndFeel {

	public static void installColors(JComponent c, String defaultBgName, String defaultFgName) {
	}

	public static void installColorsAndFont(JComponent c, String defaultBgName,
			String defaultFgName, String defaultFontName) {
	}

	public static void installBorder(JComponent c, String defaultBorderName) {
	}

	public static void uninstallBorder(JComponent c) {
	}

	public static void installProperty(JComponent c, String propertyName, Object propertyValue) {
	}

	public static JTextComponent.KeyBinding[] makeKeyBindings(Object[] keyBindingList) {
		JTextComponent.KeyBinding[] rv = new JTextComponent.KeyBinding[0];

		return rv;
	}

	public static InputMap makeInputMap(Object[] keys) {
		return null;
	}

	public static ComponentInputMap makeComponentInputMap(JComponent c, Object[] keys) {
		return null;
	}

	public static void loadKeyBindings(InputMap retMap, Object[] keys) {
	}

	public static Object makeIcon(final Class<?> baseClass, final String gifFile) {
		return null;
	}

	public LayoutStyle getLayoutStyle() {
		return null;
	}

	public void provideErrorFeedback(Component component) {
	}

	public static Object getDesktopPropertyValue(String systemPropertyName, Object fallbackValue) {
		return null;
	}

	public Icon getDisabledIcon(JComponent component, Icon icon) {
		return null;
	}

	public Icon getDisabledSelectedIcon(JComponent component, Icon icon) {
		return getDisabledIcon(component, icon);
	}

	public abstract String getName();

	public abstract String getID();

	public abstract String getDescription();

	public boolean getSupportsWindowDecorations() {
		return false;
	}

	public abstract boolean isNativeLookAndFeel();

	public abstract boolean isSupportedLookAndFeel();

	public void initialize() {
	}

	public void uninitialize() {
	}

	public UIDefaults getDefaults() {
		return null;
	}

	public String toString() {
		return "[" + getDescription() + " - " + getClass().getName() + "]";
	}
}
