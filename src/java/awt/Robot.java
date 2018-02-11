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
import hc.android.AndroidClassUtil;
import hc.android.AndroidPlatformService;
import hc.android.InputSystem;
import hc.android.J2SEInitor;
import hc.android.KeyEventThreadBinder;
import hc.android.UIThreadViewChanger;
import hc.android.AndroidUIUtil;
import hc.android.WindowManager;

import java.awt.image.BufferedImage;
import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

/**
 * This class is used to generate native system input events
 * for the purposes of test automation, self-running demos, and
 * other applications where control of the mouse and keyboard
 * is needed. The primary purpose of Robot is to facilitate
 * automated testing of Java platform implementations.
 * <p>
 * Using the class to generate input events differs from posting
 * events to the AWT event queue or AWT components in that the
 * events are generated in the platform's native input
 * queue. For example, <code>Robot.mouseMove</code> will actually move
 * the mouse cursor instead of just generating mouse move events.
 * <p>
 * Note that some platforms require special privileges or extensions
 * to access low-level input control. If the current platform configuration
 * does not allow input control, an <code>AWTException</code> will be thrown
 * when trying to construct Robot objects. For example, X-Window systems
 * will throw the exception if the XTEST 2.2 standard extension is not supported
 * (or not enabled) by the X server.
 * <p>
 * Applications that use Robot for purposes other than self-testing should
 * handle these error conditions gracefully.
 *
 * @author      Robi Khan
 * @since       1.3
 */
public class Robot {
	private boolean isAutoWaitForIdle = false;
	private int autoDelay = 0;
	private static int LEGAL_BUTTON_MASK = 0;

	public Robot() throws AWTException {
	}

	 public Robot(GraphicsDevice screen) throws AWTException {
		 this();
	 }
	 private int mouseX, mouseY;
	 
	public synchronized void mouseMove(int x, int y) {
        long downTime = SystemClock.uptimeMillis() - EVENT_MIN_INTERVAL;
        long eventTime = SystemClock.uptimeMillis();
        int metaState = 0;

        mouseX = x;
        mouseY = y;
        
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, metaState);

        dispathEventAdAPI(event);
	}

	static boolean isInputMode = false;
	static boolean isInputSystemErrorOnMouse=false;
	
	public void mousePress(int buttons) {
		if(isInputSystemErrorOnMouse == false && InputSystem.mouseClick(mouseX, mouseY) == false){
			isInputSystemErrorOnMouse = true;
		}
		if(isInputSystemErrorOnMouse){
			mousePressByEvent();
		}
	}

	private void mousePressByEvent() {
		long downTime = SystemClock.uptimeMillis() - EVENT_MIN_INTERVAL;
        long eventTime = SystemClock.uptimeMillis();
        int metaState = 0;

        final MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, mouseX, mouseY, metaState);
        
        dispathEventAdAPI(event);
	}

	private final void dispathEventAdAPI(final MotionEvent event) {
		final Activity activity = ActivityManager.getActivity();
        activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				activity.dispatchTouchEvent(event);
			}
		});
	}

	public void mouseRelease(int buttons) {
		if(isInputSystemErrorOnMouse == false){
			
		}else{
			mouseReleaseByEvent();
		}
	}

	private void mouseReleaseByEvent() {
		long downTime = SystemClock.uptimeMillis() - EVENT_MIN_INTERVAL;
        long eventTime = SystemClock.uptimeMillis();
        int metaState = 0;

        MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, mouseX, mouseY, metaState);

        dispathEventAdAPI(event);
	}
	
	private static final int EVENT_MIN_INTERVAL = 10;
	
	public static void generateZoomGesture(Instrumentation inst,
	        long startTime, boolean ifMove, Point startPoint1,
	        Point startPoint2, Point endPoint1,
	        Point endPoint2, int duration) {

	    if (inst == null || startPoint1 == null
	            || (ifMove && endPoint1 == null)) {
	        return;
	    }

	    long eventTime = startTime;
	    long downTime = startTime;
	    MotionEvent event;
	    float eventX1, eventY1, eventX2, eventY2;

	    eventX1 = startPoint1.x;
	    eventY1 = startPoint1.y;
	    eventX2 = startPoint2.x;
	    eventY2 = startPoint2.y;

	    // specify the property for the two touch points
	    MotionEvent.PointerProperties[] properties = new MotionEvent.PointerProperties[2];
	    MotionEvent.PointerProperties pp1 = new MotionEvent.PointerProperties();
	    pp1.id = 0;
	    pp1.toolType = MotionEvent.TOOL_TYPE_FINGER;
	    MotionEvent.PointerProperties pp2 = new MotionEvent.PointerProperties();
	    pp2.id = 1;
	    pp2.toolType = MotionEvent.TOOL_TYPE_FINGER;

	    properties[0] = pp1;
	    properties[1] = pp2;

	    //specify the coordinations of the two touch points
	    //NOTE: you MUST set the pressure and size value, or it doesn't work
	    MotionEvent.PointerCoords[] pointerCoords = new MotionEvent.PointerCoords[2];
	    MotionEvent.PointerCoords pc1 = new MotionEvent.PointerCoords();
	    pc1.x = eventX1;
	    pc1.y = eventY1;
	    pc1.pressure = 1;
	    pc1.size = 1;
	    MotionEvent.PointerCoords pc2 = new MotionEvent.PointerCoords();
	    pc2.x = eventX2;
	    pc2.y = eventY2;
	    pc2.pressure = 1;
	    pc2.size = 1;
	    pointerCoords[0] = pc1;
	    pointerCoords[1] = pc2;

	    //////////////////////////////////////////////////////////////
	    // events sequence of zoom gesture
	    // 1. send ACTION_DOWN event of one start point
	    // 2. send ACTION_POINTER_2_DOWN of two start points
	    // 3. send ACTION_MOVE of two middle points
	    // 4. repeat step 3 with updated middle points (x,y),
	    //      until reach the end points
	    // 5. send ACTION_POINTER_2_UP of two end points
	    // 6. send ACTION_UP of one end point
	    //////////////////////////////////////////////////////////////

	    // step 1
	    event = MotionEvent.obtain(downTime, eventTime, 
	                MotionEvent.ACTION_DOWN, 1, properties, 
	                pointerCoords, 0,  0, 1, 1, 0, 0, 0, 0 );

	    inst.sendPointerSync(event);

	    //step 2
	    event = MotionEvent.obtain(downTime, eventTime, 
	                MotionEvent.ACTION_POINTER_2_DOWN, 2, 
	                properties, pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);

	    inst.sendPointerSync(event);

	    //step 3, 4
	    if (ifMove) {
	        int moveEventNumber = 1;
	        moveEventNumber = duration / EVENT_MIN_INTERVAL;

	        float stepX1, stepY1, stepX2, stepY2;

	        stepX1 = (endPoint1.x - startPoint1.x) / moveEventNumber;
	        stepY1 = (endPoint1.y - startPoint1.y) / moveEventNumber;
	        stepX2 = (endPoint2.x - startPoint2.x) / moveEventNumber;
	        stepY2 = (endPoint2.y - startPoint2.y) / moveEventNumber;

	        for (int i = 0; i < moveEventNumber; i++) {
	            // update the move events
	            eventTime += EVENT_MIN_INTERVAL;
	            eventX1 += stepX1;
	            eventY1 += stepY1;
	            eventX2 += stepX2;
	            eventY2 += stepY2;

	            pc1.x = eventX1;
	            pc1.y = eventY1;
	            pc2.x = eventX2;
	            pc2.y = eventY2;

	            pointerCoords[0] = pc1;
	            pointerCoords[1] = pc2;

	            event = MotionEvent.obtain(downTime, eventTime,
	                        MotionEvent.ACTION_MOVE, 2, properties, 
	                        pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);

	            inst.sendPointerSync(event);
	        }
	    }

	    //step 5
	    pc1.x = endPoint1.x;
	    pc1.y = endPoint1.y;
	    pc2.x = endPoint2.x;
	    pc2.y = endPoint2.y;
	    pointerCoords[0] = pc1;
	    pointerCoords[1] = pc2;

	    eventTime += EVENT_MIN_INTERVAL;
	    event = MotionEvent.obtain(downTime, eventTime,
	                MotionEvent.ACTION_POINTER_2_UP, 2, properties, 
	                pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
	    inst.sendPointerSync(event);

	    // step 6
	    eventTime += EVENT_MIN_INTERVAL;
	    event = MotionEvent.obtain(downTime, eventTime, 
	                MotionEvent.ACTION_UP, 1, properties, 
	                pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0 );
	    inst.sendPointerSync(event);
	}

	private void checkButtonsArgument(int buttons) {
		if ((buttons | LEGAL_BUTTON_MASK) != LEGAL_BUTTON_MASK) {
			throw new IllegalArgumentException(
					"Invalid combination of button flags");
		}
	}

	public synchronized void mouseWheel(int wheelAmt) {
		AndroidClassUtil.callEmptyMethod();
	}

    final Instrumentation inst = new Instrumentation();

    /**
     * 注意：本方法加锁与KeyComper.keyAction互斥。因为有可能另线程单独J2SE的VK*
     * @param keycode 符合J2SE的KeyEvent.VK_*
     */
    public synchronized void keyPress(int keycode) {
    	boolean isForceAndroidkeycode = KeyEventThreadBinder.isForceAndroidKeyCode();
    	
    	if(isForceAndroidkeycode == false){//非Android KeyCode，即标准J2SE，需要转换
    		keycode = AndroidPlatformService.convertToAndroidKeyCodeAdAPI(keycode);
    	}
    	
    	//see keyRelease 
		if(isInputMode){
			InputSystem.keyPressed(keycode, true);
		}else{
			if(keycode == KeyEventThreadBinder.CTRL_MASK || keycode == KeyEventThreadBinder.SHIFT_MASK 
					|| keycode == KeyEventThreadBinder.ALT_MASK){
				KeyEventThreadBinder.bindMeta(keycode);
			}else{
				final int mask = KeyEventThreadBinder.getMetaAndReset();
				if(mask == 0){
					inst.sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, keycode));
					inst.sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, keycode));
				}else{
					final long downTime = SystemClock.uptimeMillis();
		            final long eventTime = SystemClock.uptimeMillis();

		            final KeyEvent altDown = new KeyEvent(downTime, eventTime, KeyEvent.ACTION_DOWN,
		            		keycode, 1, mask);
		            final KeyEvent altUp = new KeyEvent(downTime, eventTime, KeyEvent.ACTION_UP,
		            		keycode, 1, mask);

		            inst.sendKeySync(altDown);//TOFIX:会抛出如下异常
//		        	Exception : java.lang.SecurityException
//		        	at : android.os.Parcel.readException(Parcel.java:1546)
//		        	at : android.os.Parcel.readException(Parcel.java:1499)
//		        	at : android.hardware.input.IInputManager$Stub$Proxy.injectInputEvent(IInputManager.java:417)
//		        	at : android.hardware.input.InputManager.injectInputEvent(InputManager.java:685)
//		        	at : android.app.Instrumentation.sendKeySync(Instrumentation.java:904)
		            inst.sendKeySync(altUp);
				}
			}
		}
	}

	public void keyRelease(final int keycode) {
		//内含down, up两个
		if(isInputMode){
			InputSystem.keyPressed(keycode, false);
		}else{
			inst.sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, keycode));
		}
	}

	private void checkKeycodeArgument(int keycode) {
		// if (keycode == KeyEvent.VK_UNDEFINED) {
		// throw new IllegalArgumentException("Invalid key code");
		// }
	}

	 public synchronized Color getPixelColor(int x, int y) {
		 Bitmap b1 = capBitmap();
		 Color c = new Color(b1.getPixel(x, y));
		 b1.recycle();
		 return c;
	 }

	 final Bitmap[] capBitmapOut = new Bitmap[1];
	 final Runnable capRunnable = new Runnable() {
		@Override
		public void run() {
			capBitmapOut[0] = capBitmap();
		}
	};
	
	public synchronized BufferedImage createScreenCapture(Rectangle screenRect) {
		AndroidUIUtil.runOnUiThreadAndWait(capRunnable);

		Bitmap b1 = capBitmapOut[0];
		
		BufferedImage image;
		if (screenRect.x == 0 && screenRect.y == 0
				&& screenRect.width == b1.getWidth()
				&& screenRect.height == b1.getHeight()) {
			image = new BufferedImage(b1);
		} else {
			image = new BufferedImage(Bitmap.createBitmap(b1, screenRect.x,
					screenRect.y, screenRect.width, screenRect.height));
		}
		return image;
	}

	View capView;
	Bitmap capBitmap, popBitmap;
	Canvas capCanvas, popCanvas;
	final int[] loc = new int[2];
	int[] popArea;
	int capWidth, capHeight;
	
	private final Bitmap capBitmap() {
//     process = Runtime.getRuntime().exec("input keyevent 120");
//		The output picture will be saved here: /sdcard/Pictures/Screenshots
		
		if(capView == null){
			capView = UIThreadViewChanger.getContentView();//ActivityManager.getActivity().getWindow().getDecorView();
		}
		if(capBitmap == null){
			//因为capView在锁屏时，会返回w:0, h:0
			capWidth = J2SEInitor.screenWidth;
			capHeight = J2SEInitor.screenHeight;
			
			capBitmap = Bitmap.createBitmap(capWidth, capHeight, Config.ARGB_8888);
			capCanvas = new Canvas(capBitmap);
			
			popBitmap = Bitmap.createBitmap(capWidth, capHeight, Config.ARGB_8888);
			popCanvas = new Canvas(popBitmap);
			
			popArea = new int[capWidth * capHeight];
		}
		capView.draw(capCanvas);
		final PopupWindow popWin = WindowManager.getCurrPopup();
		if(popWin != null){
			//合成两层图片
			View contentView = popWin.getContentView();
			contentView.getLocationOnScreen(loc);
			
			final int w = contentView.getWidth();
			final int h = contentView.getHeight();

			capBitmap.getPixels(popArea, 0, capWidth, loc[0], loc[1], w, h);
			popBitmap.setPixels(popArea, 0, capWidth, 0, 0, w, h);
			
			contentView.draw(popCanvas);

			popBitmap.getPixels(popArea, 0, capWidth, 0, 0, w, h);
			capBitmap.setPixels(popArea, 0, capWidth, loc[0], loc[1], w, h);
		}
		return capBitmap;
	}

	public synchronized boolean isAutoWaitForIdle() {
		return isAutoWaitForIdle;
	}

	public synchronized void setAutoWaitForIdle(boolean isOn) {
		isAutoWaitForIdle = isOn;
	}

	private void autoWaitForIdle() {
		if (isAutoWaitForIdle) {
			waitForIdle();
		}
	}

	public synchronized int getAutoDelay() {
		return autoDelay;
	}

	public synchronized void setAutoDelay(int ms) {
		autoDelay = ms;
	}

	private void autoDelay() {
		delay(autoDelay);
	}

	public synchronized void delay(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ite) {
			ite.printStackTrace();
		}
	}

	public synchronized void waitForIdle() {
	}

}