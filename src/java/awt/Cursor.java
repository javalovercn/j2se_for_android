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
package java.awt;

/**
 * A class to encapsulate the bitmap representation of the mouse cursor.
 *
 * @see Component#setCursor
 * @author Amy Fowler
 */
public class Cursor implements java.io.Serializable {
	public static final int DEFAULT_CURSOR = 0;
	public static final int CROSSHAIR_CURSOR = 1;
	public static final int TEXT_CURSOR = 2;
	public static final int WAIT_CURSOR = 3;
	public static final int SW_RESIZE_CURSOR = 4;
	public static final int SE_RESIZE_CURSOR = 5;
	public static final int NW_RESIZE_CURSOR = 6;
	public static final int NE_RESIZE_CURSOR = 7;
	public static final int N_RESIZE_CURSOR = 8;
	public static final int S_RESIZE_CURSOR = 9;
	public static final int W_RESIZE_CURSOR = 10;
	public static final int E_RESIZE_CURSOR = 11;
	public static final int HAND_CURSOR = 12;
	public static final int MOVE_CURSOR = 13;

	protected static Cursor predefined[] = new Cursor[14];

	private final static Cursor[] predefinedPrivate = new Cursor[14];

	int type = DEFAULT_CURSOR;

	public static final int CUSTOM_CURSOR = -1;

	private transient long pData;
	private transient Object anchor = new Object();
	protected String name;

	static public Cursor getPredefinedCursor(int type) {
		return null;// TODO
	}

	static public Cursor getSystemCustomCursor(final String name)
			throws AWTException, HeadlessException {
		return null;// TODO
	}

	static public Cursor getDefaultCursor() {
		return getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	}

	public Cursor(int type) {
		if (type < Cursor.DEFAULT_CURSOR || type > Cursor.MOVE_CURSOR) {
			throw new IllegalArgumentException("illegal cursor type");
		}
		this.type = type;

		name = "";
	}

	protected Cursor(String name) {
		this.type = Cursor.CUSTOM_CURSOR;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return getClass().getName() + "[" + getName() + "]";
	}
}
