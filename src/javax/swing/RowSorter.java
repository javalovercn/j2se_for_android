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

import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;

/**
 * <code>RowSorter</code> provides the basis for sorting and filtering. Beyond
 * creating and installing a <code>RowSorter</code>, you very rarely need to
 * interact with one directly. Refer to {@link javax.swing.table.TableRowSorter
 * TableRowSorter} for a concrete implementation of <code>RowSorter</code> for
 * <code>JTable</code>.
 * <p>
 * <code>RowSorter</code>'s primary role is to provide a mapping between two
 * coordinate systems: that of the view (for example a <code>JTable</code>) and
 * that of the underlying data source, typically a model.
 * <p>
 * The view invokes the following methods on the <code>RowSorter</code>:
 * <ul>
 * <li><code>toggleSortOrder</code> &#151; The view invokes this when the
 * appropriate user gesture has occurred to trigger a sort. For example, the
 * user clicked a column header in a table.
 * <li>One of the model change methods &#151; The view invokes a model change
 * method when the underlying model has changed. There may be order dependencies
 * in how the events are delivered, so a <code>RowSorter</code> should not
 * update its mapping until one of these methods is invoked.
 * </ul>
 * Because the view makes extensive use of the
 * <code>convertRowIndexToModel</code>, <code>convertRowIndexToView</code> and
 * <code>getViewRowCount</code> methods, these methods need to be fast.
 * <p>
 * <code>RowSorter</code> provides notification of changes by way of
 * <code>RowSorterListener</code>. Two types of notification are sent:
 * <ul>
 * <li><code>RowSorterEvent.Type.SORT_ORDER_CHANGED</code> &#151; notifies
 * listeners that the sort order has changed. This is typically followed by a
 * notification that the sort has changed.
 * <li><code>RowSorterEvent.Type.SORTED</code> &#151; notifies listeners that
 * the mapping maintained by the <code>RowSorter</code> has changed in some way.
 * </ul>
 * <code>RowSorter</code> implementations typically don't have a one-to-one
 * mapping with the underlying model, but they can. For example, if a database
 * does the sorting, <code>toggleSortOrder</code> might call through to the
 * database (on a background thread), and override the mapping methods to return
 * the argument that is passed in.
 * <p>
 * Concrete implementations of <code>RowSorter</code> need to reference a model
 * such as <code>TableModel</code> or <code>ListModel</code>. The view classes,
 * such as <code>JTable</code> and <code>JList</code>, will also have a
 * reference to the model. To avoid ordering dependencies,
 * <code>RowSorter</code> implementations should not install a listener on the
 * model. Instead the view class will call into the <code>RowSorter</code> when
 * the model changes. For example, if a row is updated in a
 * <code>TableModel</code> <code>JTable</code> invokes <code>rowsUpdated</code>.
 * When the model changes, the view may call into any of the following methods:
 * <code>modelStructureChanged</code>, <code>allRowsChanged</code>,
 * <code>rowsInserted</code>, <code>rowsDeleted</code> and
 * <code>rowsUpdated</code>.
 *
 * @param <M>
 *            the type of the underlying model
 * @see javax.swing.table.TableRowSorter
 * @since 1.6
 */
public abstract class RowSorter<M> {
	private EventListenerList listenerList = new EventListenerList();

	public RowSorter() {
	}

	public abstract M getModel();

	public abstract void toggleSortOrder(int column);

	public abstract int convertRowIndexToModel(int index);

	public abstract int convertRowIndexToView(int index);

	public abstract void setSortKeys(List<? extends SortKey> keys);

	public abstract List<? extends SortKey> getSortKeys();

	public abstract int getViewRowCount();

	public abstract int getModelRowCount();

	public abstract void modelStructureChanged();

	public abstract void allRowsChanged();

	public abstract void rowsInserted(int firstRow, int endRow);

	public abstract void rowsDeleted(int firstRow, int endRow);

	public abstract void rowsUpdated(int firstRow, int endRow);

	public abstract void rowsUpdated(int firstRow, int endRow, int column);

	public void addRowSorterListener(RowSorterListener l) {
		listenerList.add(RowSorterListener.class, l);
	}

	public void removeRowSorterListener(RowSorterListener l) {
		listenerList.remove(RowSorterListener.class, l);
	}

	protected void fireSortOrderChanged() {
		fireRowSorterChanged(new RowSorterEvent(this));
	}

	protected void fireRowSorterChanged(int[] lastRowIndexToModel) {
		fireRowSorterChanged(
				new RowSorterEvent(this, RowSorterEvent.Type.SORTED, lastRowIndexToModel));
	}

	void fireRowSorterChanged(RowSorterEvent event) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == RowSorterListener.class) {
				((RowSorterListener) listeners[i + 1]).sorterChanged(event);
			}
		}
	}

	public static class SortKey {
		private int column;
		private SortOrder sortOrder;

		public SortKey(int column, SortOrder sortOrder) {
			if (sortOrder == null) {
				throw new IllegalArgumentException("sort order must be non-null");
			}
			this.column = column;
			this.sortOrder = sortOrder;
		}

		public final int getColumn() {
			return column;
		}

		public final SortOrder getSortOrder() {
			return sortOrder;
		}

		public int hashCode() {
			int result = 17;
			result = 37 * result + column;
			result = 37 * result + sortOrder.hashCode();
			return result;
		}

		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o instanceof SortKey) {
				return (((SortKey) o).column == column && ((SortKey) o).sortOrder == sortOrder);
			}
			return false;
		}
	}
}