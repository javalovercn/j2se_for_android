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

/**
 * <b>NOTE:</b> The <code>Event</code> class is obsolete and is
 * available only for backwards compatilibility.  It has been replaced
 * by the <code>AWTEvent</code> class and its subclasses.
 * <p>
 * <code>Event</code> is a platform-independent class that
 * encapsulates events from the platform's Graphical User
 * Interface in the Java&nbsp;1.0 event model. In Java&nbsp;1.1
 * and later versions, the <code>Event</code> class is maintained
 * only for backwards compatibilty. The information in this
 * class description is provided to assist programmers in
 * converting Java&nbsp;1.0 programs to the new event model.
 * <p>
 * In the Java&nbsp;1.0 event model, an event contains an
 * {@link Event#id} field
 * that indicates what type of event it is and which other
 * <code>Event</code> variables are relevant for the event.
 * <p>
 * For keyboard events, {@link Event#key}
 * contains a value indicating which key was activated, and
 * {@link Event#modifiers} contains the
 * modifiers for that event.  For the KEY_PRESS and KEY_RELEASE
 * event ids, the value of <code>key</code> is the unicode
 * character code for the key. For KEY_ACTION and
 * KEY_ACTION_RELEASE, the value of <code>key</code> is
 * one of the defined action-key identifiers in the
 * <code>Event</code> class (<code>PGUP</code>,
 * <code>PGDN</code>, <code>F1</code>, <code>F2</code>, etc).
 *
 * @author     Sami Shaio
 * @since      JDK1.0
 */
public class Event implements java.io.Serializable {
	private transient long data;

	public static final int SHIFT_MASK = 1 << 0;
	public static final int CTRL_MASK = 1 << 1;
	public static final int META_MASK = 1 << 2;
	public static final int ALT_MASK = 1 << 3;
	public static final int HOME = 1000;
	public static final int END = 1001;
	public static final int PGUP = 1002;
	public static final int PGDN = 1003;
	public static final int UP = 1004;
	public static final int DOWN = 1005;
	public static final int LEFT = 1006;
	public static final int RIGHT = 1007;
	public static final int F1 = 1008;
	public static final int F2 = 1009;
	public static final int F3 = 1010;
	public static final int F4 = 1011;
	public static final int F5 = 1012;
	public static final int F6 = 1013;
	public static final int F7 = 1014;
	public static final int F8 = 1015;
	public static final int F9 = 1016;
	public static final int F10 = 1017;
	public static final int F11 = 1018;
	public static final int F12 = 1019;
	public static final int PRINT_SCREEN = 1020;
	public static final int SCROLL_LOCK = 1021;
	public static final int CAPS_LOCK = 1022;
	public static final int NUM_LOCK = 1023;
	public static final int PAUSE = 1024;
	public static final int INSERT = 1025;
	public static final int ENTER = '\n';
	public static final int BACK_SPACE = '\b';
	public static final int TAB = '\t';
	public static final int ESCAPE = 27;
	public static final int DELETE = 127;
	private static final int WINDOW_EVENT = 200;
	public static final int WINDOW_DESTROY = 1 + WINDOW_EVENT;
	public static final int WINDOW_EXPOSE = 2 + WINDOW_EVENT;
	public static final int WINDOW_ICONIFY = 3 + WINDOW_EVENT;
	public static final int WINDOW_DEICONIFY = 4 + WINDOW_EVENT;
	public static final int WINDOW_MOVED = 5 + WINDOW_EVENT;
	private static final int KEY_EVENT = 400;
	public static final int KEY_PRESS = 1 + KEY_EVENT;
	public static final int KEY_RELEASE = 2 + KEY_EVENT;
	public static final int KEY_ACTION = 3 + KEY_EVENT;
	public static final int KEY_ACTION_RELEASE = 4 + KEY_EVENT;
	private static final int MOUSE_EVENT = 500;
	public static final int MOUSE_DOWN = 1 + MOUSE_EVENT;
	public static final int MOUSE_UP = 2 + MOUSE_EVENT;
	public static final int MOUSE_MOVE = 3 + MOUSE_EVENT;
	public static final int MOUSE_ENTER = 4 + MOUSE_EVENT;
	public static final int MOUSE_EXIT = 5 + MOUSE_EVENT;
	public static final int MOUSE_DRAG = 6 + MOUSE_EVENT;
	private static final int SCROLL_EVENT = 600;
	public static final int SCROLL_LINE_UP = 1 + SCROLL_EVENT;
	public static final int SCROLL_LINE_DOWN = 2 + SCROLL_EVENT;
	public static final int SCROLL_PAGE_UP = 3 + SCROLL_EVENT;
	public static final int SCROLL_PAGE_DOWN = 4 + SCROLL_EVENT;
	public static final int SCROLL_ABSOLUTE = 5 + SCROLL_EVENT;
	public static final int SCROLL_BEGIN = 6 + SCROLL_EVENT;
	public static final int SCROLL_END = 7 + SCROLL_EVENT;
	private static final int LIST_EVENT = 700;
	public static final int LIST_SELECT = 1 + LIST_EVENT;
	public static final int LIST_DESELECT = 2 + LIST_EVENT;
	private static final int MISC_EVENT = 1000;
	public static final int ACTION_EVENT = 1 + MISC_EVENT;
	public static final int LOAD_FILE = 2 + MISC_EVENT;
	public static final int SAVE_FILE = 3 + MISC_EVENT;
	public static final int GOT_FOCUS = 4 + MISC_EVENT;
	public static final int LOST_FOCUS = 5 + MISC_EVENT;

	public Object target;
	public long when;
	public int id;
	public int x;
	public int y;
	public int key;
	public int modifiers;
	public int clickCount;
	public Object arg;
	public Event evt;

	private static final int actionKeyCodes[][] = {
			{ KeyEvent.VK_HOME, Event.HOME }, { KeyEvent.VK_END, Event.END },
			{ KeyEvent.VK_PAGE_UP, Event.PGUP },
			{ KeyEvent.VK_PAGE_DOWN, Event.PGDN },
			{ KeyEvent.VK_UP, Event.UP }, { KeyEvent.VK_DOWN, Event.DOWN },
			{ KeyEvent.VK_LEFT, Event.LEFT },
			{ KeyEvent.VK_RIGHT, Event.RIGHT }, { KeyEvent.VK_F1, Event.F1 },
			{ KeyEvent.VK_F2, Event.F2 }, { KeyEvent.VK_F3, Event.F3 },
			{ KeyEvent.VK_F4, Event.F4 }, { KeyEvent.VK_F5, Event.F5 },
			{ KeyEvent.VK_F6, Event.F6 }, { KeyEvent.VK_F7, Event.F7 },
			{ KeyEvent.VK_F8, Event.F8 }, { KeyEvent.VK_F9, Event.F9 },
			{ KeyEvent.VK_F10, Event.F10 }, { KeyEvent.VK_F11, Event.F11 },
			{ KeyEvent.VK_F12, Event.F12 },
			{ KeyEvent.VK_PRINTSCREEN, Event.PRINT_SCREEN },
			{ KeyEvent.VK_SCROLL_LOCK, Event.SCROLL_LOCK },
			{ KeyEvent.VK_CAPS_LOCK, Event.CAPS_LOCK },
			{ KeyEvent.VK_NUM_LOCK, Event.NUM_LOCK },
			{ KeyEvent.VK_PAUSE, Event.PAUSE },
			{ KeyEvent.VK_INSERT, Event.INSERT } };

	private boolean consumed = false;

	public Event(Object target, long when, int id, int x, int y, int key,
			int modifiers, Object arg) {
		this.target = target;
		this.when = when;
		this.id = id;
		this.x = x;
		this.y = y;
		this.key = key;
		this.modifiers = modifiers;
		this.arg = arg;
		this.data = 0;
		this.clickCount = 0;
		switch (id) {
		case ACTION_EVENT:
		case WINDOW_DESTROY:
		case WINDOW_ICONIFY:
		case WINDOW_DEICONIFY:
		case WINDOW_MOVED:
		case SCROLL_LINE_UP:
		case SCROLL_LINE_DOWN:
		case SCROLL_PAGE_UP:
		case SCROLL_PAGE_DOWN:
		case SCROLL_ABSOLUTE:
		case SCROLL_BEGIN:
		case SCROLL_END:
		case LIST_SELECT:
		case LIST_DESELECT:
			consumed = true;
			break;
		default:
		}
	}

	public Event(Object target, long when, int id, int x, int y, int key,
			int modifiers) {
		this(target, when, id, x, y, key, modifiers, null);
	}

	public Event(Object target, int id, Object arg) {
		this(target, 0, id, 0, 0, 0, 0, arg);
	}

	public void translate(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}

	public boolean shiftDown() {
		return (modifiers & SHIFT_MASK) != 0;
	}

	public boolean controlDown() {
		return (modifiers & CTRL_MASK) != 0;
	}

	public boolean metaDown() {
		return (modifiers & META_MASK) != 0;
	}

	void consume() {
		switch (id) {
		case KEY_PRESS:
		case KEY_RELEASE:
		case KEY_ACTION:
		case KEY_ACTION_RELEASE:
			consumed = true;
			break;
		default:
		}
	}

	boolean isConsumed() {
		return consumed;
	}

	static int getOldEventKey(KeyEvent e) {
		int keyCode = e.getKeyCode();
		for (int i = 0; i < actionKeyCodes.length; i++) {
			if (actionKeyCodes[i][0] == keyCode) {
				return actionKeyCodes[i][1];
			}
		}
		return (int) e.getKeyChar();
	}

	char getKeyEventChar() {
		for (int i = 0; i < actionKeyCodes.length; i++) {
			if (actionKeyCodes[i][1] == key) {
				return KeyEvent.CHAR_UNDEFINED;
			}
		}
		return (char) key;
	}

	protected String paramString() {
		return "id=" + id + ",x=" + x + ",y=" + y;
	}

	public String toString() {
		return getClass().getName() + "[" + paramString() + "]";
	}
}
