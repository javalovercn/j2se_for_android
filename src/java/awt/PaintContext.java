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

import java.awt.image.ColorModel;
import java.awt.image.Raster;

/**
 * The <code>PaintContext</code> interface defines the encapsulated
 * and optimized environment to generate color patterns in device
 * space for fill or stroke operations on a
 * {@link Graphics2D}.  The <code>PaintContext</code> provides
 * the necessary colors for <code>Graphics2D</code> operations in the
 * form of a {@link Raster} associated with a {@link ColorModel}.
 * The <code>PaintContext</code> maintains state for a particular paint
 * operation.  In a multi-threaded environment, several
 * contexts can exist simultaneously for a single {@link Paint} object.
 * @see Paint
 */

public interface PaintContext {
	public void dispose();

	ColorModel getColorModel();

	Raster getRaster(int x, int y, int w, int h);

}
