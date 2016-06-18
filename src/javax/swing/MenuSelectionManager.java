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
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A MenuSelectionManager owns the selection in menu hierarchy.
 *
 * @author Arnaud Weber
 */
public class MenuSelectionManager {
	private static MenuSelectionManager msm;

	public static MenuSelectionManager defaultManager() {
		if (msm == null) {
			msm = new MenuSelectionManager();
		}

		return msm;
	}

	protected transient ChangeEvent changeEvent = null;

	public void setSelectedPath(MenuElement[] path) {
	}

	public MenuElement[] getSelectedPath() {
		return new MenuElement[0];
	}

	public void clearSelectedPath() {
	}

	public void addChangeListener(ChangeListener l) {
	}

	public void removeChangeListener(ChangeListener l) {
	}

	public ChangeListener[] getChangeListeners() {
		return null;
	}

	protected void fireStateChanged() {
	}

	public void processMouseEvent(MouseEvent event) {
	}

	private void printMenuElementArray(MenuElement path[]) {
	}

	private void printMenuElementArray(MenuElement path[], boolean dumpStack) {
	}

	public Component componentForPoint(Component source, Point sourcePoint) {
		return null;
	}

	public void processKeyEvent(KeyEvent e) {
	}

	public boolean isComponentPartOfCurrentMenu(Component c) {
		return false;
	}

	private boolean isComponentPartOfCurrentMenu(MenuElement root, Component c) {
		return false;
	}
}