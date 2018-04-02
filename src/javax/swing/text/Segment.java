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

import java.text.CharacterIterator;

/**
 * A segment of a character array representing a fragment of text. It should be
 * treated as immutable even though the array is directly accessible. This gives
 * fast access to fragments of text without the overhead of copying around
 * characters. This is effectively an unprotected String.
 * <p>
 * The Segment implements the java.text.CharacterIterator interface to support
 * use with the i18n support without copying text into a string.
 *
 * @author Timothy Prinzing
 */
public class Segment implements Cloneable, CharacterIterator, CharSequence {
	public char[] array;
	public int offset;
	public int count;

	private boolean partialReturn;

	public Segment() {
		this(null, 0, 0);
	}

	public Segment(char[] array, int offset, int count) {
		this.array = array;
		this.offset = offset;
		this.count = count;
		partialReturn = false;
	}

	public void setPartialReturn(boolean p) {
		partialReturn = p;
	}

	public boolean isPartialReturn() {
		return partialReturn;
	}

	public String toString() {
		if (array != null) {
			return new String(array, offset, count);
		}
		return "";
	}

	public char first() {
		pos = offset;
		if (count != 0) {
			return array[pos];
		}
		return DONE;
	}

	public char last() {
		pos = offset + count;
		if (count != 0) {
			pos -= 1;
			return array[pos];
		}
		return DONE;
	}

	public char current() {
		if (count != 0 && pos < offset + count) {
			return array[pos];
		}
		return DONE;
	}

	public char next() {
		pos += 1;
		int end = offset + count;
		if (pos >= end) {
			pos = end;
			return DONE;
		}
		return current();
	}

	public char previous() {
		if (pos == offset) {
			return DONE;
		}
		pos -= 1;
		return current();
	}

	public char setIndex(int position) {
		int end = offset + count;
		if ((position < offset) || (position > end)) {
			throw new IllegalArgumentException("bad position: " + position);
		}
		pos = position;
		if ((pos != end) && (count != 0)) {
			return array[pos];
		}
		return DONE;
	}

	public int getBeginIndex() {
		return offset;
	}

	public int getEndIndex() {
		return offset + count;
	}

	public int getIndex() {
		return pos;
	}

	public char charAt(int index) {
		if (index < 0 || index >= count) {
			throw new StringIndexOutOfBoundsException(index);
		}
		return array[offset + index];
	}

	public int length() {
		return count;
	}

	public CharSequence subSequence(int start, int end) {
		if (start < 0) {
			throw new StringIndexOutOfBoundsException(start);
		}
		if (end > count) {
			throw new StringIndexOutOfBoundsException(end);
		}
		if (start > end) {
			throw new StringIndexOutOfBoundsException(end - start);
		}
		Segment segment = new Segment();
		segment.array = this.array;
		segment.offset = this.offset + start;
		segment.count = end - start;
		return segment;
	}

	public Object clone() {
		Segment out = new Segment();
		out.array = new char[this.array.length];
		System.arraycopy(this.array, 0, out.array, 0, this.array.length);
		out.count = this.count;
		out.offset = this.offset;

		return out;
	}

	private int pos;

}