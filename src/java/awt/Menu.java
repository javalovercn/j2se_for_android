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
 * A <code>Menu</code> object is a pull-down menu component that is deployed
 * from a menu bar.
 * <p>
 * A menu can optionally be a <i>tear-off</i> menu. A tear-off menu can be
 * opened and dragged away from its parent menu bar or menu. It remains on the
 * screen after the mouse button has been released. The mechanism for tearing
 * off a menu is platform dependent, since the look and feel of the tear-off
 * menu is determined by its peer. On platforms that do not support tear-off
 * menus, the tear-off property is ignored.
 * <p>
 * Each item in a menu must belong to the <code>MenuItem</code> class. It can be
 * an instance of <code>MenuItem</code>, a submenu (an instance of
 * <code>Menu</code>), or a check box (an instance of
 * <code>CheckboxMenuItem</code>).
 *
 * @author Sami Shaio
 * @see java.awt.MenuItem
 * @see java.awt.CheckboxMenuItem
 * @since JDK1.0
 */
public class Menu extends MenuItem implements MenuContainer, Accessible {
	Vector items = new Vector();
	boolean tearOff;
	boolean isHelpMenu;

	public Menu() throws HeadlessException {
		this("", false);
	}

	public Menu(String label) throws HeadlessException {
		this(label, false);
	}

	public Menu(String label, boolean tearOff) throws HeadlessException {
		super(label);
		this.tearOff = tearOff;
	}

	String constructComponentName() {
		return "";
	}

	public void addNotify() {
	}

	public void removeNotify() {
	}

	public boolean isTearOff() {
		return tearOff;
	}

	public int getItemCount() {
		return countItems();
	}

	public int countItems() {
		return countItemsImpl();
	}

	final int countItemsImpl() {
		return items.size();
	}

	public MenuItem getItem(int index) {
		return getItemImpl(index);
	}

	final MenuItem getItemImpl(int index) {
		return (MenuItem) items.elementAt(index);
	}

	public MenuItem add(MenuItem mi) {
		return null;
	}

	public void add(String label) {
		add(new MenuItem(label));
	}

	public void insert(MenuItem menuitem, int index) {
	}

	public void insert(String label, int index) {
		insert(new MenuItem(label), index);
	}

	public void addSeparator() {
		add("-");
	}

	public void insertSeparator(int index) {
	}

	public void remove(int index) {
	}

	public void remove(MenuComponent item) {
		int index = items.indexOf(item);
		if (index >= 0) {
			remove(index);
		}
	}

	public void removeAll() {
		int nitems = getItemCount();
		for (int i = nitems - 1; i >= 0; i--) {
			remove(i);
		}
	}

	boolean handleShortcut(KeyEvent e) {
		return false;
	}

	MenuItem getShortcutMenuItem(MenuShortcut s) {
		return null;
	}

	synchronized Enumeration shortcuts() {
		return null;
	}

	void deleteShortcut(MenuShortcut s) {
	}

	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
	}

	private void readObject(ObjectInputStream s)
			throws IOException, ClassNotFoundException, HeadlessException {
	}

	public String paramString() {
		String str = ",tearOff=" + tearOff + ",isHelpMenu=" + isHelpMenu;
		return super.paramString() + str;
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

	int getAccessibleChildIndex(MenuComponent child) {
		return items.indexOf(child);
	}

	protected class AccessibleAWTMenu extends AccessibleAWTMenuItem {
		public AccessibleRole getAccessibleRole() {
			return AccessibleRole.MENU;
		}

	}
}
