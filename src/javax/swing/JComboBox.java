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
import hc.android.JHCComponent;
import hc.android.UICore;
import hc.android.AndroidUIUtil;

import java.awt.AWTEvent;
import java.awt.ComponentOrientation;
import java.awt.EventQueue;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComboBoxUI;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import hc.android.HCRUtil;

/**
 * A component that combines a button or editable field and a drop-down list.
 * The user can select a value from the drop-down list, which appears at the
 * user's request. If you make the combo box editable, then the combo box
 * includes an editable field into which the user can type a value.
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
 * <p>
 * See <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/combobox.html">How
 * to Use Combo Boxes</a> in
 * <a href="http://java.sun.com/Series/Tutorial/index.html"><em>The Java
 * Tutorial</em></a> for further information.
 * <p>
 * 
 * @see ComboBoxModel
 * @see DefaultComboBoxModel
 *
 * @param <E>
 *            the type of the elements of this combo box
 *
 * @beaninfo attribute: isContainer false description: A combination of a text
 *           field and a drop-down list.
 *
 * @author Arnaud Weber
 * @author Mark Davidson
 */
public class JComboBox<E> extends JHCComponent
		implements ItemSelectable, ListDataListener, ActionListener, Accessible {
	private Spinner spinner;
	private static final String uiClassID = "ComboBoxUI";
	private final DataSetObservable mDataSetObservable = new DataSetObservable();

	public void applyComponentOrientation(ComponentOrientation o) {
		super.applyComponentOrientation(o);
	}

	protected ComboBoxModel<E> dataModel;
	protected ListCellRenderer<? super E> renderer;
	protected ComboBoxEditor editor;
	protected int maximumRowCount = 8;
	protected boolean isEditable = false;
	protected KeySelectionManager keySelectionManager = null;
	protected boolean lightWeightPopupEnabled = false;
	protected Object selectedItemReminder = null;
	private E prototypeDisplayValue;
	private boolean firingActionEvent = false;
	private boolean selectingItem = false;

	public JComboBox(ComboBoxModel<E> aModel) {
		super();
		setModel(aModel);
	}

	public JComboBox(E[] items) {
		super();
		setModel(new DefaultComboBoxModel<E>(items));
	}

	public JComboBox(Vector<E> items) {
		super();
		setModel(new DefaultComboBoxModel<E>(items));
	}

	public JComboBox() {
		super();
		setModel(new DefaultComboBoxModel<E>());
	}

	@Override
	public View getPeerAdAPI() {
		if (spinner == null) {
			AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
				@Override
				public void run() {
					initSpinner();
				}
			});
		}

		return super.getPeerAdAPI();
	}

	public View getFocusablePeerViewAdAPI() {
		return spinner;
	}

	private void initSpinner() {
		spinner = new Spinner(ActivityManager.applicationContext);
		setPeerAdAPI(spinner);
		addOnLayoutChangeListenerAdAPI(spinner);

		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position < getItemCount() && position >= 0) {// Android初始为position=0,而J2SE允许集合为0
					JComboBox.this.setSelectedIndex(position);
				}
				// System.out.println("spinner onItemSelected at position : " +
				// position + ", id : " + id);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// System.out.println("onNothingSelected");
			}
		});
		AndroidUIUtil.buildListenersForComponent(spinner, this);
		spinner.setAdapter(new SpinnerAdapter() {
			private LayoutInflater mInflater = (LayoutInflater) ActivityManager.applicationContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			@Override
			public void unregisterDataSetObserver(DataSetObserver observer) {
				mDataSetObservable.unregisterObserver(observer);
			}

			@Override
			public void registerDataSetObserver(DataSetObserver observer) {
				mDataSetObservable.registerObserver(observer);
			}

			@Override
			public boolean isEmpty() {
				return getCount() == 0;
			}

			@Override
			public boolean hasStableIds() {
				return false;
			}

			private final int TYPE_STRING = 0;
			private final int TYPE_ICON = 1;

			@Override
			public int getItemViewType(int position) {
				if (dataModel.getSize() > position) {
					Object out = dataModel.getElementAt(position);
					if (out != null && out instanceof Icon) {
						return TYPE_ICON;
					}
				}
				return TYPE_STRING;
			}

			@Override
			public int getViewTypeCount() {
				// 注意：
				// 05-18 11:57:21.581: E/HomeCenter(29594):
				// java.lang.IllegalArgumentException: Spinner adapter view type
				// count must be 1
				// This error is being reported for targetSdkVersion of 21.
				return 1;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (getItemViewType(position) == TYPE_ICON) {
					return createIconViewFromResource(position, convertView, parent,
							HCRUtil.getResource(HCRUtil.R_layout_simple_spinner_image_item));
				} else {
					return createViewFromResource(position, convertView, parent,
							HCRUtil.getResource(HCRUtil.R_layout_simple_spinner_item));
				}
				// if(convertView instanceof TextView){
				// return convertView;
				// }else{
				// TextView textView = new
				// TextView(ActivityManager.applicationContext);
				// textView.setTextColor(JComboBox.this.isEnable?UIUtil.WIN_FONT_COLOR.toAndroid():UIUtil.WIN_FONT_DISABLE_COLOR.toAndroid());
				// textViewUIUtil.setTextSize(JComboBox.this.font.getSize());
				//
				// return textView;
				// }
			}

			private View createIconViewFromResource(int position, View convertView,
					ViewGroup parent, int resource) {
				View view;
				ImageView bitmapView;
				if (convertView == null) {
					view = mInflater.inflate(resource, parent, false);
				} else {
					view = convertView;
				}
				try {
					int mFieldId = 0;
					if (mFieldId == 0) {
						bitmapView = (ImageView) view;
					} else {
						bitmapView = (ImageView) view.findViewById(mFieldId);
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
				if (item instanceof Icon) {
					bitmapView.setImageDrawable(ImageIcon
							.getAdapterBitmapDrawableAdAPI((ImageIcon) item, JComboBox.this));
				}
				return view;
			}

			private View createViewFromResource(int position, View convertView, ViewGroup parent,
					int resource) {
				View view;
				TextView text;
				if (convertView == null) {
					view = mInflater.inflate(resource, parent, false);
				} else {
					view = convertView;
				}
				try {
					int mFieldId = 0;
					if (mFieldId == 0) {
						text = (TextView) view;
					} else {
						text = (TextView) view.findViewById(mFieldId);
					}
					UICore.setTextSize(text, JComboBox.this.getFont(),
							JComboBox.this.getScreenAdapterAdAPI());
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
				return view;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return dataModel.getElementAt(position);
			}

			@Override
			public int getCount() {
				return dataModel.getSize();
			}

			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent) {
				if (getItemViewType(position) == TYPE_ICON) {
					return createIconViewFromResource(position, convertView, parent, HCRUtil
							.getResource(HCRUtil.R_layout_simple_spinner_dropdown_image_item));
				} else {
					return createViewFromResource(position, convertView, parent,
							HCRUtil.getResource(HCRUtil.R_layout_simple_spinner_dropdown_item));
				}
			}
		});

		actionCommand = "comboBoxChanged";
		// installAncestorListener();
		// setUIProperty("opaque", true);
		updateUI();
	}

	protected void installAncestorListener() {
		addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event) {
				hidePopup();
			}

			public void ancestorRemoved(AncestorEvent event) {
				hidePopup();
			}

			public void ancestorMoved(AncestorEvent event) {
				if (event.getSource() != JComboBox.this)
					hidePopup();
			}
		});
	}

	public void setUI(ComboBoxUI ui) {
		// super.setUI(ui);
		AndroidClassUtil.callEmptyMethod();
	}

	public void updateUI() {
		// spinner.postInvalidate();
		// AndroidClassUtil.callEmptyMethod();
		// setUI((ComboBoxUI) UIManager.getUI(this));
		//
		// ListCellRenderer<? super E> renderer = getRenderer();
		// if (renderer instanceof Component) {
		// SwingUtilities.updateComponentTreeUI((Component) renderer);
		// }
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public ComboBoxUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return (ComboBoxUI) null;
	}

	public void setModel(ComboBoxModel<E> aModel) {
		ComboBoxModel<E> oldModel = dataModel;
		if (oldModel != null) {
			oldModel.removeListDataListener(this);
		}
		dataModel = aModel;
		dataModel.addListDataListener(this);

		// set the current selected item.
		selectedItemReminder = dataModel.getSelectedItem();
		firePropertyChange("model", oldModel, dataModel);
	}

	public ComboBoxModel<E> getModel() {
		return dataModel;
	}

	public void setLightWeightPopupEnabled(boolean aFlag) {
		// boolean oldFlag = lightWeightPopupEnabled;
		lightWeightPopupEnabled = aFlag;
		// firePropertyChange("lightWeightPopupEnabled", oldFlag,
		// lightWeightPopupEnabled);
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean isLightWeightPopupEnabled() {
		return lightWeightPopupEnabled;
	}

	public void setEditable(boolean aFlag) {
		// boolean oldFlag = isEditable;
		isEditable = aFlag;
		// firePropertyChange("editable", oldFlag, isEditable);
		spinner.setSelected(aFlag);
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setMaximumRowCount(int count) {
		// int oldCount = maximumRowCount;
		maximumRowCount = count;
		// firePropertyChange("maximumRowCount", oldCount, maximumRowCount);
		AndroidClassUtil.callEmptyMethod();
	}

	public int getMaximumRowCount() {
		AndroidClassUtil.callEmptyMethod();
		return maximumRowCount;
	}

	public void setRenderer(ListCellRenderer<? super E> aRenderer) {
		AndroidClassUtil.callEmptyMethod();
		// ListCellRenderer<? super E> oldRenderer = renderer;
		renderer = aRenderer;
		// firePropertyChange("renderer", oldRenderer, renderer);
		// invalidate();
	}

	public ListCellRenderer<? super E> getRenderer() {
		AndroidClassUtil.callEmptyMethod();
		return renderer;
	}

	public void setEditor(ComboBoxEditor anEditor) {
		ComboBoxEditor oldEditor = editor;

		if (editor != null) {
			editor.removeActionListener(this);
		}
		editor = anEditor;
		if (editor != null) {
			editor.addActionListener(this);
		}
		// firePropertyChange("editor", oldEditor, editor);
		AndroidClassUtil.callEmptyMethod();
	}

	public ComboBoxEditor getEditor() {
		return editor;
	}

	public void setSelectedItem(Object anObject) {
		Object oldSelection = selectedItemReminder;
		Object objectToSelect = anObject;
		int foundIdx = -1;
		if (oldSelection == null || !oldSelection.equals(anObject)) {

			if (anObject != null && !isEditable()) {
				boolean found = false;
				for (int i = 0; i < dataModel.getSize(); i++) {
					E element = dataModel.getElementAt(i);
					if (anObject.equals(element)) {
						found = true;
						foundIdx = i;
						objectToSelect = element;
						break;
					}
				}
				if (!found) {
					return;
				}
			}

			selectingItem = true;
			if (spinner == null) {
				AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
					@Override
					public void run() {
						initSpinner();
					}
				});
			}
			spinner.setSelection(foundIdx);
			dataModel.setSelectedItem(objectToSelect);
			mDataSetObservable.notifyChanged();// 刷新显示数据
			selectingItem = false;

			if (selectedItemReminder != dataModel.getSelectedItem()) {
				selectedItemChanged();
			}
		}
		fireActionEvent();
	}

	public Object getSelectedItem() {
		return dataModel.getSelectedItem();
	}

	public void setSelectedIndex(int anIndex) {
		int size = dataModel.getSize();

		if (anIndex == -1) {
			setSelectedItem(null);
		} else if (anIndex < -1 || anIndex >= size) {
			throw new IllegalArgumentException("setSelectedIndex: " + anIndex + " out of bounds");
		} else {
			setSelectedItem(dataModel.getElementAt(anIndex));
		}
	}

	@Transient
	public int getSelectedIndex() {
		Object sObject = dataModel.getSelectedItem();
		int i, c;
		E obj;

		for (i = 0, c = dataModel.getSize(); i < c; i++) {
			obj = dataModel.getElementAt(i);
			if (obj != null && obj.equals(sObject))
				return i;
		}
		return -1;
	}

	public E getPrototypeDisplayValue() {
		return prototypeDisplayValue;
	}

	public void setPrototypeDisplayValue(E prototypeDisplayValue) {
		// Object oldValue = this.prototypeDisplayValue;
		this.prototypeDisplayValue = prototypeDisplayValue;
		// firePropertyChange("prototypeDisplayValue", oldValue,
		// prototypeDisplayValue);
		AndroidClassUtil.callEmptyMethod();
	}

	public void addItem(E item) {
		checkMutableComboBoxModel();
		((MutableComboBoxModel<E>) dataModel).addElement(item);
		// spinner.invalidate();//不需要
	}

	public void insertItemAt(E item, int index) {
		checkMutableComboBoxModel();
		((MutableComboBoxModel<E>) dataModel).insertElementAt(item, index);
	}

	public void removeItem(Object anObject) {
		checkMutableComboBoxModel();
		((MutableComboBoxModel) dataModel).removeElement(anObject);
	}

	public void removeItemAt(int anIndex) {
		checkMutableComboBoxModel();
		((MutableComboBoxModel<E>) dataModel).removeElementAt(anIndex);
	}

	public void removeAllItems() {
		checkMutableComboBoxModel();
		MutableComboBoxModel<E> model = (MutableComboBoxModel<E>) dataModel;
		int size = model.getSize();

		if (model instanceof DefaultComboBoxModel) {
			((DefaultComboBoxModel) model).removeAllElements();
		} else {
			for (int i = 0; i < size; ++i) {
				E element = model.getElementAt(0);
				model.removeElement(element);
			}
		}
		selectedItemReminder = null;
		if (isEditable()) {
			if (editor != null) {
				editor.setItem(null);
			}
		}
	}

	void checkMutableComboBoxModel() {
		if (!(dataModel instanceof MutableComboBoxModel))
			throw new RuntimeException("Cannot use this method with a non-Mutable data model.");
	}

	public void showPopup() {
		// setPopupVisible(true);
		AndroidClassUtil.callEmptyMethod();
	}

	public void hidePopup() {
		// setPopupVisible(false);
		AndroidClassUtil.callEmptyMethod();
	}

	public void setPopupVisible(boolean v) {
		// getUI().setPopupVisible(this, v);
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean isPopupVisible() {
		// return getUI().isPopupVisible(this);
		return false;
	}

	public void addItemListener(ItemListener aListener) {
		list.add(ItemListener.class, aListener);
	}

	public void removeItemListener(ItemListener aListener) {
		list.remove(ItemListener.class, aListener);
	}

	public ItemListener[] getItemListeners() {
		return list.getListeners(ItemListener.class);
	}

	public ActionListener[] getActionListeners() {
		return list.getListeners(ActionListener.class);
	}

	public void addPopupMenuListener(PopupMenuListener l) {
		list.add(PopupMenuListener.class, l);
	}

	public void removePopupMenuListener(PopupMenuListener l) {
		list.remove(PopupMenuListener.class, l);
	}

	public PopupMenuListener[] getPopupMenuListeners() {
		return list.getListeners(PopupMenuListener.class);
	}

	public void firePopupMenuWillBecomeVisible() {
		PopupMenuListener[] listeners = list.getListeners(PopupMenuListener.class);
		PopupMenuEvent e = null;
		for (int i = listeners.length - 1; i >= 0; i--) {
			if (e == null)
				e = new PopupMenuEvent(this);
			((PopupMenuListener) listeners[i]).popupMenuWillBecomeVisible(e);
		}
	}

	public void firePopupMenuWillBecomeInvisible() {
		PopupMenuListener[] listeners = list.getListeners(PopupMenuListener.class);
		PopupMenuEvent e = null;
		for (int i = listeners.length - 1; i >= 0; i--) {
			if (e == null)
				e = new PopupMenuEvent(this);
			((PopupMenuListener) listeners[i]).popupMenuWillBecomeInvisible(e);
		}
	}

	public void firePopupMenuCanceled() {
		PopupMenuListener[] listeners = list.getListeners(PopupMenuListener.class);
		PopupMenuEvent e = null;
		for (int i = listeners.length - 1; i >= 0; i--) {
			if (e == null)
				e = new PopupMenuEvent(this);
			((PopupMenuListener) listeners[i]).popupMenuCanceled(e);
		}
	}

	protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
		return new ComboBoxActionPropertyChangeListener(this, a);
	}

	protected void actionPropertyChanged(Action action, String propertyName) {
		if (propertyName == Action.ACTION_COMMAND_KEY) {
			setActionCommandFromAction(action);
		} else if (propertyName == "enabled") {
			AbstractAction.setEnabledFromAction(this, action);
		} else if (Action.SHORT_DESCRIPTION == propertyName) {
			AbstractAction.setToolTipTextFromAction(this, action);
		}
	}

	private static class ComboBoxActionPropertyChangeListener
			extends ActionPropertyChangeListener<JComboBox<?>> {
		ComboBoxActionPropertyChangeListener(JComboBox<?> b, Action a) {
			super(b, a);
		}

		protected void actionPropertyChanged(JComboBox<?> cb, Action action,
				PropertyChangeEvent e) {
			if (AbstractAction.shouldReconfigure(e)) {
				cb.configurePropertiesFromAction(action);
			} else {
				cb.actionPropertyChanged(action, e.getPropertyName());
			}
		}
	}

	protected void fireItemStateChanged(ItemEvent e) {
		ItemListener[] listeners = list.getListeners(ItemListener.class);
		for (int i = listeners.length - 1; i >= 0; i--) {
			((ItemListener) listeners[i]).itemStateChanged(e);
		}
	}

	protected void fireActionEvent() {
		if (!firingActionEvent) {
			firingActionEvent = true;
			ActionEvent e = null;
			ActionListener[] listeners = list.getListeners(ActionListener.class);
			long mostRecentEventTime = EventQueue.getMostRecentEventTime();
			int modifiers = 0;
			AWTEvent currentEvent = EventQueue.getCurrentEvent();
			if (currentEvent instanceof InputEvent) {
				modifiers = ((InputEvent) currentEvent).getModifiers();
			} else if (currentEvent instanceof ActionEvent) {
				modifiers = ((ActionEvent) currentEvent).getModifiers();
			}

			for (int i = listeners.length - 1; i >= 0; i--) {
				if (e == null)
					e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getActionCommand(),
							mostRecentEventTime, modifiers);
				((ActionListener) listeners[i]).actionPerformed(e);
			}
			firingActionEvent = false;
		}
	}

	protected void selectedItemChanged() {
		if (selectedItemReminder != null) {
			fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED,
					selectedItemReminder, ItemEvent.DESELECTED));
		}

		selectedItemReminder = dataModel.getSelectedItem();

		if (selectedItemReminder != null) {
			fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED,
					selectedItemReminder, ItemEvent.SELECTED));
		}
	}

	public Object[] getSelectedObjects() {
		Object selectedObject = getSelectedItem();
		if (selectedObject == null)
			return new Object[0];
		else {
			Object result[] = new Object[1];
			result[0] = selectedObject;
			return result;
		}
	}

	public void actionPerformed(ActionEvent e) {
		AndroidClassUtil.callEmptyMethod();
		ComboBoxEditor editor2 = getEditor();
		if (editor2 != null) {
			Object newItem = editor2.getItem();
			setPopupVisible(false);
			getModel().setSelectedItem(newItem);
			String oldCommand = getActionCommand();
			setActionCommand("comboBoxEdited");
			fireActionEvent();
			setActionCommand(oldCommand);
		}
	}

	public void contentsChanged(ListDataEvent e) {
		Object oldSelection = selectedItemReminder;
		Object newSelection = dataModel.getSelectedItem();
		if (oldSelection == null || !oldSelection.equals(newSelection)) {
			selectedItemChanged();
			if (!selectingItem) {
				fireActionEvent();
			}
		}
	}

	public void intervalAdded(ListDataEvent e) {
		if (selectedItemReminder != dataModel.getSelectedItem()) {
			selectedItemChanged();
		}
	}

	public void intervalRemoved(ListDataEvent e) {
		contentsChanged(e);
	}

	public boolean selectWithKeyChar(char keyChar) {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void setEnabled(boolean b) {
		super.setEnabled(b);
		if (spinner != null) {
			spinner.setEnabled(b);
		}
		// firePropertyChange("enabled", !isEnabled(), isEnabled());
	}

	public void configureEditor(ComboBoxEditor anEditor, Object anItem) {
		anEditor.setItem(anItem);
	}

	public void processKeyEvent(KeyEvent e) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setKeySelectionManager(KeySelectionManager aManager) {
		keySelectionManager = aManager;
	}

	public KeySelectionManager getKeySelectionManager() {
		return keySelectionManager;
	}

	public int getItemCount() {
		return dataModel.getSize();
	}

	public E getItemAt(int index) {
		return dataModel.getElementAt(index);
	}

	protected KeySelectionManager createDefaultKeySelectionManager() {
		return new DefaultKeySelectionManager();
	}

	public interface KeySelectionManager {
		int selectionForKey(char aKey, ComboBoxModel aModel);
	}

	class DefaultKeySelectionManager implements KeySelectionManager, Serializable {
		public int selectionForKey(char aKey, ComboBoxModel aModel) {
			return -1;
		}
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