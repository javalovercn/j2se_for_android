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
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.FlavorMap;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.EventListener;

/**
 * The <code>DragSource</code> is the entity responsible
 * for the initiation of the Drag
 * and Drop operation, and may be used in a number of scenarios:
 * <UL>
 * <LI>1 default instance per JVM for the lifetime of that JVM.
 * <LI>1 instance per class of potential Drag Initiator object (e.g
 * TextField). [implementation dependent]
 * <LI>1 per instance of a particular
 * <code>Component</code>, or application specific
 * object associated with a <code>Component</code>
 * instance in the GUI. [implementation dependent]
 * <LI>Some other arbitrary association. [implementation dependent]
 *</UL>
 *
 * Once the <code>DragSource</code> is
 * obtained, a <code>DragGestureRecognizer</code> should
 * also be obtained to associate the <code>DragSource</code>
 * with a particular
 * <code>Component</code>.
 * <P>
 * The initial interpretation of the user's gesture,
 * and the subsequent starting of the drag operation
 * are the responsibility of the implementing
 * <code>Component</code>, which is usually
 * implemented by a <code>DragGestureRecognizer</code>.
 *<P>
 * When a drag gesture occurs, the
 * <code>DragSource</code>'s
 * startDrag() method shall be
 * invoked in order to cause processing
 * of the user's navigational
 * gestures and delivery of Drag and Drop
 * protocol notifications. A
 * <code>DragSource</code> shall only
 * permit a single Drag and Drop operation to be
 * current at any one time, and shall
 * reject any further startDrag() requests
 * by throwing an <code>IllegalDnDOperationException</code>
 * until such time as the extant operation is complete.
 * <P>
 * The startDrag() method invokes the
 * createDragSourceContext() method to
 * instantiate an appropriate
 * <code>DragSourceContext</code>
 * and associate the <code>DragSourceContextPeer</code>
 * with that.
 * <P>
 * If the Drag and Drop System is
 * unable to initiate a drag operation for
 * some reason, the startDrag() method throws
 * a <code>java.awt.dnd.InvalidDnDOperationException</code>
 * to signal such a condition. Typically this
 * exception is thrown when the underlying platform
 * system is either not in a state to
 * initiate a drag, or the parameters specified are invalid.
 * <P>
 * Note that during the drag, the
 * set of operations exposed by the source
 * at the start of the drag operation may not change
 * until the operation is complete.
 * The operation(s) are constant for the
 * duration of the operation with respect to the
 * <code>DragSource</code>.
 *
 * @since 1.2
 */

public class DragSource implements Serializable {
    private static Cursor load(String name) {
            return null;
    }

    public static final Cursor DefaultCopyDrop =
        load("DnD.Cursor.CopyDrop");
    public static final Cursor DefaultMoveDrop =
        load("DnD.Cursor.MoveDrop");
    public static final Cursor DefaultLinkDrop =
        load("DnD.Cursor.LinkDrop");
    public static final Cursor DefaultCopyNoDrop =
        load("DnD.Cursor.CopyNoDrop");
    public static final Cursor DefaultMoveNoDrop =
        load("DnD.Cursor.MoveNoDrop");
    public static final Cursor DefaultLinkNoDrop =
        load("DnD.Cursor.LinkNoDrop");

    private static final DragSource dflt = new DragSource();

    static final String dragSourceListenerK = "dragSourceL";
    static final String dragSourceMotionListenerK = "dragSourceMotionL";

    public static DragSource getDefaultDragSource() {
            return dflt;
    }

    public static boolean isDragImageSupported() {
            return false;
    }

    public DragSource() throws HeadlessException {
    }

    public void startDrag(DragGestureEvent   trigger,
                          Cursor             dragCursor,
                          Image              dragImage,
                          Point              imageOffset,
                          Transferable       transferable,
                          DragSourceListener dsl,
                          FlavorMap          flavorMap) throws InvalidDnDOperationException {
    }

    public void startDrag(DragGestureEvent   trigger,
                          Cursor             dragCursor,
                          Transferable       transferable,
                          DragSourceListener dsl,
                          FlavorMap          flavorMap) throws InvalidDnDOperationException {
    }

    public void startDrag(DragGestureEvent   trigger,
                          Cursor             dragCursor,
                          Image              dragImage,
                          Point              dragOffset,
                          Transferable       transferable,
                          DragSourceListener dsl) throws InvalidDnDOperationException {
    }

    public void startDrag(DragGestureEvent   trigger,
                          Cursor             dragCursor,
                          Transferable       transferable,
                          DragSourceListener dsl) throws InvalidDnDOperationException {
    }

    protected DragSourceContext createDragSourceContext(DragSourceContextPeer dscp, DragGestureEvent dgl, Cursor dragCursor, Image dragImage, Point imageOffset, Transferable t, DragSourceListener dsl) {
        return new DragSourceContext(dscp, dgl, dragCursor, dragImage, imageOffset, t, dsl);
    }

    public FlavorMap getFlavorMap() { return flavorMap; }

    public <T extends DragGestureRecognizer> T
        createDragGestureRecognizer(Class<T> recognizerAbstractClass,
                                    Component c, int actions,
                                    DragGestureListener dgl)
    {
        return null;
    }

    public DragGestureRecognizer createDefaultDragGestureRecognizer(Component c, int actions, DragGestureListener dgl) {
        return null;
    }

    public void addDragSourceListener(DragSourceListener dsl) {
    }

    public void removeDragSourceListener(DragSourceListener dsl) {
    }

    public DragSourceListener[] getDragSourceListeners() {
        return (DragSourceListener[])getListeners(DragSourceListener.class);
    }

    public void addDragSourceMotionListener(DragSourceMotionListener dsml) {
    }

    public void removeDragSourceMotionListener(DragSourceMotionListener dsml) {
    }

    public DragSourceMotionListener[] getDragSourceMotionListeners() {
        return (DragSourceMotionListener[])
            getListeners(DragSourceMotionListener.class);
    }

    public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
        return null;
    }

    void processDragEnter(DragSourceDragEvent dsde) {
    }

    void processDragOver(DragSourceDragEvent dsde) {
    }

    void processDropActionChanged(DragSourceDragEvent dsde) {
    }

    void processDragExit(DragSourceEvent dse) {
    }

    void processDragDropEnd(DragSourceDropEvent dsde) {
    }

    void processDragMouseMoved(DragSourceDragEvent dsde) {
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
    }

    private void readObject(ObjectInputStream s)
      throws ClassNotFoundException, IOException {
    }

    public static int getDragThreshold() {
        return 0;
    }

    private transient FlavorMap flavorMap = null;

    private transient DragSourceListener listener;

    private transient DragSourceMotionListener motionListener;
}
