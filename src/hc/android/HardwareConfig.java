package hc.android;

import android.util.DisplayMetrics;
import hc.server.ui.ClientDesc;

public class HardwareConfig {
	static HardwareConfig serverHC;
	static HardwareConfig clientHC;

	// public static HardwareConfig getClientHardwareConfig(){
	// if(clientHC == null){
	// clientHC = new HardwareConfig(ClientDesc.getClientWidth(),
	// ClientDesc.getClientHeight(), ClientDesc.getDPI(),
	// ClientDesc.getXDPI(), ClientDesc.getYDPI(), ClientDesc.getDensity(),
	// ClientDesc.getClientLang(), ClientDesc.getHCClientVer());
	// }
	// return clientHC;
	// }

	public static HardwareConfig getServerHardwareConfig() {
		if (serverHC == null) {
			DisplayMetrics dm = ActivityManager.applicationContext.getResources().getDisplayMetrics();
			serverHC = new HardwareConfig(dm.widthPixels, dm.heightPixels, dm.densityDpi, dm.xdpi,
					dm.ydpi, dm.density, "", "");
		}
		return serverHC;
	}

	int clientWidth, clientHeight, dpi;
	float xdpi, ydpi, density;
	String clientLang, clientVer;

	public HardwareConfig(int cW, int cH, int dpi, float xdpi, float ydpi, float density,
			String cLang, String cVer) {
		this.clientWidth = cW;
		this.clientHeight = cH;
		this.dpi = dpi;
		this.xdpi = xdpi;
		this.ydpi = ydpi;
		this.density = density;
		this.clientLang = cLang;
		this.clientVer = cVer;
	}

	public int getClientWidth() {
		return clientWidth;
	}

	public int getClientHeight() {
		return clientHeight;
	}

	public int getDPI() {
		return dpi;
	}

	public float getXDPI() {
		return xdpi;
	}

	public float getYDPI() {
		return ydpi;
	}

	public float getDensity() {
		return density;
	}

	public String getClientLang() {
		return clientLang;
	}

	public String clientVer() {
		return clientVer;
	}
}
