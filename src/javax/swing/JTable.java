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
package javax.swing;

import hc.android.ActivityManager;
import hc.android.AndroidClassUtil;
import hc.android.CanvasGraphics;
import hc.android.AndroidUIUtil;
import hc.core.util.LogManager;
import hc.util.ResourceUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Vector;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.print.PrintService;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.TableUI;
import javax.swing.plaf.UIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent.PointerCoords;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import hc.android.HCRUtil;

/**
 * The <code>JTable</code> is used to display and edit regular two-dimensional
 * tables of cells. See <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/table.html">How
 * to Use Tables</a> in <em>The Java Tutorial</em> for task-oriented
 * documentation and examples of using <code>JTable</code>.
 *
 * <p>
 * The <code>JTable</code> has many facilities that make it possible to
 * customize its rendering and editing but provides defaults for these features
 * so that simple tables can be set up easily. For example, to set up a table
 * with 10 rows and 10 columns of numbers:
 * <p>
 * 
 * <pre>
 * TableModel dataModel = new AbstractTableModel() {
 * 	public int getColumnCount() {
 * 		return 10;
 * 	}
 * 
 * 	public int getRowCount() {
 * 		return 10;
 * 	}
 * 
 * 	public Object getValueAt(int row, int col) {
 * 		return new Integer(row * col);
 * 	}
 * };
 * JTable table = new JTable(dataModel);
 * JScrollPane scrollpane = new JScrollPane(table);
 * </pre>
 * <p>
 * {@code JTable}s are typically placed inside of a {@code JScrollPane}. By
 * default, a {@code JTable} will adjust its width such that a horizontal
 * scrollbar is unnecessary. To allow for a horizontal scrollbar, invoke
 * {@link #setAutoResizeMode} with {@code AUTO_RESIZE_OFF}. Note that if you
 * wish to use a <code>JTable</code> in a standalone view (outside of a
 * <code>JScrollPane</code>) and want the header displayed, you can get it using
 * {@link #getTableHeader} and display it separately.
 * <p>
 * To enable sorting and filtering of rows, use a {@code RowSorter}. You can set
 * up a row sorter in either of two ways:
 * <ul>
 * <li>Directly set the {@code RowSorter}. For example:
 * {@code table.setRowSorter(new TableRowSorter(model))}.
 * <li>Set the {@code autoCreateRowSorter} property to {@code true}, so that the
 * {@code JTable} creates a {@code RowSorter} for you. For example:
 * {@code setAutoCreateRowSorter(true)}.
 * </ul>
 * <p>
 * When designing applications that use the <code>JTable</code> it is worth
 * paying close attention to the data structures that will represent the table's
 * data. The <code>DefaultTableModel</code> is a model implementation that uses
 * a <code>Vector</code> of <code>Vector</code>s of <code>Object</code>s to
 * store the cell values. As well as copying the data from an application into
 * the <code>DefaultTableModel</code>, it is also possible to wrap the data in
 * the methods of the <code>TableModel</code> interface so that the data can be
 * passed to the <code>JTable</code> directly, as in the example above. This
 * often results in more efficient applications because the model is free to
 * choose the internal representation that best suits the data. A good rule of
 * thumb for deciding whether to use the <code>AbstractTableModel</code> or the
 * <code>DefaultTableModel</code> is to use the <code>AbstractTableModel</code>
 * as the base class for creating subclasses and the
 * <code>DefaultTableModel</code> when subclassing is not required.
 * <p>
 * The "TableExample" directory in the demo area of the source distribution
 * gives a number of complete examples of <code>JTable</code> usage, covering
 * how the <code>JTable</code> can be used to provide an editable view of data
 * taken from a database and how to modify the columns in the display to use
 * specialized renderers and editors.
 * <p>
 * The <code>JTable</code> uses integers exclusively to refer to both the rows
 * and the columns of the model that it displays. The <code>JTable</code> simply
 * takes a tabular range of cells and uses <code>getValueAt(int, int)</code> to
 * retrieve the values from the model during painting. It is important to
 * remember that the column and row indexes returned by various
 * <code>JTable</code> methods are in terms of the <code>JTable</code> (the
 * view) and are not necessarily the same indexes used by the model.
 * <p>
 * By default, columns may be rearranged in the <code>JTable</code> so that the
 * view's columns appear in a different order to the columns in the model. This
 * does not affect the implementation of the model at all: when the columns are
 * reordered, the <code>JTable</code> maintains the new order of the columns
 * internally and converts its column indices before querying the model.
 * <p>
 * So, when writing a <code>TableModel</code>, it is not necessary to listen for
 * column reordering events as the model will be queried in its own coordinate
 * system regardless of what is happening in the view. In the examples area
 * there is a demonstration of a sorting algorithm making use of exactly this
 * technique to interpose yet another coordinate system where the order of the
 * rows is changed, rather than the order of the columns.
 * <p>
 * Similarly when using the sorting and filtering functionality provided by
 * <code>RowSorter</code> the underlying <code>TableModel</code> does not need
 * to know how to do sorting, rather <code>RowSorter</code> will handle it.
 * Coordinate conversions will be necessary when using the row based methods of
 * <code>JTable</code> with the underlying <code>TableModel</code>. All of
 * <code>JTable</code>s row based methods are in terms of the
 * <code>RowSorter</code>, which is not necessarily the same as that of the
 * underlying <code>TableModel</code>. For example, the selection is always in
 * terms of <code>JTable</code> so that when using <code>RowSorter</code> you
 * will need to convert using <code>convertRowIndexToView</code> or
 * <code>convertRowIndexToModel</code>. The following shows how to convert
 * coordinates from <code>JTable</code> to that of the underlying model:
 * 
 * <pre>
 * int[] selection = table.getSelectedRows();
 * for (int i = 0; i &lt; selection.length; i++) {
 * 	selection[i] = table.convertRowIndexToModel(selection[i]);
 * }
 * // selection is now in terms of the underlying TableModel
 * </pre>
 * <p>
 * By default if sorting is enabled <code>JTable</code> will persist the
 * selection and variable row heights in terms of the model on sorting. For
 * example if row 0, in terms of the underlying model, is currently selected,
 * after the sort row 0, in terms of the underlying model will be selected.
 * Visually the selection may change, but in terms of the underlying model it
 * will remain the same. The one exception to that is if the model index is no
 * longer visible or was removed. For example, if row 0 in terms of model was
 * filtered out the selection will be empty after the sort.
 * <p>
 * J2SE 5 adds methods to <code>JTable</code> to provide convenient access to
 * some common printing needs. Simple new {@link #print()} methods allow for
 * quick and easy addition of printing support to your application. In addition,
 * a new {@link #getPrintable} method is available for more advanced printing
 * needs.
 * <p>
 * As for all <code>JComponent</code> classes, you can use {@link InputMap} and
 * {@link ActionMap} to associate an {@link Action} object with a
 * {@link KeyStroke} and execute the action under specified conditions.
 * <p>
 * <strong>Warning:</strong> Swing is not thread safe. For more information see
 * <a href="package-summary.html#threading">Swing's Threading Policy</a>.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 *
 * @beaninfo attribute: isContainer false description: A component which
 *           displays data in a two dimensional grid.
 *
 * @author Philip Milne
 * @author Shannon Hickey (printing support)
 * @see javax.swing.table.DefaultTableModel
 * @see javax.swing.table.TableRowSorter
 */
/*
 * The first versions of the JTable, contained in Swing-0.1 through Swing-0.4,
 * were written by Alan Chung.
 */
public class JTable extends JComponent
		implements TableModelListener, Scrollable, TableColumnModelListener, ListSelectionListener,
		CellEditorListener, Accessible, RowSorterListener {
	private static final String uiClassID = "TableUI";
	private final int gridLineWidth = 1;
	private TableLayout tableView;
	private boolean isShowSelectedColumn = true;
	private TableRow columnHeaderRow;
	protected boolean rowSelectionAllowed;
	public static final int AUTO_RESIZE_OFF = 0;
	public static final int AUTO_RESIZE_NEXT_COLUMN = 1;
	public static final int AUTO_RESIZE_SUBSEQUENT_COLUMNS = 2;
	public static final int AUTO_RESIZE_LAST_COLUMN = 3;
	public static final int AUTO_RESIZE_ALL_COLUMNS = 4;

	public enum PrintMode {
		NORMAL, FIT_WIDTH
	}

	protected TableModel dataModel;
	protected TableColumnModel columnModel;
	protected ListSelectionModel selectionModel;
	protected JTableHeader tableHeader;
	protected int rowHeight;

	/** margin between the cells in each row. */
	protected int rowMargin;
	protected Color gridColor = AndroidUIUtil.WINDOW_TABLE_BODY_CELL_BORDER_COLOR;

	protected int autoResizeMode;
	protected boolean autoCreateColumnsFromModel;

	protected Dimension preferredViewportSize;

	/*
	 * If true, both a row selection and a column selection can be non-empty at
	 * the same time, the selected cells are the the cells whose row and column
	 * are both selected.
	 */
	protected boolean cellSelectionEnabled;

	transient protected HashMap<Class, TableCellRenderer> defaultRenderersByColumnClass;
	transient protected HashMap<Class, TableCellEditor> defaultEditorsByColumnClass;

	protected Color selectionForeground = AndroidUIUtil.WIN_FONT_COLOR;
	protected Color selectionBackground;// = UIUtil.WIN_BODY_SELECTED_BACK;
	protected Color defaultForeground = AndroidUIUtil.WIN_UNSELECTED_FONT_COLOR;
	protected Color defaultBackground;// = UIUtil.WIN_BODY_BACK;

	private SizeSequence rowModel;
	private boolean columnSelectionAdjusting;
	/**
	 * The last value of getValueIsAdjusting from the row selection models
	 * valueChanged notification. Used to test if a repaint is needed.
	 */
	private boolean rowSelectionAdjusting;

	private boolean fillsViewportHeight;
	private DropMode dropMode = DropMode.USE_SELECTION;

	public static final class DropLocation extends TransferHandler.DropLocation {
		private final int row;
		private final int col;
		private final boolean isInsertRow;
		private final boolean isInsertCol;

		private DropLocation(Point p, int row, int col, boolean isInsertRow, boolean isInsertCol) {

			super(p);
			this.row = row;
			this.col = col;
			this.isInsertRow = isInsertRow;
			this.isInsertCol = isInsertCol;
		}

		public int getRow() {
			return row;
		}

		public int getColumn() {
			return col;
		}

		public boolean isInsertRow() {
			return isInsertRow;
		}

		public boolean isInsertColumn() {
			return isInsertCol;
		}

		public String toString() {
			return getClass().getName() + "[row=" + row + "," + "column=" + col + "," + "insertRow="
					+ isInsertRow + "," + "insertColumn=" + isInsertCol + "]";
		}
	}

	public JTable() {
		this(null, null, null);
	}

	public JTable(TableModel tm) {
		this(tm, null, null);
	}

	public JTable(TableModel tm, TableColumnModel cm) {
		this(tm, cm, null);
	}

	public JTable(TableModel tableModel, TableColumnModel columnModel,
			ListSelectionModel selectionMode) {
		super();

		createDefaultRenderers();
		setLayout(null);

		if (columnModel == null) {
			columnModel = createDefaultColumnModel();
			autoCreateColumnsFromModel = true;
		}
		setColumnModel(columnModel);

		if (selectionMode == null) {
			selectionMode = createDefaultSelectionModel();
		}
		setSelectionModel(selectionMode);

		initializeLocalVars();

		if (tableModel == null) {
			tableModel = createDefaultDataModel();
		}
		setModel(tableModel);

		super.setPeerAdAPI(null);
	}

	@Override
	public synchronized View getPeerAdAPI() {
		if (tableView == null) {
			AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
				@Override
				public void run() {
					tableView = new TableLayout(ActivityManager.applicationContext);
					tableView.setStretchAllColumns(true);// 可拉伸，即宽度可被拉伸以填充全屏
					setPeerAdAPI(tableView);

					updateUI();
				}
			});
		}
		return super.getPeerAdAPI();
	}

	public JTable(int numRows, int numColumns) {
		this(new DefaultTableModel(numRows, numColumns));
	}

	public JTable(Vector rowData, Vector columnNames) {
		this(new DefaultTableModel(rowData, columnNames));
	}

	public JTable(final Object[][] rowData, final Object[] columnNames) {
		this(new AbstractTableModel() {
			public String getColumnName(int column) {
				return columnNames[column].toString();
			}

			public int getRowCount() {
				return rowData.length;
			}

			public int getColumnCount() {
				return columnNames.length;
			}

			public Object getValueAt(int row, int col) {
				return rowData[row][col];
			}

			public boolean isCellEditable(int row, int column) {
				return false;
			}

			public void setValueAt(Object value, int row, int col) {
				rowData[row][col] = value;
				fireTableCellUpdated(row, col);
			}
		});
	}

	public void addNotify() {
		super.addNotify();
		configureEnclosingScrollPane();
	}

	protected void configureEnclosingScrollPane() {
		AndroidClassUtil.callEmptyMethod();
	}

	private void configureEnclosingScrollPaneUI() {
		AndroidClassUtil.callEmptyMethod();
	}

	public void removeNotify() {
	}

	protected void unconfigureEnclosingScrollPane() {
		AndroidClassUtil.callEmptyMethod();
	}

	void setUIProperty(String propertyName, Object value) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Deprecated
	static public JScrollPane createScrollPaneForTable(JTable table) {
		return new JScrollPane(table);
	}

	public void setTableHeader(JTableHeader tableHeader) {
		if (this.tableHeader != tableHeader) {
			JTableHeader old = this.tableHeader;
			if (old != null) {
				old.setTable(null);
			}
			this.tableHeader = tableHeader;
			if (tableHeader != null) {
				tableHeader.setTable(this);
				updateUI();// added by yyh
			}
		}
	}

	public JTableHeader getTableHeader() {
		AndroidClassUtil.callEmptyMethod();
		return tableHeader;
	}

	public void setRowHeight(int rowHeight) {
		if (rowHeight <= 0) {
			throw new IllegalArgumentException("New row height less than 1");
		}
		AndroidClassUtil.callEmptyMethod();
	}

	public int getRowHeight() {
		return rowHeight;
	}

	private SizeSequence getRowModel() {
		if (rowModel == null) {
			rowModel = new SizeSequence(getRowCount(), getRowHeight());
		}
		return rowModel;
	}

	public void setRowHeight(int row, int rowHeight) {
		if (rowHeight <= 0) {
			throw new IllegalArgumentException("New row height less than 1");
		}
		AndroidClassUtil.callEmptyMethod();
		// getRowModel().setSize(row, rowHeight);
		// resizeAndRepaint();
	}

	public int getRowHeight(int row) {
		return (rowModel == null) ? getRowHeight() : rowModel.getSize(row);
	}

	public void setRowMargin(int rowMargin) {
		AndroidClassUtil.callEmptyMethod();
		// int old = this.rowMargin;
		// this.rowMargin = rowMargin;
		// resizeAndRepaint();
		// firePropertyChange("rowMargin", old, rowMargin);
	}

	public int getRowMargin() {
		return rowMargin;
	}

	public void setIntercellSpacing(Dimension intercellSpacing) {
		AndroidClassUtil.callEmptyMethod();
		// Set the rowMargin here and columnMargin in the TableColumnModel
		// setRowMargin(intercellSpacing.height);
		// getColumnModel().setColumnMargin(intercellSpacing.width);
		//
		// resizeAndRepaint();
	}

	public Dimension getIntercellSpacing() {
		AndroidClassUtil.callEmptyMethod();
		return new Dimension(getColumnModel().getColumnMargin(), rowMargin);
	}

	public void setGridColor(Color gridColor) {
		if (gridColor == null) {
			throw new IllegalArgumentException("New color is null");
		}
		this.gridColor = gridColor;
		isForceRefreshTable = true;
		repaint();
	}

	/**
	 * Overrided by yyh
	 */
	public void repaint() {
		isForceRefreshTable = true;
		updateUI();
	}

	public Color getGridColor() {
		return gridColor;
	}

	public void setShowGrid(boolean showGrid) {
		AndroidClassUtil.callEmptyMethod();

		// setShowHorizontalLines(showGrid);
		// setShowVerticalLines(showGrid);
		//
		// repaint();
	}

	public void setShowHorizontalLines(boolean showHorizontalLines) {
		AndroidClassUtil.callEmptyMethod();
		// boolean old = this.showHorizontalLines;
		// this.showHorizontalLines = showHorizontalLines;
		// firePropertyChange("showHorizontalLines", old, showHorizontalLines);
		//
		// repaint();
	}

	public void setShowVerticalLines(boolean showVerticalLines) {
		AndroidClassUtil.callEmptyMethod();
		// boolean old = this.showVerticalLines;
		// this.showVerticalLines = showVerticalLines;
		// firePropertyChange("showVerticalLines", old, showVerticalLines);
		// repaint();
	}

	public boolean getShowHorizontalLines() {
		return true;
		// return showHorizontalLines;
	}

	public boolean getShowVerticalLines() {
		return true;
		// return showVerticalLines;
	}

	public void setAutoResizeMode(int mode) {
		AndroidClassUtil.callEmptyMethod();
	}

	public int getAutoResizeMode() {
		return autoResizeMode;
	}

	public void setAutoCreateColumnsFromModel(boolean autoCreateColumnsFromModel) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getAutoCreateColumnsFromModel() {
		return autoCreateColumnsFromModel;
	}

	public void createDefaultColumnsFromModel() {
		TableModel m = getModel();
		if (m != null) {
			TableColumnModel cm = getColumnModel();
			while (cm.getColumnCount() > 0) {
				cm.removeColumn(cm.getColumn(0));
			}

			for (int i = 0; i < m.getColumnCount(); i++) {
				TableColumn newColumn = new TableColumn(i);
				addColumn(newColumn);
			}
		}
	}

	/**
	 * @param columnClass
	 * @param renderer
	 *            null to remove old
	 */
	public void setDefaultRenderer(Class<?> columnClass, TableCellRenderer renderer) {
		if (renderer != null) {
			defaultRenderersByColumnClass.put(columnClass, renderer);
		} else {
			defaultRenderersByColumnClass.remove(columnClass);
		}
	}

	public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
		if (columnClass == null) {
			return null;
		} else {
			Object renderer = defaultRenderersByColumnClass.get(columnClass);
			if (renderer != null) {
				return (TableCellRenderer) renderer;
			} else {
				Class c = columnClass.getSuperclass();
				if (c == null && columnClass != Object.class) {
					c = Object.class;
				}
				return getDefaultRenderer(c);
			}
		}
	}

	public void setDefaultEditor(Class<?> columnClass, TableCellEditor editor) {
		AndroidClassUtil.callEmptyMethod();

		// if (editor != null) {
		// defaultEditorsByColumnClass.put(columnClass, editor);
		// } else {
		// defaultEditorsByColumnClass.remove(columnClass);
		// }
	}

	public TableCellEditor getDefaultEditor(Class<?> columnClass) {
		AndroidClassUtil.callEmptyMethod();
		return new GenericEditor();

		// if (columnClass == null) {
		// return null;
		// }
		// else {
		// Object editor = defaultEditorsByColumnClass.get(columnClass);
		// if (editor != null) {
		// return (TableCellEditor)editor;
		// }
		// else {
		// return getDefaultEditor(columnClass.getSuperclass());
		// }
		// }
	}

	public void setDragEnabled(boolean b) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getDragEnabled() {
		return false;
	}

	public final void setDropMode(DropMode dropMode) {
		AndroidClassUtil.callEmptyMethod();

		if (dropMode != null) {
			switch (dropMode) {
			case USE_SELECTION:
			case ON:
			case INSERT:
			case INSERT_ROWS:
			case INSERT_COLS:
			case ON_OR_INSERT:
			case ON_OR_INSERT_ROWS:
			case ON_OR_INSERT_COLS:
				this.dropMode = dropMode;
				return;
			}
		}

		throw new IllegalArgumentException(dropMode + ": Unsupported drop mode for table");
	}

	public final DropMode getDropMode() {
		return dropMode;
	}

	DropLocation dropLocationForPoint(Point p) {
		return null;
	}

	Object setDropLocation(TransferHandler.DropLocation location, Object state, boolean forDrop) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public final DropLocation getDropLocation() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setAutoCreateRowSorter(boolean autoCreateRowSorter) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getAutoCreateRowSorter() {
		return false;
	}

	public void setUpdateSelectionOnSort(boolean update) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getUpdateSelectionOnSort() {
		return false;
	}

	public void setRowSorter(RowSorter<? extends TableModel> sorter) {
		AndroidClassUtil.callEmptyMethod();
	}

	public RowSorter<? extends TableModel> getRowSorter() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	/**
	 * SINGLE_SELECTION is supported
	 * 
	 * @param selectionMode
	 */
	public void setSelectionMode(int selectionMode) {
		if (selectionMode != ListSelectionModel.SINGLE_SELECTION) {
			LogManager.warning("only SINGLE_SELECTION is supported and is default");
		}
		selectionMode = ListSelectionModel.SINGLE_SELECTION;

		clearSelection();
		getSelectionModel().setSelectionMode(selectionMode);
		getColumnModel().getSelectionModel().setSelectionMode(selectionMode);
	}

	public void setRowSelectionAllowed(boolean rowSelectionAllowed) {
		boolean old = this.rowSelectionAllowed;
		this.rowSelectionAllowed = rowSelectionAllowed;
		if (old != rowSelectionAllowed) {
			isForceRefreshTable = true;
			repaint();
		}
	}

	public boolean getRowSelectionAllowed() {
		return rowSelectionAllowed;
	}

	public void setColumnSelectionAllowed(boolean columnSelectionAllowed) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getColumnSelectionAllowed() {
		return false;
	}

	public void setCellSelectionEnabled(boolean cellSelectionEnabled) {
		AndroidClassUtil.callEmptyMethod();
		// setRowSelectionAllowed(cellSelectionEnabled);
		// setColumnSelectionAllowed(cellSelectionEnabled);
		// boolean old = this.cellSelectionEnabled;
		// this.cellSelectionEnabled = cellSelectionEnabled;
		// firePropertyChange("cellSelectionEnabled", old,
		// cellSelectionEnabled);
	}

	public boolean getCellSelectionEnabled() {
		return getRowSelectionAllowed() && getColumnSelectionAllowed();
	}

	/**
	 * Selects all rows, columns, and cells in the table.
	 */
	public void selectAll() {
		AndroidClassUtil.callEmptyMethod();
	}

	public void clearSelection() {
		selectionModel.clearSelection();
		columnModel.getSelectionModel().clearSelection();
	}

	private void clearSelectionAndLeadAnchor() {
		selectionModel.setValueIsAdjusting(true);
		columnModel.getSelectionModel().setValueIsAdjusting(true);

		clearSelection();

		selectionModel.setAnchorSelectionIndex(-1);
		selectionModel.setLeadSelectionIndex(-1);
		columnModel.getSelectionModel().setAnchorSelectionIndex(-1);
		columnModel.getSelectionModel().setLeadSelectionIndex(-1);

		selectionModel.setValueIsAdjusting(false);
		columnModel.getSelectionModel().setValueIsAdjusting(false);
	}

	private int getAdjustedIndex(int index, boolean row) {
		int compare = row ? getRowCount() : getColumnCount();
		return index < compare ? index : -1;
	}

	private int boundRow(int row) throws IllegalArgumentException {
		if (row < 0 || row >= getRowCount()) {
			throw new IllegalArgumentException("Row index out of range");
		}
		return row;
	}

	private int boundColumn(int col) {
		if (col < 0 || col >= getColumnCount()) {
			throw new IllegalArgumentException("Column index out of range");
		}
		return col;
	}

	public void setRowSelectionInterval(int index0, int index1) {
		refreshRowIdx = index0;
		selectionModel.setSelectionInterval(boundRow(index0), boundRow(index1));
	}

	public void setColumnSelectionInterval(int index0, int index1) {
		columnModel.getSelectionModel().setSelectionInterval(boundColumn(index0),
				boundColumn(index1));
	}

	public void addRowSelectionInterval(int index0, int index1) {
		refreshRowIdx = index0;
		selectionModel.addSelectionInterval(boundRow(index0), boundRow(index1));
	}

	public void addColumnSelectionInterval(int index0, int index1) {
		columnModel.getSelectionModel().addSelectionInterval(boundColumn(index0),
				boundColumn(index1));
	}

	public void removeRowSelectionInterval(int index0, int index1) {
		refreshRowIdx = index0;
		selectionModel.removeSelectionInterval(boundRow(index0), boundRow(index1));
	}

	public void removeColumnSelectionInterval(int index0, int index1) {
		columnModel.getSelectionModel().removeSelectionInterval(boundColumn(index0),
				boundColumn(index1));
	}

	public int getSelectedRow() {
		return selectionModel.getMinSelectionIndex();
	}

	public int getSelectedColumn() {
		return columnModel.getSelectionModel().getMinSelectionIndex();
	}

	public int[] getSelectedRows() {
		int[] rv = new int[0];
		rv[0] = getSelectedRow();
		return rv;
	}

	public int[] getSelectedColumns() {
		return columnModel.getSelectedColumns();
	}

	public int getSelectedRowCount() {
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

	public int getSelectedColumnCount() {
		return columnModel.getSelectedColumnCount();
	}

	public boolean isRowSelected(int row) {
		return getSelectedRow() == row || selectionModel.isSelectedIndex(row);
	}

	public boolean isColumnSelected(int column) {
		return columnModel.getSelectionModel().isSelectedIndex(column);
	}

	public boolean isCellSelected(int row, int column) {
		if (!getRowSelectionAllowed() && !getColumnSelectionAllowed()) {
			return false;
		}
		return (!getRowSelectionAllowed() || isRowSelected(row))
				&& (!getColumnSelectionAllowed() || isColumnSelected(column));
	}

	public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
	}

	public Color getSelectionForeground() {
		return selectionForeground;
	}

	public Color getForeground() {
		return defaultForeground;
	}

	public Color getBackground() {
		return defaultBackground;
	}

	public void setForeground(Color fg) {
		if (fg == null) {
			return;
		}

		defaultForeground = fg;
		isForceRefreshTable = true;
		repaint();
	}

	public void setBackground(Color bg) {
		if (bg == null) {
			return;
		}
		defaultBackground = bg;
		isForceRefreshTable = true;
		repaint();
	}

	public void setSelectionForeground(Color selectionForeground) {
		if (selectionForeground == null) {
			return;
		}

		this.selectionForeground = selectionForeground;
		isForceRefreshTable = true;
		repaint();
	}

	public Color getSelectionBackground() {
		return selectionBackground;
	}

	public void setSelectionBackground(Color selectionBackground) {
		if (selectionBackground == null) {
			return;
		}

		this.selectionBackground = selectionBackground;
		isForceRefreshTable = true;
		repaint();
	}

	public TableColumn getColumn(Object identifier) {
		TableColumnModel cm = getColumnModel();
		int columnIndex = cm.getColumnIndex(identifier);
		return cm.getColumn(columnIndex);
	}

	public int convertColumnIndexToModel(int viewColumnIndex) {
		return viewColumnIndex;
	}

	public int convertColumnIndexToView(int modelColumnIndex) {
		return modelColumnIndex;
	}

	public int convertRowIndexToView(int modelRowIndex) {
		return modelRowIndex;
	}

	public int convertRowIndexToModel(int viewRowIndex) {
		return viewRowIndex;
	}

	public int getRowCount() {
		return getModel().getRowCount();
	}

	public int getColumnCount() {
		return getColumnModel().getColumnCount();
	}

	public String getColumnName(int column) {
		return getModel().getColumnName(convertColumnIndexToModel(column));
	}

	public Class<?> getColumnClass(int column) {
		return getModel().getColumnClass(convertColumnIndexToModel(column));
	}

	public Object getValueAt(int row, int column) {
		return getModel().getValueAt(convertRowIndexToModel(row),
				convertColumnIndexToModel(column));
	}

	public void setValueAt(Object aValue, int row, int column) {
		getModel().setValueAt(aValue, convertRowIndexToModel(row),
				convertColumnIndexToModel(column));
	}

	public boolean isCellEditable(int row, int column) {
		return getModel().isCellEditable(convertRowIndexToModel(row),
				convertColumnIndexToModel(column));
	}

	public void addColumn(TableColumn aColumn) {
		if (aColumn.getHeaderValue() == null) {
			int modelColumn = aColumn.getModelIndex();
			String columnName = getModel().getColumnName(modelColumn);
			aColumn.setHeaderValue(columnName);
		}
		getColumnModel().addColumn(aColumn);
	}

	public void removeColumn(TableColumn aColumn) {
		getColumnModel().removeColumn(aColumn);
	}

	public void moveColumn(int column, int targetColumn) {
		getColumnModel().moveColumn(column, targetColumn);
	}

	public int columnAtPoint(Point point) {
		if (tableView == null) {
			return -1;
		}
		int[] winLoc = new int[2];
		int[] viewLoc = new int[2];
		tableView.getLocationInWindow(winLoc);
		TableRow tableRow = (TableRow) tableView.getChildAt(0);
		int columnNum = tableRow.getChildCount();
		for (int i = 1; i < columnNum; i++) {
			tableRow.getVirtualChildAt(i).getLocationInWindow(viewLoc);
			int shiftX = viewLoc[0] - winLoc[0];
			if (i == 1) {
				if (point.x < shiftX) {
					return -1;
				}
			}
			if (point.x < shiftX) {
				return i - 1;
			}
		}
		return columnNum - 1;
	}

	public int rowAtPoint(Point point) {
		if (tableView == null) {
			return -1;
		}
		int[] winLoc = new int[2];
		int[] viewLoc = new int[2];
		tableView.getLocationInWindow(winLoc);
		int rowNum = tableView.getChildCount();
		for (int i = 1; i < rowNum; i++) {
			TableRow tableRow = (TableRow) tableView.getChildAt(i);
			tableRow.getVirtualChildAt(0).getLocationInWindow(viewLoc);
			int shiftY = viewLoc[1] - winLoc[1];
			if (i == 1) {
				if (point.y < shiftY) {
					return -1;
				}
			}
			if (point.y < shiftY) {
				return i - 2;
			}
		}
		return rowNum - 2;
	}

	public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
		AndroidClassUtil.callEmptyMethod();
		return new Rectangle();
	}

	private int viewIndexForColumn(TableColumn aColumn) {
		TableColumnModel cm = getColumnModel();
		for (int column = 0; column < cm.getColumnCount(); column++) {
			if (cm.getColumn(column) == aColumn) {
				return column;
			}
		}
		return -1;
	}

	public void doLayout() {
	}

	private TableColumn getResizingColumn() {
		return (tableHeader == null) ? null : tableHeader.getResizingColumn();
	}

	public void sizeColumnsToFit(boolean lastColumnOnly) {
	}

	public void sizeColumnsToFit(int resizingColumn) {
	}

	private void setWidthsFromPreferredWidths(final boolean inverse) {
	}

	private void accommodateDelta(int resizingColumnIndex, int delta) {
	}

	private interface Resizable2 {
		public int getElementCount();

		public int getLowerBoundAt(int i);

		public int getUpperBoundAt(int i);

		public void setSizeAt(int newSize, int i);
	}

	private interface Resizable3 extends Resizable2 {
		public int getMidPointAt(int i);
	}

	private void adjustSizes(long target, final Resizable3 r, boolean inverse) {
	}

	private void adjustSizes(long target, Resizable2 r, boolean limitToRange) {
	}

	public String getToolTipText(MouseEvent event) {
		return "";
	}

	public void setSurrendersFocusOnKeystroke(boolean surrendersFocusOnKeystroke) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getSurrendersFocusOnKeystroke() {
		return false;
	}

	public boolean editCellAt(int row, int column) {
		return editCellAt(row, column, null);
	}

	public boolean editCellAt(int row, int column, EventObject e) {
		return false;
	}

	public boolean isEditing() {
		return false;
	}

	public Component getEditorComponent() {
		return null;
	}

	public int getEditingColumn() {
		return 0;
	}

	public int getEditingRow() {
		return 0;
	}

	public TableUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setUI(TableUI ui) {
		AndroidClassUtil.callEmptyMethod();
	}

	boolean isForceRefreshTable = true;
	int refreshRowIdx = -1;

	public void updateUI() {
		AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				TableModel lockTable = getModel();
				if (lockTable == null || tableView == null) {
					return;
				}
				synchronized (lockTable) {
					if (isForceRefreshTable == true || (refreshRowIdx < 0)
							|| selectColumnCache.get((Integer) refreshRowIdx) == null) {// 可能更新数据后，先触发rowSelected事件
						isForceRefreshTable = false;
						refreshRowIdx = -1;
						buildTableContentAdAPI(-1, getModel().getRowCount() + 1);
					} else {
						refreshRowAdAPI(refreshRowIdx);
						refreshRowIdx = -1;
					}
				}
			}
		});
	}

	private final void refreshRowAdAPI(int rowIdx) {
		refreshSelectedColumnAdAPI(rowIdx, selectColumnCache.get((Integer) rowIdx));

		final int ncols = getModel().getColumnCount();
		for (int j = 0; j < ncols; j++) {
			final String cacheKey = rowIdx + "_" + j;
			refreshCellContentAdAPI(rowIdx, j, imageViewCache.get(cacheKey));
		}
	}

	private final HashMap<String, ImageView> imageViewCache = new HashMap<String, ImageView>();
	private final HashMap<Integer, ImageView> selectColumnCache = new HashMap<Integer, ImageView>();

	private final int minDisplayRow = 0;

	/**
	 * @param refreshRowStartIndex
	 *            -1:tableHeader, 0:first data Row
	 * @param rowNumber
	 *            refresh row number after start refresh row index
	 */
	private void buildTableContentAdAPI(int refreshRowStartIndex, int rowNumber) {
		if (isForceRefreshTable == true) {
			updateUI();
			return;
		}

		final int ncols = getModel().getColumnCount();

		// 有可能删除一行
		if (rowNumber == getModel().getRowCount() + 1) {
			tableView.removeAllViews();
		}

		int endRefreshRowIdx = refreshRowStartIndex + rowNumber;
		if (refreshRowStartIndex == -1) {
			if (rowNumber < minDisplayRow) {
				endRefreshRowIdx = refreshRowStartIndex + minDisplayRow;
			}
		}
		for (int i = refreshRowStartIndex; i < endRefreshRowIdx; i++) {

			TableRow tableRow = null;

			final boolean isTitle = (i == -1);
			if (isTitle) {
				if (columnHeaderRow == null) {
					tableRow = new TableRow(ActivityManager.applicationContext);
					columnHeaderRow = tableRow;
				} else {
					tableRow = columnHeaderRow;
				}
				tableRow.removeAllViews();
			} else {
				tableRow = new TableRow(ActivityManager.applicationContext);
			}

			for (int j = (isShowSelectedColumn ? -1 : 0); j < ncols; j++) {
				final int rowTouchIdx = i;
				final int columnTouchIdx = j;

				ImageView rendererView;
				TableCellRenderer cellRenderer;

				if (isTitle) {
					// Colomn Header
					rendererView = new ImageView(ActivityManager.applicationContext);

					final String selectColumnName = (String) ResourceUtil.get(7003);
					Object value = (columnTouchIdx == -1) ? selectColumnName
							: getModel().getColumnName(columnTouchIdx);
					cellRenderer = getTableHeader().getDefaultRenderer();
					Component m = cellRenderer.getTableCellRendererComponent(this, value, false,
							false, 0, j);
					rendererView.setImageBitmap(AndroidUIUtil.getViewBitmap(m.getPeerAdAPI()));

					rendererView.setFocusable(false);
					rendererView.setFocusableInTouchMode(false);

					// Click on tableHeader
					if (false) {
						rendererView.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								// mousePressed
								// JComponent jComponent =
								// (JComponent)component;
								// {
								// MouseEvent swingMouseEvent =
								// new MouseEvent(component,
								// MouseEvent.MOUSE_PRESSED,
								// System.currentTimeMillis(), 0, 0, 0, 0,
								// false, MouseEvent.BUTTON1);
								// jComponent.processMouseEventAdAPI(swingMouseEvent);
								// }

								// mouseReleased
								// {
								// MouseEvent swingMouseEvent =
								// new MouseEvent(component,
								// MouseEvent.MOUSE_RELEASED,
								// System.currentTimeMillis(), 0, 0, 0, 0,
								// false, MouseEvent.BUTTON1);
								// jComponent.processMouseEventAdAPI(swingMouseEvent);
								// }

								// mouseClicked
								// {
								// MouseEvent swingMouseEvent =
								// new MouseEvent(component,
								// MouseEvent.MOUSE_CLICKED,
								// System.currentTimeMillis(), 0, 0, 0, 0,
								// false, MouseEvent.BUTTON1);
								// jComponent.processMouseEventAdAPI(swingMouseEvent);
								// }
							}
						});
					}
				} else if (columnTouchIdx == -1) {
					// Selected Column
					rendererView = buildSelectedColumnAdAPI(rowTouchIdx);
					cellRenderer = refreshSelectedColumnAdAPI(rowTouchIdx, rendererView);
				} else {
					// User Data
					final String cacheKey = rowTouchIdx + "_" + columnTouchIdx;
					rendererView = imageViewCache.get(cacheKey);
					if (rendererView == null) {
						rendererView = new ImageView(ActivityManager.applicationContext) {
							public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
								if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
									if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER) {
										actionOnCellAdAPI(rowTouchIdx, columnTouchIdx);
										return true;
									}
								}
								return super.onKeyDown(keyCode, event);
							}
						};

						imageViewCache.put(cacheKey, rendererView);
					}

					cellRenderer = refreshCellContentAdAPI(rowTouchIdx, columnTouchIdx,
							rendererView);

					final View rendererTouch = rendererView;
					rendererView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if (hasFocus) {
								if (isSelectedWhenFocus) {
									try {
										if (currentFocusRowNumber >= 0) {
											if (JTable.this.isRowSelected(currentFocusRowNumber)) {
												JTable.this.removeRowSelectionInterval(rowTouchIdx,
														rowTouchIdx);
											}
										}

										JTable.this.setRowSelectionInterval(rowTouchIdx,
												rowTouchIdx);
									} catch (Throwable e) {
									}
								}
								currentFocusRowNumber = rowTouchIdx;
							}
						}
					});
					rendererView.setOnKeyListener(new View.OnKeyListener() {
						@Override
						public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
							if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER
									|| keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
								int action = event.getAction();
								if (action == android.view.KeyEvent.ACTION_DOWN) {
									KeyEvent awtEvent = new KeyEvent(JTable.this,
											KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0,
											KeyEvent.VK_ENTER);
									JTable.this.processKeyEvent(awtEvent);

									return true;
								} else if (action == android.view.KeyEvent.ACTION_UP) {
									KeyEvent awtEvent = new KeyEvent(JTable.this,
											KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0,
											KeyEvent.VK_ENTER);
									JTable.this.processKeyEvent(awtEvent);

									awtEvent = new KeyEvent(JTable.this, KeyEvent.KEY_PRESSED,
											System.currentTimeMillis(), 0, KeyEvent.VK_ENTER);
									JTable.this.processKeyEvent(awtEvent);

									return true;
								}
							}
							return false;
						}
					});
					rendererView.setOnLongClickListener(new View.OnLongClickListener() {
						PointerCoords coords;

						@Override
						public boolean onLongClick(View v) {
							if (coords == null) {
								final int[] tableLoc = new int[2];
								final int[] rendererViewLoc = new int[2];
								coords = new PointerCoords();
								coords.x = coords.y = 5;
								tableView.getLocationOnScreen(tableLoc);
								rendererTouch.getLocationOnScreen(rendererViewLoc);
								coords.x += rendererViewLoc[0] - tableLoc[0];
								coords.y += rendererViewLoc[1] - tableLoc[1];
							}
							buildMouseEvents(coords, 2);
							return true;
						}
					});
					rendererView.setOnClickListener(new View.OnClickListener() {
						PointerCoords coords;

						@Override
						public void onClick(View v) {
							JTable.this.setRowSelectionInterval(rowTouchIdx, rowTouchIdx);
							actionOnCellAdAPI(rowTouchIdx, columnTouchIdx);
							rendererTouch.requestFocus();

							if (coords == null) {
								final int[] tableLoc = new int[2];
								final int[] rendererViewLoc = new int[2];
								coords = new PointerCoords();
								coords.x = coords.y = 5;
								tableView.getLocationOnScreen(tableLoc);
								rendererTouch.getLocationOnScreen(rendererViewLoc);
								coords.x += rendererViewLoc[0] - tableLoc[0];
								coords.y += rendererViewLoc[1] - tableLoc[1];
							}
							buildMouseEvents(coords, 1);
						}
						// final PointerCoords coords = new PointerCoords();
						// final int[] tableLoc = new int[2];
						// final int[] rendererViewLoc = new int[2];
						// @Override
						// public boolean onTouch(View v, MotionEvent event) {
						// final int action = event.getAction();
						// if (action == MotionEvent.ACTION_UP) {
						// JTable.this.setRowSelectionInterval(rowTouchIdx,
						// rowTouchIdx);
						// actionOnCellAdAPI(rowTouchIdx, columnTouchIdx);
						// rendererTouch.requestFocus();
						//
						// event.getPointerCoords(0, coords);
						// tableView.getLocationOnScreen(tableLoc);
						// rendererTouch.getLocationOnScreen(rendererViewLoc);
						// coords.x += rendererViewLoc[0] - tableLoc[0];
						// coords.y += rendererViewLoc[1] - tableLoc[1];
						// buildMouseEvents(coords, 1);
						// }
						//
						// return true;
						// }
					});
				}

				final LinearLayout cellWrapper = new LinearLayout(ActivityManager.applicationContext);
				if (isTitle) {
					cellWrapper.setBackgroundDrawable(new Drawable() {
						@Override
						public void setColorFilter(ColorFilter arg0) {
						}

						@Override
						public void setAlpha(int arg0) {
						}

						@Override
						public int getOpacity() {
							return 0;
						}

						@Override
						public void draw(Canvas arg0) {
							int width = cellWrapper.getWidth();
							int height = cellWrapper.getHeight();

							drawCellBorder(ncols, columnTouchIdx, arg0, width, height,
									AndroidUIUtil.WINDOW_TABLE_HEADER_BORDER_COLOR, true);
						}
					});
				} else {
					cellWrapper.setBackgroundDrawable(new Drawable() {
						@Override
						public void setColorFilter(ColorFilter arg0) {
						}

						@Override
						public void setAlpha(int arg0) {
						}

						@Override
						public int getOpacity() {
							return 0;
						}

						@Override
						public void draw(Canvas arg0) {
							int width = cellWrapper.getWidth();
							int height = cellWrapper.getHeight();

							drawCellBorder(ncols, columnTouchIdx, arg0, width, height, gridColor,
									false);
						}
					});
				}
				{
					LinearLayout.LayoutParams itemLP;
					if (isTitle || (cellRenderer instanceof BooleanRenderer)// 布尔类型的JCheckBox，LinearLayout最大化后，能使图标居中
					) {
						itemLP = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT);// LayoutParams.WRAP_CONTENT
					} else {
						itemLP = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);

						// 边增加一个像素，以防内容盖住grid line
						itemLP.leftMargin = gridLineWidth;
						itemLP.topMargin = gridLineWidth;
						itemLP.rightMargin = gridLineWidth;
						itemLP.bottomMargin = gridLineWidth;
					}
					itemLP.gravity = (Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);// |
																							// Gravity.CENTER)
					AndroidUIUtil.addView(cellWrapper, rendererView, itemLP, viewRelation);
				}
				TableRow.LayoutParams lp = new TableRow.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT, 1.0F);

				cellWrapper.setFocusable(false);
				cellWrapper.setFocusableInTouchMode(false);
				AndroidUIUtil.addView(tableRow, cellWrapper, lp, viewRelation);
			}

			if (isTitle) {
				tableRow.setBackgroundResource(
						HCRUtil.getResource(HCRUtil.R_drawable_table_header));
			}

			TableLayout.LayoutParams lp = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, 1.0F);

			lp.gravity = (Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL | Gravity.CENTER);

			AndroidUIUtil.addView(tableView, tableRow, i + 1, lp, viewRelation);
		}

		if (isRequireFirstRowFocusAfterRefreshContent) {
			isRequireFirstRowFocusAfterRefreshContent = false;
			try {
				TableRow tRow = (TableRow) tableView.getChildAt(1);
				tRow.getChildAt(columnIdxRequireFirstRowFocusAfterRefreshContent + 1)
						.requestFocus();
			} catch (Throwable e) {
			}
		}
	}

	private final DefaultTableCellRenderer empty_dtcr = new DefaultTableCellRenderer();

	private final TableCellRenderer refreshCellContentAdAPI(int row, int col,
			ImageView rendererView) {
		// refreshSelectedColumnAdAPI(row, selectColumnCache.get((Integer)row));

		TableCellRenderer cellRenderer;
		Object value = null;
		try {
			value = getModel().getValueAt(row, col);
		} catch (Exception e) {
		}
		if (value == null) {
			rendererView.setFocusable(false);
			rendererView.setFocusableInTouchMode(false);
			rendererView.setClickable(false);

			Component m = empty_dtcr.getTableCellRendererComponent(this, value, isRowSelected(row),
					false, row, col);
			rendererView.setImageBitmap(AndroidUIUtil.getViewBitmap(m.getPeerAdAPI()));
			return empty_dtcr;
		}

		boolean isCellEditable = getModel().isCellEditable(row, col);
		cellRenderer = getCellRenderer(row, col);// getColumnModel().getColumn(j).getCellRenderer();
		Component m = cellRenderer.getTableCellRendererComponent(this, value, isRowSelected(row),
				false, row, col);
		if (isCellEditable) {
			m.setBackground(AndroidUIUtil.transFullColor);
		}
		rendererView.setImageBitmap(AndroidUIUtil.getViewBitmap(m.getPeerAdAPI()));

		if (isCellEditable || (focusableColumn != null && focusableColumn.isFocusable(col))) {
			rendererView.setFocusable(true);
			rendererView.setFocusableInTouchMode(false);
			rendererView.setClickable(true);

			rendererView.setBackgroundResource(HCRUtil.getResource(HCRUtil.R_drawable_tree_node));
		} else {
			rendererView.setFocusable(false);
			rendererView.setFocusableInTouchMode(false);
			rendererView.setClickable(false);
		}
		return cellRenderer;
	}

	SelectableRow selectableRow;
	FocusableColumn focusableColumn;

	public void setSelectableRow(SelectableRow sRow) {
		this.selectableRow = sRow;
	}

	public void setFocusableColumnAdAPI(FocusableColumn fc) {
		this.focusableColumn = fc;
	}

	interface SelectableRow {
		public boolean isSelectable(int row);
	}

	interface FocusableColumn {
		public boolean isFocusable(int columnIdx);
	}

	private boolean isSelectedWhenFocus = false;
	private int currentFocusRowNumber = -1;

	public void setSelectedWhenFocusAdAPI(boolean isSelected) {
		this.isSelectedWhenFocus = isSelected;
	}

	public int getCurrentFocusRowAdAPI() {
		return currentFocusRowNumber;
	}

	private ImageView buildSelectedColumnAdAPI(final int row) {
		final ImageView rendererView = new ImageView(ActivityManager.applicationContext) {
			public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
				if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER) {
					if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
						actionOnSelectedColumnAdAPI(row);
						return true;
					}
				}
				return super.onKeyDown(keyCode, event);
			}
		};

		selectColumnCache.put(row, rendererView);

		boolean selectable = (getRowCount() > row)
				? ((selectableRow != null) ? selectableRow.isSelectable(row) : true)
				: false;// 无内容时，绘制空白格，故无效。
		rendererView.setFocusable(selectable);
		rendererView.setFocusableInTouchMode(false);
		rendererView.setClickable(selectable);

		rendererView.setBackgroundResource(HCRUtil.getResource(HCRUtil.R_drawable_tree_node));

		rendererView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				actionOnSelectedColumnAdAPI(row);
				// rendererView.requestFocus();
			}
		});

		return rendererView;
	}

	private void actionOnSelectedColumnAdAPI(int row) {
		if (isRowSelected(row)) {
			removeRowSelectionInterval(row, row);
		} else {
			addRowSelectionInterval(row, row);
		}
	}

	private TableCellRenderer refreshSelectedColumnAdAPI(int row, ImageView rendererView) {
		TableCellRenderer cellRenderer;
		cellRenderer = defaultRenderersByColumnClass.get(Boolean.class);// getColumnModel().getColumn(j).getCellRenderer();
		final boolean rowSelected = isRowSelected(row);
		// System.out.println("-------------before refreshSelectedColumnAdAPI at
		// row " + row + ", and rowIsSelected : " + rowSelected);
		Component m = cellRenderer.getTableCellRendererComponent(this, rowSelected, rowSelected,
				false, row, 0);
		// System.out.println("-------------after refreshSelectedColumnAdAPI : "
		// + m);
		m.setBackground(AndroidUIUtil.transFullColor);
		rendererView.setImageBitmap(AndroidUIUtil.getViewBitmap(m.getPeerAdAPI()));
		return cellRenderer;
	}

	private void actionOnCellAdAPI(final int row, final int column) {
		if (getModel().isCellEditable(row, column)
				&& getModel().getColumnClass(column) == Boolean.class) {
			Object oldValue = (Object) getValueAt(row, column);
			if (oldValue instanceof Boolean) {
				setValueAt(!(Boolean) oldValue, row, column);
			}
		}
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public void setModel(TableModel dataModel) {
		if (dataModel == null) {
			throw new IllegalArgumentException("Cannot set a null TableModel");
		}
		if (this.dataModel != dataModel) {
			TableModel old = this.dataModel;
			if (old != null) {
				old.removeTableModelListener(this);
			}
			this.dataModel = dataModel;
			dataModel.addTableModelListener(this);

			tableChanged(new TableModelEvent(dataModel, TableModelEvent.HEADER_ROW));
		}
	}

	public TableModel getModel() {
		return dataModel;
	}

	public void setColumnModel(TableColumnModel columnModel) {
		if (columnModel == null) {
			throw new IllegalArgumentException("a null ColumnModel");
		}
		TableColumnModel old = this.columnModel;
		if (columnModel != old) {
			if (old != null) {
				old.removeColumnModelListener(this);
			}
			this.columnModel = columnModel;
			columnModel.addColumnModelListener(this);

			if (tableHeader != null) {
				tableHeader.setColumnModel(columnModel);
			}

			resizeAndRepaint();
		}
	}

	public TableColumnModel getColumnModel() {
		return columnModel;
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

			isForceRefreshTable = true;
			repaint();
		}
	}

	public ListSelectionModel getSelectionModel() {
		return selectionModel;
	}

	public void sorterChanged(RowSorterEvent e) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void tableChanged(TableModelEvent e) {
		if (e == null || e.getFirstRow() == TableModelEvent.HEADER_ROW) {
			clearSelectionAndLeadAnchor();

			rowModel = null;

			if (getAutoCreateColumnsFromModel()) {
				createDefaultColumnsFromModel();
				return;
			}

			resizeAndRepaint();
			return;
		}

		if (rowModel != null) {
			repaint();
		}

		if (e.getType() == TableModelEvent.INSERT) {
			tableRowsInserted(e);
			return;
		}

		if (e.getType() == TableModelEvent.DELETE) {
			tableRowsDeleted(e);
			return;
		}

		int end = e.getLastRow();

		if (end != Integer.MAX_VALUE) {
			refreshRowIdx = end;
			repaint();
		} else {
			clearSelectionAndLeadAnchor();
			resizeAndRepaint();
			rowModel = null;
		}
	}

	boolean isRequireFirstRowFocusAfterRefreshContent = false;
	int columnIdxRequireFirstRowFocusAfterRefreshContent;

	public void setRequireFirstRowFocusAfterRefreshContentAdAPI(boolean isRequire, int columnIdx) {
		isRequireFirstRowFocusAfterRefreshContent = isRequire;
		columnIdxRequireFirstRowFocusAfterRefreshContent = columnIdx;
	}

	private void tableRowsInserted(TableModelEvent e) {
		int start = e.getFirstRow();
		int end = e.getLastRow();
		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = getRowCount() - 1;
		}

		int length = end - start + 1;
		selectionModel.insertIndexInterval(start, length, true);

		if (rowModel != null) {
			rowModel.insertEntries(start, length, getRowHeight());
		}

		isForceRefreshTable = true;
		repaint();
	}

	private void tableRowsDeleted(TableModelEvent e) {
		int start = e.getFirstRow();
		int end = e.getLastRow();
		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = getRowCount() - 1;
		}

		int deletedCount = end - start + 1;
		selectionModel.removeIndexInterval(start, end);

		if (rowModel != null) {
			rowModel.removeEntries(start, deletedCount);
		}

		isForceRefreshTable = true;
		repaint();
	}

	public void columnAdded(TableColumnModelEvent e) {
		if (isEditing()) {
			removeEditor();
		}
		resizeAndRepaint();
	}

	public void columnRemoved(TableColumnModelEvent e) {
		if (isEditing()) {
			removeEditor();
		}
		resizeAndRepaint();
	}

	public void columnMoved(TableColumnModelEvent e) {
		if (isEditing() && !getCellEditor().stopCellEditing()) {
			getCellEditor().cancelCellEditing();
		}
		isForceRefreshTable = true;
		repaint();
	}

	public void columnMarginChanged(ChangeEvent e) {
		if (isEditing() && !getCellEditor().stopCellEditing()) {
			getCellEditor().cancelCellEditing();
		}
		TableColumn resizingColumn = getResizingColumn();
		if (resizingColumn != null && autoResizeMode == AUTO_RESIZE_OFF) {
			resizingColumn.setPreferredWidth(resizingColumn.getWidth());
		}
		resizeAndRepaint();
	}

	private int limit(int i, int a, int b) {
		return Math.min(b, Math.max(i, a));
	}

	public void columnSelectionChanged(ListSelectionEvent e) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void valueChanged(ListSelectionEvent e) {
		if (isForceRefreshTable || e == null) {
			repaint();
		} else {
			for (int i = e.getFirstIndex(); i <= e.getLastIndex(); i++) {
				refreshRowIdx = i;
				repaint();
			}
		}
	}

	public void editingStopped(ChangeEvent e) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void editingCanceled(ChangeEvent e) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setPreferredScrollableViewportSize(Dimension size) {
		preferredViewportSize = size;
	}

	public Dimension getPreferredScrollableViewportSize() {
		return preferredViewportSize;
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	private int getPreviousBlockIncrement(Rectangle visibleRect, int orientation) {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	private int getNextBlockIncrement(Rectangle visibleRect, int orientation) {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	private int getLeadingRow(Rectangle visibleRect) {
		Point leadingPoint;

		if (getComponentOrientation().isLeftToRight()) {
			leadingPoint = new Point(visibleRect.x, visibleRect.y);
		} else {
			leadingPoint = new Point(visibleRect.x + visibleRect.width - 1, visibleRect.y);
		}
		return rowAtPoint(leadingPoint);
	}

	private int getLeadingCol(Rectangle visibleRect) {
		Point leadingPoint;

		if (getComponentOrientation().isLeftToRight()) {
			leadingPoint = new Point(visibleRect.x, visibleRect.y);
		} else {
			leadingPoint = new Point(visibleRect.x + visibleRect.width - 1, visibleRect.y);
		}
		return columnAtPoint(leadingPoint);
	}

	private int getTrailingRow(Rectangle visibleRect) {
		Point trailingPoint;

		if (getComponentOrientation().isLeftToRight()) {
			trailingPoint = new Point(visibleRect.x, visibleRect.y + visibleRect.height - 1);
		} else {
			trailingPoint = new Point(visibleRect.x + visibleRect.width - 1,
					visibleRect.y + visibleRect.height - 1);
		}
		return rowAtPoint(trailingPoint);
	}

	private int getTrailingCol(Rectangle visibleRect) {
		Point trailingPoint;

		if (getComponentOrientation().isLeftToRight()) {
			trailingPoint = new Point(visibleRect.x + visibleRect.width - 1, visibleRect.y);
		} else {
			trailingPoint = new Point(visibleRect.x, visibleRect.y);
		}
		return columnAtPoint(trailingPoint);
	}

	private int leadingEdge(Rectangle rect, int orientation) {
		if (orientation == SwingConstants.VERTICAL) {
			return rect.y;
		} else if (getComponentOrientation().isLeftToRight()) {
			return rect.x;
		} else {
			return rect.x + rect.width;
		}
	}

	private int trailingEdge(Rectangle rect, int orientation) {
		if (orientation == SwingConstants.VERTICAL) {
			return rect.y + rect.height;
		} else if (getComponentOrientation().isLeftToRight()) {
			return rect.x + rect.width;
		} else {
			return rect.x;
		}
	}

	public boolean getScrollableTracksViewportWidth() {
		return !(autoResizeMode == AUTO_RESIZE_OFF);
	}

	public boolean getScrollableTracksViewportHeight() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void setFillsViewportHeight(boolean fillsViewportHeight) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getFillsViewportHeight() {
		AndroidClassUtil.callEmptyMethod();
		return fillsViewportHeight;
	}

	protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
		return false;
	}

	private void setLazyRenderer(Class c, TableCellRenderer s) {
		defaultRenderersByColumnClass.put(c, s);
	}

	protected void createDefaultRenderers() {
		defaultRenderersByColumnClass = new HashMap<Class, TableCellRenderer>();

		setLazyRenderer(Object.class, new DefaultTableCellRenderer());

		setLazyRenderer(Number.class, new NumberRenderer());

		setLazyRenderer(Float.class, new DoubleRenderer());
		setLazyRenderer(Double.class, new DoubleRenderer());

		setLazyRenderer(Date.class, new DateRenderer());

		setLazyRenderer(Icon.class, new IconRenderer());
		setLazyRenderer(ImageIcon.class, new IconRenderer());

		setLazyRenderer(Boolean.class, new BooleanRenderer());
	}

	static class NumberRenderer extends DefaultTableCellRenderer {
		public NumberRenderer() {
			super();
			setHorizontalAlignment(JLabel.RIGHT);
		}
	}

	static class DoubleRenderer extends NumberRenderer {
		NumberFormat formatter;

		public DoubleRenderer() {
			super();
		}

		public void setValue(Object value) {
			if (formatter == null) {
				formatter = NumberFormat.getInstance();
			}
			setText((value == null) ? "" : formatter.format(value));
		}
	}

	static class DateRenderer extends DefaultTableCellRenderer {
		DateFormat formatter;

		public DateRenderer() {
			super();
		}

		public void setValue(Object value) {
			if (formatter == null) {
				formatter = DateFormat.getDateInstance();
			}
			setText((value == null) ? "" : formatter.format(value));
		}
	}

	static class IconRenderer extends DefaultTableCellRenderer {
		public IconRenderer() {
			super();
			setHorizontalAlignment(JLabel.CENTER);
		}

		public void setValue(Object value) {
			setIcon((value instanceof Icon) ? (Icon) value : null);
		}
	}

	static class BooleanRenderer extends JCheckBox implements TableCellRenderer, UIResource {
		// private static final Border noFocusBorder = new EmptyBorder(1, 1, 1,
		// 1);

		public BooleanRenderer() {
			super(HCRUtil.getResource(HCRUtil.R_drawable_checkbox_table));
			setHorizontalAlignment(JLabel.CENTER);
			// setBorderPainted(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			setSelected((value != null && ((Boolean) value).booleanValue()));

			// if (hasFocus) {
			// setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			// } else {
			// setBorder(noFocusBorder);
			// }

			return this;
		}
	}

	private void setLazyEditor(Class c, TableCellEditor s) {
		defaultEditorsByColumnClass.put(c, s);
	}

	protected void createDefaultEditors() {
		defaultEditorsByColumnClass = new HashMap<Class, TableCellEditor>();

		setLazyEditor(Object.class, new GenericEditor());

		setLazyEditor(Number.class, new NumberEditor());

		setLazyEditor(Boolean.class, new BooleanEditor());
	}

	static class GenericEditor extends DefaultCellEditor {

		Class[] argTypes = new Class[] { String.class };
		java.lang.reflect.Constructor constructor;
		Object value;

		public GenericEditor() {
			super(new JTextField());
		}

		public boolean stopCellEditing() {
			return false;
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
				int row, int column) {
			return null;
		}

		public Object getCellEditorValue() {
			return value;
		}
	}

	static class NumberEditor extends GenericEditor {

		public NumberEditor() {
			((JTextField) getComponent()).setHorizontalAlignment(JTextField.RIGHT);
		}
	}

	static class BooleanEditor extends DefaultCellEditor {
		public BooleanEditor() {
			super(new JCheckBox());
			JCheckBox checkBox = (JCheckBox) getComponent();
			checkBox.setHorizontalAlignment(JCheckBox.CENTER);
		}
	}

	protected void initializeLocalVars() {
		// updateSelectionOnSort = true;//rem by yyh
		setOpaque(true);
		createDefaultRenderers();
		// createDefaultEditors();

		setTableHeader(createDefaultTableHeader());

		setShowGrid(true);
		setAutoResizeMode(AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		setRowHeight(16);
		// isRowHeightSet = false;//rem by yyh
		setRowMargin(1);
		setRowSelectionAllowed(true);
		setCellEditor(null);
		setEditingColumn(-1);
		setEditingRow(-1);
		setSurrendersFocusOnKeystroke(false);
		setPreferredScrollableViewportSize(new Dimension(450, 400));

		// ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
		// toolTipManager.registerComponent(this);

		setAutoscrolls(true);
	}

	protected TableModel createDefaultDataModel() {
		return new DefaultTableModel();
	}

	protected TableColumnModel createDefaultColumnModel() {
		return new DefaultTableColumnModel();
	}

	protected ListSelectionModel createDefaultSelectionModel() {
		return new DefaultListSelectionModel();
	}

	protected JTableHeader createDefaultTableHeader() {
		return new JTableHeader(columnModel);
	}

	protected void resizeAndRepaint() {
		isForceRefreshTable = true;
		revalidate();
		repaint();
	}

	public TableCellEditor getCellEditor() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setCellEditor(TableCellEditor anEditor) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setEditingColumn(int aColumn) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setEditingRow(int aRow) {
		AndroidClassUtil.callEmptyMethod();
	}

	public TableCellRenderer getCellRenderer(int row, int column) {
		TableColumn tableColumn = getColumnModel().getColumn(column);
		TableCellRenderer renderer = tableColumn.getCellRenderer();
		if (renderer == null) {// 被updateUI依赖
			renderer = getDefaultRenderer(getColumnClass(column));
		}
		return renderer;
	}

	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public TableCellEditor getCellEditor(int row, int column) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public Component prepareEditor(TableCellEditor editor, int row, int column) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void removeEditor() {
		AndroidClassUtil.callEmptyMethod();
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
	}

	void compWriteObjectNotify() {
	}

	protected String paramString() {
		return "";
	}

	public boolean print() throws PrinterException {
		return print(PrintMode.FIT_WIDTH);
	}

	public boolean print(PrintMode printMode) throws PrinterException {
		return print(printMode, null, null);
	}

	public boolean print(PrintMode printMode, MessageFormat headerFormat,
			MessageFormat footerFormat) throws PrinterException {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public boolean print(PrintMode printMode, MessageFormat headerFormat,
			MessageFormat footerFormat, boolean showPrintDialog, PrintRequestAttributeSet attr,
			boolean interactive) throws PrinterException, HeadlessException {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public boolean print(PrintMode printMode, MessageFormat headerFormat,
			MessageFormat footerFormat, boolean showPrintDialog, PrintRequestAttributeSet attr,
			boolean interactive, PrintService service) throws PrinterException, HeadlessException {
		AndroidClassUtil.callEmptyMethod();
		return true;
	}

	public Printable getPrintable(PrintMode printMode, MessageFormat headerFormat,
			MessageFormat footerFormat) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	private class ThreadSafePrintable implements Printable {
		private Printable printDelegate;

		private int retVal;

		private Throwable retThrowable;

		public ThreadSafePrintable(Printable printDelegate) {
			this.printDelegate = printDelegate;
		}

		public int print(final Graphics graphics, final PageFormat pageFormat, final int pageIndex)
				throws PrinterException {
			return 0;
		}
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

	private void drawCellBorder(final int columnNumber, final int columnTouchIdx, Canvas arg0,
			int width, int height, Color drawColor, boolean drawTop) {
		Graphics g = new CanvasGraphics(arg0, getScreenAdapterAdAPI());
		g.setColor(drawColor);

		g.drawLine(0, 0, 0, height);
		if (drawTop) {
			g.drawLine(0, 0, width - gridLineWidth, 0);
		}
		g.drawLine(0, height - gridLineWidth, width - gridLineWidth, height - gridLineWidth);
		if (columnTouchIdx == columnNumber - 1) {
			g.drawLine(width - gridLineWidth, 0, width - gridLineWidth, height - gridLineWidth);// 收尾右边
		}
	}

	private void buildMouseEvents(PointerCoords coords, int clickCount) {
		long currMS = System.currentTimeMillis();

		// mousePressed
		MouseEvent swingMouseEvent = new MouseEvent(JTable.this, MouseEvent.MOUSE_PRESSED, currMS,
				0, (int) coords.x, (int) coords.y, clickCount, false, MouseEvent.BUTTON1);
		JTable.this.processMouseEventAdAPI(swingMouseEvent);

		// mouseReleased
		swingMouseEvent = new MouseEvent(JTable.this, MouseEvent.MOUSE_RELEASED, currMS, 0,
				(int) coords.x, (int) coords.y, clickCount, false, MouseEvent.BUTTON1);
		JTable.this.processMouseEventAdAPI(swingMouseEvent);

		// mouseClicked
		MouseEvent clickEvent = new MouseEvent(JTable.this, MouseEvent.MOUSE_CLICKED, currMS, 0,
				(int) coords.x, (int) coords.y, clickCount, false, MouseEvent.BUTTON1);
		JTable.this.processMouseEventAdAPI(clickEvent);
	}

}
