/*
 * Copyright (c) 1999, 2007, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package java.awt;

import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

/**
 * The <code>Graphics</code> class is the abstract base class for
 * all graphics contexts that allow an application to draw onto
 * components that are realized on various devices, as well as
 * onto off-screen images.
 * <p>
 * A <code>Graphics</code> object encapsulates state information needed
 * for the basic rendering operations that Java supports.  This
 * state information includes the following properties:
 * <p>
 * <ul>
 * <li>The <code>Component</code> object on which to draw.
 * <li>A translation origin for rendering and clipping coordinates.
 * <li>The current clip.
 * <li>The current color.
 * <li>The current font.
 * <li>The current logical pixel operation function (XOR or Paint).
 * <li>The current XOR alternation color
 *     (see {@link Graphics#setXORMode}).
 * </ul>
 * <p>
 * Coordinates are infinitely thin and lie between the pixels of the
 * output device.
 * Operations that draw the outline of a figure operate by traversing
 * an infinitely thin path between pixels with a pixel-sized pen that hangs
 * down and to the right of the anchor point on the path.
 * Operations that fill a figure operate by filling the interior
 * of that infinitely thin path.
 * Operations that render horizontal text render the ascending
 * portion of character glyphs entirely above the baseline coordinate.
 * <p>
 * The graphics pen hangs down and to the right from the path it traverses.
 * This has the following implications:
 * <p><ul>
 * <li>If you draw a figure that covers a given rectangle, that
 * figure occupies one extra row of pixels on the right and bottom edges
 * as compared to filling a figure that is bounded by that same rectangle.
 * <li>If you draw a horizontal line along the same <i>y</i> coordinate as
 * the baseline of a line of text, that line is drawn entirely below
 * the text, except for any descenders.
 * </ul><p>
 * All coordinates that appear as arguments to the methods of this
 * <code>Graphics</code> object are considered relative to the
 * translation origin of this <code>Graphics</code> object prior to
 * the invocation of the method.
 * <p>
 * All rendering operations modify only pixels which lie within the
 * area bounded by the current clip, which is specified by a {@link Shape}
 * in user space and is controlled by the program using the
 * <code>Graphics</code> object.  This <i>user clip</i>
 * is transformed into device space and combined with the
 * <i>device clip</i>, which is defined by the visibility of windows and
 * device extents.  The combination of the user clip and device clip
 * defines the <i>composite clip</i>, which determines the final clipping
 * region.  The user clip cannot be modified by the rendering
 * system to reflect the resulting composite clip. The user clip can only
 * be changed through the <code>setClip</code> or <code>clipRect</code>
 * methods.
 * All drawing or writing is done in the current color,
 * using the current paint mode, and in the current font.
 *
 * @author      Sami Shaio
 * @author      Arthur van Hoff
 * @see     java.awt.Component
 * @see     java.awt.Graphics#clipRect(int, int, int, int)
 * @see     java.awt.Graphics#setColor(java.awt.Color)
 * @see     java.awt.Graphics#setPaintMode()
 * @see     java.awt.Graphics#setXORMode(java.awt.Color)
 * @see     java.awt.Graphics#setFont(java.awt.Font)
 * @since       JDK1.0
 */
public abstract class Graphics {

	protected Graphics() {
	}

	public abstract Graphics create();

	public Graphics create(final int x, final int y, final int width, final int height) {
		final Graphics g = create();
		if (g == null)
			return null;
		g.translate(x, y);
		g.clipRect(0, 0, width, height);
		return g;
	}
	
	public abstract void translate(int x, int y);

	public abstract Color getColor();

	public abstract void setColor(Color c);

	public abstract void setPaintMode();

	public abstract void setXORMode(Color c1);

	public abstract Font getFont();

    public FontMetrics getFontMetrics() {
        return getFontMetrics(getFont());
    }

    public abstract FontMetrics getFontMetrics(Font f);
    
	public abstract void setFont(Font font);

	public abstract Rectangle getClipBounds();

	public abstract void clipRect(int x, int y, int width, int height);

	public abstract void setClip(int x, int y, int width, int height);

	public abstract Shape getClip();

	public abstract void setClip(Shape clip);

	public abstract void copyArea(int x, int y, int width, int height, int dx, int dy);

	public abstract void drawLine(int x1, int y1, int x2, int y2);

	public abstract void fillRect(int x, int y, int width, int height);

	public abstract void drawRect(int x, int y, int width, int height);// {
//		if ((width < 0) || (height < 0)) {
//			return;
//		}
//
//		if (height == 0 || width == 0) {
//			drawLine(x, y, x + width, y + height);
//		} else {
//			drawLine(x, y, x + width - 1, y);
//			drawLine(x + width, y, x + width, y + height - 1);
//			drawLine(x + width, y + height, x + 1, y + height);
//			drawLine(x, y + height, x, y + 1);
//		}
//	}

	public abstract void clearRect(int x, int y, int width, int height);

	public abstract void drawRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight);

	public abstract void fillRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight);

	public void draw3DRect(final int x, final int y, final int width, final int height, final boolean raised) {
		final Color c = getColor();
		final Color brighter = c.brighter();
		final Color darker = c.darker();

		setColor(raised ? brighter : darker);
		drawLine(x, y, x, y + height);
		drawLine(x + 1, y, x + width - 1, y);
		setColor(raised ? darker : brighter);
		drawLine(x + 1, y + height, x + width, y + height);
		drawLine(x + width, y, x + width, y + height - 1);
		setColor(c);
	}

	public void fill3DRect(final int x, final int y, final int width, final int height, final boolean raised) {
		final Color c = getColor();
		final Color brighter = c.brighter();
		final Color darker = c.darker();

		if (!raised) {
			setColor(darker);
		}
		fillRect(x + 1, y + 1, width - 2, height - 2);
		setColor(raised ? brighter : darker);
		drawLine(x, y, x, y + height - 1);
		drawLine(x + 1, y, x + width - 2, y);
		setColor(raised ? darker : brighter);
		drawLine(x + 1, y + height - 1, x + width - 1, y + height - 1);
		drawLine(x + width - 1, y, x + width - 1, y + height - 2);
		setColor(c);
	}

	public abstract void drawOval(int x, int y, int width, int height);

	public abstract void fillOval(int x, int y, int width, int height);

	public abstract void drawArc(int x, int y, int width, int height,
			int startAngle, int arcAngle);

	public abstract void fillArc(int x, int y, int width, int height,
			int startAngle, int arcAngle);

	public abstract void drawPolyline(int xPoints[], int yPoints[], int nPoints);

	public abstract void drawPolygon(int xPoints[], int yPoints[], int nPoints);

	public void drawPolygon(final Polygon p) {
		drawPolygon(p.xpoints, p.ypoints, p.npoints);
	}

	public abstract void fillPolygon(int xPoints[], int yPoints[], int nPoints);

	public void fillPolygon(final Polygon p) {
		fillPolygon(p.xpoints, p.ypoints, p.npoints);
	}

	public abstract void drawString(String str, int x, int y);

	public abstract void drawString(AttributedCharacterIterator iterator, int x, int y);

	public void drawChars(final char data[], final int offset, final int length, final int x, final int y) {
		drawString(new String(data, offset, length), x, y);
	}

	public void drawBytes(final byte data[], final int offset, final int length, final int x, final int y) {
		drawString(new String(data, 0, offset, length), x, y);
	}

	/**
	 * Draws as much of the specified image as is currently available. The image
	 * is drawn with its top-left corner at (<i>x</i>,&nbsp;<i>y</i>) in this
	 * graphics context's coordinate space. Transparent pixels in the image do
	 * not affect whatever pixels are already there.
	 * <p>
	 * This method returns immediately in all cases, even if the complete image
	 * has not yet been loaded, and it has not been dithered and converted for
	 * the current output device.
	 * <p>
	 * If the image has completely loaded and its pixels are no longer being
	 * changed, then <code>drawImage</code> returns <code>true</code>.
	 * Otherwise, <code>drawImage</code> returns <code>false</code> and as more
	 * of the image becomes available or it is time to draw another frame of
	 * animation, the process that loads the image notifies the specified image
	 * observer.
	 * 
	 * @param img
	 *            the specified image to be drawn. This method does nothing if
	 *            <code>img</code> is null.
	 * @param x
	 *            the <i>x</i> coordinate.
	 * @param y
	 *            the <i>y</i> coordinate.
	 * @param observer
	 *            object to be notified as more of the image is converted.
	 * @return <code>false</code> if the image pixels are still changing;
	 *         <code>true</code> otherwise.
	 * @see java.awt.Image
	 * @see java.awt.image.ImageObserver
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int,
	 *      int, int, int)
	 */
	public abstract boolean drawImage(Image img, int x, int y,
			ImageObserver observer);

	/**
	 * Draws as much of the specified image as has already been scaled to fit
	 * inside the specified rectangle.
	 * <p>
	 * The image is drawn inside the specified rectangle of this graphics
	 * context's coordinate space, and is scaled if necessary. Transparent
	 * pixels do not affect whatever pixels are already there.
	 * <p>
	 * This method returns immediately in all cases, even if the entire image
	 * has not yet been scaled, dithered, and converted for the current output
	 * device. If the current output representation is not yet complete, then
	 * <code>drawImage</code> returns <code>false</code>. As more of the image
	 * becomes available, the process that loads the image notifies the image
	 * observer by calling its <code>imageUpdate</code> method.
	 * <p>
	 * A scaled version of an image will not necessarily be available
	 * immediately just because an unscaled version of the image has been
	 * constructed for this output device. Each size of the image may be cached
	 * separately and generated from the original data in a separate image
	 * production sequence.
	 * 
	 * @param img
	 *            the specified image to be drawn. This method does nothing if
	 *            <code>img</code> is null.
	 * @param x
	 *            the <i>x</i> coordinate.
	 * @param y
	 *            the <i>y</i> coordinate.
	 * @param width
	 *            the width of the rectangle.
	 * @param height
	 *            the height of the rectangle.
	 * @param observer
	 *            object to be notified as more of the image is converted.
	 * @return <code>false</code> if the image pixels are still changing;
	 *         <code>true</code> otherwise.
	 * @see java.awt.Image
	 * @see java.awt.image.ImageObserver
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int,
	 *      int, int, int)
	 */
	public abstract boolean drawImage(Image img, int x, int y, int width,
			int height, ImageObserver observer);

	/**
	 * Draws as much of the specified image as is currently available. The image
	 * is drawn with its top-left corner at (<i>x</i>,&nbsp;<i>y</i>) in this
	 * graphics context's coordinate space. Transparent pixels are drawn in the
	 * specified background color.
	 * <p>
	 * This operation is equivalent to filling a rectangle of the width and
	 * height of the specified image with the given color and then drawing the
	 * image on top of it, but possibly more efficient.
	 * <p>
	 * This method returns immediately in all cases, even if the complete image
	 * has not yet been loaded, and it has not been dithered and converted for
	 * the current output device.
	 * <p>
	 * If the image has completely loaded and its pixels are no longer being
	 * changed, then <code>drawImage</code> returns <code>true</code>.
	 * Otherwise, <code>drawImage</code> returns <code>false</code> and as more
	 * of the image becomes available or it is time to draw another frame of
	 * animation, the process that loads the image notifies the specified image
	 * observer.
	 * 
	 * @param img
	 *            the specified image to be drawn. This method does nothing if
	 *            <code>img</code> is null.
	 * @param x
	 *            the <i>x</i> coordinate.
	 * @param y
	 *            the <i>y</i> coordinate.
	 * @param bgcolor
	 *            the background color to paint under the non-opaque portions of
	 *            the image.
	 * @param observer
	 *            object to be notified as more of the image is converted.
	 * @return <code>false</code> if the image pixels are still changing;
	 *         <code>true</code> otherwise.
	 * @see java.awt.Image
	 * @see java.awt.image.ImageObserver
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int,
	 *      int, int, int)
	 */
	public abstract boolean drawImage(Image img, int x, int y, Color bgcolor,
			ImageObserver observer);

	/**
	 * Draws as much of the specified image as has already been scaled to fit
	 * inside the specified rectangle.
	 * <p>
	 * The image is drawn inside the specified rectangle of this graphics
	 * context's coordinate space, and is scaled if necessary. Transparent
	 * pixels are drawn in the specified background color. This operation is
	 * equivalent to filling a rectangle of the width and height of the
	 * specified image with the given color and then drawing the image on top of
	 * it, but possibly more efficient.
	 * <p>
	 * This method returns immediately in all cases, even if the entire image
	 * has not yet been scaled, dithered, and converted for the current output
	 * device. If the current output representation is not yet complete then
	 * <code>drawImage</code> returns <code>false</code>. As more of the image
	 * becomes available, the process that loads the image notifies the
	 * specified image observer.
	 * <p>
	 * A scaled version of an image will not necessarily be available
	 * immediately just because an unscaled version of the image has been
	 * constructed for this output device. Each size of the image may be cached
	 * separately and generated from the original data in a separate image
	 * production sequence.
	 * 
	 * @param img
	 *            the specified image to be drawn. This method does nothing if
	 *            <code>img</code> is null.
	 * @param x
	 *            the <i>x</i> coordinate.
	 * @param y
	 *            the <i>y</i> coordinate.
	 * @param width
	 *            the width of the rectangle.
	 * @param height
	 *            the height of the rectangle.
	 * @param bgcolor
	 *            the background color to paint under the non-opaque portions of
	 *            the image.
	 * @param observer
	 *            object to be notified as more of the image is converted.
	 * @return <code>false</code> if the image pixels are still changing;
	 *         <code>true</code> otherwise.
	 * @see java.awt.Image
	 * @see java.awt.image.ImageObserver
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int,
	 *      int, int, int)
	 */
	public abstract boolean drawImage(Image img, int x, int y, int width,
			int height, Color bgcolor, ImageObserver observer);

	/**
	 * Draws as much of the specified area of the specified image as is
	 * currently available, scaling it on the fly to fit inside the specified
	 * area of the destination drawable surface. Transparent pixels do not
	 * affect whatever pixels are already there.
	 * <p>
	 * This method returns immediately in all cases, even if the image area to
	 * be drawn has not yet been scaled, dithered, and converted for the current
	 * output device. If the current output representation is not yet complete
	 * then <code>drawImage</code> returns <code>false</code>. As more of the
	 * image becomes available, the process that loads the image notifies the
	 * specified image observer.
	 * <p>
	 * This method always uses the unscaled version of the image to render the
	 * scaled rectangle and performs the required scaling on the fly. It does
	 * not use a cached, scaled version of the image for this operation. Scaling
	 * of the image from source to destination is performed such that the first
	 * coordinate of the source rectangle is mapped to the first coordinate of
	 * the destination rectangle, and the second source coordinate is mapped to
	 * the second destination coordinate. The subimage is scaled and flipped as
	 * needed to preserve those mappings.
	 * 
	 * @param img
	 *            the specified image to be drawn. This method does nothing if
	 *            <code>img</code> is null.
	 * @param dx1
	 *            the <i>x</i> coordinate of the first corner of the destination
	 *            rectangle.
	 * @param dy1
	 *            the <i>y</i> coordinate of the first corner of the destination
	 *            rectangle.
	 * @param dx2
	 *            the <i>x</i> coordinate of the second corner of the
	 *            destination rectangle.
	 * @param dy2
	 *            the <i>y</i> coordinate of the second corner of the
	 *            destination rectangle.
	 * @param sx1
	 *            the <i>x</i> coordinate of the first corner of the source
	 *            rectangle.
	 * @param sy1
	 *            the <i>y</i> coordinate of the first corner of the source
	 *            rectangle.
	 * @param sx2
	 *            the <i>x</i> coordinate of the second corner of the source
	 *            rectangle.
	 * @param sy2
	 *            the <i>y</i> coordinate of the second corner of the source
	 *            rectangle.
	 * @param observer
	 *            object to be notified as more of the image is scaled and
	 *            converted.
	 * @return <code>false</code> if the image pixels are still changing;
	 *         <code>true</code> otherwise.
	 * @see java.awt.Image
	 * @see java.awt.image.ImageObserver
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int,
	 *      int, int, int)
	 * @since JDK1.1
	 */
	public abstract boolean drawImage(Image img, int dx1, int dy1, int dx2,
			int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer);

	/**
	 * Draws as much of the specified area of the specified image as is
	 * currently available, scaling it on the fly to fit inside the specified
	 * area of the destination drawable surface.
	 * <p>
	 * Transparent pixels are drawn in the specified background color. This
	 * operation is equivalent to filling a rectangle of the width and height of
	 * the specified image with the given color and then drawing the image on
	 * top of it, but possibly more efficient.
	 * <p>
	 * This method returns immediately in all cases, even if the image area to
	 * be drawn has not yet been scaled, dithered, and converted for the current
	 * output device. If the current output representation is not yet complete
	 * then <code>drawImage</code> returns <code>false</code>. As more of the
	 * image becomes available, the process that loads the image notifies the
	 * specified image observer.
	 * <p>
	 * This method always uses the unscaled version of the image to render the
	 * scaled rectangle and performs the required scaling on the fly. It does
	 * not use a cached, scaled version of the image for this operation. Scaling
	 * of the image from source to destination is performed such that the first
	 * coordinate of the source rectangle is mapped to the first coordinate of
	 * the destination rectangle, and the second source coordinate is mapped to
	 * the second destination coordinate. The subimage is scaled and flipped as
	 * needed to preserve those mappings.
	 * 
	 * @param img
	 *            the specified image to be drawn. This method does nothing if
	 *            <code>img</code> is null.
	 * @param dx1
	 *            the <i>x</i> coordinate of the first corner of the destination
	 *            rectangle.
	 * @param dy1
	 *            the <i>y</i> coordinate of the first corner of the destination
	 *            rectangle.
	 * @param dx2
	 *            the <i>x</i> coordinate of the second corner of the
	 *            destination rectangle.
	 * @param dy2
	 *            the <i>y</i> coordinate of the second corner of the
	 *            destination rectangle.
	 * @param sx1
	 *            the <i>x</i> coordinate of the first corner of the source
	 *            rectangle.
	 * @param sy1
	 *            the <i>y</i> coordinate of the first corner of the source
	 *            rectangle.
	 * @param sx2
	 *            the <i>x</i> coordinate of the second corner of the source
	 *            rectangle.
	 * @param sy2
	 *            the <i>y</i> coordinate of the second corner of the source
	 *            rectangle.
	 * @param bgcolor
	 *            the background color to paint under the non-opaque portions of
	 *            the image.
	 * @param observer
	 *            object to be notified as more of the image is scaled and
	 *            converted.
	 * @return <code>false</code> if the image pixels are still changing;
	 *         <code>true</code> otherwise.
	 * @see java.awt.Image
	 * @see java.awt.image.ImageObserver
	 * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int,
	 *      int, int, int)
	 * @since JDK1.1
	 */
	public abstract boolean drawImage(Image img, int dx1, int dy1, int dx2,
			int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor,
			ImageObserver observer);

	/**
	 * Disposes of this graphics context and releases any system resources that
	 * it is using. A <code>Graphics</code> object cannot be used after
	 * <code>dispose</code>has been called.
	 * <p>
	 * When a Java program runs, a large number of <code>Graphics</code> objects
	 * can be created within a short time frame. Although the finalization
	 * process of the garbage collector also disposes of the same system
	 * resources, it is preferable to manually free the associated resources by
	 * calling this method rather than to rely on a finalization process which
	 * may not run to completion for a long period of time.
	 * <p>
	 * Graphics objects which are provided as arguments to the
	 * <code>paint</code> and <code>update</code> methods of components are
	 * automatically released by the system when those methods return. For
	 * efficiency, programmers should call <code>dispose</code> when finished
	 * using a <code>Graphics</code> object only if it was created directly from
	 * a component or another <code>Graphics</code> object.
	 * 
	 * @see java.awt.Graphics#finalize
	 * @see java.awt.Component#paint
	 * @see java.awt.Component#update
	 * @see java.awt.Component#getGraphics
	 * @see java.awt.Graphics#create
	 */
	public abstract void dispose();

	/**
	 * Disposes of this graphics context once it is no longer referenced.
	 * 
	 * @see #dispose
	 */
	public void finalize() {
		dispose();
	}

	/**
	 * Returns a <code>String</code> object representing this
	 * <code>Graphics</code> object's value.
	 * 
	 * @return a string representation of this graphics context.
	 */
	public String toString() {
		return getClass().getName() + "[font=" + getFont() + ",color="
				+ getColor() + "]";
	}

	/**
	 * Returns the bounding rectangle of the current clipping area.
	 * 
	 * @return the bounding rectangle of the current clipping area or
	 *         <code>null</code> if no clip is set.
	 * @deprecated As of JDK version 1.1, replaced by
	 *             <code>getClipBounds()</code>.
	 */
	@Deprecated
	public Rectangle getClipRect() {
		return getClipBounds();
	}

	/**
	 * Returns true if the specified rectangular area might intersect the
	 * current clipping area. The coordinates of the specified rectangular area
	 * are in the user coordinate space and are relative to the coordinate
	 * system origin of this graphics context. This method may use an algorithm
	 * that calculates a result quickly but which sometimes might return true
	 * even if the specified rectangular area does not intersect the clipping
	 * area. The specific algorithm employed may thus trade off accuracy for
	 * speed, but it will never return false unless it can guarantee that the
	 * specified rectangular area does not intersect the current clipping area.
	 * The clipping area used by this method can represent the intersection of
	 * the user clip as specified through the clip methods of this graphics
	 * context as well as the clipping associated with the device or image
	 * bounds and window visibility.
	 * 
	 * @param x
	 *            the x coordinate of the rectangle to test against the clip
	 * @param y
	 *            the y coordinate of the rectangle to test against the clip
	 * @param width
	 *            the width of the rectangle to test against the clip
	 * @param height
	 *            the height of the rectangle to test against the clip
	 * @return <code>true</code> if the specified rectangle intersects the
	 *         bounds of the current clip; <code>false</code> otherwise.
	 */
	public boolean hitClip(final int x, final int y, final int width, final int height) {
		// Note, this implementation is not very efficient.
		// Subclasses should override this method and calculate
		// the results more directly.
		final Rectangle clipRect = getClipBounds();
		if (clipRect == null) {
			return true;
		}
		return clipRect.intersects(x, y, width, height);
	}

	/**
	 * Returns the bounding rectangle of the current clipping area. The
	 * coordinates in the rectangle are relative to the coordinate system origin
	 * of this graphics context. This method differs from
	 * {@link #getClipBounds() getClipBounds} in that an existing rectangle is
	 * used instead of allocating a new one. This method refers to the user
	 * clip, which is independent of the clipping associated with device bounds
	 * and window visibility. If no clip has previously been set, or if the clip
	 * has been cleared using <code>setClip(null)</code>, this method returns
	 * the specified <code>Rectangle</code>.
	 * 
	 * @param r
	 *            the rectangle where the current clipping area is copied to.
	 *            Any current values in this rectangle are overwritten.
	 * @return the bounding rectangle of the current clipping area.
	 */
	public Rectangle getClipBounds(final Rectangle r) {
		// Note, this implementation is not very efficient.
		// Subclasses should override this method and avoid
		// the allocation overhead of getClipBounds().
		final Rectangle clipRect = getClipBounds();
		if (clipRect != null) {
			r.x = clipRect.x;
			r.y = clipRect.y;
			r.width = clipRect.width;
			r.height = clipRect.height;
		} else if (r == null) {
			throw new NullPointerException("null rectangle parameter");
		}
		return r;
	}
}
