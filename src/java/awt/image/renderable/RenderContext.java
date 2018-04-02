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

/* ********************************************************************
 **********************************************************************
 **********************************************************************
 *** COPYRIGHT (c) Eastman Kodak Company, 1997                      ***
 *** As  an unpublished  work pursuant to Title 17 of the United    ***
 *** States Code.  All rights reserved.                             ***
 **********************************************************************
 **********************************************************************
 **********************************************************************/

package java.awt.image.renderable;

import java.awt.geom.*;
import java.awt.*;

/**
 * A RenderContext encapsulates the information needed to produce a specific
 * rendering from a RenderableImage. It contains the area to be rendered
 * specified in rendering-independent terms, the resolution at which the
 * rendering is to be performed, and hints used to control the rendering
 * process.
 *
 * <p>
 * Users create RenderContexts and pass them to the RenderableImage via the
 * createRendering method. Most of the methods of RenderContexts are not meant
 * to be used directly by applications, but by the RenderableImage and operator
 * classes to which it is passed.
 *
 * <p>
 * The AffineTransform parameter passed into and out of this class are cloned.
 * The RenderingHints and Shape parameters are not necessarily cloneable and are
 * therefore only reference copied. Altering RenderingHints or Shape instances
 * that are in use by instances of RenderContext may have undesired side
 * effects.
 */
public class RenderContext implements Cloneable {
	RenderingHints hints;
	AffineTransform usr2dev;
	Shape aoi;

	public RenderContext(AffineTransform usr2dev, Shape aoi, RenderingHints hints) {
		this.hints = hints;
		this.aoi = aoi;
		this.usr2dev = (AffineTransform) usr2dev.clone();
	}

	public RenderContext(AffineTransform usr2dev) {
		this(usr2dev, null, null);
	}

	public RenderContext(AffineTransform usr2dev, RenderingHints hints) {
		this(usr2dev, null, hints);
	}

	public RenderContext(AffineTransform usr2dev, Shape aoi) {
		this(usr2dev, aoi, null);
	}

	public RenderingHints getRenderingHints() {
		return hints;
	}

	public void setRenderingHints(RenderingHints hints) {
		this.hints = hints;
	}

	public void setTransform(AffineTransform newTransform) {
		usr2dev = (AffineTransform) newTransform.clone();
	}

	public void preConcatenateTransform(AffineTransform modTransform) {
		this.preConcetenateTransform(modTransform);
	}

	public void preConcetenateTransform(AffineTransform modTransform) {
		usr2dev.preConcatenate(modTransform);
	}

	public void concatenateTransform(AffineTransform modTransform) {
		this.concetenateTransform(modTransform);
	}

	public void concetenateTransform(AffineTransform modTransform) {
		usr2dev.concatenate(modTransform);
	}

	public AffineTransform getTransform() {
		return (AffineTransform) usr2dev.clone();
	}

	public void setAreaOfInterest(Shape newAoi) {
		aoi = newAoi;
	}

	public Shape getAreaOfInterest() {
		return aoi;
	}

	public Object clone() {
		return null;
	}
}