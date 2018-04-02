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

import java.awt.Component;
import java.awt.Graphics;
import java.util.Hashtable;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleKeyBinding;
import javax.accessibility.AccessibleRole;

/**
 * <code>JLayeredPane</code> adds depth to a JFC/Swing container, allowing
 * components to overlap each other when needed. An <code>Integer</code> object
 * specifies each component's depth in the container, where higher-numbered
 * components sit &quot;on top&quot; of other components. For task-oriented
 * documentation and examples of using layered panes see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/layeredpane.html">How
 * to Use a Layered Pane</a>, a section in <em>The Java Tutorial</em>.
 * <P>
 * <TABLE ALIGN="RIGHT" BORDER="0" SUMMARY="layout">
 * <TR>
 * <TD ALIGN="CENTER">
 * <P ALIGN="CENTER">
 * <IMG SRC="doc-files/JLayeredPane-1.gif" alt="The following text describes
 * this image." WIDTH="269" HEIGHT="264" ALIGN="BOTTOM" BORDER="0"></TD>
 * </TR>
 * </TABLE>
 * For convenience, <code>JLayeredPane</code> divides the depth-range into
 * several different layers. Putting a component into one of those layers makes
 * it easy to ensure that components overlap properly, without having to worry
 * about specifying numbers for specific depths:
 * <DL>
 * <DT><FONT SIZE="2">DEFAULT_LAYER</FONT></DT>
 * <DD>The standard layer, where most components go. This the bottommost layer.
 * <DT><FONT SIZE="2">PALETTE_LAYER</FONT></DT>
 * <DD>The palette layer sits over the default layer. Useful for floating
 * toolbars and palettes, so they can be positioned above other components.
 * <DT><FONT SIZE="2">MODAL_LAYER</FONT></DT>
 * <DD>The layer used for modal dialogs. They will appear on top of any
 * toolbars, palettes, or standard components in the container.
 * <DT><FONT SIZE="2">POPUP_LAYER</FONT></DT>
 * <DD>The popup layer displays above dialogs. That way, the popup windows
 * associated with combo boxes, tooltips, and other help text will appear above
 * the component, palette, or dialog that generated them.
 * <DT><FONT SIZE="2">DRAG_LAYER</FONT></DT>
 * <DD>When dragging a component, reassigning it to the drag layer ensures that
 * it is positioned over every other component in the container. When finished
 * dragging, it can be reassigned to its normal layer.
 * </DL>
 * The <code>JLayeredPane</code> methods <code>moveToFront(Component)</code>,
 * <code>moveToBack(Component)</code> and <code>setPosition</code> can be used
 * to reposition a component within its layer. The <code>setLayer</code> method
 * can also be used to change the component's current layer.
 *
 * <h2>Details</h2> <code>JLayeredPane</code> manages its list of children like
 * <code>Container</code>, but allows for the definition of a several layers
 * within itself. Children in the same layer are managed exactly like the normal
 * <code>Container</code> object, with the added feature that when children
 * components overlap, children in higher layers display above the children in
 * lower layers.
 * <p>
 * Each layer is a distinct integer number. The layer attribute can be set on a
 * <code>Component</code> by passing an <code>Integer</code> object during the
 * add call.<br>
 * For example:
 * 
 * <PRE>
 *     layeredPane.add(child, JLayeredPane.DEFAULT_LAYER);
 * or
 *     layeredPane.add(child, new Integer(10));
 * </PRE>
 * 
 * The layer attribute can also be set on a Component by calling
 * 
 * <PRE>
 * layeredPaneParent.setLayer(child, 10)
 * </PRE>
 * 
 * on the <code>JLayeredPane</code> that is the parent of component. The layer
 * should be set <i>before</i> adding the child to the parent.
 * <p>
 * Higher number layers display above lower number layers. So, using numbers for
 * the layers and letters for individual components, a representative list order
 * would look like this:
 * 
 * <PRE>
 *       5a, 5b, 5c, 2a, 2b, 2c, 1a
 * </PRE>
 * 
 * where the leftmost components are closest to the top of the display.
 * <p>
 * A component can be moved to the top or bottom position within its layer by
 * calling <code>moveToFront</code> or <code>moveToBack</code>.
 * <p>
 * The position of a component within a layer can also be specified directly.
 * Valid positions range from 0 up to one less than the number of components in
 * that layer. A value of -1 indicates the bottommost position. A value of 0
 * indicates the topmost position. Unlike layer numbers, higher position values
 * are <i>lower</i> in the display. <blockquote> <b>Note:</b> This sequence
 * (defined by java.awt.Container) is the reverse of the layer numbering
 * sequence. Usually though, you will use <code>moveToFront</code>,
 * <code>moveToBack</code>, and <code>setLayer</code>. </blockquote> Here are
 * some examples using the method add(Component, layer, position): Calling
 * add(5x, 5, -1) results in:
 * 
 * <PRE>
 *       5a, 5b, 5c, 5x, 2a, 2b, 2c, 1a
 * </PRE>
 *
 * Calling add(5z, 5, 2) results in:
 * 
 * <PRE>
 *       5a, 5b, 5z, 5c, 5x, 2a, 2b, 2c, 1a
 * </PRE>
 *
 * Calling add(3a, 3, 7) results in:
 * 
 * <PRE>
 *       5a, 5b, 5z, 5c, 5x, 3a, 2a, 2b, 2c, 1a
 * </PRE>
 *
 * Using normal paint/event mechanics results in 1a appearing at the bottom and
 * 5a being above all other components.
 * <p>
 * <b>Note:</b> that these layers are simply a logical construct and
 * LayoutManagers will affect all child components of this container without
 * regard for layer settings.
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
 * @author David Kloba
 */
public class JLayeredPane extends JComponent implements Accessible {
	public final static Integer DEFAULT_LAYER = new Integer(0);
	public final static Integer PALETTE_LAYER = new Integer(100);
	public final static Integer MODAL_LAYER = new Integer(200);
	public final static Integer POPUP_LAYER = new Integer(300);
	public final static Integer DRAG_LAYER = new Integer(400);
	public final static Integer FRAME_CONTENT_LAYER = new Integer(-30000);

	public final static String LAYER_PROPERTY = "layeredContainerLayer";
	private Hashtable<Component, Integer> componentToLayer;
	private boolean optimizedDrawingPossible = true;

	public JLayeredPane() {
		setLayout(null);
	}

	private void validateOptimizedDrawing() {
	}

	protected void addImpl(Component comp, Object constraints, int index) {
	}

	public void remove(int index) {
	}

	public void removeAll() {
	}

	public boolean isOptimizedDrawingEnabled() {
		return optimizedDrawingPossible;
	}

	public static void putLayer(JComponent c, int layer) {
	}

	public static int getLayer(JComponent c) {
		return 0;
	}

	public static JLayeredPane getLayeredPaneAbove(Component c) {
		return null;
	}

	public void setLayer(Component c, int layer) {
		setLayer(c, layer, -1);
	}

	public void setLayer(Component c, int layer, int position) {
	}

	public int getLayer(Component c) {
		return 0;
	}

	public int getIndexOf(Component c) {
		return -1;
	}

	public void moveToFront(Component c) {
		setPosition(c, 0);
	}

	public void moveToBack(Component c) {
		setPosition(c, -1);
	}

	public void setPosition(Component c, int position) {
		setLayer(c, getLayer(c), position);
	}

	public int getPosition(Component c) {
		return -1;
	}

	public int highestLayer() {
		return 0;
	}

	public int lowestLayer() {
		return 0;
	}

	public int getComponentCountInLayer(int layer) {
		return 0;
	}

	public Component[] getComponentsInLayer(int layer) {
		return new Component[0];
	}

	public void paint(Graphics g) {
	}

	protected Hashtable<Component, Integer> getComponentToLayer() {
		if (componentToLayer == null)
			componentToLayer = new Hashtable<Component, Integer>(4);
		return componentToLayer;
	}

	protected Integer getObjectForLayer(int layer) {
		Integer layerObj;
		layerObj = DEFAULT_LAYER;
		return layerObj;
	}

	protected int insertIndexForLayer(int layer, int position) {
		return insertIndexForLayer(null, layer, position);
	}

	private int insertIndexForLayer(Component comp, int layer, int position) {
		return 0;
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