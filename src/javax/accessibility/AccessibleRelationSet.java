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

import java.util.Vector;

/**
 * Class AccessibleRelationSet determines a component's relation set. The
 * relation set of a component is a set of AccessibleRelation objects that
 * describe the component's relationships with other components.
 *
 * @see AccessibleRelation
 *
 * @author Lynn Monsanto
 * @since 1.3
 */
public class AccessibleRelationSet {
	protected Vector<AccessibleRelation> relations = null;

	public AccessibleRelationSet() {
		relations = null;
	}

	public AccessibleRelationSet(AccessibleRelation[] relations) {
		if (relations.length != 0) {
			this.relations = new Vector(relations.length);
			for (int i = 0; i < relations.length; i++) {
				add(relations[i]);
			}
		}
	}

	public boolean add(AccessibleRelation relation) {
		if (relations == null) {
			relations = new Vector();
		}

		AccessibleRelation existingRelation = get(relation.getKey());
		if (existingRelation == null) {
			relations.addElement(relation);
			return true;
		} else {
			Object[] existingTarget = existingRelation.getTarget();
			Object[] newTarget = relation.getTarget();
			int mergedLength = existingTarget.length + newTarget.length;
			Object[] mergedTarget = new Object[mergedLength];
			for (int i = 0; i < existingTarget.length; i++) {
				mergedTarget[i] = existingTarget[i];
			}
			for (int i = existingTarget.length, j = 0; i < mergedLength; i++, j++) {
				mergedTarget[i] = newTarget[j];
			}
			existingRelation.setTarget(mergedTarget);
		}
		return true;
	}

	public void addAll(AccessibleRelation[] relations) {
		if (relations.length != 0) {
			if (this.relations == null) {
				this.relations = new Vector(relations.length);
			}
			for (int i = 0; i < relations.length; i++) {
				add(relations[i]);
			}
		}
	}

	public boolean remove(AccessibleRelation relation) {
		if (relations == null) {
			return false;
		} else {
			return relations.removeElement(relation);
		}
	}

	public void clear() {
		if (relations != null) {
			relations.removeAllElements();
		}
	}

	public int size() {
		if (relations == null) {
			return 0;
		} else {
			return relations.size();
		}
	}

	public boolean contains(String key) {
		return get(key) != null;
	}

	public AccessibleRelation get(String key) {
		if (relations == null) {
			return null;
		} else {
			int len = relations.size();
			for (int i = 0; i < len; i++) {
				AccessibleRelation relation = (AccessibleRelation) relations.elementAt(i);
				if (relation != null && relation.getKey().equals(key)) {
					return relation;
				}
			}
			return null;
		}
	}

	public AccessibleRelation[] toArray() {
		if (relations == null) {
			return new AccessibleRelation[0];
		} else {
			AccessibleRelation[] relationArray = new AccessibleRelation[relations.size()];
			for (int i = 0; i < relationArray.length; i++) {
				relationArray[i] = (AccessibleRelation) relations.elementAt(i);
			}
			return relationArray;
		}
	}

	public String toString() {
		String ret = "";
		return ret;
	}
}
