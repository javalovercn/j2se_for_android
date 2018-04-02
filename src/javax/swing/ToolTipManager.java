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
import hc.android.J2SEInitor;
import hc.android.UICore;
import hc.android.AndroidUIUtil;
import hc.core.HCTimer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import hc.android.HCRUtil;

/**
 * Manages all the <code>ToolTips</code> in the system.
 * <p>
 * ToolTipManager contains numerous properties for configuring how long it will
 * take for the tooltips to become visible, and how long till they hide.
 * Consider a component that has a different tooltip based on where the mouse
 * is, such as JTree. When the mouse moves into the JTree and over a region that
 * has a valid tooltip, the tooltip will become visibile after
 * <code>initialDelay</code> milliseconds. After <code>dismissDelay</code>
 * milliseconds the tooltip will be hidden. If the mouse is over a region that
 * has a valid tooltip, and the tooltip is currently visible, when the mouse
 * moves to a region that doesn't have a valid tooltip the tooltip will be
 * hidden. If the mouse then moves back into a region that has a valid tooltip
 * within <code>reshowDelay</code> milliseconds, the tooltip will immediately be
 * shown, otherwise the tooltip will be shown again after
 * <code>initialDelay</code> milliseconds.
 *
 * @see JComponent#createToolTip
 * @author Dave Moore
 * @author Rich Schiavi
 */
public class ToolTipManager extends MouseAdapter implements MouseMotionListener {
	Timer enterTimer, exitTimer, insideTimer;
	String toolTipText;
	Point preferredLocation;
	JComponent insideComponent;
	MouseEvent mouseEvent;
	boolean showImmediately;

	private JComponent current;
	private Toast toast;
	private HCTimer time = new HCTimer("", 1000, true) {
		@Override
		public void doBiz() {
			showTipWindow();
		}
	};

	ToolTipManager() {
	}

	boolean isEnableTip = true;

	public void setEnabled(boolean flag) {
		isEnableTip = flag;
		if (isEnableTip) {
		} else {
			hideTipWindow();
		}
	}

	public boolean isEnabled() {
		return isEnableTip;
	}

	public void setLightWeightPopupEnabled(boolean aFlag) {
	}

	public boolean isLightWeightPopupEnabled() {
		return false;
	}

	public void setInitialDelay(int milliseconds) {
		if (milliseconds < 300) {
			milliseconds = 300;
		}
		time.setIntervalMS(milliseconds);
	}

	public long getInitialDelay() {
		return time.getIntervalMS();
	}

	public void setDismissDelay(int milliseconds) {
	}

	public int getDismissDelay() {
		return 100;
	}

	public void setReshowDelay(int milliseconds) {
	}

	public int getReshowDelay() {
		return 100;
	}

	void showTipWindow() {
		JComponent toDisp = current;
		if (toDisp != null) {
			AndroidUIUtil.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					JComponent toDisp = current;
					if (toDisp != null) {
						synchronized (time) {
							String tip = toDisp.getToolTipText();
							toast = new Toast(ActivityManager.applicationContext);
							View focusableView = toDisp.getFocusablePeerViewAdAPI();
							if (focusableView == null) {
								focusableView = toDisp.getPeerAdAPI();
							}
							int[] xy = new int[2];
							focusableView.getLocationOnScreen(xy);
							Point point = new Point(xy[0], xy[1]);
							toast.setDuration(Toast.LENGTH_LONG);

							int halfWidth = focusableView.getWidth() / 2;
							int halfHeight = focusableView.getHeight() / 2;

							// int centerX = point.x + halfWidth;
							int centerY = point.y + halfHeight;
							// boolean biggerHalfX = centerX >
							// J2SEInitor.screenWidth/2;
							boolean biggerHalfY = centerY > J2SEInitor.screenHeight / 2;

							TextView tv = new TextView(ActivityManager.applicationContext);
							tv.setBackgroundResource(HCRUtil.getResource(HCRUtil.R_drawable_tip));
							tv.setTextColor(AndroidUIUtil.WINDOW_TIP_FONT_COLOR.toAndroid());
							UICore.setTextSize(tv,
									UICore.getDefaultDialogInputFontForSystemUIOnly(), -2,
									J2SEInitor.getAndroidServerScreenAdapter());
							if (AndroidUIUtil.containsHTML(tip)) {
								tv.setText(Html.fromHtml(tip));
							} else {
								tv.setText(tip);
							}
							Dimension dimension = new Dimension();
							AndroidUIUtil.getViewWidthAndHeight(tv, dimension);
							// System.out.println("halfWidth : " + halfWidth +
							// ", halfHeight : "
							// + halfHeight + ", pX : " + point.x + ", pY : " +
							// point.y + ", tip w : " + dimension.width + ", h :
							// "+ dimension.height +
							// focusableView.getClass().getName());

							// 需要考虑Toast自身部分的高宽
							int pixel = AndroidUIUtil.dpToPx(15);// 假定为边框为15dp

							toast.setGravity(Gravity.TOP | Gravity.LEFT, point.x,
									biggerHalfY ? (point.y - dimension.height - pixel * 2)
											: (point.y + halfHeight * 2 + pixel));

							toast.setView(tv);
							toast.show();

							time.setEnable(false);
						}
					}
				}
			});
		}
	}

	void hideTipWindow() {
		time.setEnable(false);

		if (toast != null) {
			AndroidUIUtil.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					synchronized (time) {
						if (toast != null) {
							toast.cancel();
							toast = null;
						}
					}
				}
			});
		}
	}

	private final static ToolTipManager manager = new ToolTipManager();

	public static ToolTipManager sharedInstance() {
		return manager;
	}

	public void registerComponent(JComponent component) {
		unregisterComponent(null);
		if (isEnableTip) {
			this.current = component;
			time.setEnable(true);
			time.resetTimerCount();
		}
	}

	public void unregisterComponent(JComponent component) {
		this.current = null;

		hideTipWindow();
	}

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}

	public void mousePressed(MouseEvent event) {
	}

	public void mouseDragged(MouseEvent event) {
	}

	public void mouseMoved(MouseEvent event) {
	}

	protected class insideTimerAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
		}
	}

	protected class outsideTimerAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
		}
	}

	protected class stillInsideTimerAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
		}
	}

	private class MoveBeforeEnterListener extends MouseMotionAdapter {
		public void mouseMoved(MouseEvent e) {
		}
	}

	static Frame frameForComponent(Component component) {
		while (!(component instanceof Frame) && component != null) {
			component = component.getParent();
		}
		return (Frame) component;
	}

	private class AccessibilityKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
		}
	}
}
