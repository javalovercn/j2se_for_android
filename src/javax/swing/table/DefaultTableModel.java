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
import java.util.Vector;

import javax.swing.event.TableModelEvent;

/**
 * This is an implementation of <code>TableModel</code> that uses a
 * <code>Vector</code> of <code>Vectors</code> to store the cell value objects.
 * <p>
 * <strong>Warning:</strong> <code>DefaultTableModel</code> returns a column
 * class of <code>Object</code>. When <code>DefaultTableModel</code> is used
 * with a <code>TableRowSorter</code> this will result in extensive use of
 * <code>toString</code>, which for non-<code>String</code> data types is
 * expensive. If you use <code>DefaultTableModel</code> with a
 * <code>TableRowSorter</code> you are strongly encouraged to override
 * <code>getColumnClass</code> to return the appropriate type.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Philip Milne
 *
 * @see TableModel
 * @see #getDataVector
 */
public class DefaultTableModel extends AbstractTableModel implements Serializable {
	protected Vector dataVector;
	protected Vector columnIdentifiers;

	public DefaultTableModel() {
		this(0, 0);
	}

	private static Vector newVector(int size) {
		Vector v = new Vector(size);
		v.setSize(size);
		return v;
	}

	public DefaultTableModel(int rowCount, int columnCount) {
		this(newVector(columnCount), rowCount);
	}

	public DefaultTableModel(Vector columnNames, int rowCount) {
		setDataVector(newVector(rowCount), columnNames);
	}

	public DefaultTableModel(Object[] columnNames, int rowCount) {
		this(convertToVector(columnNames), rowCount);
	}

	public DefaultTableModel(Vector data, Vector columnNames) {
		setDataVector(data, columnNames);
	}

	public DefaultTableModel(Object[][] data, Object[] columnNames) {
		setDataVector(data, columnNames);
	}

	public Vector getDataVector() {
		return dataVector;
	}

	private static Vector nonNullVector(Vector v) {
		return (v != null) ? v : new Vector();
	}

	public void setDataVector(Vector dataVector, Vector columnIdentifiers) {
		synchronized (this) {
			this.dataVector = nonNullVector(dataVector);
			this.columnIdentifiers = nonNullVector(columnIdentifiers);
		}

		resizeDataStoreRows(0, getRowCount());
		fireTableStructureChanged();
	}

	public void setDataVector(Object[][] dataVector, Object[] columnIdentifiers) {
		setDataVector(convertToVector(dataVector), convertToVector(columnIdentifiers));
	}

	/**
	 * Equivalent to fireTableChanged.
	 */
	public void newDataAvailable(TableModelEvent event) {
		fireTableChanged(event);
	}

	private void resizeDataStoreRows(int from, int to) {
		synchronized (this) {
			dataVector.setSize(getRowCount());

			for (int i = from; i < to; i++) {
				if (dataVector.elementAt(i) == null) {
					dataVector.setElementAt(new Vector(), i);
				}
				((Vector) dataVector.elementAt(i)).setSize(getColumnCount());
			}
		}
	}

	public void newRowsAdded(TableModelEvent e) {
		resizeDataStoreRows(e.getFirstRow(), e.getLastRow() + 1);
		fireTableChanged(e);
	}

	/**
	 * Equivalent to fireTableChanged.
	 */
	public void rowsRemoved(TableModelEvent event) {
		fireTableChanged(event);
	}

	/*
	 * @see #setRowCount
	 */
	public void setNumRows(int rowCount) {
		int old = getRowCount();
		if (old == rowCount) {
			return;
		}
		synchronized (this) {
			dataVector.setSize(rowCount);
		}
		if (rowCount <= old) {
			fireTableRowsDeleted(rowCount, old - 1);
		} else {
			resizeDataStoreRows(old, rowCount);
			fireTableRowsInserted(old, rowCount - 1);
		}
	}

	public void setRowCount(int rowCount) {
		setNumRows(rowCount);
	}

	/**
	 * add a row to the end of the model.
	 */
	public void addRow(Vector rowData) {
		insertRow(getRowCount(), rowData);
	}

	public void addRow(Object[] rowData) {
		addRow(convertToVector(rowData));
	}

	/**
	 * Inserts a row in the model.
	 */
	public void insertRow(int row, Vector rowData) {
		synchronized (this) {
			dataVector.insertElementAt(rowData, row);
		}
		resizeDataStoreRows(row, row + 1);
		fireTableRowsInserted(row, row);
	}

	public void insertRow(int row, Object[] rowData) {
		insertRow(row, convertToVector(rowData));
	}

	private static int gcd(int i, int j) {
		return (j == 0) ? i : gcd(j, i % j);
	}

	private static void rotate(Vector v, int a, int b, int shift) {
		int size = b - a;
		int r = size - shift;
		int g = gcd(size, r);
		for (int i = 0; i < g; i++) {
			int to = i;
			Object tmp = v.elementAt(a + to);
			for (int from = (to + r) % size; from != i; from = (to + r) % size) {
				v.setElementAt(v.elementAt(a + from), a + to);
				to = from;
			}
			v.setElementAt(tmp, a + to);
		}
	}

	/**
	 * Examples of moves: <br>
	 * 1. moveRow(1,3,5); a|B|C|D|e|f|g|h|i|j|k - before a|e|f|g|h|B|C|D|i|j|k -
	 * after <br>
	 * 2. moveRow(6,7,1); a|b|c|d|e|f|G|H|i|j|k - before a|G|H|b|c|d|e|f|i|j|k -
	 * after
	 */
	public void moveRow(int start, int end, int to) {
		int first, last;
		synchronized (this) {
			int shift = to - start;
			if (shift < 0) {
				first = to;
				last = end;
			} else {
				first = start;
				last = to + end - start;
			}
			rotate(dataVector, first, last + 1, shift);
		}
		fireTableRowsUpdated(first, last);
	}

	public void removeRow(int row) {
		synchronized (this) {
			dataVector.removeElementAt(row);
		}
		fireTableRowsDeleted(row, row);
	}

	public void setColumnIdentifiers(Vector columnIdentifiers) {
		setDataVector(dataVector, columnIdentifiers);
	}

	public void setColumnIdentifiers(Object[] newIdentifiers) {
		setColumnIdentifiers(convertToVector(newIdentifiers));
	}

	public void setColumnCount(int columnCount) {
		columnIdentifiers.setSize(columnCount);
		resizeDataStoreRows(0, getRowCount());
		fireTableStructureChanged();
	}

	public void addColumn(Object columnName) {
		addColumn(columnName, (Vector) null);
	}

	public void addColumn(Object columnName, Vector columnData) {
		synchronized (this) {
			columnIdentifiers.addElement(columnName);
			if (columnData != null) {
				int columnSize = columnData.size();
				if (columnSize > getRowCount()) {
					dataVector.setSize(columnSize);
				}
				resizeDataStoreRows(0, getRowCount());
				int newColumn = getColumnCount() - 1;
				for (int i = 0; i < columnSize; i++) {
					Vector row = (Vector) dataVector.elementAt(i);
					row.setElementAt(columnData.elementAt(i), newColumn);
				}
			} else {
				resizeDataStoreRows(0, getRowCount());
			}
		}

		fireTableStructureChanged();
	}

	public void addColumn(Object columnName, Object[] columnData) {
		addColumn(columnName, convertToVector(columnData));
	}

	public int getRowCount() {
		synchronized (this) {
			return dataVector.size();
		}
	}

	public int getColumnCount() {
		synchronized (this) {
			return columnIdentifiers.size();
		}
	}

	public String getColumnName(int column) {
		synchronized (this) {
			Object id = null;
			if (column < columnIdentifiers.size() && (column >= 0)) {
				id = columnIdentifiers.elementAt(column);
			}
			return (id == null) ? super.getColumnName(column) : id.toString();
		}
	}

	public boolean isCellEditable(int row, int column) {
		return true;
	}

	public Object getValueAt(int row, int column) {
		synchronized (this) {
			Vector rowVector = (Vector) dataVector.elementAt(row);
			if (rowVector == null) {
				return null;
			}
			return rowVector.elementAt(column);
		}
	}

	public void setValueAt(Object aValue, int row, int column) {
		synchronized (this) {
			Vector rowVector = (Vector) dataVector.elementAt(row);
			rowVector.setElementAt(aValue, column);
		}
		fireTableCellUpdated(row, column);
	}

	protected static Vector convertToVector(Object[] anArray) {
		if (anArray == null) {
			return null;
		}
		Vector<Object> v = new Vector<Object>(anArray.length);
		for (Object o : anArray) {
			v.addElement(o);
		}
		return v;
	}

	protected static Vector convertToVector(Object[][] anArray) {
		if (anArray == null) {
			return null;
		}
		Vector<Vector> v = new Vector<Vector>(anArray.length);
		for (Object[] o : anArray) {
			v.addElement(convertToVector(o));
		}
		return v;
	}

}