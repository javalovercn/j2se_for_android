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
package javax.print.attribute;

/**
 * Interface PrintRequestAttributeSet specifies the interface for a set of print
 * request attributes, i.e. printing attributes that implement interface
 * {@link PrintRequestAttribute PrintRequestAttribute}. The client uses a
 * PrintRequestAttributeSet to specify the settings to be applied to a whole
 * print job and to all the docs in the print job.
 * <P>
 * PrintRequestAttributeSet is just an {@link AttributeSet AttributeSet} whose
 * constructors and mutating operations guarantee an additional invariant,
 * namely that all attribute values in the PrintRequestAttributeSet must be
 * instances of interface {@link PrintRequestAttribute PrintRequestAttribute}.
 * The {@link #add(Attribute) <CODE>add(Attribute)</CODE>}, and
 * {@link #addAll(AttributeSet) <CODE>addAll(AttributeSet)</CODE>} operations
 * are respecified below to guarantee this additional invariant.
 * <P>
 *
 * @author Alan Kaminsky
 */
public interface PrintRequestAttributeSet extends AttributeSet {

	public boolean add(Attribute attribute);

	public boolean addAll(AttributeSet attributes);

}
