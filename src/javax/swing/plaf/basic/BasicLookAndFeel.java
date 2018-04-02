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
package javax.swing.plaf.basic;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Locale;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

/**
 * A base class to use in creating a look and feel for Swing.
 * <p>
 * Each of the {@code ComponentUI}s provided by {@code
 * BasicLookAndFeel} derives its behavior from the defaults table. Unless
 * otherwise noted each of the {@code ComponentUI} implementations in this
 * package document the set of defaults they use. Unless otherwise noted the
 * defaults are installed at the time {@code installUI} is invoked, and follow
 * the recommendations outlined in {@code LookAndFeel} for installing defaults.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 *
 * @author unattributed
 */
public abstract class BasicLookAndFeel extends LookAndFeel implements Serializable {
	static boolean needsEventHelper;
	private transient Object audioLock = new Object();

	AWTEventHelper invocator = null;

	private PropertyChangeListener disposer = null;

	/**
	 * Returns the look and feel defaults. The returned {@code UIDefaults} is
	 * populated by invoking, in order, {@code initClassDefaults},
	 * {@code initSystemColorDefaults} and {@code initComponentDefaults}.
	 * <p>
	 * While this method is public, it should only be invoked by the
	 * {@code UIManager} when the look and feel is set as the current look and
	 * feel and after {@code initialize} has been invoked.
	 *
	 * @return the look and feel defaults
	 *
	 * @see #initClassDefaults
	 * @see #initSystemColorDefaults
	 * @see #initComponentDefaults
	 */
	public UIDefaults getDefaults() {
		UIDefaults table = new UIDefaults(610, 0.75f);

		initClassDefaults(table);
		initSystemColorDefaults(table);
		initComponentDefaults(table);

		return table;
	}

	/**
	 * {@inheritDoc}
	 */
	public void initialize() {
		if (needsEventHelper) {
			installAWTEventListener();
		}
	}

	void installAWTEventListener() {
	}

	public void uninitialize() {
	}

	/**
	 * Populates {@code table} with mappings from {@code uiClassID} to the fully
	 * qualified name of the ui class. The value for a particular
	 * {@code uiClassID} is {@code
	 * "javax.swing.plaf.basic.Basic + uiClassID"}. For example, the value for
	 * the {@code uiClassID} {@code TreeUI} is {@code
	 * "javax.swing.plaf.basic.BasicTreeUI"}.
	 *
	 * @param table
	 *            the {@code UIDefaults} instance the entries are added to
	 * @throws NullPointerException
	 *             if {@code table} is {@code null}
	 *
	 * @see javax.swing.LookAndFeel
	 * @see #getDefaults
	 */
	protected void initClassDefaults(UIDefaults table) {
		final String basicPackageName = "javax.swing.plaf.basic.";
		Object[] uiDefaults = { "ButtonUI", basicPackageName + "BasicButtonUI", "CheckBoxUI",
				basicPackageName + "BasicCheckBoxUI", "ColorChooserUI",
				basicPackageName + "BasicColorChooserUI", "FormattedTextFieldUI",
				basicPackageName + "BasicFormattedTextFieldUI", "MenuBarUI",
				basicPackageName + "BasicMenuBarUI", "MenuUI", basicPackageName + "BasicMenuUI",
				"MenuItemUI", basicPackageName + "BasicMenuItemUI", "CheckBoxMenuItemUI",
				basicPackageName + "BasicCheckBoxMenuItemUI", "RadioButtonMenuItemUI",
				basicPackageName + "BasicRadioButtonMenuItemUI", "RadioButtonUI",
				basicPackageName + "BasicRadioButtonUI", "ToggleButtonUI",
				basicPackageName + "BasicToggleButtonUI", "PopupMenuUI",
				basicPackageName + "BasicPopupMenuUI", "ProgressBarUI",
				basicPackageName + "BasicProgressBarUI", "ScrollBarUI",
				basicPackageName + "BasicScrollBarUI", "ScrollPaneUI",
				basicPackageName + "BasicScrollPaneUI", "SplitPaneUI",
				basicPackageName + "BasicSplitPaneUI", "SliderUI",
				basicPackageName + "BasicSliderUI", "SeparatorUI",
				basicPackageName + "BasicSeparatorUI", "SpinnerUI",
				basicPackageName + "BasicSpinnerUI", "ToolBarSeparatorUI",
				basicPackageName + "BasicToolBarSeparatorUI", "PopupMenuSeparatorUI",
				basicPackageName + "BasicPopupMenuSeparatorUI", "TabbedPaneUI",
				basicPackageName + "BasicTabbedPaneUI", "TextAreaUI",
				basicPackageName + "BasicTextAreaUI", "TextFieldUI",
				basicPackageName + "BasicTextFieldUI", "PasswordFieldUI",
				basicPackageName + "BasicPasswordFieldUI", "TextPaneUI",
				basicPackageName + "BasicTextPaneUI", "EditorPaneUI",
				basicPackageName + "BasicEditorPaneUI", "TreeUI", basicPackageName + "BasicTreeUI",
				"LabelUI", basicPackageName + "BasicLabelUI", "ListUI",
				basicPackageName + "BasicListUI", "ToolBarUI", basicPackageName + "BasicToolBarUI",
				"ToolTipUI", basicPackageName + "BasicToolTipUI", "ComboBoxUI",
				basicPackageName + "BasicComboBoxUI", "TableUI", basicPackageName + "BasicTableUI",
				"TableHeaderUI", basicPackageName + "BasicTableHeaderUI", "InternalFrameUI",
				basicPackageName + "BasicInternalFrameUI", "DesktopPaneUI",
				basicPackageName + "BasicDesktopPaneUI", "DesktopIconUI",
				basicPackageName + "BasicDesktopIconUI", "FileChooserUI",
				basicPackageName + "BasicFileChooserUI", "OptionPaneUI",
				basicPackageName + "BasicOptionPaneUI", "PanelUI",
				basicPackageName + "BasicPanelUI", "ViewportUI",
				basicPackageName + "BasicViewportUI", "RootPaneUI",
				basicPackageName + "BasicRootPaneUI", };

		table.putDefaults(uiDefaults);
	}

	/**
	 * Populates {@code table} with system colors. This creates an array of
	 * {@code name-color} pairs and invokes {@code
	 * loadSystemColors}.
	 * <p>
	 * The name is a {@code String} that corresponds to the name of one of the
	 * static {@code SystemColor} fields in the {@code
	 * SystemColor} class. A name-color pair is created for every such
	 * {@code SystemColor} field.
	 * <p>
	 * The {@code color} corresponds to a hex {@code String} as understood by
	 * {@code Color.decode}. For example, one of the {@code name-color} pairs is
	 * {@code
	 * "desktop"-"#005C5C"}. This corresponds to the {@code
	 * SystemColor} field {@code desktop}, with a color value of
	 * {@code new Color(0x005C5C)}.
	 * <p>
	 * The following shows two of the {@code name-color} pairs:
	 * 
	 * <pre>
	 * String[] nameColorPairs = new String[] { "desktop", "#005C5C", "activeCaption", "#000080" };
	 * loadSystemColors(table, nameColorPairs, isNativeLookAndFeel());
	 * </pre>
	 *
	 * As previously stated, this invokes {@code loadSystemColors} with the
	 * supplied {@code table} and {@code name-color} pair array. The last
	 * argument to {@code loadSystemColors} indicates whether the value of the
	 * field in {@code SystemColor} should be used. This method passes the value
	 * of {@code
	 * isNativeLookAndFeel()} as the last argument to {@code loadSystemColors}.
	 *
	 * @param table
	 *            the {@code UIDefaults} object the values are added to
	 * @throws NullPointerException
	 *             if {@code table} is {@code null}
	 *
	 * @see java.awt.SystemColor
	 * @see #getDefaults
	 * @see #loadSystemColors
	 */
	protected void initSystemColorDefaults(UIDefaults table) {
		String[] defaultSystemColors = { "desktop", "#005C5C", /*
																 * Color of the
																 * desktop
																 * background
																 */
				"activeCaption", "#000080", /*
											 * Color for captions (title bars)
											 * when they are active.
											 */
				"activeCaptionText", "#FFFFFF", /*
												 * Text color for text in
												 * captions (title bars).
												 */
				"activeCaptionBorder", "#C0C0C0", /*
													 * Border color for caption
													 * (title bar) window
													 * borders.
													 */
				"inactiveCaption", "#808080", /*
												 * Color for captions (title
												 * bars) when not active.
												 */
				"inactiveCaptionText", "#C0C0C0", /*
													 * Text color for text in
													 * inactive captions (title
													 * bars).
													 */
				"inactiveCaptionBorder", "#C0C0C0", /*
													 * Border color for inactive
													 * caption (title bar)
													 * window borders.
													 */
				"window", "#FFFFFF", /*
										 * Default color for the interior of
										 * windows
										 */
				"windowBorder", "#000000", /* ??? */
				"windowText", "#000000", /* ??? */
				"menu", "#C0C0C0", /* Background color for menus */
				"menuText", "#000000", /* Text color for menus */
				"text", "#C0C0C0", /* Text background color */
				"textText", "#000000", /* Text foreground color */
				"textHighlight", "#000080", /*
											 * Text background color when
											 * selected
											 */
				"textHighlightText", "#FFFFFF", /* Text color when selected */
				"textInactiveText", "#808080", /* Text color when disabled */
				"control", "#C0C0C0", /*
										 * Default color for controls (buttons,
										 * sliders, etc)
										 */
				"controlText", "#000000", /*
											 * Default color for text in
											 * controls
											 */
				"controlHighlight", "#C0C0C0", /*
												 * Specular highlight (opposite
												 * of the shadow)
												 */
				"controlLtHighlight", "#FFFFFF", /*
													 * Highlight color for
													 * controls
													 */
				"controlShadow", "#808080", /* Shadow color for controls */
				"controlDkShadow", "#000000", /*
												 * Dark shadow color for
												 * controls
												 */
				"scrollbar", "#E0E0E0", /*
										 * Scrollbar background (usually the
										 * "track")
										 */
				"info", "#FFFFE1", /* ??? */
				"infoText", "#000000" /* ??? */
		};

		loadSystemColors(table, defaultSystemColors, isNativeLookAndFeel());
	}

	/**
	 * Populates {@code table} with the {@code name-color} pairs in
	 * {@code systemColors}. Refer to
	 * {@link #initSystemColorDefaults(UIDefaults)} for details on the format of
	 * {@code systemColors}.
	 * <p>
	 * An entry is added to {@code table} for each of the {@code name-color}
	 * pairs in {@code systemColors}. The entry key is the {@code name} of the
	 * {@code name-color} pair.
	 * <p>
	 * The value of the entry corresponds to the {@code color} of the
	 * {@code name-color} pair. The value of the entry is calculated in one of
	 * two ways. With either approach the value is always a
	 * {@code ColorUIResource}.
	 * <p>
	 * If {@code useNative} is {@code false}, the {@code color} is created by
	 * using {@code Color.decode} to convert the {@code
	 * String} into a {@code Color}. If {@code decode} can not convert the
	 * {@code String} into a {@code Color} ({@code
	 * NumberFormatException} is thrown) then a {@code
	 * ColorUIResource} of black is used.
	 * <p>
	 * If {@code useNative} is {@code true}, the {@code color} is the value of
	 * the field in {@code SystemColor} with the same name as the {@code name}
	 * of the {@code name-color} pair. If the field is not valid, a
	 * {@code ColorUIResource} of black is used.
	 *
	 * @param table
	 *            the {@code UIDefaults} object the values are added to
	 * @param systemColors
	 *            array of {@code name-color} pairs as described in
	 *            {@link #initSystemColorDefaults(UIDefaults)}
	 * @param useNative
	 *            whether the color is obtained from {@code SystemColor} or
	 *            {@code Color.decode}
	 * @throws NullPointerException
	 *             if {@code systemColors} is {@code null}; or
	 *             {@code systemColors} is not empty, and {@code table} is
	 *             {@code null}; or one of the names of the {@code name-color}
	 *             pairs is {@code null}; or {@code useNative} is {@code false}
	 *             and one of the {@code colors} of the {@code name-color} pairs
	 *             is {@code null}
	 * @throws ArrayIndexOutOfBoundsException
	 *             if {@code useNative} is {@code false} and
	 *             {@code systemColors.length} is odd
	 *
	 * @see #initSystemColorDefaults(javax.swing.UIDefaults)
	 * @see java.awt.SystemColor
	 * @see java.awt.Color#decode(String)
	 */
	protected void loadSystemColors(UIDefaults table, String[] systemColors, boolean useNative) {
	}

	/**
	 * Initialize the defaults table with the name of the ResourceBundle used
	 * for getting localized defaults. Also initialize the default locale used
	 * when no locale is passed into UIDefaults.get(). The default locale should
	 * generally not be relied upon. It is here for compatability with releases
	 * prior to 1.4.
	 */
	private void initResourceBundle(UIDefaults table) {
		table.setDefaultLocale(Locale.getDefault());
		table.addResourceBundle("com.sun.swing.internal.plaf.basic.resources.basic");
	}

	/**
	 * Populates {@code table} with the defaults for the basic look and feel.
	 *
	 * @param table
	 *            the {@code UIDefaults} to add the values to
	 * @throws NullPointerException
	 *             if {@code table} is {@code null}
	 */
	protected void initComponentDefaults(UIDefaults table) {
	}

	static int getFocusAcceleratorKeyMask() {
		return ActionEvent.ALT_MASK;
	}

	/**
	 * Returns the ui that is of type <code>klass</code>, or null if one can not
	 * be found.
	 */
	static Object getUIOfType(ComponentUI ui, Class klass) {
		if (klass.isInstance(ui)) {
			return ui;
		}
		return null;
	}

	// ********* Auditory Cue support methods and objects *********
	// also see the "AuditoryCues" section of the defaults table

	/**
	 * Returns an <code>ActionMap</code> containing the audio actions for this
	 * look and feel.
	 * <P>
	 * The returned <code>ActionMap</code> contains <code>Actions</code> that
	 * embody the ability to render an auditory cue. These auditory cues map
	 * onto user and system activities that may be useful for an end user to
	 * know about (such as a dialog box appearing).
	 * <P>
	 * At the appropriate time, the {@code ComponentUI} is responsible for
	 * obtaining an <code>Action</code> out of the <code>ActionMap</code> and
	 * passing it to <code>playSound</code>.
	 * <P>
	 * This method first looks up the {@code ActionMap} from the defaults using
	 * the key {@code "AuditoryCues.actionMap"}.
	 * <p>
	 * If the value is {@code non-null}, it is returned. If the value of the
	 * default {@code "AuditoryCues.actionMap"} is {@code null} and the value of
	 * the default {@code "AuditoryCues.cueList"} is {@code non-null}, an
	 * {@code ActionMapUIResource} is created and populated. Population is done
	 * by iterating over each of the elements of the
	 * {@code "AuditoryCues.cueList"} array, and invoking
	 * {@code createAudioAction()} to create an {@code
	 * Action} for each element. The resulting {@code Action} is placed in the
	 * {@code ActionMapUIResource}, using the array element as the key. For
	 * example, if the {@code
	 * "AuditoryCues.cueList"} array contains a single-element, {@code
	 * "audioKey"}, the {@code ActionMapUIResource} is created, then populated
	 * by way of {@code actionMap.put(cueList[0],
	 * createAudioAction(cueList[0]))}.
	 * <p>
	 * If the value of the default {@code "AuditoryCues.actionMap"} is
	 * {@code null} and the value of the default {@code "AuditoryCues.cueList"}
	 * is {@code null}, an empty {@code ActionMapUIResource} is created.
	 *
	 *
	 * @return an ActionMap containing {@code Actions} responsible for playing
	 *         auditory cues
	 * @throws ClassCastException
	 *             if the value of the default {@code "AuditoryCues.actionMap"}
	 *             is not an {@code ActionMap}, or the value of the default
	 *             {@code "AuditoryCues.cueList"} is not an {@code Object[]}
	 * @see #createAudioAction
	 * @see #playSound(Action)
	 * @since 1.4
	 */
	protected ActionMap getAudioActionMap() {
		return null;
	}

	/**
	 * Creates and returns an {@code Action} used to play a sound.
	 * <p>
	 * If {@code key} is {@code non-null}, an {@code Action} is created using
	 * the value from the defaults with key {@code key}. The value identifies
	 * the sound resource to load when {@code actionPerformed} is invoked on the
	 * {@code Action}. The sound resource is loaded into a {@code byte[]} by way
	 * of {@code getClass().getResourceAsStream()}.
	 *
	 * @param key
	 *            the key identifying the audio action
	 * @return an {@code Action} used to play the source, or {@code null} if
	 *         {@code key} is {@code null}
	 * @see #playSound(Action)
	 * @since 1.4
	 */
	protected Action createAudioAction(Object key) {
		return null;
	}

	/**
	 * Utility method that loads audio bits for the specified
	 * <code>soundFile</code> filename. If this method is unable to build a
	 * viable path name from the <code>baseClass</code> and
	 * <code>soundFile</code> passed into this method, it will return
	 * <code>null</code>.
	 *
	 * @param soundFile
	 *            the name of the audio file to be retrieved from disk
	 * @return A byte[] with audio data or null
	 * @since 1.4
	 */
	private byte[] loadAudioData(final String soundFile) {
		if (soundFile == null) {
			return null;
		}
		/*
		 * Copy resource into a byte array. This is necessary because several
		 * browsers consider Class.getResource a security risk since it can be
		 * used to load additional classes. Class.getResourceAsStream just
		 * returns raw bytes, which we can convert to a sound.
		 */
		byte[] buffer = AccessController.doPrivileged(new PrivilegedAction<byte[]>() {
			public byte[] run() {
				try {
					InputStream resource = BasicLookAndFeel.this.getClass()
							.getResourceAsStream(soundFile);
					if (resource == null) {
						return null;
					}
					BufferedInputStream in = new BufferedInputStream(resource);
					ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
					byte[] buffer = new byte[1024];
					int n;
					while ((n = in.read(buffer)) > 0) {
						out.write(buffer, 0, n);
					}
					in.close();
					out.flush();
					buffer = out.toByteArray();
					return buffer;
				} catch (IOException ioe) {
					System.err.println(ioe.toString());
					return null;
				}
			}
		});
		if (buffer == null) {
			System.err.println(getClass().getName() + "/" + soundFile + " not found.");
			return null;
		}
		if (buffer.length == 0) {
			System.err.println("warning: " + soundFile + " is zero-length");
			return null;
		}
		return buffer;
	}

	/**
	 * If necessary, invokes {@code actionPerformed} on {@code audioAction} to
	 * play a sound. The {@code actionPerformed} method is invoked if the value
	 * of the {@code "AuditoryCues.playList"} default is a {@code
	 * non-null} {@code Object[]} containing a {@code String} entry equal to the
	 * name of the {@code audioAction}.
	 *
	 * @param audioAction
	 *            an Action that knows how to render the audio associated with
	 *            the system or user activity that is occurring; a value of
	 *            {@code null}, is ignored
	 * @throws ClassCastException
	 *             if {@code audioAction} is {@code non-null} and the value of
	 *             the default {@code "AuditoryCues.playList"} is not an
	 *             {@code Object[]}
	 * @since 1.4
	 */
	protected void playSound(Action audioAction) {
		if (audioAction != null) {
			Object[] audioStrings = (Object[]) UIManager.get("AuditoryCues.playList");
			if (audioStrings != null) {
				// create a HashSet to help us decide to play or not
				HashSet<Object> audioCues = new HashSet<Object>();
				for (Object audioString : audioStrings) {
					audioCues.add(audioString);
				}
				// get the name of the Action
				String actionName = (String) audioAction.getValue(Action.NAME);
				// if the actionName is in the audioCues HashSet, play it.
				if (audioCues.contains(actionName)) {
					audioAction.actionPerformed(
							new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionName));
				}
			}
		}
	}

	/**
	 * Sets the parent of the passed in ActionMap to be the audio action map.
	 */
	static void installAudioActionMap(ActionMap map) {
		LookAndFeel laf = UIManager.getLookAndFeel();
		if (laf instanceof BasicLookAndFeel) {
			map.setParent(((BasicLookAndFeel) laf).getAudioActionMap());
		}
	}

	/**
	 * Helper method to play a named sound.
	 *
	 * @param c
	 *            JComponent to play the sound for.
	 * @param actionKey
	 *            Key for the sound.
	 */
	static void playSound(JComponent c, Object actionKey) {
		LookAndFeel laf = UIManager.getLookAndFeel();
		if (laf instanceof BasicLookAndFeel) {
			ActionMap map = c.getActionMap();
			if (map != null) {
				Action audioAction = map.get(actionKey);
				if (audioAction != null) {
					// pass off firing the Action to a utility method
					((BasicLookAndFeel) laf).playSound(audioAction);
				}
			}
		}
	}

	/**
	 * This class contains listener that watches for all the mouse events that
	 * can possibly invoke popup on the component
	 */
	class AWTEventHelper implements AWTEventListener, PrivilegedAction<Object> {
		AWTEventHelper() {
			super();
			AccessController.doPrivileged(this);
		}

		public Object run() {
			Toolkit tk = Toolkit.getDefaultToolkit();
			if (invocator == null) {
				tk.addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK);
			} else {
				tk.removeAWTEventListener(invocator);
			}
			// Return value not used.
			return null;
		}

		public void eventDispatched(AWTEvent ev) {
		}
	}
}
