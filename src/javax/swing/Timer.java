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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.AccessControlContext;
import java.util.EventListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.event.EventListenerList;

/**
 * Fires one or more {@code ActionEvent}s at specified intervals. An example use
 * is an animation object that uses a <code>Timer</code> as the trigger for
 * drawing its frames.
 * <p>
 * Setting up a timer involves creating a <code>Timer</code> object, registering
 * one or more action listeners on it, and starting the timer using the
 * <code>start</code> method. For example, the following code creates and starts
 * a timer that fires an action event once per second (as specified by the first
 * argument to the <code>Timer</code> constructor). The second argument to the
 * <code>Timer</code> constructor specifies a listener to receive the timer's
 * action events.
 *
 * <pre>
 *  int delay = 1000; //milliseconds
 *  ActionListener taskPerformer = new ActionListener() {
 *      public void actionPerformed(ActionEvent evt) {
 *          <em>//...Perform a task...</em>
 *      }
 *  };
 *  new Timer(delay, taskPerformer).start();
 * </pre>
 *
 * <p>
 * {@code Timers} are constructed by specifying both a delay parameter and an
 * {@code ActionListener}. The delay parameter is used to set both the initial
 * delay and the delay between event firing, in milliseconds. Once the timer has
 * been started, it waits for the initial delay before firing its first
 * <code>ActionEvent</code> to registered listeners. After this first event, it
 * continues to fire events every time the between-event delay has elapsed,
 * until it is stopped.
 * <p>
 * After construction, the initial delay and the between-event delay can be
 * changed independently, and additional <code>ActionListeners</code> may be
 * added.
 * <p>
 * If you want the timer to fire only the first time and then stop, invoke
 * <code>setRepeats(false)</code> on the timer.
 * <p>
 * Although all <code>Timer</code>s perform their waiting using a single, shared
 * thread (created by the first <code>Timer</code> object that executes), the
 * action event handlers for <code>Timer</code>s execute on another thread --
 * the event-dispatching thread. This means that the action handlers for
 * <code>Timer</code>s can safely perform operations on Swing components.
 * However, it also means that the handlers must execute quickly to keep the GUI
 * responsive.
 *
 * <p>
 * In v 1.3, another <code>Timer</code> class was added to the Java platform:
 * <code>java.util.Timer</code>. Both it and <code>javax.swing.Timer</code>
 * provide the same basic functionality, but <code>java.util.Timer</code> is
 * more general and has more features. The <code>javax.swing.Timer</code> has
 * two features that can make it a little easier to use with GUIs. First, its
 * event handling metaphor is familiar to GUI programmers and can make dealing
 * with the event-dispatching thread a bit simpler. Second, its automatic thread
 * sharing means that you don't have to take special steps to avoid spawning too
 * many threads. Instead, your timer uses the same thread used to make cursors
 * blink, tool tips appear, and so on.
 *
 * <p>
 * You can find further documentation and several examples of using timers by
 * visiting
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/timer.html"
 * target = "_top">How to Use Timers</a>, a section in <em>The Java
 * Tutorial.</em> For more examples and help in choosing between this
 * <code>Timer</code> class and <code>java.util.Timer</code>, see
 * <a href="http://java.sun.com/products/jfc/tsc/articles/timer/" target=
 * "_top">Using Timers in Swing Applications</a>, an article in <em>The Swing
 * Connection.</em>
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @see java.util.Timer <code>java.util.Timer</code>
 *
 *
 * @author Dave Moore
 */
public class Timer implements Serializable {
	protected EventListenerList listenerList = new EventListenerList();

	private transient final AtomicBoolean notify = new AtomicBoolean(false);

	private volatile int initialDelay, delay;
	private volatile boolean repeats = true, coalesce = true;

	private transient final Runnable doPostEvent;

	private static volatile boolean logTimers;

	private transient final Lock lock = new ReentrantLock();
	transient TimerQueue.DelayedTimer delayedTimer = null;

	private volatile String actionCommand;

	public Timer(int delay, ActionListener listener) {
		super();
		this.delay = delay;
		this.initialDelay = delay;

		doPostEvent = new DoPostEvent();

		if (listener != null) {
			addActionListener(listener);
		}
	}

	final AccessControlContext getAccessControlContext() {
		return null;
	}

	class DoPostEvent implements Runnable {
		public void run() {
		}

		Timer getTimer() {
			return Timer.this;
		}
	}

	public void addActionListener(ActionListener listener) {
		listenerList.add(ActionListener.class, listener);
	}

	public void removeActionListener(ActionListener listener) {
		listenerList.remove(ActionListener.class, listener);
	}

	public ActionListener[] getActionListeners() {
		return listenerList.getListeners(ActionListener.class);
	}

	protected void fireActionPerformed(ActionEvent e) {
	}

	public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
		return listenerList.getListeners(listenerType);
	}

	private TimerQueue timerQueue() {
		return TimerQueue.sharedInstance();
	}

	public static void setLogTimers(boolean flag) {
		logTimers = flag;
	}

	public static boolean getLogTimers() {
		return logTimers;
	}

	public void setDelay(int delay) {
		if (delay < 0) {
			throw new IllegalArgumentException("Invalid delay: " + delay);
		} else {
			this.delay = delay;
		}
	}

	public int getDelay() {
		return delay;
	}

	public void setInitialDelay(int initialDelay) {
		if (initialDelay < 0) {
			throw new IllegalArgumentException("Invalid initial delay: " + initialDelay);
		} else {
			this.initialDelay = initialDelay;
		}
	}

	public int getInitialDelay() {
		return initialDelay;
	}

	public void setRepeats(boolean flag) {
		repeats = flag;
	}

	public boolean isRepeats() {
		return repeats;
	}

	public void setCoalesce(boolean flag) {
	}

	public boolean isCoalesce() {
		return coalesce;
	}

	public void setActionCommand(String command) {
		this.actionCommand = command;
	}

	public String getActionCommand() {
		return actionCommand;
	}

	public void start() {
	}

	public boolean isRunning() {
		return timerQueue().containsTimer(this);
	}

	public void stop() {
	}

	public void restart() {
	}

	void cancelEvent() {
	}

	void post() {
	}

	Lock getLock() {
		return lock;
	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
	}

	private Object readResolve() {
		return null;
	}
}
