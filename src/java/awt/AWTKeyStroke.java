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

import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.lang.reflect.Constructor;

import javax.swing.KeyStroke;

/**
 * An <code>AWTKeyStroke</code> represents a key action on the keyboard, or
 * equivalent input device. <code>AWTKeyStroke</code>s can correspond to only a
 * press or release of a particular key, just as <code>KEY_PRESSED</code> and
 * <code>KEY_RELEASED</code> <code>KeyEvent</code>s do; alternately, they can
 * correspond to typing a specific Java character, just as
 * <code>KEY_TYPED</code> <code>KeyEvent</code>s do. In all cases,
 * <code>AWTKeyStroke</code>s can specify modifiers (alt, shift, control, meta,
 * altGraph, or a combination thereof) which must be present during the action
 * for an exact match.
 * <p>
 * <code>AWTKeyStrokes</code> are immutable, and are intended to be unique.
 * Client code should never create an <code>AWTKeyStroke</code> on its own, but
 * should instead use a variant of <code>getAWTKeyStroke</code>. Client use of
 * these factory methods allows the <code>AWTKeyStroke</code> implementation to
 * cache and share instances efficiently.
 *
 * @see #getAWTKeyStroke
 *
 * @author Arnaud Weber
 * @author David Mendenhall
 * @since 1.4
 */
public class AWTKeyStroke implements Serializable {
	private static VKCollection vks;

	private static Class getAWTKeyStrokeClass() {
		return AWTKeyStroke.class;
	}

	private char keyChar = KeyEvent.CHAR_UNDEFINED;
	private int keyCode = KeyEvent.VK_UNDEFINED;
	private int modifiers;
	private boolean onKeyRelease;

	protected AWTKeyStroke() {
	}

	protected AWTKeyStroke(char keyChar, int keyCode, int modifiers, boolean onKeyRelease) {
		this.keyChar = keyChar;
		this.keyCode = keyCode;
		this.modifiers = modifiers;
		this.onKeyRelease = onKeyRelease;
	}

	protected static void registerSubclass(Class<?> subclass) {
	}

	private static Constructor getCtor(final Class clazz) {
		return null;
	}

	private static synchronized AWTKeyStroke getCachedStroke(char keyChar, int keyCode,
			int modifiers, boolean onKeyRelease) {
		return KeyStroke.getKeyStroke(keyCode, modifiers, onKeyRelease);
	}

	public static AWTKeyStroke getAWTKeyStroke(char keyChar) {
		return getAWTKeyStroke(Character.valueOf(keyChar), 0);
	}

	public static AWTKeyStroke getAWTKeyStroke(Character keyChar, int modifiers) {
		return getCachedStroke(keyChar.charValue(), KeyEvent.VK_UNDEFINED, modifiers, false);
	}

	public static AWTKeyStroke getAWTKeyStroke(int keyCode, int modifiers, boolean onKeyRelease) {
		return getCachedStroke(KeyEvent.CHAR_UNDEFINED, keyCode, modifiers, onKeyRelease);
	}

	public static AWTKeyStroke getAWTKeyStroke(int keyCode, int modifiers) {
		return getAWTKeyStroke(keyCode, modifiers, false);
	}

	public static AWTKeyStroke getAWTKeyStrokeForEvent(KeyEvent anEvent) {
		return getCachedStroke(KeyEvent.CHAR_UNDEFINED, anEvent.getKeyCode(),
				anEvent.getModifiers(), (anEvent.getID() == KeyEvent.KEY_RELEASED));
	}

	public static AWTKeyStroke getAWTKeyStroke(String s) {
		return null;
	}

	private static VKCollection getVKCollection() {
		if (vks == null) {
			vks = new VKCollection();
		}
		return vks;
	}

	private static int getVKValue(String key) {
		return 0;
	}

	public final char getKeyChar() {
		return keyChar;
	}

	public final int getKeyCode() {
		return keyCode;
	}

	public final int getModifiers() {
		return modifiers;
	}

	public final boolean isOnKeyRelease() {
		return onKeyRelease;
	}

	public final int getKeyEventType() {
		return 0;
	}

	public int hashCode() {
		return 0;
	}

	public final boolean equals(Object anObject) {
		return false;
	}

	public String toString() {
		return getModifiersText(modifiers) + "typed " + keyChar;
	}

	static String getModifiersText(int modifiers) {
		return "";
	}

	static String getVKText(int keyCode) {
		return "UNKNOWN";
	}

	protected Object readResolve() throws java.io.ObjectStreamException {
		return this;
	}

	private static int mapOldModifiers(int modifiers) {
		return modifiers;
	}

	private static int mapNewModifiers(int modifiers) {
		return modifiers;
	}

}

class VKCollection {
	public VKCollection() {
	}

	public synchronized void put(String name, Integer code) {
	}

	public synchronized Integer findCode(String name) {
		return 0;
	}

	public synchronized String findName(Integer code) {
		return "";
	}
}
