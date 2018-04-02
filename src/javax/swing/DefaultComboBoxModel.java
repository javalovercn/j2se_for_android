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
import java.util.Vector;

/**
 * The default model for combo boxes.
 *
 * @param <E>
 *            the type of the elements of this model
 *
 * @author Arnaud Weber
 * @author Tom Santos
 */
public class DefaultComboBoxModel<E> extends AbstractListModel<E>
		implements MutableComboBoxModel<E>, Serializable {
	Vector<E> list;
	Object selected;

	public DefaultComboBoxModel() {
		list = new Vector<E>();
	}

	public DefaultComboBoxModel(final E items[]) {
		list = new Vector<E>(items.length);

		int i, len;
		for (i = 0, len = items.length; i < len; i++)
			list.addElement(items[i]);

		if (getSize() > 0) {
			selected = getElementAt(0);
		}
	}

	public DefaultComboBoxModel(Vector<E> v) {
		list = v;

		if (getSize() > 0) {
			selected = getElementAt(0);
		}
	}

	public void setSelectedItem(Object item) {
		if ((selected != null && !selected.equals(item)) || selected == null && item != null) {
			selected = item;
			fireContentsChanged(this, -1, -1);
		}
	}

	public Object getSelectedItem() {
		return selected;
	}

	public int getSize() {
		return list.size();
	}

	public E getElementAt(int index) {
		if (index >= 0 && index < list.size())
			return list.elementAt(index);
		else
			return null;
	}

	public int getIndexOf(Object item) {
		return list.indexOf(item);
	}

	public void addElement(E item) {
		list.addElement(item);
		fireIntervalAdded(this, list.size() - 1, list.size() - 1);
		if (list.size() == 1 && selected == null && item != null) {
			setSelectedItem(item);
		}
	}

	public void insertElementAt(E item, int index) {
		list.insertElementAt(item, index);
		fireIntervalAdded(this, index, index);
	}

	public void removeElementAt(int index) {
		if (getElementAt(index) == selected) {
			if (index == 0) {
				setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
			} else {
				setSelectedItem(getElementAt(index - 1));
			}
		}

		list.removeElementAt(index);

		fireIntervalRemoved(this, index, index);
	}

	public void removeElement(Object item) {
		int index = list.indexOf(item);
		if (index != -1) {
			removeElementAt(index);
		}
	}

	public void removeAllElements() {
		if (list.size() > 0) {
			int firstIndex = 0;
			int lastIndex = list.size() - 1;
			list.removeAllElements();
			selected = null;
			fireIntervalRemoved(this, firstIndex, lastIndex);
		} else {
			selected = null;
		}
	}
}