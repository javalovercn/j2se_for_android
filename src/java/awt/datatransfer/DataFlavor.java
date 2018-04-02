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
package java.awt.datatransfer;

/**
 * A {@code DataFlavor} provides meta information about data. {@code DataFlavor}
 * is typically used to access data on the clipboard, or during a drag and drop
 * operation.
 * <p>
 * An instance of {@code DataFlavor} encapsulates a content type as defined in
 * <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a> and
 * <a href="http://www.ietf.org/rfc/rfc2046.txt">RFC 2046</a>. A content type is
 * typically referred to as a MIME type.
 * <p>
 * A content type consists of a media type (referred to as the primary type), a
 * subtype, and optional parameters. See
 * <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a> for details on the
 * syntax of a MIME type.
 * <p>
 * The JRE data transfer implementation interprets the parameter
 * &quot;class&quot; of a MIME type as <B>a representation class</b>. The
 * representation class reflects the class of the object being transferred. In
 * other words, the representation class is the type of object returned by
 * {@link Transferable#getTransferData}. For example, the MIME type of
 * {@link #imageFlavor} is {@code "image/x-java-image;class=java.awt.Image"},
 * the primary type is {@code image}, the subtype is {@code x-java-image}, and
 * the representation class is {@code java.awt.Image}. When
 * {@code getTransferData} is invoked with a {@code DataFlavor} of
 * {@code imageFlavor}, an instance of {@code java.awt.Image} is returned. It's
 * important to note that {@code DataFlavor} does no error checking against the
 * representation class. It is up to consumers of {@code DataFlavor}, such as
 * {@code Transferable}, to honor the representation class. <br>
 * Note, if you do not specify a representation class when creating a
 * {@code DataFlavor}, the default representation class is used. See appropriate
 * documentation for {@code DataFlavor}'s constructors.
 * <p>
 * Also, {@code DataFlavor} instances with the &quot;text&quot; primary MIME
 * type may have a &quot;charset&quot; parameter. Refer to
 * <a href="http://www.ietf.org/rfc/rfc2046.txt">RFC 2046</a> and
 * {@link #selectBestTextFlavor} for details on &quot;text&quot; MIME types and
 * the &quot;charset&quot; parameter.
 * <p>
 * Equality of {@code DataFlavors} is determined by the primary type, subtype,
 * and representation class. Refer to {@link #equals(DataFlavor)} for details.
 * When determining equality, any optional parameters are ignored. For example,
 * the following produces two {@code DataFlavors} that are considered identical:
 * 
 * <pre>
 * DataFlavor flavor1 = new DataFlavor(Object.class,
 * 		&quot;X-test/test; class=&lt;java.lang.Object&gt;; foo=bar&quot;);
 * DataFlavor flavor2 = new DataFlavor(Object.class, &quot;X-test/test; class=&lt;java.lang.Object&gt;; x=y&quot;);
 * // The following returns true.
 * flavor1.equals(flavor2);
 * </pre>
 * 
 * As mentioned, {@code flavor1} and {@code flavor2} are considered identical.
 * As such, asking a {@code Transferable} for either {@code DataFlavor} returns
 * the same results.
 * <p>
 * For more information on the using data transfer with Swing see the
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/dnd.html"> How
 * to Use Drag and Drop and Data Transfer</a>, section in <em>Java
 * Tutorial</em>.
 *
 * @author Blake Sullivan
 * @author Laurence P. G. Cable
 * @author Jeff Dunn
 */
public class DataFlavor {

	static private DataFlavor createConstant(String mt, String prn) {
		try {
			return new DataFlavor(mt, prn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final DataFlavor stringFlavor = createConstant("java.lang.String",
			"Unicode String");

	public static final DataFlavor imageFlavor = createConstant(
			"image/x-java-image; class=java.awt.Image", "Image");

	public static final DataFlavor plainTextFlavor = createConstant(
			"text/plain; charset=unicode; class=java.io.InputStream", "Plain Text");

	public static final String javaSerializedObjectMimeType = "application/x-java-serialized-object";

	public static final DataFlavor javaFileListFlavor = createConstant(
			"application/x-java-file-list;class=java.util.List", null);

	public static final String javaJVMLocalObjectMimeType = "application/x-java-jvm-local-objectref";

	public static final String javaRemoteObjectMimeType = "application/x-java-remote-object";

	public DataFlavor() {
		this("", "");
	}

	public DataFlavor(String mimeType, String humanPresentableName) {
		super();
		if (mimeType == null) {
			throw new NullPointerException("mimeType");
		}
		this.mimeType = mimeType;
		this.humanPresentableName = humanPresentableName;
	}

	public DataFlavor(String mimeType) throws ClassNotFoundException {
		this(mimeType, "");
	}

	public String toString() {
		String string = getClass().getName();
		string += "[" + paramString() + "]";
		return string;
	}

	private String paramString() {
		String params = "";
		params += "mimetype=";
		if (mimeType == null) {
			params += "null";
		} else {
			params += mimeType;
		}
		return params;
	}

	public static final DataFlavor getTextPlainUnicodeFlavor() {
		return new DataFlavor("text/plain;charset=UTF-8;class=java.io.InputStream", "Plain Text");
	}

	public String getMimeType() {
		return (mimeType != null) ? mimeType.toString() : null;
	}

	public String getHumanPresentableName() {
		return humanPresentableName;
	}

	public void setHumanPresentableName(String humanPresentableName) {
		this.humanPresentableName = humanPresentableName;
	}

	public boolean equals(Object o) {
		return ((o instanceof DataFlavor) && equals((DataFlavor) o));
	}

	public boolean equals(DataFlavor that) {
		if (that == null) {
			return false;
		}
		if (this == that) {
			return true;
		}

		if (mimeType == null) {
			if (that.mimeType != null) {
				return false;
			}
		} else {
			if (!mimeType.equals(that.mimeType)) {
				return false;
			}
		}

		if (humanPresentableName == null) {
			if (that.humanPresentableName != null) {
				return false;
			}
		} else {
			if (!humanPresentableName.equals(that.humanPresentableName)) {
				return false;
			}
		}

		return true;
	}

	public boolean equals(String s) {
		if (s == null || mimeType == null)
			return false;
		return isMimeTypeEqual(s);
	}

	public int hashCode() {
		int total = 0;

		if (mimeType != null) {
			total += mimeType.hashCode();
		}

		if (humanPresentableName != null) {
			total += humanPresentableName.hashCode();
		}

		return total;
	}

	public boolean isMimeTypeEqual(String mimeType) {
		return this.mimeType.equals(mimeType);
	}

	public final boolean isMimeTypeEqual(DataFlavor dataFlavor) {
		return isMimeTypeEqual(dataFlavor.mimeType);
	}

	private String mimeType;
	private String humanPresentableName;
}
