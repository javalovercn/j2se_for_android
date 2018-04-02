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
package javax.swing;

import java.awt.AWTKeyStroke;
import java.awt.event.KeyEvent;

/**
 * A KeyStroke represents a key action on the keyboard, or equivalent input
 * device. KeyStrokes can correspond to only a press or release of a particular
 * key, just as KEY_PRESSED and KEY_RELEASED KeyEvents do; alternately, they can
 * correspond to typing a specific Java character, just as KEY_TYPED KeyEvents
 * do. In all cases, KeyStrokes can specify modifiers (alt, shift, control,
 * meta, altGraph, or a combination thereof) which must be present during the
 * action for an exact match.
 * <p>
 * KeyStrokes are used to define high-level (semantic) action events. Instead of
 * trapping every keystroke and throwing away the ones you are not interested
 * in, those keystrokes you care about automatically initiate actions on the
 * Components with which they are registered.
 * <p>
 * KeyStrokes are immutable, and are intended to be unique. Client code cannot
 * create a KeyStroke; a variant of <code>getKeyStroke</code> must be used
 * instead. These factory methods allow the KeyStroke implementation to cache
 * and share instances efficiently.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @see javax.swing.text.Keymap
 * @see #getKeyStroke
 *
 * @author Arnaud Weber
 * @author David Mendenhall
 */
public class KeyStroke extends AWTKeyStroke {

	private KeyStroke() {
	}

	private KeyStroke(char keyChar, int keyCode, int modifiers, boolean onKeyRelease) {
		super(keyChar, keyCode, modifiers, onKeyRelease);
	}

	public static KeyStroke getKeyStroke(char keyChar) {
		return (KeyStroke) getAWTKeyStroke(keyChar);
	}

	public static KeyStroke getKeyStroke(char keyChar, boolean onKeyRelease) {
		return (KeyStroke) getKeyStroke(keyChar, 0, onKeyRelease);
	}

	public static KeyStroke getKeyStroke(Character keyChar, int modifiers) {
		return getKeyStroke(keyChar.charValue(), false);
	}

	public static KeyStroke getKeyStroke(int keyCode, int modifiers, boolean onKeyRelease) {
		return new KeyStroke(KeyEvent.CHAR_UNDEFINED, keyCode, 0, onKeyRelease);
	}

	public static KeyStroke getKeyStroke(int keyCode, int modifiers) {
		return getKeyStroke(keyCode, modifiers, false);
	}

	public static KeyStroke getKeyStrokeForEvent(KeyEvent anEvent) {
		synchronized (AWTKeyStroke.class) {
			registerSubclass(KeyStroke.class);
			return (KeyStroke) getAWTKeyStrokeForEvent(anEvent);
		}
	}

	public static KeyStroke getKeyStroke(String s) {
		if (s == null || s.length() == 0) {
			return null;
		}
		synchronized (AWTKeyStroke.class) {
			registerSubclass(KeyStroke.class);
			try {
				return (KeyStroke) getAWTKeyStroke(s);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	}
}