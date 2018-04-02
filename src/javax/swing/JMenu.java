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
import java.awt.ComponentOrientation;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import android.view.View;

/**
 * An implementation of a menu -- a popup window containing
 * <code>JMenuItem</code>s that is displayed when the user selects an item on
 * the <code>JMenuBar</code>. In addition to <code>JMenuItem</code>s, a
 * <code>JMenu</code> can also contain <code>JSeparator</code>s.
 * <p>
 * In essence, a menu is a button with an associated <code>JPopupMenu</code>.
 * When the "button" is pressed, the <code>JPopupMenu</code> appears. If the
 * "button" is on the <code>JMenuBar</code>, the menu is a top-level window. If
 * the "button" is another menu item, then the <code>JPopupMenu</code> is
 * "pull-right" menu.
 * <p>
 * Menus can be configured, and to some degree controlled, by
 * <code><a href="Action.html">Action</a></code>s. Using an <code>Action</code>
 * with a menu has many benefits beyond directly configuring a menu. Refer to
 * <a href="Action.html#buttonActions"> Swing Components Supporting
 * <code>Action</code></a> for more details, and you can find more information
 * in <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/misc/action.html">How to Use
 * Actions</a>, a section in <em>The Java Tutorial</em>.
 * <p>
 * For information and examples of using menus see <a href=
 * "http://java.sun.com/doc/books/tutorial/uiswing/components/menu.html">How to
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
 * @beaninfo attribute: isContainer true description: A popup window containing
 *           menu items displayed in a menu bar.
 *
 * @author Georges Saab
 * @author David Karlton
 * @author Arnaud Weber
 * @see JMenuItem
 * @see JSeparator
 * @see JMenuBar
 * @see JPopupMenu
 */
public class JMenu extends JMenuItem implements Accessible, MenuElement {
	private static final String uiClassID = "MenuUI";

	private JPopupMenu popupMenu;
	private MenuEvent menuEvent = null;
	private int delay;
	private Point customMenuLocation = null;

	public JMenu() {
		this("");
	}

	public JMenu(String s) {
		super(s);

		popupMenu = new JPopupMenu();
		popupMenu.setInvoker(this);
	}

	public JMenu(Action a) {
		this();
		setAction(a);
	}

	public JMenu(String s, boolean b) {
		this(s);
	}

	void initFocusability() {
	}

	public void updateUI() {
		super.updateUI();// 必须
	}

	public String getUIClassID() {
		return uiClassID;
	}

	View desktopView;

	public void setModel(ButtonModel newModel) {
	}

	public boolean isSelected() {
		return getModel().isSelected();
	}

	public void setSelected(boolean b) {
	}

	public boolean isPopupMenuVisible() {
		return false;
	}

	public void setPopupMenuVisible(boolean b) {
		// TODO
	}

	protected Point getPopupMenuOrigin() {
		return new Point(0, 0);
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int d) {
		delay = d;
	}

	protected WinListener popupListener;

	private void ensurePopupMenuCreated() {
	}

	private Point getCustomMenuLocation() {
		return customMenuLocation;
	}

	public void setMenuLocation(int x, int y) {
		AndroidClassUtil.callEmptyMethod();
	}

	public JMenuItem add(JMenuItem menuItem) {
		popupMenu.add(menuItem);
		return menuItem;
	}

	public Component add(Component c) {
		popupMenu.add(c);
		return c;
	}

	public Component add(Component c, int index) {
		popupMenu.add(c, index);
		return c;
	}

	public JMenuItem add(String s) {
		return add(new JMenuItem(s));
	}

	public JMenuItem add(Action a) {
		JMenuItem mi = createActionComponent(a);
		mi.setAction(a);
		add(mi);
		return mi;
	}

	protected JMenuItem createActionComponent(Action a) {
		AndroidClassUtil.callEmptyMethod();
		return new JMenuItem(a);
	}

	protected PropertyChangeListener createActionChangeListener(JMenuItem b) {
		return b.createActionPropertyChangeListener0(b.getAction());
	}

	public void addSeparator() {
		add(new JPopupMenu.Separator());
	}

	public void insert(String s, int pos) {
		insert(new JMenuItem(s), pos);
	}

	public JMenuItem insert(JMenuItem mi, int pos) {
		popupMenu.insert(mi, pos);
		return mi;
	}

	public JMenuItem insert(Action a, int pos) {
		JMenuItem item = new JMenuItem(a);
		insert(item, pos);
		return item;
	}

	public void insertSeparator(int index) {
	}

	public JMenuItem getItem(int pos) {
		return (JMenuItem) popupMenu.getComponentAtIndex(pos);
	}

	public int getItemCount() {
		return getMenuComponentCount();
	}

	public boolean isTearOff() {
		return false;
	}

	public void remove(JMenuItem item) {
		popupMenu.remove(item);
	}

	public void remove(int pos) {
		popupMenu.remove(pos);
	}

	public void remove(Component c) {
		popupMenu.remove(c);
	}

	public void removeAll() {
		popupMenu.removeAll();
	}

	public int getMenuComponentCount() {
		return popupMenu.getComponentCount();
	}

	public Component getMenuComponent(int n) {
		return popupMenu.getComponent(n);
	}

	public Component[] getMenuComponents() {
		return popupMenu.getComponents();
	}

	public boolean isTopLevelMenu() {
		return getParent() instanceof JMenuBar;
	}

	public boolean isMenuComponent(Component c) {
		return false;
	}

	private Point translateToPopupMenu(Point p) {
		return translateToPopupMenu(p.x, p.y);
	}

	private Point translateToPopupMenu(int x, int y) {
		return new Point(0, 0);
	}

	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}

	public void addMenuListener(MenuListener l) {
		list.add(MenuListener.class, l);
	}

	public void removeMenuListener(MenuListener l) {
		list.remove(MenuListener.class, l);
	}

	public MenuListener[] getMenuListeners() {
		return list.getListeners(MenuListener.class);
	}

	protected void fireMenuSelected() {
		Object[] listeners = list.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == MenuListener.class) {
				if (menuEvent == null)
					menuEvent = new MenuEvent(this);
				((MenuListener) listeners[i + 1]).menuSelected(menuEvent);
			}
		}
	}

	protected void fireMenuDeselected() {
		Object[] listeners = list.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == MenuListener.class) {
				if (menuEvent == null)
					menuEvent = new MenuEvent(this);
				((MenuListener) listeners[i + 1]).menuDeselected(menuEvent);
			}
		}
	}

	protected void fireMenuCanceled() {
		Object[] listeners = list.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == MenuListener.class) {
				if (menuEvent == null)
					menuEvent = new MenuEvent(this);
				((MenuListener) listeners[i + 1]).menuCanceled(menuEvent);
			}
		}
	}

	void configureAcceleratorFromAction(Action a) {
	}

	class MenuChangeListener implements ChangeListener, Serializable {
		boolean isSelected = false;

		public void stateChanged(ChangeEvent e) {
		}
	}

	private ChangeListener createMenuChangeListener() {
		return new MenuChangeListener();
	}

	protected WinListener createWinListener(JPopupMenu p) {
		return new WinListener(p);
	}

	protected class WinListener extends WindowAdapter implements Serializable {
		JPopupMenu popupMenu;

		public WinListener(JPopupMenu p) {
			this.popupMenu = p;
		}

		public void windowClosing(WindowEvent e) {
			setSelected(false);
		}
	}

	public void menuSelectionChanged(boolean isIncluded) {
	}

	public MenuElement[] getSubElements() {
		return new MenuElement[0];
	}

	public Component getComponent() {
		return this;
	}

	public void applyComponentOrientation(ComponentOrientation o) {
	}

	public void setComponentOrientation(ComponentOrientation o) {
	}

	public void setAccelerator(KeyStroke keyStroke) {
	}

	protected void processKeyEvent(KeyEvent evt) {
	}

	public void doClick(int pressTime) {
	}

	private MenuElement[] buildMenuElementArray(JMenu leaf) {
		return new MenuElement[0];
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	protected String paramString() {
		return super.paramString();
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

}