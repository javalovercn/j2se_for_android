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
package java.awt.im;

import java.awt.Rectangle;
import java.awt.font.TextHitInfo;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;

/**
 * InputMethodRequests defines the requests that a text editing component
 * has to handle in order to work with input methods. The component
 * can implement this interface itself or use a separate object that
 * implements it. The object implementing this interface must be returned
 * from the component's getInputMethodRequests method.
 *
 * <p>
 * The text editing component also has to provide an input method event
 * listener.
 *
 * <p>
 * The interface is designed to support one of two input user interfaces:
 * <ul>
 * <li><em>on-the-spot</em> input, where the composed text is displayed as part
 *     of the text component's text body.
 * <li><em>below-the-spot</em> input, where the composed text is displayed in
 *     a separate composition window just below the insertion point where
 *     the text will be inserted when it is committed. Note that, if text is
 *     selected within the component's text body, this text will be replaced by
 *     the committed text upon commitment; therefore it is not considered part
 *     of the context that the text is input into.
 * </ul>
 *
 * @see java.awt.Component#getInputMethodRequests
 * @see java.awt.event.InputMethodListener
 *
 * @author JavaSoft Asia/Pacific
 * @since 1.2
 */

public interface InputMethodRequests {

    Rectangle getTextLocation(TextHitInfo offset);

    TextHitInfo getLocationOffset(int x, int y);

    int getInsertPositionOffset();

    AttributedCharacterIterator getCommittedText(int beginIndex, int endIndex,
                                                 Attribute[] attributes);

    int getCommittedTextLength();

    AttributedCharacterIterator cancelLatestCommittedText(Attribute[] attributes);

    AttributedCharacterIterator getSelectedText(Attribute[] attributes);
}

