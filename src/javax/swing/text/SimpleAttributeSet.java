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
package javax.swing.text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;

/**
 * A straightforward implementation of MutableAttributeSet using a hash table.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Tim Prinzing
 */
public class SimpleAttributeSet implements MutableAttributeSet, Serializable, Cloneable {
	public static final AttributeSet EMPTY = new EmptyAttributeSet();

	private transient Hashtable<Object, Object> table = new Hashtable<Object, Object>(3);

	public SimpleAttributeSet() {
	}

	public SimpleAttributeSet(AttributeSet source) {
		addAttributes(source);
	}

	private SimpleAttributeSet(Hashtable<Object, Object> table) {
		this.table = table;
	}

	public boolean isEmpty() {
		return table.isEmpty();
	}

	public int getAttributeCount() {
		return table.size();
	}

	public boolean isDefined(Object attrName) {
		return table.containsKey(attrName);
	}

	public boolean isEqual(AttributeSet attr) {
		return ((getAttributeCount() == attr.getAttributeCount()) && containsAttributes(attr));
	}

	public AttributeSet copyAttributes() {
		return (AttributeSet) clone();
	}

	public Enumeration<?> getAttributeNames() {
		return table.keys();
	}

	public Object getAttribute(Object name) {
		Object value = table.get(name);
		if (value == null) {
			AttributeSet parent = getResolveParent();
			if (parent != null) {
				value = parent.getAttribute(name);
			}
		}
		return value;
	}

	public boolean containsAttribute(Object name, Object value) {
		return value.equals(getAttribute(name));
	}

	public boolean containsAttributes(AttributeSet attributes) {
		boolean result = true;

		Enumeration names = attributes.getAttributeNames();
		while (result && names.hasMoreElements()) {
			Object name = names.nextElement();
			result = attributes.getAttribute(name).equals(getAttribute(name));
		}

		return result;
	}

	public void addAttribute(Object name, Object value) {
		table.put(name, value);
	}

	public void addAttributes(AttributeSet attributes) {
		Enumeration names = attributes.getAttributeNames();
		while (names.hasMoreElements()) {
			Object name = names.nextElement();
			addAttribute(name, attributes.getAttribute(name));
		}
	}

	public void removeAttribute(Object name) {
		table.remove(name);
	}

	public void removeAttributes(Enumeration<?> names) {
		while (names.hasMoreElements())
			removeAttribute(names.nextElement());
	}

	public void removeAttributes(AttributeSet attributes) {
		if (attributes == this) {
			table.clear();
		} else {
			Enumeration names = attributes.getAttributeNames();
			while (names.hasMoreElements()) {
				Object name = names.nextElement();
				Object value = attributes.getAttribute(name);
				if (value.equals(getAttribute(name)))
					removeAttribute(name);
			}
		}
	}

	public AttributeSet getResolveParent() {
		return (AttributeSet) table.get(StyleConstants.ResolveAttribute);
	}

	public void setResolveParent(AttributeSet parent) {
		addAttribute(StyleConstants.ResolveAttribute, parent);
	}

	public Object clone() {
		SimpleAttributeSet attr;
		attr = new SimpleAttributeSet();
		attr.table.putAll(table);
		return attr;
	}

	public int hashCode() {
		return table.hashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof AttributeSet) {
			AttributeSet attrs = (AttributeSet) obj;
			return isEqual(attrs);
		}
		return false;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		Enumeration names = getAttributeNames();
		while (names.hasMoreElements()) {
			Object key = names.nextElement();
			Object value = getAttribute(key);
			if (value instanceof AttributeSet) {
				s.append(key);
				s.append("=**AttributeSet** ");
			} else {
				s.append(key);
				s.append("=");
				s.append(value);
				s.append(" ");
			}
		}
		return s.toString();
	}

	private void writeObject(java.io.ObjectOutputStream s) throws IOException {
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
	}

	static class EmptyAttributeSet implements AttributeSet, Serializable {
		public int getAttributeCount() {
			return 0;
		}

		public boolean isDefined(Object attrName) {
			return false;
		}

		public boolean isEqual(AttributeSet attr) {
			return (attr.getAttributeCount() == 0);
		}

		public AttributeSet copyAttributes() {
			return this;
		}

		public Object getAttribute(Object key) {
			return null;
		}

		public Enumeration getAttributeNames() {
			return new Enumeration() {
				public boolean hasMoreElements() {
					return false;
				}

				public Object nextElement() {
					throw new NoSuchElementException();
				}
			};
		}

		public boolean containsAttribute(Object name, Object value) {
			return false;
		}

		public boolean containsAttributes(AttributeSet attributes) {
			return (attributes.getAttributeCount() == 0);
		}

		public AttributeSet getResolveParent() {
			return null;
		}

		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			return ((obj instanceof AttributeSet)
					&& (((AttributeSet) obj).getAttributeCount() == 0));
		}

		public int hashCode() {
			return 0;
		}
	}
}
