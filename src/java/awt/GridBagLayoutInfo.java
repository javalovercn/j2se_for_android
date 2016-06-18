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
 * The {@code GridBagLayoutInfo} is an utility class for
 * {@code GridBagLayout} layout manager.
 * It stores align, size and baseline parameters for every component within a container.
 * <p>
 * @see       java.awt.GridBagLayout
 * @see       java.awt.GridBagConstraints
 * @since 1.6
 */
public class GridBagLayoutInfo implements java.io.Serializable {
	int width, height;
	int startx, starty;
	int minWidth[];
	int minHeight[];
	double weightX[];
	double weightY[];
	boolean hasBaseline;
	short baselineType[];
	int maxAscent[];
	int maxDescent[];

	GridBagLayoutInfo(int width, int height) {
		this.width = width;
		this.height = height;
	}

	boolean hasConstantDescent(int row) {
		return ((baselineType[row] & (1 << Component.BaselineResizeBehavior.CONSTANT_DESCENT
				.ordinal())) != 0);
	}

	boolean hasBaseline(int row) {
		return (hasBaseline && baselineType[row] != 0);
	}
}