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
package java.awt.image;

import java.util.Hashtable;

/**
 * The interface for objects expressing interest in image data through
 * the ImageProducer interfaces.  When a consumer is added to an image
 * producer, the producer delivers all of the data about the image
 * using the method calls defined in this interface.
 *
 * @see ImageProducer
 *
 * @author      Jim Graham
 */
public interface ImageConsumer {
    void setDimensions(int width, int height);
    void setProperties(Hashtable<?,?> props);
    void setColorModel(ColorModel model);
    void setHints(int hintflags);
    int RANDOMPIXELORDER = 1;
    int TOPDOWNLEFTRIGHT = 2;
    int COMPLETESCANLINES = 4;
    int SINGLEPASS = 8;
    int SINGLEFRAME = 16;
    void setPixels(int x, int y, int w, int h,
                   ColorModel model, byte pixels[], int off, int scansize);
    void setPixels(int x, int y, int w, int h,
                   ColorModel model, int pixels[], int off, int scansize);
    void imageComplete(int status);
    int IMAGEERROR = 1;
    int SINGLEFRAMEDONE = 2;
    int STATICIMAGEDONE = 3;
    int IMAGEABORTED = 4;
}
