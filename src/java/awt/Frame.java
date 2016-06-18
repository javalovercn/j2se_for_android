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

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.swing.JFrame;
import javax.swing.JRootPane;

/**
 * A <code>Frame</code> is a top-level window with a title and a border.
 * <p>
 * The size of the frame includes any area designated for the
 * border.  The dimensions of the border area may be obtained
 * using the <code>getInsets</code> method, however, since
 * these dimensions are platform-dependent, a valid insets
 * value cannot be obtained until the frame is made displayable
 * by either calling <code>pack</code> or <code>show</code>.
 * Since the border area is included in the overall size of the
 * frame, the border effectively obscures a portion of the frame,
 * constraining the area available for rendering and/or displaying
 * subcomponents to the rectangle which has an upper-left corner
 * location of <code>(insets.left, insets.top)</code>, and has a size of
 * <code>width - (insets.left + insets.right)</code> by
 * <code>height - (insets.top + insets.bottom)</code>.
 * <p>
 * The default layout for a frame is <code>BorderLayout</code>.
 * <p>
 * A frame may have its native decorations (i.e. <code>Frame</code>
 * and <code>Titlebar</code>) turned off
 * with <code>setUndecorated</code>. This can only be done while the frame
 * is not {@link Component#isDisplayable() displayable}.
 * <p>
 * In a multi-screen environment, you can create a <code>Frame</code>
 * on a different screen device by constructing the <code>Frame</code>
 * with {@link #Frame(GraphicsConfiguration)} or
 * {@link #Frame(String title, GraphicsConfiguration)}.  The
 * <code>GraphicsConfiguration</code> object is one of the
 * <code>GraphicsConfiguration</code> objects of the target screen
 * device.
 * <p>
 * In a virtual device multi-screen environment in which the desktop
 * area could span multiple physical screen devices, the bounds of all
 * configurations are relative to the virtual-coordinate system.  The
 * origin of the virtual-coordinate system is at the upper left-hand
 * corner of the primary physical screen.  Depending on the location
 * of the primary screen in the virtual device, negative coordinates
 * are possible, as shown in the following figure.
 * <p>
 * <img src="doc-files/MultiScreen.gif"
 * alt="Diagram of virtual device encompassing three physical screens and one primary physical screen. The primary physical screen
 * shows (0,0) coords while a different physical screen shows (-80,-100) coords."
 * ALIGN=center HSPACE=10 VSPACE=7>
 * <p>
 * In such an environment, when calling <code>setLocation</code>,
 * you must pass a virtual coordinate to this method.  Similarly,
 * calling <code>getLocationOnScreen</code> on a <code>Frame</code>
 * returns virtual device coordinates.  Call the <code>getBounds</code>
 * method of a <code>GraphicsConfiguration</code> to find its origin in
 * the virtual coordinate system.
 * <p>
 * The following code sets the
 * location of the <code>Frame</code> at (10, 10) relative
 * to the origin of the physical screen of the corresponding
 * <code>GraphicsConfiguration</code>.  If the bounds of the
 * <code>GraphicsConfiguration</code> is not taken into account, the
 * <code>Frame</code> location would be set at (10, 10) relative to the
 * virtual-coordinate system and would appear on the primary physical
 * screen, which might be different from the physical screen of the
 * specified <code>GraphicsConfiguration</code>.
 *
 * <pre>
 *      Frame f = new Frame(GraphicsConfiguration gc);
 *      Rectangle bounds = gc.getBounds();
 *      f.setLocation(10 + bounds.x, 10 + bounds.y);
 * </pre>
 *
 * <p>
 * Frames are capable of generating the following types of
 * <code>WindowEvent</code>s:
 * <ul>
 * <li><code>WINDOW_OPENED</code>
 * <li><code>WINDOW_CLOSING</code>:
 *     <br>If the program doesn't
 *     explicitly hide or dispose the window while processing
 *     this event, the window close operation is canceled.
 * <li><code>WINDOW_CLOSED</code>
 * <li><code>WINDOW_ICONIFIED</code>
 * <li><code>WINDOW_DEICONIFIED</code>
 * <li><code>WINDOW_ACTIVATED</code>
 * <li><code>WINDOW_DEACTIVATED</code>
 * <li><code>WINDOW_GAINED_FOCUS</code>
 * <li><code>WINDOW_LOST_FOCUS</code>
 * <li><code>WINDOW_STATE_CHANGED</code>
 * </ul>
 *
 * @author      Sami Shaio
 * @see WindowEvent
 * @see Window#addWindowListener
 * @since       JDK1.0
 */
public class Frame extends Window implements MenuContainer {

	public static final int DEFAULT_CURSOR = Cursor.DEFAULT_CURSOR;
	public static final int CROSSHAIR_CURSOR = Cursor.CROSSHAIR_CURSOR;
	public static final int TEXT_CURSOR = Cursor.TEXT_CURSOR;
	public static final int WAIT_CURSOR = Cursor.WAIT_CURSOR;
	public static final int SW_RESIZE_CURSOR = Cursor.SW_RESIZE_CURSOR;
	public static final int SE_RESIZE_CURSOR = Cursor.SE_RESIZE_CURSOR;
	public static final int NW_RESIZE_CURSOR = Cursor.NW_RESIZE_CURSOR;
	public static final int NE_RESIZE_CURSOR = Cursor.NE_RESIZE_CURSOR;
	public static final int N_RESIZE_CURSOR = Cursor.N_RESIZE_CURSOR;
	public static final int S_RESIZE_CURSOR = Cursor.S_RESIZE_CURSOR;
	public static final int W_RESIZE_CURSOR = Cursor.W_RESIZE_CURSOR;
	public static final int E_RESIZE_CURSOR = Cursor.E_RESIZE_CURSOR;
	public static final int HAND_CURSOR = Cursor.HAND_CURSOR;
	public static final int MOVE_CURSOR = Cursor.MOVE_CURSOR;

	public static final int NORMAL = 0;
	public static final int ICONIFIED = 1;
	public static final int MAXIMIZED_HORIZ = 2;
	public static final int MAXIMIZED_VERT = 4;
	public static final int MAXIMIZED_BOTH = MAXIMIZED_VERT | MAXIMIZED_HORIZ;

	Rectangle maximizedBounds;

	boolean mbManagement = false;
	private int state = NORMAL;
	Vector ownedWindows;

	public Frame() throws HeadlessException {
		this("");
	}

	public Frame(GraphicsConfiguration gc) {
		this("", gc);
	}

	public Frame(String title) throws HeadlessException {
		init(title, null);
	}

	public Frame(String title, GraphicsConfiguration gc) {
		super(gc);
		init(title, gc);
	}

	private void init(String title, GraphicsConfiguration gc) {
	}

	private static int nameCounter = 1;
	
	String constructComponentName() {
		synchronized (Frame.class) {
            return "Frame" + nameCounter++;
        }
	}

	public void addNotify() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		if(this instanceof JFrame){
			((JFrame)this).getRootPane().setTitleAdAPI(title);
		}
	}

	public Image getIconImage() {
		java.util.List<Image> icons = this.icons;
        if (icons != null) {
            if (icons.size() > 0) {
                return icons.get(0);
            }
        }
        return null;
	}

	public void setIconImage(Image image) {
		super.setIconImage(image);
	}

	public MenuBar getMenuBar() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setMenuBar(MenuBar mb) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean isResizable() {
		return false;
	}

	public void setResizable(boolean resizable) {
		AndroidClassUtil.callEmptyMethod();
	}

	/**
	 * 
	 * @param state Frame.NORMAL or Frame.ICONIFIED
	 */
	public synchronized void setState(int state) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setExtendedState(int state) {
		AndroidClassUtil.callEmptyMethod();
	}

	private boolean isFrameStateSupported(int state) {
		return false;
	}

	public synchronized int getState() {
		return NORMAL;
	}

	public int getExtendedState() {
		return state;
	}

	public void setMaximizedBounds(Rectangle bounds) {
		AndroidClassUtil.callEmptyMethod();
		this.maximizedBounds = bounds;
	}

	public Rectangle getMaximizedBounds() {
		AndroidClassUtil.callEmptyMethod();
		return maximizedBounds;// TODO
	}
	
	boolean undecorated = false;
	
	public void setUndecorated(boolean undecorated) {
		this.undecorated = undecorated;
		if(this instanceof JFrame){
			if(undecorated == false){
				((JFrame)this).getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
			}else{
				((JFrame)this).getRootPane().setWindowDecorationStyle(JRootPane.NONE);
			}
		}
	}

	public boolean isUndecorated() {
		return undecorated;
	}

	public void setOpacity(float opacity) {
		super.setOpacity(opacity);
	}

	public void setShape(Shape shape) {
		super.setShape(shape);
	}

	public void setBackground(Color bgColor) {
		super.setBackground(bgColor);
	}

	public void remove(MenuComponent m) {
		AndroidClassUtil.callEmptyMethod();
		if (m == null) {
			return;
		}
	}

	public void removeNotify() {
	}

	void postProcessKeyEvent(KeyEvent e) {
		//AWT, MenuBar，而非JMenuBar
//		if (menuBar != null && menuBar.handleShortcut(e)) {
//            e.consume();
//            return;
//        }
        super.postProcessKeyEvent(e);
	}

	protected String paramString() {
		return "FramePara";
	}

	public void setCursor(int cursorType) {
		AndroidClassUtil.callEmptyMethod();
	}

	public int getCursorType() {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public static Frame[] getFrames() {
		Window[] allWindows = Window.getWindows();

		Vector<Frame> out = new Vector<Frame>();
		
        for (Window w : allWindows) {
            if (w instanceof Frame) {
                out.add((Frame)w);
            }
        }

        Frame[] frames = new Frame[out.size()];
        for (int i = 0; i < frames.length; i++) {
			frames[i] = out.elementAt(i);
		}

        return frames;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
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

}
