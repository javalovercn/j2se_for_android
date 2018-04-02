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
package javax.swing.event;

import java.awt.event.InputEvent;
import java.net.URL;
import java.util.EventObject;

import javax.swing.text.Element;

/**
 * HyperlinkEvent is used to notify interested parties that something has
 * happened with respect to a hypertext link.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author Timothy Prinzing
 */
public class HyperlinkEvent extends EventObject {

	public HyperlinkEvent(Object source, EventType type, URL u) {
		this(source, type, u, null);
	}

	public HyperlinkEvent(Object source, EventType type, URL u, String desc) {
		this(source, type, u, desc, null);
	}

	public HyperlinkEvent(Object source, EventType type, URL u, String desc,
			Element sourceElement) {
		super(source);
		this.type = type;
		this.u = u;
		this.desc = desc;
		this.sourceElement = sourceElement;
	}

	public HyperlinkEvent(Object source, EventType type, URL u, String desc, Element sourceElement,
			InputEvent inputEvent) {
		super(source);
		this.type = type;
		this.u = u;
		this.desc = desc;
		this.sourceElement = sourceElement;
		this.inputEvent = inputEvent;
	}

	public EventType getEventType() {
		return type;
	}

	public String getDescription() {
		return desc;
	}

	public URL getURL() {
		return u;
	}

	public Element getSourceElement() {
		return sourceElement;
	}

	public InputEvent getInputEvent() {
		return inputEvent;
	}

	private EventType type;
	private URL u;
	private String desc;
	private Element sourceElement;
	private InputEvent inputEvent;

	public static final class EventType {

		private EventType(String s) {
			typeString = s;
		}

		public static final EventType ENTERED = new EventType("ENTERED");
		public static final EventType EXITED = new EventType("EXITED");
		public static final EventType ACTIVATED = new EventType("ACTIVATED");

		public String toString() {
			return typeString;
		}

		private String typeString;
	}
}
