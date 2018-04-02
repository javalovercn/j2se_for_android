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
import hc.android.DebugLogger;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.WindowEvent;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;

/**
 * An extended version of <code>java.awt.Frame</code> that adds support for the
 * JFC/Swing component architecture. You can find task-oriented documentation
 * about using <code>JFrame</code> in <em>The Java Tutorial</em>, in the section
 * <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/frame.html">How
 * to Make Frames</a>.
 *
 * <p>
 * The <code>JFrame</code> class is slightly incompatible with
 * <code>Frame</code>. Like all other JFC/Swing top-level containers, a
 * <code>JFrame</code> contains a <code>JRootPane</code> as its only child. The
 * <b>content pane</b> provided by the root pane should, as a rule, contain all
 * the non-menu components displayed by the <code>JFrame</code>. This is
 * different from the AWT <code>Frame</code> case. As a conveniance
 * <code>add</code> and its variants, <code>remove</code> and
 * <code>setLayout</code> have been overridden to forward to the
 * <code>contentPane</code> as necessary. This means you can write:
 * 
 * <pre>
 * frame.add(child);
 * </pre>
 * 
 * And the child will be added to the contentPane. The content pane will always
 * be non-null. Attempting to set it to null will cause the JFrame to throw an
 * exception. The default content pane will have a BorderLayout manager set on
 * it. Refer to {@link javax.swing.RootPaneContainer} for details on adding,
 * removing and setting the <code>LayoutManager</code> of a <code>JFrame</code>.
 * <p>
 * Unlike a <code>Frame</code>, a <code>JFrame</code> has some notion of how to
 * respond when the user attempts to close the window. The default behavior is
 * to simply hide the JFrame when the user closes the window. To change the
 * default behavior, you invoke the method {@link #setDefaultCloseOperation}. To
 * make the <code>JFrame</code> behave the same as a <code>Frame</code>
 * instance, use
 * <code>setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)</code>.
 * <p>
 * For more information on content panes and other features that root panes
 * provide, see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/toplevel.html">Using
 * Top-Level Containers</a> in <em>The Java Tutorial</em>.
 * <p>
 * In a multi-screen environment, you can create a <code>JFrame</code> on a
 * different screen device. See {@link java.awt.Frame} for more information.
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
 * @see #setDefaultCloseOperation
 * @see java.awt.event.WindowListener#windowClosing
 * @see javax.swing.RootPaneContainer
 *
 * @beaninfo attribute: isContainer true attribute: containerDelegate
 *           getContentPane description: A toplevel window which can be
 *           minimized to an icon.
 *
 * @author Jeff Dinkins
 * @author Georges Saab
 * @author David Kloba
 */
public class JFrame extends Frame implements WindowConstants, Accessible, RootPaneContainer,
		TransferHandler.HasGetTransferHandler {
	public static final int EXIT_ON_CLOSE = 3;

	private int defaultCloseOperation = HIDE_ON_CLOSE;
	boolean rootPaneCheckingEnabled = false;

	public JFrame() throws HeadlessException {
		super();
		init("");
	}

	public JFrame(GraphicsConfiguration gc) {
		super(gc);
		init("");
	}

	public JFrame(String title) throws HeadlessException {
		super(title);
		init(title);
	}

	public JFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		init(title);
	}

	protected void init(String title) {
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
			case WindowConstants.EXIT_ON_CLOSE:
				System.exit(0);
				break;
			}
		}
	}

	public void setDefaultCloseOperation(int operation) {
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
		return null;
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void setJMenuBar(JMenuBar menubar) {
		getRootPane().setMenuBar(menubar);
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
		// DebugLogger.log("Warning : please don't add component to RootPane.
		// ");
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
		// DebugLogger.log("add JRootPane : " + root.toString() + " to JFrame
		// Container : " + this.toString());
		// } finally {
		// setRootPaneCheckingEnabled(checkingEnabled);
		// }
		// }
	}

	public void setIconImage(Image image) {
		super.setIconImage(image);
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
		AndroidClassUtil.callEmptyMethod();
		return true;
	}

	protected String paramString() {
		return "";
	}

	public AccessibleContext getAccessibleContext() {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

}
