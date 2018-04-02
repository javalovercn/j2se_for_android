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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * A package-private PropertyChangeListener which listens for property changes
 * on an Action and updates the properties of an ActionEvent source.
 * <p>
 * Subclasses must override the actionPropertyChanged method, which is invoked
 * from the propertyChange method as long as the target is still valid.
 * </p>
 * <p>
 * WARNING WARNING WARNING WARNING WARNING WARNING:<br>
 * Do NOT create an annonymous inner class that extends this! If you do a strong
 * reference will be held to the containing class, which in most cases defeats
 * the purpose of this class.
 *
 * @param T
 *            the type of JComponent the underlying Action is attached to
 *
 * @author Georges Saab
 * @see AbstractButton
 */
abstract class ActionPropertyChangeListener<T extends JComponent>
		implements PropertyChangeListener, Serializable {
	private static ReferenceQueue<JComponent> queue;

	private transient OwnedWeakReference<T> target;
	private Action action;

	private static ReferenceQueue<JComponent> getQueue() {
		synchronized (ActionPropertyChangeListener.class) {
			if (queue == null) {
				queue = new ReferenceQueue<JComponent>();
			}
		}
		return queue;
	}

	public ActionPropertyChangeListener(T c, Action a) {
		super();
		setTarget(c);
		this.action = a;
	}

	public final void propertyChange(PropertyChangeEvent e) {
		T target = getTarget();
		if (target == null) {
			getAction().removePropertyChangeListener(this);
		} else {
			actionPropertyChanged(target, getAction(), e);
		}
	}

	protected abstract void actionPropertyChanged(T target, Action action, PropertyChangeEvent e);

	private void setTarget(T c) {
		ReferenceQueue<JComponent> queue = getQueue();
		OwnedWeakReference r;
		while ((r = (OwnedWeakReference) queue.poll()) != null) {
			ActionPropertyChangeListener oldPCL = r.getOwner();
			Action oldAction = oldPCL.getAction();
			if (oldAction != null) {
				oldAction.removePropertyChangeListener(oldPCL);
			}
		}
		this.target = new OwnedWeakReference<T>(c, queue, this);
	}

	public T getTarget() {
		if (target == null) {
			return null;
		}
		return this.target.get();
	}

	public Action getAction() {
		return action;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
		s.writeObject(getTarget());
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		s.defaultReadObject();
		T target = (T) s.readObject();
		if (target != null) {
			setTarget(target);
		}
	}

	private static class OwnedWeakReference<U extends JComponent> extends WeakReference<U> {
		private ActionPropertyChangeListener owner;

		OwnedWeakReference(U target, ReferenceQueue<? super U> queue,
				ActionPropertyChangeListener owner) {
			super(target, queue);
			this.owner = owner;
		}

		public ActionPropertyChangeListener getOwner() {
			return owner;
		}
	}
}
