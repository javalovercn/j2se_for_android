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
import hc.android.J2SEInitor;
import hc.android.UICore;
import hc.android.AndroidUIUtil;
import hc.android.WindowManager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.plaf.RootPaneUI;

import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import hc.android.HCRUtil;

/**
 * A lightweight container used behind the scenes by <code>JFrame</code>,
 * <code>JDialog</code>, <code>JWindow</code>, <code>JApplet</code>, and
 * <code>JInternalFrame</code>. For task-oriented information on functionality
 * provided by root panes see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/rootpane.html">How
 * to Use Root Panes</a>, a section in <em>The Java Tutorial</em>.
 *
 * <p>
 * The following image shows the relationships between the classes that use root
 * panes.
 * <p align=center>
 * <img src="doc-files/JRootPane-1.gif" alt="The following text describes this
 * graphic." HEIGHT=484 WIDTH=629>
 * </p>
 * The &quot;heavyweight&quot; components (those that delegate to a peer, or
 * native component on the host system) are shown with a darker, heavier box.
 * The four heavyweight JFC/Swing containers (<code>JFrame</code>,
 * <code>JDialog</code>, <code>JWindow</code>, and <code>JApplet</code>) are
 * shown in relation to the AWT classes they extend. These four components are
 * the only heavyweight containers in the Swing library. The lightweight
 * container <code>JInternalFrame</code> is also shown. All five of these
 * JFC/Swing containers implement the <code>RootPaneContainer</code> interface,
 * and they all delegate their operations to a <code>JRootPane</code> (shown
 * with a little "handle" on top). <blockquote> <b>Note:</b> The
 * <code>JComponent</code> method <code>getRootPane</code> can be used to obtain
 * the <code>JRootPane</code> that contains a given component. </blockquote>
 * <table align="right" border="0" summary="layout">
 * <tr>
 * <td align="center">
 * <img src="doc-files/JRootPane-2.gif" alt="The following text describes this
 * graphic." HEIGHT=386 WIDTH=349></td>
 * </tr>
 * </table>
 * The diagram at right shows the structure of a <code>JRootPane</code>. A
 * <code>JRootpane</code> is made up of a <code>glassPane</code>, an optional
 * <code>menuBar</code>, and a <code>contentPane</code>. (The
 * <code>JLayeredPane</code> manages the <code>menuBar</code> and the
 * <code>contentPane</code>.) The <code>glassPane</code> sits over the top of
 * everything, where it is in a position to intercept mouse movements. Since the
 * <code>glassPane</code> (like the <code>contentPane</code>) can be an
 * arbitrary component, it is also possible to set up the <code>glassPane</code>
 * for drawing. Lines and images on the <code>glassPane</code> can then range
 * over the frames underneath without being limited by their boundaries.
 * <p>
 * Although the <code>menuBar</code> component is optional, the
 * <code>layeredPane</code>, <code>contentPane</code>, and
 * <code>glassPane</code> always exist. Attempting to set them to
 * <code>null</code> generates an exception.
 * <p>
 * To add components to the <code>JRootPane</code> (other than the optional menu
 * bar), you add the object to the <code>contentPane</code> of the
 * <code>JRootPane</code>, like this:
 * 
 * <pre>
 * rootPane.getContentPane().add(child);
 * </pre>
 * 
 * The same principle holds true for setting layout managers, removing
 * components, listing children, etc. All these methods are invoked on the
 * <code>contentPane</code> instead of on the <code>JRootPane</code>.
 * <blockquote> <b>Note:</b> The default layout manager for the
 * <code>contentPane</code> is a <code>BorderLayout</code> manager. However, the
 * <code>JRootPane</code> uses a custom <code>LayoutManager</code>. So, when you
 * want to change the layout manager for the components you added to a
 * <code>JRootPane</code>, be sure to use code like this:
 * 
 * <pre>
 * rootPane.getContentPane().setLayout(new BoxLayout());
 * </pre>
 * 
 * </blockquote> If a <code>JMenuBar</code> component is set on the
 * <code>JRootPane</code>, it is positioned along the upper edge of the frame.
 * The <code>contentPane</code> is adjusted in location and size to fill the
 * remaining area. (The <code>JMenuBar</code> and the <code>contentPane</code>
 * are added to the <code>layeredPane</code> component at the
 * <code>JLayeredPane.FRAME_CONTENT_LAYER</code> layer.)
 * <p>
 * The <code>layeredPane</code> is the parent of all children in the
 * <code>JRootPane</code> -- both as the direct parent of the menu and the
 * grandparent of all components added to the <code>contentPane</code>. It is an
 * instance of <code>JLayeredPane</code>, which provides the ability to add
 * components at several layers. This capability is very useful when working
 * with menu popups, dialog boxes, and dragging -- situations in which you need
 * to place a component on top of all other components in the pane.
 * <p>
 * The <code>glassPane</code> sits on top of all other components in the
 * <code>JRootPane</code>. That provides a convenient place to draw above all
 * other components, and makes it possible to intercept mouse events, which is
 * useful both for dragging and for drawing. Developers can use
 * <code>setVisible</code> on the <code>glassPane</code> to control when the
 * <code>glassPane</code> displays over the other children. By default the
 * <code>glassPane</code> is not visible.
 * <p>
 * The custom <code>LayoutManager</code> used by <code>JRootPane</code> ensures
 * that:
 * <OL>
 * <LI>The <code>glassPane</code> fills the entire viewable area of the
 * <code>JRootPane</code> (bounds - insets).
 * <LI>The <code>layeredPane</code> fills the entire viewable area of the
 * <code>JRootPane</code>. (bounds - insets)
 * <LI>The <code>menuBar</code> is positioned at the upper edge of the
 * <code>layeredPane</code>.
 * <LI>The <code>contentPane</code> fills the entire viewable area, minus the
 * <code>menuBar</code>, if present.
 * </OL>
 * Any other views in the <code>JRootPane</code> view hierarchy are ignored.
 * <p>
 * If you replace the <code>LayoutManager</code> of the <code>JRootPane</code>,
 * you are responsible for managing all of these views. So ordinarily you will
 * want to be sure that you change the layout manager for the
 * <code>contentPane</code> rather than for the <code>JRootPane</code> itself!
 * <p>
 * The painting architecture of Swing requires an opaque <code>JComponent</code>
 * to exist in the containment hieararchy above all other components. This is
 * typically provided by way of the content pane. If you replace the content
 * pane, it is recommended that you make the content pane opaque by way of
 * <code>setOpaque(true)</code>. Additionally, if the content pane overrides
 * <code>paintComponent</code>, it will need to completely fill in the
 * background in an opaque color in <code>paintComponent</code>.
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
 * @see JLayeredPane
 * @see JMenuBar
 * @see JWindow
 * @see JFrame
 * @see JDialog
 * @see JApplet
 * @see JInternalFrame
 * @see JComponent
 * @see BoxLayout
 *
 * @see <a href="http://java.sun.com/products/jfc/tsc/articles/mixing/"> Mixing
 *      Heavy and Light Components</a>
 *
 * @author David Kloba
 */
/// PENDING(klobad) Who should be opaque in this component?
public class JRootPane extends JComponent implements Accessible {
	private static final String uiClassID = "RootPaneUI";
	private final int shiftPixel = 2;

	// private final String HEX_WINDOW_TITLE_COLOR = "#3C72C2";
	// final int TITLE_BORDER_COLOR =
	// Color.decode(HEX_WINDOW_TITLE_COLOR).toAndroid();//透蓝"#4876FF"
	final int WINDOW_BORDER_WIDTH_PIXEL = 2;

	public static final int NONE = 0;
	public static final int FRAME = 1;
	public static final int PLAIN_DIALOG = 2;
	public static final int INFORMATION_DIALOG = 3;
	public static final int ERROR_DIALOG = 4;
	public static final int COLOR_CHOOSER_DIALOG = 5;
	public static final int FILE_CHOOSER_DIALOG = 6;
	public static final int QUESTION_DIALOG = 7;
	public static final int WARNING_DIALOG = 8;

	TextView titleBar;
	ImageView exitIcon;
	int titleHeight = 0;

	private int windowDecorationStyle = FRAME;
	protected JMenuBar menuBar;
	protected Container contentPane;
	protected JLayeredPane layeredPane;

	protected Component glassPane;
	protected JButton defaultButton;
	protected DefaultAction defaultPressAction;
	protected DefaultAction defaultReleaseAction;

	protected Window currWindow;

	boolean useTrueDoubleBuffering = true;
	private int iconHeight;

	ActionListener keyActionListener;
	KeyStroke keyStroke;

	@Override
	public void registerKeyboardAction(ActionListener anAction, KeyStroke aKeyStroke,
			int aCondition) {
		this.keyActionListener = anAction;
		this.keyStroke = aKeyStroke;
	}

	public boolean matchKeyStrokeAdAPI(int keyCode) {
		int regKeyCode = keyStroke.getKeyCode();
		if (regKeyCode == KeyEvent.VK_ESCAPE && (keyCode == android.view.KeyEvent.KEYCODE_BACK
				|| keyCode == android.view.KeyEvent.KEYCODE_ESCAPE)) {
			keyActionListener
					.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
			return true;
		}
		return false;
	}

	private static final int getIconSizeAdAPI() {
		final int width = J2SEInitor.screenWidth;
		final int height = J2SEInitor.screenHeight;

		if (width >= 720 && height >= 720) {
			// Integer.parseInt((String)ConfigManager.get(ConfigManager.DEVICE_DPI,
			// "100")) >= 160
			// 以上参数为32寸电视配置
			return 64 / 2;
		}
		if (width >= 280 && height >= 280) {
			return 32 / 2;
		} else {
			return AndroidUIUtil.MIN_ICON_SIZE;
		}
	}

	final static int ESC_ICON_SIZE = getIconSizeAdAPI();

	public static int getEscIconHeightAdAPI() {
		Bitmap bitmap = getEscIconBitmapAdAPI();
		return bitmap.getHeight();
	}

	private static Bitmap getEscIconBitmapAdAPI() {
		return AndroidUIUtil.getBitmap("esc_" + ESC_ICON_SIZE + ".png");
	}

	public JRootPane() {
		setGlassPane(createGlassPane());
		setLayeredPane(createLayeredPane());
		setContentPane(createContentPane());
		// setLayout(createRootLayout());

		setDoubleBuffered(true);

		titleBar = new TextView(ActivityManager.applicationContext);
		titleBar.setText("");

		exitIcon = new ImageView(ActivityManager.applicationContext);

		Bitmap bitmap = getEscIconBitmapAdAPI();
		iconHeight = bitmap.getHeight();
		// if(iconHeight / 2 >= UIUtil.MIN_ICON_SIZE){
		// iconHeight = iconHeight / 2;
		// }
		// if(iconHeight < UIUtil.getDefaultDialogFont().getSize()){
		// iconHeight = UIUtil.getDefaultDialogFont().getSize();
		// }
		exitIcon.setImageBitmap(bitmap);
		exitIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				WindowManager.notifyClose(currWindow);
			}
		});
		exitIcon.setBackgroundResource(HCRUtil.getResource(HCRUtil.R_drawable_button_title));
		exitIcon.setFocusable(true);
		exitIcon.setFocusableInTouchMode(false);
		exitIcon.setClickable(true);
		titleHeight = Math.max(titleBar.getHeight(), exitIcon.getHeight());

		// updateUI();
	}

	public int getTitleBarHeightAdAPI() {
		return titleHeight;
	}

	public int getMenuBarHeightAdAPI() {
		if (menuBar != null && menuBar.isVisible()) {
			return menuBar.getPreferredSize().height;
		}
		return 0;
	}

	public void setTitleAdAPI(String title) {
		if (title == null) {
			return;
		}
		titleBar.setText(" " + title);
	}

	public void setDoubleBuffered(boolean aFlag) {
		AndroidClassUtil.callEmptyMethod();
	}

	public int getWindowDecorationStyle() {
		return windowDecorationStyle;
	}

	public void setWindowDecorationStyle(int windowDecorationStyle) {
		this.windowDecorationStyle = windowDecorationStyle;
	}

	public RootPaneUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setUI(RootPaneUI ui) {
		AndroidClassUtil.callEmptyMethod();
		// super.setUI(ui);
	}

	public void updateUI() {
		AndroidClassUtil.callEmptyMethod();
	}

	public String getUIClassID() {
		return uiClassID;
	}

	protected JLayeredPane createLayeredPane() {
		JLayeredPane p = new JLayeredPane();
		return p;
	}

	protected Container createContentPane() {
		JComponent c = new JPanel() {
			public Container getParent() {
				return currWindow;
			}
		};
		c.setLayout(new BorderLayout() {
			public void addLayoutComponent(Component comp, Object constraints) {
				if (constraints == null) {
					constraints = BorderLayout.CENTER;
				}
				super.addLayoutComponent(comp, constraints);
			}
		});
		// DebugLogger.log("createContentPane : " + c.toString());
		return c;
	}

	protected Component createGlassPane() {
		JComponent c = new JPanel();
		c.setVisible(false);
		((JPanel) c).setOpaque(false);
		return c;
	}

	public void setJMenuBar(JMenuBar menu) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setMenuBar(JMenuBar menu) {
		AndroidClassUtil.callEmptyMethod();
	}

	public JMenuBar getJMenuBar() {
		return menuBar;
	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}

	LinearLayout rootLinearView;

	private View buildTitleView() {
		LinearLayout titleView = new LinearLayout(ActivityManager.applicationContext);
		titleView.setFocusable(false);
		titleView.setOrientation(LinearLayout.HORIZONTAL);
		// titleView.setBackgroundColor(TITLE_BORDER_COLOR);
		{
			// ShapeDrawable sd = new ShapeDrawable();
			// sd.setShape(new sh))
			// String xml =
			// "<shape
			// xmlns:android=\"http://schemas.android.com/apk/res/android\"
			// android:shape=\"rectangle\">"+
			// " <stroke android:width=\"1dp\" android:color=\"#FF404040\" /> "+
			// " <corners android:radius=\"6dp\" /> "+
			// " <gradient android:startColor=\"#FF6800\"
			// android:centerColor=\"#FF8000\" android:endColor=\"#FF9700\"
			// android:angle=\"90\" /> "+
			// "</shape>";
			// titleView.setBackground(UIUtil.buildDrawbleFromString(xml));
			titleView.setBackgroundResource(HCRUtil.getResource(HCRUtil.R_drawable_window_title));
		}

		{
			final LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT,
					0.5F);// 当title区宽度大于contentPane宽度时，缩小以适合
			llp.gravity = Gravity.CENTER_VERTICAL;
			llp.topMargin = shiftPixel;
			titleBar.setTextColor(AndroidUIUtil.WINDOW_TITLE_FONT_COLOR.toAndroid());
			titleBar.setGravity(Gravity.LEFT);
			final int maxFontSize = getTitleBarHeight(iconHeight);
			titleBar.setTextSize(TypedValue.COMPLEX_UNIT_PX, maxFontSize);
			titleBar.setBackgroundColor(0);

			titleBar.setFocusable(false);

			AndroidUIUtil.addView(titleView, titleBar, llp, viewRelation);
		}

		{
			final LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			llp.gravity = (Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			llp.topMargin = shiftPixel;
			llp.rightMargin = 10;
			AndroidUIUtil.addView(titleView, exitIcon, llp, viewRelation);
		}
		return titleView;
	}

	public static int getTitleBarHeight(final int iconH) {
		return Math.max(iconH, (int) J2SEInitor.getAndroidServerScreenAdapter()
				.getFontSizeInPixel(UICore.getDefaultDialogInputFontForSystemUIOnly().getSize()));
	}

	@Override
	public View getPeerAdAPI() {
		if (rootLinearView == null) {
			rootLinearView = new LinearLayout(ActivityManager.applicationContext);
		}

		rootLinearView.removeAllViews();
		rootLinearView.setOrientation(LinearLayout.VERTICAL);
		rootLinearView.setBackgroundColor(0);// UIUtil.WIN_BODY_BACK.toAndroid());

		boolean isFrame = isShowTitleBar();
		if (isFrame) {
			{
				final LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT, 0.5F);
				AndroidUIUtil.addView(rootLinearView, buildTitleView(), llp, viewRelation);
			}

			if (menuBar != null && menuBar.isVisible()) {
				final LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);

				View menuBarView = menuBar.getPeerAdAPI();
				menuBarView.setBackgroundResource(
						HCRUtil.getResource(HCRUtil.R_drawable_window_menu_bar));
				AndroidUIUtil.addView(rootLinearView, menuBarView, llp, viewRelation);
			}
		}

		if (contentPane != null || glassPane != null) {
			if (contentPane != null) {
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						FrameLayout.LayoutParams.WRAP_CONTENT,
						FrameLayout.LayoutParams.WRAP_CONTENT, 1.0F);
				View contentView = contentPane.getPeerAdAPI();
				if (isFrame) {
					contentView.setBackgroundResource(
							HCRUtil.getResource(HCRUtil.R_drawable_window_body));
				} else {
					contentView.setBackgroundColor(AndroidUIUtil.WIN_BODY_BACK.toAndroid());// 专供无标题的窗口背景，否则会透明。
				}
				AndroidUIUtil.addView(rootLinearView, contentView, lp, viewRelation);
			}
		}

		return rootLinearView;
	}

	public void setContentPane(Container content) {
		if (contentPane != null && contentPane.getParent() == layeredPane) {
			layeredPane.remove(contentPane);
		}
		contentPane = content;
		if (currWindow != null) {
			content.parent = currWindow;
		}
		layeredPane.add(contentPane, JLayeredPane.FRAME_CONTENT_LAYER);
	}

	public Container getContentPane() {
		return contentPane;
	}

	public void setLayeredPane(JLayeredPane layered) {
		if (layeredPane != null && layeredPane.getParent() == this) {
			this.remove(layeredPane);
		}
		layeredPane = layered;

		this.add(layeredPane, -1);
	}

	public JLayeredPane getLayeredPane() {
		return layeredPane;
	}

	public void setGlassPane(Component glass) {
		boolean visible = false;
		if (glassPane != null && glassPane.getParent() == this) {
			this.remove(glassPane);
			visible = glassPane.isVisible();
		}

		glass.setVisible(visible);
		glassPane = glass;
		this.add(glassPane, 0);
		if (visible) {
			repaint();
		}
	}

	public Component getGlassPane() {
		return glassPane;
	}

	public boolean isValidateRoot() {
		return true;
	}

	public boolean isOptimizedDrawingEnabled() {
		return !glassPane.isVisible();
	}

	public void addNotify() {
	}

	public void removeNotify() {
	}

	public void setDefaultButton(JButton defaultButton) {
		JButton oldDefault = this.defaultButton;

		if (oldDefault != defaultButton) {
			this.defaultButton = defaultButton;
		}
	}

	int getTitleViewHeight() {
		return isShowTitleBar() ? titleHeight : 0;
	}

	public JButton getDefaultButton() {
		return defaultButton;
	}

	final void setUseTrueDoubleBuffering(boolean useTrueDoubleBuffering) {
		this.useTrueDoubleBuffering = useTrueDoubleBuffering;
	}

	final boolean getUseTrueDoubleBuffering() {
		return useTrueDoubleBuffering;
	}

	final void disableTrueDoubleBuffering() {
		AndroidClassUtil.callEmptyMethod();
	}

	static class DefaultAction extends AbstractAction {
		JButton owner;
		JRootPane root;
		boolean press;

		DefaultAction(JRootPane root, boolean press) {
			this.root = root;
			this.press = press;
		}

		public void setOwner(JButton owner) {
			this.owner = owner;
		}

		public void actionPerformed(ActionEvent e) {
		}

		public boolean isEnabled() {
			return owner.getModel().isEnabled();
		}
	}

	protected void addImpl(Component comp, Object constraints, int index) {
		super.addImpl(comp, constraints, index);

		// set glassPane to top.
		if (glassPane != null && glassPane.getParent() == this && getComponent(0) != glassPane) {
			add(glassPane, 0);
		}
	}

	protected String paramString() {
		return super.paramString();
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

	private boolean isShowTitleBar() {
		return windowDecorationStyle != NONE;
	}

}
