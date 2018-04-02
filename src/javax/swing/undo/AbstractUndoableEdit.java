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

import java.io.Serializable;

import javax.swing.UIManager;

/**
 * An abstract implementation of <code>UndoableEdit</code>, implementing simple
 * responses to all boolean methods in that interface.
 *
 * @author Ray Ryan
 */
public class AbstractUndoableEdit implements UndoableEdit, Serializable {

	protected static final String UndoName = "Undo";
	protected static final String RedoName = "Redo";

	boolean hasBeenDone;
	boolean alive;

	public AbstractUndoableEdit() {
		super();

		hasBeenDone = true;
		alive = true;
	}

	public void die() {
		alive = false;
	}

	public void undo() throws CannotUndoException {
		if (!canUndo()) {
			throw new CannotUndoException();
		}
		hasBeenDone = false;
	}

	public boolean canUndo() {
		return alive && hasBeenDone;
	}

	public void redo() throws CannotRedoException {
		if (!canRedo()) {
			throw new CannotRedoException();
		}
		hasBeenDone = true;
	}

	public boolean canRedo() {
		return alive && !hasBeenDone;
	}

	public boolean addEdit(UndoableEdit anEdit) {
		return false;
	}

	public boolean replaceEdit(UndoableEdit anEdit) {
		return false;
	}

	public boolean isSignificant() {
		return true;
	}

	public String getPresentationName() {
		return "";
	}

	public String getUndoPresentationName() {
		String name = getPresentationName();
		if (!"".equals(name)) {
			name = UIManager.getString("AbstractUndoableEdit.undoText") + " " + name;
		} else {
			name = UIManager.getString("AbstractUndoableEdit.undoText");
		}

		return name;
	}

	public String getRedoPresentationName() {
		return "";
	}

	public String toString() {
		return "";
	}
}