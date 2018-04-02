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

import javax.swing.table.TableModel;

/**
 * TableModelEvent is used to notify listeners that a table model has changed.
 * The model event describes changes to a TableModel and all references to rows
 * and columns are in the co-ordinate system of the model. Depending on the
 * parameters used in the constructors, the TableModelevent can be used to
 * specify the following types of changes:
 * <p>
 *
 * <pre>
 * TableModelEvent(source); // The data, ie. all rows changed
 * TableModelEvent(source, HEADER_ROW); // Structure change, reallocate
 * 										// TableColumns
 * TableModelEvent(source, 1); // Row 1 changed
 * TableModelEvent(source, 3, 6); // Rows 3 to 6 inclusive changed
 * TableModelEvent(source, 2, 2, 6); // Cell at (2, 6) changed
 * TableModelEvent(source, 3, 6, ALL_COLUMNS, INSERT); // Rows (3, 6) were
 * 													// inserted
 * TableModelEvent(source, 3, 6, ALL_COLUMNS, DELETE); // Rows (3, 6) were
 * 													// deleted
 * </pre>
 *
 * It is possible to use other combinations of the parameters, not all of them
 * are meaningful. By subclassing, you can add other information, for example:
 * whether the event WILL happen or DID happen. This makes the specification of
 * rows in DELETE events more useful but has not been included in the swing
 * package as the JTable only needs post-event notification.
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
 * @see TableModel
 */
public class TableModelEvent extends java.util.EventObject {
	public static final int INSERT = 1;
	public static final int UPDATE = 0;
	public static final int DELETE = -1;

	public static final int HEADER_ROW = -1;

	public static final int ALL_COLUMNS = -1;

	protected int type;
	protected int firstRow;
	protected int lastRow;
	protected int column;

	public TableModelEvent(TableModel source) {
		this(source, 0, Integer.MAX_VALUE, ALL_COLUMNS, UPDATE);
	}

	public TableModelEvent(TableModel source, int row) {
		this(source, row, row, ALL_COLUMNS, UPDATE);
	}

	public TableModelEvent(TableModel source, int firstRow, int lastRow) {
		this(source, firstRow, lastRow, ALL_COLUMNS, UPDATE);
	}

	public TableModelEvent(TableModel source, int firstRow, int lastRow, int column) {
		this(source, firstRow, lastRow, column, UPDATE);
	}

	public TableModelEvent(TableModel source, int firstRow, int lastRow, int column, int type) {
		super(source);
		this.firstRow = firstRow;
		this.lastRow = lastRow;
		this.column = column;
		this.type = type;
	}

	public int getFirstRow() {
		return firstRow;
	};

	public int getLastRow() {
		return lastRow;
	};

	public int getColumn() {
		return column;
	};

	public int getType() {
		return type;
	}
}
