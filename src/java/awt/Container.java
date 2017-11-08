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
import hc.android.CanvasGraphics;
import hc.android.FocusManager;
import hc.android.HCCardLayout;
import hc.android.HCTabHost;
import hc.android.AndroidUIUtil;
import hc.core.L;
import hc.core.util.LogManager;
import hc.util.PropertiesManager;

import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.border.Border;

import android.graphics.Canvas;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

/**
 * A generic Abstract Window Toolkit(AWT) container object is a component
 * that can contain other AWT components.
 * <p>
 * Components added to a container are tracked in a list.  The order
 * of the list will define the components' front-to-back stacking order
 * within the container.  If no index is specified when adding a
 * component to a container, it will be added to the end of the list
 * (and hence to the bottom of the stacking order).
 * <p>
 * <b>Note</b>: For details on the focus subsystem, see
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/focus.html">
 * How to Use the Focus Subsystem</a>,
 * a section in <em>The Java Tutorial</em>, and the
 * <a href="../../java/awt/doc-files/FocusSpec.html">Focus Specification</a>
 * for more information.
 *
 * @author      Arthur van Hoff
 * @author      Sami Shaio
 * @see       #add(java.awt.Component, int)
 * @see       #getComponent(int)
 * @see       LayoutManager
 * @since     JDK1.0
 */
public class Container extends Component {
	protected static final Component[] EMPTY_ARRAY = new Component[0];

	private java.util.List<Component> components;
	protected Graphics paintGraphics;

	protected LayoutManager layout;

	static final boolean INCLUDE_SELF = true;
	static final boolean SEARCH_HEAVYWEIGHTS = true;

	public Container() {
	}

	public int getComponentCount() {
		return countComponents();
	}

	public int countComponents() {
		if(components == null){
			return 0;
		}
		
		return components.size();
	}

//	void setGraphicsConfiguration(GraphicsConfiguration gc) {
//	}

	public void invalidate() {
		
        if (layout != null && layout instanceof LayoutManager2) {
            final LayoutManager2 lm = (LayoutManager2) layout;
            lm.invalidateLayout(this);
        }
//        super.invalidate();
    }
	
	public void setVisible(final boolean b) {
		super.setVisible(b);
		
		if(layout != null){
			getContainerViewAdAPI().setVisibility(b?View.VISIBLE:View.INVISIBLE);
		}
	}
	
	private boolean focusCycleRoot = false;
	
    public void setFocusCycleRoot(final boolean focusCycleRoot) {
        boolean oldFocusCycleRoot;
        synchronized (this) {
            oldFocusCycleRoot = this.focusCycleRoot;
            this.focusCycleRoot = focusCycleRoot;
        }
        firePropertyChange("focusCycleRoot", oldFocusCycleRoot, focusCycleRoot);
    }
    
	@Override
	public void fireComponentShowAdAPI(){
		super.fireComponentShowAdAPI();
		
		//根据J2SE标准不触发subComponent.fireComponentShowAdAPI
//		int size = getComponentCount();
//		for (int i = 0; i < size; i++) {
//			getComponent(i).fireComponentShowAdAPI();
//		}
	}
	
	@Override
	public void fireComponentHiddenAdAPI(){
		super.fireComponentHiddenAdAPI();
		
		//根据J2SE标准不触发subComponent.fireComponentHiddenAdAPI
//		int size = getComponentCount();
//		for (int i = 0; i < size; i++) {
//			getComponent(i).fireComponentHiddenAdAPI();
//		}
	}
	
	final void recursiveApplyCurrentShape() {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	final void recursiveApplyCurrentShape(final int fromZorder) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	final void recursiveApplyCurrentShape(final int fromZorder, final int toZorder) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public Component getComponent(final int n) {
		if(components == null){
			return null;
		}
		return components.get(n);
	}

	public Component[] getComponents() {
		if(components == null){
			return EMPTY_ARRAY;
		}
		
		final Component[] out = new Component[getComponentCount()];
		for (int i = 0; i < out.length; i++) {
			out[i] = components.get(i);
		}
		return out;
	}
	
    /**
     * Determines the insets of this container, which indicate the size
     * of the container's border.
     * <p>
     * A <code>Frame</code> object, for example, has a top inset that
     * corresponds to the height of the frame's title bar.
     * @return    the insets of this container.
     * @see       Insets
     * @see       LayoutManager
     * @since     JDK1.1
     */
	public Insets getInsets() {
		return insets();
	}

	/**
	 * @see JComponent#getInsets()
	 * @return
	 */
	public Insets insets() {
		//set JComponent.getInsets border.getBorderInsets(this);
//		if(this instanceof JComponent){
//			final JComponent jcom = (JComponent)this;
//			final Border border = jcom.getBorder();
//			if(border != null){
//				return border.getBorderInsets(this);
//			}
//		}
		return new Insets(0, 0, 0, 0);
	}

	public Component add(final Component comp) {
		addImpl(comp, null, -1);
		return comp;
	}

	public Component add(final String name, final Component comp) {
		addImpl(comp, name, -1);
		return comp;
	}

	public Component add(final Component comp, final int index) {
		addImpl(comp, null, index);
		return comp;
	}

	public synchronized void addContainerListener(final ContainerListener l) {
        if (l == null) {
            return;
        }
        
        list.add(ContainerListener.class, l);
    }
	
	public Set<AWTKeyStroke> getFocusTraversalKeys(final int id) {
		return new HashSet<AWTKeyStroke>();
	}

	public void setComponentZOrder(final Component comp, final int index) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public int getComponentZOrder(final Component comp) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public void add(final Component comp, final Object constraints) {
		addImpl(comp, constraints, -1);
	}

	public void add(final Component comp, final Object constraints, final int index) {
		addImpl(comp, constraints, index);
	}

	protected synchronized void addImpl(final Component comp, final Object constraints, final int index) {
		if(comp == this){
			throw new Error("add self to the container");
		}
		
		if (comp.parent != null) {
			comp.parent.remove(comp);
		}

		if(components == null){
			components = new java.util.ArrayList<Component>();
		}
		
		if (index == -1 || index > components.size()) {
			components.add(comp);
		} else {
			components.add(index, comp);
		}
		comp.parent = this;
		
		{
			final ContainerEvent event = new ContainerEvent(this, ContainerEvent.COMPONENT_ADDED, comp);
			final ContainerListener[] listener = list.getListeners(ContainerListener.class);
			for (int i = 0; i < listener.length; i++) {
				listener[i].componentAdded(event);
			}
		}

		if (layout != null) {
//			View subView = comp.getPeerAdAPI();
//			if(subView != null){
//				AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(0, 0, 0, 0);
//				layoutView.addView(subView, lp);
//			}
			
			if (layout instanceof LayoutManager2) {
				((LayoutManager2) layout).addLayoutComponent(comp, constraints);
			} else if (constraints instanceof String) {
				layout.addLayoutComponent((String) constraints, comp);
			}
		}
	}

	public void remove(final int index) {
		if(components == null){
			return;
		}
		if (index >= 0) {
			final Component c = components.get(index);
			if(c != null){
				remove(c);//调用以触发layout.removeXX
			}
		}
	}

	public synchronized void remove(final Component comp) {
		if(components == null){
			return;
		}
		
		final int index = components.indexOf(comp);
		if (index >= 0) {
			components.remove(index);
			comp.parent = null;
			
			final ViewGroup layoutView = (ViewGroup)getContainerViewAdAPI();
			final View subView = comp.getPeerAdAPI();
			if(layoutView != null && subView != null){
				ActivityManager.getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						layoutView.removeView(subView);
					}
				});
			}
			
			{
				final ContainerEvent event = new ContainerEvent(this, ContainerEvent.COMPONENT_REMOVED, comp);
				final ContainerListener[] listener = list.getListeners(ContainerListener.class);
				for (int i = 0; i < listener.length; i++) {
					listener[i].componentRemoved(event);
				}
			}
		}
		
		if (layout != null) {
			layout.removeLayoutComponent(comp);
		}
	}

	public synchronized void removeAll() {
		if(components == null){
			return;
		}
		
		final int size = getComponentCount();
		for (int i = size - 1; i >= 0; i--) {
			remove(i);
		}
	}

	@Override
	public View getPeerAdAPI(){
		if(layoutView != null){
			if(isUsePaintView){
				return paintLinearLayout;
			}else{
				return layoutView;
			}
		}
		View out = super.getPeerAdAPI();
		if(out == null){
			out = buildEmptyCanvas();
			super.setPeerAdAPI(out);
		}
		return out;
	}
	
	public LayoutManager getLayout() {
		return layout;
	}

	protected boolean isUsePaintView = false;
	protected LinearLayout paintLinearLayout;
	protected ViewGroup layoutView;
	
	private final View buildEmptyCanvas() {
		final View p = new View(ActivityManager.getActivity()){
			@Override  
		    protected void onDraw(final Canvas canvas) { 
				paintGraphics = new CanvasGraphics(canvas, getScreenAdapterAdAPI());//注意不能缓存，因为可能会调整尺寸
				paint(paintGraphics);
			}
			
			protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
		        final int measureWidth = measureWidth(widthMeasureSpec);  
		        final int measureHeight = measureHeight(heightMeasureSpec);  
		        // 计算自定义的ViewGroup中所有子控件的大小  
//		        measureChildren(widthMeasureSpec, heightMeasureSpec);  
		        // 设置自定义的控件MyViewGroup的大小  
//		        System.out.println("-------onMeasure setMeasuredDimension w : " + measureWidth + ", h : " + measureHeight);
		        setMeasuredDimension(measureWidth, measureHeight); 
		    }
			
			private int measureWidth(final int pWidthMeasureSpec) {  
		        int result = MeasureSpec.getSize(pWidthMeasureSpec);
		        final int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式  
		  
		        switch (widthMode) {  
		        case MeasureSpec.AT_MOST:  
//		        	System.out.println(" width AT_MOST getSize : " + result);
		        	if(Container.this.isSetMaxSize){
						result = Math.min(result, Container.this.maxSize.width);
					}
//		        	System.out.println(" width AT_MOST : " + result);
		            break;
		        case MeasureSpec.EXACTLY:  
//		        	System.out.println(" width EXACTLY getSize : " + result);
					if(Container.this.isSetPrefSize){
						result = Container.this.preSize.width;
					}
//					System.out.println(" width EXACTLY : " + result);
		            break;  
		        }  
		        return result;  
		    }  
			
			private int measureHeight(final int pHeightMeasureSpec) {  
		        int result = MeasureSpec.getSize(pHeightMeasureSpec);
		  
		        final int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);  
		  
		        switch (heightMode) {  
		        case MeasureSpec.AT_MOST:  
//		        	System.out.println(" height AT_MOST getSize : " + result);
		        	if(Container.this.isSetMaxSize){
						result = Math.min(result, Container.this.maxSize.height);
					}
//		        	System.out.println(" height AT_MOST : " + result);
		            break;
		        case MeasureSpec.EXACTLY:  
//		        	System.out.println(" height EXACTLY getSize : " + result);
		        	if(Container.this.isSetPrefSize){
						result = Container.this.preSize.height;
					}
//		        	System.out.println(" height EXACTLY : " + result);
		            break;  
		        }  
		        return result;  
		    }
			
			protected int getSuggestedMinimumWidth() {
				if(Container.this.isSetMinSize){
//					System.out.println("-------getSuggestedMinimumWidth isSetMinSize : " + Container.this.minSize.width);
					return Container.this.minSize.width;
				}
//				System.out.println("-------getSuggestedMinimumWidth : " + super.getSuggestedMinimumWidth());
				return super.getSuggestedMinimumWidth();
			}
			
			protected int getSuggestedMinimumHeight() {
				if(Container.this.isSetMinSize){
//					System.out.println("-------getSuggestedMinimumHeight isSetMinSize : " + Container.this.minSize.height);
					return Container.this.minSize.height;
				}
//				System.out.println("-------getSuggestedMinimumHeight : " + super.getSuggestedMinimumHeight());
				return super.getSuggestedMinimumHeight();
			}
		};
//		super.setPeerAdAPI(p);//比如JPanel含layoutView且需要paint(g)时，不能setPeerAdAPI
		return p;
	}

	public void setLayout(final LayoutManager mgr) {
		if(mgr == null){
			return;
		}
		
		layout = mgr;
		if (layoutView == null && mgr instanceof HCCardLayout) {
			layoutView = new HCTabHost(ActivityManager.getActivity(), this);
		}
		if (layoutView == null) {
			layoutView = new AbsoluteLayout(ActivityManager.getActivity());
		}
			
//		if (mgr instanceof FlowLayout) {
//			if (layoutView == null || ((layoutView instanceof RelativeLayout) == false)) {
//				layoutView = new RelativeLayout(ActivityManager.getActivity());
//			}
//		} else if (mgr instanceof BorderLayout) {
//			if (layoutView == null || ((layoutView instanceof LinearLayout) == false)) {
//				layoutView = new LinearLayout(ActivityManager.getActivity());
//			}
//		} else if (mgr instanceof GridLayout) {
//			if (layoutView == null || ((layoutView instanceof android.widget.LinearLayout) == false)) {
//				layoutView = new android.widget.LinearLayout(ActivityManager.getActivity());
//			}
//		} else if (mgr instanceof GridBagLayout) {
//			if (layoutView == null || ((layoutView instanceof android.widget.GridLayout) == false)) {
//				layoutView = new android.widget.GridLayout(ActivityManager.getActivity());
//			}
//		} else if (mgr instanceof BoxLayout) {
//			if (layoutView == null || ((layoutView instanceof LinearLayout) == false)) {
//				layoutView = new LinearLayout(ActivityManager.getActivity());
//			}
//		} else if (mgr instanceof CardLayout) {
//			if (layoutView == null || ((layoutView instanceof HCTabHost) == false)) {
//				layoutView = new HCTabHost(ActivityManager.getActivity(), this);
//			}
//		} else {
//			//for example JRootPane$RootLayout
//			if (layoutView == null || ((layoutView instanceof LinearLayout) == false)) {
//				layoutView = new LinearLayout(ActivityManager.getActivity());
//			}
//		}
		
		layoutView.setBackgroundColor(AndroidUIUtil.WIN_BODY_BACK.toAndroid());

		super.setPeerAdAPI(layoutView);
		addOnLayoutChangeListenerAdAPI(layoutView);
	}
	
	protected void validateTree() {
		final boolean isSimu = PropertiesManager.isSimu();
		
		if(isSimu){
			LogManager.log(toString() + " begin validateTree.");
		}

		if(AndroidUIUtil.isJViewportInstance(this)){
			((JViewport)this).getView().validate();
			return;
		}
		
		doLayout();

		if(components != null){
			for (int i = 0; i < components.size(); i++) {
				final Component comp = components.get(i);
				if (comp instanceof Container) {
					((Container) comp).validateTree();
				} else {
					comp.validate();
				}
			}
		}
		super.validate();
		
		if(isSimu){
			LogManager.log(toString() + " end validateTree.");
		}
    }
	
	private final void switchParent(final ViewGroup oldView, final ViewGroup newView){
		final ViewGroup parent = (ViewGroup)oldView.getParent();
		if(parent != null){
			AndroidUIUtil.substituteView(parent, oldView, newView);
		}
	}
	
	public static final boolean isDisablePaintGAdAPI = true;//不实现paint(g)
	
	public void doLayout() {
		L.V = L.WShop ? false : LogManager.log("doLayout() -- " + toString());
		
		if(isLayoutValidAdAPI()){//container含有多个组件
			if(isUsePaintView){
				isUsePaintView = false;
				
				switchParent((ViewGroup)paintLinearLayout, (ViewGroup)layoutView);
			}
			layout();
		}else if(layoutView != null){//不含组件，仅使用Container.paint(g)
			if(isDisablePaintGAdAPI == false){
				AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
					@Override
					public void run() {
						if(isUsePaintView == false){
							isUsePaintView = true;
							paintLinearLayout = new LinearLayout(ActivityManager.getActivity());
							Container.this.addOnLayoutChangeListenerAdAPI(paintLinearLayout);
						}
						
						switchParent((ViewGroup)layoutView, (ViewGroup)paintLinearLayout);
						
						paintLinearLayout.removeAllViews();
						final LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
						lp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
						paintLinearLayout.addView(buildEmptyCanvas(), lp);
					}
				});
			}
		}else{//不含container，只绘组件
			repaint();//JPanel.paint(Graphics)
		}
	}

	public void layout() {
		L.V = L.WShop ? false : LogManager.log("layout() -- " + toString());
		synchronized(this){
			final LayoutManager layoutMgr = layout;
			if (layoutMgr != null) {
				L.V = L.WShop ? false : LogManager.log("layoutContainer() -- " + layoutMgr.toString());
				layoutMgr.layoutContainer(this);// TODO
			}
		}
	}

	public boolean isValidateRoot() {
		return false;
	}

	public void revalidate() {
		doLayout();
	}

	//
	public void validate() {
		validateTree();
	}

	public void setFont(final Font f) {
		super.setFont(f);// TODO
	}

	public Dimension getPreferredSize() {
		return preferredSize();
	}
	
	boolean containsFocus() {
        final Component focusOwner = FocusManager.getFocusComponentOwner();
        if(focusOwner != null){
        	return isParentOf(focusOwner);
        }else{
        	return false;
        }
    }
	
	private boolean isParentOf(Component comp) {
        while (comp != null && comp != this && !(comp instanceof Window)) {
            comp = comp.getParent();
        }
        return (comp == this);
    }
	
	public View getContainerViewAdAPI(){
		return layoutView;//super.getPeerAdAPI();
	}
	
	public Dimension preferredSize() {
        if (preSize == null || !(isPreferredSizeSet() || isValid())) {
        	if(layout != null){
//        		final Dimension rect = new Dimension();
//        		UIUtil.getViewWidthAndHeight(this.getContainerViewAdAPI(), rect);//layout.preferredLayoutSize(this);
//        		return rect;
        		synchronized (getTreeLock()) {
        			preSize = layout.preferredLayoutSize(this);
        		}
        	}else{
        		return super.preferredSize();
        	}
        }
		L.V = L.WShop ? false : LogManager.log("preferredSize() -- " + toString() + " : " + preSize.toString());
        return new Dimension(preSize);
	}

	public Component searchComponentByViewAdAPI(final View view){
		if(getPeerAdAPI() == view){
			return this;
		}
		
		final int size = getComponentCount();
		for (int i = 0; i < size; i++) {
			final Component next = getComponent(i);
			if(next.getPeerAdAPI() == view){
				return next;
			}
			if(next instanceof Container){
				final Component out = searchComponentByViewAdAPI(view);
				if(out != null){
					return out;
				}
			}
		}
		return null;
	}

	public Dimension getMinimumSize() {
		return minimumSize();
	}

	public Dimension minimumSize() {
		if(minSize == null || !isMinimumSizeSet()){
			if (layout != null) {
	//			final Dimension dimension = new Dimension();
	//			AndroidUIUtil.getViewWidthAndHeight(layoutView, dimension);
				synchronized (getTreeLock()) {
					minSize = layout.minimumLayoutSize(this);
	    		}
			}else{
				return super.minimumSize();
			}
		}
		L.V = L.WShop ? false : LogManager.log("minimumSize() -- " + toString() + " : " + minSize.toString());
		return new Dimension(minSize);
	}

	protected final boolean isLayoutValidAdAPI() {
		return layout != null && countComponents() > 0;//因为如JPanel只含有paint(Graphics g)时,countComponents() == 0
	}

	public Dimension getMaximumSize() {
		Dimension dim = maxSize;
        if (dim == null || !(isMaximumSizeSet() || isValid())) {
            synchronized (getTreeLock()) {
               if (layout != null && layout instanceof LayoutManager2) {
                    LayoutManager2 lm = (LayoutManager2) layout;
                    maxSize = lm.maximumLayoutSize(this);
               } else {
                    maxSize = super.getMaximumSize();
               }
               dim = maxSize;
            }
        }
        if (dim != null){
            return new Dimension(dim);
        }else{
            return dim;
        }
	}

	/**
	 * 0 represents alignment along the origin, 0.5 is centered, 1 is furthest away from the origin
	 */
	public float getAlignmentX() {
        if (layout instanceof LayoutManager2) {
            return ((LayoutManager2)layout).getLayoutAlignmentX(this);
        } else {
            return super.getAlignmentX();
        }
	}

	public float getAlignmentY() {
        if (layout instanceof LayoutManager2) {
            return ((LayoutManager2)layout).getLayoutAlignmentY(this);
        } else {
            return super.getAlignmentY();
        }
	}

	public void paint(final Graphics g) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public void update(final Graphics g) {
		if (isShowing()) {
            g.clearRect(0, 0, getWidth(), getHeight());
            paint(g);
        }
	}

	public void print(final Graphics g) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public void paintComponents(final Graphics g) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public void printComponents(final Graphics g) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	/**
	 * 有一级子组件，返回一级子组件，不是N级。如果没有，且在自身组件内，返回自己。
	 * @param x
	 * @param y
	 * @return
	 */
	public Component getComponentAt(final int x, final int y) {
		return locate(x, y);
	}

	public Component locate(final int x, final int y) {
		if (contains(x, y) == false || components == null) {
            return null;
        }
        for (int i = 0; i < components.size(); i++) {
            final Component comp = components.get(i);
            if (comp != null){
            	final int subX = comp.getX();
            	final int subY = comp.getY();
//				System.out.println("--------sub idx : " + i + ", Comp : " + comp.getClass().getName() + ", subX:" + subX + ", subY:" + subY + ", subW:" + comp.getWidth() + ", subH:" + comp.getHeight());
				if (subX <= x && x <= (subX + comp.getWidth()) && subY <= y && y <= (subY + comp.getHeight())) {
//					System.out.println("------find component : " + comp.getClass().getName() + " at idx : " + i);
                    return comp;
                }
            }
        }
        return this;
	}

	public Component getComponentAt(final Point p) {
		return getComponentAt(p.x, p.y);
	}

	public Component findComponentAt(final int x, final int y) {
		return findComponentAt(x, y, true);
	}

	Component findComponentAt(final int x, final int y, final boolean ignoreEnabled) {
		synchronized (this) {
			return findComponentAtImpl(x, y, ignoreEnabled);
		}
	}

	Component findComponentAtImpl(final int x, final int y, final boolean ignoreEnabled) {
        if (((contains(x, y) == false) && isVisible() && (ignoreEnabled || isEnabled()))) {
            return null;
        }

        for (int i = 0; i < components.size(); i++) {
            Component comp = components.get(i);
            if (comp != null) {
                if (comp instanceof Container) {
                    comp = ((Container)comp).findComponentAtImpl(x - comp.getX(), y - comp.getY(), ignoreEnabled);
                } else {
                    comp = comp.locate(x - comp.getX(), y - comp.getY());
                }
                if (comp != null && comp.isVisible() && (ignoreEnabled || comp.isEnabled())){
                    return comp;
                }
            }
        }

        return this;
	}

	public Component findComponentAt(final Point p) {
		return findComponentAt(p.x, p.y);
	}

	public void applyComponentOrientation(final ComponentOrientation o) {
		super.applyComponentOrientation(o);
		if(components != null){
			synchronized (this) {
				for (int i = 0; i < components.size(); i++) {
					final Component comp = components.get(i);
					comp.applyComponentOrientation(o);
				}
			}
		}
		
//		if(old.equals(o) == false){//必须另执行validate()后生效
//			revalidate();
//		}
	}

	@Override
	public void remove(final MenuComponent comp) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	@Override
	public boolean postEvent(final Event evt) {
		return false;
	}
}