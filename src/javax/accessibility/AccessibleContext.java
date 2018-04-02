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

import java.awt.IllegalComponentStateException;
import java.beans.PropertyChangeListener;
import java.util.Locale;

/**
 * AccessibleContext represents the minimum information all accessible objects
 * return. This information includes the accessible name, description, role, and
 * state of the object, as well as information about its parent and children.
 * AccessibleContext also contains methods for obtaining more specific
 * accessibility information about a component. If the component supports them,
 * these methods will return an object that implements one or more of the
 * following interfaces:
 * <P>
 * <ul>
 * <li>{@link AccessibleAction} - the object can perform one or more actions.
 * This interface provides the standard mechanism for an assistive technology to
 * determine what those actions are and tell the object to perform them. Any
 * object that can be manipulated should support this interface.
 * <li>{@link AccessibleComponent} - the object has a graphical representation.
 * This interface provides the standard mechanism for an assistive technology to
 * determine and set the graphical representation of the object. Any object that
 * is rendered on the screen should support this interface.
 * <li>{@link AccessibleSelection} - the object allows its children to be
 * selected. This interface provides the standard mechanism for an assistive
 * technology to determine the currently selected children of the object as well
 * as modify its selection set. Any object that has children that can be
 * selected should support this interface.
 * <li>{@link AccessibleText} - the object presents editable textual information
 * on the display. This interface provides the standard mechanism for an
 * assistive technology to access that text via its content, attributes, and
 * spatial location. Any object that contains editable text should support this
 * interface.
 * <li>{@link AccessibleValue} - the object supports a numerical value. This
 * interface provides the standard mechanism for an assistive technology to
 * determine and set the current value of the object, as well as obtain its
 * minimum and maximum values. Any object that supports a numerical value should
 * support this interface.
 * </ul>
 *
 *
 * @beaninfo attribute: isContainer false description: Minimal information that
 *           all accessible objects return
 *
 * 
 * @author Peter Korn
 * @author Hans Muller
 * @author Willie Walker
 * @author Lynn Monsanto
 */
public abstract class AccessibleContext {
	public static final String ACCESSIBLE_NAME_PROPERTY = "AccessibleName";
	public static final String ACCESSIBLE_DESCRIPTION_PROPERTY = "AccessibleDescription";
	public static final String ACCESSIBLE_STATE_PROPERTY = "AccessibleState";
	public static final String ACCESSIBLE_VALUE_PROPERTY = "AccessibleValue";
	public static final String ACCESSIBLE_SELECTION_PROPERTY = "AccessibleSelection";
	public static final String ACCESSIBLE_CARET_PROPERTY = "AccessibleCaret";
	public static final String ACCESSIBLE_VISIBLE_DATA_PROPERTY = "AccessibleVisibleData";
	public static final String ACCESSIBLE_CHILD_PROPERTY = "AccessibleChild";
	public static final String ACCESSIBLE_ACTIVE_DESCENDANT_PROPERTY = "AccessibleActiveDescendant";
	public static final String ACCESSIBLE_TABLE_CAPTION_CHANGED = "accessibleTableCaptionChanged";
	public static final String ACCESSIBLE_TABLE_SUMMARY_CHANGED = "accessibleTableSummaryChanged";
	public static final String ACCESSIBLE_TABLE_MODEL_CHANGED = "accessibleTableModelChanged";
	public static final String ACCESSIBLE_TABLE_ROW_HEADER_CHANGED = "accessibleTableRowHeaderChanged";
	public static final String ACCESSIBLE_TABLE_ROW_DESCRIPTION_CHANGED = "accessibleTableRowDescriptionChanged";
	public static final String ACCESSIBLE_TABLE_COLUMN_HEADER_CHANGED = "accessibleTableColumnHeaderChanged";
	public static final String ACCESSIBLE_TABLE_COLUMN_DESCRIPTION_CHANGED = "accessibleTableColumnDescriptionChanged";
	public static final String ACCESSIBLE_ACTION_PROPERTY = "accessibleActionProperty";
	public static final String ACCESSIBLE_HYPERTEXT_OFFSET = "AccessibleHypertextOffset";
	public static final String ACCESSIBLE_TEXT_PROPERTY = "AccessibleText";
	public static final String ACCESSIBLE_INVALIDATE_CHILDREN = "accessibleInvalidateChildren";
	public static final String ACCESSIBLE_TEXT_ATTRIBUTES_CHANGED = "accessibleTextAttributesChanged";
	public static final String ACCESSIBLE_COMPONENT_BOUNDS_CHANGED = "accessibleComponentBoundsChanged";

	protected String accessibleName = null;
	protected String accessibleDescription = null;

	public String getAccessibleName() {
		return accessibleName;
	}

	public void setAccessibleName(String s) {
		String oldName = accessibleName;
		accessibleName = s;
		firePropertyChange(ACCESSIBLE_NAME_PROPERTY, oldName, accessibleName);
	}

	public String getAccessibleDescription() {
		return accessibleDescription;
	}

	public void setAccessibleDescription(String s) {
		String oldDescription = accessibleDescription;
		accessibleDescription = s;
		firePropertyChange(ACCESSIBLE_DESCRIPTION_PROPERTY, oldDescription, accessibleDescription);
	}

	public abstract AccessibleRole getAccessibleRole();

	public abstract AccessibleStateSet getAccessibleStateSet();

	public Accessible getAccessibleParent() {
		return null;
	}

	public void setAccessibleParent(Accessible a) {
	}

	public abstract int getAccessibleIndexInParent();

	public abstract int getAccessibleChildrenCount();

	public abstract Accessible getAccessibleChild(int i);

	public abstract Locale getLocale() throws IllegalComponentStateException;

	public void addPropertyChangeListener(PropertyChangeListener listener) {
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
	}

	public AccessibleAction getAccessibleAction() {
		return null;
	}

	public AccessibleComponent getAccessibleComponent() {
		return null;
	}

	public AccessibleSelection getAccessibleSelection() {
		return null;
	}

	public AccessibleText getAccessibleText() {
		return null;
	}

	public AccessibleEditableText getAccessibleEditableText() {
		return null;
	}

	public AccessibleValue getAccessibleValue() {
		return null;
	}

	public AccessibleIcon[] getAccessibleIcon() {
		return null;
	}

	public AccessibleRelationSet getAccessibleRelationSet() {
		return null;
	}

	public AccessibleTable getAccessibleTable() {
		return null;
	}

	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
	}
}
