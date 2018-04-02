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

/**
 * A <code>ComponentInputMap</code> is an <code>InputMap</code> associated with
 * a particular <code>JComponent</code>. The component is automatically notified
 * whenever the <code>ComponentInputMap</code> changes.
 * <code>ComponentInputMap</code>s are used for
 * <code>WHEN_IN_FOCUSED_WINDOW</code> bindings.
 *
 * @author Scott Violet
 * @since 1.3
 */
public class ComponentInputMap extends InputMap {
	private JComponent component;

	public ComponentInputMap(JComponent component) {
		this.component = component;
		if (component == null) {
			throw new IllegalArgumentException(
					"ComponentInputMaps must be associated with a non-null JComponent");
		}
	}

	public void setParent(InputMap map) {
	}

	public JComponent getComponent() {
		return component;
	}

	public void put(KeyStroke keyStroke, Object actionMapKey) {
		super.put(keyStroke, actionMapKey);
		if (getComponent() != null) {
			getComponent().componentInputMapChanged(this);
		}
	}

	public void remove(KeyStroke key) {
		super.remove(key);
		if (getComponent() != null) {
			getComponent().componentInputMapChanged(this);
		}
	}

	public void clear() {
		int oldSize = size();
		super.clear();
		if (oldSize > 0 && getComponent() != null) {
			getComponent().componentInputMapChanged(this);
		}
	}
}
