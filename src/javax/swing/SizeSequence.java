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
 * A <code>SizeSequence</code> object efficiently maintains an ordered list of
 * sizes and corresponding positions. One situation for which
 * <code>SizeSequence</code> might be appropriate is in a component that
 * displays multiple rows of unequal size. In this case, a single
 * <code>SizeSequence</code> object could be used to track the heights and Y
 * positions of all rows.
 * <p>
 * Another example would be a multi-column component, such as a
 * <code>JTable</code>, in which the column sizes are not all equal. The
 * <code>JTable</code> might use a single <code>SizeSequence</code> object to
 * store the widths and X positions of all the columns. The <code>JTable</code>
 * could then use the <code>SizeSequence</code> object to find the column
 * corresponding to a certain position. The <code>JTable</code> could update the
 * <code>SizeSequence</code> object whenever one or more column sizes changed.
 *
 * <p>
 * The following figure shows the relationship between size and position data
 * for a multi-column component.
 * <p>
 * <center>
 * <img src="doc-files/SizeSequence-1.gif" width=384 height = 100 alt="The first
 * item begins at position 0, the second at the position equal to the size of
 * the previous item, and so on."> </center>
 * <p>
 * In the figure, the first index (0) corresponds to the first column, the
 * second index (1) to the second column, and so on. The first column's position
 * starts at 0, and the column occupies <em>size<sub>0</sub></em> pixels, where
 * <em>size<sub>0</sub></em> is the value returned by <code>getSize(0)</code>.
 * Thus, the first column ends at <em>size<sub>0</sub></em> - 1. The second
 * column then begins at the position <em>size<sub>0</sub></em> and occupies
 * <em>size<sub>1</sub></em> (<code>getSize(1)</code>) pixels.
 * <p>
 * Note that a <code>SizeSequence</code> object simply represents intervals
 * along an axis. In our examples, the intervals represent height or width in
 * pixels. However, any other unit of measure (for example, time in days) could
 * be just as valid.
 *
 * <p>
 *
 * <h4>Implementation Notes</h4>
 *
 * Normally when storing the size and position of entries, one would choose
 * between storing the sizes or storing their positions instead. The two common
 * operations that are needed during rendering are:
 * <code>getIndex(position)</code> and <code>setSize(index, size)</code>.
 * Whichever choice of internal format is made one of these operations is costly
 * when the number of entries becomes large. If sizes are stored, finding the
 * index of the entry that encloses a particular position is linear in the
 * number of entries. If positions are stored instead, setting the size of an
 * entry at a particular index requires updating the positions of the affected
 * entries, which is also a linear calculation.
 * <p>
 * Like the above techniques this class holds an array of N integers internally
 * but uses a hybrid encoding, which is halfway between the size-based and
 * positional-based approaches. The result is a data structure that takes the
 * same space to store the information but can perform most operations in Log(N)
 * time instead of O(N), where N is the number of entries in the list.
 * <p>
 * Two operations that remain O(N) in the number of entries are the
 * <code>insertEntries</code> and <code>removeEntries</code> methods, both of
 * which are implemented by converting the internal array to a set of integer
 * sizes, copying it into the new array, and then reforming the hybrid
 * representation in place.
 *
 * @author Philip Milne
 * @since 1.3
 */

/*
 * Each method is implemented by taking the minimum and maximum of the range of
 * integers that need to be operated upon. All the algorithms work by dividing
 * this range into two smaller ranges and recursing. The recursion is terminated
 * when the upper and lower bounds are equal.
 */

public class SizeSequence {

	private static int[] emptyArray = new int[0];
	private int a[];

	public SizeSequence() {
		a = emptyArray;
	}

	public SizeSequence(int numEntries) {
		this(numEntries, 0);
	}

	public SizeSequence(int numEntries, int value) {
		this();
		insertEntries(0, numEntries, value);
	}

	public SizeSequence(int[] sizes) {
		this();
		setSizes(sizes);
	}

	void setSizes(int length, int size) {
		if (a.length != length) {
			a = new int[length];
		}
		setSizes(0, length, size);
	}

	private int setSizes(int from, int to, int size) {
		if (to <= from) {
			return 0;
		}
		int m = (from + to) / 2;
		a[m] = size + setSizes(from, m, size);
		return a[m] + setSizes(m + 1, to, size);
	}

	public void setSizes(int[] sizes) {
		if (a.length != sizes.length) {
			a = new int[sizes.length];
		}
		setSizes(0, a.length, sizes);
	}

	private int setSizes(int from, int to, int[] sizes) {
		if (to <= from) {
			return 0;
		}
		int m = (from + to) / 2;
		a[m] = sizes[m] + setSizes(from, m, sizes);
		return a[m] + setSizes(m + 1, to, sizes);
	}

	public int[] getSizes() {
		int n = a.length;
		int[] sizes = new int[n];
		getSizes(0, n, sizes);
		return sizes;
	}

	private int getSizes(int from, int to, int[] sizes) {
		if (to <= from) {
			return 0;
		}
		int m = (from + to) / 2;
		sizes[m] = a[m] - getSizes(from, m, sizes);
		return a[m] + getSizes(m + 1, to, sizes);
	}

	public int getPosition(int index) {
		return getPosition(0, a.length, index);
	}

	private int getPosition(int from, int to, int index) {
		if (to <= from) {
			return 0;
		}
		int m = (from + to) / 2;
		if (index <= m) {
			return getPosition(from, m, index);
		} else {
			return a[m] + getPosition(m + 1, to, index);
		}
	}

	public int getIndex(int position) {
		return getIndex(0, a.length, position);
	}

	private int getIndex(int from, int to, int position) {
		if (to <= from) {
			return from;
		}
		int m = (from + to) / 2;
		int pivot = a[m];
		if (position < pivot) {
			return getIndex(from, m, position);
		} else {
			return getIndex(m + 1, to, position - pivot);
		}
	}

	public int getSize(int index) {
		return getPosition(index + 1) - getPosition(index);
	}

	public void setSize(int index, int size) {
		changeSize(0, a.length, index, size - getSize(index));
	}

	private void changeSize(int from, int to, int index, int delta) {
		if (to <= from) {
			return;
		}
		int m = (from + to) / 2;
		if (index <= m) {
			a[m] += delta;
			changeSize(from, m, index, delta);
		} else {
			changeSize(m + 1, to, index, delta);
		}
	}

	public void insertEntries(int start, int length, int value) {
		int sizes[] = getSizes();
		int end = start + length;
		int n = a.length + length;
		a = new int[n];
		for (int i = 0; i < start; i++) {
			a[i] = sizes[i];
		}
		for (int i = start; i < end; i++) {
			a[i] = value;
		}
		for (int i = end; i < n; i++) {
			a[i] = sizes[i - length];
		}
		setSizes(a);
	}

	public void removeEntries(int start, int length) {
		int sizes[] = getSizes();
		int end = start + length;
		int n = a.length - length;
		a = new int[n];
		for (int i = 0; i < start; i++) {
			a[i] = sizes[i];
		}
		for (int i = start; i < n; i++) {
			a[i] = sizes[i + length];
		}
		setSizes(a);
	}
}