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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

import javax.swing.Action;
import javax.swing.JEditorPane;

/**
 * Establishes the set of things needed by a text component to be a reasonably
 * functioning editor for some <em>type</em> of text content. The EditorKit acts
 * as a factory for some kind of policy. For example, an implementation of html
 * and rtf can be provided that is replaceable with other implementations.
 * <p>
 * A kit can safely store editing state as an instance of the kit will be
 * dedicated to a text component. New kits will normally be created by cloning a
 * prototype kit. The kit will have it's <code>setComponent</code> method called
 * to establish it's relationship with a JTextComponent.
 *
 * @author Timothy Prinzing
 */
public abstract class EditorKit implements Cloneable, Serializable {

	public EditorKit() {
	}

	public Object clone() {
		Object o;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException cnse) {
			o = null;
		}
		return o;
	}

	public void install(JEditorPane c) {
	}

	public void deinstall(JEditorPane c) {
	}

	public abstract String getContentType();

	public abstract ViewFactory getViewFactory();

	public abstract Action[] getActions();

	public abstract Caret createCaret();

	public abstract Document createDefaultDocument();

	public abstract void read(InputStream in, Document doc, int pos)
			throws IOException, BadLocationException;

	public abstract void write(OutputStream out, Document doc, int pos, int len)
			throws IOException, BadLocationException;

	public abstract void read(Reader in, Document doc, int pos)
			throws IOException, BadLocationException;

	public abstract void write(Writer out, Document doc, int pos, int len)
			throws IOException, BadLocationException;

}
