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
package javax.swing.tree;

import java.io.Serializable;

/**
 * {@code TreePath} represents an array of objects that uniquely identify the
 * path to a node in a tree. The elements of the array are ordered with the root
 * as the first element of the array. For example, a file on the file system is
 * uniquely identified based on the array of parent directories and the name of
 * the file. The path {@code /tmp/foo/bar} could be represented by a
 * {@code TreePath} as {@code new TreePath(new Object[] {"tmp", "foo", "bar"})}.
 * <p>
 * {@code TreePath} is used extensively by {@code JTree} and related classes.
 * For example, {@code JTree} represents the selection as an array of
 * {@code TreePath}s. When used with {@code JTree}, the elements of the path are
 * the objects returned from the {@code TreeModel}. When {@code JTree} is paired
 * with {@code DefaultTreeModel}, the elements of the path are
 * {@code TreeNode}s. The following example illustrates extracting the user
 * object from the selection of a {@code JTree}:
 * 
 * <pre>
 *   DefaultMutableTreeNode root = ...;
 *   DefaultTreeModel model = new DefaultTreeModel(root);
 *   JTree tree = new JTree(model);
 *   ...
 *   TreePath selectedPath = tree.getSelectionPath();
 *   DefaultMutableTreeNode selectedNode =
 *       ((DefaultMutableTreeNode)selectedPath.getLastPathComponent()).
 *       getUserObject();
 * </pre>
 * 
 * Subclasses typically need override only {@code
 * getLastPathComponent}, and {@code getParentPath}. As {@code JTree} internally
 * creates {@code TreePath}s at various points, it's generally not useful to
 * subclass {@code TreePath} and use with {@code JTree}.
 * <p>
 * While {@code TreePath} is serializable, a {@code
 * NotSerializableException} is thrown if any elements of the path are not
 * serializable.
 * <p>
 * For further information and examples of using tree paths, see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/tree.html">How to
 * Use Trees</a> in <em>The Java Tutorial.</em>
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Scott Violet
 * @author Philip Milne
 */
public class TreePath extends Object implements Serializable {
	private TreePath parentPath;
	private Object lastPathComponent;

	public TreePath(Object[] path) {
		if (path == null || path.length == 0)
			throw new IllegalArgumentException("path in TreePath must be non null and not empty.");
		lastPathComponent = path[path.length - 1];
		if (lastPathComponent == null) {
			throw new IllegalArgumentException("Last path component must be non-null");
		}
		if (path.length > 1)
			parentPath = new TreePath(path, path.length - 1);
	}

	public TreePath(Object lastPathComponent) {
		if (lastPathComponent == null)
			throw new IllegalArgumentException("path in TreePath must be non null.");
		this.lastPathComponent = lastPathComponent;
		parentPath = null;
	}

	protected TreePath(TreePath parent, Object lastPathComponent) {
		if (lastPathComponent == null)
			throw new IllegalArgumentException("path in TreePath must be non null.");
		parentPath = parent;
		this.lastPathComponent = lastPathComponent;
	}

	protected TreePath(Object[] path, int length) {
		lastPathComponent = path[length - 1];
		if (lastPathComponent == null) {
			throw new IllegalArgumentException("Path elements must be non-null");
		}
		if (length > 1)
			parentPath = new TreePath(path, length - 1);
	}

	protected TreePath() {
	}

	public Object[] getPath() {
		int i = getPathCount();
		Object[] result = new Object[i--];

		for (TreePath path = this; path != null; path = path.getParentPath()) {
			result[i--] = path.getLastPathComponent();
		}
		return result;
	}

	public Object getLastPathComponent() {
		return lastPathComponent;
	}

	public int getPathCount() {
		int result = 0;
		for (TreePath path = this; path != null; path = path.getParentPath()) {
			result++;
		}
		return result;
	}

	public Object getPathComponent(int index) {
		int pathLength = getPathCount();

		if (index < 0 || index >= pathLength)
			throw new IllegalArgumentException("Index " + index + " is out of the specified range");

		TreePath path = this;

		for (int i = pathLength - 1; i != index; i--) {
			path = path.getParentPath();
		}
		return path.getLastPathComponent();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o instanceof TreePath) {
			TreePath oTreePath = (TreePath) o;

			if (getPathCount() != oTreePath.getPathCount())
				return false;
			for (TreePath path = this; path != null; path = path.getParentPath()) {
				if (!(path.getLastPathComponent().equals(oTreePath.getLastPathComponent()))) {
					return false;
				}
				oTreePath = oTreePath.getParentPath();
			}
			return true;
		}
		return false;
	}

	public int hashCode() {
		return getLastPathComponent().hashCode();
	}

	public boolean isDescendant(TreePath aTreePath) {
		if (aTreePath == this)
			return true;

		if (aTreePath != null) {
			int pathLength = getPathCount();
			int oPathLength = aTreePath.getPathCount();

			if (oPathLength < pathLength)
				return false;
			while (oPathLength-- > pathLength)
				aTreePath = aTreePath.getParentPath();
			return equals(aTreePath);
		}
		return false;
	}

	public TreePath pathByAddingChild(Object child) {
		if (child == null)
			throw new NullPointerException("Null child not allowed");

		return new TreePath(this, child);
	}

	public TreePath getParentPath() {
		return parentPath;
	}

	public String toString() {
		return "";
	}
}
