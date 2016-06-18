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
package java.awt.datatransfer;

import java.io.IOException;
import java.io.StringReader;

/**
 * A <code>Transferable</code> which implements the capability required
 * to transfer a <code>String</code>.
 *
 * This <code>Transferable</code> properly supports
 * <code>DataFlavor.stringFlavor</code>
 * and all equivalent flavors. Support for
 * <code>DataFlavor.plainTextFlavor</code>
 * and all equivalent flavors is <b>deprecated</b>. No other
 * <code>DataFlavor</code>s are supported.
 *
 * @see java.awt.datatransfer.DataFlavor#stringFlavor
 * @see java.awt.datatransfer.DataFlavor#plainTextFlavor
 */
public class StringSelection implements Transferable, ClipboardOwner {

    private static final int STRING = 0;
    private static final int PLAIN_TEXT = 1;

    private static final DataFlavor[] flavors = {
        DataFlavor.stringFlavor,
        DataFlavor.plainTextFlavor 
    };

    private String data;

    public StringSelection(String data) {
        this.data = data;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return (DataFlavor[])flavors.clone();
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        for (int i = 0; i < flavors.length; i++) {
            if (flavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }

    public Object getTransferData(DataFlavor flavor)
        throws UnsupportedFlavorException, IOException
    {
        if (flavor.equals(flavors[STRING])) {
            return (Object)data;
        } else if (flavor.equals(flavors[PLAIN_TEXT])) {
            return new StringReader(data == null ? "" : data);
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}
