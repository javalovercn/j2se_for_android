package hc.android;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Configuration;
import android.view.View;
import hc.util.ExitManager;
import hc.util.ResourceUtil;

public class J2SEActionMap {
	/**
	 * Important : for example, to press back key to close current J2SE window,
	 * you should implement a method "public void onBackPressed()" in activity
	 * and invoke the action from map.
	 */
	public static final String onBackPressed = "onBackPressed";
	public static final String onConfigurationChanged = "onConfigurationChanged";
	public static final String startBringToFrontService = "startBringToFrontService";
	public static final String stopBringToFrontService = "stopBringToFrontService";
	public static final String shutdownHC = "shutdownHC";
	public static final String shutdownBroadcase = "shutdownBroadcase";
	public static final String showTray = "showTray";
	public static final String changeToUIServer = "changeToUIServer";
	public static final String initScreen = "initScreen";
	public static final String setActivity = "setActivity";
	public static final String changeToNonUIServer = "changeToNonUIServer";
	
	public static final String p_screenWidth = "screenWidth";
	public static final String p_screenHeight = "screenHeight";
	public static final String p_screenDPI = "screenDPI";
	
	public static final String map_para = "map_para";

	public static void init(final Map<String, Object> map) {
		map.put(changeToNonUIServer, new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ResourceUtil.changeToNonUIServer();
			}
		});
		
		map.put(setActivity, new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final Object obj = map.get(map_para + setActivity);
				if(obj != null && obj instanceof Activity) {
					hc.android.ActivityManager.activity = (Activity)obj;
				}
			}
		});
		
		map.put(initScreen, new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				J2SEInitor.initScreenInfo();
			}
		});
		
		map.put(changeToUIServer, new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ResourceUtil.changeToUIServer();
			}
		});
		
		map.put(showTray, new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {//注意：非主线程，
				ResourceUtil.showTrayWithCheckReady(false);
			}
		});
		
		map.put(shutdownBroadcase, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExitManager.startExitSystem();
			}
		});

		map.put(onBackPressed, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AndroidUIUtil.runDelayNotInUIThread(new Runnable() {
					@Override
					public void run() {
						WindowManager.notifyClose(null);
					}
				});
			}
		});

		map.put(onConfigurationChanged, new View.OnClickListener() {
			boolean isFirstConfigChanged = false;
			int orientation;
			int rotation;

			@Override
			public void onClick(View v) {
				Configuration newConfig = (Configuration) map.get(map_para);
				if (isFirstConfigChanged == false) {
					isFirstConfigChanged = true;

					orientation = newConfig.orientation;
					rotation = ActivityManager.getWindowManager().getDefaultDisplay()
							.getRotation();
				} else {
					if (orientation != newConfig.orientation
							|| rotation != ActivityManager.getWindowManager()
									.getDefaultDisplay().getRotation()) {
						showMessageDialog((String) ResourceUtil.get(6000),
								(String) ResourceUtil.get(1));
					}
				}
			}
		});
	}

	private static void showMessageDialog(final String msg, final String title) {
		if(ActivityManager.isNonUIServer()) {
			return;
		}
		
		int iconID = android.R.drawable.ic_dialog_alert;

		new AlertDialog.Builder(ActivityManager.applicationContext).setTitle(title).setIcon(iconID)
				.setMessage(msg).setPositiveButton("O K", null)
				// .setNegativeButton("取消", null)
				.show();
	}
}
