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
 * Class AccessibleRelation describes a relation between the object that
 * implements the AccessibleRelation and one or more other objects. The actual
 * relations that an object has with other objects are defined as an
 * AccessibleRelationSet, which is a composed set of AccessibleRelations.
 * <p>
 * The toDisplayString method allows you to obtain the localized string for a
 * locale independent key from a predefined ResourceBundle for the keys defined
 * in this class.
 * <p>
 * The constants in this class present a strongly typed enumeration of common
 * object roles. If the constants in this class are not sufficient to describe
 * the role of an object, a subclass should be generated from this class and it
 * should provide constants in a similar manner.
 *
 * @author Lynn Monsanto
 * @since 1.3
 */
public class AccessibleRelation extends AccessibleBundle {
	private Object[] target = new Object[0];

	public static final String LABEL_FOR = new String("labelFor");
	public static final String LABELED_BY = new String("labeledBy");
	public static final String MEMBER_OF = new String("memberOf");
	public static final String CONTROLLER_FOR = new String("controllerFor");
	public static final String CONTROLLED_BY = new String("controlledBy");
	public static final String FLOWS_TO = "flowsTo";
	public static final String FLOWS_FROM = "flowsFrom";
	public static final String SUBWINDOW_OF = "subwindowOf";
	public static final String PARENT_WINDOW_OF = "parentWindowOf";
	public static final String EMBEDS = "embeds";
	public static final String EMBEDDED_BY = "embeddedBy";
	public static final String CHILD_NODE_OF = "childNodeOf";
	public static final String LABEL_FOR_PROPERTY = "labelForProperty";
	public static final String LABELED_BY_PROPERTY = "labeledByProperty";
	public static final String MEMBER_OF_PROPERTY = "memberOfProperty";
	public static final String CONTROLLER_FOR_PROPERTY = "controllerForProperty";
	public static final String CONTROLLED_BY_PROPERTY = "controlledByProperty";
	public static final String FLOWS_TO_PROPERTY = "flowsToProperty";
	public static final String FLOWS_FROM_PROPERTY = "flowsFromProperty";
	public static final String SUBWINDOW_OF_PROPERTY = "subwindowOfProperty";
	public static final String PARENT_WINDOW_OF_PROPERTY = "parentWindowOfProperty";
	public static final String EMBEDS_PROPERTY = "embedsProperty";
	public static final String EMBEDDED_BY_PROPERTY = "embeddedByProperty";
	public static final String CHILD_NODE_OF_PROPERTY = "childNodeOfProperty";

	public AccessibleRelation(String key) {
		this.key = key;
		this.target = null;
	}

	public AccessibleRelation(String key, Object target) {
		this.key = key;
		this.target = new Object[1];
		this.target[0] = target;
	}

	public AccessibleRelation(String key, Object[] target) {
		this.key = key;
		this.target = target;
	}

	public String getKey() {
		return this.key;
	}

	public Object[] getTarget() {
		if (target == null) {
			target = new Object[0];
		}
		Object[] retval = new Object[target.length];
		for (int i = 0; i < target.length; i++) {
			retval[i] = target[i];
		}
		return retval;
	}

	public void setTarget(Object target) {
		this.target = new Object[1];
		this.target[0] = target;
	}

	public void setTarget(Object[] target) {
		this.target = target;
	}
}