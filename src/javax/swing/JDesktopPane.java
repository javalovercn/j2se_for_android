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
import java.awt.Container;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.plaf.DesktopPaneUI;

/**
 * A container used to create a multiple-document interface or a virtual
 * desktop. You create <code>JInternalFrame</code> objects and add them to the
 * <code>JDesktopPane</code>. <code>JDesktopPane</code> extends
 * <code>JLayeredPane</code> to manage the potentially overlapping internal
 * frames. It also maintains a reference to an instance of
 * <code>DesktopManager</code> that is set by the UI class for the current look
 * and feel (L&F). Note that <code>JDesktopPane</code> does not support borders.
 * <p>
 * This class is normally used as the parent of <code>JInternalFrames</code> to
 * provide a pluggable <code>DesktopManager</code> object to the
 * <code>JInternalFrames</code>. The <code>installUI</code> of the L&F specific
 * implementation is responsible for setting the <code>desktopManager</code>
 * variable appropriately. When the parent of a <code>JInternalFrame</code> is a
 * <code>JDesktopPane</code>, it should delegate most of its behavior to the
 * <code>desktopManager</code> (closing, resizing, etc).
 * <p>
 * For further documentation and examples see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/internalframe.html">How
 * to Use Internal Frames</a>, a section in <em>The Java Tutorial</em>.
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
 * @see JInternalFrame
 * @see JInternalFrame.JDesktopIcon
 * @see DesktopManager
 *
 * @author David Kloba
 */
public class JDesktopPane extends JLayeredPane implements Accessible {
	private static final String uiClassID = "DesktopPaneUI";

	transient DesktopManager desktopManager;

	private transient JInternalFrame selectedFrame = null;

	public static final int LIVE_DRAG_MODE = 0;
	public static final int OUTLINE_DRAG_MODE = 1;

	private int dragMode = LIVE_DRAG_MODE;
	private boolean dragModeSet = false;
	private transient List<JInternalFrame> framesCache;
	private boolean componentOrderCheckingEnabled = true;
	private boolean componentOrderChanged = false;

	public JDesktopPane() {
		setUIProperty("opaque", Boolean.TRUE);

		updateUI();
	}

	public DesktopPaneUI getUI() {
		return (DesktopPaneUI) null;
	}

	public void setUI(DesktopPaneUI ui) {
		super.setUI(ui);
	}

	public void setDragMode(int dragMode) {
		int oldDragMode = this.dragMode;
		this.dragMode = dragMode;
		firePropertyChange("dragMode", oldDragMode, this.dragMode);
		dragModeSet = true;
	}

	public int getDragMode() {
		return dragMode;
	}

	public DesktopManager getDesktopManager() {
		return desktopManager;
	}

	public void setDesktopManager(DesktopManager d) {
		DesktopManager oldValue = desktopManager;
		desktopManager = d;
		firePropertyChange("desktopManager", oldValue, desktopManager);
	}

	public void updateUI() {
		setUI((DesktopPaneUI) UIManager.getUI(this));
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public JInternalFrame[] getAllFrames() {
		return getAllFrames(this).toArray(new JInternalFrame[0]);
	}

	private static Collection<JInternalFrame> getAllFrames(Container parent) {
		int i, count;
		Collection<JInternalFrame> results = new ArrayList<JInternalFrame>();
		count = parent.getComponentCount();
		for (i = 0; i < count; i++) {
			Component next = parent.getComponent(i);
			if (next instanceof JInternalFrame) {
				results.add((JInternalFrame) next);
			} else if (next instanceof JInternalFrame.JDesktopIcon) {
				JInternalFrame tmp = ((JInternalFrame.JDesktopIcon) next).getInternalFrame();
				if (tmp != null) {
					results.add(tmp);
				}
			} else if (next instanceof Container) {
				results.addAll(getAllFrames((Container) next));
			}
		}
		return results;
	}

	public JInternalFrame getSelectedFrame() {
		return selectedFrame;
	}

	public void setSelectedFrame(JInternalFrame f) {
		selectedFrame = f;
	}

	public JInternalFrame[] getAllFramesInLayer(int layer) {
		Collection<JInternalFrame> allFrames = getAllFrames(this);
		Iterator<JInternalFrame> iterator = allFrames.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getLayer() != layer) {
				iterator.remove();
			}
		}
		return allFrames.toArray(new JInternalFrame[0]);
	}

	private List<JInternalFrame> getFrames() {
		Component c;
		Set<ComponentPosition> set = new TreeSet<ComponentPosition>();
		for (int i = 0; i < getComponentCount(); i++) {
			c = getComponent(i);
			if (c instanceof JInternalFrame) {
				set.add(new ComponentPosition((JInternalFrame) c, getLayer(c), i));
			} else if (c instanceof JInternalFrame.JDesktopIcon) {
				c = ((JInternalFrame.JDesktopIcon) c).getInternalFrame();
				set.add(new ComponentPosition((JInternalFrame) c, getLayer(c), i));
			}
		}
		List<JInternalFrame> frames = new ArrayList<JInternalFrame>(set.size());
		for (ComponentPosition position : set) {
			frames.add(position.component);
		}
		return frames;
	}

	private static class ComponentPosition implements Comparable<ComponentPosition> {
		private final JInternalFrame component;
		private final int layer;
		private final int zOrder;

		ComponentPosition(JInternalFrame component, int layer, int zOrder) {
			this.component = component;
			this.layer = layer;
			this.zOrder = zOrder;
		}

		public int compareTo(ComponentPosition o) {
			int delta = o.layer - layer;
			if (delta == 0) {
				return zOrder - o.zOrder;
			}
			return delta;
		}
	}

	private JInternalFrame getNextFrame(JInternalFrame f, boolean forward) {
		verifyFramesCache();
		if (f == null) {
			return getTopInternalFrame();
		}
		int i = framesCache.indexOf(f);
		if (i == -1 || framesCache.size() == 1) {
			return null;
		}
		if (forward) {
			if (++i == framesCache.size()) {
				i = 0;
			}
		} else {
			if (--i == -1) {
				i = framesCache.size() - 1;
			}
		}
		return framesCache.get(i);
	}

	JInternalFrame getNextFrame(JInternalFrame f) {
		return getNextFrame(f, true);
	}

	private JInternalFrame getTopInternalFrame() {
		if (framesCache.size() == 0) {
			return null;
		}
		return framesCache.get(0);
	}

	private void updateFramesCache() {
		framesCache = getFrames();
	}

	private void verifyFramesCache() {
		if (componentOrderChanged) {
			componentOrderChanged = false;
			updateFramesCache();
		}
	}

	@Override
	public void remove(Component comp) {
		super.remove(comp);
		updateFramesCache();
	}

	public JInternalFrame selectFrame(boolean forward) {
		JInternalFrame selectedFrame = getSelectedFrame();
		JInternalFrame frameToSelect = getNextFrame(selectedFrame, forward);
		if (frameToSelect == null) {
			return null;
		}
		setComponentOrderCheckingEnabled(false);
		if (forward && selectedFrame != null) {
			selectedFrame.moveToBack();
		}
		try {
			frameToSelect.setSelected(true);
		} catch (PropertyVetoException pve) {
		}
		setComponentOrderCheckingEnabled(true);
		return frameToSelect;
	}

	void setComponentOrderCheckingEnabled(boolean enable) {
		componentOrderCheckingEnabled = enable;
	}

	protected void addImpl(Component comp, Object constraints, int index) {
		super.addImpl(comp, constraints, index);
		if (componentOrderCheckingEnabled) {
			if (comp instanceof JInternalFrame || comp instanceof JInternalFrame.JDesktopIcon) {
				componentOrderChanged = true;
			}
		}
	}

	public void remove(int index) {
		if (componentOrderCheckingEnabled) {
			Component comp = getComponent(index);
			if (comp instanceof JInternalFrame || comp instanceof JInternalFrame.JDesktopIcon) {
				componentOrderChanged = true;
			}
		}
		super.remove(index);
	}

	public void removeAll() {
		if (componentOrderCheckingEnabled) {
			int count = getComponentCount();
			for (int i = 0; i < count; i++) {
				Component comp = getComponent(i);
				if (comp instanceof JInternalFrame || comp instanceof JInternalFrame.JDesktopIcon) {
					componentOrderChanged = true;
					break;
				}
			}
		}
		super.removeAll();
	}

	public void setComponentZOrder(Component comp, int index) {
		super.setComponentZOrder(comp, index);
		if (componentOrderCheckingEnabled) {
			if (comp instanceof JInternalFrame || comp instanceof JInternalFrame.JDesktopIcon) {
				componentOrderChanged = true;
			}
		}
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	void setUIProperty(String propertyName, Object value) {
		if (propertyName == "dragMode") {
			if (!dragModeSet) {
				setDragMode(((Integer) value).intValue());
				dragModeSet = false;
			}
		} else {
			super.setUIProperty(propertyName, value);
		}
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