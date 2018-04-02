package hc.android;

import java.util.Vector;
import android.content.Context;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;

public class HCTabWidget extends LinearLayout {
	final Vector<HCTabIndicatorView> indeicatorViews = new Vector<HCTabIndicatorView>();
	private int mSelectedTab = -1;

	public HCTabWidget(Context context) {
		super(context);
	}

	public HCTabIndicatorView getChildTabViewAt(int index) {
		return indeicatorViews.elementAt(index);
	}

	public void focusCurrentTab(int index) {
		final int oldTab = mSelectedTab;

		// set the tab
		setCurrentTab(index);

		// change the focus if applicable.
		if (oldTab != index) {
			HCTabIndicatorView elementAt = indeicatorViews.elementAt(index);
			elementAt.requestFocus();
		}
	}

	public int getTabCount() {
		return getChildCount();
	}

	public void notifySelected(View newSelected, HCTabHost tabHost) {
		if (getChildAt(mSelectedTab) == newSelected) {
			return;
		}

		for (int i = 0; i < getTabCount(); i++) {
			if (getChildAt(i) == newSelected) {
				tabHost.setCurrentTab(i);
				if (tabHost.singleModel != null) {
					tabHost.singleModel.setSelectedIndex(i);
				}
				return;
			}
		}
	}

	public void setCurrentTab(int index) {
		if (index < 0 || index >= getTabCount() || index == mSelectedTab) {
			return;
		}

		if (mSelectedTab != -1) {
			HCTabIndicatorView indeicatorView = getChildTabViewAt(mSelectedTab);
			indeicatorView.setSelected(false);
		}
		mSelectedTab = index;
		HCTabIndicatorView indeicatorView = getChildTabViewAt(mSelectedTab);
		indeicatorView.setSelected(true);

		if (isShown()) {
			sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
		}
	}
}
