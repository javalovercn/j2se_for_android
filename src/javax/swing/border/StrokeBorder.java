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
package javax.swing.border;

import hc.android.ActivityManager;
import hc.android.AndroidClassUtil;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Paint;
import android.view.View;

/**
 * A class which implements a border of an arbitrary stroke.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans&trade; has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @author Sergey A. Malenkov
 *
 * @since 1.7
 */
public class StrokeBorder extends AbstractBorder {
	private final BasicStroke stroke;
	private final Paint paint;

	public StrokeBorder(BasicStroke stroke) {
		this(stroke, null);
	}

	public StrokeBorder(BasicStroke stroke, Paint paint) {
		if (stroke == null) {
			throw new NullPointerException("border's stroke");
		}
		this.stroke = stroke;
		this.paint = paint;
		throw new Error(AndroidClassUtil.UN_IMPLEMENT_METHOD);
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	}

	@Override
	public Insets getBorderInsets(Component c, Insets insets) {
		int size = (int) Math.ceil(this.stroke.getLineWidth());
		insets.set(size, size, size, size);
		return insets;
	}

	public BasicStroke getStroke() {
		return this.stroke;
	}

	public Paint getPaint() {
		return this.paint;
	}

	@Override
	public View getBorderViewAdAPI() {
		return null;
	}

	@Override
	public void setComponentViewAdAPI(View view, Component component) {
		// AndroidUIUtil.runOnUiThread(action)
	}
}