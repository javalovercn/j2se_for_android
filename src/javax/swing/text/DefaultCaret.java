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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventListener;

import javax.swing.Action;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;

/**
 * A default implementation of Caret. The caret is rendered as a vertical line
 * in the color specified by the CaretColor property of the associated
 * JTextComponent. It can blink at the rate specified by the BlinkRate property.
 * <p>
 * This implementation expects two sources of asynchronous notification. The
 * timer thread fires asynchronously, and causes the caret to simply repaint the
 * most recent bounding box. The caret also tracks change as the document is
 * modified. Typically this will happen on the event dispatch thread as a result
 * of some mouse or keyboard event. The caret behavior on both synchronous and
 * asynchronous documents updates is controlled by <code>UpdatePolicy</code>
 * property. The repaint of the new caret location will occur on the event
 * thread in any case, as calls to <code>modelToView</code> are only safe on the
 * event thread.
 * <p>
 * The caret acts as a mouse and focus listener on the text component it has
 * been installed in, and defines the caret semantics based upon those events.
 * The listener methods can be reimplemented to change the semantics. By
 * default, the first mouse button will be used to set focus and caret position.
 * Dragging the mouse pointer with the first mouse button will sweep out a
 * selection that is contiguous in the model. If the associated text component
 * is editable, the caret will become visible when focus is gained, and
 * invisible when focus is lost.
 * <p>
 * The Highlighter bound to the associated text component is used to render the
 * selection by default. Selection appearance can be customized by supplying a
 * painter to use for the highlights. By default a painter is used that will
 * render a solid color as specified in the associated text component in the
 * <code>SelectionColor</code> property. This can easily be changed by
 * reimplementing the {@link #getSelectionPainter getSelectionPainter} method.
 * <p>
 * A customized caret appearance can be achieved by reimplementing the paint
 * method. If the paint method is changed, the damage method should also be
 * reimplemented to cause a repaint for the area needed to render the caret. The
 * caret extends the Rectangle class which is used to hold the bounding box for
 * where the caret was last rendered. This enables the caret to repaint in a
 * thread-safe manner when the caret moves without making a call to modelToView
 * which is unstable between model updates and view repair (i.e. the order of
 * delivery to DocumentListeners is not guaranteed).
 * <p>
 * The magic caret position is set to null when the caret position changes. A
 * timer is used to determine the new location (after the caret change). When
 * the timer fires, if the magic caret position is still null it is reset to the
 * current caret position. Any actions that change the caret position and want
 * the magic caret position to remain the same, must remember the magic caret
 * position, change the cursor, and then set the magic caret position to its
 * original value. This has the benefit that only actions that want the magic
 * caret position to persist (such as open/down) need to know about it.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Timothy Prinzing
 * @see Caret
 */
public class DefaultCaret extends Rectangle
		implements Caret, FocusListener, MouseListener, MouseMotionListener {
	public static final int UPDATE_WHEN_ON_EDT = 0;
	public static final int NEVER_UPDATE = 1;
	public static final int ALWAYS_UPDATE = 2;

	public DefaultCaret() {
	}

	public void setUpdatePolicy(int policy) {
	}

	public int getUpdatePolicy() {
		return updatePolicy;
	}

	protected final JTextComponent getComponent() {
		return component;
	}

	protected final synchronized void repaint() {
	}

	protected synchronized void damage(Rectangle r) {
	}

	protected void adjustVisibility(Rectangle nloc) {
	}

	protected Highlighter.HighlightPainter getSelectionPainter() {
		return null;
	}

	protected void positionCaret(MouseEvent e) {
	}

	protected void moveCaret(MouseEvent e) {
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
	}

	private void selectWord(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	void adjustCaretAndFocus(MouseEvent e) {
	}

	private void adjustCaret(MouseEvent e) {
	}

	private void adjustFocus(boolean inWindow) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void paint(Graphics g) {
	}

	public void install(JTextComponent c) {
	}

	public void deinstall(JTextComponent c) {
	}

	public void addChangeListener(ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}

	public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}

	public ChangeListener[] getChangeListeners() {
		return listenerList.getListeners(ChangeListener.class);
	}

	protected void fireStateChanged() {
	}

	public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
		return listenerList.getListeners(listenerType);
	}

	public void setSelectionVisible(boolean vis) {
	}

	public boolean isSelectionVisible() {
		return selectionVisible;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean e) {
	}

	public void setBlinkRate(int rate) {
	}

	public int getBlinkRate() {
		return 0;
	}

	public int getDot() {
		return dot;
	}

	public int getMark() {
		return mark;
	}

	public void setDot(int dot) {
	}

	public void moveDot(int dot) {
		moveDot(dot, Position.Bias.Forward);
	}

	public void moveDot(int dot, Position.Bias dotBias) {
	}

	void handleMoveDot(int dot, Position.Bias dotBias) {
	}

	public void setDot(int dot, Position.Bias dotBias) {
	}

	void handleSetDot(int dot, Position.Bias dotBias) {
	}

	public Position.Bias getDotBias() {
		return dotBias;
	}

	public Position.Bias getMarkBias() {
		return markBias;
	}

	boolean isDotLeftToRight() {
		return dotLTR;
	}

	boolean isMarkLeftToRight() {
		return markLTR;
	}

	boolean isPositionLTR(int position, Position.Bias bias) {
		return true;
	}

	Position.Bias guessBiasForOffset(int offset, Position.Bias lastBias, boolean lastLTR) {
		return null;
	}

	void changeCaretPosition(int dot, Position.Bias dotBias) {
	}

	void repaintNewCaret() {
	}

	private void updateSystemSelection() {
	}

	private Clipboard getSystemSelection() {
		return null;
	}

	private ClipboardOwner getClipboardOwner() {
		return handler;
	}

	private void ensureValidPosition() {
	}

	public void setMagicCaretPosition(Point p) {
		magicCaretPosition = p;
	}

	public Point getMagicCaretPosition() {
		return magicCaretPosition;
	}

	public boolean equals(Object obj) {
		return (this == obj);
	}

	public String toString() {
		return "";
	}

	private NavigationFilter.FilterBypass getFilterBypass() {
		return null;
	}

	private boolean _contains(int X, int Y, int W, int H) {
		return false;
	}

	int getCaretWidth(int height) {
		return 0;
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	protected EventListenerList listenerList = new EventListenerList();

	protected transient ChangeEvent changeEvent = null;

	JTextComponent component;

	int updatePolicy = UPDATE_WHEN_ON_EDT;
	boolean visible;
	boolean active;
	int dot;
	int mark;
	Object selectionTag;
	boolean selectionVisible;
	Point magicCaretPosition;
	transient Position.Bias dotBias;
	transient Position.Bias markBias;
	boolean dotLTR;
	boolean markLTR;
	transient Handler handler = new Handler();
	transient private int[] flagXPoints = new int[3];
	transient private int[] flagYPoints = new int[3];
	private transient NavigationFilter.FilterBypass filterBypass;
	static private transient Action selectWord = null;
	static private transient Action selectLine = null;
	private boolean ownsSelection;
	private boolean forceCaretPositionChange;
	private transient boolean shouldHandleRelease;
	private transient MouseEvent selectedWordEvent = null;
	private int caretWidth = -1;
	private float aspectRatio = -1;

	class SafeScroller implements Runnable {

		SafeScroller(Rectangle r) {
			this.r = r;
		}

		public void run() {
		}

		Rectangle r;
	}

	class Handler
			implements PropertyChangeListener, DocumentListener, ActionListener, ClipboardOwner {
		public void actionPerformed(ActionEvent e) {
		}

		public void insertUpdate(DocumentEvent e) {
		}

		public void removeUpdate(DocumentEvent e) {
		}

		public void changedUpdate(DocumentEvent e) {
		}

		public void propertyChange(PropertyChangeEvent evt) {
		}

		public void lostOwnership(Clipboard clipboard, Transferable contents) {
		}
	}

	private class DefaultFilterBypass extends NavigationFilter.FilterBypass {
		public Caret getCaret() {
			return DefaultCaret.this;
		}

		public void setDot(int dot, Position.Bias bias) {
			handleSetDot(dot, bias);
		}

		public void moveDot(int dot, Position.Bias bias) {
			handleMoveDot(dot, bias);
		}
	}
}