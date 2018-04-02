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
package java.awt.event;

import java.awt.AWTEvent;
import java.awt.Adjustable;

/**
 * The adjustment event emitted by Adjustable objects like
 * {@link java.awt.Scrollbar} and {@link java.awt.ScrollPane}. When the user
 * changes the value of the scrolling component, it receives an instance of
 * {@code AdjustmentEvent}.
 * <p>
 * An unspecified behavior will be caused if the {@code id} parameter of any
 * particular {@code AdjustmentEvent} instance is not in the range from
 * {@code ADJUSTMENT_FIRST} to {@code ADJUSTMENT_LAST}.
 * <p>
 * The {@code type} of any {@code AdjustmentEvent} instance takes one of the
 * following values:
 * <ul>
 * <li>{@code UNIT_INCREMENT}
 * <li>{@code UNIT_DECREMENT}
 * <li>{@code BLOCK_INCREMENT}
 * <li>{@code BLOCK_DECREMENT}
 * <li>{@code TRACK}
 * </ul>
 * Assigning the value different from listed above will cause an unspecified
 * behavior.
 * 
 * @see java.awt.Adjustable
 * @see AdjustmentListener
 *
 * @author Amy Fowler
 * @since 1.1
 */
public class AdjustmentEvent extends AWTEvent {
	public static final int ADJUSTMENT_FIRST = 601;
	public static final int ADJUSTMENT_LAST = 601;
	public static final int ADJUSTMENT_VALUE_CHANGED = ADJUSTMENT_FIRST;
	public static final int UNIT_INCREMENT = 1;
	public static final int UNIT_DECREMENT = 2;
	public static final int BLOCK_DECREMENT = 3;
	public static final int BLOCK_INCREMENT = 4;
	public static final int TRACK = 5;

	Adjustable adjustable;
	int value;
	int adjustmentType;
	boolean isAdjusting;

	public AdjustmentEvent(Adjustable source, int id, int type, int value) {
		this(source, id, type, value, false);
	}

	public AdjustmentEvent(Adjustable source, int id, int type, int value, boolean isAdjusting) {
		super(source, id);
		adjustable = source;
		this.adjustmentType = type;
		this.value = value;
		this.isAdjusting = isAdjusting;
	}

	public Adjustable getAdjustable() {
		return adjustable;
	}

	public int getValue() {
		return value;
	}

	public int getAdjustmentType() {
		return adjustmentType;
	}

	public boolean getValueIsAdjusting() {
		return isAdjusting;
	}

	public String paramString() {
		String typeStr;
		switch (id) {
		case ADJUSTMENT_VALUE_CHANGED:
			typeStr = "ADJUSTMENT_VALUE_CHANGED";
			break;
		default:
			typeStr = "unknown type";
		}
		String adjTypeStr;
		switch (adjustmentType) {
		case UNIT_INCREMENT:
			adjTypeStr = "UNIT_INCREMENT";
			break;
		case UNIT_DECREMENT:
			adjTypeStr = "UNIT_DECREMENT";
			break;
		case BLOCK_INCREMENT:
			adjTypeStr = "BLOCK_INCREMENT";
			break;
		case BLOCK_DECREMENT:
			adjTypeStr = "BLOCK_DECREMENT";
			break;
		case TRACK:
			adjTypeStr = "TRACK";
			break;
		default:
			adjTypeStr = "unknown type";
		}
		return typeStr + ",adjType=" + adjTypeStr + ",value=" + value + ",isAdjusting="
				+ isAdjusting;
	}
}
