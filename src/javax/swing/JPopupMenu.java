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
import hc.android.UICore;
import hc.android.AndroidUIUtil;
import hc.android.WindowManager;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.PopupMenuUI;

import android.R;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import hc.android.HCRUtil;

/**
 * An implementation of a popup menu -- a small window that pops up and displays
 * a series of choices. A <code>JPopupMenu</code> is used for the menu that
 * appears when the user selects an item on the menu bar. It is also used for
 * "pull-right" menu that appears when the selects a menu item that activates
 * it. Finally, a <code>JPopupMenu</code> can also be used anywhere else you
 * want a menu to appear. For example, when the user right-clicks in a specified
 * area.
 * <p>
 * For information and examples of using popup menus, see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/menu.html">How to
 * Use Menus</a> in <em>The Java Tutorial.</em>
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
 * @beaninfo attribute: isContainer false description: A small window that pops
 *           up and displays a series of choices.
 *
 * @author Georges Saab
 * @author David Karlton
 * @author Arnaud Weber
 */
public class JPopupMenu extends JComponent implements Accessible, MenuElement {
	private static final String uiClassID = "PopupMenuUI";

	private static final Object defaultLWPopupEnabledKey = new StringBuffer(
			"JPopupMenu.defaultLWPopupEnabledKey");

	PopupWindow popupWindow;
	transient Component invoker;

	public static void setDefaultLightWeightPopupEnabled(boolean aFlag) {
		AndroidClassUtil.callEmptyMethod();
	}

	public static boolean getDefaultLightWeightPopupEnabled() {
		AndroidClassUtil.callEmptyMethod();
		return true;
	}

	public JPopupMenu() {
		this(null);
	}

	public JPopupMenu(String label) {
		updateUI();
	}

	public PopupMenuUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return (PopupMenuUI) null;
	}

	public void setUI(PopupMenuUI ui) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void updateUI() {
	}

	final HashMap<JMenu, LinearLayout> subMenuMapList = new HashMap<JMenu, LinearLayout>();

	LinearLayout mainAndSub;
	FrameLayout subFrames;
	LinearLayout mainMenu;
	ScrollView scrollView;

	private void buildPopupFrame() {
		subFrames = new FrameLayout(ActivityManager.applicationContext);

		mainMenu = buildMenuView(this, null);

		buildSubMenuView(mainMenu);

		mainAndSub = new LinearLayout(ActivityManager.applicationContext);
		{
			scrollView = new ScrollView(ActivityManager.applicationContext);
			{
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				scrollView.addView(mainMenu, lp);
			}

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			AndroidUIUtil.addView(mainAndSub, scrollView, lp, viewRelation);
		}

		if (subMenuMapList != null && subMenuMapList.size() > 0) {
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			lp.leftMargin = 2;
			AndroidUIUtil.addView(mainAndSub, subFrames, lp, viewRelation);
		}
	}

	private void buildSubMenuView(LinearLayout mainMenu) {
		subMenuMapList.clear();

		int size = getComponentCount();
		for (int i = 0; i < size; i++) {
			Component componentAtIndex = getComponent(i);
			if (componentAtIndex instanceof JMenu) {
				View backView = mainMenu.getChildAt(i);
				LinearLayout subMenu = buildMenuView(((JMenu) componentAtIndex).getPopupMenu(),
						backView);
				// subMenu.setVisibility(View.INVISIBLE);

				subMenuMapList.put((JMenu) componentAtIndex, subMenu);
				{
					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							FrameLayout.LayoutParams.WRAP_CONTENT,
							FrameLayout.LayoutParams.WRAP_CONTENT);
					lp.leftMargin = 0;
					lp.topMargin = backView.getTop();
					AndroidUIUtil.addView(subFrames, subMenu, lp, viewRelation);
				}
			}
		}
	}

	void setVisibleSubmenus(boolean isVisible) {
		Iterator<JMenu> it = subMenuMapList.keySet().iterator();
		while (it.hasNext()) {
			LinearLayout linearLayout = subMenuMapList.get(it.next());
			if (isVisible) {
				linearLayout.setVisibility(View.VISIBLE);
			} else {
				linearLayout.setVisibility(View.INVISIBLE);
			}
		}
	}

	LinearLayout lastShowLayout;

	void showSubMenu(JMenu menu) {
		if (lastShowLayout != null) {
			lastShowLayout.setVisibility(View.INVISIBLE);
		}

		LinearLayout layout = subMenuMapList.get(menu);
		if (layout != null) {
			lastShowLayout = layout;
			final int limitBottom = mainAndSub.getMeasuredHeight() - layout.getMeasuredHeight();
			int menuTop = menu.getPeerAdAPI().getTop() - scrollView.getScrollY();
			if (menuTop > limitBottom) {
				menuTop = limitBottom;
			}
			((FrameLayout.LayoutParams) layout.getLayoutParams()).topMargin = menuTop;
			layout.requestLayout();
			layout.setVisibility(View.VISIBLE);
		}
	}

	private LinearLayout buildMenuView(JPopupMenu popMenu, View backView) {
		LinearLayout listMenu = new LinearLayout(ActivityManager.applicationContext);
		listMenu.setOrientation(LinearLayout.VERTICAL);
		int startID = (backView == null) ? 10000 : backView.getId();
		int stepIDNum = (backView == null) ? 1000 : 1;

		int size = popMenu.getComponentCount();
		for (int i = 0; i < size; i++) {
			Component componentAtIndex = popMenu.getComponent(i);
			boolean isMenuItem = !(componentAtIndex instanceof JMenu);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);// 必须，才能满框选中
			lp.topMargin = lp.leftMargin = lp.rightMargin = lp.bottomMargin = AndroidUIUtil
					.dpToPx(1);
			if (componentAtIndex instanceof JSeparator) {
				lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
			}
			View itemView = componentAtIndex.getPeerAdAPI();
			itemView.setId(startID + stepIDNum * (i + 1));
			if (backView != null) {
				if (componentAtIndex.getComponentOrientation().isLeftToRight()) {
					itemView.setNextFocusLeftId(backView.getId());
					if (isMenuItem) {
						itemView.setNextFocusRightId(itemView.getId());// 设置自己
					}
				} else {
					itemView.setNextFocusRightId(backView.getId());
					if (isMenuItem) {
						itemView.setNextFocusLeftId(itemView.getId());
					}
				}
			} else {
				if (isMenuItem) {
					itemView.setNextFocusRightId(itemView.getId());// 设置自己
					itemView.setNextFocusLeftId(itemView.getId());
				}
			}
			AndroidUIUtil.addView(listMenu, itemView, lp, viewRelation);
		}

		if (size > 1) {
			// 底部转顶，顶转底
			View bottomView = popMenu.getComponent(size - 1).getPeerAdAPI();
			View topView = popMenu.getComponent(0).getPeerAdAPI();
			bottomView.setNextFocusDownId(topView.getId());
			topView.setNextFocusUpId(bottomView.getId());
		} else {
			View topView = popMenu.getComponent(0).getPeerAdAPI();
			topView.setNextFocusUpId(topView.getId());
			topView.setNextFocusDownId(topView.getId());
		}

		listMenu.setBackgroundDrawable(ActivityManager.applicationContext.getResources()
				.getDrawable(HCRUtil.getResource(HCRUtil.R_drawable_popup_window)));
		return listMenu;
	}

	private void showPopupAdAPI(Component invoker, int offx, int offy) {
		if (popupWindow != null) {
			dismissMenuAdAPI();
		}

		if (popupWindow == null) {
			buildPopupFrame();

			popupWindow = new PopupWindow(mainAndSub, LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT, true);
			popupWindow.setAnimationStyle(HCRUtil.getResource(HCRUtil.R_style_PopupMenuAnimation));

			popupWindow.setTouchable(true);
			popupWindow.setFocusable(true);// 遥控器版安卓必须
			// Controls whether the pop-up will be informed of touch events
			// outside of its window.
			// This only makes sense for pop-ups that are touchable but not
			// focusable
			popupWindow.setOutsideTouchable(false);

			popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
				@Override
				public void onDismiss() {
					if (WindowManager.isLastPopupWindowActioned()) {
					} else {
						firePopupMenuCanceled();
					}
					firePopupMenuWillBecomeInvisible();
				}
			});
		}

		if (!popupWindow.isShowing()) {
			firePopupMenuWillBecomeVisible();
			setVisibleSubmenus(false);
			WindowManager.showPopupWindow(popupWindow, invoker);
			isFirstFocus = true;
			isDismissPop = false;
		}

		// 落定焦点
		Component focusComponent = null;
		try {
			focusComponent = getComponent(0);// getComponentCount() - 1
			if (focusComponent.getPeerAdAPI().isFocused()) {
				focusComponent.getPeerAdAPI().clearFocus();
			}
			focusComponent.requestFocus();

			AndroidUIUtil.runDelayNotInUIThread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(AndroidUIUtil.MS_FOCUSONTOUCH);
					} catch (Exception e) {
					}
					setFocusableOnlyToJMenu(JPopupMenu.this);// 关闭setFocusableInTouchMode，以保留初次焦点
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setFocusableOnlyToJMenu(Container c) {
		int size = c.getComponentCount();
		for (int i = 0; i < size; i++) {
			Component comp = c.getComponent(i);
			final boolean isJMenu = comp instanceof JMenu;
			if (isJMenu) {
				setFocusableOnlyToJMenu(((JMenu) comp).getPopupMenu());
			} else if (comp instanceof JMenuItem) {
				try {
					comp.getPeerAdAPI().setFocusableInTouchMode(isJMenu);
				} catch (Exception e) {
				}
			}
		}
	}

	static boolean isFirstFocus = true;
	static boolean isDismissPop = false;

	public boolean isDismissPopupMenuAdAPI() {
		return isDismissPop;
	}

	public boolean isFirstFocusOnPopupMenuAdAPI() {
		if (isFirstFocus) {
			isFirstFocus = false;
			return true;
		} else {
			return false;
		}
	}

	final boolean dismissMenuAdAPI() {
		WindowManager.notifyPopupWindowActioned();
		isDismissPop = true;
		return WindowManager.disposePopupWindow();
	}

	public String getUIClassID() {
		return uiClassID;
	}

	protected void processFocusEvent(FocusEvent evt) {
	}

	protected void processKeyEvent(KeyEvent evt) {
		MenuSelectionManager.defaultManager().processKeyEvent(evt);
		if (evt.isConsumed()) {
			return;
		}
		super.processKeyEvent(evt);
	}

	public SingleSelectionModel getSelectionModel() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setSelectionModel(SingleSelectionModel model) {
		AndroidClassUtil.callEmptyMethod();
	}

	public JMenuItem add(JMenuItem menuItem) {
		super.add(menuItem);
		return menuItem;
	}

	public JMenuItem add(String s) {
		return add(new JMenuItem(s));
	}

	public JMenuItem add(Action a) {
		JMenuItem mi = createActionComponent(a);
		mi.setAction(a);
		add(mi);
		return mi;
	}

	Point adjustPopupLocationToFitScreen(int xPosition, int yPosition) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	static boolean canPopupOverlapTaskBar() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	protected JMenuItem createActionComponent(Action a) {
		JMenuItem mi = new JMenuItem() {
			protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
				PropertyChangeListener pcl = createActionChangeListener(this);
				if (pcl == null) {
					pcl = super.createActionPropertyChangeListener(a);
				}
				return pcl;
			}
		};
		mi.setHorizontalTextPosition(JButton.TRAILING);
		mi.setVerticalTextPosition(JButton.CENTER);
		return mi;
	}

	protected PropertyChangeListener createActionChangeListener(JMenuItem b) {
		return b.createActionPropertyChangeListener0(b.getAction());
	}

	public void remove(int pos) {
		super.remove(pos);
	}

	public void setLightWeightPopupEnabled(boolean aFlag) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean isLightWeightPopupEnabled() {
		AndroidClassUtil.callEmptyMethod();
		return true;
	}

	public String getLabel() {
		AndroidClassUtil.callEmptyMethod();
		return "";
	}

	public void setLabel(String label) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void addSeparator() {
		add(new JPopupMenu.Separator());
	}

	public void insert(Action a, int index) {
		JMenuItem mi = createActionComponent(a);
		mi.setAction(a);
		insert(mi, index);
	}

	public void insert(Component component, int index) {
		if (index < 0) {
			throw new IllegalArgumentException("index less than zero.");
		}

		int nitems = getComponentCount();
		Vector<Component> tempItems = new Vector<Component>();

		for (int i = index; i < nitems; i++) {
			tempItems.addElement(getComponent(index));
			remove(index);
		}

		add(component);

		for (Component tempItem : tempItems) {
			add(tempItem);
		}
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

	public void addMenuKeyListener(MenuKeyListener l) {
		list.add(MenuKeyListener.class, l);
	}

	public void removeMenuKeyListener(MenuKeyListener l) {
		list.remove(MenuKeyListener.class, l);
	}

	public MenuKeyListener[] getMenuKeyListeners() {
		return list.getListeners(MenuKeyListener.class);
	}

	protected void firePopupMenuWillBecomeVisible() {
		Object[] listeners = list.getListenerList();
		PopupMenuEvent e = null;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PopupMenuListener.class) {
				if (e == null)
					e = new PopupMenuEvent(this);
				((PopupMenuListener) listeners[i + 1]).popupMenuWillBecomeVisible(e);
			}
		}
	}

	protected void firePopupMenuWillBecomeInvisible() {
		Object[] listeners = list.getListenerList();
		PopupMenuEvent e = null;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PopupMenuListener.class) {
				if (e == null)
					e = new PopupMenuEvent(this);
				((PopupMenuListener) listeners[i + 1]).popupMenuWillBecomeInvisible(e);
			}
		}
	}

	protected void firePopupMenuCanceled() {
		Object[] listeners = list.getListenerList();
		PopupMenuEvent e = null;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == PopupMenuListener.class) {
				if (e == null)
					e = new PopupMenuEvent(this);
				((PopupMenuListener) listeners[i + 1]).popupMenuCanceled(e);
			}
		}
	}

	boolean alwaysOnTop() {
		return true;
	}

	public void pack() {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setVisible(boolean b) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean isVisible() {
		return popup.isShowing();
	}

	public void setLocation(int x, int y) {
		AndroidClassUtil.callEmptyMethod();
	}

	private boolean isPopupMenu() {
		return ((invoker != null) && !(invoker instanceof JMenu));
	}

	public Component getInvoker() {
		return this.invoker;
	}

	public void setInvoker(Component invoker) {
		this.invoker = invoker;
	}

	public void show(Component invoker, int x, int y) {
		showPopupAdAPI(invoker, x, y);
	}

	JPopupMenu getRootPopupMenu() {
		JPopupMenu mp = this;
		while ((mp != null) && (mp.isPopupMenu() != true) && (mp.getInvoker() != null)
				&& (mp.getInvoker().getParent() != null)
				&& (mp.getInvoker().getParent() instanceof JPopupMenu)) {
			mp = (JPopupMenu) mp.getInvoker().getParent();
		}
		return mp;
	}

	@Deprecated
	public Component getComponentAtIndex(int i) {
		return getComponent(i);
	}

	public int getComponentIndex(Component c) {
		int ncomponents = this.getComponentCount();
		Component[] component = this.getComponents();
		for (int i = 0; i < ncomponents; i++) {
			Component comp = component[i];
			if (comp == c)
				return i;
		}
		return -1;
	}

	public void setPopupSize(Dimension d) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setPopupSize(int width, int height) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setSelected(Component sel) {
		SingleSelectionModel model = getSelectionModel();
		int index = getComponentIndex(sel);
		model.setSelectedIndex(index);
	}

	public boolean isBorderPainted() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void setBorderPainted(boolean b) {
		AndroidClassUtil.callEmptyMethod();
	}

	protected void paintBorder(Graphics g) {
		if (isBorderPainted()) {
			super.paintBorder(g);
		}
	}

	public Insets getMargin() {
		return new Insets(0, 0, 0, 0);
	}

	boolean isSubPopupMenu(JPopupMenu popup) {
		int ncomponents = this.getComponentCount();
		Component[] component = this.getComponents();
		for (int i = 0; i < ncomponents; i++) {
			Component comp = component[i];
			if (comp instanceof JMenu) {
				JMenu menu = (JMenu) comp;
				JPopupMenu subPopup = menu.getPopupMenu();
				if (subPopup == popup)
					return true;
				if (subPopup.isSubPopupMenu(popup))
					return true;
			}
		}
		return false;
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

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
	}

	public void processMouseEvent(MouseEvent event, MenuElement path[],
			MenuSelectionManager manager) {
	}

	public void processKeyEvent(KeyEvent e, MenuElement path[], MenuSelectionManager manager) {
		MenuKeyEvent mke = new MenuKeyEvent(e.getComponent(), e.getID(), e.getWhen(),
				e.getModifiers(), e.getKeyCode(), e.getKeyChar(), path, manager);
		processMenuKeyEvent(mke);

		if (mke.isConsumed()) {
			e.consume();
		}
	}

	private void processMenuKeyEvent(MenuKeyEvent e) {
		switch (e.getID()) {
		case KeyEvent.KEY_PRESSED:
			fireMenuKeyPressed(e);
			break;
		case KeyEvent.KEY_RELEASED:
			fireMenuKeyReleased(e);
			break;
		case KeyEvent.KEY_TYPED:
			fireMenuKeyTyped(e);
			break;
		default:
			break;
		}
	}

	private void fireMenuKeyPressed(MenuKeyEvent event) {
		Object[] listeners = list.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == MenuKeyListener.class) {
				((MenuKeyListener) listeners[i + 1]).menuKeyPressed(event);
			}
		}
	}

	private void fireMenuKeyReleased(MenuKeyEvent event) {
		Object[] listeners = list.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == MenuKeyListener.class) {
				((MenuKeyListener) listeners[i + 1]).menuKeyReleased(event);
			}
		}
	}

	private void fireMenuKeyTyped(MenuKeyEvent event) {
		Object[] listeners = list.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == MenuKeyListener.class) {
				((MenuKeyListener) listeners[i + 1]).menuKeyTyped(event);
			}
		}
	}

	public void menuSelectionChanged(boolean isIncluded) {
		if (invoker instanceof JMenu) {
			JMenu m = (JMenu) invoker;
			if (isIncluded)
				m.setPopupMenuVisible(true);
			else
				m.setPopupMenuVisible(false);
		}
		if (isPopupMenu() && !isIncluded)
			setVisible(false);
	}

	public MenuElement[] getSubElements() {
		MenuElement result[];
		Vector<MenuElement> tmp = new Vector<MenuElement>();
		int c = getComponentCount();
		int i;
		Component m;

		for (i = 0; i < c; i++) {
			m = getComponent(i);
			if (m instanceof MenuElement)
				tmp.addElement((MenuElement) m);
		}

		result = new MenuElement[tmp.size()];
		for (i = 0, c = tmp.size(); i < c; i++)
			result[i] = tmp.elementAt(i);
		return result;
	}

	public Component getComponent() {
		return this;
	}

	static public class Separator extends JSeparator {
		public Separator() {
			super(JSeparator.HORIZONTAL);
		}

		public String getUIClassID() {
			return "PopupMenuSeparatorUI";
		}
	}

	public boolean isPopupTrigger(MouseEvent e) {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}
}
