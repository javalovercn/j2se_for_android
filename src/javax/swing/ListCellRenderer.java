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

import java.awt.Component;

/**
 * Identifies components that can be used as "rubber stamps" to paint the cells
 * in a JList. For example, to use a JLabel as a ListCellRenderer, you would
 * write something like this:
 * 
 * <pre>
 * {
 * 	&#64;code
 * 	class MyCellRenderer extends JLabel implements ListCellRenderer<Object> {
 * 		public MyCellRenderer() {
 * 			setOpaque(true);
 * 		}
 *
 * 		public Component getListCellRendererComponent(JList<?> list, Object value, int index,
 * 				boolean isSelected, boolean cellHasFocus) {
 *
 * 			setText(value.toString());
 *
 * 			Color background;
 * 			Color foreground;
 *
 * 			// check if this cell represents the current DnD drop location
 * 			JList.DropLocation dropLocation = list.getDropLocation();
 * 			if (dropLocation != null && !dropLocation.isInsert()
 * 					&& dropLocation.getIndex() == index) {
 *
 * 				background = Color.BLUE;
 * 				foreground = Color.WHITE;
 *
 * 				// check if this cell is selected
 * 			} else if (isSelected) {
 * 				background = Color.RED;
 * 				foreground = Color.WHITE;
 *
 * 				// unselected, and not the DnD drop location
 * 			} else {
 * 				background = Color.WHITE;
 * 				foreground = Color.BLACK;
 * 			}
 * 			;
 *
 * 			setBackground(background);
 * 			setForeground(foreground);
 *
 * 			return this;
 * 		}
 * 	}
 * }
 * </pre>
 *
 * @param <E>
 *            the type of values this renderer can be used for
 *
 * @see JList
 * @see DefaultListCellRenderer
 *
 * @author Hans Muller
 */
public interface ListCellRenderer<E> {
	Component getListCellRendererComponent(JList<? extends E> list, E value, int index,
			boolean isSelected, boolean cellHasFocus);
}
