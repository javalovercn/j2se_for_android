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
package javax.script;

/**
 * The generic <code>Exception</code> class for the Scripting APIs. Checked
 * exception types thrown by underlying scripting implementations must be
 * wrapped in instances of <code>ScriptException</code>. The class has members
 * to store line and column numbers and filenames if this information is
 * available.
 *
 * @author Mike Grogan
 * @since 1.6
 */
public class ScriptException extends Exception {

	private String fileName;
	private int lineNumber;
	private int columnNumber;

	public ScriptException(String s) {
		super(s);
		fileName = null;
		lineNumber = -1;
		columnNumber = -1;
	}

	public ScriptException(Exception e) {
		super(e);
		fileName = null;
		lineNumber = -1;
		columnNumber = -1;
	}

	public ScriptException(String message, String fileName, int lineNumber) {
		super(message);
		this.fileName = fileName;
		this.lineNumber = lineNumber;
		this.columnNumber = -1;
	}

	public ScriptException(String message, String fileName, int lineNumber, int columnNumber) {
		super(message);
		this.fileName = fileName;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
	}

	public String getMessage() {
		String ret = super.getMessage();
		if (fileName != null) {
			ret += (" in " + fileName);
			if (lineNumber != -1) {
				ret += " at line number " + lineNumber;
			}

			if (columnNumber != -1) {
				ret += " at column number " + columnNumber;
			}
		}

		return ret;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public int getColumnNumber() {
		return columnNumber;
	}

	public String getFileName() {
		return fileName;
	}
}