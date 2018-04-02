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
package javax.swing.event;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;

/**
 * MenuDragMouseEvent is used to notify interested parties that the menu element
 * has received a MouseEvent forwarded to it under drag conditions.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Georges Saab
 */
public class MenuDragMouseEvent extends MouseEvent {
	private MenuElement path[];
	private MenuSelectionManager manager;

	public MenuDragMouseEvent(Component source, int id, long when, int modifiers, int x, int y,
			int clickCount, boolean popupTrigger, MenuElement p[], MenuSelectionManager m) {
		super(source, id, when, modifiers, x, y, clickCount, popupTrigger);
		path = p;
		manager = m;
	}

	public MenuDragMouseEvent(Component source, int id, long when, int modifiers, int x, int y,
			int xAbs, int yAbs, int clickCount, boolean popupTrigger, MenuElement p[],
			MenuSelectionManager m) {
		super(source, id, when, modifiers, x, y, xAbs, yAbs, clickCount, popupTrigger,
				MouseEvent.NOBUTTON);
		path = p;
		manager = m;
	}

	public MenuElement[] getPath() {
		return path;
	}

	public MenuSelectionManager getMenuSelectionManager() {
		return manager;
	}
}