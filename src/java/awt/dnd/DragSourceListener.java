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

import java.util.EventListener;

/**
 * The <code>DragSourceListener</code> defines the event interface for
 * originators of Drag and Drop operations to track the state of the user's
 * gesture, and to provide appropriate &quot;drag over&quot; feedback to the
 * user throughout the Drag and Drop operation.
 * <p>
 * The drop site is <i>associated with the previous <code>dragEnter()</code>
 * invocation</i> if the latest invocation of <code>dragEnter()</code> on this
 * listener:
 * <ul>
 * <li>corresponds to that drop site and
 * <li>is not followed by a <code>dragExit()</code> invocation on this listener.
 * </ul>
 *
 * @since 1.2
 */

public interface DragSourceListener extends EventListener {
	void dragEnter(DragSourceDragEvent dsde);

	void dragOver(DragSourceDragEvent dsde);

	void dropActionChanged(DragSourceDragEvent dsde);

	void dragExit(DragSourceEvent dse);

	void dragDropEnd(DragSourceDropEvent dsde);
}