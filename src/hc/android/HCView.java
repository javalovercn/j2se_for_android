package hc.android;

import javax.swing.JComponent;

import android.view.View;

public class HCView {
	public JComponent jcomponent;
	public View view;

	public HCView(View view, JComponent jcomponent) {
		this.view = view;
		this.jcomponent = jcomponent;
	}
}
