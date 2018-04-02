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

import hc.android.ActivityManager;
import hc.android.ImageUtil;
import hc.android.J2SEInitor;
import hc.android.UICore;

import java.awt.datatransfer.Clipboard;
import java.awt.event.AWTEventListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.ToneGenerator;

/**
 * This class is the abstract superclass of all actual implementations of the
 * Abstract Window Toolkit. Subclasses of the <code>Toolkit</code> class are
 * used to bind the various components to particular native toolkit
 * implementations.
 * <p>
 * Many GUI events may be delivered to user asynchronously, if the opposite is
 * not specified explicitly. As well as many GUI operations may be performed
 * asynchronously. This fact means that if the state of a component is set, and
 * then the state immediately queried, the returned value may not yet reflect
 * the requested change. This behavior includes, but is not limited to:
 * <ul>
 * <li>Scrolling to a specified position. <br>
 * For example, calling <code>ScrollPane.setScrollPosition</code> and then
 * <code>getScrollPosition</code> may return an incorrect value if the original
 * request has not yet been processed.
 * <p>
 * <li>Moving the focus from one component to another. <br>
 * For more information, see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/misc/focus.html#transferTiming">Timing
 * Focus Transfers</a>, a section in
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/">The Swing
 * Tutorial</a>.
 * <p>
 * <li>Making a top-level container visible. <br>
 * Calling <code>setVisible(true)</code> on a <code>Window</code>,
 * <code>Frame</code> or <code>Dialog</code> may occur asynchronously.
 * <p>
 * <li>Setting the size or location of a top-level container. <br>
 * Calls to <code>setSize</code>, <code>setBounds</code> or
 * <code>setLocation</code> on a <code>Window</code>, <code>Frame</code> or
 * <code>Dialog</code> are forwarded to the underlying window management system
 * and may be ignored or modified. See {@link java.awt.Window} for more
 * information.
 * </ul>
 * <p>
 * Most applications should not call any of the methods in this class directly.
 * The methods defined by <code>Toolkit</code> are the "glue" that joins the
 * platform-independent classes in the <code>java.awt</code> package with their
 * counterparts in <code>java.awt.peer</code>. Some methods defined by
 * <code>Toolkit</code> query the native operating system directly.
 *
 * @author Sami Shaio
 * @author Arthur van Hoff
 * @author Fred Ecks
 * @since JDK1.0
 */
public abstract class Toolkit {
	static EventQueue getEventQueue() {
		return getDefaultToolkit().getSystemEventQueueImpl();
	}

	protected abstract EventQueue getSystemEventQueueImpl();

	public abstract void setSystemEventQueueAdAPI(EventQueue queue);

	private final static Toolkit toolkit = new Toolkit() {
		Clipboard clipboard;

		@Override
		public Clipboard getSystemClipboard() {
			if (clipboard == null) {
				clipboard = new Clipboard("");
			}
			return clipboard;
		}

		@Override
		public Image getImage(URL url) {
			InputStream is = null;
			try {
				is = url.openStream();
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				BufferedImage bi = new BufferedImage(bitmap);
				return bi;
			} catch (Throwable e) {
			} finally {
				try {
					is.close();
				} catch (Throwable e) {
				}
			}
			return null;
		}

		@Override
		public Image getImage(String filename) {
			InputStream is = J2SEInitor.class.getResourceAsStream(filename);
			Bitmap bitmap = BitmapFactory.decodeStream(is);

			BufferedImage bi = new BufferedImage(bitmap);
			return bi;
		}

		@Override
		public Image createImage(byte[] imagedata, int offset, int len) {
			Bitmap bitmap = ImageUtil.PNGBytes2Bimap(imagedata, offset, len);
			BufferedImage bi = new BufferedImage(bitmap);
			return bi;
		}

		@Override
		public Image createImage(URL url) {
			return null;
		}

		@Override
		public Image createImage(String filename) {
			return null;
		}

		@Override
		protected EventQueue getSystemEventQueueImpl() {
			return queue;
		}

		@Override
		public void setSystemEventQueueAdAPI(EventQueue queue) {
			this.queue = queue;
		}

		@Override
		public void beep() {
			ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_SYSTEM,
					ToneGenerator.MAX_VOLUME);
			toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
		}

		@Override
		public Dimension getScreenSize() throws HeadlessException {
			return new Dimension(J2SEInitor.screenWidth, J2SEInitor.screenHeight);
		}
	};

	public static synchronized Toolkit getDefaultToolkit() {
		return toolkit;
	}

	public abstract Image getImage(String filename);

	public abstract Image getImage(URL url);

	public abstract Image createImage(String filename);

	public abstract Image createImage(URL url);

	public Image createImage(byte[] imagedata) {
		return createImage(imagedata, 0, imagedata.length);
	}

	public abstract Image createImage(byte[] imagedata, int imageoffset, int imagelength);

	public abstract Clipboard getSystemClipboard();

	EventQueue queue = new EventQueue();

	public final EventQueue getSystemEventQueue() {
		return queue;
	}

	public boolean isAlwaysOnTopSupported() {
		return true;
	}

	public void addAWTEventListener(AWTEventListener listener, long eventMask) {
	}

	public void removeAWTEventListener(AWTEventListener listener) {
	}

	public AWTEventListener[] getAWTEventListeners() {
		return null;
	}

	public FontMetrics getFontMetrics(Font f) {
		return null;
	}

	public Cursor createCustomCursor(Image cursor, Point hotSpot, String name)
			throws IndexOutOfBoundsException, HeadlessException {
		// Override to implement custom cursor support.
		if (this != Toolkit.getDefaultToolkit()) {
			return Toolkit.getDefaultToolkit().createCustomCursor(cursor, hotSpot, name);
		} else {
			return new Cursor(Cursor.DEFAULT_CURSOR);
		}
	}

	public int getMenuShortcutKeyMask() throws HeadlessException {
		GraphicsEnvironment.checkHeadless();

		return Event.CTRL_MASK;
	}

	public abstract Dimension getScreenSize() throws HeadlessException;

	public Dimension getBestCursorSize(int preferredWidth, int preferredHeight)
			throws HeadlessException {
		GraphicsEnvironment.checkHeadless();

		// Override to implement custom cursor support.
		if (this != Toolkit.getDefaultToolkit()) {
			return Toolkit.getDefaultToolkit().getBestCursorSize(preferredWidth, preferredHeight);
		} else {
			return new Dimension(0, 0);
		}
	}

	public abstract void beep();

	public AWTEventListener[] getAWTEventListeners(long eventMask) {
		return null;
	}

}