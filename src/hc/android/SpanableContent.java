package hc.android;

import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.AbstractDocument.Content;
import javax.swing.undo.UndoableEdit;
import android.text.SpannableStringBuilder;

/**
 * Android专用Content
 */
public class SpanableContent implements Content {
	public final SpannableStringBuilder spanBuilder;

	public SpanableContent() {
		spanBuilder = new SpannableStringBuilder();
	}

	@Override
	public Position createPosition(int offset) throws BadLocationException {
		AndroidClassUtil.callEmptyMethod();
		return null;
	}

	@Override
	public int length() {
		return spanBuilder.length();
	}

	@Override
	public UndoableEdit insertString(int where, String str) throws BadLocationException {
		if (where > spanBuilder.length() || where < 0) {
			throw new BadLocationException("bad location", where);
		}

		spanBuilder.replace(where, where, str);
		return null;
	}

	@Override
	public UndoableEdit remove(int where, int nitems) throws BadLocationException {
		if (spanBuilder.length() < (where + nitems) || nitems < 0 || where < 0) {
			throw new BadLocationException("bad location", where);
		}

		spanBuilder.delete(where, where + nitems);
		return null;
	}

	@Override
	public String getString(int where, int len) throws BadLocationException {
		if (spanBuilder.length() < (where + len) || len < 0 || where < 0) {
			throw new BadLocationException("bad location", where);
		}

		return spanBuilder.subSequence(where, where + len).toString();
	}

	char[] segChars;

	@Override
	public void getChars(int where, int len, Segment txt) throws BadLocationException {
		if (spanBuilder.length() < (where + len) || len < 0 || where < 0) {
			throw new BadLocationException("bad location", where);
		}

		if (segChars == null || segChars.length < len) {
			segChars = new char[len];
		}

		txt.array = segChars;
		txt.offset = 0;
		txt.count = len;

		spanBuilder.getChars(where, where + len, segChars, 0);
	}
}
