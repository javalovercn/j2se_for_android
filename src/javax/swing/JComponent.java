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

import hc.App;
import hc.android.ActivityManager;
import hc.android.AndroidClassUtil;
import hc.android.CanvasGraphics;
import hc.android.UICore;
import hc.android.AndroidUIUtil;
import hc.android.ViewRelation;
import hc.core.L;
import hc.core.util.LogManager;
import hc.util.PropertiesManager;

import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.VetoableChangeListener;
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.Locale;
import java.util.Set;

import javax.accessibility.AccessibleContext;
import javax.swing.border.Border;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;

/**
 * The base class for all Swing components except top-level containers. To use a
 * component that inherits from <code>JComponent</code>, you must place the
 * component in a containment hierarchy whose root is a top-level Swing
 * container. Top-level Swing containers -- such as <code>JFrame</code>,
 * <code>JDialog</code>, and <code>JApplet</code> -- are specialized components
 * that provide a place for other Swing components to paint themselves. For an
 * explanation of containment hierarchies, see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/overview/hierarchy.html">Swing
 * Components and the Containment Hierarchy</a>, a section in <em>The Java
 * Tutorial</em>.
 *
 * <p>
 * The <code>JComponent</code> class provides:
 * <ul>
 * <li>The base class for both standard and custom components that use the Swing
 * architecture.
 * <li>A "pluggable look and feel" (L&F) that can be specified by the programmer
 * or (optionally) selected by the user at runtime. The look and feel for each
 * component is provided by a <em>UI delegate</em> -- an object that descends
 * from {@link javax.swing.plaf.ComponentUI}. See
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/plaf.html">How
 * to Set the Look and Feel</a> in <em>The Java Tutorial</em> for more
 * information.
 * <li>Comprehensive keystroke handling. See the document <a href=
 * "http://java.sun.com/products/jfc/tsc/special_report/kestrel/keybindings.html">Keyboard
 * Bindings in Swing</a>, an article in <em>The Swing Connection</em>, for more
 * information.
 * <li>Support for tool tips -- short descriptions that pop up when the cursor
 * lingers over a component. See <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/tooltip.html">How
 * to Use Tool Tips</a> in <em>The Java Tutorial</em> for more information.
 * <li>Support for accessibility. <code>JComponent</code> contains all of the
 * methods in the <code>Accessible</code> interface, but it doesn't actually
 * implement the interface. That is the responsibility of the individual classes
 * that extend <code>JComponent</code>.
 * <li>Support for component-specific properties. With the
 * {@link #putClientProperty} and {@link #getClientProperty} methods, you can
 * associate name-object pairs with any object that descends from
 * <code>JComponent</code>.
 * <li>An infrastructure for painting that includes double buffering and support
 * for borders. For more information see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/overview/draw.html">Painting</a>
 * and <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/misc/border.html">How to Use
 * Borders</a>, both of which are sections in <em>The Java Tutorial</em>.
 * </ul>
 * For more information on these subjects, see the
 * <a href="package-summary.html#package_description">Swing package
 * description</a> and <em>The Java Tutorial</em> section <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/jcomponent.html">The
 * JComponent Class</a>.
 * <p>
 * <code>JComponent</code> and its subclasses document default values for
 * certain properties. For example, <code>JTable</code> documents the default
 * row height as 16. Each <code>JComponent</code> subclass that has a
 * <code>ComponentUI</code> will create the <code>ComponentUI</code> as part of
 * its constructor. In order to provide a particular look and feel each
 * <code>ComponentUI</code> may set properties back on the
 * <code>JComponent</code> that created it. For example, a custom look and feel
 * may require <code>JTable</code>s to have a row height of 24. The documented
 * defaults are the value of a property BEFORE the <code>ComponentUI</code> has
 * been installed. If you need a specific value for a particular property you
 * should explicitly set it.
 * <p>
 * In release 1.4, the focus subsystem was rearchitected. For more information,
 * see
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/focus.html">
 * How to Use the Focus Subsystem</a>, a section in <em>The Java Tutorial</em>.
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
 * @see KeyStroke
 * @see Action
 * @see #setBorder
 * @see #registerKeyboardAction
 * @see JOptionPane
 * @see #setDebugGraphicsOptions
 * @see #setToolTipText
 * @see #setAutoscrolls
 *
 * @author Hans Muller
 * @author Arnaud Weber
 */
public abstract class JComponent extends Container
		implements Serializable, TransferHandler.HasGetTransferHandler {
	private View borderView;
	private View containerView;
	private boolean isAlignmentXSet;
	private float alignmentX;
	private boolean isAlignmentYSet;
	private float alignmentY;

	private ActionMap actionMap;
	private InputMap focusInputMap;
	private InputMap ancestorInputMap;
	private ComponentInputMap windowInputMap;

	protected transient ComponentUI ui;

	public static final int WHEN_FOCUSED = 0;
	public static final int WHEN_ANCESTOR_OF_FOCUSED_COMPONENT = 1;
	public static final int WHEN_IN_FOCUSED_WINDOW = 2;
	public static final int UNDEFINED_CONDITION = -1;

	public static final String TOOL_TIP_TEXT_KEY = "ToolTipText";

	public void setInheritsPopupMenu(final boolean value) {
		// boolean oldValue = getFlag(INHERITS_POPUP_MENU);
		// setFlag(INHERITS_POPUP_MENU, value);
	}

	public boolean getInheritsPopupMenu() {
		return false;// getFlag(INHERITS_POPUP_MENU);
	}

	JPopupMenu popup;

	public void setComponentPopupMenu(final JPopupMenu popup) {
		this.popup = popup;
	}

	public JPopupMenu getComponentPopupMenu() {
		return popup;
	}

	public JComponent() {
		super();
	}

	public void setPaintGraphicsAdAPI(final Graphics g) {
		this.paintGraphics = g;
	}

	@Override
	public void requestFocus() {
		if (requireFirstFocusAdAPI()) {
			return;
		}

		if (getFocusablePeerViewAdAPI() != null) {
			AndroidUIUtil.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					getFocusablePeerViewAdAPI().requestFocus();
				}
			});
		} else {
			if (containerView != null && containerView.isFocusable()) {
				AndroidUIUtil.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						containerView.requestFocus();
					}
				});
			}
		}
	}

	/**
	 * 
	 * @return true:成功设置标识。false:已显示，只需立即requireFocus
	 */
	public boolean requireFirstFocusAdAPI() {
		Container parent = getParent();
		while (parent != null) {
			if (parent instanceof Window) {
				if (((Window) parent).isFireComponentShow) {
					return false;
				} else {
					break;
				}
			} else {
				parent = parent.getParent();
			}
		}
		isRequireFirstFocus = true;
		return true;
	}

	public void setPeerAdAPI(final View peer) {
		if (peer == null) {
			return;
		}

		super.setPeerAdAPI(peer);
		paintGraphics = null;
	}

	protected void setUI(final ComponentUI newUI) {
	}

	public void updateUI() {
	}

	public String getUIClassID() {
		return JComponent.class.getSimpleName();
	}

	protected Graphics getComponentGraphics(final Graphics g) {
		return g;
	}

	protected void paintComponent(final Graphics g) {
		paint(g);
	}

	protected void paintChildren(final Graphics g) {
	}

	protected void paintBorder(final Graphics g) {
	}

	public void update(final Graphics g) {
		paint(g);
	}

	public void paint(final Graphics g) {
	}

	void paintForceDoubleBuffered(final Graphics g) {
	}

	boolean isPainting() {
		return true;
	}

	public void printAll(final Graphics g) {
		print(g);
	}

	class CircleView extends LinearLayout {
		Paint paint1;

		public CircleView(final Context context) {
			super(context);
			init();
		}

		public CircleView(final Context context, final AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public void init() {
			paint1 = new Paint();
			paint1.setColor(android.graphics.Color.RED);
		}

		protected void onDraw(final Canvas canvas) {
			// super.onDraw(canvas);
			canvas.drawCircle(50, 50, 25, paint1);
			// this.draw(canvas);
		}
	}

	boolean isPrintMeasured = false;

	public void print(final Graphics g) {
		// 由于JScrollPane不适合Android
		if (this instanceof JScrollPane) {
			((JScrollPane) this).getViewportViewAdAPI().print(g);
			return;
		}

		final View view = getPeerAdAPI();

		if (g instanceof CanvasGraphics) {
			final Canvas canvas = ((CanvasGraphics) g).getCanvasAdAPI();

			if (isPrintMeasured == false) {
				isPrintMeasured = true;

				validate();
			}

			final Rect rect = new Rect();
			rect.set(0, 0, canvas.getWidth(), canvas.getHeight());
			final int widthSpec = View.MeasureSpec.makeMeasureSpec(rect.width(),
					View.MeasureSpec.EXACTLY);
			final int heightSpec = View.MeasureSpec.makeMeasureSpec(rect.height(),
					View.MeasureSpec.EXACTLY);
			view.measure(widthSpec, heightSpec);

			// Lay the view out at the rect width and height
			view.layout(0, 0, rect.width(), rect.height());

			view.draw(canvas);
		}
	}

	protected void printComponent(final Graphics g) {
		paintComponent(g);
	}

	protected void printChildren(final Graphics g) {
		paintChildren(g);
	}

	protected void printBorder(final Graphics g) {
		paintBorder(g);
	}

	public boolean isPaintingTile() {
		return true;// getFlag(IS_PAINTING_TILE);
	}

	public final boolean isPaintingForPrint() {
		return true;// getFlag(IS_PRINTING);
	}

	public boolean isManagingFocus() {
		return false;
	}

	public void setNextFocusableComponent(final Component aComponent) {
		AndroidClassUtil.callEmptyMethod();
	}

	public Component getNextFocusableComponent() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setRequestFocusEnabled(final boolean requestFocusEnabled) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean isRequestFocusEnabled() {
		AndroidClassUtil.callEmptyMethod();
		return true;// !getFlag(REQUEST_FOCUS_DISABLED);
	}

	public boolean requestFocus(final boolean temporary) {
		requestFocus();
		return true;
		// if(border == null){
		// return getPeerAdAPI().requestFocusFromTouch();
		// }else{
		// return containerView.requestFocusFromTouch();
		// }
	}

	public boolean requestFocusInWindow() {
		requestFocus();
		return true;
	}

	protected boolean requestFocusInWindow(final boolean temporary) {
		requestFocus();
		return true;
		// return getPeerOrEmptyCanvas().requestFocus();
	}

	public void grabFocus() {
		requestFocus();
	}

	public void setVerifyInputWhenFocusTarget(final boolean verifyInputWhenFocusTarget) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getVerifyInputWhenFocusTarget() {
		return true;
	}

	public FontMetrics getFontMetrics(final Font font) {
		return AndroidUIUtil.getFontMetrics(this, font);
	}

	public void setPreferredSize(final Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
	}

	public Dimension getPreferredSize() {
		if (isPreferredSizeSet()) {
			return super.getPreferredSize();
		}
		Dimension size = null;
		size = getViewUISizeAdAPI(size);
		final Dimension out = (size != null) ? size : super.getPreferredSize();
		if (PropertiesManager.isSimu() && this instanceof JScrollPane) {
			System.out.println("getPreferredSize w : " + out.width + ", h : " + out.height
					+ " of JScrollPane : " + this.toString());
		}
		return out;
		// final Dimension dim = super.getPreferredSize();
		// final Insets insets = getInsets();
		// dim.width += insets.left + insets.right;
		// dim.height += insets.top + insets.bottom;
		// return dim;
	}

	private final Dimension getViewUISizeAdAPI(Dimension size) {
		if (layout == null) {
			View peerView = getPeerAdAPI();
			if (peerView != null && isUsePaintView == false
					&& (peerView instanceof AbsoluteLayout == false)) {
				size = new Dimension();
				AndroidUIUtil.getViewWidthAndHeight(peerView, size);
			}
		}
		return size;
	}

	public void setMaximumSize(final Dimension maximumSize) {
		super.setMaximumSize(maximumSize);
	}

	public Dimension getMaximumSize() {
		if (isMaximumSizeSet()) {
			return super.getMaximumSize();
		}
		Dimension size = null;
		size = getViewUISizeAdAPI(size);
		return (size != null) ? size : super.getMaximumSize();

		// final Dimension dim = super.getMaximumSize();
		// final Insets insets = getInsets();
		// dim.width += insets.left + insets.right;
		// dim.height += insets.top + insets.bottom;
		// return dim;
	}

	public void setMinimumSize(final Dimension minimumSize) {
		super.setMinimumSize(minimumSize);
	}

	public Dimension getMinimumSize() {
		if (isMinimumSizeSet()) {
			return super.getMinimumSize();
		}
		Dimension size = null;
		size = getViewUISizeAdAPI(size);
		return (size != null) ? size : super.getMinimumSize();

		// Dimension dim = super.getMinimumSize();
		// if(dim == null){//有可能出现
		// dim = new Dimension(0, 0);
		// }
		// final Insets insets = getInsets();
		// dim.width += insets.left + insets.right;
		// dim.height += insets.top + insets.bottom;
		// return dim;
	}

	public boolean contains(final int x, final int y) {
		if (borderView != null) {
			return containsAdAPI(borderView, x, y);
		} else {
			return containsAdAPI(super.getPeerAdAPI(), x, y);
		}
	}

	public boolean contains(final Point point) {
		return contains(point.x, point.y);
	}

	Border border;
	protected final ViewRelation viewRelation = new ViewRelation();

	@Override
	public View getPeerAdAPI() {
		if (border != null) {
			return borderView;
		}
		return super.getPeerAdAPI();// getPeerOrEmptyCanvas();
	}

	@Override
	public void setLayout(final LayoutManager mgr) {
		super.setLayout(mgr);
		if (borderView != null) {
			restructView(layoutView);
		}
	}

	public void setBorder(final Border p_border) {
		if (borderView != null || p_border == null) {
			return;
		}
		this.border = p_border;

		restructView(super.getPeerAdAPI());
	}

	private final void restructView(final View layoutView) {
		if (containerView != null) {
			LogManager.log("can NOT setBorder again!!!");
			return;
		}

		// LogManager.log("restruct border view for " + toString());

		// 必须先取出。因本类重载getPeerAdAPI
		this.containerView = layoutView;

		this.borderView = border.getBorderViewAdAPI();

		AndroidUIUtil.replaceChildOrBorderView(getParent(), containerView, borderView);
		viewRelation.registerViewRelation((ViewGroup) borderView, containerView);
		super.setPeerAdAPI(borderView);
		// 增加内pad
		{
			final boolean isFirst = (defaultLinearLayout == null);
			if (isFirst) {
				defaultLinearLayout = new LinearLayout(ActivityManager.applicationContext);
			} else {
				defaultLinearLayout.removeAllViews();
			}

			final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			final int padPx = AndroidUIUtil.pxToDp(2, UICore.getDeviceDensity());
			lp.topMargin = lp.leftMargin = lp.rightMargin = lp.bottomMargin = padPx;
			// containerView.setPadding(padPx, padPx, padPx, padPx);
			AndroidUIUtil.addView(defaultLinearLayout, containerView, lp, viewRelation);

			if (isFirst) {
				border.setComponentViewAdAPI(defaultLinearLayout, this);
			}
		}
		// revalidate();
	}

	LinearLayout defaultLinearLayout;

	public Border getBorder() {
		return border;
	}

	public Insets getInsets() {
		if (border != null) {
			final Insets insets = border.getBorderInsets(this);
			// LogManager.log("getBorderInsets [" + toString() + "] top : " +
			// insets.top + ", left : " + insets.left + ", right : " +
			// insets.right + ", bottom : " + insets.bottom);
			return insets;
		}
		return super.getInsets();
	}

	public Insets getInsets(final Insets insets) {
		if (border != null) {
			return border.getBorderInsets(this);
		}
		return super.getInsets();
	}

	public float getAlignmentY() {
		if (isAlignmentYSet) {
			return alignmentY;
		}
		return 0;
	}

	public void setAlignmentY(final float alignmentY) {
		this.alignmentY = alignmentY > 1.0f ? 1.0f : alignmentY < 0.0f ? 0.0f : alignmentY;
		isAlignmentYSet = true;
	}

	public float getAlignmentX() {
		if (isAlignmentXSet) {
			return alignmentX;
		}
		return 0;
	}

	public void setAlignmentX(final float alignmentX) {
		this.alignmentX = alignmentX > 1.0f ? 1.0f : alignmentX < 0.0f ? 0.0f : alignmentX;
		isAlignmentXSet = true;
	}

	public void setInputVerifier(final InputVerifier inputVerifier) {
	}

	public InputVerifier getInputVerifier() {
		return null;
	}

	public Graphics getGraphics() {
		return paintGraphics;
	}

	public void setDebugGraphicsOptions(final int debugOptions) {
	}

	public int getDebugGraphicsOptions() {
		return 0;
	}

	int shouldDebugGraphics() {
		return 0;
	}

	public void registerKeyboardAction(final ActionListener anAction, final String aCommand,
			final KeyStroke aKeyStroke, final int aCondition) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	void componentInputMapChanged(final ComponentInputMap inputMap) {
	}

	public void registerKeyboardAction(final ActionListener anAction, final KeyStroke aKeyStroke,
			final int aCondition) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void unregisterKeyboardAction(final KeyStroke aKeyStroke) {
		AndroidClassUtil.callEmptyMethod();
	}

	public KeyStroke[] getRegisteredKeyStrokes() {
		AndroidClassUtil.callEmptyMethod();
		return new KeyStroke[0];
	}

	public int getConditionForKeyStroke(final KeyStroke aKeyStroke) {
		return UNDEFINED_CONDITION;
	}

	public ActionListener getActionForKeyStroke(final KeyStroke aKeyStroke) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void resetKeyboardActions() {
		AndroidClassUtil.callEmptyMethod();
	}

	public final void setInputMap(final int condition, final InputMap map) {
		switch (condition) {
		case WHEN_IN_FOCUSED_WINDOW:
			if (map != null && !(map instanceof ComponentInputMap)) {
				throw new IllegalArgumentException(
						"WHEN_IN_FOCUSED_WINDOW InputMaps must be of type ComponentInputMap");
			}
			windowInputMap = (ComponentInputMap) map;
			break;
		case WHEN_ANCESTOR_OF_FOCUSED_COMPONENT:
			ancestorInputMap = map;
			break;
		case WHEN_FOCUSED:
			focusInputMap = map;
			break;
		default:
			throw new IllegalArgumentException(
					"condition must be one of JComponent.WHEN_IN_FOCUSED_WINDOW, JComponent.WHEN_FOCUSED or JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT");
		}
	}

	public final InputMap getInputMap(final int condition) {
		return getInputMap(condition, true);
	}

	public final InputMap getInputMap() {
		return getInputMap(WHEN_FOCUSED, true);
	}

	public final void setActionMap(final ActionMap am) {
		actionMap = am;
	}

	public final ActionMap getActionMap() {
		return getActionMap(true);
	}

	final InputMap getInputMap(final int condition, final boolean create) {
		switch (condition) {
		case WHEN_FOCUSED:
			if (focusInputMap != null) {
				return focusInputMap;
			}
			// Hasn't been created yet.
			if (create) {
				final InputMap km = new InputMap();
				setInputMap(condition, km);
				return km;
			}
			break;
		case WHEN_ANCESTOR_OF_FOCUSED_COMPONENT:
			if (ancestorInputMap != null) {
				return ancestorInputMap;
			}
			// Hasn't been created yet.
			if (create) {
				final InputMap km = new InputMap();
				setInputMap(condition, km);
				return km;
			}
			break;
		case WHEN_IN_FOCUSED_WINDOW:
			if (windowInputMap != null) {
				return windowInputMap;
			}
			// Hasn't been created yet.
			if (create) {
				final ComponentInputMap km = new ComponentInputMap(this);
				setInputMap(condition, km);
				return km;
			}
			break;
		default:
			throw new IllegalArgumentException(
					"condition must be one of JComponent.WHEN_IN_FOCUSED_WINDOW, JComponent.WHEN_FOCUSED or JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT");
		}
		return null;
	}

	final ActionMap getActionMap(final boolean create) {
		if (actionMap == null) {
			final ActionMap am = new ActionMap();
			setActionMap(am);
		}
		return actionMap;
	}

	public int getBaseline(final int width, final int height) {
		AndroidClassUtil.callEmptyMethod();
		return -1;
	}

	public BaselineResizeBehavior getBaselineResizeBehavior() {
		AndroidClassUtil.callEmptyMethod();
		return BaselineResizeBehavior.OTHER;
	}

	public boolean requestDefaultFocus() {
		return getPeerAdAPI().requestFocus();
	}

	public void setVisible(final boolean isVisible) {
		super.setVisible(isVisible);
	}

	public void setEnabled(final boolean enabled) {
		final boolean oldValue = isEnable;
		super.setEnabled(enabled);
		if (oldValue != enabled) {
			firePropertyChange("enabled", oldValue, enabled);
		}
		updateUI();
	}

	protected void notifyChangeAdAPI() {
		try {
			final ChangeEvent event = new ChangeEvent(this);
			final ChangeListener[] listener = getListeners(ChangeListener.class);
			if (listener != null) {
				for (int i = 0; i < listener.length; i++) {
					listener[i].stateChanged(event);
				}
			}
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}

	public void setForeground(final Color fg) {
		if (fg == null) {
			return;
		}

		if (super.getPeerAdAPI() instanceof android.widget.TextView) {
			final android.widget.TextView tv = (android.widget.TextView) super.getPeerAdAPI();
			(tv).setTextColor(fg.toAndroid());
		}
	}

	public void setBackground(final Color bg) {
		if (bg == null) {
			return;
		}
		super.getPeerAdAPI().setBackgroundColor(bg.toAndroid());
	}

	public void setFont(final Font newFont) {
		Font oldFont = font;
		super.setFont(newFont);
		// if (newFont != oldFont) {
		// revalidate();
		// repaint();
		// }
	}

	static public Locale getDefaultLocale() {
		return Locale.getDefault();
	}

	static public void setDefaultLocale(final Locale l) {
	}

	protected void processComponentKeyEvent(final KeyEvent e) {
		AndroidClassUtil.callEmptyMethod();
	}

	protected void processKeyEvent(final KeyEvent e) {
		super.processKeyEvent(e);
	}

	protected boolean processKeyBinding(final KeyStroke ks, final KeyEvent e, final int condition,
			final boolean pressed) {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	boolean processKeyBindings(final KeyEvent e, final boolean pressed) {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	static boolean processKeyBindingsForAllComponents(final KeyEvent e, final Container container,
			final boolean pressed) {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	String toolTip;

	public void setToolTipText(final String text) {
		toolTip = text;
	}

	public String getToolTipText() {
		return toolTip;
	}

	public String getToolTipText(final MouseEvent event) {
		return getToolTipText();
	}

	public Point getToolTipLocation(final MouseEvent event) {
		AndroidClassUtil.callEmptyMethod();
		return new Point(0, 0);
	}

	public Point getPopupLocation(final MouseEvent event) {
		AndroidClassUtil.callEmptyMethod();
		return new Point(0, 0);
	}

	public JToolTip createToolTip() {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public void scrollRectToVisible(final Rectangle aRect) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setAutoscrolls(final boolean autoscrolls) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getAutoscrolls() {
		AndroidClassUtil.callEmptyMethod();
		return true;
	}

	public void setTransferHandler(final TransferHandler newHandler) {
		AndroidClassUtil.callEmptyMethod();
	}

	public TransferHandler getTransferHandler() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	TransferHandler.DropLocation dropLocationForPoint(final Point p) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	Object setDropLocation(final TransferHandler.DropLocation location, final Object state,
			final boolean forDrop) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	void dndDone() {
		AndroidClassUtil.callEmptyMethod();
	}

	protected void processMouseEvent(final MouseEvent e) {
		super.processMouseEvent(e);
	}

	public void processMouseEventAdAPI(final MouseEvent e) {
		processMouseEvent(e);
	}

	public void processMouseMotionEventAdAPI(final MouseEvent e) {
		processMouseMotionEvent(e);
	}

	protected void processMouseMotionEvent(final MouseEvent e) {
		final MouseMotionListener[] listeners = getMouseMotionListeners();
		if (listeners != null) {
			for (int i = 0; i < listeners.length; i++) {
				if (e.getButton() == MouseEvent.NOBUTTON) {
					listeners[i].mouseMoved(e);
				} else {
					listeners[i].mouseDragged(e);
				}
			}
		}
	}

	void superProcessMouseMotionEvent(final MouseEvent e) {
		AndroidClassUtil.callEmptyMethod();
	}

	void setCreatedDoubleBuffer(final boolean newValue) {
		AndroidClassUtil.callEmptyMethod();
	}

	boolean getCreatedDoubleBuffer() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void enable() {
		setEnabled(true);
		// View v = super.getPeerAdAPI();
		// if(v instanceof ViewGroup){
		// enableDisableViewGroup((ViewGroup)v, true);
		// }else{
		// v.setEnabled(true);
		// }
	}

	public void disable() {
		setEnabled(false);
		// View v = super.getPeerAdAPI();
		// if(v instanceof ViewGroup){
		// enableDisableViewGroup((ViewGroup)v, false);
		// }else{
		// v.setEnabled(false);
		// }
	}

	private static void enableDisableViewGroup(final ViewGroup viewGroup, final boolean enabled) {
		final int childCount = viewGroup.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View view = viewGroup.getChildAt(i);
			view.setEnabled(enabled);
			if (view instanceof ViewGroup) {
				enableDisableViewGroup((ViewGroup) view, enabled);
			}
		}
	}

	private static void setVisibleViewGroup(final ViewGroup viewGroup, final boolean isVisible) {
		final int childCount = viewGroup.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View view = viewGroup.getChildAt(i);
			view.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
			if (view instanceof ViewGroup) {
				enableDisableViewGroup((ViewGroup) view, isVisible);
			}
		}
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

	// private ArrayTable getClientProperties() {
	// return new ArrayTable();
	// }

	public final Object getClientProperty(final Object key) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public final void putClientProperty(final Object key, final Object value) {
		AndroidClassUtil.callEmptyMethod();
	}

	void clientPropertyChanged(final Object key, final Object oldValue, final Object newValue) {
		AndroidClassUtil.callEmptyMethod();
	}

	void setUIProperty(final String propertyName, final Object value) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setFocusTraversalKeys(final int id, final Set<? extends AWTKeyStroke> keystrokes) {
		AndroidClassUtil.callEmptyMethod();
	}

	public static boolean isLightweightComponent(final Component c) {
		return c instanceof JComponent;
	}

	/**
	 * @deprecated As of JDK 5, replaced by
	 *             <code>Component.setBounds(int, int, int, int)</code>.
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public void reshape(final int x, final int y, final int w, final int h) {
		setBounds(x, y, w, h);
	}

	public Rectangle getBounds(final Rectangle rv) {
		if (rv == null) {
			return new Rectangle(getX(), getY(), getWidth(), getHeight());
		} else {
			rv.setBounds(getX(), getY(), getWidth(), getHeight());
			return rv;
		}
	}

	public Dimension getSize(final Dimension rv) {
		if (rv == null) {
			return new Dimension(getWidth(), getHeight());
		} else {
			rv.setSize(getWidth(), getHeight());
			return rv;
		}
	}

	public Point getLocation(final Point rv) {
		if (rv == null) {
			return new Point(getX(), getY());
		} else {
			rv.setLocation(getX(), getY());
			return rv;
		}
	}

	public int getX() {
		// return (int)getPeerAdAPI().getX();
		return super.getX();
	}

	public int getY() {
		// return (int)getPeerAdAPI().getY();
		return super.getY();
	}

	public int getWidth() {
		if (isDisablePaintGAdAPI == false) {
			if (paintGraphics != null) {
				return ((CanvasGraphics) paintGraphics).getCanvasAdAPI().getWidth();
			}
		}
		// final View snapPeer = getPeerAdAPI();//需要将Border计算入内
		// if(snapPeer != null){
		// return snapPeer.getWidth();
		// }else{
		// return getMinimumSize().width;
		// }
		return super.getWidth();
	}

	public int getHeight() {
		if (isDisablePaintGAdAPI == false) {
			if (paintGraphics != null) {
				return ((CanvasGraphics) paintGraphics).getCanvasAdAPI().getWidth();
			}
		}
		// final View snapPeer = getPeerAdAPI();//需要将Border计算入内
		// if(snapPeer != null){
		// return snapPeer.getHeight();
		// }else{
		// return getMinimumSize().height;
		// }
		return super.getHeight();
	}

	public boolean isOpaque() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void setOpaque(final boolean isOpaque) {
		AndroidClassUtil.callEmptyMethod();
	}

	boolean rectangleIsObscured(final int x, final int y, final int width, final int height) {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void computeVisibleRect(final Rectangle visibleRect) {
		AndroidClassUtil.callEmptyMethod();
	}

	public Rectangle getVisibleRect() {
		AndroidClassUtil.callEmptyMethod();
		final Rectangle visibleRect = new Rectangle();

		computeVisibleRect(visibleRect);
		return visibleRect;
	}

	public void firePropertyChange(final String propertyName, final boolean oldValue,
			final boolean newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	public void firePropertyChange(final String propertyName, final int oldValue,
			final int newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	public void firePropertyChange(final String propertyName, final char oldValue,
			final char newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	// for DefaultListCellRenderer
	public void firePropertyChange(final String propertyName, final short oldValue,
			final short newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	// for DefaultListCellRenderer
	public void firePropertyChange(final String propertyName, final byte oldValue,
			final byte newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	// for DefaultListCellRenderer
	public void firePropertyChange(final String propertyName, final long oldValue,
			final long newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	// for DefaultListCellRenderer
	public void firePropertyChange(final String propertyName, final float oldValue,
			final float newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	// for DefaultListCellRenderer
	public void firePropertyChange(final String propertyName, final double oldValue,
			final double newValue) {
		super.firePropertyChange(propertyName, oldValue, newValue);
	}

	protected void fireVetoableChange(final String propertyName, final Object oldValue,
			final Object newValue) {
		AndroidClassUtil.callEmptyMethod();
	}

	public synchronized void addVetoableChangeListener(final VetoableChangeListener listener) {
		list.add(VetoableChangeListener.class, listener);
	}

	public synchronized void removeVetoableChangeListener(final VetoableChangeListener listener) {
		list.remove(VetoableChangeListener.class, listener);
	}

	public synchronized VetoableChangeListener[] getVetoableChangeListeners() {
		return getListeners(VetoableChangeListener.class);
	}

	/**
	 * Returns the top-level Window.
	 * 
	 * @return
	 */
	public Container getTopLevelAncestor() {
		for (Container p = this; p != null; p = p.getParent()) {
			if (p instanceof Window) {
				return p;
			}
		}
		return null;
	}

	private AncestorNotifier getAncestorNotifier() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void addAncestorListener(final AncestorListener listener) {
		if (listener == null) {
			return;
		}
		list.add(AncestorListener.class, listener);
	}

	public void removeAncestorListener(final AncestorListener listener) {
		if (listener == null) {
			return;
		}
		list.remove(AncestorListener.class, listener);
	}

	public AncestorListener[] getAncestorListeners() {
		return list.getListeners(AncestorListener.class);
	}

	public void addNotify() {
		AndroidClassUtil.callEmptyMethod();
	}

	public void removeNotify() {
		AndroidClassUtil.callEmptyMethod();
	}

	public void repaint(final long tm, final int x, final int y, final int width,
			final int height) {
		super.getPeerAdAPI().invalidate(x, y, x + width, y + height);
		// LogManager.log("repain x : " + x + ", y : " + y + ", width : " +
		// width + ", height : " + height);
	}

	public void repaint(final Rectangle r) {
		repaint(0, r.x, r.y, r.width, r.height);
	}

	public void revalidate() {
		paintGraphics = null;
		// final View snapPeer = super.getPeerAdAPI();
		// AndroidUIUtil.runOnUiThread(new Runnable() {
		// @Override
		// public void run() {
		// snapPeer.invalidate();
		// }
		// });
		super.revalidate();
	}

	public void invalidate() {
		paintGraphics = null;
		// super.getPeerAdAPI().invalidate();
		super.invalidate();
	}

	public void validate() {
		paintGraphics = null;
		AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				JComponent.super.validate();				
			}
		});
	}

	public boolean isValidateRoot() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public boolean isOptimizedDrawingEnabled() {
		AndroidClassUtil.callEmptyMethod();
		return true;
	}

	protected boolean isPaintingOrigin() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void paintImmediately(final int x, final int y, final int w, final int h) {
		repaint(x, y, w, h);
	}

	public void paintImmediately(final Rectangle r) {
		paintImmediately(r.x, r.y, r.width, r.height);
	}

	boolean alwaysOnTop() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	void setPaintingChild(final Component paintingChild) {
		AndroidClassUtil.callEmptyMethod();
	}

	boolean checkIfChildObscuredBySibling() {
		AndroidClassUtil.callEmptyMethod();
		return true;
	}

	public void setDoubleBuffered(final boolean aFlag) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean isDoubleBuffered() {
		AndroidClassUtil.callEmptyMethod();
		return true;
	}

	public JRootPane getRootPane() {
		if (this instanceof RootPaneContainer) {
			return ((RootPaneContainer) this).getRootPane();
		}
		Component c = this;
		for (; c != null; c = c.getParent()) {
			if (c instanceof JRootPane) {
				return (JRootPane) c;
			}
		}
		return null;
	}

	void compWriteObjectNotify() {
		AndroidClassUtil.callEmptyMethod();
	}

	public void validateObject() throws InvalidObjectException {
		AndroidClassUtil.callEmptyMethod();
	}

	protected String paramString() {
		AndroidClassUtil.callEmptyMethod();
		return "";
	}

	public void hide() {
		setVisible(false);
	}

}