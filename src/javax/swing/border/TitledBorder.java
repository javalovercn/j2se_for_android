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
package javax.swing.border;

import hc.android.ActivityManager;
import hc.android.UICore;
import hc.android.AndroidUIUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import hc.android.HCRUtil;

/**
 * A class which implements an arbitrary border with the addition of a String
 * title in a specified position and justification.
 * <p>
 * If the border, font, or color property values are not specified in the
 * constuctor or by invoking the appropriate set methods, the property values
 * will be defined by the current look and feel, using the following property
 * names in the Defaults Table:
 * <ul>
 * <li>&quot;TitledBorder.border&quot;
 * <li>&quot;TitledBorder.font&quot;
 * <li>&quot;TitledBorder.titleColor&quot;
 * </ul>
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author David Kloba
 * @author Amy Fowler
 */
public class TitledBorder extends AbstractBorder {
	protected String title;
	protected Border border;
	protected int titlePosition;
	protected int titleJustification;
	protected Font titleFont;
	protected Color titleColor;

	FrameLayout borderView;
	private Insets insets;
	private TextView titleView;
	private final int titleViewID = 1;
	private View componentView;

	static public final int DEFAULT_POSITION = 0;
	static public final int ABOVE_TOP = 1;
	static public final int TOP = 2;
	static public final int BELOW_TOP = 3;
	static public final int ABOVE_BOTTOM = 4;
	static public final int BOTTOM = 5;
	static public final int BELOW_BOTTOM = 6;

	static public final int DEFAULT_JUSTIFICATION = 0;
	static public final int LEFT = 1;
	static public final int CENTER = 2;
	static public final int RIGHT = 3;
	static public final int LEADING = 4;
	static public final int TRAILING = 5;

	static protected final int EDGE_SPACING = 2;
	static protected final int TEXT_SPACING = 2;
	static protected final int TEXT_INSET_H = 5;

	public TitledBorder(String title) {
		this(null, title, LEADING, DEFAULT_POSITION, null, null);
	}

	public TitledBorder(Border border) {
		this(border, "", LEADING, DEFAULT_POSITION, null, null);
	}

	public TitledBorder(Border border, String title) {
		this(border, title, LEADING, DEFAULT_POSITION, null, null);
	}

	public TitledBorder(Border border, String title, int titleJustification, int titlePosition) {
		this(border, title, titleJustification, titlePosition, null, null);
	}

	public TitledBorder(Border border, String title, int titleJustification, int titlePosition,
			Font titleFont) {
		this(border, title, titleJustification, titlePosition, titleFont, null);
	}

	public TitledBorder(Border border, String title, int titleJustification, int titlePosition,
			Font titleFont, Color titleColor) {
		super();

		this.titleFont = titleFont == null ? UICore.getDefaultDialogInputFontForSystemUIOnly()
				: titleFont;
		this.titleColor = titleColor == null ? AndroidUIUtil.WIN_FONT_COLOR : titleColor;

		setTitle(title);
		this.border = border;

		buildInitView();
	}

	private void buildInitView() {
		FrameLayout framelayout = new FrameLayout(ActivityManager.applicationContext);
		framelayout.setBackgroundColor(0);

		titleView = new TextView(ActivityManager.applicationContext);
		UICore.setTextSize(titleView, titleFont, screenAdapter);

		titleView.setBackgroundColor(AndroidUIUtil.WIN_BODY_BACK.toAndroid());
		titleView.setTextColor(titleColor.toAndroid());
		titleView.setText(" " + title + " ");
		titleView.setId(titleViewID);
		titleView.setTypeface(UICore.getDefaultDialogInputFontForSystemUIOnly().typeface);

		borderView = framelayout;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	}

	public Insets getBorderInsets(Component c, Insets in) {
		in.set(insets.top, insets.left, insets.bottom, insets.right);
		return in;
	}

	public boolean isBorderOpaque() {
		return false;
	}

	public String getTitle() {
		return title;
	}

	public Border getBorder() {
		return border != null ? border : this;// UIManager.getBorder("TitledBorder.border")
	}

	public int getTitlePosition() {
		return titlePosition;
	}

	public int getTitleJustification() {
		return titleJustification;
	}

	public Font getTitleFont() {
		return titleFont;
	}

	public Color getTitleColor() {
		return titleColor;
	}

	public void setTitle(String title) {
		this.title = title;
		if (titleView != null) {
			AndroidUIUtil.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					titleView.setText(TitledBorder.this.title);
				}
			});
		}
		int pixel = AndroidUIUtil.getBorderStrokeWidthInPixel();
		final boolean isNoTitle = isNotTitleAdAPI();
		insets = new Insets(
				isNoTitle ? pixel
						: AndroidUIUtil.getDefaultTextViewHeight(this.titleFont, screenAdapter),
				pixel, pixel, pixel);
	}

	public final boolean isNotTitleAdAPI() {
		return title == null || title.trim().length() == 0;
	}

	public void setBorder(Border border) {
		this.border = border;
	}

	public void setTitlePosition(int titlePosition) {
		switch (titlePosition) {
		case ABOVE_TOP:
		case TOP:
		case BELOW_TOP:
		case ABOVE_BOTTOM:
		case BOTTOM:
		case BELOW_BOTTOM:
		case DEFAULT_POSITION:
			this.titlePosition = titlePosition;
			break;
		default:
			throw new IllegalArgumentException(titlePosition + " is not a valid title position.");
		}
	}

	public void setTitleJustification(int titleJustification) {
		switch (titleJustification) {
		case DEFAULT_JUSTIFICATION:
		case LEFT:
		case CENTER:
		case RIGHT:
		case LEADING:
		case TRAILING:
			this.titleJustification = titleJustification;
			break;
		default:
			throw new IllegalArgumentException(
					titleJustification + " is not a valid title justification.");
		}
	}

	public void setTitleFont(Font titleFont) {
		this.titleFont = titleFont;
	}

	public void setTitleColor(Color titleColor) {
		this.titleColor = titleColor;
	}

	public Dimension getMinimumSize(Component c) {
		Dimension minSize = new Dimension(0, 0);
		return minSize;
	}

	public int getBaseline(Component c, int width, int height) {
		return -1;
	}

	public Component.BaselineResizeBehavior getBaselineResizeBehavior(Component c) {
		return Component.BaselineResizeBehavior.OTHER;
	}

	private int getPosition() {
		return 0;
	}

	private int getJustification(Component c) {
		return 0;
	}

	protected Font getFont(Component c) {
		return new Font(Font.DIALOG, Font.PLAIN, 12);
	}

	private Color getColor(Component c) {
		Color color = getTitleColor();
		if (color != null) {
			return color;
		}
		return c.getForeground();
	}

	@Override
	public View getBorderViewAdAPI() {
		return borderView;
	}

	@Override
	public void setComponentViewAdAPI(final View view, final Component component) {
		AndroidUIUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (componentView != null) {
					((FrameLayout) borderView).removeAllViews();
				}

				int titleHeight = insets.top;

				{// 由于绘制圆角边框会被盖住四角，所以置于前
					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);// width必须MATCH_PARENT
					// lp.setMargins(0, 0, 0, 0);
					// int pixel = UIUtil.getBorderStrokeWidthInPixel();
					// lp.setMargins(pixel, titleHeight/2, pixel,
					// pixel);//+3增加底部，防止与底框过近
					lp.setMargins(insets.left, titleHeight, insets.right, insets.bottom);
					lp.gravity = (Gravity.TOP
							| (component.getComponentOrientation().isLeftToRight() ? Gravity.LEFT
									: Gravity.RIGHT));// (Gravity.CENTER |
														// Gravity.CENTER_HORIZONTAL
														// |
														// Gravity.CENTER_HORIZONTAL);
					// System.out.println("set Frame Margins l:" + lp.leftMargin
					// + ", t:" + lp.topMargin + ", r:" + lp.rightMargin + ",
					// b:" + lp.bottomMargin);
					borderView.addView(view, lp);
				}
				componentView = view;

				// 由于绘制圆角边框会被盖住四角，所以置于后
				{
					LinearLayout boxView = new LinearLayout(ActivityManager.applicationContext);
					boxView.setBackgroundResource(
							HCRUtil.getResource(HCRUtil.R_drawable_border_titled));

					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);// width必须MATCH_PARENT
					lp.setMargins(0, isNotTitleAdAPI() ? 0 : titleHeight / 2, 0, 0);

					borderView.addView(boxView, lp);
				}

				addTitleViewInUI(component);
			}
		});
	}

	private void addTitleViewInUI(Component component) {
		if (title == null || title.trim().length() == 0) {
		} else {
			boolean isLeftToRight = component.getComponentOrientation().isLeftToRight();

			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			lp.setMargins(insets.left + (isLeftToRight ? AndroidUIUtil.INDENT_PIXEL : 0), 0,
					insets.right + (isLeftToRight == false ? AndroidUIUtil.INDENT_PIXEL : 0),
					insets.bottom);

			if (isLeftToRight) {
				lp.gravity = Gravity.LEFT;
			} else {
				lp.gravity = Gravity.RIGHT;
			}
			// lp.gravity |= Gravity.TOP;

			titleView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP | Gravity.CENTER_VERTICAL);
			if (titleOldParent != null) {// setLayout时，有可能restructView
				titleOldParent.removeView(titleView);
			}
			borderView.addView(titleView, lp);
			titleOldParent = borderView;
		}
	}

	ViewGroup titleOldParent;
}
