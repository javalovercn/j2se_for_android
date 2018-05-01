package hc.android;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import hc.core.util.CCoreUtil;
import hc.util.ResourceUtil;

/**
 * 注意：该类被HCFont.checkIsStandardJ2SE反射使用，请不要改名
 *
 */
public class J2SEInitor {
	private static boolean isInited = false;

	public static final String getVersion() {
		return "3.7";
	}

	/**
	 * 如果增加了新参数，请同步更改到ServerMainActivity.startHCByReflect
	 * 
	 * @param paras
	 */
	public synchronized static void init(Object[] paras) {
		if (isInited) {
			throw new Error("can NOT invoke J2SEInitor.init(Object[] paras) two times or more.");
		}
		isInited = true;

		final Object para1 = paras[0];
		final Object para2 = paras[1];
		final Object para3 = paras[2];

		// ------------para1-----------------
		Map<String, Integer> resMap = (Map<String, Integer>) para1;
		HCRUtil.resMap = resMap;// convert J2SE res (String) to Android res
								// (Integer), for more, see Class
								// "hc.android.HCRUtil"

		// ------------para2-----------------
		actionMap = (Map<String, Object>) para2;
		J2SEActionMap.init(actionMap);// *** see J2SEActionMap.onBackPressed for more

		// ------------para3-----------------
		if(para3 instanceof Context) {
			if(ActivityManager.applicationContext == null) {
				ActivityManager.applicationContext = (Context) para3;
			}
		}
		
		if(para3 instanceof Activity) {
			hc.android.ActivityManager.activity = (Activity) para3;
		}
		
		init();

		try {
			appName = (String) paras[3];

			contactEmail = (String) paras[4];
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * 此方法被ServerMainActivity的升级模块使用，请勿更名
	 * 
	 * @return
	 */
	public static HashMap getResMap() {
		return new HashMap<Object, Object>() {
			public Object get(Object key) {
				if (key instanceof Integer) {
					return ResourceUtil.get((Integer) key);
				} else {
					return null;
				}
			}
		};
	}

	private static String appName = "appNameHere";// it is recommend to set it
													// in method init(Object[]
													// para)
	private static String contactEmail = "support@company.com";// it is
																// recommend to
																// set it in
																// method
																// init(Object[]
																// para)

	public static final String getAppName() {
		return appName;
	}

	public static final String getContactEmail() {
		return contactEmail;
	}

	private static Map<String, Object> actionMap;

	public static void doAction(Object mapKey) {
		View.OnClickListener listener = (View.OnClickListener) J2SEInitor.actionMap.get(mapKey);
		if (listener != null) {
			listener.onClick(null);
		}
	}

	final static Vector<ClassLoader> packagePath = new Vector<ClassLoader>();

	public static Vector<ClassLoader> getPackagesPath() {
		return packagePath;
	}

	public static int screenWidth, screenHeight, mobileDPI;

	private static void init() {
		{
			OutputStream os = new OutputStream() {
				@Override
				public synchronized void write(byte[] buffer, int offset, int len) {
					Log.i(appName, new String(buffer, offset, len));
				}

				@Override
				public void write(int oneByte) throws IOException {
				}
			};
			System.setOut(new PrintStream(os));
		}

		{
			OutputStream os = new OutputStream() {
				@Override
				public synchronized void write(byte[] buffer, int offset, int len) {
					Log.e(appName, new String(buffer, offset, len));
				}

				@Override
				public void write(int oneByte) throws IOException {
				}
			};
			System.setErr(new PrintStream(os));
		}

		packagePath.add(J2SEInitor.class.getClassLoader());

		try {
			screenHeight = Integer.parseInt(actionMap.get(J2SEActionMap.p_screenHeight).toString());// 横屏模式下的高
			screenWidth = Integer.parseInt(actionMap.get(J2SEActionMap.p_screenWidth).toString());// 横屏模式下的宽
			mobileDPI = Integer.parseInt(actionMap.get(J2SEActionMap.p_screenDPI).toString());
		}catch (Exception e) {
			DisplayMetrics dm = new DisplayMetrics();
			final android.view.WindowManager window = (android.view.WindowManager) ActivityManager.getWindowManager();
			window.getDefaultDisplay().getMetrics(dm);

			screenHeight = Math.min(dm.widthPixels, dm.heightPixels);// 横屏模式下的高
			screenWidth = Math.max(dm.widthPixels, dm.heightPixels);// 横屏模式下的宽
			mobileDPI = ActivityManager.applicationContext.getResources().getDisplayMetrics().densityDpi;
		}
		
		System.setProperty(CCoreUtil.SYS_SERVER_OS_PLATFORM,
				CCoreUtil.SYS_SERVER_OS_ANDROID_SERVER);

		try {
			final int sdkInt = Build.VERSION.SDK_INT;
			final String javaVer = CCoreUtil.SYS_ANDROID_SERVER_JAVA_VERSION;

			// 必须是可转换为Float，参见hc.App.getJREVer()
			if (sdkInt >= 24) {// Android 7.0 Nougat (API 24)
				System.setProperty(javaVer, "1.8");
			} else if (sdkInt >= 19) {// Android 4.4 KitKat (API 19)
				System.setProperty(javaVer, "1.7");
			} else {
				System.setProperty(javaVer, "1.6");
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public static void initScreenInfo() {
		hc.android.UIThreadViewChanger.callInitEmpty();
		
		packagePath.add(ActivityManager.activity.getClassLoader());
	}

	private static ScreenAdapter screenAdapter;

	public static final ScreenAdapter getAndroidServerScreenAdapter() {
		if (screenAdapter == null) {
			screenAdapter = new ScreenAdapter(screenWidth, screenHeight, mobileDPI,
					ScreenAdapter.TYPE_SERVER);
		}
		return screenAdapter;
	}

	public static final ScreenAdapter initAdapter() {
		ScreenAdapter out;
		out = ScreenAdapter.initScreenAdapterFromContext(false);
		if (out == null) {
			out = getAndroidServerScreenAdapter();
		}

		return out;
	}

}
