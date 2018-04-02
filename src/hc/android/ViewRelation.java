package hc.android;

import java.util.HashMap;
import java.util.Set;

import android.view.View;
import android.view.ViewGroup;

public class ViewRelation {
	private final HashMap<View, ViewGroup> map = new HashMap<View, ViewGroup>();

	public void registerViewRelation(ViewGroup parent, View view) {
		if (view == null) {
			return;
		}

		unregisterView(view);

		if (parent == null) {
			return;
		}
		map.put(view, parent);
	}

	public void unregisterView(final View view) {
		if (view == null) {
			return;
		}

		final ViewGroup oldParent = map.get(view);
		if (oldParent != null) {
			AndroidUIUtil.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					oldParent.removeView(view);
				}
			});
			map.remove(view);
		}
	}

	public void removeAll() {
		final View[] views = new View[map.size()];
		map.keySet().toArray(views);

		AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < views.length; i++) {
					ViewGroup oldParent = map.get(views[i]);
					if (oldParent != null) {
						oldParent.removeView(views[i]);
					}
				}
			}
		});

		map.clear();
	}
}
