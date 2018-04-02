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
package javax.accessibility;

import java.util.Locale;

/**
 * <p>
 * Base class used to maintain a strongly typed enumeration. This is the
 * superclass of {@link AccessibleState} and {@link AccessibleRole}.
 * <p>
 * The toDisplayString method allows you to obtain the localized string for a
 * locale independent key from a predefined ResourceBundle for the keys defined
 * in this class. This localized string is intended to be readable by humans.
 *
 * @see AccessibleRole
 * @see AccessibleState
 *
 * @author Willie Walker
 * @author Peter Korn
 * @author Lynn Monsanto
 */
public abstract class AccessibleBundle {

	public AccessibleBundle() {
	}

	protected String key = null;

	protected String toDisplayString(String resourceBundleName, Locale locale) {
		return "";
	}

	public String toDisplayString(Locale locale) {
		return toDisplayString("", locale);
	}

	public String toDisplayString() {
		return toDisplayString(Locale.getDefault());
	}

	public String toString() {
		return toDisplayString();
	}

	private void loadResourceBundle(String resourceBundleName, Locale locale) {
	}
}
