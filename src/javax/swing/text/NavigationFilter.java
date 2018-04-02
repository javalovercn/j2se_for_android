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

/**
 * <code>NavigationFilter</code> can be used to restrict where the cursor can be
 * positioned. When the default cursor positioning actions attempt to reposition
 * the cursor they will call into the <code>NavigationFilter</code>, assuming
 * the <code>JTextComponent</code> has a non-null <code>NavigationFilter</code>
 * set. In this manner the <code>NavigationFilter</code> can effectively
 * restrict where the cursor can be positioned. Similarly
 * <code>DefaultCaret</code> will call into the <code>NavigationFilter</code>
 * when the user is changing the selection to further restrict where the cursor
 * can be positioned.
 * <p>
 * Subclasses can conditionally call into supers implementation to restrict
 * where the cursor can be placed, or call directly into the
 * <code>FilterBypass</code>.
 *
 * @see javax.swing.text.Caret
 * @see javax.swing.text.DefaultCaret
 * @see javax.swing.text.View
 *
 * @since 1.4
 */
public class NavigationFilter {
	public void setDot(FilterBypass fb, int dot, Position.Bias bias) {
		fb.setDot(dot, bias);
	}

	public void moveDot(FilterBypass fb, int dot, Position.Bias bias) {
		fb.moveDot(dot, bias);
	}

	public int getNextVisualPositionFrom(JTextComponent text, int pos, Position.Bias bias,
			int direction, Position.Bias[] biasRet) throws BadLocationException {
		return text.getUI().getNextVisualPositionFrom(text, pos, bias, direction, biasRet);
	}

	public static abstract class FilterBypass {
		public abstract Caret getCaret();

		public abstract void setDot(int dot, Position.Bias bias);

		public abstract void moveDot(int dot, Position.Bias bias);
	}
}
