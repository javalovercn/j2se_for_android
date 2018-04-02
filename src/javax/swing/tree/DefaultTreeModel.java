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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.EventListener;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/**
 * A simple tree data model that uses TreeNodes. For further information and
 * examples that use DefaultTreeModel, see <a href=
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
 * @author Rob Davis
 * @author Ray Ryan
 * @author Scott Violet
 */
public class DefaultTreeModel implements Serializable, TreeModel {
	protected TreeNode root;
	protected EventListenerList listenerList = new EventListenerList();

	protected boolean asksAllowsChildren;

	public DefaultTreeModel(TreeNode root) {
		this(root, false);
	}

	public DefaultTreeModel(TreeNode root, boolean asksAllowsChildren) {
		super();
		this.root = root;
		this.asksAllowsChildren = asksAllowsChildren;
	}

	public void setAsksAllowsChildren(boolean newValue) {
		asksAllowsChildren = newValue;
	}

	public boolean asksAllowsChildren() {
		return asksAllowsChildren;
	}

	public void setRoot(TreeNode root) {
		Object oldRoot = this.root;
		this.root = root;
		if (root == null && oldRoot != null) {
			fireTreeStructureChanged(this, null);
		} else {
			nodeStructureChanged(root);
		}
	}

	public Object getRoot() {
		return root;
	}

	public int getIndexOfChild(Object parent, Object child) {
		if (parent == null || child == null)
			return -1;
		return ((TreeNode) parent).getIndex((TreeNode) child);
	}

	public Object getChild(Object parent, int index) {
		return ((TreeNode) parent).getChildAt(index);
	}

	public int getChildCount(Object parent) {
		return ((TreeNode) parent).getChildCount();
	}

	public boolean isLeaf(Object node) {
		if (asksAllowsChildren)
			return !((TreeNode) node).getAllowsChildren();
		return ((TreeNode) node).isLeaf();
	}

	public void reload() {
		reload(root);
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
		MutableTreeNode aNode = (MutableTreeNode) path.getLastPathComponent();

		aNode.setUserObject(newValue);
		nodeChanged(aNode);
	}

	public void insertNodeInto(MutableTreeNode newChild, MutableTreeNode parent, int index) {
		parent.insert(newChild, index);

		int[] newIndexs = new int[1];

		newIndexs[0] = index;
		nodesWereInserted(parent, newIndexs);
	}

	public void removeNodeFromParent(MutableTreeNode node) {
		MutableTreeNode parent = (MutableTreeNode) node.getParent();

		if (parent == null)
			throw new IllegalArgumentException("node does not have a parent.");

		int[] childIndex = new int[1];
		Object[] removedArray = new Object[1];

		childIndex[0] = parent.getIndex(node);
		parent.remove(childIndex[0]);
		removedArray[0] = node;
		nodesWereRemoved(parent, childIndex, removedArray);
	}

	public void nodeChanged(TreeNode node) {
		if (listenerList != null && node != null) {
			TreeNode parent = node.getParent();

			if (parent != null) {
				int anIndex = parent.getIndex(node);
				if (anIndex != -1) {
					int[] cIndexs = new int[1];

					cIndexs[0] = anIndex;
					nodesChanged(parent, cIndexs);
				}
			} else if (node == getRoot()) {
				nodesChanged(node, null);
			}
		}
	}

	public void reload(TreeNode node) {
		if (node != null) {
			fireTreeStructureChanged(this, getPathToRoot(node), null, null);
		}
	}

	public void nodesWereInserted(TreeNode node, int[] childIndices) {
		if (listenerList != null && node != null && childIndices != null
				&& childIndices.length > 0) {
			int cCount = childIndices.length;
			Object[] newChildren = new Object[cCount];

			for (int counter = 0; counter < cCount; counter++)
				newChildren[counter] = node.getChildAt(childIndices[counter]);
			fireTreeNodesInserted(this, getPathToRoot(node), childIndices, newChildren);
		}
	}

	public void nodesWereRemoved(TreeNode node, int[] childIndices, Object[] removedChildren) {
		if (node != null && childIndices != null) {
			fireTreeNodesRemoved(this, getPathToRoot(node), childIndices, removedChildren);
		}
	}

	public void nodesChanged(TreeNode node, int[] childIndices) {
		if (node != null) {
			if (childIndices != null) {
				int cCount = childIndices.length;

				if (cCount > 0) {
					Object[] cChildren = new Object[cCount];

					for (int counter = 0; counter < cCount; counter++)
						cChildren[counter] = node.getChildAt(childIndices[counter]);
					fireTreeNodesChanged(this, getPathToRoot(node), childIndices, cChildren);
				}
			} else if (node == getRoot()) {
				fireTreeNodesChanged(this, getPathToRoot(node), null, null);
			}
		}
	}

	public void nodeStructureChanged(TreeNode node) {
		if (node != null) {
			fireTreeStructureChanged(this, getPathToRoot(node), null, null);
		}
	}

	public TreeNode[] getPathToRoot(TreeNode aNode) {
		return getPathToRoot(aNode, 0);
	}

	protected TreeNode[] getPathToRoot(TreeNode node, int depth) {
		TreeNode[] out;
		if (node == null) {
			if (depth == 0)
				return null;
			else
				out = new TreeNode[depth];
		} else {
			depth++;
			if (node == root)
				out = new TreeNode[depth];
			else
				out = getPathToRoot(node.getParent(), depth);
			out[out.length - depth] = node;
		}
		return out;
	}

	public void addTreeModelListener(TreeModelListener l) {
		listenerList.add(TreeModelListener.class, l);
	}

	public void removeTreeModelListener(TreeModelListener l) {
		listenerList.remove(TreeModelListener.class, l);
	}

	public TreeModelListener[] getTreeModelListeners() {
		return listenerList.getListeners(TreeModelListener.class);
	}

	protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices,
			Object[] children) {
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				if (e == null)
					e = new TreeModelEvent(source, path, childIndices, children);
				((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
			}
		}
	}

	protected void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices,
			Object[] children) {
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				if (e == null)
					e = new TreeModelEvent(source, path, childIndices, children);
				((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
			}
		}
	}

	protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices,
			Object[] children) {
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				if (e == null)
					e = new TreeModelEvent(source, path, childIndices, children);
				((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
			}
		}
	}

	protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices,
			Object[] children) {
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				if (e == null)
					e = new TreeModelEvent(source, path, childIndices, children);
				((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
			}
		}
	}

	private void fireTreeStructureChanged(Object source, TreePath path) {
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeModelListener.class) {
				if (e == null)
					e = new TreeModelEvent(source, path);
				((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
			}
		}
	}

	public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
		return listenerList.getListeners(listenerType);
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
	}

}