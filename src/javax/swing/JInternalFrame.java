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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleValue;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.plaf.DesktopIconUI;
import javax.swing.plaf.InternalFrameUI;

/**
 * A lightweight object that provides many of the features of a native frame,
 * including dragging, closing, becoming an icon, resizing, title display, and
 * support for a menu bar. For task-oriented documentation and examples of using
 * internal frames, see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/internalframe.html"
 * target="_top">How to Use Internal Frames</a>, a section in <em>The Java
 * Tutorial</em>.
 *
 * <p>
 *
 * Generally, you add <code>JInternalFrame</code>s to a
 * <code>JDesktopPane</code>. The UI delegates the look-and-feel-specific
 * actions to the <code>DesktopManager</code> object maintained by the
 * <code>JDesktopPane</code>.
 * <p>
 * The <code>JInternalFrame</code> content pane is where you add child
 * components. As a conveniance <code>add</code> and its variants,
 * <code>remove</code> and <code>setLayout</code> have been overridden to
 * forward to the <code>contentPane</code> as necessary. This means you can
 * write:
 * 
 * <pre>
 * internalFrame.add(child);
 * </pre>
 * 
 * And the child will be added to the contentPane. The content pane is actually
 * managed by an instance of <code>JRootPane</code>, which also manages a layout
 * pane, glass pane, and optional menu bar for the internal frame. Please see
 * the <code>JRootPane</code> documentation for a complete description of these
 * components. Refer to {@link javax.swing.RootPaneContainer} for details on
 * adding, removing and setting the <code>LayoutManager</code> of a
 * <code>JInternalFrame</code>.
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
 * @see InternalFrameEvent
 * @see JDesktopPane
 * @see DesktopManager
 * @see JInternalFrame.JDesktopIcon
 * @see JRootPane
 * @see javax.swing.RootPaneContainer
 *
 * @author David Kloba
 * @author Rich Schiavi
 * @beaninfo attribute: isContainer true attribute: containerDelegate
 *           getContentPane description: A frame container which is contained
 *           within another window.
 */
public class JInternalFrame extends JComponent
		implements Accessible, WindowConstants, RootPaneContainer {
	private static final String uiClassID = "InternalFrameUI";

	protected JRootPane rootPane;

	protected boolean rootPaneCheckingEnabled = false;

	protected boolean isSelected;
	protected Icon frameIcon;
	protected String title;
	protected JDesktopIcon desktopIcon;
	private Cursor lastCursor;
	private boolean opened;
	private Rectangle normalBounds = null;
	private int defaultCloseOperation = DISPOSE_ON_CLOSE;

	private Component lastFocusOwner;

	public final static String CONTENT_PANE_PROPERTY = "contentPane";
	public final static String MENU_BAR_PROPERTY = "JMenuBar";
	public final static String TITLE_PROPERTY = "title";
	public final static String LAYERED_PANE_PROPERTY = "layeredPane";
	public final static String ROOT_PANE_PROPERTY = "rootPane";
	public final static String GLASS_PANE_PROPERTY = "glassPane";
	public final static String FRAME_ICON_PROPERTY = "frameIcon";

	public final static String IS_SELECTED_PROPERTY = "selected";
	public final static String IS_CLOSED_PROPERTY = "closed";
	public final static String IS_MAXIMUM_PROPERTY = "maximum";
	public final static String IS_ICON_PROPERTY = "icon";

	private static final Object PROPERTY_CHANGE_LISTENER_KEY = new StringBuilder(
			"InternalFramePropertyChangeListener");

	public JInternalFrame() {
		this("", false, false, false, false);
	}

	public JInternalFrame(String title) {
		this(title, false, false, false, false);
	}

	public JInternalFrame(String title, boolean resizable) {
		this(title, resizable, false, false, false);
	}

	public JInternalFrame(String title, boolean resizable, boolean closable) {
		this(title, resizable, closable, false, false);
	}

	public JInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable) {
		this(title, resizable, closable, maximizable, false);
	}

	public JInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable,
			boolean iconifiable) {

		setLayout(new BorderLayout());
		setRootPane(createRootPane());
		this.title = title;
		setVisible(false);
		setRootPaneCheckingEnabled(true);
		desktopIcon = new JDesktopIcon(this);
		updateUI();
	}

	protected JRootPane createRootPane() {
		return new JRootPane();
	}

	public InternalFrameUI getUI() {
		return (InternalFrameUI) null;
	}

	public void setUI(InternalFrameUI ui) {
	}

	public void updateUI() {
	}

	void updateUIWhenHidden() {
	}

	public String getUIClassID() {
		return uiClassID;
	}

	protected boolean isRootPaneCheckingEnabled() {
		return rootPaneCheckingEnabled;
	}

	protected void setRootPaneCheckingEnabled(boolean enabled) {
		rootPaneCheckingEnabled = enabled;
	}

	protected void addImpl(Component comp, Object constraints, int index) {
		if (isRootPaneCheckingEnabled()) {
			getContentPane().add(comp, constraints, index);
		} else {
			super.addImpl(comp, constraints, index);
		}
	}

	public void remove(Component comp) {
		int oldCount = getComponentCount();
		super.remove(comp);
		if (oldCount == getComponentCount()) {
			getContentPane().remove(comp);
		}
	}

	public void setLayout(LayoutManager manager) {
		if (isRootPaneCheckingEnabled()) {
			getContentPane().setLayout(manager);
		} else {
			super.setLayout(manager);
		}
	}

	@Deprecated
	public JMenuBar getMenuBar() {
		return getRootPane().getMenuBar();
	}

	public JMenuBar getJMenuBar() {
		return getRootPane().getJMenuBar();
	}

	@Deprecated
	public void setMenuBar(JMenuBar m) {
		JMenuBar oldValue = getMenuBar();
		getRootPane().setJMenuBar(m);
		firePropertyChange(MENU_BAR_PROPERTY, oldValue, m);
	}

	public void setJMenuBar(JMenuBar m) {
		JMenuBar oldValue = getMenuBar();
		getRootPane().setJMenuBar(m);
		firePropertyChange(MENU_BAR_PROPERTY, oldValue, m);
	}

	public Container getContentPane() {
		return getRootPane().getContentPane();
	}

	public void setContentPane(Container c) {
		Container oldValue = getContentPane();
		getRootPane().setContentPane(c);
		firePropertyChange(CONTENT_PANE_PROPERTY, oldValue, c);
	}

	public JLayeredPane getLayeredPane() {
		return getRootPane().getLayeredPane();
	}

	public void setLayeredPane(JLayeredPane layered) {
		JLayeredPane oldValue = getLayeredPane();
		getRootPane().setLayeredPane(layered);
		firePropertyChange(LAYERED_PANE_PROPERTY, oldValue, layered);
	}

	public Component getGlassPane() {
		return getRootPane().getGlassPane();
	}

	public void setGlassPane(Component glass) {
		Component oldValue = getGlassPane();
		getRootPane().setGlassPane(glass);
		firePropertyChange(GLASS_PANE_PROPERTY, oldValue, glass);
	}

	public JRootPane getRootPane() {
		return rootPane;
	}

	protected void setRootPane(JRootPane root) {
		if (rootPane != null) {
			remove(rootPane);
		}
		JRootPane oldValue = getRootPane();
		rootPane = root;
		if (rootPane != null) {
			boolean checkingEnabled = isRootPaneCheckingEnabled();
			try {
				setRootPaneCheckingEnabled(false);
				add(rootPane, BorderLayout.CENTER);
			} finally {
				setRootPaneCheckingEnabled(checkingEnabled);
			}
		}
		firePropertyChange(ROOT_PANE_PROPERTY, oldValue, root);
	}

	public void setClosable(boolean b) {
	}

	public boolean isClosable() {
		return true;
	}

	public boolean isClosed() {
		return false;
	}

	public void setClosed(boolean b) throws PropertyVetoException {
		setVisible(false);
		dispose();
	}

	public void setResizable(boolean b) {
	}

	public boolean isResizable() {
		return false;
	}

	public void setIconifiable(boolean b) {
	}

	public boolean isIconifiable() {
		return false;
	}

	public boolean isIcon() {
		return false;
	}

	public void setIcon(boolean b) throws PropertyVetoException {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public void setMaximizable(boolean b) {
	}

	public boolean isMaximizable() {
		return false;
	}

	public boolean isMaximum() {
		return false;
	}

	public void setMaximum(boolean b) throws PropertyVetoException {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		String oldValue = this.title;
		this.title = title;
		firePropertyChange(TITLE_PROPERTY, oldValue, title);
	}

	public void setSelected(boolean selected) throws PropertyVetoException {
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setFrameIcon(Icon icon) {
		Icon oldIcon = frameIcon;
		frameIcon = icon;
		firePropertyChange(FRAME_ICON_PROPERTY, oldIcon, icon);
	}

	public Icon getFrameIcon() {
		return frameIcon;
	}

	public void moveToFront() {
	}

	public void moveToBack() {
	}

	public Cursor getLastCursor() {
		return lastCursor;
	}

	public void setCursor(Cursor cursor) {
	}

	public void setLayer(Integer layer) {
		if (getParent() != null && getParent() instanceof JLayeredPane) {
			// Normally we want to do this, as it causes the LayeredPane
			// to draw properly.
			JLayeredPane p = (JLayeredPane) getParent();
			p.setLayer(this, layer.intValue(), p.getPosition(this));
		} else {
			// Try to do the right thing
			JLayeredPane.putLayer(this, layer.intValue());
			if (getParent() != null)
				getParent().repaint(getX(), getY(), getWidth(), getHeight());
		}
	}

	public void setLayer(int layer) {
		this.setLayer(Integer.valueOf(layer));
	}

	public int getLayer() {
		return JLayeredPane.getLayer(this);
	}

	public JDesktopPane getDesktopPane() {
		Container p;

		// Search upward for desktop
		p = getParent();
		while (p != null && !(p instanceof JDesktopPane))
			p = p.getParent();

		if (p == null) {
			// search its icon parent for desktop
			p = getDesktopIcon().getParent();
			while (p != null && !(p instanceof JDesktopPane))
				p = p.getParent();
		}

		return (JDesktopPane) p;
	}

	public void setDesktopIcon(JDesktopIcon d) {
		desktopIcon = d;
	}

	public JDesktopIcon getDesktopIcon() {
		return desktopIcon;
	}

	public Rectangle getNormalBounds() {
		if (normalBounds != null) {
			return normalBounds;
		} else {
			return getBounds();
		}
	}

	public void setNormalBounds(Rectangle r) {
		normalBounds = r;
	}

	public Component getFocusOwner() {
		if (isSelected()) {
			return lastFocusOwner;
		}
		return null;
	}

	public Component getMostRecentFocusOwner() {
		if (isSelected()) {
			return getFocusOwner();
		}

		if (lastFocusOwner != null) {
			return lastFocusOwner;
		}

		return getContentPane();
	}

	public void restoreSubcomponentFocus() {
	}

	private void setLastFocusOwner(Component component) {
		lastFocusOwner = component;
	}

	public void reshape(int x, int y, int width, int height) {
		super.reshape(x, y, width, height);
		validate();
		repaint();
	}

	public void addInternalFrameListener(InternalFrameListener l) {
		list.add(InternalFrameListener.class, l);
	}

	public void removeInternalFrameListener(InternalFrameListener l) {
		list.remove(InternalFrameListener.class, l);
	}

	public InternalFrameListener[] getInternalFrameListeners() {
		return list.getListeners(InternalFrameListener.class);
	}

	protected void fireInternalFrameEvent(int id) {
		Object[] listeners = list.getListenerList();
		InternalFrameEvent e = null;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == InternalFrameListener.class) {
				if (e == null) {
					e = new InternalFrameEvent(this, id);
					// System.out.println("InternalFrameEvent: " +
					// e.paramString());
				}
				switch (e.getID()) {
				case InternalFrameEvent.INTERNAL_FRAME_OPENED:
					((InternalFrameListener) listeners[i + 1]).internalFrameOpened(e);
					break;
				case InternalFrameEvent.INTERNAL_FRAME_CLOSING:
					((InternalFrameListener) listeners[i + 1]).internalFrameClosing(e);
					break;
				case InternalFrameEvent.INTERNAL_FRAME_CLOSED:
					((InternalFrameListener) listeners[i + 1]).internalFrameClosed(e);
					break;
				case InternalFrameEvent.INTERNAL_FRAME_ICONIFIED:
					((InternalFrameListener) listeners[i + 1]).internalFrameIconified(e);
					break;
				case InternalFrameEvent.INTERNAL_FRAME_DEICONIFIED:
					((InternalFrameListener) listeners[i + 1]).internalFrameDeiconified(e);
					break;
				case InternalFrameEvent.INTERNAL_FRAME_ACTIVATED:
					((InternalFrameListener) listeners[i + 1]).internalFrameActivated(e);
					break;
				case InternalFrameEvent.INTERNAL_FRAME_DEACTIVATED:
					((InternalFrameListener) listeners[i + 1]).internalFrameDeactivated(e);
					break;
				default:
					break;
				}
			}
		}
	}

	public void doDefaultCloseAction() {
		fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_CLOSING);
		switch (defaultCloseOperation) {
		case DO_NOTHING_ON_CLOSE:
			break;
		case HIDE_ON_CLOSE:
			setVisible(false);
			if (isSelected())
				try {
					setSelected(false);
				} catch (PropertyVetoException pve) {
				}
			break;
		case DISPOSE_ON_CLOSE:
			try {
				fireVetoableChange(IS_CLOSED_PROPERTY, Boolean.FALSE, Boolean.TRUE);
				setVisible(false);
				firePropertyChange(IS_CLOSED_PROPERTY, Boolean.FALSE, Boolean.TRUE);
				dispose();
			} catch (Exception pve) {
			}
			break;
		default:
			break;
		}
	}

	public void setDefaultCloseOperation(int operation) {
		this.defaultCloseOperation = operation;
	}

	public int getDefaultCloseOperation() {
		return defaultCloseOperation;
	}

	public void pack() {
		setSize(getPreferredSize());
		validate();
	}

	public void show() {
		if (isVisible()) {
			return;
		}

		if (!opened) {
			fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_OPENED);
			opened = true;
		}

		getDesktopIcon().setVisible(true);

		toFront();
		super.show();

	}

	public void hide() {
		if (isIcon()) {
			getDesktopIcon().setVisible(false);
		}
		super.hide();
	}

	public void dispose() {
		if (isVisible()) {
			setVisible(false);
		}
		if (isSelected()) {
			try {
				setSelected(false);
			} catch (PropertyVetoException pve) {
			}
		}
		fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_CLOSED);
	}

	public void toFront() {
		moveToFront();
	}

	public void toBack() {
		moveToBack();
	}

	public final void setFocusCycleRoot(boolean focusCycleRoot) {
	}

	public final boolean isFocusCycleRoot() {
		return true;
	}

	public final Container getFocusCycleRootAncestor() {
		return null;
	}

	public final String getWarningString() {
		return null;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	void compWriteObjectNotify() {
	}

	protected String paramString() {
		return "";
	}

	boolean isDragging = false;
	boolean danger = false;

	protected void paintComponent(Graphics g) {
		if (isDragging) {
			danger = true;
		}

		super.paintComponent(g);
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

	static public class JDesktopIcon extends JComponent implements Accessible {
		JInternalFrame internalFrame;

		public JDesktopIcon(JInternalFrame f) {
			setVisible(false);
			setInternalFrame(f);
			updateUI();
		}

		public DesktopIconUI getUI() {
			return (DesktopIconUI) null;
		}

		public void setUI(DesktopIconUI ui) {
			super.setUI(ui);
		}

		public JInternalFrame getInternalFrame() {
			return internalFrame;
		}

		public void setInternalFrame(JInternalFrame f) {
			internalFrame = f;
		}

		public JDesktopPane getDesktopPane() {
			if (getInternalFrame() != null)
				return getInternalFrame().getDesktopPane();
			return null;
		}

		public void updateUI() {
		}

		void updateUIWhenHidden() {
		}

		public String getUIClassID() {
			return "DesktopIconUI";
		}

		private void writeObject(ObjectOutputStream s) throws IOException {
		}

		public AccessibleContext getAccessibleContext() {
			if (accessibleContext == null) {
				accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
			}
			return accessibleContext;
		}

	}
}
