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

import hc.android.HCRUtil;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * An implementation of a "push" button.
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
 * Tutorial</em> for information and examples of using buttons.
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
 *           \"push\" button.
 *
 * @author Jeff Dinkins
 */
public class JButton extends AbstractButton implements Accessible {
	LinearLayout buttonLinearLayout, defaultLinearLayout;
	ImageView imageButton;
	TextView btnTextView;

	private static final String uiClassID = "ButtonUI";

	public JButton() {
		this(null, null);
	}

	public JButton(Icon icon) {
		this(null, icon);
	}

	public JButton(String text) {
		this(text, null);
	}

	public JButton(Action a) {
		this();
		setAction(a);
	}

	public JButton(String text, Icon icon) {
		init(text, icon);
		defaultLinearLayout = new LinearLayout(ActivityManager.applicationContext);
		buttonLinearLayout = new LinearLayout(ActivityManager.applicationContext);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		lp.topMargin = lp.bottomMargin = lp.leftMargin = lp.rightMargin = AndroidUIUtil.BUTTON_GAP_PX;
		defaultLinearLayout.addView(buttonLinearLayout, lp);

		setPeerAdAPI(defaultLinearLayout);
		this.addOnLayoutChangeListenerAdAPI(defaultLinearLayout);

		updateUI();
	}

	public View getFocusablePeerViewAdAPI() {
		return buttonLinearLayout;
	}

	public void setText(final String text) {
		if (text == null) {
			return;
		}
		if (btnTextView != null) {
			trigPropertyChangeAdAPI(text);
			AndroidUIUtil.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					btnTextView.setText(text);
				}
			});
		} else {
			super.setText(text);
		}
	}

	public void setVisible(boolean isVisible) {
		super.setVisible(isVisible);
	}

	public void setIcon(Icon defaultIcon) {
		super.setIcon(defaultIcon);
	}

	public void applyComponentOrientation(final ComponentOrientation o) {
		super.applyComponentOrientation(o);
		// final TextView snap = btnTextView;
		// if(snap != null){
		// ServerMainActivity.activity.runOnUiThread(new Runnable() {
		// @Override
		// public void run() {
		// snap.setGravity(o.isLeftToRight()?Gravity.LEFT:Gravity.RIGHT);
		// }
		// });
		// }
	}

	public void setDisabledIcon(Icon disabledIcon) {
		super.setDisabledIcon(disabledIcon);
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
	}

	public void setFont(Font font) {
		super.setFont(font);
		TextView snap = btnTextView;
		if (snap != null) {
			UICore.setTextSize(snap, font, getScreenAdapterAdAPI());
		}
	}

	public void updateUI() {
		if (buttonLinearLayout == null) {
			return;
		}

		AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				buttonLinearLayout.removeAllViews();
				buttonLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

				viewRelation.unregisterView(btnTextView);
				btnTextView = null;
				viewRelation.unregisterView(imageButton);
				imageButton = null;

				boolean btnEnabled = JButton.this.isEnabled();

				AndroidUIUtil.buildListenersForComponent(buttonLinearLayout, JButton.this);
				buttonLinearLayout.setEnabled(btnEnabled);

				buttonLinearLayout
						.setBackgroundResource(HCRUtil.getResource(HCRUtil.R_drawable_button));
				buttonLinearLayout.setFocusable(btnEnabled);
				buttonLinearLayout.setFocusableInTouchMode(false);
				buttonLinearLayout.setClickable(btnEnabled);

				if (JButton.this.isVisible) {
					if (JButton.this.getComponentOrientation().isLeftToRight()) {
						addIconAdAPI();
						addTextAdAPI();
					} else {
						addTextAdAPI();
						addIconAdAPI();
					}
				}
			}
		});
	}

	private Icon getDrawableIcon() {
		if (JButton.this.isEnabled() == false && disabledIcon != null) {
			return disabledIcon;
		} else {
			if (defaultIcon != null) {
				return defaultIcon;
			}
		}
		return null;
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public boolean isDefaultButton() {
		return false;
	}

	public boolean isDefaultCapable() {
		return defaultCapable;
	}

	public void setDefaultCapable(boolean defaultCapable) {
	}

	public void removeNotify() {
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	protected String paramString() {
		return "";
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

	private Icon addIconAdAPI() {
		Icon icon = getDrawableIcon();
		if (defaultIcon != null) {
			imageButton = new ImageView(ActivityManager.applicationContext);

			imageButton.setImageDrawable(
					ImageIcon.getAdapterBitmapDrawableAdAPI((ImageIcon) icon, this));

			LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			lp.gravity = (Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL | Gravity.CENTER);

			AndroidUIUtil.addView(buttonLinearLayout, imageButton, lp, viewRelation);
		}
		return icon;
	}

	private final void addTextAdAPI() {
		if (text.length() != 0) {
			LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, 1.0F);
			lp.gravity = (Gravity.CENTER);

			if (btnTextView == null) {
				btnTextView = new TextView(ActivityManager.applicationContext);
				// btnTextView.setSingleLine(true);//会导致JComponent.print(Graphics)时，不出现。
				btnTextView.setMaxWidth(2048);
			}
			btnTextView.setTextColor(isEnable ? AndroidUIUtil.WINDOW_BTN_TEXT_COLOR.toAndroid()
					: AndroidUIUtil.WIN_FONT_DISABLE_COLOR.toAndroid());
			if (AndroidUIUtil.setTextForTextViewAdAPI(text, btnTextView) == false) {
				btnTextView.setLines(1);
			}
			btnTextView.setGravity(Gravity.CENTER);
			UICore.setTextSize(btnTextView, getFont(), getScreenAdapterAdAPI());

			AndroidUIUtil.addView(buttonLinearLayout, btnTextView, lp, viewRelation);
		}
	}

}