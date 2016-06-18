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
package java.awt.event;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;

/**
 * An event which executes the <code>run()</code> method on a <code>Runnable
 * </code> when dispatched by the AWT event dispatcher thread. This class can
 * be used as a reference implementation of <code>ActiveEvent</code> rather
 * than declaring a new class and defining <code>dispatch()</code>.<p>
 *
 * Instances of this class are placed on the <code>EventQueue</code> by calls
 * to <code>invokeLater</code> and <code>invokeAndWait</code>. Client code
 * can use this fact to write replacement functions for <code>invokeLater
 * </code> and <code>invokeAndWait</code> without writing special-case code
 * in any <code>AWTEventListener</code> objects.
 * <p>
 * An unspecified behavior will be caused if the {@code id} parameter
 * of any particular {@code InvocationEvent} instance is not
 * in the range from {@code INVOCATION_FIRST} to {@code INVOCATION_LAST}.
 *
 * @author      Fred Ecks
 * @author      David Mendenhall
 *
 * @see         java.awt.ActiveEvent
 * @see         java.awt.EventQueue#invokeLater
 * @see         java.awt.EventQueue#invokeAndWait
 * @see         AWTEventListener
 *
 * @since       1.2
 */
public class InvocationEvent extends AWTEvent implements ActiveEvent {
    public static final int INVOCATION_FIRST = 1200;
    public static final int INVOCATION_DEFAULT = INVOCATION_FIRST;
    public static final int INVOCATION_LAST = INVOCATION_DEFAULT;

    protected Runnable runnable;

    protected Object notifier;
    private volatile boolean dispatched = false;
    protected boolean catchExceptions;
    private Exception exception = null;
    private Throwable throwable = null;

    private long when;

    public InvocationEvent(Object source, Runnable runnable) {
        this(source, runnable, null, false);
    }

    public InvocationEvent(Object source, Runnable runnable, Object notifier,
                           boolean catchThrowables) {
        this(source, INVOCATION_DEFAULT, runnable, notifier, catchThrowables);
    }

    protected InvocationEvent(Object source, int id, Runnable runnable,
                              Object notifier, boolean catchThrowables) {
        super(source, id);
        this.runnable = runnable;
        this.notifier = notifier;
        this.catchExceptions = catchThrowables;
        this.when = System.currentTimeMillis();
    }

    public void dispatch() {
        try {
            if (catchExceptions) {
                try {
                    runnable.run();
                }
                catch (Throwable t) {
                    if (t instanceof Exception) {
                        exception = (Exception) t;
                    }
                    throwable = t;
                }
            }else {
            	try{
            		runnable.run();
            	}catch (Throwable e) {
            		e.printStackTrace();
				}
            }
        } finally {
            dispatched = true;

            if (notifier != null) {
                synchronized (notifier) {
                    notifier.notifyAll();
                }
            }
        }
    }

    public Exception getException() {
        return (catchExceptions) ? exception : null;
    }

    public Throwable getThrowable() {
        return (catchExceptions) ? throwable : null;
    }

    public long getWhen() {
        return when;
    }

    public boolean isDispatched() {
        return dispatched;
    }

    public String paramString() {
        return "runnable=" + runnable + ",notifier=" + notifier +
            ",catchExceptions=" + catchExceptions + ",when=" + when;
    }
}