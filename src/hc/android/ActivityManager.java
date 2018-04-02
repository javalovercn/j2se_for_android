package hc.android;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import hc.util.ResourceUtil;

public class ActivityManager {
	final static Thread mainThread = Looper.getMainLooper().getThread();
	final static Handler mainHandler = new Handler(Looper.getMainLooper());
	public static Context applicationContext;
	public static Activity activity;

	public static View getCurrentFocusView() {
		return activity.getCurrentFocus();
	}
	
	public static WindowManager getWindowManager() {
		return (android.view.WindowManager) applicationContext.getSystemService(Context.WINDOW_SERVICE);
	}
	
	public static boolean isNonUIServer() {
		return ResourceUtil.isNonUIServer();
	}
	
	public static boolean isUIServer() {
		return ! isNonUIServer();
	}

}
