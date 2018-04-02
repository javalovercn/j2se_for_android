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

/**
 * DesktopManager objects are owned by a JDesktopPane object. They are
 * responsible for implementing L&F specific behaviors for the JDesktopPane.
 * JInternalFrame implementations should delegate specific behaviors to the
 * DesktopManager. For instance, if a JInternalFrame was asked to iconify, it
 * should try:
 * 
 * <PRE>
 * getDesktopPane().getDesktopManager().iconifyFrame(frame);
 * </PRE>
 * 
 * This delegation allows each L&F to provide custom behaviors for
 * desktop-specific actions. (For example, how and where the internal frame's
 * icon would appear.)
 * <p>
 * This class provides a policy for the various JInternalFrame methods, it is
 * not meant to be called directly rather the various JInternalFrame methods
 * will call into the DesktopManager.
 * </p>
 *
 * @see JDesktopPane
 * @see JInternalFrame
 * @see JInternalFrame.JDesktopIcon
 *
 * @author David Kloba
 */
public interface DesktopManager {
	void openFrame(JInternalFrame f);

	void closeFrame(JInternalFrame f);

	void maximizeFrame(JInternalFrame f);

	void minimizeFrame(JInternalFrame f);

	void iconifyFrame(JInternalFrame f);

	void deiconifyFrame(JInternalFrame f);

	void activateFrame(JInternalFrame f);

	void deactivateFrame(JInternalFrame f);

	void beginDraggingFrame(JComponent f);

	void dragFrame(JComponent f, int newX, int newY);

	void endDraggingFrame(JComponent f);

	void beginResizingFrame(JComponent f, int direction);

	void resizeFrame(JComponent f, int newX, int newY, int newWidth, int newHeight);

	void endResizingFrame(JComponent f);

	void setBoundsForFrame(JComponent f, int newX, int newY, int newWidth, int newHeight);
}
