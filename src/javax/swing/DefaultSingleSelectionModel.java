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

import java.io.Serializable;
import java.util.EventListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * A generic implementation of SingleSelectionModel.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Dave Moore
 */
public class DefaultSingleSelectionModel implements SingleSelectionModel, Serializable {
	protected transient ChangeEvent changeEvent = null;
	protected EventListenerList listenerList = new EventListenerList();

	private int index = -1;

	public int getSelectedIndex() {
		return index;
	}

	public void setSelectedIndex(int index) {
		if (this.index != index) {
			this.index = index;
			fireStateChanged();
		}
	}

	public void clearSelection() {
		setSelectedIndex(-1);
	}

	public boolean isSelected() {
		boolean ret = false;
		if (getSelectedIndex() != -1) {
			ret = true;
		}
		return ret;
	}

	public void addChangeListener(ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}

	public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}

	public ChangeListener[] getChangeListeners() {
		return listenerList.getListeners(ChangeListener.class);
	}

	protected void fireStateChanged() {
		ChangeListener[] listeners = listenerList.getListeners(ChangeListener.class);
		if (changeEvent == null) {
			changeEvent = new ChangeEvent(this);
		}
		for (int i = listeners.length - 1; i >= 0; i--) {
			((ChangeListener) listeners[i]).stateChanged(changeEvent);
		}
	}

	public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
		return listenerList.getListeners(listenerType);
	}
}