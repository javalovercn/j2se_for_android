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

import hc.android.AndroidClassUtil;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.plaf.MenuBarUI;

/**
 * An implementation of a menu bar. You add <code>JMenu</code> objects to the
 * menu bar to construct a menu. When the user selects a <code>JMenu</code>
 * object, its associated <code>JPopupMenu</code> is displayed, allowing the
 * user to select one of the <code>JMenuItems</code> on it.
 * <p>
 * For information and examples of using menu bars see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/menu.html">How to
 * Use Menus</a>, a section in <em>The Java Tutorial.</em>
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
 * @beaninfo attribute: isContainer true description: A container for holding
 *           and displaying menus.
 *
 * @author Georges Saab
 * @author David Karlton
 * @author Arnaud Weber
 * @see JMenu
 * @see JPopupMenu
 * @see JMenuItem
 */
public class JMenuBar extends JComponent implements Accessible, MenuElement {
	private static final String uiClassID = "MenuBarUI";

	private transient SingleSelectionModel selectionModel;

	private boolean paintBorder = true;
	private Insets margin = null;

	public JMenuBar() {
		super();
	}

	public MenuBarUI getUI() {
		return null;
	}

	public void setUI(MenuBarUI ui) {
		super.setUI(ui);
	}

	public void updateUI() {
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public SingleSelectionModel getSelectionModel() {
		return selectionModel;
	}

	public void setSelectionModel(SingleSelectionModel model) {
		this.selectionModel = model;
	}

	public JMenu add(JMenu c) {
		super.add(c);
		return c;
	}

	public JMenu getMenu(int index) {
		return null;
	}

	public int getMenuCount() {
		return getComponentCount();
	}

	public void setHelpMenu(JMenu menu) {
	}

	public JMenu getHelpMenu() {
		return null;
	}

	public Component getComponentAtIndex(int i) {
		if (i < 0 || i >= getComponentCount()) {
			return null;
		}
		return getComponent(i);
	}

	public int getComponentIndex(Component c) {
		int ncomponents = this.getComponentCount();
		Component[] component = this.getComponents();
		for (int i = 0; i < ncomponents; i++) {
			Component comp = component[i];
			if (comp == c)
				return i;
		}
		return -1;
	}

	public void setSelected(Component sel) {
	}

	public boolean isSelected() {
		return selectionModel.isSelected();
	}

	public boolean isBorderPainted() {
		return paintBorder;
	}

	public void setBorderPainted(boolean b) {
	}

	protected void paintBorder(Graphics g) {
	}

	public void setMargin(Insets m) {
	}

	public Insets getMargin() {
		if (margin == null) {
			return new Insets(0, 0, 0, 0);
		} else {
			return margin;
		}
	}

	public void processMouseEvent(MouseEvent event, MenuElement path[],
			MenuSelectionManager manager) {
	}

	public void processKeyEvent(KeyEvent e, MenuElement path[], MenuSelectionManager manager) {
	}

	public void menuSelectionChanged(boolean isIncluded) {
	}

	public MenuElement[] getSubElements() {
		return new MenuElement[0];
	}

	public Component getComponent() {
		return this;
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

	protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
		return false;
	}

	static boolean processBindingForKeyStrokeRecursive(MenuElement elem, KeyStroke ks, KeyEvent e,
			int condition, boolean pressed) {
		return false;
	}

	public void addNotify() {
	}

	public void removeNotify() {
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
	}
}