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
import javax.accessibility.AccessibleContext;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import android.text.InputType;

/**
 * <code>JPasswordField</code> is a lightweight component that allows the
 * editing of a single line of text where the view indicates something was
 * typed, but does not show the original characters. You can find further
 * information and examples in <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/textfield.html">How
 * to Use Text Fields</a>, a section in <em>The Java Tutorial.</em>
 * <p>
 * <code>JPasswordField</code> is intended to be source-compatible with
 * <code>java.awt.TextField</code> used with <code>echoChar</code> set. It is
 * provided separately to make it easier to safely change the UI for the
 * <code>JTextField</code> without affecting password entries.
 * <p>
 * <strong>NOTE:</strong> By default, JPasswordField disables input methods;
 * otherwise, input characters could be visible while they were composed using
 * input methods. If an application needs the input methods support, please use
 * the inherited method, <code>enableInputMethods(true)</code>.
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
 * @beaninfo attribute: isContainer false description: Allows the editing of a
 *           line of text but doesn't show the characters.
 *
 * @author Timothy Prinzing
 */
public class JPasswordField extends JTextField {
	public JPasswordField() {
		this(null, null, 0);
	}

	public JPasswordField(String text) {
		this(null, text, 0);
	}

	public JPasswordField(int columns) {
		this(null, null, columns);
	}

	public JPasswordField(String text, int columns) {
		this(null, text, columns);
	}

	public JPasswordField(Document doc, String txt, int columns) {
		super(null, txt, columns);
		editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public void updateUI() {
		super.updateUI();
	}

	public char getEchoChar() {
		return echoChar;
	}

	public void setEchoChar(char c) {
		echoChar = c;
		echoCharSet = true;
		repaint();
		revalidate();
	}

	public boolean echoCharIsSet() {
		return echoChar != 0;
	}

	public void cut() {
		super.cut();
	}

	public void copy() {
		super.copy();
	}

	@Deprecated
	public String getText() {
		return super.getText();
	}

	@Deprecated
	public String getText(int offs, int len) throws BadLocationException {
		return super.getText(offs, len);
	}

	public char[] getPassword() {
		String pwd = getText();
		return pwd.toCharArray();
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private static final String uiClassID = "PasswordFieldUI";

	private char echoChar = '*';

	private boolean echoCharSet = false;

	protected String paramString() {
		return super.paramString() + ",echoChar=" + echoChar;
	}

	boolean customSetUIProperty(String propertyName, Object value) {
		if (propertyName == "echoChar") {
			if (!echoCharSet) {
				setEchoChar((Character) value);
				echoCharSet = false;
			}
			return true;
		}
		return false;
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

}