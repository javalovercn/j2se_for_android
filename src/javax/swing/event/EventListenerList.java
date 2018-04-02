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

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.EventListener;

/**
 * A class that holds a list of EventListeners. A single instance can be used to
 * hold all listeners (of all types) for the instance using the list. It is the
 * responsiblity of the class using the EventListenerList to provide type-safe
 * API (preferably conforming to the JavaBeans spec) and methods which dispatch
 * event notification methods to appropriate Event Listeners on the list.
 *
 * The main benefits that this class provides are that it is relatively cheap in
 * the case of no listeners, and it provides serialization for event-listener
 * lists in a single place, as well as a degree of MT safety (when used
 * correctly).
 *
 * Usage example: Say one is defining a class that sends out FooEvents, and one
 * wants to allow users of the class to register FooListeners and receive
 * notification when FooEvents occur. The following should be added to the class
 * definition:
 * 
 * <pre>
 * EventListenerList listenerList = new EventListenerList();
 * FooEvent fooEvent = null;
 *
 * public void addFooListener(FooListener l) {
 * 	listenerList.add(FooListener.class, l);
 * }
 *
 * public void removeFooListener(FooListener l) {
 * 	listenerList.remove(FooListener.class, l);
 * }
 *
 * // Notify all listeners that have registered interest for
 * // notification on this event type. The event instance
 * // is lazily created using the parameters passed into
 * // the fire method.
 *
 * protected void fireFooXXX() {
 * 	// Guaranteed to return a non-null array
 * 	Object[] listeners = listenerList.getListenerList();
 * 	// Process the listeners last to first, notifying
 * 	// those that are interested in this event
 * 	for (int i = listeners.length - 2; i >= 0; i -= 2) {
 * 		if (listeners[i] == FooListener.class) {
 * 			// Lazily create the event:
 * 			if (fooEvent == null)
 * 				fooEvent = new FooEvent(this);
 * 			((FooListener) listeners[i + 1]).fooXXX(fooEvent);
 * 		}
 * 	}
 * }
 * </pre>
 * 
 * foo should be changed to the appropriate name, and fireFooXxx to the
 * appropriate method name. One fire method should exist for each notification
 * method in the FooListener interface.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Georges Saab
 * @author Hans Muller
 * @author James Gosling
 */
public class EventListenerList implements Serializable {
	private final static Object[] NULL_ARRAY = new Object[0];
	protected transient Object[] listenerList = NULL_ARRAY;

	public Object[] getListenerList() {
		return listenerList;
	}

	/**
	 * 如果没有，则返回长度0
	 * 
	 * @param t
	 * @return
	 */
	public <T extends EventListener> T[] getListeners(Class<T> t) {
		Object[] lList = listenerList;
		int n = getListenerCount(lList, t);
		T[] result = (T[]) Array.newInstance(t, n);
		int j = 0;
		for (int i = lList.length - 2; i >= 0; i -= 2) {
			if (lList[i] == t) {
				result[j++] = (T) lList[i + 1];
			}
		}
		return result;
	}

	public int getListenerCount() {
		return listenerList.length / 2;
	}

	public int getListenerCount(Class<?> t) {
		Object[] lList = listenerList;
		return getListenerCount(lList, t);
	}

	private int getListenerCount(Object[] list, Class t) {
		int count = 0;
		for (int i = 0; i < list.length; i += 2) {
			if (t == (Class) list[i])
				count++;
		}
		return count;
	}

	public synchronized <T extends EventListener> void add(Class<T> t, T l) {
		if (l == null) {
			return;
		}
		if (!t.isInstance(l)) {
			throw new IllegalArgumentException("Listener " + l + " is not of type " + t);
		}
		if (listenerList == NULL_ARRAY) {
			listenerList = new Object[] { t, l };
		} else {
			int i = listenerList.length;
			Object[] tmp = new Object[i * 2];
			System.arraycopy(listenerList, 0, tmp, 0, i);

			tmp[i] = t;
			tmp[i + 1] = l;

			listenerList = tmp;
		}
	}

	public synchronized <T extends EventListener> void remove(Class<T> t, T l) {
		if (l == null) {
			return;
		}
		if (!t.isInstance(l)) {
			throw new IllegalArgumentException("Listener " + l + " is not of type " + t);
		}
		int index = -1;
		for (int i = listenerList.length - 2; i >= 0; i -= 2) {
			if ((listenerList[i] == t) && (listenerList[i + 1].equals(l) == true)) {
				index = i;
				break;
			}
		}

		if (index != -1) {
			Object[] tmp = new Object[listenerList.length - 2];
			System.arraycopy(listenerList, 0, tmp, 0, index);
			if (index < tmp.length)
				System.arraycopy(listenerList, index + 2, tmp, index, tmp.length - index);
			listenerList = (tmp.length == 0) ? NULL_ARRAY : tmp;
		}
	}

	public String toString() {
		return "";
	}
}
