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

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Internal class to manage all Timers using one thread. TimerQueue manages a
 * queue of Timers. The Timers are chained together in a linked list sorted by
 * the order in which they will expire.
 *
 * @author Dave Moore
 * @author Igor Kushnirskiy
 */
class TimerQueue implements Runnable {
	private static final Object sharedInstanceKey = new StringBuffer(
			"TimerQueue.sharedInstanceKey");
	private static final Object expiredTimersKey = new StringBuffer("TimerQueue.expiredTimersKey");

	private final DelayQueue<DelayedTimer> queue;
	private volatile boolean running;
	private final Lock runningLock;

	private static final Object classLock = new Object();
	private static final long NANO_ORIGIN = System.nanoTime();

	public TimerQueue() {
		super();
		queue = new DelayQueue<DelayedTimer>();
		runningLock = new ReentrantLock();
		startIfNeeded();
	}

	public static TimerQueue sharedInstance() {
		synchronized (classLock) {
			TimerQueue sharedInst = (TimerQueue) SwingUtilities.appContextGet(sharedInstanceKey);
			if (sharedInst == null) {
				sharedInst = new TimerQueue();
				SwingUtilities.appContextPut(sharedInstanceKey, sharedInst);
			}
			return sharedInst;
		}
	}

	void startIfNeeded() {
	}

	void addTimer(Timer timer, long delayMillis) {
	}

	private void addTimer(DelayedTimer delayedTimer) {
	}

	void removeTimer(Timer timer) {
	}

	boolean containsTimer(Timer timer) {
		return false;
	}

	public void run() {
	}

	public String toString() {
		return "";
	}

	private static long now() {
		return System.nanoTime() - NANO_ORIGIN;
	}

	static class DelayedTimer implements Delayed {
		private static final AtomicLong sequencer = new AtomicLong(0);

		private final long sequenceNumber;
		private volatile long time;
		private final Timer timer;

		DelayedTimer(Timer timer, long nanos) {
			this.timer = timer;
			time = nanos;
			sequenceNumber = sequencer.getAndIncrement();
		}

		final public long getDelay(TimeUnit unit) {
			return unit.convert(time - now(), TimeUnit.NANOSECONDS);
		}

		final void setTime(long nanos) {
			time = nanos;
		}

		final Timer getTimer() {
			return timer;
		}

		public int compareTo(Delayed other) {
			if (other == this) {
				return 0;
			}
			if (other instanceof DelayedTimer) {
				DelayedTimer x = (DelayedTimer) other;
				long diff = time - x.time;
				if (diff < 0) {
					return -1;
				} else if (diff > 0) {
					return 1;
				} else if (sequenceNumber < x.sequenceNumber) {
					return -1;
				} else {
					return 1;
				}
			}
			long d = (getDelay(TimeUnit.NANOSECONDS) - other.getDelay(TimeUnit.NANOSECONDS));
			return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
		}
	}
}