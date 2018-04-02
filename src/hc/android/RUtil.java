package hc.android;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import hc.core.util.LogManager;

public class RUtil {
	public static final String INNER_R = "com.android.internal.R";

	public static Object getRResource(String RClassName, String innerLeverClass, String fieldName) {
		try {
			Class rClass = Class.forName(RClassName);
			Class[] declaredClasses = rClass.getDeclaredClasses();
			for (int i = 0; i < declaredClasses.length; i++) {
				if (declaredClasses[i].getSimpleName().equals(innerLeverClass)) {
					Field[] fields = declaredClasses[i].getDeclaredFields();
					for (int j = 0; j < fields.length; j++) {
						Field field = fields[j];
						if (field.getName().equals(fieldName)) {
							boolean isStatic = Modifier.isStatic(field.getModifiers());
							if (isStatic) {
								Object out = field.get(null);
								return out;
							}
						}
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		LogManager.err("fail to get " + RClassName + "." + innerLeverClass + "." + fieldName);
		return null;
	}
}
