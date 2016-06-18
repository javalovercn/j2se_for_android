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
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.font.TextHitInfo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.AttributedCharacterIterator;

/**
 * Input method events contain information about text that is being
 * composed using an input method. Whenever the text changes, the
 * input method sends an event. If the text component that's currently
 * using the input method is an active client, the event is dispatched
 * to that component. Otherwise, it is dispatched to a separate
 * composition window.
 *
 * <p>
 * The text included with the input method event consists of two parts:
 * committed text and composed text. Either part may be empty. The two
 * parts together replace any uncommitted composed text sent in previous events,
 * or the currently selected committed text.
 * Committed text should be integrated into the text component's persistent
 * data, it will not be sent again. Composed text may be sent repeatedly,
 * with changes to reflect the user's editing operations. Committed text
 * always precedes composed text.
 *
 * @author JavaSoft Asia/Pacific
 * @since 1.2
 */
public class InputMethodEvent extends AWTEvent {

    public static final int INPUT_METHOD_FIRST = 1100;

    public static final int INPUT_METHOD_TEXT_CHANGED = INPUT_METHOD_FIRST;
    public static final int CARET_POSITION_CHANGED = INPUT_METHOD_FIRST + 1;
    public static final int INPUT_METHOD_LAST = INPUT_METHOD_FIRST + 1;

    long when;

    public InputMethodEvent(Component source, int id, long when,
            AttributedCharacterIterator text, int committedCharacterCount,
            TextHitInfo caret, TextHitInfo visiblePosition) {
        super(source, id);
        this.when = when;
   }

    public InputMethodEvent(Component source, int id,
            AttributedCharacterIterator text, int committedCharacterCount,
            TextHitInfo caret, TextHitInfo visiblePosition) {
        this(source, id, EventQueue.getMostRecentEventTime(), text,
             committedCharacterCount, caret, visiblePosition);
    }

    public InputMethodEvent(Component source, int id, TextHitInfo caret,
            TextHitInfo visiblePosition) {
        this(source, id, EventQueue.getMostRecentEventTime(), null,
             0, caret, visiblePosition);
    }

    public AttributedCharacterIterator getText() {
        return null;
    }

    public int getCommittedCharacterCount() {
        return 0;
    }

    public TextHitInfo getCaret() {
        return null;
    }

    public TextHitInfo getVisiblePosition() {
        return null;
    }

    public void consume() {
        consumed = true;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public long getWhen() {
      return when;
    }

    public String paramString() {
        return "";
    }

    private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
    }
}
