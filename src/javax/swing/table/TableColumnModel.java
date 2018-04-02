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

import java.util.Enumeration;

import javax.swing.ListSelectionModel;
import javax.swing.event.TableColumnModelListener;

/**
 * Defines the requirements for a table column model object suitable for use
 * with <code>JTable</code>.
 *
 * @author Alan Chung
 * @author Philip Milne
 * @see DefaultTableColumnModel
 */
public interface TableColumnModel {
	public void addColumn(TableColumn aColumn);

	public void removeColumn(TableColumn column);

	public void moveColumn(int columnIndex, int newIndex);

	public void setColumnMargin(int newMargin);

	public int getColumnCount();

	public Enumeration<TableColumn> getColumns();

	public int getColumnIndex(Object columnIdentifier);

	public TableColumn getColumn(int columnIndex);

	public int getColumnMargin();

	public int getColumnIndexAtX(int xPosition);

	public int getTotalColumnWidth();

	public void setColumnSelectionAllowed(boolean flag);

	public boolean getColumnSelectionAllowed();

	public int[] getSelectedColumns();

	public int getSelectedColumnCount();

	public void setSelectionModel(ListSelectionModel newModel);

	public ListSelectionModel getSelectionModel();

	public void addColumnModelListener(TableColumnModelListener x);

	public void removeColumnModelListener(TableColumnModelListener x);
}
