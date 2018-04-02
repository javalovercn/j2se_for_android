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

import hc.App;
import hc.android.AndroidClassUtil;
import hc.android.AndroidUIUtil;
import hc.server.ui.ClientDesc;
import hc.util.ResourceUtil;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.security.PrivilegedAction;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.plaf.OptionPaneUI;

import android.graphics.drawable.Drawable;

/**
 * <code>JOptionPane</code> makes it easy to pop up a standard dialog box that
 * prompts users for a value or informs them of something. For information about
 * using <code>JOptionPane</code>, see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/dialog.html">How
 * to Make Dialogs</a>, a section in <em>The Java Tutorial</em>.
 *
 * <p>
 *
 * While the <code>JOptionPane</code> class may appear complex because of the
 * large number of methods, almost all uses of this class are one-line calls to
 * one of the static <code>showXxxDialog</code> methods shown below:
 * <blockquote>
 *
 *
 * <table border=1 summary="Common JOptionPane method names and their
 * descriptions">
 * <tr>
 * <th>Method Name</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>showConfirmDialog</td>
 * <td>Asks a confirming question, like yes/no/cancel.</td>
 * </tr>
 * <tr>
 * <td>showInputDialog</td>
 * <td>Prompt for some input.</td>
 * </tr>
 * <tr>
 * <td>showMessageDialog</td>
 * <td>Tell the user about something that has happened.</td>
 * </tr>
 * <tr>
 * <td>showOptionDialog</td>
 * <td>The Grand Unification of the above three.</td>
 * </tr>
 * </table>
 *
 * </blockquote> Each of these methods also comes in a
 * <code>showInternalXXX</code> flavor, which uses an internal frame to hold the
 * dialog box (see {@link JInternalFrame}). Multiple convenience methods have
 * also been defined -- overloaded versions of the basic methods that use
 * different parameter lists.
 * <p>
 * All dialogs are modal. Each <code>showXxxDialog</code> method blocks the
 * caller until the user's interaction is complete.
 * <p>
 *
 * <table cellspacing=6 cellpadding=4 border=0 align=right summary="layout">
 * <tr>
 * <td bgcolor=#FFe0d0 rowspan=2>icon</td>
 * <td bgcolor=#FFe0d0>message</td>
 * </tr>
 * <tr>
 * <td bgcolor=#FFe0d0>input value</td>
 * </tr>
 * <tr>
 * <td bgcolor=#FFe0d0 colspan=2>option buttons</td>
 * </tr>
 * </table>
 *
 * The basic appearance of one of these dialog boxes is generally similar to the
 * picture at the right, although the various look-and-feels are ultimately
 * responsible for the final result. In particular, the look-and-feels will
 * adjust the layout to accommodate the option pane's
 * <code>ComponentOrientation</code> property. <br clear=all>
 * <p>
 * <b>Parameters:</b><br>
 * The parameters to these methods follow consistent patterns: <blockquote> <dl
 * compact>
 * <dt>parentComponent
 * <dd>Defines the <code>Component</code> that is to be the parent of this
 * dialog box. It is used in two ways: the <code>Frame</code> that contains it
 * is used as the <code>Frame</code> parent for the dialog box, and its screen
 * coordinates are used in the placement of the dialog box. In general, the
 * dialog box is placed just below the component. This parameter may be
 * <code>null</code>, in which case a default <code>Frame</code> is used as the
 * parent, and the dialog will be centered on the screen (depending on the L&F).
 * <dt><a name=message>message</a>
 * <dd>A descriptive message to be placed in the dialog box. In the most common
 * usage, message is just a <code>String</code> or <code>String</code> constant.
 * However, the type of this parameter is actually <code>Object</code>. Its
 * interpretation depends on its type: <dl compact>
 * <dt>Object[]
 * <dd>An array of objects is interpreted as a series of messages (one per
 * object) arranged in a vertical stack. The interpretation is recursive -- each
 * object in the array is interpreted according to its type.
 * <dt>Component
 * <dd>The <code>Component</code> is displayed in the dialog.
 * <dt>Icon
 * <dd>The <code>Icon</code> is wrapped in a <code>JLabel</code> and displayed
 * in the dialog.
 * <dt>others
 * <dd>The object is converted to a <code>String</code> by calling its
 * <code>toString</code> method. The result is wrapped in a <code>JLabel</code>
 * and displayed.
 * </dl>
 * <dt>messageType
 * <dd>Defines the style of the message. The Look and Feel manager may lay out
 * the dialog differently depending on this value, and will often provide a
 * default icon. The possible values are:
 * <ul>
 * <li><code>ERROR_MESSAGE</code>
 * <li><code>INFORMATION_MESSAGE</code>
 * <li><code>WARNING_MESSAGE</code>
 * <li><code>QUESTION_MESSAGE</code>
 * <li><code>PLAIN_MESSAGE</code>
 * </ul>
 * <dt>optionType
 * <dd>Defines the set of option buttons that appear at the bottom of the dialog
 * box:
 * <ul>
 * <li><code>DEFAULT_OPTION</code>
 * <li><code>YES_NO_OPTION</code>
 * <li><code>YES_NO_CANCEL_OPTION</code>
 * <li><code>OK_CANCEL_OPTION</code>
 * </ul>
 * You aren't limited to this set of option buttons. You can provide any buttons
 * you want using the options parameter.
 * <dt>options
 * <dd>A more detailed description of the set of option buttons that will appear
 * at the bottom of the dialog box. The usual value for the options parameter is
 * an array of <code>String</code>s. But the parameter type is an array of
 * <code>Objects</code>. A button is created for each object depending on its
 * type: <dl compact>
 * <dt>Component
 * <dd>The component is added to the button row directly.
 * <dt>Icon
 * <dd>A <code>JButton</code> is created with this as its label.
 * <dt>other
 * <dd>The <code>Object</code> is converted to a string using its
 * <code>toString</code> method and the result is used to label a
 * <code>JButton</code>.
 * </dl>
 * <dt>icon
 * <dd>A decorative icon to be placed in the dialog box. A default value for
 * this is determined by the <code>messageType</code> parameter.
 * <dt>title
 * <dd>The title for the dialog box.
 * <dt>initialValue
 * <dd>The default selection (input value).
 * </dl>
 * </blockquote>
 * <p>
 * When the selection is changed, <code>setValue</code> is invoked, which
 * generates a <code>PropertyChangeEvent</code>.
 * <p>
 * If a <code>JOptionPane</code> has configured to all input
 * <code>setWantsInput</code> the bound property
 * <code>JOptionPane.INPUT_VALUE_PROPERTY</code> can also be listened to, to
 * determine when the user has input or selected a value.
 * <p>
 * When one of the <code>showXxxDialog</code> methods returns an integer, the
 * possible values are:
 * <ul>
 * <li><code>YES_OPTION</code>
 * <li><code>NO_OPTION</code>
 * <li><code>CANCEL_OPTION</code>
 * <li><code>OK_OPTION</code>
 * <li><code>CLOSED_OPTION</code>
 * </ul>
 * <b>Examples:</b>
 * <dl>
 * <dt>Show an error dialog that displays the message, 'alert':
 * <dd><code>
 * JOptionPane.showMessageDialog(null, "alert", "alert", JOptionPane.ERROR_MESSAGE);
 * </code>
 * <p>
 * <dt>Show an internal information dialog with the message, 'information':
 * <dd><code>
 * JOptionPane.showInternalMessageDialog(frame, "information",<br>
 *             <ul><ul>"information", JOptionPane.INFORMATION_MESSAGE);</ul></ul>
 * </code>
 * <p>
 * <dt>Show an information panel with the options yes/no and message 'choose
 * one':
 * <dd><code>JOptionPane.showConfirmDialog(null,
 *             <ul><ul>"choose one", "choose one", JOptionPane.YES_NO_OPTION);</ul></ul>
 * </code>
 * <p>
 * <dt>Show an internal information dialog with the options yes/no/cancel and
 * message 'please choose one' and title information:
 * <dd><code>JOptionPane.showInternalConfirmDialog(frame,
 *             <ul><ul>"please choose one", "information",</ul></ul>
 *             <ul><ul>JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);</ul></ul>
 * </code>
 * <p>
 * <dt>Show a warning dialog with the options OK, CANCEL, title 'Warning', and
 * message 'Click OK to continue':
 * <dd><code>
 * Object[] options = { "OK", "CANCEL" };<br>
 * JOptionPane.showOptionDialog(null, "Click OK to continue", "Warning",
 *             <ul><ul>JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,</ul></ul>
 *             <ul><ul>null, options, options[0]);</ul></ul>
 * </code>
 * <p>
 * <dt>Show a dialog asking the user to type in a String:
 * <dd><code>
 * String inputValue = JOptionPane.showInputDialog("Please input a value");
 * </code>
 * <p>
 * <dt>Show a dialog asking the user to select a String:
 * <dd><code>
 * Object[] possibleValues = { "First", "Second", "Third" };<br>
 * Object selectedValue = JOptionPane.showInputDialog(null,
 *             <ul><ul>"Choose one", "Input",</ul></ul>
 *             <ul><ul>JOptionPane.INFORMATION_MESSAGE, null,</ul></ul>
 *             <ul><ul>possibleValues, possibleValues[0]);</ul></ul>
 * </code>
 * <p>
 * </dl>
 * <b>Direct Use:</b><br>
 * To create and use an <code>JOptionPane</code> directly, the standard pattern
 * is roughly as follows:
 * 
 * <pre>
 *     JOptionPane pane = new JOptionPane(<i>arguments</i>);
 *     pane.set<i>.Xxxx(...); // Configure</i>
 *     JDialog dialog = pane.createDialog(<i>parentComponent, title</i>);
 *     dialog.show();
 *     Object selectedValue = pane.getValue();
 *     if(selectedValue == null)
 *       return CLOSED_OPTION;
 *     <i>//If there is <b>not</b> an array of option buttons:</i>
 *     if(options == null) {
 *       if(selectedValue instanceof Integer)
 *          return ((Integer)selectedValue).intValue();
 *       return CLOSED_OPTION;
 *     }
 *     <i>//If there is an array of option buttons:</i>
 *     for(int counter = 0, maxCounter = options.length;
 *        counter < maxCounter; counter++) {
 *        if(options[counter].equals(selectedValue))
 *        return counter;
 *     }
 *     return CLOSED_OPTION;
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
 * @see JInternalFrame
 *
 * @beaninfo attribute: isContainer true description: A component which
 *           implements standard dialog box controls.
 *
 * @author James Gosling
 * @author Scott Violet
 */
public class JOptionPane extends JComponent implements Accessible {
	private static final String uiClassID = "OptionPaneUI";

	public static final Object UNINITIALIZED_VALUE = "uninitializedValue";

	public static final int DEFAULT_OPTION = -1;
	public static final int YES_NO_OPTION = 0;
	public static final int YES_NO_CANCEL_OPTION = 1;
	public static final int OK_CANCEL_OPTION = 2;

	public static final int YES_OPTION = 0;
	public static final int NO_OPTION = 1;
	public static final int CANCEL_OPTION = 2;
	public static final int OK_OPTION = 0;
	public static final int CLOSED_OPTION = -1;

	public static final int ERROR_MESSAGE = 0;
	public static final int INFORMATION_MESSAGE = 1;
	public static final int WARNING_MESSAGE = 2;
	public static final int QUESTION_MESSAGE = 3;
	public static final int PLAIN_MESSAGE = -1;

	public static final String ICON_PROPERTY = "icon";
	public static final String MESSAGE_PROPERTY = "message";
	public static final String VALUE_PROPERTY = "value";
	public static final String OPTIONS_PROPERTY = "options";
	public static final String INITIAL_VALUE_PROPERTY = "initialValue";
	public static final String MESSAGE_TYPE_PROPERTY = "messageType";
	public static final String OPTION_TYPE_PROPERTY = "optionType";
	public static final String SELECTION_VALUES_PROPERTY = "selectionValues";
	public static final String INITIAL_SELECTION_VALUE_PROPERTY = "initialSelectionValue";
	public static final String INPUT_VALUE_PROPERTY = "inputValue";
	public static final String WANTS_INPUT_PROPERTY = "wantsInput";

	transient protected Icon icon;
	transient protected Object message;
	private boolean useInnerOptions = false;
	transient protected Object[] options;
	transient protected Object initialValue;
	protected int messageType;

	private JPanel buttonPanel;
	private JTextField inputField;
	private JComboBox list;

	/**
	 * DEFAULT_OPTION, YES_NO_OPTION, YES_NO_CANCEL_OPTION, OK_CANCEL_OPTION.
	 */
	protected int optionType;
	transient protected Object value;
	protected transient Object[] selectionValues;
	protected transient Object inputValue;
	protected transient Object initialSelectionValue;
	protected boolean wantsInput;

	public static String showInputDialog(Object message) throws HeadlessException {
		return showInputDialog(null, message);
	}

	public static String showInputDialog(Object message, Object initialSelectionValue) {
		return showInputDialog(null, message, initialSelectionValue);
	}

	public static String showInputDialog(Component parentComponent, Object message)
			throws HeadlessException {
		return showInputDialog(parentComponent, message,
				UIManager.getString("OptionPane.inputDialogTitle", parentComponent),
				QUESTION_MESSAGE);
	}

	public static String showInputDialog(Component parentComponent, Object message,
			Object initialSelectionValue) {
		return (String) showInputDialog(parentComponent, message,
				UIManager.getString("OptionPane.inputDialogTitle", parentComponent),
				QUESTION_MESSAGE, null, null, initialSelectionValue);
	}

	public static String showInputDialog(Component parentComponent, Object message, String title,
			int messageType) throws HeadlessException {
		return (String) showInputDialog(parentComponent, message, title, messageType, null, null,
				null);
	}

	public static Object showInputDialog(Component parentComponent, Object message, String title,
			int messageType, Icon icon, Object[] selectionValues, Object initialSelectionValue)
			throws HeadlessException {
		if (title == null) {
			title = "Input";
		}

		final Window dialog = App.buildCloseableWindow(false, null, title, true);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JLabel lable = new JLabel(message.toString(), icon, SwingConstants.LEADING);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lable, c);

		c.gridy = 1;
		JComboBox select = null;
		JTextField field = null;
		if (selectionValues != null) {
			select = new JComboBox(selectionValues);
			select.setSelectedItem(initialSelectionValue);
			panel.add(select, c);
		} else {
			String initV = null;
			if (initialSelectionValue != null) {
				initV = initialSelectionValue.toString();
			}
			field = new JTextField(initV);
			panel.add(field, c);
		}

		final Boolean[] out = new Boolean[1];

		JButton ok = App.buildDefaultOKButton();
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				out[0] = Boolean.TRUE;
				dialog.dispose();
			}
		});
		JButton cancel = App.buildDefaultCancelButton();
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});

		JPanel btnPanel = new JPanel(new GridLayout(1, 2));
		btnPanel.add(cancel);
		btnPanel.add(ok);

		c.gridy = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(ClientDesc.hgap, ClientDesc.hgap, 0, ClientDesc.hgap);
		panel.add(btnPanel, c);

		App.showCenterPanelWindowWithoutButton(panel, 0, 0, dialog, null, false);

		if (out[0] != null && out[0]) {
			if (select != null) {
				return select.getSelectedItem();
			} else if (field != null) {
				return field.getText();
			}
			return null;
		} else {
			return null;
		}
	}

	public static void showMessageDialog(Component parentComponent, Object message)
			throws HeadlessException {
		showMessageDialog(parentComponent, message,
				UIManager.getString("OptionPane.messageDialogTitle", parentComponent),
				INFORMATION_MESSAGE);
	}

	public static void showMessageDialog(Component parentComponent, Object message, String title,
			int messageType) throws HeadlessException {
		showMessageDialog(parentComponent, message, title, messageType, null);
	}

	public static void showMessageDialog(Component parentComponent, Object message, String title,
			int messageType, Icon icon) throws HeadlessException {
		showOptionDialog(parentComponent, message, title, DEFAULT_OPTION, messageType, icon, null,
				null);
	}

	public static int showConfirmDialog(Component parentComponent, Object message)
			throws HeadlessException {
		return showConfirmDialog(parentComponent, message,
				UIManager.getString("OptionPane.titleText"), YES_NO_CANCEL_OPTION);
	}

	public static int showConfirmDialog(Component parentComponent, Object message, String title,
			int optionType) throws HeadlessException {
		return showConfirmDialog(parentComponent, message, title, optionType, QUESTION_MESSAGE);
	}

	public static int showConfirmDialog(Component parentComponent, Object message, String title,
			int optionType, int messageType) throws HeadlessException {
		return showConfirmDialog(parentComponent, message, title, optionType, messageType, null);
	}

	public static int showConfirmDialog(Component parentComponent, Object message, String title,
			int optionType, int messageType, Icon icon) throws HeadlessException {
		return showOptionDialog(parentComponent, message, title, optionType, messageType, icon,
				null, null);
	}

	public static int showOptionDialog(Component parentComponent, Object message, String title,
			final int optionType, int messageType, Icon icon, Object[] options, Object initialValue)
			throws HeadlessException {
		if (title == null) {
			title = "Options";
		}

		if (options == null) {
			options = getOptionTypeButtonNumAdAPI(optionType);
			initialValue = options[0];
		}

		final Window dialog = App.buildCloseableWindow(false, null, title, true);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JLabel lable = new JLabel(message.toString(), icon, SwingConstants.LEADING);
		c.anchor = GridBagConstraints.LINE_START;
		panel.add(lable, c);

		final Integer[] out = new Integer[1];
		out[0] = CLOSED_OPTION;

		final int length = options.length;
		JButton[] btns = new JButton[length];
		for (int i = 0; i < length; i++) {
			final String btnText = options[i].toString();
			final JButton btn = new JButton(btnText);
			final int currentIdx = i;
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (optionType == OK_CANCEL_OPTION && currentIdx == 1) {
						out[0] = CANCEL_OPTION;
					} else {
						out[0] = currentIdx;
					}
					dialog.dispose();
				}
			});
			if (btnText.equals(initialValue)) {
				btn.requestFocus();
			}
			btns[i] = btn;
		}
		JPanel btnPanel = new JPanel(new GridLayout(1, length));
		for (int i = length - 1; i >= 0; i--) {// 注意：0在最后
			btnPanel.add(btns[i]);
		}
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(ClientDesc.hgap, ClientDesc.hgap, 0, ClientDesc.hgap);
		panel.add(btnPanel, c);

		App.showCenterPanelWindowWithoutButton(panel, 0, 0, dialog, null, false);

		return out[0];
	}

	public JDialog createDialog(Component parentComponent, String title) throws HeadlessException {
		int style = styleFromMessageType(getMessageType());
		return createDialog(parentComponent, title, style);
	}

	public JDialog createDialog(String title) throws HeadlessException {
		int style = styleFromMessageType(getMessageType());
		JDialog dialog = new JDialog((Dialog) null, title, true);
		initDialog(dialog, style, null);
		return dialog;
	}

	private static final String[] YES_NO_OPTIONS = { (String) ResourceUtil.get(1032),
			(String) ResourceUtil.get(1033) };
	private static final String[] YES_NO_CANCEL_OPTIONS = { (String) ResourceUtil.get(1032),
			(String) ResourceUtil.get(1033), (String) ResourceUtil.get(1018) };
	private static final String[] OK_CANCEL_OPTIONS = { (String) ResourceUtil.get(1010),
			(String) ResourceUtil.get(1018) };
	private static final String[] DEFAULT_OPTIONS = { (String) ResourceUtil.get(1010) };

	private static Object[] getOptionTypeButtonNumAdAPI(final int optionType) {
		if (optionType == YES_NO_OPTION) {
			return YES_NO_OPTIONS;
		} else if (optionType == YES_NO_CANCEL_OPTION) {
			return YES_NO_CANCEL_OPTIONS;
		} else if (optionType == OK_CANCEL_OPTION) {
			return OK_CANCEL_OPTIONS;
		} else {
			// DEFAULT_OPTION
			return DEFAULT_OPTIONS;
		}
	}

	private JDialog createDialog(final Component parentComponent, String title, final int style)
			throws HeadlessException {
		final JDialog dialog = new JDialog((Frame) null, title, true);
		// Window window = JOptionPane.getWindowForComponent(parentComponent);
		initDialog(dialog, style, parentComponent);
		return dialog;
	}

	private void doSelectOrInputAdAPI(int selectedIdx, Dialog dialog) {
		if (wantsInput == false) {
			// OptionPane
			if (useInnerOptions) {
				if (isOKOrYes(selectedIdx)) {
					value = YES_OPTION;
				} else if (isNo(selectedIdx)) {
					value = NO_OPTION;
				} else {
					value = CANCEL_OPTION;
				}
			} else {
				value = selectedIdx;
			}
		} else {
			// Want inputs
			if (inputField != null) {
				if (isOKOrYes(selectedIdx)) {
					inputValue = inputField.getText();
				}
			} else if (list != null) {
				if (isOKOrYes(selectedIdx)) {
					inputValue = list.getSelectedItem();
				}
			}
		}

		dialog.dispose();
	}

	private boolean isOKOrYes(int selectedIdx) {
		if (optionType == YES_NO_CANCEL_OPTION) {
			if (selectedIdx == 0) {
				return true;
			}
		} else if (optionType == YES_NO_OPTION) {
			if (selectedIdx == 0) {
				return true;
			}
		} else if (optionType == OK_CANCEL_OPTION) {
			if (selectedIdx == 0) {
				return true;
			}
		}
		if (selectedIdx == 0) {
			return true;
		}
		return false;
	}

	private boolean isNo(int selectedIdx) {
		if (optionType == YES_NO_CANCEL_OPTION) {
			if (selectedIdx == 1) {
				return true;
			}
		} else if (optionType == YES_NO_OPTION) {
			if (selectedIdx == 1) {
				return true;
			}
		} else if (optionType == OK_CANCEL_OPTION) {
		}
		return false;
	}

	private void initDialog(final JDialog dialog, int style, Component parentComponent) {
		Container rootPane = dialog.getContentPane();

		JPanel contentPane = new JPanel(new BorderLayout());
		Insets insets = new Insets(5, 5, 5, 5);

		{
			rootPane.setLayout(new GridBagLayout());
			rootPane.add(contentPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
					GridBagConstraints.LINE_END, GridBagConstraints.NONE, insets, 0, 0));
		}
		{
			JLabel iconLabel = null;
			if (icon != null) {
				iconLabel = new JLabel(icon);
			} else {
				if (messageType == ERROR_MESSAGE) {
					iconLabel = buildLabel(AndroidUIUtil.getSystemErrorDrawable());
				} else if (messageType == INFORMATION_MESSAGE) {
					iconLabel = buildLabel(AndroidUIUtil.getSystemInformationDrawable());
				} else if (messageType == WARNING_MESSAGE) {
					iconLabel = buildLabel(AndroidUIUtil.getSystemWarningDrawable());
				} else if (messageType == QUESTION_MESSAGE) {
					iconLabel = buildLabel(AndroidUIUtil.getSystemQuestionDrawable());
				}
			}
			if (iconLabel != null) {
				contentPane.add(iconLabel, BorderLayout.WEST);
			}
		}
		{
			if (wantsInput) {
				if (options != null) {
					// List
					list = new JComboBox(options);
					JPanel inputPanel = new JPanel(new GridLayout(2, 1));
					inputPanel.add(new JLabel(message.toString()));
					inputPanel.add(list);

					list.requestFocus();

					if (initialValue != null) {
						list.setSelectedItem(initialValue);
					}
					contentPane.add(inputPanel, BorderLayout.CENTER);
				} else {
					// Input
					JPanel inputPanel = new JPanel(new GridLayout(2, 1));
					inputPanel.add(new JLabel(message.toString()));
					inputField = new JTextField("", 22);
					inputPanel.add(inputField);

					inputField.requestFocus();

					if (initialValue != null) {
						inputField.setText(initialValue.toString());
						inputField.selectAll();
					}
					contentPane.add(inputPanel, BorderLayout.CENTER);
				}
			} else {
				if (message instanceof Component) {
					contentPane.add((Component) message, BorderLayout.CENTER);
				} else {
					contentPane.add(new JLabel(message.toString()), BorderLayout.CENTER);
				}
			}
		}
		{
			if (options == null) {
				useInnerOptions = true;
				options = getOptionTypeButtonNumAdAPI(optionType);
			}
			int cols = options.length;
			buttonPanel = new JPanel(new GridLayout(1, cols));
			for (int i = cols - 1; i >= 0; i--) {
				JButton btn = new JButton((String) options[i]);
				final int selectedIdx = i;
				btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						doSelectOrInputAdAPI(selectedIdx, dialog);
					}
				});
				buttonPanel.add(btn);
			}
			JPanel bottomPanel = new JPanel();
			bottomPanel.setLayout(new GridBagLayout());
			bottomPanel.add(buttonPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
					GridBagConstraints.LINE_END, GridBagConstraints.NONE, insets, 0, 0));
			contentPane.add(bottomPanel, BorderLayout.SOUTH);
		}
		// contentPane.setLayout(new BorderLayout());
		// contentPane.add(this, BorderLayout.CENTER);

		dialog.setComponentOrientation(this.getComponentOrientation());

		dialog.setResizable(false);
		dialog.pack();
		dialog.setLocationRelativeTo(parentComponent);

		WindowAdapter adapter = new WindowAdapter() {
			private boolean gotFocus = false;

			public void windowClosing(WindowEvent we) {
				dialog.dispose();
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowGainedFocus(WindowEvent we) {
				if (!gotFocus) {
					selectInitialValue();
					gotFocus = true;
				}
			}
		};
		dialog.addWindowListener(adapter);
		dialog.addWindowFocusListener(adapter);
		// dialog.addComponentListener(new ComponentAdapter() {
		// public void componentShown(ComponentEvent ce) {
		// setValue(JOptionPane.UNINITIALIZED_VALUE);
		// }
		// });
	}

	private JLabel buildLabel(Drawable draw) {// 会根据屏幕尺寸自动调整比例
		return new JLabel(new ImageIcon(new BufferedImage(draw)));
	}

	public static void showInternalMessageDialog(Component parentComponent, Object message) {
		showInternalMessageDialog(parentComponent, message,
				UIManager.getString("OptionPane.messageDialogTitle", parentComponent),
				INFORMATION_MESSAGE);
	}

	public static void showInternalMessageDialog(Component parentComponent, Object message,
			String title, int messageType) {
		showInternalMessageDialog(parentComponent, message, title, messageType, null);
	}

	public static void showInternalMessageDialog(Component parentComponent, Object message,
			String title, int messageType, Icon icon) {
		showInternalOptionDialog(parentComponent, message, title, DEFAULT_OPTION, messageType, icon,
				null, null);
	}

	public static int showInternalConfirmDialog(Component parentComponent, Object message) {
		return showInternalConfirmDialog(parentComponent, message,
				UIManager.getString("OptionPane.titleText"), YES_NO_CANCEL_OPTION);
	}

	public static int showInternalConfirmDialog(Component parentComponent, Object message,
			String title, int optionType) {
		return showInternalConfirmDialog(parentComponent, message, title, optionType,
				QUESTION_MESSAGE);
	}

	public static int showInternalConfirmDialog(Component parentComponent, Object message,
			String title, int optionType, int messageType) {
		return showInternalConfirmDialog(parentComponent, message, title, optionType, messageType,
				null);
	}

	public static int showInternalConfirmDialog(Component parentComponent, Object message,
			String title, int optionType, int messageType, Icon icon) {
		return showInternalOptionDialog(parentComponent, message, title, optionType, messageType,
				icon, null, null);
	}

	public static int showInternalOptionDialog(Component parentComponent, Object message,
			String title, int optionType, int messageType, Icon icon, Object[] options,
			Object initialValue) {
		JOptionPane pane = new JOptionPane(message, messageType, optionType, icon, options,
				initialValue);

		pane.setInitialValue(initialValue);

		JInternalFrame dialog = pane.createInternalFrame(parentComponent, title);
		dialog.setVisible(true);

		Object selectedValue = pane.getValue();

		if (selectedValue == null) {
			return CLOSED_OPTION;
		}
		if (options == null) {
			if (selectedValue instanceof Integer) {
				return ((Integer) selectedValue).intValue();
			}
			return CLOSED_OPTION;
		}
		for (int counter = 0, maxCounter = options.length; counter < maxCounter; counter++) {
			if (options[counter].equals(selectedValue)) {
				return counter;
			}
		}
		return CLOSED_OPTION;
	}

	public static String showInternalInputDialog(Component parentComponent, Object message) {
		return showInternalInputDialog(parentComponent, message,
				UIManager.getString("OptionPane.inputDialogTitle", parentComponent),
				QUESTION_MESSAGE);
	}

	public static String showInternalInputDialog(Component parentComponent, Object message,
			String title, int messageType) {
		return (String) showInternalInputDialog(parentComponent, message, title, messageType, null,
				null, null);
	}

	public static Object showInternalInputDialog(Component parentComponent, Object message,
			String title, int messageType, Icon icon, Object[] selectionValues,
			Object initialSelectionValue) {
		JOptionPane pane = new JOptionPane(message, messageType, OK_CANCEL_OPTION, icon, null,
				null);

		pane.setWantsInput(true);
		pane.setSelectionValues(selectionValues);
		pane.setInitialSelectionValue(initialSelectionValue);

		JInternalFrame dialog = pane.createInternalFrame(parentComponent, title);

		dialog.setVisible(true);

		Object value = pane.getInputValue();

		if (value == UNINITIALIZED_VALUE) {
			return null;
		}
		return value;
	}

	public JInternalFrame createInternalFrame(Component parentComponent, String title) {
		Container parent = JOptionPane.getDesktopPaneForComponent(parentComponent);

		if (parent == null
				&& (parentComponent == null || (parent = parentComponent.getParent()) == null)) {
			throw new RuntimeException(
					"JOptionPane: parentComponent does " + "not have a valid parent");
		}

		final JInternalFrame iFrame = new JInternalFrame(title, false, true, false, false);
		return iFrame;
	}

	public static Frame getFrameForComponent(Component parentComponent) throws HeadlessException {
		if (parentComponent == null)
			return getRootFrame();
		if (parentComponent instanceof Frame)
			return (Frame) parentComponent;
		return JOptionPane.getFrameForComponent(parentComponent.getParent());
	}

	static Window getWindowForComponent(Component parentComponent) throws HeadlessException {
		while (parentComponent != null) {
			if (parentComponent instanceof Window) {
				return (Window) parentComponent;
			}
			parentComponent = parentComponent.getParent();
		}
		return null;
	}

	public static JDesktopPane getDesktopPaneForComponent(Component parentComponent) {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public static void setRootFrame(Frame newRootFrame) {
		AndroidClassUtil.callEmptyMethod();
	}

	public static Frame getRootFrame() throws HeadlessException {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	public JOptionPane() {
		this("JOptionPane message");
	}

	public JOptionPane(Object message) {
		this(message, PLAIN_MESSAGE);
	}

	public JOptionPane(Object message, int messageType) {
		this(message, messageType, DEFAULT_OPTION);
	}

	public JOptionPane(Object message, int messageType, int optionType) {
		this(message, messageType, optionType, null);
	}

	public JOptionPane(Object message, int messageType, int optionType, Icon icon) {
		this(message, messageType, optionType, icon, null);
	}

	public JOptionPane(Object message, int messageType, int optionType, Icon icon,
			Object[] options) {
		this(message, messageType, optionType, icon, options, null);
	}

	public JOptionPane(Object message, int messageType, int optionType, Icon icon, Object[] options,
			Object initialValue) {
		this.message = message;
		this.options = options;
		this.initialValue = initialValue;
		this.icon = icon;
		setMessageType(messageType);
		setOptionType(optionType);
		value = UNINITIALIZED_VALUE;
		inputValue = UNINITIALIZED_VALUE;
		// updateUI();
	}

	public void setUI(OptionPaneUI ui) {
		AndroidClassUtil.callEmptyMethod();
	}

	public OptionPaneUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return (OptionPaneUI) null;
	}

	public void updateUI() {
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public void setMessage(Object newMessage) {
		message = newMessage;
	}

	public Object getMessage() {
		return message;
	}

	public void setIcon(Icon newIcon) {
		icon = newIcon;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setValue(Object newValue) {
		value = newValue;
	}

	public Object getValue() {
		return value;
	}

	public void setOptions(Object[] newOptions) {
		options = newOptions;
	}

	public Object[] getOptions() {
		if (options != null) {
			int optionCount = options.length;
			Object[] retOptions = new Object[optionCount];

			System.arraycopy(options, 0, retOptions, 0, optionCount);
			return retOptions;
		}
		return options;
	}

	public void setInitialValue(Object newInitialValue) {
		initialValue = newInitialValue;
	}

	public Object getInitialValue() {
		return initialValue;
	}

	public void setMessageType(int newType) {
		if (newType != ERROR_MESSAGE && newType != INFORMATION_MESSAGE && newType != WARNING_MESSAGE
				&& newType != QUESTION_MESSAGE && newType != PLAIN_MESSAGE)
			throw new RuntimeException(
					"JOptionPane: type must be one of ERROR_MESSAGE, INFORMATION_MESSAGE, "
							+ "WARNING_MESSAGE, QUESTION_MESSAGE or PLAIN_MESSAGE");

		messageType = newType;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setOptionType(int newType) {
		if (newType != DEFAULT_OPTION && newType != YES_NO_OPTION && newType != YES_NO_CANCEL_OPTION
				&& newType != OK_CANCEL_OPTION)
			throw new RuntimeException(
					"JOptionPane: option type must be one of DEFAULT_OPTION, YES_NO_OPTION, "
							+ "YES_NO_CANCEL_OPTION or OK_CANCEL_OPTION");

		optionType = newType;
	}

	public int getOptionType() {
		return optionType;
	}

	public void setSelectionValues(Object[] newValues) {
		selectionValues = newValues;
		if (selectionValues != null)
			setWantsInput(true);
	}

	public Object[] getSelectionValues() {
		return selectionValues;
	}

	public void setInitialSelectionValue(Object newValue) {
		initialSelectionValue = newValue;
	}

	public Object getInitialSelectionValue() {
		return initialSelectionValue;
	}

	public void setInputValue(Object newValue) {
		inputValue = newValue;
	}

	/**
	 * Returns the value the user has input, if wantsInput.
	 */
	public Object getInputValue() {
		return inputValue;
	}

	public int getMaxCharactersPerLineCount() {
		return Integer.MAX_VALUE;
	}

	public void setWantsInput(boolean newValue) {
		wantsInput = newValue;
	}

	public boolean getWantsInput() {
		return wantsInput;
	}

	/**
	 * 本逻辑option,input均调用
	 */
	public void selectInitialValue() {
		if (wantsInput == false) {
			// OptionPane
			if (useInnerOptions) {
				JButton lastBtn = (JButton) buttonPanel
						.getComponent(buttonPanel.getComponentCount() - 1);
				lastBtn.requestFocus();
			} else {
				if (initialValue != null) {
					int size = buttonPanel.getComponentCount();
					for (int i = 0; i < size; i++) {
						JButton btn = (JButton) buttonPanel.getComponent(i);
						if (btn.getText().equals(initialValue)) {
							btn.requestFocus();
							return;
						}
					}
				}
			}
		} else {// want Inputs

		}
	}

	private static int styleFromMessageType(int messageType) {
		switch (messageType) {
		case ERROR_MESSAGE:
			return JRootPane.ERROR_DIALOG;
		case QUESTION_MESSAGE:
			return JRootPane.QUESTION_DIALOG;
		case WARNING_MESSAGE:
			return JRootPane.WARNING_DIALOG;
		case INFORMATION_MESSAGE:
			return JRootPane.INFORMATION_DIALOG;
		case PLAIN_MESSAGE:
		default:
			return JRootPane.PLAIN_DIALOG;
		}
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
	}

	protected String paramString() {
		return "";
	}

	private static class ModalPrivilegedAction implements PrivilegedAction<Method> {
		private Class<?> clazz;
		private String methodName;

		public ModalPrivilegedAction(Class<?> clazz, String methodName) {
			this.clazz = clazz;
			this.methodName = methodName;
		}

		public Method run() {
			Method method = null;
			try {
				method = clazz.getDeclaredMethod(methodName, (Class[]) null);
			} catch (NoSuchMethodException ex) {
			}
			if (method != null) {
				method.setAccessible(true);
			}
			return method;
		}
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

}