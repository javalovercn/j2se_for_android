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
package java.awt;

import hc.android.AndroidClassUtil;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.accessibility.AccessibleContext;
import javax.swing.JDialog;
import javax.swing.JRootPane;

/**
 * A Dialog is a top-level window with a title and a border
 * that is typically used to take some form of input from the user.
 *
 * The size of the dialog includes any area designated for the
 * border.  The dimensions of the border area can be obtained
 * using the <code>getInsets</code> method, however, since
 * these dimensions are platform-dependent, a valid insets
 * value cannot be obtained until the dialog is made displayable
 * by either calling <code>pack</code> or <code>show</code>.
 * Since the border area is included in the overall size of the
 * dialog, the border effectively obscures a portion of the dialog,
 * constraining the area available for rendering and/or displaying
 * subcomponents to the rectangle which has an upper-left corner
 * location of <code>(insets.left, insets.top)</code>, and has a size of
 * <code>width - (insets.left + insets.right)</code> by
 * <code>height - (insets.top + insets.bottom)</code>.
 * <p>
 * The default layout for a dialog is <code>BorderLayout</code>.
 * <p>
 * A dialog may have its native decorations (i.e. Frame & Titlebar) turned off
 * with <code>setUndecorated</code>.  This can only be done while the dialog
 * is not {@link Component#isDisplayable() displayable}.
 * <p>
 * A dialog may have another window as its owner when it's constructed.  When
 * the owner window of a visible dialog is minimized, the dialog will
 * automatically be hidden from the user. When the owner window is subsequently
 * restored, the dialog is made visible to the user again.
 * <p>
 * In a multi-screen environment, you can create a <code>Dialog</code>
 * on a different screen device than its owner.  See {@link java.awt.Frame} for
 * more information.
 * <p>
 * A dialog can be either modeless (the default) or modal.  A modal
 * dialog is one which blocks input to some other top-level windows
 * in the application, except for any windows created with the dialog
 * as their owner. See <a href="doc-files/Modality.html">AWT Modality</a>
 * specification for details.
 * <p>
 * Dialogs are capable of generating the following
 * <code>WindowEvents</code>:
 * <code>WindowOpened</code>, <code>WindowClosing</code>,
 * <code>WindowClosed</code>, <code>WindowActivated</code>,
 * <code>WindowDeactivated</code>, <code>WindowGainedFocus</code>,
 * <code>WindowLostFocus</code>.
 *
 * @see WindowEvent
 * @see Window#addWindowListener
 *
 * @author      Sami Shaio
 * @author      Arthur van Hoff
 * @since       JDK1.0
 */
public class Dialog extends Window {
	boolean resizable = false;
	boolean undecorated = false;

	public static enum ModalityType {
		MODELESS, DOCUMENT_MODAL, APPLICATION_MODAL, TOOLKIT_MODAL
	};

	public final static ModalityType DEFAULT_MODALITY_TYPE = ModalityType.APPLICATION_MODAL;

	boolean modal;
	ModalityType modalityType;

	public static enum ModalExclusionType {
		NO_EXCLUDE, APPLICATION_EXCLUDE, TOOLKIT_EXCLUDE
	};

	String title;

	private static final String base = "dialog";
	private static int nameCounter = 0;

	public Dialog(Frame owner) {
		this(owner, "", false);
	}

	public Dialog(Frame owner, boolean modal) {
		this(owner, "", modal);
	}

	public Dialog(Frame owner, String title) {
		this(owner, title, false);
	}

	public Dialog(Frame owner, String title, boolean modal) {
		this(owner, title, modal ? DEFAULT_MODALITY_TYPE
				: ModalityType.MODELESS);
	}

	public Dialog(Frame owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		this(owner, title, modal ? DEFAULT_MODALITY_TYPE
				: ModalityType.MODELESS, gc);
	}

	public Dialog(Dialog owner) {
		this(owner, "", false);
	}

	public Dialog(Dialog owner, String title) {
		this(owner, title, false);
	}

	public Dialog(Dialog owner, String title, boolean modal) {
		this(owner, title, modal ? DEFAULT_MODALITY_TYPE
				: ModalityType.MODELESS);
	}

	public Dialog(Dialog owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		this(owner, title, modal ? DEFAULT_MODALITY_TYPE
				: ModalityType.MODELESS, gc);
	}

	public Dialog(Window owner) {
		this(owner, "", ModalityType.MODELESS);
	}

	public Dialog(Window owner, String title) {
		this(owner, title, ModalityType.MODELESS);
	}

	public Dialog(Window owner, ModalityType modalityType) {
		this(owner, "", modalityType);
	}

	public Dialog(Window owner, String title, ModalityType modalityType,
			GraphicsConfiguration gc) {
		this(owner, title, modalityType);
	}

	public Dialog(Window owner, String title, ModalityType modalityType) {
		super(owner);

		if ((owner != null) && !(owner instanceof Frame)
				&& !(owner instanceof Dialog)) {
			throw new IllegalArgumentException("Wrong owner. It should be Frame or Dialog");
		}

//		setTitle(title);
		setModalityType(modalityType);
	}

	String constructComponentName() {
		synchronized (Dialog.class) {
			return base + nameCounter++;
		}
	}

	public void addNotify() {
		AndroidClassUtil.callEmptyMethod();
	}

	public boolean isModal() {
		return isModal_NoClientCode();
	}

	final boolean isModal_NoClientCode() {
		return modalityType != ModalityType.MODELESS;
	}

	public void setModal(boolean modal) {
		this.modal = modal;
		setModalityType(modal ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS);
	}

	public ModalityType getModalityType() {
		return modalityType;
	}

	public void setModalityType(ModalityType type) {
		if (type == null) {
			type = Dialog.ModalityType.MODELESS;
		}
		modalityType = type;
		modal = (modalityType != ModalityType.MODELESS);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		if(this instanceof JDialog){
			((JDialog)this).getRootPane().setTitleAdAPI(title);
		}
	}

	public void setVisible(boolean b) {
		super.setVisible(b);
	}

	@Deprecated
	public void show() {
		setVisible(true);
	}

	@Deprecated
	public void hide() {
		setVisible(false);
	}

	public void toBack() {
		super.toBack();
	}

	public boolean isResizable() {
		return resizable;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	public void setUndecorated(boolean undecorated) {
		this.undecorated = undecorated;
		if(this instanceof JDialog){
			if(undecorated == false){
				((JDialog)this).getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
			}else{
				((JDialog)this).getRootPane().setWindowDecorationStyle(JRootPane.NONE);
			}
		}
	}

	public boolean isUndecorated() {
		return undecorated;
	}

	@Override
	public void setOpacity(float opacity) {
		if ((opacity < 1.0f) && !isUndecorated()) {
			throw new IllegalComponentStateException("The dialog is decorated");
		}
		super.setOpacity(opacity);
	}

	@Override
	public void setShape(Shape shape) {
		if ((shape != null) && !isUndecorated()) {
			throw new IllegalComponentStateException("The dialog is decorated");
		}
		super.setShape(shape);
	}

	@Override
	public void setBackground(Color bgColor) {
		if ((bgColor != null) && (bgColor.getAlpha() < 255) && !isUndecorated()) {
			throw new IllegalComponentStateException("The dialog is decorated");
		}
		super.setBackground(bgColor);
	}

	protected String paramString() {
		String str = "";
		return str;
	}

	boolean shouldBlock(Window w) {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	void blockWindow(Window w) {
		AndroidClassUtil.callEmptyMethod();
	}

	void blockWindows(java.util.List<Window> toBlock) {
		AndroidClassUtil.callEmptyMethod();
	}

	void unblockWindow(Window w) {
		AndroidClassUtil.callEmptyMethod();
	}

	static void checkShouldBeBlocked(Window w) {
		AndroidClassUtil.callEmptyMethod();
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException,
			IOException, HeadlessException {
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = AndroidClassUtil.buildAccessibleContext(this);
		}
		return accessibleContext;
	}

}
