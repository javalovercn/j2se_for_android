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
package javax.swing.plaf.synth;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.UIDefaults;

/**
 * A distinct rendering area of a Swing component. A component may support one
 * or more regions. Specific component regions are defined by the typesafe
 * enumeration in this class.
 * <p>
 * Regions are typically used as a way to identify the <code>Component</code>s
 * and areas a particular style is to apply to. Synth's file format allows you
 * to bind styles based on the name of a <code>Region</code>. The name is
 * derived from the field name of the constant:
 * <ol>
 * <li>Map all characters to lowercase.
 * <li>Map the first character to uppercase.
 * <li>Map the first character after underscores to uppercase.
 * <li>Remove all underscores.
 * </ol>
 * For example, to identify the <code>SPLIT_PANE</code> <code>Region</code> you
 * would use <code>SplitPane</code>. The following shows a custom
 * <code>SynthStyleFactory</code> that returns a specific style for split panes:
 * 
 * <pre>
 *    public SynthStyle getStyle(JComponent c, Region id) {
 *        if (id == Region.SPLIT_PANE) {
 *            return splitPaneStyle;
 *        }
 *        ...
 *    }
 * </pre>
 * 
 * The following <a href="doc-files/synthFileFormat.html">xml</a> accomplishes
 * the same thing:
 * 
 * <pre>
 * &lt;style id="splitPaneStyle">
 *   ...
 * &lt;/style>
 * &lt;bind style="splitPaneStyle" type="region" key="SplitPane"/>
 * </pre>
 *
 * @since 1.5
 * @author Scott Violet
 */
public class Region {
	private static final Object UI_TO_REGION_MAP_KEY = new Object();
	private static final Object LOWER_CASE_NAME_MAP_KEY = new Object();

	public static final Region ARROW_BUTTON = new Region("ArrowButton", false);
	public static final Region BUTTON = new Region("Button", false);
	public static final Region CHECK_BOX = new Region("CheckBox", false);
	public static final Region CHECK_BOX_MENU_ITEM = new Region("CheckBoxMenuItem", false);
	public static final Region COLOR_CHOOSER = new Region("ColorChooser", false);
	public static final Region COMBO_BOX = new Region("ComboBox", false);
	public static final Region DESKTOP_PANE = new Region("DesktopPane", false);
	public static final Region DESKTOP_ICON = new Region("DesktopIcon", false);
	public static final Region EDITOR_PANE = new Region("EditorPane", false);
	public static final Region FILE_CHOOSER = new Region("FileChooser", false);
	public static final Region FORMATTED_TEXT_FIELD = new Region("FormattedTextField", false);
	public static final Region INTERNAL_FRAME = new Region("InternalFrame", false);
	public static final Region INTERNAL_FRAME_TITLE_PANE = new Region("InternalFrameTitlePane",
			false);
	public static final Region LABEL = new Region("Label", false);
	public static final Region LIST = new Region("List", false);
	public static final Region MENU = new Region("Menu", false);
	public static final Region MENU_BAR = new Region("MenuBar", false);
	public static final Region MENU_ITEM = new Region("MenuItem", false);
	public static final Region MENU_ITEM_ACCELERATOR = new Region("MenuItemAccelerator", true);
	public static final Region OPTION_PANE = new Region("OptionPane", false);
	public static final Region PANEL = new Region("Panel", false);
	public static final Region PASSWORD_FIELD = new Region("PasswordField", false);
	public static final Region POPUP_MENU = new Region("PopupMenu", false);
	public static final Region POPUP_MENU_SEPARATOR = new Region("PopupMenuSeparator", false);
	public static final Region PROGRESS_BAR = new Region("ProgressBar", false);
	public static final Region RADIO_BUTTON = new Region("RadioButton", false);
	public static final Region RADIO_BUTTON_MENU_ITEM = new Region("RadioButtonMenuItem", false);
	public static final Region ROOT_PANE = new Region("RootPane", false);
	public static final Region SCROLL_BAR = new Region("ScrollBar", false);
	public static final Region SCROLL_BAR_TRACK = new Region("ScrollBarTrack", true);
	public static final Region SCROLL_BAR_THUMB = new Region("ScrollBarThumb", true);
	public static final Region SCROLL_PANE = new Region("ScrollPane", false);
	public static final Region SEPARATOR = new Region("Separator", false);
	public static final Region SLIDER = new Region("Slider", false);
	public static final Region SLIDER_TRACK = new Region("SliderTrack", true);
	public static final Region SLIDER_THUMB = new Region("SliderThumb", true);
	public static final Region SPINNER = new Region("Spinner", false);
	public static final Region SPLIT_PANE = new Region("SplitPane", false);
	public static final Region SPLIT_PANE_DIVIDER = new Region("SplitPaneDivider", true);
	public static final Region TABBED_PANE = new Region("TabbedPane", false);
	public static final Region TABBED_PANE_TAB = new Region("TabbedPaneTab", true);
	public static final Region TABBED_PANE_TAB_AREA = new Region("TabbedPaneTabArea", true);
	public static final Region TABBED_PANE_CONTENT = new Region("TabbedPaneContent", true);
	public static final Region TABLE = new Region("Table", false);
	public static final Region TABLE_HEADER = new Region("TableHeader", false);
	public static final Region TEXT_AREA = new Region("TextArea", false);
	public static final Region TEXT_FIELD = new Region("TextField", false);
	public static final Region TEXT_PANE = new Region("TextPane", false);
	public static final Region TOGGLE_BUTTON = new Region("ToggleButton", false);
	public static final Region TOOL_BAR = new Region("ToolBar", false);
	public static final Region TOOL_BAR_CONTENT = new Region("ToolBarContent", true);
	public static final Region TOOL_BAR_DRAG_WINDOW = new Region("ToolBarDragWindow", false);
	public static final Region TOOL_TIP = new Region("ToolTip", false);
	public static final Region TOOL_BAR_SEPARATOR = new Region("ToolBarSeparator", false);
	public static final Region TREE = new Region("Tree", false);
	public static final Region TREE_CELL = new Region("TreeCell", true);
	public static final Region VIEWPORT = new Region("Viewport", false);

	private static Map<String, Region> getUItoRegionMap() {
		return null;
	}

	private static Map<Region, String> getLowerCaseNameMap() {
		return null;
	}

	static Region getRegion(JComponent c) {
		return getUItoRegionMap().get(c.getUIClassID());
	}

	static void registerUIs(UIDefaults table) {
		for (Object key : getUItoRegionMap().keySet()) {
			table.put(key, "javax.swing.plaf.synth.SynthLookAndFeel");
		}
	}

	private final String name;
	private final boolean subregion;

	private Region(String name, boolean subregion) {
		if (name == null) {
			throw new NullPointerException("You must specify a non-null name");
		}
		this.name = name;
		this.subregion = subregion;
	}

	/**
	 * Creates a Region with the specified name. This should only be used if you
	 * are creating your own <code>JComponent</code> subclass with a custom
	 * <code>ComponentUI</code> class.
	 *
	 * @param name
	 *            Name of the region
	 * @param ui
	 *            String that will be returned from
	 *            <code>component.getUIClassID</code>. This will be null if this
	 *            is a subregion.
	 * @param subregion
	 *            Whether or not this is a subregion.
	 */
	protected Region(String name, String ui, boolean subregion) {
		this(name, subregion);
		if (ui != null) {
			getUItoRegionMap().put(ui, this);
		}
	}

	/**
	 * Returns true if the Region is a subregion of a Component, otherwise
	 * false. For example, <code>Region.BUTTON</code> corresponds do a
	 * <code>Component</code> so that <code>Region.BUTTON.isSubregion()</code>
	 * returns false.
	 *
	 * @return true if the Region is a subregion of a Component.
	 */
	public boolean isSubregion() {
		return subregion;
	}

	/**
	 * Returns the name of the region.
	 *
	 * @return name of the Region.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the name, in lowercase.
	 *
	 * @return lower case representation of the name of the Region
	 */
	String getLowerCaseName() {
		Map<Region, String> lowerCaseNameMap = getLowerCaseNameMap();
		String lowerCaseName = lowerCaseNameMap.get(this);
		if (lowerCaseName == null) {
			lowerCaseName = name.toLowerCase(Locale.ENGLISH);
			lowerCaseNameMap.put(this, lowerCaseName);
		}
		return lowerCaseName;
	}

	/**
	 * Returns the name of the Region.
	 *
	 * @return name of the Region.
	 */
	@Override
	public String toString() {
		return name;
	}
}