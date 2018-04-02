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
import hc.android.J2SEInitor;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import android.content.Intent;
import android.net.Uri;

/**
 * The {@code Desktop} class allows a Java application to launch associated
 * applications registered on the native desktop to handle a
 * {@link java.net.URI} or a file.
 *
 * <p>
 * Supported operations include:
 * <ul>
 * <li>launching the user-default browser to show a specified URI;</li>
 * <li>launching the user-default mail client with an optional {@code mailto}
 * URI;</li>
 * <li>launching a registered application to open, edit or print a specified
 * file.</li>
 * </ul>
 *
 * <p>
 * This class provides methods corresponding to these operations. The methods
 * look for the associated application registered on the current platform, and
 * launch it to handle a URI or file. If there is no associated application or
 * the associated application fails to be launched, an exception is thrown.
 *
 * <p>
 * An application is registered to a URI or file type; for example, the
 * {@code "sxi"} file extension is typically registered to StarOffice. The
 * mechanism of registering, accessing, and launching the associated application
 * is platform-dependent.
 *
 * <p>
 * Each operation is an action type represented by the {@link Desktop.Action}
 * class.
 *
 * <p>
 * Note: when some action is invoked and the associated application is executed,
 * it will be executed on the same system as the one on which the Java
 * application was launched.
 *
 * @since 1.6
 * @author Armin Chen
 * @author George Zhang
 */
public class Desktop {
	public static enum Action {
		OPEN, EDIT, PRINT, MAIL, BROWSE
	};

	private Desktop() {
	}

	private static Desktop instance;

	public static synchronized Desktop getDesktop() {
		if (instance == null) {
			instance = new Desktop();
		}

		return instance;
	}

	public static boolean isDesktopSupported() {
		return true;
	}

	public boolean isSupported(Action action) {
		if (action.equals(java.awt.Desktop.Action.BROWSE)) {
			return true;
		} else if (action.equals(Desktop.Action.MAIL)) {
			return true;
		}
		return false;
	}

	private static void checkFileValidation(File file) {
		if (file == null)
			throw new NullPointerException("File must not be null");

		if (!file.exists()) {
			throw new IllegalArgumentException("The file: " + file.getPath() + " doesn't exist.");
		}

		file.canRead();
	}

	private void checkActionSupport(Action actionType) {
		if (!isSupported(actionType)) {
			throw new UnsupportedOperationException("The " + actionType.name()
					+ " action is not supported on the current platform!");
		}
	}

	private void checkAWTPermission() {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			sm.checkPermission(new AWTPermission("showWindowWithoutWarningBanner"));
		}
	}

	public void open(File file) throws IOException {
		// checkAWTPermission();
		// checkExec();
		// checkActionSupport(Action.OPEN);
		// checkFileValidation(file);
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public void edit(File file) throws IOException {
		// checkAWTPermission();
		// checkExec();
		// checkActionSupport(Action.EDIT);
		// file.canWrite();
		// checkFileValidation(file);

		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public void print(File file) throws IOException {
		// checkExec();
		// SecurityManager sm = System.getSecurityManager();
		// if (sm != null) {
		// sm.checkPrintJobAccess();
		// }
		// checkActionSupport(Action.PRINT);
		// checkFileValidation(file);

		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	public void browse(URI uri) throws IOException {
		if (uri == null) {
			throw new NullPointerException();
		}
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(uri.toString());
		intent.setData(content_url);
		ActivityManager.applicationContext.startActivity(intent);
		return;
	}

	public void mail() throws IOException {
		Intent intent = new Intent(Intent.ACTION_SEND);
		String[] receiver = new String[] { J2SEInitor.getContactEmail() };
		intent.putExtra(Intent.EXTRA_EMAIL, receiver);
		ActivityManager.applicationContext.startActivity(intent);// 调用系统的mail客户端进行发送}
	}

	// http://www.ietf.org/rfc/rfc2368.txt
	public void mail(URI mailtoURI) throws IOException {
		mail();
	}

	private void checkExec() throws SecurityException {
	}
}