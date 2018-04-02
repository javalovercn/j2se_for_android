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

/**
 * <code>InputMap</code> provides a binding between an input event (currently
 * only <code>KeyStroke</code>s are used) and an <code>Object</code>.
 * <code>InputMap</code>s are usually used with an <code>ActionMap</code>, to
 * determine an <code>Action</code> to perform when a key is pressed. An
 * <code>InputMap</code> can have a parent that is searched for bindings not
 * defined in the <code>InputMap</code>.
 * <p>
 * As with <code>ActionMap</code> if you create a cycle, eg:
 * 
 * <pre>
 *   InputMap am = new InputMap();
 *   InputMap bm = new InputMap():
 *   am.setParent(bm);
 *   bm.setParent(am);
 * </pre>
 * 
 * some of the methods will cause a StackOverflowError to be thrown.
 *
 * @author Scott Violet
 * @since 1.3
 */
public class InputMap implements Serializable {
	private InputMap parent;

	public InputMap() {
	}

	public void setParent(InputMap map) {
		this.parent = map;
	}

	public InputMap getParent() {
		return parent;
	}

	public void put(KeyStroke keyStroke, Object actionMapKey) {
	}

	public Object get(KeyStroke keyStroke) {
		return null;
	}

	public void remove(KeyStroke key) {
	}

	public void clear() {
	}

	public KeyStroke[] keys() {
		return new KeyStroke[0];
	}

	public int size() {
		return 0;
	}

	public KeyStroke[] allKeys() {
		return new KeyStroke[0];
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
	}
}