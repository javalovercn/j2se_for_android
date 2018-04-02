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

import java.beans.PropertyChangeListener;

import javax.swing.event.TreeSelectionListener;

/**
 * This interface represents the current state of the selection for the tree
 * component. For information and examples of using tree selection models, see
 * <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/tree.html">How to
 * Use Trees</a> in <em>The Java Tutorial.</em>
 *
 * <p>
 * The state of the tree selection is characterized by a set of TreePaths, and
 * optionally a set of integers. The mapping from TreePath to integer is done by
 * way of an instance of RowMapper. It is not necessary for a TreeSelectionModel
 * to have a RowMapper to correctly operate, but without a RowMapper
 * <code>getSelectionRows</code> will return null.
 *
 * <p>
 *
 * A TreeSelectionModel can be configured to allow only one path
 * (<code>SINGLE_TREE_SELECTION</code>) a number of continguous paths
 * (<code>CONTIGUOUS_TREE_SELECTION</code>) or a number of discontiguous paths
 * (<code>DISCONTIGUOUS_TREE_SELECTION</code>). A <code>RowMapper</code> is used
 * to determine if TreePaths are contiguous. In the absence of a RowMapper
 * <code>CONTIGUOUS_TREE_SELECTION</code> and
 * <code>DISCONTIGUOUS_TREE_SELECTION</code> behave the same, that is they allow
 * any number of paths to be contained in the TreeSelectionModel.
 *
 * <p>
 *
 * For a selection model of <code>CONTIGUOUS_TREE_SELECTION</code> any time the
 * paths are changed (<code>setSelectionPath</code>,
 * <code>addSelectionPath</code> ...) the TreePaths are again checked to make
 * they are contiguous. A check of the TreePaths can also be forced by invoking
 * <code>resetRowSelection</code>. How a set of discontiguous TreePaths is
 * mapped to a contiguous set is left to implementors of this interface to
 * enforce a particular policy.
 *
 * <p>
 *
 * Implementations should combine duplicate TreePaths that are added to the
 * selection. For example, the following code
 * 
 * <pre>
 * TreePath[] paths = new TreePath[] { treePath, treePath };
 * treeSelectionModel.setSelectionPaths(paths);
 * </pre>
 * 
 * should result in only one path being selected: <code>treePath</code>, and not
 * two copies of <code>treePath</code>.
 *
 * <p>
 *
 * The lead TreePath is the last path that was added (or set). The lead row is
 * then the row that corresponds to the TreePath as determined from the
 * RowMapper.
 *
 * @author Scott Violet
 */
public interface TreeSelectionModel {
	public static final int SINGLE_TREE_SELECTION = 1;
	public static final int CONTIGUOUS_TREE_SELECTION = 2;
	public static final int DISCONTIGUOUS_TREE_SELECTION = 4;

	void setSelectionMode(int mode);

	int getSelectionMode();

	void setSelectionPath(TreePath path);

	void setSelectionPaths(TreePath[] paths);

	void addSelectionPath(TreePath path);

	void addSelectionPaths(TreePath[] paths);

	void removeSelectionPath(TreePath path);

	void removeSelectionPaths(TreePath[] paths);

	TreePath getSelectionPath();

	TreePath[] getSelectionPaths();

	int getSelectionCount();

	boolean isPathSelected(TreePath path);

	boolean isSelectionEmpty();

	void clearSelection();

	void setRowMapper(RowMapper newMapper);

	RowMapper getRowMapper();

	int[] getSelectionRows();

	int getMinSelectionRow();

	int getMaxSelectionRow();

	boolean isRowSelected(int row);

	void resetRowSelection();

	int getLeadSelectionRow();

	TreePath getLeadSelectionPath();

	void addPropertyChangeListener(PropertyChangeListener listener);

	void removePropertyChangeListener(PropertyChangeListener listener);

	void addTreeSelectionListener(TreeSelectionListener x);

	void removeTreeSelectionListener(TreeSelectionListener x);
}
