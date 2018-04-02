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
package javax.swing.event;

import java.awt.AWTEvent;

import javax.swing.JInternalFrame;

/**
 * An <code>AWTEvent</code> that adds support for <code>JInternalFrame</code>
 * objects as the event source. This class has the same event types as
 * <code>WindowEvent</code>, although different IDs are used. Help on handling
 * internal frame events is in <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/events/internalframelistener.html"
 * target="_top">How to Write an Internal Frame Listener</a>, a section in
 * <em>The Java Tutorial</em>.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @see java.awt.event.WindowEvent
 * @see java.awt.event.WindowListener
 * @see JInternalFrame
 * @see InternalFrameListener
 *
 * @author Thomas Ball
 */
public class InternalFrameEvent extends AWTEvent {
	public static final int INTERNAL_FRAME_FIRST = 25549;
	public static final int INTERNAL_FRAME_LAST = 25555;
	public static final int INTERNAL_FRAME_OPENED = INTERNAL_FRAME_FIRST;

	public static final int INTERNAL_FRAME_CLOSING = 1 + INTERNAL_FRAME_FIRST;
	public static final int INTERNAL_FRAME_CLOSED = 2 + INTERNAL_FRAME_FIRST;
	public static final int INTERNAL_FRAME_ICONIFIED = 3 + INTERNAL_FRAME_FIRST;
	public static final int INTERNAL_FRAME_DEICONIFIED = 4 + INTERNAL_FRAME_FIRST;
	public static final int INTERNAL_FRAME_ACTIVATED = 5 + INTERNAL_FRAME_FIRST;
	public static final int INTERNAL_FRAME_DEACTIVATED = 6 + INTERNAL_FRAME_FIRST;

	public InternalFrameEvent(JInternalFrame source, int id) {
		super(source, id);
	}

	public String paramString() {
		return "";
	}

	public JInternalFrame getInternalFrame() {
		return (source instanceof JInternalFrame) ? (JInternalFrame) source : null;
	}

}
