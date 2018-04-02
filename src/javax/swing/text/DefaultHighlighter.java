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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.util.Vector;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;

/**
 * Implements the Highlighter interfaces. Implements a simple highlight painter
 * that renders in a solid color.
 *
 * @author Timothy Prinzing
 * @see Highlighter
 */
public class DefaultHighlighter extends LayeredHighlighter {
	private final int FLAG_SPAN_INCLUSIVE_EXCLUSIVE = Spanned.SPAN_INCLUSIVE_EXCLUSIVE;

	public DefaultHighlighter() {
		drawsLayeredHighlights = true;
	}

	public void paint(Graphics g) {
		component.updateUI();
	}

	public void install(JTextComponent c) {
		component = c;
		component.setHighlighter(this);
		spannable = component.getSpannableAdAPI();
		removeAllHighlights();
	}

	public void deinstall(JTextComponent c) {
		component.setHighlighter(null);
		component = null;
	}

	public Object addHighlight(int startIdx, int endIdx, Highlighter.HighlightPainter p)
			throws BadLocationException {
		if (startIdx < 0) {
			throw new BadLocationException("Invalid start offset", startIdx);
		}
		if (endIdx < startIdx) {
			throw new BadLocationException("Invalid end offset", endIdx);
		}

		HighlightInfo i = (getDrawsLayeredHighlights()
				&& (p instanceof LayeredHighlighter.LayerPainter)) ? new LayeredHighlightInfo()
						: new HighlightInfo();
		i.p0 = new InfoPosition(startIdx);
		i.p1 = new InfoPosition(endIdx);
		i.painter = p;

		highlights.add(i);
		i.span = p.getCharacterStyleAdAPI();
		try {
			spannable.setSpan(i.span, startIdx, endIdx, FLAG_SPAN_INCLUSIVE_EXCLUSIVE);
		} catch (Throwable e) {// IndexOutOfBoundsException setSpan (0 … 1) ends
								// beyond length 0
			e.printStackTrace();
		}
		return i;
	}

	public void removeHighlight(Object tag) {
		if (tag instanceof HighlightInfo) {
			HighlightInfo hInfo = (HighlightInfo) tag;
			highlights.removeElement(hInfo);
			spannable.removeSpan(hInfo.span);
		}
	}

	public void removeAllHighlights() {
		if (spannable instanceof SpannableStringBuilder) {
			((SpannableStringBuilder) spannable).clearSpans();
			highlights.removeAllElements();
		} else {
			int size = highlights.size();
			for (int i = 0; i < size; i++) {
				HighlightInfo hInfo = highlights.elementAt(i);
				spannable.removeSpan(hInfo.span);
			}
			highlights.removeAllElements();
		}
	}

	public void changeHighlight(Object tag, int startIdx, int endIdx) throws BadLocationException {
		if (startIdx < 0) {
			throw new BadLocationException("Invalid start offset", startIdx);
		}
		if (endIdx < startIdx) {
			throw new BadLocationException("Invalid end offset", endIdx);
		}

		if (tag instanceof HighlightInfo) {
			HighlightInfo hInfo = (HighlightInfo) tag;
			hInfo.p0 = new InfoPosition(startIdx);
			hInfo.p1 = new InfoPosition(endIdx);
			spannable.setSpan(hInfo.span, startIdx, endIdx, FLAG_SPAN_INCLUSIVE_EXCLUSIVE);
		}
	}

	public Highlighter.Highlight[] getHighlights() {
		int size = highlights.size();
		if (size == 0) {
			return noHighlights;
		}
		Highlighter.Highlight[] h = new Highlighter.Highlight[size];
		highlights.copyInto(h);
		return h;
	}

	public void paintLayeredHighlights(Graphics g, int p0, int p1, Shape viewBounds,
			JTextComponent editor, View view) {
	}

	public void setDrawsLayeredHighlights(boolean newValue) {
		drawsLayeredHighlights = newValue;
	}

	public boolean getDrawsLayeredHighlights() {
		return drawsLayeredHighlights;
	}

	public static final LayeredHighlighter.LayerPainter DefaultPainter = null;// new
																				// DefaultHighlightPainter(null);

	public static class DefaultHighlightPainter extends LayeredHighlighter.LayerPainter {
		public DefaultHighlightPainter(Color c) {
			color = c;
		}

		public Color getColor() {
			return color;
		}

		public void paint(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c) {
		}

		public Shape paintLayer(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c,
				View view) {
			return null;
		}

		private Color color;

		public CharacterStyle getCharacterStyleAdAPI() {
			return new BackgroundColorSpan(color.toAndroid());
		}
	}

	class InfoPosition implements Position {
		int offset;

		InfoPosition(int offset) {
			this.offset = offset;
		}

		@Override
		public int getOffset() {
			return offset;
		}
	}

	class HighlightInfo implements Highlighter.Highlight {
		public int getStartOffset() {
			return p0.getOffset();
		}

		public int getEndOffset() {
			return p1.getOffset();
		}

		public Highlighter.HighlightPainter getPainter() {
			return painter;
		}

		public CharacterStyle getCharacterStyleAdAPI() {
			return span;
		}

		Position p0;
		Position p1;
		Highlighter.HighlightPainter painter;
		CharacterStyle span;
	}

	class LayeredHighlightInfo extends HighlightInfo {
		void union(Shape bounds) {
		}

		void paintLayeredHighlights(Graphics g, int p0, int p1, Shape viewBounds,
				JTextComponent editor, View view) {
		}

		int x;
		int y;
		int width;
		int height;
	}

	private final static Highlighter.Highlight[] noHighlights = new Highlighter.Highlight[0];
	private Vector<HighlightInfo> highlights = new Vector<HighlightInfo>();
	private JTextComponent component;
	private android.text.Spannable spannable;
	private boolean drawsLayeredHighlights;
}
