package hc.android;

import hc.util.LogServerSide;
import android.util.Log;

public class AndroidLogServerSide extends LogServerSide {
	final String tag = "HomeCenter";
	
	public AndroidLogServerSide() {
		Log.i(tag, "create instance of AndroidLogServerSide.");
	}

	@Override
	public void log(final String msg) {
		if (isLogToFile) {
			super.log(msg);
		}
		Log.i(tag, msg);
	}

	@Override
	public void err(final String msg) {
		if (isLogToFile) {
			super.err(msg);
		}
		Log.e(tag, msg);
	}

	@Override
	public void warning(final String msg) {
		if (isLogToFile) {
			super.warning(msg);
		}
		Log.w(tag, msg);
	}
}
