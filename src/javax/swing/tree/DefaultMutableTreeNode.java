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

import hc.android.AndroidClassUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Vector;

/**
 * A <code>DefaultMutableTreeNode</code> is a general-purpose node in a tree
 * data structure. For examples of using default mutable tree nodes, see
 * <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/tree.html">How to
 * Use Trees</a> in <em>The Java Tutorial.</em>
 *
 * <p>
 *
 * A tree node may have at most one parent and 0 or more children.
 * <code>DefaultMutableTreeNode</code> provides operations for examining and
 * modifying a node's parent and children and also operations for examining the
 * tree that the node is a part of. A node's tree is the set of all nodes that
 * can be reached by starting at the node and following all the possible links
 * to parents and children. A node with no parent is the root of its tree; a
 * node with no children is a leaf. A tree may consist of many subtrees, each
 * node acting as the root for its own subtree.
 * <p>
 * This class provides enumerations for efficiently traversing a tree or subtree
 * in various orders or for following the path between two nodes. A
 * <code>DefaultMutableTreeNode</code> may also hold a reference to a user
 * object, the use of which is left to the user. Asking a
 * <code>DefaultMutableTreeNode</code> for its string representation with
 * <code>toString()</code> returns the string representation of its user object.
 * <p>
 * <b>This is not a thread safe class.</b>If you intend to use a
 * DefaultMutableTreeNode (or a tree of TreeNodes) in more than one thread, you
 * need to do your own synchronizing. A good convention to adopt is
 * synchronizing on the root node of a tree.
 * <p>
 * While DefaultMutableTreeNode implements the MutableTreeNode interface and
 * will allow you to add in any implementation of MutableTreeNode not all of the
 * methods in DefaultMutableTreeNode will be applicable to all MutableTreeNodes
 * implementations. Especially with some of the enumerations that are provided,
 * using some of these methods assumes the DefaultMutableTreeNode contains only
 * DefaultMutableNode instances. All of the TreeNode/MutableTreeNode methods
 * will behave as defined no matter what implementations are added.
 *
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @see MutableTreeNode
 *
 * @author Rob Davis
 */
public class DefaultMutableTreeNode implements Cloneable, MutableTreeNode, Serializable {
	static public final Enumeration<TreeNode> EMPTY_ENUMERATION = new Enumeration<TreeNode>() {
		public boolean hasMoreElements() {
			return false;
		}

		public TreeNode nextElement() {
			throw new NoSuchElementException();
		}
	};
	protected MutableTreeNode parent;
	protected Vector children;
	transient protected Object userObject;
	protected boolean allowsChildren;

	public DefaultMutableTreeNode() {
		this(null);
	}

	public DefaultMutableTreeNode(Object userObject) {
		this(userObject, true);
	}

	public DefaultMutableTreeNode(Object userObject, boolean allowsChildren) {
		super();
		parent = null;
		this.allowsChildren = allowsChildren;
		this.userObject = userObject;
	}

	public void insert(MutableTreeNode newChild, int childIndex) {
		if (!allowsChildren) {
			throw new IllegalStateException("node does not allow children");
		} else if (newChild == null) {
			throw new IllegalArgumentException("new child is null");
		} else if (isNodeAncestor(newChild)) {
			throw new IllegalArgumentException("new child is an ancestor");
		}

		MutableTreeNode oldParent = (MutableTreeNode) newChild.getParent();

		if (oldParent != null) {
			oldParent.remove(newChild);
		}
		newChild.setParent(this);
		if (children == null) {
			children = new Vector();
		}
		children.insertElementAt(newChild, childIndex);
	}

	public void remove(int childIndex) {
		MutableTreeNode child = (MutableTreeNode) getChildAt(childIndex);
		children.removeElementAt(childIndex);
		child.setParent(null);
	}

	public void setParent(MutableTreeNode newParent) {
		parent = newParent;
	}

	public TreeNode getParent() {
		return parent;
	}

	public TreeNode getChildAt(int index) {
		if (children == null) {
			throw new ArrayIndexOutOfBoundsException("node has no children");
		}
		return (TreeNode) children.elementAt(index);
	}

	public int getChildCount() {
		if (children == null) {
			return 0;
		} else {
			return children.size();
		}
	}

	public int getIndex(TreeNode aChild) {
		if (aChild == null) {
			throw new IllegalArgumentException("argument is null");
		}

		if (!isNodeChild(aChild)) {
			return -1;
		}
		return children.indexOf(aChild);
	}

	public Enumeration children() {
		if (children == null) {
			return EMPTY_ENUMERATION;
		} else {
			return children.elements();
		}
	}

	public void setAllowsChildren(boolean allows) {
		if (allows != allowsChildren) {
			allowsChildren = allows;
			if (!allowsChildren) {
				removeAllChildren();
			}
		}
	}

	public boolean getAllowsChildren() {
		return allowsChildren;
	}

	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	public Object getUserObject() {
		return userObject;
	}

	public void removeFromParent() {
		MutableTreeNode parent = (MutableTreeNode) getParent();
		if (parent != null) {
			parent.remove(this);
		}
	}

	public void remove(MutableTreeNode aChild) {
		if (aChild == null) {
			throw new IllegalArgumentException("argument is null");
		}

		if (!isNodeChild(aChild)) {
			throw new IllegalArgumentException("argument is not a child");
		}
		remove(getIndex(aChild));
	}

	public void removeAllChildren() {
		for (int i = getChildCount() - 1; i >= 0; i--) {
			remove(i);
		}
	}

	public void add(MutableTreeNode newChild) {
		if (newChild != null && newChild.getParent() == this)
			insert(newChild, getChildCount() - 1);
		else
			insert(newChild, getChildCount());
	}

	/**
	 * @param anotherNode
	 * @return true if <code>anotherNode</code> is an ancestor of this node
	 */
	public boolean isNodeAncestor(TreeNode anotherNode) {
		if (anotherNode == null) {
			return false;
		}

		TreeNode ancestor = this;

		do {
			if (ancestor == anotherNode) {
				return true;
			}
		} while ((ancestor = ancestor.getParent()) != null);

		return false;
	}

	/**
	 * @param anotherNode
	 * @return true if this node is an ancestor of <code>anotherNode</code>
	 */
	public boolean isNodeDescendant(DefaultMutableTreeNode anotherNode) {
		if (anotherNode == null)
			return false;

		return anotherNode.isNodeAncestor(this);
	}

	/**
	 * Returns the nearest common ancestor to this node and <code>aNode</code>.
	 * 
	 * @param aNode
	 * @return null if none
	 */
	public TreeNode getSharedAncestor(DefaultMutableTreeNode aNode) {
		if (aNode == this) {
			return this;
		} else if (aNode == null) {
			return null;
		}

		int level1, level2, diff;
		TreeNode node1, node2;

		level1 = getLevel();
		level2 = aNode.getLevel();

		if (level2 > level1) {
			diff = level2 - level1;
			node1 = aNode;
			node2 = this;
		} else {
			diff = level1 - level2;
			node1 = this;
			node2 = aNode;
		}

		while (diff > 0) {
			node1 = node1.getParent();
			diff--;
		}

		do {
			if (node1 == node2) {
				return node1;
			}
			node1 = node1.getParent();
			node2 = node2.getParent();
		} while (node1 != null);

		if (node1 != null || node2 != null) {
			throw new Error("nodes should be null");
		}

		return null;
	}

	/**
	 * @return true if aNode is in the same tree as this node
	 */
	public boolean isNodeRelated(DefaultMutableTreeNode aNode) {
		return (aNode != null) && (getRoot() == aNode.getRoot());
	}

	/**
	 * @return the depth of the tree whose root is this node
	 */
	public int getDepth() {
		Object last = null;
		Enumeration enum_ = breadthFirstEnumeration();

		while (enum_.hasMoreElements()) {
			last = enum_.nextElement();
		}

		if (last == null) {
			throw new Error("nodes should be null");
		}

		return ((DefaultMutableTreeNode) last).getLevel() - getLevel();
	}

	/**
	 * @return the number of levels above this node
	 */
	public int getLevel() {
		TreeNode parent;
		int levels = 0;

		parent = this;
		while ((parent = parent.getParent()) != null) {
			levels++;
		}

		return levels;
	}

	/**
	 * @return first element is the root and the last element is current node.
	 */
	public TreeNode[] getPath() {
		return getPathToRoot(this, 0);
	}

	protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
		TreeNode[] retNodes;

		if (aNode == null) {
			if (depth == 0)
				return null;
			else
				retNodes = new TreeNode[depth];
		} else {
			depth++;
			retNodes = getPathToRoot(aNode.getParent(), depth);
			retNodes[retNodes.length - depth] = aNode;
		}
		return retNodes;
	}

	public Object[] getUserObjectPath() {
		TreeNode[] realPath = getPath();
		Object[] retPath = new Object[realPath.length];

		for (int counter = 0; counter < realPath.length; counter++)
			retPath[counter] = ((DefaultMutableTreeNode) realPath[counter]).getUserObject();
		return retPath;
	}

	public TreeNode getRoot() {
		TreeNode ancestor = this;
		TreeNode previous;

		do {
			previous = ancestor;
			ancestor = ancestor.getParent();
		} while (ancestor != null);

		return previous;
	}

	public boolean isRoot() {
		return getParent() == null;
	}

	/**
	 * @return the node that follows this node in a preorder traversal, or null
	 *         if this node is last
	 */
	public DefaultMutableTreeNode getNextNode() {
		if (getChildCount() == 0) {
			DefaultMutableTreeNode nextSibling = getNextSibling();

			if (nextSibling == null) {
				DefaultMutableTreeNode aNode = (DefaultMutableTreeNode) getParent();

				do {
					if (aNode == null) {
						return null;
					}

					nextSibling = aNode.getNextSibling();
					if (nextSibling != null) {
						return nextSibling;
					}

					aNode = (DefaultMutableTreeNode) aNode.getParent();
				} while (true);
			} else {
				return nextSibling;
			}
		} else {
			return (DefaultMutableTreeNode) getChildAt(0);
		}
	}

	public DefaultMutableTreeNode getPreviousNode() {
		DefaultMutableTreeNode previousSibling;
		DefaultMutableTreeNode myParent = (DefaultMutableTreeNode) getParent();

		if (myParent == null) {
			return null;
		}

		previousSibling = getPreviousSibling();

		if (previousSibling != null) {
			if (previousSibling.getChildCount() == 0)
				return previousSibling;
			else
				return previousSibling.getLastLeaf();
		} else {
			return myParent;
		}
	}

	public Enumeration preorderEnumeration() {
		return new PreorderEnumeration(this);
	}

	public Enumeration postorderEnumeration() {
		return new PostorderEnumeration(this);
	}

	public Enumeration breadthFirstEnumeration() {
		return new BreadthFirstEnumeration(this);
	}

	public Enumeration depthFirstEnumeration() {
		return postorderEnumeration();
	}

	public Enumeration pathFromAncestorEnumeration(TreeNode ancestor) {
		return new PathBetweenNodesEnumeration(ancestor, this);
	}

	public boolean isNodeChild(TreeNode aNode) {
		boolean retval;

		if (aNode == null) {
			retval = false;
		} else {
			if (getChildCount() == 0) {
				retval = false;
			} else {
				retval = (aNode.getParent() == this);
			}
		}

		return retval;
	}

	public TreeNode getFirstChild() {
		if (getChildCount() == 0) {
			throw new NoSuchElementException("node has no children");
		}
		return getChildAt(0);
	}

	public TreeNode getLastChild() {
		if (getChildCount() == 0) {
			throw new NoSuchElementException("node has no children");
		}
		return getChildAt(getChildCount() - 1);
	}

	public TreeNode getChildAfter(TreeNode aChild) {
		if (aChild == null) {
			throw new IllegalArgumentException("argument is null");
		}

		int index = getIndex(aChild);

		if (index == -1) {
			throw new IllegalArgumentException("node is not a child");
		}

		if (index < getChildCount() - 1) {
			return getChildAt(index + 1);
		} else {
			return null;
		}
	}

	public TreeNode getChildBefore(TreeNode aChild) {
		if (aChild == null) {
			throw new IllegalArgumentException("argument is null");
		}

		int index = getIndex(aChild);

		if (index == -1) {
			throw new IllegalArgumentException("argument is not a child");
		}

		if (index > 0) {
			return getChildAt(index - 1);
		} else {
			return null;
		}
	}

	public boolean isNodeSibling(TreeNode anotherNode) {
		boolean retval;

		if (anotherNode == null) {
			retval = false;
		} else if (anotherNode == this) {
			retval = true;
		} else {
			TreeNode myParent = getParent();
			retval = (myParent != null && myParent == anotherNode.getParent());

			if (retval && !((DefaultMutableTreeNode) getParent()).isNodeChild(anotherNode)) {
				throw new Error("sibling has different parent");
			}
		}

		return retval;
	}

	public int getSiblingCount() {
		TreeNode myParent = getParent();

		if (myParent == null) {
			return 1;
		} else {
			return myParent.getChildCount();
		}
	}

	public DefaultMutableTreeNode getNextSibling() {
		DefaultMutableTreeNode retval;

		DefaultMutableTreeNode myParent = (DefaultMutableTreeNode) getParent();

		if (myParent == null) {
			retval = null;
		} else {
			retval = (DefaultMutableTreeNode) myParent.getChildAfter(this);
		}

		if (retval != null && !isNodeSibling(retval)) {
			throw new Error("child of parent is not a sibling");
		}

		return retval;
	}

	public DefaultMutableTreeNode getPreviousSibling() {
		DefaultMutableTreeNode retval;

		DefaultMutableTreeNode myParent = (DefaultMutableTreeNode) getParent();

		if (myParent == null) {
			retval = null;
		} else {
			retval = (DefaultMutableTreeNode) myParent.getChildBefore(this);
		}

		if (retval != null && !isNodeSibling(retval)) {
			throw new Error("child of parent is not a sibling");
		}

		return retval;
	}

	public boolean isLeaf() {
		return (getChildCount() == 0);
	}

	public DefaultMutableTreeNode getFirstLeaf() {
		DefaultMutableTreeNode node = this;

		while (!node.isLeaf()) {
			node = (DefaultMutableTreeNode) node.getFirstChild();
		}

		return node;
	}

	public DefaultMutableTreeNode getLastLeaf() {
		DefaultMutableTreeNode node = this;

		while (!node.isLeaf()) {
			node = (DefaultMutableTreeNode) node.getLastChild();
		}

		return node;
	}

	public DefaultMutableTreeNode getNextLeaf() {
		DefaultMutableTreeNode nextSibling;
		DefaultMutableTreeNode myParent = (DefaultMutableTreeNode) getParent();

		if (myParent == null)
			return null;

		nextSibling = getNextSibling();

		if (nextSibling != null)
			return nextSibling.getFirstLeaf();

		return myParent.getNextLeaf();
	}

	public DefaultMutableTreeNode getPreviousLeaf() {
		DefaultMutableTreeNode previousSibling;
		DefaultMutableTreeNode myParent = (DefaultMutableTreeNode) getParent();

		if (myParent == null)
			return null;

		previousSibling = getPreviousSibling();

		if (previousSibling != null)
			return previousSibling.getLastLeaf();

		return myParent.getPreviousLeaf();
	}

	public int getLeafCount() {
		int count = 0;

		TreeNode node;
		Enumeration enum_ = breadthFirstEnumeration();

		while (enum_.hasMoreElements()) {
			node = (TreeNode) enum_.nextElement();
			if (node.isLeaf()) {
				count++;
			}
		}

		if (count < 1) {
			throw new Error("tree has zero leaves");
		}

		return count;
	}

	public String toString() {
		if (userObject == null) {
			return "";
		} else {
			return userObject.toString();
		}
	}

	public Object clone() {
		DefaultMutableTreeNode newNode;

		try {
			newNode = (DefaultMutableTreeNode) super.clone();

			newNode.children = null;
			newNode.parent = null;

		} catch (CloneNotSupportedException e) {
			throw new Error(e.toString());
		}

		return newNode;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
	}

	private final class PreorderEnumeration implements Enumeration<TreeNode> {
		private final Stack<Enumeration> stack = new Stack<Enumeration>();

		public PreorderEnumeration(TreeNode rootNode) {
			super();
			Vector<TreeNode> v = new Vector<TreeNode>(1);
			v.addElement(rootNode);
			stack.push(v.elements());
		}

		public boolean hasMoreElements() {
			return (!stack.empty() && stack.peek().hasMoreElements());
		}

		public TreeNode nextElement() {
			Enumeration enumer = stack.peek();
			TreeNode node = (TreeNode) enumer.nextElement();
			Enumeration children = node.children();

			if (!enumer.hasMoreElements()) {
				stack.pop();
			}
			if (children.hasMoreElements()) {
				stack.push(children);
			}
			return node;
		}

	}

	final class PostorderEnumeration implements Enumeration<TreeNode> {
		protected TreeNode root;
		protected Enumeration<TreeNode> children;
		protected Enumeration<TreeNode> subtree;

		public PostorderEnumeration(TreeNode rootNode) {
			super();
			root = rootNode;
			children = root.children();
			subtree = EMPTY_ENUMERATION;
		}

		public boolean hasMoreElements() {
			return root != null;
		}

		public TreeNode nextElement() {
			TreeNode retval;

			if (subtree.hasMoreElements()) {
				retval = subtree.nextElement();
			} else if (children.hasMoreElements()) {
				subtree = new PostorderEnumeration(children.nextElement());
				retval = subtree.nextElement();
			} else {
				retval = root;
				root = null;
			}

			return retval;
		}

	}

	final class BreadthFirstEnumeration implements Enumeration<TreeNode> {
		protected Queue queue;

		public BreadthFirstEnumeration(TreeNode rootNode) {
			super();
			Vector<TreeNode> v = new Vector<TreeNode>(1);
			v.addElement(rootNode);
			queue = new Queue();
			queue.enqueue(v.elements());
		}

		public boolean hasMoreElements() {
			return (!queue.isEmpty() && ((Enumeration) queue.firstObject()).hasMoreElements());
		}

		public TreeNode nextElement() {
			Enumeration enumer = (Enumeration) queue.firstObject();
			TreeNode node = (TreeNode) enumer.nextElement();
			Enumeration children = node.children();

			if (!enumer.hasMoreElements()) {
				queue.dequeue();
			}
			if (children.hasMoreElements()) {
				queue.enqueue(children);
			}
			return node;
		}

		final class Queue {
			QNode head;
			QNode tail;

			final class QNode {
				public Object object;
				public QNode next;

				public QNode(Object object, QNode next) {
					this.object = object;
					this.next = next;
				}
			}

			public void enqueue(Object anObject) {
				if (head == null) {
					head = tail = new QNode(anObject, null);
				} else {
					tail.next = new QNode(anObject, null);
					tail = tail.next;
				}
			}

			public Object dequeue() {
				if (head == null) {
					throw new NoSuchElementException("No more elements");
				}

				Object retval = head.object;
				QNode oldHead = head;
				head = head.next;
				if (head == null) {
					tail = null;
				} else {
					oldHead.next = null;
				}
				return retval;
			}

			public Object firstObject() {
				if (head == null) {
					throw new NoSuchElementException("No more elements");
				}

				return head.object;
			}

			public boolean isEmpty() {
				return head == null;
			}

		}
	}

	final class PathBetweenNodesEnumeration implements Enumeration<TreeNode> {
		protected Stack<TreeNode> stack;

		public PathBetweenNodesEnumeration(TreeNode ancestor, TreeNode descendant) {
			super();

			if (ancestor == null || descendant == null) {
				throw new IllegalArgumentException("argument is null");
			}

			TreeNode current;

			stack = new Stack<TreeNode>();
			stack.push(descendant);

			current = descendant;
			while (current != ancestor) {
				current = current.getParent();
				if (current == null && descendant != ancestor) {
					throw new IllegalArgumentException(
							"node " + ancestor + " is not an ancestor of " + descendant);
				}
				stack.push(current);
			}
		}

		public boolean hasMoreElements() {
			return stack.size() > 0;
		}

		public TreeNode nextElement() {
			try {
				return stack.pop();
			} catch (EmptyStackException e) {
				throw new NoSuchElementException("No more elements");
			}
		}

	}
}