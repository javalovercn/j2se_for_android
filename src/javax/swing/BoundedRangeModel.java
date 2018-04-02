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

import javax.swing.event.ChangeListener;

/**
 * Defines the data model used by components like <code>Slider</code>s and
 * <code>ProgressBar</code>s. Defines four interrelated integer properties:
 * minimum, maximum, extent and value. These four integers define two nested
 * ranges like this:
 * 
 * <pre>
 * minimum &lt;= value &lt;= value + extent &lt;= maximum
 * </pre>
 * 
 * The outer range is <code>minimum,maximum</code> and the inner range is
 * <code>value,value+extent</code>. The inner range must lie within the outer
 * one, i.e. <code>value</code> must be less than or equal to
 * <code>maximum</code> and <code>value+extent</code> must greater than or equal
 * to <code>minimum</code>, and <code>maximum</code> must be greater than or
 * equal to <code>minimum</code>. There are a few features of this model that
 * one might find a little surprising. These quirks exist for the convenience of
 * the Swing BoundedRangeModel clients, such as <code>Slider</code> and
 * <code>ScrollBar</code>.
 * <ul>
 * <li>The minimum and maximum set methods "correct" the other three properties
 * to accommodate their new value argument. For example setting the model's
 * minimum may change its maximum, value, and extent properties (in that order),
 * to maintain the constraints specified above.
 *
 * <li>The value and extent set methods "correct" their argument to fit within
 * the limits defined by the other three properties. For example if
 * <code>value == maximum</code>, <code>setExtent(10)</code> would change the
 * extent (back) to zero.
 *
 * <li>The four BoundedRangeModel values are defined as Java Beans properties
 * however Swing ChangeEvents are used to notify clients of changes rather than
 * PropertyChangeEvents. This was done to keep the overhead of monitoring a
 * BoundedRangeModel low. Changes are often reported at MouseDragged rates.
 * </ul>
 *
 * <p>
 *
 * For an example of specifying custom bounded range models used by sliders, see
 * <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/overview/anatomy.html">The
 * Anatomy of a Swing-Based Program</a> in <em>The Java Tutorial.</em>
 *
 * @author Hans Muller
 * @see DefaultBoundedRangeModel
 */
public interface BoundedRangeModel {
	int getMinimum();

	void setMinimum(int newMinimum);

	int getMaximum();

	void setMaximum(int newMaximum);

	int getValue();

	void setValue(int newValue);

	void setValueIsAdjusting(boolean b);

	boolean getValueIsAdjusting();

	int getExtent();

	void setExtent(int newExtent);

	void setRangeProperties(int value, int extent, int min, int max, boolean adjusting);

	void addChangeListener(ChangeListener x);

	void removeChangeListener(ChangeListener x);

}