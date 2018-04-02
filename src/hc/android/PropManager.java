package hc.android;

import hc.server.PlatformManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropManager {
	public static final String TRUE = "true";

	private static final File fileProp = new File(PlatformManager.getService().getBaseDir(),
			"hc_android.properties");

	public static final String p_DEX_OPT_SET = "DexOptPre.set";

	static boolean statusChanged = false;

	private static Properties propertie;

	public static synchronized final void save() {
		if (propertie == null || (statusChanged == false)) {
			return;
		}
		try {
			FileOutputStream outputFile = new FileOutputStream(fileProp);
			propertie.store(outputFile, null);
			outputFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized final void setValue(String key, String value) {
		final String oldValue = (String) propertie.get(key);
		if (oldValue != null && value.equals(oldValue)) {

		} else {
			statusChanged = true;
			propertie.setProperty(key, value);
		}
	}

	public static final boolean isTrue(String key) {
		String v = getValue(key);
		if (v == null) {
			return false;
		} else {
			return v.equals(TRUE);
		}
	}

	/**
	 * 如果没有，则返回null
	 * 
	 * @param key
	 * @return
	 */
	public static final String getValue(String key) {
		if (propertie.containsKey(key)) {
			return propertie.getProperty(key);// 得到某一属
		} else {
			return null;
		}
	}

	public static final String getValue(String key, String defaultValue) {
		final String v = getValue(key);
		if (v == null) {
			return defaultValue;
		} else {
			return v;
		}
	}

	public static final void remove(String key) {
		propertie.remove(key);
	}

	private static final void init() {
		propertie = new Properties();

		try {
			if (fileProp.exists()) {
				FileInputStream inputFile = new FileInputStream(fileProp);
				propertie.load(inputFile);
				inputFile.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	static {
		init();
	}
}
