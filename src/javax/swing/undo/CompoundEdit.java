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
package javax.swing.undo;

import java.util.Vector;

/**
 * A concrete subclass of AbstractUndoableEdit, used to assemble little
 * UndoableEdits into great big ones.
 *
 * @author Ray Ryan
 */
public class CompoundEdit extends AbstractUndoableEdit {
	boolean inProgress;

	protected Vector<UndoableEdit> edits;

	public CompoundEdit() {
		super();
		inProgress = true;
		edits = new Vector<UndoableEdit>();
	}

	public void undo() throws CannotUndoException {
		super.undo();
	}

	public void redo() throws CannotRedoException {
		super.redo();
	}

	protected UndoableEdit lastEdit() {
		return null;
	}

	public void die() {
	}

	public boolean addEdit(UndoableEdit anEdit) {
		return false;
	}

	public void end() {
	}

	public boolean canUndo() {
		return false;
	}

	public boolean canRedo() {
		return false;
	}

	public boolean isInProgress() {
		return inProgress;
	}

	public boolean isSignificant() {
		return false;
	}

	public String getPresentationName() {
		return null;
	}

	public String getUndoPresentationName() {
		return null;
	}

	public String getRedoPresentationName() {
		return null;
	}

	public String toString() {
		return "";
	}
}