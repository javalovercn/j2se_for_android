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
import hc.core.util.LogManager;
import hc.util.PropertiesManager;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.border.Border;
import javax.swing.plaf.ScrollPaneUI;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Provides a scrollable view of a lightweight component. A
 * <code>JScrollPane</code> manages a viewport, optional vertical and horizontal
 * scroll bars, and optional row and column heading viewports. You can find
 * task-oriented documentation of <code>JScrollPane</code> in <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/scrollpane.html">How
 * to Use Scroll Panes</a>, a section in <em>The Java Tutorial</em>. Note that
 * <code>JScrollPane</code> does not support heavyweight components.
 * <p>
 * <TABLE ALIGN="RIGHT" BORDER="0" SUMMARY="layout">
 * <TR>
 * <TD ALIGN="CENTER">
 * <P ALIGN="CENTER">
 * <IMG SRC="doc-files/JScrollPane-1.gif" alt="The following text describes this
 * image." WIDTH="256" HEIGHT="248" ALIGN="BOTTOM" BORDER="0"></TD>
 * </TR>
 * </TABLE>
 * The <code>JViewport</code> provides a window, or &quot;viewport&quot; onto a
 * data source -- for example, a text file. That data source is the
 * &quot;scrollable client&quot; (aka data model) displayed by the
 * <code>JViewport</code> view. A <code>JScrollPane</code> basically consists of
 * <code>JScrollBar</code>s, a <code>JViewport</code>, and the wiring between
 * them, as shown in the diagram at right.
 * <p>
 * In addition to the scroll bars and viewport, a <code>JScrollPane</code> can
 * have a column header and a row header. Each of these is a
 * <code>JViewport</code> object that you specify with
 * <code>setRowHeaderView</code>, and <code>setColumnHeaderView</code>. The
 * column header viewport automatically scrolls left and right, tracking the
 * left-right scrolling of the main viewport. (It never scrolls vertically,
 * however.) The row header acts in a similar fashion.
 * <p>
 * Where two scroll bars meet, the row header meets the column header, or a
 * scroll bar meets one of the headers, both components stop short of the
 * corner, leaving a rectangular space which is, by default, empty. These spaces
 * can potentially exist in any number of the four corners. In the previous
 * diagram, the top right space is present and identified by the label "corner
 * component".
 * <p>
 * Any number of these empty spaces can be replaced by using the
 * <code>setCorner</code> method to add a component to a particular corner.
 * (Note: The same component cannot be added to multiple corners.) This is
 * useful if there's some extra decoration or function you'd like to add to the
 * scroll pane. The size of each corner component is entirely determined by the
 * size of the headers and/or scroll bars that surround it.
 * <p>
 * A corner component will only be visible if there is an empty space in that
 * corner for it to exist in. For example, consider a component set into the top
 * right corner of a scroll pane with a column header. If the scroll pane's
 * vertical scrollbar is not present, perhaps because the view component hasn't
 * grown large enough to require it, then the corner component will not be shown
 * (since there is no empty space in that corner created by the meeting of the
 * header and vertical scroll bar). Forcing the scroll bar to always be shown,
 * using <code>setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS)</code>,
 * will ensure that the space for the corner component always exists.
 * <p>
 * To add a border around the main viewport, you can use
 * <code>setViewportBorder</code>. (Of course, you can also add a border around
 * the whole scroll pane using <code>setBorder</code>.)
 * <p>
 * A common operation to want to do is to set the background color that will be
 * used if the main viewport view is smaller than the viewport, or is not
 * opaque. This can be accomplished by setting the background color of the
 * viewport, via <code>scrollPane.getViewport().setBackground()</code>. The
 * reason for setting the color of the viewport and not the scrollpane is that
 * by default <code>JViewport</code> is opaque which, among other things, means
 * it will completely fill in its background using its background color.
 * Therefore when <code>JScrollPane</code> draws its background the viewport
 * will usually draw over it.
 * <p>
 * By default <code>JScrollPane</code> uses <code>ScrollPaneLayout</code> to
 * handle the layout of its child Components. <code>ScrollPaneLayout</code>
 * determines the size to make the viewport view in one of two ways:
 * <ol>
 * <li>If the view implements <code>Scrollable</code> a combination of
 * <code>getPreferredScrollableViewportSize</code>,
 * <code>getScrollableTracksViewportWidth</code> and
 * <code>getScrollableTracksViewportHeight</code>is used, otherwise
 * <li><code>getPreferredSize</code> is used.
 * </ol>
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
 * @see JScrollBar
 * @see JViewport
 * @see ScrollPaneLayout
 * @see Scrollable
 * @see Component#getPreferredSize
 * @see #setViewportView
 * @see #setRowHeaderView
 * @see #setColumnHeaderView
 * @see #setCorner
 * @see #setViewportBorder
 *
 * @beaninfo attribute: isContainer true attribute: containerDelegate
 *           getViewport description: A specialized container that manages a
 *           viewport, optional scrollbars and headers
 *
 * @author Hans Muller
 */
public class JScrollPane extends JComponent implements ScrollPaneConstants, Accessible {
	final LinearLayout defaultLinearLayout;
	final ScrollView scrollView;
	final HorizontalScrollView hScrollView;//注意：采用HVScroll会导致嵌入的JTable下的header左挤在一起

	private static final String uiClassID = "ScrollPaneUI";

	protected int verticalScrollBarPolicy = VERTICAL_SCROLLBAR_AS_NEEDED;
	protected int horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_AS_NEEDED;

	public JScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		defaultLinearLayout = new LinearLayout(ActivityManager.applicationContext);

		setLayout(new ScrollPaneLayout.UIResource());

		scrollView = new ScrollView(ActivityManager.applicationContext);
		scrollView.setBackgroundColor(AndroidUIUtil.WIN_BODY_BACK.toAndroid());
		// scrollView.setBackgroundColor(UIUtil.getEditBackground().toAndroid());

		hScrollView = new HorizontalScrollView(ActivityManager.applicationContext);
		hScrollView.setBackgroundColor(AndroidUIUtil.WIN_BODY_BACK.toAndroid());

		{
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			hScrollView.addView(scrollView, lp);

			hScrollView.setFillViewport(true);// 如果表格内容小于最大可用宽度，则自动扩展以填满宽度。
		}

		{
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			defaultLinearLayout.addView(hScrollView, lp);
		}

		if (view != null) {
			setViewportView(view);
		}

		setHorizontalScrollBarPolicy(hsbPolicy);
		setVerticalScrollBarPolicy(vsbPolicy);
	}

	public JScrollPane(Component view) {
		this(view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	public JScrollPane(int vsbPolicy, int hsbPolicy) {
		this(null, vsbPolicy, hsbPolicy);
	}

	public JScrollPane() {
		this(null, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	public ScrollPaneUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setUI(ScrollPaneUI ui) {
		AndroidClassUtil.callEmptyMethod();
		super.setUI(ui);
	}

	public void updateUI() {
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public void setLayout(LayoutManager layout) {
		super.setLayout(layout);
		setPeerAdAPI(defaultLinearLayout);// 特别处理
	}

	@Override
	public boolean isValidateRoot() {
		AndroidClassUtil.callEmptyMethod();
		return true;
	}

	public int getVerticalScrollBarPolicy() {
		return verticalScrollBarPolicy;
	}

	public void setVerticalScrollBarPolicy(int policy) {
		LogManager.warning("#########only VERTICAL_SCROLLBAR_AS_NEEDED is supported!#########");
	}

	public int getHorizontalScrollBarPolicy() {
		return horizontalScrollBarPolicy;
	}

	public void setHorizontalScrollBarPolicy(int policy) {
		LogManager.warning(
				"#########only HORIZONTAL_SCROLLBAR_AS_NEEDED is supported and default !#########");
	}

	public Border getViewportBorder() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setViewportBorder(Border viewportBorder) {
		setBorder(viewportBorder);
	}

	public Rectangle getViewportBorderBounds() {
		AndroidClassUtil.callEmptyMethod();
		Rectangle borderR = new Rectangle(getSize());
		return borderR;
	}

	public JScrollBar createHorizontalScrollBar() {
		JScrollBar out = new JScrollBar(JScrollBar.HORIZONTAL);
		out.scrollPane = this;
		return out;
	}

	@Transient
	public JScrollBar getHorizontalScrollBar() {
		if (hScrollBar == null) {
			hScrollBar = createHorizontalScrollBar();
		}
		return hScrollBar;
	}

	public void setHorizontalScrollBar(JScrollBar horizontalScrollBar) {
		hScrollBar = horizontalScrollBar;
	}

	public JScrollBar createVerticalScrollBar() {
		JScrollBar out = new JScrollBar(JScrollBar.VERTICAL);
		out.scrollPane = this;
		return out;
	}

	JScrollBar vScrollBar, hScrollBar;

	@Transient
	public JScrollBar getVerticalScrollBar() {
		if (vScrollBar == null) {
			vScrollBar = createVerticalScrollBar();
		}
		return vScrollBar;
	}

	public void setVerticalScrollBar(JScrollBar verticalScrollBar) {
		vScrollBar = verticalScrollBar;
	}

	/**
	 * Returns a new <code>JViewport</code> by default. Used to create the
	 * viewport (as needed) in <code>setViewportView</code>,
	 * <code>setRowHeaderView</code>, and <code>setColumnHeaderView</code>.
	 * Subclasses may override this method to return a subclass of
	 * <code>JViewport</code>.
	 *
	 * @return a new <code>JViewport</code>
	 */
	protected JViewport createViewport() {
		return new JViewport();
	}

	/**
	 * The scrollpane's viewport child. Default is an empty
	 * <code>JViewport</code>.
	 * 
	 * @see #setViewport
	 */
	protected JViewport viewport;

	public JViewport getViewport() {
		return viewport;
	}

	public void setViewport(JViewport viewport) {
		JViewport old = getViewport();
		this.viewport = viewport;
		if (viewport != null) {
			add(viewport, VIEWPORT);
		} else if (old != null) {
			remove(old);
		}
		firePropertyChange("viewport", old, viewport);

		// rem by yyh
		// if (accessibleContext != null) {
		// ((AccessibleJScrollPane)accessibleContext).resetViewPort();
		// }

		revalidate();
		repaint();
	}

	Component viewportComponent;

	public void setViewportView(Component view) {
		if (getViewport() == null) {
			final JViewport createViewport = createViewport();
			createViewport.setView(view);
			setViewport(createViewport);// 会导致revalidate，需要view不为null
		} else {
			getViewport().setView(view);
		}

		view.validate();

		viewportComponent = view;

		final View peerAdded = viewportComponent.getPeerAdAPI();
		AndroidUIUtil.removeFromParent(peerAdded);
		scrollView.removeAllViews();
		ScrollView.LayoutParams lp = new ScrollView.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		scrollView.addView(peerAdded, lp);
	}

	// public void validate() {
	// if(viewportComponent != null){
	//// viewportComponent.setPreferredSize(getPreferredSize());
	// viewportComponent.validate();
	//// final Dimension vcDim = viewportComponent.getPreferredSize();
	//// final Dimension jsDim = getPreferredSize();
	//// if(vcDim.width < jsDim.width && vcDim.height < jsDim.height){
	//// viewportComponent.setPreferredSize(jsDim);
	//// viewportComponent.validate();
	//// LogManager.log("viewportComponent validate to fit JScrollPane.");
	//// }
	// }
	// }

	// Android专有
	public Component getViewportViewAdAPI() {
		return viewportComponent;
	}

	@Override
	public View getPeerAdAPI() {
		return defaultLinearLayout;

		//
		// return super.getPeerAdAPI();
	}

	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
		// hScrollView.setLayoutParams(new
		// LinearLayout.LayoutParams(preSize.width, preSize.height));
		if (PropertiesManager.isSimu()) {
			System.out.println("setPreferredSize for JScrollPane w : " + preSize.width + ", h : "
					+ preSize.height);
		}
		// LayoutParams lp = scrollView.getLayoutParams();
		// lp.width = preferredSize.width;
		// lp.height = preferredSize.height;
	}

	@Transient
	public JViewport getRowHeader() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setRowHeader(JViewport rowHeader) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setRowHeaderView(Component view) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Transient
	public JViewport getColumnHeader() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setColumnHeader(JViewport columnHeader) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setColumnHeaderView(Component view) {
		AndroidClassUtil.callEmptyMethod();
	}

	public Component getCorner(String key) {
		AndroidClassUtil.callEmptyMethod();

		return null;
	}

	public void setCorner(String key, Component corner) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setComponentOrientation(ComponentOrientation co) {
		super.setComponentOrientation(co);
	}

	public void applyComponentOrientation(final ComponentOrientation o) {
		super.applyComponentOrientation(o);

		if (viewportComponent != null) {
			viewportComponent.applyComponentOrientation(o);
		}
	}

	public boolean isWheelScrollingEnabled() {
		return false;
	}

	public void setWheelScrollingEnabled(boolean handleWheel) {
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
