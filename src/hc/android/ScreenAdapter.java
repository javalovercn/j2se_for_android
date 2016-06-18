package hc.android;

import hc.core.L;
import hc.core.util.LogManager;
import hc.server.ui.ProjectContext;
import hc.server.util.ContextSecurityConfig;
import hc.server.util.ContextSecurityManager;

public class ScreenAdapter {
	public static final int TYPE_MLET = 1;
	public static final int TYPE_SERVER = 2;
	
	public final static int STAND_SCREEN_HEIGHT = 768;
	public final static int STAND_SCREEN_WIDTH = 1024;
	public final static float avgFontMin = STAND_SCREEN_WIDTH*1.0F/12.0F;
	//一英寸分72磅
	final static float ADJUST_FONT = 120F / STAND_SCREEN_WIDTH / 72;
	
	public final int width;
	public final int height;
	public final int mobileDPI;
	public final int type;

	/**
	 * 对系统级Component进行绽放；对Mlet级Component不进行缩放
	 * @param w
	 * @param h
	 * @param dpi
	 * @param type
	 */
	public ScreenAdapter(final int w, final int h, final int dpi, final int type){
		this.width = w;
		this.height = h;
		if(dpi == 0){
			this.mobileDPI = 120;
		}else{
			this.mobileDPI = dpi;
		}
		this.type = type;
	}
	
	public final int getPreAdapterWidth(final int screenWidth){
		if(this.type == TYPE_MLET){
			return screenWidth;
		}
		return width * screenWidth / ScreenAdapter.STAND_SCREEN_WIDTH;
	}

	public final int getPreAdapterHeight(final int screenHeight){
		if(this.type == TYPE_MLET){
			return screenHeight;
		}
		return height * screenHeight / ScreenAdapter.STAND_SCREEN_HEIGHT;
	}
	
	public final int imageSizeToScreenFloat(final int size, final float zoom) {
		if(type == TYPE_MLET){
			return size;
		}else{
			final int max = Math.max(width, height);
			final int out = (int)( max * size * zoom / ScreenAdapter.STAND_SCREEN_HEIGHT);//UICore.getDeviceDensity()
//			L.V = L.O ? false : LogManager.log("resize image from : " + size + ", to : " + out + ", zoom : " + zoom);
			return out;
		}
	}
	
	public final float getFontSize(final int size){
		if(type == ScreenAdapter.TYPE_MLET){
			return size;
			
			//Mlet完全采用用户指定大小。
//			int densityDPI = screenAdapter.mobileDPI;
//			if(densityDPI == 0){
//				densityDPI = 120;
//			}
//			return densityDPI * size / ScreenServer.J2SE_STANDARD_DPI;
		}else{
			//Android环境下，采用自适应智能大小
			final int max = Math.max(width, height);
			final float out = size * max * ADJUST_FONT;
//			System.out.println("fontSizeToPixel:" + out + ", max:" + max + ", size:" + size + ", ADJ_FONT:" + ADJUST_FONT);
			return out;
		}
	}
	
//	/**
//	 * 将一个设定到缺省密度的尺寸。
//	 * @param fileName
//	 * @param defaultDensitySize
//	 * @return
//	 */
//	public Bitmap getDIPBitmap(String fileName, float zoom) {
//		InputStream is = J2SEInitor.class.getResourceAsStream(fileName);
//		Bitmap bitmap = BitmapFactory.decodeStream(is);
//		return getDIPBitmap(bitmap, zoom);
//	}
	private static final ScreenAdapter mletScreenAdapter = new ScreenAdapter(0, 0, 0, TYPE_MLET);

	public static final ScreenAdapter initScreenAdapterFromContext(final boolean forHCFont){
		final ContextSecurityConfig csc = ContextSecurityManager.getConfig(Thread.currentThread().getThreadGroup());
		if(csc != null){
			if(forHCFont){
				final ProjectContext projectContext = csc.getProjectContext();
				return new ScreenAdapter(projectContext.getMobileWidth(), projectContext.getMobileHeight(), 
						projectContext.getMobileDPI(), TYPE_SERVER);
			}else{
				return mletScreenAdapter;
			}
		}
		return null;
	}
}
