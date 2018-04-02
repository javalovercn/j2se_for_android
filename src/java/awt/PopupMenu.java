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

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;

/**
 * A class that implements a menu which can be dynamically popped up at a
 * specified position within a component.
 * <p>
 * As the inheritance hierarchy implies, a <code>PopupMenu</code> can be used
 * anywhere a <code>Menu</code> can be used. However, if you use a
 * <code>PopupMenu</code> like a <code>Menu</code> (e.g., you add it to a
 * <code>MenuBar</code>), then you <b>cannot</b> call <code>show</code> on that
 * <code>PopupMenu</code>.
 *
 * @author Amy Fowler
 */
public class PopupMenu extends Menu {

	private static final String base = "popup";
	static int nameCounter = 0;

	transient boolean isTrayIconPopup = false;

	public PopupMenu() throws HeadlessException {
		this("");
	}

	public PopupMenu(String label) throws HeadlessException {
		super(label);
	}

	public MenuContainer getParent() {
		if (isTrayIconPopup) {
			return null;
		}
		return super.getParent();
	}

	String constructComponentName() {
		synchronized (PopupMenu.class) {
			return base + nameCounter++;
		}
	}

	public void addNotify() {
	}

	public void show(Component origin, int x, int y) {
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

	protected class AccessibleAWTPopupMenu extends AccessibleAWTMenu {

		public AccessibleRole getAccessibleRole() {
			return AccessibleRole.POPUP_MENU;
		}

	}
}