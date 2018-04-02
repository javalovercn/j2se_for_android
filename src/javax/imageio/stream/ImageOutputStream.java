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
package javax.imageio.stream;

import java.io.DataOutput;
import java.io.IOException;

/**
 * A seekable output stream interface for use by <code>ImageWriter</code>s.
 * Various output destinations, such as <code>OutputStream</code>s and
 * <code>File</code>s, as well as future fast I/O destinations may be "wrapped"
 * by a suitable implementation of this interface for use by the Image I/O API.
 *
 * <p>
 * Unlike a standard <code>OutputStream</code>, ImageOutputStream extends its
 * counterpart, <code>ImageInputStream</code>. Thus it is possible to read from
 * the stream as it is being written. The same seek and flush positions apply to
 * both reading and writing, although the semantics for dealing with a non-zero
 * bit offset before a byte-aligned write are necessarily different from the
 * semantics for dealing with a non-zero bit offset before a byte-aligned read.
 * When reading bytes, any bit offset is set to 0 before the read; when writing
 * bytes, a non-zero bit offset causes the remaining bits in the byte to be
 * written as 0s. The byte-aligned write then starts at the next byte position.
 *
 * @see ImageInputStream
 *
 */
public interface ImageOutputStream extends ImageInputStream, DataOutput {

	void write(int b) throws IOException;

	void write(byte b[]) throws IOException;

	void write(byte b[], int off, int len) throws IOException;

	void writeBoolean(boolean v) throws IOException;

	void writeByte(int v) throws IOException;

	void writeShort(int v) throws IOException;

	void writeChar(int v) throws IOException;

	void writeInt(int v) throws IOException;

	void writeLong(long v) throws IOException;

	void writeFloat(float v) throws IOException;

	void writeDouble(double v) throws IOException;

	void writeBytes(String s) throws IOException;

	void writeChars(String s) throws IOException;

	void writeUTF(String s) throws IOException;

	void writeShorts(short[] s, int off, int len) throws IOException;

	void writeChars(char[] c, int off, int len) throws IOException;

	void writeInts(int[] i, int off, int len) throws IOException;

	void writeLongs(long[] l, int off, int len) throws IOException;

	void writeFloats(float[] f, int off, int len) throws IOException;

	void writeDoubles(double[] d, int off, int len) throws IOException;

	void writeBit(int bit) throws IOException;

	void writeBits(long bits, int numBits) throws IOException;

	void flushBefore(long pos) throws IOException;
}
