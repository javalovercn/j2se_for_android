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
 * Class AccessibleTable describes a user-interface component that presents data
 * in a two-dimensional table format.
 *
 * @author Lynn Monsanto
 * @since 1.3
 */
public interface AccessibleTable {

	public Accessible getAccessibleCaption();

	public void setAccessibleCaption(Accessible a);

	public Accessible getAccessibleSummary();

	public void setAccessibleSummary(Accessible a);

	public int getAccessibleRowCount();

	public int getAccessibleColumnCount();

	public Accessible getAccessibleAt(int r, int c);

	public int getAccessibleRowExtentAt(int r, int c);

	public int getAccessibleColumnExtentAt(int r, int c);

	public AccessibleTable getAccessibleRowHeader();

	public void setAccessibleRowHeader(AccessibleTable table);

	public AccessibleTable getAccessibleColumnHeader();

	public void setAccessibleColumnHeader(AccessibleTable table);

	public Accessible getAccessibleRowDescription(int r);

	public void setAccessibleRowDescription(int r, Accessible a);

	public Accessible getAccessibleColumnDescription(int c);

	public void setAccessibleColumnDescription(int c, Accessible a);

	public boolean isAccessibleSelected(int r, int c);

	public boolean isAccessibleRowSelected(int r);

	public boolean isAccessibleColumnSelected(int c);

	public int[] getSelectedAccessibleRows();

	public int[] getSelectedAccessibleColumns();
}
