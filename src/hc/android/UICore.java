package hc.android;

import hc.android.ScreenAdapter;
import java.awt.Font;
import android.util.TypedValue;
import android.widget.TextView;

public class UICore {
	public static final int MS_DOUBLE_CLICK = 300;
	public static final int STANDARD_J2SE_FONT_SIZE = 13;
	private static final Font DIALOG_INPUT_FONT = buildDefaultDialogInputFont();

	public static Font buildDefaultDialogInputFont() {
		// int adjustFontSize = adjustFontForServer();
		// System.out.println("DefaultDialogInputFont size : " +
		// adjustFontSize);
		return new Font(Font.DIALOG_INPUT, Font.PLAIN, STANDARD_J2SE_FONT_SIZE);
	}

	public static Font buildDefaultDialogButtonFont() {
		return new Font(Font.DIALOG, Font.PLAIN, STANDARD_J2SE_FONT_SIZE + 2);
	}

	public static Font buildDefaultDialogFont() {
		return new Font(Font.DIALOG, Font.PLAIN, STANDARD_J2SE_FONT_SIZE);
	}

	public static Font getDefaultDialogInputFontForSystemUIOnly() {
		return DIALOG_INPUT_FONT;
	}

	public static void setTextSize(TextView view, ScreenAdapter screenAdapter) {
		UICore.setTextSize(view, DIALOG_INPUT_FONT, screenAdapter);
	}

	/**
	 * 
	 * @return 1.0, 2.0.....等标准倍数
	 */
	public static float getDeviceDensity() {
		HardwareConfig config = HardwareConfigManager.getConfig();
		final float scale;
		if (config == null || config.getDensity() == 0) {
			scale = ActivityManager.applicationContext.getResources().getDisplayMetrics().density;
		} else {
			scale = config.getDensity();
		}
		return scale;
	}

	public static void setTextSize(TextView view, Font font, ScreenAdapter screenAdapter) {
		setTextSize(view, font, 0, screenAdapter);
	}

	/**
	 * 
	 * @param view
	 * @param font
	 * @param shiftSize
	 *            负数:表示在指定字体尺寸上向小偏移。正数:在指定字体尺寸上向大偏移
	 */
	public static void setTextSize(TextView view, Font font, int shiftSize,
			ScreenAdapter screenAdapter) {
		UICore.setTextSize(view, font.getSize() + shiftSize, screenAdapter);
	}

	public static void setTextSize(TextView view, int fontSize, ScreenAdapter screenAdapter) {
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				screenAdapter == null ? fontSize : screenAdapter.getFontSizeInPixel(fontSize));
	}

}
