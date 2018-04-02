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
import hc.util.ResourceUtil;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.FileChooserUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * <code>JFileChooser</code> provides a simple mechanism for the user to choose
 * a file. For information about using <code>JFileChooser</code>, see <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/filechooser.html">How
 * to Use File Choosers</a>, a section in <em>The Java Tutorial</em>.
 *
 * <p>
 *
 * The following code pops up a file chooser for the user's home directory that
 * sees only .jpg and .gif images:
 * 
 * <pre>
 * JFileChooser chooser = new JFileChooser();
 * FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
 * chooser.setFileFilter(filter);
 * int returnVal = chooser.showOpenDialog(parent);
 * if (returnVal == JFileChooser.APPROVE_OPTION) {
 * 	System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
 * }
 * </pre>
 * <p>
 * <strong>Warning:</strong> Swing is not thread safe. For more information see
 * <a href="package-summary.html#threading">Swing's Threading Policy</a>.
 *
 * @beaninfo attribute: isContainer false description: A component which allows
 *           for the interactive selection of a file.
 *
 * @author Jeff Dinkins
 *
 */
public class JFileChooser extends JComponent implements Accessible {

	private static final File UP_LEVEL_FILE = new File(FileSystemView.UP_LEVEL_DIR_TAG);

	private static final String uiClassID = "FileChooserUI";

	public static final int OPEN_DIALOG = 0;
	public static final int SAVE_DIALOG = 1;
	public static final int CUSTOM_DIALOG = 2;

	public static final int CANCEL_OPTION = 1;
	public static final int APPROVE_OPTION = 0;
	public static final int ERROR_OPTION = -1;

	public static final int FILES_ONLY = 0;
	public static final int DIRECTORIES_ONLY = 1;
	public static final int FILES_AND_DIRECTORIES = 2;

	public static final String CANCEL_SELECTION = "CancelSelection";
	public static final String APPROVE_SELECTION = "ApproveSelection";
	public static final String APPROVE_BUTTON_TEXT_CHANGED_PROPERTY = "ApproveButtonTextChangedProperty";

	public static final String APPROVE_BUTTON_TOOL_TIP_TEXT_CHANGED_PROPERTY = "ApproveButtonToolTipTextChangedProperty";
	public static final String APPROVE_BUTTON_MNEMONIC_CHANGED_PROPERTY = "ApproveButtonMnemonicChangedProperty";
	public static final String CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY = "ControlButtonsAreShownChangedProperty";
	public static final String DIRECTORY_CHANGED_PROPERTY = "directoryChanged";
	public static final String SELECTED_FILE_CHANGED_PROPERTY = "SelectedFileChangedProperty";
	public static final String SELECTED_FILES_CHANGED_PROPERTY = "SelectedFilesChangedProperty";
	public static final String MULTI_SELECTION_ENABLED_CHANGED_PROPERTY = "MultiSelectionEnabledChangedProperty";

	public static final String FILE_SYSTEM_VIEW_CHANGED_PROPERTY = "FileSystemViewChanged";
	public static final String FILE_VIEW_CHANGED_PROPERTY = "fileViewChanged";
	public static final String FILE_HIDING_CHANGED_PROPERTY = "FileHidingChanged";
	public static final String FILE_FILTER_CHANGED_PROPERTY = "fileFilterChanged";

	public static final String FILE_SELECTION_MODE_CHANGED_PROPERTY = "fileSelectionChanged";
	public static final String ACCESSORY_CHANGED_PROPERTY = "AccessoryChangedProperty";

	public static final String ACCEPT_ALL_FILE_FILTER_USED_CHANGED_PROPERTY = "acceptAllFileFilterUsedChanged";
	public static final String DIALOG_TITLE_CHANGED_PROPERTY = "DialogTitleChangedProperty";
	public static final String DIALOG_TYPE_CHANGED_PROPERTY = "DialogTypeChangedProperty";
	public static final String CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY = "ChoosableFileFilterChangedProperty";

	private String dialogTitle = null;
	private String approveButtonText = null;
	private String approveButtonToolTipText = null;
	private int approveButtonMnemonic = 0;

	private Vector<FileFilter> filters = new Vector<FileFilter>(5);
	private int dialogType = OPEN_DIALOG;
	private int returnValue = ERROR_OPTION;
	private JComponent accessory = null;

	private FileView fileView = null;

	private boolean controlsShown = true;
	private boolean useFileHiding = true;
	private int fileSelectionMode = FILES_ONLY;
	private boolean multiSelectionEnabled = false;
	private boolean useAcceptAllFileFilter = true;
	private boolean dragEnabled = false;
	private FileFilter fileFilter = null;
	private FileSystemView fileSystemView = null;

	private File currentDirectory = null;
	private File selectedFile = null;
	private File[] selectedFiles;

	JTable filesTable;
	JScrollPane scrollTable;
	JComboBox<String> rootCB, filterCB;
	Vector<File> dataFileVector = new Vector<File>();

	final Vector<FileFilter> filterVector = new Vector<FileFilter>();
	final FileFilter allFileFilter = new FileFilter() {
		@Override
		public String getDescription() {
			return "all files (*.*)";
		}

		@Override
		public boolean accept(File f) {
			return true;
		}
	};

	public JFileChooser() {
		this((File) null, (FileSystemView) null);
	}

	public JFileChooser(String currentDirectoryPath) {
		this(currentDirectoryPath, (FileSystemView) null);
	}

	public JFileChooser(File currentDirectory) {
		this(currentDirectory, (FileSystemView) null);
	}

	public JFileChooser(FileSystemView fsv) {
		this((File) null, fsv);
	}

	public JFileChooser(File currentDirectory, FileSystemView fsv) {
		setup(fsv);
		setCurrentDirectory(currentDirectory);
	}

	public JFileChooser(String currentDirectoryPath, FileSystemView fsv) {
		setup(fsv);
		if (currentDirectoryPath == null) {
			setCurrentDirectory(null);
		} else {
			setCurrentDirectory(fileSystemView.createFileObject(currentDirectoryPath));
		}
	}

	protected void setup(FileSystemView view) {
		if (view == null) {
			view = FileSystemView.getFileSystemView();
		}
		setFileSystemView(view);
		filterVector.add(allFileFilter);
		filterCB = new JComboBox<String>(new ComboBoxModel<String>() {
			@Override
			public void removeListDataListener(ListDataListener l) {
			}

			@Override
			public int getSize() {
				return filterVector.size();
			}

			@Override
			public String getElementAt(int index) {
				return filterVector.elementAt(index).getDescription();
			}

			@Override
			public void addListDataListener(ListDataListener l) {
			}

			@Override
			public void setSelectedItem(Object anItem) {
				selected = anItem;
			}

			Object selected;

			@Override
			public Object getSelectedItem() {
				return selected;
			}
		});

		updateUI();
		if (isAcceptAllFileFilterUsed()) {
			setFileFilter(getAcceptAllFileFilter());
		}
	}

	public void setDragEnabled(boolean b) {
		dragEnabled = b;
	}

	public boolean getDragEnabled() {
		return dragEnabled;
	}

	public File getSelectedFile() {
		return selectedFile;
	}

	public void setSelectedFile(File file) {
		selectedFile = file;
		if (selectedFile != null) {
			if (file.isAbsolute()
					&& !getFileSystemView().isParent(getCurrentDirectory(), selectedFile)) {
				setCurrentDirectory(selectedFile.getParentFile());
			}
			if (!isMultiSelectionEnabled() || selectedFiles == null || selectedFiles.length == 1) {
				ensureFileIsVisible(selectedFile);
			}
		}
	}

	public File[] getSelectedFiles() {
		if (selectedFiles == null) {
			return new File[0];
		} else {
			return selectedFiles.clone();
		}
	}

	public void setSelectedFiles(File[] selectedFiles) {
		if (selectedFiles == null || selectedFiles.length == 0) {
			selectedFiles = null;
			this.selectedFiles = null;
			setSelectedFile(null);
		} else {
			this.selectedFiles = selectedFiles.clone();
			setSelectedFile(this.selectedFiles[0]);
		}
	}

	public File getCurrentDirectory() {
		return currentDirectory;
	}

	public void setCurrentDirectory(File dir) {
		if (dir != null && !dir.exists()) {
			dir = currentDirectory;
		}
		if (dir == null) {
			dir = getFileSystemView().getDefaultDirectory();
		}
		if (currentDirectory != null) {
			if (this.currentDirectory.equals(dir)) {
				return;
			}
		}

		currentDirectory = dir;
	}

	public void changeToParentDirectory() {
		String absPath = currentDirectory.getAbsolutePath();
		if ((absPath.equals("/") == false)) {
			int idx = absPath.lastIndexOf("/");
			if (idx == 0) {
				setCurrentDirectory(new File("/"));
			} else {
				setCurrentDirectory(new File(absPath.substring(0, idx)));
			}
		}
	}

	public void rescanCurrentDirectory() {
		refreshTableAdAPI();
	}

	public void ensureFileIsVisible(File f) {
		AndroidClassUtil.callEmptyMethod();
	}

	public int showOpenDialog(Component parent) throws HeadlessException {
		setDialogType(OPEN_DIALOG);
		return showDialog(parent, null);
	}

	public int showSaveDialog(Component parent) throws HeadlessException {
		setDialogType(SAVE_DIALOG);
		return showDialog(parent, null);
	}

	public int showDialog(Component parent, String approveButtonText) throws HeadlessException {
		if (approveButtonText != null) {
			setApproveButtonText(approveButtonText);
			setDialogType(CUSTOM_DIALOG);
		}
		returnValue = ERROR_OPTION;

		createDialog(parent).show();

		return returnValue;
	}

	private void enterOrSelectRowAdAPI(int row, boolean isTouch) {
		if (row < dataFileVector.size() && row >= 0) {// 注意：与filesTable.addMouseListener进入保持同步
			if (dataFileVector.elementAt(row) == UP_LEVEL_FILE) {// 注意：与filesTable.addMouseListener进入保持同步
				changeToParentDirectory();
				refreshTableAdAPI();
			} else if (dataFileVector.elementAt(row).isDirectory()) {
				setCurrentDirectory(dataFileVector.elementAt(row));
				refreshTableAdAPI();
			} else {
				if (isTouch) {
					// 因Click事件自动选中
				} else {
					// 选择或反选
					if (filesTable.isRowSelected(row)) {
						filesTable.removeRowSelectionInterval(row, row);
					} else {
						filesTable.addRowSelectionInterval(row, row);
					}
				}
			}
		}
	}

	private String[] convertFilterStrAdAPI() {
		String[] out = new String[filterVector.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = filterVector.elementAt(i).getDescription();
		}
		return out;
	}

	protected JDialog createDialog(Component parent) throws HeadlessException {
		JPanel filePanel = new JPanel(new BorderLayout());
		{
			String[] rootDir = convertRootToStr();
			rootCB = new JComboBox<String>(rootDir);

			filesTable = new JTable();
			scrollTable = new JScrollPane(filesTable);

			filesTable.setSelectedWhenFocusAdAPI(false);
			filesTable.setFocusableColumnAdAPI(new JTable.FocusableColumn() {
				@Override
				public boolean isFocusable(int columnIdx) {
					if (columnIdx == 0) {
						return true;
					}
					return false;
				}
			});
			filesTable.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					final int row = filesTable.rowAtPoint(e.getPoint());// 获得行位置
					// e.getClickCount() == 2 || e.getClickCount() == 1
					enterOrSelectRowAdAPI(row, true);
				}
			});
			filesTable.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
				}

				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						enterOrSelectRowAdAPI(filesTable.getCurrentFocusRowAdAPI(), false);
					}
				}
			});
			filesTable.setSelectionMode(
					multiSelectionEnabled ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
							: ListSelectionModel.SINGLE_SELECTION);
			final Vector<String> dataTableTitleVector = new Vector<String>();
			dataTableTitleVector.add((String) ResourceUtil.get(7004));// select
																		// file
																		// name
			refreshTableAdAPI();
			filesTable.setModel(new AbstractTableModel() {// dataFileVector,
															// dataTableTitleVector){
				@Override
				public Object getValueAt(int rowIndex, int columnIndex) {
					File file = null;
					try {
						file = dataFileVector.elementAt(rowIndex);
					} catch (Exception e) {
					}
					if (file == null) {
						return "";
					} else {
						return JFileChooser.this.getFileSystemView().getSystemDisplayName(file);
					}
				}

				@Override
				public int getRowCount() {
					return dataFileVector.size();
				}

				@Override
				public int getColumnCount() {
					return dataTableTitleVector.size();
				}

				@Override
				public String getColumnName(int column) {
					return dataTableTitleVector.elementAt(column);
				}

				@Override
				public int findColumn(String columnName) {
					for (int i = 0; i < getColumnCount(); i++) {
						if (columnName.equals(getColumnName(i))) {
							return i;
						}
					}
					return -1;
				}
			});
			filesTable.getColumnModel().getColumn(0)
					.setCellRenderer(new DefaultTableCellRenderer() {
						public Component getTableCellRendererComponent(JTable table, Object value,
								boolean isSelected, boolean hasFocus, int row, int column) {
							JLabel label = (JLabel) super.getTableCellRendererComponent(table,
									value, isSelected, hasFocus, row, column);
							if (value == null) {
								label.setText("");
							} else {
								label.setText((String) value);
							}
							if (row < dataFileVector.size()) {
								File file = dataFileVector.elementAt(row);
								Icon icon = JFileChooser.this.getFileSystemView()
										.getSystemIcon(file);
								label.setIcon(icon);
							} else {
								label.setIcon(null);
							}
							return label;
						}
					});
			rootCB.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					int selectedIdx = rootCB.getSelectedIndex();
					File file = getFileSystemView().getRoots()[selectedIdx];
					setCurrentDirectory(file);
					refreshTableAdAPI();
				}
			});
			filterCB.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					int selectedIdx = filterCB.getSelectedIndex();
					fileFilter = filterVector.elementAt(selectedIdx);
					refreshTableAdAPI();
				}
			});
			filePanel.add(rootCB, BorderLayout.NORTH);
			rootCB.requestFocus();

			filePanel.add(scrollTable, BorderLayout.CENTER);

			filePanel.add(filterCB, BorderLayout.SOUTH);
		}

		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isMultiSelectionEnabled()) {
					int[] rows = filesTable.getSelectedRows();
					if(rows.length == 0 || rows.length > 0 && rows[0] == -1) {
						cancelSelection();
						return;
					}
					selectedFiles = convertToFileAdAPI(rows);
				} else {
					int selectedRow = filesTable.getSelectedRow();
					if(selectedRow == -1) {
						cancelSelection();
						return;
					}
					int[] rows = { selectedRow };
					selectedFile = convertToFileAdAPI(rows)[0];
				}
				approveSelection();
			}
		};
		ActionListener cancelListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelSelection();
			}
		};

		return (JDialog) App.showCenterPanelMain(filePanel, 0, 0,
				(dialogTitle == null) ? "Choose / Save file" : dialogTitle, true, null, null,
				listener, cancelListener, null, true, false, parent, false, false);
	}

	private final File[] convertToFileAdAPI(int[] rows) {
		File[] out = new File[rows.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = dataFileVector.elementAt(rows[i]);
		}
		return out;
	}

	private void refreshTableAdAPI() {
		if (rootCB != null && filesTable != null) {
			dataFileVector.clear();

			if (currentDirectory == null) {
				int idx = rootCB.getSelectedIndex();
				currentDirectory = getFileSystemView().getRoots()[idx];
			}

			File[] currDirList = currentDirectory.listFiles();
			// 优先显示过滤文件
			addItemToTable(currDirList, false);
			if (currentDirectory.getAbsolutePath().equals("/") == false) {
				// 增加上一级
				dataFileVector.add(UP_LEVEL_FILE);
			}
			// 其次是文件夹
			addItemToTable(currDirList, true);

			filesTable.clearSelection();
			filesTable.setRequireFirstRowFocusAfterRefreshContentAdAPI(true, 0);
			filesTable.tableChanged(new TableModelEvent(filesTable.getModel()));

			final JScrollBar verticalScrollBar = scrollTable.getVerticalScrollBar();
			verticalScrollBar.setValue(verticalScrollBar.getMinimum());
			// filesTable.updateUI();
		}
	}

	private void addItemToTable(File[] currDirList, boolean isLimitDirectory) {
		if (currDirList != null) {// 没有访问权限时，会返回null
			for (int i = 0; i < currDirList.length; i++) {
				File file = currDirList[i];
				if (file.isDirectory() == isLimitDirectory) {
					if (fileFilter == null || fileFilter.accept(file)) {
						dataFileVector.add(file);
					}
				}
			}
		}
	}

	private String[] convertRootToStr() {
		File[] files = getFileSystemView().getRoots();
		String[] out = new String[files.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = getFileSystemView().getSystemDisplayName(files[i]);
		}
		return out;
	}

	public boolean getControlButtonsAreShown() {
		return controlsShown;
	}

	public void setControlButtonsAreShown(boolean b) {
		if (controlsShown == b) {
			return;
		}
		controlsShown = b;
	}

	public int getDialogType() {
		return dialogType;
	}

	public void setDialogType(int dialogType) {
		if (this.dialogType == dialogType) {
			return;
		}
		if (!(dialogType == OPEN_DIALOG || dialogType == SAVE_DIALOG
				|| dialogType == CUSTOM_DIALOG)) {
			throw new IllegalArgumentException("Incorrect Dialog Type: " + dialogType);
		}
		this.dialogType = dialogType;
		if (dialogType == OPEN_DIALOG || dialogType == SAVE_DIALOG) {
			setApproveButtonText(null);
		}
	}

	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	public String getDialogTitle() {
		return dialogTitle;
	}

	public void setApproveButtonToolTipText(String toolTipText) {
		if (approveButtonToolTipText == toolTipText) {
			return;
		}
		approveButtonToolTipText = toolTipText;
	}

	public String getApproveButtonToolTipText() {
		return approveButtonToolTipText;
	}

	public int getApproveButtonMnemonic() {
		return approveButtonMnemonic;
	}

	public void setApproveButtonMnemonic(int mnemonic) {
		if (approveButtonMnemonic == mnemonic) {
			return;
		}
		approveButtonMnemonic = mnemonic;
	}

	public void setApproveButtonMnemonic(char mnemonic) {
		int vk = (int) mnemonic;
		if (vk >= 'a' && vk <= 'z') {
			vk -= ('a' - 'A');
		}
		setApproveButtonMnemonic(vk);
	}

	public void setApproveButtonText(String approveButtonText) {
		if (this.approveButtonText == approveButtonText) {
			return;
		}
		this.approveButtonText = approveButtonText;
	}

	public String getApproveButtonText() {
		return approveButtonText;
	}

	public FileFilter[] getChoosableFileFilters() {
		FileFilter[] filterArray = new FileFilter[filters.size()];
		filters.copyInto(filterArray);
		return filterArray;
	}

	public void addChoosableFileFilter(FileFilter filter) {
		if (filter != null && !filters.contains(filter)) {
			filters.addElement(filter);
			if (fileFilter == null && filters.size() == 1) {
				setFileFilter(filter);
			}
		}
	}

	public boolean removeChoosableFileFilter(FileFilter f) {
		if (filters.contains(f)) {
			if (getFileFilter() == f) {
				setFileFilter(null);
			}
			filters.removeElement(f);
			return true;
		} else {
			return false;
		}
	}

	public void resetChoosableFileFilters() {
		setFileFilter(null);
		filters.removeAllElements();
		if (isAcceptAllFileFilterUsed()) {
			addChoosableFileFilter(getAcceptAllFileFilter());
		}
	}

	public FileFilter getAcceptAllFileFilter() {
		return null;
	}

	public boolean isAcceptAllFileFilterUsed() {
		return useAcceptAllFileFilter;
	}

	public void setAcceptAllFileFilterUsed(boolean b) {
		useAcceptAllFileFilter = b;
		if (!b) {
			removeChoosableFileFilter(getAcceptAllFileFilter());
		} else {
			removeChoosableFileFilter(getAcceptAllFileFilter());
			addChoosableFileFilter(getAcceptAllFileFilter());
		}
	}

	public JComponent getAccessory() {
		return accessory;
	}

	public void setAccessory(JComponent newAccessory) {
		accessory = newAccessory;
	}

	/**
	 * FILES_ONLY, DIRECTORIES_ONLY, FILES_AND_DIRECTORIES
	 */
	public void setFileSelectionMode(int mode) {
		if (fileSelectionMode == mode) {
			return;
		}

		if ((mode == FILES_ONLY) || (mode == DIRECTORIES_ONLY) || (mode == FILES_AND_DIRECTORIES)) {
			fileSelectionMode = mode;
		} else {
			throw new IllegalArgumentException("Incorrect Mode for file selection: " + mode);
		}
	}

	public int getFileSelectionMode() {
		return fileSelectionMode;
	}

	public boolean isFileSelectionEnabled() {
		return ((fileSelectionMode == FILES_ONLY) || (fileSelectionMode == FILES_AND_DIRECTORIES));
	}

	public boolean isDirectorySelectionEnabled() {
		return ((fileSelectionMode == DIRECTORIES_ONLY)
				|| (fileSelectionMode == FILES_AND_DIRECTORIES));
	}

	public void setMultiSelectionEnabled(boolean b) {
		if (multiSelectionEnabled == b) {
			return;
		}
		multiSelectionEnabled = b;
	}

	public boolean isMultiSelectionEnabled() {
		return multiSelectionEnabled;
	}

	public boolean isFileHidingEnabled() {
		return useFileHiding;
	}

	public void setFileHidingEnabled(boolean b) {
		useFileHiding = b;
	}

	public void setFileFilter(FileFilter filter) {
		fileFilter = filter;
		if (filter != null) {
			if (filterVector.size() > 1) {
				filterVector.remove(1);
			}
			filterVector.add(fileFilter);
			// 因为addItem事件会处理，所以关闭下列代码
			filterCB.setSelectedIndex(1);
		}
	}

	public FileFilter getFileFilter() {
		return fileFilter;
	}

	public void setFileView(FileView fileView) {
		this.fileView = fileView;
	}

	public FileView getFileView() {
		return fileView;
	}

	public String getName(File f) {
		if (fileView == null) {
			return f.getName();
		} else {
			return fileView.getName(f);
		}
	}

	public String getDescription(File f) {
		if (fileView == null) {
			return f.getName();
		} else {
			return fileView.getDescription(f);
		}
	}

	public String getTypeDescription(File f) {
		if (fileView == null) {
			return f.getName();
		} else {
			return fileView.getTypeDescription(f);
		}
	}

	public Icon getIcon(File f) {
		if (fileView == null) {
			return getFileSystemView().getSystemIcon(f);
		} else {
			return fileView.getIcon(f);
		}
	}

	/**
	 * Returns true if the file (directory) can be visited.
	 */
	public boolean isTraversable(File f) {
		Boolean traversable = null;
		if (f != null) {
			if (getFileView() != null) {
				traversable = getFileView().isTraversable(f);
			}

			if (traversable == null) {
				traversable = getFileSystemView().isTraversable(f);
			}
		}
		return (traversable != null && traversable.booleanValue());
	}

	public boolean accept(File f) {
		boolean shown = true;
		if (f != null && fileFilter != null) {
			shown = fileFilter.accept(f);
		}
		return shown;
	}

	public void setFileSystemView(FileSystemView fsv) {
		fileSystemView = fsv;
	}

	public FileSystemView getFileSystemView() {
		return fileSystemView;
	}

	public void approveSelection() {
		returnValue = APPROVE_OPTION;
		fireActionPerformed(APPROVE_SELECTION);
	}

	public void cancelSelection() {
		returnValue = CANCEL_OPTION;
		fireActionPerformed(CANCEL_SELECTION);
	}

	public void addActionListener(ActionListener l) {
		list.add(ActionListener.class, l);
	}

	public void removeActionListener(ActionListener l) {
		list.remove(ActionListener.class, l);
	}

	public ActionListener[] getActionListeners() {
		return list.getListeners(ActionListener.class);
	}

	protected void fireActionPerformed(String command) {
		Object[] listeners = list.getListenerList();
		ActionEvent e = null;
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				if (e == null) {
					e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command,
							System.currentTimeMillis(), 0);
				}
				((ActionListener) listeners[i + 1]).actionPerformed(e);
			}
		}
	}

	public void updateUI() {
	}

	public String getUIClassID() {
		return uiClassID;
	}

	public FileChooserUI getUI() {
		AndroidClassUtil.callEmptyMethod();
		return (FileChooserUI) null;
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException {
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	protected String paramString() {
		return "";
	}

	protected AccessibleContext accessibleContext = null;

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

}