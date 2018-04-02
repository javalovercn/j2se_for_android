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

import java.util.EventObject;

import javax.swing.tree.TreePath;

/**
 * Encapsulates information describing changes to a tree model, and used to
 * notify tree model listeners of the change. For more information and examples
 * see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/events/treemodellistener.html">How
 * to Write a Tree Model Listener</a>, a section in <em>The Java Tutorial.</em>
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Rob Davis
 * @author Ray Ryan
 * @author Scott Violet
 */
public class TreeModelEvent extends EventObject {
	protected TreePath path;
	protected int[] childIndices;
	protected Object[] children;

	public TreeModelEvent(Object source, Object[] path, int[] childIndices, Object[] children) {
		this(source, new TreePath(path), childIndices, children);
	}

	public TreeModelEvent(Object source, TreePath path, int[] childIndices, Object[] children) {
		super(source);
		this.path = path;
		this.childIndices = childIndices;
		this.children = children;
	}

	public TreeModelEvent(Object source, Object[] path) {
		this(source, new TreePath(path));
	}

	public TreeModelEvent(Object source, TreePath path) {
		super(source);
		this.path = path;
		this.childIndices = new int[0];
	}

	public TreePath getTreePath() {
		return path;
	}

	public Object[] getPath() {
		if (path != null)
			return path.getPath();
		return null;
	}

	public Object[] getChildren() {
		if (children != null) {
			int cCount = children.length;
			Object[] retChildren = new Object[cCount];

			System.arraycopy(children, 0, retChildren, 0, cCount);
			return retChildren;
		}
		return null;
	}

	public int[] getChildIndices() {
		if (childIndices != null) {
			int cCount = childIndices.length;
			int[] retArray = new int[cCount];

			System.arraycopy(childIndices, 0, retArray, 0, cCount);
			return retArray;
		}
		return null;
	}

	public String toString() {
		StringBuffer retBuffer = new StringBuffer();

		retBuffer.append(getClass().getName() + " [");
		if (path != null)
			retBuffer.append(" path " + path);
		if (childIndices != null) {
			retBuffer.append(" indices [ ");
			for (int counter = 0; counter < childIndices.length; counter++)
				retBuffer.append(Integer.toString(childIndices[counter]) + " ");
			retBuffer.append("]");
		}
		if (children != null) {
			retBuffer.append(" children [ ");
			for (int counter = 0; counter < children.length; counter++)
				retBuffer.append(children[counter] + " ");
			retBuffer.append("]");
		}
		retBuffer.append("]");
		return retBuffer.toString();
	}
}