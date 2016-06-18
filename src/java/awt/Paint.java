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

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

/**
 * This <code>Paint</code> interface defines how color patterns
 * can be generated for {@link Graphics2D} operations.  A class
 * implementing the <code>Paint</code> interface is added to the
 * <code>Graphics2D</code> context in order to define the color
 * pattern used by the <code>draw</code> and <code>fill</code> methods.
 * <p>
 * Instances of classes implementing <code>Paint</code> must be
 * read-only because the <code>Graphics2D</code> does not clone
 * these objects when they are set as an attribute with the
 * <code>setPaint</code> method or when the <code>Graphics2D</code>
 * object is itself cloned.
 * @see PaintContext
 * @see Color
 * @see GradientPaint
 * @see TexturePaint
 * @see Graphics2D#setPaint
 * @version 1.36, 06/05/07
 */
public interface Paint extends Transparency {
	public PaintContext createContext(ColorModel cm, Rectangle deviceBounds,
			Rectangle2D userBounds, AffineTransform xform, RenderingHints hints);

}