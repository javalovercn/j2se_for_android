package hc.android;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.SingleSelectionModel;
import hc.android.HCRUtil;
import hc.core.L;
import hc.core.util.LogManager;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class HCTabHost extends LinearLayout {
	final ViewRelation vr = new ViewRelation();
	public HCTabWidget mTabWidget;
	private FrameLayout mTabContent;
	private ArrayList<HCTabSpec> mTabSpecs = new ArrayList<HCTabSpec>(2);
	private boolean isLeftToRight;

	SingleSelectionModel singleModel;

	protected int mCurrentTab = -1;
	private final ViewRelation viewRelation = new ViewRelation();

	public int getCurrentTab() {
		return mCurrentTab;
	}

	public void setSingleSelectionMode(SingleSelectionModel sModel) {
		this.singleModel = sModel;
	}

	public void clearAllTabs() {
		mTabSpecs.clear();
		rebuldWidget(ActivityManager.applicationContext);
		viewRelation.removeAll();

		mCurrentTab = -1;
	}

	public void setLeftToRight(boolean isLeftToRight) {
		this.isLeftToRight = isLeftToRight;
	}

	final Component ori_comp;

	public HCTabHost(Context context, Component ori_comp) {
		super(context);
		this.ori_comp = ori_comp;

		// rebuldWidget(applicationContext);

		super.setOrientation(LinearLayout.VERTICAL);

		mCurrentTab = -1;

		// mTabKeyListener = new OnKeyListener() {
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		// switch (keyCode) {
		// case KeyEvent.KEYCODE_DPAD_CENTER:
		// case KeyEvent.KEYCODE_DPAD_LEFT:
		// case KeyEvent.KEYCODE_DPAD_RIGHT:
		// case KeyEvent.KEYCODE_DPAD_UP:
		// case KeyEvent.KEYCODE_DPAD_DOWN:
		// case KeyEvent.KEYCODE_ENTER:
		// return false;
		// }
		// mTabContent.requestFocus(View.FOCUS_FORWARD);
		// return mTabContent.dispatchKeyEvent(event);
		// }
		// };
	}

	private void rebuldWidget(Context context) {
		if (mTabWidget != null) {
			vr.unregisterView(mTabWidget);
		}
		if (mTabContent != null) {
			vr.unregisterView(mTabContent);
		}

		mTabWidget = new HCTabWidget(context) {
			int mSelectedTab = -1;

			public void setCurrentTab(int index) {
				if (index < 0 || index >= getTabCount() || index == mSelectedTab) {
					return;
				}
				super.setCurrentTab(index);
				mSelectedTab = index;
			}

			public void onFocusChange(View v, boolean hasFocus) {
				if (v == this && hasFocus && mTabWidget.getTabCount() > 0) {
					mTabWidget.getChildTabViewAt(mSelectedTab).requestFocus();
					return;
				}

				if (hasFocus) {
					int i = 0;
					int numTabs = mTabWidget.getTabCount();
					while (i < numTabs) {
						if (getChildTabViewAt(i).defaultLinearLayout == v) {
							mTabWidget.setCurrentTab(i);
							// mSelectionChangedListener.onTabSelectionChanged(i,
							// false);
							// {
							// mTabWidget.setCurrentTab(i);
							// if (false) {
							// mTabContent.requestFocus(View.FOCUS_FORWARD);
							// }
							// }
							// if (mTabWidget.isShown()) {
							// mTabWidget.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
							// }
							break;
						}
						i++;
					}
				}
			}
		};

		{
			LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			lp.gravity = (isLeftToRight ? Gravity.LEFT : Gravity.RIGHT) | Gravity.TOP;
			AndroidUIUtil.addView(this, mTabWidget, 0, lp, vr);
		}

		mTabContent = new FrameLayout(context);
		mTabContent.setBackgroundResource(HCRUtil.getResource(HCRUtil.R_drawable_border_titled));

		{
			LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			lp.gravity = (isLeftToRight ? Gravity.LEFT : Gravity.RIGHT) | Gravity.TOP;
			int margin = HCCardLayout.getBorderWidth();
			lp.setMargins(margin, margin, margin, margin);
			AndroidUIUtil.addView(this, mTabContent, lp, vr);
		}
	}

	public void removeTab(int index) {
		mTabSpecs.remove(index);
	}

	public void addTab(String tag, Icon icon, String tip, View content, int idx) {
		HCTabSpec tabSpec = new HCTabSpec(tag, icon, tip, content);

		HCTabIndicatorView tabIndicator = createIndicatorView(tabSpec);

		{
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			mTabWidget.indeicatorViews.add(tabIndicator);
			if (idx < 0) {
				mTabWidget.addView(tabIndicator.defaultLinearLayout, lp);
			} else {
				mTabWidget.addView(tabIndicator.defaultLinearLayout, idx, lp);
			}
		}
		if (idx < 0) {
			mTabSpecs.add(tabSpec);
		} else {
			mTabSpecs.add(idx, tabSpec);
		}

		{
			View view = tabSpec.content;
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			lp.gravity = (Gravity.TOP | (isLeftToRight ? Gravity.LEFT : Gravity.RIGHT));
			view.setVisibility(View.INVISIBLE);
			int margin = AndroidUIUtil.getBorderStrokeWidthInPixel();
			lp.setMargins(margin, margin, margin, margin);// 增加内空间，防止其它元素与此贴合
			AndroidUIUtil.addView(mTabContent, view, lp, viewRelation);
		}

	}

	public void setCurrentTab(int index) {
		if (mCurrentTab == index) {
			return;
		}

		int oldDispTabIdx = mCurrentTab;
		if (index >= 0 && index < mTabSpecs.size()) {
			if (oldDispTabIdx >= 0 && oldDispTabIdx < mTabSpecs.size()) {
				mTabSpecs.get(oldDispTabIdx).content.setVisibility(View.INVISIBLE);
			}
			// // notify old tab content
			// if (mCurrentTab != -1) {
			// mTabSpecs.get(mCurrentTab).mContentStrategy.tabClosed();
			// }
			mTabWidget.focusCurrentTab(index);
			mTabSpecs.get(index).content.setVisibility(View.VISIBLE);

			mCurrentTab = index;
		}
	}

	void onTabSelectionChanged(int tabIndex) {
		setCurrentTab(tabIndex);
	}

	public HCTabIndicatorView createIndicatorView(final HCTabSpec tabSpec) {
		HCTabIndicatorView indView = new HCTabIndicatorView(this);

		boolean isLeftToRight = true;
		boolean btnEnabled = true;

		indView.init(isLeftToRight, btnEnabled, tabSpec.icon, tabSpec.tag, tabSpec.tip, this,
				mTabWidget);

		return indView;
	}

	public static class HCTabSpec {
		public String tag;
		public Icon icon;
		public String tip;
		public View content;

		public HCTabSpec(String tag, Icon icon, String tip, View content) {
			this.tag = tag;
			this.icon = icon;
			this.tip = tip;
			this.content = content;
		}
	}
}
