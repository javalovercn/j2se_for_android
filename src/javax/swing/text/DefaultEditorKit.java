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

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import javax.swing.Action;

/**
 * This is the set of things needed by a text component to be a reasonably
 * functioning editor for some <em>type</em> of text document. This
 * implementation provides a default implementation which treats text as plain
 * text and provides a minimal set of actions for a simple editor.
 * <p>
 * <dl>
 * <dt><b><font size=+1>Newlines</font></b>
 * <dd>There are two properties which deal with newlines. The system property,
 * <code>line.separator</code>, is defined to be platform-dependent, either
 * "\n", "\r", or "\r\n". There is also a property defined in
 * <code>DefaultEditorKit</code>, called
 * <a href=#EndOfLineStringProperty><code>EndOfLineStringProperty</code></a>,
 * which is defined automatically when a document is loaded, to be the first
 * occurrence of any of the newline characters. When a document is loaded,
 * <code>EndOfLineStringProperty</code> is set appropriately, and when the
 * document is written back out, the <code>EndOfLineStringProperty</code> is
 * used. But while the document is in memory, the "\n" character is used to
 * define a newline, regardless of how the newline is defined when the document
 * is on disk. Therefore, for searching purposes, "\n" should always be used.
 * When a new document is created, and the <code>EndOfLineStringProperty</code>
 * has not been defined, it will use the System property when writing out the
 * document.
 * <p>
 * Note that <code>EndOfLineStringProperty</code> is set on the
 * <code>Document</code> using the <code>get/putProperty</code> methods.
 * Subclasses may override this behavior.
 *
 * </dl>
 *
 * @author Timothy Prinzing
 */
public class DefaultEditorKit extends EditorKit {

	public DefaultEditorKit() {
	}

	public String getContentType() {
		return "text/plain";
	}

	public ViewFactory getViewFactory() {
		return null;
	}

	public Action[] getActions() {
		return new Action[0];
	}

	public Caret createCaret() {
		return null;
	}

	public Document createDefaultDocument() {
		return null;
	}

	public void read(InputStream in, Document doc, int pos)
			throws IOException, BadLocationException {
	}

	public void write(OutputStream out, Document doc, int pos, int len)
			throws IOException, BadLocationException {
	}

	MutableAttributeSet getInputAttributes() {
		return null;
	}

	public void read(Reader in, Document doc, int pos) throws IOException, BadLocationException {
	}

	public void write(Writer out, Document doc, int pos, int len)
			throws IOException, BadLocationException {
	}

	public static final String EndOfLineStringProperty = "__EndOfLine__";
	public static final String insertContentAction = "insert-content";

	public static final String insertBreakAction = "insert-break";
	public static final String insertTabAction = "insert-tab";
	public static final String deletePrevCharAction = "delete-previous";
	public static final String deleteNextCharAction = "delete-next";

	public static final String deleteNextWordAction = "delete-next-word";
	public static final String deletePrevWordAction = "delete-previous-word";
	public static final String readOnlyAction = "set-read-only";
	public static final String writableAction = "set-writable";
	public static final String cutAction = "cut-to-clipboard";
	public static final String copyAction = "copy-to-clipboard";
	public static final String pasteAction = "paste-from-clipboard";
	public static final String beepAction = "beep";
	public static final String pageUpAction = "page-up";
	public static final String pageDownAction = "page-down";
	static final String selectionPageUpAction = "selection-page-up";
	static final String selectionPageDownAction = "selection-page-down";
	static final String selectionPageLeftAction = "selection-page-left";
	static final String selectionPageRightAction = "selection-page-right";
	public static final String forwardAction = "caret-forward";
	public static final String backwardAction = "caret-backward";
	public static final String selectionForwardAction = "selection-forward";
	public static final String selectionBackwardAction = "selection-backward";
	public static final String upAction = "caret-up";
	public static final String downAction = "caret-down";
	public static final String selectionUpAction = "selection-up";
	public static final String selectionDownAction = "selection-down";
	public static final String beginWordAction = "caret-begin-word";
	public static final String endWordAction = "caret-end-word";
	public static final String selectionBeginWordAction = "selection-begin-word";
	public static final String selectionEndWordAction = "selection-end-word";
	public static final String previousWordAction = "caret-previous-word";
	public static final String nextWordAction = "caret-next-word";
	public static final String selectionPreviousWordAction = "selection-previous-word";
	public static final String selectionNextWordAction = "selection-next-word";
	public static final String beginLineAction = "caret-begin-line";
	public static final String endLineAction = "caret-end-line";
	public static final String selectionBeginLineAction = "selection-begin-line";
	public static final String selectionEndLineAction = "selection-end-line";
	public static final String beginParagraphAction = "caret-begin-paragraph";
	public static final String endParagraphAction = "caret-end-paragraph";
	public static final String selectionBeginParagraphAction = "selection-begin-paragraph";
	public static final String selectionEndParagraphAction = "selection-end-paragraph";
	public static final String beginAction = "caret-begin";
	public static final String endAction = "caret-end";
	public static final String selectionBeginAction = "selection-begin";
	public static final String selectionEndAction = "selection-end";
	public static final String selectWordAction = "select-word";
	public static final String selectLineAction = "select-line";
	public static final String selectParagraphAction = "select-paragraph";
	public static final String selectAllAction = "select-all";
	static final String unselectAction = "unselect";
	static final String toggleComponentOrientationAction = "toggle-componentOrientation";
	public static final String defaultKeyTypedAction = "default-typed";

	public static class DefaultKeyTypedAction extends TextAction {

		public DefaultKeyTypedAction() {
			super(defaultKeyTypedAction);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	public static class InsertContentAction extends TextAction {

		public InsertContentAction() {
			super(insertContentAction);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	public static class InsertBreakAction extends TextAction {

		public InsertBreakAction() {
			super(insertBreakAction);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	public static class InsertTabAction extends TextAction {

		public InsertTabAction() {
			super(insertTabAction);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	static class DeletePrevCharAction extends TextAction {

		DeletePrevCharAction() {
			super(deletePrevCharAction);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	static class DeleteNextCharAction extends TextAction {

		DeleteNextCharAction() {
			super(deleteNextCharAction);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	static class DeleteWordAction extends TextAction {
		DeleteWordAction(String name) {
			super(name);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	static class ReadOnlyAction extends TextAction {

		ReadOnlyAction() {
			super(readOnlyAction);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	static class WritableAction extends TextAction {

		WritableAction() {
			super(writableAction);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	public static class CutAction extends TextAction {

		public CutAction() {
			super(cutAction);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	public static class CopyAction extends TextAction {

		public CopyAction() {
			super(copyAction);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	public static class PasteAction extends TextAction {

		public PasteAction() {
			super(pasteAction);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	public static class BeepAction extends TextAction {

		public BeepAction() {
			super(beepAction);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	static class VerticalPageAction extends TextAction {

		public VerticalPageAction(String nm, int direction, boolean select) {
			super(nm);
		}

		public void actionPerformed(ActionEvent e) {
		}

	}

	static class PageAction extends TextAction {

		public PageAction(String nm, boolean left, boolean select) {
			super(nm);
		}

		public void actionPerformed(ActionEvent e) {
		}

	}

	static class DumpModelAction extends TextAction {

		DumpModelAction() {
			super("dump-model");
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	static class NextVisualPositionAction extends TextAction {

		NextVisualPositionAction(String nm, boolean select, int direction) {
			super(nm);
		}

		public void actionPerformed(ActionEvent e) {
		}

	}

	static class BeginWordAction extends TextAction {

		BeginWordAction(String nm, boolean select) {
			super(nm);
		}

		public void actionPerformed(ActionEvent e) {
		}

	}

	static class EndWordAction extends TextAction {

		EndWordAction(String nm, boolean select) {
			super(nm);
		}

		public void actionPerformed(ActionEvent e) {
		}

	}

	static class PreviousWordAction extends TextAction {

		PreviousWordAction(String nm, boolean select) {
			super(nm);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	static class NextWordAction extends TextAction {

		NextWordAction(String nm, boolean select) {
			super(nm);
		}

		public void actionPerformed(ActionEvent e) {
		}

	}

	static class BeginLineAction extends TextAction {

		BeginLineAction(String nm, boolean select) {
			super(nm);
		}

		public void actionPerformed(ActionEvent e) {
		}

	}

	static class EndLineAction extends TextAction {

		EndLineAction(String nm, boolean select) {
			super(nm);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	static class BeginParagraphAction extends TextAction {

		BeginParagraphAction(String nm, boolean select) {
			super(nm);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	static class EndParagraphAction extends TextAction {

		EndParagraphAction(String nm, boolean select) {
			super(nm);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	static class BeginAction extends TextAction {

		BeginAction(String nm, boolean select) {
			super(nm);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	static class EndAction extends TextAction {

		EndAction(String nm, boolean select) {
			super(nm);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	static class SelectWordAction extends TextAction {

		SelectWordAction() {
			super(selectWordAction);
		}

		public void actionPerformed(ActionEvent e) {
		}

	}

	static class SelectLineAction extends TextAction {
		SelectLineAction() {
			super(selectLineAction);
		}

		public void actionPerformed(ActionEvent e) {
		}

	}

	static class SelectParagraphAction extends TextAction {
		SelectParagraphAction() {
			super(selectParagraphAction);
		}

		public void actionPerformed(ActionEvent e) {
		}

	}

	static class SelectAllAction extends TextAction {

		SelectAllAction() {
			super(selectAllAction);
		}

		public void actionPerformed(ActionEvent e) {
		}

	}

	static class UnselectAction extends TextAction {

		UnselectAction() {
			super(unselectAction);
		}

		public void actionPerformed(ActionEvent e) {
		}

	}

	static class ToggleComponentOrientationAction extends TextAction {

		ToggleComponentOrientationAction() {
			super(toggleComponentOrientationAction);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

}
