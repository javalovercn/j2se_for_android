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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.SpinnerUI;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 * A single line input field that lets the user select a number or an object
 * value from an ordered sequence. Spinners typically provide a pair of tiny
 * arrow buttons for stepping through the elements of the sequence. The keyboard
 * up/down arrow keys also cycle through the elements. The user may also be
 * allowed to type a (legal) value directly into the spinner. Although combo
 * boxes provide similar functionality, spinners are sometimes preferred because
 * they don't require a drop down list that can obscure important data.
 * <p>
 * A <code>JSpinner</code>'s sequence value is defined by its
 * <code>SpinnerModel</code>. The <code>model</code> can be specified as a
 * constructor argument and changed with the <code>model</code> property.
 * <code>SpinnerModel</code> classes for some common types are provided:
 * <code>SpinnerListModel</code>, <code>SpinnerNumberModel</code>, and
 * <code>SpinnerDateModel</code>.
 * <p>
 * A <code>JSpinner</code> has a single child component that's responsible for
 * displaying and potentially changing the current element or <i>value</i> of
 * the model, which is called the <code>editor</code>. The editor is created by
 * the <code>JSpinner</code>'s constructor and can be changed with the
 * <code>editor</code> property. The <code>JSpinner</code>'s editor stays in
 * sync with the model by listening for <code>ChangeEvent</code>s. If the user
 * has changed the value displayed by the <code>editor</code> it is possible for
 * the <code>model</code>'s value to differ from that of the
 * <code>editor</code>. To make sure the <code>model</code> has the same value
 * as the editor use the <code>commitEdit</code> method, eg:
 * 
 * <pre>
 *   try {
 *       spinner.commitEdit();
 *   }
 *   catch (ParseException pe) {{
 *       // Edited value is invalid, spinner.getValue() will return
 *       // the last valid value, you could revert the spinner to show that:
 *       JComponent editor = spinner.getEditor()
 *       if (editor instanceof DefaultEditor) {
 *           ((DefaultEditor)editor).getTextField().setValue(spinner.getValue();
 *       }
 *       // reset the value to some known value:
 *       spinner.setValue(fallbackValue);
 *       // or treat the last valid value as the current, in which
 *       // case you don't need to do anything.
 *   }
 *   return spinner.getValue();
 * </pre>
 * <p>
 * For information and examples of using spinner see <a href=
 * "http://java.sun.com/doc/books/tutorial/uiswing/components/spinner.html">How
 * to Use Spinners</a>, a section in <em>The Java Tutorial.</em>
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
 * @beaninfo attribute: isContainer false description: A single line input field
 *           that lets the user select a number or an object value from an
 *           ordered set.
 *
 * @see SpinnerModel
 * @see AbstractSpinnerModel
 * @see SpinnerListModel
 * @see SpinnerNumberModel
 * @see SpinnerDateModel
 * @see JFormattedTextField
 *
 * @author Hans Muller
 * @author Lynn Monsanto (accessibility)
 * @since 1.4
 */
public class JSpinner extends JComponent implements Accessible {
	/**
	 * @see #getUIClassID
	 * @see #readObject
	 */
	private static final String uiClassID = "SpinnerUI";

	private static final Action DISABLED_ACTION = new DisabledAction();

	private SpinnerModel model;
	private JComponent editor;
	private ChangeListener modelListener;
	private transient ChangeEvent changeEvent;
	private boolean editorExplicitlySet = false;

	/**
	 * Constructs a spinner for the given model. The spinner has a set of
	 * previous/next buttons, and an editor appropriate for the model.
	 *
	 * @throws NullPointerException
	 *             if the model is {@code null}
	 */
	public JSpinner(SpinnerModel model) {
		if (model == null) {
			throw new NullPointerException("model cannot be null");
		}
		this.model = model;
		this.editor = createEditor(model);
		setUIProperty("opaque", true);
		updateUI();
	}

	/**
	 * Constructs a spinner with an <code>Integer SpinnerNumberModel</code> with
	 * initial value 0 and no minimum or maximum limits.
	 */
	public JSpinner() {
		this(new SpinnerNumberModel());
	}

	/**
	 * Returns the look and feel (L&F) object that renders this component.
	 *
	 * @return the <code>SpinnerUI</code> object that renders this component
	 */
	public SpinnerUI getUI() {
		return (SpinnerUI) ui;
	}

	/**
	 * Sets the look and feel (L&F) object that renders this component.
	 *
	 * @param ui
	 *            the <code>SpinnerUI</code> L&F object
	 * @see UIDefaults#getUI
	 */
	public void setUI(SpinnerUI ui) {
		super.setUI(ui);
	}

	/**
	 * Returns the suffix used to construct the name of the look and feel (L&F)
	 * class used to render this component.
	 *
	 * @return the string "SpinnerUI"
	 * @see JComponent#getUIClassID
	 * @see UIDefaults#getUI
	 */
	public String getUIClassID() {
		return uiClassID;
	}

	/**
	 * Resets the UI property with the value from the current look and feel.
	 *
	 * @see UIManager#getUI
	 */
	public void updateUI() {
		setUI((SpinnerUI) UIManager.getUI(this));
		invalidate();
	}

	/**
	 * This method is called by the constructors to create the
	 * <code>JComponent</code> that displays the current value of the sequence.
	 * The editor may also allow the user to enter an element of the sequence
	 * directly. An editor must listen for <code>ChangeEvents</code> on the
	 * <code>model</code> and keep the value it displays in sync with the value
	 * of the model.
	 * <p>
	 * Subclasses may override this method to add support for new
	 * <code>SpinnerModel</code> classes. Alternatively one can just replace the
	 * editor created here with the <code>setEditor</code> method. The default
	 * mapping from model type to editor is:
	 * <ul>
	 * <li><code>SpinnerNumberModel =&gt; JSpinner.NumberEditor</code>
	 * <li><code>SpinnerDateModel =&gt; JSpinner.DateEditor</code>
	 * <li><code>SpinnerListModel =&gt; JSpinner.ListEditor</code>
	 * <li><i>all others</i> =&gt; <code>JSpinner.DefaultEditor</code>
	 * </ul>
	 *
	 * @return a component that displays the current value of the sequence
	 * @param model
	 *            the value of getModel
	 * @see #getModel
	 * @see #setEditor
	 */
	protected JComponent createEditor(SpinnerModel model) {
		// if (model instanceof SpinnerDateModel) {
		// return new DateEditor(this);
		// }
		// else if (model instanceof SpinnerListModel) {
		// return new ListEditor(this);
		// }
		// else if (model instanceof SpinnerNumberModel) {
		// return new NumberEditor(this);
		// }
		// else {
		return new DefaultEditor(this);
		// }
	}

	/**
	 * Changes the model that represents the value of this spinner. If the
	 * editor property has not been explicitly set, the editor property is
	 * (implicitly) set after the <code>"model"</code>
	 * <code>PropertyChangeEvent</code> has been fired. The editor property is
	 * set to the value returned by <code>createEditor</code>, as in:
	 * 
	 * <pre>
	 * setEditor(createEditor(model));
	 * </pre>
	 *
	 * @param model
	 *            the new <code>SpinnerModel</code>
	 * @see #getModel
	 * @see #getEditor
	 * @see #setEditor
	 * @throws IllegalArgumentException
	 *             if model is <code>null</code>
	 *
	 * @beaninfo bound: true attribute: visualUpdate true description: Model
	 *           that represents the value of this spinner.
	 */
	public void setModel(SpinnerModel model) {
		if (model == null) {
			throw new IllegalArgumentException("null model");
		}
		if (!model.equals(this.model)) {
			SpinnerModel oldModel = this.model;
			this.model = model;
			if (modelListener != null) {
				oldModel.removeChangeListener(modelListener);
				this.model.addChangeListener(modelListener);
			}
			firePropertyChange("model", oldModel, model);
			if (!editorExplicitlySet) {
				setEditor(createEditor(model)); // sets editorExplicitlySet true
				editorExplicitlySet = false;
			}
			repaint();
			revalidate();
		}
	}

	/**
	 * Returns the <code>SpinnerModel</code> that defines this spinners sequence
	 * of values.
	 *
	 * @return the value of the model property
	 * @see #setModel
	 */
	public SpinnerModel getModel() {
		return model;
	}

	/**
	 * Returns the current value of the model, typically this value is displayed
	 * by the <code>editor</code>. If the user has changed the value displayed
	 * by the <code>editor</code> it is possible for the <code>model</code>'s
	 * value to differ from that of the <code>editor</code>, refer to the class
	 * level javadoc for examples of how to deal with this.
	 * <p>
	 * This method simply delegates to the <code>model</code>. It is equivalent
	 * to:
	 * 
	 * <pre>
	 * getModel().getValue()
	 * </pre>
	 *
	 * @see #setValue
	 * @see SpinnerModel#getValue
	 */
	public Object getValue() {
		return getModel().getValue();
	}

	/**
	 * Changes current value of the model, typically this value is displayed by
	 * the <code>editor</code>. If the <code>SpinnerModel</code> implementation
	 * doesn't support the specified value then an
	 * <code>IllegalArgumentException</code> is thrown.
	 * <p>
	 * This method simply delegates to the <code>model</code>. It is equivalent
	 * to:
	 * 
	 * <pre>
	 * getModel().setValue(value)
	 * </pre>
	 *
	 * @throws IllegalArgumentException
	 *             if <code>value</code> isn't allowed
	 * @see #getValue
	 * @see SpinnerModel#setValue
	 */
	public void setValue(Object value) {
		getModel().setValue(value);
	}

	/**
	 * Returns the object in the sequence that comes after the object returned
	 * by <code>getValue()</code>. If the end of the sequence has been reached
	 * then return <code>null</code>. Calling this method does not effect
	 * <code>value</code>.
	 * <p>
	 * This method simply delegates to the <code>model</code>. It is equivalent
	 * to:
	 * 
	 * <pre>
	 * getModel().getNextValue()
	 * </pre>
	 *
	 * @return the next legal value or <code>null</code> if one doesn't exist
	 * @see #getValue
	 * @see #getPreviousValue
	 * @see SpinnerModel#getNextValue
	 */
	public Object getNextValue() {
		return getModel().getNextValue();
	}

	/**
	 * We pass <code>Change</code> events along to the listeners with the the
	 * slider (instead of the model itself) as the event source.
	 */
	private class ModelListener implements ChangeListener, Serializable {
		public void stateChanged(ChangeEvent e) {
			fireStateChanged();
		}
	}

	/**
	 * Adds a listener to the list that is notified each time a change to the
	 * model occurs. The source of <code>ChangeEvents</code> delivered to
	 * <code>ChangeListeners</code> will be this <code>JSpinner</code>. Note
	 * also that replacing the model will not affect listeners added directly to
	 * JSpinner. Applications can add listeners to the model directly. In that
	 * case is that the source of the event would be the
	 * <code>SpinnerModel</code>.
	 *
	 * @param listener
	 *            the <code>ChangeListener</code> to add
	 * @see #removeChangeListener
	 * @see #getModel
	 */
	public void addChangeListener(ChangeListener listener) {
		if (modelListener == null) {
			modelListener = new ModelListener();
			getModel().addChangeListener(modelListener);
		}
		list.add(ChangeListener.class, listener);
	}

	/**
	 * Removes a <code>ChangeListener</code> from this spinner.
	 *
	 * @param listener
	 *            the <code>ChangeListener</code> to remove
	 * @see #fireStateChanged
	 * @see #addChangeListener
	 */
	public void removeChangeListener(ChangeListener listener) {
		list.remove(ChangeListener.class, listener);
	}

	/**
	 * Returns an array of all the <code>ChangeListener</code>s added to this
	 * JSpinner with addChangeListener().
	 *
	 * @return all of the <code>ChangeListener</code>s added or an empty array
	 *         if no listeners have been added
	 * @since 1.4
	 */
	public ChangeListener[] getChangeListeners() {
		return list.getListeners(ChangeListener.class);
	}

	/**
	 * Sends a <code>ChangeEvent</code>, whose source is this
	 * <code>JSpinner</code>, to each <code>ChangeListener</code>. When a
	 * <code>ChangeListener</code> has been added to the spinner, this method
	 * method is called each time a <code>ChangeEvent</code> is received from
	 * the model.
	 *
	 * @see #addChangeListener
	 * @see #removeChangeListener
	 * @see EventListenerList
	 */
	protected void fireStateChanged() {
		Object[] listeners = list.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);
				}
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}

	/**
	 * Returns the object in the sequence that comes before the object returned
	 * by <code>getValue()</code>. If the end of the sequence has been reached
	 * then return <code>null</code>. Calling this method does not effect
	 * <code>value</code>.
	 * <p>
	 * This method simply delegates to the <code>model</code>. It is equivalent
	 * to:
	 * 
	 * <pre>
	 * getModel().getPreviousValue()
	 * </pre>
	 *
	 * @return the previous legal value or <code>null</code> if one doesn't
	 *         exist
	 * @see #getValue
	 * @see #getNextValue
	 * @see SpinnerModel#getPreviousValue
	 */
	public Object getPreviousValue() {
		return getModel().getPreviousValue();
	}

	/**
	 * Changes the <code>JComponent</code> that displays the current value of
	 * the <code>SpinnerModel</code>. It is the responsibility of this method to
	 * <i>disconnect</i> the old editor from the model and to connect the new
	 * editor. This may mean removing the old editors
	 * <code>ChangeListener</code> from the model or the spinner itself and
	 * adding one for the new editor.
	 *
	 * @param editor
	 *            the new editor
	 * @see #getEditor
	 * @see #createEditor
	 * @see #getModel
	 * @throws IllegalArgumentException
	 *             if editor is <code>null</code>
	 *
	 * @beaninfo bound: true attribute: visualUpdate true description:
	 *           JComponent that displays the current value of the model
	 */
	public void setEditor(JComponent editor) {
		if (editor == null) {
			throw new IllegalArgumentException("null editor");
		}
		if (!editor.equals(this.editor)) {
			JComponent oldEditor = this.editor;
			this.editor = editor;
			if (oldEditor instanceof DefaultEditor) {
				((DefaultEditor) oldEditor).dismiss(this);
			}
			editorExplicitlySet = true;
			firePropertyChange("editor", oldEditor, editor);
			revalidate();
			repaint();
		}
	}

	/**
	 * Returns the component that displays and potentially changes the model's
	 * value.
	 *
	 * @return the component that displays and potentially changes the model's
	 *         value
	 * @see #setEditor
	 * @see #createEditor
	 */
	public JComponent getEditor() {
		return editor;
	}

	/**
	 * Commits the currently edited value to the <code>SpinnerModel</code>.
	 * <p>
	 * If the editor is an instance of <code>DefaultEditor</code>, the call if
	 * forwarded to the editor, otherwise this does nothing.
	 *
	 * @throws ParseException
	 *             if the currently edited value couldn't be commited.
	 */
	public void commitEdit() throws ParseException {
		JComponent editor = getEditor();
		if (editor instanceof DefaultEditor) {
			((DefaultEditor) editor).commitEdit();
		}
	}

	/*
	 * See readObject and writeObject in JComponent for more information about
	 * serialization in Swing.
	 *
	 * @param s Stream to write to
	 */
	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	/**
	 * This subclass of javax.swing.NumberFormatter maps the minimum/maximum
	 * properties to a SpinnerNumberModel and initializes the valueClass of the
	 * NumberFormatter to match the type of the initial models value.
	 */
	private static class NumberEditorFormatter extends NumberFormatter {
		private final SpinnerNumberModel model;

		NumberEditorFormatter(SpinnerNumberModel model, NumberFormat format) {
			super(format);
			this.model = model;
			setValueClass(model.getValue().getClass());
		}

		public void setMinimum(Comparable min) {
			model.setMinimum(min);
		}

		public Comparable getMinimum() {
			return model.getMinimum();
		}

		public void setMaximum(Comparable max) {
			model.setMaximum(max);
		}

		public Comparable getMaximum() {
			return model.getMaximum();
		}
	}

	/**
	 * An editor for a <code>JSpinner</code> whose model is a
	 * <code>SpinnerNumberModel</code>. The value of the editor is displayed
	 * with a <code>JFormattedTextField</code> whose format is defined by a
	 * <code>NumberFormatter</code> instance whose <code>minimum</code> and
	 * <code>maximum</code> properties are mapped to the
	 * <code>SpinnerNumberModel</code>.
	 * 
	 * @since 1.4
	 */
	// PENDING(hmuller): more example javadoc
	public static class NumberEditor extends DefaultEditor {
		// This is here until DecimalFormat gets a constructor that
		// takes a Locale: 4923525
		private static String getDefaultPattern(Locale locale) {
			// Get the pattern for the default locale.
			// ResourceBundle rb = LocaleData.getNumberFormatData(locale);
			// String[] all = rb.getStringArray("NumberPatterns");
			// return all[0];
			return "#,##0.###;-#,##0.###";// Locale.ENGLISH
		}

		/**
		 * Construct a <code>JSpinner</code> editor that supports displaying and
		 * editing the value of a <code>SpinnerNumberModel</code> with a
		 * <code>JFormattedTextField</code>. <code>This</code>
		 * <code>NumberEditor</code> becomes both a <code>ChangeListener</code>
		 * on the spinner and a <code>PropertyChangeListener</code> on the new
		 * <code>JFormattedTextField</code>.
		 *
		 * @param spinner
		 *            the spinner whose model <code>this</code> editor will
		 *            monitor
		 * @exception IllegalArgumentException
		 *                if the spinners model is not an instance of
		 *                <code>SpinnerNumberModel</code>
		 *
		 * @see #getModel
		 * @see #getFormat
		 * @see SpinnerNumberModel
		 */
		public NumberEditor(JSpinner spinner) {
			this(spinner, getDefaultPattern(spinner.getLocale()));
		}

		/**
		 * Construct a <code>JSpinner</code> editor that supports displaying and
		 * editing the value of a <code>SpinnerNumberModel</code> with a
		 * <code>JFormattedTextField</code>. <code>This</code>
		 * <code>NumberEditor</code> becomes both a <code>ChangeListener</code>
		 * on the spinner and a <code>PropertyChangeListener</code> on the new
		 * <code>JFormattedTextField</code>.
		 *
		 * @param spinner
		 *            the spinner whose model <code>this</code> editor will
		 *            monitor
		 * @param decimalFormatPattern
		 *            the initial pattern for the <code>DecimalFormat</code>
		 *            object that's used to display and parse the value of the
		 *            text field.
		 * @exception IllegalArgumentException
		 *                if the spinners model is not an instance of
		 *                <code>SpinnerNumberModel</code> or if
		 *                <code>decimalFormatPattern</code> is not a legal
		 *                argument to <code>DecimalFormat</code>
		 *
		 * @see #getTextField
		 * @see SpinnerNumberModel
		 * @see java.text.DecimalFormat
		 */
		public NumberEditor(JSpinner spinner, String decimalFormatPattern) {
			this(spinner, new DecimalFormat(decimalFormatPattern));
		}

		/**
		 * Construct a <code>JSpinner</code> editor that supports displaying and
		 * editing the value of a <code>SpinnerNumberModel</code> with a
		 * <code>JFormattedTextField</code>. <code>This</code>
		 * <code>NumberEditor</code> becomes both a <code>ChangeListener</code>
		 * on the spinner and a <code>PropertyChangeListener</code> on the new
		 * <code>JFormattedTextField</code>.
		 *
		 * @param spinner
		 *            the spinner whose model <code>this</code> editor will
		 *            monitor
		 * @param decimalFormatPattern
		 *            the initial pattern for the <code>DecimalFormat</code>
		 *            object that's used to display and parse the value of the
		 *            text field.
		 * @exception IllegalArgumentException
		 *                if the spinners model is not an instance of
		 *                <code>SpinnerNumberModel</code>
		 *
		 * @see #getTextField
		 * @see SpinnerNumberModel
		 * @see java.text.DecimalFormat
		 */
		private NumberEditor(JSpinner spinner, DecimalFormat format) {
			super(spinner);
			if (!(spinner.getModel() instanceof SpinnerNumberModel)) {
				throw new IllegalArgumentException("model not a SpinnerNumberModel");
			}

			SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
			NumberFormatter formatter = new NumberEditorFormatter(model, format);
			DefaultFormatterFactory factory = new DefaultFormatterFactory(formatter);
			JFormattedTextField ftf = getTextField();
			ftf.setEditable(true);
			ftf.setFormatterFactory(factory);
			ftf.setHorizontalAlignment(JTextField.RIGHT);

			/*
			 * TBD - initializing the column width of the text field is
			 * imprecise and doing it here is tricky because the developer may
			 * configure the formatter later.
			 */
			try {
				String maxString = formatter.valueToString(model.getMinimum());
				String minString = formatter.valueToString(model.getMaximum());
				ftf.setColumns(Math.max(maxString.length(), minString.length()));
			} catch (ParseException e) {
				// TBD should throw a chained error here
			}

		}

		/**
		 * Returns the <code>java.text.DecimalFormat</code> object the
		 * <code>JFormattedTextField</code> uses to parse and format numbers.
		 *
		 * @return the value of
		 *         <code>getTextField().getFormatter().getFormat()</code>.
		 * @see #getTextField
		 * @see java.text.DecimalFormat
		 */
		public DecimalFormat getFormat() {
			return (DecimalFormat) ((NumberFormatter) (getTextField().getFormatter())).getFormat();
		}

		/**
		 * Return our spinner ancestor's <code>SpinnerNumberModel</code>.
		 *
		 * @return <code>getSpinner().getModel()</code>
		 * @see #getSpinner
		 * @see #getTextField
		 */
		public SpinnerNumberModel getModel() {
			return (SpinnerNumberModel) (getSpinner().getModel());
		}
	}

	/**
	 * A simple base class for more specialized editors that displays a
	 * read-only view of the model's current value with a
	 * <code>JFormattedTextField</code>. Subclasses can configure the
	 * <code>JFormattedTextField</code> to create an editor that's appropriate
	 * for the type of model they support and they may want to override the
	 * <code>stateChanged</code> and <code>propertyChanged</code> methods, which
	 * keep the model and the text field in sync.
	 * <p>
	 * This class defines a <code>dismiss</code> method that removes the editors
	 * <code>ChangeListener</code> from the <code>JSpinner</code> that it's part
	 * of. The <code>setEditor</code> method knows about
	 * <code>DefaultEditor.dismiss</code>, so if the developer replaces an
	 * editor that's derived from <code>JSpinner.DefaultEditor</code> its
	 * <code>ChangeListener</code> connection back to the <code>JSpinner</code>
	 * will be removed. However after that, it's up to the developer to manage
	 * their editor listeners. Similarly, if a subclass overrides
	 * <code>createEditor</code>, it's up to the subclasser to deal with their
	 * editor subsequently being replaced (with <code>setEditor</code>). We
	 * expect that in most cases, and in editor installed with
	 * <code>setEditor</code> or created by a <code>createEditor</code>
	 * override, will not be replaced anyway.
	 * <p>
	 * This class is the <code>LayoutManager</code> for it's single
	 * <code>JFormattedTextField</code> child. By default the child is just
	 * centered with the parents insets.
	 * 
	 * @since 1.4
	 */
	public static class DefaultEditor extends JPanel
			implements ChangeListener, PropertyChangeListener, LayoutManager {
		/**
		 * Constructs an editor component for the specified
		 * <code>JSpinner</code>. This <code>DefaultEditor</code> is it's own
		 * layout manager and it is added to the spinner's
		 * <code>ChangeListener</code> list. The constructor creates a single
		 * <code>JFormattedTextField</code> child, initializes it's value to be
		 * the spinner model's current value and adds it to <code>this</code>
		 * <code>DefaultEditor</code>.
		 *
		 * @param spinner
		 *            the spinner whose model <code>this</code> editor will
		 *            monitor
		 * @see #getTextField
		 * @see JSpinner#addChangeListener
		 */
		public DefaultEditor(JSpinner spinner) {
			super(null);

			JFormattedTextField ftf = new JFormattedTextField();
			ftf.setName("Spinner.formattedTextField");
			ftf.setValue(spinner.getValue());
			ftf.addPropertyChangeListener(this);
			ftf.setEditable(false);
			ftf.setInheritsPopupMenu(true);

			String toolTipText = spinner.getToolTipText();
			if (toolTipText != null) {
				ftf.setToolTipText(toolTipText);
			}

			add(ftf);

			setLayout(this);
			spinner.addChangeListener(this);

			// We want the spinner's increment/decrement actions to be
			// active vs those of the JFormattedTextField. As such we
			// put disabled actions in the JFormattedTextField's actionmap.
			// A binding to a disabled action is treated as a nonexistant
			// binding.
			ActionMap ftfMap = ftf.getActionMap();

			if (ftfMap != null) {
				ftfMap.put("increment", DISABLED_ACTION);
				ftfMap.put("decrement", DISABLED_ACTION);
			}
		}

		/**
		 * Disconnect <code>this</code> editor from the specified
		 * <code>JSpinner</code>. By default, this method removes itself from
		 * the spinners <code>ChangeListener</code> list.
		 *
		 * @param spinner
		 *            the <code>JSpinner</code> to disconnect this editor from;
		 *            the same spinner as was passed to the constructor.
		 */
		public void dismiss(JSpinner spinner) {
			spinner.removeChangeListener(this);
		}

		/**
		 * Returns the <code>JSpinner</code> ancestor of this editor or
		 * <code>null</code> if none of the ancestors are a
		 * <code>JSpinner</code>. Typically the editor's parent is a
		 * <code>JSpinner</code> however subclasses of <code>JSpinner</code> may
		 * override the the <code>createEditor</code> method and insert one or
		 * more containers between the <code>JSpinner</code> and it's editor.
		 *
		 * @return <code>JSpinner</code> ancestor; <code>null</code> if none of
		 *         the ancestors are a <code>JSpinner</code>
		 *
		 * @see JSpinner#createEditor
		 */
		public JSpinner getSpinner() {
			for (Component c = this; c != null; c = c.getParent()) {
				if (c instanceof JSpinner) {
					return (JSpinner) c;
				}
			}
			return null;
		}

		/**
		 * Returns the <code>JFormattedTextField</code> child of this editor. By
		 * default the text field is the first and only child of editor.
		 *
		 * @return the <code>JFormattedTextField</code> that gives the user
		 *         access to the <code>SpinnerDateModel's</code> value.
		 * @see #getSpinner
		 * @see #getModel
		 */
		public JFormattedTextField getTextField() {
			return (JFormattedTextField) getComponent(0);
		}

		/**
		 * This method is called when the spinner's model's state changes. It
		 * sets the <code>value</code> of the text field to the current value of
		 * the spinners model.
		 *
		 * @param e
		 *            the <code>ChangeEvent</code> whose source is the
		 *            <code>JSpinner</code> whose model has changed.
		 * @see #getTextField
		 * @see JSpinner#getValue
		 */
		public void stateChanged(ChangeEvent e) {
			JSpinner spinner = (JSpinner) (e.getSource());
			getTextField().setValue(spinner.getValue());
		}

		/**
		 * Called by the <code>JFormattedTextField</code>
		 * <code>PropertyChangeListener</code>. When the <code>"value"</code>
		 * property changes, which implies that the user has typed a new number,
		 * we set the value of the spinners model.
		 * <p>
		 * This class ignores <code>PropertyChangeEvents</code> whose source is
		 * not the <code>JFormattedTextField</code>, so subclasses may safely
		 * make <code>this</code> <code>DefaultEditor</code> a
		 * <code>PropertyChangeListener</code> on other objects.
		 *
		 * @param e
		 *            the <code>PropertyChangeEvent</code> whose source is the
		 *            <code>JFormattedTextField</code> created by this class.
		 * @see #getTextField
		 */
		public void propertyChange(PropertyChangeEvent e) {
			JSpinner spinner = getSpinner();

			if (spinner == null) {
				// Indicates we aren't installed anywhere.
				return;
			}

			Object source = e.getSource();
			String name = e.getPropertyName();
			if ((source instanceof JFormattedTextField) && "value".equals(name)) {
				Object lastValue = spinner.getValue();

				// Try to set the new value
				try {
					spinner.setValue(getTextField().getValue());
				} catch (IllegalArgumentException iae) {
					// SpinnerModel didn't like new value, reset
					try {
						((JFormattedTextField) source).setValue(lastValue);
					} catch (IllegalArgumentException iae2) {
						// Still bogus, nothing else we can do, the
						// SpinnerModel and JFormattedTextField are now out
						// of sync.
					}
				}
			}
		}

		/**
		 * This <code>LayoutManager</code> method does nothing. We're only
		 * managing a single child and there's no support for layout
		 * constraints.
		 *
		 * @param name
		 *            ignored
		 * @param child
		 *            ignored
		 */
		public void addLayoutComponent(String name, Component child) {
		}

		/**
		 * This <code>LayoutManager</code> method does nothing. There isn't any
		 * per-child state.
		 *
		 * @param child
		 *            ignored
		 */
		public void removeLayoutComponent(Component child) {
		}

		/**
		 * Returns the size of the parents insets.
		 */
		private Dimension insetSize(Container parent) {
			Insets insets = parent.getInsets();
			int w = insets.left + insets.right;
			int h = insets.top + insets.bottom;
			return new Dimension(w, h);
		}

		/**
		 * Returns the preferred size of first (and only) child plus the size of
		 * the parents insets.
		 *
		 * @param parent
		 *            the Container that's managing the layout
		 * @return the preferred dimensions to lay out the subcomponents of the
		 *         specified container.
		 */
		public Dimension preferredLayoutSize(Container parent) {
			Dimension preferredSize = insetSize(parent);
			if (parent.getComponentCount() > 0) {
				Dimension childSize = getComponent(0).getPreferredSize();
				preferredSize.width += childSize.width;
				preferredSize.height += childSize.height;
			}
			return preferredSize;
		}

		/**
		 * Returns the minimum size of first (and only) child plus the size of
		 * the parents insets.
		 *
		 * @param parent
		 *            the Container that's managing the layout
		 * @return the minimum dimensions needed to lay out the subcomponents of
		 *         the specified container.
		 */
		public Dimension minimumLayoutSize(Container parent) {
			Dimension minimumSize = insetSize(parent);
			if (parent.getComponentCount() > 0) {
				Dimension childSize = getComponent(0).getMinimumSize();
				minimumSize.width += childSize.width;
				minimumSize.height += childSize.height;
			}
			return minimumSize;
		}

		/**
		 * Resize the one (and only) child to completely fill the area within
		 * the parents insets.
		 */
		public void layoutContainer(Container parent) {
			if (parent.getComponentCount() > 0) {
				Insets insets = parent.getInsets();
				int w = parent.getWidth() - (insets.left + insets.right);
				int h = parent.getHeight() - (insets.top + insets.bottom);
				getComponent(0).setBounds(insets.left, insets.top, w, h);
			}
		}

		/**
		 * Pushes the currently edited value to the <code>SpinnerModel</code>.
		 * <p>
		 * The default implementation invokes <code>commitEdit</code> on the
		 * <code>JFormattedTextField</code>.
		 *
		 * @throws ParseException
		 *             if the edited value is not legal
		 */
		public void commitEdit() throws ParseException {
			// If the value in the JFormattedTextField is legal, this will have
			// the result of pushing the value to the SpinnerModel
			// by way of the <code>propertyChange</code> method.
			JFormattedTextField ftf = getTextField();

			ftf.commitEdit();
		}

		/**
		 * Returns the baseline.
		 *
		 * @throws IllegalArgumentException
		 *             {@inheritDoc}
		 * @see javax.swing.JComponent#getBaseline(int,int)
		 * @see javax.swing.JComponent#getBaselineResizeBehavior()
		 * @since 1.6
		 */
		public int getBaseline(int width, int height) {
			// check size.
			super.getBaseline(width, height);
			Insets insets = getInsets();
			width = width - insets.left - insets.right;
			height = height - insets.top - insets.bottom;
			int baseline = getComponent(0).getBaseline(width, height);
			if (baseline >= 0) {
				return baseline + insets.top;
			}
			return -1;
		}

		/**
		 * Returns an enum indicating how the baseline of the component changes
		 * as the size changes.
		 *
		 * @throws NullPointerException
		 *             {@inheritDoc}
		 * @see javax.swing.JComponent#getBaseline(int, int)
		 * @since 1.6
		 */
		public BaselineResizeBehavior getBaselineResizeBehavior() {
			return getComponent(0).getBaselineResizeBehavior();
		}
	}

	/**
	 * An Action implementation that is always disabled.
	 */
	private static class DisabledAction implements Action {
		public Object getValue(String key) {
			return null;
		}

		public void putValue(String key, Object value) {
		}

		public void setEnabled(boolean b) {
		}

		public boolean isEnabled() {
			return false;
		}

		public void addPropertyChangeListener(PropertyChangeListener l) {
		}

		public void removePropertyChangeListener(PropertyChangeListener l) {
		}

		public void actionPerformed(ActionEvent ae) {
		}
	}

	/////////////////
	// Accessibility support
	////////////////

	/**
	 * Gets the <code>AccessibleContext</code> for the <code>JSpinner</code>
	 *
	 * @return the <code>AccessibleContext</code> for the <code>JSpinner</code>
	 * @since 1.5
	 */
	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}
}
