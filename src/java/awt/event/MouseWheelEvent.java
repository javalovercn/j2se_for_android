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

import java.awt.Component;

/**
 * An event which indicates that the mouse wheel was rotated in a component.
 * <P>
 * A wheel mouse is a mouse which has a wheel in place of the middle button.
 * This wheel can be rotated towards or away from the user. Mouse wheels are
 * most often used for scrolling, though other uses are possible.
 * <P>
 * A MouseWheelEvent object is passed to every <code>MouseWheelListener</code>
 * object which registered to receive the "interesting" mouse events using the
 * component's <code>addMouseWheelListener</code> method. Each such listener
 * object gets a <code>MouseEvent</code> containing the mouse event.
 * <P>
 * Due to the mouse wheel's special relationship to scrolling Components,
 * MouseWheelEvents are delivered somewhat differently than other MouseEvents.
 * This is because while other MouseEvents usually affect a change on the
 * Component directly under the mouse cursor (for instance, when clicking a
 * button), MouseWheelEvents often have an effect away from the mouse cursor
 * (moving the wheel while over a Component inside a ScrollPane should scroll
 * one of the Scrollbars on the ScrollPane).
 * <P>
 * MouseWheelEvents start delivery from the Component underneath the mouse
 * cursor. If MouseWheelEvents are not enabled on the Component, the event is
 * delivered to the first ancestor Container with MouseWheelEvents enabled. This
 * will usually be a ScrollPane with wheel scrolling enabled. The source
 * Component and x,y coordinates will be relative to the event's final
 * destination (the ScrollPane). This allows a complex GUI to be installed
 * without modification into a ScrollPane, and for all MouseWheelEvents to be
 * delivered to the ScrollPane for scrolling.
 * <P>
 * Some AWT Components are implemented using native widgets which display their
 * own scrollbars and handle their own scrolling. The particular Components for
 * which this is true will vary from platform to platform. When the mouse wheel
 * is moved over one of these Components, the event is delivered straight to the
 * native widget, and not propagated to ancestors.
 * <P>
 * Platforms offer customization of the amount of scrolling that should take
 * place when the mouse wheel is moved. The two most common settings are to
 * scroll a certain number of "units" (commonly lines of text in a text-based
 * component) or an entire "block" (similar to page-up/page-down). The
 * MouseWheelEvent offers methods for conforming to the underlying platform
 * settings. These platform settings can be changed at any time by the user.
 * MouseWheelEvents reflect the most recent settings.
 * <P>
 * The <code>MouseWheelEvent</code> class includes methods for getting the
 * number of "clicks" by which the mouse wheel is rotated. The
 * {@link #getWheelRotation} method returns the integer number of "clicks"
 * corresponding to the number of notches by which the wheel was rotated. In
 * addition to this method, the <code>MouseWheelEvent</code> class provides the
 * {@link #getPreciseWheelRotation} method which returns a double number of
 * "clicks" in case a partial rotation occurred. The
 * {@link #getPreciseWheelRotation} method is useful if a mouse supports a
 * high-resolution wheel, such as a freely rotating wheel with no notches.
 * Applications can benefit by using this method to process mouse wheel events
 * more precisely, and thus, making visual perception smoother.
 *
 * @author Brent Christian
 * @see MouseWheelListener
 * @see java.awt.ScrollPane
 * @see java.awt.ScrollPane#setWheelScrollingEnabled(boolean)
 * @see javax.swing.JScrollPane
 * @see javax.swing.JScrollPane#setWheelScrollingEnabled(boolean)
 * @since 1.4
 */

public class MouseWheelEvent extends MouseEvent {
	public static final int WHEEL_UNIT_SCROLL = 0;
	public static final int WHEEL_BLOCK_SCROLL = 1;

	int scrollType;
	int scrollAmount;
	int wheelRotation;
	double preciseWheelRotation;

	public MouseWheelEvent(Component source, int id, long when, int modifiers, int x, int y,
			int clickCount, boolean popupTrigger, int scrollType, int scrollAmount,
			int wheelRotation) {

		this(source, id, when, modifiers, x, y, 0, 0, clickCount, popupTrigger, scrollType,
				scrollAmount, wheelRotation);
	}

	public MouseWheelEvent(Component source, int id, long when, int modifiers, int x, int y,
			int xAbs, int yAbs, int clickCount, boolean popupTrigger, int scrollType,
			int scrollAmount, int wheelRotation) {
		this(source, id, when, modifiers, x, y, xAbs, yAbs, clickCount, popupTrigger, scrollType,
				scrollAmount, wheelRotation, wheelRotation);
	}

	public MouseWheelEvent(Component source, int id, long when, int modifiers, int x, int y,
			int xAbs, int yAbs, int clickCount, boolean popupTrigger, int scrollType,
			int scrollAmount, int wheelRotation, double preciseWheelRotation) {
		super(source, id, when, modifiers, x, y, xAbs, yAbs, clickCount, popupTrigger,
				MouseEvent.NOBUTTON);

		this.scrollType = scrollType;
		this.scrollAmount = scrollAmount;
		this.wheelRotation = wheelRotation;
		this.preciseWheelRotation = preciseWheelRotation;
	}

	public int getScrollType() {
		return scrollType;
	}

	public int getScrollAmount() {
		return scrollAmount;
	}

	public int getWheelRotation() {
		return wheelRotation;
	}

	public double getPreciseWheelRotation() {
		return preciseWheelRotation;
	}

	public int getUnitsToScroll() {
		return scrollAmount * wheelRotation;
	}

	public String paramString() {
		return "unknown scroll type";
	}
}
