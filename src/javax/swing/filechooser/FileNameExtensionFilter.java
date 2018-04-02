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
import java.util.Locale;

/**
 * An implementation of {@code FileFilter} that filters using a specified set of
 * extensions. The extension for a file is the portion of the file name after
 * the last ".". Files whose name does not contain a "." have no file name
 * extension. File name extension comparisons are case insensitive.
 * <p>
 * The following example creates a {@code FileNameExtensionFilter} that will
 * show {@code jpg} files:
 * 
 * <pre>
 * FileFilter filter = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg");
 * JFileChooser fileChooser = ...;
 * fileChooser.addChoosableFileFilter(filter);
 * </pre>
 *
 * @see FileFilter
 * @see javax.swing.JFileChooser#setFileFilter
 * @see javax.swing.JFileChooser#addChoosableFileFilter
 * @see javax.swing.JFileChooser#getFileFilter
 *
 * @since 1.6
 */
public final class FileNameExtensionFilter extends FileFilter {
	private final String description;
	private final String[] extensions;
	private final String[] lowerCaseExtensions;

	public FileNameExtensionFilter(String description, String... extensions) {
		if (extensions == null || extensions.length == 0) {
			throw new IllegalArgumentException("Extensions must be non-null and not empty");
		}
		this.description = description;
		this.extensions = new String[extensions.length];
		this.lowerCaseExtensions = new String[extensions.length];
		for (int i = 0; i < extensions.length; i++) {
			if (extensions[i] == null || extensions[i].length() == 0) {
				throw new IllegalArgumentException("Each extension must be non-null and not empty");
			}
			this.extensions[i] = extensions[i];
			lowerCaseExtensions[i] = extensions[i].toLowerCase(Locale.ENGLISH);
		}
	}

	/**
	 * True is returned if the extension matches one of the file name
	 * extensions, or the file is a directory.
	 */
	public boolean accept(File f) {
		if (f != null) {
			if (f.isDirectory()) {
				return true;
			}
			String fileName = f.getName();
			int i = fileName.lastIndexOf('.');
			if (i > 0 && i < fileName.length() - 1) {
				String desiredExtension = fileName.substring(i + 1).toLowerCase(Locale.ENGLISH);
				for (String extension : lowerCaseExtensions) {
					if (desiredExtension.equals(extension)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * For example: "JPG and GIF Images."
	 */
	public String getDescription() {
		return description;
	}

	public String[] getExtensions() {
		String[] result = new String[extensions.length];
		System.arraycopy(extensions, 0, result, 0, extensions.length);
		return result;
	}

	public String toString() {
		return "";
	}
}