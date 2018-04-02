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

import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;

/**
 * <p>
 * The <code>Document</code> is a container for text that serves as the model
 * for swing text components. The goal for this interface is to scale from very
 * simple needs (a plain text textfield) to complex needs (an HTML or XML
 * document, for example).
 *
 * <p>
 * <b><font size=+1>Content</font></b>
 * <p>
 * At the simplest level, text can be modeled as a linear sequence of
 * characters. To support internationalization, the Swing text model uses
 * <a href="http://www.unicode.org/">unicode</a> characters. The sequence of
 * characters displayed in a text component is generally referred to as the
 * component's <em>content</em>.
 * <p>
 * To refer to locations within the sequence, the coordinates used are the
 * location between two characters. As the diagram below shows, a location in a
 * text document can be referred to as a position, or an offset. This position
 * is zero-based.
 * <p align=center>
 * <img src="doc-files/Document-coord.gif" alt="The following text describes
 * this graphic.">
 * <p>
 * In the example, if the content of a document is the sequence "The quick brown
 * fox," as shown in the preceding diagram, the location just before the word
 * "The" is 0, and the location after the word "The" and before the whitespace
 * that follows it is 3. The entire sequence of characters in the sequence "The"
 * is called a <em>range</em>.
 * <p>
 * The following methods give access to the character data that makes up the
 * content.
 * <ul>
 * <li><a href="#getLength()">getLength()</a>
 * <li><a href="#getText(int, int)">getText(int, int)</a>
 * <li><a href="#getText(int, int, javax.swing.text.Segment)">getText(int, int,
 * Segment)</a>
 * </ul>
 * <p>
 * <b><font size=+1>Structure</font></b>
 * <p>
 * Text is rarely represented simply as featureless content. Rather, text
 * typically has some sort of structure associated with it. Exactly what
 * structure is modeled is up to a particular Document implementation. It might
 * be as simple as no structure (i.e. a simple text field), or it might be
 * something like diagram below.
 * <p align=center>
 * <img src="doc-files/Document-structure.gif" alt="Diagram shows
 * Book->Chapter->Paragraph">
 * <p>
 * The unit of structure (i.e. a node of the tree) is referred to by the
 * <a href="Element.html">Element</a> interface. Each Element can be tagged with
 * a set of attributes. These attributes (name/value pairs) are defined by the
 * <a href="AttributeSet.html">AttributeSet</a> interface.
 * <p>
 * The following methods give access to the document structure.
 * <ul>
 * <li><a href="#getDefaultRootElement()">getDefaultRootElement</a>
 * <li><a href="#getRootElements()">getRootElements</a>
 * </ul>
 *
 * <p>
 * <b><font size=+1>Mutations</font></b>
 * <p>
 * All documents need to be able to add and remove simple text. Typically, text
 * is inserted and removed via gestures from a keyboard or a mouse. What effect
 * the insertion or removal has upon the document structure is entirely up to
 * the implementation of the document.
 * <p>
 * The following methods are related to mutation of the document content:
 * <ul>
 * <li><a href="#insertString(int, java.lang.String,
 * javax.swing.text.AttributeSet)">insertString(int, String, AttributeSet)</a>
 * <li><a href="#remove(int, int)">remove(int, int)</a>
 * <li><a href="#createPosition(int)">createPosition(int)</a>
 * </ul>
 *
 * <p>
 * <b><font size=+1>Notification</font></b>
 * <p>
 * Mutations to the <code>Document</code> must be communicated to interested
 * observers. The notification of change follows the event model guidelines that
 * are specified for JavaBeans. In the JavaBeans event model, once an event
 * notification is dispatched, all listeners must be notified before any further
 * mutations occur to the source of the event. Further, order of delivery is not
 * guaranteed.
 * <p>
 * Notification is provided as two separate events,
 * <a href="../event/DocumentEvent.html">DocumentEvent</a>, and
 * <a href="../event/UndoableEditEvent.html">UndoableEditEvent</a>. If a
 * mutation is made to a <code>Document</code> through its api, a
 * <code>DocumentEvent</code> will be sent to all of the registered
 * <code>DocumentListeners</code>. If the <code>Document</code> implementation
 * supports undo/redo capabilities, an <code>UndoableEditEvent</code> will be
 * sent to all of the registered <code>UndoableEditListener</code>s. If an
 * undoable edit is undone, a <code>DocumentEvent</code> should be fired from
 * the Document to indicate it has changed again. In this case however, there
 * should be no <code>UndoableEditEvent</code> generated since that edit is
 * actually the source of the change rather than a mutation to the
 * <code>Document</code> made through its api.
 * <p align=center>
 * <img src="doc-files/Document-notification.gif" alt="The preceeding text
 * describes this graphic.">
 * <p>
 * Referring to the above diagram, suppose that the component shown on the left
 * mutates the document object represented by the blue rectangle. The document
 * responds by dispatching a DocumentEvent to both component views and sends an
 * UndoableEditEvent to the listening logic, which maintains a history buffer.
 * <p>
 * Now suppose that the component shown on the right mutates the same document.
 * Again, the document dispatches a DocumentEvent to both component views and
 * sends an UndoableEditEvent to the listening logic that is maintaining the
 * history buffer.
 * <p>
 * If the history buffer is then rolled back (i.e. the last UndoableEdit
 * undone), a DocumentEvent is sent to both views, causing both of them to
 * reflect the undone mutation to the document (that is, the removal of the
 * right component's mutation). If the history buffer again rolls back another
 * change, another DocumentEvent is sent to both views, causing them to reflect
 * the undone mutation to the document -- that is, the removal of the left
 * component's mutation.
 * <p>
 * The methods related to observing mutations to the document are:
 * <ul>
 * <li><a href=
 * "#addDocumentListener(javax.swing.event.DocumentListener)">addDocumentListener(DocumentListener)</a>
 * <li><a href=
 * "#removeDocumentListener(javax.swing.event.DocumentListener)">removeDocumentListener(DocumentListener)</a>
 * <li><a href=
 * "#addUndoableEditListener(javax.swing.event.UndoableEditListener)">addUndoableEditListener(UndoableEditListener)</a>
 * <li><a href=
 * "#removeUndoableEditListener(javax.swing.event.UndoableEditListener)">removeUndoableEditListener(UndoableEditListener)</a>
 * </ul>
 *
 * <p>
 * <b><font size=+1>Properties</font></b>
 * <p>
 * Document implementations will generally have some set of properties
 * associated with them at runtime. Two well known properties are the
 * <a href="#StreamDescriptionProperty">StreamDescriptionProperty</a>, which can
 * be used to describe where the <code>Document</code> came from, and the
 * <a href="#TitleProperty">TitleProperty</a>, which can be used to name the
 * <code>Document</code>. The methods related to the properties are:
 * <ul>
 * <li><a href="#getProperty(java.lang.Object)">getProperty(Object)</a>
 * <li><a href="#putProperty(java.lang.Object,
 * java.lang.Object)">putProperty(Object, Object)</a>
 * </ul>
 *
 * <p>
 * For more information on the <code>Document</code> class, see
 * <a href="http://java.sun.com/products/jfc/tsc">The Swing Connection</a> and
 * most particularly the article, <a href=
 * "http://java.sun.com/products/jfc/tsc/articles/text/element_interface"> The
 * Element Interface</a>.
 *
 * @author Timothy Prinzing
 *
 * @see javax.swing.event.DocumentEvent
 * @see javax.swing.event.DocumentListener
 * @see javax.swing.event.UndoableEditEvent
 * @see javax.swing.event.UndoableEditListener
 * @see Element
 * @see Position
 * @see AttributeSet
 */
public interface Document {

	public int getLength();

	public void addDocumentListener(DocumentListener listener);

	public void removeDocumentListener(DocumentListener listener);

	public void addUndoableEditListener(UndoableEditListener listener);

	public void removeUndoableEditListener(UndoableEditListener listener);

	public Object getProperty(Object key);

	public void putProperty(Object key, Object value);

	public void remove(int offs, int len) throws BadLocationException;

	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException;

	public String getText(int offset, int length) throws BadLocationException;

	public void getText(int offset, int length, Segment txt) throws BadLocationException;

	public Position getStartPosition();

	public Position getEndPosition();

	public Position createPosition(int offs) throws BadLocationException;

	public Element[] getRootElements();

	public Element getDefaultRootElement();

	public void render(Runnable r);

	public static final String StreamDescriptionProperty = "stream";
	public static final String TitleProperty = "title";

}