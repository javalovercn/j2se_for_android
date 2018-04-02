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
 * A generic implementation of BoundedRangeModel.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author David Kloba
 * @author Hans Muller
 * @see BoundedRangeModel
 */
public class DefaultBoundedRangeModel implements BoundedRangeModel, Serializable {
	protected transient ChangeEvent changeEvent = null;
	protected EventListenerList listenerList = new EventListenerList();

	private int value = 0;
	private int extent = 0;
	private int min = 0;
	private int max = 100;
	private boolean isAdjusting = false;

	public DefaultBoundedRangeModel() {
	}

	public DefaultBoundedRangeModel(int value, int extent, int min, int max) {
		if ((max >= min) && (value >= min) && ((value + extent) >= value)
				&& ((value + extent) <= max)) {
			this.value = value;
			this.extent = extent;
			this.min = min;
			this.max = max;
		} else {
			throw new IllegalArgumentException("invalid range properties");
		}
	}

	public int getValue() {
		return value;
	}

	public int getExtent() {
		return extent;
	}

	public int getMinimum() {
		return min;
	}

	public int getMaximum() {
		return max;
	}

	public void setValue(int n) {
		n = Math.min(n, Integer.MAX_VALUE - extent);

		int newValue = Math.max(n, min);
		if (newValue + extent > max) {
			newValue = max - extent;
		}
		setRangeProperties(newValue, extent, min, max, isAdjusting);
	}

	public void setExtent(int n) {
		int newExtent = Math.max(0, n);
		if (value + newExtent > max) {
			newExtent = max - value;
		}
		setRangeProperties(value, newExtent, min, max, isAdjusting);
	}

	public void setMinimum(int n) {
		int newMax = Math.max(n, max);
		int newValue = Math.max(n, value);
		int newExtent = Math.min(newMax - newValue, extent);
		setRangeProperties(newValue, newExtent, n, newMax, isAdjusting);
	}

	public void setMaximum(int n) {
		int newMin = Math.min(n, min);
		int newExtent = Math.min(n - newMin, extent);
		int newValue = Math.min(n - newExtent, value);
		setRangeProperties(newValue, newExtent, newMin, n, isAdjusting);
	}

	public void setValueIsAdjusting(boolean b) {
		setRangeProperties(value, extent, min, max, b);
	}

	public boolean getValueIsAdjusting() {
		return isAdjusting;
	}

	public void setRangeProperties(int newValue, int newExtent, int newMin, int newMax,
			boolean adjusting) {
		if (newMin > newMax) {
			newMin = newMax;
		}
		if (newValue > newMax) {
			newMax = newValue;
		}
		if (newValue < newMin) {
			newMin = newValue;
		}

		if (((long) newExtent + (long) newValue) > newMax) {
			newExtent = newMax - newValue;
		}

		if (newExtent < 0) {
			newExtent = 0;
		}

		boolean isChange = (newValue != value) || (newExtent != extent) || (newMin != min)
				|| (newMax != max) || (adjusting != isAdjusting);

		if (isChange) {
			value = newValue;
			extent = newExtent;
			min = newMin;
			max = newMax;
			isAdjusting = adjusting;

			fireStateChanged();
		}
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
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);
				}
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}

	public String toString() {
		return "";
	}

	public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
		return listenerList.getListeners(listenerType);
	}
}