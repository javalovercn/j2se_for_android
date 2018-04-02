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
package javax.swing.table;

import hc.android.AndroidClassUtil;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

/**
 * The standard column-handler for a <code>JTable</code>.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Alan Chung
 * @author Philip Milne
 * @see JTable
 */
public class DefaultTableColumnModel
		implements TableColumnModel, PropertyChangeListener, ListSelectionListener, Serializable {

	protected Vector<TableColumn> tableColumns;
	protected ListSelectionModel selectionModel;
	/** Width margin between each column */
	protected int columnMargin;
	protected EventListenerList listenerList = new EventListenerList();
	/** select Column or not */
	protected boolean columnSelectionAllowed;
	/** A local cache of the combined width of all columns */
	protected int totalColumnWidth;

	public DefaultTableColumnModel() {
		super();

		tableColumns = new Vector<TableColumn>();
		setSelectionModel(createSelectionModel());
		setColumnMargin(1);
		invalidateWidthCache();
		setColumnSelectionAllowed(false);
	}

	public void addColumn(TableColumn aColumn) {
		if (aColumn == null) {
			throw new IllegalArgumentException("Object is null");
		}

		tableColumns.addElement(aColumn);
		invalidateWidthCache();
		fireColumnAdded(new TableColumnModelEvent(this, 0, getColumnCount() - 1));
	}

	public void removeColumn(TableColumn column) {
		int columnIndex = tableColumns.indexOf(column);

		if (columnIndex != -1) {
			if (selectionModel != null) {
				selectionModel.removeIndexInterval(columnIndex, columnIndex);
			}

			column.removePropertyChangeListener(this);
			tableColumns.removeElementAt(columnIndex);
			invalidateWidthCache();

			fireColumnRemoved(new TableColumnModelEvent(this, columnIndex, 0));
		}
	}

	public void moveColumn(int columnIndex, int newIndex) {
		if ((columnIndex < 0) || (columnIndex >= getColumnCount()) || (newIndex < 0)
				|| (newIndex >= getColumnCount()))
			throw new IllegalArgumentException("moveColumn() - Index out of range");

		TableColumn aColumn;

		if (columnIndex == newIndex) {
			fireColumnMoved(new TableColumnModelEvent(this, columnIndex, newIndex));
			return;
		}
		aColumn = tableColumns.elementAt(columnIndex);

		tableColumns.removeElementAt(columnIndex);
		boolean selected = selectionModel.isSelectedIndex(columnIndex);
		selectionModel.removeIndexInterval(columnIndex, columnIndex);

		tableColumns.insertElementAt(aColumn, newIndex);
		selectionModel.insertIndexInterval(newIndex, 1, true);
		if (selected) {
			selectionModel.addSelectionInterval(newIndex, newIndex);
		} else {
			selectionModel.removeSelectionInterval(newIndex, newIndex);
		}

		fireColumnMoved(new TableColumnModelEvent(this, columnIndex, newIndex));
	}

	public void setColumnMargin(int newMargin) {
		if (newMargin != columnMargin) {
			columnMargin = newMargin;
			fireColumnMarginChanged();
		}
	}

	public int getColumnCount() {
		return tableColumns.size();
	}

	public Enumeration<TableColumn> getColumns() {
		return tableColumns.elements();
	}

	public int getColumnIndex(Object identifier) {
		if (identifier == null) {
			throw new IllegalArgumentException("Identifier is null");
		}

		Enumeration enumeration = getColumns();
		TableColumn aColumn;
		int index = 0;

		while (enumeration.hasMoreElements()) {
			aColumn = (TableColumn) enumeration.nextElement();
			if (identifier.equals(aColumn.getIdentifier()))
				return index;
			index++;
		}
		throw new IllegalArgumentException("Identifier not found");
	}

	public TableColumn getColumn(int columnIndex) {
		return tableColumns.elementAt(columnIndex);
	}

	public int getColumnMargin() {
		return columnMargin;
	}

	/**
	 * Returns the index of the column that lies at position X
	 */
	public int getColumnIndexAtX(int x) {
		if (x < 0) {
			return -1;
		}
		int cc = getColumnCount();
		for (int column = 0; column < cc; column++) {
			x = x - getColumn(column).getWidth();
			if (x < 0) {
				return column;
			}
		}
		return -1;
	}

	public int getTotalColumnWidth() {
		if (totalColumnWidth == -1) {
			recalcWidthCache();
		}
		return totalColumnWidth;
	}

	public void setSelectionModel(ListSelectionModel newModel) {
		if (newModel == null) {
			throw new IllegalArgumentException("Cannot set a null SelectionModel");
		}

		ListSelectionModel oldModel = selectionModel;

		if (newModel != oldModel) {
			if (oldModel != null) {
				oldModel.removeListSelectionListener(this);
			}

			selectionModel = newModel;
			newModel.addListSelectionListener(this);
		}
	}

	public ListSelectionModel getSelectionModel() {
		return selectionModel;
	}

	public void setColumnSelectionAllowed(boolean flag) {
		columnSelectionAllowed = flag;
	}

	public boolean getColumnSelectionAllowed() {
		return columnSelectionAllowed;
	}

	public int[] getSelectedColumns() {
		if (selectionModel != null) {
			int min = selectionModel.getMinSelectionIndex();
			int max = selectionModel.getMaxSelectionIndex();

			if ((min == -1) || (max == -1)) {
				return new int[0];
			}

			int[] out = new int[1 + (max - min)];
			int n = 0;
			for (int i = min; i <= max; i++) {
				if (selectionModel.isSelectedIndex(i)) {
					out[n++] = i;
				}
			}
			int[] rv = new int[n];
			System.arraycopy(out, 0, rv, 0, n);
			return rv;
		}
		return new int[0];
	}

	public int getSelectedColumnCount() {
		if (selectionModel != null) {
			int iMin = selectionModel.getMinSelectionIndex();
			int iMax = selectionModel.getMaxSelectionIndex();
			int count = 0;

			for (int i = iMin; i <= iMax; i++) {
				if (selectionModel.isSelectedIndex(i)) {
					count++;
				}
			}
			return count;
		}
		return 0;
	}

	public void addColumnModelListener(TableColumnModelListener x) {
		listenerList.add(TableColumnModelListener.class, x);
	}

	public void removeColumnModelListener(TableColumnModelListener x) {
		listenerList.remove(TableColumnModelListener.class, x);
	}

	public TableColumnModelListener[] getColumnModelListeners() {
		return listenerList.getListeners(TableColumnModelListener.class);
	}

	protected void fireColumnAdded(TableColumnModelEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TableColumnModelListener.class) {
				((TableColumnModelListener) listeners[i + 1]).columnAdded(e);
			}
		}
	}

	protected void fireColumnRemoved(TableColumnModelEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TableColumnModelListener.class) {
				((TableColumnModelListener) listeners[i + 1]).columnRemoved(e);
			}
		}
	}

	protected void fireColumnMoved(TableColumnModelEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TableColumnModelListener.class) {
				((TableColumnModelListener) listeners[i + 1]).columnMoved(e);
			}
		}
	}

	protected void fireColumnSelectionChanged(ListSelectionEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TableColumnModelListener.class) {
				((TableColumnModelListener) listeners[i + 1]).columnSelectionChanged(e);
			}
		}
	}

	protected void fireColumnMarginChanged() {
		Object[] listeners = listenerList.getListenerList();
		ChangeEvent changeEvent = null;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TableColumnModelListener.class) {
				if (changeEvent == null)
					changeEvent = new ChangeEvent(this);
				((TableColumnModelListener) listeners[i + 1]).columnMarginChanged(changeEvent);
			}
		}
	}

	public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
		return listenerList.getListeners(listenerType);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();

		if (name == "width" || name == "preferredWidth") {
			invalidateWidthCache();
			fireColumnMarginChanged();
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		fireColumnSelectionChanged(e);
	}

	protected ListSelectionModel createSelectionModel() {
		return new DefaultListSelectionModel();
	}

	protected void recalcWidthCache() {
		Enumeration enumeration = getColumns();
		totalColumnWidth = 0;
		while (enumeration.hasMoreElements()) {
			totalColumnWidth += ((TableColumn) enumeration.nextElement()).getWidth();
		}
	}

	private void invalidateWidthCache() {
		totalColumnWidth = -1;
	}
}
