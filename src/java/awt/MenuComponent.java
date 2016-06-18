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
package java.awt;

import hc.android.AndroidClassUtil;

import java.awt.peer.MenuComponentPeer;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleSelection;
import javax.accessibility.AccessibleStateSet;

/**
 * The abstract class <code>MenuComponent</code> is the superclass
 * of all menu-related components. In this respect, the class
 * <code>MenuComponent</code> is analogous to the abstract superclass
 * <code>Component</code> for AWT components.
 * <p>
 * Menu components receive and process AWT events, just as components do,
 * through the method <code>processEvent</code>.
 *
 * @author      Arthur van Hoff
 * @since       JDK1.0
 */
public abstract class MenuComponent implements java.io.Serializable {

	transient MenuContainer parent;
	protected AccessibleContext accessibleContext = null;
	Font font;

	private String name;
	private boolean nameExplicitlySet = false;
	boolean newEventsOnly = false;

	public MenuComponent() throws HeadlessException {
	}

	String constructComponentName() {
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MenuContainer getParent() {
		return getParent_NoClientCode();
	}

	final MenuContainer getParent_NoClientCode() {
		return parent;
	}

	public MenuComponentPeer getPeer() {
		return null;
	}

	public Font getFont() {
		Font font = this.font;
		if (font != null) {
			return font;
		}
		return null;
	}

	final Font getFont_NoClientCode() {
		Font font = this.font;
		if (font != null) {
			return font;
		}

		return font;
	}

	public void setFont(Font f) {
		//TODO f.screenAdapter
		font = f;
	}

	public void removeNotify() {
	}

	public boolean postEvent(Event evt) {
		return false;
	}

	public final void dispatchEvent(AWTEvent e) {
		dispatchEventImpl(e);
	}

	void dispatchEventImpl(AWTEvent e) {
	}

	boolean eventEnabled(AWTEvent e) {
		return false;
	}

	protected void processEvent(AWTEvent e) {
	}

	protected String paramString() {
		String thisName = getName();
		return (thisName != null ? thisName : "");
	}

	public String toString() {
		return getClass().getName() + "[" + paramString() + "]";
	}

	protected final Object getTreeLock() {
		return this;
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException,
			IOException, HeadlessException {
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

	protected abstract class AccessibleAWTMenuComponent extends
			AccessibleContext implements java.io.Serializable,
			AccessibleComponent, AccessibleSelection {
		protected AccessibleAWTMenuComponent() {
		}

		public AccessibleSelection getAccessibleSelection() {
			return this;
		}

		public String getAccessibleName() {
			return accessibleName;
		}

		public String getAccessibleDescription() {
			return accessibleDescription;
		}

		public AccessibleRole getAccessibleRole() {
			return AccessibleRole.AWT_COMPONENT;
		}

		public AccessibleStateSet getAccessibleStateSet() {
			return MenuComponent.this.getAccessibleStateSet();
		}

		public Accessible getAccessibleParent() {
			return null;
		}

		public int getAccessibleIndexInParent() {
			return MenuComponent.this.getAccessibleIndexInParent();
		}

		public int getAccessibleChildrenCount() {
			return 0;
		}

		public Accessible getAccessibleChild(int i) {
			return null;
		}

		public java.util.Locale getLocale() {
			return java.util.Locale.getDefault();
		}

		public AccessibleComponent getAccessibleComponent() {
			return this;
		}

		public Color getBackground() {
			return null;
		}

		public void setBackground(Color c) {
		}

		public Color getForeground() {
			return null;
		}

		public void setForeground(Color c) {
		}

		public Cursor getCursor() {
			return null;
		}

		public void setCursor(Cursor cursor) {
		}

		public Font getFont() {
			return null;
		}

		public void setFont(Font f) {
		}

		public FontMetrics getFontMetrics(Font f) {
			return null;
		}

		public boolean isEnabled() {
			return true;
		}

		public void setEnabled(boolean b) {
		}

		public boolean isVisible() {
			return true;
		}

		public void setVisible(boolean b) {
		}

		public boolean isShowing() {
			return true;
		}

		public boolean contains(Point p) {
			return false;
		}

		public Point getLocationOnScreen() {
			return null;
		}

		public Point getLocation() {
			return null;
		}

		public void setLocation(Point p) {
		}

		public Rectangle getBounds() {
			return null;
		}

		public void setBounds(Rectangle r) {
		}

		public Dimension getSize() {
			return null;
		}

		public void setSize(Dimension d) {
		}

		public Accessible getAccessibleAt(Point p) {
			return null;
		}

		public boolean isFocusTraversable() {
			return true;
		}

		public void requestFocus() {
		}

		public void addFocusListener(java.awt.event.FocusListener l) {
		}

		public void removeFocusListener(java.awt.event.FocusListener l) {
		}

		public int getAccessibleSelectionCount() {
			return 0;
		}

		public Accessible getAccessibleSelection(int i) {
			return null;
		}

		public boolean isAccessibleChildSelected(int i) {
			return false;
		}

		public void addAccessibleSelection(int i) {
		}

		public void removeAccessibleSelection(int i) {
		}

		public void clearAccessibleSelection() {
		}

		public void selectAllAccessibleSelection() {
		}

	}

	int getAccessibleIndexInParent() {
		MenuContainer localParent = parent;
		if (!(localParent instanceof MenuComponent)) {
			return -1;
		}
		MenuComponent localParentMenu = (MenuComponent) localParent;
		return localParentMenu.getAccessibleChildIndex(this);
	}

	int getAccessibleChildIndex(MenuComponent child) {
		return -1;
	}

	AccessibleStateSet getAccessibleStateSet() {
		AccessibleStateSet states = new AccessibleStateSet();
		return states;
	}

}