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

import java.awt.Component;
import java.awt.Event;
import java.util.Arrays;

/**
 * The root event class for all component-level input events.
 *
 * Input events are delivered to listeners before they are
 * processed normally by the source where they originated.
 * This allows listeners and component subclasses to "consume"
 * the event so that the source will not process them in their
 * default manner.  For example, consuming mousePressed events
 * on a Button component will prevent the Button from being
 * activated.
 *
 * @author Carl Quinn
 *
 * @see KeyEvent
 * @see KeyAdapter
 * @see MouseEvent
 * @see MouseAdapter
 * @see MouseMotionAdapter
 *
 * @since 1.1
 */
public abstract class InputEvent extends ComponentEvent {
    public static final int SHIFT_MASK = Event.SHIFT_MASK;
    public static final int CTRL_MASK = Event.CTRL_MASK;
    public static final int META_MASK = Event.META_MASK;
    public static final int ALT_MASK = Event.ALT_MASK;
    public static final int ALT_GRAPH_MASK = 1 << 5;
    public static final int BUTTON1_MASK = 1 << 4;
    public static final int BUTTON2_MASK = Event.ALT_MASK;
    public static final int BUTTON3_MASK = Event.META_MASK;
    public static final int SHIFT_DOWN_MASK = 1 << 6;
    public static final int CTRL_DOWN_MASK = 1 << 7;
    public static final int META_DOWN_MASK = 1 << 8;
    public static final int ALT_DOWN_MASK = 1 << 9;
    public static final int BUTTON1_DOWN_MASK = 1 << 10;
    public static final int BUTTON2_DOWN_MASK = 1 << 11;
    public static final int BUTTON3_DOWN_MASK = 1 << 12;
    public static final int ALT_GRAPH_DOWN_MASK = 1 << 13;

    private static final int [] BUTTON_DOWN_MASK = new int [] { BUTTON1_DOWN_MASK,
                                                               BUTTON2_DOWN_MASK,
                                                               BUTTON3_DOWN_MASK,
                                                               1<<14, 
                                                               1<<15, 
                                                               1<<16,
                                                               1<<17,
                                                               1<<18,
                                                               1<<19,
                                                               1<<20,
                                                               1<<21,
                                                               1<<22,
                                                               1<<23,
                                                               1<<24,
                                                               1<<25,
                                                               1<<26,
                                                               1<<27,
                                                               1<<28,
                                                               1<<29,
                                                               1<<30};

    private static int [] getButtonDownMasks(){
        return Arrays.copyOf(BUTTON_DOWN_MASK, BUTTON_DOWN_MASK.length);
    }

    public static int getMaskForButton(int button) {
        if (button <= 0 || button > BUTTON_DOWN_MASK.length) {
            throw new IllegalArgumentException("button doesn\'t exist " + button);
        }
        return BUTTON_DOWN_MASK[button - 1];
    }

    static final int FIRST_HIGH_BIT = 1 << 31;

    static final int JDK_1_3_MODIFIERS = SHIFT_DOWN_MASK - 1;
    static final int HIGH_MODIFIERS = ~( FIRST_HIGH_BIT - 1 );

    long when;
    int modifiers;
    private transient boolean canAccessSystemClipboard;

    InputEvent(Component source, int id, long when, int modifiers) {
        super(source, id);
        this.when = when;
        this.modifiers = modifiers;
        canAccessSystemClipboard = canAccessSystemClipboard();
    }

    private boolean canAccessSystemClipboard() {
        return false;
    }

    public boolean isShiftDown() {
        return (modifiers & SHIFT_MASK) != 0;
    }

    public boolean isControlDown() {
        return (modifiers & CTRL_MASK) != 0;
    }

    public boolean isMetaDown() {
        return (modifiers & META_MASK) != 0;
    }

    public boolean isAltDown() {
        return (modifiers & ALT_MASK) != 0;
    }

    public boolean isAltGraphDown() {
        return (modifiers & ALT_GRAPH_MASK) != 0;
    }

    public long getWhen() {
        return when;
    }

    public int getModifiers() {
        return modifiers & (JDK_1_3_MODIFIERS | HIGH_MODIFIERS);
    }

    public int getModifiersEx() {
        return modifiers & ~JDK_1_3_MODIFIERS;
    }

    public void consume() {
        consumed = true;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public static String getModifiersExText(int modifiers) {
        return "";
    }
}
