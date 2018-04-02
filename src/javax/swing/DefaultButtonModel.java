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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;
import java.util.EventListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * The default implementation of a <code>Button</code> component's data model.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Jeff Dinkins
 */
public class DefaultButtonModel implements ButtonModel, Serializable {

	protected int stateMask = 0;

	protected String actionCommand = null;

	protected ButtonGroup group = null;

	protected int mnemonic = 0;

	protected transient ChangeEvent changeEvent = null;

	protected EventListenerList listenerList = new EventListenerList();

	private boolean menuItem = false;

	public DefaultButtonModel() {
		stateMask = 0;
		setEnabled(true);
	}

	public final static int ARMED = 1 << 0;
	public final static int SELECTED = 1 << 1;
	public final static int PRESSED = 1 << 2;
	public final static int ENABLED = 1 << 3;
	public final static int ROLLOVER = 1 << 4;

	public void setActionCommand(String actionCommand) {
		this.actionCommand = actionCommand;
	}

	public String getActionCommand() {
		return actionCommand;
	}

	public boolean isArmed() {
		return (stateMask & ARMED) != 0;
	}

	public boolean isSelected() {
		return (stateMask & SELECTED) != 0;
	}

	public boolean isEnabled() {
		return (stateMask & ENABLED) != 0;
	}

	public boolean isPressed() {
		return (stateMask & PRESSED) != 0;
	}

	public boolean isRollover() {
		return (stateMask & ROLLOVER) != 0;
	}

	public void setArmed(boolean b) {
	}

	public void setEnabled(boolean b) {
	}

	public void setSelected(boolean b) {
	}

	public void setPressed(boolean b) {
	}

	public void setRollover(boolean b) {
	}

	public void setMnemonic(int key) {
		mnemonic = key;
	}

	public int getMnemonic() {
		return mnemonic;
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
		ChangeListener[] listeners = listenerList.getListeners(ChangeListener.class);
		ChangeEvent changeEvent = null;
		for (int i = 0; i < listeners.length; i++) {
			if (changeEvent == null) {
				changeEvent = new ChangeEvent(this);
			}
			((ChangeListener) listeners[i]).stateChanged(changeEvent);
		}
	}

	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
	}

	public void removeActionListener(ActionListener l) {
		listenerList.remove(ActionListener.class, l);
	}

	public ActionListener[] getActionListeners() {
		return listenerList.getListeners(ActionListener.class);
	}

	protected void fireActionPerformed(ActionEvent e) {
		ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
		for (int i = 0; i < listeners.length; i++) {
			((ActionListener) listeners[i]).actionPerformed(e);
		}
	}

	public void addItemListener(ItemListener l) {
		listenerList.add(ItemListener.class, l);
	}

	public void removeItemListener(ItemListener l) {
		listenerList.remove(ItemListener.class, l);
	}

	public ItemListener[] getItemListeners() {
		return listenerList.getListeners(ItemListener.class);
	}

	protected void fireItemStateChanged(ItemEvent e) {
		ItemListener[] listeners = listenerList.getListeners(ItemListener.class);
		for (int i = 0; i < listeners.length; i++) {
			((ItemListener) listeners[i]).itemStateChanged(e);
		}
	}

	public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
		return listenerList.getListeners(listenerType);
	}

	public Object[] getSelectedObjects() {
		return null;
	}

	public void setGroup(ButtonGroup group) {
		this.group = group;
	}

	public ButtonGroup getGroup() {
		return group;
	}

	boolean isMenuItem() {
		return menuItem;
	}

	void setMenuItem(boolean menuItem) {
		this.menuItem = menuItem;
	}
}