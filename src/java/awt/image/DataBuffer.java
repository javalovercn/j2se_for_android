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

/* ****************************************************************
 ******************************************************************
 ******************************************************************
 *** COPYRIGHT (c) Eastman Kodak Company, 1997
 *** As  an unpublished  work pursuant to Title 17 of the United
 *** States Code.  All rights reserved.
 ******************************************************************
 ******************************************************************
 ******************************************************************/

package java.awt.image;

/**
 * This class exists to wrap one or more data arrays.  Each data array in
 * the DataBuffer is referred to as a bank.  Accessor methods for getting
 * and setting elements of the DataBuffer's banks exist with and without
 * a bank specifier.  The methods without a bank specifier use the default 0th
 * bank.  The DataBuffer can optionally take an offset per bank, so that
 * data in an existing array can be used even if the interesting data
 * doesn't start at array location zero.  Getting or setting the 0th
 * element of a bank, uses the (0+offset)th element of the array.  The
 * size field specifies how much of the data array is available for
 * use.  Size + offset for a given bank should never be greater
 * than the length of the associated data array.  The data type of
 * a data buffer indicates the type of the data array(s) and may also
 * indicate additional semantics, e.g. storing unsigned 8-bit data
 * in elements of a byte array.  The data type may be TYPE_UNDEFINED
 * or one of the types defined below.  Other types may be added in
 * the future.  Generally, an object of class DataBuffer will be cast down
 * to one of its data type specific subclasses to access data type specific
 * methods for improved performance.  Currently, the Java 2D(tm) API
 * image classes use TYPE_BYTE, TYPE_USHORT, TYPE_INT, TYPE_SHORT,
 * TYPE_FLOAT, and TYPE_DOUBLE DataBuffers to store image data.
 * @see java.awt.image.Raster
 * @see java.awt.image.SampleModel
 */
public abstract class DataBuffer {
    public static final int TYPE_BYTE  = 0;
    public static final int TYPE_USHORT = 1;
    public static final int TYPE_SHORT = 2;
    public static final int TYPE_INT   = 3;
    public static final int TYPE_FLOAT  = 4;
    public static final int TYPE_DOUBLE  = 5;
    public static final int TYPE_UNDEFINED = 32;

    protected int dataType;
    protected int banks;
    protected int offset;
    protected int size;
    protected int offsets[];

    private static final int dataTypeSize[] = {8,16,16,32,32,64};

    public static int getDataTypeSize(int type) {
        if (type < TYPE_BYTE || type > TYPE_DOUBLE) {
            throw new IllegalArgumentException("Unknown data type "+type);
        }
        return dataTypeSize[type];
    }

    protected DataBuffer(int dataType, int size)
    {
        this.dataType = dataType;
        this.banks = 1;
        this.size = size;
        this.offset = 0;
        this.offsets = new int[1]; 
    }

    protected DataBuffer(int dataType, int size, int numBanks)
    {
        this.dataType = dataType;
        this.banks = numBanks;
        this.size = size;
        this.offset = 0;
        this.offsets = new int[banks];
    }

    protected DataBuffer(int dataType, int size, int numBanks, int offset)
    {
        this.dataType = dataType;
        this.banks = numBanks;
        this.size = size;
        this.offset = offset;
        this.offsets = new int[numBanks];
        for (int i = 0; i < numBanks; i++) {
            this.offsets[i] = offset;
        }
    }

    protected DataBuffer(int dataType, int size, int numBanks, int offsets[])
    {
        this.dataType = dataType;
        this.banks = numBanks;
        this.size = size;
        this.offset = offsets[0];
        this.offsets = (int[])offsets.clone();
    }

    public int getDataType() {
        return dataType;
    }

    public int getSize() {
        return size;
    }

    public int getOffset() {
        return offset;
    }

    public int[] getOffsets() {
        return (int[])offsets.clone();
    }

    public int getNumBanks() {
        return banks;
    }

    public int getElem(int i) {
        return getElem(0,i);
    }

    public abstract int getElem(int bank, int i);

    public void  setElem(int i, int val) {
    }

    public abstract void setElem(int bank, int i, int val);

    public float getElemFloat(int i) {
        return 0;
    }

    public float getElemFloat(int bank, int i) {
        return 0;
    }

    public void setElemFloat(int i, float val) {
    }

    public void setElemFloat(int bank, int i, float val) {
    }

    public double getElemDouble(int i) {
        return 0;
    }

    public double getElemDouble(int bank, int i) {
        return 0;
    }

    public void setElemDouble(int i, double val) {
    }

    public void setElemDouble(int bank, int i, double val) {
    }

    static int[] toIntArray(Object obj) {
        if (obj instanceof int[]) {
            return (int[])obj;
        } else if (obj == null) {
            return null;
        } else if (obj instanceof short[]) {
            short sdata[] = (short[])obj;
            int idata[] = new int[sdata.length];
            for (int i = 0; i < sdata.length; i++) {
                idata[i] = (int)sdata[i] & 0xffff;
            }
            return idata;
        } else if (obj instanceof byte[]) {
            byte bdata[] = (byte[])obj;
            int idata[] = new int[bdata.length];
            for (int i = 0; i < bdata.length; i++) {
                idata[i] = 0xff & (int)bdata[i];
            }
            return idata;
        }
        return null;
    }
}