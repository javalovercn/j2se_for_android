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
package javax.swing;

import java.awt.ItemSelectable;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.event.ChangeListener;

/**
 * State model for buttons.
 * <p>
 * This model is used for regular buttons, as well as check boxes and radio
 * buttons, which are special kinds of buttons. In practice, a button's UI takes
 * the responsibility of calling methods on its model to manage the state, as
 * detailed below:
 * <p>
 * In simple terms, pressing and releasing the mouse over a regular button
 * triggers the button and causes and <code>ActionEvent</code> to be fired. The
 * same behavior can be produced via a keyboard key defined by the look and feel
 * of the button (typically the SPACE BAR). Pressing and releasing this key
 * while the button has focus will give the same results. For check boxes and
 * radio buttons, the mouse or keyboard equivalent sequence just described
 * causes the button to become selected.
 * <p>
 * In details, the state model for buttons works as follows when used with the
 * mouse: <br>
 * Pressing the mouse on top of a button makes the model both armed and pressed.
 * As long as the mouse remains down, the model remains pressed, even if the
 * mouse moves outside the button. On the contrary, the model is only armed
 * while the mouse remains pressed within the bounds of the button (it can move
 * in or out of the button, but the model is only armed during the portion of
 * time spent within the button). A button is triggered, and an
 * <code>ActionEvent</code> is fired, when the mouse is released while the model
 * is armed - meaning when it is released over top of the button after the mouse
 * has previously been pressed on that button (and not already released). Upon
 * mouse release, the model becomes unarmed and unpressed.
 * <p>
 * In details, the state model for buttons works as follows when used with the
 * keyboard: <br>
 * Pressing the look and feel defined keyboard key while the button has focus
 * makes the model both armed and pressed. As long as this key remains down, the
 * model remains in this state. Releasing the key sets the model to unarmed and
 * unpressed, triggers the button, and causes an <code>ActionEvent</code> to be
 * fired.
 *
 * @author Jeff Dinkins
 */
public interface ButtonModel extends ItemSelectable {

	boolean isArmed();

	boolean isSelected();

	boolean isEnabled();

	boolean isPressed();

	boolean isRollover();

	public void setArmed(boolean b);

	public void setSelected(boolean b);

	public void setEnabled(boolean b);

	public void setPressed(boolean b);

	public void setRollover(boolean b);

	public void setMnemonic(int key);

	public int getMnemonic();

	public void setActionCommand(String s);

	public String getActionCommand();

	public void setGroup(ButtonGroup group);

	void addActionListener(ActionListener l);

	void removeActionListener(ActionListener l);

	void addItemListener(ItemListener l);

	void removeItemListener(ItemListener l);

	void addChangeListener(ChangeListener l);

	void removeChangeListener(ChangeListener l);

}