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

import javax.swing.event.ListSelectionListener;

/**
 * This interface represents the current state of the selection for any of the
 * components that display a list of values with stable indices. The selection
 * is modeled as a set of intervals, each interval represents a contiguous range
 * of selected list elements. The methods for modifying the set of selected
 * intervals all take a pair of indices, index0 and index1, that represent a
 * closed interval, i.e. the interval includes both index0 and index1.
 *
 * @author Hans Muller
 * @author Philip Milne
 * @see DefaultListSelectionModel
 */
public interface ListSelectionModel {
	int SINGLE_SELECTION = 0;
	int SINGLE_INTERVAL_SELECTION = 1;
	int MULTIPLE_INTERVAL_SELECTION = 2;

	void setSelectionInterval(int index0, int index1);

	void addSelectionInterval(int index0, int index1);

	void removeSelectionInterval(int index0, int index1);

	int getMinSelectionIndex();

	int getMaxSelectionIndex();

	boolean isSelectedIndex(int index);

	int getAnchorSelectionIndex();

	void setAnchorSelectionIndex(int index);

	int getLeadSelectionIndex();

	void setLeadSelectionIndex(int index);

	void clearSelection();

	boolean isSelectionEmpty();

	void insertIndexInterval(int index, int length, boolean before);

	void removeIndexInterval(int index0, int index1);

	void setValueIsAdjusting(boolean valueIsAdjusting);

	boolean getValueIsAdjusting();

	void setSelectionMode(int selectionMode);

	int getSelectionMode();

	void addListSelectionListener(ListSelectionListener x);

	void removeListSelectionListener(ListSelectionListener x);
}
