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
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.plaf.TableHeaderUI;

import android.view.View;
import android.widget.TextView;

/**
 * This is the object which manages the header of the <code>JTable</code>.
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
 * @see javax.swing.JTable
 */
public class JTableHeader extends JComponent implements TableColumnModelListener, Accessible {
	private static final String uiClassID = "TableHeaderUI";

	protected JTable table;
	protected TableColumnModel columnModel;
	protected boolean reorderingAllowed;
	protected boolean resizingAllowed;
	protected boolean updateTableInRealTime;

	transient protected TableColumn resizingColumn;
	transient protected TableColumn draggedColumn;
	transient protected int draggedDistance;

	private TableCellRenderer defaultRenderer;

	public JTableHeader() {
		this(null);
	}

	public JTableHeader(TableColumnModel cm) {
		super();

		if (cm == null) {
			cm = createDefaultColumnModel();
		}
		setColumnModel(cm);

		initializeLocalVars();
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public JTable getTable() {
		return table;
	}

	public void setReorderingAllowed(boolean reorderingAllowed) {
		this.reorderingAllowed = reorderingAllowed;
	}

	public boolean getReorderingAllowed() {
		return reorderingAllowed;
	}

	public void setResizingAllowed(boolean resizingAllowed) {
		this.resizingAllowed = resizingAllowed;
	}

	public boolean getResizingAllowed() {
		return resizingAllowed;
	}

	public TableColumn getDraggedColumn() {
		return draggedColumn;
	}

	public int getDraggedDistance() {
		return draggedDistance;
	}

	public TableColumn getResizingColumn() {
		return resizingColumn;
	}

	public void setUpdateTableInRealTime(boolean flag) {
		updateTableInRealTime = flag;
	}

	public boolean getUpdateTableInRealTime() {
		return updateTableInRealTime;
	}

	public void setDefaultRenderer(TableCellRenderer defaultRenderer) {
		this.defaultRenderer = defaultRenderer;
	}

	@Transient
	public TableCellRenderer getDefaultRenderer() {
		return defaultRenderer;
	}

	public int columnAtPoint(Point point) {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public Rectangle getHeaderRect(int column) {
		AndroidClassUtil.callEmptyMethod();
		return new Rectangle();
	}

	public String getToolTipText(MouseEvent event) {
		AndroidClassUtil.callEmptyMethod();
		return "";
	}

	public TableHeaderUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setUI(TableHeaderUI ui) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void repaint() {
		updateUI();
	}

	public void updateUI() {
		JTable refreshTable = getTable();
		if (refreshTable != null) {
			refreshTable.updateUI();
		}
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public void setColumnModel(TableColumnModel columnModel) {
		if (columnModel == null) {
			throw new IllegalArgumentException("Cannot set a null ColumnModel");
		}
		TableColumnModel old = this.columnModel;
		if (columnModel != old) {
			if (old != null) {
				old.removeColumnModelListener(this);
			}
			this.columnModel = columnModel;
			columnModel.addColumnModelListener(this);

			resizeAndRepaint();
		}
	}

	public TableColumnModel getColumnModel() {
		return columnModel;
	}

	public void columnAdded(TableColumnModelEvent e) {
		resizeAndRepaint();
	}

	public void columnRemoved(TableColumnModelEvent e) {
		resizeAndRepaint();
	}

	public void columnMoved(TableColumnModelEvent e) {
		repaint();
	}

	public void columnMarginChanged(ChangeEvent e) {
		resizeAndRepaint();
	}

	public void columnSelectionChanged(ListSelectionEvent e) {
		updateUI();
	}

	protected TableColumnModel createDefaultColumnModel() {
		return new DefaultTableColumnModel();
	}

	protected TableCellRenderer createDefaultRenderer() {
		return new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				Component component = super.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);
				component.setForeground(Color.BLACK);
				setHorizontalAlignment(JLabel.CENTER);
				return component;
			}
		};
	}

	protected void initializeLocalVars() {
		setOpaque(true);
		table = null;
		reorderingAllowed = true;
		resizingAllowed = true;
		draggedColumn = null;
		draggedDistance = 0;
		resizingColumn = null;
		updateTableInRealTime = true;

		setDefaultRenderer(createDefaultRenderer());
	}

	public void resizeAndRepaint() {
		revalidate();
		repaint();
	}

	public void setDraggedColumn(TableColumn aColumn) {
		draggedColumn = aColumn;
	}

	public void setDraggedDistance(int distance) {
		draggedDistance = distance;
	}

	public void setResizingColumn(TableColumn aColumn) {
		resizingColumn = aColumn;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	protected String paramString() {
		return "";
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

}