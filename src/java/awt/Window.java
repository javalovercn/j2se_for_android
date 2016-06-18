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

import hc.App;
import hc.android.ActivityManager;
import hc.android.AndroidClassUtil;
import hc.android.DebugLogger;
import hc.android.AndroidUIUtil;
import hc.android.ScreenAdapter;
import hc.android.WindowManager;
import hc.core.ConfigManager;
import hc.core.L;
import hc.core.util.LogManager;

import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferStrategy;
import java.awt.peer.ComponentPeer;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.plaf.synth.Region;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

/**
 * A <code>Window</code> object is a top-level window with no borders and no
 * menubar.
 * The default layout for a window is <code>BorderLayout</code>.
 * <p>
 * A window must have either a frame, dialog, or another window defined as its
 * owner when it's constructed.
 * <p>
 * In a multi-screen environment, you can create a <code>Window</code>
 * on a different screen device by constructing the <code>Window</code>
 * with {@link #Window(Window, GraphicsConfiguration)}.  The
 * <code>GraphicsConfiguration</code> object is one of the
 * <code>GraphicsConfiguration</code> objects of the target screen device.
 * <p>
 * In a virtual device multi-screen environment in which the desktop
 * area could span multiple physical screen devices, the bounds of all
 * configurations are relative to the virtual device coordinate system.
 * The origin of the virtual-coordinate system is at the upper left-hand
 * corner of the primary physical screen.  Depending on the location of
 * the primary screen in the virtual device, negative coordinates are
 * possible, as shown in the following figure.
 * <p>
 * <img src="doc-files/MultiScreen.gif"
 * alt="Diagram shows virtual device containing 4 physical screens. Primary physical screen shows coords (0,0), other screen shows (-80,-100)."
 * ALIGN=center HSPACE=10 VSPACE=7>
 * <p>
 * In such an environment, when calling <code>setLocation</code>,
 * you must pass a virtual coordinate to this method.  Similarly,
 * calling <code>getLocationOnScreen</code> on a <code>Window</code> returns
 * virtual device coordinates.  Call the <code>getBounds</code> method
 * of a <code>GraphicsConfiguration</code> to find its origin in the virtual
 * coordinate system.
 * <p>
 * The following code sets the location of a <code>Window</code>
 * at (10, 10) relative to the origin of the physical screen
 * of the corresponding <code>GraphicsConfiguration</code>.  If the
 * bounds of the <code>GraphicsConfiguration</code> is not taken
 * into account, the <code>Window</code> location would be set
 * at (10, 10) relative to the virtual-coordinate system and would appear
 * on the primary physical screen, which might be different from the
 * physical screen of the specified <code>GraphicsConfiguration</code>.
 *
 * <pre>
 *      Window w = new Window(Window owner, GraphicsConfiguration gc);
 *      Rectangle bounds = gc.getBounds();
 *      w.setLocation(10 + bounds.x, 10 + bounds.y);
 * </pre>
 *
 * <p>
 * Note: the location and size of top-level windows (including
 * <code>Window</code>s, <code>Frame</code>s, and <code>Dialog</code>s)
 * are under the control of the desktop's window management system.
 * Calls to <code>setLocation</code>, <code>setSize</code>, and
 * <code>setBounds</code> are requests (not directives) which are
 * forwarded to the window management system.  Every effort will be
 * made to honor such requests.  However, in some cases the window
 * management system may ignore such requests, or modify the requested
 * geometry in order to place and size the <code>Window</code> in a way
 * that more closely matches the desktop settings.
 * <p>
 * Due to the asynchronous nature of native event handling, the results
 * returned by <code>getBounds</code>, <code>getLocation</code>,
 * <code>getLocationOnScreen</code>, and <code>getSize</code> might not
 * reflect the actual geometry of the Window on screen until the last
 * request has been processed.  During the processing of subsequent
 * requests these values might change accordingly while the window
 * management system fulfills the requests.
 * <p>
 * An application may set the size and location of an invisible
 * {@code Window} arbitrarily, but the window management system may
 * subsequently change its size and/or location when the
 * {@code Window} is made visible. One or more {@code ComponentEvent}s
 * will be generated to indicate the new geometry.
 * <p>
 * Windows are capable of generating the following WindowEvents:
 * WindowOpened, WindowClosed, WindowGainedFocus, WindowLostFocus.
 *
 * @author      Sami Shaio
 * @author      Arthur van Hoff
 * @see WindowEvent
 * @see #addWindowListener
 * @see java.awt.BorderLayout
 * @since       JDK1.0
 */
public class Window extends Container implements Accessible {
	public static final int ANDROID_FULL_SCREEN_AD_API = LayoutParams.FILL_PARENT;
	public static final int UN_LOC_AD_API = -1;
	protected JRootPane rootPane;
	boolean isPack = false;
	boolean isDisposed = false;
	
	String title = "Untitled";
	transient java.util.List<Image> icons;
	Window owner;
	static final int OPENED = 0x01;

	public static enum Type {
		NORMAL, UTILITY, POPUP
	}

	private View windowView;
	
	public View getWindowViewAdAPI(){
//		System.out.println("--------------------printViewStructure : --------------------");
//		UIUtil.printViewStructure(windowView, 0);
		
		if(rootPane != null){
			Container contentPane = rootPane.getContentPane();
			minPreSize(contentPane);
		}
		return windowView;
	}
	
	private final void minPreSize(final Component p){
		p.setMinSizeForServerUIAdAPI();
		p.setPreSizeForServerUIAdAPI();
		
		if(p instanceof Container){
			Container c = (Container)p;
			final int size = c.getComponentCount();
			for (int i = 0; i < size; i++) {
				minPreSize(c.getComponent(i));
			}
		}
	}
	
	public boolean isFireComponentShow = false;
	
	@Override
	public void fireComponentShowAdAPI(){
		isFireComponentShow = true;
		super.fireComponentShowAdAPI();
		
		if(rootPane != null){
			Container contentPane = rootPane.getContentPane();
			contentPane.fireComponentShowAdAPI();
		}
	}
	
	@Override
	public void fireComponentHiddenAdAPI(){
		super.fireComponentHiddenAdAPI();
		
		if(rootPane != null){
			Container contentPane = rootPane.getContentPane();
			contentPane.fireComponentHiddenAdAPI();
		}
	}
	
	public void setWindowViewAdAPI(View view){
		windowView = view;
	}
	
	Window() {
//		setLayout(new BorderLayout());
		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
				Container c = Window.this;
				doRequireFirstFocus(c);
			}
			private boolean doRequireFirstFocus(final Component c){
				if(c.isRequireFirstFocus){
					c.getFocusablePeerViewAdAPI().setFocusableInTouchMode(true);
					c.requestFocus();
					c.isRequireFirstFocus = false;
					
					AndroidUIUtil.runDelay(new Runnable() {
						@Override
						public void run() {
							try{
								Thread.sleep(AndroidUIUtil.MS_FOCUSONTOUCH);
							}catch (Exception e) {
							}
							c.getFocusablePeerViewAdAPI().setFocusableInTouchMode(false);
						}
					});
					return true;
				}
				if(c instanceof Container){
					Container container = (Container)c;
					int size = container.getComponentCount();
					for (int i = 0; i < size; i++) {
						if(doRequireFirstFocus(container.getComponent(i))){
							return true;
						}
					}
				}
				return false;
			}
			@Override
			public void windowIconified(WindowEvent e) {
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			@Override
			public void windowClosing(WindowEvent e) {
			}
			@Override
			public void windowClosed(WindowEvent e) {
			}
			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
		
	}

	public Window(Frame owner) {
		//TODO
		this();
	}

	public Window(Window owner) {
		this();
		this.owner = owner;
	}

	Window(GraphicsConfiguration gc) {
		//TODO
		this();
	}

	public Window(Window owner, GraphicsConfiguration gc) {
		this(gc);
		this.owner = owner;
	}

	public void setIconImage(Image image) {
        ArrayList<Image> imageList = new ArrayList<Image>();
        if (image != null) {
            imageList.add(image);
        }
        setIconImages(imageList);
	}

    public synchronized void setIconImages(java.util.List<? extends Image> icons) {
    	AndroidClassUtil.callEmptyMethod();
//        peer.updateIconImages();
    }
    
    public boolean isPackedAdAPI(){
    	return isPack;
    }
    
	public void pack() {
		isPack = true;
		
//        Dimension newSize = getPreferredSize();
//        if (getPeerAdAPI() != null) {
//        	setSize(newSize.width, newSize.height + getRootPaneTtitleBarAndMenuBarHeightAdAPI());
//        }
        Dimension newSize = getPreferredSize();
        if (getPeerAdAPI() != null) {
            setClientSize(newSize.width, newSize.height);
        }
        
        validate();
	}
	
	public JRootPane getRootPaneAdAPI(){
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getRootPane();
			}else if(isJDialog){
				return ((JDialog)this).getRootPane();
			}
		}
		return null;
	}
	
	private int getRootPaneTtitleBarAndMenuBarHeightAdAPI(){
		JRootPane rootPane = getRootPaneAdAPI();
		if(rootPane != null){
			return rootPane.getTitleBarHeightAdAPI() + rootPane.getMenuBarHeightAdAPI();
		}
		DebugLogger.log("unknow getRootPaneTtitleBarAndMenuBarHeightAdAPI!!!");
		return 0;
	}

    void setClientSize(int w, int h) {
    	getRootPaneAdAPI().getContentPane().setSize(w, h);
//        synchronized (getTreeLock()) {
//            setBoundsOp(ComponentPeer.SET_CLIENT_SIZE);
//            setBounds(getX(), getY(), w, h);
//        }
    }
    
	public void setMinimumSize(Dimension minimumSize) {
		setMinimumSize(minimumSize);
	}

//	private int windowFixWidth = ANDROID_FULL_SCREEN_AD_API, windowFixHeight = ANDROID_FULL_SCREEN_AD_API;
	
	public void setSize(Dimension d) {
		setSize(d.width, d.height);
	}

	/**
	 * to get size, {@link #getWindowFixWidthAdAPI()}, default is FILL_PARENT
	 */
	public void setSize(int width, int height) {
		Boolean enable = (Boolean)ConfigManager.get(ConfigManager.UI_ENABLE_SCREEN_ADAPTER, Boolean.TRUE);
		if(enable){
			final ScreenAdapter screenAdapter = getScreenAdapterAdAPI();
			
			if(screenAdapter == null || screenAdapter.type != ScreenAdapter.TYPE_SERVER){
			}else{
				final int oldW = width, oldH = height;
				width = screenAdapter.getPreAdapterWidth(width);
				height = screenAdapter.getPreAdapterHeight(height);
				if(App.isSimu()){
					L.V = L.O ? false : LogManager.log("auto adapter screen size from [" + oldW + ", " + oldH + "] to [" + width + ", " + height + "].");
				}
			}
		}
		
		getRootPaneAdAPI().getContentPane().setSize(width, height);
//		super.setSize(width, height);
	}
	
	/**
	 * 
	 * @return -1:FILL_PARENT, fullWidth
	 */
	public int getWindowFixWidthAdAPI(){
		return getRootPaneAdAPI().getContentPane().getWidth();
//		return getWidth();//windowFixWidth;
	}
	
	/**
	 * 
	 * @return -1:FILL_PARENT, fullHeight
	 */
	public int getWindowFixHeightAdAPI(){
		return getRootPaneAdAPI().getContentPane().getHeight();
//		return getHeight();//windowFixHeight;
	}

	private int locX = UN_LOC_AD_API, locY = UN_LOC_AD_API;
	
	/**
	 * 
	 * @return -1:Center or fullScreen
	 */
	public int getLocationXAdAPI(){
		return locX;
	}
	
	/**
	 * 
	 * @return -1:Center or fullScreen
	 */
	public int getLocationYAdAPI(){
		return locY;
	}
	
	public void setLocation(int x, int y) {
		locX = x;
		locY = y;
	}

	public void setLocation(Point p) {
		setLocation(p.x, p.y);
	}

	boolean isVisible = false;
	
	public void setVisible(boolean b) {
		if(isVisible == b || isDisposed){
			return;
		}
		
		isVisible = b;
        if(b){
        	fireComponentShowAdAPI();//注意： 不能加super.
        	
        	if(isValidated == false){
	        	AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
					@Override
					public void run() {
		                validate();						
		        	}
				});
			}
    		WindowManager.showWindow(this);
        }else{
        	fireComponentHiddenAdAPI();//注意： 不能加super.
        	WindowManager.closeWindow(this);
        }
	}

	private JRootPane getDisplayPaneAdAPI(){
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getRootPane();
			}else if(isJDialog){
				return ((JDialog)this).getRootPane();
			}
		}
		DebugLogger.log("unknow container type!!!");
		return null;
	}
	
	public View getPeerAdAPI() {
//		boolean isJFrame = false, isJDialog = false;
//		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
//			if(isJFrame){
//				return ((JFrame)this).getContentPane().getPeerAdAPI();
//			}else if(isJDialog){
//				return ((JDialog)this).getContentPane().getPeerAdAPI();
//			}
//		}
//		DebugLogger.log("unknow container type!!!");
		return getDisplayPaneAdAPI().getPeerAdAPI();
	}
	
	/**
	 * replaced by setVisible(boolean) from JDK version 1.5.
	 */
	public void show() {
		setVisible(true);
	}

	boolean isValidated = false;
	
	public void validate() {
		isValidated = true;
		
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				((JFrame)this).getContentPane().validate();
//				getDisplayPaneAdAPI().validate();
				return;
			}else if(isJDialog){
				((JDialog)this).getContentPane().validate();
//				getDisplayPaneAdAPI().validate();
				return;
			}
		}
		DebugLogger.log("unknow container type!!!");
	}
	
	public int getComponentCount() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().getComponentCount();
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().getComponentCount();
			}
		}
		DebugLogger.log("unknow container type!!!");
		return 0;
	}
	
	public int countComponents() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().countComponents();
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().countComponents();
			}
		}
		DebugLogger.log("unknow container type!!!");
		return 0;
	}
	
	public void invalidate() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				((JFrame)this).getContentPane().invalidate();
				return;
			}else if(isJDialog){
				((JDialog)this).getContentPane().invalidate();
				return;
			}
		}
		DebugLogger.log("unknow container type!!!");
	}
	
	public Component getComponent(int n) {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().getComponent(n);
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().getComponent(n);
			}
		}
		DebugLogger.log("unknow container type!!!");
		return null;
	}
	
	public Component[] getComponents() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().getComponents();
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().getComponents();
			}
		}
		DebugLogger.log("unknow container type!!!");
		return EMPTY_ARRAY;
	}
	
	public Insets getInsets() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().getInsets();
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().getInsets();
			}
		}
		DebugLogger.log("unknow container type!!!");
		return null;
	}
	
	public Insets insets() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().insets();
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().insets();
			}
		}
		DebugLogger.log("unknow container type!!!");
		return null;
	}
	
	protected synchronized void addImpl(Component comp, Object constraints, int index) {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				((JFrame)this).getContentPane().addImpl(comp, constraints, index);
				return;
			}else if(isJDialog){
				((JDialog)this).getContentPane().addImpl(comp, constraints, index);
				return;
			}
		}
		DebugLogger.log("unknow container type!!!");
	}
	
	public void remove(int index) {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				((JFrame)this).getContentPane().remove(index);
				return;
			}else if(isJDialog){
				((JDialog)this).getContentPane().remove(index);
				return;
			}
		}
		DebugLogger.log("unknow container type!!!");
	}
	
	public synchronized void remove(Component comp) {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				((JFrame)this).getContentPane().remove(comp);
				return;
			}else if(isJDialog){
				((JDialog)this).getContentPane().remove(comp);
				return;
			}
		}
		DebugLogger.log("unknow container type!!!");
	}
	
	public synchronized void removeAll() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				((JFrame)this).getContentPane().removeAll();
				return;
			}else if(isJDialog){
				((JDialog)this).getContentPane().removeAll();
				return;
			}
		}
		DebugLogger.log("unknow container type!!!");
	}
	
	public LayoutManager getLayout() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().getLayout();
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().getLayout();
			}
		}
		DebugLogger.log("unknow container type!!!");
		return null;
	}
	
	public void setLayout(LayoutManager mgr) {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				((JFrame)this).getContentPane().setLayout(mgr);
				return;
			}else if(isJDialog){
				((JDialog)this).getContentPane().setLayout(mgr);
				return;
			}
		}
		DebugLogger.log("unknow container type!!!");
	}
	
//	final JPanel windowPanel = new JPanel(new BorderLayout());
	
	protected void validateTree() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				((JFrame)this).getContentPane().validateTree();
				return;
			}else if(isJDialog){
				((JDialog)this).getContentPane().validateTree();
				return;
			}
		}
		DebugLogger.log("unknow container type!!!");
	}
	
	public void doLayout() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				getDisplayPaneAdAPI().doLayout();
				return;
			}else if(isJDialog){
				getDisplayPaneAdAPI().doLayout();
				return;
			}
		}
		DebugLogger.log("unknow container type!!!");
	}
	
	public void layout() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				getDisplayPaneAdAPI().layout();
				return;
			}else if(isJDialog){
				getDisplayPaneAdAPI().layout();
				return;
			}
		}
		DebugLogger.log("unknow container type!!!");
	}
	
	public boolean isValidateRoot() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return getDisplayPaneAdAPI().isValidateRoot();
			}else if(isJDialog){
				return getDisplayPaneAdAPI().isValidateRoot();
			}
		}
		DebugLogger.log("unknow container type!!!");
		return true;
	}
	
	public void revalidate() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				getDisplayPaneAdAPI().revalidate();
				return;
			}else if(isJDialog){
				getDisplayPaneAdAPI().revalidate();
				return;
			}
		}
		DebugLogger.log("unknow container type!!!");
	}
	
	public Dimension getPreferredSize() {
		return preferredSize();
//		if(this instanceof JFrame || this instanceof JDialog){
//			Dimension dimension = new Dimension();
//			UIUtil.getViewWidthAndHeight(getPeerAdAPI(), dimension);
//			return dimension;
//		}
//		DebugLogger.log("unknow container type!!!");
//		return super.getPreferredSize();
	}
	
	final boolean containsFocus() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().containsFocus();
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().containsFocus();
			}
		}
		DebugLogger.log("unknow container type!!!");
		return false;
	}
	
	public Dimension preferredSize() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().preferredSize();
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().preferredSize();
			}
		}
		DebugLogger.log("unknow container type!!!");
		return super.preferredSize();
	}
	
	public Component searchComponentByViewAdAPI(View view){
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().searchComponentByViewAdAPI(view);
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().searchComponentByViewAdAPI(view);
			}
		}
		DebugLogger.log("unknow container type!!!");
		return null;
	}
	
	public Dimension getMinimumSize() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().getMinimumSize();
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().getMinimumSize();
			}
		}
		DebugLogger.log("unknow container type!!!");
		return super.getMinimumSize();
	}
	
	public Dimension minimumSize() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().minimumSize();
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().minimumSize();
			}
		}
		DebugLogger.log("unknow container type!!!");
		return super.minimumSize();
	}
	
	public Dimension getMaximumSize() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().getMaximumSize();
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().getMaximumSize();
			}
		}
		DebugLogger.log("unknow container type!!!");
		return super.getMaximumSize();
	}
	
	public float getAlignmentX() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().getAlignmentX();
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().getAlignmentX();
			}
		}
		DebugLogger.log("unknow container type!!!");
		return 0;
	}
	
	public float getAlignmentY() {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().getAlignmentY();
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().getAlignmentY();
			}
		}
		DebugLogger.log("unknow container type!!!");
		return 0;
	}
	
	public void update(Graphics g) {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				((JFrame)this).getContentPane().update(g);
				return;
			}else if(isJDialog){
				((JDialog)this).getContentPane().update(g);
				return;
			}
		}
		DebugLogger.log("unknow container type!!!");
	}
	
	public void print(Graphics g) {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				((JFrame)this).getContentPane().print(g);
				return;
			}else if(isJDialog){
				((JDialog)this).getContentPane().print(g);
				return;
			}
		}
		DebugLogger.log("unknow container type!!!");
	}
	
	public Component getComponentAt(int x, int y) {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().getComponentAt(x, y);
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().getComponentAt(x, y);
			}
		}
		DebugLogger.log("unknow container type!!!");
		return super.getComponentAt(x, y);
	}
	
	public Component locate(int x, int y) {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().locate(x, y);
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().locate(x, y);
			}
		}
		DebugLogger.log("unknow container type!!!");
		return super.locate(x, y);
	}
	
	public Component getComponentAt(Point p) {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().getComponentAt(p);
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().getComponentAt(p);
			}
		}
		DebugLogger.log("unknow container type!!!");
		return super.getComponentAt(p);
	}
	
	public Component findComponentAt(int x, int y) {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().findComponentAt(x, y);
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().findComponentAt(x, y);
			}
		}
		DebugLogger.log("unknow container type!!!");
		return super.findComponentAt(x, y);
	}
	
	final Component findComponentAt(int x, int y, boolean ignoreEnabled) {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().findComponentAt(x, y, ignoreEnabled);
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().findComponentAt(x, y, ignoreEnabled);
			}
		}
		DebugLogger.log("unknow container type!!!");
		return super.findComponentAt(x, y, ignoreEnabled);
	}
	
	Component findComponentAtImpl(int x, int y, boolean ignoreEnabled) {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().findComponentAtImpl(x, y, ignoreEnabled);
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().findComponentAtImpl(x, y, ignoreEnabled);
			}
		}
		DebugLogger.log("unknow container type!!!");
		return super.findComponentAtImpl(x, y, ignoreEnabled);
	}
	
	public Component findComponentAt(Point p) {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				return ((JFrame)this).getContentPane().findComponentAt(p);
			}else if(isJDialog){
				return ((JDialog)this).getContentPane().findComponentAt(p);
			}
		}
		DebugLogger.log("unknow container type!!!");
		return super.findComponentAt(p);
	}
	
	public void applyComponentOrientation(ComponentOrientation o) {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				((JFrame)this).getContentPane().applyComponentOrientation(o);
				return;
			}else if(isJDialog){
				((JDialog)this).getContentPane().applyComponentOrientation(o);
				return;
			}
		}
		DebugLogger.log("unknow container type!!!");
	}
	
	public void remove(MenuComponent comp) {
		boolean isJFrame = false, isJDialog = false;
		if((isJFrame = (this instanceof JFrame)) || (isJDialog = (this instanceof JDialog))){
			if(isJFrame){
				((JFrame)this).getContentPane().remove(comp);
				return;
			}else if(isJDialog){
				((JDialog)this).getContentPane().remove(comp);
				return;
			}
		}
		DebugLogger.log("unknow container type!!!");
	}
	
	synchronized void postWindowEvent(int id) {
	}

	public void hide() {
		setVisible(false);
	}

	public void dispose() {
		synchronized(this){
			if(isDisposed == false){
				isDisposed = true;
				isVisible = false;
			}else{
				return;
			}
		}
		if(windowView != null){//MCanvas时，该值为null
			WindowManager.closeWindow(this);
		}
	}

	public void toFront() {
		if(isDisposed){
			return;
		}
		WindowManager.toFront(this);
	}

	public void toBack() {
		if(isDisposed){
			return;
		}
		WindowManager.toBack(this);
	}

	public Toolkit getToolkit() {
		return Toolkit.getDefaultToolkit();
	}

	public Locale getLocale() {
		return Locale.getDefault();
	}

	public Window getOwner() {
		return owner;
	}

	public static Window[] getWindows() {
		return WindowManager.getWindows();
	}

	public synchronized void addWindowListener(WindowListener l) {
		if (l == null) {
			return;
		}
		list.add(WindowListener.class, l);
	}

	public synchronized void addWindowStateListener(WindowStateListener l) {
		if (l == null) {
			return;
		}
		list.add(WindowStateListener.class, l);
	}

	public synchronized void addWindowFocusListener(WindowFocusListener l) {
		if (l == null) {
			return;
		}
		list.add(WindowFocusListener.class, l);
	}

	public synchronized void removeWindowListener(WindowListener l) {
		if (l == null) {
			return;
		}
		list.remove(WindowListener.class, l);
	}

	public synchronized void removeWindowStateListener(WindowStateListener l) {
		if (l == null) {
			return;
		}
		list.remove(WindowStateListener.class, l);
	}

	public synchronized void removeWindowFocusListener(WindowFocusListener l) {
		if (l == null) {
			return;
		}
		list.remove(WindowFocusListener.class, l);
	}

	public synchronized WindowListener[] getWindowListeners() {
		return getListeners(WindowListener.class);
	}

	public synchronized WindowFocusListener[] getWindowFocusListeners() {
		return getListeners(WindowFocusListener.class);
	}

	public synchronized WindowStateListener[] getWindowStateListeners() {
		return getListeners(WindowStateListener.class);
	}

	boolean eventEnabled(AWTEvent e) {
		return false;
	}
	
	public void processEventAdAPI(AWTEvent e){
		processEvent(e);
	}

	protected void processEvent(AWTEvent e) {
		if (e instanceof WindowEvent) {
            switch (e.getID()) {
                case WindowEvent.WINDOW_OPENED:
                case WindowEvent.WINDOW_CLOSING:
                case WindowEvent.WINDOW_CLOSED:
                case WindowEvent.WINDOW_ICONIFIED:
                case WindowEvent.WINDOW_DEICONIFIED:
                case WindowEvent.WINDOW_ACTIVATED:
                case WindowEvent.WINDOW_DEACTIVATED:
                    processWindowEvent((WindowEvent)e);
                    break;
                case WindowEvent.WINDOW_GAINED_FOCUS:
                case WindowEvent.WINDOW_LOST_FOCUS:
                    processWindowFocusEvent((WindowEvent)e);
                    break;
                case WindowEvent.WINDOW_STATE_CHANGED:
                    processWindowStateEvent((WindowEvent)e);
                default:
                    break;
            }
            return;
        }
//        super.processEvent(e);
	}

	protected void processWindowEvent(WindowEvent e) {
		try{
		WindowListener[] wl = getWindowListeners();
		if(wl == null){
			return;
		}
        switch(e.getID()) {
            case WindowEvent.WINDOW_OPENED:
				DebugLogger.log("fire WindowListener.windowOpened.");
				for (int i = 0; i < wl.length; i++) {
					wl[i].windowOpened(e);
				}
                break;
            case WindowEvent.WINDOW_CLOSING:
            	DebugLogger.log("fire WindowListener.windowClosing.");
				for (int i = 0; i < wl.length; i++) {
					wl[i].windowClosing(e);
				}
                break;
            case WindowEvent.WINDOW_CLOSED:
            	DebugLogger.log("fire WindowListener.windowClosed.");
				for (int i = 0; i < wl.length; i++) {
					wl[i].windowClosed(e);
				}
                break;
            case WindowEvent.WINDOW_ICONIFIED:
            	DebugLogger.log("fire WindowListener.windowIconified.");
				for (int i = 0; i < wl.length; i++) {
					wl[i].windowIconified(e);
				}
                break;
            case WindowEvent.WINDOW_DEICONIFIED:
            	DebugLogger.log("fire WindowListener.windowDeiconified.");
				for (int i = 0; i < wl.length; i++) {
					wl[i].windowDeiconified(e);
				}
                break;
            case WindowEvent.WINDOW_ACTIVATED:
            	DebugLogger.log("fire WindowListener.windowActivated.");
				for (int i = 0; i < wl.length; i++) {
					wl[i].windowActivated(e);
				}
                break;
            case WindowEvent.WINDOW_DEACTIVATED:
            	DebugLogger.log("fire WindowListener.windowDeactivated.");
				for (int i = 0; i < wl.length; i++) {
					wl[i].windowDeactivated(e);
				}
                break;
            default:
                break;
        }
		}catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	protected void processWindowFocusEvent(WindowEvent e) {
		try{
		WindowFocusListener[] wl = getWindowFocusListeners();
		if(wl == null){
			return;
		}
        switch (e.getID()) {
            case WindowEvent.WINDOW_GAINED_FOCUS:
            	DebugLogger.log("fire WindowListener.windowGainedFocus.");
				for (int i = 0; i < wl.length; i++) {
					wl[i].windowGainedFocus(e);
				}
                break;
            case WindowEvent.WINDOW_LOST_FOCUS:
            	DebugLogger.log("fire WindowListener.windowLostFocus.");
				for (int i = 0; i < wl.length; i++) {
					wl[i].windowLostFocus(e);
				}
                break;
            default:
                break;
        }
		}catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	protected void processWindowStateEvent(WindowEvent e) {
		try{
		WindowStateListener[] wl = getWindowStateListeners();
		if(wl == null){
			return;
		}
        switch (e.getID()) {
            case WindowEvent.WINDOW_STATE_CHANGED:
            	DebugLogger.log("fire WindowListener.windowStateChanged.");
				for (int i = 0; i < wl.length; i++) {
					wl[i].windowStateChanged(e);
				}
                break;
            default:
                break;
        }
		}catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	void preProcessKeyEvent(KeyEvent e) {
	}

	void postProcessKeyEvent(KeyEvent e) {
	}

	public final void setAlwaysOnTop(boolean alwaysOnTop) throws SecurityException {
	}

	public boolean isAlwaysOnTopSupported() {
		return true;
	}

	public final boolean isAlwaysOnTop() {
		return true;
	}

	public Component getFocusOwner() {
		if(isFocused()){
			View currFocus = ActivityManager.getActivity().getCurrentFocus();
			return searchComponentByViewAdAPI(currFocus);
		}else{
			return null;
		}
	}

	public Component getMostRecentFocusOwner() {
		return null;
	}

	public boolean isActive() {
		return WindowManager.isFocusWindow(this);
	}

	public boolean isFocused() {
		return WindowManager.isFocusWindow(this);
	}

	public Set<AWTKeyStroke> getFocusTraversalKeys(int id) {
		return null;
	}

	public final void setFocusCycleRoot(boolean focusCycleRoot) {
	}

	public final boolean isFocusCycleRoot() {
		return true;
	}

	public final Container getFocusCycleRootAncestor() {
		return null;
	}

	public final boolean isFocusableWindow() {
		return true;
	}

	public boolean getFocusableWindowState() {
		return true;
	}

	public void setFocusableWindowState(boolean focusableWindowState) {
	}

	public void setAutoRequestFocus(boolean autoRequestFocus) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean isAutoRequestFocus() {
		AndroidClassUtil.callEmptyMethod();
		return true;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
	}

	public boolean postEvent(Event e) {
		if (handleEvent(e)) {
            e.consume();
            return true;
        }
        return false;
	}

	public boolean isShowing() {
		return isVisible;
	}

	boolean isDisposing() {
		return false;
	}

	public void applyResourceBundle(ResourceBundle rb) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public void applyResourceBundle(String rbName) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	void addOwnedWindow(WeakReference weakWindow) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	void removeOwnedWindow(WeakReference weakWindow) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	void connectOwnedWindow(Window child) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	private Type type = Type.NORMAL;

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException,
			IOException, HeadlessException {
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

	@Override
	void setGraphicsConfiguration(GraphicsConfiguration gc) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public void setLocationRelativeTo(Component c) {
		if(c == null){
			return;
		}
		
		int dx = 0, dy = 0;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = getSize();
		Dimension compSize = c.getSize();
		Point compLocation = c.getLocationOnScreen();
		dx = compLocation.x + ((compSize.width - windowSize.width) / 2);
		dy = compLocation.y + ((compSize.height - windowSize.height) / 2);

		if (dy + windowSize.height > screenSize.height) {
			dy = screenSize.height - windowSize.height;
			if (compLocation.x + compSize.width / 2 < screenSize.width / 2) {
				dx = compLocation.x + compSize.width;
			} else {
				dx = compLocation.x - windowSize.width;
			}
		}

		if (dx < 0) {
			dx = 0;
		}
		if (dy < 0) {
			dy = 0;
		}
		if (dx + windowSize.width > screenSize.width) {
			dx = screenSize.width - windowSize.width;
		}
		if (dy + windowSize.height > screenSize.height) {
			dy = screenSize.height - windowSize.height;
		}

		setLocation(dx, dy);
	}

	void deliverMouseWheelToAncestor(MouseWheelEvent e) {
	}

	boolean dispatchMouseWheelToAncestor(MouseWheelEvent e) {
		return false;
	}

	public void createBufferStrategy(int numBuffers) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public void createBufferStrategy(int numBuffers, BufferCapabilities caps)
			throws AWTException {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public BufferStrategy getBufferStrategy() {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	Component getTemporaryLostComponent() {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	Component setTemporaryLostComponent(Component component) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	boolean canContainFocusOwner(Component focusOwnerCandidate) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public void setLocationByPlatform(boolean locationByPlatform) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public boolean isLocationByPlatform() {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
//		return false;
	}

	public void setBounds(int x, int y, int width, int height) {
		synchronized (getTreeLock()) {
//            if (getBoundsOp() == ComponentPeer.SET_LOCATION ||
//                getBoundsOp() == ComponentPeer.SET_BOUNDS)
//            {
//                locationByPlatform = false;
//            }
            super.setBounds(x, y, width, height);
        }
	}

	public void setBounds(Rectangle r) {
		setBounds(r.x, r.y, r.width, r.height);
	}

	boolean isRecursivelyVisible() {
		return isVisible();
	}

	public float getOpacity() {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public void setOpacity(float opacity) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public Shape getShape() {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public void setShape(Shape shape) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public Color getBackground() {
		return super.getBackground();
	}

	public void setBackground(Color bgColor) {
		super.setBackground(bgColor);
	}

	public boolean isOpaque() {
		Color bg = getBackground();
		return bg != null ? bg.getAlpha() == 255 : true;
	}

	public void paint(Graphics g) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	final Container getContainer() {
		//it does NOT have a container
		return null;
	}

	final void applyCompoundShape(Region shape) {
	}

	@Override
	final void applyCurrentShape() {
	}

	@Override
	final void mixOnReshaping() {
	}

	@Override
	final Point getLocationOnWindow() {
//		return new Point(0, 0);//旧代码
		return new Point(getX(), getY());
	}

	void updateZOrder() {
	}

}
