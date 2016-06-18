/*
 * Copyright (c) 1996, 2011, Oracle and/or its affiliates. All rights reserved.
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

import java.util.EventObject;

/**
 * The root event class for all AWT events.
 * This class and its subclasses supercede the original
 * java.awt.Event class.
 * Subclasses of this root AWTEvent class defined outside of the
 * java.awt.event package should define event ID values greater than
 * the value defined by RESERVED_ID_MAX.
 * <p>
 * The event masks defined in this class are needed by Component subclasses
 * which are using Component.enableEvents() to select for event types not
 * selected by registered listeners. If a listener is registered on a
 * component, the appropriate event mask is already set internally by the
 * component.
 * <p>
 * The masks are also used to specify to which types of events an
 * AWTEventListener should listen. The masks are bitwise-ORed together
 * and passed to Toolkit.addAWTEventListener.
 *
 * @see Component#enableEvents
 * @see Toolkit#addAWTEventListener
 *
 * @see java.awt.event.ActionEvent
 * @see java.awt.event.AdjustmentEvent
 * @see java.awt.event.ComponentEvent
 * @see java.awt.event.ContainerEvent
 * @see java.awt.event.FocusEvent
 * @see java.awt.event.InputMethodEvent
 * @see java.awt.event.InvocationEvent
 * @see java.awt.event.ItemEvent
 * @see java.awt.event.HierarchyEvent
 * @see java.awt.event.KeyEvent
 * @see java.awt.event.MouseEvent
 * @see java.awt.event.MouseWheelEvent
 * @see java.awt.event.PaintEvent
 * @see java.awt.event.TextEvent
 * @see java.awt.event.WindowEvent
 *
 * @author Carl Quinn
 * @author Amy Fowler
 * @since 1.1
 */
public abstract class AWTEvent extends EventObject {
	boolean isPosted = false;// in jdk, but not in android
	protected final int id;
	protected boolean consumed = false;
    /**
     * The event mask for selecting component events.
     */
    public final static long COMPONENT_EVENT_MASK = 0x01;

    /**
     * The event mask for selecting container events.
     */
    public final static long CONTAINER_EVENT_MASK = 0x02;

    /**
     * The event mask for selecting focus events.
     */
    public final static long FOCUS_EVENT_MASK = 0x04;

    /**
     * The event mask for selecting key events.
     */
    public final static long KEY_EVENT_MASK = 0x08;

    /**
     * The event mask for selecting mouse events.
     */
    public final static long MOUSE_EVENT_MASK = 0x10;

    /**
     * The event mask for selecting mouse motion events.
     */
    public final static long MOUSE_MOTION_EVENT_MASK = 0x20;

    /**
     * The event mask for selecting window events.
     */
    public final static long WINDOW_EVENT_MASK = 0x40;

    /**
     * The event mask for selecting action events.
     */
    public final static long ACTION_EVENT_MASK = 0x80;

    /**
     * The event mask for selecting adjustment events.
     */
    public final static long ADJUSTMENT_EVENT_MASK = 0x100;

    /**
     * The event mask for selecting item events.
     */
    public final static long ITEM_EVENT_MASK = 0x200;

    /**
     * The event mask for selecting text events.
     */
    public final static long TEXT_EVENT_MASK = 0x400;

    /**
     * The event mask for selecting input method events.
     */
    public final static long INPUT_METHOD_EVENT_MASK = 0x800;

    /**
     * The pseudo event mask for enabling input methods.
     * We're using one bit in the eventMask so we don't need
     * a separate field inputMethodsEnabled.
     */
    final static long INPUT_METHODS_ENABLED_MASK = 0x1000;

    /**
     * The event mask for selecting paint events.
     */
    public final static long PAINT_EVENT_MASK = 0x2000;

    /**
     * The event mask for selecting invocation events.
     */
    public final static long INVOCATION_EVENT_MASK = 0x4000;

    /**
     * The event mask for selecting hierarchy events.
     */
    public final static long HIERARCHY_EVENT_MASK = 0x8000;

    /**
     * The event mask for selecting hierarchy bounds events.
     */
    public final static long HIERARCHY_BOUNDS_EVENT_MASK = 0x10000;

    /**
     * The event mask for selecting mouse wheel events.
     * @since 1.4
     */
    public final static long MOUSE_WHEEL_EVENT_MASK = 0x20000;

    /**
     * The event mask for selecting window state events.
     * @since 1.4
     */
    public final static long WINDOW_STATE_EVENT_MASK = 0x40000;

    /**
     * The event mask for selecting window focus events.
     * @since 1.4
     */
    public final static long WINDOW_FOCUS_EVENT_MASK = 0x80000;

    /**
     * WARNING: there are more mask defined privately.  See
     * SunToolkit.GRAB_EVENT_MASK.
     */

    /**
     * The maximum value for reserved AWT event IDs. Programs defining
     * their own event IDs should use IDs greater than this value.
     */
    public final static int RESERVED_ID_MAX = 1999;

    /*
     * JDK 1.1 serialVersionUID
     */
    private static final long serialVersionUID = -1825314779160409405L;

    /**
     * Constructs an AWTEvent object from the parameters of a 1.0-style event.
     * @param event the old-style event
     */
    public AWTEvent(Event event) {
        this(event.target, event.id);
    }

    /**
     * Constructs an AWTEvent object with the specified source object and type.
     *
     * @param source the object where the event originated
     * @param id the event type
     */
    public AWTEvent(Object source, int id) {
        super(source);
        this.id = id;
    }

    /**
     * Retargets an event to a new source. This method is typically used to
     * retarget an event to a lightweight child Component of the original
     * heavyweight source.
     * <p>
     * This method is intended to be used only by event targeting subsystems,
     * such as client-defined KeyboardFocusManagers. It is not for general
     * client use.
     *
     * @param newSource the new Object to which the event should be dispatched
     * @since 1.4
     */
    public void setSource(Object newSource) {
        if (source == newSource) {
            return;
        }

        synchronized (this) {
            source = newSource;
        }
    }

    /**
     * Returns the event type.
     */
    public int getID() {
        return id;
    }

    /**
     * Returns a String representation of this object.
     */
	public String toString() {
		String srcName = null;
		return getClass().getName() + " on "
				+ (srcName != null ? srcName : source);
	}

    /**
     * Returns a string representing the state of this <code>Event</code>.
     * This method is intended to be used only for debugging purposes, and the
     * content and format of the returned string may vary between
     * implementations. The returned string may be empty but may not be
     * <code>null</code>.
     *
     * @return  a string representation of this event
     */
    public String paramString() {
        return "";
    }

    /**
     * Returns whether this event has been consumed.
     */
    protected boolean isConsumed() {
        return consumed;
    }

}

