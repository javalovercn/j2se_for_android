package hc.android;

import hc.android.loader.StringUtil;
import hc.android.ScreenAdapter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Iterator;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

public class CanvasGraphics extends Graphics2D {
	final RenderingHints renderingHint = new RenderingHints(null);

	final Canvas canvas;
	final ScreenAdapter screenAdapter;
	final Paint paint;
	Color c;
	int[] copyBuf;
	final Rect bounds;
	final Paint paintLine;
	Bitmap bitmap;

	public Canvas getCanvasAdAPI() {
		return canvas;
	}

	public CanvasGraphics(final Object[] canvas, final ScreenAdapter screenAdapter) {
		this((Canvas) canvas[1], screenAdapter);
		bitmap = (Bitmap) canvas[0];
	}

	/**
	 * 
	 * @param canvas
	 * @param screenAdapter
	 *            通过AbstractBorder过来的，有可能为null
	 */
	public CanvasGraphics(final Canvas canvas, final ScreenAdapter screenAdapter) {
		this.canvas = canvas;
		if (screenAdapter == null) {
			this.screenAdapter = J2SEInitor.getAndroidServerScreenAdapter();
		} else {
			this.screenAdapter = screenAdapter;
		}

		paint = new Paint();
		bounds = new Rect();

		paint.setAntiAlias(true);
		c = new Color(paint.getColor());

		paintLine = new Paint();
		paintLine.setStyle(Paint.Style.STROKE);
		paintLine.setStrokeWidth(0);
		paintLine.setStrokeCap(Paint.Cap.ROUND);

		setFont(UICore.buildDefaultDialogFont());
	}

	@Override
	public Graphics create() {
		final BitmapGraphics bg = new BitmapGraphics(canvas.getWidth(), canvas.getHeight(),
				screenAdapter);
		bg.setColor(getColor());
		bg.setFont(getFont());
		return bg;
	}

	@Override
	public void translate(final int x, final int y) {
		canvas.translate(x, y);
	}

	@Override
	public Color getColor() {
		return c;
	}

	@Override
	public void setColor(final Color c) {
		this.c = c;
		final int RGB = c.toAndroid();
		if ((RGB >> 24) == 0) {
			paint.setColor(RGB | 0xFF000000);
		} else {
			paint.setColor(RGB);
		}
	}

	@Override
	public void setPaintMode() {
	}

	@Override
	public void setXORMode(final Color c1) {

	}

	Font font;

	@Override
	public Font getFont() {
		return font;
	}

	@Override
	public void setFont(final Font font) {
		this.font = font;
		paint.setTypeface(font.typeface);
		paint.setTextSize(screenAdapter.getFontSizeInPixel(font.getSize()));// paint.setTextSize单位为pixel
	}

	@Override
	public Rectangle getClipBounds() {
		final Rectangle r = new Rectangle();
		final Rect rect = canvas.getClipBounds();
		r.x = rect.left;
		r.y = rect.top;
		r.width = rect.width();
		r.height = rect.height();

		return r;
	}

	@Override
	public void clipRect(final int x, final int y, final int width, final int height) {
		canvas.clipRect(x, y, x + width - 1, y + height - 1);
	}

	@Override
	public void setClip(final int x, final int y, final int width, final int height) {
		canvas.clipRect(x, y, x + width - 1, y + height - 1, android.graphics.Region.Op.REPLACE);
	}

	Shape clip;

	@Override
	public Shape getClip() {
		if (clip == null) {
			final Rectangle rect = clip.getBounds();
			clip = new Rectangle2D.Float(rect.x, rect.y, rect.width, rect.height);
		}
		return clip;
	}

	@Override
	public void setClip(final Shape clip) {
		this.clip = clip;

		final Rectangle rect = clip.getBounds();
		setClip(rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	public void copyArea(final int x_src, final int y_src, final int width, final int height,
			final int x_dest, final int y_dest) {
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	@Override
	public void drawLine(final int x1, final int y1, final int x2, final int y2) {
		paintLine.setColor(paint.getColor());
		canvas.drawPoint(x1, y1, paintLine);
		canvas.drawLine(x1, y1, x2, y2, paintLine);
		canvas.drawPoint(x2, y2, paintLine);
	}

	@Override
	public void fillRect(final int x, final int y, final int width, final int height) {
		final Paint.Style oldStyle = paint.getStyle();
		final boolean anti = paint.isAntiAlias();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(false);
		canvas.drawRect(x, y, x + width, y + height, paint);
		paint.setAntiAlias(anti);
		paint.setStyle(oldStyle);
	}

	@Override
	public void clearRect(final int x, final int y, final int width, final int height) {
		final Color oldColor = getColor();
		setColor(Color.WHITE);
		fillRect(x, y, width, height);
		setColor(oldColor);
	}

	@Override
	public void drawRoundRect(final int x, final int y, final int width, final int height,
			final int arcWidth, final int arcHeight) {
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRoundRect(new RectF(x, y, x + width - 1, y + height - 1), arcWidth / 2,
				arcHeight / 2, paint);
	}

	@Override
	public void fillRoundRect(final int x, final int y, final int width, final int height,
			final int arcWidth, final int arcHeight) {
		final Paint.Style oldStyle = paint.getStyle();
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRoundRect(new RectF(x, y, x + width, y + height), arcWidth / 2, arcHeight / 2,
				paint);
		paint.setStyle(oldStyle);
	}

	@Override
	public void drawOval(final int x, final int y, final int width, final int height) {
		final RectF rf = new RectF(x, y, x + width, y + height);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawOval(rf, paint);
	}

	@Override
	public void fillOval(final int x, final int y, final int width, final int height) {
		final Paint.Style oldStyle = paint.getStyle();
		paint.setStyle(Paint.Style.FILL);
		canvas.drawOval(new RectF(x, y, x + width, y + height), paint);
		paint.setStyle(oldStyle);
	}

	@Override
	public void drawArc(final int x, final int y, final int width, final int height,
			final int startAngle, final int arcAngle) {
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawArc(new RectF(x, y, x + width - 1, y + height - 1), startAngle, arcAngle, true,
				paint);
	}

	@Override
	public void fillArc(final int x, final int y, final int width, final int height,
			final int startAngle, final int arcAngle) {
		final Paint.Style oldStyle = paint.getStyle();
		paint.setStyle(Paint.Style.FILL);
		canvas.drawArc(new RectF(x, y, x + width, y + height), startAngle, arcAngle, true, paint);
		paint.setStyle(oldStyle);
	}

	@Override
	public void drawPolyline(final int[] xPoints, final int[] yPoints, final int nPoints) {
		drawPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public void drawPolygon(final int[] xPoints, final int[] yPoints, final int nPoints) {
		final float[] drawxy = new float[nPoints * 2];
		for (int i = 0, j = 0; i < nPoints; i++) {
			drawxy[j++] = xPoints[i];
			drawxy[j++] = yPoints[i];
		}
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawPoints(drawxy, paint);
	}

	@Override
	public void fillPolygon(final int[] xPoints, final int[] yPoints, final int nPoints) {
		final Paint.Style oldStyle = paint.getStyle();
		paint.setStyle(Paint.Style.FILL);

		final float[] drawxy = new float[nPoints * 2];
		for (int i = 0, j = 0; i < nPoints; i++) {
			drawxy[j++] = xPoints[i];
			drawxy[j++] = yPoints[i];
		}
		canvas.drawPoints(drawxy, paint);

		paint.setStyle(oldStyle);
	}

	@Override
	public void drawString(final String str, final int x, final int y) {
		drawSubString(str, 0, str.length(), x, y);
	}

	private void drawSubString(final String str, final int offset, final int len, final float x,
			final float y) {
		final Paint.Cap oldCap = paint.getStrokeCap();
		final Paint.Style oldStyle = paint.getStyle();
		final boolean isOldAnti = paint.isAntiAlias();

		// RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
		final Object antiStatus = renderingHint.get(RenderingHints.KEY_ANTIALIASING);
		boolean isAntiAlias = false;
		if (antiStatus != null && antiStatus.equals(RenderingHints.VALUE_ANTIALIAS_ON)) {
			isAntiAlias = true;
		}
		paint.setAntiAlias(isAntiAlias);
		// 当然也可以设置为"实心"(Paint.Style.FILL)
		// Paint.setStyle(Paint.Style.STROKE);设置paint的风格为“空心”
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeCap(Paint.Cap.ROUND);
		final FontMetrics fm = paint.getFontMetrics();
		synchronized (bounds) {
			paint.getTextBounds(str, offset, offset + len, bounds);
			final int fontHeight = font.getSize();
			final int baseline = (int) (y + (fontHeight - fm.bottom + fm.top) / 2 - fm.top);
			canvas.drawText(str, x, baseline, paint);
		}

		paint.setStyle(oldStyle);
		paint.setStrokeCap(oldCap);
		paint.setAntiAlias(isOldAnti);
	}

	@Override
	public void drawString(final AttributedCharacterIterator iterator, final int x, final int y) {
		drawString(StringUtil.toString(iterator), x, y);
	}

	@Override
	public boolean drawImage(final Image img, final int x, final int y,
			final ImageObserver observer) {
		canvas.drawBitmap(img.getBitmapAdAPI(), x, y, null);// 去掉paint，因为会产生重影
		return true;
	}

	@Override
	public boolean drawImage(final Image img, final int x, final int y, final int width,
			final int height, final ImageObserver observer) {
		final Bitmap bm = img.getBitmapAdAPI();
		final Rect src = new Rect(0, 0, bm.getWidth(), bm.getHeight());
		final Rect dst = new Rect(x, y, x + width, y + height);
		canvas.drawBitmap(bm, src, dst, null);// 去掉paint，因为会产生重影
		return true;
	}

	@Override
	public boolean drawImage(final Image img, final int x, final int y, final Color bgcolor,
			final ImageObserver observer) {
		// Color oldColor = getColor();
		//
		// canvas.drawColor(bgcolor.toAndroid());
		// canvas.drawBitmap(img.getBitmapAdAPI(), x, y, null);//去掉paint，因为会产生重影
		//
		// setColor(oldColor);
		drawImage(img, x, y, img.getWidth(null), img.getHeight(null), bgcolor, observer);
		return true;
	}

	@Override
	public boolean drawImage(final Image img, final int x, final int y, final int width,
			final int height, final Color bgcolor, final ImageObserver observer) {
		final Color oldColor = getColor();

		final Path path = new Path();
		path.moveTo(x, y);
		final int x_w = x + width;
		path.lineTo(x_w, y);
		final int y_h = y + height;
		path.lineTo(x_w, y_h);
		path.lineTo(x, y_h);
		path.close();

		canvas.clipPath(path);
		canvas.drawColor(bgcolor.toAndroid());
		final Bitmap bm = img.getBitmapAdAPI();
		final Rect src = new Rect(0, 0, bm.getWidth(), bm.getHeight());
		final Rect dst = new Rect(x, y, x_w, y_h);
		final boolean isAnti = paintLine.isAntiAlias();
		paintLine.setAntiAlias(false);
		canvas.drawBitmap(bm, src, dst, paintLine);// 去掉paint，因为会产生重影
		paintLine.setAntiAlias(isAnti);

		setColor(oldColor);
		return true;
	}

	@Override
	public boolean drawImage(final Image img, final int dx1, final int dy1, final int dx2,
			final int dy2, final int sx1, final int sy1, final int sx2, final int sy2,
			final ImageObserver observer) {
		final Bitmap bm = img.getBitmapAdAPI();

		drawBitmap(dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bm);

		return true;
	}

	private void drawBitmap(final int dx1, final int dy1, final int dx2, final int dy2,
			final int sx1, final int sy1, final int sx2, final int sy2, final Bitmap bm) {
		final Rect src = new Rect(sx1, sy1, sx2, sy2);
		final Rect dst = new Rect(dx1, dy1, dx2, dy2);
		canvas.drawBitmap(bm, src, dst, null);// 去掉paint，因为会产生重影
	}

	@Override
	public boolean drawImage(final Image img, final int dx1, final int dy1, final int dx2,
			final int dy2, final int sx1, final int sy1, final int sx2, final int sy2,
			final Color bgcolor, final ImageObserver observer) {
		final Color oldColor = getColor();

		final Path path = new Path();
		path.moveTo(dx1, dy1);
		path.lineTo(dx2, dy1);
		path.lineTo(dx2, dy2);
		path.lineTo(dx1, dy2);
		path.close();

		canvas.clipPath(path);
		canvas.drawColor(bgcolor.toAndroid());
		final Bitmap bm = img.getBitmapAdAPI();

		drawBitmap(dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bm);

		setColor(oldColor);
		return true;
	}

	@Override
	public void dispose() {
		font = null;
		copyBuf = null;
		c = null;
	}

	@Override
	public void draw(final Shape s) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public boolean drawImage(final Image img, final AffineTransform xform,
			final ImageObserver obs) {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	@Override
	public void drawImage(final BufferedImage img, final BufferedImageOp op, final int x,
			final int y) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public void drawRenderedImage(final RenderedImage img, final AffineTransform xform) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public void drawRenderableImage(final RenderableImage img, final AffineTransform xform) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public void drawString(final String str, final float x, final float y) {
		drawSubString(str, 0, str.length(), x, y);
	}

	@Override
	public void drawString(final AttributedCharacterIterator iterator, final float x,
			final float y) {
		final String str = StringUtil.toString(iterator);
		drawSubString(str, 0, str.length(), x, y);
	}

	@Override
	public void drawGlyphVector(final GlyphVector g, final float x, final float y) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public void fill(final Shape s) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public boolean hit(final Rectangle rect, final Shape s, final boolean onStroke) {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	@Override
	public void setComposite(final Composite comp) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public void setPaint(final java.awt.Paint paint) {
		AndroidClassUtil.callEmptyMethod();
	}

	private Stroke stroke;

	@Override
	public void setStroke(final Stroke s) {
		stroke = s;
		paint.setStyle(Paint.Style.STROKE);
		if (stroke instanceof BasicStroke) {
			paint.setStrokeWidth(((BasicStroke) stroke).getLineWidth());
		}
	}

	public void drawRect(final int x, final int y, final int width, final int height) {
		canvas.drawRect(x, y, x + width, y + height, paint);
	}

	@Override
	public void setRenderingHint(final Key hintKey, final Object hintValue) {
		renderingHint.put(hintKey, hintValue);
	}

	@Override
	public Object getRenderingHint(final Key hintKey) {
		return renderingHint.get(hintKey);
	}

	@Override
	public void setRenderingHints(final Map<?, ?> hints) {
		renderingHint.clear();

		final Iterator it = hints.keySet().iterator();
		while (it.hasNext()) {
			final Object key = it.next();
			if (key instanceof Key) {
				renderingHint.put(key, hints.get(key));
			}
		}
	}

	@Override
	public void addRenderingHints(final Map<?, ?> hints) {
		final Iterator it = hints.keySet().iterator();
		while (it.hasNext()) {
			final Object key = it.next();
			if (key instanceof Key) {
				renderingHint.put(key, hints.get(key));
			}
		}
	}

	@Override
	public RenderingHints getRenderingHints() {
		return renderingHint;
	}

	@Override
	public void translate(final double tx, final double ty) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public void rotate(final double theta) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public void rotate(final double theta, final double x, final double y) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public void scale(final double sx, final double sy) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public void shear(final double shx, final double shy) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public void transform(final AffineTransform Tx) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public void setTransform(final AffineTransform Tx) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public AffineTransform getTransform() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	@Override
	public java.awt.Paint getPaint() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	@Override
	public void setBackground(final Color color) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public Color getBackground() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	@Override
	public Stroke getStroke() {
		return stroke;
	}

	@Override
	public void clip(final Shape s) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public FontRenderContext getFontRenderContext() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	@Override
	public java.awt.FontMetrics getFontMetrics(final Font f) {
		return new java.awt.FontMetrics(f) {
		};
	}

}