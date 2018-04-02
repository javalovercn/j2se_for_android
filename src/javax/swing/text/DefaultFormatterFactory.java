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
package javax.swing.text;

import java.io.Serializable;

import javax.swing.JFormattedTextField;

/**
 * An implementation of
 * <code>JFormattedTextField.AbstractFormatterFactory</code>.
 * <code>DefaultFormatterFactory</code> allows specifying a number of different
 * <code>JFormattedTextField.AbstractFormatter</code>s that are to be used. The
 * most important one is the default one (<code>setDefaultFormatter</code>). The
 * default formatter will be used if a more specific formatter could not be
 * found. The following process is used to determine the appropriate formatter
 * to use.
 * <ol>
 * <li>Is the passed in value null? Use the null formatter.
 * <li>Does the <code>JFormattedTextField</code> have focus? Use the edit
 * formatter.
 * <li>Otherwise, use the display formatter.
 * <li>If a non-null <code>AbstractFormatter</code> has not been found, use the
 * default formatter.
 * </ol>
 * <p>
 * The following code shows how to configure a <code>JFormattedTextField</code>
 * with two <code>JFormattedTextField.AbstractFormatter</code>s, one for display
 * and one for editing.
 * 
 * <pre>
 * JFormattedTextField.AbstractFormatter editFormatter = ...;
 * JFormattedTextField.AbstractFormatter displayFormatter = ...;
 * DefaultFormatterFactory factory = new DefaultFormatterFactory(
 *                 displayFormatter, displayFormatter, editFormatter);
 * JFormattedTextField tf = new JFormattedTextField(factory);
 * </pre>
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @see javax.swing.JFormattedTextField
 *
 * @since 1.4
 */
public class DefaultFormatterFactory extends JFormattedTextField.AbstractFormatterFactory
		implements Serializable {
	private JFormattedTextField.AbstractFormatter defaultFormat;

	private JFormattedTextField.AbstractFormatter displayFormat;

	private JFormattedTextField.AbstractFormatter editFormat;

	private JFormattedTextField.AbstractFormatter nullFormat;

	public DefaultFormatterFactory() {
	}

	public DefaultFormatterFactory(JFormattedTextField.AbstractFormatter defaultFormat) {
		this(defaultFormat, null);
	}

	public DefaultFormatterFactory(JFormattedTextField.AbstractFormatter defaultFormat,
			JFormattedTextField.AbstractFormatter displayFormat) {
		this(defaultFormat, displayFormat, null);
	}

	public DefaultFormatterFactory(JFormattedTextField.AbstractFormatter defaultFormat,
			JFormattedTextField.AbstractFormatter displayFormat,
			JFormattedTextField.AbstractFormatter editFormat) {
		this(defaultFormat, displayFormat, editFormat, null);
	}

	public DefaultFormatterFactory(JFormattedTextField.AbstractFormatter defaultFormat,
			JFormattedTextField.AbstractFormatter displayFormat,
			JFormattedTextField.AbstractFormatter editFormat,
			JFormattedTextField.AbstractFormatter nullFormat) {
		this.defaultFormat = defaultFormat;
		this.displayFormat = displayFormat;
		this.editFormat = editFormat;
		this.nullFormat = nullFormat;
	}

	public void setDefaultFormatter(JFormattedTextField.AbstractFormatter atf) {
		defaultFormat = atf;
	}

	public JFormattedTextField.AbstractFormatter getDefaultFormatter() {
		return defaultFormat;
	}

	public void setDisplayFormatter(JFormattedTextField.AbstractFormatter atf) {
		displayFormat = atf;
	}

	public JFormattedTextField.AbstractFormatter getDisplayFormatter() {
		return displayFormat;
	}

	public void setEditFormatter(JFormattedTextField.AbstractFormatter atf) {
		editFormat = atf;
	}

	public JFormattedTextField.AbstractFormatter getEditFormatter() {
		return editFormat;
	}

	public void setNullFormatter(JFormattedTextField.AbstractFormatter atf) {
		nullFormat = atf;
	}

	public JFormattedTextField.AbstractFormatter getNullFormatter() {
		return nullFormat;
	}

	public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField source) {
		JFormattedTextField.AbstractFormatter format = null;

		if (source == null) {
			return null;
		}
		Object value = source.getValue();

		if (value == null) {
			format = getNullFormatter();
		}
		if (format == null) {
			if (source.hasFocus()) {
				format = getEditFormatter();
			} else {
				format = getDisplayFormatter();
			}
			if (format == null) {
				format = getDefaultFormatter();
			}
		}
		return format;
	}
}