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
package java.awt.event;

import java.util.EventListener;

/**
 * The listener interface for receiving window events. The class that is
 * interested in processing a window event either implements this interface (and
 * all the methods it contains) or extends the abstract
 * <code>WindowAdapter</code> class (overriding only the methods of interest).
 * The listener object created from that class is then registered with a Window
 * using the window's <code>addWindowListener</code> method. When the window's
 * status changes by virtue of being opened, closed, activated or deactivated,
 * iconified or deiconified, the relevant method in the listener object is
 * invoked, and the <code>WindowEvent</code> is passed to it.
 *
 * @author Carl Quinn
 *
 * @see WindowAdapter
 * @see WindowEvent
 * @see <a href=
 *      "http://java.sun.com/docs/books/tutorial/uiswing/events/windowlistener.html">Tutorial:
 *      How to Write Window Listeners</a>
 *
 * @since 1.1
 */
public interface WindowListener extends EventListener {
	public void windowOpened(WindowEvent e);

	public void windowClosing(WindowEvent e);

	public void windowClosed(WindowEvent e);

	public void windowIconified(WindowEvent e);

	public void windowDeiconified(WindowEvent e);

	public void windowActivated(WindowEvent e);

	public void windowDeactivated(WindowEvent e);
}
