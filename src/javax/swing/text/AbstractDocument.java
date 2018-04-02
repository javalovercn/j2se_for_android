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
import hc.android.UnimplementManager;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Hashtable;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.tree.TreeNode;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;

import android.widget.EditText;

/**
 * An implementation of the document interface to serve as a basis for
 * implementing various kinds of documents. At this level there is very little
 * policy, so there is a corresponding increase in difficulty of use.
 * <p>
 * This class implements a locking mechanism for the document. It allows
 * multiple readers or one writer, and writers must wait until all observers of
 * the document have been notified of a previous change before beginning another
 * mutation to the document. The read lock is acquired and released using the
 * <code>render</code> method. A write lock is aquired by the methods that
 * mutate the document, and are held for the duration of the method call.
 * Notification is done on the thread that produced the mutation, and the thread
 * has full read access to the document for the duration of the notification,
 * but other readers are kept out until the notification has finished. The
 * notification is a beans event notification which does not allow any further
 * mutations until all listeners have been notified.
 * <p>
 * Any models subclassed from this class and used in conjunction with a text
 * component that has a look and feel implementation that is derived from
 * BasicTextUI may be safely updated asynchronously, because all access to the
 * View hierarchy is serialized by BasicTextUI if the document is of type
 * <code>AbstractDocument</code>. The locking assumes that an independent thread
 * will access the View hierarchy only from the DocumentListener methods, and
 * that there will be only one event thread active at a time.
 * <p>
 * If concurrency support is desired, there are the following additional
 * implications. The code path for any DocumentListener implementation and any
 * UndoListener implementation must be threadsafe, and not access the component
 * lock if trying to be safe from deadlocks. The <code>repaint</code> and
 * <code>revalidate</code> methods on JComponent are safe.
 * <p>
 * AbstractDocument models an implied break at the end of the document. Among
 * other things this allows you to position the caret after the last character.
 * As a result of this, <code>getLength</code> returns one less than the length
 * of the Content. If you create your own Content, be sure and initialize it to
 * have an additional character. Refer to StringContent and GapContent for
 * examples of this. Another implication of this is that Elements that model the
 * implied end character will have an endOffset == (getLength() + 1). For
 * example, in DefaultStyledDocument
 * <code>getParagraphElement(getLength()).getEndOffset() == getLength() + 1
 * </code>.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Timothy Prinzing
 */
public abstract class AbstractDocument implements Document, Serializable {
	EditText edittext;

	protected AbstractDocument(Content data) {
		this(data, null);
	}

	public void setEditTextAdAPI(EditText edittext) {
		this.edittext = edittext;
	}

	protected AbstractDocument(Content data, AttributeContext context) {
		this.data = data;
		this.context = context;
		bidiRoot = new BidiRootElement();
	}

	public Dictionary<Object, Object> getDocumentProperties() {
		if (documentProperties == null) {
			documentProperties = new Hashtable<Object, Object>(2);
		}
		return documentProperties;
	}

	public void setDocumentProperties(Dictionary<Object, Object> x) {
		documentProperties = x;
	}

	protected void fireInsertUpdate(DocumentEvent e) {
		DocumentListener[] listeners = getDocumentListeners();
		if (listeners != null) {
			for (int i = 0; i < listeners.length; i++) {
				listeners[i].insertUpdate(e);
			}
		}
	}

	protected void fireChangedUpdate(DocumentEvent e) {
		UnimplementManager.printFireChangedUpdate();

		DocumentListener[] listeners = getDocumentListeners();
		if (listeners != null) {
			for (int i = 0; i < listeners.length; i++) {
				listeners[i].changedUpdate(e);
			}
		}
	}

	protected void fireRemoveUpdate(DocumentEvent e) {
		DocumentListener[] listeners = getDocumentListeners();
		if (listeners != null) {
			for (int i = 0; i < listeners.length; i++) {
				listeners[i].removeUpdate(e);
			}
		}
	}

	protected void fireUndoableEditUpdate(UndoableEditEvent e) {
		UndoableEditListener[] listeners = getUndoableEditListeners();
		if (listeners != null) {
			for (int i = 0; i < listeners.length; i++) {
				listeners[i].undoableEditHappened(e);
			}
		}
	}

	public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
		return listenerList.getListeners(listenerType);
	}

	public int getAsynchronousLoadPriority() {
		AndroidClassUtil.callEmptyMethod();
		return -1;
	}

	public void setAsynchronousLoadPriority(int p) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setDocumentFilter(DocumentFilter filter) {
		documentFilter = filter;
	}

	public DocumentFilter getDocumentFilter() {
		return documentFilter;
	}

	public void render(Runnable r) {
		AndroidClassUtil.callEmptyMethod();
	}

	public int getLength() {
		return data.length();// - 1;
	}

	public void addDocumentListener(DocumentListener listener) {
		UnimplementManager.printFireChangedUpdate();

		listenerList.add(DocumentListener.class, listener);
	}

	public void removeDocumentListener(DocumentListener listener) {
		listenerList.remove(DocumentListener.class, listener);
	}

	public DocumentListener[] getDocumentListeners() {
		return listenerList.getListeners(DocumentListener.class);
	}

	public void addUndoableEditListener(UndoableEditListener listener) {
		listenerList.add(UndoableEditListener.class, listener);
	}

	public void removeUndoableEditListener(UndoableEditListener listener) {
		listenerList.remove(UndoableEditListener.class, listener);
	}

	public UndoableEditListener[] getUndoableEditListeners() {
		return listenerList.getListeners(UndoableEditListener.class);
	}

	public final Object getProperty(Object key) {
		return getDocumentProperties().get(key);
	}

	public final void putProperty(Object key, Object value) {
		getDocumentProperties().put(key, value);
	}

	public void remove(int offs, int len) throws BadLocationException {
		data.remove(offs, len);
	}

	// void handleRemove(int offs, int len) throws BadLocationException {
	// }

	public void replace(int offset, int length, String text, AttributeSet attrs)
			throws BadLocationException {
		data.remove(offset, length);
		insertString(offset, text, attrs);
	}

	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		data.insertString(offs, str);
		if (a != null && this instanceof DefaultStyledDocument) {
			((DefaultStyledDocument) this).setCharacterAttributes(offs, str.length(), a, true);
		}
	}

	// void handleInsertString(int offs, String str, AttributeSet a)
	// throws BadLocationException {
	// }

	public String getText(int offset, int length) throws BadLocationException {
		return data.getString(offset, length);
	}

	public void getText(int offset, int length, Segment txt) throws BadLocationException {
		data.getChars(offset, length, txt);
	}

	public synchronized Position createPosition(int offs) throws BadLocationException {
		return data.createPosition(offs);
	}

	public final Position getStartPosition() {
		AndroidClassUtil.callEmptyMethod();
		return new Position() {
			@Override
			public int getOffset() {
				return 0;
			}
		};
	}

	public final Position getEndPosition() {
		AndroidClassUtil.callEmptyMethod();
		return new Position() {
			@Override
			public int getOffset() {
				return 0;
			}
		};
	}

	public Element[] getRootElements() {
		AndroidClassUtil.callEmptyMethod();
		Element[] elems = new Element[2];
		return elems;
	}

	public abstract Element getDefaultRootElement();

	private DocumentFilter.FilterBypass getFilterBypass() {
		if (filterBypass == null) {
			filterBypass = new DefaultFilterBypass();
		}
		return filterBypass;
	}

	public Element getBidiRootElement() {
		AndroidClassUtil.callEmptyMethod();
		return bidiRoot;
	}

	boolean isLeftToRight(int p0, int p1) {
		AndroidClassUtil.callEmptyMethod();
		return true;
	}

	public abstract Element getParagraphElement(int pos);

	protected final AttributeContext getAttributeContext() {
		return context;
	}

	protected void insertUpdate(DefaultDocumentEvent chng, AttributeSet attr) {
		AndroidClassUtil.callEmptyMethod();
	}

	protected void removeUpdate(DefaultDocumentEvent chng) {
		AndroidClassUtil.callEmptyMethod();
	}

	protected void postRemoveUpdate(DefaultDocumentEvent chng) {
		AndroidClassUtil.callEmptyMethod();
	}

	void updateBidi(DefaultDocumentEvent chng) {
		AndroidClassUtil.callEmptyMethod();
	}

	private byte[] calculateBidiLevels(int firstPStart, int lastPEnd) {
		return null;
	}

	public void dump(PrintStream out) {
		AndroidClassUtil.callEmptyMethod();
	}

	protected final Content getContent() {
		return data;
	}

	protected Element createLeafElement(Element parent, AttributeSet a, int p0, int p1) {
		AndroidClassUtil.callEmptyMethod();
		return new LeafElement(parent, a, p0, p1);
	}

	protected Element createBranchElement(Element parent, AttributeSet a) {
		AndroidClassUtil.callEmptyMethod();
		return new BranchElement(parent, a);
	}

	protected synchronized final Thread getCurrentWriter() {
		return currWriter;
	}

	protected synchronized final void writeLock() {
	}

	protected synchronized final void writeUnlock() {
	}

	public synchronized final void readLock() {
	}

	public synchronized final void readUnlock() {
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
	}

	private transient int numReaders;
	private transient Thread currWriter;
	private transient int numWriters;
	private transient boolean notifyingListeners;
	private static Boolean defaultI18NProperty;
	private Dictionary<Object, Object> documentProperties = null;
	protected EventListenerList listenerList = new EventListenerList();
	protected Content data;
	private AttributeContext context;
	private transient BranchElement bidiRoot;
	private DocumentFilter documentFilter;
	private transient DocumentFilter.FilterBypass filterBypass;
	private static final String BAD_LOCK_STATE = "document lock failure";
	protected static final String BAD_LOCATION = "document location failure";
	public static final String ParagraphElementName = "paragraph";
	public static final String ContentElementName = "content";
	public static final String SectionElementName = "section";
	public static final String BidiElementName = "bidi level";
	public static final String ElementNameAttribute = "$ename";
	static final String I18NProperty = "i18n";
	static final Object MultiByteProperty = "multiByte";
	static final String AsyncLoadPriority = "load priority";

	public interface Content {
		public Position createPosition(int offset) throws BadLocationException;

		public int length();

		public UndoableEdit insertString(int where, String str) throws BadLocationException;

		public UndoableEdit remove(int where, int nitems) throws BadLocationException;

		public String getString(int where, int len) throws BadLocationException;

		public void getChars(int where, int len, Segment txt) throws BadLocationException;
	}

	public interface AttributeContext {

		public AttributeSet addAttribute(AttributeSet old, Object name, Object value);

		public AttributeSet addAttributes(AttributeSet old, AttributeSet attr);

		public AttributeSet removeAttribute(AttributeSet old, Object name);

		public AttributeSet removeAttributes(AttributeSet old, Enumeration<?> names);

		public AttributeSet removeAttributes(AttributeSet old, AttributeSet attrs);

		public AttributeSet getEmptySet();

		public void reclaim(AttributeSet a);
	}

	public abstract class AbstractElement
			implements Element, MutableAttributeSet, Serializable, TreeNode {

		public AbstractElement(Element parent, AttributeSet a) {
		}

		private final void indent(PrintWriter out, int n) {
		}

		public void dump(PrintStream psOut, int indentAmount) {
		}

		public int getAttributeCount() {
			return attributes.getAttributeCount();
		}

		public boolean isDefined(Object attrName) {
			return attributes.isDefined(attrName);
		}

		public boolean isEqual(AttributeSet attr) {
			return attributes.isEqual(attr);
		}

		public AttributeSet copyAttributes() {
			return attributes.copyAttributes();
		}

		public Object getAttribute(Object attrName) {
			return null;
		}

		public Enumeration<?> getAttributeNames() {
			return attributes.getAttributeNames();
		}

		public boolean containsAttribute(Object name, Object value) {
			return attributes.containsAttribute(name, value);
		}

		public boolean containsAttributes(AttributeSet attrs) {
			return attributes.containsAttributes(attrs);
		}

		public AttributeSet getResolveParent() {
			return null;
		}

		public void addAttribute(Object name, Object value) {
		}

		public void addAttributes(AttributeSet attr) {
		}

		public void removeAttribute(Object name) {
		}

		public void removeAttributes(Enumeration<?> names) {
		}

		public void removeAttributes(AttributeSet attrs) {
		}

		public void setResolveParent(AttributeSet parent) {
		}

		private final void checkForIllegalCast() {
		}

		public Document getDocument() {
			return AbstractDocument.this;
		}

		public Element getParentElement() {
			return parent;
		}

		public AttributeSet getAttributes() {
			return this;
		}

		public String getName() {
			return null;
		}

		public abstract int getStartOffset();

		public abstract int getEndOffset();

		public abstract Element getElement(int index);

		public abstract int getElementCount();

		public abstract int getElementIndex(int offset);

		public abstract boolean isLeaf();

		public TreeNode getChildAt(int childIndex) {
			return (TreeNode) getElement(childIndex);
		}

		public int getChildCount() {
			return getElementCount();
		}

		public TreeNode getParent() {
			return (TreeNode) getParentElement();
		}

		public int getIndex(TreeNode node) {
			for (int counter = getChildCount() - 1; counter >= 0; counter--)
				if (getChildAt(counter) == node)
					return counter;
			return -1;
		}

		public abstract boolean getAllowsChildren();

		public abstract Enumeration children();

		private void writeObject(ObjectOutputStream s) throws IOException {
		}

		private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
		}

		private Element parent;
		private transient AttributeSet attributes;
	}

	public class BranchElement extends AbstractElement {

		public BranchElement(Element parent, AttributeSet a) {
			super(parent, a);
			children = new AbstractElement[1];
			nchildren = 0;
			lastIndex = -1;
		}

		public Element positionToElement(int pos) {
			return null;
		}

		public void replace(int offset, int length, Element[] elems) {
		}

		public String toString() {
			return "";
		}

		public String getName() {
			String nm = super.getName();
			return nm;
		}

		public int getStartOffset() {
			return children[0].getStartOffset();
		}

		public int getEndOffset() {
			return 0;
		}

		public Element getElement(int index) {
			return null;
		}

		public int getElementCount() {
			return nchildren;
		}

		public int getElementIndex(int offset) {
			return 0;
		}

		public boolean isLeaf() {
			return false;
		}

		public boolean getAllowsChildren() {
			return true;
		}

		public Enumeration children() {
			return null;
		}

		private AbstractElement[] children;
		private int nchildren;
		private int lastIndex;
	}

	public class LeafElement extends AbstractElement {

		public LeafElement(Element parent, AttributeSet a, int offs0, int offs1) {
			super(parent, a);
		}

		public String toString() {
			return "";
		}

		public int getStartOffset() {
			return p0.getOffset();
		}

		public int getEndOffset() {
			return p1.getOffset();
		}

		public String getName() {
			String nm = super.getName();
			return nm;
		}

		public int getElementIndex(int pos) {
			return 0;
		}

		public Element getElement(int index) {
			return null;
		}

		public int getElementCount() {
			return 0;
		}

		public boolean isLeaf() {
			return true;
		}

		public boolean getAllowsChildren() {
			return false;
		}

		public Enumeration children() {
			return null;
		}

		private void writeObject(ObjectOutputStream s) throws IOException {
		}

		private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
		}

		private transient Position p0;
		private transient Position p1;
	}

	class BidiRootElement extends BranchElement {

		BidiRootElement() {
			super(null, null);
		}

		public String getName() {
			return "bidi root";
		}
	}

	class BidiElement extends LeafElement {

		BidiElement(Element parent, int start, int end, int level) {
			super(parent, null, start, end);
		}

		public String getName() {
			return BidiElementName;
		}

		int getLevel() {
			return 0;
		}

		boolean isLeftToRight() {
			return ((getLevel() % 2) == 0);
		}
	}

	public class DefaultDocumentEvent extends CompoundEdit implements DocumentEvent {

		public DefaultDocumentEvent(int offs, int len, DocumentEvent.EventType type) {
			super();
			offset = offs;
			length = len;
			this.type = type;
		}

		public String toString() {
			return edits.toString();
		}

		public boolean addEdit(UndoableEdit anEdit) {
			return false;
		}

		public void redo() throws CannotRedoException {
		}

		public void undo() throws CannotUndoException {
		}

		public boolean isSignificant() {
			return true;
		}

		public String getPresentationName() {
			return null;
		}

		public String getUndoPresentationName() {
			return null;
		}

		public String getRedoPresentationName() {
			return "";
		}

		public DocumentEvent.EventType getType() {
			return type;
		}

		public int getOffset() {
			return offset;
		}

		public int getLength() {
			return length;
		}

		public Document getDocument() {
			return AbstractDocument.this;
		}

		public DocumentEvent.ElementChange getChange(Element elem) {
			return null;
		}

		private int offset;
		private int length;
		private Hashtable<Element, ElementChange> changeLookup;
		private DocumentEvent.EventType type;
	}

	class UndoRedoDocumentEvent implements DocumentEvent {
		private DefaultDocumentEvent src = null;
		private boolean isUndo;
		private EventType type = null;

		public UndoRedoDocumentEvent(DefaultDocumentEvent src, boolean isUndo) {
			this.src = src;
			this.isUndo = isUndo;
		}

		public DefaultDocumentEvent getSource() {
			return src;
		}

		public int getOffset() {
			return src.getOffset();
		}

		public int getLength() {
			return src.getLength();
		}

		public Document getDocument() {
			return src.getDocument();
		}

		public DocumentEvent.EventType getType() {
			return type;
		}

		public DocumentEvent.ElementChange getChange(Element elem) {
			return src.getChange(elem);
		}
	}

	public static class ElementEdit extends AbstractUndoableEdit
			implements DocumentEvent.ElementChange {

		public ElementEdit(Element e, int index, Element[] removed, Element[] added) {
			super();
			this.e = e;
			this.index = index;
			this.removed = removed;
			this.added = added;
		}

		public Element getElement() {
			return e;
		}

		public int getIndex() {
			return index;
		}

		public Element[] getChildrenRemoved() {
			return removed;
		}

		public Element[] getChildrenAdded() {
			return added;
		}

		public void redo() throws CannotRedoException {
		}

		public void undo() throws CannotUndoException {
		}

		private Element e;
		private int index;
		private Element[] removed;
		private Element[] added;
	}

	private class DefaultFilterBypass extends DocumentFilter.FilterBypass {
		public Document getDocument() {
			return AbstractDocument.this;
		}

		public void remove(int offset, int length) throws BadLocationException {
		}

		public void insertString(int offset, String string, AttributeSet attr)
				throws BadLocationException {
		}

		public void replace(int offset, int length, String text, AttributeSet attrs)
				throws BadLocationException {
		}
	}
}