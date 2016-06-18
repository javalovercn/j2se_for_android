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
package java.awt.font;

/**
* The <code>LineMetrics</code> class allows access to the
* metrics needed to layout characters along a line
* and to layout of a set of lines.  A <code>LineMetrics</code>
* object encapsulates the measurement information associated
* with a run of text.
* <p>
* Fonts can have different metrics for different ranges of
* characters.  The <code>getLineMetrics</code> methods of
* {@link java.awt.Font Font} take some text as an argument
* and return a <code>LineMetrics</code> object describing the
* metrics of the initial number of characters in that text, as
* returned by {@link #getNumChars}.
*/

public abstract class LineMetrics {

    public abstract int getNumChars();

    public abstract float getAscent();

    public abstract float getDescent();

    public abstract float getLeading();

    public abstract float getHeight();

    public abstract int getBaselineIndex();

    public abstract float[] getBaselineOffsets();

    public abstract float getStrikethroughOffset();

    public abstract float getStrikethroughThickness();

    public abstract float getUnderlineOffset();

    public abstract float getUnderlineThickness();
}
