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
package javax.swing.plaf;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.View;

/**
 * Text editor user interface
 *
 * @author Timothy Prinzing
 */
public abstract class TextUI extends ComponentUI {
	public abstract Rectangle modelToView(JTextComponent t, int pos) throws BadLocationException;

	public abstract Rectangle modelToView(JTextComponent t, int pos, Position.Bias bias)
			throws BadLocationException;

	public abstract int viewToModel(JTextComponent t, Point pt);

	public abstract int viewToModel(JTextComponent t, Point pt, Position.Bias[] biasReturn);

	public abstract int getNextVisualPositionFrom(JTextComponent t, int pos, Position.Bias b,
			int direction, Position.Bias[] biasRet) throws BadLocationException;

	public abstract void damageRange(JTextComponent t, int p0, int p1);

	public abstract void damageRange(JTextComponent t, int p0, int p1, Position.Bias firstBias,
			Position.Bias secondBias);

	public abstract EditorKit getEditorKit(JTextComponent t);

	public abstract View getRootView(JTextComponent t);

	public String getToolTipText(JTextComponent t, Point pt) {
		return null;
	}
}