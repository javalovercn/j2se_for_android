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

/**
 * <P>
 * Class AccessibleRole determines the role of a component. The role of a
 * component describes its generic function. (E.G., "push button," "table," or
 * "list.")
 * <p>
 * The toDisplayString method allows you to obtain the localized string for a
 * locale independent key from a predefined ResourceBundle for the keys defined
 * in this class.
 * <p>
 * The constants in this class present a strongly typed enumeration of common
 * object roles. A public constructor for this class has been purposely omitted
 * and applications should use one of the constants from this class. If the
 * constants in this class are not sufficient to describe the role of an object,
 * a subclass should be generated from this class and it should provide
 * constants in a similar manner.
 *
 * @author Willie Walker
 * @author Peter Korn
 * @author Lynn Monsanto
 */
public class AccessibleRole extends AccessibleBundle {
	public static final AccessibleRole ALERT = new AccessibleRole("alert");

	public static final AccessibleRole COLUMN_HEADER = new AccessibleRole("columnheader");

	public static final AccessibleRole CANVAS = new AccessibleRole("canvas");
	public static final AccessibleRole COMBO_BOX = new AccessibleRole("combobox");
	public static final AccessibleRole DESKTOP_ICON = new AccessibleRole("desktopicon");
	public static final AccessibleRole HTML_CONTAINER = new AccessibleRole("htmlcontainer");

	public static final AccessibleRole INTERNAL_FRAME = new AccessibleRole("internalframe");
	public static final AccessibleRole DESKTOP_PANE = new AccessibleRole("desktoppane");
	public static final AccessibleRole OPTION_PANE = new AccessibleRole("optionpane");
	public static final AccessibleRole WINDOW = new AccessibleRole("window");
	public static final AccessibleRole FRAME = new AccessibleRole("frame");
	public static final AccessibleRole DIALOG = new AccessibleRole("dialog");
	public static final AccessibleRole COLOR_CHOOSER = new AccessibleRole("colorchooser");
	public static final AccessibleRole DIRECTORY_PANE = new AccessibleRole("directorypane");
	public static final AccessibleRole FILE_CHOOSER = new AccessibleRole("filechooser");
	public static final AccessibleRole FILLER = new AccessibleRole("filler");
	public static final AccessibleRole HYPERLINK = new AccessibleRole("hyperlink");
	public static final AccessibleRole ICON = new AccessibleRole("icon");
	public static final AccessibleRole LABEL = new AccessibleRole("label");
	public static final AccessibleRole ROOT_PANE = new AccessibleRole("rootpane");
	public static final AccessibleRole GLASS_PANE = new AccessibleRole("glasspane");
	public static final AccessibleRole LAYERED_PANE = new AccessibleRole("layeredpane");
	public static final AccessibleRole LIST = new AccessibleRole("list");
	public static final AccessibleRole LIST_ITEM = new AccessibleRole("listitem");
	public static final AccessibleRole MENU_BAR = new AccessibleRole("menubar");
	public static final AccessibleRole POPUP_MENU = new AccessibleRole("popupmenu");
	public static final AccessibleRole MENU = new AccessibleRole("menu");
	public static final AccessibleRole MENU_ITEM = new AccessibleRole("menuitem");
	public static final AccessibleRole SEPARATOR = new AccessibleRole("separator");
	public static final AccessibleRole PAGE_TAB_LIST = new AccessibleRole("pagetablist");
	public static final AccessibleRole PAGE_TAB = new AccessibleRole("pagetab");
	public static final AccessibleRole PANEL = new AccessibleRole("panel");
	public static final AccessibleRole PROGRESS_BAR = new AccessibleRole("progressbar");
	public static final AccessibleRole PASSWORD_TEXT = new AccessibleRole("passwordtext");
	public static final AccessibleRole PUSH_BUTTON = new AccessibleRole("pushbutton");
	public static final AccessibleRole TOGGLE_BUTTON = new AccessibleRole("togglebutton");
	public static final AccessibleRole CHECK_BOX = new AccessibleRole("checkbox");
	public static final AccessibleRole RADIO_BUTTON = new AccessibleRole("radiobutton");
	public static final AccessibleRole ROW_HEADER = new AccessibleRole("rowheader");
	public static final AccessibleRole SCROLL_PANE = new AccessibleRole("scrollpane");
	public static final AccessibleRole SCROLL_BAR = new AccessibleRole("scrollbar");
	public static final AccessibleRole VIEWPORT = new AccessibleRole("viewport");
	public static final AccessibleRole SLIDER = new AccessibleRole("slider");
	public static final AccessibleRole SPLIT_PANE = new AccessibleRole("splitpane");
	public static final AccessibleRole TABLE = new AccessibleRole("table");
	public static final AccessibleRole TEXT = new AccessibleRole("text");
	public static final AccessibleRole TREE = new AccessibleRole("tree");
	public static final AccessibleRole TOOL_BAR = new AccessibleRole("toolbar");
	public static final AccessibleRole TOOL_TIP = new AccessibleRole("tooltip");
	public static final AccessibleRole AWT_COMPONENT = new AccessibleRole("awtcomponent");
	public static final AccessibleRole SWING_COMPONENT = new AccessibleRole("swingcomponent");
	public static final AccessibleRole UNKNOWN = new AccessibleRole("unknown");
	public static final AccessibleRole STATUS_BAR = new AccessibleRole("statusbar");
	public static final AccessibleRole DATE_EDITOR = new AccessibleRole("dateeditor");
	public static final AccessibleRole SPIN_BOX = new AccessibleRole("spinbox");
	public static final AccessibleRole FONT_CHOOSER = new AccessibleRole("fontchooser");
	public static final AccessibleRole GROUP_BOX = new AccessibleRole("groupbox");
	public static final AccessibleRole HEADER = new AccessibleRole("header");
	public static final AccessibleRole FOOTER = new AccessibleRole("footer");
	public static final AccessibleRole PARAGRAPH = new AccessibleRole("paragraph");
	public static final AccessibleRole RULER = new AccessibleRole("ruler");
	static public final AccessibleRole EDITBAR = new AccessibleRole("editbar");
	static public final AccessibleRole PROGRESS_MONITOR = new AccessibleRole("progressMonitor");

	protected AccessibleRole(String key) {
		this.key = key;
	}
}
