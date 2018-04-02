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

import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * The abstract definition for the data model that provides a <code>List</code>
 * with its contents.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @param <E>
 *            the type of the elements of this model
 *
 * @author Hans Muller
 */
public abstract class AbstractListModel<E> implements ListModel<E>, Serializable {
	protected EventListenerList listenerList = new EventListenerList();

	public void addListDataListener(ListDataListener l) {
		listenerList.add(ListDataListener.class, l);
	}

	public void removeListDataListener(ListDataListener l) {
		listenerList.remove(ListDataListener.class, l);
	}

	public ListDataListener[] getListDataListeners() {
		return listenerList.getListeners(ListDataListener.class);
	}

	protected void fireContentsChanged(Object source, int startIdx, int endIdx) {
		fireListener(source, ListDataEvent.CONTENTS_CHANGED, startIdx, endIdx);
	}

	protected void fireIntervalAdded(Object source, int startIdx, int endIdx) {
		fireListener(source, ListDataEvent.INTERVAL_ADDED, startIdx, endIdx);
	}

	/**
	 * 
	 * @param source
	 * @param startIdx
	 *            起始index
	 * @param endIdx
	 *            收尾index
	 */
	protected void fireIntervalRemoved(Object source, int startIdx, int endIdx) {
		fireListener(source, ListDataEvent.INTERVAL_REMOVED, startIdx, endIdx);
	}

	private void fireListener(Object source, int type, int startIdx, int endIdx) {
		Object[] listeners = listenerList.getListeners(ListDataListener.class);
		ListDataEvent e = null;

		for (int i = listeners.length - 1; i >= 0; i--) {
			if (listeners[i] == ListDataListener.class) {
				if (e == null) {
					e = new ListDataEvent(source, type, startIdx, endIdx);
				}
				((ListDataListener) listeners[i]).intervalRemoved(e);
			}
		}
	}

	public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
		return listenerList.getListeners(listenerType);
	}
}