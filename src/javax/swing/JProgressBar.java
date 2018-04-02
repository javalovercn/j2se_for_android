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
import hc.android.AndroidUIUtil;

import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.Format;
import java.text.NumberFormat;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ProgressBarUI;

import hc.android.HCRUtil;
import hc.core.util.LogManager;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * A component that visually displays the progress of some task. As the task
 * progresses towards completion, the progress bar displays the task's
 * percentage of completion. This percentage is typically represented visually
 * by a rectangle which starts out empty and gradually becomes filled in as the
 * task progresses. In addition, the progress bar can display a textual
 * representation of this percentage.
 * <p>
 * {@code JProgressBar} uses a {@code BoundedRangeModel} as its data model, with
 * the {@code value} property representing the "current" state of the task, and
 * the {@code minimum} and {@code maximum} properties representing the beginning
 * and end points, respectively.
 * <p>
 * To indicate that a task of unknown length is executing, you can put a
 * progress bar into indeterminate mode. While the bar is in indeterminate mode,
 * it animates constantly to show that work is occurring. As soon as you can
 * determine the task's length and amount of progress, you should update the
 * progress bar's value and switch it back to determinate mode.
 *
 * <p>
 *
 * Here is an example of creating a progress bar, where <code>task</code> is an
 * object (representing some piece of work) which returns information about the
 * progress of the task:
 *
 * <pre>
 * progressBar = new JProgressBar(0, task.getLengthOfTask());
 * progressBar.setValue(0);
 * progressBar.setStringPainted(true);
 * </pre>
 *
 * Here is an example of querying the current state of the task, and using the
 * returned value to update the progress bar:
 *
 * <pre>
 * progressBar.setValue(task.getCurrent());
 * </pre>
 *
 * Here is an example of putting a progress bar into indeterminate mode, and
 * then switching back to determinate mode once the length of the task is known:
 *
 * <pre>
 *progressBar = new JProgressBar();
 *<em>...//when the task of (initially) unknown length begins:</em>
 *progressBar.setIndeterminate(true);
 *<em>...//do some work; get length of task...</em>
 *progressBar.setMaximum(newLength);
 *progressBar.setValue(newValue);
 *progressBar.setIndeterminate(false);
 * </pre>
 *
 * <p>
 *
 * For complete examples and further documentation see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/progress.html"
 * target="_top">How to Monitor Progress</a>, a section in <em>The Java
 * Tutorial.</em>
 *
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
 * @see javax.swing.plaf.basic.BasicProgressBarUI
 * @see javax.swing.BoundedRangeModel
 * @see javax.swing.SwingWorker
 *
 * @beaninfo attribute: isContainer false description: A component that displays
 *           an integer value.
 *
 * @author Michael C. Albers
 * @author Kathy Walrath
 */
public class JProgressBar extends JComponent implements SwingConstants, Accessible {
	protected ProgressBar progressBar;

	/**
	 * @see #getUIClassID
	 */
	private static final String uiClassID = "ProgressBarUI";

	protected int orientation;
	protected boolean paintBorder;

	protected BoundedRangeModel model;
	protected String progressString;
	protected boolean paintString;

	static final private int defaultMinimum = 0;
	static final private int defaultMaximum = 100;
	static final private int defaultOrientation = HORIZONTAL;

	protected transient ChangeEvent changeEvent = null;
	protected ChangeListener changeListener = null;

	/**
	 * Format used when displaying percent complete.
	 */
	private transient Format format;
	private boolean indeterminate;

	public JProgressBar() {
		this(defaultOrientation);
	}

	public JProgressBar(int orient) {
		this(orient, defaultMinimum, defaultMaximum);
	}

	public JProgressBar(int min, int max) {
		this(defaultOrientation, min, max);
	}

	public JProgressBar(int orient, int min, int max) {
		setOrientation(orient);
		setModel(new DefaultBoundedRangeModel(min, 0, min, max));
		initBar();
		updateUI();

		setBorderPainted(true);
		setStringPainted(false);
		setString(null);
		setIndeterminate(false);
	}

	private void initBar() {
		progressBar = new ProgressBar(ActivityManager.applicationContext, null,
				android.R.attr.progressBarStyleHorizontal) {
			Paint mPaint;
			Rect rect = new Rect();

			protected void onDraw(Canvas canvas) {
				super.onDraw(canvas);

				if (paintString && progressString != null) {
					getPaint().getTextBounds(progressString, 0, progressString.length(), rect);
					int x = (getWidth() / 2) - rect.centerX();
					int y = (getHeight() / 2) - rect.centerY();
					canvas.drawText(progressString, x, y, getPaint());
				}
			}

			private Paint getPaint() {
				if (mPaint == null) {
					mPaint = new Paint();
					mPaint.setTypeface(JProgressBar.this.getFont().typeface);
					mPaint.setTextSize(getScreenAdapterAdAPI()
							.getFontSizeInPixel(JProgressBar.this.getFont().getSize()));
					mPaint.setAntiAlias(true);
					mPaint.setColor(Color.BLACK);
				}
				return mPaint;
			}
		};

		progressBar.setMinimumHeight(
				(int) getScreenAdapterAdAPI().getFontSizeInPixel(getFont().getSize()));
		progressBar.setFocusable(false);
		progressBar.setClickable(false);
		progressBar.setMax(getModel().getMaximum());
		progressBar.setProgressDrawable(ActivityManager.applicationContext.getResources()
				.getDrawable(HCRUtil.getResource(HCRUtil.R_drawable_progress_bar)));

		LinearLayout layout = new LinearLayout(ActivityManager.applicationContext);
		{
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			lp.gravity = (Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			layout.addView(progressBar, lp);
		}
		setPeerAdAPI(layout);
	}

	public JProgressBar(BoundedRangeModel newModel) {
		setModel(newModel);
		initBar();
		updateUI();

		setOrientation(defaultOrientation);
		setBorderPainted(true);
		setStringPainted(false);
		setString(null);
		setIndeterminate(false);
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int newOrientation) {
		orientation = newOrientation;
	}

	public boolean isStringPainted() {
		return paintString;
	}

	public void setStringPainted(boolean b) {
		boolean oldValue = paintString;
		paintString = b;
		if (paintString != oldValue) {
			progressBar.postInvalidate();
		}
	}

	public String getString() {
		if (progressString != null) {
			return progressString;
		} else {
			if (format == null) {
				format = NumberFormat.getPercentInstance();
			}
			return format.format(new Double(getPercentComplete()));
		}
	}

	public void setString(String s) {
		String oldValue = progressString;
		progressString = s;
		if (progressString == null || oldValue == null || !progressString.equals(oldValue)) {
			progressBar.postInvalidate();
		}
	}

	/**
	 * @return between 0.0 and 1.0
	 */
	public double getPercentComplete() {
		long span = model.getMaximum() - model.getMinimum();
		double currentValue = model.getValue();
		double pc = (currentValue - model.getMinimum()) / span;
		return pc;
	}

	public boolean isBorderPainted() {
		return paintBorder;
	}

	public void setBorderPainted(boolean b) {
		AndroidClassUtil.callEmptyMethod();
	}

	protected void paintBorder(Graphics g) {
		AndroidClassUtil.callEmptyMethod();
	}

	public ProgressBarUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return (ProgressBarUI) null;
	}

	public void setUI(ProgressBarUI ui) {
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
		return model;
	}

	public void setModel(BoundedRangeModel newModel) {
		BoundedRangeModel oldModel = getModel();

		if (newModel.getMinimum() != 0) {
			LogManager.errToLog(
					"javax.swing.JProgressBar must set min to zero in Android server, there is a difference with standard J2SE.");
		}

		if (newModel != oldModel) {
			if (oldModel != null) {
				oldModel.removeChangeListener(changeListener);
				changeListener = null;
			}

			model = newModel;

			if (newModel != null) {
				changeListener = createChangeListener();
				newModel.addChangeListener(changeListener);
			}

			if (model != null) {
				model.setExtent(0);
			}
		}
	}

	public int getValue() {
		return getModel().getValue();
	}

	public int getMinimum() {
		return getModel().getMinimum();
	}

	public int getMaximum() {
		return getModel().getMaximum();
	}

	public void setValue(final int n) {
		BoundedRangeModel brm = getModel();
		brm.setValue(n);

		AndroidUIUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progressBar.setProgress(n);
			}
		});
	}

	public void setMinimum(int n) {
		getModel().setMinimum(n);
	}

	public void setMaximum(int n) {
		progressBar.setMax(n);
		getModel().setMaximum(n);
	}

	public void setIndeterminate(boolean newValue) {
		indeterminate = newValue;
		progressBar.setIndeterminate(newValue);
	}

	public boolean isIndeterminate() {
		return indeterminate;
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