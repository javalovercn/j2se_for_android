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
package java.awt;

/**
 * EventDispatchThread is a package-private AWT class which takes events off the
 * EventQueue and dispatches them to the appropriate AWT components.
 *
 * The Thread starts a "permanent" event pump with a call to
 * pumpEvents(Conditional) in its run() method. Event handlers can choose to
 * block this event pump at any time, but should start a new pump (<b>not</b> a
 * new EventDispatchThread) by again calling pumpEvents(Conditional). This
 * secondary event pump will exit automatically as soon as the Condtional
 * evaluate()s to false and an additional Event is pumped and dispatched.
 *
 * @author Tom Ball
 * @author Amy Fowler
 * @author Fred Ecks
 * @author David Mendenhall
 *
 * @since 1.1
 */
class EventDispatchThread extends Thread {
	private EventQueue theQueue;
	private volatile boolean doDispatch = true;

	private static final int ANY_EVENT = -1;

	EventDispatchThread(ThreadGroup group, String name, EventQueue queue) {
		super(group, name);
		setEventQueue(queue);
	}

	public void stopDispatching() {
		doDispatch = false;
	}

	public void run() {
		while (true) {
			try {
				AWTEvent event;
				synchronized (theQueue) {
					event = theQueue.getNextEventPrivate();

					if (event == null) {
						theQueue.wait();
						continue;
					}
				}

				theQueue.dispatchEvent(event);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	void pumpEvents(Conditional cond) {
		pumpEvents(ANY_EVENT, cond);
	}

	void pumpEventsForHierarchy(Conditional cond, Component modalComponent) {
		pumpEventsForHierarchy(ANY_EVENT, cond, modalComponent);
	}

	void pumpEvents(int id, Conditional cond) {
		pumpEventsForHierarchy(id, cond, null);
	}

	void pumpEventsForHierarchy(int id, Conditional cond, Component modalComponent) {
	}

	void pumpEventsForFilter(Conditional cond, EventFilter filter) {
		pumpEventsForFilter(ANY_EVENT, cond, filter);
	}

	void pumpEventsForFilter(int id, Conditional cond, EventFilter filter) {
		addEventFilter(filter);
		doDispatch = true;
		while (doDispatch && !isInterrupted() && cond.evaluate()) {
			pumpOneEventForFilters(id);
		}
		removeEventFilter(filter);
	}

	void addEventFilter(EventFilter filter) {
	}

	void removeEventFilter(EventFilter filter) {
	}

	void pumpOneEventForFilters(int id) {
	}

	private void processException(Throwable e) {
		getUncaughtExceptionHandler().uncaughtException(this, e);
	}

	public synchronized EventQueue getEventQueue() {
		return theQueue;
	}

	public synchronized void setEventQueue(EventQueue eq) {
		theQueue = eq;
	}

}
