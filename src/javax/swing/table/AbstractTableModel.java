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

import java.io.Serializable;
import java.util.EventListener;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * This abstract class provides default implementations for most of the methods
 * in the <code>TableModel</code> interface. It takes care of the management of
 * listeners and provides some conveniences for generating
 * <code>TableModelEvents</code> and dispatching them to the listeners. To
 * create a concrete <code>TableModel</code> as a subclass of
 * <code>AbstractTableModel</code> you need only provide implementations for the
 * following three methods:
 *
 * <pre>
 * public int getRowCount();
 * 
 * public int getColumnCount();
 * 
 * public Object getValueAt(int row, int column);
 * </pre>
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
 */
public abstract class AbstractTableModel implements TableModel, Serializable {
	protected EventListenerList listenerList = new EventListenerList();

	public String getColumnName(int column) {
		return "column " + column;
	}

	public int findColumn(String columnName) {
		for (int i = 0; i < getColumnCount(); i++) {
			if (columnName.equals(getColumnName(i))) {
				return i;
			}
		}
		return -1;
	}

	public Class<?> getColumnClass(int columnIndex) {
		return Object.class;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	public void addTableModelListener(TableModelListener l) {
		listenerList.add(TableModelListener.class, l);
	}

	public void removeTableModelListener(TableModelListener l) {
		listenerList.remove(TableModelListener.class, l);
	}

	public TableModelListener[] getTableModelListeners() {
		return listenerList.getListeners(TableModelListener.class);
	}

	public void fireTableDataChanged() {
		fireTableChanged(new TableModelEvent(this));
	}

	public void fireTableStructureChanged() {
		fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
	}

	public void fireTableRowsInserted(int firstRow, int lastRow) {
		fireTableChanged(new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS,
				TableModelEvent.INSERT));
	}

	public void fireTableRowsUpdated(int firstRow, int lastRow) {
		fireTableChanged(new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS,
				TableModelEvent.UPDATE));
	}

	public void fireTableRowsDeleted(int firstRow, int lastRow) {
		fireTableChanged(new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS,
				TableModelEvent.DELETE));
	}

	public void fireTableCellUpdated(int row, int column) {
		fireTableChanged(new TableModelEvent(this, row, row, column));
	}

	public void fireTableChanged(TableModelEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TableModelListener.class) {
				((TableModelListener) listeners[i + 1]).tableChanged(e);
			}
		}
	}

	public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
		return listenerList.getListeners(listenerType);
	}
}
