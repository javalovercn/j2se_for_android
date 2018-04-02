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

import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;

/**
 * A menu item that can be selected or deselected. If selected, the menu item
 * typically appears with a checkmark next to it. If unselected or deselected,
 * the menu item appears without a checkmark. Like a regular menu item, a check
 * box menu item can have either text or a graphic icon associated with it, or
 * both.
 * <p>
 * Either <code>isSelected</code>/<code>setSelected</code> or
 * <code>getState</code>/<code>setState</code> can be used to determine/specify
 * the menu item's selection state. The preferred methods are
 * <code>isSelected</code> and <code>setSelected</code>, which work for all
 * menus and buttons. The <code>getState</code> and <code>setState</code>
 * methods exist for compatibility with other component sets.
 * <p>
 * Menu items can be configured, and to some degree controlled, by
 * <code><a href="Action.html">Action</a></code>s. Using an <code>Action</code>
 * with a menu item has many benefits beyond directly configuring a menu item.
 * Refer to <a href="Action.html#buttonActions"> Swing Components Supporting
 * <code>Action</code></a> for more details, and you can find more information
 * in <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/misc/action.html">How to Use
 * Actions</a>, a section in <em>The Java Tutorial</em>.
 * <p>
 * For further information and examples of using check box menu items, see
 * <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/menu.html">How to
 * Use Menus</a>, a section in <em>The Java Tutorial.</em>
 * <p>
 * <strong>Warning:</strong> Swing is not thread safe. For more information see
 * <a href="package-summary.html#threading">Swing's Threading Policy</a>.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @beaninfo attribute: isContainer false description: A menu item which can be
 *           selected or deselected.
 *
 * @author Georges Saab
 * @author David Karlton
 */
public class JCheckBoxMenuItem extends JMenuItem implements SwingConstants, Accessible {
	private static final String uiClassID = "CheckBoxMenuItemUI";

	public JCheckBoxMenuItem() {
		this(null, null, false);
	}

	public JCheckBoxMenuItem(Icon icon) {
		this(null, icon, false);
	}

	public JCheckBoxMenuItem(String text) {
		this(text, null, false);
	}

	public JCheckBoxMenuItem(Action a) {
		this();
		setAction(a);
	}

	public JCheckBoxMenuItem(String text, Icon icon) {
		this(text, icon, false);
	}

	public JCheckBoxMenuItem(String text, boolean b) {
		this(text, null, b);
	}

	public JCheckBoxMenuItem(String text, Icon icon, boolean b) {
		super(text, icon);
		setModel(new JToggleButton.ToggleButtonModel(this));
		setSelected(b);
		// setFocusable(false);
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public boolean getState() {
		return isSelected();
	}

	public synchronized void setState(boolean b) {
		setSelected(b);
	}

	public Object[] getSelectedObjects() {
		if (isSelected() == false)
			return new Object[0];
		Object[] selectedObjects = new Object[1];
		selectedObjects[0] = getText();
		return selectedObjects;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	protected String paramString() {
		return super.paramString();
	}

	boolean shouldUpdateSelectedStateFromAction() {
		return true;
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

}
