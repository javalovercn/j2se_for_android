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
package javax.swing;

/**
 * A collection of constants generally used for positioning and orienting
 * components on the screen.
 *
 * @author Jeff Dinkins
 * @author Ralph Kar (orientation support)
 */
public interface SwingConstants {
	public static final int CENTER = 0;
	public static final int TOP = 1;
	public static final int LEFT = 2;
	public static final int BOTTOM = 3;
	public static final int RIGHT = 4;
	public static final int NORTH = 1;
	public static final int NORTH_EAST = 2;
	public static final int EAST = 3;
	public static final int SOUTH_EAST = 4;
	public static final int SOUTH = 5;
	public static final int SOUTH_WEST = 6;
	public static final int WEST = 7;
	public static final int NORTH_WEST = 8;

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	public static final int LEADING = 10;
	public static final int TRAILING = 11;
	public static final int NEXT = 12;
	public static final int PREVIOUS = 13;
}
