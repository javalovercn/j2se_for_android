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
package java.awt.image;

import java.awt.BufferCapabilities;
import java.awt.Graphics;

/**
 * The <code>BufferStrategy</code> class represents the mechanism with which
 * to organize complex memory on a particular <code>Canvas</code> or
 * <code>Window</code>.  Hardware and software limitations determine whether and
 * how a particular buffer strategy can be implemented.  These limitations
 * are detectible through the capabilities of the
 * <code>GraphicsConfiguration</code> used when creating the
 * <code>Canvas</code> or <code>Window</code>.
 * <p>
 * It is worth noting that the terms <i>buffer</i> and <i>surface</i> are meant
 * to be synonymous: an area of contiguous memory, either in video device
 * memory or in system memory.
 * <p>
 * There are several types of complex buffer strategies, including
 * sequential ring buffering and blit buffering.
 * Sequential ring buffering (i.e., double or triple
 * buffering) is the most common; an application draws to a single <i>back
 * buffer</i> and then moves the contents to the front (display) in a single
 * step, either by copying the data or moving the video pointer.
 * Moving the video pointer exchanges the buffers so that the first buffer
 * drawn becomes the <i>front buffer</i>, or what is currently displayed on the
 * device; this is called <i>page flipping</i>.
 * <p>
 * Alternatively, the contents of the back buffer can be copied, or
 * <i>blitted</i> forward in a chain instead of moving the video pointer.
 * <p>
 * <pre>
 * Double buffering:
 *
 *                    ***********         ***********
 *                    *         * ------> *         *
 * [To display] <---- * Front B *   Show  * Back B. * <---- Rendering
 *                    *         * <------ *         *
 *                    ***********         ***********
 *
 * Triple buffering:
 *
 * [To      ***********         ***********        ***********
 * display] *         * --------+---------+------> *         *
 *    <---- * Front B *   Show  * Mid. B. *        * Back B. * <---- Rendering
 *          *         * <------ *         * <----- *         *
 *          ***********         ***********        ***********
 *
 * </pre>
 * <p>
 * Here is an example of how buffer strategies can be created and used:
 * <pre><code>
 *
 * // Check the capabilities of the GraphicsConfiguration
 * ...
 *
 * // Create our component
 * Window w = new Window(gc);
 *
 * // Show our window
 * w.setVisible(true);
 *
 * // Create a general double-buffering strategy
 * w.createBufferStrategy(2);
 * BufferStrategy strategy = w.getBufferStrategy();
 *
 * // Main loop
 * while (!done) {
 *     // Prepare for rendering the next frame
 *     // ...
 *
 *     // Render single frame
 *     do {
 *         // The following loop ensures that the contents of the drawing buffer
 *         // are consistent in case the underlying surface was recreated
 *         do {
 *             // Get a new graphics context every time through the loop
 *             // to make sure the strategy is validated
 *             Graphics graphics = strategy.getDrawGraphics();
 *
 *             // Render to graphics
 *             // ...
 *
 *             // Dispose the graphics
 *             graphics.dispose();
 *
 *             // Repeat the rendering if the drawing buffer contents
 *             // were restored
 *         } while (strategy.contentsRestored());
 *
 *         // Display the buffer
 *         strategy.show();
 *
 *         // Repeat the rendering if the drawing buffer was lost
 *     } while (strategy.contentsLost());
 * }
 *
 * // Dispose the window
 * w.setVisible(false);
 * w.dispose();
 * </code></pre>
 *
 * @see java.awt.Window
 * @see java.awt.Canvas
 * @see java.awt.GraphicsConfiguration
 * @see VolatileImage
 * @author Michael Martak
 * @since 1.4
 */
public abstract class BufferStrategy {
    public abstract BufferCapabilities getCapabilities();

    public abstract Graphics getDrawGraphics();

    public abstract boolean contentsLost();

    public abstract boolean contentsRestored();

    public abstract void show();

    public void dispose() {
    }
}