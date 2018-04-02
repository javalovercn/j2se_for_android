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
package javax.accessibility;

/**
 * <P>
 * Class AccessibleState describes a component's particular state. The actual
 * state of the component is defined as an AccessibleStateSet, which is a
 * composed set of AccessibleStates.
 * <p>
 * The toDisplayString method allows you to obtain the localized string for a
 * locale independent key from a predefined ResourceBundle for the keys defined
 * in this class.
 * <p>
 * The constants in this class present a strongly typed enumeration of common
 * object roles. A public constructor for this class has been purposely omitted
 * and applications should use one of the constants from this class. If the
 * constants in this class are not sufficient to describe the role of an object,
 * a subclass should be generated from this class and it should provide
 * constants in a similar manner.
 *
 * @author Willie Walker
 * @author Peter Korn
 */
public class AccessibleState extends AccessibleBundle {
	public static final AccessibleState ACTIVE = new AccessibleState("active");
	public static final AccessibleState PRESSED = new AccessibleState("pressed");
	public static final AccessibleState ARMED = new AccessibleState("armed");
	public static final AccessibleState BUSY = new AccessibleState("busy");
	public static final AccessibleState CHECKED = new AccessibleState("checked");
	public static final AccessibleState EDITABLE = new AccessibleState("editable");
	public static final AccessibleState EXPANDABLE = new AccessibleState("expandable");
	public static final AccessibleState COLLAPSED = new AccessibleState("collapsed");
	public static final AccessibleState EXPANDED = new AccessibleState("expanded");
	public static final AccessibleState ENABLED = new AccessibleState("enabled");
	public static final AccessibleState FOCUSABLE = new AccessibleState("focusable");
	public static final AccessibleState FOCUSED = new AccessibleState("focused");
	public static final AccessibleState ICONIFIED = new AccessibleState("iconified");
	public static final AccessibleState MODAL = new AccessibleState("modal");
	public static final AccessibleState OPAQUE = new AccessibleState("opaque");
	public static final AccessibleState RESIZABLE = new AccessibleState("resizable");
	public static final AccessibleState MULTISELECTABLE = new AccessibleState("multiselectable");
	public static final AccessibleState SELECTABLE = new AccessibleState("selectable");
	public static final AccessibleState SELECTED = new AccessibleState("selected");
	public static final AccessibleState SHOWING = new AccessibleState("showing");
	public static final AccessibleState VISIBLE = new AccessibleState("visible");
	public static final AccessibleState VERTICAL = new AccessibleState("vertical");
	public static final AccessibleState HORIZONTAL = new AccessibleState("horizontal");
	public static final AccessibleState SINGLE_LINE = new AccessibleState("singleline");
	public static final AccessibleState MULTI_LINE = new AccessibleState("multiline");
	public static final AccessibleState TRANSIENT = new AccessibleState("transient");
	public static final AccessibleState MANAGES_DESCENDANTS = new AccessibleState(
			"managesDescendants");
	public static final AccessibleState INDETERMINATE = new AccessibleState("indeterminate");
	static public final AccessibleState TRUNCATED = new AccessibleState("truncated");

	protected AccessibleState(String key) {
		this.key = key;
	}
}
