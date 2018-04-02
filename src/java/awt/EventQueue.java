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

import hc.android.AndroidClassUtil;
import hc.core.ContextManager;
import hc.core.util.LogManager;
import hc.core.util.Stack;
import java.awt.event.InvocationEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.EmptyStackException;

/**
 * <code>EventQueue</code> is a platform-independent class that queues events,
 * both from the underlying peer classes and from trusted application classes.
 * <p>
 * It encapsulates asynchronous event dispatch machinery which extracts events
 * from the queue and dispatches them by calling {@link #dispatchEvent(AWTEvent)
 * dispatchEvent(AWTEvent)} method on this <code>EventQueue</code> with the
 * event to be dispatched as an argument. The particular behavior of this
 * machinery is implementation-dependent. The only requirements are that events
 * which were actually enqueued to this queue (note that events being posted to
 * the <code>EventQueue</code> can be coalesced) are dispatched:
 * <dl>
 * <dt>Sequentially.
 * <dd>That is, it is not permitted that several events from this queue are
 * dispatched simultaneously.
 * <dt>In the same order as they are enqueued.
 * <dd>That is, if <code>AWTEvent</code>&nbsp;A is enqueued to the
 * <code>EventQueue</code> before <code>AWTEvent</code>&nbsp;B then event B will
 * not be dispatched before event A.
 * </dl>
 * <p>
 * Some browsers partition applets in different code bases into separate
 * contexts, and establish walls between these contexts. In such a scenario,
 * there will be one <code>EventQueue</code> per context. Other browsers place
 * all applets into the same context, implying that there will be only a single,
 * global <code>EventQueue</code> for all applets. This behavior is
 * implementation-dependent. Consult your browser's documentation for more
 * information.
 * <p>
 * For information on the threading issues of the event dispatch machinery, see
 * <a href="doc-files/AWTThreadIssues.html#Autoshutdown" >AWT Threading
 * Issues</a>.
 *
 * @author Thomas Ball
 * @author Fred Ecks
 * @author David Mendenhall
 *
 * @since 1.1
 */
public class EventQueue {
	private final Queue queues;

	private EventDispatchThread dispatchThread;

	private long mostRecentEventTime = System.currentTimeMillis();

	private final String name = "AWT-EventQueue-" + 0;

	public EventQueue() {
		queues = new Queue();

		dispatchThread = new EventDispatchThread(null, name, this);
		dispatchThread.setPriority(Thread.NORM_PRIORITY + 1);
		dispatchThread.setDaemon(true);

		dispatchThread.start();
	}

	public void postEvent(AWTEvent theEvent) {
		if (theEvent == null) {
			return;
		}

		synchronized (this) {
			EventQueueNode newItem = EventQueueNode.getFree();
			newItem.event = theEvent;

			if (queues.head == null) {
				queues.head = queues.tail = newItem;
			} else {
				queues.tail.next = newItem;
				queues.tail = newItem;
			}

			this.notify();
		}
	}

	private boolean noEvents() {
		if (queues.head != null) {
			return false;
		}
		return true;
	}

	public AWTEvent getNextEvent() throws InterruptedException {
		try {
			AWTEvent event = getNextEventPrivate();
			if (event != null) {
				return event;
			}
		} finally {
		}
		return null;
	}

	AWTEvent getNextEventPrivate() throws InterruptedException {
		synchronized (this) {
			if (queues.head != null) {
				EventQueueNode entry = queues.head;
				EventQueueNode nextNode = entry.next;
				queues.head = nextNode;
				if (nextNode == null) {
					queues.tail = null;
				}
				AWTEvent out = entry.event;
				EventQueueNode.cycle(entry);
				return out;
			}
		}
		return null;
	}

	AWTEvent getNextEvent(int id) throws InterruptedException {
		synchronized (this) {
			for (EventQueueNode entry = queues.head, prev = null; entry != null; prev = entry, entry = entry.next) {
				if (entry.event.getID() == id) {
					if (prev == null) {
						queues.head = entry.next;
					} else {
						prev.next = entry.next;
					}
					if (queues.tail == entry) {
						queues.tail = prev;
					}
					AWTEvent out = entry.event;
					EventQueueNode.cycle(entry);
					return out;
				}
			}
		}
		return null;
	}

	public AWTEvent peekEvent() {
		synchronized (this) {
			if (queues.head != null) {
				return queues.head.event;
			}
		}
		return null;
	}

	public AWTEvent peekEvent(int id) {
		synchronized (this) {
			EventQueueNode q = queues.head;
			for (; q != null; q = q.next) {
				if (q.event.getID() == id) {
					return q.event;
				}
			}
		}

		return null;
	}

	protected void dispatchEvent(final AWTEvent event) {
		ContextManager.getThreadPool().run(new Runnable() {
			@Override
			public void run() {
				dispatchEventImpl(event, event.getSource());
			}
		});
	}

	private void dispatchEventImpl(final AWTEvent event, final Object src) {
		event.isPosted = true;
		if (event instanceof ActiveEvent) {
			setCurrentEventAndMostRecentTimeImpl(event);
			((ActiveEvent) event).dispatch();
		} else {
			LogManager.log("Unable to dispatch event: " + event);
		}
	}

	public static long getMostRecentEventTime() {
		return Toolkit.getEventQueue().getMostRecentEventTimeImpl();
	}

	private long getMostRecentEventTimeImpl() {
		try {
			return (Thread.currentThread() == dispatchThread) ? mostRecentEventTime
					: System.currentTimeMillis();
		} finally {
		}
	}

	long getMostRecentEventTimeEx() {
		try {
			return mostRecentEventTime;
		} finally {
		}
	}

	public static AWTEvent getCurrentEvent() {
		return Toolkit.getEventQueue().getCurrentEventImpl();
	}

	private AWTEvent getCurrentEventImpl() {
		AndroidClassUtil.callEmptyMethod();

		return new AWTEvent(this, 0) {
		};
	}

	public void push(EventQueue newEventQueue) {
		// dispatchThread.setEventQueue(newEventQueue);
		// newEventQueue.dispatchThread = dispatchThread;
		dispatchThread = newEventQueue.dispatchThread;// 修复切换时Bug
		Toolkit.getDefaultToolkit().setSystemEventQueueAdAPI(this);
	}

	protected void pop() throws EmptyStackException {
	}

	public SecondaryLoop createSecondaryLoop() {
		return createSecondaryLoop(null, null, 0);
	}

	SecondaryLoop createSecondaryLoop(Conditional cond, EventFilter filter, long interval) {
		return null;
	}

	public static boolean isDispatchThread() {
		EventQueue eq = Toolkit.getEventQueue();
		return eq.isDispatchThreadImpl();
		// return true;//关闭DispatchThread
	}

	final boolean isDispatchThreadImpl() {
		return Thread.currentThread() == dispatchThread;
	}

	final boolean detachDispatchThread(EventDispatchThread edt, boolean forceDetach) {
		return false;
	}

	final EventDispatchThread getDispatchThread() {
		return dispatchThread;
	}

	synchronized final void removeSourceEvents(Object source, boolean removeAllEvents) {
	}

	static void setCurrentEventAndMostRecentTime(AWTEvent e) {
		Toolkit.getEventQueue().setCurrentEventAndMostRecentTimeImpl(e);
	}

	private synchronized void setCurrentEventAndMostRecentTimeImpl(AWTEvent e) {
	}

	public static void invokeLater(Runnable runnable) {
		Toolkit.getEventQueue()
				.postEvent(new InvocationEvent(Toolkit.getDefaultToolkit(), runnable));
	}

	public static void invokeAndWait(Runnable runnable)
			throws InterruptedException, InvocationTargetException {
		invokeAndWait(Toolkit.getDefaultToolkit(), runnable);
	}

	static void invokeAndWait(Object source, Runnable runnable)
			throws InterruptedException, InvocationTargetException {
		if (EventQueue.isDispatchThread()) {
			// throw new Error("Cannot call invokeAndWait from the event
			// dispatcher thread");
			hc.core.util.LogManager
					.warning("exec Runnable.run in EventQueue.invokeAndWait at DispatchThread.");
			runnable.run();
			return;
		}

		class AWTInvocationLock {
		}
		Object lock = new AWTInvocationLock();

		InvocationEvent event = new InvocationEvent(source, runnable, lock, true);

		synchronized (lock) {
			Toolkit.getEventQueue().postEvent(event);
			while (!event.isDispatched()) {
				lock.wait();
			}
		}

		Throwable eventThrowable = event.getThrowable();
		if (eventThrowable != null) {
			throw new InvocationTargetException(eventThrowable);
		}
	}

	private void wakeup(boolean isShutdown) {
	}
}

class Queue {
	EventQueueNode head;
	EventQueueNode tail;
}

class EventQueueNode {
	static Stack cache = new Stack(32);

	public static final EventQueueNode getFree() {
		Object out;
		synchronized (cache) {
			out = cache.pop();
		}
		if (out == null) {
			out = new EventQueueNode();
		}
		return (EventQueueNode) out;
	}

	public static final void cycle(EventQueueNode node) {
		node.next = null;
		synchronized (cache) {
			cache.push(node);
		}
	}

	AWTEvent event;

	EventQueueNode next;
}
