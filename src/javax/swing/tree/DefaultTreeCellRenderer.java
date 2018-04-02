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
package javax.swing.tree;

import hc.android.DefaultLookup;
import hc.android.AndroidUIUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.UIResource;

/**
 * Displays an entry in a tree. <code>DefaultTreeCellRenderer</code> is not
 * opaque and unless you subclass paint you should not change this. See <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/tree.html">How to
 * Use Trees</a> in <em>The Java Tutorial</em> for examples of customizing node
 * display using this class.
 * <p>
 * The set of icons and colors used by {@code DefaultTreeCellRenderer} can be
 * configured using the various setter methods. The value for each property is
 * initialized from the defaults table. When the look and feel changes
 * ({@code updateUI} is invoked), any properties that have a value of type
 * {@code UIResource} are refreshed from the defaults table. The following table
 * lists the mapping between {@code DefaultTreeCellRenderer} property and
 * defaults table key:
 * <table border="1" cellpadding="1" cellspacing="0" valign="top" >
 * <tr valign="top" align="left">
 * <th bgcolor="#CCCCFF" align="left">Property:
 * <th bgcolor="#CCCCFF" align="left">Key:
 * <tr>
 * <td>"leafIcon"
 * <td>"Tree.leafIcon"
 * <tr>
 * <td>"closedIcon"
 * <td>"Tree.closedIcon"
 * <tr>
 * <td>"openIcon"
 * <td>"Tree.openIcon"
 * <tr>
 * <td>"textSelectionColor"
 * <td>"Tree.selectionForeground"
 * <tr>
 * <td>"textNonSelectionColor"
 * <td>"Tree.textForeground"
 * <tr>
 * <td>"backgroundSelectionColor"
 * <td>"Tree.selectionBackground"
 * <tr>
 * <td>"backgroundNonSelectionColor"
 * <td>"Tree.textBackground"
 * <tr>
 * <td>"borderSelectionColor"
 * <td>"Tree.selectionBorderColor"
 * </table>
 * <p>
 * <strong><a name="override">Implementation Note:</a></strong> This class
 * overrides <code>invalidate</code>, <code>validate</code>,
 * <code>revalidate</code>, <code>repaint</code>, and
 * <code>firePropertyChange</code> solely to improve performance. If not
 * overridden, these frequently called methods would execute code paths that are
 * unnecessary for the default tree cell renderer. If you write your own
 * renderer, take care to weigh the benefits and drawbacks of overriding these
 * methods.
 *
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Rob Davis
 * @author Ray Ryan
 * @author Scott Violet
 */
public class DefaultTreeCellRenderer extends JLabel implements TreeCellRenderer {
	private JTree tree;

	protected boolean selected;
	protected boolean hasFocus;

	transient protected Icon closedIcon;
	transient protected Icon leafIcon;
	transient protected Icon openIcon;
	protected Color textSelectionColor = AndroidUIUtil.WIN_FONT_COLOR;
	protected Color textNonSelectionColor = AndroidUIUtil.WIN_UNSELECTED_FONT_COLOR;

	protected Color backgroundSelectionColor = AndroidUIUtil.WIN_BODY_SELECTED_BACK;
	protected Color backgroundNonSelectionColor = AndroidUIUtil.WIN_BODY_BACK;
	protected Color borderSelectionColor = AndroidUIUtil.WINDOW_SELECTED_BORDER_COLOR;

	private boolean inited;

	public DefaultTreeCellRenderer() {
		inited = true;
	}

	public void updateUI() {
		super.updateUI();
		if (!inited || (getLeafIcon() instanceof UIResource)) {
			setLeafIcon(hc.android.DefaultLookup.getIcon(hc.android.DefaultLookup.TREE_LEAFICON));
		}
		if (!inited || (getClosedIcon() instanceof UIResource)) {
			setClosedIcon(
					hc.android.DefaultLookup.getIcon(hc.android.DefaultLookup.TREE_CLOSEDICON));
		}
		if (!inited || (getOpenIcon() instanceof UIManager)) {
			setOpenIcon(hc.android.DefaultLookup.getIcon(hc.android.DefaultLookup.TREE_OPENICON));
		}
		if (!inited || (getTextSelectionColor() instanceof UIResource)) {
			setTextSelectionColor(hc.android.DefaultLookup
					.getColor(hc.android.DefaultLookup.TREE_SELECTIONFOREGROUND));
		}
		if (!inited || (getTextNonSelectionColor() instanceof UIResource)) {
			setTextNonSelectionColor(hc.android.DefaultLookup
					.getColor(hc.android.DefaultLookup.TREE_TEXTFOREGROUND));
		}
		if (!inited || (getBackgroundSelectionColor() instanceof UIResource)) {
			setBackgroundSelectionColor(hc.android.DefaultLookup
					.getColor(hc.android.DefaultLookup.TREE_SELECTIONBACKGROUND));
		}
		if (!inited || (getBackgroundNonSelectionColor() instanceof UIResource)) {
			setBackgroundNonSelectionColor(hc.android.DefaultLookup
					.getColor(hc.android.DefaultLookup.TREE_TEXTBACKGROUND));
		}

		setName("Tree.cellRenderer");
	}

	public Icon getDefaultOpenIcon() {
		return hc.android.DefaultLookup.getIcon(hc.android.DefaultLookup.TREE_OPENICON);
	}

	public Icon getDefaultClosedIcon() {
		return hc.android.DefaultLookup.getIcon(hc.android.DefaultLookup.TREE_CLOSEDICON);
	}

	public Icon getDefaultLeafIcon() {
		return DefaultLookup.getIcon(DefaultLookup.TREE_LEAFICON);
	}

	public void setOpenIcon(Icon newIcon) {
		openIcon = newIcon;
	}

	public Icon getOpenIcon() {
		return openIcon;
	}

	public void setClosedIcon(Icon newIcon) {
		closedIcon = newIcon;
	}

	public Icon getClosedIcon() {
		return closedIcon;
	}

	public void setLeafIcon(Icon newIcon) {
		leafIcon = newIcon;
	}

	public Icon getLeafIcon() {
		return leafIcon;
	}

	public void setTextSelectionColor(Color newColor) {
		if (newColor != null) {
			textSelectionColor = newColor;
		}
	}

	public Color getTextSelectionColor() {
		return textSelectionColor;
	}

	public void setTextNonSelectionColor(Color newColor) {
		if (newColor != null) {
			textNonSelectionColor = newColor;
		}
	}

	public Color getTextNonSelectionColor() {
		return textNonSelectionColor;
	}

	public void setBackgroundSelectionColor(Color newColor) {
		backgroundSelectionColor = newColor;
	}

	public Color getBackgroundSelectionColor() {
		return backgroundSelectionColor;
	}

	public void setBackgroundNonSelectionColor(Color newColor) {
		backgroundNonSelectionColor = newColor;
	}

	public Color getBackgroundNonSelectionColor() {
		return backgroundNonSelectionColor;
	}

	public void setBorderSelectionColor(Color newColor) {
		if (newColor != null) {
			borderSelectionColor = newColor;
		}
	}

	public Color getBorderSelectionColor() {
		return borderSelectionColor;
	}

	public void setFont(Font font) {
		if (font != null) {
			if (font instanceof FontUIResource)
				font = null;
			super.setFont(font);
		}
	}

	public Font getFont() {
		Font font = super.getFont();

		if (font == null && tree != null) {
			font = tree.getFont();
		}
		return font;
	}

	public void setBackground(Color color) {
		super.setBackground(color);
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, final boolean sel,
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		String stringValue = tree.convertValueToText(value, sel, expanded, leaf, row, hasFocus);
		setComponentOrientation(tree.getComponentOrientation());

		this.tree = tree;
		this.hasFocus = hasFocus;
		setTextAdAPI(stringValue);
		// JTree.DropLocation dropLocation = tree.getDropLocation();
		// if (dropLocation != null
		// && dropLocation.getChildIndex() == -1
		// && tree.getRowForPath(dropLocation.getPath()) == row) {
		//
		// isDropCell = true;

		Icon icon = null;
		if (leaf) {
			icon = getLeafIcon();
		} else if (expanded) {
			icon = getOpenIcon();
		} else {
			icon = getClosedIcon();
		}

		if (!tree.isEnabled()) {
			setEnabled(false);
			LookAndFeel laf = UIManager.getLookAndFeel();
			Icon disabledIcon = laf.getDisabledIcon(tree, icon);
			if (disabledIcon != null)
				icon = disabledIcon;
			setDisabledIcon(icon);
		} else {
			setEnabled(true);

			Color fg = null;
			if (sel) {
				fg = getTextSelectionColor();
			} else {
				fg = getTextNonSelectionColor();
			}
			super.setForeground(fg);
			setIcon(icon);
		}

		selected = sel;

		return this;
	}

	public void paint(Graphics g) {
		super.paint(g);
	}

	public Dimension getPreferredSize() {
		return super.getPreferredSize();
	}

	public void validate() {
	}

	public void invalidate() {
	}

	public void revalidate() {
	}

	public void repaint(long tm, int x, int y, int width, int height) {
	}

	public void repaint(Rectangle r) {
	}

	public void repaint() {
	}

	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
	}

	public void firePropertyChange(String propertyName, char oldValue, char newValue) {
	}

	public void firePropertyChange(String propertyName, short oldValue, short newValue) {
	}

	public void firePropertyChange(String propertyName, int oldValue, int newValue) {
	}

	public void firePropertyChange(String propertyName, long oldValue, long newValue) {
	}

	public void firePropertyChange(String propertyName, float oldValue, float newValue) {
	}

	public void firePropertyChange(String propertyName, double oldValue, double newValue) {
	}

	public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
	}

}
