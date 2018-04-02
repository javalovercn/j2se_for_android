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
import hc.android.WindowManager;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.plaf.MenuItemUI;

import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import hc.android.HCRUtil;

/**
 * An implementation of an item in a menu. A menu item is essentially a button
 * sitting in a list. When the user selects the "button", the action associated
 * with the menu item is performed. A <code>JMenuItem</code> contained in a
 * <code>JPopupMenu</code> performs exactly that function.
 * <p>
 * Menu items can be configured, and to some degree controlled, by
 * <code><a href="Action.html">Action</a></code>s. Using an <code>Action</code>
 * with a menu item has many benefits beyond directly configuring a menu item.
 * Refer to <a href="Action.html#buttonActions"> Swing Components Supporting
 * <code>Action</code></a> for more details, and you can find more information
 * in <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/misc/action.html">How to Use
 * Actions</a>, a section in <em>The Java Tutorial</em>.
 * <p>
 * For further documentation and for examples, see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/menu.html">How to
 * Use Menus</a> in <em>The Java Tutorial.</em>
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
 * @beaninfo attribute: isContainer false description: An item which can be
 *           selected in a menu.
 *
 * @author Georges Saab
 * @author David Karlton
 * @see JPopupMenu
 * @see JMenu
 * @see JCheckBoxMenuItem
 * @see JRadioButtonMenuItem
 */
public class JMenuItem extends AbstractButton implements Accessible, MenuElement {
	private static final String uiClassID = "MenuItemUI";
	private LinearLayout defaultLinearLayout;
	private TextView textView;

	public JMenuItem() {
		this(null, (Icon) null);
	}

	public JMenuItem(Icon icon) {
		this(null, icon);
	}

	public JMenuItem(String text) {
		this(text, (Icon) null);
	}

	public JMenuItem(Action a) {
		this();
		setAction(a);
	}

	public JMenuItem(String text, Icon icon) {
		super.init(text, icon);
	}

	public JMenuItem(String text, int mnemonic) {
		this(text);
	}

	public void setModel(ButtonModel newModel) {
		super.setModel(newModel);// 必须
	}

	void initFocusability() {
	}

	public void setUI(MenuItemUI ui) {
		super.setUI(ui);
	}

	@Override
	public void requestFocus() {
		if (defaultLinearLayout != null) {
			defaultLinearLayout.requestFocus();
		}
	}

	@Override
	public View getPeerAdAPI() {
		if (defaultLinearLayout == null) {
			defaultLinearLayout = new LinearLayout(ActivityManager.applicationContext);
			setPeerAdAPI(defaultLinearLayout);

			focusListener = new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
				}

				@Override
				public void focusGained(FocusEvent e) {
					if (getPopupMenuAdAPI().isDismissPopupMenuAdAPI()) {
						return;
					}

					if (JMenuItem.this instanceof JMenu) {
						getPopupMenuAdAPI().showSubMenu((JMenu) JMenuItem.this);
					} else {
						getPopupMenuAdAPI().showSubMenu(null);// 关闭旧的
						if (defaultLinearLayout.isInTouchMode()) {
							if (getPopupMenuAdAPI().isFirstFocusOnPopupMenuAdAPI()) {
							} else {
								JMenuItem.this.fireActionPerformed(new ActionEvent(JMenuItem.this,
										ActionEvent.ACTION_PERFORMED, ""));
							}
						}
					}
				}
			};
			addFocusListener(focusListener);

			AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
				@Override
				public void run() {
					updateUI();
				}
			});
		}
		return defaultLinearLayout;
	}

	public void applyComponentOrientation(final ComponentOrientation o) {
		super.applyComponentOrientation(o);
		updateUI();
	}

	FocusListener focusListener;

	public void updateUI() {
		if (defaultLinearLayout == null) {
			return;
		}

		rebuildUI();

		defaultLinearLayout
				.setBackgroundResource(HCRUtil.getResource(HCRUtil.R_drawable_tree_node));

		defaultLinearLayout.setClickable(isEnable);
		defaultLinearLayout.setFocusable(isEnable);
		defaultLinearLayout.setFocusableInTouchMode(true);// 保证JMenu/JMenuItem初次展开时，能显示焦点状态。另见JPopupMenu.setFocusableOnlyToJMenu

		AndroidUIUtil.buildListenersForComponent(defaultLinearLayout, this);

		defaultLinearLayout.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {

				if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {// 先于Focus之前
					if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_RIGHT) {
						if (JMenuItem.this instanceof JMenu
								&& JMenuItem.this.getComponentOrientation().isLeftToRight()) {
							return false;
						}
					}

					if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_LEFT) {
						if (JMenuItem.this instanceof JMenu && (JMenuItem.this
								.getComponentOrientation().isLeftToRight() == false)) {
							return false;
						}
					}
				} else {
					if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
						if (getPopupMenuAdAPI().dismissMenuAdAPI()) {
							return true;
						}
					}

					if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER) {
						JMenuItem.this.fireActionPerformed(
								new ActionEvent(JMenuItem.this, ActionEvent.ACTION_PERFORMED, ""));
						return true;
					}
				}
				return false;
			}
		});
	}

	private void rebuildUI() {
		if (defaultLinearLayout == null) {
			return;
		}

		AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				defaultLinearLayout.removeAllViews();
				boolean isLeftToRight = getComponentOrientation().isLeftToRight();

				if (isLeftToRight) {
					addCheckIcon(isLeftToRight);
					addItemIcon(isLeftToRight);
					addText(isLeftToRight);
					if (JMenuItem.this instanceof JMenu) {
						addMenuExpand(isLeftToRight);
					}
				} else {
					if (JMenuItem.this instanceof JMenu) {
						addMenuExpand(isLeftToRight);
					}
					addText(isLeftToRight);
					addItemIcon(isLeftToRight);
					addCheckIcon(isLeftToRight);
				}
			}
		});
	}

	private void addMenuExpand(boolean isLeftToRight) {
		String charSet = (isLeftToRight ? "  >" : "<  ");

		TextView charSetView = new TextView(ActivityManager.applicationContext);
		charSetView.setText(charSet);
		UICore.setTextSize(charSetView, getScreenAdapterAdAPI());
		charSetView.setTextColor(isEnable ? AndroidUIUtil.WIN_FONT_COLOR.toAndroid()
				: AndroidUIUtil.WIN_FONT_DISABLE_COLOR.toAndroid());
		charSetView.setGravity(
				Gravity.CENTER_VERTICAL | (isLeftToRight ? Gravity.RIGHT : Gravity.LEFT));
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
				1.0F);
		lp.gravity = charSetView.getGravity();

		AndroidUIUtil.addView(defaultLinearLayout, charSetView, lp, viewRelation);
	}

	private void addText(boolean isLeftToRight) {
		textView = new TextView(ActivityManager.applicationContext);
		textView.setText(text);
		textView.setSingleLine(true);
		textView.setTextColor(isEnable ? AndroidUIUtil.WIN_FONT_COLOR.toAndroid()
				: AndroidUIUtil.WIN_FONT_DISABLE_COLOR.toAndroid());
		UICore.setTextSize(textView, getScreenAdapterAdAPI());
		textView.setMaxWidth(300);
		textView.setEllipsize(TruncateAt.MARQUEE);
		textView.setGravity(
				Gravity.CENTER_VERTICAL | (isLeftToRight ? Gravity.LEFT : Gravity.RIGHT));
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.gravity = textView.getGravity();
		if (isLeftToRight) {
			lp.leftMargin = 5;
		} else {
			lp.rightMargin = 5;
		}
		AndroidUIUtil.addView(defaultLinearLayout, textView, lp, viewRelation);
	}

	private void addItemIcon(boolean isLeftToRight) {
		Icon icon = isEnable ? defaultIcon : disabledIcon;
		if (icon == null) {
			return;
		}

		ImageView iconView = new ImageView(ActivityManager.applicationContext);
		iconView.setImageDrawable(ImageIcon.getAdapterBitmapDrawableAdAPI((ImageIcon) icon, this));

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.gravity = (Gravity.CENTER_VERTICAL | (isLeftToRight ? Gravity.LEFT : Gravity.RIGHT));

		AndroidUIUtil.addView(defaultLinearLayout, iconView, lp, viewRelation);
	}

	private void addCheckIcon(boolean isLeftToRight) {
		if (toggleIsSelected && ((this instanceof JCheckBoxMenuItem)
				|| (this instanceof JRadioButtonMenuItem))) {
			CheckBox checkView = new CheckBox(ActivityManager.applicationContext);
			checkView.setChecked(true);

			checkView.setFocusable(false);
			checkView.setClickable(false);
			checkView.setFocusableInTouchMode(false);

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.gravity = (Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL
					| (isLeftToRight ? Gravity.LEFT : Gravity.RIGHT));

			AndroidUIUtil.addView(defaultLinearLayout, checkView, lp, viewRelation);
		} else {
			// 空白块
			ImageView imgView = new ImageView(ActivityManager.applicationContext);
			imgView.setMinimumHeight(1);
			imgView.setMinimumWidth(AndroidUIUtil.getCheckBoxWidth());

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.gravity = (Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL
					| (isLeftToRight ? Gravity.LEFT : Gravity.RIGHT));

			AndroidUIUtil.addView(defaultLinearLayout, imgView, lp, viewRelation);
		}
	}

	public void processActionListenerAdAPI(ActionEvent event) {
		if (this instanceof JMenu) {
			getPeerAdAPI().requestFocus();
		} else {
			getPopupMenuAdAPI().dismissMenuAdAPI();
			super.processActionListenerAdAPI(event);
		}
	}

	@Override
	public void fireActionPerformed(ActionEvent event) {
		super.fireActionPerformed(event);
	}

	public void setSelected(boolean b) {
		if (b == toggleIsSelected) {
			return;
		}

		super.setSelected(b);
		if (model != null) {
			model.setSelected(b);
		}

		rebuildUI();
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public void setArmed(boolean b) {
	}

	public boolean isArmed() {
		return false;
	}

	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);

		if (defaultLinearLayout != null) {
			defaultLinearLayout.setClickable(isEnable);
			defaultLinearLayout.setFocusable(isEnable);
			defaultLinearLayout.setFocusableInTouchMode(false);
		}

		if (textView != null) {
			textView.setTextColor(isEnable ? AndroidUIUtil.WIN_FONT_COLOR.toAndroid()
					: AndroidUIUtil.WIN_FONT_DISABLE_COLOR.toAndroid());
		}
	}

	boolean alwaysOnTop() {
		return false;
	}

	private KeyStroke accelerator;

	public void setAccelerator(KeyStroke keyStroke) {
	}

	public KeyStroke getAccelerator() {
		return this.accelerator;
	}

	protected void configurePropertiesFromAction(Action a) {
	}

	void setIconFromAction(Action a) {
	}

	void largeIconChanged(Action a) {
	}

	void smallIconChanged(Action a) {
	}

	void configureAcceleratorFromAction(Action a) {
	}

	protected void actionPropertyChanged(Action action, String propertyName) {
	}

	public void processMouseEvent(MouseEvent e, MenuElement path[], MenuSelectionManager manager) {
	}

	public void processKeyEvent(KeyEvent e, MenuElement path[], MenuSelectionManager manager) {
	}

	public void processMenuDragMouseEvent(MenuDragMouseEvent e) {
	}

	public void processMenuKeyEvent(MenuKeyEvent e) {
	}

	protected void fireMenuDragMouseEntered(MenuDragMouseEvent event) {
	}

	protected void fireMenuDragMouseExited(MenuDragMouseEvent event) {
	}

	protected void fireMenuDragMouseDragged(MenuDragMouseEvent event) {
	}

	protected void fireMenuDragMouseReleased(MenuDragMouseEvent event) {
	}

	protected void fireMenuKeyPressed(MenuKeyEvent event) {
	}

	protected void fireMenuKeyReleased(MenuKeyEvent event) {
	}

	protected void fireMenuKeyTyped(MenuKeyEvent event) {
	}

	public void menuSelectionChanged(boolean isIncluded) {
	}

	public MenuElement[] getSubElements() {
		return new MenuElement[0];
	}

	public Component getComponent() {
		return this;
	}

	public void addMenuDragMouseListener(MenuDragMouseListener l) {
	}

	public void removeMenuDragMouseListener(MenuDragMouseListener l) {
	}

	public MenuDragMouseListener[] getMenuDragMouseListeners() {
		return list.getListeners(MenuDragMouseListener.class);
	}

	public void addMenuKeyListener(MenuKeyListener l) {
	}

	public void removeMenuKeyListener(MenuKeyListener l) {
	}

	public MenuKeyListener[] getMenuKeyListeners() {
		return list.getListeners(MenuKeyListener.class);
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
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

	private JPopupMenu getPopupMenuAdAPI() {
		return (JPopupMenu) getParent();
	}

}