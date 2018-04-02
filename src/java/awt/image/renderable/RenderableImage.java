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

import java.util.Vector;
import java.awt.RenderingHints;
import java.awt.image.*;

/**
 * A RenderableImage is a common interface for rendering-independent images (a
 * notion which subsumes resolution independence). That is, images which are
 * described and have operations applied to them independent of any specific
 * rendering of the image. For example, a RenderableImage can be rotated and
 * cropped in resolution-independent terms. Then, it can be rendered for various
 * specific contexts, such as a draft preview, a high-quality screen display, or
 * a printer, each in an optimal fashion.
 *
 * <p>
 * A RenderedImage is returned from a RenderableImage via the createRendering()
 * method, which takes a RenderContext. The RenderContext specifies how the
 * RenderedImage should be constructed. Note that it is not possible to extract
 * pixels directly from a RenderableImage.
 *
 * <p>
 * The createDefaultRendering() and createScaledRendering() methods are
 * convenience methods that construct an appropriate RenderContext internally.
 * All of the rendering methods may return a reference to a previously produced
 * rendering.
 */
public interface RenderableImage {
	static final String HINTS_OBSERVED = "HINTS_OBSERVED";

	Vector<RenderableImage> getSources();

	Object getProperty(String name);

	String[] getPropertyNames();

	boolean isDynamic();

	float getWidth();

	float getHeight();

	float getMinX();

	float getMinY();

	RenderedImage createScaledRendering(int w, int h, RenderingHints hints);

	RenderedImage createDefaultRendering();

	RenderedImage createRendering(RenderContext renderContext);
}
