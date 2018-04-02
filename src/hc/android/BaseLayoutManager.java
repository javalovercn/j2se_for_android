package hc.android;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;

public abstract class BaseLayoutManager implements LayoutManager {
	boolean isModified = false;
	protected final ViewRelation viewRelation = new ViewRelation();
	protected boolean isLayout = false;
	protected Container parent;

	@Override
	public void addLayoutComponent(String name, Component comp) {
		isModified = true;
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		isModified = true;
	}

	@Override
	public void layoutContainer(Container parent) {
		isModified = false;
		isLayout = true;
		this.parent = parent;

		AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				viewRelation.removeAll();
			}
		});
	}

}
