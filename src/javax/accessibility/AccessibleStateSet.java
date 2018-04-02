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
 * Class AccessibleStateSet determines a component's state set. The state set of
 * a component is a set of AccessibleState objects and descriptions. E.G., The
 * current overall state of the object, such as whether it is enabled, has
 * focus, etc.
 *
 * @see AccessibleState
 *
 * @author Willie Walker
 */
public class AccessibleStateSet {
	protected Vector<AccessibleState> states = null;

	public AccessibleStateSet() {
		states = null;
	}

	public AccessibleStateSet(AccessibleState[] states) {
		if (states.length != 0) {
			this.states = new Vector(states.length);
			for (int i = 0; i < states.length; i++) {
				if (!this.states.contains(states[i])) {
					this.states.addElement(states[i]);
				}
			}
		}
	}

	public boolean add(AccessibleState state) {
		if (states == null) {
			states = new Vector();
		}

		if (!states.contains(state)) {
			states.addElement(state);
			return true;
		} else {
			return false;
		}
	}

	public void addAll(AccessibleState[] states) {
		if (states.length != 0) {
			if (this.states == null) {
				this.states = new Vector(states.length);
			}
			for (int i = 0; i < states.length; i++) {
				if (!this.states.contains(states[i])) {
					this.states.addElement(states[i]);
				}
			}
		}
	}

	public boolean remove(AccessibleState state) {
		if (states == null) {
			return false;
		} else {
			return states.removeElement(state);
		}
	}

	public void clear() {
		if (states != null) {
			states.removeAllElements();
		}
	}

	public boolean contains(AccessibleState state) {
		if (states == null) {
			return false;
		} else {
			return states.contains(state);
		}
	}

	public AccessibleState[] toArray() {
		if (states == null) {
			return new AccessibleState[0];
		} else {
			AccessibleState[] stateArray = new AccessibleState[states.size()];
			for (int i = 0; i < stateArray.length; i++) {
				stateArray[i] = (AccessibleState) states.elementAt(i);
			}
			return stateArray;
		}
	}

	public String toString() {
		String ret = "AccStateSet : ";
		if ((states != null) && (states.size() > 0)) {
			ret = ((AccessibleState) (states.elementAt(0))).toDisplayString();
			for (int i = 1; i < states.size(); i++) {
				ret = ret + "," + ((AccessibleState) (states.elementAt(i))).toDisplayString();
			}
		}
		return ret;
	}
}
