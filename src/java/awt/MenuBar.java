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

import hc.android.AndroidClassUtil;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;

/**
 * The <code>MenuBar</code> class encapsulates the platform's concept of a menu
 * bar bound to a frame. In order to associate the menu bar with a
 * <code>Frame</code> object, call the frame's <code>setMenuBar</code> method.
 * <p>
 * <A NAME="mbexample"></A><!-- target for cross references --> This is what a
 * menu bar might look like:
 * <p>
 * <img src="doc-files/MenuBar-1.gif" <alt="Diagram of MenuBar containing 2
 * menus: Examples and Options. Examples menu is expanded showing items: Basic,
 * Simple, Check, and More Examples." ALIGN=center HSPACE=10 VSPACE=7>
 * <p>
 * A menu bar handles keyboard shortcuts for menu items, passing them along to
 * its child menus. (Keyboard shortcuts, which are optional, provide the user
 * with an alternative to the mouse for invoking a menu item and the action that
 * is associated with it.) Each menu item can maintain an instance of
 * <code>MenuShortcut</code>. The <code>MenuBar</code> class defines several
 * methods, {@link MenuBar#shortcuts} and {@link MenuBar#getShortcutMenuItem}
 * that retrieve information about the shortcuts a given menu bar is managing.
 *
 * @author Sami Shaio
 * @see java.awt.Frame
 * @see java.awt.Frame#setMenuBar(java.awt.MenuBar)
 * @see java.awt.Menu
 * @see java.awt.MenuItem
 * @see java.awt.MenuShortcut
 * @since JDK1.0
 */
public class MenuBar extends MenuComponent implements MenuContainer, Accessible {
	Vector menus = new Vector();
	Menu helpMenu;

	public MenuBar() throws HeadlessException {
	}

	String constructComponentName() {
		return "";
	}

	public void addNotify() {
	}

	public void removeNotify() {
	}

	public Menu getHelpMenu() {
		return helpMenu;
	}

	public void setHelpMenu(Menu m) {
	}

	public Menu add(Menu m) {
		return null;
	}

	public void remove(int index) {
	}

	public void remove(MenuComponent m) {
	}

	public int getMenuCount() {
		return countMenus();
	}

	public int countMenus() {
		return getMenuCountImpl();
	}

	final int getMenuCountImpl() {
		return menus.size();
	}

	public Menu getMenu(int i) {
		return getMenuImpl(i);
	}

	final Menu getMenuImpl(int i) {
		return null;
	}

	public synchronized Enumeration<MenuShortcut> shortcuts() {
		return null;
	}

	public MenuItem getShortcutMenuItem(MenuShortcut s) {
		return null;
	}

	boolean handleShortcut(KeyEvent e) {
		return false;
	}

	public void deleteShortcut(MenuShortcut s) {
	}

	private void writeObject(java.io.ObjectOutputStream s)
			throws java.lang.ClassNotFoundException, java.io.IOException {
	}

	private void readObject(ObjectInputStream s)
			throws ClassNotFoundException, IOException, HeadlessException {
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

	int getAccessibleChildIndex(MenuComponent child) {
		return menus.indexOf(child);
	}

	protected class AccessibleAWTMenuBar extends AccessibleAWTMenuComponent {
		public AccessibleRole getAccessibleRole() {
			return AccessibleRole.MENU_BAR;
		}

	}

}
