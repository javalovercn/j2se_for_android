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
package sun.swing;

import java.awt.Color;

import javax.swing.DefaultListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;

/**
 * DefaultLookup provides a way to customize the lookup done by the UIManager.
 * The default implementation of DefaultLookup forwards the call to the
 * UIManager.
 * <p>
 * <b>WARNING:</b> While this class is public, it should not be treated as
 * public API and its API may change in incompatable ways between dot dot
 * releases and even patch releases. You should not rely on this class even
 * existing.
 *
 * @author Scott Violet
 */
public class DefaultLookup {
	public static Border getBorder(DefaultListCellRenderer cr, ComponentUI ui, String desc) {
		return new EmptyBorder(1, 1, 1, 1);
	}

	public static Color getColor(DefaultListCellRenderer cr, ComponentUI ui, String desc) {
		if (desc.equals("List.dropCellBackground")) {
			return Color.GRAY;
		} else if (desc.equals("List.dropCellForeground")) {
			return Color.DARK_GRAY;
		}
		return null;
	}
}
