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
package javax.accessibility;

import javax.swing.text.AttributeSet;

/**
 * <P>
 * The AccessibleEditableText interface should be implemented by all classes
 * that present editable textual information on the display. Along with the
 * AccessibleText interface, this interface provides the standard mechanism for
 * an assistive technology to access that text via its content, attributes, and
 * spatial location. Applications can determine if an object supports the
 * AccessibleEditableText interface by first obtaining its AccessibleContext
 * (see {@link Accessible}) and then calling the
 * {@link AccessibleContext#getAccessibleEditableText} method of
 * AccessibleContext. If the return value is not null, the object supports this
 * interface.
 *
 * @see Accessible
 * @see Accessible#getAccessibleContext
 * @see AccessibleContext
 * @see AccessibleContext#getAccessibleText
 * @see AccessibleContext#getAccessibleEditableText
 *
 * @author Lynn Monsanto
 * @since 1.4
 */
public interface AccessibleEditableText extends AccessibleText {

	public void setTextContents(String s);

	public void insertTextAtIndex(int index, String s);

	public String getTextRange(int startIndex, int endIndex);

	public void delete(int startIndex, int endIndex);

	public void cut(int startIndex, int endIndex);

	public void paste(int startIndex);

	public void replaceText(int startIndex, int endIndex, String s);

	public void selectText(int startIndex, int endIndex);

	public void setAttributes(int startIndex, int endIndex, AttributeSet as);

}
