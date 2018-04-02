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

import java.awt.Adjustable;
import java.awt.Dimension;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ScrollBarUI;

import android.widget.ScrollView;

/**
 * An implementation of a scrollbar. The user positions the knob in the
 * scrollbar to determine the contents of the viewing area. The program
 * typically adjusts the display so that the end of the scrollbar represents the
 * end of the displayable contents, or 100% of the contents. The start of the
 * scrollbar is the beginning of the displayable contents, or 0%. The position
 * of the knob within those bounds then translates to the corresponding
 * percentage of the displayable contents.
 * <p>
 * Typically, as the position of the knob in the scrollbar changes a
 * corresponding change is made to the position of the JViewport on the
 * underlying view, changing the contents of the JViewport.
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
 * @see JScrollPane
 * @beaninfo attribute: isContainer false description: A component that helps
 *           determine the visible content range of an area.
 *
 * @author David Kloba
 */
public class JScrollBar extends JComponent implements Adjustable, Accessible {
	private static final String uiClassID = "ScrollBarUI";
	JScrollPane scrollPane;
	protected BoundedRangeModel model;

	protected int orientation;
	protected int unitIncrement;
	protected int blockIncrement;
	protected int min, max;

	private void checkOrientation(int orientation) {
		switch (orientation) {
		case VERTICAL:
		case HORIZONTAL:
			break;
		default:
			throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
		}
	}

	public JScrollBar(int orientation, int value, int extent, int min, int max) {
		this.orientation = orientation;
		this.min = min;
		this.max = max;
	}

	public JScrollBar(int orientation) {
		this(orientation, 0, 10, 0, 100);
	}

	public JScrollBar() {
		this(VERTICAL);
	}

	public void setUI(ScrollBarUI ui) {
		// 由于J2SE/JScrollPane需要ScrollBarUI不可见时，设置尺寸为0。故本方法被调用
		AndroidClassUtil.callEmptyMethod();
		super.setUI(ui);
	}

	public ScrollBarUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void updateUI() {
		AndroidClassUtil.callEmptyMethod();
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		checkOrientation(orientation);
		this.orientation = orientation;
	}

	public BoundedRangeModel getModel() {
		return model;
	}

	public void setModel(BoundedRangeModel newModel) {
		AndroidClassUtil.callEmptyMethod();
	}

	public int getUnitIncrement(int direction) {
		return unitIncrement;
	}

	public void setUnitIncrement(int unitIncrement) {
		this.unitIncrement = unitIncrement;
		AndroidClassUtil.callEmptyMethod();
	}

	public int getBlockIncrement(int direction) {
		return blockIncrement;
	}

	public void setBlockIncrement(int blockIncrement) {
		this.blockIncrement = blockIncrement;
		AndroidClassUtil.callEmptyMethod();
	}

	public int getUnitIncrement() {
		return unitIncrement;
	}

	public int getBlockIncrement() {
		return blockIncrement;
	}

	public int getValue() {
		return 0;
	}

	public void setValue(int value) {
		if (value == min) {
			scrollPane.scrollView.fullScroll(ScrollView.FOCUS_UP);
		} else if (value == max) {
			scrollPane.scrollView.fullScroll(ScrollView.FOCUS_DOWN);
		}
	}

	public int getVisibleAmount() {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public void setVisibleAmount(int extent) {
		AndroidClassUtil.callEmptyMethod();
	}

	public int getMinimum() {
		return min;
	}

	public void setMinimum(int minimum) {
		AndroidClassUtil.callEmptyMethod();
	}

	public int getMaximum() {
		return max;
	}

	public void setMaximum(int maximum) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getValueIsAdjusting() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void setValueIsAdjusting(boolean b) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setValues(int newValue, int newExtent, int newMin, int newMax) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void addAdjustmentListener(AdjustmentListener l) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void removeAdjustmentListener(AdjustmentListener l) {
		AndroidClassUtil.callEmptyMethod();
	}

	public AdjustmentListener[] getAdjustmentListeners() {
		return list.getListeners(AdjustmentListener.class);
	}

	protected void fireAdjustmentValueChanged(int id, int type, int value) {
		fireAdjustmentValueChanged(id, type, value, getValueIsAdjusting());
	}

	private void fireAdjustmentValueChanged(int id, int type, int value, boolean isAdjusting) {
		AndroidClassUtil.callEmptyMethod();
	}

	private class ModelListener implements ChangeListener, Serializable {
		public void stateChanged(ChangeEvent e) {
			AndroidClassUtil.callEmptyMethod();
		}
	}

	public Dimension getMinimumSize() {
		AndroidClassUtil.callEmptyMethod();
		return new Dimension(5, 5);
	}

	public Dimension getMaximumSize() {
		AndroidClassUtil.callEmptyMethod();
		return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
	}

	public void setEnabled(boolean x) {
		AndroidClassUtil.callEmptyMethod();
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
}