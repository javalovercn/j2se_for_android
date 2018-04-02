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
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleStateSet;

/**
 * A collection of utility methods for Swing.
 *
 * @author unknown
 */
public class SwingUtilities implements SwingConstants {
	private static boolean canAccessEventQueue = false;
	private static boolean eventQueueTested = false;

	private static boolean suppressDropSupport;

	private static boolean checkedSuppressDropSupport;

	static void installSwingDropTargetAsNecessary(Component c, TransferHandler t) {
	}

	public static final boolean isRectangleContainingRectangle(Rectangle a, Rectangle b) {
		return b.x >= a.x && (b.x + b.width) <= (a.x + a.width) && b.y >= a.y
				&& (b.y + b.height) <= (a.y + a.height);
	}

	public static Rectangle getLocalBounds(Component aComponent) {
		Rectangle b = new Rectangle(aComponent.getBounds());
		b.x = b.y = 0;
		return b;
	}

	public static Window getWindowAncestor(Component c) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	static Point convertScreenLocationToParent(Container parent, int x, int y) {
		return null;
	}

	public static Point convertPoint(Component source, Point aPoint, Component destination) {
		return null;
	}

	public static Point convertPoint(Component source, int x, int y, Component destination) {
		Point point = new Point(x, y);
		return convertPoint(source, point, destination);
	}

	public static Rectangle convertRectangle(Component source, Rectangle aRectangle,
			Component destination) {
		Point point = new Point(aRectangle.x, aRectangle.y);
		point = convertPoint(source, point, destination);
		return new Rectangle(point.x, point.y, aRectangle.width, aRectangle.height);
	}

	public static Container getAncestorOfClass(Class<?> c, Component comp) {
		return null;
	}

	public static Container getAncestorNamed(String name, Component comp) {
		return null;
	}

	public static Component getDeepestComponentAt(Component parent, int x, int y) {
		return null;
	}

	public static MouseEvent convertMouseEvent(Component source, MouseEvent sourceEvent,
			Component destination) {
		return null;
	}

	public static void convertPointToScreen(Point p, Component c) {
	}

	public static void convertPointFromScreen(Point p, Component c) {
	}

	public static Window windowForComponent(Component c) {
		return getWindowAncestor(c);
	}

	public static boolean isDescendingFrom(Component a, Component b) {
		return false;
	}

	public static Rectangle computeIntersection(int x, int y, int width, int height,
			Rectangle dest) {
		return null;
	}

	public static Rectangle computeUnion(int x, int y, int width, int height, Rectangle dest) {
		return null;
	}

	public static Rectangle[] computeDifference(Rectangle rectA, Rectangle rectB) {
		return null;
	}

	public static boolean isLeftMouseButton(MouseEvent anEvent) {
		return ((anEvent.getModifiers() & InputEvent.BUTTON1_MASK) != 0);
	}

	public static boolean isMiddleMouseButton(MouseEvent anEvent) {
		return ((anEvent.getModifiers() & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK);
	}

	public static boolean isRightMouseButton(MouseEvent anEvent) {
		return ((anEvent.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK);
	}

	public static int computeStringWidth(FontMetrics fm, String str) {
		return 0;
	}

	public static String layoutCompoundLabel(JComponent c, FontMetrics fm, String text, Icon icon,
			int verticalAlignment, int horizontalAlignment, int verticalTextPosition,
			int horizontalTextPosition, Rectangle viewR, Rectangle iconR, Rectangle textR,
			int textIconGap) {
		return null;
	}

	public static String layoutCompoundLabel(FontMetrics fm, String text, Icon icon,
			int verticalAlignment, int horizontalAlignment, int verticalTextPosition,
			int horizontalTextPosition, Rectangle viewR, Rectangle iconR, Rectangle textR,
			int textIconGap) {
		return layoutCompoundLabelImpl(null, fm, text, icon, verticalAlignment, horizontalAlignment,
				verticalTextPosition, horizontalTextPosition, viewR, iconR, textR, textIconGap);
	}

	private static String layoutCompoundLabelImpl(JComponent c, FontMetrics fm, String text,
			Icon icon, int verticalAlignment, int horizontalAlignment, int verticalTextPosition,
			int horizontalTextPosition, Rectangle viewR, Rectangle iconR, Rectangle textR,
			int textIconGap) {
		return "";
	}

	public static void paintComponent(Graphics g, Component c, Container p, int x, int y, int w,
			int h) {
	}

	public static void paintComponent(Graphics g, Component c, Container p, Rectangle r) {
	}

	public static void updateComponentTreeUI(Component c) {
	}

	private static void updateComponentTreeUI0(Component c) {
	}

	public static void invokeLater(Runnable doRun) {
		EventQueue.invokeLater(doRun);
	}

	public static void invokeAndWait(final Runnable doRun)
			throws InterruptedException, InvocationTargetException {
		EventQueue.invokeAndWait(doRun);
	}

	public static boolean isEventDispatchThread() {
		return EventQueue.isDispatchThread();
	}

	public static int getAccessibleIndexInParent(Component c) {
		return 0;
	}

	public static Accessible getAccessibleAt(Component c, Point p) {
		return null;
	}

	public static AccessibleStateSet getAccessibleStateSet(Component c) {
		return null;
	}

	public static int getAccessibleChildrenCount(Component c) {
		return 0;
	}

	public static Accessible getAccessibleChild(Component c, int i) {
		return null;
	}

	public static Component findFocusOwner(Component c) {
		return null;
	}

	public static JRootPane getRootPane(Component c) {
		return null;
	}

	public static Component getRoot(Component c) {
		Component applet = null;
		for (Component p = c; p != null; p = p.getParent()) {
			if (p instanceof Window) {
				return p;
			}
		}
		return applet;
	}

	static JComponent getPaintingOrigin(JComponent c) {
		return null;
	}

	public static boolean processKeyBindings(KeyEvent event) {
		return false;
	}

	static boolean isValidKeyEventForKeyBindings(KeyEvent e) {
		return true;
	}

	public static boolean notifyAction(Action action, KeyStroke ks, KeyEvent event, Object sender,
			int modifiers) {
		return true;
	}

	public static void replaceUIInputMap(JComponent component, int type, InputMap uiInputMap) {
	}

	public static void replaceUIActionMap(JComponent component, ActionMap uiActionMap) {
	}

	public static InputMap getUIInputMap(JComponent component, int condition) {
		return null;
	}

	public static ActionMap getUIActionMap(JComponent component) {
		return null;
	}

	private static final Object sharedOwnerFrameKey = new StringBuffer(
			"SwingUtilities.sharedOwnerFrame");

	static class SharedOwnerFrame extends Frame implements WindowListener {
		public void addNotify() {
		}

		void installListeners() {
		}

		public void windowClosed(WindowEvent e) {
			dispose();
		}

		public void windowOpened(WindowEvent e) {
		}

		public void windowClosing(WindowEvent e) {
		}

		public void windowIconified(WindowEvent e) {
		}

		public void windowDeiconified(WindowEvent e) {
		}

		public void windowActivated(WindowEvent e) {
		}

		public void windowDeactivated(WindowEvent e) {
		}

		public void show() {
		}

		public void dispose() {
			super.dispose();
		}
	}

	static Frame getSharedOwnerFrame() throws HeadlessException {
		return null;
	}

	static WindowListener getSharedOwnerFrameShutdownListener() throws HeadlessException {
		return null;
	}

	static Object appContextGet(Object key) {
		return null;
	}

	static void appContextPut(Object key, Object value) {
	}

	static void appContextRemove(Object key) {
	}

	static Class<?> loadSystemClass(String className) throws ClassNotFoundException {
		return Class.forName(className, true, Thread.currentThread().getContextClassLoader());
	}

	static boolean isLeftToRight(Component c) {
		return c.getComponentOrientation().isLeftToRight();
	}

	private SwingUtilities() {
	}

	static boolean doesIconReferenceImage(Icon icon, Image image) {
		return false;
	}

	static int findDisplayedMnemonicIndex(String text, int mnemonic) {
		return -1;
	}

	public static Rectangle calculateInnerArea(JComponent c, Rectangle r) {
		return null;
	}

	static void updateRendererOrEditorUI(Object rendererOrEditor) {
	}

	public static Container getUnwrappedParent(Component component) {
		return null;
	}

	public static Component getUnwrappedView(JViewport viewport) {
		return null;
	}

	static Container getValidateRoot(Container c, boolean visibleOnly) {
		return null;
	}
}
