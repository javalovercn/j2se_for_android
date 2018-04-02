package hc.android;

import hc.App;
import hc.core.L;
import hc.core.util.LogManager;
import hc.util.ResourceUtil;

import java.awt.Component;
import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.accessibility.AccessibleContext;

import android.app.Activity;
import dalvik.system.DexClassLoader;

public class AndroidClassUtil {
	public static final String UN_IMPLEMENT_METHOD = "this is an unimplemented method for Android";

	public static final void callEmptyMethod() {
		if (AndroidServerOption.displayEmptyMethodMessage) {
			hc.util.ClassUtil.printCurrentThreadStack(
					"--------Note : call an empty/unimplemented Swing/J2SE method---------",
					"callEmptyMethod");
		}
	}

	public static final void notifyReplaceWithSwingComponent() {
		hc.util.ClassUtil.printCurrentThreadStack(
				"--------Note : This class is not provided, please use Swing Component!---------");
	}

	public static Class loadClass(String className, String baseDir, String libFileName,
			Activity activity) {
		// final String baseDir =
		// Environment.getExternalStorageDirectory().toString();
		final File dexLibPath = new File(ResourceUtil.getBaseDir(),
				baseDir + File.separator + libFileName);// "test.jar"

		// 这个可以加载jar/apk/dex，也可以从SD卡中加载，也是本文的重点。
		DexClassLoader cl = new DexClassLoader(dexLibPath.getAbsolutePath(), baseDir, null,
				activity.getClassLoader());
		Class clazz = null;
		try {
			clazz = cl.loadClass(className);
			return clazz;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	// public static void loadJar(URL urls){
	// //dx --dex --core-library --output=C:\classes.dex C:\MyJar.jar
	//
	// //只能加载已经安装到Android系统中的apk文件。
	// dalvik.system.PathClassLoader myClassLoader = new
	// dalvik.system.PathClassLoader(
	// urls.toString(), AndroidClassUtil.class.getClassLoader());
	// }

	public static AccessibleContext buildAccessibleContext(Object compoent) {
		return null;
	}

	public static Enumeration getEmptyEnumeration() {
		return new Enumeration() {
			public boolean hasMoreElements() {
				return false;
			}

			public Object nextElement() {
				throw new NoSuchElementException();
			}
		};
	}

	// /**
	// *
	// * @param srcFileName
	// * @param toFileName Output name must end with one of: .dex .jar .zip .apk
	// * @return 0 success
	// */
	// public static int dx(String srcFileName, String toFileName) throws
	// Exception{
	// //dx --dex --output=dexed.jar hello.jar
	// String[] args = {
	// "--output=" + toFileName, srcFileName
	// };
	// return run(args);
	// }

	// private static int run(String[] argArray) throws Exception{
	// Arguments arguments = new Arguments();
	// arguments.parse(argArray);
	//
	// return com.android.dx.command.dexer.Main.run(arguments);
	// }
}
