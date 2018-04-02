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
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.peer.DropTargetContextPeer;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * A <code>DropTargetContext</code> is created whenever the logical cursor
 * associated with a Drag and Drop operation coincides with the visible geometry
 * of a <code>Component</code> associated with a <code>DropTarget</code>. The
 * <code>DropTargetContext</code> provides the mechanism for a potential
 * receiver of a drop operation to both provide the end user with the
 * appropriate drag under feedback, but also to effect the subsequent data
 * transfer if appropriate.
 *
 * @since 1.2
 */

public class DropTargetContext implements Serializable {
	DropTargetContext(DropTarget dt) {
		super();

		dropTarget = dt;
	}

	public DropTarget getDropTarget() {
		return dropTarget;
	}

	public Component getComponent() {
		return dropTarget.getComponent();
	}

	public void addNotify(DropTargetContextPeer dtcp) {
		dropTargetContextPeer = dtcp;
	}

	public void removeNotify() {
		dropTargetContextPeer = null;
		transferable = null;
	}

	protected void setTargetActions(int actions) {
	}

	protected int getTargetActions() {
		return 0;
	}

	public void dropComplete(boolean success) throws InvalidDnDOperationException {
	}

	protected void acceptDrag(int dragOperation) {
	}

	protected void rejectDrag() {
	}

	protected void acceptDrop(int dropOperation) {
	}

	protected void rejectDrop() {
	}

	protected DataFlavor[] getCurrentDataFlavors() {
		return new DataFlavor[0];
	}

	protected List<DataFlavor> getCurrentDataFlavorsAsList() {
		return null;
	}

	protected boolean isDataFlavorSupported(DataFlavor df) {
		return getCurrentDataFlavorsAsList().contains(df);
	}

	protected Transferable getTransferable() throws InvalidDnDOperationException {
		return null;
	}

	DropTargetContextPeer getDropTargetContextPeer() {
		return dropTargetContextPeer;
	}

	protected Transferable createTransferableProxy(Transferable t, boolean local) {
		return new TransferableProxy(t, local);
	}

	protected class TransferableProxy implements Transferable {
		TransferableProxy(Transferable t, boolean local) {
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[0];
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return false;
		}

		public Object getTransferData(DataFlavor df)
				throws UnsupportedFlavorException, IOException {
			return null;
		}

		protected Transferable transferable;
		protected boolean isLocal;
	}

	private DropTarget dropTarget;
	private transient DropTargetContextPeer dropTargetContextPeer;
	private transient Transferable transferable;
}