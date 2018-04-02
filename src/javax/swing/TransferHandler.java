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

import java.awt.Component;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Serializable;
import java.util.TooManyListenersException;

import javax.swing.event.EventListenerList;
import javax.swing.plaf.UIResource;

/**
 * This class is used to handle the transfer of a <code>Transferable</code> to
 * and from Swing components. The <code>Transferable</code> is used to represent
 * data that is exchanged via a cut, copy, or paste to/from a clipboard. It is
 * also used in drag-and-drop operations to represent a drag from a component,
 * and a drop to a component. Swing provides functionality that automatically
 * supports cut, copy, and paste keyboard bindings that use the functionality
 * provided by an implementation of this class. Swing also provides
 * functionality that automatically supports drag and drop that uses the
 * functionality provided by an implementation of this class. The Swing
 * developer can concentrate on specifying the semantics of a transfer primarily
 * by setting the <code>transferHandler</code> property on a Swing component.
 * <p>
 * This class is implemented to provide a default behavior of transferring a
 * component property simply by specifying the name of the property in the
 * constructor. For example, to transfer the foreground color from one component
 * to another either via the clipboard or a drag and drop operation a
 * <code>TransferHandler</code> can be constructed with the string "foreground".
 * The built in support will use the color returned by
 * <code>getForeground</code> as the source of the transfer, and
 * <code>setForeground</code> for the target of a transfer.
 * <p>
 * Please see
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/dnd.html"> How
 * to Use Drag and Drop and Data Transfer</a>, a section in <em>The Java
 * Tutorial</em>, for more information.
 *
 *
 * @author Timothy Prinzing
 * @author Shannon Hickey
 * @since 1.4
 */
@SuppressWarnings("serial")
public class TransferHandler implements Serializable {
	public static final int NONE = DnDConstants.ACTION_NONE;
	public static final int COPY = DnDConstants.ACTION_COPY;
	public static final int MOVE = DnDConstants.ACTION_MOVE;
	public static final int COPY_OR_MOVE = DnDConstants.ACTION_COPY_OR_MOVE;
	public static final int LINK = DnDConstants.ACTION_LINK;

	interface HasGetTransferHandler {
		public TransferHandler getTransferHandler();
	}

	public static class DropLocation {
		private final Point dropPoint;

		protected DropLocation(Point dropPoint) {
			if (dropPoint == null) {
				throw new IllegalArgumentException("Point cannot be null");
			}

			this.dropPoint = new Point(dropPoint);
		}

		public final Point getDropPoint() {
			return new Point(dropPoint);
		}

		public String toString() {
			return getClass().getName() + "[dropPoint=" + dropPoint + "]";
		}
	};

	public final static class TransferSupport {
		private boolean isDrop;
		private Component component;

		private boolean showDropLocationIsSet;
		private boolean showDropLocation;

		private int dropAction = -1;

		private Object source;

		private DropLocation dropLocation;

		private TransferSupport(Component component, DropTargetEvent event) {

			isDrop = true;
			setDNDVariables(component, event);
		}

		public TransferSupport(Component component, Transferable transferable) {
			isDrop = false;
			this.component = component;
			this.source = transferable;
		}

		private void setDNDVariables(Component component, DropTargetEvent event) {
		}

		public boolean isDrop() {
			return isDrop;
		}

		public Component getComponent() {
			return component;
		}

		private void assureIsDrop() {
		}

		public DropLocation getDropLocation() {
			return dropLocation;
		}

		public void setShowDropLocation(boolean showDropLocation) {
		}

		public void setDropAction(int dropAction) {
		}

		public int getDropAction() {
			return dropAction;
		}

		public int getUserDropAction() {
			return 0;
		}

		public int getSourceDropActions() {
			return 0;
		}

		public DataFlavor[] getDataFlavors() {
			return new DataFlavor[0];
		}

		public boolean isDataFlavorSupported(DataFlavor df) {
			return false;
		}

		public Transferable getTransferable() {
			return (Transferable) source;
		}
	}

	public static Action getCutAction() {
		return cutAction;
	}

	public static Action getCopyAction() {
		return copyAction;
	}

	public static Action getPasteAction() {
		return pasteAction;
	}

	public TransferHandler(String property) {
		propertyName = property;
	}

	protected TransferHandler() {
		this(null);
	}

	private Image dragImage;
	private Point dragImageOffset;

	public void setDragImage(Image img) {
		dragImage = img;
	}

	public Image getDragImage() {
		return dragImage;
	}

	public void setDragImageOffset(Point p) {
		dragImageOffset = new Point(p);
	}

	public Point getDragImageOffset() {
		if (dragImageOffset == null) {
			return new Point(0, 0);
		}
		return new Point(dragImageOffset);
	}

	public void exportAsDrag(JComponent comp, InputEvent e, int action) {
	}

	public void exportToClipboard(JComponent comp, Clipboard clip, int action)
			throws IllegalStateException {
	}

	public boolean importData(TransferSupport support) {
		return false;
	}

	public boolean importData(JComponent comp, Transferable t) {
		return false;
	}

	public boolean canImport(TransferSupport support) {
		return false;
	}

	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
		return false;
	}

	public int getSourceActions(JComponent c) {
		return NONE;
	}

	public Icon getVisualRepresentation(Transferable t) {
		return null;
	}

	protected Transferable createTransferable(JComponent c) {
		return null;
	}

	protected void exportDone(JComponent source, Transferable data, int action) {
	}

	private PropertyDescriptor getPropertyDescriptor(JComponent comp) {
		return null;
	}

	private DataFlavor getPropertyDataFlavor(Class<?> k, DataFlavor[] flavors) {
		return null;
	}

	private String propertyName;
	private static SwingDragGestureRecognizer recognizer = null;

	private static DropTargetListener getDropTargetListener() {
		return null;
	}

	static class PropertyTransferable implements Transferable {
		PropertyTransferable(PropertyDescriptor p, JComponent c) {
			property = p;
			component = c;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return null;
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return false;
		}

		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException, IOException {
			return null;
		}

		JComponent component;
		PropertyDescriptor property;
	}

	static class SwingDropTarget extends DropTarget implements UIResource {

		SwingDropTarget(Component c) {
			super(c, COPY_OR_MOVE | LINK, null);
			try {
				super.addDropTargetListener(getDropTargetListener());
			} catch (TooManyListenersException tmle) {
			}
		}

		public void addDropTargetListener(DropTargetListener dtl) throws TooManyListenersException {
			if (listenerList == null) {
				listenerList = new EventListenerList();
			}
			listenerList.add(DropTargetListener.class, dtl);
		}

		public void removeDropTargetListener(DropTargetListener dtl) {
			if (listenerList != null) {
				listenerList.remove(DropTargetListener.class, dtl);
			}
		}

		public void dragEnter(DropTargetDragEvent e) {
		}

		public void dragOver(DropTargetDragEvent e) {
		}

		public void dragExit(DropTargetEvent e) {
		}

		public void drop(DropTargetDropEvent e) {
		}

		public void dropActionChanged(DropTargetDragEvent e) {
		}

		private EventListenerList listenerList;
	}

	private static class DropHandler implements DropTargetListener, Serializable, ActionListener {

		private Point lastPosition;
		private Rectangle outer = new Rectangle();
		private Rectangle inner = new Rectangle();
		private int hysteresis = 10;

		private Component component;
		private Object state;
		private TransferSupport support = new TransferSupport(null, (DropTargetEvent) null);

		private static final int AUTOSCROLL_INSET = 10;

		private void updateAutoscrollRegion(JComponent c) {
		}

		private void autoscroll(JComponent c, Point pos) {
		}

		private void initPropertiesIfNecessary() {
		}

		public void actionPerformed(ActionEvent e) {
		}

		private void setComponentDropLocation(TransferSupport support, boolean forDrop) {
		}

		private void handleDrag(DropTargetDragEvent e) {
		}

		public void dragEnter(DropTargetDragEvent e) {
		}

		public void dragOver(DropTargetDragEvent e) {
		}

		public void dragExit(DropTargetEvent e) {
		}

		public void drop(DropTargetDropEvent e) {
		}

		public void dropActionChanged(DropTargetDragEvent e) {
		}

		private void cleanup(boolean forDrop) {
		}
	}

	private static class DragHandler implements DragGestureListener, DragSourceListener {
		private boolean scrolls;

		public void dragGestureRecognized(DragGestureEvent dge) {
		}

		public void dragEnter(DragSourceDragEvent dsde) {
		}

		public void dragOver(DragSourceDragEvent dsde) {
		}

		public void dragExit(DragSourceEvent dsde) {
		}

		public void dragDropEnd(DragSourceDropEvent dsde) {
		}

		public void dropActionChanged(DragSourceDragEvent dsde) {
		}
	}

	private static class SwingDragGestureRecognizer extends DragGestureRecognizer {

		SwingDragGestureRecognizer(DragGestureListener dgl) {
			super(DragSource.getDefaultDragSource(), null, NONE, dgl);
		}

		void gestured(JComponent c, MouseEvent e, int srcActions, int action) {
		}

		protected void registerListeners() {
		}

		protected void unregisterListeners() {
		}
	}

	static final Action cutAction = null;
	static final Action copyAction = null;
	static final Action pasteAction = null;

}