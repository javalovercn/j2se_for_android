package hc.android;

import hc.core.ConfigManager;
import hc.core.GlobalConditionWatcher;
import hc.core.IWatcher;
import hc.server.PlatformManager;

import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.util.Log;

public class HCWiFiManager {
	final static String TAG = "[WiFi]";

	// public static boolean isWiFiConnected(final Context applicationContext){
	// try{
	// final WifiManager manager = (WifiManager)
	// applicationContext.getSystemService(Context.WIFI_SERVICE);
	// final WifiInfo info = manager.getConnectionInfo();
	// if(info.getSSID() != null){
	// return true;
	// }
	// }catch (final Throwable e) {
	// }
	// return false;
	// }

	private static WifiConfiguration isExsits(final WifiManager manager, final String SSID) {
		final List<WifiConfiguration> existingConfigs = manager.getConfiguredNetworks();
		for (final WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
	}

	static void enableWifiAp(final WifiManager wifiManager, final WifiConfiguration config,
			final boolean enable) {
		try {
			final Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled",
					WifiConfiguration.class, boolean.class);
			method2.setAccessible(true);
			method2.invoke(wifiManager, config, enable);
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}

	public static boolean addAP(final WifiManager manager, final WifiConfiguration wcg,
			final String uuid, final int disappearSeconds) {
		final int wcgID = manager.addNetwork(wcg);
		final boolean result = manager.enableNetwork(wcgID, false);
		if (result) {
			enableWifiAp(manager, wcg, true);
			final AutoDisappearAPWatcher watcher = new AutoDisappearAPWatcher(wcg,
					disappearSeconds);
			GlobalConditionWatcher.addWatcher(watcher);
		}
		return result;
	}

	public static WifiConfiguration createWifiInfo(final WifiManager manager, final String SSID,
			final String password, final String securityOption) {
		Log.v(TAG, "SSID = " + SSID + "## Password = " + password + "## Type = " + securityOption);

		final WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";

		final WifiConfiguration tempConfig = isExsits(manager, SSID);
		if (tempConfig != null) {
			manager.removeNetwork(tempConfig.networkId);
		}

		if (securityOption.equals(ConfigManager.WIFI_SECURITY_OPTION_NO_PASSWORD)) {// 没有密码
			config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		} else if (securityOption.equals(ConfigManager.WIFI_SECURITY_OPTION_WEP)) {// 用wep加密
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + password + "\"";
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		} else if (securityOption.equals(ConfigManager.WIFI_SECURITY_OPTION_WPA_WPA2_PSK)) {// 用wpa加密
			config.preSharedKey = "\"" + password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}

		return config;
	}

	/**
	 * 
	 * @param multicastIP
	 *            for example, 224.X.X.X or 239.X.X.X
	 * @param port
	 * @param bs
	 */
	public static void multicast(final String multicastIP, final int port, final byte[] sendData) {
//		final WifiManager wifi = getWiFiManager();
//		MulticastLock multicastLock = wifi
//				.createMulticastLock(String.valueOf(System.currentTimeMillis()));
//		multicastLock.setReferenceCounted(true);
//		multicastLock.acquire();
		PlatformManager.getService().enableMulticast();

		try {
			final MulticastSocket multicastSocket = new MulticastSocket(port);
			multicastSocket.setLoopbackMode(true);
			final InetAddress group = InetAddress.getByName(multicastIP);
			multicastSocket.joinGroup(group);

			final DatagramPacket packet = new DatagramPacket(sendData, sendData.length, group,
					port);

			multicastSocket.send(packet);
		} catch (final Throwable e) {
			e.printStackTrace();
		}

//		if (multicastLock != null) {
//			multicastLock.release();
//			multicastLock = null;
//		}
	}

	public static WifiManager getWiFiManager() {
		return (WifiManager) ActivityManager.applicationContext.getSystemService(Context.WIFI_SERVICE);
	}

	static void removeWiFiAp(final WifiConfiguration config) {
		HCWiFiManager.enableWifiAp(HCWiFiManager.getWiFiManager(), config, false);
		HCWiFiManager.getWiFiManager().removeNetwork(config.networkId);
	}
}

class AutoDisappearAPWatcher implements IWatcher {
	private final WifiConfiguration config;
	private final long startMS = System.currentTimeMillis();
	private final int disappearSeconds;
	boolean isDone;

	AutoDisappearAPWatcher(final WifiConfiguration config, final int disappearSeconds) {
		this.config = config;
		this.disappearSeconds = disappearSeconds;
	}

	@Override
	public boolean watch() {
		synchronized (this) {
			if (isDone) {
				return true;
			}

			if (System.currentTimeMillis() - startMS > disappearSeconds * 1000) {
				HCWiFiManager.removeWiFiAp(config);
				return true;
			}
		}
		return false;
	}

	@Override
	public void setPara(final Object p) {
	}

	@Override
	public void cancel() {
	}

	@Override
	public boolean isCancelable() {
		return false;
	}
}
