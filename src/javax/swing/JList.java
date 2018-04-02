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

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ListUI;
import javax.swing.text.Position;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import hc.android.HCRUtil;

/**
 * A component that displays a list of objects and allows the user to select one
 * or more items. A separate model, {@code ListModel}, maintains the contents of
 * the list.
 * <p>
 * It's easy to display an array or Vector of objects, using the {@code JList}
 * constructor that automatically builds a read-only {@code ListModel} instance
 * for you:
 * 
 * <pre>
 * {
 * 	&#64;code
 * 	// Create a JList that displays strings from an array
 *
 * 	String[] data = { "one", "two", "three", "four" };
 * 	JList<String> myList = new JList<String>(data);
 *
 * 	// Create a JList that displays the superclasses of JList.class, by
 * 	// creating it with a Vector populated with this data
 *
 * 	Vector<Class<?>> superClasses = new Vector<Class<?>>();
 * 	Class<JList> rootClass = javax.swing.JList.class;
 * 	for (Class<?> cls = rootClass; cls != null; cls = cls.getSuperclass()) {
 * 		superClasses.addElement(cls);
 * 	}
 * 	JList<Class<?>> myList = new JList<Class<?>>(superClasses);
 *
 * 	// The automatically created model is stored in JList's "model"
 * 	// property, which you can retrieve
 *
 * 	ListModel<Class<?>> model = myList.getModel();
 * 	for (int i = 0; i < model.getSize(); i++) {
 * 		System.out.println(model.getElementAt(i));
 * 	}
 * }
 * </pre>
 * <p>
 * A {@code ListModel} can be supplied directly to a {@code JList} by way of a
 * constructor or the {@code setModel} method. The contents need not be static -
 * the number of items, and the values of items can change over time. A correct
 * {@code ListModel} implementation notifies the set of
 * {@code javax.swing.event.ListDataListener}s that have been added to it, each
 * time a change occurs. These changes are characterized by a
 * {@code javax.swing.event.ListDataEvent}, which identifies the range of list
 * indices that have been modified, added, or removed. {@code JList}'s
 * {@code ListUI} is responsible for keeping the visual representation up to
 * date with changes, by listening to the model.
 * <p>
 * Simple, dynamic-content, {@code JList} applications can use the
 * {@code DefaultListModel} class to maintain list elements. This class
 * implements the {@code ListModel} interface and also provides a
 * <code>java.util.Vector</code>-like API. Applications that need a more custom
 * <code>ListModel</code> implementation may instead wish to subclass
 * {@code AbstractListModel}, which provides basic support for managing and
 * notifying listeners. For example, a read-only implementation of
 * {@code AbstractListModel}:
 * 
 * <pre>
 * {
 * 	&#64;code
 * 	// This list model has about 2^16 elements. Enjoy scrolling.
 *
 * 	ListModel<String> bigData = new AbstractListModel<String>() {
 * 		public int getSize() {
 * 			return Short.MAX_VALUE;
 * 		}
 * 
 * 		public String getElementAt(int index) {
 * 			return "Index " + index;
 * 		}
 * 	};
 * }
 * </pre>
 * <p>
 * The selection state of a {@code JList} is managed by another separate model,
 * an instance of {@code ListSelectionModel}. {@code JList} is initialized with
 * a selection model on construction, and also contains methods to query or set
 * this selection model. Additionally, {@code JList} provides convenient methods
 * for easily managing the selection. These methods, such as
 * {@code setSelectedIndex} and {@code getSelectedValue}, are cover methods that
 * take care of the details of interacting with the selection model. By default,
 * {@code JList}'s selection model is configured to allow any combination of
 * items to be selected at a time; selection mode
 * {@code MULTIPLE_INTERVAL_SELECTION}. The selection mode can be changed on the
 * selection model directly, or via {@code JList}'s cover method. Responsibility
 * for updating the selection model in response to user gestures lies with the
 * list's {@code ListUI}.
 * <p>
 * A correct {@code ListSelectionModel} implementation notifies the set of
 * {@code javax.swing.event.ListSelectionListener}s that have been added to it
 * each time a change to the selection occurs. These changes are characterized
 * by a {@code javax.swing.event.ListSelectionEvent}, which identifies the range
 * of the selection change.
 * <p>
 * The preferred way to listen for changes in list selection is to add
 * {@code ListSelectionListener}s directly to the {@code JList}. {@code JList}
 * then takes care of listening to the the selection model and notifying your
 * listeners of change.
 * <p>
 * Responsibility for listening to selection changes in order to keep the list's
 * visual representation up to date lies with the list's {@code ListUI}.
 * <p>
 * <a name="renderer"> Painting of cells in a {@code JList} is handled by a
 * delegate called a cell renderer, installed on the list as the
 * {@code cellRenderer} property. The renderer provides a
 * {@code java.awt.Component} that is used like a "rubber stamp" to paint the
 * cells. Each time a cell needs to be painted, the list's {@code ListUI} asks
 * the cell renderer for the component, moves it into place, and has it paint
 * the contents of the cell by way of its {@code paint} method. A default cell
 * renderer, which uses a {@code JLabel} component to render, is installed by
 * the lists's {@code ListUI}. You can substitute your own renderer using code
 * like this:
 * 
 * <pre>
 * {
 * 	&#64;code
 * 	// Display an icon and a string for each object in the list.
 *
 * 	class MyCellRenderer extends JLabel implements ListCellRenderer<Object> {
 * 		final static ImageIcon longIcon = new ImageIcon("long.gif");
 * 		final static ImageIcon shortIcon = new ImageIcon("short.gif");
 *
 * 		// This is the only method defined by ListCellRenderer.
 * 		// We just reconfigure the JLabel each time we're called.
 *
 * 		public Component getListCellRendererComponent(JList<?> list, // the
 * 																		// list
 * 				Object value, // value to display
 * 				int index, // cell index
 * 				boolean isSelected, // is the cell selected
 * 				boolean cellHasFocus) // does the cell have focus
 * 		{
 * 			String s = value.toString();
 * 			setText(s);
 * 			setIcon((s.length() > 10) ? longIcon : shortIcon);
 * 			if (isSelected) {
 * 				setBackground(list.getSelectionBackground());
 * 				setForeground(list.getSelectionForeground());
 * 			} else {
 * 				setBackground(list.getBackground());
 * 				setForeground(list.getForeground());
 * 			}
 * 			setEnabled(list.isEnabled());
 * 			setFont(list.getFont());
 * 			setOpaque(true);
 * 			return this;
 * 		}
 * 	}
 *
 * 	myList.setCellRenderer(new MyCellRenderer());
 * }
 * </pre>
 * <p>
 * Another job for the cell renderer is in helping to determine sizing
 * information for the list. By default, the list's {@code ListUI} determines
 * the size of cells by asking the cell renderer for its preferred size for each
 * list item. This can be expensive for large lists of items. To avoid these
 * calculations, you can set a {@code fixedCellWidth} and
 * {@code fixedCellHeight} on the list, or have these values calculated
 * automatically based on a single prototype value: <a name="prototype_example">
 * 
 * <pre>
 * {
 * 	&#64;code
 * 	JList<String> bigDataList = new JList<String>(bigData);
 *
 * 	// We don't want the JList implementation to compute the width
 * 	// or height of all of the list cells, so we give it a string
 * 	// that's as big as we'll need for any cell. It uses this to
 * 	// compute values for the fixedCellWidth and fixedCellHeight
 * 	// properties.
 *
 * 	bigDataList.setPrototypeCellValue("Index 1234567890");
 * }
 * </pre>
 * <p>
 * {@code JList} doesn't implement scrolling directly. To create a list that
 * scrolls, make it the viewport view of a {@code JScrollPane}. For example:
 * 
 * <pre>
 * JScrollPane scrollPane = new JScrollPane(myList);
 *
 * // Or in two steps:
 * JScrollPane scrollPane = new JScrollPane();
 * scrollPane.getViewport().setView(myList);
 * </pre>
 * <p>
 * {@code JList} doesn't provide any special handling of double or triple (or N)
 * mouse clicks, but it's easy to add a {@code MouseListener} if you wish to
 * take action on these events. Use the {@code locationToIndex} method to
 * determine what cell was clicked. For example:
 * 
 * <pre>
 * MouseListener mouseListener = new MouseAdapter() {
 * 	public void mouseClicked(MouseEvent e) {
 * 		if (e.getClickCount() == 2) {
 * 			int index = list.locationToIndex(e.getPoint());
 * 			System.out.println("Double clicked on Item " + index);
 * 		}
 * 	}
 * };
 * list.addMouseListener(mouseListener);
 * </pre>
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
 * <p>
 * See <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/list.html">How to
 * Use Lists</a> in
 * <a href="http://java.sun.com/Series/Tutorial/index.html"><em>The Java
 * Tutorial</em></a> for further documentation. Also see the article <a href=
 * "http://java.sun.com/products/jfc/tsc/tech_topics/jlist_1/jlist.html">Advanced
 * JList Programming</a> in
 * <a href="http://java.sun.com/products/jfc/tsc"><em>The Swing
 * Connection</em></a>.
 * <p>
 * 
 * @see ListModel
 * @see AbstractListModel
 * @see DefaultListModel
 * @see ListSelectionModel
 * @see DefaultListSelectionModel
 * @see ListCellRenderer
 * @see DefaultListCellRenderer
 *
 * @param <E>
 *            the type of the elements of this list
 *
 * @beaninfo attribute: isContainer false description: A component which allows
 *           for the selection of one or more objects from a list.
 *
 * @author Hans Muller
 */
public class JList<E> extends JComponent implements Scrollable, Accessible {
	private static final String uiClassID = "ListUI";
	private ListView listView;

	public static final int VERTICAL = 0;
	public static final int VERTICAL_WRAP = 1;
	public static final int HORIZONTAL_WRAP = 2;

	private int fixedCellWidth = -1;
	private int fixedCellHeight = -1;
	private E prototypeCellValue;
	private int visibleRowCount = 8;
	private Color selectionForeground;
	private Color selectionBackground;
	private boolean dragEnabled;

	private ListSelectionModel selectionModel;
	private ListModel<E> dataModel;
	private BaseAdapter listAdapter;
	private ListCellRenderer<? super E> cellRenderer;
	private ListSelectionListener selectionListener;

	private int layoutOrientation;

	private DropMode dropMode = DropMode.USE_SELECTION;
	private transient DropLocation dropLocation;

	public static final class DropLocation extends TransferHandler.DropLocation {
		private final int index;
		private final boolean isInsert;

		private DropLocation(Point p, int index, boolean isInsert) {
			super(p);
			this.index = index;
			this.isInsert = isInsert;
		}

		public int getIndex() {
			return index;
		}

		public boolean isInsert() {
			return isInsert;
		}

		public String toString() {
			return getClass().getName() + "[dropPoint=" + getDropPoint() + "," + "index=" + index
					+ "," + "insert=" + isInsert + "]";
		}
	}

	public JList(ListModel<E> dataModel) {
		if (dataModel == null) {
			throw new IllegalArgumentException("dataModel must be non null");
		}
		listView = new ListView(ActivityManager.applicationContext);
		// scrollView = new ScrollView(ActivityManager.applicationContext);

		this.dataModel = dataModel;
		// {
		// LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// scrollView.addView(listView, lp);
		// }
		setPeerAdAPI(listView);
		addOnLayoutChangeListenerAdAPI(listView);

		// listView.setSelected(true);
		listView.setItemsCanFocus(true);
		AndroidUIUtil.buildListenersForComponent(listView, JList.this);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position < getModel().getSize() && position >= 0) {
					if (JList.this.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION) {
						JList.this.setSelectedIndex(position);// (position,
																// position);
					} else {
						if (JList.this.isSelectedIndex(position)) {
							JList.this.removeSelectionInterval(position, position);
						} else {
							JList.this.addSelectionInterval(position, position);
						}
					}
				}
			}
		});
		listAdapter = new BaseAdapter() {
			private LayoutInflater mInflater = (LayoutInflater) ActivityManager.applicationContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (JList.this.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION) {
					return createViewFromResource(position, convertView, parent,
							HCRUtil.getResource(HCRUtil.R_layout_simple_list_item_single_choice));
				} else {
					return createViewFromResource(position, convertView, parent,
							HCRUtil.getResource(HCRUtil.R_layout_simple_list_item_multiple_choice));
				}
			}

			private View createViewFromResource(int position, View convertView, ViewGroup parent,
					int resource) {
				View view;
				CheckedTextView text;
				if (convertView == null) {
					view = mInflater.inflate(resource, parent, false);
				} else {
					view = convertView;
				}
				try {
					int mFieldId = 0;
					if (mFieldId == 0) {
						text = (CheckedTextView) view;
					} else {
						text = (CheckedTextView) view.findViewById(mFieldId);
					}
				} catch (ClassCastException e) {
					Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
					throw new IllegalStateException(
							"ArrayAdapter requires the resource ID to be a TextView", e);
				}
				Object item = getItem(position);
				if (item == null) {
					item = "";
				}
				if (item instanceof CharSequence) {
					text.setText((CharSequence) item);
				} else {
					text.setText(item.toString());
				}
				// System.out.println("getView : " + JList.this.toString() + ",
				// at Indx : " + position + ", isSelected : " +
				// JList.this.selectionModel.isSelectedIndex(position));
				// text.setChecked(JList.this.selectionModel.isSelectedIndex(position));
				return view;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return JList.this.dataModel.getElementAt(position);
			}

			@Override
			public int getCount() {
				return JList.this.dataModel.getSize();
			}
		};
		listView.setAdapter(listAdapter);
		setSelectionModel(createSelectionModel());// 须初始化listAdapter之后

		layoutOrientation = VERTICAL;

		setSelectionMode(getSelectionMode());
		// if(selectionModel.getSelectionMode() !=
		// ListSelectionModel.SINGLE_SELECTION){
		// if(dataModel.getSize() > 0){
		// listView.setItemChecked(0, true);
		// listView.setItemChecked(0, false);
		// }
		// }
		listAdapter.notifyDataSetChanged();
		// setAutoscrolls(true);
		// setOpaque(true);
		updateUI();
	}

	public View getFocusablePeerViewAdAPI() {
		return listView;
	}

	public JList(final E[] listData) {
		this(new AbstractListModel<E>() {
			public int getSize() {
				return listData.length;
			}

			public E getElementAt(int i) {
				return listData[i];
			}
		});
	}

	public JList(final Vector<? extends E> listData) {
		this(new AbstractListModel<E>() {
			public int getSize() {
				return listData.size();
			}

			public E getElementAt(int i) {
				return listData.elementAt(i);
			}
		});
	}

	public JList() {
		this(new AbstractListModel<E>() {
			public int getSize() {
				return 0;
			}

			public E getElementAt(int i) {
				throw new IndexOutOfBoundsException("No Data Model");
			}
		});
	}

	public void applyComponentOrientation(ComponentOrientation o) {
		super.applyComponentOrientation(o);
		// CheckBox snap = null;//TODO ########################################
		// if(snap != null){
		// snap.setGravity(o.isLeftToRight()?Gravity.LEFT:Gravity.RIGHT);
		// }
	}

	public ListUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setUI(ListUI ui) {
		AndroidClassUtil.callEmptyMethod();
		super.setUI(ui);
	}

	public void updateUI() {
		AndroidClassUtil.callEmptyMethod();
		// setUI((ListUI) UIManager.getUI(this));
		//
		// ListCellRenderer<? super E> renderer = getCellRenderer();
		// if (renderer instanceof Component) {
		// SwingUtilities.updateComponentTreeUI((Component) renderer);
		// }
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public E getPrototypeCellValue() {
		return prototypeCellValue;
	}

	public void setPrototypeCellValue(E prototypeCellValue) {
		this.prototypeCellValue = prototypeCellValue;
		AndroidClassUtil.callEmptyMethod();
	}

	public int getFixedCellWidth() {
		return fixedCellWidth;
	}

	public void setFixedCellWidth(int width) {
		fixedCellWidth = width;
		AndroidClassUtil.callEmptyMethod();
	}

	public int getFixedCellHeight() {
		return fixedCellHeight;
	}

	public void setFixedCellHeight(int height) {
		fixedCellHeight = height;
		AndroidClassUtil.callEmptyMethod();
	}

	@Transient
	public ListCellRenderer<? super E> getCellRenderer() {
		return cellRenderer;
	}

	public void setCellRenderer(ListCellRenderer<? super E> cellRenderer) {
		this.cellRenderer = cellRenderer;
		AndroidClassUtil.callEmptyMethod();
	}

	public Color getSelectionForeground() {
		return selectionForeground;
	}

	public void setSelectionForeground(Color selectionForeground) {
		this.selectionForeground = selectionForeground;
		AndroidClassUtil.callEmptyMethod();
	}

	public Color getSelectionBackground() {
		return selectionBackground;
	}

	public void setSelectionBackground(Color selectionBackground) {
		this.selectionBackground = selectionBackground;
		AndroidClassUtil.callEmptyMethod();
	}

	public int getVisibleRowCount() {
		return visibleRowCount;
	}

	public void setVisibleRowCount(int visibleRowCount) {
		this.visibleRowCount = Math.max(0, visibleRowCount);
		AndroidClassUtil.callEmptyMethod();
	}

	public int getLayoutOrientation() {
		return layoutOrientation;
	}

	public void setLayoutOrientation(int layoutOrientation) {
		switch (layoutOrientation) {
		case VERTICAL:
		case VERTICAL_WRAP:
		case HORIZONTAL_WRAP:
			this.layoutOrientation = layoutOrientation;
			AndroidClassUtil.callEmptyMethod();
			break;
		default:
			throw new IllegalArgumentException(
					"layoutOrientation must be one of: VERTICAL, HORIZONTAL_WRAP or VERTICAL_WRAP");
		}
	}

	public int getFirstVisibleIndex() {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public int getLastVisibleIndex() {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public void ensureIndexIsVisible(int index) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setDragEnabled(boolean b) {
		dragEnabled = b;
	}

	public boolean getDragEnabled() {
		return dragEnabled;
	}

	public final void setDropMode(DropMode dropMode) {
		if (dropMode != null) {
			switch (dropMode) {
			case USE_SELECTION:
			case ON:
			case INSERT:
			case ON_OR_INSERT:
				this.dropMode = dropMode;
				return;
			}
		}

		throw new IllegalArgumentException(dropMode + ": Unsupported drop mode for list");
	}

	public final DropMode getDropMode() {
		return dropMode;
	}

	DropLocation dropLocationForPoint(Point p) {
		AndroidClassUtil.callEmptyMethod();
		return new DropLocation(null, 0, false);
	}

	Object setDropLocation(TransferHandler.DropLocation location, Object state, boolean forDrop) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public final DropLocation getDropLocation() {
		return dropLocation;
	}

	public int getNextMatch(String prefix, int startIndex, Position.Bias bias) {
		AndroidClassUtil.callEmptyMethod();
		return -1;
	}

	public String getToolTipText(MouseEvent event) {
		return super.getToolTipText();
	}

	public int locationToIndex(Point location) {
		AndroidClassUtil.callEmptyMethod();
		return -1;
	}

	public Point indexToLocation(int index) {
		AndroidClassUtil.callEmptyMethod();
		return new Point(0, 0);
	}

	public Rectangle getCellBounds(int index0, int index1) {
		AndroidClassUtil.callEmptyMethod();
		return new Rectangle(0, 0);
	}

	public ListModel<E> getModel() {
		return dataModel;
	}

	public void setModel(final ListModel<E> model) {
		if (model == null) {
			throw new IllegalArgumentException("model must be non null");
		}
		dataModel = model;
		clearSelection();
	}

	public void setListData(final E[] listData) {
		setModel(new AbstractListModel<E>() {
			public int getSize() {
				return listData.length;
			}

			public E getElementAt(int i) {
				return listData[i];
			}
		});
	}

	public void setListData(final Vector<? extends E> listData) {
		setModel(new AbstractListModel<E>() {
			public int getSize() {
				return listData.size();
			}

			public E getElementAt(int i) {
				return listData.elementAt(i);
			}
		});
	}

	protected ListSelectionModel createSelectionModel() {
		return new DefaultListSelectionModel();
	}

	public ListSelectionModel getSelectionModel() {
		return selectionModel;
	}

	protected void fireSelectionValueChanged(int firstIndex, int lastIndex, boolean isAdjusting) {
		Object[] listeners = list.getListenerList();
		ListSelectionEvent e = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ListSelectionListener.class) {
				if (e == null) {
					e = new ListSelectionEvent(this, firstIndex, lastIndex, isAdjusting);
				}
				((ListSelectionListener) listeners[i + 1]).valueChanged(e);
			}
		}
	}

	private class ListSelectionHandler implements ListSelectionListener, Serializable {
		public void valueChanged(ListSelectionEvent e) {
			fireSelectionValueChanged(e.getFirstIndex(), e.getLastIndex(), e.getValueIsAdjusting());
		}
	}

	public void addListSelectionListener(ListSelectionListener listener) {
		if (selectionListener == null) {
			selectionListener = new ListSelectionHandler();
			getSelectionModel().addListSelectionListener(selectionListener);
		}

		list.add(ListSelectionListener.class, listener);
	}

	public void removeListSelectionListener(ListSelectionListener listener) {
		list.remove(ListSelectionListener.class, listener);
	}

	public ListSelectionListener[] getListSelectionListeners() {
		return list.getListeners(ListSelectionListener.class);
	}

	public void setSelectionModel(ListSelectionModel selectionModel) {
		if (selectionModel == null) {
			throw new IllegalArgumentException("selectionModel must be non null");
		}

		if (selectionListener != null) {
			this.selectionModel.removeListSelectionListener(selectionListener);
			selectionModel.addListSelectionListener(selectionListener);
		}

		if (selectionModel instanceof JList.WrapListSelectionModel) {
			this.selectionModel = selectionModel;
		} else {
			ListSelectionModel wrap = new WrapListSelectionModel(selectionModel);
			this.selectionModel = wrap;
		}
		setSelectionMode(getSelectionMode());
	}

	public void setSelectionMode(int selectionMode) {
		if (ListSelectionModel.SINGLE_SELECTION == selectionMode) {
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		} else {
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		}
		getSelectionModel().setSelectionMode(selectionMode);
	}

	public int getSelectionMode() {
		return getSelectionModel().getSelectionMode();
	}

	public int getAnchorSelectionIndex() {
		return getSelectionModel().getAnchorSelectionIndex();
	}

	public int getLeadSelectionIndex() {
		return getSelectionModel().getLeadSelectionIndex();
	}

	public int getMinSelectionIndex() {
		return getSelectionModel().getMinSelectionIndex();
	}

	public int getMaxSelectionIndex() {
		return getSelectionModel().getMaxSelectionIndex();
	}

	public boolean isSelectedIndex(int index) {
		return getSelectionModel().isSelectedIndex(index);
	}

	public boolean isSelectionEmpty() {
		return getSelectionModel().isSelectionEmpty();
	}

	public void clearSelection() {
		getSelectionModel().clearSelection();
	}

	public void setSelectionInterval(int anchor, int lead) {
		getSelectionModel().setSelectionInterval(anchor, lead);
	}

	public void addSelectionInterval(int anchor, int lead) {
		getSelectionModel().addSelectionInterval(anchor, lead);
	}

	public void removeSelectionInterval(int index0, int index1) {
		getSelectionModel().removeSelectionInterval(index0, index1);
	}

	public void setValueIsAdjusting(boolean b) {
		getSelectionModel().setValueIsAdjusting(b);
	}

	public boolean getValueIsAdjusting() {
		return getSelectionModel().getValueIsAdjusting();
	}

	@Transient
	public int[] getSelectedIndices() {
		ListSelectionModel sm = getSelectionModel();
		int iMin = sm.getMinSelectionIndex();
		int iMax = sm.getMaxSelectionIndex();

		if ((iMin < 0) || (iMax < 0)) {
			return new int[0];
		}

		int[] rvTmp = new int[1 + (iMax - iMin)];
		int n = 0;
		for (int i = iMin; i <= iMax; i++) {
			if (sm.isSelectedIndex(i)) {
				rvTmp[n++] = i;
			}
		}
		int[] rv = new int[n];
		System.arraycopy(rvTmp, 0, rv, 0, n);
		return rv;
	}

	public void setSelectedIndex(int index) {
		if (index >= getModel().getSize()) {
			return;
		}
		getSelectionModel().setSelectionInterval(index, index);
	}

	public void setSelectedIndices(int[] indices) {
		ListSelectionModel sm = getSelectionModel();
		sm.clearSelection();
		int size = getModel().getSize();
		for (int i : indices) {
			if (i < size) {
				sm.addSelectionInterval(i, i);
			}
		}
	}

	@Deprecated
	public Object[] getSelectedValues() {
		ListSelectionModel sm = getSelectionModel();
		ListModel<E> dm = getModel();

		int iMin = sm.getMinSelectionIndex();
		int iMax = sm.getMaxSelectionIndex();

		if ((iMin < 0) || (iMax < 0)) {
			return new Object[0];
		}

		Object[] rvTmp = new Object[1 + (iMax - iMin)];
		int n = 0;
		for (int i = iMin; i <= iMax; i++) {
			if (sm.isSelectedIndex(i)) {
				rvTmp[n++] = dm.getElementAt(i);
			}
		}
		Object[] rv = new Object[n];
		System.arraycopy(rvTmp, 0, rv, 0, n);
		return rv;
	}

	public List<E> getSelectedValuesList() {
		ListSelectionModel sm = getSelectionModel();
		ListModel<E> dm = getModel();

		int iMin = sm.getMinSelectionIndex();
		int iMax = sm.getMaxSelectionIndex();

		if ((iMin < 0) || (iMax < 0)) {
			return Collections.emptyList();
		}

		List<E> selectedItems = new ArrayList<E>();
		for (int i = iMin; i <= iMax; i++) {
			if (sm.isSelectedIndex(i)) {
				selectedItems.add(dm.getElementAt(i));
			}
		}
		return selectedItems;
	}

	public int getSelectedIndex() {
		return getMinSelectionIndex();
	}

	public E getSelectedValue() {
		int i = getMinSelectionIndex();
		return (i == -1) ? null : getModel().getElementAt(i);
	}

	public void setSelectedValue(Object anObject, boolean shouldScroll) {
		if (anObject == null)
			setSelectedIndex(-1);
		else if (!anObject.equals(getSelectedValue())) {
			int i, c;
			ListModel<E> dm = getModel();
			for (i = 0, c = dm.getSize(); i < c; i++)
				if (anObject.equals(dm.getElementAt(i))) {
					setSelectedIndex(i);
					if (shouldScroll)
						ensureIndexIsVisible(i);
					repaint();
					return;
				}
			setSelectedIndex(-1);
		}
		repaint();
	}

	private void checkScrollableParameters(Rectangle visibleRect, int orientation) {
		if (visibleRect == null) {
			throw new IllegalArgumentException("visibleRect must be non-null");
		}
		switch (orientation) {
		case SwingConstants.VERTICAL:
		case SwingConstants.HORIZONTAL:
			break;
		default:
			throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
		}
	}

	public Dimension getPreferredScrollableViewportSize() {
		AndroidClassUtil.callEmptyMethod();
		return getPreferredSize();
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		AndroidClassUtil.callEmptyMethod();
		return 1;
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public boolean getScrollableTracksViewportWidth() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public boolean getScrollableTracksViewportHeight() {
		AndroidClassUtil.callEmptyMethod();
		return false;
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

	class WrapListSelectionModel implements ListSelectionModel {
		final ListSelectionModel base;

		WrapListSelectionModel(ListSelectionModel base) {
			this.base = base;
		}

		private void synToListView() {
			if (selectionModel.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION) {
				listView.setItemChecked(selectionModel.getMinSelectionIndex(), true);
			} else {
				int[] indexs = getSelectedIndices();
				listView.clearChoices();
				for (int i = 0; i < indexs.length; i++) {
					listView.setItemChecked(indexs[i], true);
				}
			}
		}

		@Override
		public void setSelectionInterval(int index0, int index1) {
			base.setSelectionInterval(index0, index1);
			synToListView();
			// listAdapter.notifyDataSetChanged();
		}

		@Override
		public void addSelectionInterval(int index0, int index1) {
			base.addSelectionInterval(index0, index1);
			synToListView();
		}

		@Override
		public void removeSelectionInterval(int index0, int index1) {
			base.removeSelectionInterval(index0, index1);
			synToListView();
		}

		@Override
		public int getMinSelectionIndex() {
			return base.getMinSelectionIndex();
		}

		@Override
		public int getMaxSelectionIndex() {
			return base.getMaxSelectionIndex();
		}

		@Override
		public boolean isSelectedIndex(int index) {
			return base.isSelectedIndex(index);
		}

		@Override
		public int getAnchorSelectionIndex() {
			return base.getAnchorSelectionIndex();
		}

		@Override
		public void setAnchorSelectionIndex(int index) {
			base.setAnchorSelectionIndex(index);
			synToListView();
		}

		@Override
		public int getLeadSelectionIndex() {
			return base.getLeadSelectionIndex();
		}

		@Override
		public void setLeadSelectionIndex(int index) {
			base.setLeadSelectionIndex(index);
			synToListView();
		}

		@Override
		public void clearSelection() {
			base.clearSelection();
			synToListView();
		}

		@Override
		public boolean isSelectionEmpty() {
			return base.isSelectionEmpty();
		}

		@Override
		public void insertIndexInterval(int index, int length, boolean before) {
			base.insertIndexInterval(index, length, before);
			synToListView();
		}

		@Override
		public void removeIndexInterval(int index0, int index1) {
			base.removeIndexInterval(index0, index1);
			synToListView();
		}

		@Override
		public void setValueIsAdjusting(boolean valueIsAdjusting) {
			base.setValueIsAdjusting(valueIsAdjusting);
		}

		@Override
		public boolean getValueIsAdjusting() {
			return base.getValueIsAdjusting();
		}

		@Override
		public void setSelectionMode(int selectionMode) {
			base.setSelectionMode(selectionMode);
			listAdapter.notifyDataSetInvalidated();
		}

		@Override
		public int getSelectionMode() {
			return base.getSelectionMode();
		}

		@Override
		public void addListSelectionListener(ListSelectionListener x) {
			base.addListSelectionListener(x);
		}

		@Override
		public void removeListSelectionListener(ListSelectionListener x) {
			base.removeListSelectionListener(x);
		}
	}
}
