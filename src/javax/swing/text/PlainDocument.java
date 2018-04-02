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

import hc.android.SpanableContent;

import java.util.Vector;

/**
 * A plain document that maintains no character attributes. The default element
 * structure for this document is a map of the lines in the text. The Element
 * returned by getDefaultRootElement is a map of the lines, and each child
 * element represents a line. This model does not maintain any character level
 * attributes, but each line can be tagged with an arbitrary set of attributes.
 * Line to offset, and offset to line translations can be quickly performed
 * using the default root element. The structure information of the
 * DocumentEvent's fired by edits will indicate the line structure changes.
 * <p>
 * The default content storage management is performed by a gapped buffer
 * implementation (GapContent). It supports editing reasonably large documents
 * with good efficiency when the edits are contiguous or clustered, as is
 * typical.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Timothy Prinzing
 * @see Document
 * @see AbstractDocument
 */
public class PlainDocument extends AbstractDocument {

	public static final String tabSizeAttribute = "tabSize";
	public static final String lineLimitAttribute = "lineLimit";

	public PlainDocument() {
		this(new SpanableContent());
	}

	public PlainDocument(Content c) {
		super(c);
		putProperty(tabSizeAttribute, Integer.valueOf(8));
		defaultRoot = createDefaultRoot();
	}

	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		Object filterNewlines = getProperty("filterNewlines");
		if ((filterNewlines instanceof Boolean) && filterNewlines.equals(Boolean.TRUE)) {
			if ((str != null) && (str.indexOf('\n') >= 0)) {
				StringBuilder filtered = new StringBuilder(str);
				int n = filtered.length();
				for (int i = 0; i < n; i++) {
					if (filtered.charAt(i) == '\n') {
						filtered.setCharAt(i, ' ');
					}
				}
				str = filtered.toString();
			}
		}
		super.insertString(offs, str, a);
	}

	public Element getDefaultRootElement() {
		return defaultRoot;
	}

	protected AbstractElement createDefaultRoot() {
		BranchElement map = (BranchElement) createBranchElement(null, null);
		Element line = createLeafElement(map, null, 0, 1);
		Element[] lines = new Element[1];
		lines[0] = line;
		map.replace(0, 0, lines);
		return map;
	}

	public Element getParagraphElement(int pos) {
		Element lineMap = getDefaultRootElement();
		return lineMap.getElement(lineMap.getElementIndex(pos));
	}

	protected void insertUpdate(DefaultDocumentEvent chng, AttributeSet attr) {
	}

	protected void removeUpdate(DefaultDocumentEvent chng) {
	}

	private void insertComposedTextUpdate(DefaultDocumentEvent chng, AttributeSet attr) {
		added.removeAllElements();
		BranchElement lineMap = (BranchElement) getDefaultRootElement();
		int offset = chng.getOffset();
		int length = chng.getLength();
		int index = lineMap.getElementIndex(offset);
		Element elem = lineMap.getElement(index);
		int elemStart = elem.getStartOffset();
		int elemEnd = elem.getEndOffset();
		BranchElement[] abelem = new BranchElement[1];
		abelem[0] = (BranchElement) createBranchElement(lineMap, null);
		Element[] relem = new Element[1];
		relem[0] = elem;
		if (elemStart != offset)
			added.addElement(createLeafElement(abelem[0], null, elemStart, offset));
		added.addElement(createLeafElement(abelem[0], attr, offset, offset + length));
		if (elemEnd != offset + length)
			added.addElement(createLeafElement(abelem[0], null, offset + length, elemEnd));
		Element[] alelem = new Element[added.size()];
		added.copyInto(alelem);
		ElementEdit ee = new ElementEdit(lineMap, index, relem, abelem);
		chng.addEdit(ee);

		abelem[0].replace(0, 0, alelem);
		lineMap.replace(index, 1, abelem);
	}

	private AbstractElement defaultRoot;
	private Vector<Element> added = new Vector<Element>();
	private Vector<Element> removed = new Vector<Element>();
	private transient Segment s;
}
