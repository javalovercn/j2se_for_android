package hc.android;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager2;

public abstract class BaseLayoutManager2 implements LayoutManager2 {
	protected boolean isModified = true;
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
		isLayout = true;
		isModified = false;
		this.parent = parent;
		AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				viewRelation.removeAll();
			}
		});
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		isModified = true;
	}

}
