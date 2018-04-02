package hc.android;

import hc.android.ScreenAdapter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;

public class BitmapGraphics extends CanvasGraphics {

	private static Object[] initCanvas(int width, int height) {
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Object[] out = { bitmap, canvas };
		return out;
	}

	public BitmapGraphics(int width, int height, ScreenAdapter screenAdapter) {
		super(initCanvas(width, height), screenAdapter);
	}

	public Bitmap getBitmapAdAPI() {
		return bitmap;
	}

	@Override
	public void copyArea(final int x_src, final int y_src, final int width, final int height,
			final int x_dest, final int y_dest) {
		final int sizeBuf = width * height;
		if (copyBuf == null) {
			copyBuf = new int[width * height];
		} else if (sizeBuf > copyBuf.length) {
			copyBuf = new int[sizeBuf];
		}

		bitmap.getPixels(copyBuf, 0, width, x_src, y_src, width, height);

		canvas.drawBitmap(copyBuf, 0, width, x_dest, y_dest, width, height, true, null);
	}

}
