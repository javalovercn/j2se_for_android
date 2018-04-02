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
package javax.swing.filechooser;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import android.os.Environment;

/**
 * FileSystemView is JFileChooser's gateway to the file system. Since the JDK1.1
 * File API doesn't allow access to such information as root partitions, file
 * type information, or hidden file bits, this class is designed to intuit as
 * much OS-specific file system information as possible.
 *
 * <p>
 *
 * Java Licensees may want to provide a different implementation of
 * FileSystemView to better handle a given operating system.
 *
 * @author Jeff Dinkins
 */

// PENDING(jeff) - need to provide a specification for
// how Mac/OS2/BeOS/etc file systems can modify FileSystemView
// to handle their particular type of file system.

public abstract class FileSystemView {
	public static final String UP_LEVEL_DIR_TAG = "↺";

	static FileSystemView unixFileSystemView = null;

	public static FileSystemView getFileSystemView() {
		if (unixFileSystemView == null) {
			unixFileSystemView = new HCFileSystemView();
		}
		return unixFileSystemView;
	}

	public FileSystemView() {
	}

	public boolean isRoot(File f) {
		if (f == null || !f.isAbsolute()) {
			return false;
		}

		File[] roots = getRoots();
		for (File root : roots) {
			if (root.equals(f)) {
				return true;
			}
		}
		return false;
	}

	public Boolean isTraversable(File f) {
		return Boolean.valueOf(f.isDirectory());
	}

	/**
	 * Example from Windows: the "M:\" directory displays as "CD-ROM (M:)"
	 */
	public String getSystemDisplayName(File f) {
		if (f == null) {
			return null;
		}

		return f.getName();

	}

	/**
	 * Example from Windows: the "Desktop" folder
	 */
	public String getSystemTypeDescription(File f) {
		return "";
	}

	public Icon getSystemIcon(File f) {
		return getSystemIcon(f.isDirectory());
	}

	protected Icon getSystemIcon(boolean isDirectory) {
		return UIManager.getIcon(
				isDirectory ? UIDefaults.FILE_VIEW_DIRECTORY_ICON : UIDefaults.FILE_VIEW_FILE_ICON);
	}

	public boolean isParent(File folder, File file) {
		if (folder == null || file == null) {
			return false;
		} else {
			return folder.equals(file.getParentFile());
		}
	}

	public File getChild(File parent, String fileName) {
		return createFileObject(parent, fileName);
	}

	public boolean isFileSystem(File f) {
		return true;
	}

	public abstract File createNewFolder(File containingDir) throws IOException;

	public boolean isHiddenFile(File f) {
		return f.isHidden();
	}

	public boolean isFileSystemRoot(File dir) {
		if (rootFiles != null) {
			for (int i = 0; i < rootFiles.length; i++) {
				if (dir.equals(rootFiles[i])) {
					return true;
				}
			}
		}
		return false;
	}

	File[] rootFiles;

	public boolean isDrive(File dir) {
		return false;
	}

	public boolean isFloppyDrive(File dir) {
		return false;
	}

	public boolean isComputerNode(File dir) {
		return false;
	}

	private File getExternalFileAdAPI() {
		if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
			return Environment.getExternalStorageDirectory();
		} else {
			return null;
		}
	}

	final File usbCardDir = getExternalFileAdAPI();

	public File[] getRoots() {
		if (rootFiles == null) {
			File[] sdcardDir = { Environment.getExternalStorageDirectory() };// 外部存储，如/mnt/sdcard
			rootFiles = sdcardDir;
		}
		return rootFiles;
	}

	public File getHomeDirectory() {
		return createFileObject(System.getProperty("user.home"));
	}

	public File getDefaultDirectory() {
		return usbCardDir;
	}

	public File createFileObject(File dir, String filename) {
		if (dir == null) {
			return new File(filename);
		} else {
			return new File(dir, filename);
		}
	}

	public File createFileObject(String path) {
		File f = new File(path);
		if (isFileSystemRoot(f)) {
			f = createFileSystemRoot(f);
		}
		return f;
	}

	public File[] getFiles(File dir, boolean useFileHiding) {
		List<File> files = new ArrayList<File>();

		File[] names = dir.listFiles();

		if (names == null) {
			return new File[0];
		}

		for (File f : names) {
			if (Thread.currentThread().isInterrupted()) {
				break;
			}

			if (!useFileHiding || !isHiddenFile(f)) {
				files.add(f);
			}
		}

		return files.toArray(new File[files.size()]);
	}

	public File getParentDirectory(File dir) {
		if (dir == null || !dir.exists()) {
			return null;
		}

		File psf = dir.getParentFile();

		if (psf == null) {
			return null;
		}

		if (isFileSystem(psf)) {
			File f = psf;
			if (!f.exists()) {
				File ppsf = psf.getParentFile();
				if (ppsf == null || !isFileSystem(ppsf)) {
					f = createFileSystemRoot(f);
				}
			}
			return f;
		} else {
			return psf;
		}
	}

	protected File createFileSystemRoot(File f) {
		return new FileSystemRoot(f);
	}

	static class FileSystemRoot extends File {
		public FileSystemRoot(File f) {
			super(f, "");
		}

		public FileSystemRoot(String s) {
			super(s);
		}

		public boolean isDirectory() {
			return true;
		}

		public String getName() {
			return getPath();
		}
	}
}

class HCFileSystemView extends UnixFileSystemView {

	public String getSystemDisplayName(File f) {
		String name = f.getName();
		if (name.equals(UP_LEVEL_DIR_TAG)) {
			return "../" + UP_LEVEL_DIR_TAG;
		}
		String absPath = f.getAbsolutePath();
		int idx = absPath.lastIndexOf("/");
		if (idx > 0) {
			return absPath.substring(idx + 1);
		} else {
			return absPath;
		}
	}

	public Icon getSystemIcon(File f) {
		String name = f.getName();
		if (name.equals(UP_LEVEL_DIR_TAG)) {
			return super.getSystemIcon(true);
		}
		return super.getSystemIcon(f);
	}
}

class UnixFileSystemView extends FileSystemView {

	private static final String newFolderString = UIManager
			.getString("FileChooser.other.newFolder");
	private static final String newFolderNextString = UIManager
			.getString("FileChooser.other.newFolder.subsequent");

	public File createNewFolder(File containingDir) throws IOException {
		if (containingDir == null) {
			throw new IOException("Containing directory is null:");
		}
		File newFolder;
		newFolder = createFileObject(containingDir, newFolderString);
		int i = 1;
		while (newFolder.exists() && i < 100) {
			newFolder = createFileObject(containingDir,
					MessageFormat.format(newFolderNextString, new Integer(i)));
			i++;
		}

		if (newFolder.exists()) {
			throw new IOException("Directory already exists:" + newFolder.getAbsolutePath());
		} else {
			newFolder.mkdirs();
		}

		return newFolder;
	}

	public boolean isFileSystemRoot(File dir) {
		return dir != null && dir.getAbsolutePath().equals("/");
	}

	public boolean isDrive(File dir) {
		return isFloppyDrive(dir);
	}

	public boolean isFloppyDrive(File dir) {
		return false;
	}

	public boolean isComputerNode(File dir) {
		if (dir != null) {
			String parent = dir.getParent();
			if (parent != null && parent.equals("/net")) {
				return true;
			}
		}
		return false;
	}
}

class GenericFileSystemView extends FileSystemView {

	private static final String newFolderString = UIManager
			.getString("FileChooser.other.newFolder");

	public File createNewFolder(File containingDir) throws IOException {
		if (containingDir == null) {
			throw new IOException("Containing directory is null:");
		}
		File newFolder = createFileObject(containingDir, newFolderString);

		if (newFolder.exists()) {
			throw new IOException("Directory already exists:" + newFolder.getAbsolutePath());
		} else {
			newFolder.mkdirs();
		}

		return newFolder;
	}

}