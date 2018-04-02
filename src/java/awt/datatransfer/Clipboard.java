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

import hc.android.ActivityManager;
import hc.android.AndroidUIUtil;

import java.io.IOException;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * A class that implements a mechanism to transfer data using cut/copy/paste
 * operations.
 * <p>
 * {@link FlavorListener}s may be registered on an instance of the Clipboard
 * class to be notified about changes to the set of {@link DataFlavor}s
 * available on this clipboard (see {@link #addFlavorListener}).
 *
 * @see java.awt.Toolkit#getSystemClipboard
 * @see java.awt.Toolkit#getSystemSelection
 *
 * @author Amy Fowler
 * @author Alexander Gerasimov
 */
public class Clipboard {
	ClipboardManager cm;
	String name;

	public Clipboard(String name) {
		this.name = name;
		AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				cm = (ClipboardManager) ActivityManager.applicationContext
						.getSystemService(Context.CLIPBOARD_SERVICE);
			}
		});
	}

	public String getName() {
		return name;
	}

	public synchronized void setContents(Transferable contents, ClipboardOwner owner) {
		if (contents instanceof StringSelection) {
			try {
				cm.setPrimaryClip(ClipData.newPlainText("hc_clip",
						(String) contents.getTransferData(DataFlavor.stringFlavor)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
	}

	public synchronized Transferable getContents(Object requestor) {
		return new Transferable() {
			@Override
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				if (flavor.equals(DataFlavor.stringFlavor)) {
					return true;
				}
				return false;
			}

			@Override
			public DataFlavor[] getTransferDataFlavors() {
				DataFlavor[] out = { DataFlavor.stringFlavor };
				return out;
			}

			@Override
			public Object getTransferData(DataFlavor flavor)
					throws UnsupportedFlavorException, IOException {
				if (flavor.equals(DataFlavor.stringFlavor)) {
					if (cm.hasPrimaryClip()) {
						return cm.getPrimaryClip().getItemAt(0).getText();
					} else {
						return "";
					}
				}
				return null;
			}
		};
	}

	public DataFlavor[] getAvailableDataFlavors() {
		DataFlavor[] out = { DataFlavor.stringFlavor };
		return out;
	}

	public boolean isDataFlavorAvailable(DataFlavor flavor) {
		if (flavor.equals(DataFlavor.stringFlavor) && cm.hasPrimaryClip()) {
			return true;
		}
		return false;
	}

	public synchronized Object getData(DataFlavor flavor) {
		if (flavor.equals(DataFlavor.stringFlavor)) {
			if (cm.hasPrimaryClip()) {
				return cm.getPrimaryClip().getItemAt(0).getText();
			} else {
				return "";
			}
		}
		return null;
	}

}