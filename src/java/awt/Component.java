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
package java.awt;

import hc.android.ActivityManager;
import hc.android.AndroidClassUtil;
import hc.android.AndroidUIUtil;
import hc.android.HCTabHost;
import hc.android.J2SEInitor;
import hc.android.ScreenAdapter;
import hc.android.UICore;
import hc.core.ConfigManager;
import hc.core.L;
import hc.core.util.LogManager;
import hc.server.html5.syn.MletHtmlCanvas;
import hc.util.PropertiesManager;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyListener;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.image.ImageObserver;
import java.awt.peer.ComponentPeer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.EventListener;
import java.util.Locale;

import javax.accessibility.AccessibleContext;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;

import android.view.View;
import android.widget.AbsoluteLayout;

/**
 * A <em>component</em> is an object having a graphical representation
 * that can be displayed on the screen and that can interact with the
 * user. Examples of components are the buttons, checkboxes, and scrollbars
 * of a typical graphical user interface. <p>
 * The <code>Component</code> class is the abstract superclass of
 * the nonmenu-related Abstract Window Toolkit components. Class
 * <code>Component</code> can also be extended directly to create a
 * lightweight component. A lightweight component is a component that is
 * not associated with a native window. On the contrary, a heavyweight
 * component is associated with a native window. The {@link #isLightweight()}
 * method may be used to distinguish between the two kinds of the components.
 * <p>
 * Lightweight and heavyweight components may be mixed in a single component
 * hierarchy. However, for correct operating of such a mixed hierarchy of
 * components, the whole hierarchy must be valid. When the hierarchy gets
 * invalidated, like after changing the bounds of components, or
 * adding/removing components to/from containers, the whole hierarchy must be
 * validated afterwards by means of the {@link Container#validate()} method
 * invoked on the top-most invalid container of the hierarchy.
 * <p>
 * <h3>Serialization</h3>
 * It is important to note that only AWT listeners which conform
 * to the <code>Serializable</code> protocol will be saved when
 * the object is stored.  If an AWT object has listeners that
 * aren't marked serializable, they will be dropped at
 * <code>writeObject</code> time.  Developers will need, as always,
 * to consider the implications of making an object serializable.
 * One situation to watch out for is this:
 * <pre>
 *    import java.awt.*;
 *    import java.awt.event.*;
 *    import java.io.Serializable;
 *
 *    class MyApp implements ActionListener, Serializable
 *    {
 *        BigObjectThatShouldNotBeSerializedWithAButton bigOne;
 *        Button aButton = new Button();
 *
 *        MyApp()
 *        {
 *            // Oops, now aButton has a listener with a reference
 *            // to bigOne!
 *            aButton.addActionListener(this);
 *        }
 *
 *        public void actionPerformed(ActionEvent e)
 *        {
 *            System.out.println("Hello There");
 *        }
 *    }
 * </pre>
 * In this example, serializing <code>aButton</code> by itself
 * will cause <code>MyApp</code> and everything it refers to
 * to be serialized as well.  The problem is that the listener
 * is serializable by coincidence, not by design.  To separate
 * the decisions about <code>MyApp</code> and the
 * <code>ActionListener</code> being serializable one can use a
 * nested class, as in the following example:
 * <pre>
 *    import java.awt.*;
 *    import java.awt.event.*;
 *    import java.io.Serializable;
 *
 *    class MyApp implements java.io.Serializable
 *    {
 *         BigObjectThatShouldNotBeSerializedWithAButton bigOne;
 *         Button aButton = new Button();
 *
 *         static class MyActionListener implements ActionListener
 *         {
 *             public void actionPerformed(ActionEvent e)
 *             {
 *                 System.out.println("Hello There");
 *             }
 *         }
 *
 *         MyApp()
 *         {
 *             aButton.addActionListener(new MyActionListener());
 *         }
 *    }
 * </pre>
 * <p>
 * <b>Note</b>: For more information on the paint mechanisms utilitized
 * by AWT and Swing, including information on how to write the most
 * efficient painting code, see
 * <a href="http://java.sun.com/products/jfc/tsc/articles/painting/index.html">Painting in AWT and Swing</a>.
 * <p>
 * For details on the focus subsystem, see
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/focus.html">
 * How to Use the Focus Subsystem</a>,
 * a section in <em>The Java Tutorial</em>, and the
 * <a href="../../java/awt/doc-files/FocusSpec.html">Focus Specification</a>
 * for more information.
 *
 * @author      Arthur van Hoff
 * @author      Sami Shaio
 */
public abstract class Component implements ImageObserver, MenuContainer,
		Serializable {
	private View peer;
	public Container parent;
	protected final EventListenerList list = new EventListenerList();
	protected AccessibleContext accessibleContext = null;

	protected Font font;
	Font peerFont;
	Locale locale;
	
	private String name;

	private final View.OnLayoutChangeListener layoutChangeListener = new View.OnLayoutChangeListener() {
		@Override
		public void onLayoutChange(final View arg0, final int left, final int top, final int right, final int bottom, final int oldLeft, final int oldTop, final int oldRight, final int oldBottom) {
			if(left != oldLeft || top != oldTop){
				fireComponentMovedAdAPI();
			}
			
			
			if((oldRight - oldLeft) != (right - left) || (oldBottom - oldTop) != (bottom - top)){
				fireComponentResizedAdAPI();
			}
		}
	};
	
	public void setPeerAdAPI(final View peer) {
		this.peer = peer;
		addViewToParentAdAPI();
	}
	
	static final Object LOCK = new Object();
	
	public final Object getTreeLock() {
        return LOCK;
    }
	
	private int boundsOp = ComponentPeer.DEFAULT_OPERATION;
	
	int getBoundsOp() {
		return boundsOp;
	}
	
	void setBoundsOp(int op) {
		if (op == ComponentPeer.RESET_OPERATION) {
            boundsOp = ComponentPeer.DEFAULT_OPERATION;
        } else
            if (boundsOp == ComponentPeer.DEFAULT_OPERATION) {
                boundsOp = op;
            }
	}
	
	public final void addOnLayoutChangeListenerAdAPI(final View view){
		view.removeOnLayoutChangeListener(layoutChangeListener);
		view.addOnLayoutChangeListener(layoutChangeListener);
	}
	
	public View getPeerAdAPI() {
		return peer;
	}

	Point getLocationOnWindow() {
		if(isValid() == false){
			return new Point(0, 0);
		}else{
			final int[] xy = new int[2];
			peer.getLocationInWindow(xy);
			return new Point(xy[0], xy[1]);
		}
	}

	void applyCurrentShape() {
	}

	public boolean isFocusOwner() {
		return hasFocus();
	}

	public boolean hasFocus() {
		if(isValid() == false){
			return false;
		}else{
			return peer.isFocused();
		}
	}

	void mixOnReshaping() {
	}

	protected Dimension minSize;
	boolean isSetMinSize;
	boolean isDoneServerMinSize;

	protected Dimension preSize;
	boolean isSetPrefSize;
	boolean isDoneServerPreSize;

	protected Dimension maxSize;
	boolean isSetMaxSize;
	boolean isDoneServerMaxSize;

	public static final float TOP_ALIGNMENT = 0.0f;
	public static final float CENTER_ALIGNMENT = 0.5f;
	public static final float BOTTOM_ALIGNMENT = 1.0f;
	public static final float LEFT_ALIGNMENT = 0.0f;
	public static final float RIGHT_ALIGNMENT = 1.0f;

	public Component() {
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Container getParent() {
		return parent;
	}

	Container getContainer() {
		return getParent();
	}

	public int getBaseline(final int width, final int height) {
		if (width < 0 || height < 0) {
			throw new IllegalArgumentException("Width and height must be >= 0");
		}
		if(isValid() == false){
			return -1;
		}else{
			return peer.getBaseline();
		}
	}
	
	public Component.BaselineResizeBehavior getBaselineResizeBehavior(){
		return Component.BaselineResizeBehavior.OTHER;
	}

	public Toolkit getToolkit() {
		return Toolkit.getDefaultToolkit();
	}

	public boolean isValid() {
		return (peer != null) && valid;
	}

	public boolean isDisplayable() {
		if(isValid() == false){
			return false;
		}else{
			return true;
		}
	}

	public boolean isVisible() {
		if(isValid() == false){
			return true;
		}else{
			return (peer.getVisibility() == View.VISIBLE);
		}
	}

	public boolean isShowing() {
		if (isVisible()) {
			final Container parent = this.parent;
			return (parent != null) && parent.isShowing();//近似正确
		}
		return false;
	}

	public Component locate(final int x, final int y) {
		return contains(x, y) ? this : null;
	}

	public ComponentOrientation getComponentOrientation() {
		return componentOrientation;
	}

	public synchronized void addKeyListener(final KeyListener l) {
		if (l == null) {
			return;
		}
		list.add(KeyListener.class, l);
	}

	public synchronized void removeKeyListener(final KeyListener l) {
		if (l == null) {
			return;
		}
		list.remove(KeyListener.class, l);
	}

	public synchronized KeyListener[] getKeyListeners() {
		return list.getListeners(KeyListener.class);
	}

	public void applyComponentOrientation(final ComponentOrientation orientation) {
		if (orientation == null) {
			throw new NullPointerException();
		}
		setComponentOrientation(orientation);
	}

	ComponentOrientation componentOrientation = ComponentOrientation.LEFT_TO_RIGHT;

	public void setComponentOrientation(final ComponentOrientation o) {
		if(o == null){
			return;
		}
		componentOrientation = o;
//		if(o.isLeftToRight() == false && peer != null){
//			Class[] para = {Integer.class};
//			Object[] paraValues = {Integer.valueOf(View.TEXT_DIRECTION_ANY_RTL)};
//			ClassUtil.invoke(View.class, peer, "setTextDirection", para, paraValues);
//		}
	}

	public boolean contains(final int x, final int y) {
		return inside(x, y);
	}

	public boolean containsAdAPI(final View view, final int x, final int y){
		if(x >= 0 && x <= (view.getRight() - view.getLeft()) && y >= 0 && y <= (view.getBottom() - view.getTop())){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean inAdAPI(final View view, final int x, final int y){
		if(x >= view.getLeft() && x <= view.getRight() && y >= view.getTop() && y <= view.getBottom()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * replaced by contains(int, int).
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean inside(final int x, final int y) {
		if(isValid()){
			return containsAdAPI(peer, x, y);
		}else{
			return false;
		}
	}

	private volatile boolean valid = false;
	protected boolean isEnable = true;
	protected boolean isVisible = true;
	
	public boolean isEnabled() {
		return isEnable;
	}

	public void setEnabled(final boolean b) {
		isEnable = b;
		setForeground(isEnable?AndroidUIUtil.WIN_FONT_COLOR:AndroidUIUtil.WIN_FONT_DISABLE_COLOR);
	}

	public void enable() {
		setEnabled(true);
	}

	public void enable(final boolean b) {
		setEnabled(b);
	}

	public void disable() {
		setEnabled(false);
	}

	public boolean isDoubleBuffered() {
		return false;
	}

	public void setVisible(final boolean b) {
		final boolean isChanged = (isVisible == b);
		isVisible = b;
		
		if(isChanged){//必须要变动
			if(isVisible){
				fireComponentShowAdAPI();
			}else{
				fireComponentHiddenAdAPI();
			}
		}
	}

	public void show() {
		if(peer != null){
			peer.setVisibility(View.VISIBLE);
		}
	}

	public void show(final boolean b) {
		if (b) {
			show();
		} else {
			hide();
		}
	}

	boolean containsFocus() {
		return isFocusOwner();
	}

	public void hide() {
		if(isValid()){
			peer.setVisibility(View.GONE);
		}
	}

	public Color getForeground() {
//		if(isValid()){
//			if(peer instanceof TextView){
//				ColorStateList colorList = ((TextView)peer).getTextColors();
//				int[] colorint = new int[3];
//				colorList.getColorForState(colorint, 0);
//				return UIUtil.toJ2SEColor(colorint[0]);
//			}
//		}
		L.V = L.O ? false : LogManager.log("return [WHITE] getForeground for Component : " + this.getClass().getName());
		return Color.WHITE;
	}

	boolean isForegroundSet = false;
	
	public void setForeground(final Color c) {
		isForegroundSet = true;
		
//		if(isValid()){
//			if(peer instanceof TextView){
//				((TextView)peer).setTextColor(c.toAndroid());
//				return;
//			}
//		}
		L.V = L.O ? false : LogManager.log("skip setForeground for Component : " + this.getClass().getName());
	}

	public boolean isForegroundSet() {
		return isForegroundSet;
	}

	public Color getBackground() {
		L.V = L.O ? false : LogManager.log("return [WHITE] getBackground for Component : " + this.getClass().getName());
		return Color.WHITE;
	}

	boolean isBackgroundSet = false;
	
	public void setBackground(final Color c) {
		isBackgroundSet = true;
		if(isValid()){
			peer.setBackgroundColor(c.toAndroid());
			return;
		}
		L.V = L.O ? false : LogManager.log("skip setBackground for Component : " + this.getClass().getName());
	}

	public boolean isBackgroundSet() {
		return isBackgroundSet;
	}

	boolean isFontSet = false;
	
	public Font getFont() {
		if (font != null) {
			return font;
		} else {
			return UICore.buildDefaultDialogInputFont();
		}
	}

	public void setFont(final Font f) {
		if(f == null){
			return;
		}
		
		font = f;
//		f.screenAdapter = getScreenAdapterAdAPI();
		isFontSet = true;
//		if(isValid()){
//			if(peer instanceof TextView){
//			}
//		}
//		L.V = L.O ? false : LogManager.log("skip setFont for Component : " + this.getClass().getName());
	}

	public boolean isFontSet() {
		return isFontSet;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(final Locale l) {
		locale = l;
	}

	public Point getLocation() {
		return location();
	}

	public Point getLocationOnScreen() {
		if(isValid() == false){
			return new Point(0,0);
		}else{
			Point pt = getLocation();
            for(Component c = this.getParent(); c != null; c = c.getParent()) {
                pt.x += c.x;
                pt.y += c.y;
            }
            return pt;
		}
	}

	public Point location() {
//		if(isValid() == false){
//			return new Point(-1, -1);
//		}else{
			return new Point(x, y);
//		}
	}

	public void setLocation(final int x, final int y) {
		move(x, y);
	}

	public void move(final int x, final int y) {
		setBounds(x, y, getWidth(), getHeight());
	}

	public void setLocation(final Point p) {
		setLocation(p.x, p.y);
	}

	public Dimension getSize() {
		return size();
	}

	public Dimension size() {
		return new Dimension(getWidth(), getHeight());
	}

	public void setSize(final int width, final int height) {
		resize(width, height);
	}

	public void resize(final int width, final int height) {
		setBounds(getX(), getY(), width, height);
	}

	public void setSize(final Dimension d) {
		resize(d);
	}

	public void resize(final Dimension d) {
		resize(d.width, d.height);
	}

	public Rectangle getBounds() {
		return getBounds(new Rectangle());
	}

	public Rectangle bounds() {
		return getBounds();
	}

	public void setBounds(final Rectangle r) {
		setBounds(r.x, r.y, r.width, r.height);
	}

	public void setBounds(final int x, final int y, final int width, final int height) {
		if(AndroidUIUtil.isJViewportInstance(this)){
			((JViewport)this).getView().setBounds(x, y, width, height);
			return;
		}
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		if(PropertiesManager.isSimu()){
			final boolean isContain = this instanceof Container;
			final LayoutManager lm = isContain?((Container)this).getLayout():null; 
			final String layDesc = isContain?((lm==null)?"null":lm.toString()):"null";
			L.V = L.O ? false : LogManager.log("setBounds [x : " + x + ", y : " + y + ", w : " + width + ", h : " + height + "] for " + this.toString() + ", layout : " + layDesc);
		}
		
		if(peer != null){
			addViewToParentAdAPI();
			return;
		}
		L.V = L.O ? false : LogManager.log("skip setBounds for Component : " + this.getClass().getName());
	}

	public final void addViewToParentAdAPI() {
		if(parent != null){
			final View containerView = parent.getContainerViewAdAPI();
			
			if(containerView != null){
				if(containerView instanceof AbsoluteLayout){
					AndroidUIUtil.removeFromParent(peer);
					AbsoluteLayout vg = (AbsoluteLayout)containerView;
					
	//					L.V = L.O ? false : LogManager.log("AbsoluteLayout addView for " + this.toString());
					final Border border = (parent instanceof JComponent)?((JComponent)parent).getBorder():null;
					final Insets insets = (border != null)?border.getBorderInsets(parent):null;
					vg.addView(peer, new AbsoluteLayout.LayoutParams(width, height, x - ((insets != null)?insets.left:0), y - ((insets != null)?insets.top:0)));
				}else if(containerView instanceof HCTabHost){
					//view已加入，不用处理
				}
			}
		}
	}

	void setGraphicsConfiguration(final GraphicsConfiguration gc) {
		throw new Error(hc.android.AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}
	
	int x, y, width, height;
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Rectangle getBounds(Rectangle rv) {
		if (rv == null) {
			rv = new Rectangle(0, 0, 0, 0);
		}
		
		rv.x = x;
		rv.y = y;
		rv.width = width;
		rv.height = height;
		
//		L.V = L.O ? false : LogManager.log("getBounds [x : "+ x + ", y : " + y + ", w : " + width+ ", h : " + height + "] for " + this.toString());
		
		return rv;
	}

	public Dimension getSize(final Dimension rv) {
		if (rv == null) {
			return new Dimension(getWidth(), getHeight());
		} else {
			rv.setSize(getWidth(), getHeight());
			return rv;
		}
	}

	public Point getLocation(Point rv) {
		if (rv == null) {
			rv = new Point(0, 0);
		}
		rv.x = x;
		rv.y = y;
		return rv;
	}

	public boolean isOpaque() {
		return false;
	}

	public boolean isLightweight() {
		return JComponent.isLightweightComponent(this);
	}

	public void setPreferredSize(final Dimension preferredSize) {
		if(this instanceof Dialog || this instanceof Frame){
			L.V = L.O ? false : LogManager.log("skip setPreferredSize for Dialog/Frame.");
		}else{
			this.preSize = preferredSize;
			isSetPrefSize = (preferredSize != null);
			
			{
				final View peerAdAPI = getPeerAdAPI();
				if(peerAdAPI == null){
					return;
				}
				
				peerAdAPI.setMinimumWidth(preSize.width);
				peerAdAPI.setMinimumHeight(preSize.height);
			}
			
			isDoneServerPreSize = false;
			
			if(this instanceof JScrollPane && MletHtmlCanvas.isForAddHtml((JScrollPane)this)){
				if(PropertiesManager.isSimu()){
					L.V = L.O ? false : LogManager.log("ignore adapter size for AddHarHTMLMlet.");
				}
				return;
			}
			
			Boolean enable = (Boolean)ConfigManager.get(ConfigManager.UI_ENABLE_SCREEN_ADAPTER, Boolean.TRUE);
			if(enable){
				setPreSizeForServerUIAdAPI();
			}
		}
	}
	
	private final ScreenAdapter projScreenAdapter = ScreenAdapter.initScreenAdapterFromContext(false);
	
	public final ScreenAdapter getScreenAdapterAdAPI(){
		if(projScreenAdapter != null){
			return projScreenAdapter;
		}
		
		if(parent != null){
			return parent.getScreenAdapterAdAPI();
		}
		
		//比如JComboBox.setSelectedIndex/initSpinner, JPopupMenu可能返回null
		return J2SEInitor.getAndroidServerScreenAdapter();
	}
	
//	protected final void setMaxSizeAdAPI(){
//		if(isDoneServerMaxSize || isSetMaxSize == false || 
//				getScreenAdapterAdAPI() == null || screenAdapter.type != ScreenAdapter.TYPE_SERVER){
//			return;
//		}
//		
//		isDoneServerMaxSize = true;
//		
//		View peerAdAPI = getPeerAdAPI();
//		if(peerAdAPI == null){
//			return;
//		}
//		
//		maxSize.width = screenAdapter.getAdapterWidth(maxSize.width);
//		peerAdAPI.setMaximumWidth(maxSize.width);
//		maxSize.height = screenAdapter.getAdapterHeight(maxSize.height);
//		peerAdAPI.setMaximumHeight(maxSize.height);
//	}
	
	protected final void setMinSizeForServerUIAdAPI(){
		final ScreenAdapter screenAdapter = getScreenAdapterAdAPI();
		
		if(isDoneServerMinSize || isSetMinSize == false || 
				screenAdapter == null || screenAdapter.type != ScreenAdapter.TYPE_SERVER){
			return;
		}
		
		isDoneServerMinSize = true;
		
		final View peerAdAPI = getPeerAdAPI();
		if(peerAdAPI == null){
			return;
		}
		
		minSize.width = screenAdapter.getPreAdapterWidth(minSize.width);
		peerAdAPI.setMinimumWidth(minSize.width);
		minSize.height = screenAdapter.getPreAdapterHeight(minSize.height);
		peerAdAPI.setMinimumHeight(minSize.height);
	}

	protected final void setPreSizeForServerUIAdAPI() {
		final ScreenAdapter screenAdapter = getScreenAdapterAdAPI();
		
		if(isDoneServerPreSize || isSetPrefSize == false || 
				screenAdapter == null || screenAdapter.type != ScreenAdapter.TYPE_SERVER){
			return;
		}
		
		isDoneServerPreSize = true;
		
		final View peerAdAPI = getPeerAdAPI();
		if(peerAdAPI == null){
			return;
		}

		if(PropertiesManager.isSimu()){
			final int oldW = preSize.width;
			final int oldH = preSize.height;
			L.V = L.O ? false : LogManager.log("adapter Component preferred size [" + oldW + ", " + oldH + "] to [" + preSize.width + ", " + preSize.height + "]");
		}
		
		preSize.width = screenAdapter.getPreAdapterWidth(preSize.width);
		peerAdAPI.setMinimumWidth(preSize.width);
		preSize.height = screenAdapter.getPreAdapterHeight(preSize.height);
		peerAdAPI.setMinimumHeight(preSize.height);
	}

	public void enableInputMethods(final boolean enable) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void removePropertyChangeListener(final PropertyChangeListener listener) {
		list.add(PropertyChangeListener.class, listener);
	}

	public boolean isPreferredSizeSet() {
		return isSetPrefSize;
	}

	public Dimension getPreferredSize() {
		return preferredSize();
	}

	public Dimension preferredSize() {
		if(isSetPrefSize && preSize != null){
			return new Dimension(preSize);
		}
		
		if (preSize == null || !(isPreferredSizeSet() || isValid())) {
            if(peer == null) {
            	preSize = getMinimumSize();
            }else{
            	if(preSize == null){
            		preSize = new Dimension();
            	}
            	preSize.width = peer.getMeasuredWidth();
            	preSize.height = peer.getMeasuredHeight();
            	if(preSize.width == 0 || preSize.height == 0){
            		AndroidUIUtil.getViewWidthAndHeight(peer, preSize);
            	}
            }
//            if(getScreenAdapterAdAPI().type == ScreenAdapter.TYPE_SERVER){
//            	if(preSize.width > Window.getMaxWindowPreferredWidth()){
//            		preSize.width = Window.getMaxWindowPreferredWidth();
//            	}
//            	if(preSize.height > Window.getMaxWindowPreferredHeight()){
//            		preSize.height = Window.getMaxWindowPreferredHeight();
//            	}
//            }
		}
		return new Dimension(preSize);
	}

	public void setMinimumSize(final Dimension minimumSize) {
		if(this instanceof Dialog || this instanceof Frame){
			L.V = L.O ? false : LogManager.log("skip setPreferredSize for Dialog/Frame.");
		}else{
			this.minSize = minimumSize;
			isSetMinSize = (minimumSize != null);
			isDoneServerMinSize = false;
			setMinSizeForServerUIAdAPI();
		}
	}

	public PropertyChangeListener[] getPropertyChangeListeners() {
		return getListeners(PropertyChangeListener.class);
	}

	public PropertyChangeListener[] getPropertyChangeListeners(
			final String propertyName) {
		return getListeners(PropertyChangeListener.class);
	}

	public boolean isMinimumSizeSet() {
		return isSetMinSize;
	}

	public Dimension getMinimumSize() {
		return minimumSize();
	}

	public Dimension minimumSize() {
		if (minSize == null || !(isMinimumSizeSet() || isValid())) {
            if(peer == null) {
            	minSize = size();
            }else{
            	if(minSize == null){
            		minSize = new Dimension();
            	}
            	minSize.width = peer.getMeasuredWidth();
            	minSize.height = peer.getMeasuredHeight();
            }
		}
		return new Dimension(minSize);
	}

	public void setFocusable(final boolean focusable) {
		AndroidClassUtil.callEmptyMethod();
//		L.V = L.O ? false : LogManager.log("skip setFocusable for Component : " + this.getClass().getName());
		return;
//		if(isValid()){
//			peer.setFocusable(focusable);
//		}
	}
	
	public boolean isRequireFirstFocus = false;
	
	public boolean isFocusable(){
		return true;
//		if(isValid()){
//			return peer.isFocusable();
//		}else{
//			return false;
//		}
	}

	public synchronized void addMouseListener(final MouseListener l) {
		if (l == null) {
			return;
		}
		list.add(MouseListener.class, l);
	}

	public synchronized MouseListener[] getMouseListeners() {
		return list.getListeners(MouseListener.class);
	}

	public synchronized void removeMouseListener(final MouseListener l) {
		if (l == null) {
			return;
		}
		list.remove(MouseListener.class, l);
	}

	public synchronized void addMouseMotionListener(final MouseMotionListener l) {
		if (l == null) {
			return;
		}
		list.add(MouseMotionListener.class, l);
	}

	public synchronized void removeMouseMotionListener(final MouseMotionListener l) {
		if (l == null) {
			return;
		}
		list.remove(MouseMotionListener.class, l);
	}

	public synchronized MouseMotionListener[] getMouseMotionListeners() {
		return list.getListeners(MouseMotionListener.class);
	}

	public synchronized void addMouseWheelListener(final MouseWheelListener l) {
		if (l == null) {
			return;
		}
		list.add(MouseWheelListener.class, l);
	}

	public synchronized void removeMouseWheelListener(final MouseWheelListener l) {
		if (l == null) {
			return;
		}
		list.remove(MouseWheelListener.class, l);
	}

	public synchronized MouseWheelListener[] getMouseWheelListeners() {
		return list.getListeners(MouseWheelListener.class);
	}

	public synchronized void addInputMethodListener(final InputMethodListener l) {
		if (l == null) {
			return;
		}
		list.add(InputMethodListener.class, l);
	}

	public synchronized void removeInputMethodListener(final InputMethodListener l) {
		if (l == null) {
			return;
		}
		list.remove(InputMethodListener.class, l);
	}

	public synchronized InputMethodListener[] getInputMethodListeners() {
		return list.getListeners(InputMethodListener.class);
	}

	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		list.add(PropertyChangeListener.class, listener);
	}

	public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		this.addPropertyChangeListener(listener);
	}

	public synchronized void addComponentListener(final ComponentListener l) {
		if (l == null) {
			return;
		}
		list.add(ComponentListener.class, l);
	}

	public synchronized void removeComponentListener(final ComponentListener l) {
		if (l == null) {
			return;
		}
		list.remove(ComponentListener.class, l);
	}

	public synchronized ComponentListener[] getComponentListeners() {
		return getListeners(ComponentListener.class);
	}

	public void fireComponentResizedAdAPI(){
		final ComponentEvent event = new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED);
		
		final ComponentListener[] listeners = getComponentListeners();
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].componentResized(event);
		}
	}
	
	public void fireComponentMovedAdAPI(){
		final ComponentEvent event = new ComponentEvent(this, ComponentEvent.COMPONENT_MOVED);
		
		final ComponentListener[] listeners = getComponentListeners();
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].componentMoved(event);
		}
	}
	
	public void fireComponentShowAdAPI(){
		final ComponentEvent event = new ComponentEvent(this, ComponentEvent.COMPONENT_SHOWN);
		
		final ComponentListener[] listeners = getComponentListeners();
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].componentShown(event);
		}
	}
	
	public void fireComponentHiddenAdAPI(){
		final ComponentEvent event = new ComponentEvent(this, ComponentEvent.COMPONENT_HIDDEN);
		
		final ComponentListener[] listeners = getComponentListeners();
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].componentHidden(event);
		}
	}
	
	public synchronized void addFocusListener(final FocusListener l) {
		if (l == null) {
			return;
		}
		list.add(FocusListener.class, l);
	}

	public synchronized void removeFocusListener(final FocusListener l) {
		if (l == null) {
			return;
		}
		list.remove(FocusListener.class, l);
	}

	public synchronized FocusListener[] getFocusListeners() {
		return getListeners(FocusListener.class);
	}

	public void addHierarchyListener(final HierarchyListener l) {
		if (l == null) {
			return;
		}
		list.add(HierarchyListener.class, l);
	}

	public void removeHierarchyListener(final HierarchyListener l) {
		if (l == null) {
			return;
		}
		list.remove(HierarchyListener.class, l);
	}

	public synchronized HierarchyListener[] getHierarchyListeners() {
		return (getListeners(HierarchyListener.class));
	}

	public void addHierarchyBoundsListener(final HierarchyBoundsListener l) {
		if (l == null) {
			return;
		}
		list.add(HierarchyBoundsListener.class, l);
	}

	public void removeHierarchyBoundsListener(final HierarchyBoundsListener l) {
		if (l == null) {
			return;
		}
		list.remove(HierarchyBoundsListener.class, l);
	}

	public synchronized HierarchyBoundsListener[] getHierarchyBoundsListeners() {
		return getListeners(HierarchyBoundsListener.class);
	}

	protected void processMouseEvent(final MouseEvent e) {
		try{
        final MouseListener[] listener = getMouseListeners();
        if(listener == null || listener.length == 0){
        	return;
        }
        
        final int id = e.getID();
        switch(id) {
          case MouseEvent.MOUSE_PRESSED:
        	  for (int i = 0; i < listener.length; i++) {
				listener[i].mousePressed(e);
        	  }
              break;
          case MouseEvent.MOUSE_RELEASED:
        	  for (int i = 0; i < listener.length; i++) {
  				listener[i].mouseReleased(e);
          	  }
              break;
          case MouseEvent.MOUSE_CLICKED:
        	  for (int i = 0; i < listener.length; i++) {
    				listener[i].mouseClicked(e);
              }
              break;
          case MouseEvent.MOUSE_EXITED:
        	  for (int i = 0; i < listener.length; i++) {
    				listener[i].mouseExited(e);
        	  }
              break;
          case MouseEvent.MOUSE_ENTERED:
              for (int i = 0; i < listener.length; i++) {
    				listener[i].mouseEntered(e);
              }
              break;
        }
		}catch (final Throwable ex) {
			ex.printStackTrace();
		}
    }
	
	protected void processKeyEvent(final KeyEvent e) {
		try{
        final KeyListener[] listener = getKeyListeners();
        if (listener != null && listener.length > 0) {
            final int id = e.getID();
            switch(id) {
              case KeyEvent.KEY_TYPED:
            	  for (int i = 0; i < listener.length; i++) {
            		  listener[i].keyTyped(e);
            	  }
                  break;
              case KeyEvent.KEY_PRESSED:
            	  for (int i = 0; i < listener.length; i++) {
            		  listener[i].keyPressed(e);
            	  }
                  break;
              case KeyEvent.KEY_RELEASED:
            	  for (int i = 0; i < listener.length; i++) {
            		  listener[i].keyReleased(e);
            	  }
                  break;
            }
        }
		}catch (final Throwable ex) {
			ex.printStackTrace();
		}
    }
	
	public boolean handleEvent(final Event evt) {
        return false;
    }
	
	private static Cursor defaultCursor = new Cursor(Cursor.HAND_CURSOR);

	public Cursor getCursor() {
		return defaultCursor;
	}

	public void requestFocus() {
		if(isValid()){
			if(getFocusablePeerViewAdAPI() != null){
				ActivityManager.getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getFocusablePeerViewAdAPI().requestFocus();
					}
				});
			}
		}
	}
	
	public View getFocusablePeerViewAdAPI(){
		return peer;
	}

	protected boolean requestFocus(final boolean temporary) {
		requestFocus();
		return false;
	}

	public boolean requestFocusInWindow() {
		requestFocus();
		return false;
	}

	protected boolean requestFocusInWindow(final boolean temporary) {
		return requestFocusInWindow();
	}

	public void setMaximumSize(final Dimension maximumSize) {
		if(this instanceof Dialog || this instanceof Frame){
			L.V = L.O ? false : LogManager.log("skip setMaximumSize for Dialog/Frame.");
		}else{
			this.maxSize = maximumSize;
			isSetMaxSize = (maximumSize != null);
//			setMaxSizeAdAPI();
		}
	}

	public boolean isMaximumSizeSet() {
		return isSetMaxSize;
	}

	public Dimension getMaximumSize() {
		if (isMaximumSizeSet()) {
			return new Dimension(maxSize);
		}
		return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
	}

	public float getAlignmentX() {
		return CENTER_ALIGNMENT;
	}

	public float getAlignmentY() {
		return CENTER_ALIGNMENT;
	}

	public void repaint() {
//		repaint(0, 0, 0, width, height);
		if(isValid()){
			ActivityManager.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					getPeerAdAPI().invalidate();//支持java.awt.Window
				}
			});
		}
	}

	public void repaint(final long tm) {
//		repaint(tm, 0, 0, width, height);
		repaint();
	}

	public Component getComponentAt(final int x, final int y) {
		return locate(x, y);
	}

	public Component getComponentAt(final Point p) {
		return getComponentAt(p.x, p.y);
	}

	//replaced by dispatchEvent(AWTEvent e)
	public void deliverEvent(final Event e) {
		throw new Error(hc.android.AndroidClassUtil.UN_IMPLEMENT_METHOD);
    }
	
	public final void dispatchEvent(final AWTEvent e) {
		dispatchEventImpl(e);
	}

	void dispatchEventImpl(final AWTEvent e) {
		switch (e.id) {
        case MouseEvent.MOUSE_PRESSED:
        case MouseEvent.MOUSE_RELEASED:
        case MouseEvent.MOUSE_CLICKED:
        case MouseEvent.MOUSE_EXITED:
        case MouseEvent.MOUSE_ENTERED:
            processMouseEvent((MouseEvent)e);
            return;
        case KeyEvent.KEY_PRESSED:
        case KeyEvent.KEY_TYPED:
        case KeyEvent.KEY_RELEASED:
        	processKeyEvent((KeyEvent)e);
            return;
        case FocusEvent.FOCUS_GAINED:
        	requestFocus();
        	return;
      }
	}
	
	public <T extends EventListener> T[] getListeners(final Class<T> listenerType) {
		return list.getListeners(listenerType);
	}

	public void setCursor(final Cursor cursor) {
	}

	public void repaint(final int x, final int y, final int width, final int height) {
		repaint(0, x, y, width, height);
	}

	public void repaint(final long tm, final int x, final int y, final int width, final int height) {
		if(isValid()){
			ActivityManager.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					getPeerAdAPI().invalidate(x, y, x + width, y + height);//支持java.awt.Window
				}
			});
		}
	}

	public void print(final Graphics g) {
	}

	protected void firePropertyChange(final String name, final Object oldValue, final Object newValue) {
		final PropertyChangeListener[] pcl = getListeners(PropertyChangeListener.class);
		if(pcl.length > 0){
			final PropertyChangeEvent pce = new PropertyChangeEvent(this, name, oldValue, newValue);
			for (int i = 0; i < pcl.length; i++) {
				pcl[i].propertyChange(pce);
			}
		}
	}

	/**
	 * Prints this and all of its subcomponents.
	 * @param g
	 */
	public void printAll(final Graphics g) {
	}

	public void doLayout() {
		layout();
	}

	public void layout() {
		if(isValid()){
			peer.requestLayout();
		}
	}

	public void validate() {
		valid = true;
		if(peer != null){
			peer.requestLayout();
		}
	}

	public void invalidate() {
		valid = false;
		
        if (isPreferredSizeSet() == false) {
            preSize = null;
        }
        if (isMinimumSizeSet() == false) {
            minSize = null;
        }
        if (isMaximumSizeSet() == false) {
            maxSize = null;
        }
        
        if (parent != null) {
            parent.invalidateIfValid();
        }
	}
	
    final void invalidateIfValid() {
        if (isValid()) {
            invalidate();
        }
    }

    /**
     * Revalidates the component hierarchy up to the nearest validate root.
     * <p>
     * This method first invalidates the component hierarchy starting from this
     * component up to the nearest validate root. Afterwards, the component
     * hierarchy is validated starting from the nearest validate root.
     * <p>
     * This is a convenience method supposed to help application developers
     * avoid looking for validate roots manually. Basically, it's equivalent to
     * first calling the {@link #invalidate()} method on this component, and
     * then calling the {@link #validate()} method on the nearest validate
     * root.
     *
     * @see Container#isValidateRoot
     * @since 1.7
     */
	public void revalidate() {
		synchronized (getTreeLock()) {
            invalidate();

            Container root = getContainer();
            if (root == null) {
                // There's no parents. Just validate itself.
                validate();
            } else {
                while (!root.isValidateRoot()) {
                    if (root.getContainer() == null) {
                        // If there's no validate roots, we'll validate the
                        // topmost container
                        break;
                    }

                    root = root.getContainer();
                }

                root.validate();
            }
        }
	}

	public FontMetrics getFontMetrics(final Font font) {
		final FontMetrics fm = new FontMetrics(font) {
		};
		return fm;
	}


	public enum BaselineResizeBehavior {
		CONSTANT_ASCENT,
		CONSTANT_DESCENT,
		CENTER_OFFSET,
		OTHER
	}
}