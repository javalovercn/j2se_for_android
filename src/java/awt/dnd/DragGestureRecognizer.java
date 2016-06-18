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
import java.awt.Point;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TooManyListenersException;

/**
 * The <code>DragGestureRecognizer</code> is an
 * abstract base class for the specification
 * of a platform-dependent listener that can be associated with a particular
 * <code>Component</code> in order to
 * identify platform-dependent drag initiating gestures.
 * <p>
 * The appropriate <code>DragGestureRecognizer</code>
 * subclass instance is obtained from the
 * {@link DragSource} asssociated with
 * a particular <code>Component</code>, or from the <code>Toolkit</code> object via its
 * {@link java.awt.Toolkit#createDragGestureRecognizer createDragGestureRecognizer()}
 * method.
 * <p>
 * Once the <code>DragGestureRecognizer</code>
 * is associated with a particular <code>Component</code>
 * it will register the appropriate listener interfaces on that
 * <code>Component</code>
 * in order to track the input events delivered to the <code>Component</code>.
 * <p>
 * Once the <code>DragGestureRecognizer</code> identifies a sequence of events
 * on the <code>Component</code> as a drag initiating gesture, it will notify
 * its unicast <code>DragGestureListener</code> by
 * invoking its
 * {@link java.awt.dnd.DragGestureListener#dragGestureRecognized gestureRecognized()}
 * method.
 * <P>
 * When a concrete <code>DragGestureRecognizer</code>
 * instance detects a drag initiating
 * gesture on the <code>Component</code> it is associated with,
 * it fires a {@link DragGestureEvent} to
 * the <code>DragGestureListener</code> registered on
 * its unicast event source for <code>DragGestureListener</code>
 * events. This <code>DragGestureListener</code> is responsible
 * for causing the associated
 * <code>DragSource</code> to start the Drag and Drop operation (if
 * appropriate).
 * <P>
 * @author Laurence P. G. Cable
 * @see java.awt.dnd.DragGestureListener
 * @see java.awt.dnd.DragGestureEvent
 * @see java.awt.dnd.DragSource
 */

public abstract class DragGestureRecognizer implements Serializable {
    protected DragGestureRecognizer(DragSource ds, Component c, int sa, DragGestureListener dgl) {
        super();

        if (ds == null) throw new IllegalArgumentException("null DragSource");

        dragSource    = ds;
        component     = c;
        sourceActions = sa & (DnDConstants.ACTION_COPY_OR_MOVE | DnDConstants.ACTION_LINK);
    }

    protected DragGestureRecognizer(DragSource ds, Component c, int sa) {
        this(ds, c, sa, null);
    }

    protected DragGestureRecognizer(DragSource ds, Component c) {
        this(ds, c, DnDConstants.ACTION_NONE);
    }

    protected DragGestureRecognizer(DragSource ds) {
        this(ds, null);
    }

    protected abstract void registerListeners();

    protected abstract void unregisterListeners();

    public DragSource getDragSource() { return dragSource; }

    public synchronized Component getComponent() { return component; }

    public synchronized void setComponent(Component c) {
    }

    public synchronized int getSourceActions() { return sourceActions; }

    public synchronized void setSourceActions(int actions) {
        sourceActions = actions & (DnDConstants.ACTION_COPY_OR_MOVE | DnDConstants.ACTION_LINK);
    }

    public InputEvent getTriggerEvent() { return events.isEmpty() ? null : (InputEvent)events.get(0); }

    public void resetRecognizer() { events.clear(); }

    public synchronized void addDragGestureListener(DragGestureListener dgl) throws TooManyListenersException {
    }

    public synchronized void removeDragGestureListener(DragGestureListener dgl) {
    }

    protected synchronized void fireDragGestureRecognized(int dragAction, Point p) {
    }

    protected synchronized void appendEvent(InputEvent awtie) {
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
    }

    private void readObject(ObjectInputStream s)
        throws ClassNotFoundException, IOException
    {
    }

    protected DragSource          dragSource;
    protected Component           component;
    protected transient DragGestureListener dragGestureListener;

  protected int  sourceActions;

   protected ArrayList<InputEvent> events = new ArrayList<InputEvent>(1);
}
