package hc.android;

import java.awt.Component;

public class FocusManager {
	private static Component currFocusComponent;

	public static void setFocusOwner(Component c) {
		currFocusComponent = c;
	}

	public static Component getFocusComponentOwner() {
		return currFocusComponent;
	}
}
