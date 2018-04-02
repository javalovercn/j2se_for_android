package hc.android;

import java.util.HashMap;
import java.util.Iterator;

public class DexOptimizeSet {
	private final static HashMap<String, DexOptimizeItem> set = new HashMap<String, DexOptimizeItem>(
			16);
	private final static String split = "#";
	private final static String splitSet = "%%%";

	static {
		init();
	}

	public static boolean contain(String optimizeID) {
		return set.containsKey(optimizeID);
	}

	public static DexOptimizeItem getItem(String optID) {
		return set.get(optID);
	}

	/**
	 * with save.
	 * 
	 * @param optID
	 */
	public static void removeItem(String optID) {
		set.remove(optID);
		save();
	}

	public static void putItem(DexOptimizeItem item) {
		set.put(item.optimizeID, item);
	}

	private static String toSerial() {
		StringBuilder sb = new StringBuilder(1024);
		Iterator<String> it = set.keySet().iterator();
		while (it.hasNext()) {
			if (sb.length() > 0) {
				sb.append(splitSet);
			}

			DexOptimizeItem item = set.get(it.next());

			sb.append(item.optimizeID);
			sb.append(split);
			sb.append(item.optimizeRandomDir);
			sb.append(split);
			sb.append(item.lastModifySum);

		}
		return sb.toString();
	}

	public static void save() {
		PropManager.setValue(PropManager.p_DEX_OPT_SET, toSerial());
		PropManager.save();
	}

	private static void init() {
		String setsStr = PropManager.getValue(PropManager.p_DEX_OPT_SET, "");
		String[] items = setsStr.split(splitSet);

		for (int i = 0; i < items.length; i++) {
			if (items[i].length() > 0) {
				String[] item = items[i].split(split);

				DexOptimizeItem dexItem = new DexOptimizeItem();
				dexItem.optimizeID = item[0];
				dexItem.optimizeRandomDir = item[1];
				dexItem.lastModifySum = Long.parseLong(item[2]);

				set.put(dexItem.optimizeID, dexItem);
			}
		}
	}
}
