package hc.android;

import android.app.Activity;
import android.view.View;

public class ActivityManager {
	public static Activity sysActivity;
	
	public static Activity getActivity(){
		return sysActivity;
	}

	public static View getCurrentFocusView(){
		return sysActivity.getCurrentFocus();
	}
	
}
