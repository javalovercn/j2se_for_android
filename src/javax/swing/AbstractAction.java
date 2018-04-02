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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.event.SwingPropertyChangeSupport;

/**
 * This class provides default implementations for the JFC <code>Action</code>
 * interface. Standard behaviors like the get and set methods for
 * <code>Action</code> object properties (icon, text, and enabled) are defined
 * here. The developer need only subclass this abstract class and define the
 * <code>actionPerformed</code> method.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Georges Saab
 * @see Action
 */
public abstract class AbstractAction implements Action, Cloneable, Serializable {
	private static Boolean RECONFIGURE_ON_NULL;
	protected boolean enabled = true;
	private transient ArrayTable arrayTable;

	static boolean shouldReconfigure(PropertyChangeEvent e) {
		return false;
	}

	static void setEnabledFromAction(JComponent c, Action a) {// 注意：被JHCComponent使用，由于不在同一包，请同步代码
		c.setEnabled((a != null) ? a.isEnabled() : true);
	}

	static void setToolTipTextFromAction(JComponent c, Action a) {// 注意：被JHCComponent使用，由于不在同一包，请同步代码
	}

	static boolean hasSelectedKey(Action a) {
		return (a != null && a.getValue(Action.SELECTED_KEY) != null);
	}

	static boolean isSelected(Action a) {
		return Boolean.TRUE.equals(a.getValue(Action.SELECTED_KEY));
	}

	public AbstractAction() {
	}

	public AbstractAction(String name) {
		putValue(Action.NAME, name);
	}

	public AbstractAction(String name, Icon icon) {
		this(name);
		putValue(Action.SMALL_ICON, icon);
	}

	public Object getValue(String key) {
		if (key.equals("enabled")) {
			return enabled;
		}
		if (arrayTable == null) {
			return null;
		}
		return arrayTable.get(key);
	}

	public void putValue(String key, Object newValue) {
		Object oldValue = null;
		if (key.equals("enabled")) {
			if (newValue == null || !(newValue instanceof Boolean)) {
				newValue = false;
			}
			oldValue = enabled;
			enabled = (Boolean) newValue;
		} else {
			if (arrayTable == null) {
				arrayTable = new ArrayTable();
			}
			if (arrayTable.containsKey(key))
				oldValue = arrayTable.get(key);
			// Remove the entry for key if newValue is null
			// else put in the newValue for key.
			if (newValue == null) {
				arrayTable.remove(key);
			} else {
				arrayTable.put(key, newValue);
			}
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean newValue) {
		boolean oldValue = this.enabled;

		if (oldValue != newValue) {
			this.enabled = newValue;
		}
	}

	public Object[] getKeys() {
		if (arrayTable == null) {
			return null;
		}
		Object[] keys = new Object[arrayTable.size()];
		arrayTable.getKeys(keys);
		return keys;
	}

	protected SwingPropertyChangeSupport changeSupport;

	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
	}

	public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
		if (changeSupport == null) {
			changeSupport = new SwingPropertyChangeSupport(this);
		}
		changeSupport.addPropertyChangeListener(listener);
	}

	public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
		if (changeSupport == null) {
			return;
		}
		changeSupport.removePropertyChangeListener(listener);
	}

	public synchronized PropertyChangeListener[] getPropertyChangeListeners() {
		if (changeSupport == null) {
			return new PropertyChangeListener[0];
		}
		return changeSupport.getPropertyChangeListeners();
	}

	protected Object clone() throws CloneNotSupportedException {
		return null;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
	}
}
