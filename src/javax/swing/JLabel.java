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
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.plaf.LabelUI;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * A display area for a short text string or an image, or both. A label does not
 * react to input events. As a result, it cannot get the keyboard focus. A label
 * can, however, display a keyboard alternative as a convenience for a nearby
 * component that has a keyboard alternative but can't display it.
 * <p>
 * A <code>JLabel</code> object can display either text, an image, or both. You
 * can specify where in the label's display area the label's contents are
 * aligned by setting the vertical and horizontal alignment. By default, labels
 * are vertically centered in their display area. Text-only labels are leading
 * edge aligned, by default; image-only labels are horizontally centered, by
 * default.
 * <p>
 * You can also specify the position of the text relative to the image. By
 * default, text is on the trailing edge of the image, with the text and image
 * vertically aligned.
 * <p>
 * A label's leading and trailing edge are determined from the value of its
 * {@link java.awt.ComponentOrientation} property. At present, the default
 * ComponentOrientation setting maps the leading edge to left and the trailing
 * edge to right.
 *
 * <p>
 * Finally, you can use the <code>setIconTextGap</code> method to specify how
 * many pixels should appear between the text and the image. The default is 4
 * pixels.
 * <p>
 * See <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/label.html">How
 * to Use Labels</a> in <em>The Java Tutorial</em> for further documentation.
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
 * @beaninfo attribute: isContainer false description: A component that displays
 *           a short string and an icon.
 *
 * @author Hans Muller
 */
public class JLabel extends JComponent implements SwingConstants, Accessible {
	private static final String uiClassID = "LabelUI";

	private int mnemonic = '\0';
	private int mnemonicIndex = -1;

	private String text = "";
	private Icon defaultIcon = null;
	private Icon disabledIcon = null;
	private boolean disabledIconSet = false;

	private int verticalAlignment = CENTER;
	private int horizontalAlignment = LEADING;
	private int verticalTextPosition = CENTER;
	private int horizontalTextPosition = TRAILING;
	private int iconTextGap = 4;
	private LinearLayout defaultLinearLayout;
	private ImageView iconView;
	public final TextView textView;
	protected Component labelFor = null;

	static final String LABELED_BY_PROPERTY = "labeledBy";

	public JLabel(String text, Icon icon, int horizontalAlignment) {
		if (text != null) {
			this.text = (text);
		}

		textView = new TextView(ActivityManager.applicationContext);
		textView.setTextColor(AndroidUIUtil.WIN_FONT_COLOR.toAndroid());

		this.defaultIcon = (icon);
		this.horizontalAlignment = checkHorizontalKey(horizontalAlignment, "horizontalAlignment");
		updateUI();
		setAlignmentX(LEFT_ALIGNMENT);
	}

	public JLabel(String text, int horizontalAlignment) {
		this(text, null, horizontalAlignment);
	}

	public JLabel(String text) {
		this(text, null, LEADING);
	}

	public JLabel(Icon image, int horizontalAlignment) {
		this(null, image, horizontalAlignment);
	}

	public JLabel(Icon image) {
		this(null, image, CENTER);
	}

	public JLabel() {
		this("", null, LEADING);
	}

	public LabelUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return (LabelUI) null;
	}

	public void setUI(LabelUI ui) {
		AndroidClassUtil.callEmptyMethod();
		super.setUI(ui);
	}

	public void setFont(Font font) {
		super.setFont(font);
		TextView snap = textView;
		if (snap != null) {
			UICore.setTextSize(snap, font, getScreenAdapterAdAPI());
		}
	}

	public void applyComponentOrientation(ComponentOrientation o) {
		super.applyComponentOrientation(o);
		TextView snap = textView;
		if (snap != null) {
			snap.setGravity(o.isLeftToRight() ? Gravity.LEFT : Gravity.RIGHT);
		}
	}

	public void updateUI() {
		AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				if (defaultLinearLayout == null) {
					defaultLinearLayout = new LinearLayout(ActivityManager.applicationContext);
					setPeerAdAPI(defaultLinearLayout);
					JLabel.this.addOnLayoutChangeListenerAdAPI(defaultLinearLayout);
				}
				// defaultLinearLayout.setBackgroundColor(UIUtil.WIN_BODY_BACK.toAndroid());
				defaultLinearLayout.removeAllViews();
				// defaultLinearLayout.setFocusable(true);
				defaultLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

				if (JLabel.this.isVisible) {
					final boolean hasText = (text.length() != 0);

					if (getIcon() != null) {
						LinearLayout.LayoutParams lp;
						if (hasText) {
							lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT);
							lp.gravity = Gravity.CENTER_VERTICAL;
						} else {
							// 仅图片时，要居中
							lp = new LayoutParams(LayoutParams.MATCH_PARENT,
									LayoutParams.WRAP_CONTENT);
							lp.gravity = (Gravity.CENTER);
						}

						iconView = new ImageView(ActivityManager.applicationContext);
						final Icon icon = isEnable ? getIcon() : getDisabledIcon();
						iconView.setImageDrawable(ImageIcon
								.getAdapterBitmapDrawableAdAPI((ImageIcon) icon, JLabel.this));
						iconView.setFocusable(false);
						AndroidUIUtil.addView(defaultLinearLayout, iconView, lp, viewRelation);
					} else {
						iconView = null;
					}

					if (hasText) {
						LinearLayout.LayoutParams lp = new LayoutParams(
								ViewGroup.LayoutParams.MATCH_PARENT,
								ViewGroup.LayoutParams.WRAP_CONTENT, 1.0F);
						lp.gravity = Gravity.CENTER;

						//以下要置于setText之前，否则可能产生移动
						UICore.setTextSize(textView, JLabel.this.getFont(),
								JLabel.this.getScreenAdapterAdAPI());
						textView.setFocusable(false);
						textView.setGravity(AndroidUIUtil.convertToGravity(horizontalAlignment, getComponentOrientation()));
						
						if (AndroidUIUtil.setTextForTextViewAdAPI(text, textView) == false) {
							textView.setLines(1);// 不含html。不能设置setSingleLine()，会导致表头字重叠
						}
						AndroidUIUtil.addView(defaultLinearLayout, textView, lp, viewRelation);
					}
					defaultLinearLayout.setMinimumHeight(AndroidUIUtil.getDefaultTextViewHeight(
							JLabel.this.getFont(), JLabel.this.getScreenAdapterAdAPI()));// 在无文本内容时，计算高度
				}
			}
		});
	}

	public void setForeground(Color fg) {
		if (textView != null && fg != null) {
			textView.setTextColor(fg.toAndroid());
		}
	}

	public void setBackground(Color color) {
		if (textView != null && color != null) {
			textView.setBackgroundColor(color.toAndroid());
		}
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text == null) {
			return;
		}
		final String oldValue = this.text;
		boolean isChanged = (oldValue.equals(text) == false);
		if (isChanged) {
			this.text = text;
			firePropertyChange("text", oldValue, text);
			updateUI();
		}
		return;

	}

	public void setTextAdAPI(String text) {
		this.text = text;
	}

	public Icon getIcon() {
		return defaultIcon;
	}

	public void setIcon(Icon icon) {
		Icon oldValue = defaultIcon;
		defaultIcon = icon;

		if (defaultIcon != oldValue) {
			refreshIconToMobile(oldValue);
			updateUI();
		}
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

	public void setEnabled(final boolean enabled) {
		boolean changed = (super.isEnable != enabled);
		super.setEnabled(enabled);
		if (changed) {
			refreshIconToMobile(null);
		}
	}

	private final void refreshIconToMobile(final Icon oldValue) {
		if (isEnable) {
			firePropertyChange("icon", oldValue, defaultIcon);
		} else {
			firePropertyChange("disabledIcon", oldValue, disabledIcon);
		}
	}

	public void setDisabledIcon(Icon disabledIcon) {
		if (disabledIcon == null) {
			return;
		}

		Icon oldValue = disabledIcon;
		boolean isChanged = (oldValue != disabledIcon);
		this.disabledIcon = disabledIcon;
		if (isChanged) {
			refreshIconToMobile(oldValue);
			updateUI();
		}
	}

	public void setDisplayedMnemonic(int key) {
		mnemonic = key;
	}

	public void setDisplayedMnemonic(char aChar) {
		int vk = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(aChar);
		if (vk != java.awt.event.KeyEvent.VK_UNDEFINED) {
			setDisplayedMnemonic(vk);
		}
	}

	public int getDisplayedMnemonic() {
		return mnemonic;
	}

	public void setDisplayedMnemonicIndex(int index) throws IllegalArgumentException {
		if (index == -1) {
			mnemonicIndex = -1;
		} else {
			String text = getText();
			int textLength = (text == null) ? 0 : text.length();
			if (index < -1 || index >= textLength) {
				throw new IllegalArgumentException("index == " + index);
			}
		}
		mnemonicIndex = index;
	}

	public int getDisplayedMnemonicIndex() {
		return mnemonicIndex;
	}

	protected int checkHorizontalKey(int key, String message) {
		if ((key == LEFT) || (key == CENTER) || (key == RIGHT) || (key == LEADING)
				|| (key == TRAILING)) {
			return key;
		} else {
			throw new IllegalArgumentException(message);
		}
	}

	protected int checkVerticalKey(int key, String message) {
		if ((key == TOP) || (key == CENTER) || (key == BOTTOM)) {
			return key;
		} else {
			throw new IllegalArgumentException(message);
		}
	}

	public int getIconTextGap() {
		return iconTextGap;
	}

	public void setIconTextGap(int iconTextGap) {
		this.iconTextGap = iconTextGap;
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

	public void setHorizontalAlignment(final int alignment) {
		if (alignment == horizontalAlignment) {
			return;
		}
		AndroidUIUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				horizontalAlignment = checkHorizontalKey(alignment, "horizontalAlignment");
				textView.setGravity(AndroidUIUtil.convertToGravity(horizontalAlignment, getComponentOrientation()));
			}
		});
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
		int old = horizontalTextPosition;
		this.horizontalTextPosition = checkHorizontalKey(textPosition, "horizontalTextPosition");
	}

	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
		if (!isShowing() || !SwingUtilities.doesIconReferenceImage(getIcon(), img)
				&& !SwingUtilities.doesIconReferenceImage(disabledIcon, img)) {

			return false;
		}
		return false;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	protected String paramString() {
		return "";
	}

	public Component getLabelFor() {
		AndroidClassUtil.callEmptyMethod();
		return labelFor;
	}

	public void setLabelFor(Component c) {
		AndroidClassUtil.callEmptyMethod();
		labelFor = c;
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

}
