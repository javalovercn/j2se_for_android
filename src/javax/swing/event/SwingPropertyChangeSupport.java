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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

/**
 * This subclass of {@code java.beans.PropertyChangeSupport} is almost identical
 * in functionality. The only difference is if constructed with
 * {@code SwingPropertyChangeSupport(sourceBean, true)} it ensures listeners are
 * only ever notified on the <i>Event Dispatch Thread</i>.
 *
 * @author Igor Kushnirskiy
 */
public final class SwingPropertyChangeSupport extends PropertyChangeSupport {
	public SwingPropertyChangeSupport(Object sourceBean) {
		this(sourceBean, false);
	}

	public SwingPropertyChangeSupport(Object sourceBean, boolean notifyOnEDT) {
		super(sourceBean);
		this.notifyOnEDT = notifyOnEDT;
	}

	public void firePropertyChange(final PropertyChangeEvent evt) {
	}

	public final boolean isNotifyOnEDT() {
		return notifyOnEDT;
	}

	private final boolean notifyOnEDT;
}