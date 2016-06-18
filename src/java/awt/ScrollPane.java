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

import java.awt.event.MouseWheelEvent;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;

/**
 * A container class which implements automatic horizontal and/or
 * vertical scrolling for a single child component.  The display
 * policy for the scrollbars can be set to:
 * <OL>
 * <LI>as needed: scrollbars created and shown only when needed by scrollpane
 * <LI>always: scrollbars created and always shown by the scrollpane
 * <LI>never: scrollbars never created or shown by the scrollpane
 * </OL>
 * <P>
 * The state of the horizontal and vertical scrollbars is represented
 * by two <code>ScrollPaneAdjustable</code> objects (one for each
 * dimension) which implement the <code>Adjustable</code> interface.
 * The API provides methods to access those objects such that the
 * attributes on the Adjustable object (such as unitIncrement, value,
 * etc.) can be manipulated.
 * <P>
 * Certain adjustable properties (minimum, maximum, blockIncrement,
 * and visibleAmount) are set internally by the scrollpane in accordance
 * with the geometry of the scrollpane and its child and these should
 * not be set by programs using the scrollpane.
 * <P>
 * If the scrollbar display policy is defined as "never", then the
 * scrollpane can still be programmatically scrolled using the
 * setScrollPosition() method and the scrollpane will move and clip
 * the child's contents appropriately.  This policy is useful if the
 * program needs to create and manage its own adjustable controls.
 * <P>
 * The placement of the scrollbars is controlled by platform-specific
 * properties set by the user outside of the program.
 * <P>
 * The initial size of this container is set to 100x100, but can
 * be reset using setSize().
 * <P>
 * Scrolling with the wheel on a wheel-equipped mouse is enabled by default.
 * This can be disabled using <code>setWheelScrollingEnabled</code>.
 * Wheel scrolling can be customized by setting the block and
 * unit increment of the horizontal and vertical Adjustables.
 * For information on how mouse wheel events are dispatched, see
 * the class description for {@link MouseWheelEvent}.
 * <P>
 * Insets are used to define any space used by scrollbars and any
 * borders created by the scroll pane. getInsets() can be used
 * to get the current value for the insets.  If the value of
 * scrollbarsAlwaysVisible is false, then the value of the insets
 * will change dynamically depending on whether the scrollbars are
 * currently visible or not.
 *
 * @author      Tom Ball
 * @author      Amy Fowler
 * @author      Tim Prinzing
 */
public class ScrollPane extends Container implements Accessible {
    private static native void initIDs();

    public static final int SCROLLBARS_AS_NEEDED = 0;
    public static final int SCROLLBARS_ALWAYS = 1;
    public static final int SCROLLBARS_NEVER = 2;

    private int scrollbarDisplayPolicy;

    private static final String base = "scrollpane";
    private static int nameCounter = 0;
    private static final boolean defaultWheelScroll = true;
    private boolean wheelScrollingEnabled = defaultWheelScroll;

    public ScrollPane() throws HeadlessException {
        this(SCROLLBARS_AS_NEEDED);
    }

    public ScrollPane(int scrollbarDisplayPolicy) throws HeadlessException {
    	AndroidClassUtil.notifyReplaceWithSwingComponent();
    }

    String constructComponentName() {
        synchronized (ScrollPane.class) {
            return base + nameCounter++;
        }
    }

    private void addToPanel(Component comp, Object constraints, int index) {
    }

    protected final void addImpl(Component comp, Object constraints, int index) {
    	AndroidClassUtil.notifyReplaceWithSwingComponent();
    }

    public int getScrollbarDisplayPolicy() {
        return scrollbarDisplayPolicy;
    }

    public Dimension getViewportSize() {
        Insets i = getInsets();
        return new Dimension(0, 0);
    }

    public int getHScrollbarHeight() {
    	AndroidClassUtil.notifyReplaceWithSwingComponent();
        int h = 0;
        return h;
    }

    public int getVScrollbarWidth() {
        int w = 0;
        return w;
    }

    public Adjustable getVAdjustable() {
    	AndroidClassUtil.notifyReplaceWithSwingComponent();
        return null;
    }

    public Adjustable getHAdjustable() {
    	AndroidClassUtil.notifyReplaceWithSwingComponent();
        return null;
    }

    public void setScrollPosition(int x, int y) {
    	AndroidClassUtil.notifyReplaceWithSwingComponent();
    }

    public void setScrollPosition(Point p) {
        setScrollPosition(p.x, p.y);
    }

    @Transient
    public Point getScrollPosition() {
    	AndroidClassUtil.notifyReplaceWithSwingComponent();
    	return new Point(0, 0);
    }

    public final void setLayout(LayoutManager mgr) {
        throw new AWTError("ScrollPane controls layout");
    }

    public void doLayout() {
        layout();
    }

    @Deprecated
    public void layout() {
    	AndroidClassUtil.notifyReplaceWithSwingComponent();
        return;
    }

    public void printComponents(Graphics g) {
    	AndroidClassUtil.notifyReplaceWithSwingComponent();
    }

    public void addNotify() {
    	AndroidClassUtil.notifyReplaceWithSwingComponent();
    }

    public String paramString() {
        return "";
    }

    void autoProcessMouseWheel(MouseWheelEvent e) {
        processMouseWheelEvent(e);
    }

    protected void processMouseWheelEvent(MouseWheelEvent e) {
    	AndroidClassUtil.notifyReplaceWithSwingComponent();
    }

    protected boolean eventTypeEnabled(int type) {
    	AndroidClassUtil.notifyReplaceWithSwingComponent();
        return true;
    }

    public void setWheelScrollingEnabled(boolean handleWheel) {
        wheelScrollingEnabled = handleWheel;
    }

    public boolean isWheelScrollingEnabled() {
        return wheelScrollingEnabled;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
    }

    private void readObject(ObjectInputStream s)
        throws ClassNotFoundException, IOException, HeadlessException
    {
    }

    public AccessibleContext getAccessibleContext() {
        return accessibleContext;
    }
}