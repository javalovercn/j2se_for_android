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
 * An event that characterizes a change in the current selection. The change is
 * based on any number of paths. TreeSelectionListeners will generally query the
 * source of the event for the new selected status of each potentially changed
 * row.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @see TreeSelectionListener
 * @see javax.swing.tree.TreeSelectionModel
 *
 * @author Scott Violet
 */
public class TreeSelectionEvent extends EventObject {
	protected TreePath[] paths;
	protected boolean[] areNew;
	protected TreePath oldLeadSelectionPath;
	protected TreePath newLeadSelectionPath;

	public TreeSelectionEvent(Object source, TreePath[] paths, boolean[] areNew,
			TreePath oldLeadSelectionPath, TreePath newLeadSelectionPath) {
		super(source);
		this.paths = paths;
		this.areNew = areNew;
		this.oldLeadSelectionPath = oldLeadSelectionPath;
		this.newLeadSelectionPath = newLeadSelectionPath;
	}

	public TreeSelectionEvent(Object source, TreePath path, boolean isNew,
			TreePath oldLeadSelectionPath, TreePath newLeadSelectionPath) {
		super(source);
		paths = new TreePath[1];
		paths[0] = path;
		areNew = new boolean[1];
		areNew[0] = isNew;
		this.oldLeadSelectionPath = oldLeadSelectionPath;
		this.newLeadSelectionPath = newLeadSelectionPath;
	}

	public TreePath[] getPaths() {
		int numPaths;
		TreePath[] retPaths;

		numPaths = paths.length;
		retPaths = new TreePath[numPaths];
		System.arraycopy(paths, 0, retPaths, 0, numPaths);
		return retPaths;
	}

	public TreePath getPath() {
		return paths[0];
	}

	public boolean isAddedPath() {
		return areNew[0];
	}

	public boolean isAddedPath(TreePath path) {
		for (int counter = paths.length - 1; counter >= 0; counter--)
			if (paths[counter].equals(path))
				return areNew[counter];
		throw new IllegalArgumentException(
				"path is not a path identified by the TreeSelectionEvent");
	}

	public boolean isAddedPath(int index) {
		if (paths == null || index < 0 || index >= paths.length) {
			throw new IllegalArgumentException(
					"index is beyond range of added paths identified by TreeSelectionEvent");
		}
		return areNew[index];
	}

	public TreePath getOldLeadSelectionPath() {
		return oldLeadSelectionPath;
	}

	public TreePath getNewLeadSelectionPath() {
		return newLeadSelectionPath;
	}

	public Object cloneWithSource(Object newSource) {
		return new TreeSelectionEvent(newSource, paths, areNew, oldLeadSelectionPath,
				newLeadSelectionPath);
	}
}
