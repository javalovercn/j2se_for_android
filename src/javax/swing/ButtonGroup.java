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

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

/**
 * This class is used to create a multiple-exclusion scope for a set of buttons.
 * Creating a set of buttons with the same <code>ButtonGroup</code> object means
 * that turning "on" one of those buttons turns off all other buttons in the
 * group.
 * <p>
 * A <code>ButtonGroup</code> can be used with any set of objects that inherit
 * from <code>AbstractButton</code>. Typically a button group contains instances
 * of <code>JRadioButton</code>, <code>JRadioButtonMenuItem</code>, or
 * <code>JToggleButton</code>. It wouldn't make sense to put an instance of
 * <code>JButton</code> or <code>JMenuItem</code> in a button group because
 * <code>JButton</code> and <code>JMenuItem</code> don't implement the selected
 * state.
 * <p>
 * Initially, all buttons in the group are unselected.
 * <p>
 * For examples and further information on using button groups see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/button.html#radiobutton">How
 * to Use Radio Buttons</a>, a section in <em>The Java Tutorial</em>.
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
public class ButtonGroup implements Serializable {

	protected Vector<AbstractButton> list = new Vector<AbstractButton>();

	ButtonModel selected = null;

	public ButtonGroup() {
	}

	public void add(AbstractButton b) {
		if (b == null) {
			return;
		}
		list.addElement(b);

		if (b.isSelected()) {
			if (selected == null) {
				selected = b.getModel();
			} else {
				b.setSelected(false);
			}
		}

		b.getModel().setGroup(this);
	}

	public void remove(AbstractButton b) {
		if (b == null) {
			return;
		}
		list.removeElement(b);
		if (b.getModel() == selected) {
			selected = null;
		}
		b.getModel().setGroup(null);
	}

	public void clearSelection() {
		if (selected != null) {
			ButtonModel oldSelection = selected;
			selected = null;
			oldSelection.setSelected(false);
		}
	}

	public Enumeration<AbstractButton> getElements() {
		return list.elements();
	}

	public ButtonModel getSelection() {
		return selected;
	}

	public void setSelected(ButtonModel m, boolean b) {
		if (b && m != null && m != selected) {
			ButtonModel oldSelection = selected;
			selected = m;
			if (oldSelection != null) {
				oldSelection.setSelected(false);
			}
			m.setSelected(true);
		}
	}

	public boolean isSelected(ButtonModel m) {
		return (m == selected);
	}

	public int getButtonCount() {
		if (list == null) {
			return 0;
		} else {
			return list.size();
		}
	}

}
