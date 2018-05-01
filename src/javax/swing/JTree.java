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
import hc.android.FocusManager;
import hc.android.AndroidUIUtil;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.plaf.TreeUI;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import hc.android.HCRUtil;

/**
 * <a name="jtree_description"> A control that displays a set of hierarchical
 * data as an outline. You can find task-oriented documentation and examples of
 * using trees in <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/tree.html">How to
 * Use Trees</a>, a section in <em>The Java Tutorial.</em>
 * <p>
 * A specific node in a tree can be identified either by a <code>TreePath</code>
 * (an object that encapsulates a node and all of its ancestors), or by its
 * display row, where each row in the display area displays one node. An
 * <i>expanded</i> node is a non-leaf node (as identified by
 * <code>TreeModel.isLeaf(node)</code> returning false) that will displays its
 * children when all its ancestors are <i>expanded</i>. A <i>collapsed</i> node
 * is one which hides them. A <i>hidden</i> node is one which is under a
 * collapsed ancestor. All of a <i>viewable</i> nodes parents are expanded, but
 * may or may not be displayed. A <i>displayed</i> node is both viewable and in
 * the display area, where it can be seen.
 * <p>
 * The following <code>JTree</code> methods use "visible" to mean "displayed":
 * <ul>
 * <li><code>isRootVisible()</code>
 * <li><code>setRootVisible()</code>
 * <li><code>scrollPathToVisible()</code>
 * <li><code>scrollRowToVisible()</code>
 * <li><code>getVisibleRowCount()</code>
 * <li><code>setVisibleRowCount()</code>
 * </ul>
 * <p>
 * The next group of <code>JTree</code> methods use "visible" to mean "viewable"
 * (under an expanded parent):
 * <ul>
 * <li><code>isVisible()</code>
 * <li><code>makeVisible()</code>
 * </ul>
 * <p>
 * If you are interested in knowing when the selection changes implement the
 * <code>TreeSelectionListener</code> interface and add the instance using the
 * method <code>addTreeSelectionListener</code>. <code>valueChanged</code> will
 * be invoked when the selection changes, that is if the user clicks twice on
 * the same node <code>valueChanged</code> will only be invoked once.
 * <p>
 * If you are interested in detecting either double-click events or when a user
 * clicks on a node, regardless of whether or not it was selected, we recommend
 * you do the following:
 * 
 * <pre>
 * final JTree tree = ...;
 *
 * MouseListener ml = new MouseAdapter() {
 *     public void <b>mousePressed</b>(MouseEvent e) {
 *         int selRow = tree.getRowForLocation(e.getX(), e.getY());
 *         TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
 *         if(selRow != -1) {
 *             if(e.getClickCount() == 1) {
 *                 mySingleClick(selRow, selPath);
 *             }
 *             else if(e.getClickCount() == 2) {
 *                 myDoubleClick(selRow, selPath);
 *             }
 *         }
 *     }
 * };
 * tree.addMouseListener(ml);
 * </pre>
 * 
 * NOTE: This example obtains both the path and row, but you only need to get
 * the one you're interested in.
 * <p>
 * To use <code>JTree</code> to display compound nodes (for example, nodes
 * containing both a graphic icon and text), subclass {@link TreeCellRenderer}
 * and use {@link #setCellRenderer} to tell the tree to use it. To edit such
 * nodes, subclass {@link TreeCellEditor} and use {@link #setCellEditor}.
 * <p>
 * Like all <code>JComponent</code> classes, you can use {@link InputMap} and
 * {@link ActionMap} to associate an {@link Action} object with a
 * {@link KeyStroke} and execute the action under specified conditions.
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
 * @beaninfo attribute: isContainer false description: A component that displays
 *           a set of hierarchical data as an outline.
 *
 * @author Rob Davis
 * @author Ray Ryan
 * @author Scott Violet
 */
public class JTree extends JComponent implements Scrollable, Accessible {
	private final HashMap<TreeNode, TreeNodeView> nodeAndView = new HashMap<TreeNode, JTree.TreeNodeView>();

	private static final String uiClassID = "TreeUI";

	transient protected TreeModel treeModel;
	transient protected TreeSelectionModel selectionModel;
	protected boolean rootVisible = true;
	transient protected TreeCellRenderer cellRenderer;
	TreeNode currSelectedTreeNode;

	transient private HashMap<TreeNode, Boolean> expandedState = new HashMap<TreeNode, Boolean>();
	protected transient TreeSelectionRedirector selectionRedirector;
	protected int visibleRowCount;

	transient protected TreeModelListener treeModelListener;
	final LinearLayout defaultLinearLayout = new LinearLayout(ActivityManager.applicationContext);

	final ScrollView rootScrollView = new ScrollView(ActivityManager.applicationContext);

	private TreePath leadPath;
	private TreePath anchorPath;

	public static final class DropLocation extends TransferHandler.DropLocation {
		private final TreePath path;
		private final int index;

		private DropLocation(Point p, TreePath path, int index) {
			super(p);
			this.path = path;
			this.index = index;
		}

		public int getChildIndex() {
			return index;
		}

		public TreePath getPath() {
			return path;
		}

		public String toString() {
			return getClass().getName() + "[dropPoint=" + getDropPoint() + "," + "path=" + path
					+ "," + "childIndex=" + index + "]";
		}
	}

	private final TreeExpansionListener uiRefreshTreeExpansionListener = new TreeExpansionListener() {
		@Override
		public void treeExpanded(TreeExpansionEvent event) {
			toExpand(event.getPath());
		}

		private void toExpand(TreePath path) {
			if (path.getParentPath() != null) {
				toExpand(path.getParentPath());
			}
			TreeNode lastPathComponent = (TreeNode) path.getLastPathComponent();
			TreeNodeView tnv = nodeAndView.get(lastPathComponent);
			tnv.updateExpandChildsUI(lastPathComponent, true, false);
		}

		@Override
		public void treeCollapsed(TreeExpansionEvent event) {
			toCollapse(event.getPath());
		}

		private void toCollapse(TreePath path) {
			if (path.getParentPath() != null) {
				toCollapse(path.getParentPath());
			}
			TreeNode lastPathComponent = (TreeNode) path.getLastPathComponent();
			TreeNodeView tnv = nodeAndView.get(lastPathComponent);
			tnv.updateChildGroupUI(lastPathComponent);
		}
	};

	public final static String CELL_RENDERER_PROPERTY = "cellRenderer";
	public final static String TREE_MODEL_PROPERTY = "model";
	public final static String ROOT_VISIBLE_PROPERTY = "rootVisible";
	public final static String SHOWS_ROOT_HANDLES_PROPERTY = "showsRootHandles";
	public final static String ROW_HEIGHT_PROPERTY = "rowHeight";
	public final static String CELL_EDITOR_PROPERTY = "cellEditor";
	public final static String EDITABLE_PROPERTY = "editable";
	public final static String LARGE_MODEL_PROPERTY = "largeModel";
	public final static String SELECTION_MODEL_PROPERTY = "selectionModel";
	public final static String VISIBLE_ROW_COUNT_PROPERTY = "visibleRowCount";
	public final static String INVOKES_STOP_CELL_EDITING_PROPERTY = "invokesStopCellEditing";
	public final static String SCROLLS_ON_EXPAND_PROPERTY = "scrollsOnExpand";
	public final static String TOGGLE_CLICK_COUNT_PROPERTY = "toggleClickCount";
	public final static String LEAD_SELECTION_PATH_PROPERTY = "leadSelectionPath";
	public final static String ANCHOR_SELECTION_PATH_PROPERTY = "anchorSelectionPath";
	public final static String EXPANDS_SELECTED_PATHS_PROPERTY = "expandsSelectedPaths";

	protected static TreeModel getDefaultTreeModel() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("JTree");
		return new DefaultTreeModel(root);
	}

	protected static TreeModel createTreeModel(Object value) {
		DefaultMutableTreeNode root;

		if ((value instanceof Object[]) || (value instanceof Hashtable)
				|| (value instanceof Vector)) {
			root = new DefaultMutableTreeNode("root");
			DynamicUtilTreeNode.createChildren(root, value);
		} else {
			root = new DynamicUtilTreeNode("root", value);
		}
		return new DefaultTreeModel(root, false);
	}

	public JTree() {
		this(getDefaultTreeModel());
	}

	public JTree(Object[] value) {
		this(createTreeModel(value));
		this.setRootVisible(false);
		this.setShowsRootHandles(true);
	}

	public JTree(Vector<?> value) {
		this(createTreeModel(value));
		this.setRootVisible(false);
		this.setShowsRootHandles(true);
	}

	public JTree(Hashtable<?, ?> value) {
		this(createTreeModel(value));
		this.setRootVisible(false);
		this.setShowsRootHandles(true);
	}

	public JTree(TreeNode root) {
		this(root, false);
	}

	public JTree(TreeNode root, boolean asksAllowsChildren) {
		this(new DefaultTreeModel(root, asksAllowsChildren));
	}

	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
		// LayoutParams lp = rootScrollView.getLayoutParams();
		// lp.width = preferredSize.width;
		// lp.height = preferredSize.height;
	}

	public JTree(TreeModel newModel) {
		super();
		visibleRowCount = 15;
		rootVisible = true;
		selectionModel = new DefaultTreeSelectionModel();
		cellRenderer = null;
		// {
		// LinearLayout.LayoutParams lp = new
		// LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
		// LinearLayout.LayoutParams.MATCH_PARENT);
		// defaultLinearLayout.addView(rootScrollView, lp);
		// }
		// setPeerAdAPI(defaultLinearLayout);
		setModel(newModel);
		updateUI();
	}

	public TreeUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setUI(TreeUI ui) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void updateUI() {
		AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				if (getModel() != null) {
					refreshRootNodeAdAPI((TreeNode) getModel().getRoot(), true);
				}
			}
		});
	}

	protected class TreeSelectionRedirector implements Serializable, TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {
			fireValueChanged(e);
		}
	}

	class TreeNodeView {
		LinearLayout nodeViewContainer;
		ImageView expandNodeView;
		LinearLayout nodeView;// 包含expan, icon, text
		LinearLayout childGroup;
		JCheckBox selected;
		final ImageView rendererView;

		public static final int gap = 5;
		public static final int expandIconSize = AndroidUIUtil.MIN_DRAWING_ICON_SIZE;

		TreeNodeView(final TreeNode treeNode) {
			nodeAndView.put(treeNode, this);

			nodeViewContainer = new LinearLayout(ActivityManager.applicationContext);
			nodeViewContainer.setOrientation(LinearLayout.VERTICAL);

			nodeView = new LinearLayout(ActivityManager.applicationContext);
			nodeView.setOrientation(LinearLayout.HORIZONTAL);

			{
				android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				nodeViewContainer.addView(nodeView, lp);
			}

			if (treeNode.isLeaf()) {
				selected = new JCheckBox();
				selected.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						switchSelection(treeNode, true);
					}
				});
			}

			rendererView = new ImageView(ActivityManager.applicationContext) {
				public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
					if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
						if (treeNode.isLeaf() == false) {
							Boolean expandStatus = expandedState.get(treeNode);
							expandStatus = (expandStatus != null && expandStatus);
							if ((keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER)
									|| (expandStatus
											&& keyCode == android.view.KeyEvent.KEYCODE_DPAD_LEFT)
									|| (expandStatus == false
											&& keyCode == android.view.KeyEvent.KEYCODE_DPAD_RIGHT)) {
								doExpandOrCollpse(treeNode);
								rendererView.requestFocus();
								return true;
							}
						}
					}
					return super.onKeyDown(keyCode, event);
				}
			};
			rendererView.setFocusable(true);
			rendererView.setFocusableInTouchMode(false);
			rendererView.setClickable(true);
			rendererView.setBackgroundResource(HCRUtil.getResource(HCRUtil.R_drawable_tree_node));

			AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
				@Override
				public void run() {
					addAndRefreshInUI(treeNode);
				}
			});

			Boolean status = expandedState.get(treeNode);
			if (status != null && status) {
				updateExpandChildsUI(treeNode, true, false);
			}
		}

		public void refreshSelected(TreeNode treeNode, boolean isSelected) {
			if(isSelected) {
				currSelectedTreeNode = treeNode;
			}
			if (selected != null) {
				selected.setSelected(isSelected);
			}
		}

		/**
		 * 
		 * @param treeNode
		 * @param refresh
		 *            展开，并重绘
		 * @param nested
		 *            递归
		 */
		final void updateExpandChildsUI(TreeNode treeNode, boolean refresh, boolean nested) {
			if (childGroup == null) {
				childGroup = new LinearLayout(ActivityManager.applicationContext);
				childGroup.setOrientation(LinearLayout.VERTICAL);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.gravity = (Gravity.LEFT);
				lp.leftMargin = expandIconSize;
				nodeViewContainer.addView(childGroup, lp);
			}

			int count = treeNode.getChildCount();
			if (refresh || childGroup.getChildCount() > 0) {
				for (int i = 0; i < count; i++) {
					TreeNode aChild = treeNode.getChildAt(i);
					TreeNodeView tnv = nodeAndView.get(aChild);
					boolean willAdd = false;
					if (tnv == null) {
						willAdd = true;
						tnv = new TreeNodeView(aChild);
					} else {
						if (tnv.nodeViewContainer == childGroup.getChildAt(i)) {
							if (nested && (aChild.isLeaf() == false) && tnv.childGroup != null) {
								tnv.updateExpandChildsUI(aChild, refresh, nested);
							}
						} else {
							willAdd = true;
						}
					}
					if (willAdd) {
						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						lp.gravity = (Gravity.LEFT);
						AndroidUIUtil.addView(childGroup, tnv.nodeViewContainer, i, lp,
								viewRelation);
					}
				}
				while (childGroup.getChildCount() > count) {
					viewRelation.unregisterView(childGroup.getChildAt(count));
				}
			}
			updateChildGroupUI(treeNode);
			refreshRenderer(treeNode);
		}

		final void updateChildGroupUI(TreeNode treeNode) {
			if (childGroup == null) {
				return;
			}
			refreshExpandOrCollapse(treeNode);
			Boolean status = expandedState.get(treeNode);
			if (status == null || status == false) {
				childGroup.setVisibility(View.GONE);
			} else {
				childGroup.setVisibility(View.VISIBLE);
			}
		}

		/**
		 * 重绘当前结点的展开或收拢。
		 * 
		 * @param treeNode
		 * @param refreshIconText
		 *            同时重绘内容区
		 */
		private final void addAndRefreshInUI(final TreeNode treeNode) {
			int indent = 0;
			if (treeNode.getChildCount() > 0) {
				refreshExpandOrCollapse(treeNode);

				indent = gap;
			} else {
				indent = expandIconSize + gap;
			}

			if (selected != null) {
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER;
				lp.leftMargin = indent;
				nodeView.addView(selected.getPeerAdAPI(), lp);
			}

			nodeView.removeView(rendererView);
			// AndroidUIUtil.removeFromParent(rendererView);

			refreshRenderer(treeNode);

			rendererView.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						switchSelection(treeNode, false);// 对叶节点
						swapExpand(treeNode);// 对非叶节点
						return true;
					}
					return false;
				}
			});
			rendererView.setOnKeyListener(new View.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER
							|| keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
						int action = event.getAction();
						if (action == android.view.KeyEvent.ACTION_DOWN) {
							swapExpand(treeNode);

							if (treeNode.isLeaf()) {
								selected.doClick();
							}
							return true;
						}
					}
					return false;
				}
			});
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER;
			if (selected != null) {
			} else {
				lp.leftMargin = indent;
			}
			nodeView.addView(rendererView, lp);
		}

		private void refreshRenderer(final TreeNode treeNode) {
			AndroidUIUtil.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Component comp = renderer(treeNode);
					if (comp instanceof JComponent) {
						((JComponent) comp).updateUI();
					}
					rendererView.setImageBitmap(AndroidUIUtil.getViewBitmap(comp.getPeerAdAPI()));
				}
			});
		}

		private void refreshExpandOrCollapse(final TreeNode treeNode) {
			Boolean status = expandedState.get(treeNode);
			if (status == null || status == false) {
				expandNodeView = AndroidUIUtil.getExpandDrawable();
			} else {
				expandNodeView = AndroidUIUtil.getCollapseDrawable();
			}
			expandNodeView.setClickable(true);
			expandNodeView.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						doExpandOrCollpse(treeNode);
						return true;
					}
					return false;
				}
			});
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(expandIconSize,
					expandIconSize);
			lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER;
			if (nodeView.getChildCount() > 0) {
				nodeView.removeViewAt(0);
			}
			nodeView.addView(expandNodeView, 0, lp);
		}

		DefaultTreeCellRenderer dtcr;

		private Component renderer(TreeNode node) {
			TreeCellRenderer cellRenderer2 = JTree.this.getCellRenderer();
			if (cellRenderer2 == null) {
				if (dtcr == null) {
					dtcr = new DefaultTreeCellRenderer();
				}
				cellRenderer2 = dtcr;
			}

			Boolean expanded = expandedState.get(node);
			expanded = (expanded != null && expanded);
			return cellRenderer2.getTreeCellRendererComponent(JTree.this, node,
					node == currSelectedTreeNode, expanded, node.isLeaf(), 0,
					FocusManager.getFocusComponentOwner() == node);
		}

		void doExpandOrCollpse(final TreeNode treeNode) {
			Boolean expandStatus = expandedState.get(treeNode);
			expandStatus = (expandStatus != null && expandStatus);

			TreePath tp = getTreePathAdAPI(treeNode);

			if (expandStatus == false) {
				expandPath(tp);
				updateExpandChildsUI(treeNode, true, false);
			} else {
				collapsePath(tp);
				updateChildGroupUI(treeNode);
			}
			refreshExpandOrCollapse(treeNode);
		}

		private void swapExpand(final TreeNode treeNode) {
			if (treeNode.isLeaf() == false) {
				Boolean expandStatus = expandedState.get(treeNode);
				expandStatus = (expandStatus != null && expandStatus);
				doExpandOrCollpse(treeNode);
			}

			rendererView.requestFocus();
		}

		private void switchSelection(final TreeNode treeNode, boolean isEventDone) {
			TreeNode oldSelected = JTree.this.currSelectedTreeNode;
			if(treeNode == oldSelected) {
				return;
			}
			
			if (treeNode.isLeaf()) {
				final boolean isSelected = selected.isSelected();
				if (isEventDone ? isSelected : !isSelected) {
					notifyNodeSelectChangedAdAPI(treeNode);
				} else {
					clearSelection(treeNode);
				}
			}
		}

	}

	public String getUIClassID() {
		return uiClassID;
	}

	public TreeCellRenderer getCellRenderer() {
		return cellRenderer;
	}

	public void setCellRenderer(TreeCellRenderer x) {
		cellRenderer = x;
		updateUI();
	}

	public void setEditable(boolean flag) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean isEditable() {
		return false;
	}

	public void setCellEditor(TreeCellEditor cellEditor) {
		AndroidClassUtil.callEmptyMethod();
	}

	public TreeCellEditor getCellEditor() {
		return null;
	}

	public TreeModel getModel() {
		return treeModel;
	}

	public void setModel(TreeModel newModel) {
		clearSelection(null);

		TreeModel oldModel = treeModel;

		if (treeModel != null && treeModelListener != null)
			treeModel.removeTreeModelListener(treeModelListener);

		if (accessibleContext != null) {
			if (treeModel != null) {
				treeModel.removeTreeModelListener((TreeModelListener) accessibleContext);
			}
			if (newModel != null) {
				newModel.addTreeModelListener((TreeModelListener) accessibleContext);
			}
		}

		treeModel = newModel;
		clearToggledPaths();
		if (treeModel != null) {
			if (treeModelListener == null) {
				treeModelListener = createTreeModelListener();
			}
			if (treeModelListener != null) {
				treeModel.addTreeModelListener(treeModelListener);
			}
			if (treeModel.getRoot() != null && !treeModel.isLeaf(treeModel.getRoot())) {
				expandedState.put((TreeNode) treeModel.getRoot(), Boolean.TRUE);
			}
		}

		{
			nodeAndView.clear();
			TreeNode root = (TreeNode) getModel().getRoot();
			TreeNodeView tnv = new TreeNodeView(root);
			expandRoot();

			// rootScrollView.removeAllViews();
			// LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
			// LayoutParams.MATCH_PARENT);
			// rootScrollView.addView(tnv.nodeViewContainer, lp);
			setPeerAdAPI(tnv.nodeViewContainer);
		}

		firePropertyChange(TREE_MODEL_PROPERTY, oldModel, treeModel);
		invalidate();
	}

	public void invalidate() {
		updateUI();
	}

	public boolean isRootVisible() {
		return rootVisible;
	}

	public void setRootVisible(boolean rootVisible) {
		this.rootVisible = rootVisible;

		if (getModel() != null) {
			TreeNode root = (TreeNode) getModel().getRoot();
			refreshRootNodeAdAPI(root, true);
		}
	}

	public void setShowsRootHandles(boolean newValue) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getShowsRootHandles() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void setRowHeight(int rowHeight) {
		AndroidClassUtil.callEmptyMethod();
	}

	public int getRowHeight() {
		AndroidClassUtil.callEmptyMethod();
		return AndroidUIUtil.getDefaultTextViewHeight(getFont(), getScreenAdapterAdAPI());
	}

	public boolean isFixedRowHeight() {
		return true;
	}

	public void setLargeModel(boolean newValue) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean isLargeModel() {
		return false;
	}

	public void setInvokesStopCellEditing(boolean newValue) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getInvokesStopCellEditing() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void setScrollsOnExpand(boolean newValue) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getScrollsOnExpand() {
		return false;
	}

	public void setToggleClickCount(int clickCount) {
		AndroidClassUtil.callEmptyMethod();
	}

	public int getToggleClickCount() {
		return 2;
	}

	public void setExpandsSelectedPaths(boolean newValue) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getExpandsSelectedPaths() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void setDragEnabled(boolean b) {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean getDragEnabled() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public final void setDropMode(DropMode dropMode) {
		AndroidClassUtil.callEmptyMethod();
	}

	public final DropMode getDropMode() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	DropLocation dropLocationForPoint(Point p) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	Object setDropLocation(TransferHandler.DropLocation location, Object state, boolean forDrop) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	void dndDone() {
		AndroidClassUtil.callEmptyMethod();
	}

	public final DropLocation getDropLocation() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public boolean isPathEditable(TreePath path) {
		AndroidClassUtil.callEmptyMethod();
		return isEditable();
	}

	public String getToolTipText(MouseEvent event) {
		AndroidClassUtil.callEmptyMethod();
		return "";
	}

	public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		if (value != null) {
			String sValue = value.toString();
			if (sValue != null) {
				return sValue;
			}
		}
		return "";
	}

	public int getRowCount() {
		return visibleRowCount;
	}

	public void setSelectionPath(TreePath path) {
		getSelectionModel().setSelectionPath(path);
		final TreeNode lastPathComponent = (TreeNode) path.getLastPathComponent();
		TreeNodeView tnv = nodeAndView.get(lastPathComponent);
		if (tnv != null) {
			tnv.refreshSelected(lastPathComponent, true);
		}
	}

	public void setSelectionPaths(TreePath[] paths) {
		getSelectionModel().setSelectionPaths(paths);
	}

	public void setLeadSelectionPath(TreePath newPath) {
		TreePath oldValue = leadPath;

		leadPath = newPath;
		firePropertyChange(LEAD_SELECTION_PATH_PROPERTY, oldValue, newPath);
	}

	public void setAnchorSelectionPath(TreePath newPath) {
		TreePath oldValue = anchorPath;

		anchorPath = newPath;
		firePropertyChange(ANCHOR_SELECTION_PATH_PROPERTY, oldValue, newPath);
	}

	public void setSelectionRow(int row) {
		int[] rows = { row };

		setSelectionRows(rows);
	}

	public void setSelectionRows(int[] rows) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void addSelectionPath(TreePath path) {
		getSelectionModel().addSelectionPath(path);
	}

	public void addSelectionPaths(TreePath[] paths) {
		getSelectionModel().addSelectionPaths(paths);
	}

	public void addSelectionRow(int row) {
		int[] rows = { row };

		addSelectionRows(rows);
	}

	public void addSelectionRows(int[] rows) {
		// 如果实现此功能，请务必getTreeCellRendererComponent绘制中实现row参数
		AndroidClassUtil.callEmptyMethod();
	}

	public Object getLastSelectedPathComponent() {
		TreePath selPath = getSelectionModel().getSelectionPath();

		if (selPath != null)
			return selPath.getLastPathComponent();
		return null;
	}

	public TreePath getLeadSelectionPath() {
		return leadPath;
	}

	public TreePath getAnchorSelectionPath() {
		return anchorPath;
	}

	public TreePath getSelectionPath() {
		return getSelectionModel().getSelectionPath();
	}

	public TreePath[] getSelectionPaths() {
		TreePath[] selectionPaths = getSelectionModel().getSelectionPaths();

		return (selectionPaths != null && selectionPaths.length > 0) ? selectionPaths : null;
	}

	public int[] getSelectionRows() {
		return getSelectionModel().getSelectionRows();
	}

	public int getSelectionCount() {
		return selectionModel.getSelectionCount();
	}

	public int getMinSelectionRow() {
		return getSelectionModel().getMinSelectionRow();
	}

	public int getMaxSelectionRow() {
		return getSelectionModel().getMaxSelectionRow();
	}

	public int getLeadSelectionRow() {
		TreePath leadPath = getLeadSelectionPath();

		if (leadPath != null) {
			return getRowForPath(leadPath);
		}
		return -1;
	}

	public boolean isPathSelected(TreePath path) {
		return getSelectionModel().isPathSelected(path);
	}

	public boolean isRowSelected(int row) {
		return getSelectionModel().isRowSelected(row);
	}

	public Enumeration<TreePath> getExpandedDescendants(TreePath parent) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public boolean hasBeenExpanded(TreePath path) {
		return (path != null && expandedState.get((TreeNode) path.getLastPathComponent()) != null);
	}

	public boolean isExpanded(TreePath path) {
		if (path == null)
			return false;
		Object value;
		do {
			value = expandedState.get((TreeNode) path.getLastPathComponent());
			if (value == null || !((Boolean) value).booleanValue())
				return false;
		} while ((path = path.getParentPath()) != null);

		return true;
	}

	public boolean isExpanded(int row) {
		TreePath path = getPathForRow(row);

		if (path != null) {
			Boolean value = expandedState.get((TreeNode) path.getLastPathComponent());

			return (value != null && value.booleanValue());
		}
		return false;
	}

	public boolean isCollapsed(TreePath path) {
		return !isExpanded(path);
	}

	public boolean isCollapsed(int row) {
		return !isExpanded(row);
	}

	public void makeVisible(TreePath path) {
		if (path != null) {
			TreePath parentPath = path.getParentPath();

			if (parentPath != null) {
				expandPath(parentPath);
			}
		}
	}

	public static TreePath getTreePathAdAPI(TreeNode node) {
		TreeNode parent = node.getParent();
		if (parent == null) {
			return new TreePath(node);
		} else {
			TreePath path = getTreePathAdAPI(parent);
			return path.pathByAddingChild(node);
		}
	}

	public boolean isVisible(TreePath path) {
		if (path != null) {
			TreePath parentPath = path.getParentPath();

			if (parentPath != null)
				return isExpanded(parentPath);
			return true;
		}
		return false;
	}

	public Rectangle getPathBounds(TreePath path) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public Rectangle getRowBounds(int row) {
		return getPathBounds(getPathForRow(row));
	}

	public void scrollPathToVisible(TreePath path) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void scrollRowToVisible(int row) {
		scrollPathToVisible(getPathForRow(row));
	}

	public TreePath getPathForRow(int row) {
		TreeUI tree = getUI();

		if (tree != null)
			return tree.getPathForRow(this, row);
		return null;
	}

	public int getRowForPath(TreePath path) {
		TreeUI tree = getUI();

		if (tree != null)
			return tree.getRowForPath(this, path);
		return -1;
	}

	public void expandPath(TreePath path) {
		TreeModel model = getModel();

		if (path != null && model != null && !model.isLeaf(path.getLastPathComponent())) {
			setExpandedState(path, true);
		}
	}

	public void expandRow(int row) {
		expandPath(getPathForRow(row));
	}

	public void collapsePath(TreePath path) {
		setExpandedState(path, false);
	}

	public void collapseRow(int row) {
		collapsePath(getPathForRow(row));
	}

	public TreePath getPathForLocation(int x, int y) {
		TreePath closestPath = getClosestPathForLocation(x, y);

		if (closestPath != null) {
			Rectangle pathBounds = getPathBounds(closestPath);

			if (pathBounds != null && x >= pathBounds.x && x < (pathBounds.x + pathBounds.width)
					&& y >= pathBounds.y && y < (pathBounds.y + pathBounds.height))
				return closestPath;
		}
		return null;
	}

	public int getRowForLocation(int x, int y) {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public TreePath getClosestPathForLocation(int x, int y) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public int getClosestRowForLocation(int x, int y) {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public boolean isEditing() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public boolean stopEditing() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public void cancelEditing() {
		AndroidClassUtil.callEmptyMethod();
	}

	public void startEditingAtPath(TreePath path) {
		AndroidClassUtil.callEmptyMethod();
	}

	public TreePath getEditingPath() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setSelectionModel(TreeSelectionModel selectionModel) {
		if (selectionModel == null)
			return;

		TreeSelectionModel oldValue = this.selectionModel;

		if (accessibleContext != null) {
			this.selectionModel
					.removeTreeSelectionListener((TreeSelectionListener) accessibleContext);
			selectionModel.addTreeSelectionListener((TreeSelectionListener) accessibleContext);
		}

		this.selectionModel = selectionModel;
		// firePropertyChange(SELECTION_MODEL_PROPERTY, oldValue,
		// this.selectionModel);
	}

	public TreeSelectionModel getSelectionModel() {
		return selectionModel;
	}

	protected TreePath[] getPathBetweenRows(int index0, int index1) {
		AndroidClassUtil.callEmptyMethod();
		return new TreePath[0];
	}

	public void setSelectionInterval(int index0, int index1) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void addSelectionInterval(int index0, int index1) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void removeSelectionInterval(int index0, int index1) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void removeSelectionPath(TreePath path) {
		this.getSelectionModel().removeSelectionPath(path);
	}

	public void removeSelectionPaths(TreePath[] paths) {
		this.getSelectionModel().removeSelectionPaths(paths);
	}

	public void removeSelectionRow(int row) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void removeSelectionRows(int[] rows) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void clearSelection(final TreeNode treeNode) {
		if (treeNode != null) {
			TreeNodeView tnv = nodeAndView.get(treeNode);
			tnv.refreshSelected(treeNode, false);
		}

		getSelectionModel().clearSelection();
	}

	public boolean isSelectionEmpty() {
		return getSelectionModel().isSelectionEmpty();
	}

	public void addTreeExpansionListener(TreeExpansionListener tel) {
		list.add(TreeExpansionListener.class, tel);
	}

	public void removeTreeExpansionListener(TreeExpansionListener tel) {
		list.remove(TreeExpansionListener.class, tel);
	}

	public TreeExpansionListener[] getTreeExpansionListeners() {
		return list.getListeners(TreeExpansionListener.class);
	}

	public void addTreeWillExpandListener(TreeWillExpandListener tel) {
		list.add(TreeWillExpandListener.class, tel);
	}

	public void removeTreeWillExpandListener(TreeWillExpandListener tel) {
		list.remove(TreeWillExpandListener.class, tel);
	}

	public TreeWillExpandListener[] getTreeWillExpandListeners() {
		return list.getListeners(TreeWillExpandListener.class);
	}

	public void fireTreeExpanded(TreePath path) {
		Object[] listeners = list.getListenerList();
		TreeExpansionEvent e = null;
		if (uiRefreshTreeExpansionListener != null) {
			e = new TreeExpansionEvent(this, path);
			uiRefreshTreeExpansionListener.treeExpanded(e);
		}
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeExpansionListener.class) {
				if (e == null)
					e = new TreeExpansionEvent(this, path);
				((TreeExpansionListener) listeners[i + 1]).treeExpanded(e);
			}
		}
	}

	public void fireTreeCollapsed(TreePath path) {
		Object[] listeners = list.getListenerList();
		TreeExpansionEvent e = null;
		if (uiRefreshTreeExpansionListener != null) {
			e = new TreeExpansionEvent(this, path);
			uiRefreshTreeExpansionListener.treeCollapsed(e);
		}
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeExpansionListener.class) {
				if (e == null)
					e = new TreeExpansionEvent(this, path);
				((TreeExpansionListener) listeners[i + 1]).treeCollapsed(e);
			}
		}
	}

	public void fireTreeWillExpand(TreePath path) throws ExpandVetoException {
		Object[] listeners = list.getListenerList();
		TreeExpansionEvent e = null;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeWillExpandListener.class) {
				if (e == null)
					e = new TreeExpansionEvent(this, path);
				((TreeWillExpandListener) listeners[i + 1]).treeWillExpand(e);
			}
		}
	}

	public void fireTreeWillCollapse(TreePath path) throws ExpandVetoException {
		Object[] listeners = list.getListenerList();
		TreeExpansionEvent e = null;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeWillExpandListener.class) {
				if (e == null)
					e = new TreeExpansionEvent(this, path);
				((TreeWillExpandListener) listeners[i + 1]).treeWillCollapse(e);
			}
		}
	}

	public void addTreeSelectionListener(TreeSelectionListener tsl) {
		list.add(TreeSelectionListener.class, tsl);
		if (list.getListenerCount(TreeSelectionListener.class) != 0
				&& selectionRedirector == null) {
			selectionRedirector = new TreeSelectionRedirector();
			selectionModel.addTreeSelectionListener(selectionRedirector);
		}
	}

	public void removeTreeSelectionListener(TreeSelectionListener tsl) {
		list.remove(TreeSelectionListener.class, tsl);
		if (list.getListenerCount(TreeSelectionListener.class) == 0
				&& selectionRedirector != null) {
			selectionModel.removeTreeSelectionListener(selectionRedirector);
			selectionRedirector = null;
		}
	}

	public TreeSelectionListener[] getTreeSelectionListeners() {
		return list.getListeners(TreeSelectionListener.class);
	}

	protected void fireValueChanged(TreeSelectionEvent e) {
		Object[] listeners = list.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TreeSelectionListener.class) {
				((TreeSelectionListener) listeners[i + 1]).valueChanged(e);
			}
		}
	}

	public void treeDidChange() {
		// revalidate();
		// repaint();
		updateUI();
	}

	public void setVisibleRowCount(int newCount) {
		visibleRowCount = newCount;
		AndroidClassUtil.callEmptyMethod();
	}

	public int getVisibleRowCount() {
		return visibleRowCount;
	}

	private void expandRoot() {
		TreeModel model = getModel();

		if (model != null && model.getRoot() != null) {
			expandPath(new TreePath(model.getRoot()));
		}
	}

	public TreePath getNextMatch(String prefix, int startingRow, Position.Bias bias) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
	}

	public Dimension getPreferredScrollableViewportSize() {
		AndroidClassUtil.callEmptyMethod();
		return new Dimension(0, 0);
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		AndroidClassUtil.callEmptyMethod();
		return 4;
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return (orientation == SwingConstants.VERTICAL) ? visibleRect.height : visibleRect.width;
	}

	public boolean getScrollableTracksViewportWidth() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public boolean getScrollableTracksViewportHeight() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	/**
	 * 
	 * @param path
	 * @param state
	 *            false, all parents of path are marked EXPANDED, but path
	 *            itself is marked collapsed.
	 */
	protected void setExpandedState(TreePath path, boolean state) {
		if (path != null) {
			TreePath parentPath = path.getParentPath();
			while (parentPath != null) {
				if (!isExpanded(parentPath)) {
					try {
						fireTreeWillExpand(parentPath);
					} catch (ExpandVetoException eve) {
						return;
					}
					TreeNode tmpNode = (TreeNode) parentPath.getLastPathComponent();
					expandedState.put(tmpNode, Boolean.TRUE);
					fireTreeExpanded(parentPath);
				}
				parentPath = parentPath.getParentPath();
			}

			if (!state) {
				Object cValue = expandedState.get((TreeNode) path.getLastPathComponent());

				if (cValue != null && ((Boolean) cValue).booleanValue()) {
					try {
						fireTreeWillCollapse(path);
					} catch (ExpandVetoException eve) {
						return;
					}
					TreeNode tmpNode = (TreeNode) path.getLastPathComponent();
					expandedState.put(tmpNode, Boolean.FALSE);
					fireTreeCollapsed(path);
					if (removeDescendantSelectedPaths(path, false) && !isPathSelected(path)) {
						addSelectionPath(path);
					}
				}
			} else {
				Object cValue = expandedState.get((TreeNode) path.getLastPathComponent());

				if (cValue == null || (((Boolean) cValue).booleanValue() == false)) {
					try {
						fireTreeWillExpand(path);
					} catch (ExpandVetoException eve) {
						return;
					}
					TreeNode tmpNode = (TreeNode) path.getLastPathComponent();
					expandedState.put(tmpNode, Boolean.TRUE);
					fireTreeExpanded(path);
				}
			}
		}
	}

	protected Enumeration<TreePath> getDescendantToggledPaths(TreePath parent) {
		AndroidClassUtil.callEmptyMethod();

		if (parent == null)
			return null;

		Vector<TreePath> descendants = new Vector<TreePath>();
		return descendants.elements();
	}

	protected void removeDescendantToggledPaths(Enumeration<TreePath> toRemove) {
		AndroidClassUtil.callEmptyMethod();
	}

	protected void clearToggledPaths() {
		expandedState.clear();
	}

	protected TreeModelListener createTreeModelListener() {
		return new TreeModelHandler();
	}

	protected boolean removeDescendantSelectedPaths(TreePath path, boolean includePath) {
		TreePath[] toRemove = getDescendantSelectedPaths(path, includePath);

		if (toRemove != null) {
			getSelectionModel().removeSelectionPaths(toRemove);
			return true;
		}
		return false;
	}

	private TreePath[] getDescendantSelectedPaths(TreePath path, boolean includePath) {
		TreeSelectionModel sm = getSelectionModel();
		TreePath[] selPaths = (sm != null) ? sm.getSelectionPaths() : null;

		if (selPaths != null) {
			boolean shouldRemove = false;

			for (int counter = selPaths.length - 1; counter >= 0; counter--) {
				if (selPaths[counter] != null && path.isDescendant(selPaths[counter])
						&& (!path.equals(selPaths[counter]) || includePath))
					shouldRemove = true;
				else
					selPaths[counter] = null;
			}
			if (!shouldRemove) {
				selPaths = null;
			}
			return selPaths;
		}
		return null;
	}

	void removeDescendantSelectedPaths(TreeModelEvent e) {
		TreePath pPath = e.getTreePath();
		Object[] oldChildren = e.getChildren();
		TreeSelectionModel sm = getSelectionModel();

		if (sm != null && pPath != null && oldChildren != null && oldChildren.length > 0) {
			for (int counter = oldChildren.length - 1; counter >= 0; counter--) {
				removeDescendantSelectedPaths(pPath.pathByAddingChild(oldChildren[counter]), true);
			}
		}
	}

	protected class TreeModelHandler implements TreeModelListener {
		public void treeNodesChanged(TreeModelEvent e) {
		}

		public void treeNodesInserted(TreeModelEvent e) {
		}

		public void treeStructureChanged(TreeModelEvent e) {
			if (e == null)
				return;

			TreePath parent = e.getTreePath();

			if (parent == null)
				return;

			if (parent.getPathCount() == 1) {
				clearToggledPaths();
				if (treeModel.getRoot() != null && !treeModel.isLeaf(treeModel.getRoot())) {
					expandedState.put((TreeNode) parent.getLastPathComponent(), Boolean.TRUE);
				}
			} else if (expandedState.get((TreeNode) parent.getLastPathComponent()) != null) {
				Vector<TreePath> toRemove = new Vector<TreePath>(1);
				boolean isExpanded = isExpanded(parent);

				toRemove.addElement(parent);
				removeDescendantToggledPaths(toRemove.elements());
				if (isExpanded) {
					TreeModel model = getModel();

					if (model == null || model.isLeaf(parent.getLastPathComponent()))
						collapsePath(parent);
					else
						expandedState.put((TreeNode) parent.getLastPathComponent(), Boolean.TRUE);
				}
			}
			removeDescendantSelectedPaths(parent, false);
		}

		public void treeNodesRemoved(TreeModelEvent e) {
			if (e == null)
				return;

			TreePath parent = e.getTreePath();
			Object[] children = e.getChildren();

			if (children == null)
				return;

			TreePath rPath;
			Vector<TreePath> toRemove = new Vector<TreePath>(Math.max(1, children.length));

			for (int counter = children.length - 1; counter >= 0; counter--) {
				rPath = parent.pathByAddingChild(children[counter]);
				if (expandedState.get((TreeNode) rPath.getLastPathComponent()) != null)
					toRemove.addElement(rPath);
			}
			if (toRemove.size() > 0)
				removeDescendantToggledPaths(toRemove.elements());

			TreeModel model = getModel();

			if (model == null || model.isLeaf(parent.getLastPathComponent()))
				expandedState.remove(parent);

			removeDescendantSelectedPaths(e);
		}
	}

	public static class DynamicUtilTreeNode extends DefaultMutableTreeNode {
		protected boolean hasChildren;
		protected Object childValue;
		protected boolean loadedChildren;

		public static void createChildren(DefaultMutableTreeNode parent, Object children) {
			if (children instanceof Vector) {
				Vector childVector = (Vector) children;

				for (int counter = 0, maxCounter = childVector
						.size(); counter < maxCounter; counter++)
					parent.add(new DynamicUtilTreeNode(childVector.elementAt(counter),
							childVector.elementAt(counter)));
			} else if (children instanceof Hashtable) {
				Hashtable childHT = (Hashtable) children;
				Enumeration keys = childHT.keys();
				Object aKey;

				while (keys.hasMoreElements()) {
					aKey = keys.nextElement();
					parent.add(new DynamicUtilTreeNode(aKey, childHT.get(aKey)));
				}
			} else if (children instanceof Object[]) {
				Object[] childArray = (Object[]) children;

				for (int counter = 0, maxCounter = childArray.length; counter < maxCounter; counter++)
					parent.add(new DynamicUtilTreeNode(childArray[counter], childArray[counter]));
			}
		}

		public DynamicUtilTreeNode(Object value, Object children) {
			super(value);
			loadedChildren = false;
			childValue = children;
			if (children != null) {
				if (children instanceof Vector)
					setAllowsChildren(true);
				else if (children instanceof Hashtable)
					setAllowsChildren(true);
				else if (children instanceof Object[])
					setAllowsChildren(true);
				else
					setAllowsChildren(false);
			} else
				setAllowsChildren(false);
		}

		public boolean isLeaf() {
			return !getAllowsChildren();
		}

		public int getChildCount() {
			if (!loadedChildren)
				loadChildren();
			return super.getChildCount();
		}

		protected void loadChildren() {
			loadedChildren = true;
			createChildren(this, childValue);
		}

		public TreeNode getChildAt(int index) {
			if (!loadedChildren)
				loadChildren();
			return super.getChildAt(index);
		}

		public Enumeration children() {
			if (!loadedChildren)
				loadChildren();
			return super.children();
		}
	}

	void setUIProperty(String propertyName, Object value) {
		AndroidClassUtil.callEmptyMethod();
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

	private void notifyNodeSelectChangedAdAPI(final TreeNode treeNode) {
		TreeNode oldSelected = JTree.this.currSelectedTreeNode;
		currSelectedTreeNode = treeNode;

		if (oldSelected != treeNode) {
			if (oldSelected != null) {
				TreeNodeView tnv = nodeAndView.get(oldSelected);
				tnv.refreshSelected(oldSelected, false);
			}
		}

		TreeNodeView tnv = nodeAndView.get(treeNode);
		tnv.refreshSelected(treeNode, true);

		boolean[] isNew = { true };
		TreePath newLeadSelectionPath = (treeNode != null) ? JTree.this.getTreePathAdAPI(treeNode)
				: null;
		TreePath oldLeadSelectionPath = (oldSelected != null)
				? JTree.this.getTreePathAdAPI(oldSelected)
				: null;
		TreePath[] path = { newLeadSelectionPath };

		TreeSelectionModel selModel = JTree.this.getSelectionModel();
		selModel.setSelectionPath(newLeadSelectionPath);

		fireValueChanged(new TreeSelectionEvent(JTree.this, path, isNew, oldLeadSelectionPath,
				newLeadSelectionPath));
	}

	private void refreshRootNodeAdAPI(TreeNode treeNode, boolean isRoot) {
		TreeModel model = getModel();
		if (model == null) {
			return;
		}

		TreeNodeView treeNodeView = nodeAndView.get(treeNode);
		treeNodeView.updateExpandChildsUI(treeNode, true, true);

		// int count = treeNode.getChildCount();
		// for (int i = 0; i < count; i++) {
		// refreshRootNodeAdAPI(treeNode.getChildAt(i), false);
		// }

		if (isRoot) {
			LinearLayout nodeView = treeNodeView.nodeView;
			if (rootVisible) {
				nodeView.setVisibility(View.VISIBLE);
			} else {
				nodeView.setVisibility(View.GONE);
			}
		}
	}

}