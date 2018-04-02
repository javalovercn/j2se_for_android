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
package javax.swing;

import hc.android.AndroidClassUtil;
import hc.android.AndroidUIUtil;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.accessibility.AccessibleContext;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import android.widget.RadioButton;

/**
 * <code>JTextField</code> is a lightweight component that allows the editing of
 * a single line of text. For information on and examples of using text fields,
 * see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/textfield.html">How
 * to Use Text Fields</a> in <em>The Java Tutorial.</em>
 *
 * <p>
 * <code>JTextField</code> is intended to be source-compatible with
 * <code>java.awt.TextField</code> where it is reasonable to do so. This
 * component has capabilities not found in the <code>java.awt.TextField</code>
 * class. The superclass should be consulted for additional capabilities.
 * <p>
 * <code>JTextField</code> has a method to establish the string used as the
 * command string for the action event that gets fired. The
 * <code>java.awt.TextField</code> used the text of the field as the command
 * string for the <code>ActionEvent</code>. <code>JTextField</code> will use the
 * command string set with the <code>setActionCommand</code> method if not
 * <code>null</code>, otherwise it will use the text of the field as a
 * compatibility with <code>java.awt.TextField</code>.
 * <p>
 * The method <code>setEchoChar</code> and <code>getEchoChar</code> are not
 * provided directly to avoid a new implementation of a pluggable look-and-feel
 * inadvertently exposing password characters. To provide password-like services
 * a separate class <code>JPasswordField</code> extends <code>JTextField</code>
 * to provide this service with an independently pluggable look-and-feel.
 * <p>
 * The <code>java.awt.TextField</code> could be monitored for changes by adding
 * a <code>TextListener</code> for <code>TextEvent</code>'s. In the
 * <code>JTextComponent</code> based components, changes are broadcasted from
 * the model via a <code>DocumentEvent</code> to <code>DocumentListeners</code>.
 * The <code>DocumentEvent</code> gives the location of the change and the kind
 * of change if desired. The code fragment might look something like:
 * 
 * <pre>
 * <code>
 * &nbsp;   DocumentListener myListener = ??;
 * &nbsp;   JTextField myArea = ??;
 * &nbsp;   myArea.getDocument().addDocumentListener(myListener);
 * </code>
 * </pre>
 * <p>
 * The horizontal alignment of <code>JTextField</code> can be set to be left
 * justified, leading justified, centered, right justified or trailing
 * justified. Right/trailing justification is useful if the required size of the
 * field text is smaller than the size allocated to it. This is determined by
 * the <code>setHorizontalAlignment</code> and
 * <code>getHorizontalAlignment</code> methods. The default is to be leading
 * justified.
 * <p>
 * How the text field consumes VK_ENTER events depends on whether the text field
 * has any action listeners. If so, then VK_ENTER results in the listeners
 * getting an ActionEvent, and the VK_ENTER event is consumed. This is
 * compatible with how AWT text fields handle VK_ENTER events. If the text field
 * has no action listeners, then as of v 1.3 the VK_ENTER event is not consumed.
 * Instead, the bindings of ancestor components are processed, which enables the
 * default button feature of JFC/Swing to work.
 * <p>
 * Customized fields can easily be created by extending the model and changing
 * the default model provided. For example, the following piece of code will
 * create a field that holds only upper case characters. It will work even if
 * text is pasted into from the clipboard or it is altered via programmatic
 * changes.
 * 
 * <pre>
 * <code>

&nbsp;public class UpperCaseField extends JTextField {
&nbsp;
&nbsp;    public UpperCaseField(int cols) {
&nbsp;        super(cols);
&nbsp;    }
&nbsp;
&nbsp;    protected Document createDefaultModel() {
&nbsp;        return new UpperCaseDocument();
&nbsp;    }
&nbsp;
&nbsp;    static class UpperCaseDocument extends PlainDocument {
&nbsp;
&nbsp;        public void insertString(int offs, String str, AttributeSet a)
&nbsp;            throws BadLocationException {
&nbsp;
&nbsp;            if (str == null) {
&nbsp;                return;
&nbsp;            }
&nbsp;            char[] upper = str.toCharArray();
&nbsp;            for (int i = 0; i < upper.length; i++) {
&nbsp;                upper[i] = Character.toUpperCase(upper[i]);
&nbsp;            }
&nbsp;            super.insertString(offs, new String(upper), a);
&nbsp;        }
&nbsp;    }
&nbsp;}

 * </code>
 * </pre>
 * <p>
 * <strong>Warning:</strong> Swing is not thread safe. For more information see
 * <a href="package-summary.html#threading">Swing's Threading Policy</a>.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @beaninfo attribute: isContainer false description: A component which allows
 *           for the editing of a single line of text.
 *
 * @author Timothy Prinzing
 * @see #setActionCommand
 * @see JPasswordField
 * @see #addActionListener
 */
public class JTextField extends JTextComponent implements SwingConstants {
	public JTextField() {
		this(null, null, 15);
	}

	public JTextField(String text) {
		this(null, text, 15);
	}

	public JTextField(int columns) {
		this(null, null, columns);
	}

	public JTextField(String text, int columns) {
		this(null, text, columns);
	}

	public JTextField(Document doc, String text, int columns) {
		setColumns(columns);
		editText.setLines(1);
		editText.setMaxWidth(2048);
		super.setText(text);
		setHorizontalAlignment(LEADING);
		// 保留以下代码，采用系统缺省，以与JTextArea一致
		// editText.setBackgroundResource(HCRUtil.getResource(HCRUtil.R_drawable_textfield));
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public void setDocument(Document doc) {
		AndroidClassUtil.callEmptyMethod();
	}

	@Override
	public boolean isValidateRoot() {
		return false;
	}

	public int getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void applyComponentOrientation(ComponentOrientation o) {
		super.applyComponentOrientation(o);
		setHorizontalAlignment(getHorizontalAlignment());
	}

	public void setHorizontalAlignment(final int alignment) {
		if (alignment == horizontalAlignment)
			return;
		if ((alignment == LEFT) || (alignment == CENTER) || (alignment == RIGHT)
				|| (alignment == LEADING) || (alignment == TRAILING)) {
			horizontalAlignment = alignment;
			if (editText != null) {
				AndroidUIUtil.setViewHorizontalAlignment(this, editText, alignment, false);
			}
		} else {
			throw new IllegalArgumentException("horizontalAlignment");
		}
	}

	protected Document createDefaultModel() {
		return new DefaultStyledDocument();
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		if (columns == 0) {
			return;
		}
		if (columns < 0) {
			columns = 10;
		}
		this.columns = columns;

		editText.setMinWidth(getColumnWidth());
	}

	protected int getColumnWidth() {
		if (columns == 0) {
			return 160;
		} else {
			FontMetrics metrics = getFontMetrics(getFont());
			return columns * metrics.charWidth('m');
		}
	}

	public Dimension getPreferredSize() {
		return super.getPreferredSize();
	}

	public synchronized void addActionListener(ActionListener l) {
		list.add(ActionListener.class, l);
	}

	public synchronized void removeActionListener(ActionListener l) {
		if ((l != null) && (getAction() == l)) {
			setAction(null);
		} else {
			list.remove(ActionListener.class, l);
		}
	}

	public synchronized ActionListener[] getActionListeners() {
		return list.getListeners(ActionListener.class);
	}

	protected void fireActionPerformed() {
		ActionListener[] listeners = list.getListeners(ActionListener.class);
		int modifiers = 0;
		// AWTEvent currentEvent = EventQueue.getCurrentEvent();
		// if (currentEvent instanceof InputEvent) {
		// modifiers = ((InputEvent) currentEvent).getModifiers();
		// } else if (currentEvent instanceof ActionEvent) {
		// modifiers = ((ActionEvent) currentEvent).getModifiers();
		// }
		ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
				(command != null) ? command : getText(), EventQueue.getMostRecentEventTime(),
				modifiers);

		for (int i = 0; i < listeners.length; i++) {
			listeners[i].actionPerformed(e);
		}
	}

	public void setActionCommand(String command) {
		this.command = command;
	}

	private Action action;
	private PropertyChangeListener actionPropertyChangeListener;

	public void setAction(Action a) {
		Action oldValue = getAction();
		if (action == null || !action.equals(a)) {
			action = a;
			if (oldValue != null) {
				removeActionListener(oldValue);
			}
			if (action != null) {
				addActionListener(action);
			}
		}
	}

	public Action getAction() {
		return action;
	}

	protected void configurePropertiesFromAction(Action a) {
		AbstractAction.setEnabledFromAction(this, a);
		AbstractAction.setToolTipTextFromAction(this, a);
		setActionCommandFromAction(a);
	}

	protected void actionPropertyChanged(Action action, String propertyName) {
		AndroidClassUtil.callEmptyMethod();
	}

	private void setActionCommandFromAction(Action action) {
		setActionCommand(
				(action == null) ? null : (String) action.getValue(Action.ACTION_COMMAND_KEY));
	}

	protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
		return new TextFieldActionPropertyChangeListener(this, a);
	}

	private static class TextFieldActionPropertyChangeListener
			extends ActionPropertyChangeListener<JTextField> {
		TextFieldActionPropertyChangeListener(JTextField tf, Action a) {
			super(tf, a);
		}

		protected void actionPropertyChanged(JTextField textField, Action action,
				PropertyChangeEvent e) {
			if (AbstractAction.shouldReconfigure(e)) {
				textField.configurePropertiesFromAction(action);
			} else {
				textField.actionPropertyChanged(action, e.getPropertyName());
			}
		}
	}

	public Action[] getActions() {
		return TextAction.augmentList(super.getActions(), defaultActions);
	}

	public void postActionEvent() {
		fireActionPerformed();
	}

	public BoundedRangeModel getHorizontalVisibility() {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public int getScrollOffset() {
		return 0;
	}
	
	boolean isDisableFieldHint = false;
	
	public final void setDisableFieldHintAD(final boolean isDisableFieldHint) {
		this.isDisableFieldHint = isDisableFieldHint;
	}

	public void setToolTipText(final String tip) {
		if (isDisableFieldHint || AndroidUIUtil.containsHTML(tip)) {
			super.setToolTipText(tip);// 弹出提示式，而非原生hint
		} else {
			if(isDisableFieldHint) {//会导致Field过宽
				return;
			}
			
			AndroidUIUtil.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					editText.setHint(tip);
				}
			});
		}
	}

	public void setScrollOffset(int scrollOffset) {
		AndroidClassUtil.callEmptyMethod();
	}

	public void scrollRectToVisible(Rectangle r) {
		AndroidClassUtil.callEmptyMethod();
	}

	boolean hasActionListener() {
		return list.getListenerCount(ActionListener.class) > 0;
	}

	public static final String notifyAction = "notify-field-accept";

	private int horizontalAlignment = 0;// LEADING 参见构造方法
	private int columns;
	private String command;

	private static final Action[] defaultActions = { new NotifyAction() };

	private static final String uiClassID = "TextFieldUI";

	static class NotifyAction extends TextAction {

		NotifyAction() {
			super(notifyAction);
		}

		public void actionPerformed(ActionEvent e) {
			JTextComponent target = getFocusedComponent();
			if (target instanceof JTextField) {
				JTextField field = (JTextField) target;
				field.postActionEvent();
			}
		}

		public boolean isEnabled() {
			JTextComponent target = getFocusedComponent();
			if (target instanceof JTextField) {
				return ((JTextField) target).hasActionListener();
			}
			return false;
		}
	}

	class ScrollRepainter implements ChangeListener, Serializable {

		public void stateChanged(ChangeEvent e) {
			repaint();
		}

	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	protected String paramString() {
		return "";
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

}
