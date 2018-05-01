package hc.android;

import hc.App;
import hc.PlatformTrayIcon;
import hc.android.loader.AndroidDX;
import hc.core.ContextManager;
import hc.core.IConstant;
import hc.core.util.CCoreUtil;
import hc.core.util.LogManager;
import hc.core.util.StringUtil;
import hc.core.util.WiFiDeviceManager;
import hc.server.PlatformService;
import hc.server.data.screen.KeyComper;
import hc.server.ui.design.AddHarHTMLMlet;
import hc.server.ui.design.J2SESession;
import hc.util.ClassUtil;
import hc.util.LogServerSide;
import hc.util.PropertiesManager;
import hc.util.ResourceUtil;

import java.awt.Image;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.StatFs;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import dalvik.system.DexClassLoader;

public class AndroidPlatformService implements PlatformService {

	final File optimizBaseDir;
	final File dxCache;

	public AndroidPlatformService() {
		dxCache = new File(getBaseDir(), "dxCache");
		optimizBaseDir = new File(getBaseDir(), "/dex_optimized");// 注意与HCAndroidStarter同步
		final boolean isExists = optimizBaseDir.exists();
		optimizBaseDir.mkdirs();
		if (isExists == false) {
			System.out.println("create dir : " + optimizBaseDir.getAbsolutePath());
		}
	}

	public File getJRubyAndroidOptimizBaseDir() {
		return optimizBaseDir;
	}

	@Override
	public void setJRubyHome(final String version, final String absPath) {
		// 1.7.19
		String jrubyHome = "file:" + absPath + "!/META-INF/jruby.home";
		System.setProperty("jruby.home", jrubyHome);
		LogManager.log("successful set system property [jruby.home] : " + jrubyHome);
	}

	public void setAutoStart(final boolean isAutoStart) {
		final Class[] paraTypes = { boolean.class };
		final Object[] para = { isAutoStart };
		ClassUtil.invoke(ActivityManager.activity.getClass(), ActivityManager.activity, "setAutoStart", paraTypes, para, false);
		PropertiesManager.setValue(PropertiesManager.p_autoStart,
				isAutoStart ? IConstant.TRUE : IConstant.FALSE);
		PropertiesManager.saveFile();

		// if(isAutoStart){
		// PackageManager mPackageManager =
		// ActivityManager.applicationContext.getPackageManager();
		// Intent intent = new Intent();
		// intent.setAction(Intent.ACTION_BOOT_COMPLETED );
		// List<ResolveInfo> resolveInfoList =
		// mPackageManager.queryBroadcastReceivers(intent,
		// PackageManager.GET_DISABLED_COMPONENTS);
		//
		// final String packageName = activity.getPackageName();
		// final int size = resolveInfoList.size();
		// for (int i = 0; i < size; i++) {
		// ResolveInfo ri = resolveInfoList.get(i);
		// Log.e("HomeCenter", ri.toString());
		// if(ri.activityInfo.applicationInfo.packageName.equals(packageName)){
		// AndroidUIUtil.showCenterToast((String)ResourceUtil.get(5002));
		// return;
		// }
		// }
		// }
	}

	/**
	 * 计算剩余空间
	 * 
	 * @param path
	 * @return
	 */
	private static long getAvailableSize(String path) {
		StatFs fileStats = new StatFs(path);
		fileStats.restat(path);
		try {
			// return (long) fileStats.getAvailableBlocksLong() *
			// fileStats.getBlockSizeLong();//API level 18
			final long getAvailableBlocksLong = (Long) ClassUtil.invokeWithExceptionOut(
					StatFs.class, fileStats, "getAvailableBlocksLong", ClassUtil.NULL_PARA_TYPES,
					ClassUtil.NULL_PARAS, false);
			final long getBlockSizeLong = (Long) ClassUtil.invokeWithExceptionOut(StatFs.class,
					fileStats, "getBlockSizeLong", ClassUtil.NULL_PARA_TYPES, ClassUtil.NULL_PARAS,
					false);
			return getAvailableBlocksLong * getBlockSizeLong;
		} catch (Throwable e) {
			return (long) fileStats.getAvailableBlocks() * fileStats.getBlockSize();
		}
	}

	public long getAvailableSize() {
		return getAvailableSize("/data");
	}

	public boolean isLockScreen() {
		if(ActivityManager.isNonUIServer()) {
			return true;
		}
		
		final KeyguardManager km = (KeyguardManager) ActivityManager.applicationContext
				.getSystemService(Context.KEYGUARD_SERVICE);
		final boolean isLock = km.inKeyguardRestrictedInputMode();

		if (isLock == false) {
			final String isPause = System.getProperty("user.hc.app.isPause");
			if (isPause != null && isPause.equals("true")) {
				// 切换到当前的应用中
				AndroidUIUtil.runDelayNotInUIThread(new Runnable() {
					@Override
					public void run() {
						J2SEInitor.doAction(J2SEActionMap.startBringToFrontService);

						try {
							Thread.sleep(2000);
						} catch (final Exception e) {
						}

						J2SEInitor.doAction(J2SEActionMap.stopBringToFrontService);
					}
				});
			}
		}

		return isLock;
	}

	@Override
	public String[] listAssets(final Object path) {
		try {
			return ActivityManager.applicationContext.getAssets().list((String) path);
		} catch (IOException e) {
			LogManager.errToLog("no resource assets for path : " + path);
			return new String[0];
		}
	}

	public static int convertToAndroidKeyCodeAdAPI(final int keycodeJ2SE) {
		if (java.awt.event.KeyEvent.VK_0 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_0;
		} else if (java.awt.event.KeyEvent.VK_1 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_1;
		} else if (java.awt.event.KeyEvent.VK_2 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_2;
		} else if (java.awt.event.KeyEvent.VK_3 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_3;
		} else if (java.awt.event.KeyEvent.VK_4 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_4;
		} else if (java.awt.event.KeyEvent.VK_5 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_5;
		} else if (java.awt.event.KeyEvent.VK_6 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_6;
		} else if (java.awt.event.KeyEvent.VK_7 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_7;
		} else if (java.awt.event.KeyEvent.VK_8 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_8;
		} else if (java.awt.event.KeyEvent.VK_9 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_9;

		} else if (java.awt.event.KeyEvent.VK_A == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_A;
		} else if (java.awt.event.KeyEvent.VK_B == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_B;
		} else if (java.awt.event.KeyEvent.VK_C == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_C;
		} else if (java.awt.event.KeyEvent.VK_D == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_D;
		} else if (java.awt.event.KeyEvent.VK_E == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_E;
		} else if (java.awt.event.KeyEvent.VK_F == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_F;
		} else if (java.awt.event.KeyEvent.VK_G == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_G;
		} else if (java.awt.event.KeyEvent.VK_H == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_H;
		} else if (java.awt.event.KeyEvent.VK_I == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_I;
		} else if (java.awt.event.KeyEvent.VK_J == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_J;
		} else if (java.awt.event.KeyEvent.VK_K == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_K;
		} else if (java.awt.event.KeyEvent.VK_L == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_L;
		} else if (java.awt.event.KeyEvent.VK_M == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_M;
		} else if (java.awt.event.KeyEvent.VK_N == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_N;
		} else if (java.awt.event.KeyEvent.VK_O == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_O;
		} else if (java.awt.event.KeyEvent.VK_P == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_P;
		} else if (java.awt.event.KeyEvent.VK_Q == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_Q;
		} else if (java.awt.event.KeyEvent.VK_R == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_R;
		} else if (java.awt.event.KeyEvent.VK_S == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_S;
		} else if (java.awt.event.KeyEvent.VK_T == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_T;
		} else if (java.awt.event.KeyEvent.VK_U == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_U;
		} else if (java.awt.event.KeyEvent.VK_V == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_V;
		} else if (java.awt.event.KeyEvent.VK_W == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_W;
		} else if (java.awt.event.KeyEvent.VK_X == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_X;
		} else if (java.awt.event.KeyEvent.VK_Y == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_Y;
		} else if (java.awt.event.KeyEvent.VK_Z == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_Z;

		} else if (java.awt.event.KeyEvent.VK_F1 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_F1;
		} else if (java.awt.event.KeyEvent.VK_F2 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_F2;
		} else if (java.awt.event.KeyEvent.VK_F3 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_F3;
		} else if (java.awt.event.KeyEvent.VK_F4 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_F4;
		} else if (java.awt.event.KeyEvent.VK_F5 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_F5;
		} else if (java.awt.event.KeyEvent.VK_F6 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_F6;
		} else if (java.awt.event.KeyEvent.VK_F7 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_F7;
		} else if (java.awt.event.KeyEvent.VK_F8 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_F8;
		} else if (java.awt.event.KeyEvent.VK_F9 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_F9;
		} else if (java.awt.event.KeyEvent.VK_F10 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_F10;
		} else if (java.awt.event.KeyEvent.VK_F11 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_F11;
		} else if (java.awt.event.KeyEvent.VK_F12 == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_F12;

		} else if (java.awt.event.KeyEvent.VK_WINDOWS == keycodeJ2SE) {
			printConvertInfo("VK_WINDOWS", "KEYCODE_HOME");
			return android.view.KeyEvent.KEYCODE_HOME;
		} else if (java.awt.event.KeyEvent.VK_CONTEXT_MENU == keycodeJ2SE) {
			printConvertInfo("VK_CONTEXT_MENU", "KEYCODE_MENU");
			return android.view.KeyEvent.KEYCODE_MENU;
		} else if (java.awt.event.KeyEvent.VK_SPACE == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_SPACE;
		} else if (java.awt.event.KeyEvent.VK_CAPS_LOCK == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_CAPS_LOCK;
		} else if (java.awt.event.KeyEvent.VK_TAB == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_TAB;
		} else if (java.awt.event.KeyEvent.VK_ESCAPE == keycodeJ2SE) {
			printConvertInfo("VK_ESCAPE", "KEYCODE_BACK");
			return android.view.KeyEvent.KEYCODE_BACK;// Not KEYCODE_ESCAPE
		} else if (java.awt.event.KeyEvent.VK_PRINTSCREEN == keycodeJ2SE) {
			printConvertInfo("VK_PRINTSCREEN", "KEYCODE_SYSRQ");
			return android.view.KeyEvent.KEYCODE_SYSRQ;
		} else if (java.awt.event.KeyEvent.VK_NUM_LOCK == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_NUM_LOCK;
		} else if (java.awt.event.KeyEvent.VK_PAUSE == keycodeJ2SE) {
			printConvertInfo("VK_PAUSE", "KEYCODE_MEDIA_PLAY_PAUSE");
			return android.view.KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;

		} else if (java.awt.event.KeyEvent.VK_INSERT == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_INSERT;
		} else if (java.awt.event.KeyEvent.VK_HOME == keycodeJ2SE) {
			printConvertInfo("VK_HOME", "KEYCODE_MOVE_HOME");
			return android.view.KeyEvent.KEYCODE_MOVE_HOME;
		} else if (java.awt.event.KeyEvent.VK_PAGE_UP == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_PAGE_UP;
		} else if (java.awt.event.KeyEvent.VK_DELETE == keycodeJ2SE) {
			printConvertInfo("VK_DELETE", "KEYCODE_DEL");
			return android.view.KeyEvent.KEYCODE_DEL;
		} else if (java.awt.event.KeyEvent.VK_END == keycodeJ2SE) {
			printConvertInfo("VK_END", "KEYCODE_MOVE_END");
			return android.view.KeyEvent.KEYCODE_MOVE_END;
		} else if (java.awt.event.KeyEvent.VK_PAGE_DOWN == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_PAGE_DOWN;
		} else if (java.awt.event.KeyEvent.VK_BACK_SPACE == keycodeJ2SE) {
			printConvertInfo("VK_BACK_SPACE", "KEYCODE_DEL");
			return android.view.KeyEvent.KEYCODE_DEL;// java.awt.event.KeyEvent.VK_DELETE
		} else if (java.awt.event.KeyEvent.VK_ENTER == keycodeJ2SE) {
			printConvertInfo("VK_ENTER", "KEYCODE_DPAD_CENTER");
			return android.view.KeyEvent.KEYCODE_DPAD_CENTER;

		} else if (java.awt.event.KeyEvent.VK_LEFT == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_DPAD_LEFT;
		} else if (java.awt.event.KeyEvent.VK_UP == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_DPAD_UP;
		} else if (java.awt.event.KeyEvent.VK_RIGHT == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
		} else if (java.awt.event.KeyEvent.VK_DOWN == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_DPAD_DOWN;

		} else if (java.awt.event.KeyEvent.VK_MINUS == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_MINUS;
		} else if (java.awt.event.KeyEvent.VK_EQUALS == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_EQUALS;
		} else if (java.awt.event.KeyEvent.VK_OPEN_BRACKET == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_LEFT_BRACKET;
		} else if (java.awt.event.KeyEvent.VK_CLOSE_BRACKET == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_RIGHT_BRACKET;
		} else if (java.awt.event.KeyEvent.VK_BACK_SLASH == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_BACKSLASH;
		} else if (java.awt.event.KeyEvent.VK_SEMICOLON == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_SEMICOLON;
		} else if (java.awt.event.KeyEvent.VK_COMMA == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_COMMA;
		} else if (java.awt.event.KeyEvent.VK_PERIOD == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_PERIOD;
		} else if (java.awt.event.KeyEvent.VK_SLASH == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_SLASH;

		} else if (java.awt.event.KeyEvent.VK_BACK_QUOTE == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_GRAVE;
		} else if (java.awt.event.KeyEvent.VK_QUOTE == keycodeJ2SE) {
			return android.view.KeyEvent.KEYCODE_APOSTROPHE;

		} else if (java.awt.event.KeyEvent.VK_SHIFT == keycodeJ2SE) {
			return KeyEventThreadBinder.SHIFT_MASK;
		} else if (java.awt.event.KeyEvent.VK_CONTROL == keycodeJ2SE) {
			return KeyEventThreadBinder.CTRL_MASK;
		} else if (java.awt.event.KeyEvent.VK_ALT == keycodeJ2SE) {
			return KeyEventThreadBinder.ALT_MASK;
		}

		return -1;
	}

	private static void printConvertInfo(final String from, final String to) {
		LogManager.log("convert J2SE key : " + from + " to Android key : " + to + ".");
	}

	@Override
	public Object doExtBiz(final int bizID, final Object para) {
		if (bizID == BIZ_GET_ANDROID_KEYCODE) {
			return getAndroidKeyCode((String) para);
		} else if (bizID == BIZ_CONVERT_J2SE_KE_TO_ANDROID_KEY) {
			return convertToAndroidKeyCodeAdAPI((Integer) para);
		} else if (bizID == BIZ_CTRL_V) {
			try {
				final String str = ResourceUtil.getTxtFromClipboard();
				if (str != null && str.length() > 0) {
					AndroidUIUtil.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							final View currFocusView = ActivityManager.getCurrentFocusView();
							if (currFocusView != null) {
								pasteStr(currFocusView, str);
							}
						}
					});
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
			return null;
		} else if (bizID == BIZ_DEL_HAR_OPTIMIZE_DIR) {
			if (para instanceof String) {
				DexOptimizeSet.removeItem((String) para);
			}
			return null;
		} else if (bizID == BIZ_BIND_FORCE_ANDROID_KEYCODE) {
			KeyEventThreadBinder.bindForceAndroidKeyCode((Boolean) para);
		} else if (bizID == BIZ_INIT_RUBOTO_ENVIROMENT) {
			// put(para, "$package_name",
			// ActivityManager.applicationContext.getPackageName());
			// runScriptlet(para, "::RUBOTO_JAVA_PROXIES = {}");
			// put(para, "$application_context",
			// ActivityManager.applicationContext.getApplicationContext());
			// runScriptlet(para, "begin\n require 'environment'\nrescue
			// LoadError => e\n puts e\nend");
			// // runScriptlet("begin\n require 'environment'\nrescue LoadError
			// => e\n java.lang.System.out.println(e)\nend");
		} else if (bizID == BIZ_GO_HOME) {
			if(ActivityManager.isNonUIServer()) {
				return null;
			}
			
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			ActivityManager.applicationContext.startActivity(intent);
		} else if (bizID == BIZ_BCL) {
			return "bcl_android.txt";
		} else if(bizID == BIZ_DISABLE_TEXTFIELD_HINT) {
			if(para instanceof JTextField) {
				((JTextField)para).setDisableFieldHintAD(true);
			}
		} else if(bizID == BIZ_PRINT_MAIN_TRACE_STACK) {
			final Thread main = ActivityManager.mainThread;
			ClassUtil.printTraceStack(main, main.isDaemon(), main.getStackTrace());
		} else if(bizID == BIZ_ENABLE_ANDROID_LOGCAT) {
			if(androidLog == null) {
				getLog();
			}
			androidLog.enableAndroidLogCat(IConstant.TRUE.equals(para));
		}
		return null;
	}
	
	AndroidLogServerSide androidLog;

	private static Object runScriptlet(final Object ruby, final String code) {
		try {
			final Method runScriptletMethod = ruby.getClass().getMethod("runScriptlet",
					String.class);
			return runScriptletMethod.invoke(ruby, code);
		} catch (final NoSuchMethodException nsme) {
			throw new RuntimeException(nsme);
		} catch (final IllegalAccessException iae) {
			throw new RuntimeException(iae);
		} catch (final Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	// private static void put(final Object ruby, final String name, final
	// Object object) {
	// try {
	// final Method putMethod = ruby.getClass().getMethod("put", String.class,
	// Object.class);
	// putMethod.invoke(ruby, name, object);
	// } catch (final NoSuchMethodException nsme) {
	// throw new RuntimeException(nsme);
	// } catch (final IllegalAccessException iae) {
	// throw new RuntimeException(iae);
	// } catch (final java.lang.reflect.InvocationTargetException ite) {
	// throw new RuntimeException(ite);
	// }
	// }

	private boolean pasteStr(final View currFocusView, final String str) {
		if (currFocusView instanceof EditText) {
			((EditText) currFocusView).setText(str);
			return true;
		} else if (currFocusView instanceof ViewGroup) {
			final ViewGroup group = (ViewGroup) currFocusView;
			final int size = group.getChildCount();
			for (int i = 0; i < size; i++) {
				if (pasteStr(group.getChildAt(i), str)) {
					return true;
				}
			}
		}
		return false;
	}

	private Integer getAndroidKeyCode(final String upperCase) {
		if (upperCase.equals("KEYCODE_ALT_LEFT") || upperCase.equals("KEYCODE_ALT_RIGHT")) {
			return KeyEventThreadBinder.ALT_MASK;
		} else if (upperCase.equals("KEYCODE_CTRL_LEFT")
				|| upperCase.equals("KEYCODE_CTRL_RIGHT")) {
			return KeyEventThreadBinder.CTRL_MASK;
		}
		if (upperCase.equals("KEYCODE_SHIFT_LEFT") || upperCase.equals("KEYCODE_SHIFT_RIGHT")) {
			return KeyEventThreadBinder.SHIFT_MASK;
		}

		Field f;
		try {
			f = android.view.KeyEvent.class
					.getField(upperCase.startsWith(KeyComper.ANDROID_KEYEVENT_PREFIX) ? upperCase
							: (KeyComper.ANDROID_KEYEVENT_PREFIX + upperCase));
			f.setAccessible(true);
			return (Integer) f.get(null);
		} catch (final Exception ex) {
		}
		return -1;
	}

	@Override
	public void setWindowOpaque(final Window win, final boolean bool) throws Exception {
	}

	HCDesktop hcDesktop;

	public static boolean isWiFi() {
		final ConnectivityManager manager = (ConnectivityManager) ActivityManager.applicationContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			final String type = networkInfo.getTypeName();
			if (type.equalsIgnoreCase("WIFI")) {
				return true;
			} else if (type.equalsIgnoreCase("MOBILE")) {
			}
		}
		return false;
	}

	private void doExtTest() {
		try {
			// new TestJPanelPaint(Color.BLUE).doTest();
			// new TestXYWH().doTest();

			// new TestJRuby().doTest();

			// new TestSplitPane().doTest();
			// JPanel panel = new JPanel(new GridLayout(2, 1));
			// panel.add(new JButton("Hello"));
			//
			// {
			// JPanel panelSub = new JPanel(new GridLayout(2, 1));
			// panelSub.add(new JButton("World1"));
			// panelSub.add(new JButton("World2"));
			//
			// panel.add(panelSub);
			// }
			//
			// new TestJTree().doTest();

			// Map<String, Object> map = HCjar.loadHar(new
			// File("/storage/sdcard/JRubyIOT.har"), false);
			// System.out.println((String)map.get(HCjar.PROJ_VER));
			// File file = FileSelector.selectImageFile(null,
			// FileSelector.HAR_FILTER, true);
			// System.out.println("selected file : " + file.getAbsolutePath());

			// Designer.showLinkPanel(null, false, null);

			// SpannableStringBuilder ssb = new SpannableStringBuilder();
			// EditText editText = new EditText(ActivityManager.applicationContext);
			// editText.setText(ssb, BufferType.SPANNABLE);
			//
			// ssb.append("Hello world");
			// ssb.setSpan(new ForegroundColorSpan(Color.GREEN), 6, 11,
			// Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			// final Spanned fromHtml = Html.fromHtml("<html>Hello
			// <STRONG>world</STRONG></html>");
			// System.out.println(fromHtml);
			// editText.setText(fromHtml);
			// System.out.println(editText.getText().toString());
			// LinearLayout linear = new
			// LinearLayout(ActivityManager.applicationContext);
			// linear.addView(editText);
			// WindowManager.show(linear, false);

			// final long downTime = SystemClock.uptimeMillis();
			// final long eventTime = SystemClock.uptimeMillis();
			//
			// final KeyEvent altDown = new KeyEvent(downTime, eventTime,
			// KeyEvent.ACTION_DOWN,
			// KeyEvent.KEYCODE_A, 1, KeyEvent.META_SHIFT_LEFT_ON);
			// final KeyEvent altUp = new KeyEvent(downTime, eventTime,
			// KeyEvent.ACTION_UP,
			// KeyEvent.KEYCODE_A, 1, KeyEvent.META_SHIFT_LEFT_ON);
			//
			// final Instrumentation instrumentation = new Instrumentation();
			// instrumentation.sendKeySync(altDown);
			//// instrumentation.sendCharacterSync(KeyEvent.KEYCODE_A);
			// instrumentation.sendKeySync(altUp);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		// KeyComperPanel.showKeyComperPanel();
	}

	@Override
	public PlatformTrayIcon buildPlatformTrayIcon(final Image image, final String toolTip,
			final JPopupMenu menu) {
		if (hcDesktop == null) {
			hcDesktop = new HCDesktop(image, toolTip, menu);
			hcDesktop.showDesktop();
			
			if (PropertiesManager.isTrue(PropertiesManager.p_IsSimu)) {
				AndroidUIUtil.runDelayNotInUIThread(new Runnable() {
					@Override
					public void run() {
						doExtTest();
					}
				});
			}
		} else {
			hcDesktop.popupMenu = menu;
			hcDesktop.setImage(image);
			hcDesktop.setIconToolTip(toolTip);
		}
		return hcDesktop;
	}

	@Override
	public void addJCEProvider() {
		// Android SDK is based on Java and is compliant with the java
		// cryptography extension (JCE)
	}

	@Override
	public BufferedImage makeRoundedCorner(final BufferedImage image, final int cornerRadius) {
		final Bitmap out = ImageUtil.getRoundedCornerBitmap(image.getBitmapAdAPI(), cornerRadius);
		return new BufferedImage(out);
	}

	@Override
	public BufferedImage composeImage(final BufferedImage base, final BufferedImage cover) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Shape getImageShape(final Image img) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWindowShape(final Window win, final Shape shape) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public Object createRobotPeer(final Robot robot) throws Throwable {
		return null;
	}

	@Override
	public void buildCaptureMenu(final JPopupMenu popupTi, final ThreadGroup threadPoolToken) {

	}

	@Override
	public void startCaptureIfEnable() {
	}

	@Override
	public void stopCaptureIfEnable() {
	}

	@Override
	public void printAndroidServerInfo() {
		LogManager.log("Android API (SDK) : " + android.os.Build.VERSION.SDK_INT);
	}

	@Override
	public void exitSystem() {
		CCoreUtil.checkAccess();

		J2SEInitor.doAction(J2SEActionMap.shutdownHC);
		
		if(ActivityManager.isUIServer()) {
			ContextManager.getThreadPool().run(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(500);// 等待stop service
					} catch (Exception e) {
					}
					hc.android.ActivityManager.activity.finish();
				}
			});
		}
	}

	/**
	 * 在J2SE环境下，直接调用ExitManager.startExitSystem()；Android服务器模式下，增加后台运行选项
	 */
	@Override
	public void startExitSystem() {
		WindowManager.askMainBoardExit();
	}

	@Override
	public File getBaseDir() {
		return ActivityManager.applicationContext.getFilesDir();
	}

	/**
	 * 此接口专供Skin使用，所以Android环境下不用
	 * 
	 */
	@Override
	public void addSystemLib(final File jardexFile, final boolean isReload) {
		// Android环境下不用
		App.showMessageDialog(null, "addSystemLib is NOT valid in Android.");
	}

	public BufferedImage resizeImage(final BufferedImage bufferedimage, final int w, final int h) {
		return new BufferedImage(ImageUtil.zoomBitmap(bufferedimage.getBitmapAdAPI(), w, h));
	}

	public ClassLoader thirdClassLoader;

	@Override
	public synchronized ClassLoader get3rdAndServClassLoader(final File[] files) {
		if (files == null && thirdClassLoader != null) {
		} else {
			thirdClassLoader = loadClasses(files, SYSTEM_CLASS_LOADER, false, "hc.thirdLibs");
		}
		return thirdClassLoader;
	}

	/**
	 * 注意与HCAndroidStarter同步
	 */
	@Override
	public ClassLoader loadClasses(final File[] filePaths, final ClassLoader parent,
			final boolean isDex, final String loadOpID) {
		if (filePaths == null || filePaths.length == 0) {
			return parent;
		}

		final String[] dexFileAbsPaths = new String[filePaths.length];

		long totalLastModiMS = 0;
		if (isDex == false) {
			boolean isDxMessageOut = false;
			boolean isDxError = false;
			for (int i = 0; i < dexFileAbsPaths.length; i++) {
				dexFileAbsPaths[i] = filePaths[i].getAbsolutePath() + ResourceUtil.EXT_DEX_JAR;
				final File dexFile = new File(dexFileAbsPaths[i]);
				if (dexFile.exists() == false) {
					LogManager.log("dexing... : " + dexFile.getAbsolutePath());
					try {
						if (isDxMessageOut == false) {
							isDxMessageOut = true;
							final J2SESession coreSS = AddHarHTMLMlet
									.getCurrAddHarHTMLMletCoreSession();
							AddHarHTMLMlet.showMsgForAddHar(IConstant.INFO,
									(String) ResourceUtil.get(coreSS, 9188));
						}
						AndroidDX.dx(filePaths[i], dexFile);
						totalLastModiMS += dexFile.lastModified();
						LogManager.log("successful dex : " + dexFile.getAbsolutePath());
					} catch (final Throwable e) {
						isDxError = true;
						e.printStackTrace();
						final String dxFileName = dexFile.getName();
						final J2SESession coreSS = AddHarHTMLMlet
								.getCurrAddHarHTMLMletCoreSession();
						String msg = StringUtil.replace((String) ResourceUtil.get(coreSS, 9189),
								"{file}", dxFileName);
						msg = StringUtil.replace(msg, "{error}", ResourceUtil.getErrorI18N(coreSS));
						AddHarHTMLMlet.showMsgForAddHar(IConstant.ERROR, msg);
						App.showMessageDialog(null, "<html>Fail to dex file : " + dxFileName
								+ ", at Exception : [" + e.getMessage()
								+ "]<BR>close other applications and try again, or restart.</html>",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			if (isDxMessageOut && isDxError == false) {
				final J2SESession coreSS = AddHarHTMLMlet.getCurrAddHarHTMLMletCoreSession();
				AddHarHTMLMlet.showMsgForAddHar(IConstant.INFO,
						(String) ResourceUtil.get(coreSS, 9190));
			}
		} else {
			for (int i = 0; i < dexFileAbsPaths.length; i++) {
				totalLastModiMS += filePaths[i].lastModified();
				dexFileAbsPaths[i] = filePaths[i].getAbsolutePath();
			}
		}

		DexOptimizeItem item = DexOptimizeSet.getItem(loadOpID);
		boolean isNewItem = false;
		if (item != null) {
			if (item.lastModifySum == totalLastModiMS) {
				LogManager.log(
						dexFileAbsPaths[0] + "[...] is not modified, use old optimize directory.");
			} else {
				isNewItem = true;
				LogManager.log(dexFileAbsPaths[0]
						+ "[...] is new/modified, del old and use new optimize directory.");
				PropertiesManager.addDelDir(
						new File(optimizBaseDir, item.optimizeRandomDir).getAbsolutePath());
				PropertiesManager.saveFile();
			}
		}

		if (isNewItem || item == null) {
			if (item == null) {
				item = new DexOptimizeItem();
				item.optimizeID = loadOpID;
			}
			item.lastModifySum = totalLastModiMS;
			item.optimizeRandomDir = ResourceUtil.createRandomFileNameWithExt(optimizBaseDir, "");
			LogManager.log("create optimize dir : " + item.optimizeRandomDir + " for loadID : "
					+ loadOpID);
			DexOptimizeSet.putItem(item);
			DexOptimizeSet.save();
		}

		final File newSubOptDirFile = new File(optimizBaseDir, item.optimizeRandomDir);
		newSubOptDirFile.mkdirs();
		final String absolutePath = newSubOptDirFile.getAbsolutePath();

		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < dexFileAbsPaths.length; i++) {
			if (sb.length() > 0) {
				sb.append(":");
			}
			sb.append(dexFileAbsPaths[i]);
		}

		final String path = sb.toString();
		final ClassLoader cl = new DexClassLoader(path, absolutePath, null, parent);
		LogManager.log("successful create dex ClassLoader for : " + path);
		return cl;
	}

	WiFiDeviceManager wifiDeviceManager;

	@Override
	public WiFiDeviceManager getWiFiManager() {
		if (wifiDeviceManager != null) {
			return wifiDeviceManager;
		} else {
			wifiDeviceManager = new WiFiDeviceManager() {

				@Override
				public InputStream listenFromWiFiMulticast(final String multicastIP,
						final int port) {
					return null;
				}

				@Override
				public boolean isWiFiConnected() {
					return false;
				}

				@Override
				public boolean hasWiFiModule() {
					return false;
				}

				@Override
				public String[] getWiFiAccount() {
					return null;
				}

				@Override
				public String[] getSSIDListOnAir() {
					return null;
				}

				@Override
				public boolean canCreateWiFiAccount() {
					return false;
				}

				@Override
				public void broadcastWiFiAccountAsSSID(final String[] commands,
						final String cmdGroup) {
				}

				@Override
				public void startWiFiAP(final String ssid, final String pwd,
						final String securityOption) {
				}

				@Override
				public void clearWiFiAccountGroup(String cmdGroup) {
				}

				@Override
				public OutputStream createWiFiMulticastStream(String multicastIP, int port) {
					return null;
				}
			};
		}
		return wifiDeviceManager;
	}

	@Override
	public LogServerSide getLog() {
		if(androidLog == null) {
			androidLog = new AndroidLogServerSide();
			doExtBiz(BIZ_ENABLE_ANDROID_LOGCAT, IConstant.toString(ResourceUtil.isEnableAndroidLogCat()));
		}
		return androidLog;
	}

	android.app.ActivityManager.MemoryInfo info;

	@Override
	public long getFreeMem() {
		if (info == null) {
			info = new android.app.ActivityManager.MemoryInfo();
		}

		final android.app.ActivityManager activityManager = (android.app.ActivityManager) hc.android.ActivityManager.applicationContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(info);
		return (info.availMem >> 20);
	}

	@Override
	public void closeLoader(ClassLoader loader) {
	}

	@Override
	public String getOsNameAndVersion() {
		return "Android/" + android.os.Build.VERSION.RELEASE;
	}

	@Override
	public String[] getMethodCodeParameter(Method method) {
		return null;
	}

	@Override
	public void resetClassPool() {
	}

	private File rubotoFile;

	@Override
	public File[] getRubotoAndDxFiles() {
		synchronized (AndroidPlatformService.class) {
			if (rubotoFile == null) {
				final String rubotoFileName = "ruboto.dex.jar";
				rubotoFile = new File(getBaseDir(), rubotoFileName);

				if (rubotoFile.exists() == false
						|| ResourceUtil.isSameContent(getRubotoFileInputStream(rubotoFile),
								getRubotoInputStream(rubotoFileName)) == false) {
					ResourceUtil.saveToFile(getRubotoInputStream(rubotoFileName), rubotoFile);
					LogManager.log(
							"no ruboto or old version, save new '" + rubotoFileName + "' to disk!");
				}
			}
		}

		return new File[] { rubotoFile, new File(getBaseDir(), AndroidDX.DX_DEX) };
	}

	private final InputStream getRubotoInputStream(final String rubotoFileName) {
		return AndroidPlatformService.class.getClassLoader()
				.getResourceAsStream("hc/android/res/" + rubotoFileName);
	}

	private final InputStream getRubotoFileInputStream(final File file) {
		try {
			return new FileInputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private WifiManager.MulticastLock multicastLock;
	
	@Override
	public void enableMulticast() {
		if(multicastLock != null) {
			return;
		}
		
		WifiManager wm = (WifiManager)ActivityManager.applicationContext.getSystemService(Context.WIFI_SERVICE);
		multicastLock = wm.createMulticastLock("homecenter");
		multicastLock.setReferenceCounted(true);
		multicastLock.acquire(); 		
	}

}
