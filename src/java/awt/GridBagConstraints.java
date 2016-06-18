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

/**
 * The <code>GridBagConstraints</code> class specifies constraints
 * for components that are laid out using the
 * <code>GridBagLayout</code> class.
 *
 * @author Doug Stein
 * @author Bill Spitzak (orignial NeWS & OLIT implementation)
 * @see java.awt.GridBagLayout
 * @since JDK1.0
 */
public class GridBagConstraints implements Cloneable, java.io.Serializable {

	/**
	 * 如果gridx与gridy都是RELATIVE，则组件会被放置在一行。<br>
	 * 如果gridx是RELATIVE而gridy是常量，组件会被放置在一行，位于前一个组件的右边。<br>
	 * 如果grix是常量而gridy为RELATIVE，则组件会被放置在一列，位于前一个组件的下边。<br>
	 * 当设置其他域RELATIVE时变化gridx与gridy会开始新的一行显示，将组件作为新行的第一个元素。<br>
	 * gridwidth与gridheight的值也可以是RELATIVE，这会强制组件成为行或是列中相邻最后一个组件的组件。
	 */
	public static final int RELATIVE = -1;

	/**
	 * 如果gridwidth或是gridheight被设置为REMAINDER，组件将是行或是列中占用剩余空间的最后一个元素。<br>
	 * 例如，对于最右边列的组件，gridwidth值可以为REMAINDER。<br>
	 * 类似的，对于位于底部行中的组件gridheight也可以设置为REMAINDER。
	 */
	public static final int REMAINDER = 0;

	public static final int NONE = 0;
	public static final int BOTH = 1;
	public static final int HORIZONTAL = 2;
	public static final int VERTICAL = 3;

	public static final int CENTER = 10;
	public static final int NORTH = 11;
	public static final int NORTHEAST = 12;
	public static final int EAST = 13;
	public static final int SOUTHEAST = 14;
	public static final int SOUTH = 15;
	public static final int SOUTHWEST = 16;
	public static final int WEST = 17;
	public static final int NORTHWEST = 18;

	public static final int PAGE_START = 19;
	public static final int PAGE_END = 20;
	public static final int LINE_START = 21;
	public static final int LINE_END = 22;
	public static final int FIRST_LINE_START = 23;
	public static final int FIRST_LINE_END = 24;
	public static final int LAST_LINE_START = 25;
	public static final int LAST_LINE_END = 26;
	public static final int BASELINE = 0x100;
	public static final int BASELINE_LEADING = 0x200;
	public static final int BASELINE_TRAILING = 0x300;
	public static final int ABOVE_BASELINE = 0x400;
	public static final int ABOVE_BASELINE_LEADING = 0x500;
	public static final int ABOVE_BASELINE_TRAILING = 0x600;
	public static final int BELOW_BASELINE = 0x700;
	public static final int BELOW_BASELINE_LEADING = 0x800;
	public static final int BELOW_BASELINE_TRAILING = 0x900;

	public int gridx;
	public int gridy;
	public int gridwidth;
	public int gridheight;
	/**
	 * 如果weightx为0.0，则该组件不会获得该行上的额外可用空间。<br>
	 * 如果一行中的一个或是多个组件具有一个正的weightx，则额外空间会在这些组件之间按比例分配。<br>
	 * 例如，如果一个组件的weightx值为1.0，而其他的组件全部为0.0，则该组件会获得全部的额外空间。<br>
	 * 如果一行中的四个组件每一个的weightx的值都为1.0，而其他组件的weightx的值为0.0，则四个组件中的每一个会获得额外空间的四分之一。
	 */
	public double weightx;
	public double weighty;
	public int anchor;
	public int fill;

	public Insets insets;

	public int ipadx;
	public int ipady;

	int tempX;
	int tempY;
	int tempWidth;
	int tempHeight;
	int minWidth;
	int minHeight;

	transient int ascent;
	transient int descent;
	transient Component.BaselineResizeBehavior baselineResizeBehavior;
	transient int centerPadding;
	transient int centerOffset;

	public GridBagConstraints() {
		gridx = RELATIVE;
		gridy = RELATIVE;
		gridwidth = 1;
		gridheight = 1;

		weightx = 0;
		weighty = 0;
		anchor = CENTER;
		fill = NONE;

		insets = new Insets(0, 0, 0, 0);
		ipadx = 0;
		ipady = 0;
	}

	public GridBagConstraints(int gridx, int gridy, int gridwidth,
			int gridheight, double weightx, double weighty, int anchor,
			int fill, Insets insets, int ipadx, int ipady) {
		this.gridx = gridx;
		this.gridy = gridy;
		this.gridwidth = gridwidth;
		this.gridheight = gridheight;
		this.fill = fill;
		this.ipadx = ipadx;
		this.ipady = ipady;
		this.insets = insets;
		this.anchor = anchor;
		this.weightx = weightx;
		this.weighty = weighty;
	}

	public Object clone() {
		GridBagConstraints c = new GridBagConstraints(gridx, gridy, gridwidth,
				gridheight, weightx, weighty, anchor, fill, insets, ipadx,
				ipady);
		c.insets = (Insets) insets.clone();
		return c;
	}

	boolean isVerticallyResizable() {
		return (fill == BOTH || fill == VERTICAL);
	}
}
