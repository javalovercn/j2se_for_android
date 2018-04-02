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
 * Interface AttributeSet specifies the interface for a set of printing
 * attributes. A printing attribute is an object whose class implements
 * interface {@link Attribute Attribute}.
 * <P>
 * An attribute set contains a group of <I>attribute values,</I> where duplicate
 * values are not allowed in the set. Furthermore, each value in an attribute
 * set is a member of some <I>category,</I> and at most one value in any
 * particular category is allowed in the set. For an attribute set, the values
 * are {@link Attribute Attribute} objects, and the categories are
 * {@link java.lang.Class Class} objects. An attribute's category is the class
 * (or interface) at the root of the class hierarchy for that kind of attribute.
 * Note that an attribute object's category may be a superclass of the attribute
 * object's class rather than the attribute object's class itself. An attribute
 * object's category is determined by calling the {@link Attribute#getCategory()
 * <CODE>getCategory()</CODE>} method defined in interface {@link Attribute
 * Attribute}.
 * <P>
 * The interfaces of an AttributeSet resemble those of the Java Collections
 * API's java.util.Map interface, but is more restrictive in the types it will
 * accept, and combines keys and values into an Attribute.
 * <P>
 * Attribute sets are used in several places in the Print Service API. In each
 * context, only certain kinds of attributes are allowed to appear in the
 * attribute set, as determined by the tagging interfaces which the attribute
 * class implements -- {@link DocAttribute DocAttribute},
 * {@link PrintRequestAttribute PrintRequestAttribute}, {@link PrintJobAttribute
 * PrintJobAttribute}, and {@link PrintServiceAttribute PrintServiceAttribute}.
 * There are four specializations of an attribute set that are restricted to
 * contain just one of the four kinds of attribute -- {@link DocAttributeSet
 * DocAttributeSet}, {@link PrintRequestAttributeSet PrintRequestAttributeSet},
 * {@link PrintJobAttributeSet PrintJobAttributeSet}, and
 * {@link PrintServiceAttributeSet PrintServiceAttributeSet}, respectively. Note
 * that many attribute classes implement more than one tagging interface and so
 * may appear in more than one context.
 * <UL>
 * <LI>A {@link DocAttributeSet DocAttributeSet}, containing {@link DocAttribute
 * DocAttribute}s, specifies the characteristics of an individual doc and the
 * print job settings to be applied to an individual doc.
 * <P>
 * <LI>A {@link PrintRequestAttributeSet PrintRequestAttributeSet}, containing
 * {@link PrintRequestAttribute PrintRequestAttribute}s, specifies the settings
 * to be applied to a whole print job and to all the docs in the print job.
 * <P>
 * <LI>A {@link PrintJobAttributeSet PrintJobAttributeSet}, containing
 * {@link PrintJobAttribute PrintJobAttribute}s, reports the status of a print
 * job.
 * <P>
 * <LI>A {@link PrintServiceAttributeSet PrintServiceAttributeSet}, containing
 * {@link PrintServiceAttribute PrintServiceAttribute}s, reports the status of a
 * Print Service instance.
 * </UL>
 * <P>
 * In some contexts, the client is only allowed to examine an attribute set's
 * contents but not change them (the set is read-only). In other places, the
 * client is allowed both to examine and to change an attribute set's contents
 * (the set is read-write). For a read-only attribute set, calling a mutating
 * operation throws an UnmodifiableSetException.
 * <P>
 * The Print Service API provides one implementation of interface AttributeSet,
 * class {@link HashAttributeSet HashAttributeSet}. A client can use class
 * {@link HashAttributeSet HashAttributeSet} or provide its own implementation
 * of interface AttributeSet. The Print Service API also provides
 * implementations of interface AttributeSet's subinterfaces -- classes
 * {@link HashDocAttributeSet HashDocAttributeSet},
 * {@link HashPrintRequestAttributeSet HashPrintRequestAttributeSet},
 * {@link HashPrintJobAttributeSet HashPrintJobAttributeSet}, and
 * {@link HashPrintServiceAttributeSet HashPrintServiceAttributeSet}.
 * <P>
 *
 * @author Alan Kaminsky
 */
public interface AttributeSet {

	public Attribute get(Class<?> category);

	public boolean add(Attribute attribute);

	public boolean remove(Class<?> category);

	public boolean remove(Attribute attribute);

	public boolean containsKey(Class<?> category);

	public boolean containsValue(Attribute attribute);

	public boolean addAll(AttributeSet attributes);

	public int size();

	public Attribute[] toArray();

	public void clear();

	public boolean isEmpty();

	public boolean equals(Object object);

	public int hashCode();

}
