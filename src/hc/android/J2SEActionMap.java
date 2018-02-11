package hc.android;

import hc.util.ExitManager;
import hc.util.ResourceUtil;
import java.util.Map;
import android.app.AlertDialog;
import android.content.res.Configuration;
import android.view.View;

public class J2SEActionMap {
	/**
	 * Important :
	 * for example, to press back key to close current J2SE window, you should implement a method "public void onBackPressed()" in activity 
	 * and invoke the action from map.
	 */
	public static final String onBackPressed = "onBackPressed";
	public static final String onConfigurationChanged = "onConfigurationChanged";
	public static final String startBringToFrontService = "startBringToFrontService";
	public static final String stopBringToFrontService = "stopBringToFrontService";
	public static final String shutdownHC = "shutdownHC";
	public static final String shutdownBroadcase = "shutdownBroadcase";
	
	public static final String map_para = "map_para";
	
	public static void init(final Map<String, Object> map){
		map.put(shutdownBroadcase, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExitManager.startExitSystem();
			}
		});
		
		map.put(onBackPressed, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AndroidUIUtil.runDelay(new Runnable() {
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
				Configuration newConfig = (Configuration)map.get(map_para);
				if(isFirstConfigChanged == false){
					isFirstConfigChanged = true;
					
					orientation = newConfig.orientation;
					rotation = ActivityManager.sysActivity.getWindowManager().getDefaultDisplay().getRotation();
				}else{
					if(orientation != newConfig.orientation || rotation != ActivityManager.sysActivity.getWindowManager().getDefaultDisplay().getRotation()){
						showMessageDialog((String)ResourceUtil.get(6000), (String)ResourceUtil.get(1));
					}
				}				
			}
		});
	}
	
	private static void showMessageDialog(final String msg, final String title){
		int iconID = android.R.drawable.ic_dialog_alert;
		
		new AlertDialog.Builder(ActivityManager.getActivity())
	 	.setTitle(title)
	 	.setIcon(iconID)
	 	.setMessage(msg)
	 	.setPositiveButton("O K", null)
//	 	.setNegativeButton("取消", null)
	 	.show();
	}
}
