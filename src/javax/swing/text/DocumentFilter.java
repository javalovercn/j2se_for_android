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
 * <code>DocumentFilter</code>, as the name implies, is a filter for the
 * <code>Document</code> mutation methods. When a <code>Document</code>
 * containing a <code>DocumentFilter</code> is modified (either through
 * <code>insert</code> or <code>remove</code>), it forwards the appropriate
 * method invocation to the <code>DocumentFilter</code>. The default
 * implementation allows the modification to occur. Subclasses can filter the
 * modifications by conditionally invoking methods on the superclass, or
 * invoking the necessary methods on the passed in <code>FilterBypass</code>.
 * Subclasses should NOT call back into the Document for the modification
 * instead call into the superclass or the <code>FilterBypass</code>.
 * <p>
 * When <code>remove</code> or <code>insertString</code> is invoked on the
 * <code>DocumentFilter</code>, the <code>DocumentFilter</code> may callback
 * into the <code>FilterBypass</code> multiple times, or for different regions,
 * but it should not callback into the <code>FilterBypass</code> after returning
 * from the <code>remove</code> or <code>insertString</code> method.
 * <p>
 * By default, text related document mutation methods such as
 * <code>insertString</code>, <code>replace</code> and <code>remove</code> in
 * <code>AbstractDocument</code> use <code>DocumentFilter</code> when available,
 * and <code>Element</code> related mutation methods such as
 * <code>create</code>, <code>insert</code> and <code>removeElement</code> in
 * <code>DefaultStyledDocument</code> do not use <code>DocumentFilter</code>. If
 * a method doesn't follow these defaults, this must be explicitly stated in the
 * method documentation.
 *
 * @see javax.swing.text.Document
 * @see javax.swing.text.AbstractDocument
 * @see javax.swing.text.DefaultStyledDocument
 *
 * @since 1.4
 */
public class DocumentFilter {
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
		fb.remove(offset, length);
	}

	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
			throws BadLocationException {
		fb.insertString(offset, string, attr);
	}

	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
			throws BadLocationException {
		fb.replace(offset, length, text, attrs);
	}

	public static abstract class FilterBypass {
		public abstract Document getDocument();

		public abstract void remove(int offset, int length) throws BadLocationException;

		public abstract void insertString(int offset, String string, AttributeSet attr)
				throws BadLocationException;

		public abstract void replace(int offset, int length, String string, AttributeSet attrs)
				throws BadLocationException;
	}
}
