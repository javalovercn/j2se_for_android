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

import javax.swing.event.TreeModelListener;

/**
 * The model used by <code>JTree</code>.
 * <p>
 * <code>JTree</code> and its related classes make extensive use of
 * <code>TreePath</code>s for indentifying nodes in the <code>TreeModel</code>.
 * If a <code>TreeModel</code> returns the same object, as compared by
 * <code>equals</code>, at two different indices under the same parent than the
 * resulting <code>TreePath</code> objects will be considered equal as well.
 * Some implementations may assume that if two <code>TreePath</code>s are equal,
 * they identify the same node. If this condition is not met, painting problems
 * and other oddities may result. In other words, if <code>getChild</code> for a
 * given parent returns the same Object (as determined by <code>equals</code>)
 * problems may result, and it is recommended you avoid doing this.
 * <p>
 * Similarly <code>JTree</code> and its related classes place
 * <code>TreePath</code>s in <code>Map</code>s. As such if a node is requested
 * twice, the return values must be equal (using the <code>equals</code> method)
 * and have the same <code>hashCode</code>.
 * <p>
 * For further information on tree models, including an example of a custom
 * implementation, see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/tree.html">How to
 * Use Trees</a> in <em>The Java Tutorial.</em>
 *
 * @see TreePath
 *
 * @author Rob Davis
 * @author Ray Ryan
 */
public interface TreeModel {
	public Object getRoot();

	public Object getChild(Object parent, int index);

	public int getChildCount(Object parent);

	public boolean isLeaf(Object node);

	public void valueForPathChanged(TreePath path, Object newValue);

	public int getIndexOfChild(Object parent, Object child);

	void addTreeModelListener(TreeModelListener l);

	void removeTreeModelListener(TreeModelListener l);

}