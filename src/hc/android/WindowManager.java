package hc.android;

import hc.App;
import hc.core.ContextManager;
import hc.core.L;
import hc.core.util.LogManager;
import hc.core.util.Stack;
import hc.server.PlatformManager;
import hc.util.ExitManager;
import hc.util.PropertiesManager;
import hc.util.ResourceUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.swing.JOptionPane;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class WindowManager {
	private static Window currentWindow;
	private static Stack hcWindowStack = new Stack();
	private static final ConcurrentMap<Dialog, Integer> dialogLock = new ConcurrentHashMap<Dialog, Integer>();
	private static final ConcurrentMap<ViewGroup, View> focusViewOfVG = new ConcurrentHashMap<ViewGroup, View>();

	private static PopupWindow currPopup;
	private static boolean isActioned = false;

	public static void notifyPopupWindowActioned() {
		if (currPopup != null) {
			isActioned = true;
		}
	}

	public static boolean isLastPopupWindowActioned() {
		return isActioned;
	}

	public static synchronized void showPopupWindow(final PopupWindow popupWindow,
			final Component invoker) {
		AndroidUIUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				View peerAdAPI = invoker.getPeerAdAPI();
				if (invoker instanceof HCHomeIcon) {
					popupWindow.showAsDropDown(peerAdAPI, peerAdAPI.getWidth() / 2,
							-peerAdAPI.getHeight() / 2);
				} else {
					popupWindow.showAsDropDown(peerAdAPI, 0, 0);// on Button
				}
				popupWindow.update();

				if (currPopup != null) {
					disposePopupWindow();
				}
				isActioned = false;
				currPopup = popupWindow;
			}
		});
	}

	public static PopupWindow getCurrPopup() {
		return currPopup;
	}

	public static synchronized boolean disposePopupWindow() {
		if (currPopup != null) {
			currPopup.dismiss();
			currPopup = null;
			return true;
		} else {
			return false;
		}
	}

	public static Window getFocusWindow() {
		return currentWindow;
	}

	public static boolean isFocusWindow(Window win) {
		return win == currentWindow;
	}

	public static synchronized Window[] getWindows() {
		if (currentWindow != null) {
			Window[] out = new Window[hcWindowStack.size() + 1];
			for (int i = 0; i < (out.length - 1); i++) {
				out[i] = (Window) hcWindowStack.elementAt(i);
			}
			out[out.length - 1] = currentWindow;
			return out;
		} else {
			return new Window[0];
		}

	}

	// 由于Dialog会锁住线程，所以此方法加锁会与toFront互锁
	public static void askMainBoardExit() {
		String areQuit = (String) ResourceUtil.get(7000);
		String runInBackground = (String) ResourceUtil.get(7001);
		String fullExit = (String) ResourceUtil.get(7002);
		String cancel = (String) ResourceUtil.get(1018);

		String[] options = { runInBackground, fullExit, cancel };

		int out = App.showOptionDialog(null, areQuit, (String) ResourceUtil.get(5),
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
				runInBackground);// App.getSysIcon(App.SYS_QUES_ICON)
		if (out == JOptionPane.YES_OPTION) {
			runInBackgroundAndJumpHome(false);
			return;
		} else if (out == JOptionPane.NO_OPTION) {
			DebugLogger.log(fullExit);

			quitAndExit();
		} else if (out == JOptionPane.CANCEL_OPTION) {
			DebugLogger.log(cancel);
		}
	}

	public static void quitAndExit() {
		NotificationManager nm = (NotificationManager) ActivityManager.applicationContext
				.getSystemService(Activity.NOTIFICATION_SERVICE);
		nm.cancelAll();

		ExitManager.startExitSystem();
		PlatformManager.getService().exitSystem();
	}

	private static void runInBackgroundAndJumpHome(final boolean tipBackground) {
		final String background = (String) ResourceUtil.get(7001);
		DebugLogger.log(background);

		if (tipBackground) {
			AndroidUIUtil.showCenterToast(background);
		}

		jumpToHomeScreen();
	}

	public static void jumpToHomeScreen() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		ActivityManager.applicationContext.startActivity(intent);
	}

	public static boolean notifyClose(Window win) {
		if (win == null) {
			if (currentWindow != null) {
				win = currentWindow;
			} else {
				if (PropertiesManager.isSimu()) {// 模拟环境下直接关闭退出，
					quitAndExit();
				} else {// 正常用户环境下，转为后台运行
					runInBackgroundAndJumpHome(true);// 缺省后台运行，不提问
				}
				// askMainBoardExit();
				return true;
			}
		}

		// windowClosing
		WindowEvent event = new WindowEvent(win, WindowEvent.WINDOW_CLOSING);
		win.processEventAdAPI(event);

		return true;
	}

	public static void closeWindow(final Window win) {
		AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				View windowViewAdAPI = win.getWindowViewAdAPI();
				UIThreadViewChanger.removeView(windowViewAdAPI);

				if (win == currentWindow) {
					sendWindowEvent(win, WindowEvent.WINDOW_DEACTIVATED);
					sendWindowEvent(win, WindowEvent.WINDOW_LOST_FOCUS);
				}
				sendWindowEvent(win, WindowEvent.WINDOW_CLOSED);
				sendWindowEvent(win, WindowEvent.WINDOW_STATE_CHANGED);
				focusViewOfVG.remove((ViewGroup) windowViewAdAPI);

				if (win == currentWindow) {
					currentWindow = (Window) hcWindowStack.pop();
					// windowActivated
					if (currentWindow != null) {
						final View windowView = currentWindow.getWindowViewAdAPI();
						windowView.setClickable(true);
						sendWindowEvent(currentWindow, WindowEvent.WINDOW_GAINED_FOCUS);
						sendWindowEvent(currentWindow, WindowEvent.WINDOW_ACTIVATED);
						sendWindowEvent(currentWindow, WindowEvent.WINDOW_STATE_CHANGED);

						switchFocusView((ViewGroup) windowView);
						// show(currentWindow.getWindowViewAdAPI(), false);
					} else {
						// HCDesktop
						ViewGroup vg = getHCDesktopVG();
						if (vg != null) {
							switchFocusView(vg);
						}
					}
				}
			}
		});

		System.gc();

		if (win instanceof Dialog) {
			// UIThreadViewChanger.removeView(dialogBackView);

			if (((Dialog) win).getModalityType() != ModalityType.MODELESS) {
				final Integer lock = dialogLock.get(win);
				if (lock != null) {
					dialogLock.remove(win);
					AndroidUIUtil.runDelayNotInUIThread(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(Constants.UI_DELAY_MS);// 等待后序关闭逻辑执行完毕
							} catch (Exception e) {
							}
							synchronized (lock) {
								try {
									lock.notify();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					});
				}
			}
		}
	}

	private static void sendWindowEvent(Window win, int id) {
		WindowEvent event = new WindowEvent(win, id);
		win.processEventAdAPI(event);
	}

	public static synchronized void toBack(Window win) {
		if (win != currentWindow) {
			return;
		}

		Window next = (Window) hcWindowStack.pop();
		if (next == null) {
			return;
		}

		toFront(next);
	}

	public static synchronized boolean toFront(Window win) {
		if (win == currentWindow) {
			return true;
		}

		int idx = hcWindowStack.search(win);
		boolean isOpened = (idx >= 0);
		if (isOpened) {
			hcWindowStack.removeAt(idx);
		}

		if (currentWindow != null) {
			sendWindowEvent(currentWindow, WindowEvent.WINDOW_DEACTIVATED);
			sendWindowEvent(currentWindow, WindowEvent.WINDOW_LOST_FOCUS);
			sendWindowEvent(currentWindow, WindowEvent.WINDOW_STATE_CHANGED);

			final ViewGroup group = (ViewGroup) currentWindow.getWindowViewAdAPI();
			group.setClickable(false);
			final View focusedChild = group.getFocusedChild();
			if (group != null && focusedChild != null) {
				saveFocusView(group, focusedChild);
			}
			clearFocus(focusedChild);

			hcWindowStack.push(currentWindow);
		} else {
			// HCDesktop
			final ViewGroup vg = getHCDesktopVG();
			if (vg != null) {
				clearFocus(focusViewOfVG.get(vg));
			}
		}

		currentWindow = win;

		if (isOpened) {
			setTopShow(win);
			activeWindow(win, true);
		}

		return isOpened;
	}

	private static void clearFocus(final View focusedChild) {
		if (focusedChild != null) {
			AndroidUIUtil.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					focusedChild.clearFocus();
				}
			});
		}
	}

	private static void activeWindow(final Window win, final boolean switchFocusOn) {
		sendWindowEvent(win, WindowEvent.WINDOW_ACTIVATED);
		sendWindowEvent(win, WindowEvent.WINDOW_GAINED_FOCUS);
		sendWindowEvent(win, WindowEvent.WINDOW_STATE_CHANGED);

		if (switchFocusOn) {
			switchFocusView((ViewGroup) win.getWindowViewAdAPI());
		}
	}

	public static void showWindow(final Window win) {
		AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				if (win == currentWindow) {
					return;
				}

				// if(isNeedShowBackGray(win)){
				// dialogBackView.setVisibility(View.VISIBLE);
				// show(dialogBackView);
				// }else{
				// if(dialogBackView.getVisibility() == View.VISIBLE){
				// dialogBackView.setVisibility(View.INVISIBLE);
				// }
				// }

				// View.bringToFront
				if (toFront(win)) {
					return;
				}

				final View peerView = win.getPeerAdAPI();

				win.setWindowViewAdAPI(peerView);

				int winWidth = win.getWindowFixWidthAdAPI();
				int winHeight = win.getWindowFixHeightAdAPI();

				Dimension rect = new Dimension();
				AndroidUIUtil.getViewWidthAndHeight(peerView, rect);

				// 如果指定尺寸小于适合尺寸，则改用适用尺寸
				if (rect.width > winWidth) {
					winWidth = rect.width;
				}
				if (rect.height > winHeight) {
					winHeight = rect.height;
				}

				if (win.isPackedAdAPI()) {
					winWidth = rect.width;
					winHeight = rect.height;
				}

				L.V = L.WShop ? false
						: LogManager.log("Window width : " + winWidth + ", height : " + winHeight
								+ ", screen width : " + J2SEInitor.screenWidth
								+ ", screen height : " + J2SEInitor.screenHeight);

				final boolean isSmallWidthThanScreen = winWidth < J2SEInitor.screenWidth;
				final boolean isSmallHeightThanScreen = winHeight < J2SEInitor.screenHeight;
				if (winWidth == Window.ANDROID_FULL_SCREEN_AD_API
						|| (isSmallWidthThanScreen == false && isSmallHeightThanScreen == false)) {
					L.V = L.WShop ? false : LogManager.log("show window without back trans layer");

					// 超过最大尺寸
					final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							(winWidth >= J2SEInitor.screenWidth)
									? FrameLayout.LayoutParams.MATCH_PARENT
									: FrameLayout.LayoutParams.WRAP_CONTENT,
							(winHeight >= J2SEInitor.screenHeight)
									? FrameLayout.LayoutParams.MATCH_PARENT
									: FrameLayout.LayoutParams.WRAP_CONTENT);
					lp.gravity = (Gravity.CENTER);
					peerView.setLayoutParams(lp);
					buildKeyListener(win, peerView);
				} else {
					L.V = L.WShop ? false : LogManager.log("show window with back trans layer");

					FrameLayout frameLayout = new FrameLayout(ActivityManager.applicationContext);
					frameLayout
							.setBackgroundColor(AndroidUIUtil.WINDOW_TRANS_LAYER_COLOR.toAndroid());
					buildKeyListener(win, frameLayout);
					frameLayout.setClickable(true);
					{
						frameLayout.setLayoutParams(
								new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
										ViewGroup.LayoutParams.MATCH_PARENT));
					}

					win.setWindowViewAdAPI(frameLayout);

					FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
							isSmallWidthThanScreen ? FrameLayout.LayoutParams.WRAP_CONTENT
									: FrameLayout.LayoutParams.MATCH_PARENT,
							isSmallHeightThanScreen ? FrameLayout.LayoutParams.WRAP_CONTENT
									: FrameLayout.LayoutParams.MATCH_PARENT);
					lp.gravity = (Gravity.CENTER);
					LinearLayout linear = new LinearLayout(ActivityManager.applicationContext);
					linear.setGravity(Gravity.CENTER);
					linear.setOrientation(LinearLayout.VERTICAL);
					linear.setBackgroundColor(android.graphics.Color.TRANSPARENT);
					{
						LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
								isSmallWidthThanScreen ? winWidth : J2SEInitor.screenWidth,
								isSmallHeightThanScreen ? winHeight : J2SEInitor.screenHeight);
						// LinearLayout.LayoutParams.WRAP_CONTENT,
						// LinearLayout.LayoutParams.WRAP_CONTENT);
						linear.addView(peerView, llp);
					}
					frameLayout.addView(linear, lp);
				}
				setTopShow(win);

				activeWindow(win, false);

				sendWindowEvent(win, WindowEvent.WINDOW_OPENED);// requireFocus事件，不需要运行runLater

				if (PropertiesManager.isSimu()) {
					ContextManager.getThreadPool().run(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(1000);
							} catch (Exception e) {
							}
							AndroidUIUtil.printComponentsInfo(
									win.getRootPaneAdAPI().getContentPane(), 1);
							AndroidUIUtil.printViewStructure(peerView, 1);
						}
					});
				}
			}
		});

		// 如果是JDialog，线程等待
		if (win instanceof Dialog) {
			if (((Dialog) win).getModalityType() != ModalityType.MODELESS) {
				Integer lock = Integer.valueOf(random.nextInt());
				dialogLock.put((Dialog) win, lock);
				synchronized (lock) {
					try {
						lock.wait();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	final static Random random = new Random(System.currentTimeMillis());

	private static void setTopShow(Window win) {
		win.getWindowViewAdAPI().setClickable(true);
		show(win.getWindowViewAdAPI(), null);
	}

	private static boolean isNeedShowBackGray(Window window) {
		return window instanceof Dialog;
	}

	public static void show(final View view, final View focusView) {
		// if(needPushIn){
		// hcWindowStack.push(view);
		// }
		if (focusView != null) {
			saveFocusView((ViewGroup) view, focusView);
		}
		UIThreadViewChanger.setCurr(view);
	}

	private static void saveFocusView(final ViewGroup group, final View focusView) {
		focusViewOfVG.put(group, focusView);
	}

	private static void buildKeyListener(final Window win, View viewLayout) {
		viewLayout.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
					return win.getRootPaneAdAPI().matchKeyStrokeAdAPI(keyCode);
				}
				return false;
			}
		});
	}

	private static void switchFocusView(final ViewGroup vg) {
		final View focusView = focusViewOfVG.get(vg);
		if (focusView != null) {
			AndroidUIUtil.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					focusView.requestFocus();
				}
			});
		}
	}

	private static ViewGroup getHCDesktopVG() {
		if (focusViewOfVG.size() == 1) {
			Iterator<ViewGroup> evg = focusViewOfVG.keySet().iterator();
			while (evg.hasNext()) {
				return evg.next();
			}
		}
		return null;
	}
}
