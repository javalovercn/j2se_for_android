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

import java.awt.Graphics;
import java.awt.Shape;
import javax.swing.text.Position.Bias;

/**
 * View of a simple line-wrapping paragraph that supports multiple fonts,
 * colors, components, icons, etc. It is basically a vertical box with a margin
 * around it. The contents of the box are a bunch of rows which are special
 * horizontal boxes. This view creates a collection of views that represent the
 * child elements of the paragraph element. Each of these views are placed into
 * a row directly if they will fit, otherwise the <code>breakView</code> method
 * is called to try and carve the view into pieces that fit.
 *
 * @author Timothy Prinzing
 * @author Scott Violet
 * @author Igor Kushnirskiy
 * @see View
 */
public class ParagraphView extends View {

	// extends FlowView implements TabExpander

	public ParagraphView(Element elem) {
		super(elem);
	}

	@Override
	public float getPreferredSpan(int axis) {
		return 0;
	}

	@Override
	public void paint(Graphics g, Shape allocation) {

	}

	@Override
	public Shape modelToView(int pos, Shape a, Bias b) throws BadLocationException {
		return null;
	}

	@Override
	public int viewToModel(float x, float y, Shape a, Bias[] biasReturn) {
		return 0;
	}
}
