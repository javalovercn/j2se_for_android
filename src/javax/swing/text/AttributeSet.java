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
package javax.swing.text;

import java.util.Enumeration;

/**
 * A collection of unique attributes. This is a read-only, immutable interface.
 * An attribute is basically a key and a value assigned to the key. The
 * collection may represent something like a style run, a logical style, etc.
 * These are generally used to describe features that will contribute to some
 * graphical representation such as a font. The set of possible keys is
 * unbounded and can be anything. Typically View implementations will respond to
 * attribute definitions and render something to represent the attributes.
 * <p>
 * Attributes can potentially resolve in a hierarchy. If a key doesn't resolve
 * locally, and a resolving parent exists, the key will be resolved through the
 * parent.
 *
 * @author Timothy Prinzing
 * @see MutableAttributeSet
 */
public interface AttributeSet {

	public interface FontAttribute {
	}

	public interface ColorAttribute {
	}

	public interface CharacterAttribute {
	}

	public interface ParagraphAttribute {
	}

	public int getAttributeCount();

	public boolean isDefined(Object attrName);

	public boolean isEqual(AttributeSet attr);

	public AttributeSet copyAttributes();

	public Object getAttribute(Object key);

	public Enumeration<?> getAttributeNames();

	public boolean containsAttribute(Object name, Object value);

	public boolean containsAttributes(AttributeSet attributes);

	public AttributeSet getResolveParent();

	public static final Object NameAttribute = StyleConstants.NameAttribute;

	public static final Object ResolveAttribute = StyleConstants.ResolveAttribute;

}
