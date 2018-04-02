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

import javax.swing.event.ChangeListener;

/**
 * A place within a document view that represents where things can be inserted
 * into the document model. A caret has a position in the document referred to
 * as a dot. The dot is where the caret is currently located in the model. There
 * is a second position maintained by the caret that represents the other end of
 * a selection called mark. If there is no selection the dot and mark will be
 * equal. If a selection exists, the two values will be different.
 * <p>
 * The dot can be placed by either calling <code>setDot</code> or
 * <code>moveDot</code>. Setting the dot has the effect of removing any
 * selection that may have previously existed. The dot and mark will be equal.
 * Moving the dot has the effect of creating a selection as the mark is left at
 * whatever position it previously had.
 *
 * @author Timothy Prinzing
 */
public interface Caret {

	public void install(JTextComponent c);

	public void deinstall(JTextComponent c);

	public void paint(Graphics g);

	public void addChangeListener(ChangeListener l);

	public void removeChangeListener(ChangeListener l);

	public boolean isVisible();

	public void setVisible(boolean v);

	public boolean isSelectionVisible();

	public void setSelectionVisible(boolean v);

	public void setMagicCaretPosition(Point p);

	public Point getMagicCaretPosition();

	public void setBlinkRate(int rate);

	public int getBlinkRate();

	public int getDot();

	public int getMark();

	public void setDot(int dot);

	public void moveDot(int dot);

};
