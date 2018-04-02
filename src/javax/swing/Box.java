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
import hc.android.CanvasGraphics;
import hc.android.AndroidUIUtil;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import android.graphics.Canvas;
import android.view.View;

/**
 * A lightweight container that uses a BoxLayout object as its layout manager.
 * Box provides several class methods that are useful for containers using
 * BoxLayout -- even non-Box containers.
 *
 * <p>
 * The <code>Box</code> class can create several kinds of invisible components
 * that affect layout: glue, struts, and rigid areas. If all the components your
 * <code>Box</code> contains have a fixed size, you might want to use a glue
 * component (returned by <code>createGlue</code>) to control the components'
 * positions. If you need a fixed amount of space between two components, try
 * using a strut (<code>createHorizontalStrut</code> or
 * <code>createVerticalStrut</code>). If you need an invisible component that
 * always takes up the same amount of space, get it by invoking
 * <code>createRigidArea</code>.
 * <p>
 * If you are implementing a <code>BoxLayout</code> you can find further
 * information and examples in
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/layout/box.html">How
 * to Use BoxLayout</a>, a section in <em>The Java Tutorial.</em>
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @see BoxLayout
 *
 * @author Timothy Prinzing
 */
public class Box extends JComponent implements Accessible {

	public Box(int axis) {
		super();
		super.setLayout(new BoxLayout(this, axis));
	}

	public static Box createHorizontalBox() {
		return new Box(BoxLayout.X_AXIS);
	}

	public static Box createVerticalBox() {
		return new Box(BoxLayout.Y_AXIS);
	}

	/**
	 * 固定大小透明块
	 * 
	 * @param d
	 * @return
	 */
	public static Component createRigidArea(Dimension d) {
		return new Filler(d, d, d);
	}

	/**
	 * 固定宽度，高度不限
	 * 
	 * @param width
	 * @return
	 */
	public static Component createHorizontalStrut(int width) {
		return new Filler(new Dimension(width, 0), new Dimension(width, 0),
				new Dimension(width, Short.MAX_VALUE));
	}

	/**
	 * 高度固定，宽度不限
	 * 
	 * @param height
	 * @return
	 */
	public static Component createVerticalStrut(int height) {
		return new Filler(new Dimension(0, height), new Dimension(0, height),
				new Dimension(Short.MAX_VALUE, height));
	}

	/**
	 * 最大高度、宽度不限
	 * 
	 * @return
	 */
	public static Component createGlue() {
		return new Filler(new Dimension(0, 0), new Dimension(0, 0),
				new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
	}

	/**
	 * 最大高度、宽度不限
	 * 
	 * @return
	 */
	public static Component createHorizontalGlue() {
		return new Filler(new Dimension(0, 0), new Dimension(0, 0),
				new Dimension(Short.MAX_VALUE, 0));
	}

	public static boolean isVerticalGlueAdAPI(Component filler) {
		Dimension max = filler.getMaximumSize();
		if (max.width == 0 && max.height == Short.MAX_VALUE) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isHorizontalVerticalGlueAdAPI(Component filler) {
		Dimension max = filler.getMaximumSize();
		if (max.width == Short.MAX_VALUE && max.height == Short.MAX_VALUE) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isHorizontalGlueAdAPI(Component filler) {
		Dimension max = filler.getMaximumSize();
		if (max.width == Short.MAX_VALUE && max.height == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 最大高度、宽度不限
	 * 
	 * @return
	 */
	public static Component createVerticalGlue() {
		return new Filler(new Dimension(0, 0), new Dimension(0, 0),
				new Dimension(0, Short.MAX_VALUE));
	}

	public void setLayout(LayoutManager l) {
		throw new Error("Illegal request");
	}

	protected void paintComponent(Graphics g) {
		// g.setColor(getBackground());
		// g.fillRect(0, 0, getWidth(), getHeight());
	}

	public static class Filler extends JComponent implements Accessible {

		public Filler(Dimension min, Dimension pref, Dimension max) {
			setMinimumSize(min);
			setPreferredSize(pref);
			setMaximumSize(max);
			init();
			revalidate();
		}

		public void changeShape(Dimension min, Dimension pref, Dimension max) {
			setMinimumSize(min);
			setPreferredSize(pref);
			setMaximumSize(max);
			init();
			revalidate();
		}

		private void init() {
			setPeerAdAPI(new View(hc.android.ActivityManager.applicationContext) {
				// MeasureSpec.getSize(measureSpec)
				@Override
				protected void onDraw(Canvas canvas) {
					paintComponent(new CanvasGraphics(canvas, getScreenAdapterAdAPI()));// 注意不能缓存，因为可能会调整尺寸
				}

				protected int getSuggestedMinimumWidth() {
					Dimension minimumSize = Filler.this.getMinimumSize();
					int outWidth = minimumSize.width;
					if (outWidth <= 0) {
						outWidth = 5;
					}
					return outWidth;
				}

				protected int getSuggestedMinimumHeight() {
					Dimension minimumSize = Filler.this.getMinimumSize();
					int outHeight = minimumSize.height;
					if (outHeight <= 0) {
						outHeight = 5;
					}
					return outHeight;
				}
			});

		}

		protected void paintComponent(Graphics g) {
			g.setColor(AndroidUIUtil.WIN_BODY_BACK);
			g.fillRect(0, 0, getWidth(), getHeight());
		}

		public AccessibleContext getAccessibleContext() {
			if (accessibleContext == null) {
				accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
			}
			return accessibleContext;
		}

	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

}
