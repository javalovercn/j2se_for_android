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

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;

import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * An implementation of a check box -- an item that can be selected or
 * deselected, and which displays its state to the user. By convention, any
 * number of check boxes in a group can be selected. See <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/button.html">How
 * to Use Buttons, Check Boxes, and Radio Buttons</a> in <em>The Java
 * Tutorial</em> for examples and information on using check boxes.
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
 * @see JRadioButton
 *
 * @beaninfo attribute: isContainer false description: A component which can be
 *           selected or deselected.
 *
 * @author Jeff Dinkins
 */
public class JCheckBox extends JToggleButton implements Accessible {
	private CheckBox checkBox;
	LinearLayout defaultLinearLayout;

	public static final String BORDER_PAINTED_FLAT_CHANGED_PROPERTY = "borderPaintedFlat";

	private boolean flat = false;

	private static final String uiClassID = "CheckBoxUI";

	public void applyComponentOrientation(ComponentOrientation o) {
		super.applyComponentOrientation(o);
		setHorizontalAlignment(getHorizontalAlignment());
	}

	public JCheckBox(int drawabel) {
		this(null, null, false, drawabel);
	}

	public JCheckBox() {
		this(null, null, false);
	}

	public JCheckBox(Icon icon) {
		this(null, icon, false);
	}

	public JCheckBox(Icon icon, boolean selected) {
		this(null, icon, selected);
	}

	public JCheckBox(String text) {
		this(text, null, false);
	}

	public JCheckBox(Action a) {
		this();
		setAction(a);
	}

	public JCheckBox(String text, boolean selected) {
		this(text, null, selected);
	}

	public JCheckBox(String text, Icon icon) {
		this(text, icon, false);
	}

	public JCheckBox(String text, Icon icon, boolean selected, int drawable) {
		super(text, icon, selected, drawable);
		setUIProperty("borderPainted", Boolean.FALSE);
		setHorizontalAlignment(LEADING);
	}

	public JCheckBox(String text, Icon icon, boolean selected) {
		this(text, icon, selected, 0);
	}

	public void setBorderPaintedFlat(boolean b) {
		flat = b;
	}

	public boolean isBorderPaintedFlat() {
		return flat;
	}

	public void setSelected(final boolean b) {
		if (b != toggleIsSelected) {
			super.setSelected(b);
			final CheckBox snap = checkBox;
			if (snap != null) {
				AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
					@Override
					public void run() {
						snap.setChecked(b);
					}
				});
			}
		}
	}

	public void setFont(Font font) {
		super.setFont(font);
		CheckBox snap = checkBox;
		if (snap != null) {
			UICore.setTextSize(snap, font, getScreenAdapterAdAPI());
		}
	}

	public void setBackground(Color bg) {
		if (bg == null) {
			return;
		}

		if (checkBox != null) {
			checkBox.setBackgroundColor(bg.toAndroid());
		}
	}

	@Override
	public View getPeerAdAPI() {
		if (defaultLinearLayout == null) {
			updateUI();
		}
		return super.getPeerAdAPI();
	}

	public void updateUI() {
		AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				if (defaultLinearLayout == null) {
					defaultLinearLayout = new LinearLayout(ActivityManager.applicationContext);
					JCheckBox.this.setPeerAdAPI(defaultLinearLayout);
					JCheckBox.this.addOnLayoutChangeListenerAdAPI(defaultLinearLayout);
				}
				defaultLinearLayout.removeAllViews();
				// defaultLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

				viewRelation.unregisterView(checkBox);
				checkBox = null;

				if (JCheckBox.this.isVisible) {
					checkBox = new CheckBox(ActivityManager.applicationContext);

					if (checkBoxForTableDrawable != 0) {
						// 缺省JCheckBox在Letv表格环境下不正常，需定制drawabel
						checkBox.setButtonDrawable(checkBoxForTableDrawable);
					}

					boolean enabled = JCheckBox.this.isEnabled();
					checkBox.setEnabled(enabled);
					checkBox.setTextColor(enabled ? AndroidUIUtil.WIN_FONT_COLOR.toAndroid()
							: AndroidUIUtil.WIN_FONT_DISABLE_COLOR.toAndroid());
					UICore.setTextSize(checkBox, JCheckBox.this.getFont(),
							JCheckBox.this.getScreenAdapterAdAPI());
					checkBox.setText(text);
					AndroidUIUtil.setViewHorizontalAlignment(JCheckBox.this, checkBox,
							getHorizontalAlignment(), true);
					checkBox.setChecked(JCheckBox.this.isSelected());
					AndroidUIUtil.buildListenersForComponent(checkBox, JCheckBox.this);

					LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					lp.gravity = (Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL
							| Gravity.CENTER);
					AndroidUIUtil.addView(defaultLinearLayout, checkBox, lp, viewRelation);
				}
			}
		});
	}

	public void setHorizontalAlignment(int alignment) {
		super.setHorizontalAlignment(alignment);
		if (checkBox != null) {
			AndroidUIUtil.setViewHorizontalAlignment(this, checkBox, getHorizontalAlignment(),
					true);
		}
	}

	public View getFocusablePeerViewAdAPI() {
		if (checkBox == null) {
			updateUI();
		}
		return checkBox;
	}

	public String getUIClassID() {
		return uiClassID;
	}

	void setIconFromAction(Action a) {
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
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
