package hc.android;

import hc.android.HCTabHost;
import hc.android.AndroidUIUtil;
import hc.android.HCTabHost.HCTabSpec;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JTabbedPane;

public class HCCardLayout extends BaseLayoutManager2 implements Serializable {
	public Vector<Card> vector = new Vector<Card>();

	public static class Card implements Serializable {
		public JTabbedPane.TabParameter para;
		public Component comp;

		public Card(JTabbedPane.TabParameter para, Component cardComponent) {
			this.para = para;
			comp = cardComponent;
		}
	}

	int hgap;
	int vgap;

	public HCCardLayout() {
		this(0, 0);
	}

	public HCCardLayout(int hgap, int vgap) {
		this.hgap = hgap;
		this.vgap = vgap;
	}

	public int getHgap() {
		return hgap;
	}

	public void setHgap(int hgap) {
		this.hgap = hgap;
	}

	public int getVgap() {
		return vgap;
	}

	public void setVgap(int vgap) {
		this.vgap = vgap;
	}

	public synchronized void addLayoutComponent(Component comp, Object constraints) {
		if (constraints == null) {
			constraints = "";
		}
		if (constraints instanceof JTabbedPane.TabParameter) {
			addLayoutComponent((JTabbedPane.TabParameter) constraints, comp);
		} else if (constraints instanceof String) {
			addLayoutComponent((String) constraints, comp);
		} else {
			throw new IllegalArgumentException(
					"constraint must be a JTabbedPane.TabParameter or String");
		}
		if (isLayout) {
			layoutContainer(parent);
		}
	}

	/**
	 * @deprecated replaced by
	 *             <code>addLayoutComponent(Component, Object)</code>.
	 */
	@Deprecated
	public synchronized void addLayoutComponent(JTabbedPane.TabParameter para, Component comp) {
		for (int i = 0; i < vector.size(); i++) {
			Card card = (Card) vector.get(i);
			if (card.para.tag.equals(para.tag)) {
				card.para.icon = para.icon;
				card.comp = comp;
				return;
			}
		}

		if (para.idx < 0) {
			vector.add(new Card(para, comp));
		} else {
			vector.add(para.idx, new Card(para, comp));
		}
	}

	public synchronized void addLayoutComponent(String name, Component comp) {
		JTabbedPane.TabParameter para = new JTabbedPane.TabParameter(name, (Icon) null, "", -1);
		addLayoutComponent(para, comp);
	}

	public synchronized void removeLayoutComponent(Component comp) {
		for (int i = 0; i < vector.size(); i++) {
			if (((Card) vector.get(i)).comp == comp) {
				vector.remove(i);
				if (isLayout) {
					layoutContainer(parent);
				}
				return;
			}
		}
	}

	final static int doubleGap = 2;

	private final int getTabRightAdAPI() {
		return getBorderWidth() * doubleGap;// *2增加边量，否则挤在一起
	}

	private final int getTabLeftAdAPI() {
		return getBorderWidth() * doubleGap;// *2增加边量，否则挤在一起
	}

	private HCTabIndicatorView indicatorViewSample;

	private final int getTabTopAdAPI(Container container) {
		HCTabHost tabHost = (HCTabHost) container.getContainerViewAdAPI();
		if (indicatorViewSample == null) {
			HCTabSpec tabSpec = new HCTabSpec("AA", null, "", null);
			indicatorViewSample = tabHost.createIndicatorView(tabSpec);
		}
		Dimension dimension = new Dimension(0, 0);
		AndroidUIUtil.getViewWidthAndHeight(indicatorViewSample.defaultLinearLayout, dimension);
		return getBorderWidth() * doubleGap + dimension.height;// *2增加边量，否则挤在一起
	}

	private final int getTabBottomAdAPI() {
		return getBorderWidth() * doubleGap;// *2增加边量，否则挤在一起
	}

	public final static int getBorderWidth() {
		return AndroidUIUtil.getBorderStrokeWidthInPixel();
	}

	public Dimension preferredLayoutSize(Container parent) {
		Insets insets = parent.getInsets();
		int ncomponents = parent.getComponentCount();
		int w = 0;
		int h = 0;

		for (int i = 0; i < ncomponents; i++) {
			Component comp = parent.getComponent(i);
			Dimension d = comp.getPreferredSize();
			if (d.width > w) {
				w = d.width;
			}
			if (d.height > h) {
				h = d.height;
			}
		}
		return new Dimension(
				getTabLeftAdAPI() + insets.left + insets.right + w + hgap * 2 + getTabRightAdAPI(),
				getTabTopAdAPI(parent) + insets.top + insets.bottom + h + vgap * 2
						+ getTabBottomAdAPI());
	}

	public Dimension minimumLayoutSize(Container parent) {
		Insets insets = parent.getInsets();
		int ncomponents = parent.getComponentCount();
		int w = 0;
		int h = 0;

		for (int i = 0; i < ncomponents; i++) {
			Component comp = parent.getComponent(i);
			Dimension d = comp.getMinimumSize();
			if (d.width > w) {
				w = d.width;
			}
			if (d.height > h) {
				h = d.height;
			}
		}
		return new Dimension(
				getTabLeftAdAPI() + insets.left + insets.right + w + hgap * 2 + getTabRightAdAPI(),
				getTabTopAdAPI(parent) + insets.top + insets.bottom + h + vgap * 2
						+ getTabBottomAdAPI());
	}

	public Dimension maximumLayoutSize(Container target) {
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	public float getLayoutAlignmentX(Container parent) {
		return 0.5f;
	}

	public float getLayoutAlignmentY(Container parent) {
		return 0.5f;
	}

	public void invalidateLayout(Container target) {
	}

	public void layoutContainer(final Container target) {
		AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				layoutContainerUI(target);
			}
		});
	}

	private void layoutContainerUI(Container parent) {
		super.layoutContainer(parent);

		HCTabHost tabHost = (HCTabHost) parent.getContainerViewAdAPI();
		tabHost.setLeftToRight(parent.getComponentOrientation().isLeftToRight());
		tabHost.clearAllTabs();

		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			int ncomponents = parent.getComponentCount();
			Component comp = null;

			for (int i = 0; i < ncomponents; i++) {
				comp = parent.getComponent(i);
				comp.setBounds(hgap + insets.left, vgap + insets.top,
						parent.getWidth() - (getTabLeftAdAPI() + hgap * 2 + insets.left
								+ insets.right + getTabRightAdAPI()),
						parent.getHeight() - (getTabTopAdAPI(parent) + vgap * 2 + insets.top
								+ insets.bottom + getTabBottomAdAPI()));
			}
		}

		int nChildren = vector.size();
		for (int i = 0; i < nChildren; i++) {
			Card card = vector.get(i);

			tabHost.addTab(card.para.tag, card.para.icon, card.para.tip, card.comp.getPeerAdAPI(),
					-1);
		}

		refresh(parent, -1);
	}

	void checkLayout(Container parent) {
		if (parent.getLayout() != this) {
			throw new IllegalArgumentException("wrong parent for CardLayout");
		}
	}

	/**
	 * Flips to the first card.
	 */
	public synchronized void first(Container parent) {
		checkLayout(parent);

		refresh(parent, 0);
	}

	public synchronized void next(Container parent) {
		checkLayout(parent);

		HCTabHost tabHost = (HCTabHost) parent.getContainerViewAdAPI();
		int currIdx = tabHost.getCurrentTab() + 1;
		if (currIdx >= vector.size()) {
			currIdx = vector.size() - 1;
		}

		refresh(parent, currIdx);
	}

	public synchronized void previous(Container parent) {
		checkLayout(parent);

		HCTabHost tabHost = (HCTabHost) parent.getContainerViewAdAPI();
		int currIdx = tabHost.getCurrentTab() - 1;
		if (currIdx < 0) {
			currIdx = 0;
		}

		refresh(parent, currIdx);
	}

	void refresh(Container target, int currIdx) {
		if (currIdx < 0) {
			currIdx = 0;
		}
		if (currIdx >= vector.size()) {
			currIdx = 0;
		}

		if (currIdx >= 0 && currIdx < vector.size()) {
			HCTabHost tabHost = (HCTabHost) target.getContainerViewAdAPI();
			tabHost.setCurrentTab(currIdx);
		}
	}

	/**
	 * Flips to the last card.
	 */
	public synchronized void last(Container parent) {
		checkLayout(parent);

		refresh(parent, vector.size() - 1);
	}

	public synchronized void show(Container parent, String name) {
		checkLayout(parent);

		int ncomponents = vector.size();
		for (int i = 0; i < ncomponents; i++) {
			Card card = (Card) vector.get(i);
			if (card.para.equals(name)) {
				refresh(parent, i);
				return;
			}
		}
	}

	public String toString() {
		return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + "]";
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}
}
