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

import java.awt.Rectangle;
import java.awt.Shape;

/**
 * <code>CompositeView</code> is an abstract <code>View</code> implementation
 * which manages one or more child views. (Note that <code>CompositeView</code>
 * is intended for managing relatively small numbers of child views.)
 * <code>CompositeView</code> is intended to be used as a starting point for
 * <code>View</code> implementations, such as <code>BoxView</code>, that will
 * contain child <code>View</code>s. Subclasses that wish to manage the
 * collection of child <code>View</code>s should use the {@link #replace}
 * method. As <code>View</code> invokes <code>replace</code> during
 * <code>DocumentListener</code> notification, you normally won't need to
 * directly invoke <code>replace</code>.
 *
 * <p>
 * While <code>CompositeView</code> does not impose a layout policy on its child
 * <code>View</code>s, it does allow for inseting the child <code>View</code>s
 * it will contain. The insets can be set by either {@link #setInsets} or
 * {@link #setParagraphInsets}.
 *
 * <p>
 * In addition to the abstract methods of {@link javax.swing.text.View},
 * subclasses of <code>CompositeView</code> will need to override:
 * <ul>
 * <li>{@link #isBefore} - Used to test if a given <code>View</code> location is
 * before the visual space of the <code>CompositeView</code>.
 * <li>{@link #isAfter} - Used to test if a given <code>View</code> location is
 * after the visual space of the <code>CompositeView</code>.
 * <li>{@link #getViewAtPoint} - Returns the view at a given visual location.
 * <li>{@link #childAllocation} - Returns the bounds of a particular child
 * <code>View</code>. <code>getChildAllocation</code> will invoke
 * <code>childAllocation</code> after offseting the bounds by the
 * <code>Inset</code>s of the <code>CompositeView</code>.
 * </ul>
 *
 * @author Timothy Prinzing
 */
public abstract class CompositeView extends View {

	public CompositeView(Element elem) {
		super(elem);
		children = new View[1];
		nchildren = 0;
		childAlloc = new Rectangle();
	}

	protected void loadChildren(ViewFactory f) {
	}

	public void setParent(View parent) {
	}

	public int getViewCount() {
		return nchildren;
	}

	public View getView(int n) {
		return children[n];
	}

	public void replace(int offset, int length, View[] views) {
	}

	public Shape getChildAllocation(int index, Shape a) {
		return null;
	}

	public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
		return null;
	}

	public Shape modelToView(int p0, Position.Bias b0, int p1, Position.Bias b1, Shape a)
			throws BadLocationException {
		return null;
	}

	public int viewToModel(float x, float y, Shape a, Position.Bias[] bias) {
		return -1;
	}

	public int getNextVisualPositionFrom(int pos, Position.Bias b, Shape a, int direction,
			Position.Bias[] biasRet) throws BadLocationException {
		return 0;
	}

	public int getViewIndex(int pos, Position.Bias b) {
		return -1;
	}

	protected abstract boolean isBefore(int x, int y, Rectangle alloc);

	protected abstract boolean isAfter(int x, int y, Rectangle alloc);

	protected abstract View getViewAtPoint(int x, int y, Rectangle alloc);

	protected abstract void childAllocation(int index, Rectangle a);

	protected View getViewAtPosition(int pos, Rectangle a) {
		return null;
	}

	protected int getViewIndexAtPosition(int pos) {
		return 0;
	}

	protected Rectangle getInsideAllocation(Shape a) {
		return null;
	}

	protected void setParagraphInsets(AttributeSet attr) {
	}

	protected void setInsets(short top, short left, short bottom, short right) {
		this.top = top;
		this.left = left;
		this.right = right;
		this.bottom = bottom;
	}

	protected short getLeftInset() {
		return left;
	}

	protected short getRightInset() {
		return right;
	}

	protected short getTopInset() {
		return top;
	}

	protected short getBottomInset() {
		return bottom;
	}

	protected int getNextNorthSouthVisualPositionFrom(int pos, Position.Bias b, Shape a,
			int direction, Position.Bias[] biasRet) throws BadLocationException {
		return 0;
	}

	protected int getNextEastWestVisualPositionFrom(int pos, Position.Bias b, Shape a,
			int direction, Position.Bias[] biasRet) throws BadLocationException {
		return 0;
	}

	protected boolean flipEastAndWestAtEnds(int position, Position.Bias bias) {
		return false;
	}

	private static View[] ZERO = new View[0];

	private View[] children;
	private int nchildren;
	private short left;
	private short right;
	private short top;
	private short bottom;
	private Rectangle childAlloc;
}
