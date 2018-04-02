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

import javax.swing.event.TableModelListener;

/**
 * The <code>TableModel</code> interface specifies the methods the
 * <code>JTable</code> will use to interrogate a tabular data model.
 * <p>
 *
 * The <code>JTable</code> can be set up to display any data model which
 * implements the <code>TableModel</code> interface with a couple of lines of
 * code:
 * <p>
 * 
 * <pre>
 * TableModel myData = new MyTableModel();
 * JTable table = new JTable(myData);
 * </pre>
 * <p>
 *
 * For further documentation, see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/table.html#data">Creating
 * a Table Model</a> in <em>The Java Tutorial</em>.
 * <p>
 * 
 * @author Philip Milne
 * @see JTable
 */
public interface TableModel {
	public int getRowCount();

	public int getColumnCount();

	public String getColumnName(int columnIndex);

	public Class<?> getColumnClass(int columnIndex);

	public boolean isCellEditable(int rowIndex, int columnIndex);

	public Object getValueAt(int rowIndex, int columnIndex);

	public void setValueAt(Object aValue, int rowIndex, int columnIndex);

	public void addTableModelListener(TableModelListener l);

	public void removeTableModelListener(TableModelListener l);
}
