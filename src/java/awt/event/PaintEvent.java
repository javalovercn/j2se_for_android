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
package java.awt.event;

import java.awt.Component;
import java.awt.Rectangle;

/**
 * The component-level paint event. This event is a special type which is used
 * to ensure that paint/update method calls are serialized along with the other
 * events delivered from the event queue. This event is not designed to be used
 * with the Event Listener model; programs should continue to override
 * paint/update methods in order render themselves properly.
 * <p>
 * An unspecified behavior will be caused if the {@code id} parameter of any
 * particular {@code PaintEvent} instance is not in the range from
 * {@code PAINT_FIRST} to {@code PAINT_LAST}.
 *
 * @author Amy Fowler
 * @since 1.1
 */
public class PaintEvent extends ComponentEvent {
	public static final int PAINT_FIRST = 800;
	public static final int PAINT_LAST = 801;
	public static final int PAINT = PAINT_FIRST;
	public static final int UPDATE = PAINT_FIRST + 1;

	Rectangle updateRect;

	public PaintEvent(Component source, int id, Rectangle updateRect) {
		super(source, id);
		this.updateRect = updateRect;
	}

	public Rectangle getUpdateRect() {
		return updateRect;
	}

	public void setUpdateRect(Rectangle updateRect) {
		this.updateRect = updateRect;
	}

	public String paramString() {
		return "unknown type";
	}
}