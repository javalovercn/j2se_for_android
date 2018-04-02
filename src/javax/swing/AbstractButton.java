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
import hc.android.ImageUtil;
import hc.android.JHCComponent;
import hc.android.UICore;
import hc.core.util.ThreadPriorityManager;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ButtonUI;
import android.graphics.Bitmap;
import android.view.ViewGroup;

/**
 * Defines common behaviors for buttons and menu items.
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
 * For further information see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/button.html">How
 * to Use Buttons, Check Boxes, and Radio Buttons</a>, a section in <em>The Java
 * Tutorial</em>.
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
public abstract class AbstractButton extends JHCComponent
		implements ItemSelectable, SwingConstants {
	public static final String MODEL_CHANGED_PROPERTY = "model";
	public static final String TEXT_CHANGED_PROPERTY = "text";
	public static final String MNEMONIC_CHANGED_PROPERTY = "mnemonic";

	public static final String MARGIN_CHANGED_PROPERTY = "margin";
	public static final String VERTICAL_ALIGNMENT_CHANGED_PROPERTY = "verticalAlignment";
	public static final String HORIZONTAL_ALIGNMENT_CHANGED_PROPERTY = "horizontalAlignment";

	public static final String VERTICAL_TEXT_POSITION_CHANGED_PROPERTY = "verticalTextPosition";
	public static final String HORIZONTAL_TEXT_POSITION_CHANGED_PROPERTY = "horizontalTextPosition";

	public static final String BORDER_PAINTED_CHANGED_PROPERTY = "borderPainted";
	public static final String FOCUS_PAINTED_CHANGED_PROPERTY = "focusPainted";
	public static final String ROLLOVER_ENABLED_CHANGED_PROPERTY = "rolloverEnabled";
	public static final String CONTENT_AREA_FILLED_CHANGED_PROPERTY = "contentAreaFilled";

	public static final String ICON_CHANGED_PROPERTY = "icon";

	public static final String PRESSED_ICON_CHANGED_PROPERTY = "pressedIcon";
	public static final String SELECTED_ICON_CHANGED_PROPERTY = "selectedIcon";

	public static final String ROLLOVER_ICON_CHANGED_PROPERTY = "rolloverIcon";
	public static final String ROLLOVER_SELECTED_ICON_CHANGED_PROPERTY = "rolloverSelectedIcon";

	public static final String DISABLED_ICON_CHANGED_PROPERTY = "disabledIcon";
	public static final String DISABLED_SELECTED_ICON_CHANGED_PROPERTY = "disabledSelectedIcon";

	protected ButtonModel model = null;

	protected String text = "";
	private Insets margin = null;
	private Insets defaultMargin = null;

	protected Icon defaultIcon = null;
	protected Icon pressedIcon = null;
	protected Icon disabledIcon = null;

	protected Icon selectedIcon = null;
	protected Icon disabledSelectedIcon = null;

	protected Icon rolloverIcon = null;
	protected Icon rolloverSelectedIcon = null;

	private boolean paintBorder = true;
	private boolean paintFocus = true;
	private boolean rolloverEnabled = false;
	private boolean contentAreaFilled = true;

	private int verticalAlignment = CENTER;
	private int horizontalAlignment = CENTER;

	private int verticalTextPosition = CENTER;
	private int horizontalTextPosition = TRAILING;

	private int iconTextGap = 4;

	private int mnemonic;
	private int mnemonicIndex = -1;
	private long multiClickThreshhold = 0;
	boolean defaultCapable = true;
	private Handler handler;

	private boolean hideActionText = false;

	public void setHideActionText(boolean hideActionText) {
		this.hideActionText = hideActionText;
	}

	public boolean getHideActionText() {
		return hideActionText;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		trigPropertyChangeAdAPI(text);
		updateUI();
	}

	protected void trigPropertyChangeAdAPI(String text) {
		boolean isChanged = (this.text != text);
		Object oldValue = this.text;
		this.text = text;
		if (isChanged) {
			firePropertyChange(TEXT_CHANGED_PROPERTY, oldValue, text);
		}
	}

	boolean toggleIsSelected;

	public boolean isSelected() {
		return toggleIsSelected;
	}

	public void setSelected(boolean b) {
		if (b != toggleIsSelected) {
			toggleIsSelected = b;

			ItemEvent itemEvent = new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, this,
					b ? ItemEvent.SELECTED : ItemEvent.DESELECTED);
			fireItemStateChanged(itemEvent);

			ChangeEvent changeEvent = new ChangeEvent(this);
			fireChangeListener(changeEvent);// updateUI分散到实例内
		}
	}

	public void setEnabled(boolean enabled) {
		boolean changed = (super.isEnable != enabled);
		super.setEnabled(enabled);// supe call updateUI

		if (changed) {
			ChangeEvent changeEvent = new ChangeEvent(this);
			fireChangeListener(changeEvent);

			refreshIconToMobile(null);
		}
	}

	private final void refreshIconToMobile(final Icon oldValue) {
		if (isEnable) {
			firePropertyChange(ICON_CHANGED_PROPERTY, oldValue, defaultIcon);
		} else {
			firePropertyChange(DISABLED_ICON_CHANGED_PROPERTY, oldValue, disabledIcon);
		}
	}

	public void processActionListenerAdAPI(ActionEvent event) {
		try {
			ActionListener[] listeners = getActionListeners();
			if (listeners != null) {
				for (int i = listeners.length - 1; i >= 0; i--) {// 先入先执行，因为取出Listeners时，是先入为后.
					listeners[i].actionPerformed(event);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void setVisible(boolean isVisible) {
		boolean changed = (isVisible != super.isVisible);

		if (changed) {
			super.setVisible(isVisible);
			updateUI();
		}
	}

	public void doClick() {
		doClick(ThreadPriorityManager.UI_WAIT_MS);
	}

	public void doClick(int pressTime) {
		if (getPeerAdAPI().performClick() == false) {
			// 生成事件
			fireActionPerformed(
					new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getActionCommand()));
		}
	}

	public void setMargin(Insets m) {
		Insets old = margin;
		margin = m;
		firePropertyChange(MARGIN_CHANGED_PROPERTY, old, m);
		ViewGroup.LayoutParams para = getPeerAdAPI().getLayoutParams();
		if (para instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) para;
			mlp.setMargins(m.left, m.top, m.right, m.bottom);
			getPeerAdAPI().setLayoutParams(mlp);
		}
	}

	/**
	 * 如果没有设置，则返回null
	 * 
	 * @return
	 */
	public Insets getMargin() {
		return (margin == null) ? null : margin;
	}

	public Icon getIcon() {
		return defaultIcon;
	}

	public void setIcon(Icon defaultIcon) {
		if (defaultIcon == null || (defaultIcon instanceof ImageIcon) == false) {
			return;
		}
		Icon oldValue = this.defaultIcon;
		this.defaultIcon = defaultIcon;
		boolean isChanged = (oldValue != defaultIcon);
		if (isChanged) {
			refreshIconToMobile(oldValue);
		}
		// 生成缺省的灰色图片
		setDisabledIcon(ImageIcon.toGrayAdAPI((ImageIcon) defaultIcon));
		updateUI();
	}

	public Icon getPressedIcon() {
		return isEnable ? defaultIcon : ((disabledIcon != null) ? disabledIcon : defaultIcon);
	}

	public void setPressedIcon(Icon pressedIcon) {
		this.pressedIcon = pressedIcon;
	}

	public Icon getSelectedIcon() {
		return selectedIcon;
	}

	public void setSelectedIcon(Icon selectedIcon) {
		if (selectedIcon == null || (defaultIcon instanceof ImageIcon) == false) {
			return;
		}

		this.selectedIcon = selectedIcon;
		// 生成缺省的灰色图片
		setDisabledSelectedIcon(ImageIcon.toGrayAdAPI((ImageIcon) selectedIcon));
		updateUI();
	}

	public Icon getRolloverIcon() {
		return rolloverIcon;
	}

	public void setRolloverIcon(Icon rolloverIcon) {
		this.rolloverIcon = rolloverIcon;
		setRolloverEnabled(true);
	}

	public Icon getRolloverSelectedIcon() {
		return rolloverSelectedIcon;
	}

	public void setRolloverSelectedIcon(Icon rolloverSelectedIcon) {
		this.rolloverSelectedIcon = rolloverSelectedIcon;
		setRolloverEnabled(true);
	}

	public Icon getDisabledIcon() {
		if (disabledIcon == null) {
			if (defaultIcon != null) {
				disabledIcon = ImageIcon.toGrayAdAPI((ImageIcon) defaultIcon);
				refreshIconToMobile(null);
			}
		}
		return disabledIcon;
	}

	public void setDisabledIcon(Icon disabledIcon) {
		if (disabledIcon == null) {
			return;
		}
		Icon oldValue = this.disabledIcon;
		this.disabledIcon = disabledIcon;
		boolean isChanged = (oldValue != this.disabledIcon);
		if (isChanged) {
			refreshIconToMobile(oldValue);
		}
		updateUI();
	}

	public Icon getDisabledSelectedIcon() {
		if (disabledSelectedIcon == null) {
			if (selectedIcon != null) {
				disabledSelectedIcon = getSelectedIcon();
			} else {
				return getDisabledIcon();
			}
		}
		return disabledSelectedIcon;
	}

	public void setDisabledSelectedIcon(Icon disabledSelectedIcon) {
		this.disabledSelectedIcon = disabledSelectedIcon;
		updateUI();
	}

	public int getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(int alignment) {
		if (alignment == verticalAlignment)
			return;
		verticalAlignment = checkVerticalKey(alignment, "verticalAlignment");
	}

	public int getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(int alignment) {
		if (alignment == horizontalAlignment)
			return;
		horizontalAlignment = checkHorizontalKey(alignment, "horizontalAlignment");
	}

	public int getVerticalTextPosition() {
		return verticalTextPosition;
	}

	public void setVerticalTextPosition(int textPosition) {
		if (textPosition == verticalTextPosition)
			return;
		verticalTextPosition = checkVerticalKey(textPosition, "verticalTextPosition");
	}

	public int getHorizontalTextPosition() {
		return horizontalTextPosition;
	}

	public void setHorizontalTextPosition(int textPosition) {
		if (textPosition == horizontalTextPosition)
			return;
		horizontalTextPosition = checkHorizontalKey(textPosition, "horizontalTextPosition");
	}

	public int getIconTextGap() {
		return iconTextGap;
	}

	public void setIconTextGap(int iconTextGap) {
		this.iconTextGap = iconTextGap;
	}

	protected int checkHorizontalKey(int key, String exception) {
		if ((key == LEFT) || (key == CENTER) || (key == RIGHT) || (key == LEADING)
				|| (key == TRAILING)) {
			return key;
		} else {
			throw new IllegalArgumentException(exception);
		}
	}

	protected int checkVerticalKey(int key, String exception) {
		if ((key == TOP) || (key == CENTER) || (key == BOTTOM)) {
			return key;
		} else {
			throw new IllegalArgumentException(exception);
		}
	}

	public void removeNotify() {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setActionCommand(String actionCommand) {
		getModel().setActionCommand(actionCommand);
	}

	public String getActionCommand() {
		final ButtonModel btnModel = getModel();
		String ac = null;
		if (btnModel != null) {
			ac = btnModel.getActionCommand();
		}
		if (ac == null) {
			ac = getText();
		}
		return ac;
	}

	private Action action;
	private PropertyChangeListener actionPropertyChangeListener;

	void clientPropertyChanged(Object key, Object oldValue, Object newValue) {
		AndroidClassUtil.callEmptyMethod();
	}

	boolean shouldUpdateSelectedStateFromAction() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	protected void actionPropertyChanged(Action action, String propertyName) {
		AndroidClassUtil.callEmptyMethod();
	}

	private void setDisplayedMnemonicIndexFromAction(Action a, boolean fromPropertyChange) {
		AndroidClassUtil.callEmptyMethod();
	}

	private void setMnemonicFromAction(Action a) {
		AndroidClassUtil.callEmptyMethod();
	}

	void setIconFromAction(Action a) {
		AndroidClassUtil.callEmptyMethod();
	}

	void smallIconChanged(Action a) {
		AndroidClassUtil.callEmptyMethod();
	}

	void largeIconChanged(Action a) {
		AndroidClassUtil.callEmptyMethod();
	}

	protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
		return createActionPropertyChangeListener0(a);
	}

	PropertyChangeListener createActionPropertyChangeListener0(Action a) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public boolean isBorderPainted() {
		return paintBorder;
	}

	public void setBorderPainted(boolean b) {
		paintBorder = b;
		AndroidClassUtil.callEmptyMethod();
	}

	protected void paintBorder(Graphics g) {
		if (isBorderPainted()) {
			super.paintBorder(g);
		}
	}

	public boolean isFocusPainted() {
		return paintFocus;
	}

	public void setFocusPainted(boolean b) {
		paintFocus = b;
	}

	public boolean isContentAreaFilled() {
		return contentAreaFilled;
	}

	public void setContentAreaFilled(boolean b) {
		contentAreaFilled = b;
	}

	public boolean isRolloverEnabled() {
		return rolloverEnabled;
	}

	public void setRolloverEnabled(boolean b) {
		rolloverEnabled = b;
	}

	public int getMnemonic() {
		return mnemonic;
	}

	public void setMnemonic(int mnemonic) {
		AndroidClassUtil.callEmptyMethod();
		int oldValue = getMnemonic();
		model.setMnemonic(mnemonic);
	}

	public void setMnemonic(char mnemonic) {
		int vk = (int) mnemonic;
		if (vk >= 'a' && vk <= 'z')
			vk -= ('a' - 'A');
		setMnemonic(vk);
	}

	public void setDisplayedMnemonicIndex(int index) throws IllegalArgumentException {
		AndroidClassUtil.callEmptyMethod();
	}

	public int getDisplayedMnemonicIndex() {
		return mnemonicIndex;
	}

	private void updateDisplayedMnemonicIndex(String text, int mnemonic) {
		setDisplayedMnemonicIndex(SwingUtilities.findDisplayedMnemonicIndex(text, mnemonic));
	}

	public void setMultiClickThreshhold(long threshhold) {
		if (threshhold < 0) {
			throw new IllegalArgumentException("threshhold must be >= 0");
		}
		this.multiClickThreshhold = threshhold;
	}

	public long getMultiClickThreshhold() {
		return multiClickThreshhold;
	}

	public ButtonModel getModel() {
		return model;
	}

	public void setModel(ButtonModel newModel) {
		model = newModel;
	}

	public ButtonUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setUI(ButtonUI ui) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void updateUI() {
	}

	protected void addImpl(Component comp, Object constraints, int index) {
	}

	/**
	 * Adds a ChangeListener to the button.
	 * 
	 * @param l
	 */
	public void addChangeListener(ChangeListener l) {
		list.add(ChangeListener.class, l);
	}

	public void removeChangeListener(ChangeListener l) {
		list.remove(ChangeListener.class, l);
	}

	public ChangeListener[] getChangeListeners() {
		return list.getListeners(ChangeListener.class);
	}

	protected void fireStateChanged() {
		notifyChangeAdAPI();
	}

	public ActionListener[] getActionListeners() {
		return list.getListeners(ActionListener.class);
	}

	protected ChangeListener createChangeListener() {
		return getHandler();
	}

	public void fireActionPerformed(ActionEvent event) {
		processActionListenerAdAPI(event);
	}

	protected void fireChangeListener(ChangeEvent event) {
		try {
			ChangeListener[] listener = getChangeListeners();
			if (listener != null) {
				for (int i = 0; i < listener.length; i++) {
					listener[i].stateChanged(event);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	protected void fireItemStateChanged(ItemEvent event) {
		try {
			ItemListener[] listener = getItemListeners();
			if (listener != null) {
				for (int i = 0; i < listener.length; i++) {
					listener[i].itemStateChanged(event);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	protected ActionListener createActionListener() {
		return getHandler();
	}

	protected ItemListener createItemListener() {
		return getHandler();
	}

	public String getLabel() {
		return getText();
	}

	public void setLabel(String label) {
		setText(label);
	}

	/**
	 * Adds an ItemListener to the checkbox.
	 */
	public void addItemListener(ItemListener l) {
		list.add(ItemListener.class, l);
	}

	public void removeItemListener(ItemListener l) {
		list.remove(ItemListener.class, l);
	}

	public ItemListener[] getItemListeners() {
		return list.getListeners(ItemListener.class);
	}

	public Object[] getSelectedObjects() {
		if (isSelected() == false) {
			return null;
		}
		Object[] selectedObjects = new Object[1];
		selectedObjects[0] = getText();
		return selectedObjects;
	}

	protected void init(String text, Icon icon) {
		setFont(UICore.buildDefaultDialogButtonFont());

		if (text != null) {
			setText(text);
		}

		if (icon != null) {
			setIcon(icon);
		}

		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (AbstractButton.this instanceof JCheckBox
						|| AbstractButton.this instanceof JCheckBoxMenuItem) {
					setSelected(!toggleIsSelected);// 由各实现更新状态，及刷新
				} else {
					setSelected(true);// 由各实现更新状态，及刷新
				}

				ChangeEvent changeEvent = new ChangeEvent(e.getSource());
				AbstractButton.this.fireChangeListener(changeEvent);
			}
		});

		setAlignmentX(LEFT_ALIGNMENT);
		setAlignmentY(CENTER_ALIGNMENT);

		updateUI();
	}

	public Dimension getPreferredSize() {
		return super.getPreferredSize();
	}

	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	void setUIProperty(String propertyName, Object value) {
		AndroidClassUtil.callEmptyMethod();
	}

	protected String paramString() {
		return "text=" + text;
	}

	private Handler getHandler() {
		if (handler == null) {
			handler = new Handler();
		}
		return handler;
	}

	class Handler implements ActionListener, ChangeListener, ItemListener, Serializable {
		public void stateChanged(ChangeEvent e) {
			fireStateChanged();
			repaint();
		}

		public void actionPerformed(ActionEvent event) {
			fireActionPerformed(event);
		}

		public void itemStateChanged(ItemEvent event) {
		}
	}

}