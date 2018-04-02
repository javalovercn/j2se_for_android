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
import hc.core.util.LogManager;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.BitSet;
import java.util.EventListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.SwingPropertyChangeSupport;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * Default implementation of TreeSelectionModel. Listeners are notified whenever
 * the paths in the selection change, not the rows. In order to be able to track
 * row changes you may wish to become a listener for expansion events on the
 * tree and test for changes from there.
 * <p>
 * resetRowSelection is called from any of the methods that update the selected
 * paths. If you subclass any of these methods to filter what is allowed to be
 * selected, be sure and message <code>resetRowSelection</code> if you do not
 * message super.
 *
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @see javax.swing.JTree
 *
 * @author Scott Violet
 */
public class DefaultTreeSelectionModel implements Cloneable, Serializable, TreeSelectionModel {
	public static final String SELECTION_MODE_PROPERTY = "selectionMode";

	protected TreePath[] selection;
	protected EventListenerList listenerList = new EventListenerList();
	transient protected RowMapper rowMapper;
	protected DefaultListSelectionModel listSelectionModel;

	protected int selectionMode;

	protected TreePath leadPath;
	protected int leadIndex;
	protected int leadRow;

	private Hashtable<TreePath, Boolean> uniquePaths;
	private Hashtable<TreePath, Boolean> lastPaths;
	private TreePath[] tempPaths;

	public DefaultTreeSelectionModel() {
		listSelectionModel = new DefaultListSelectionModel();
		selectionMode = DISCONTIGUOUS_TREE_SELECTION;
		leadIndex = leadRow = -1;
		uniquePaths = new Hashtable<TreePath, Boolean>();
		lastPaths = new Hashtable<TreePath, Boolean>();
		tempPaths = new TreePath[1];
	}

	public void setRowMapper(RowMapper newMapper) {
		rowMapper = newMapper;
		resetRowSelection();
	}

	public RowMapper getRowMapper() {
		return rowMapper;
	}

	public void setSelectionMode(int mode) {
		if (selectionMode != TreeSelectionModel.SINGLE_TREE_SELECTION) {
			LogManager.warning("only SINGLE_TREE_SELECTION is supported in this lib.");
		}

		selectionMode = mode;
		if (selectionMode != TreeSelectionModel.SINGLE_TREE_SELECTION
				&& selectionMode != TreeSelectionModel.CONTIGUOUS_TREE_SELECTION
				&& selectionMode != TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION)
			selectionMode = TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION;
	}

	public int getSelectionMode() {
		return selectionMode;
	}

	public void setSelectionPath(TreePath path) {
		if (path == null)
			setSelectionPaths(null);
		else {
			TreePath[] newPaths = new TreePath[1];

			newPaths[0] = path;
			setSelectionPaths(newPaths);
		}
	}

	public void setSelectionPaths(TreePath[] pPaths) {
		if (pPaths == null) {
			currentSelectionPath = null;
		} else {
			currentSelectionPath = pPaths[0];
		}
	}

	public void addSelectionPath(TreePath path) {
		if (path != null) {
			TreePath[] toAdd = new TreePath[1];

			toAdd[0] = path;
			addSelectionPaths(toAdd);
		}
	}

	public void addSelectionPaths(TreePath[] paths) {
		int newPathLength = ((paths == null) ? 0 : paths.length);

		if (newPathLength > 0) {
			if (selectionMode == TreeSelectionModel.SINGLE_TREE_SELECTION) {
				setSelectionPaths(paths);
				return;
			}
		}
		AndroidClassUtil.callEmptyMethod();
	}

	public void removeSelectionPath(TreePath path) {
		if (path != null) {
			TreePath[] rPath = new TreePath[1];

			rPath[0] = path;
			removeSelectionPaths(rPath);
		}
	}

	public void removeSelectionPaths(TreePath[] paths) {
		AndroidClassUtil.callEmptyMethod();
	}

	public TreePath getSelectionPath() {
		return currentSelectionPath;
	}

	public TreePath[] getSelectionPaths() {
		TreePath[] out = { currentSelectionPath };
		return out;
	}

	public int getSelectionCount() {
		return (currentSelectionPath == null) ? 0 : 1;
	}

	public boolean isPathSelected(TreePath path) {
		return (path != null) ? (uniquePaths.get(path) != null) : false;
	}

	public boolean isSelectionEmpty() {
		return (currentSelectionPath == null);// || selection.length == 0
	}

	public void clearSelection() {
		if (selection != null && selection.length > 0) {
			int selSize = selection.length;
			boolean[] newness = new boolean[selSize];

			for (int counter = 0; counter < selSize; counter++)
				newness[counter] = false;

			TreeSelectionEvent event = new TreeSelectionEvent(this, selection, newness, leadPath,
					null);

			leadPath = null;
			leadIndex = leadRow = -1;
			uniquePaths.clear();
			selection = null;
			resetRowSelection();
			fireValueChanged(event);
		}
	}

	public void addTreeSelectionListener(TreeSelectionListener x) {
		listenerList.add(TreeSelectionListener.class, x);
	}

	public void removeTreeSelectionListener(TreeSelectionListener x) {
		listenerList.remove(TreeSelectionListener.class, x);
	}

	public TreeSelectionListener[] getTreeSelectionListeners() {
		return listenerList.getListeners(TreeSelectionListener.class);
	}

	TreePath currentSelectionPath;

	protected void fireValueChanged(TreeSelectionEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeSelectionListener.class) {
				((TreeSelectionListener) listeners[i + 1]).valueChanged(e);
			}
		}
	}

	public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
		return listenerList.getListeners(listenerType);
	}

	public int[] getSelectionRows() {
		if (rowMapper != null && selection != null && selection.length > 0) {
			int[] rows = rowMapper.getRowsForPaths(selection);

			if (rows != null) {
				int invisCount = 0;

				for (int counter = rows.length - 1; counter >= 0; counter--) {
					if (rows[counter] == -1) {
						invisCount++;
					}
				}
				if (invisCount > 0) {
					if (invisCount == rows.length) {
						rows = null;
					} else {
						int[] tempRows = new int[rows.length - invisCount];

						for (int counter = rows.length
								- 1, visCounter = 0; counter >= 0; counter--) {
							if (rows[counter] != -1) {
								tempRows[visCounter++] = rows[counter];
							}
						}
						rows = tempRows;
					}
				}
			}
			return rows;
		}
		return new int[0];
	}

	public int getMinSelectionRow() {
		return listSelectionModel.getMinSelectionIndex();
	}

	public int getMaxSelectionRow() {
		return listSelectionModel.getMaxSelectionIndex();
	}

	public boolean isRowSelected(int row) {
		return listSelectionModel.isSelectedIndex(row);
	}

	public void resetRowSelection() {
		listSelectionModel.clearSelection();
		if (selection != null && rowMapper != null) {
			int aRow;
			int[] rows = rowMapper.getRowsForPaths(selection);

			for (int counter = 0, maxCounter = selection.length; counter < maxCounter; counter++) {
				aRow = rows[counter];
				if (aRow != -1) {
					listSelectionModel.addSelectionInterval(aRow, aRow);
				}
			}
			if (leadIndex != -1 && rows != null) {
				leadRow = rows[leadIndex];
			} else if (leadPath != null) {
				tempPaths[0] = leadPath;
				rows = rowMapper.getRowsForPaths(tempPaths);
				leadRow = (rows != null) ? rows[0] : -1;
			} else {
				leadRow = -1;
			}
			insureRowContinuity();

		} else {
			leadRow = -1;
		}
	}

	public int getLeadSelectionRow() {
		return leadRow;
	}

	public TreePath getLeadSelectionPath() {
		return leadPath;
	}

	public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
		AndroidClassUtil.callEmptyMethod();
	}

	public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
		AndroidClassUtil.callEmptyMethod();
	}

	public PropertyChangeListener[] getPropertyChangeListeners() {
		AndroidClassUtil.callEmptyMethod();
		return new PropertyChangeListener[0];
	}

	protected void insureRowContinuity() {
		if (selectionMode == TreeSelectionModel.CONTIGUOUS_TREE_SELECTION && selection != null
				&& rowMapper != null) {
			DefaultListSelectionModel lModel = listSelectionModel;
			int min = lModel.getMinSelectionIndex();

			if (min != -1) {
				for (int counter = min, maxCounter = lModel
						.getMaxSelectionIndex(); counter <= maxCounter; counter++) {
					if (!lModel.isSelectedIndex(counter)) {
						if (counter == min) {
							clearSelection();
						} else {
							TreePath[] newSel = new TreePath[counter - min];
							int selectionIndex[] = rowMapper.getRowsForPaths(selection);
							for (int i = 0; i < selectionIndex.length; i++) {
								if (selectionIndex[i] < counter) {
									newSel[selectionIndex[i] - min] = selection[i];
								}
							}
							setSelectionPaths(newSel);
							break;
						}
					}
				}
			}
		} else if (selectionMode == TreeSelectionModel.SINGLE_TREE_SELECTION && selection != null
				&& selection.length > 1) {
			setSelectionPath(selection[0]);
		}
	}

	protected boolean arePathsContiguous(TreePath[] paths) {
		if (rowMapper == null || paths.length < 2)
			return true;
		else {
			BitSet bitSet = new BitSet(32);
			int anIndex, counter, min;
			int pathCount = paths.length;
			int validCount = 0;
			TreePath[] tempPath = new TreePath[1];

			tempPath[0] = paths[0];
			min = rowMapper.getRowsForPaths(tempPath)[0];
			for (counter = 0; counter < pathCount; counter++) {
				if (paths[counter] != null) {
					tempPath[0] = paths[counter];
					int[] rows = rowMapper.getRowsForPaths(tempPath);
					if (rows == null) {
						return false;
					}
					anIndex = rows[0];
					if (anIndex == -1 || anIndex < (min - pathCount) || anIndex > (min + pathCount))
						return false;
					if (anIndex < min)
						min = anIndex;
					if (!bitSet.get(anIndex)) {
						bitSet.set(anIndex);
						validCount++;
					}
				}
			}
			int maxCounter = validCount + min;

			for (counter = min; counter < maxCounter; counter++)
				if (!bitSet.get(counter))
					return false;
		}
		return true;
	}

	protected boolean canPathsBeAdded(TreePath[] paths) {
		if (paths == null || paths.length == 0 || rowMapper == null || selection == null
				|| selectionMode == TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION)
			return true;
		else {
			BitSet bitSet = new BitSet();
			DefaultListSelectionModel lModel = listSelectionModel;
			int anIndex;
			int counter;
			int min = lModel.getMinSelectionIndex();
			int max = lModel.getMaxSelectionIndex();
			TreePath[] tempPath = new TreePath[1];

			if (min != -1) {
				for (counter = min; counter <= max; counter++) {
					if (lModel.isSelectedIndex(counter))
						bitSet.set(counter);
				}
			} else {
				tempPath[0] = paths[0];
				min = max = rowMapper.getRowsForPaths(tempPath)[0];
			}
			for (counter = paths.length - 1; counter >= 0; counter--) {
				if (paths[counter] != null) {
					tempPath[0] = paths[counter];
					int[] rows = rowMapper.getRowsForPaths(tempPath);
					if (rows == null) {
						return false;
					}
					anIndex = rows[0];
					min = Math.min(anIndex, min);
					max = Math.max(anIndex, max);
					if (anIndex == -1)
						return false;
					bitSet.set(anIndex);
				}
			}
			for (counter = min; counter <= max; counter++)
				if (!bitSet.get(counter))
					return false;
		}
		return true;
	}

	protected boolean canPathsBeRemoved(TreePath[] paths) {
		if (rowMapper == null || selection == null
				|| selectionMode == TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION)
			return true;
		else {
			BitSet bitSet = new BitSet();
			int counter;
			int pathCount = paths.length;
			int anIndex;
			int min = -1;
			int validCount = 0;
			TreePath[] tempPath = new TreePath[1];
			int[] rows;

			lastPaths.clear();
			for (counter = 0; counter < pathCount; counter++) {
				if (paths[counter] != null) {
					lastPaths.put(paths[counter], Boolean.TRUE);
				}
			}
			for (counter = selection.length - 1; counter >= 0; counter--) {
				if (lastPaths.get(selection[counter]) == null) {
					tempPath[0] = selection[counter];
					rows = rowMapper.getRowsForPaths(tempPath);
					if (rows != null && rows[0] != -1 && !bitSet.get(rows[0])) {
						validCount++;
						if (min == -1)
							min = rows[0];
						else
							min = Math.min(min, rows[0]);
						bitSet.set(rows[0]);
					}
				}
			}
			lastPaths.clear();
			if (validCount > 1) {
				for (counter = min + validCount - 1; counter >= min; counter--)
					if (!bitSet.get(counter))
						return false;
			}
		}
		return true;
	}

	protected void notifyPathChange(Vector changedPaths, TreePath oldLeadSelection) {
		int cPathCount = changedPaths.size();
		boolean[] newness = new boolean[cPathCount];
		TreePath[] paths = new TreePath[cPathCount];
		PathPlaceHolder placeholder;

		for (int counter = 0; counter < cPathCount; counter++) {
			placeholder = (PathPlaceHolder) changedPaths.elementAt(counter);
			newness[counter] = placeholder.isNew;
			paths[counter] = placeholder.path;
		}

		TreeSelectionEvent event = new TreeSelectionEvent(this, paths, newness, oldLeadSelection,
				leadPath);

		fireValueChanged(event);
	}

	protected void updateLeadIndex() {
		if (leadPath != null) {
			if (selection == null) {
				leadPath = null;
				leadIndex = leadRow = -1;
			} else {
				leadRow = leadIndex = -1;
				for (int counter = selection.length - 1; counter >= 0; counter--) {
					if (selection[counter] == leadPath) {
						leadIndex = counter;
						break;
					}
				}
			}
		} else {
			leadIndex = -1;
		}
	}

	protected void insureUniqueness() {
	}

	public String toString() {
		return "";
	}

	public Object clone() throws CloneNotSupportedException {
		return null;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
	}
}

class PathPlaceHolder {
	protected boolean isNew;
	protected TreePath path;

	PathPlaceHolder(TreePath path, boolean isNew) {
		this.path = path;
		this.isNew = isNew;
	}
}