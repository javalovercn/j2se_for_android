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

import hc.android.AndroidClassUtil;

import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;

/**
 * An implementation of a two-state button. The <code>JRadioButton</code> and
 * <code>JCheckBox</code> classes are subclasses of this class. For information
 * on using them see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/button.html">How
 * to Use Buttons, Check Boxes, and Radio Buttons</a>, a section in <em>The Java
 * Tutorial</em>.
 * <p>
 * Buttons can be configured, and to some degree controlled, by
 * <code><a href="Action.html">Action</a></code>s. Using an <code>Action</code>
 * with a button has many benefits beyond directly configuring a button. Refer
 * to <a href="Action.html#buttonActions"> Swing Components Supporting
 * <code>Action</code></a> for more details, and you can find more information
 * in <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/misc/action.html">How to Use
 * Actions</a>, a section in <em>The Java Tutorial</em>.
 * <p>
 * <strong>Warning:</strong> Swing is not thread safe. For more information see
 * <a href="package-summary.html#threading">Swing's Threading Policy</a>.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @beaninfo attribute: isContainer false description: An implementation of a
 *           two-state button.
 *
 * @see JRadioButton
 * @see JCheckBox
 * @author Jeff Dinkins
 */
public class JToggleButton extends AbstractButton implements Accessible {
	private static final String uiClassID = "ToggleButtonUI";
	int checkBoxForTableDrawable;

	public JToggleButton() {
		this(null, null, false);
	}

	public JToggleButton(Icon icon) {
		this(null, icon, false);
	}

	public JToggleButton(Icon icon, boolean selected) {
		this(null, icon, selected);
	}

	public JToggleButton(String text) {
		this(text, null, false);
	}

	public JToggleButton(String text, boolean selected) {
		this(text, null, selected);
	}

	public JToggleButton(Action a) {
		this();
		setAction(a);
	}

	public JToggleButton(String text, Icon icon) {
		this(text, icon, false);
	}

	public JToggleButton(String text, Icon icon, boolean selected, int drawable) {
		checkBoxForTableDrawable = drawable;

		setModel(new ToggleButtonModel(this));

		model.setSelected(selected);

		init(text, icon);
	}

	public JToggleButton(String text, Icon icon, boolean selected) {
		this(text, icon, selected, 0);
	}

	public void updateUI() {
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public void setSelected(boolean b) {
		super.setSelected(b);
		model.setSelected(b);
	}

	boolean shouldUpdateSelectedStateFromAction() {
		return true;
	}

	public static class ToggleButtonModel extends DefaultButtonModel {
		AbstractButton source;

		public ToggleButtonModel(AbstractButton s) {
			source = s;
		}

		public boolean isSelected() {
			return source.isSelected();
		}

		public void setSelected(boolean b) {
			ButtonGroup group = getGroup();
			if (group != null) {
				group.setSelected(this, b);
				b = group.isSelected(this);
			}

			if (isSelected() == b) {
				return;
			}

			source.setSelected(b);
		}

		public void setPressed(boolean b) {
		}
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	protected String paramString() {
		return super.paramString();
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

}