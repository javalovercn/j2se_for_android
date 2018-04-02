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
import hc.android.HCCardLayout;
import hc.android.HCTabHost;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.TabbedPaneUI;

/**
 * A component that lets the user switch between a group of components by
 * clicking on a tab with a given title and/or icon. For examples and
 * information on using tabbed panes see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/tabbedpane.html">How
 * to Use Tabbed Panes</a>, a section in <em>The Java Tutorial</em>.
 * <p>
 * Tabs/components are added to a <code>TabbedPane</code> object by using the
 * <code>addTab</code> and <code>insertTab</code> methods. A tab is represented
 * by an index corresponding to the position it was added in, where the first
 * tab has an index equal to 0 and the last tab has an index equal to the tab
 * count minus 1.
 * <p>
 * The <code>TabbedPane</code> uses a <code>SingleSelectionModel</code> to
 * represent the set of tab indices and the currently selected index. If the tab
 * count is greater than 0, then there will always be a selected index, which by
 * default will be initialized to the first tab. If the tab count is 0, then the
 * selected index will be -1.
 * <p>
 * The tab title can be rendered by a <code>Component</code>. For example, the
 * following produce similar results:
 * 
 * <pre>
 * // In this case the look and feel renders the title for the tab.
 * tabbedPane.addTab("Tab", myComponent);
 * // In this case the custom component is responsible for rendering the
 * // title of the tab.
 * tabbedPane.addTab(null, myComponent);
 * tabbedPane.setTabComponentAt(0, new JLabel("Tab"));
 * </pre>
 * 
 * The latter is typically used when you want a more complex user interaction
 * that requires custom components on the tab. For example, you could provide a
 * custom component that animates or one that has widgets for closing the tab.
 * <p>
 * If you specify a component for a tab, the <code>JTabbedPane</code> will not
 * render any text or icon you have specified for the tab.
 * <p>
 * <strong>Note:</strong> Do not use <code>setVisible</code> directly on a tab
 * component to make it visible, use <code>setSelectedComponent</code> or
 * <code>setSelectedIndex</code> methods instead.
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
 * @beaninfo attribute: isContainer true description: A component which provides
 *           a tab folder metaphor for displaying one component from a set of
 *           components.
 *
 * @author Dave Moore
 * @author Philip Milne
 * @author Amy Fowler
 *
 * @see SingleSelectionModel
 */
public class JTabbedPane extends JComponent implements Serializable, Accessible, SwingConstants {

	public static final int WRAP_TAB_LAYOUT = 0;
	public static final int SCROLL_TAB_LAYOUT = 1;

	private static final String uiClassID = "TabbedPaneUI";

	protected int tabPlacement = TOP;
	private int tabLayoutPolicy;
	protected SingleSelectionModel model;
	private boolean haveRegistered;

	protected ChangeListener changeListener = null;

	private HCCardLayout cardLayout = null;

	/**
	 * Only one <code>ChangeEvent</code> is needed per <code>TabPane</code>
	 * instance since the event's only (read-only) state is the source property.
	 * The source of events generated here is always "this".
	 */
	protected transient ChangeEvent changeEvent = null;

	public JTabbedPane() {
		this(TOP, WRAP_TAB_LAYOUT);
	}

	public JTabbedPane(int tabPlacement) {
		this(tabPlacement, WRAP_TAB_LAYOUT);
	}

	public JTabbedPane(int tabPlacement, int tabLayoutPolicy) {
		setTabPlacement(tabPlacement);
		setTabLayoutPolicy(tabLayoutPolicy);
		cardLayout = new HCCardLayout();
		setLayout(cardLayout);
		setModel(new DefaultSingleSelectionModel());
		updateUI();
	}

	public TabbedPaneUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return (TabbedPaneUI) null;
	}

	public void setUI(TabbedPaneUI ui) {
		super.setUI(ui);
		AndroidClassUtil.callEmptyMethod();
	}

	public void updateUI() {
		AndroidClassUtil.callEmptyMethod();
	}

	public String getUIClassID() {
		return uiClassID;
	}

	protected class ModelListener implements ChangeListener, Serializable {
		public void stateChanged(ChangeEvent e) {
			fireStateChanged();
		}
	}

	protected ChangeListener createChangeListener() {
		return new ModelListener();
	}

	public void addChangeListener(ChangeListener l) {
		list.add(ChangeListener.class, l);
	}

	/**
	 * Removes a <code>ChangeListener</code> from this tabbedpane.
	 * 
	 * @param l
	 *            the <code>ChangeListener</code> to remove
	 * @see #fireStateChanged
	 * @see #addChangeListener
	 */
	public void removeChangeListener(ChangeListener l) {
		list.remove(ChangeListener.class, l);
	}

	/**
	 * Returns an array of all the <code>ChangeListener</code>s added to this
	 * <code>JTabbedPane</code> with <code>addChangeListener</code>.
	 * 
	 * @return all of the <code>ChangeListener</code>s added or an empty array
	 *         if no listeners have been added
	 * @since 1.4
	 */
	public ChangeListener[] getChangeListeners() {
		return list.getListeners(ChangeListener.class);
	}

	/**
	 * Sends a {@code ChangeEvent}, with this {@code JTabbedPane} as the source,
	 * to each registered listener. This method is called each time there is a
	 * change to either the selected index or the selected tab in the
	 * {@code JTabbedPane}. Usually, the selected index and selected tab change
	 * together. However, there are some cases, such as tab addition, where the
	 * selected index changes and the same tab remains selected. There are other
	 * cases, such as deleting the selected tab, where the index remains the
	 * same, but a new tab moves to that index. Events are fired for all of
	 * these cases.
	 * 
	 * @see #addChangeListener
	 * @see EventListenerList
	 */
	protected void fireStateChanged() {
		if (changeEvent == null) {
			changeEvent = new ChangeEvent(this);
		}
		ChangeListener[] listeners = list.getListeners(ChangeListener.class);
		for (int i = listeners.length - 1; i >= 0; i--) {
			((ChangeListener) listeners[i]).stateChanged(changeEvent);
		}
	}

	public SingleSelectionModel getModel() {
		return model;
	}

	public void setModel(final SingleSelectionModel model) {
		SingleSelectionModel oldModel = getModel();

		if (oldModel != null) {
			oldModel.removeChangeListener(changeListener);
			changeListener = null;
		}

		this.model = model;

		if (model != null) {
			getHCTabHostAdAPI().setSingleSelectionMode(model);
			this.model = new SingleSelectionModel() {
				@Override
				public void setSelectedIndex(int index) {
					JTabbedPane.this.getHCTabHostAdAPI().setCurrentTab(index);
					model.setSelectedIndex(index);
				}

				@Override
				public void removeChangeListener(ChangeListener listener) {
					model.removeChangeListener(listener);
				}

				@Override
				public boolean isSelected() {
					return model.isSelected();
				}

				@Override
				public int getSelectedIndex() {
					return model.getSelectedIndex();
				}

				@Override
				public void clearSelection() {
					// 因为JTabbedPane须显示一个，同时与CardLayout逻辑一致。所以关闭此功能
					// model.clearSelection();
				}

				@Override
				public void addChangeListener(ChangeListener listener) {
					model.addChangeListener(listener);
				}
			};
			changeListener = createChangeListener();
			model.addChangeListener(changeListener);
		}
	}

	private HCCardLayout getCardLayoutAdAPI() {
		return (HCCardLayout) this.getLayout();
	}

	private HCTabHost getHCTabHostAdAPI() {
		return (HCTabHost) this.getContainerViewAdAPI();
	}

	public int getTabPlacement() {
		return tabPlacement;
	}

	public void setTabPlacement(int tabPlacement) {
		if (tabPlacement != TOP && tabPlacement != LEFT && tabPlacement != BOTTOM
				&& tabPlacement != RIGHT) {
			throw new IllegalArgumentException(
					"illegal tab placement: must be TOP, BOTTOM, LEFT, or RIGHT");
		}
		if (this.tabPlacement != tabPlacement) {
			this.tabPlacement = tabPlacement;
		}
	}

	public int getTabLayoutPolicy() {
		return tabLayoutPolicy;
	}

	public void setTabLayoutPolicy(int tabLayoutPolicy) {
		if (tabLayoutPolicy != WRAP_TAB_LAYOUT && tabLayoutPolicy != SCROLL_TAB_LAYOUT) {
			throw new IllegalArgumentException(
					"illegal tab layout policy: must be WRAP_TAB_LAYOUT or SCROLL_TAB_LAYOUT");
		}
		if (this.tabLayoutPolicy != tabLayoutPolicy) {
			this.tabLayoutPolicy = tabLayoutPolicy;
		}
	}

	@Transient
	public int getSelectedIndex() {
		return model.getSelectedIndex();
	}

	public void setSelectedIndex(int index) {
		if (index != -1) {
			checkIndex(index);
		}
		model.setSelectedIndex(index);
	}

	@Transient
	public Component getSelectedComponent() {
		int index = getSelectedIndex();
		if (index == -1) {
			return null;
		}
		return getComponentAt(index);
	}

	public void setSelectedComponent(Component c) {
		int index = indexOfComponent(c);
		if (index != -1) {
			setSelectedIndex(index);
		} else {
			throw new IllegalArgumentException("component not found in tabbed pane");
		}
	}

	public void insertTab(String title, Icon icon, Component component, String tip, int index) {
		if (component != null) {
			TabParameter para = new TabParameter(title, icon, tip, index);
			addImpl(component, para, index);
		}
	}

	public void addTab(String title, Icon icon, Component component, String tip) {
		insertTab(title, icon, component, tip, getComponentCount());
	}

	public void addTab(String title, Icon icon, Component component) {
		insertTab(title, icon, component, null, getComponentCount());
	}

	public void addTab(String title, Component component) {
		insertTab(title, null, component, null, getComponentCount());
	}

	public Component add(Component component) {
		addTab(component.getName(), component);
		return component;
	}

	public Component add(String title, Component component) {
		addTab(title, component);
		return component;
	}

	public Component add(Component component, int index) {
		insertTab(component.getName(), null, component, null, index == -1 ? getTabCount() : index);
		return component;
	}

	public void add(Component component, Object constraints) {
		if (constraints instanceof String) {
			addTab((String) constraints, component);
		} else if (constraints instanceof Icon) {
			addTab(null, (Icon) constraints, component);
		} else {
			add(component);
		}
	}

	public void add(Component component, Object constraints, int index) {
		Icon icon = constraints instanceof Icon ? (Icon) constraints : null;
		String title = constraints instanceof String ? (String) constraints : null;
		insertTab(title, icon, component, null, index == -1 ? getTabCount() : index);
	}

	public void removeTabAt(int index) {
		remove(index);
	}

	public void remove(Component component) {
		int index = indexOfComponent(component);
		if (index != -1) {
			removeTabAt(index);
		}
	}

	public void remove(int index) {
		super.remove(index);
		getCardLayoutAdAPI().vector.remove(index);
		revalidate();
		repaint();
	}

	public void removeAll() {
		int tabCount = getTabCount();
		while (tabCount-- > 0) {
			removeTabAt(tabCount);
		}
	}

	public int getTabCount() {
		return getComponentCount();
	}

	public int getTabRunCount() {
		return getComponentCount();
	}

	public String getTitleAt(int index) {
		return getCardLayoutAdAPI().vector.get(index).para.tag;
	}

	public Icon getIconAt(int index) {
		return getCardLayoutAdAPI().vector.get(index).para.icon;
	}

	public Icon getDisabledIconAt(int index) {
		return getIconAt(index);
	}

	public String getToolTipTextAt(int index) {
		return getCardLayoutAdAPI().vector.get(index).para.tip;
	}

	public Color getBackgroundAt(int index) {
		AndroidClassUtil.callEmptyMethod();
		return Color.white;
	}

	public Color getForegroundAt(int index) {
		AndroidClassUtil.callEmptyMethod();
		return Color.WHITE;
	}

	public boolean isEnabledAt(int index) {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public Component getComponentAt(int index) {
		return super.getComponent(index);
	}

	public int getMnemonicAt(int tabIndex) {
		checkIndex(tabIndex);
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public int getDisplayedMnemonicIndexAt(int tabIndex) {
		checkIndex(tabIndex);
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public Rectangle getBoundsAt(int index) {
		checkIndex(index);
		return new Rectangle(0, 0);
	}

	public void setTitleAt(int index, String title) {
		getCardLayoutAdAPI().vector.get(index).para.tag = title;
		validate();
		revalidate();
	}

	public void setIconAt(int index, Icon icon) {
		getCardLayoutAdAPI().vector.get(index).para.icon = icon;
		validate();
		revalidate();
	}

	public void setDisabledIconAt(int index, Icon disabledIcon) {
		setIconAt(index, disabledIcon);
	}

	public void setToolTipTextAt(int index, String toolTipText) {
		getCardLayoutAdAPI().vector.get(index).para.tip = toolTipText;
		validate();
		revalidate();
	}

	public void setBackgroundAt(int index, Color background) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setForegroundAt(int index, Color foreground) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setEnabledAt(int index, boolean enabled) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setComponentAt(int index, Component component) {
		int oldIdx = indexOfComponent(component);
		HCCardLayout.Card oldCard = null;
		if (oldIdx >= 0) {
			super.remove(component);
			oldCard = getCardLayoutAdAPI().vector.remove(oldIdx);
		}
		super.add(component, index);
		if (oldCard == null) {
			oldCard = new HCCardLayout.Card(new TabParameter(component.getName(), null, "", index),
					component);
		} else {
			oldCard.comp = component;
		}
		getCardLayoutAdAPI().vector.add(oldIdx, oldCard);

		validate();
		revalidate();
	}

	public void setDisplayedMnemonicIndexAt(int tabIndex, int mnemonicIndex) {
		checkIndex(tabIndex);
		AndroidClassUtil.callEmptyMethod();
	}

	public void setMnemonicAt(int tabIndex, int mnemonic) {
		checkIndex(tabIndex);
		AndroidClassUtil.callEmptyMethod();
	}

	public int indexOfTab(String title) {
		for (int i = 0; i < getTabCount(); i++) {
			if (getTitleAt(i).equals(title == null ? "" : title)) {
				return i;
			}
		}
		return -1;
	}

	public int indexOfTab(Icon icon) {
		for (int i = 0; i < getTabCount(); i++) {
			Icon tabIcon = getIconAt(i);
			if ((tabIcon != null && tabIcon.equals(icon)) || (tabIcon == null && tabIcon == icon)) {
				return i;
			}
		}
		return -1;
	}

	public int indexOfComponent(Component component) {
		for (int i = 0; i < getTabCount(); i++) {
			Component c = getComponentAt(i);
			if ((c != null && c.equals(component)) || (c == null && c == component)) {
				return i;
			}
		}
		return -1;
	}

	public int indexAtLocation(int x, int y) {
		AndroidClassUtil.callEmptyMethod();
		return -1;
	}

	public String getToolTipText(MouseEvent event) {
		AndroidClassUtil.callEmptyMethod();
		return "";
	}

	private void checkIndex(int index) {
		if (index < 0 || index >= getComponentCount()) {
			throw new IndexOutOfBoundsException(
					"Index: " + index + ", Tab count: " + getComponentCount());
		}
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	void compWriteObjectNotify() {
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
	}

	protected String paramString() {
		return "";
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = AndroidClassUtil.buildAccessibleContext(this);
		}
		return accessibleContext;
	}

	public void setTabComponentAt(int index, Component component) {
		if (component != null && indexOfComponent(component) != -1) {
			throw new IllegalArgumentException("Component is already added to this JTabbedPane");
		}
		setComponentAt(index, component);
	}

	public Component getTabComponentAt(int index) {
		return getCardLayoutAdAPI().vector.get(index).comp;
	}

	public int indexOfTabComponent(Component tabComponent) {
		for (int i = 0; i < getTabCount(); i++) {
			Component c = getTabComponentAt(i);
			if (c == tabComponent) {
				return i;
			}
		}
		return -1;
	}

	public static class TabParameter {
		public String tag;
		public Icon icon;
		public String tip;
		public int idx;

		public TabParameter(String tag, Icon icon, String tip, int idx) {
			this.tag = tag;
			this.icon = icon;
			this.tip = tip;
			this.idx = idx;
		}
	}
}
