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
package java.awt;

import hc.android.AndroidClassUtil;

import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.im.InputMethodRequests;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.BreakIterator;
import java.util.EventListener;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleText;
import javax.swing.event.EventListenerList;
import javax.swing.text.AttributeSet;

/**
 * The <code>TextComponent</code> class is the superclass of any component that
 * allows the editing of some text.
 * <p>
 * A text component embodies a string of text. The <code>TextComponent</code>
 * class defines a set of methods that determine whether or not this text is
 * editable. If the component is editable, it defines another set of methods
 * that supports a text insertion caret.
 * <p>
 * In addition, the class defines methods that are used to maintain a current
 * <em>selection</em> from the text. The text selection, a substring of the
 * component's text, is the target of editing operations. It is also referred to
 * as the <em>selected text</em>.
 *
 * @author Sami Shaio
 * @author Arthur van Hoff
 * @since JDK1.0
 */
public class TextComponent extends Component implements Accessible {

	/**
	 * The value of the text. A <code>null</code> value is the same as "".
	 * 
	 * @serial
	 * @see #setText(String)
	 * @see #getText()
	 */
	String text;

	/**
	 * A boolean indicating whether or not this <code>TextComponent</code> is
	 * editable. It will be <code>true</code> if the text component is editable
	 * and <code>false</code> if not.
	 * 
	 * @serial
	 * @see #isEditable()
	 */
	boolean editable = true;

	/**
	 * The selection refers to the selected text, and the
	 * <code>selectionStart</code> is the start position of the selected text.
	 * 
	 * @serial
	 * @see #getSelectionStart()
	 * @see #setSelectionStart(int)
	 */
	int selectionStart;

	/**
	 * The selection refers to the selected text, and the
	 * <code>selectionEnd</code> is the end position of the selected text.
	 * 
	 * @serial
	 * @see #getSelectionEnd()
	 * @see #setSelectionEnd(int)
	 */
	int selectionEnd;

	// A flag used to tell whether the background has been set by
	// developer code (as opposed to AWT code). Used to determine
	// the background color of non-editable TextComponents.
	boolean backgroundSetByClientCode = false;

	transient protected EventListenerList list = new EventListenerList();

	/**
	 * Constructs a new text component initialized with the specified text. Sets
	 * the value of the cursor to <code>Cursor.TEXT_CURSOR</code>.
	 * 
	 * @param text
	 *            the text to be displayed; if <code>text</code> is
	 *            <code>null</code>, the empty string <code>""</code> will be
	 *            displayed
	 * @exception HeadlessException
	 *                if <code>GraphicsEnvironment.isHeadless</code> returns
	 *                true
	 * @see java.awt.GraphicsEnvironment#isHeadless
	 * @see java.awt.Cursor
	 */
	TextComponent(String text) throws HeadlessException {
		GraphicsEnvironment.checkHeadless();
		this.text = (text != null) ? text : "";
	}

	private void enableInputMethodsIfNecessary() {
		if (checkForEnableIM) {
			checkForEnableIM = false;
			try {
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				boolean shouldEnable = false;
				enableInputMethods(shouldEnable);
			} catch (Exception e) {
				// if something bad happens, just don't enable input methods
			}
		}
	}

	/**
	 * Enables or disables input method support for this text component. If
	 * input method support is enabled and the text component also processes key
	 * events, incoming events are offered to the current input method and will
	 * only be processed by the component or dispatched to its listeners if the
	 * input method does not consume them. Whether and how input method support
	 * for this text component is enabled or disabled by default is
	 * implementation dependent.
	 * 
	 * @param enable
	 *            true to enable, false to disable
	 * @see #processKeyEvent
	 * @since 1.2
	 */
	public void enableInputMethods(boolean enable) {
		checkForEnableIM = false;
		super.enableInputMethods(enable);
	}

	boolean areInputMethodsEnabled() {
		return true;
	}

	public InputMethodRequests getInputMethodRequests() {
		return null;
	}

	/**
	 * Makes this Component displayable by connecting it to a native screen
	 * resource. This method is called internally by the toolkit and should not
	 * be called directly by programs.
	 * 
	 * @see java.awt.TextComponent#removeNotify
	 */
	public void addNotify() {
	}

	/**
	 * Removes the <code>TextComponent</code>'s peer. The peer allows us to
	 * modify the appearance of the <code>TextComponent</code> without changing
	 * its functionality.
	 */
	public void removeNotify() {
	}

	/**
	 * Sets the text that is presented by this text component to be the
	 * specified text.
	 * 
	 * @param t
	 *            the new text; if this parameter is <code>null</code> then the
	 *            text is set to the empty string ""
	 * @see java.awt.TextComponent#getText
	 */
	public synchronized void setText(String t) {
		boolean skipTextEvent = (text == null || text.isEmpty()) && (t == null || t.isEmpty());
		text = (t != null) ? t : "";
		// TextComponentPeer peer = (TextComponentPeer)this.peer;
		// // Please note that we do not want to post an event
		// // if TextArea.setText() or TextField.setText() replaces an empty
		// text
		// // by an empty text, that is, if component's text remains unchanged.
		// if (peer != null && !skipTextEvent) {
		// peer.setText(text);
		// }
	}

	/**
	 * Returns the text that is presented by this text component. By default,
	 * this is an empty string.
	 * 
	 * @return the value of this <code>TextComponent</code>
	 * @see java.awt.TextComponent#setText
	 */
	public synchronized String getText() {
		// TextComponentPeer peer = (TextComponentPeer)this.peer;
		// if (peer != null) {
		// text = peer.getText();
		// }
		return text;
	}

	/**
	 * Returns the selected text from the text that is presented by this text
	 * component.
	 * 
	 * @return the selected text of this text component
	 * @see java.awt.TextComponent#select
	 */
	public synchronized String getSelectedText() {
		return getText().substring(getSelectionStart(), getSelectionEnd());
	}

	/**
	 * Indicates whether or not this text component is editable.
	 * 
	 * @return <code>true</code> if this text component is editable;
	 *         <code>false</code> otherwise.
	 * @see java.awt.TextComponent#setEditable
	 * @since JDK1.0
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Sets the flag that determines whether or not this text component is
	 * editable.
	 * <p>
	 * If the flag is set to <code>true</code>, this text component becomes user
	 * editable. If the flag is set to <code>false</code>, the user cannot
	 * change the text of this text component. By default, non-editable text
	 * components have a background color of SystemColor.control. This default
	 * can be overridden by calling setBackground.
	 * 
	 * @param b
	 *            a flag indicating whether this text component is user
	 *            editable.
	 * @see java.awt.TextComponent#isEditable
	 * @since JDK1.0
	 */
	public synchronized void setEditable(boolean b) {
		if (editable == b) {
			return;
		}

		editable = b;
	}

	/**
	 * Gets the background color of this text component.
	 * 
	 * By default, non-editable text components have a background color of
	 * SystemColor.control. This default can be overridden by calling
	 * setBackground.
	 * 
	 * @return This text component's background color. If this text component
	 *         does not have a background color, the background color of its
	 *         parent is returned.
	 * @see #setBackground(Color)
	 * @since JDK1.0
	 */
	public Color getBackground() {
		return super.getBackground();
	}

	/**
	 * Sets the background color of this text component.
	 * 
	 * @param c
	 *            The color to become this text component's color. If this
	 *            parameter is null then this text component will inherit the
	 *            background color of its parent.
	 * @see #getBackground()
	 * @since JDK1.0
	 */
	public void setBackground(Color c) {
		backgroundSetByClientCode = true;
		super.setBackground(c);
	}

	/**
	 * Gets the start position of the selected text in this text component.
	 * 
	 * @return the start position of the selected text
	 * @see java.awt.TextComponent#setSelectionStart
	 * @see java.awt.TextComponent#getSelectionEnd
	 */
	public synchronized int getSelectionStart() {
		// TextComponentPeer peer = (TextComponentPeer)this.peer;
		// if (peer != null) {
		// selectionStart = peer.getSelectionStart();
		// }
		return selectionStart;
	}

	/**
	 * Sets the selection start for this text component to the specified
	 * position. The new start point is constrained to be at or before the
	 * current selection end. It also cannot be set to less than zero, the
	 * beginning of the component's text. If the caller supplies a value for
	 * <code>selectionStart</code> that is out of bounds, the method enforces
	 * these constraints silently, and without failure.
	 * 
	 * @param selectionStart
	 *            the start position of the selected text
	 * @see java.awt.TextComponent#getSelectionStart
	 * @see java.awt.TextComponent#setSelectionEnd
	 * @since JDK1.1
	 */
	public synchronized void setSelectionStart(int selectionStart) {
		/*
		 * Route through select method to enforce consistent policy between
		 * selectionStart and selectionEnd.
		 */
		select(selectionStart, getSelectionEnd());
	}

	/**
	 * Gets the end position of the selected text in this text component.
	 * 
	 * @return the end position of the selected text
	 * @see java.awt.TextComponent#setSelectionEnd
	 * @see java.awt.TextComponent#getSelectionStart
	 */
	public synchronized int getSelectionEnd() {
		// TextComponentPeer peer = (TextComponentPeer)this.peer;
		// if (peer != null) {
		// selectionEnd = peer.getSelectionEnd();
		// }
		return selectionEnd;
	}

	/**
	 * Sets the selection end for this text component to the specified position.
	 * The new end point is constrained to be at or after the current selection
	 * start. It also cannot be set beyond the end of the component's text. If
	 * the caller supplies a value for <code>selectionEnd</code> that is out of
	 * bounds, the method enforces these constraints silently, and without
	 * failure.
	 * 
	 * @param selectionEnd
	 *            the end position of the selected text
	 * @see java.awt.TextComponent#getSelectionEnd
	 * @see java.awt.TextComponent#setSelectionStart
	 * @since JDK1.1
	 */
	public synchronized void setSelectionEnd(int selectionEnd) {
		/*
		 * Route through select method to enforce consistent policy between
		 * selectionStart and selectionEnd.
		 */
		select(getSelectionStart(), selectionEnd);
	}

	/**
	 * Selects the text between the specified start and end positions.
	 * <p>
	 * This method sets the start and end positions of the selected text,
	 * enforcing the restriction that the start position must be greater than or
	 * equal to zero. The end position must be greater than or equal to the
	 * start position, and less than or equal to the length of the text
	 * component's text. The character positions are indexed starting with zero.
	 * The length of the selection is <code>endPosition</code> -
	 * <code>startPosition</code>, so the character at <code>endPosition</code>
	 * is not selected. If the start and end positions of the selected text are
	 * equal, all text is deselected.
	 * <p>
	 * If the caller supplies values that are inconsistent or out of bounds, the
	 * method enforces these constraints silently, and without failure.
	 * Specifically, if the start position or end position is greater than the
	 * length of the text, it is reset to equal the text length. If the start
	 * position is less than zero, it is reset to zero, and if the end position
	 * is less than the start position, it is reset to the start position.
	 * 
	 * @param selectionStart
	 *            the zero-based index of the first character (<code>char</code>
	 *            value) to be selected
	 * @param selectionEnd
	 *            the zero-based end position of the text to be selected; the
	 *            character (<code>char</code> value) at
	 *            <code>selectionEnd</code> is not selected
	 * @see java.awt.TextComponent#setSelectionStart
	 * @see java.awt.TextComponent#setSelectionEnd
	 * @see java.awt.TextComponent#selectAll
	 */
	public synchronized void select(int selectionStart, int selectionEnd) {
		String text = getText();
		if (selectionStart < 0) {
			selectionStart = 0;
		}
		if (selectionStart > text.length()) {
			selectionStart = text.length();
		}
		if (selectionEnd > text.length()) {
			selectionEnd = text.length();
		}
		if (selectionEnd < selectionStart) {
			selectionEnd = selectionStart;
		}

		this.selectionStart = selectionStart;
		this.selectionEnd = selectionEnd;

		// TextComponentPeer peer = (TextComponentPeer)this.peer;
		// if (peer != null) {
		// peer.select(selectionStart, selectionEnd);
		// }
	}

	/**
	 * Selects all the text in this text component.
	 * 
	 * @see java.awt.TextComponent#select
	 */
	public synchronized void selectAll() {
		this.selectionStart = 0;
		this.selectionEnd = getText().length();

		// TextComponentPeer peer = (TextComponentPeer)this.peer;
		// if (peer != null) {
		// peer.select(selectionStart, selectionEnd);
		// }
	}

	/**
	 * Sets the position of the text insertion caret. The caret position is
	 * constrained to be between 0 and the last character of the text,
	 * inclusive. If the passed-in value is greater than this range, the value
	 * is set to the last character (or 0 if the <code>TextComponent</code>
	 * contains no text) and no error is returned. If the passed-in value is
	 * less than 0, an <code>IllegalArgumentException</code> is thrown.
	 * 
	 * @param position
	 *            the position of the text insertion caret
	 * @exception IllegalArgumentException
	 *                if <code>position</code> is less than zero
	 * @since JDK1.1
	 */
	public synchronized void setCaretPosition(int position) {
		if (position < 0) {
			throw new IllegalArgumentException("position less than zero.");
		}

		int maxposition = getText().length();
		if (position > maxposition) {
			position = maxposition;
		}

		// TextComponentPeer peer = (TextComponentPeer)this.peer;
		// if (peer != null) {
		// peer.setCaretPosition(position);
		// } else {
		// select(position, position);
		// }
	}

	/**
	 * Returns the position of the text insertion caret. The caret position is
	 * constrained to be between 0 and the last character of the text,
	 * inclusive. If the text or caret have not been set, the default caret
	 * position is 0.
	 * 
	 * @return the position of the text insertion caret
	 * @see #setCaretPosition(int)
	 * @since JDK1.1
	 */
	public synchronized int getCaretPosition() {
		// TextComponentPeer peer = (TextComponentPeer)this.peer;
		// int position = 0;
		//
		// if (peer != null) {
		// position = peer.getCaretPosition();
		// } else {
		// position = selectionStart;
		// }
		// int maxposition = getText().length();
		// if (position > maxposition) {
		// position = maxposition;
		// }
		return 0;
	}

	/**
	 * Adds the specified text event listener to receive text events from this
	 * text component. If <code>l</code> is <code>null</code>, no exception is
	 * thrown and no action is performed.
	 * <p>
	 * Refer to <a href="doc-files/AWTThreadIssues.html#ListenersThreads" >AWT
	 * Threading Issues</a> for details on AWT's threading model.
	 * 
	 * @param l
	 *            the text event listener
	 * @see #removeTextListener
	 * @see #getTextListeners
	 * @see java.awt.event.TextListener
	 */
	public synchronized void addTextListener(TextListener l) {
		if (l == null) {
			return;
		}
		list.add(TextListener.class, l);
	}

	/**
	 * Removes the specified text event listener so that it no longer receives
	 * text events from this text component If <code>l</code> is
	 * <code>null</code>, no exception is thrown and no action is performed.
	 * <p>
	 * Refer to <a href="doc-files/AWTThreadIssues.html#ListenersThreads" >AWT
	 * Threading Issues</a> for details on AWT's threading model.
	 * 
	 * @param l
	 *            the text listener
	 * @see #addTextListener
	 * @see #getTextListeners
	 * @see java.awt.event.TextListener
	 * @since JDK1.1
	 */
	public synchronized void removeTextListener(TextListener l) {
		if (l == null) {
			return;
		}
		list.remove(TextListener.class, l);
	}

	/**
	 * Returns an array of all the text listeners registered on this text
	 * component.
	 * 
	 * @return all of this text component's <code>TextListener</code>s or an
	 *         empty array if no text listeners are currently registered
	 * 
	 * 
	 * @see #addTextListener
	 * @see #removeTextListener
	 * @since 1.4
	 */
	public synchronized TextListener[] getTextListeners() {
		return (TextListener[]) (getListeners(TextListener.class));
	}

	/**
	 * Returns an array of all the objects currently registered as
	 * <code><em>Foo</em>Listener</code>s upon this <code>TextComponent</code>.
	 * <code><em>Foo</em>Listener</code>s are registered using the
	 * <code>add<em>Foo</em>Listener</code> method.
	 * 
	 * <p>
	 * You can specify the <code>listenerType</code> argument with a class
	 * literal, such as <code><em>Foo</em>Listener.class</code>. For example,
	 * you can query a <code>TextComponent</code> <code>t</code> for its text
	 * listeners with the following code:
	 * 
	 * <pre>
	 * TextListener[] tls = (TextListener[]) (t.getListeners(TextListener.class));
	 * </pre>
	 * 
	 * If no such listeners exist, this method returns an empty array.
	 * 
	 * @param listenerType
	 *            the type of listeners requested; this parameter should specify
	 *            an interface that descends from
	 *            <code>java.util.EventListener</code>
	 * @return an array of all objects registered as
	 *         <code><em>Foo</em>Listener</code>s on this text component, or an
	 *         empty array if no such listeners have been added
	 * @exception ClassCastException
	 *                if <code>listenerType</code> doesn't specify a class or
	 *                interface that implements
	 *                <code>java.util.EventListener</code>
	 * 
	 * @see #getTextListeners
	 * @since 1.3
	 */
	public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
		return list.getListeners(listenerType);
	}

	// REMIND: remove when filtering is done at lower level
	boolean eventEnabled(AWTEvent e) {
		// if (e.id == TextEvent.TEXT_VALUE_CHANGED) {
		// if ((eventMask & AWTEvent.TEXT_EVENT_MASK) != 0 ||
		// textListener != null) {
		// return true;
		// }
		// return false;
		// }
		return false;// super.eventEnabled(e);
	}

	/**
	 * Processes events on this text component. If the event is a
	 * <code>TextEvent</code>, it invokes the <code>processTextEvent</code>
	 * method else it invokes its superclass's <code>processEvent</code>.
	 * <p>
	 * Note that if the event parameter is <code>null</code> the behavior is
	 * unspecified and may result in an exception.
	 * 
	 * @param e
	 *            the event
	 */
	protected void processEvent(AWTEvent e) {
		if (e instanceof TextEvent) {
			processTextEvent((TextEvent) e);
			return;
		}
	}

	/**
	 * Processes text events occurring on this text component by dispatching
	 * them to any registered <code>TextListener</code> objects.
	 * <p>
	 * NOTE: This method will not be called unless text events are enabled for
	 * this component. This happens when one of the following occurs:
	 * <ul>
	 * <li>A <code>TextListener</code> object is registered via
	 * <code>addTextListener</code>
	 * <li>Text events are enabled via <code>enableEvents</code>
	 * </ul>
	 * <p>
	 * Note that if the event parameter is <code>null</code> the behavior is
	 * unspecified and may result in an exception.
	 * 
	 * @param e
	 *            the text event
	 * @see Component#enableEvents
	 */
	protected void processTextEvent(TextEvent e) {
		TextListener[] listener = getTextListeners();
		if (listener != null) {
			int id = e.getID();
			switch (id) {
			case TextEvent.TEXT_VALUE_CHANGED:
				for (int i = 0; i < listener.length; i++) {
					listener[i].textValueChanged(e);
				}
				break;
			}
		}
	}

	/**
	 * Returns a string representing the state of this
	 * <code>TextComponent</code>. This method is intended to be used only for
	 * debugging purposes, and the content and format of the returned string may
	 * vary between implementations. The returned string may be empty but may
	 * not be <code>null</code>.
	 * 
	 * @return the parameter string of this text component
	 */
	protected String paramString() {
		return "";
	}

	/**
	 * Assigns a valid value to the canAccessClipboard instance variable.
	 */
	private boolean canAccessClipboard() {
		SecurityManager sm = System.getSecurityManager();
		if (sm == null)
			return true;
		try {
			sm.checkSystemClipboardAccess();
			return true;
		} catch (SecurityException e) {
		}
		return false;
	}

	/*
	 * Serialization support.
	 */
	/**
	 * The textComponent SerializedDataVersion.
	 * 
	 * @serial
	 */
	private int textComponentSerializedDataVersion = 1;

	/**
	 * Writes default serializable fields to stream. Writes a list of
	 * serializable TextListener(s) as optional data. The non-serializable
	 * TextListener(s) are detected and no attempt is made to serialize them.
	 * 
	 * @serialData Null terminated sequence of zero or more pairs. A pair
	 *             consists of a String and Object. The String indicates the
	 *             type of object and is one of the following : textListenerK
	 *             indicating and TextListener object.
	 * 
	 * @see AWTEventMulticaster#save(ObjectOutputStream, String, EventListener)
	 * @see java.awt.Component#textListenerK
	 */
	private void writeObject(java.io.ObjectOutputStream s) throws IOException {
	}

	/**
	 * Read the ObjectInputStream, and if it isn't null, add a listener to
	 * receive text events fired by the TextComponent. Unrecognized keys or
	 * values will be ignored.
	 * 
	 * @exception HeadlessException
	 *                if <code>GraphicsEnvironment.isHeadless()</code> returns
	 *                <code>true</code>
	 * @see #removeTextListener
	 * @see #addTextListener
	 * @see java.awt.GraphicsEnvironment#isHeadless
	 */
	private void readObject(ObjectInputStream s)
			throws ClassNotFoundException, IOException, HeadlessException {
	}

	// ///////////////
	// Accessibility support
	// //////////////

	/**
	 *
	 */
	int getIndexAtPoint(Point p) {
		return -1;
		/*
		 * To be fully implemented in a future release if (peer == null) {
		 * return -1; } TextComponentPeer peer = (TextComponentPeer)this.peer;
		 * return peer.getIndexAtPoint(p.x, p.y);
		 */
	}

	/**
	 *
	 */
	Rectangle getCharacterBounds(int i) {
		return null;
		/*
		 * To be fully implemented in a future release if (peer == null) {
		 * return null; } TextComponentPeer peer = (TextComponentPeer)this.peer;
		 * return peer.getCharacterBounds(i);
		 */
	}

	/**
	 * Gets the AccessibleContext associated with this TextComponent. For text
	 * components, the AccessibleContext takes the form of an
	 * AccessibleAWTTextComponent. A new AccessibleAWTTextComponent instance is
	 * created if necessary.
	 * 
	 * @return an AccessibleAWTTextComponent that serves as the
	 *         AccessibleContext of this TextComponent
	 * @since 1.3
	 */
	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

	private boolean checkForEnableIM = true;

	@Override
	public void remove(MenuComponent comp) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean postEvent(Event evt) {
		// TODO Auto-generated method stub
		return false;
	}
}
