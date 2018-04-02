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
import java.awt.dnd.peer.DragSourceContextPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TooManyListenersException;

/**
 * The <code>DragSourceContext</code> class is responsible for managing the
 * initiator side of the Drag and Drop protocol. In particular, it is
 * responsible for managing drag event notifications to the
 * {@linkplain DragSourceListener DragSourceListeners} and
 * {@linkplain DragSourceMotionListener DragSourceMotionListeners}, and
 * providing the {@link Transferable} representing the source data for the drag
 * operation.
 * <p>
 * Note that the <code>DragSourceContext</code> itself implements the
 * <code>DragSourceListener</code> and <code>DragSourceMotionListener</code>
 * interfaces. This is to allow the platform peer (the
 * {@link DragSourceContextPeer} instance) created by the {@link DragSource} to
 * notify the <code>DragSourceContext</code> of state changes in the ongoing
 * operation. This allows the <code>DragSourceContext</code> object to interpose
 * itself between the platform and the listeners provided by the initiator of
 * the drag operation.
 * <p>
 * <a name="defaultCursor" /> By default, {@code DragSourceContext} sets the
 * cursor as appropriate for the current state of the drag and drop operation.
 * For example, if the user has chosen {@linkplain DnDConstants#ACTION_MOVE the
 * move action}, and the pointer is over a target that accepts the move action,
 * the default move cursor is shown. When the pointer is over an area that does
 * not accept the transfer, the default "no drop" cursor is shown.
 * <p>
 * This default handling mechanism is disabled when a custom cursor is set by
 * the {@link #setCursor} method. When the default handling is disabled, it
 * becomes the responsibility of the developer to keep the cursor up to date, by
 * listening to the {@code DragSource} events and calling the
 * {@code setCursor()} method. Alternatively, you can provide custom cursor
 * behavior by providing custom implementations of the {@code DragSource} and
 * the {@code DragSourceContext} classes.
 *
 * @see DragSourceListener
 * @see DragSourceMotionListener
 * @see DnDConstants
 * @since 1.2
 */

public class DragSourceContext
		implements DragSourceListener, DragSourceMotionListener, Serializable {
	protected static final int DEFAULT = 0;
	protected static final int ENTER = 1;
	protected static final int OVER = 2;
	protected static final int CHANGED = 3;

	public DragSourceContext(DragSourceContextPeer dscp, DragGestureEvent trigger,
			Cursor dragCursor, Image dragImage, Point offset, Transferable t,
			DragSourceListener dsl) {
		sourceActions = 0;
	}

	public DragSource getDragSource() {
		return trigger.getDragSource();
	}

	public Component getComponent() {
		return trigger.getComponent();
	}

	public DragGestureEvent getTrigger() {
		return trigger;
	}

	public int getSourceActions() {
		return sourceActions;
	}

	public synchronized void setCursor(Cursor c) {
	}

	public Cursor getCursor() {
		return cursor;
	}

	public synchronized void addDragSourceListener(DragSourceListener dsl)
			throws TooManyListenersException {
	}

	public synchronized void removeDragSourceListener(DragSourceListener dsl) {
	}

	public void transferablesFlavorsChanged() {
	}

	public void dragEnter(DragSourceDragEvent dsde) {
	}

	public void dragOver(DragSourceDragEvent dsde) {
	}

	public void dragExit(DragSourceEvent dse) {
	}

	public void dropActionChanged(DragSourceDragEvent dsde) {
	}

	public void dragDropEnd(DragSourceDropEvent dsde) {
	}

	public void dragMouseMoved(DragSourceDragEvent dsde) {
	}

	public Transferable getTransferable() {
		return transferable;
	}

	protected synchronized void updateCurrentCursor(int sourceAct, int targetAct, int status) {
	}

	private void setCursorImpl(Cursor c) {
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
	}

	private static Transferable emptyTransferable;

	private transient DragSourceContextPeer peer;

	private DragGestureEvent trigger;

	private Cursor cursor;

	private transient Transferable transferable;

	private transient DragSourceListener listener;

	private boolean useCustomCursor;

	private final int sourceActions;
}
