package hc.android;

import hc.android.ScreenAdapter;

import java.awt.Font;

public class FontScreenKey {
	public final Font font;
	public final ScreenAdapter adapter;

	public FontScreenKey(final ScreenAdapter sa, final Font font) {
		this.adapter = sa;
		this.font = font;
	}
}
