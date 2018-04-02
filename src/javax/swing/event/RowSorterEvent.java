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

import javax.swing.RowSorter;

/**
 * <code>RowSorterEvent</code> provides notification of changes to a
 * <code>RowSorter</code>. Two types of notification are possible:
 * <ul>
 * <li><code>Type.SORT_ORDER_CHANGED</code>: indicates the sort order has
 * changed. This is typically followed by a notification of:
 * <li><code>Type.SORTED</code>: indicates the contents of the model have been
 * transformed in some way. For example, the contents may have been sorted or
 * filtered.
 * </ul>
 *
 * @see javax.swing.RowSorter
 * @since 1.6
 */
public class RowSorterEvent extends java.util.EventObject {
	private Type type;
	private int[] oldViewToModel;

	public enum Type {
		SORT_ORDER_CHANGED, SORTED
	}

	public RowSorterEvent(RowSorter source) {
		this(source, Type.SORT_ORDER_CHANGED, null);
	}

	public RowSorterEvent(RowSorter source, Type type, int[] previousRowIndexToModel) {
		super(source);
		if (type == null) {
			throw new IllegalArgumentException("type must be non-null");
		}
		this.type = type;
		this.oldViewToModel = previousRowIndexToModel;
	}

	public RowSorter getSource() {
		return (RowSorter) super.getSource();
	}

	public Type getType() {
		return type;
	}

	public int convertPreviousRowIndexToModel(int index) {
		if (oldViewToModel != null && index >= 0 && index < oldViewToModel.length) {
			return oldViewToModel[index];
		}
		return -1;
	}

	public int getPreviousRowCount() {
		return (oldViewToModel == null) ? 0 : oldViewToModel.length;
	}
}