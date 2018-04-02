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

import hc.android.ActivityManager;
import hc.android.AndroidClassUtil;
import hc.android.UICore;
import hc.android.AndroidUIUtil;

import java.awt.ComponentOrientation;
import java.awt.Font;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;

/**
 * An implementation of a radio button -- an item that can be selected or
 * deselected, and which displays its state to the user. Used with a
 * {@link ButtonGroup} object to create a group of buttons in which only one
 * button at a time can be selected. (Create a ButtonGroup object and use its
 * <code>add</code> method to include the JRadioButton objects in the group.)
 * <blockquote> <strong>Note:</strong> The ButtonGroup object is a logical
 * grouping -- not a physical grouping. To create a button panel, you should
 * still create a {@link JPanel} or similar container-object and add a
 * {@link javax.swing.border.Border} to it to set it off from surrounding
 * components. </blockquote>
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
 * See <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/button.html">How
 * to Use Buttons, Check Boxes, and Radio Buttons</a> in <em>The Java
 * Tutorial</em> for further documentation.
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
 * @beaninfo attribute: isContainer false description: A component which can
 *           display it's state as selected or deselected.
 *
 * @see ButtonGroup
 * @see JCheckBox
 * @author Jeff Dinkins
 */
public class JRadioButton extends JToggleButton implements Accessible {
	private RadioButton radioButton;
	LinearLayout defaultLinearLayout;

	private static final String uiClassID = "RadioButtonUI";

	public void applyComponentOrientation(ComponentOrientation o) {
		super.applyComponentOrientation(o);
		RadioButton snap = radioButton;
		if (snap != null) {
			setHorizontalAlignment(getHorizontalAlignment());
		}
	}

	public JRadioButton() {
		this(null, null, false);
	}

	public JRadioButton(Icon icon) {
		this(null, icon, false);
	}

	public JRadioButton(Action a) {
		this();
		setAction(a);
	}

	public JRadioButton(Icon icon, boolean selected) {
		this(null, icon, selected);
	}

	public JRadioButton(String text) {
		this(text, null, false);
	}

	public JRadioButton(String text, boolean selected) {
		this(text, null, selected);
	}

	public JRadioButton(String text, Icon icon) {
		this(text, icon, false);
	}

	public JRadioButton(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
		setBorderPainted(false);
		setHorizontalAlignment(LEADING);
	}

	public void setFont(Font font) {
		super.setFont(font);
		RadioButton snap = radioButton;
		if (snap != null) {
			UICore.setTextSize(snap, font, getScreenAdapterAdAPI());
		}
	}

	public void setSelected(final boolean b) {
		super.setSelected(b);
		final RadioButton snap = radioButton;
		if (snap != null) {
			AndroidUIUtil.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					snap.setChecked(b);
				}
			});
		}
	}

	public View getFocusablePeerViewAdAPI() {
		return radioButton;
	}

	public void updateUI() {
		AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				if (defaultLinearLayout == null) {
					defaultLinearLayout = new LinearLayout(ActivityManager.applicationContext);
					JRadioButton.this.setPeerAdAPI(defaultLinearLayout);
					JRadioButton.this.addOnLayoutChangeListenerAdAPI(defaultLinearLayout);
				}
				defaultLinearLayout.removeAllViews();
				// defaultLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

				viewRelation.unregisterView(radioButton);
				radioButton = null;

				if (JRadioButton.this.isVisible) {
					radioButton = new RadioButton(ActivityManager.applicationContext);

					boolean enabled = JRadioButton.this.isEnabled();
					radioButton.setEnabled(enabled);
					radioButton.setTextColor(enabled ? AndroidUIUtil.WIN_FONT_COLOR.toAndroid()
							: AndroidUIUtil.WIN_FONT_DISABLE_COLOR.toAndroid());
					UICore.setTextSize(radioButton, JRadioButton.this.getFont(),
							JRadioButton.this.getScreenAdapterAdAPI());
					radioButton.setText(text);
					AndroidUIUtil.setViewHorizontalAlignment(JRadioButton.this, radioButton,
							getHorizontalAlignment(), true);
					radioButton.setChecked(JRadioButton.this.isSelected());

					AndroidUIUtil.buildListenersForComponent(radioButton, JRadioButton.this);

					LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					lp.gravity = (Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL
							| Gravity.CENTER);
					AndroidUIUtil.addView(defaultLinearLayout, radioButton, lp, viewRelation);
				}
			}
		});
	}

	public String getUIClassID() {
		return uiClassID;
	}

	void setIconFromAction(Action a) {
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	public void setHorizontalAlignment(int alignment) {
		super.setHorizontalAlignment(alignment);
		if (radioButton != null) {
			AndroidUIUtil.setViewHorizontalAlignment(this, radioButton, getHorizontalAlignment(),
					true);
		}
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