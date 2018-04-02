package hc.android.loader;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;

public class StringUtil {
	public static String toString(AttributedCharacterIterator x) {
		StringBuffer sb = new StringBuffer(x.getEndIndex() - x.getIndex());

		sb.append(x.current());
		while (x.getIndex() < x.getEndIndex()) {
			sb.append(x.next());
		}
		sb.setLength(sb.length() - 1);

		return sb.toString();
	}

	public static String getMD5(final File file) {
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			BigInteger bigInt = new BigInteger(1, digest.digest());
			return bigInt.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Throwable e) {
			}
		}
		return "";
	}

	public static String toString(CharacterIterator ci, int beginIdx, int limit) {
		StringBuffer sb = new StringBuffer(limit);
		for (char c = ci.setIndex(beginIdx); c != CharacterIterator.DONE; c = ci.next()) {
			sb.append(c);
		}
		return sb.toString();
	}
}
