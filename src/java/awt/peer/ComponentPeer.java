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
package java.awt.peer;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.PaintEvent;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;

/**
 * The peer interface for {@link Component}. This is the top level peer
 * interface for widgets and defines the bulk of methods for AWT component
 * peers. Most component peers have to implement this interface (via one
 * of the subinterfaces), except menu components, which implement
 * {@link MenuComponentPeer}.
 *
 * The peer interfaces are intended only for use in porting
 * the AWT. They are not intended for use by application
 * developers, and developers should not implement peers
 * nor invoke any of the peer methods directly on the peer
 * instances.
 */

public interface ComponentPeer {

    public static final int SET_LOCATION = 1;
    public static final int SET_SIZE = 2;
    public static final int SET_BOUNDS = 3;
    public static final int SET_CLIENT_SIZE = 4;
    public static final int RESET_OPERATION = 5;
    public static final int NO_EMBEDDED_CHECK = (1 << 14);
    public static final int DEFAULT_OPERATION = SET_BOUNDS;

    boolean isObscured();

    boolean canDetermineObscurity();

    void setVisible(boolean v);

    void setEnabled(boolean e);

    void paint(Graphics g);

    void print(Graphics g);

    void setBounds(int x, int y, int width, int height, int op);

    void handleEvent(AWTEvent e);

    void coalescePaintEvent(PaintEvent e);

    Point getLocationOnScreen();

    Dimension getPreferredSize();

    Dimension getMinimumSize();

    ColorModel getColorModel();

    Toolkit getToolkit();

    Graphics getGraphics();

    FontMetrics getFontMetrics(Font font);

    void dispose();

    void setForeground(Color c);

    void setBackground(Color c);

    void setFont(Font f);

    void updateCursorImmediately();

    boolean requestFocus(Component lightweightChild, boolean temporary,
                         boolean focusedWindowChangeAllowed, long time,
                         Object cause);//MODI CausedFocusEvent.Cause

    boolean isFocusable();

    Image createImage(ImageProducer producer);

    Image createImage(int width, int height);

    VolatileImage createVolatileImage(int width, int height);

    boolean prepareImage(Image img, int w, int h, ImageObserver o);

    int checkImage(Image img, int w, int h, ImageObserver o);

    GraphicsConfiguration getGraphicsConfiguration();

    boolean handlesWheelScrolling();

    void createBuffers(int numBuffers, BufferCapabilities caps)
         throws AWTException;

    Image getBackBuffer();

    void flip(int x1, int y1, int x2, int y2, BufferCapabilities.FlipContents flipAction);

    void destroyBuffers();

    void reparent(ContainerPeer newContainer);

    boolean isReparentSupported();

    void layout();

    void applyShape(javax.swing.plaf.synth.Region shape);

    void setZOrder(ComponentPeer above);

    boolean updateGraphicsData(GraphicsConfiguration gc);
}