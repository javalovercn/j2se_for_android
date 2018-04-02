package hc.android;

public class HardwareConfigManager {
	static final java.util.concurrent.ConcurrentMap<Long, HardwareConfig> configs = new java.util.concurrent.ConcurrentHashMap<Long, HardwareConfig>();

	public static void setConfig(HardwareConfig config) {
		configs.put(Thread.currentThread().getId(), config);
	}

	public static HardwareConfig getConfig() {
		return configs.get(Thread.currentThread().getId());
	}
}
