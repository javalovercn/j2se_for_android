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

import android.text.style.CharacterStyle;

/**
 * An interface for an object that allows one to mark up the background with
 * colored areas.
 *
 * @author Timothy Prinzing
 */
public interface Highlighter {
	public void install(JTextComponent c);

	public void deinstall(JTextComponent c);

	public void paint(Graphics g);

	public Object addHighlight(int p0, int p1, HighlightPainter p) throws BadLocationException;

	public void removeHighlight(Object tag);

	public void removeAllHighlights();

	public void changeHighlight(Object tag, int p0, int p1) throws BadLocationException;

	public Highlight[] getHighlights();

	public interface HighlightPainter {
		public void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent c);

		public CharacterStyle getCharacterStyleAdAPI();
	}

	public interface Highlight {
		public int getStartOffset();

		public int getEndOffset();

		public HighlightPainter getPainter();
	}

};