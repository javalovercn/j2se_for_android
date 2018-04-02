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
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.FlavorMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.peer.ComponentPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TooManyListenersException;

/**
 * The <code>DropTarget</code> is associated with a <code>Component</code> when
 * that <code>Component</code> wishes to accept drops during Drag and Drop
 * operations.
 * <P>
 * Each <code>DropTarget</code> is associated with a <code>FlavorMap</code>. The
 * default <code>FlavorMap</code> hereafter designates the
 * <code>FlavorMap</code> returned by
 * <code>SystemFlavorMap.getDefaultFlavorMap()</code>.
 *
 * @since 1.2
 */
public class DropTarget implements DropTargetListener, Serializable {

	public DropTarget(Component c, int ops, DropTargetListener dtl, boolean act, FlavorMap fm)
			throws HeadlessException {
	}

	public DropTarget(Component c, int ops, DropTargetListener dtl, boolean act)
			throws HeadlessException {
		this(c, ops, dtl, act, null);
	}

	public DropTarget() throws HeadlessException {
		this(null, DnDConstants.ACTION_COPY_OR_MOVE, null, true, null);
	}

	public DropTarget(Component c, DropTargetListener dtl) throws HeadlessException {
		this(c, DnDConstants.ACTION_COPY_OR_MOVE, dtl, true, null);
	}

	public DropTarget(Component c, int ops, DropTargetListener dtl) throws HeadlessException {
		this(c, ops, dtl, true);
	}

	public synchronized void setComponent(Component c) {
		if (component == c || component != null && component.equals(c))
			return;
	}

	public synchronized Component getComponent() {
		return component;
	}

	public void setDefaultActions(int ops) {
	}

	void doSetDefaultActions(int ops) {
		actions = ops;
	}

	public int getDefaultActions() {
		return actions;
	}

	public synchronized void setActive(boolean isActive) {
		if (isActive != active) {
			active = isActive;
		}
	}

	public boolean isActive() {
		return active;
	}

	public synchronized void addDropTargetListener(DropTargetListener dtl)
			throws TooManyListenersException {
	}

	public synchronized void removeDropTargetListener(DropTargetListener dtl) {
	}

	public synchronized void dragEnter(DropTargetDragEvent dtde) {
	}

	public synchronized void dragOver(DropTargetDragEvent dtde) {
	}

	public synchronized void dropActionChanged(DropTargetDragEvent dtde) {
	}

	public synchronized void dragExit(DropTargetEvent dte) {
	}

	public synchronized void drop(DropTargetDropEvent dtde) {
	}

	public FlavorMap getFlavorMap() {
		return null;
	}

	public void setFlavorMap(FlavorMap fm) {
	}

	public void addNotify(ComponentPeer peer) {
	}

	public void removeNotify(ComponentPeer peer) {
	}

	public DropTargetContext getDropTargetContext() {
		return dropTargetContext;
	}

	protected DropTargetContext createDropTargetContext() {
		return new DropTargetContext(this);
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
	}

	protected static class DropTargetAutoScroller implements ActionListener {
		protected DropTargetAutoScroller(Component c, Point p) {
			super();
		}

		private void updateRegion() {
		}

		protected synchronized void updateLocation(Point newLocn) {
		}

		protected void stop() {
		}

		public synchronized void actionPerformed(ActionEvent e) {
		}

		private Component component;
		private Point locn;
		private Point prev;
		private Rectangle outer = new Rectangle();
		private Rectangle inner = new Rectangle();
		private int hysteresis = 10;
	}

	protected DropTargetAutoScroller createDropTargetAutoScroller(Component c, Point p) {
		return new DropTargetAutoScroller(c, p);
	}

	protected void initializeAutoscrolling(Point p) {
	}

	protected void updateAutoscroll(Point dragCursorLocn) {
	}

	protected void clearAutoscroll() {
	}

	private DropTargetContext dropTargetContext = createDropTargetContext();

	private Component component;

	int actions = DnDConstants.ACTION_COPY_OR_MOVE;

	boolean active = true;

	private transient DropTargetAutoScroller autoScroller;

	private transient DropTargetListener dtListener;

}
