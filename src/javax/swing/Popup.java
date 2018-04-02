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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;

/**
 * Popups are used to display a <code>Component</code> to the user, typically on
 * top of all the other <code>Component</code>s in a particular containment
 * hierarchy. <code>Popup</code>s have a very small life cycle. Once you have
 * obtained a <code>Popup</code>, and hidden it (invoked the <code>hide</code>
 * method), you should no longer invoke any methods on it. This allows the
 * <code>PopupFactory</code> to cache <code>Popup</code>s for later use.
 * <p>
 * The general contract is that if you need to change the size of the
 * <code>Component</code>, or location of the <code>Popup</code>, you should
 * obtain a new <code>Popup</code>.
 * <p>
 * <code>Popup</code> does not descend from <code>Component</code>, rather
 * implementations of <code>Popup</code> are responsible for creating and
 * maintaining their own <code>Component</code>s to render the requested
 * <code>Component</code> to the user.
 * <p>
 * You typically do not explicitly create an instance of <code>Popup</code>,
 * instead obtain one from a <code>PopupFactory</code>.
 *
 * @see PopupFactory
 *
 * @since 1.4
 */
public class Popup {
	private Component component;

	protected Popup(Component owner, Component contents, int x, int y) {
		this();
		if (contents == null) {
			throw new IllegalArgumentException("Contents must be non-null");
		}
		reset(owner, contents, x, y);
	}

	protected Popup() {
	}

	public void show() {
		Component component = getComponent();

		if (component != null) {
			component.show();
		}
	}

	public void hide() {
		Component component = getComponent();

		dispose();
	}

	void dispose() {
		AndroidClassUtil.callEmptyMethod();
	}

	void reset(Component owner, Component contents, int ownerX, int ownerY) {
		if (getComponent() == null) {
			component = createComponent(owner);
		}

		Component c = getComponent();

		if (c instanceof JWindow) {
			JWindow component = (JWindow) getComponent();

			component.setLocation(ownerX, ownerY);
			component.getContentPane().add(contents, BorderLayout.CENTER);
			component.invalidate();
			component.validate();
			if (component.isVisible()) {
				pack();
			}
		}
	}

	void pack() {
		Component component = getComponent();

		if (component instanceof Window) {
			((Window) component).pack();
		}
	}

	private Window getParentWindow(Component owner) {
		Window window = null;

		if (owner instanceof Window) {
			window = (Window) owner;
		} else if (owner != null) {
			window = SwingUtilities.getWindowAncestor(owner);
		}
		if (window == null) {
			window = new DefaultFrame();
		}
		return window;
	}

	Component createComponent(Component owner) {
		return null;
	}

	Component getComponent() {
		return component;
	}

	static class DefaultFrame extends Frame {
	}
}