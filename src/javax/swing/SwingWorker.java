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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * An abstract class to perform lengthy GUI-interaction tasks in a background
 * thread. Several background threads can be used to execute such tasks.
 * However, the exact strategy of choosing a thread for any particular
 * {@code SwingWorker} is unspecified and should not be relied on.
 * <p>
 * When writing a multi-threaded application using Swing, there are two
 * constraints to keep in mind: (refer to
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html">
 * How to Use Threads </a> for more details):
 * <ul>
 * <li>Time-consuming tasks should not be run on the <i>Event Dispatch
 * Thread</i>. Otherwise the application becomes unresponsive.</li>
 * <li>Swing components should be accessed on the <i>Event Dispatch Thread</i>
 * only.</li>
 * </ul>
 *
 * <p>
 *
 * <p>
 * These constraints mean that a GUI application with time intensive computing
 * needs at least two threads: 1) a thread to perform the lengthy task and 2)
 * the <i>Event Dispatch Thread</i> (EDT) for all GUI-related activities. This
 * involves inter-thread communication which can be tricky to implement.
 *
 * <p>
 * {@code SwingWorker} is designed for situations where you need to have a long
 * running task run in a background thread and provide updates to the UI either
 * when done, or while processing. Subclasses of {@code SwingWorker} must
 * implement the {@link #doInBackground} method to perform the background
 * computation.
 *
 *
 * <p>
 * <b>Workflow</b>
 * <p>
 * There are three threads involved in the life cycle of a {@code SwingWorker} :
 * <ul>
 * <li>
 * <p>
 * <i>Current</i> thread: The {@link #execute} method is called on this thread.
 * It schedules {@code SwingWorker} for the execution on a <i>worker</i> thread
 * and returns immediately. One can wait for the {@code SwingWorker} to complete
 * using the {@link #get get} methods.
 * <li>
 * <p>
 * <i>Worker</i> thread: The {@link #doInBackground} method is called on this
 * thread. This is where all background activities should happen. To notify
 * {@code PropertyChangeListeners} about bound properties changes use the
 * {@link #firePropertyChange firePropertyChange} and
 * {@link #getPropertyChangeSupport} methods. By default there are two bound
 * properties available: {@code state} and {@code progress}.
 * <li>
 * <p>
 * <i>Event Dispatch Thread</i>: All Swing related activities occur on this
 * thread. {@code SwingWorker} invokes the {@link #process process} and
 * {@link #done} methods and notifies any {@code PropertyChangeListeners} on
 * this thread.
 * </ul>
 *
 * <p>
 * Often, the <i>Current</i> thread is the <i>Event Dispatch Thread</i>.
 *
 *
 * <p>
 * Before the {@code doInBackground} method is invoked on a <i>worker</i>
 * thread, {@code SwingWorker} notifies any {@code PropertyChangeListeners}
 * about the {@code state} property change to {@code StateValue.STARTED}. After
 * the {@code doInBackground} method is finished the {@code done} method is
 * executed. Then {@code SwingWorker} notifies any
 * {@code PropertyChangeListeners} about the {@code state} property change to
 * {@code StateValue.DONE}.
 *
 * <p>
 * {@code SwingWorker} is only designed to be executed once. Executing a
 * {@code SwingWorker} more than once will not result in invoking the
 * {@code doInBackground} method twice.
 *
 * <p>
 * <b>Sample Usage</b>
 * <p>
 * The following example illustrates the simplest use case. Some processing is
 * done in the background and when done you update a Swing component.
 *
 * <p>
 * Say we want to find the "Meaning of Life" and display the result in a
 * {@code JLabel}.
 *
 * <pre>
 *   final JLabel label;
 *   class MeaningOfLifeFinder extends SwingWorker&lt;String, Object&gt; {
 *       {@code @Override}
 *       public String doInBackground() {
 *           return findTheMeaningOfLife();
 *       }
 *
 *       {@code @Override}
 *       protected void done() {
 *           try {
 *               label.setText(get());
 *           } catch (Exception ignore) {
 *           }
 *       }
 *   }
 *
 *   (new MeaningOfLifeFinder()).execute();
 * </pre>
 *
 * <p>
 * The next example is useful in situations where you wish to process data as it
 * is ready on the <i>Event Dispatch Thread</i>.
 *
 * <p>
 * Now we want to find the first N prime numbers and display the results in a
 * {@code JTextArea}. While this is computing, we want to update our progress in
 * a {@code JProgressBar}. Finally, we also want to print the prime numbers to
 * {@code System.out}.
 * 
 * <pre>
 * class PrimeNumbersTask extends
 *         SwingWorker&lt;List&lt;Integer&gt;, Integer&gt; {
 *     PrimeNumbersTask(JTextArea textArea, int numbersToFind) {
 *         //initialize
 *     }
 *
 *     {@code @Override}
 *     public List&lt;Integer&gt; doInBackground() {
 *         while (! enough &amp;&amp; ! isCancelled()) {
 *                 number = nextPrimeNumber();
 *                 publish(number);
 *                 setProgress(100 * numbers.size() / numbersToFind);
 *             }
 *         }
 *         return numbers;
 *     }
 *
 *     {@code @Override}
 *     protected void process(List&lt;Integer&gt; chunks) {
 *         for (int number : chunks) {
 *             textArea.append(number + &quot;\n&quot;);
 *         }
 *     }
 * }
 *
 * JTextArea textArea = new JTextArea();
 * final JProgressBar progressBar = new JProgressBar(0, 100);
 * PrimeNumbersTask task = new PrimeNumbersTask(textArea, N);
 * task.addPropertyChangeListener(
 *     new PropertyChangeListener() {
 *         public  void propertyChange(PropertyChangeEvent evt) {
 *             if (&quot;progress&quot;.equals(evt.getPropertyName())) {
 *                 progressBar.setValue((Integer)evt.getNewValue());
 *             }
 *         }
 *     });
 *
 * task.execute();
 * System.out.println(task.get()); //prints all prime numbers we have got
 * </pre>
 *
 * <p>
 * Because {@code SwingWorker} implements {@code Runnable}, a
 * {@code SwingWorker} can be submitted to an
 * {@link java.util.concurrent.Executor} for execution.
 *
 * @author Igor Kushnirskiy
 *
 * @param <T>
 *            the result type returned by this {@code SwingWorker's}
 *            {@code doInBackground} and {@code get} methods
 * @param <V>
 *            the type used for carrying out intermediate results by this
 *            {@code SwingWorker's} {@code publish} and {@code process} methods
 *
 * @since 1.6
 */
public abstract class SwingWorker<T, V> implements RunnableFuture<T> {
	private static final int MAX_WORKER_THREADS = 10;

	private volatile int progress;
	private volatile StateValue state;

	public enum StateValue {
		PENDING, STARTED, DONE
	}

	public SwingWorker() {
	}

	protected abstract T doInBackground() throws Exception;

	public final void run() {
	}

	protected final void publish(V... chunks) {
	}

	protected void process(List<V> chunks) {
	}

	protected void done() {
	}

	protected final void setProgress(int progress) {
	}

	public final int getProgress() {
		return progress;
	}

	public final void execute() {
	}

	public final boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	public final boolean isCancelled() {
		return false;
	}

	public final boolean isDone() {
		return false;
	}

	public final T get() throws InterruptedException, ExecutionException {
		return null;
	}

	public final T get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return null;
	}

	public final void addPropertyChangeListener(PropertyChangeListener listener) {
	}

	public final void removePropertyChangeListener(PropertyChangeListener listener) {
	}

	public final void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
	}

	public final PropertyChangeSupport getPropertyChangeSupport() {
		return null;
	}

	public final StateValue getState() {
		return state;
	}

	private void setState(StateValue state) {
	}

	private void doneEDT() {
	}

	private static final Object DO_SUBMIT_KEY = new StringBuilder("doSubmit");
}
