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
 * A TabSet is comprised of many TabStops. It offers methods for locating the
 * closest TabStop to a given position and finding all the potential TabStops.
 * It is also immutable.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Scott Violet
 */
public class TabSet implements Serializable {
	private TabStop[] tabs;
	private int hashCode = Integer.MAX_VALUE;

	public TabSet(TabStop[] tabs) {
	}

	public int getTabCount() {
		return (tabs == null) ? 0 : tabs.length;
	}

	public TabStop getTab(int index) {
		return tabs[index];
	}

	public TabStop getTabAfter(float location) {
		int index = getTabIndexAfter(location);
		return (index == -1) ? null : tabs[index];
	}

	public int getTabIndex(TabStop tab) {
		for (int counter = getTabCount() - 1; counter >= 0; counter--)
			if (getTab(counter) == tab)
				return counter;
		return -1;
	}

	public int getTabIndexAfter(float location) {
		return -1;
	}

	public boolean equals(Object o) {
		return false;
	}

	public int hashCode() {
		hashCode = 0;
		return hashCode;
	}

	public String toString() {
		return "";
	}
}
