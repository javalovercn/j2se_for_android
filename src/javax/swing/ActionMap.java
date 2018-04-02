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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * <code>ActionMap</code> provides mappings from <code>Object</code>s (called
 * <em>keys</em> or <em><code>Action</code> names</em>) to <code>Action</code>s.
 * An <code>ActionMap</code> is usually used with an <code>InputMap</code> to
 * locate a particular action when a key is pressed. As with
 * <code>InputMap</code>, an <code>ActionMap</code> can have a parent that is
 * searched for keys not defined in the <code>ActionMap</code>.
 * <p>
 * As with <code>InputMap</code> if you create a cycle, eg:
 * 
 * <pre>
 *   ActionMap am = new ActionMap();
 *   ActionMap bm = new ActionMap():
 *   am.setParent(bm);
 *   bm.setParent(am);
 * </pre>
 * 
 * some of the methods will cause a StackOverflowError to be thrown.
 *
 * @see InputMap
 *
 * @author Scott Violet
 * @since 1.3
 */
public class ActionMap implements Serializable {
	private ActionMap parent;
	private transient HashMap<Object, Action> table;

	public ActionMap() {
		table = new HashMap<Object, Action>();
	}

	public void setParent(ActionMap map) {
		this.parent = map;
	}

	public ActionMap getParent() {
		return parent;
	}

	public void put(Object key, Action action) {
		if (key == null) {
			return;
		}
		if (action == null) {
			remove(key);
		}

		table.put(key, action);
	}

	public Action get(Object key) {
		return table.get(key);
	}

	public void remove(Object key) {
		table.remove(key);
	}

	public void clear() {
		table.clear();
	}

	public Object[] keys() {
		return table.keySet().toArray();
	}

	public int size() {
		return table.size();
	}

	public Object[] allKeys() {
		return keys();
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
	}
}