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
import hc.core.IConstant;
import hc.core.L;
import hc.server.TrayMenuUtil;
import hc.util.ResourceUtil;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.plaf.SplitPaneUI;

/**
 * <code>JSplitPane</code> is used to divide two (and only two)
 * <code>Component</code>s. The two <code>Component</code>s are graphically
 * divided based on the look and feel implementation, and the two
 * <code>Component</code>s can then be interactively resized by the user.
 * Information on using <code>JSplitPane</code> is in <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/splitpane.html">How
 * to Use Split Panes</a> in <em>The Java Tutorial</em>.
 * <p>
 * The two <code>Component</code>s in a split pane can be aligned left to right
 * using <code>JSplitPane.HORIZONTAL_SPLIT</code>, or top to bottom using
 * <code>JSplitPane.VERTICAL_SPLIT</code>. The preferred way to change the size
 * of the <code>Component</code>s is to invoke <code>setDividerLocation</code>
 * where <code>location</code> is either the new x or y position, depending on
 * the orientation of the <code>JSplitPane</code>.
 * <p>
 * To resize the <code>Component</code>s to their preferred sizes invoke
 * <code>resetToPreferredSizes</code>.
 * <p>
 * When the user is resizing the <code>Component</code>s the minimum size of the
 * <code>Components</code> is used to determine the maximum/minimum position the
 * <code>Component</code>s can be set to. If the minimum size of the two
 * components is greater than the size of the split pane the divider will not
 * allow you to resize it. To alter the minimum size of a
 * <code>JComponent</code>, see {@link JComponent#setMinimumSize}.
 * <p>
 * When the user resizes the split pane the new space is distributed between the
 * two components based on the <code>resizeWeight</code> property. A value of 0,
 * the default, indicates the right/bottom component gets all the space, where
 * as a value of 1 indicates the left/top component gets all the space.
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
 * @see #setDividerLocation
 * @see #resetToPreferredSizes
 *
 * @author Scott Violet
 */
public class JSplitPane extends JComponent implements Accessible {
	private static final String uiClassID = "SplitPaneUI";

	public final static int VERTICAL_SPLIT = 0;
	public final static int HORIZONTAL_SPLIT = 1;
	public final static String LEFT = "left";
	public final static String RIGHT = "right";
	public final static String TOP = "top";
	public final static String BOTTOM = "bottom";
	public final static String DIVIDER = "divider";
	public final static String ORIENTATION_PROPERTY = "orientation";
	public final static String CONTINUOUS_LAYOUT_PROPERTY = "continuousLayout";
	public final static String DIVIDER_SIZE_PROPERTY = "dividerSize";
	public final static String ONE_TOUCH_EXPANDABLE_PROPERTY = "oneTouchExpandable";
	public final static String LAST_DIVIDER_LOCATION_PROPERTY = "lastDividerLocation";
	public final static String DIVIDER_LOCATION_PROPERTY = "dividerLocation";
	public final static String RESIZE_WEIGHT_PROPERTY = "resizeWeight";

	protected int orientation;
	protected boolean continuousLayout;
	protected Component leftComponent;
	protected Component rightComponent;
	protected int dividerSize;
	private boolean dividerSizeSet = false;

	protected boolean oneTouchExpandable;
	private boolean oneTouchExpandableSet;
	protected int lastDividerLocation;
	private double resizeWeight;
	private int dividerLocation;

	public JSplitPane() {
		this(JSplitPane.HORIZONTAL_SPLIT, UIManager.getBoolean("SplitPane.continuousLayout"),
				new JButton(UIManager.getString("SplitPane.leftButtonText")),
				new JButton(UIManager.getString("SplitPane.rightButtonText")));
	}

	public JSplitPane(int newOrientation) {
		this(newOrientation, UIManager.getBoolean("SplitPane.continuousLayout"));
	}

	public JSplitPane(int newOrientation, boolean newContinuousLayout) {
		this(newOrientation, newContinuousLayout, null, null);
	}

	public JSplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent) {
		this(newOrientation, UIManager.getBoolean("SplitPane.continuousLayout"), newLeftComponent,
				newRightComponent);
	}

	public JSplitPane(int newOrientation, boolean newContinuousLayout, Component newLeftComponent,
			Component newRightComponent) {
		super();

		if(L.isInWorkshop) {
			TrayMenuUtil.displayMessage(ResourceUtil.getErrorI18N(), "JSplitPane is NOT supported", IConstant.ERROR, null, 0);
		}
		
		dividerLocation = -1;
		setLayout(new BorderLayout());
		setUIProperty("opaque", Boolean.TRUE);
		orientation = newOrientation;
		if (orientation != HORIZONTAL_SPLIT && orientation != VERTICAL_SPLIT)
			throw new IllegalArgumentException(
					"cannot create JSplitPane, " + "orientation must be one of "
							+ "JSplitPane.HORIZONTAL_SPLIT " + "or JSplitPane.VERTICAL_SPLIT");
		continuousLayout = newContinuousLayout;
		if (newLeftComponent != null)
			setLeftComponent(newLeftComponent);
		if (newRightComponent != null)
			setRightComponent(newRightComponent);
		updateUI();
	}

	public void setUI(SplitPaneUI ui) {
	}

	public SplitPaneUI getUI() {
		return (SplitPaneUI) null;
	}

	public void updateUI() {
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public void setDividerSize(int newSize) {
		int oldSize = dividerSize;

		dividerSizeSet = true;
		if (oldSize != newSize) {
			dividerSize = newSize;
			firePropertyChange(DIVIDER_SIZE_PROPERTY, oldSize, newSize);
		}
	}

	public int getDividerSize() {
		return dividerSize;
	}

	public void setLeftComponent(Component comp) {
		if (comp == null) {
			if (leftComponent != null) {
				remove(leftComponent);
				leftComponent = null;
			}
		} else {
			if (orientation == JSplitPane.HORIZONTAL_SPLIT) {
				add(comp, BorderLayout.WEST);
			} else {
				add(comp, BorderLayout.NORTH);
			}
			leftComponent = comp;
		}
	}

	public Component getLeftComponent() {
		return leftComponent;
	}

	public void setTopComponent(Component comp) {
		setLeftComponent(comp);
	}

	public Component getTopComponent() {
		return leftComponent;
	}

	public void setRightComponent(Component comp) {
		if (comp == null) {
			if (rightComponent != null) {
				remove(rightComponent);
				rightComponent = null;
			}
		} else {
			if (orientation == JSplitPane.HORIZONTAL_SPLIT) {
				add(comp, BorderLayout.EAST);
			} else {
				add(comp, BorderLayout.SOUTH);
			}
			rightComponent = comp;
		}
	}

	public Component getRightComponent() {
		return rightComponent;
	}

	public void setBottomComponent(Component comp) {
		setRightComponent(comp);
	}

	public Component getBottomComponent() {
		return rightComponent;
	}

	public void setOneTouchExpandable(boolean newValue) {
		AndroidClassUtil.callEmptyMethod();

		oneTouchExpandable = newValue;
		oneTouchExpandableSet = true;
	}

	public boolean isOneTouchExpandable() {
		return oneTouchExpandable;
	}

	public void setLastDividerLocation(int newLastLocation) {
		AndroidClassUtil.callEmptyMethod();
		lastDividerLocation = newLastLocation;
	}

	public int getLastDividerLocation() {
		return lastDividerLocation;
	}

	public void setOrientation(int orientation) {
		if ((orientation != VERTICAL_SPLIT) && (orientation != HORIZONTAL_SPLIT)) {
			throw new IllegalArgumentException("JSplitPane: orientation must " + "be one of "
					+ "JSplitPane.VERTICAL_SPLIT or " + "JSplitPane.HORIZONTAL_SPLIT");
		}

		this.orientation = orientation;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setContinuousLayout(boolean newContinuousLayout) {
		continuousLayout = newContinuousLayout;
	}

	public boolean isContinuousLayout() {
		return continuousLayout;
	}

	public void setResizeWeight(double value) {
		if (value < 0 || value > 1) {
			throw new IllegalArgumentException("JSplitPane weight must be between 0 and 1");
		}
		resizeWeight = value;
	}

	public double getResizeWeight() {
		return resizeWeight;
	}

	public void resetToPreferredSizes() {
		SplitPaneUI ui = getUI();

		if (ui != null) {
			ui.resetToPreferredSizes(this);
		}
	}

	public void setDividerLocation(double proportionalLocation) {
		AndroidClassUtil.callEmptyMethod();
		// if (proportionalLocation < 0.0 || proportionalLocation > 1.0) {
		// throw new IllegalArgumentException("proportional location must "
		// + "be between 0.0 and 1.0.");
		// }
		// if (getOrientation() == VERTICAL_SPLIT) {
		// setDividerLocation((int) ((double) (getHeight() - getDividerSize()) *
		// proportionalLocation));
		// } else {
		// setDividerLocation((int) ((double) (getWidth() - getDividerSize()) *
		// proportionalLocation));
		// }
	}

	public void setDividerLocation(int location) {
		dividerLocation = location;
		AndroidClassUtil.callEmptyMethod();
	}

	public int getDividerLocation() {
		return dividerLocation;
	}

	public int getMinimumDividerLocation() {
		SplitPaneUI ui = getUI();

		if (ui != null) {
			return ui.getMinimumDividerLocation(this);
		}
		return -1;
	}

	public int getMaximumDividerLocation() {
		SplitPaneUI ui = getUI();

		if (ui != null) {
			return ui.getMaximumDividerLocation(this);
		}
		return -1;
	}

	public void remove(Component component) {
		if (component == leftComponent) {
			leftComponent = null;
		} else if (component == rightComponent) {
			rightComponent = null;
		}
		super.remove(component);
		// revalidate();
		// repaint();
	}

	public void remove(int index) {
		Component comp = getComponent(index);

		if (comp == leftComponent) {
			leftComponent = null;
		} else if (comp == rightComponent) {
			rightComponent = null;
		}
		super.remove(index);

		// revalidate();
		// repaint();
	}

	public void removeAll() {
		leftComponent = rightComponent = null;
		super.removeAll();

		// revalidate();
		// repaint();
	}

	public boolean isValidateRoot() {
		return true;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	void setUIProperty(String propertyName, Object value) {
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
