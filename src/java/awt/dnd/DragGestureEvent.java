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
package java.awt.dnd;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;


/**
 * A <code>DragGestureEvent</code> is passed
 * to <code>DragGestureListener</code>'s
 * dragGestureRecognized() method
 * when a particular <code>DragGestureRecognizer</code> detects that a
 * platform dependent drag initiating gesture has occurred
 * on the <code>Component</code> that it is tracking.
 *
 * The {@code action} field of any {@code DragGestureEvent} instance should take one of the following
 * values:
 * <ul>
 * <li> {@code DnDConstants.ACTION_COPY}
 * <li> {@code DnDConstants.ACTION_MOVE}
 * <li> {@code DnDConstants.ACTION_LINK}
 * </ul>
 * Assigning the value different from listed above will cause an unspecified behavior.
 *
 * @see java.awt.dnd.DragGestureRecognizer
 * @see java.awt.dnd.DragGestureListener
 * @see java.awt.dnd.DragSource
 * @see java.awt.dnd.DnDConstants
 */

public class DragGestureEvent extends EventObject {
    public DragGestureEvent(DragGestureRecognizer dgr, int act, Point ori,
                            List<? extends InputEvent> evs)
    {
        super(dgr);
        events     = evs;
        action     = act;
        origin     = ori;
    }

    public DragGestureRecognizer getSourceAsDragGestureRecognizer() {
        return (DragGestureRecognizer)getSource();
    }

    public Component getComponent() { return component; }

    public DragSource getDragSource() { return dragSource; }

    public Point getDragOrigin() {
        return origin;
    }

    public Iterator<InputEvent> iterator() { return events.iterator(); }

    public Object[] toArray() { return events.toArray(); }

    public Object[] toArray(Object[] array) { return events.toArray(array); }

    public int getDragAction() { return action; }

    public InputEvent getTriggerEvent() {
        return getSourceAsDragGestureRecognizer().getTriggerEvent();
    }

    public void startDrag(Cursor dragCursor, Transferable transferable)
      throws InvalidDnDOperationException {
    }

    public void startDrag(Cursor dragCursor, Transferable transferable, DragSourceListener dsl) throws InvalidDnDOperationException {
    }

    public void startDrag(Cursor dragCursor, Image dragImage, Point imageOffset, Transferable transferable, DragSourceListener dsl) throws InvalidDnDOperationException {
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
    }

    private void readObject(ObjectInputStream s)
        throws ClassNotFoundException, IOException
    {
    }

    private transient List events;

    private DragSource dragSource;

    private Component  component;

    private Point      origin;

    private int        action;
}