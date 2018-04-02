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

/**
 * Class AccessibleExtendedTable provides extended information about a
 * user-interface component that presents data in a two-dimensional table
 * format. Applications can determine if an object supports the
 * AccessibleExtendedTable interface by first obtaining its AccessibleContext
 * and then calling the {@link AccessibleContext#getAccessibleTable} method. If
 * the return value is not null and the type of the return value is
 * AccessibleExtendedTable, the object supports this interface.
 *
 * @author Lynn Monsanto
 * @since 1.4
 */
public interface AccessibleExtendedTable extends AccessibleTable {

	public int getAccessibleRow(int index);

	public int getAccessibleColumn(int index);

	public int getAccessibleIndex(int r, int c);
}