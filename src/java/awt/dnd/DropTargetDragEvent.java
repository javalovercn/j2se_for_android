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

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.List;

/**
 * The <code>DropTargetDragEvent</code> is delivered to a
 * <code>DropTargetListener</code> via its
 * dragEnter() and dragOver() methods.
 * <p>
 * The <code>DropTargetDragEvent</code> reports the <i>source drop actions</i>
 * and the <i>user drop action</i> that reflect the current state of
 * the drag operation.
 * <p>
 * <i>Source drop actions</i> is a bitwise mask of <code>DnDConstants</code>
 * that represents the set of drop actions supported by the drag source for
 * this drag operation.
 * <p>
 * <i>User drop action</i> depends on the drop actions supported by the drag
 * source and the drop action selected by the user. The user can select a drop
 * action by pressing modifier keys during the drag operation:
 * <pre>
 *   Ctrl + Shift -> ACTION_LINK
 *   Ctrl         -> ACTION_COPY
 *   Shift        -> ACTION_MOVE
 * </pre>
 * If the user selects a drop action, the <i>user drop action</i> is one of
 * <code>DnDConstants</code> that represents the selected drop action if this
 * drop action is supported by the drag source or
 * <code>DnDConstants.ACTION_NONE</code> if this drop action is not supported
 * by the drag source.
 * <p>
 * If the user doesn't select a drop action, the set of
 * <code>DnDConstants</code> that represents the set of drop actions supported
 * by the drag source is searched for <code>DnDConstants.ACTION_MOVE</code>,
 * then for <code>DnDConstants.ACTION_COPY</code>, then for
 * <code>DnDConstants.ACTION_LINK</code> and the <i>user drop action</i> is the
 * first constant found. If no constant is found the <i>user drop action</i>
 * is <code>DnDConstants.ACTION_NONE</code>.
 *
 * @since 1.2
 */

public class DropTargetDragEvent extends DropTargetEvent {

	public DropTargetDragEvent(DropTargetContext dtc, Point cursorLocn, int dropAction, int srcActions)  {
        super(dtc);

        if (cursorLocn == null) throw new NullPointerException("cursorLocn");

        if (dropAction != DnDConstants.ACTION_NONE &&
            dropAction != DnDConstants.ACTION_COPY &&
            dropAction != DnDConstants.ACTION_MOVE &&
            dropAction != DnDConstants.ACTION_LINK
        ) throw new IllegalArgumentException("dropAction" + dropAction);

        if ((srcActions & ~(DnDConstants.ACTION_COPY_OR_MOVE | DnDConstants.ACTION_LINK)) != 0) throw new IllegalArgumentException("srcActions");

        location        = cursorLocn;
        actions         = srcActions;
        this.dropAction = dropAction;
    }

    public Point getLocation() {
        return location;
    }

    public DataFlavor[] getCurrentDataFlavors() {
        return getDropTargetContext().getCurrentDataFlavors();
    }

    public List<DataFlavor> getCurrentDataFlavorsAsList() {
        return getDropTargetContext().getCurrentDataFlavorsAsList();
    }

    public boolean isDataFlavorSupported(DataFlavor df) {
        return getDropTargetContext().isDataFlavorSupported(df);
    }

    public int getSourceActions() { return actions; }

    public int getDropAction() { return dropAction; }

    public Transferable getTransferable() {
    	try{
    		return getDropTargetContext().getTransferable();
    	}catch (Exception e) {
    		return null;
		}
    }

    public void acceptDrag(int dragOperation) {
        getDropTargetContext().acceptDrag(dragOperation);
    }

    public void rejectDrag() {
        getDropTargetContext().rejectDrag();
    }

    private Point               location;
    private int                 actions;
    private int                 dropAction;
}
