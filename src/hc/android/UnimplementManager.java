package hc.android;

import hc.core.util.LogManager;

public class UnimplementManager {

	public static void printFireChangedUpdate() {
		// 注意:暂不实现removeUpdate,insertUpdate
		LogManager.warning("removeUpdate and insertUpdate is NOT implemented in current version.");
	}

}
