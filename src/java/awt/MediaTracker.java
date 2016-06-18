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

import hc.android.AndroidClassUtil;

/**
 * The <code>MediaTracker</code> class is a utility class to track
 * the status of a number of media objects. Media objects could
 * include audio clips as well as images, though currently only
 * images are supported.
 * <p>
 * To use a media tracker, create an instance of
 * <code>MediaTracker</code> and call its <code>addImage</code>
 * method for each image to be tracked. In addition, each image can
 * be assigned a unique identifier. This identifier controls the
 * priority order in which the images are fetched. It can also be used
 * to identify unique subsets of the images that can be waited on
 * independently. Images with a lower ID are loaded in preference to
 * those with a higher ID number.
 *
 * <p>
 *
 * Tracking an animated image
 * might not always be useful
 * due to the multi-part nature of animated image
 * loading and painting,
 * but it is supported.
 * <code>MediaTracker</code> treats an animated image
 * as completely loaded
 * when the first frame is completely loaded.
 * At that point, the <code>MediaTracker</code>
 * signals any waiters
 * that the image is completely loaded.
 * If no <code>ImageObserver</code>s are observing the image
 * when the first frame has finished loading,
 * the image might flush itself
 * to conserve resources
 * (see {@link Image#flush()}).
 *
 * <p>
 * Here is an example of using <code>MediaTracker</code>:
 * <p>
 * <hr><blockquote><pre>
 * import java.applet.Applet;
 * import java.awt.Color;
 * import java.awt.Image;
 * import java.awt.Graphics;
 * import java.awt.MediaTracker;
 *
 * public class ImageBlaster extends Applet implements Runnable {
 *      MediaTracker tracker;
 *      Image bg;
 *      Image anim[] = new Image[5];
 *      int index;
 *      Thread animator;
 *
 *      // Get the images for the background (id == 0)
 *      // and the animation frames (id == 1)
 *      // and add them to the MediaTracker
 *      public void init() {
 *          tracker = new MediaTracker(this);
 *          bg = getImage(getDocumentBase(),
 *                  "images/background.gif");
 *          tracker.addImage(bg, 0);
 *          for (int i = 0; i < 5; i++) {
 *              anim[i] = getImage(getDocumentBase(),
 *                      "images/anim"+i+".gif");
 *              tracker.addImage(anim[i], 1);
 *          }
 *      }
 *
 *      // Start the animation thread.
 *      public void start() {
 *          animator = new Thread(this);
 *          animator.start();
 *      }
 *
 *      // Stop the animation thread.
 *      public void stop() {
 *          animator = null;
 *      }
 *
 *      // Run the animation thread.
 *      // First wait for the background image to fully load
 *      // and paint.  Then wait for all of the animation
 *      // frames to finish loading. Finally, loop and
 *      // increment the animation frame index.
 *      public void run() {
 *          try {
 *              tracker.waitForID(0);
 *              tracker.waitForID(1);
 *          } catch (InterruptedException e) {
 *              return;
 *          }
 *          Thread me = Thread.currentThread();
 *          while (animator == me) {
 *              try {
 *                  Thread.sleep(100);
 *              } catch (InterruptedException e) {
 *                  break;
 *              }
 *              synchronized (this) {
 *                  index++;
 *                  if (index >= anim.length) {
 *                      index = 0;
 *                  }
 *              }
 *              repaint();
 *          }
 *      }
 *
 *      // The background image fills the frame so we
 *      // don't need to clear the applet on repaints.
 *      // Just call the paint method.
 *      public void update(Graphics g) {
 *          paint(g);
 *      }
 *
 *      // Paint a large red rectangle if there are any errors
 *      // loading the images.  Otherwise always paint the
 *      // background so that it appears incrementally as it
 *      // is loading.  Finally, only paint the current animation
 *      // frame if all of the frames (id == 1) are done loading,
 *      // so that we don't get partial animations.
 *      public void paint(Graphics g) {
 *          if ((tracker.statusAll(false) & MediaTracker.ERRORED) != 0) {
 *              g.setColor(Color.red);
 *              g.fillRect(0, 0, size().width, size().height);
 *              return;
 *          }
 *          g.drawImage(bg, 0, 0, this);
 *          if (tracker.statusID(1, false) == MediaTracker.COMPLETE) {
 *              g.drawImage(anim[index], 10, 10, this);
 *          }
 *      }
 * }
 * </pre></blockquote><hr>
 *
 * @author      Jim Graham
 * @since       JDK1.0
 */
public class MediaTracker implements java.io.Serializable {

	Component target;
	MediaEntry head;

	public MediaTracker(Component comp) {
		target = comp;
	}

	public void addImage(Image image, int id) {
		addImage(image, id, -1, -1);
	}

	public synchronized void addImage(Image image, int id, int w, int h) {
		AndroidClassUtil.callEmptyMethod();
	}

	public static final int LOADING = 1;
	public static final int ABORTED = 2;
	public static final int ERRORED = 4;
	public static final int COMPLETE = 8;

	static final int DONE = (ABORTED | ERRORED | COMPLETE);

	public boolean checkAll() {
		return false;
	}

	public boolean checkAll(boolean load) {
		return false;
	}

	public synchronized boolean isErrorAny() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public synchronized Object[] getErrorsAny() {
		AndroidClassUtil.callEmptyMethod();
		Object errors[] = new Object[0];
		return errors;
	}

	public void waitForAll() throws InterruptedException {
		waitForAll(0);
	}

	public synchronized boolean waitForAll(long ms) throws InterruptedException {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public int statusAll(boolean load) {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public boolean checkID(int id) {
		return checkID(id, false, true);
	}

	public boolean checkID(int id, boolean load) {
		return checkID(id, load, true);
	}

	private synchronized boolean checkID(int id, boolean load, boolean verify) {
		AndroidClassUtil.callEmptyMethod();
		return true;
	}

	public synchronized boolean isErrorID(int id) {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public synchronized Object[] getErrorsID(int id) {
		Object errors[] = new Object[0];
		return errors;
	}

	public void waitForID(int id) throws InterruptedException {
		waitForID(id, 0);
	}

	public synchronized boolean waitForID(int id, long ms)
			throws InterruptedException {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public int statusID(int id, boolean load) {
		return statusID(id, load, true);
	}

	private synchronized int statusID(int id, boolean load, boolean verify) {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public synchronized void removeImage(Image image) {
		AndroidClassUtil.callEmptyMethod();
	}

	public synchronized void removeImage(Image image, int id) {
		AndroidClassUtil.callEmptyMethod();
	}

	public synchronized void removeImage(Image image, int id, int width,
			int height) {
		AndroidClassUtil.callEmptyMethod();
	}

	synchronized void setDone() {
		AndroidClassUtil.callEmptyMethod();
	}
}

abstract class MediaEntry {
	MediaTracker tracker;
	int ID;
	MediaEntry next;

	int status;
	boolean cancelled;

	MediaEntry(MediaTracker mt, int id) {
		tracker = mt;
		ID = id;
	}

	abstract Object getMedia();

	static MediaEntry insert(MediaEntry head, MediaEntry me) {
		AndroidClassUtil.callEmptyMethod();
		return head;
	}

	int getID() {
		return ID;
	}

	abstract void startLoad();

	void cancel() {
		cancelled = true;
	}

	static final int LOADING = MediaTracker.LOADING;
	static final int ABORTED = MediaTracker.ABORTED;
	static final int ERRORED = MediaTracker.ERRORED;
	static final int COMPLETE = MediaTracker.COMPLETE;

	static final int LOADSTARTED = (LOADING | ERRORED | COMPLETE);
	static final int DONE = (ABORTED | ERRORED | COMPLETE);

	synchronized int getStatus(boolean doLoad, boolean doVerify) {
		AndroidClassUtil.callEmptyMethod();
		return status;
	}

	void setStatus(int flag) {
		AndroidClassUtil.callEmptyMethod();
	}
}
