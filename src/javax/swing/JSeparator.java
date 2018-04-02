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
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.plaf.SeparatorUI;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

/**
 * <code>JSeparator</code> provides a general purpose component for implementing
 * divider lines - most commonly used as a divider between menu items that
 * breaks them up into logical groupings. Instead of using
 * <code>JSeparator</code> directly, you can use the <code>JMenu</code> or
 * <code>JPopupMenu</code> <code>addSeparator</code> method to create and add a
 * separator. <code>JSeparator</code>s may also be used elsewhere in a GUI
 * wherever a visual divider is useful.
 *
 * <p>
 *
 * For more information and examples see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/menu.html">How to
 * Use Menus</a>, a section in <em>The Java Tutorial.</em>
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
 * @beaninfo attribute: isContainer false description: A divider between menu
 *           items.
 *
 * @author Georges Saab
 * @author Jeff Shapiro
 */
public class JSeparator extends JComponent implements SwingConstants, Accessible {
	private static final String uiClassID = "SeparatorUI";

	private int orientation = HORIZONTAL;

	public JSeparator() {
		this(HORIZONTAL);
	}

	public JSeparator(int orientation) {
		this.orientation = orientation;
		setFocusable(false);
		updateUI();
	}

	public SeparatorUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return (SeparatorUI) null;
	}

	public void setUI(SeparatorUI ui) {
		AndroidClassUtil.callEmptyMethod();
	}

	LinearLayout defaultLinearLayout;

	public View getPeerAdAPI() {
		if (defaultLinearLayout == null) {
			defaultLinearLayout = new LinearLayout(ActivityManager.applicationContext);

			final int px = AndroidUIUtil.dpToPx(1);

			defaultLinearLayout.setLayoutParams(
					new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, px));

			if (orientation == HORIZONTAL) {
				defaultLinearLayout.setMinimumHeight(px);
			} else {
				defaultLinearLayout.setMinimumWidth(px);
			}
			setPeerAdAPI(defaultLinearLayout);

			defaultLinearLayout.setBackgroundDrawable(new Drawable() {
				@Override
				public void draw(Canvas arg0) {
					Paint paint = new Paint();
					paint.setColor(AndroidUIUtil.WINDOW_BORDER_COLOR.toAndroid());
					paint.setStrokeWidth(px);
					int width = defaultLinearLayout.getWidth();
					int height = defaultLinearLayout.getHeight();

					if (JSeparator.this.orientation == HORIZONTAL) {
						drawLine(arg0, paint, 1, 0, width - 2, 0);
					} else {
						drawLine(arg0, paint, 0, 1, 0, height - 2);
					}
				}

				private void drawLine(Canvas canvas, Paint paint, int x1, int y1, int x2, int y2) {
					canvas.drawPoint(x1, y1, paint);
					canvas.drawLine(x1, y1, x2, y2, paint);
					canvas.drawPoint(x2, y2, paint);
				}

				@Override
				public int getOpacity() {
					return 0;
				}

				@Override
				public void setAlpha(int arg0) {
				}

				@Override
				public void setColorFilter(ColorFilter arg0) {
				}
			});
		}

		return defaultLinearLayout;
	}

	public void updateUI() {
	}

	public String getUIClassID() {
		return uiClassID;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	public int getOrientation() {
		return this.orientation;
	}

	public void setOrientation(int orientation) {
		if (this.orientation == orientation) {
			return;
		}
		this.orientation = orientation;
		revalidate();
		repaint();
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

}
