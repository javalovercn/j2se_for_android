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

import java.io.Serializable;
import java.util.EventObject;

import javax.swing.event.CellEditorListener;

/**
 *
 * A base class for <code>CellEditors</code>, providing default implementations
 * for the methods in the <code>CellEditor</code> interface except
 * <code>getCellEditorValue()</code>. Like the other abstract implementations in
 * Swing, also manages a list of listeners.
 *
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Philip Milne
 * @since 1.3
 */
public abstract class AbstractCellEditor implements CellEditor, Serializable {

	public boolean isCellEditable(EventObject e) {
		return true;
	}

	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	public boolean stopCellEditing() {
		fireEditingStopped();
		return true;
	}

	public void cancelCellEditing() {
		fireEditingCanceled();
	}

	public void addCellEditorListener(CellEditorListener l) {
		// listenerList.add(CellEditorListener.class, l);
	}

	public void removeCellEditorListener(CellEditorListener l) {
		// listenerList.remove(CellEditorListener.class, l);
	}

	public CellEditorListener[] getCellEditorListeners() {
		// return listenerList.getListeners(CellEditorListener.class);
		return null;
	}

	protected void fireEditingStopped() {
	}

	protected void fireEditingCanceled() {
	}
}
