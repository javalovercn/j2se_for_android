package hc.android;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import android.view.KeyEvent;

public class KeyEventThreadBinder {
	private static final ConcurrentMap<Long, KeyEventThreadBinder> map = new ConcurrentHashMap<Long, KeyEventThreadBinder>();

	private final static int MASK_MASK = 0x7F000000;// 空最顶的0
	public final static int CTRL_MASK = MASK_MASK | KeyEvent.META_CTRL_LEFT_ON;
	public final static int SHIFT_MASK = MASK_MASK | KeyEvent.META_SHIFT_LEFT_ON;
	public final static int ALT_MASK = MASK_MASK | KeyEvent.META_ALT_LEFT_ON;// 增加掩码，因为与KeyEvent.KEYCODE_9相同

	public final static String CONTROL = "CONTROL";
	public final static String SHIFT = "SHIFT";
	public final static String ALT = "ALT";
	int mask = 0;
	boolean isForceAndroidKeycode = false;

	public static int getMetaAndReset() {
		int oldMata = 0;
		KeyEventThreadBinder binder = getBinder(false);
		if (binder != null) {
			if (binder.mask != 0) {
				oldMata = (binder.mask ^ MASK_MASK);// 消掉掩码
				binder.mask = 0;
			}
			binder.isForceAndroidKeycode = false;
		}
		return oldMata;
	}

	public static void bindForceAndroidKeyCode(boolean isAndroidKeycode) {
		KeyEventThreadBinder binder = getBinder(true);
		binder.isForceAndroidKeycode = isAndroidKeycode;
	}

	public static boolean isForceAndroidKeyCode() {
		KeyEventThreadBinder binder = getBinder(false);
		if (binder != null) {
			return binder.isForceAndroidKeycode;
		} else {
			return false;
		}
	}

	public static void bindMeta(int metaMask) {
		KeyEventThreadBinder binder = getBinder(true);
		binder.mask |= metaMask;
	}

	private static KeyEventThreadBinder getBinder(boolean ifNullThenPut) {
		final long threadID = Thread.currentThread().getId();
		KeyEventThreadBinder binder = map.get(threadID);
		if (binder == null && ifNullThenPut) {
			binder = new KeyEventThreadBinder();
			map.put(threadID, binder);
		}
		return binder;
	}
}
