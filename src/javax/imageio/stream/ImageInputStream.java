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

import java.io.Closeable;
import java.io.DataInput;
import java.io.IOException;
import java.nio.ByteOrder;

/**
 * A seekable input stream interface for use by <code>ImageReader</code>s.
 * Various input sources, such as <code>InputStream</code>s and
 * <code>File</code>s, as well as future fast I/O sources may be "wrapped" by a
 * suitable implementation of this interface for use by the Image I/O API.
 *
 * @see ImageInputStreamImpl
 * @see FileImageInputStream
 * @see FileCacheImageInputStream
 * @see MemoryCacheImageInputStream
 *
 */
public interface ImageInputStream extends DataInput, Closeable {

	void setByteOrder(ByteOrder byteOrder);

	ByteOrder getByteOrder();

	int read() throws IOException;

	int read(byte[] b) throws IOException;

	int read(byte[] b, int off, int len) throws IOException;

	void readBytes(IIOByteBuffer buf, int len) throws IOException;

	boolean readBoolean() throws IOException;

	byte readByte() throws IOException;

	int readUnsignedByte() throws IOException;

	short readShort() throws IOException;

	int readUnsignedShort() throws IOException;

	char readChar() throws IOException;

	int readInt() throws IOException;

	long readUnsignedInt() throws IOException;

	long readLong() throws IOException;

	float readFloat() throws IOException;

	double readDouble() throws IOException;

	String readLine() throws IOException;

	String readUTF() throws IOException;

	void readFully(byte[] b, int off, int len) throws IOException;

	void readFully(byte[] b) throws IOException;

	void readFully(short[] s, int off, int len) throws IOException;

	void readFully(char[] c, int off, int len) throws IOException;

	void readFully(int[] i, int off, int len) throws IOException;

	void readFully(long[] l, int off, int len) throws IOException;

	void readFully(float[] f, int off, int len) throws IOException;

	void readFully(double[] d, int off, int len) throws IOException;

	long getStreamPosition() throws IOException;

	int getBitOffset() throws IOException;

	void setBitOffset(int bitOffset) throws IOException;

	int readBit() throws IOException;

	long readBits(int numBits) throws IOException;

	long length() throws IOException;

	int skipBytes(int n) throws IOException;

	long skipBytes(long n) throws IOException;

	void seek(long pos) throws IOException;

	void mark();

	void reset() throws IOException;

	void flushBefore(long pos) throws IOException;

	void flush() throws IOException;

	long getFlushedPosition();

	boolean isCached();

	boolean isCachedMemory();

	boolean isCachedFile();

	void close() throws IOException;
}
