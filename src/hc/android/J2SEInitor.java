package hc.android;

import hc.core.util.CCoreUtil;
import hc.android.ScreenAdapter;
import hc.util.ResourceUtil;
import java.util.Map;
import java.util.Vector;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * 注意：该类被HCFont.checkIsStandardJ2SE反射使用，请不要改名
 *
 */
public class J2SEInitor {
	private static boolean isInited = false;
	
	/**
	 * 如果增加了新参数，请同步更改到ServerMainActivity.startHCByReflect
	 * @param paras
	 */
	public synchronized static void init(Object[] paras){
		if(isInited){
			throw new Error("can NOT invoke J2SEInitor.init(Object[] paras) two times or more.");
		}
		isInited = true;
		
		final Object para1 = paras[0];
		final Object para2 = paras[1];
		final Object para3 = paras[2];

		//------------para1-----------------
		Map<String, Integer> resMap = (Map<String, Integer>)para1;
		HCRUtil.resMap = resMap;//convert J2SE res (String) to Android res (Integer), for more, see Class "hc.android.HCRUtil"
		
		//------------para2-----------------
		actionMap = (Map<String, Object>)para2;
		J2SEActionMap.init(actionMap);
		
		//------------para3-----------------
		hc.android.ActivityManager.sysActivity = (Activity)para3;
		init(ActivityManager.getActivity());
		
		try{
			appName = (String)paras[3];
			
			contactEmail = (String)paras[4];
		}catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private static String appName = "appNameHere";//it is recommend to set it in method init(Object[] para)
	private static String contactEmail = "support@company.com";//it is recommend to set it in method init(Object[] para)
	
	public static final String getAppName(){
		return appName;
	}
	
	public static final String getContactEmail(){
		return contactEmail;
	}
	
	private static Map<String, Object> actionMap;
	
	public static void doAction(Object mapKey){
		View.OnClickListener listener = (View.OnClickListener)J2SEInitor.actionMap.get(mapKey);
		if(listener != null){
			listener.onClick(null);
		}
	}
	
	/**
	 * 注意：请勿改方法名。
	 * 本方法被HCAndroidServer下的ServerMainActivity.startHCByReflect()反射式调用
	 */
	public static void sendNotification(){
		Activity context = ActivityManager.getActivity();

		String title = J2SEInitor.getAppName(); 
		try{
			title = context.getPackageManager().getPackageInfo(  
					context.getPackageName(), 0).applicationInfo.loadLabel(context.getPackageManager()).toString(); 
		}catch (Exception e) {
		}
	    String msg = (String)ResourceUtil.get(6002);
	    Notification n = new Notification(HCRUtil.getResource(HCRUtil.R_drawable_hc_48), msg, System.currentTimeMillis());
		n.flags = Notification.FLAG_ONGOING_EVENT;
		Intent intent = new Intent(context, context.getClass());
		PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
		n.setLatestEventInfo(context, title, msg, pi);

	    NotificationManager nm = (NotificationManager)context.getSystemService(Activity.NOTIFICATION_SERVICE);
	    nm.notify(100, n);
	}
	
	final static Vector<ClassLoader> packagePath = new Vector<ClassLoader>();
	
	public static Vector<ClassLoader> getPackagesPath(){
		return packagePath;
	}
	
	public static int screenWidth, screenHeight, mobileDPI;
	
	private static void init(Activity activity){
		packagePath.add(activity.getClassLoader());
		packagePath.add(J2SEInitor.class.getClassLoader());
		
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

		screenHeight = Math.min(dm.widthPixels, dm.heightPixels);
		screenWidth = Math.max(dm.widthPixels, dm.heightPixels);
		mobileDPI = activity.getResources().getDisplayMetrics().densityDpi;
		
		System.setProperty(CCoreUtil.SYS_SERVER_OS_PLATFORM, CCoreUtil.SYS_SERVER_OS_ANDROID_SERVER);
		
		try{
			System.setProperty(CCoreUtil.SYS_ANDROID_SERVER_JAVA_VERSION, "1.6");//必须是可转换为Float，参见hc.App.getJREVer()
		}catch (Throwable e) {
			e.printStackTrace();
		}
		
		hc.android.UIThreadViewChanger.callInitEmpty();
	}

	private static ScreenAdapter screenAdapter;
	
	public static final ScreenAdapter getAndroidServerScreenAdapter(){
		if(screenAdapter == null){
			screenAdapter = new ScreenAdapter(
					screenWidth, screenHeight, mobileDPI, ScreenAdapter.TYPE_SERVER);
		}
		return screenAdapter;
	}

	public static final ScreenAdapter initAdapter(){
		ScreenAdapter out;
		out = ScreenAdapter.initScreenAdapterFromContext(false);
		if(out == null){
			out = getAndroidServerScreenAdapter();
		}
		
		return out;
	}
	
}
