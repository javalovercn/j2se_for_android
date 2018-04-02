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
import hc.android.AndroidUIUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * The standard class for rendering (displaying) individual cells in a
 * <code>JTable</code>.
 * <p>
 *
 * <strong><a name="override">Implementation Note:</a></strong> This class
 * inherits from <code>JLabel</code>, a standard component class. However
 * <code>JTable</code> employs a unique mechanism for rendering its cells and
 * therefore requires some slightly modified behavior from its cell renderer.
 * The table class defines a single cell renderer and uses it as a as a
 * rubber-stamp for rendering all cells in the table; it renders the first cell,
 * changes the contents of that cell renderer, shifts the origin to the new
 * location, re-draws it, and so on. The standard <code>JLabel</code> component
 * was not designed to be used this way and we want to avoid triggering a
 * <code>revalidate</code> each time the cell is drawn. This would greatly
 * decrease performance because the <code>revalidate</code> message would be
 * passed up the hierarchy of the container to determine whether any other
 * components would be affected. As the renderer is only parented for the
 * lifetime of a painting operation we similarly want to avoid the overhead
 * associated with walking the hierarchy for painting operations. So this class
 * overrides the <code>validate</code>, <code>invalidate</code>,
 * <code>revalidate</code>, <code>repaint</code>, and
 * <code>firePropertyChange</code> methods to be no-ops and override the
 * <code>isOpaque</code> method solely to improve performance. If you write your
 * own renderer, please keep this performance consideration in mind.
 * <p>
 *
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Philip Milne
 * @see JTable
 */
public class DefaultTableCellRenderer extends JLabel implements TableCellRenderer, Serializable {

	private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
	private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
	protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;

	protected Color selectedForeground = AndroidUIUtil.WIN_FONT_COLOR;
	protected Color selectedBackground;// = UIUtil.WIN_BODY_SELECTED_BACK;

	protected Color unselectedForeground = selectedForeground.darker();
	protected Color unselectedBackground;// = UIUtil.WIN_BODY_BACK;

	public DefaultTableCellRenderer() {
		super();
		setOpaque(true);
		setName("Table.cellRenderer");
	}

	private Border getNoFocusBorder() {
		return SAFE_NO_FOCUS_BORDER;
	}

	public void setForeground(Color c) {
		super.setForeground(c);
		unselectedForeground = c;
	}

	public void setBackground(Color c) {
		super.setBackground(c);
		unselectedBackground = c;
	}

	public void updateUI() {
		super.updateUI();
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		if (table == null) {
			return this;
		}

		if (isSelected) {
			super.setForeground(selectedForeground);
			super.setBackground(selectedBackground);
		} else {
			super.setForeground(unselectedForeground);
			super.setBackground(unselectedBackground);
		}

		setValue(value);

		return this;
	}

	public boolean isOpaque() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void invalidate() {
	}

	public void validate() {
	}

	public void revalidate() {
	}

	public void repaint(long tm, int x, int y, int width, int height) {
		repaint();
	}

	public void repaint(Rectangle r) {
		repaint(r.x, r.y, r.width, r.height);
	}

	public void repaint() {
		updateUI();
	}

	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		AndroidClassUtil.callEmptyMethod();
	}

	protected void setValue(Object value) {
		setText((value == null) ? "" : value.toString());
	}

	public static class UIResource extends DefaultTableCellRenderer
			implements javax.swing.plaf.UIResource {
	}

}