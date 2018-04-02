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
import hc.android.HCRUtil;
import hc.android.UICore;
import hc.core.L;
import hc.core.util.LogManager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleValue;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.ProgressBarUI;
import javax.swing.plaf.SliderUI;
import javax.swing.plaf.UIResource;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * A component that lets the user graphically select a value by sliding a knob
 * within a bounded interval. The knob is always positioned at the points that
 * match integer values within the specified interval.
 * <p>
 * The slider can show both major tick marks, and minor tick marks between the
 * major ones. The number of values between the tick marks is controlled with
 * <code>setMajorTickSpacing</code> and <code>setMinorTickSpacing</code>.
 * Painting of tick marks is controlled by {@code setPaintTicks}.
 * <p>
 * Sliders can also print text labels at regular intervals (or at arbitrary
 * locations) along the slider track. Painting of labels is controlled by
 * {@code setLabelTable} and {@code setPaintLabels}.
 * <p>
 * For further information and examples see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/slider.html">How
 * to Use Sliders</a>, a section in <em>The Java Tutorial.</em>
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
 * @beaninfo attribute: isContainer false description: A component that supports
 *           selecting a integer value from a range.
 *
 * @author David Kloba
 */
public class JSlider extends JComponent implements SwingConstants, Accessible {
	private static final String uiClassID = "SliderUI";
	private SeekBar sb;
	protected BoundedRangeModel sliderModel;

	boolean snapToValue = true;

	protected int orientation;

	private Dictionary labelTable;

	protected ChangeListener changeListener = createChangeListener();

	protected transient ChangeEvent changeEvent = null;

	private void checkOrientation(int orientation) {
		switch (orientation) {
		case VERTICAL:
		case HORIZONTAL:
			break;
		default:
			throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
		}
	}

	private void initBar() {
		sb = new SeekBar(ActivityManager.applicationContext);

		sb.setMinimumHeight((int) getScreenAdapterAdAPI().getFontSizeInPixel(getFont().getSize()));
		sb.setFocusable(false);
		sb.setClickable(true);
		updateToSeekBar();
		// sb.setProgressDrawable(ActivityManager.applicationContext.getResources().getDrawable(
		// HCRUtil.getResource(HCRUtil.R_drawable_progress_bar)));

		OnSeekBarChangeListener osbcl = new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				synchronized (lock) {
					LogManager.log("onProgressChanged");
					if (progress != getValue()) {
						setValue(progress);
					}
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				LogManager.log("onStartTrackingTouch");
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				LogManager.log("onStopTrackingTouch");
			}
		};

		// 为拖动条绑定监听器
		sb.setOnSeekBarChangeListener(osbcl);

		LinearLayout layout = new LinearLayout(ActivityManager.applicationContext);
		{
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			lp.gravity = (Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			layout.addView(sb, lp);
		}
		setPeerAdAPI(layout);
	}

	private void updateToSeekBar() {
		if (sb != null) {
			sb.setMax(getMaximum());
			sb.setProgress(getValue());
		}
	}

	public JSlider() {
		this(HORIZONTAL, 0, 100, 50);
	}

	public JSlider(int orientation) {
		this(orientation, 0, 100, 50);
	}

	public JSlider(int min, int max) {
		this(HORIZONTAL, min, max, (min + max) / 2);
	}

	public JSlider(int min, int max, int value) {
		this(HORIZONTAL, min, max, value);
	}

	final Object lock;

	public JSlider(int orientation, int min, int max, int value) {
		lock = this;

		checkOrientation(orientation);
		this.orientation = orientation;
		setModel(new DefaultBoundedRangeModel(value, 0, min, max));// 0 : extend
		initBar();
		updateUI();
	}

	public JSlider(BoundedRangeModel brm) {
		lock = this;

		this.orientation = JSlider.HORIZONTAL;
		setModel(brm);
		initBar();
		updateUI();
	}

	public SliderUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return (SliderUI) null;
	}

	public void setUI(SliderUI ui) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void updateUI() {
	}

	public String getUIClassID() {
		return uiClassID;
	}

	private class ModelListener implements ChangeListener, Serializable {
		public void stateChanged(ChangeEvent e) {
			fireStateChanged();
			updateToSeekBar();
		}
	}

	protected ChangeListener createChangeListener() {
		return new ModelListener();
	}

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
		Object[] listeners = list.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (changeEvent == null)
					changeEvent = new ChangeEvent(this);
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}

	public BoundedRangeModel getModel() {
		return sliderModel;
	}

	public void setModel(BoundedRangeModel newModel) {
		if (newModel.getMinimum() != 0) {
			LogManager.errToLog(
					"javax.swing.JSlider must set min to zero in Android server, there is a difference with standard J2SE.");
		}

		BoundedRangeModel oldModel = getModel();

		if (oldModel != null) {
			oldModel.removeChangeListener(changeListener);
		}

		sliderModel = newModel;

		if (newModel != null) {
			newModel.addChangeListener(changeListener);
		}

		updateToSeekBar();

		// if (accessibleContext != null) {
		// accessibleContext.firePropertyChange(
		// AccessibleContext.ACCESSIBLE_VALUE_PROPERTY,
		// (oldModel == null
		// ? null : Integer.valueOf(oldModel.getValue())),
		// (newModel == null
		// ? null : Integer.valueOf(newModel.getValue())));
		// }

		firePropertyChange("model", oldModel, sliderModel);
	}

	public int getValue() {
		return getModel().getValue();
	}

	public void setValue(int n) {
		synchronized (lock) {
			BoundedRangeModel m = getModel();
			int oldValue = m.getValue();
			if (oldValue == n) {
				return;
			}
			m.setValue(n);
		}
		// if (accessibleContext != null) {
		// accessibleContext.firePropertyChange(
		// AccessibleContext.ACCESSIBLE_VALUE_PROPERTY,
		// Integer.valueOf(oldValue),
		// Integer.valueOf(m.getValue()));
		// }
	}

	public int getMinimum() {
		return getModel().getMinimum();
	}

	public void setMinimum(int minimum) {
		int oldMin = getModel().getMinimum();
		getModel().setMinimum(minimum);
		firePropertyChange("minimum", Integer.valueOf(oldMin), Integer.valueOf(minimum));
	}

	public int getMaximum() {
		return getModel().getMaximum();
	}

	public void setMaximum(int maximum) {
		int oldMax = getModel().getMaximum();
		getModel().setMaximum(maximum);
		firePropertyChange("maximum", Integer.valueOf(oldMax), Integer.valueOf(maximum));
	}

	public boolean getValueIsAdjusting() {
		return getModel().getValueIsAdjusting();
	}

	public void setValueIsAdjusting(boolean b) {
		BoundedRangeModel m = getModel();
		boolean oldValue = m.getValueIsAdjusting();
		m.setValueIsAdjusting(b);

		// if ((oldValue != b) && (accessibleContext != null)) {
		// accessibleContext.firePropertyChange(
		// AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
		// ((oldValue) ? AccessibleState.BUSY : null),
		// ((b) ? AccessibleState.BUSY : null));
		// }
	}

	public int getExtent() {
		return getModel().getExtent();
	}

	public void setExtent(int extent) {
		getModel().setExtent(extent);
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		checkOrientation(orientation);
		int oldValue = this.orientation;
		this.orientation = orientation;
		firePropertyChange("orientation", oldValue, orientation);

		// if ((oldValue != orientation) && (accessibleContext != null)) {
		// accessibleContext.firePropertyChange(
		// AccessibleContext.ACCESSIBLE_STATE_PROPERTY,
		// ((oldValue == VERTICAL)
		// ? AccessibleState.VERTICAL : AccessibleState.HORIZONTAL),
		// ((orientation == VERTICAL)
		// ? AccessibleState.VERTICAL : AccessibleState.HORIZONTAL));
		// }
		if (orientation != oldValue) {
			revalidate();
		}
	}

	public void setFont(Font font) {
		super.setFont(font);
		updateLabelSizes();
	}

	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
		return false;
	}

	public Dictionary getLabelTable() {
		AndroidClassUtil.callEmptyMethod();
		return labelTable;
	}

	public void setLabelTable(Dictionary labels) {
		AndroidClassUtil.callEmptyMethod();
	}

	protected void updateLabelUIs() {
		AndroidClassUtil.callEmptyMethod();
	}

	private void updateLabelSizes() {
		AndroidClassUtil.callEmptyMethod();
	}

	public Hashtable createStandardLabels(int increment) {
		AndroidClassUtil.callEmptyMethod();
		return createStandardLabels(increment, getMinimum());
	}

	public Hashtable createStandardLabels(int increment, int start) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public boolean getInverted() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void setInverted(boolean b) {
		AndroidClassUtil.callEmptyMethod();
	}

	public int getMajorTickSpacing() {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public void setMajorTickSpacing(int n) {
		AndroidClassUtil.callEmptyMethod();
	}

	public int getMinorTickSpacing() {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public void setMinorTickSpacing(int n) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getSnapToTicks() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	boolean getSnapToValue() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void setSnapToTicks(boolean b) {
		AndroidClassUtil.callEmptyMethod();
	}

	void setSnapToValue(boolean b) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getPaintTicks() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void setPaintTicks(boolean b) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getPaintTrack() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void setPaintTrack(boolean b) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getPaintLabels() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void setPaintLabels(boolean b) {
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