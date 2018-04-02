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

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Private storage mechanism for Action key-value pairs. In most cases this will
 * be an array of alternating key-value pairs. As it grows larger it is scaled
 * up to a Hashtable.
 * <p>
 * This does no synchronization, if you need thread safety synchronize on
 * another object before calling this.
 *
 * @author Georges Saab
 * @author Scott Violet
 */
class ArrayTable implements Cloneable {
	private Object table = null;
	private static final int ARRAY_BOUNDARY = 8;

	static void writeArrayTable(ObjectOutputStream s, ArrayTable table) throws IOException {
	}

	public void put(Object key, Object value) {
	}

	public Object get(Object key) {
		return null;
	}

	public int size() {
		return 0;
	}

	public boolean containsKey(Object key) {
		return false;
	}

	public Object remove(Object key) {
		return null;
	}

	public void clear() {
		table = null;
	}

	public Object clone() {
		return null;
	}

	public Object[] getKeys(Object[] keys) {
		return null;
	}

	private boolean isArray() {
		return (table instanceof Object[]);
	}

	private void grow() {
	}

	private void shrink() {
	}
}