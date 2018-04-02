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
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.IllegalComponentStateException;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.WindowListener;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;

/**
 * A <code>JWindow</code> is a container that can be displayed anywhere on the
 * user's desktop. It does not have the title bar, window-management buttons, or
 * other trimmings associated with a <code>JFrame</code>, but it is still a
 * "first-class citizen" of the user's desktop, and can exist anywhere on it.
 * <p>
 * The <code>JWindow</code> component contains a <code>JRootPane</code> as its
 * only child. The <code>contentPane</code> should be the parent of any children
 * of the <code>JWindow</code>. As a conveniance <code>add</code> and its
 * variants, <code>remove</code> and <code>setLayout</code> have been overridden
 * to forward to the <code>contentPane</code> as necessary. This means you can
 * write:
 * 
 * <pre>
 * window.add(child);
 * </pre>
 * 
 * And the child will be added to the contentPane. The <code>contentPane</code>
 * will always be non-<code>null</code>. Attempting to set it to
 * <code>null</code> will cause the <code>JWindow</code> to throw an exception.
 * The default <code>contentPane</code> will have a <code>BorderLayout</code>
 * manager set on it. Refer to {@link javax.swing.RootPaneContainer} for details
 * on adding, removing and setting the <code>LayoutManager</code> of a
 * <code>JWindow</code>.
 * <p>
 * Please see the {@link JRootPane} documentation for a complete description of
 * the <code>contentPane</code>, <code>glassPane</code>, and
 * <code>layeredPane</code> components.
 * <p>
 * In a multi-screen environment, you can create a <code>JWindow</code> on a
 * different screen device. See {@link java.awt.Window} for more information.
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
 * @see JRootPane
 *
 * @beaninfo attribute: isContainer true attribute: containerDelegate
 *           getContentPane description: A toplevel window which has no system
 *           border or controls.
 *
 * @author David Kloba
 */
public class JWindow extends Window
		implements Accessible, RootPaneContainer, TransferHandler.HasGetTransferHandler {
	protected JRootPane rootPane;
	protected boolean rootPaneCheckingEnabled = false;
	private TransferHandler transferHandler;

	public JWindow() {
		this((Frame) null);
	}

	public JWindow(GraphicsConfiguration gc) {
		this(null, gc);
		super.setFocusableWindowState(false);
	}

	public JWindow(Frame owner) {
		super(owner == null ? SwingUtilities.getSharedOwnerFrame() : owner);
		if (owner == null) {
			WindowListener ownerShutdownListener = SwingUtilities
					.getSharedOwnerFrameShutdownListener();
			addWindowListener(ownerShutdownListener);
		}
		windowInit();
	}

	public JWindow(Window owner) {
		super(owner == null ? (Window) SwingUtilities.getSharedOwnerFrame() : owner);
		if (owner == null) {
			WindowListener ownerShutdownListener = SwingUtilities
					.getSharedOwnerFrameShutdownListener();
			addWindowListener(ownerShutdownListener);
		}
		windowInit();
	}

	public JWindow(Window owner, GraphicsConfiguration gc) {
		super(owner == null ? (Window) SwingUtilities.getSharedOwnerFrame() : owner, gc);
		if (owner == null) {
			WindowListener ownerShutdownListener = SwingUtilities
					.getSharedOwnerFrameShutdownListener();
			addWindowListener(ownerShutdownListener);
		}
		windowInit();
	}

	protected void windowInit() {
		setLocale(JComponent.getDefaultLocale());
		setRootPane(createRootPane());
		setRootPaneCheckingEnabled(true);
	}

	protected JRootPane createRootPane() {
		JRootPane rp = new JRootPane();
		rp.currWindow = this;
		rp.setOpaque(true);
		return rp;
	}

	protected boolean isRootPaneCheckingEnabled() {
		return rootPaneCheckingEnabled;
	}

	public void setTransferHandler(TransferHandler newHandler) {
		TransferHandler oldHandler = transferHandler;
		transferHandler = newHandler;
		SwingUtilities.installSwingDropTargetAsNecessary(this, transferHandler);
		firePropertyChange("transferHandler", oldHandler, newHandler);
	}

	public TransferHandler getTransferHandler() {
		return transferHandler;
	}

	public void update(Graphics g) {
		paint(g);
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
		if (comp == rootPane) {
			super.remove(comp);
		} else {
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

	public JRootPane getRootPane() {
		return rootPane;
	}

	protected void setRootPane(JRootPane root) {
		if (rootPane != null) {
			remove(rootPane);
		}
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
	}

	public Container getContentPane() {
		return getRootPane().getContentPane();
	}

	public void setContentPane(Container contentPane) {
		getRootPane().setContentPane(contentPane);
	}

	public JLayeredPane getLayeredPane() {
		return getRootPane().getLayeredPane();
	}

	public void setLayeredPane(JLayeredPane layeredPane) {
		getRootPane().setLayeredPane(layeredPane);
	}

	public Component getGlassPane() {
		return getRootPane().getGlassPane();
	}

	public void setGlassPane(Component glassPane) {
		getRootPane().setGlassPane(glassPane);
	}

	public Graphics getGraphics() {
		return null;
	}

	public void repaint(long time, int x, int y, int width, int height) {
	}

	protected String paramString() {
		return "";
	}

	protected AccessibleContext accessibleContext = null;

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

}
