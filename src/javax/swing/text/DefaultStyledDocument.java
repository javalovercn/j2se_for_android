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

import hc.android.AndroidClassUtil;
import hc.android.SpanableContent;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

/**
 * A document that can be marked up with character and paragraph styles in a
 * manner similar to the Rich Text Format. The element structure for this
 * document represents style crossings for style runs. These style runs are
 * mapped into a paragraph element structure (which may reside in some other
 * structure). The style runs break at paragraph boundaries since logical styles
 * are assigned to paragraph boundaries.
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
public class DefaultStyledDocument extends AbstractDocument implements StyledDocument {
	public DefaultStyledDocument(Content c, StyleContext styles) {
		super(c, styles);
		if (data == null) {
			// 缺省采用
			data = new SpanableContent() {
				public Position createPosition(int offset) throws BadLocationException {
					if (marks == null) {
						marks = new Vector<PosRec>();
					}
					return new AndroidStickyPosition(offset);
				}
			};
		}
		listeningStyles = new Vector<Style>();
		buffer = new ElementBuffer(createDefaultRoot());
		Style defaultStyle = styles.getStyle(StyleContext.DEFAULT_STYLE);
		setLogicalStyle(0, defaultStyle);
	}

	public SpanableContent getSpanableContentAdAPI() {
		return (SpanableContent) data;
	}

	public DefaultStyledDocument(StyleContext styles) {
		this(null, styles);
	}

	public DefaultStyledDocument() {
		this(null, new StyleContext());
	}

	public Element getDefaultRootElement() {
		AndroidClassUtil.callEmptyMethod();
		return buffer.getRootElement();
	}

	protected void create(ElementSpec[] data) {
		AndroidClassUtil.callEmptyMethod();
	}

	protected void insert(int offset, ElementSpec[] data) throws BadLocationException {
		AndroidClassUtil.callEmptyMethod();
	}

	public void removeElement(Element elem) {
		AndroidClassUtil.callEmptyMethod();
	}

	public Style addStyle(String nm, Style parent) {
		StyleContext styles = (StyleContext) getAttributeContext();
		return styles.addStyle(nm, parent);
	}

	public void removeStyle(String nm) {
		StyleContext styles = (StyleContext) getAttributeContext();
		styles.removeStyle(nm);
	}

	public Style getStyle(String nm) {
		StyleContext styles = (StyleContext) getAttributeContext();
		return styles.getStyle(nm);
	}

	public Enumeration<?> getStyleNames() {
		return ((StyleContext) getAttributeContext()).getStyleNames();
	}

	public void setLogicalStyle(int pos, Style s) {
		AndroidClassUtil.callEmptyMethod();
	}

	public Style getLogicalStyle(int p) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setCharacterAttributes(int offset, int length, AttributeSet s, boolean replace) {
		// SimpleAttributeSet attributes = new SimpleAttributeSet();
		// StyleConstants.setForeground(attributes, c);
		// StyleConstants.setBold(attributes, bold);
		if (s == null) {
			return;
		}

		Content content = getContent();
		if (content instanceof SpanableContent) {
			SpanableContent spanContent = (SpanableContent) content;

			Enumeration enumAttr = s.getAttributeNames();
			while (enumAttr.hasMoreElements()) {
				Object attr = enumAttr.nextElement();

				Object spanObject = null;
				if (attr == StyleConstants.Foreground) {
					Object c = s.getAttribute(attr);
					if (c != null && c instanceof Color) {
						Color color = (Color) c;
						spanObject = new ForegroundColorSpan(color.toAndroid());
					}
				} else if (attr == StyleConstants.Background) {
					Object c = s.getAttribute(attr);
					if (c != null && c instanceof Color) {
						Color color = (Color) c;
						spanObject = new BackgroundColorSpan(color.toAndroid());
					}
				} else if (attr == StyleConstants.Bold) {
					Object c = s.getAttribute(attr);
					if (c != null && c instanceof Boolean) {
						Boolean blod = (Boolean) c;
						spanObject = new StyleSpan(blod ? Typeface.BOLD : Typeface.NORMAL);
					}
				}

				if (spanObject != null) {
					spanContent.spanBuilder.setSpan(spanObject, offset, offset + length,
							Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				}
			}
		}
	}

	public void setParagraphAttributes(int offset, int length, AttributeSet s, boolean replace) {
		AndroidClassUtil.callEmptyMethod();
	}

	public Element getParagraphElement(int pos) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public Element getCharacterElement(int pos) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	protected void insertUpdate(DefaultDocumentEvent chng, AttributeSet attr) {
		AndroidClassUtil.callEmptyMethod();
	}

	short createSpecsForInsertAfterNewline(Element paragraph, Element pParagraph,
			AttributeSet pattr, Vector<ElementSpec> parseBuffer, int offset, int endOffset) {
		AndroidClassUtil.callEmptyMethod();
		return ElementSpec.OriginateDirection;
	}

	protected void removeUpdate(DefaultDocumentEvent chng) {
		super.removeUpdate(chng);
		buffer.remove(chng.getOffset(), chng.getLength(), chng);
	}

	protected AbstractElement createDefaultRoot() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public Color getForeground(AttributeSet attr) {
		StyleContext styles = (StyleContext) getAttributeContext();
		return styles.getForeground(attr);
	}

	public Color getBackground(AttributeSet attr) {
		StyleContext styles = (StyleContext) getAttributeContext();
		return styles.getBackground(attr);
	}

	public Font getFont(AttributeSet attr) {
		StyleContext styles = (StyleContext) getAttributeContext();
		return styles.getFont(attr);
	}

	protected void styleChanged(Style style) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void addDocumentListener(DocumentListener listener) {
		AndroidClassUtil.callEmptyMethod();
		super.addDocumentListener(listener);
	}

	public void removeDocumentListener(DocumentListener listener) {
		AndroidClassUtil.callEmptyMethod();
		super.removeDocumentListener(listener);
	}

	ChangeListener createStyleChangeListener() {
		return new StyleChangeHandler(this);
	}

	ChangeListener createStyleContextChangeListener() {
		return new StyleContextChangeHandler(this);
	}

	void updateStylesListeningTo() {
		AndroidClassUtil.callEmptyMethod();
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
	}

	public static final int BUFFER_SIZE_DEFAULT = 4096;

	protected ElementBuffer buffer;

	private transient Vector<Style> listeningStyles;
	private transient ChangeListener styleChangeListener;
	private transient ChangeListener styleContextChangeListener;
	private transient ChangeUpdateRunnable updateRunnable;

	protected class SectionElement extends BranchElement {
		public SectionElement() {
			super(null, null);
		}

		public String getName() {
			return SectionElementName;
		}
	}

	public static class ElementSpec {
		public static final short StartTagType = 1;
		public static final short EndTagType = 2;
		public static final short ContentType = 3;
		public static final short JoinPreviousDirection = 4;
		public static final short JoinNextDirection = 5;
		public static final short OriginateDirection = 6;
		public static final short JoinFractureDirection = 7;

		public ElementSpec(AttributeSet a, short type) {
			this(a, type, null, 0, 0);
		}

		public ElementSpec(AttributeSet a, short type, int len) {
			this(a, type, null, 0, len);
		}

		public ElementSpec(AttributeSet a, short type, char[] txt, int offs, int len) {
			attr = a;
			this.type = type;
			this.data = txt;
			this.offs = offs;
			this.len = len;
			this.direction = OriginateDirection;
		}

		public void setType(short type) {
			this.type = type;
		}

		public short getType() {
			return type;
		}

		public void setDirection(short direction) {
			this.direction = direction;
		}

		public short getDirection() {
			return direction;
		}

		public AttributeSet getAttributes() {
			return attr;
		}

		public char[] getArray() {
			return data;
		}

		public int getOffset() {
			return offs;
		}

		public int getLength() {
			return len;
		}

		public String toString() {
			return "unknow";
		}

		private AttributeSet attr;
		private int len;
		private short type;
		private short direction;

		private int offs;
		private char[] data;
	}

	public class ElementBuffer implements Serializable {
		public ElementBuffer(Element root) {
			this.root = root;
			changes = new Vector<ElemChanges>();
			path = new Stack<ElemChanges>();
		}

		public Element getRootElement() {
			return root;
		}

		public void insert(int offset, int length, ElementSpec[] data, DefaultDocumentEvent de) {
			AndroidClassUtil.callEmptyMethod();
		}

		void create(int length, ElementSpec[] data, DefaultDocumentEvent de) {
			AndroidClassUtil.callEmptyMethod();
		}

		public void remove(int offset, int length, DefaultDocumentEvent de) {
			AndroidClassUtil.callEmptyMethod();
		}

		public void change(int offset, int length, DefaultDocumentEvent de) {
			AndroidClassUtil.callEmptyMethod();
		}

		protected void insertUpdate(ElementSpec[] data) {
			AndroidClassUtil.callEmptyMethod();
		}

		protected void removeUpdate() {
			AndroidClassUtil.callEmptyMethod();
		}

		protected void changeUpdate() {
			AndroidClassUtil.callEmptyMethod();
		}

		boolean split(int offs, int len) {
			AndroidClassUtil.callEmptyMethod();
			return false;
		}

		void endEdits(DefaultDocumentEvent de) {
			AndroidClassUtil.callEmptyMethod();
		}

		void beginEdits(int offset, int length) {
			AndroidClassUtil.callEmptyMethod();
		}

		void push(Element e, int index, boolean isFracture) {
			AndroidClassUtil.callEmptyMethod();
		}

		void push(Element e, int index) {
			push(e, index, false);
		}

		void pop() {
			AndroidClassUtil.callEmptyMethod();
		}

		void advance(int n) {
			pos += n;
		}

		void insertElement(ElementSpec es) {
			AndroidClassUtil.callEmptyMethod();
		}

		boolean removeElements(Element elem, int rmOffs0, int rmOffs1) {
			AndroidClassUtil.callEmptyMethod();
			return false;
		}

		boolean canJoin(Element e0, Element e1) {
			AndroidClassUtil.callEmptyMethod();
			return true;
		}

		Element join(Element p, Element left, Element right, int rmOffs0, int rmOffs1) {
			AndroidClassUtil.callEmptyMethod();
			return null;
		}

		public Element clone(Element parent, Element clonee) {
			AndroidClassUtil.callEmptyMethod();
			return null;
		}

		Element cloneAsNecessary(Element parent, Element clonee, int rmOffs0, int rmOffs1) {
			AndroidClassUtil.callEmptyMethod();
			return null;
		}

		void fracture(int depth) {
			AndroidClassUtil.callEmptyMethod();
		}

		void fractureFrom(ElemChanges[] changed, int startIndex, int endFractureIndex) {
			AndroidClassUtil.callEmptyMethod();
		}

		Element recreateFracturedElement(Element parent, Element toDuplicate) {
			AndroidClassUtil.callEmptyMethod();
			return null;
		}

		void fractureDeepestLeaf(ElementSpec[] specs) {
			AndroidClassUtil.callEmptyMethod();
		}

		void insertFirstContent(ElementSpec[] specs) {
			AndroidClassUtil.callEmptyMethod();
		}

		Element root;
		transient int pos;
		transient int offset;
		transient int length;
		transient int endOffset;
		transient Vector<ElemChanges> changes;
		transient Stack<ElemChanges> path;
		transient boolean insertOp;

		transient boolean recreateLeafs;

		transient ElemChanges[] insertPath;
		transient boolean createdFracture;
		transient Element fracturedParent;
		transient Element fracturedChild;
		transient boolean offsetLastIndex;
		transient boolean offsetLastIndexOnReplace;

		class ElemChanges {

			ElemChanges(Element parent, int index, boolean isFracture) {
				this.parent = parent;
				this.index = index;
				this.isFracture = isFracture;
				added = new Vector<Element>();
				removed = new Vector<Element>();
			}

			public String toString() {
				return "added: " + added + "\nremoved: " + removed + "\n";
			}

			Element parent;
			int index;
			Vector<Element> added;
			Vector<Element> removed;
			boolean isFracture;
		}

	}

	public static class AttributeUndoableEdit extends AbstractUndoableEdit {
		public AttributeUndoableEdit(Element element, AttributeSet newAttributes,
				boolean isReplacing) {
			super();
			this.element = element;
			this.newAttributes = newAttributes;
			this.isReplacing = isReplacing;
			copy = element.getAttributes().copyAttributes();
		}

		public void redo() throws CannotRedoException {
			super.redo();
			AndroidClassUtil.callEmptyMethod();
		}

		public void undo() throws CannotUndoException {
			super.undo();
			AndroidClassUtil.callEmptyMethod();
		}

		protected AttributeSet newAttributes;
		protected AttributeSet copy;
		protected boolean isReplacing;
		protected Element element;
	}

	static class StyleChangeUndoableEdit extends AbstractUndoableEdit {
		public StyleChangeUndoableEdit(AbstractElement element, Style newStyle) {
			super();
			this.element = element;
			this.newStyle = newStyle;
			oldStyle = element.getResolveParent();
		}

		public void redo() throws CannotRedoException {
			super.redo();
			element.setResolveParent(newStyle);
		}

		public void undo() throws CannotUndoException {
			super.undo();
			element.setResolveParent(oldStyle);
		}

		protected AbstractElement element;
		protected Style newStyle;
		protected AttributeSet oldStyle;
	}

	abstract static class AbstractChangeHandler implements ChangeListener {
		private class DocReference extends WeakReference<DefaultStyledDocument> {

			DocReference(DefaultStyledDocument d, ReferenceQueue<DefaultStyledDocument> q) {
				super(d, q);
			}

			ChangeListener getListener() {
				return AbstractChangeHandler.this;
			}
		}

		private final static Map<Class, ReferenceQueue<DefaultStyledDocument>> queueMap = new HashMap<Class, ReferenceQueue<DefaultStyledDocument>>();

		private DocReference doc;

		AbstractChangeHandler(DefaultStyledDocument d) {
			Class c = getClass();
			ReferenceQueue<DefaultStyledDocument> q;
			synchronized (queueMap) {
				q = queueMap.get(c);
				if (q == null) {
					q = new ReferenceQueue<DefaultStyledDocument>();
					queueMap.put(c, q);
				}
			}
			doc = new DocReference(d, q);
		}

		static List<ChangeListener> getStaleListeners(ChangeListener l) {
			List<ChangeListener> staleListeners = new ArrayList<ChangeListener>();
			ReferenceQueue<DefaultStyledDocument> q = queueMap.get(l.getClass());

			if (q != null) {
				DocReference r;
				synchronized (q) {
					while ((r = (DocReference) q.poll()) != null) {
						staleListeners.add(r.getListener());
					}
				}
			}

			return staleListeners;
		}

		public void stateChanged(ChangeEvent e) {
			DefaultStyledDocument d = doc.get();
			if (d != null) {
				fireStateChanged(d, e);
			}
		}

		abstract void fireStateChanged(DefaultStyledDocument d, ChangeEvent e);
	}

	static class StyleChangeHandler extends AbstractChangeHandler {

		StyleChangeHandler(DefaultStyledDocument d) {
			super(d);
		}

		void fireStateChanged(DefaultStyledDocument d, ChangeEvent e) {
			Object source = e.getSource();
			if (source instanceof Style) {
				d.styleChanged((Style) source);
			} else {
				d.styleChanged(null);
			}
		}
	}

	static class StyleContextChangeHandler extends AbstractChangeHandler {

		StyleContextChangeHandler(DefaultStyledDocument d) {
			super(d);
		}

		void fireStateChanged(DefaultStyledDocument d, ChangeEvent e) {
			d.updateStylesListeningTo();
		}
	}

	class ChangeUpdateRunnable implements Runnable {
		boolean isPending = false;

		public void run() {
			synchronized (this) {
				isPending = false;
			}

			try {
				writeLock();
				DefaultDocumentEvent dde = new DefaultDocumentEvent(0, getLength(),
						DocumentEvent.EventType.CHANGE);
				dde.end();
				fireChangedUpdate(dde);
			} finally {
				writeUnlock();
			}
		}
	}

	transient Vector<PosRec> marks;

	final class AndroidStickyPosition implements Position {

		AndroidStickyPosition(int offset) {
			rec = new PosRec(offset);
			marks.addElement(rec);
		}

		public int getOffset() {
			return rec.offset;
		}

		protected void finalize() throws Throwable {
			rec.unused = true;
		}

		public String toString() {
			return Integer.toString(getOffset());
		}

		PosRec rec;
	}

	final class PosRec {

		PosRec(int offset) {
			this.offset = offset;
		}

		int offset;
		boolean unused;
	}
}
