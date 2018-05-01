package hc.android;

import hc.core.IConstant;
import hc.core.L;
import hc.core.util.BooleanValue;
import hc.server.TrayMenuUtil;
import hc.util.LogServerSide;
import hc.util.ResourceUtil;
import android.util.Log;

public class AndroidLogServerSide extends LogServerSide {
	final String tag = "HomeCenter";
	private boolean isDispErrorForSimu = false;
	private boolean isCheckInWorkShop = false;
	private boolean isEnableAndroidLogCat = false;
	
	public AndroidLogServerSide() {
		Log.i(tag, "create instance of AndroidLogServerSide.");
	}
	
	public final void enableAndroidLogCat(final boolean enable) {
		isEnableAndroidLogCat = enable;
	}

	@Override
	public void log(final String msg) {
		if (isLogToFile) {
			super.log(msg);
		}
		if(isEnableAndroidLogCat) {
			Log.i(tag, msg);
		}
	}

	@Override
	public void err(final String msg) {
		if(isCheckInWorkShop == false) {
			isCheckInWorkShop = true;
			isDispErrorForSimu = L.isInWorkshop && (ResourceUtil.isNonUIServer() == false);
		}
		
		if(isDispErrorForSimu) {
			TrayMenuUtil.displayMessage(ResourceUtil.getErrorI18N(), msg, IConstant.ERROR, null, 0);
		}
		
		if (isLogToFile) {
			super.err(msg);
		}
		if(isEnableAndroidLogCat) {
			Log.e(tag, msg);
		}
	}

	@Override
	public void warning(final String msg) {
		if (isLogToFile) {
			super.warning(msg);
		}
		if(isEnableAndroidLogCat) {
			Log.w(tag, msg);
		}
	}
}
