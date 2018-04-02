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
package javax.print;

import javax.print.attribute.Attribute;

/**
 * Interface PrintService is the factory for a DocPrintJob. A PrintService
 * describes the capabilities of a Printer and can be queried regarding a
 * printer's supported attributes.
 * <P>
 * Example:
 * 
 * <PRE>
 * DocFlavor flavor = DocFlavor.INPUT_STREAM.POSTSCRIPT;
 * PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
 * aset.add(MediaSizeName.ISO_A4);
 * PrintService[] pservices = PrintServiceLookup.lookupPrintServices(flavor, aset);
 * if (pservices.length > 0) {
 * 	DocPrintJob pj = pservices[0].createPrintJob();
 * 	try {
 * 		FileInputStream fis = new FileInputStream("test.ps");
 * 		Doc doc = new SimpleDoc(fis, flavor, null);
 * 		pj.print(doc, aset);
 * 	} catch (FileNotFoundException fe) {
 * 	} catch (PrintException e) {
 * 	}
 * }
 * </PRE>
 */
public interface PrintService {

	public String getName();

	public Class<?>[] getSupportedAttributeCategories();

	public boolean isAttributeCategorySupported(Class<? extends Attribute> category);

	public Object getDefaultAttributeValue(Class<? extends Attribute> category);

	public boolean equals(Object obj);

	public int hashCode();

}