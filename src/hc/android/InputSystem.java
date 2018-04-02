package hc.android;

import hc.core.util.ReturnableRunnable;
import java.io.InputStream;

public class InputSystem {
	private final static String CMD_INPUT = "input";
	private final static ProcessBuilder clickProcessBuilder = new ProcessBuilder();
	private final static byte[] buffers = new byte[1024];

	public static boolean mouseClick(int x, int y) {
		final String inputPara = "tap " + x + " " + y;
		return runProcess(CMD_INPUT, inputPara, true);
	}

	private static boolean runProcess(final String cmd, final String inputPara,
			final boolean isErrorWithPrint) {
		return (Boolean) AndroidUIUtil.runAndWaitNotInUIThread(new ReturnableRunnable() {
			@Override
			public Object run() throws Throwable {
				boolean out = Boolean.TRUE;
				synchronized (clickProcessBuilder) {
					Process process = null;
					try {
						process = clickProcessBuilder.command(cmd, inputPara)
								.redirectErrorStream(true).start();
						InputStream is = process.getInputStream();
						int len = 0;
						while ((len = is.read(buffers)) != -1) {
							if (isErrorWithPrint) {
								out = Boolean.FALSE;
							}
							System.out.println(new String(buffers, 0, len));
						}
						return out;
					} catch (Exception e) {
						out = Boolean.FALSE;
						e.printStackTrace();
						return out;
					} finally {
						if (process != null) {
							try {
								process.destroy();
							} catch (Exception e) {
							}
						}
					}
				}
			}
		});
	}

	public static boolean keyPressed(final int keycode, final boolean isPressNoRelease) {
		// byKeyEvent(keycode);

		// sendevent /dev/input/event0 1 229 1
		// [type] 1 is unknow for me ( maybe code for physical button on device
		// )
		// [code] 229 is the MENU button of the emulator
		// [value] 1 is keydown or press down ( for keyup or up use 0 )
		return runProcess("sendevent",
				"/dev/input/event0 1 " + keycode + " " + (isPressNoRelease ? 1 : 0), true);
	}

	private static boolean byKeyEvent(final int keycode) {
		return runProcess(CMD_INPUT, "keyevent " + keycode, true);
	}

	public static boolean mouseDragAndDrop(final int x1, final int y1, final int x2, final int y2) {
		return runProcess(CMD_INPUT, "swipe " + x1 + " " + y1 + " " + x2 + " " + y2, true);
	}
}
