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

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JTable;

/**
 * A <code>TableColumn</code> represents all the attributes of a column in a
 * <code>JTable</code>, such as width, resizibility, minimum and maximum width.
 * In addition, the <code>TableColumn</code> provides slots for a renderer and
 * an editor that can be used to display and edit the values in this column.
 * <p>
 * It is also possible to specify renderers and editors on a per type basis
 * rather than a per column basis - see the <code>setDefaultRenderer</code>
 * method in the <code>JTable</code> class. This default mechanism is only used
 * when the renderer (or editor) in the <code>TableColumn</code> is
 * <code>null</code>.
 * <p>
 * The <code>TableColumn</code> stores the link between the columns in the
 * <code>JTable</code> and the columns in the <code>TableModel</code>. The
 * <code>modelIndex</code> is the column in the <code>TableModel</code>, which
 * will be queried for the data values for the cells in this column. As the
 * column moves around in the view this <code>modelIndex</code> does not change.
 * <p>
 * <b>Note:</b> Some implementations may assume that all
 * <code>TableColumnModel</code>s are unique, therefore we would recommend that
 * the same <code>TableColumn</code> instance not be added more than once to a
 * <code>TableColumnModel</code>. To show <code>TableColumn</code>s with the
 * same column of data from the model, create a new instance with the same
 * <code>modelIndex</code>.
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
 * @see javax.swing.table.TableColumnModel
 *
 * @see javax.swing.table.DefaultTableColumnModel
 * @see javax.swing.table.JTableHeader#getDefaultRenderer()
 * @see JTable#getDefaultRenderer(Class)
 * @see JTable#getDefaultEditor(Class)
 * @see JTable#getCellRenderer(int, int)
 * @see JTable#getCellEditor(int, int)
 */
public class TableColumn extends Object implements Serializable {
	public final static String COLUMN_WIDTH_PROPERTY = "columWidth";
	public final static String HEADER_VALUE_PROPERTY = "headerValue";
	public final static String HEADER_RENDERER_PROPERTY = "headerRenderer";
	public final static String CELL_RENDERER_PROPERTY = "cellRenderer";

	protected int modelIndex;

	protected Object identifier;

	protected int width;
	protected int minWidth = 0;
	private int preferredWidth;
	protected int maxWidth = Integer.MAX_VALUE;

	protected TableCellRenderer headerRenderer;
	protected Object headerValue;

	protected TableCellRenderer cellRenderer;
	protected TableCellEditor cellEditor;

	protected boolean isResizable;

	@Deprecated
	transient protected int resizedPostingDisableCount;

	public TableColumn() {
		this(0);
	}

	public TableColumn(int modelIndex) {
		this(modelIndex, 75, null, null);
	}

	public TableColumn(int modelIndex, int width) {
		this(modelIndex, width, null, null);
	}

	public TableColumn(int modelIndex, int width, TableCellRenderer cellRenderer,
			TableCellEditor cellEditor) {
		super();
		this.modelIndex = modelIndex;
		preferredWidth = this.width = Math.max(width, 0);

		this.cellRenderer = cellRenderer;
		this.cellEditor = cellEditor;

		minWidth = Math.min(15, this.width);
		maxWidth = Integer.MAX_VALUE;
		isResizable = true;
		resizedPostingDisableCount = 0;
		headerValue = null;
	}

	public void setModelIndex(int modelIndex) {
		this.modelIndex = modelIndex;
	}

	public int getModelIndex() {
		return modelIndex;
	}

	public void setIdentifier(Object identifier) {
		this.identifier = identifier;
	}

	public Object getIdentifier() {
		return (identifier != null) ? identifier : getHeaderValue();

	}

	public void setHeaderValue(Object headerValue) {
		this.headerValue = headerValue;
	}

	public Object getHeaderValue() {
		return headerValue;
	}

	public void setHeaderRenderer(TableCellRenderer headerRenderer) {
		this.headerRenderer = headerRenderer;
	}

	public TableCellRenderer getHeaderRenderer() {
		return headerRenderer;
	}

	public void setCellRenderer(TableCellRenderer cellRenderer) {
		this.cellRenderer = cellRenderer;
	}

	public TableCellRenderer getCellRenderer() {
		return cellRenderer;
	}

	public void setCellEditor(TableCellEditor cellEditor) {
		this.cellEditor = cellEditor;
	}

	public TableCellEditor getCellEditor() {
		return cellEditor;
	}

	public void setWidth(int width) {
		this.width = Math.min(Math.max(width, minWidth), maxWidth);
	}

	public int getWidth() {
		// System.out.println("TableColumn getPreferredWidth : " + width);
		return width;// real width
	}

	public void setPreferredWidth(int preferredWidth) {
		this.preferredWidth = Math.min(Math.max(preferredWidth, minWidth), maxWidth);
	}

	public int getPreferredWidth() {
		// System.out.println("TableColumn getPreferredWidth : " +
		// preferredWidth);
		return preferredWidth;
	}

	public void setMinWidth(int minWidth) {
		int old = this.minWidth;
		this.minWidth = Math.max(Math.min(minWidth, maxWidth), 0);
		if (width < this.minWidth) {
			setWidth(this.minWidth);
		}
		if (preferredWidth < this.minWidth) {
			setPreferredWidth(this.minWidth);
		}
	}

	public int getMinWidth() {
		return minWidth;
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = Math.max(minWidth, maxWidth);
		if (width > this.maxWidth) {
			setWidth(this.maxWidth);
		}
		if (preferredWidth > this.maxWidth) {
			setPreferredWidth(this.maxWidth);
		}
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public void setResizable(boolean isResizable) {
		this.isResizable = isResizable;
	}

	public boolean getResizable() {
		return isResizable;
	}

	public void sizeWidthToFit() {
		AndroidClassUtil.callEmptyMethod();
	}

	@Deprecated
	public void disableResizedPosting() {
		resizedPostingDisableCount++;
	}

	@Deprecated
	public void enableResizedPosting() {
		resizedPostingDisableCount--;
	}

	public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
		AndroidClassUtil.callEmptyMethod();
	}

	public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
		AndroidClassUtil.callEmptyMethod();
	}

	public synchronized PropertyChangeListener[] getPropertyChangeListeners() {
		AndroidClassUtil.callEmptyMethod();
		return new PropertyChangeListener[0];
	}

	protected TableCellRenderer createDefaultHeaderRenderer() {
		DefaultTableCellRenderer label = new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				if (table != null) {
					JTableHeader header = table.getTableHeader();
					if (header != null) {
						setForeground(header.getForeground());
						setBackground(header.getBackground());
						setFont(header.getFont());
					}
				}

				setText((value == null) ? "" : value.toString());
				// setBorder(UIManager.getBorder("TableHeader.cellBorder"));
				return this;
			}
		};
		label.setHorizontalAlignment(JLabel.CENTER);
		return label;
	}

}