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
package javax.swing.text;

import java.io.Serializable;

/**
 * This class encapsulates a single tab stop (basically as tab stops are thought
 * of by RTF). A tab stop is at a specified distance from the left margin,
 * aligns text in a specified way, and has a specified leader. TabStops are
 * immutable, and usually contained in TabSets.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 */
public class TabStop implements Serializable {

	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_RIGHT = 1;
	public static final int ALIGN_CENTER = 2;
	public static final int ALIGN_DECIMAL = 4;
	public static final int ALIGN_BAR = 5;
	public static final int LEAD_NONE = 0;
	public static final int LEAD_DOTS = 1;
	public static final int LEAD_HYPHENS = 2;
	public static final int LEAD_UNDERLINE = 3;
	public static final int LEAD_THICKLINE = 4;
	public static final int LEAD_EQUALS = 5;

	private int alignment;
	private float position;
	private int leader;

	public TabStop(float pos) {
		this(pos, ALIGN_LEFT, LEAD_NONE);
	}

	public TabStop(float pos, int align, int leader) {
		alignment = align;
		this.leader = leader;
		position = pos;
	}

	public float getPosition() {
		return position;
	}

	public int getAlignment() {
		return alignment;
	}

	public int getLeader() {
		return leader;
	}

	public boolean equals(Object other) {
		return false;
	}

	public int hashCode() {
		return 0;
	}

	public String toString() {
		return "";
	}
}
