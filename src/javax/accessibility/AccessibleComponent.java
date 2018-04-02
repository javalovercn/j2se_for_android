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
package javax.accessibility;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusListener;

/**
 * The AccessibleComponent interface should be supported by any object that is
 * rendered on the screen. This interface provides the standard mechanism for an
 * assistive technology to determine and set the graphical representation of an
 * object. Applications can determine if an object supports the
 * AccessibleComponent interface by first obtaining its AccessibleContext and
 * then calling the {@link AccessibleContext#getAccessibleComponent} method. If
 * the return value is not null, the object supports this interface.
 *
 * @see Accessible
 * @see Accessible#getAccessibleContext
 * @see AccessibleContext
 * @see AccessibleContext#getAccessibleComponent
 *
 * @author Peter Korn
 * @author Hans Muller
 * @author Willie Walker
 */
public interface AccessibleComponent {
	public Color getBackground();

	public void setBackground(Color c);

	public Color getForeground();

	public void setForeground(Color c);

	public Cursor getCursor();

	public void setCursor(Cursor cursor);

	public Font getFont();

	public void setFont(Font f);

	public FontMetrics getFontMetrics(Font f);

	public boolean isEnabled();

	public void setEnabled(boolean b);

	public boolean isVisible();

	public void setVisible(boolean b);

	public boolean isShowing();

	public boolean contains(Point p);

	public Point getLocationOnScreen();

	public Point getLocation();

	public void setLocation(Point p);

	public Rectangle getBounds();

	public void setBounds(Rectangle r);

	public Dimension getSize();

	public void setSize(Dimension d);

	public Accessible getAccessibleAt(Point p);

	public boolean isFocusTraversable();

	public void requestFocus();

	public void addFocusListener(FocusListener l);

	public void removeFocusListener(FocusListener l);
}
