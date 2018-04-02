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

import hc.App;
import hc.android.ActivityManager;
import hc.android.AndroidClassUtil;
import hc.android.UICore;
import hc.core.util.LogManager;
import hc.android.AndroidUIUtil;
import hc.util.PropertiesManager;
import android.view.View;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextHitInfo;
import java.awt.im.InputMethodRequests;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Method;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.print.PrintService;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DropMode;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.TransferHandler;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent.ElementChange;
import javax.swing.event.DocumentEvent.EventType;
import javax.swing.plaf.TextUI;
import javax.swing.plaf.UIResource;

import android.content.ClipData;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.NumberKeyListener;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * <code>JTextComponent</code> is the base class for swing text components. It
 * tries to be compatible with the <code>java.awt.TextComponent</code> class
 * where it can reasonably do so. Also provided are other services for
 * additional flexibility (beyond the pluggable UI and bean support). You can
 * find information on how to use the functionality this class provides in
 * <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/generaltext.html">General
 * Rules for Using Text Components</a>, a section in <em>The Java Tutorial.</em>
 *
 * <p>
 * <dl>
 * <dt><b><font size=+1>Caret Changes</font></b>
 * <dd>The caret is a pluggable object in swing text components. Notification of
 * changes to the caret position and the selection are sent to implementations
 * of the <code>CaretListener</code> interface that have been registered with
 * the text component. The UI will install a default caret unless a customized
 * caret has been set. <br>
 * By default the caret tracks all the document changes performed on the Event
 * Dispatching Thread and updates it's position accordingly if an insertion
 * occurs before or at the caret position or a removal occurs before the caret
 * position. <code>DefaultCaret</code> tries to make itself visible which may
 * lead to scrolling of a text component within <code>JScrollPane</code>. The
 * default caret behavior can be changed by the
 * {@link DefaultCaret#setUpdatePolicy} method. <br>
 * <b>Note</b>: Non-editable text components also have a caret though it may not
 * be painted.
 *
 * <p>
 * <dt><b><font size=+1>Commands</font></b>
 * <dd>Text components provide a number of commands that can be used to
 * manipulate the component. This is essentially the way that the component
 * expresses its capabilities. These are expressed in terms of the swing
 * <code>Action</code> interface, using the <code>TextAction</code>
 * implementation. The set of commands supported by the text component can be
 * found with the {@link #getActions} method. These actions can be bound to key
 * events, fired from buttons, etc.
 *
 * <p>
 * <dt><b><font size=+1>Text Input</font></b>
 * <dd>The text components support flexible and internationalized text input,
 * using keymaps and the input method framework, while maintaining compatibility
 * with the AWT listener model.
 * <p>
 * A {@link javax.swing.text.Keymap} lets an application bind key strokes to
 * actions. In order to allow keymaps to be shared across multiple text
 * components, they can use actions that extend <code>TextAction</code>.
 * <code>TextAction</code> can determine which <code>JTextComponent</code> most
 * recently has or had focus and therefore is the subject of the action (In the
 * case that the <code>ActionEvent</code> sent to the action doesn't contain the
 * target text component as its source).
 * <p>
 * The <a href="../../../../technotes/guides/imf/spec.html">input method
 * framework</a> lets text components interact with input methods, separate
 * software components that preprocess events to let users enter thousands of
 * different characters using keyboards with far fewer keys.
 * <code>JTextComponent</code> is an <em>active client</em> of the framework, so
 * it implements the preferred user interface for interacting with input
 * methods. As a consequence, some key events do not reach the text component
 * because they are handled by an input method, and some text input reaches the
 * text component as committed text within an
 * {@link java.awt.event.InputMethodEvent} instead of as a key event. The
 * complete text input is the combination of the characters in
 * <code>keyTyped</code> key events and committed text in input method events.
 * <p>
 * The AWT listener model lets applications attach event listeners to components
 * in order to bind events to actions. Swing encourages the use of keymaps
 * instead of listeners, but maintains compatibility with listeners by giving
 * the listeners a chance to steal an event by consuming it.
 * <p>
 * Keyboard event and input method events are handled in the following stages,
 * with each stage capable of consuming the event:
 *
 * <table border=1 summary="Stages of keyboard and input method event handling">
 * <tr>
 * <th id="stage">
 * <p align="left">
 * Stage
 * </p>
 * </th>
 * <th id="ke">
 * <p align="left">
 * KeyEvent
 * </p>
 * </th>
 * <th id="ime">
 * <p align="left">
 * InputMethodEvent
 * </p>
 * </th>
 * </tr>
 * <tr>
 * <td headers="stage">1.</td>
 * <td headers="ke">input methods</td>
 * <td headers="ime">(generated here)</td>
 * </tr>
 * <tr>
 * <td headers="stage">2.</td>
 * <td headers="ke">focus manager</td>
 * <td headers="ime"></td>
 * </tr>
 * <tr>
 * <td headers="stage">3.</td>
 * <td headers="ke">registered key listeners</td>
 * <td headers="ime">registered input method listeners
 * </tr>
 * <tr>
 * <td headers="stage">4.</td>
 * <td headers="ke"></td>
 * <td headers="ime">input method handling in JTextComponent
 * </tr>
 * <tr>
 * <td headers="stage">5.</td>
 * <td headers="ke ime" colspan=2>keymap handling using the current keymap</td>
 * </tr>
 * <tr>
 * <td headers="stage">6.</td>
 * <td headers="ke">keyboard handling in JComponent (e.g. accelerators,
 * component navigation, etc.)</td>
 * <td headers="ime"></td>
 * </tr>
 * </table>
 *
 * <p>
 * To maintain compatibility with applications that listen to key events but are
 * not aware of input method events, the input method handling in stage 4
 * provides a compatibility mode for components that do not process input method
 * events. For these components, the committed text is converted to keyTyped key
 * events and processed in the key event pipeline starting at stage 3 instead of
 * in the input method event pipeline.
 * <p>
 * By default the component will create a keymap (named <b>DEFAULT_KEYMAP</b>)
 * that is shared by all JTextComponent instances as the default keymap.
 * Typically a look-and-feel implementation will install a different keymap that
 * resolves to the default keymap for those bindings not found in the different
 * keymap. The minimal bindings include:
 * <ul>
 * <li>inserting content into the editor for the printable keys.
 * <li>removing content with the backspace and del keys.
 * <li>caret movement forward and backward
 * </ul>
 *
 * <p>
 * <dt><b><font size=+1>Model/View Split</font></b>
 * <dd>The text components have a model-view split. A text component pulls
 * together the objects used to represent the model, view, and controller. The
 * text document model may be shared by other views which act as observers of
 * the model (e.g. a document may be shared by multiple components).
 *
 * <p align=center>
 * <img src="doc-files/editor.gif" alt="Diagram showing interaction between
 * Controller, Document, events, and ViewFactory" HEIGHT=358 WIDTH=587>
 * </p>
 *
 * <p>
 * The model is defined by the {@link Document} interface. This is intended to
 * provide a flexible text storage mechanism that tracks change during edits and
 * can be extended to more sophisticated models. The model interfaces are meant
 * to capture the capabilities of expression given by SGML, a system used to
 * express a wide variety of content. Each modification to the document causes
 * notification of the details of the change to be sent to all observers in the
 * form of a {@link DocumentEvent} which allows the views to stay up to date
 * with the model. This event is sent to observers that have implemented the
 * {@link DocumentListener} interface and registered interest with the model
 * being observed.
 *
 * <p>
 * <dt><b><font size=+1>Location Information</font></b>
 * <dd>The capability of determining the location of text in the view is
 * provided. There are two methods, {@link #modelToView} and
 * {@link #viewToModel} for determining this information.
 *
 * <p>
 * <dt><b><font size=+1>Undo/Redo support</font></b>
 * <dd>Support for an edit history mechanism is provided to allow undo/redo
 * operations. The text component does not itself provide the history buffer by
 * default, but does provide the <code>UndoableEdit</code> records that can be
 * used in conjunction with a history buffer to provide the undo/redo support.
 * The support is provided by the Document model, which allows one to attach
 * UndoableEditListener implementations.
 *
 * <p>
 * <dt><b><font size=+1>Thread Safety</font></b>
 * <dd>The swing text components provide some support of thread safe operations.
 * Because of the high level of configurability of the text components, it is
 * possible to circumvent the protection provided. The protection primarily
 * comes from the model, so the documentation of <code>AbstractDocument</code>
 * describes the assumptions of the protection provided. The methods that are
 * safe to call asynchronously are marked with comments.
 *
 * <p>
 * <dt><b><font size=+1>Newlines</font></b>
 * <dd>For a discussion on how newlines are handled, see
 * <a href="DefaultEditorKit.html">DefaultEditorKit</a>.
 *
 * <p>
 * <dt><b><font size=+1>Printing support</font></b>
 * <dd>Several {@link #print print} methods are provided for basic document
 * printing. If more advanced printing is needed, use the {@link #getPrintable}
 * method.
 * </dl>
 *
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @beaninfo attribute: isContainer false
 *
 * @author Timothy Prinzing
 * @author Igor Kushnirskiy (printing support)
 * @see Document
 * @see DocumentEvent
 * @see DocumentListener
 * @see Caret
 * @see CaretEvent
 * @see CaretListener
 * @see TextUI
 * @see View
 * @see ViewFactory
 */
public abstract class JTextComponent extends JComponent implements Scrollable, Accessible {
	protected final LinearLayout defaultLinearLayout;
	protected EditText editText;

	public JTextComponent() {
		super();
		AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				editText = new EditText(ActivityManager.applicationContext);
			}
		});

		model = new DefaultStyledDocument(null, new StyleContext());

		if (this instanceof JTextArea) {
			editText.setTextIsSelectable(true);
			editText.setHorizontallyScrolling(true);
			editText.setCursorVisible(true);
			setTextBySpanBuilder("");
		} else {
			editText.setText("");
		}

		// 如果标签里有链接要加上这句，否则链接无效。如setSpan(new URLSpan(
		editText.setMovementMethod(LinkMovementMethod.getInstance());

		// editText.setText(((DefaultStyledDocument)model).getSpanableContentAdAPI().spanBuilder,
		// TextView.BufferType.EDITABLE);

		highlighter.install(this);// 注意：此行要置于setText(spanBuilder之后

		// refreshToEditText();
		((DefaultStyledDocument) model).setEditTextAdAPI(editText);
		UICore.setTextSize(editText, getScreenAdapterAdAPI());
		AndroidUIUtil.buildListenersForComponent(editText, this);

		editText.addTextChangedListener(new TextWatcher() {
			int modifyCount;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (PropertiesManager.isSimu()) {
					System.out.println("[onTextChanged start : " + start + ", before : " + before
							+ ", count : " + count + ", CharSequence : " + s + "]");
				}
				final int sLen = s.length();
				if (sLen == 0 || sLen <= start) {
					return;
				}
				try {
					if (before > 0) {
						model.remove(start, modifyCount);
					}

					if (count > 0) {
						model.insertString(start, s.subSequence(start, start + count).toString(),
								null);
					}
					// System.out.println("Result : " +
					// JTextComponent.this.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
				KeyListener[] listeners = JTextComponent.this.getKeyListeners();
				if (listeners != null && listeners.length > 0) {
					// DebugLogger.warning("create KeyEvent for " +
					// "[onTextChanged start : "+start+", before : "+before+",
					// count : "+count+"]");
					long curMS = System.currentTimeMillis();
					char keyCode = s.charAt(start);
					for (int i = 0; i < listeners.length; i++) {
						KeyEvent event = new KeyEvent(JTextComponent.this, KeyEvent.KEY_RELEASED,
								curMS, 0, keyCode);
						listeners[i].keyTyped(event);
					}
				}

				trigChangedUpdate(sLen);
			}

			@Override
			public void beforeTextChanged(CharSequence s, final int start, int count, int after) {
				if (PropertiesManager.isSimu()) {
					LogManager.log("[beforeTextChanged start : " + start + ", after : " + after
							+ ", count : " + count + ", CharSequence : " + s + "]");
				}

				int sLen = s.length();
				if (sLen == 0 || sLen <= start) {
					return;
				}
				modifyCount = count;
				KeyListener[] listeners = JTextComponent.this.getKeyListeners();
				if (listeners != null && listeners.length > 0) {
					// DebugLogger.warning("create KeyEvent for " +
					// "[beforeTextChanged start : "+start+", after : "+after+",
					// count : "+count+"]");
					long curMS = System.currentTimeMillis();
					char keyCode = s.charAt(start);
					for (int i = 0; i < listeners.length; i++) {
						KeyEvent event = new KeyEvent(JTextComponent.this, KeyEvent.KEY_PRESSED,
								curMS, 0, keyCode);
						listeners[i].keyTyped(event);
					}
				}

				CaretEvent c = new CaretEvent(JTextComponent.this) {
					@Override
					public int getMark() {
						return 0;
					}

					@Override
					public int getDot() {
						return start;
					}
				};
				JTextComponent.this.fireCaretUpdate(c);
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (PropertiesManager.isSimu()) {
					LogManager.log("[afterTextChanged Editable : " + s + "]");
				}
				KeyListener[] listeners = JTextComponent.this.getKeyListeners();
				if (listeners != null && listeners.length > 0) {
					// DebugLogger.warning("create KeyEvent for " +
					// "[afterTextChanged]");
					for (int i = 0; i < listeners.length; i++) {
						KeyEvent event = new KeyEvent(JTextComponent.this, KeyEvent.KEY_TYPED,
								System.currentTimeMillis(), 0, 0);
						listeners[i].keyTyped(event);
					}
				}
			}
		});
		editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, android.view.KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager imm = (InputMethodManager) v.getContext()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					return true;
				} else if (actionId == EditorInfo.IME_ACTION_NEXT) {
					int id = editText.getNextFocusDownId();
					if (id != android.view.View.NO_ID) {
						ActivityManager.activity.findViewById(id).requestFocus();
						return true;
					}
					// InputMethodManager imm =
					// (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					// imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					// sendKeyCode(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
				}
				return false;
			}

		});

		defaultLinearLayout = new LinearLayout(ActivityManager.applicationContext);
		setPeerAdAPI(defaultLinearLayout);
		addOnLayoutChangeListenerAdAPI(defaultLinearLayout);

		{
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
			AndroidUIUtil.addView(defaultLinearLayout, editText, lp, viewRelation);

			editText.setTextColor(isEnable ? AndroidUIUtil.WIN_FONT_COLOR.toAndroid()
					: AndroidUIUtil.WIN_FONT_DISABLE_COLOR.toAndroid());
			UICore.setTextSize(editText, getFont(), getScreenAdapterAdAPI());
		}
	}

	public View getFocusablePeerViewAdAPI() {
		return editText;
	}

	// private void refreshToEditText() {
	// editText.setText(((DefaultStyledDocument)model).getSpanableContentAdAPI().spanBuilder,
	// TextView.BufferType.SPANNABLE);
	// }

	public void applyComponentOrientation(final ComponentOrientation o) {
		super.applyComponentOrientation(o);

		// AndroidUIUtil.runOnUiThread(new Runnable() {
		// public void run(){
		// editText.setGravity(o.isLeftToRight()?Gravity.LEFT:Gravity.RIGHT);
		// if(JTextComponent.this instanceof JEditorPane){
		// editText.setGravity(editText.getGravity() |
		// Gravity.TOP);//继承时，//setHorizontalAlignment已有
		// }
		// }
		// });
	}

	public android.text.Spannable getSpannableAdAPI() {
		Editable editable = editText.getText();
		if (editable instanceof android.text.Spannable) {
			return (android.text.Spannable) editable;
		}
		return null;
	}

	public TextUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setUI(TextUI ui) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void updateUI() {
		super.updateUI();
	}

	public void addCaretListener(CaretListener listener) {
		list.add(CaretListener.class, listener);
	}

	public void removeCaretListener(CaretListener listener) {
		list.remove(CaretListener.class, listener);
	}

	public CaretListener[] getCaretListeners() {
		return list.getListeners(CaretListener.class);
	}

	protected void fireCaretUpdate(CaretEvent e) {
		CaretListener[] carets = JTextComponent.this.getCaretListeners();
		if (carets != null) {
			for (int i = 0; i < carets.length; i++) {
				carets[i].caretUpdate(e);
			}
		}
	}

	public void setDocument(Document doc) {
		model = doc;
	}

	public Document getDocument() {
		return model;
	}

	public void setComponentOrientation(ComponentOrientation o) {
		super.setComponentOrientation(o);
	}

	public Action[] getActions() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public void setMargin(Insets m) {
		AndroidClassUtil.callEmptyMethod();
	}

	public Insets getMargin() {
		return margin;
	}

	public void setNavigationFilter(NavigationFilter filter) {
		AndroidClassUtil.callEmptyMethod();
	}

	public NavigationFilter getNavigationFilter() {
		return navigationFilter;
	}

	public Caret getCaret() {
		return caret;
	}

	public void setCaret(Caret c) {
		AndroidClassUtil.callEmptyMethod();
	}

	public Highlighter getHighlighter() {
		return highlighter;
	}

	public void setHighlighter(Highlighter h) {
		highlighter = h;
	}

	public void setKeymap(Keymap map) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void setDragEnabled(boolean b) {
		AndroidClassUtil.callEmptyMethod();
		dragEnabled = b;
	}

	public boolean getDragEnabled() {
		return dragEnabled;
	}

	public final void setDropMode(DropMode dropMode) {
		AndroidClassUtil.callEmptyMethod();
		this.dropMode = dropMode;
	}

	public final DropMode getDropMode() {
		return dropMode;
	}

	DropLocation dropLocationForPoint(Point p) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public final DropLocation getDropLocation() {
		return dropLocation;
	}

	void updateInputMap(Keymap oldKm, Keymap newKm) {
		AndroidClassUtil.callEmptyMethod();
	}

	public Keymap getKeymap() {
		return keymap;
	}

	public static Keymap addKeymap(String nm, Keymap parent) {
		return getKeymapTable().put(nm, parent);
	}

	public static Keymap removeKeymap(String nm) {
		return getKeymapTable().remove(nm);
	}

	public static Keymap getKeymap(String nm) {
		return getKeymapTable().get(nm);
	}

	private static HashMap<String, Keymap> getKeymapTable() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public static class KeyBinding {
		public KeyStroke key;
		public String actionName;

		public KeyBinding(KeyStroke key, String actionName) {
			this.key = key;
			this.actionName = actionName;
		}
	}

	public static void loadKeymap(Keymap map, KeyBinding[] bindings, Action[] actions) {
		AndroidClassUtil.callEmptyMethod();
	}

	private static Boolean isProcessInputMethodEventOverridden(Class<?> klass) {
		return Boolean.FALSE;
	}

	public Color getCaretColor() {
		AndroidClassUtil.callEmptyMethod();
		return caretColor;
	}

	public void setCaretColor(Color c) {
		AndroidClassUtil.callEmptyMethod();
	}

	public Color getSelectionColor() {
		AndroidClassUtil.callEmptyMethod();
		return selectionColor;
	}

	public void setSelectionColor(Color c) {
		AndroidClassUtil.callEmptyMethod();
	}

	public Color getSelectedTextColor() {
		AndroidClassUtil.callEmptyMethod();
		return selectedTextColor;
	}

	public void setSelectedTextColor(Color c) {
		AndroidClassUtil.callEmptyMethod();
	}

	public Color getDisabledTextColor() {
		AndroidClassUtil.callEmptyMethod();
		return disabledTextColor;
	}

	public void setDisabledTextColor(Color c) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void replaceSelection(String content) {
	}

	public String getText(int offs, int len) throws BadLocationException {
		return getDocument().getText(offs, len);
	}

	public Rectangle modelToView(int pos) throws BadLocationException {
		return getUI().modelToView(this, pos);
	}

	public int viewToModel(Point pt) {
		return getUI().viewToModel(this, pt);
	}

	public void cut() {
		copy();
		if (editText.isSelected()) {
			int startIdx = editText.getSelectionStart();
			int endIdx = editText.getSelectionEnd();
			Editable editable = editText.getText();
			if (startIdx > endIdx) {
				editable.delete(endIdx, startIdx - endIdx);
			} else {
				editable.delete(startIdx, endIdx - startIdx);
			}
		}
	}

	public void copy() {
		if (editText.isSelected()) {
			android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) ActivityManager
					.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboardManager.setPrimaryClip(ClipData.newPlainText(null, getSelectedText()));
		}
	}

	public void paste() {
		android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) ActivityManager
				.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE);
		if (clipboardManager.hasPrimaryClip()) {
			CharSequence s = clipboardManager.getPrimaryClip().getItemAt(0).getText();
			int startIdx = editText.getSelectionStart();
			int endIdx = editText.getSelectionEnd();
			if (startIdx > endIdx) {
				editText.append(s, endIdx, startIdx);
			} else {
				editText.append(s, startIdx, endIdx);
			}
		}
	}

	public void moveCaretPosition(int pos) {
		AndroidClassUtil.callEmptyMethod();
	}

	public static final String FOCUS_ACCELERATOR_KEY = "focusAcceleratorKey";

	public void setFocusAccelerator(char aKey) {
		AndroidClassUtil.callEmptyMethod();
		focusAccelerator = aKey;
	}

	public char getFocusAccelerator() {
		return focusAccelerator;
	}

	public void read(Reader in, Object desc) throws IOException {
		StringBuffer sb = new StringBuffer(2048);
		char[] chars = new char[512];
		int length = 0;
		while ((length = in.read(chars)) >= 0) {
			sb.append(chars, 0, length);
		}
		setText(sb.toString());
		// try {
		// model.insertString(0, sb.toString(), null);
		// } catch (BadLocationException e) {
		// e.printStackTrace();
		// }
	}

	public void write(Writer out) throws IOException {
	}

	public void removeNotify() {
	}

	public void setCaretPosition(final int position) {
		AndroidUIUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				editText.setSelection(position);
			}
		});
	}

	public int getCaretPosition() {
		return getSelectionStart();
	}

	public void setText(String t) {
		if (t == null) {
			t = "";
		}

		// if(t.equals(getTextInner())){//避免循环
		// return;
		// }

		final String snapText = t;
		// 不需要TEXT_CHANGED_PROPERTY
		AndroidUIUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (JTextComponent.this instanceof JTextArea) {
					setTextBySpanBuilder(snapText);

					highlighter.install(JTextComponent.this);
				} else {
					editText.setText(snapText);
				}
			}
		});
	}

	public String getText() {
		// if(currText == null){
		// return "";
		// }else{
		// return currText;
		// }
		return getTextInner();
	}

	private final String getTextInner() {
		return editText.getText().toString();
	}

	public void setFont(Font f) {
		super.setFont(f);
		if (f != null) {
			UICore.setTextSize(editText, getFont(), getScreenAdapterAdAPI());
		}
	}

	public String getSelectedText() {
		int startIdx = getSelectionStart();
		int endIdx = getSelectionEnd();
		if (startIdx > endIdx) {
			return editText.getText().subSequence(endIdx, startIdx).toString();
		} else {
			return editText.getText().subSequence(startIdx, endIdx).toString();
		}
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEnabled(boolean enable) {
		super.setEnabled(enable);
		editText.setTextColor(enable ? AndroidUIUtil.WIN_FONT_COLOR.toAndroid()
				: AndroidUIUtil.WIN_FONT_DISABLE_COLOR.toAndroid());
		editText.setEnabled(enable);
		editText.setFocusable(enable);
		editText.setClickable(enable);
	}

	android.text.method.KeyListener oldKeyListener;

	private void setShowSoftInputOnFocus(final EditText ed, final boolean isShow) {
		AndroidUIUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				int mode = isShow ? WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
						: WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
				ActivityManager.activity.getWindow().setSoftInputMode(mode);

				int currentVersion = android.os.Build.VERSION.SDK_INT;
				String methodName = null;
				if (currentVersion >= 16) {// 4.2
					methodName = "setShowSoftInputOnFocus";
				} else if (currentVersion >= 14) {// 4.0
					methodName = "setSoftInputShownOnFocus";
				}

				if (methodName == null) {
					setKeyNull(isShow);
				} else {
					Class<EditText> cls = EditText.class;
					Method setShowSoftInputOnFocus;
					try {
						setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
						setShowSoftInputOnFocus.setAccessible(true);
						setShowSoftInputOnFocus.invoke(ed, isShow);
					} catch (Exception e) {
						setKeyNull(isShow);
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void setEditable(boolean b) {
		boolean oldValue = editable;
		if (oldValue != b) {
			editable = b;

			firePropertyChange("editable", oldValue, b);
		}

		editText.setTextColor(editable ? AndroidUIUtil.WIN_FONT_COLOR.toAndroid()
				: AndroidUIUtil.WIN_FONT_DISABLE_COLOR.toAndroid());
		setKeyNull(b);

		// setShowSoftInputOnFocus(editText, b);
	}

	private void setKeyNull(boolean isShow) {
		if (isShow == false) {
			if (oldKeyListener == null) {
				oldKeyListener = editText.getKeyListener();
			}

			// 确保浏览License时，能上下移动
			editText.setKeyListener(new NumberKeyListener() {
				public int getInputType() {
					return InputType.TYPE_NULL;
				}

				protected char[] getAcceptedChars() {
					return new char[] {};
				}
			});
		} else {
			if (oldKeyListener != null) {
				editText.setKeyListener(oldKeyListener);
			}
		}
	}

	public int getSelectionStart() {
		if (editText.isSelected()) {
			return editText.getSelectionStart();
		} else {
			return 0;
		}
	}

	public void setSelectionStart(int selectionStart) {
		select(selectionStart, getSelectionEnd());
	}

	public int getSelectionEnd() {
		if (editText.isSelected()) {
			return editText.getSelectionEnd();
		} else {
			return 0;
		}
	}

	public void setSelectionEnd(int selectionEnd) {
		select(getSelectionStart(), selectionEnd);
	}

	public void select(final int selectionStart, final int selectionEnd) {
		AndroidUIUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				editText.setSelection(selectionStart, selectionEnd);
			}
		});
	}

	public void setToolTipText(String tip) {
		super.setToolTipText(tip);
	}

	public void selectAll() {
		editText.selectAll();
	}

	public String getToolTipText(MouseEvent event) {
		return super.getToolTipText(event);
	}

	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		AndroidClassUtil.callEmptyMethod();
		return 0;
	}

	public boolean getScrollableTracksViewportWidth() {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	public boolean print() throws PrinterException {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public boolean print(final MessageFormat headerFormat, final MessageFormat footerFormat)
			throws PrinterException {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	public boolean print(final MessageFormat headerFormat, final MessageFormat footerFormat,
			final boolean showPrintDialog, final PrintService service,
			final PrintRequestAttributeSet attributes, final boolean interactive)
			throws PrinterException {
		AndroidClassUtil.callEmptyMethod();
		return false;
	}

	final Callable<Object> doPrint = new Callable<Object>() {
		public Object call() throws Exception {
			return null;
		}
	};

	final FutureTask<Object> futurePrinting = new FutureTask<Object>(doPrint);

	final Runnable runnablePrinting = new Runnable() {
		public void run() {
		}
	};

	public Printable getPrintable(final MessageFormat headerFormat,
			final MessageFormat footerFormat) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
	}

	protected Document model;

	private final Caret caret = new Caret() {
		@Override
		public void setVisible(boolean v) {
		}

		@Override
		public void setSelectionVisible(boolean v) {
		}

		@Override
		public void setMagicCaretPosition(Point p) {
		}

		@Override
		public void setDot(int dot) {
		}

		@Override
		public void setBlinkRate(int rate) {
		}

		@Override
		public void removeChangeListener(ChangeListener l) {
		}

		@Override
		public void paint(Graphics g) {
		}

		@Override
		public void moveDot(int dot) {
		}

		@Override
		public boolean isVisible() {
			return false;
		}

		@Override
		public boolean isSelectionVisible() {
			return false;
		}

		@Override
		public void install(JTextComponent c) {
		}

		@Override
		public int getMark() {
			return 0;
		}

		@Override
		public Point getMagicCaretPosition() {
			return null;
		}

		@Override
		public int getDot() {
			return 0;
		}

		@Override
		public int getBlinkRate() {
			return 0;
		}

		@Override
		public void deinstall(JTextComponent c) {
		}

		@Override
		public void addChangeListener(ChangeListener l) {
		}
	};
	private NavigationFilter navigationFilter;
	private Highlighter highlighter = new DefaultHighlighter();
	private transient Keymap keymap;

	private transient MutableCaretEvent caretEvent;
	private Color caretColor;
	private Color selectionColor;
	private Color selectedTextColor;
	private Color disabledTextColor;
	private boolean editable = true;
	private Insets margin;
	private char focusAccelerator;
	private boolean dragEnabled;

	private DropMode dropMode = DropMode.USE_SELECTION;

	private transient DropLocation dropLocation;

	public static final class DropLocation extends TransferHandler.DropLocation {
		private final int index;
		private final Position.Bias bias;

		private DropLocation(Point p, int index, Position.Bias bias) {
			super(p);
			this.index = index;
			this.bias = bias;
		}

		public int getIndex() {
			return index;
		}

		public Position.Bias getBias() {
			return bias;
		}

		public String toString() {
			return "";
		}
	}

	private static DefaultTransferHandler defaultTransferHandler;

	private static Map<String, Boolean> overrideMap;

	protected String paramString() {
		return "";
	}

	static class DefaultTransferHandler extends TransferHandler implements UIResource {
		public void exportToClipboard(JComponent comp, Clipboard clipboard, int action)
				throws IllegalStateException {
		}

		public boolean importData(JComponent comp, Transferable t) {
			return false;
		}

		public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
			return false;
		}

		public int getSourceActions(JComponent c) {
			return NONE;
		}

		private DataFlavor getFlavor(DataFlavor[] flavors) {
			return null;
		}
	}

	static final JTextComponent getFocusedComponent() {
		return null;
	}

	private int getCurrentEventModifiers() {
		return 0;
	}

	private static final Object KEYMAP_TABLE = new StringBuilder("JTextComponent_KeymapTable");

	private String composedTextContent;
	private Position composedTextStart;
	private Position composedTextEnd;
	private Position latestCommittedTextStart;
	private Position latestCommittedTextEnd;
	private ComposedTextCaret composedTextCaret;
	private transient Caret originalCaret;
	private boolean checkedInputOverride;
	private boolean needToSendKeyTypedEvent;

	static class DefaultKeymap implements Keymap {

		DefaultKeymap(String nm, Keymap parent) {
			this.nm = nm;
			this.parent = parent;
			bindings = new Hashtable<KeyStroke, Action>();
		}

		public Action getDefaultAction() {
			return null;
		}

		public void setDefaultAction(Action a) {
		}

		public String getName() {
			return nm;
		}

		public Action getAction(KeyStroke key) {
			return null;
		}

		public KeyStroke[] getBoundKeyStrokes() {
			KeyStroke[] keys = new KeyStroke[0];
			return keys;
		}

		public Action[] getBoundActions() {
			Action[] actions = new Action[0];
			return actions;
		}

		public KeyStroke[] getKeyStrokesForAction(Action a) {
			return null;
		}

		public boolean isLocallyDefined(KeyStroke key) {
			return bindings.containsKey(key);
		}

		public void addActionForKeyStroke(KeyStroke key, Action a) {
		}

		public void removeKeyStrokeBinding(KeyStroke key) {
		}

		public void removeBindings() {
		}

		public Keymap getResolveParent() {
			return parent;
		}

		public void setResolveParent(Keymap parent) {
			this.parent = parent;
		}

		public String toString() {
			return "Keymap[" + nm + "]" + bindings;
		}

		String nm;
		Keymap parent;
		Hashtable<KeyStroke, Action> bindings;
		Action defaultAction;
	}

	static class KeymapWrapper extends InputMap {
		static final Object DefaultActionKey = new Object();

		private Keymap keymap;

		KeymapWrapper(Keymap keymap) {
			this.keymap = keymap;
		}

		public KeyStroke[] keys() {
			return null;
		}

		public int size() {
			return 0;
		}

		public Object get(KeyStroke keyStroke) {
			return null;
		}
	}

	static class KeymapActionMap extends ActionMap {
		private Keymap keymap;

		KeymapActionMap(Keymap keymap) {
			this.keymap = keymap;
		}

		public Object[] keys() {
			return null;
		}

		public int size() {
			return 0;
		}

		public Action get(Object key) {
			return null;
		}
	}

	private static final Object FOCUSED_COMPONENT = new StringBuilder(
			"JTextComponent_FocusedComponent");

	public static final String DEFAULT_KEYMAP = "default";

	static class MutableCaretEvent extends CaretEvent
			implements ChangeListener, FocusListener, MouseListener {

		MutableCaretEvent(JTextComponent c) {
			super(c);
		}

		final void fire() {
		}

		public final String toString() {
			return "dot=" + dot + "," + "mark=" + mark;
		}

		public final int getDot() {
			return dot;
		}

		public final int getMark() {
			return mark;
		}

		public final void stateChanged(ChangeEvent e) {
		}

		public void focusGained(FocusEvent fe) {
		}

		public void focusLost(FocusEvent fe) {
		}

		public final void mousePressed(MouseEvent e) {
		}

		public final void mouseReleased(MouseEvent e) {
		}

		public final void mouseClicked(MouseEvent e) {
		}

		public final void mouseEntered(MouseEvent e) {
		}

		public final void mouseExited(MouseEvent e) {
		}

		private boolean dragActive;
		private int dot;
		private int mark;
	}

	protected void processInputMethodEvent(InputMethodEvent e) {
	}

	public InputMethodRequests getInputMethodRequests() {
		return null;
	}

	public void addInputMethodListener(InputMethodListener l) {
	}

	class InputMethodRequestsHandler implements InputMethodRequests, DocumentListener {
		public AttributedCharacterIterator cancelLatestCommittedText(Attribute[] attributes) {
			return null;
		}

		public AttributedCharacterIterator getCommittedText(int beginIndex, int endIndex,
				Attribute[] attributes) {
			return null;
		}

		public int getCommittedTextLength() {
			return 0;
		}

		public int getInsertPositionOffset() {
			return 0;
		}

		public TextHitInfo getLocationOffset(int x, int y) {
			return null;
		}

		public Rectangle getTextLocation(TextHitInfo offset) {
			return null;
		}

		public AttributedCharacterIterator getSelectedText(Attribute[] attributes) {
			return null;
		}

		public void changedUpdate(DocumentEvent e) {
		}

		public void insertUpdate(DocumentEvent e) {
		}

		public void removeUpdate(DocumentEvent e) {
		}
	}

	private void replaceInputMethodText(InputMethodEvent e) {
	}

	private void createComposedTextAttribute(int composedIndex, AttributedCharacterIterator text) {
	}

	protected boolean saveComposedText(int pos) {
		return false;
	}

	protected void restoreComposedText() {
	}

	private void mapCommittedTextToAction(String committedText) {
	}

	private void setInputMethodCaretPosition(InputMethodEvent e) {
	}

	private void exchangeCaret(Caret oldCaret, Caret newCaret) {
	}

	private boolean shouldSynthensizeKeyEvents() {
		return false;
	}

	private boolean isProcessInputMethodEventOverridden() {
		return false;
	}

	boolean composedTextExists() {
		return (composedTextStart != null);
	}

	public final void setTextBySpanBuilder(final String snapText) {
		final SpannableStringBuilder spanBuilder = ((DefaultStyledDocument) model)
				.getSpanableContentAdAPI().spanBuilder;
		spanBuilder.clear();
		spanBuilder.append(snapText);
		editText.setText(spanBuilder, TextView.BufferType.EDITABLE);
	}

	private final void trigChangedUpdate(final int sLen) {
		((AbstractDocument) model).fireChangedUpdate(new DocumentEvent() {
			@Override
			public EventType getType() {
				return EventType.CHANGE;
			}

			@Override
			public int getOffset() {
				return 0;
			}

			@Override
			public int getLength() {
				return sLen;
			}

			@Override
			public Document getDocument() {
				return model;
			}

			@Override
			public ElementChange getChange(Element elem) {
				return null;
			}
		});
	}

	class ComposedTextCaret extends DefaultCaret implements Serializable {
		Color bg;

		public void install(JTextComponent c) {
		}

		public void paint(Graphics g) {
		}

		protected void positionCaret(MouseEvent me) {
		}
	}

	private class DoSetCaretPosition implements Runnable {
		JTextComponent host;
		Position newPos;

		DoSetCaretPosition(JTextComponent host, Position newPos) {
			this.host = host;
			this.newPos = newPos;
		}

		public void run() {
			host.setCaretPosition(newPos.getOffset());
		}
	}
}
