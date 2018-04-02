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

/**
 * The <code>DragSourceDragEvent</code> is delivered from the
 * <code>DragSourceContextPeer</code>, via the <code>DragSourceContext</code>,
 * to the <code>DragSourceListener</code> registered with that
 * <code>DragSourceContext</code> and with its associated
 * <code>DragSource</code>.
 * <p>
 * The <code>DragSourceDragEvent</code> reports the <i>target drop action</i>
 * and the <i>user drop action</i> that reflect the current state of the drag
 * operation.
 * <p>
 * <i>Target drop action</i> is one of <code>DnDConstants</code> that represents
 * the drop action selected by the current drop target if this drop action is
 * supported by the drag source or <code>DnDConstants.ACTION_NONE</code> if this
 * drop action is not supported by the drag source.
 * <p>
 * <i>User drop action</i> depends on the drop actions supported by the drag
 * source and the drop action selected by the user. The user can select a drop
 * action by pressing modifier keys during the drag operation:
 * 
 * <pre>
 *   Ctrl + Shift -> ACTION_LINK
 *   Ctrl         -> ACTION_COPY
 *   Shift        -> ACTION_MOVE
 * </pre>
 * 
 * If the user selects a drop action, the <i>user drop action</i> is one of
 * <code>DnDConstants</code> that represents the selected drop action if this
 * drop action is supported by the drag source or
 * <code>DnDConstants.ACTION_NONE</code> if this drop action is not supported by
 * the drag source.
 * <p>
 * If the user doesn't select a drop action, the set of
 * <code>DnDConstants</code> that represents the set of drop actions supported
 * by the drag source is searched for <code>DnDConstants.ACTION_MOVE</code>,
 * then for <code>DnDConstants.ACTION_COPY</code>, then for
 * <code>DnDConstants.ACTION_LINK</code> and the <i>user drop action</i> is the
 * first constant found. If no constant is found the <i>user drop action</i> is
 * <code>DnDConstants.ACTION_NONE</code>.
 *
 * @since 1.2
 *
 */
public class DragSourceDragEvent extends DragSourceEvent {
	public DragSourceDragEvent(DragSourceContext dsc, int dropAction, int action, int modifiers) {
		super(dsc);

		targetActions = action;
		gestureModifiers = modifiers;
		this.dropAction = dropAction;
	}

	public DragSourceDragEvent(DragSourceContext dsc, int dropAction, int action, int modifiers,
			int x, int y) {
		super(dsc, x, y);

		targetActions = action;
		gestureModifiers = modifiers;
		this.dropAction = dropAction;
	}

	public int getTargetActions() {
		return targetActions;
	}

	// private static final int JDK_1_3_MODIFIERS = InputEvent.SHIFT_DOWN_MASK -
	// 1;
	// private static final int JDK_1_4_MODIFIERS =
	// ((InputEvent.ALT_GRAPH_DOWN_MASK << 1) - 1) & ~JDK_1_3_MODIFIERS;

	public int getGestureModifiers() {
		return 0;
	}

	public int getGestureModifiersEx() {
		return 0;
	}

	public int getUserAction() {
		return dropAction;
	}

	public int getDropAction() {
		return targetActions & getDragSourceContext().getSourceActions();
	}

	private int targetActions = DnDConstants.ACTION_NONE;
	private int dropAction = DnDConstants.ACTION_NONE;
	private int gestureModifiers = 0;
	private boolean invalidModifiers;

	private void setNewModifiers() {
	}

	private void setOldModifiers() {
	}
}