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

import hc.android.AndroidClassUtil;

import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.plaf.ToolTipUI;

/**
 * Used to display a "Tip" for a Component. Typically components provide api to
 * automate the process of using <code>ToolTip</code>s. For example, any Swing
 * component can use the <code>JComponent</code> <code>setToolTipText</code>
 * method to specify the text for a standard tooltip. A component that wants to
 * create a custom <code>ToolTip</code> display can override
 * <code>JComponent</code>'s <code>createToolTip</code> method and use a
 * subclass of this class.
 * <p>
 * See <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/tooltip.html">How
 * to Use Tool Tips</a> in <em>The Java Tutorial</em> for further documentation.
 * <p>
 * <strong>Warning:</strong> Swing is not thread safe. For more information see
 * <a href="package-summary.html#threading">Swing's Threading Policy</a>.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @see JComponent#setToolTipText
 * @see JComponent#createToolTip
 * @author Dave Moore
 * @author Rich Shiavi
 */
public class JToolTip extends JComponent implements Accessible {

	public JToolTip() {
	}

	public ToolTipUI getUI() {
		return null;
	}

	public void updateUI() {
	}

	public String getUIClassID() {
		return "";
	}

	public void setTipText(String tipText) {
	}

	public String getTipText() {
		return "";
	}

	public void setComponent(JComponent c) {
	}

	public JComponent getComponent() {
		return null;
	}

	boolean alwaysOnTop() {
		return true;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	protected String paramString() {
		return "";
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

}