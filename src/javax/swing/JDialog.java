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
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;

/**
 * The main class for creating a dialog window. You can use this class to create
 * a custom dialog, or invoke the many class methods in {@link JOptionPane} to
 * create a variety of standard dialogs. For information about creating dialogs,
 * see <em>The Java Tutorial</em> section <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/dialog.html">How
 * to Make Dialogs</a>.
 *
 * <p>
 *
 * The {@code JDialog} component contains a {@code JRootPane} as its only child.
 * The {@code contentPane} should be the parent of any children of the
 * {@code JDialog}. As a convenience {@code add} and its variants,
 * {@code remove} and {@code setLayout} have been overridden to forward to the
 * {@code contentPane} as necessary. This means you can write:
 * 
 * <pre>
 * dialog.add(child);
 * </pre>
 * 
 * And the child will be added to the contentPane. The {@code contentPane} is
 * always non-{@code null}. Attempting to set it to {@code null} generates an
 * exception. The default {@code contentPane} has a {@code BorderLayout} manager
 * set on it. Refer to {@link javax.swing.RootPaneContainer} for details on
 * adding, removing and setting the {@code LayoutManager} of a {@code JDialog}.
 * <p>
 * Please see the {@code JRootPane} documentation for a complete description of
 * the {@code contentPane}, {@code glassPane}, and {@code layeredPane}
 * components.
 * <p>
 * In a multi-screen environment, you can create a {@code JDialog} on a
 * different screen device than its owner. See {@link java.awt.Frame} for more
 * information.
 * <p>
 * <strong>Warning:</strong> Swing is not thread safe. For more information see
 * <a href="package-summary.html#threading">Swing's Threading Policy</a>.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * {@code java.beans} package. Please see {@link java.beans.XMLEncoder}.
 *
 * @see JOptionPane
 * @see JRootPane
 * @see javax.swing.RootPaneContainer
 *
 * @beaninfo attribute: isContainer true attribute: containerDelegate
 *           getContentPane description: A toplevel window for creating dialog
 *           boxes.
 *
 * @author David Kloba
 * @author James Gosling
 * @author Scott Violet
 */
public class JDialog extends Dialog implements WindowConstants, Accessible, RootPaneContainer,
		TransferHandler.HasGetTransferHandler {
	private static final Object defaultLookAndFeelDecoratedKey = new StringBuffer(
			"JDialog.defaultLookAndFeelDecorated");

	private int defaultCloseOperation = HIDE_ON_CLOSE;

	protected boolean rootPaneCheckingEnabled = false;
	private TransferHandler transferHandler;

	public JDialog() {
		this((Frame) null, false);
	}

	public JDialog(Frame owner) {
		this(owner, false);
	}

	public JDialog(Frame owner, boolean modal) {
		this(owner, "", modal);
	}

	public JDialog(Frame owner, String title) {
		this(owner, title, false);
	}

	public JDialog(Frame owner, String title, boolean modal) {
		super(owner == null ? SwingUtilities.getSharedOwnerFrame() : owner, title, modal);
		if (owner == null) {
			WindowListener ownerShutdownListener = SwingUtilities
					.getSharedOwnerFrameShutdownListener();
			addWindowListener(ownerShutdownListener);
		}
		dialogInit(title);
	}

	public JDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner == null ? SwingUtilities.getSharedOwnerFrame() : owner, title, modal, gc);
		if (owner == null) {
			WindowListener ownerShutdownListener = SwingUtilities
					.getSharedOwnerFrameShutdownListener();
			addWindowListener(ownerShutdownListener);
		}
		dialogInit(title);
	}

	public JDialog(Dialog owner) {
		this(owner, false);
	}

	public JDialog(Dialog owner, boolean modal) {
		this(owner, "", modal);
	}

	public JDialog(Dialog owner, String title) {
		this(owner, title, false);
	}

	public JDialog(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
		dialogInit(title);
	}

	public JDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		dialogInit(title);
	}

	public JDialog(Window owner) {
		this(owner, Dialog.ModalityType.MODELESS);
	}

	public JDialog(Window owner, ModalityType modalityType) {
		this(owner, "", modalityType);
	}

	public JDialog(Window owner, String title) {
		this(owner, title, Dialog.ModalityType.MODELESS);
	}

	public JDialog(Window owner, String title, Dialog.ModalityType modalityType) {
		super(owner, title, modalityType);
		dialogInit(title);
	}

	public JDialog(Window owner, String title, Dialog.ModalityType modalityType,
			GraphicsConfiguration gc) {
		super(owner, title, modalityType, gc);
		dialogInit(title);
	}

	protected void dialogInit(String title) {
		setLocale(JComponent.getDefaultLocale());
		setRootPane(createRootPane());
		setRootPaneCheckingEnabled(true);
		setTitle(title);
	}

	protected JRootPane createRootPane() {
		JRootPane rp = new JRootPane();
		rp.currWindow = this;
		rp.setOpaque(true);
		return rp;
	}

	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);

		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			switch (defaultCloseOperation) {
			case WindowConstants.HIDE_ON_CLOSE:
				setVisible(false);
				break;
			case WindowConstants.DISPOSE_ON_CLOSE:
				dispose();
				break;
			case WindowConstants.DO_NOTHING_ON_CLOSE:
			default:
				break;
			}
		}
	}

	public void setDefaultCloseOperation(int operation) {
		if (operation != DO_NOTHING_ON_CLOSE && operation != HIDE_ON_CLOSE
				&& operation != DISPOSE_ON_CLOSE) {
			throw new IllegalArgumentException(
					"defaultCloseOperation must be one of: DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE, or DISPOSE_ON_CLOSE");
		}

		this.defaultCloseOperation = operation;
	}

	public int getDefaultCloseOperation() {
		return defaultCloseOperation;
	}

	public void setTransferHandler(TransferHandler newHandler) {
		AndroidClassUtil.callEmptyMethod();
	}

	public TransferHandler getTransferHandler() {
		AndroidClassUtil.callEmptyMethod();
		return transferHandler;
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void setJMenuBar(JMenuBar menu) {
		getRootPane().setMenuBar(menu);
	}

	public JMenuBar getJMenuBar() {
		return getRootPane().getMenuBar();
	}

	protected boolean isRootPaneCheckingEnabled() {
		return rootPaneCheckingEnabled;
	}

	protected void setRootPaneCheckingEnabled(boolean enabled) {
		rootPaneCheckingEnabled = enabled;
	}

	protected void addImpl(Component comp, Object constraints, int index) {
		// if (isRootPaneCheckingEnabled()) {
		getContentPane().add(comp, constraints, index);
		// } else {
		// super.addImpl(comp, constraints, index);
		// }
	}

	public void remove(Component comp) {
		if (comp == rootPane) {
			super.remove(comp);
		} else {
			getContentPane().remove(comp);
		}
	}

	public void setLayout(LayoutManager manager) {
		// if (isRootPaneCheckingEnabled()) {
		getContentPane().setLayout(manager);
		// } else {
		// super.setLayout(manager);
		// }
	}

	public JRootPane getRootPane() {
		return rootPane;
	}

	protected void setRootPane(JRootPane root) {
		if (rootPane != null) {
			remove(rootPane);
		}
		rootPane = root;
		// if (rootPane != null) {
		// boolean checkingEnabled = isRootPaneCheckingEnabled();
		// try {
		// setRootPaneCheckingEnabled(false);
		// add(rootPane, BorderLayout.CENTER);
		// } finally {
		// setRootPaneCheckingEnabled(checkingEnabled);
		// }
		// }
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
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public static void setDefaultLookAndFeelDecorated(boolean defaultLookAndFeelDecorated) {
		AndroidClassUtil.callEmptyMethod();
	}

	public static boolean isDefaultLookAndFeelDecorated() {
		return true;
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
