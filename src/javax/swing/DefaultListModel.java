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

import java.util.Enumeration;
import java.util.Vector;

/**
 * This class loosely implements the <code>java.util.Vector</code> API, in that
 * it implements the 1.1.x version of <code>java.util.Vector</code>, has no
 * collection class support, and notifies the <code>ListDataListener</code>s
 * when changes occur. Presently it delegates to a <code>Vector</code>, in a
 * future release it will be a real Collection implementation.
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
public class DefaultListModel<E> extends AbstractListModel<E> {
	private Vector<E> delegate = new Vector<E>();

	public int getSize() {
		return delegate.size();
	}

	public E getElementAt(int index) {
		return delegate.elementAt(index);
	}

	public void copyInto(Object anArray[]) {
		delegate.copyInto(anArray);
	}

	public void trimToSize() {
		delegate.trimToSize();
	}

	public void ensureCapacity(int minCapacity) {
		delegate.ensureCapacity(minCapacity);
	}

	public void setSize(int newSize) {
		int oldSize = delegate.size();
		delegate.setSize(newSize);
		if (oldSize > newSize) {
			fireIntervalRemoved(this, newSize, oldSize - 1);
		} else if (oldSize < newSize) {
			fireIntervalAdded(this, oldSize, newSize - 1);
		}
	}

	public int capacity() {
		return delegate.capacity();
	}

	public int size() {
		return delegate.size();
	}

	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	public Enumeration<E> elements() {
		return delegate.elements();
	}

	public boolean contains(Object elem) {
		return delegate.contains(elem);
	}

	public int indexOf(Object elem) {
		return delegate.indexOf(elem);
	}

	public int indexOf(Object elem, int beginIdx) {
		return delegate.indexOf(elem, beginIdx);
	}

	public int lastIndexOf(Object elem) {
		return delegate.lastIndexOf(elem);
	}

	public int lastIndexOf(Object elem, int index) {
		return delegate.lastIndexOf(elem, index);
	}

	public E elementAt(int index) {
		return delegate.elementAt(index);
	}

	public E firstElement() {
		return delegate.firstElement();
	}

	public E lastElement() {
		return delegate.lastElement();
	}

	public void setElementAt(E element, int index) {
		delegate.setElementAt(element, index);
		fireContentsChanged(this, index, index);
	}

	public void removeElementAt(int index) {
		delegate.removeElementAt(index);
		fireIntervalRemoved(this, index, index);
	}

	public void insertElementAt(E element, int index) {
		delegate.insertElementAt(element, index);
		fireIntervalAdded(this, index, index);
	}

	public void addElement(E element) {
		int index = delegate.size();
		delegate.addElement(element);
		fireIntervalAdded(this, index, index);
	}

	public boolean removeElement(Object obj) {
		int index = indexOf(obj);
		boolean rv = delegate.removeElement(obj);
		if (index >= 0) {
			fireIntervalRemoved(this, index, index);
		}
		return rv;
	}

	public void removeAllElements() {
		int index1 = delegate.size() - 1;
		delegate.removeAllElements();
		if (index1 >= 0) {
			fireIntervalRemoved(this, 0, index1);
		}
	}

	public String toString() {
		return delegate.toString();
	}

	public Object[] toArray() {
		Object[] rv = new Object[delegate.size()];
		delegate.copyInto(rv);
		return rv;
	}

	public E get(int index) {
		return delegate.elementAt(index);
	}

	public E set(int index, E element) {
		E rv = delegate.elementAt(index);
		delegate.setElementAt(element, index);
		fireContentsChanged(this, index, index);
		return rv;
	}

	public void add(int index, E element) {
		delegate.insertElementAt(element, index);
		fireIntervalAdded(this, index, index);
	}

	public E remove(int index) {
		E rv = delegate.elementAt(index);
		delegate.removeElementAt(index);
		fireIntervalRemoved(this, index, index);
		return rv;
	}

	public void clear() {
		int index1 = delegate.size() - 1;
		delegate.removeAllElements();
		if (index1 >= 0) {
			fireIntervalRemoved(this, 0, index1);
		}
	}

	public void removeRange(int fromIndex, int toIndex) {
		if (fromIndex > toIndex) {
			throw new IllegalArgumentException("fromIndex must be <= toIndex");
		}
		for (int i = toIndex; i >= fromIndex; i--) {
			delegate.removeElementAt(i);
		}
		fireIntervalRemoved(this, fromIndex, toIndex);
	}
}
