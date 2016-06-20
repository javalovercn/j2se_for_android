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

import hc.android.J2SEInitor;
import hc.android.loader.StringUtil;
import hc.android.ScreenAdapter;

import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.CharacterIterator;
import java.util.Locale;
import java.util.Map;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * The <code>Font</code> class represents fonts, which are used to
 * render text in a visible way.
 * A font provides the information needed to map sequences of
 * <em>characters</em> to sequences of <em>glyphs</em>
 * and to render sequences of glyphs on <code>Graphics</code> and
 * <code>Component</code> objects.
 *
 * <h4>Characters and Glyphs</h4>
 *
 * A <em>character</em> is a symbol that represents an item such as a letter,
 * a digit, or punctuation in an abstract way. For example, <code>'g'</code>,
 * <font size=-1>LATIN SMALL LETTER G</font>, is a character.
 * <p>
 * A <em>glyph</em> is a shape used to render a character or a sequence of
 * characters. In simple writing systems, such as Latin, typically one glyph
 * represents one character. In general, however, characters and glyphs do not
 * have one-to-one correspondence. For example, the character '&aacute;'
 * <font size=-1>LATIN SMALL LETTER A WITH ACUTE</font>, can be represented by
 * two glyphs: one for 'a' and one for '&acute;'. On the other hand, the
 * two-character string "fi" can be represented by a single glyph, an
 * "fi" ligature. In complex writing systems, such as Arabic or the South
 * and South-East Asian writing systems, the relationship between characters
 * and glyphs can be more complicated and involve context-dependent selection
 * of glyphs as well as glyph reordering.
 *
 * A font encapsulates the collection of glyphs needed to render a selected set
 * of characters as well as the tables needed to map sequences of characters to
 * corresponding sequences of glyphs.
 *
 * <h4>Physical and Logical Fonts</h4>
 *
 * The Java Platform distinguishes between two kinds of fonts:
 * <em>physical</em> fonts and <em>logical</em> fonts.
 * <p>
 * <em>Physical</em> fonts are the actual font libraries containing glyph data
 * and tables to map from character sequences to glyph sequences, using a font
 * technology such as TrueType or PostScript Type 1.
 * All implementations of the Java Platform must support TrueType fonts;
 * support for other font technologies is implementation dependent.
 * Physical fonts may use names such as Helvetica, Palatino, HonMincho, or
 * any number of other font names.
 * Typically, each physical font supports only a limited set of writing
 * systems, for example, only Latin characters or only Japanese and Basic
 * Latin.
 * The set of available physical fonts varies between configurations.
 * Applications that require specific fonts can bundle them and instantiate
 * them using the {@link #createFont createFont} method.
 * <p>
 * <em>Logical</em> fonts are the five font families defined by the Java
 * platform which must be supported by any Java runtime environment:
 * Serif, SansSerif, Monospaced, Dialog, and DialogInput.
 * These logical fonts are not actual font libraries. Instead, the logical
 * font names are mapped to physical fonts by the Java runtime environment.
 * The mapping is implementation and usually locale dependent, so the look
 * and the metrics provided by them vary.
 * Typically, each logical font name maps to several physical fonts in order to
 * cover a large range of characters.
 * <p>
 * Peered AWT components, such as {@link Label Label} and
 * {@link TextField TextField}, can only use logical fonts.
 * <p>
 * For a discussion of the relative advantages and disadvantages of using
 * physical or logical fonts, see the
 * <a href="http://java.sun.com/j2se/corejava/intl/reference/faqs/index.html#desktop-rendering">Internationalization FAQ</a>
 * document.
 *
 * <h4>Font Faces and Names</h4>
 *
 * A <code>Font</code>
 * can have many faces, such as heavy, medium, oblique, gothic and
 * regular. All of these faces have similar typographic design.
 * <p>
 * There are three different names that you can get from a
 * <code>Font</code> object.  The <em>logical font name</em> is simply the
 * name that was used to construct the font.
 * The <em>font face name</em>, or just <em>font name</em> for
 * short, is the name of a particular font face, like Helvetica Bold. The
 * <em>family name</em> is the name of the font family that determines the
 * typographic design across several faces, like Helvetica.
 * <p>
 * The <code>Font</code> class represents an instance of a font face from
 * a collection of  font faces that are present in the system resources
 * of the host system.  As examples, Arial Bold and Courier Bold Italic
 * are font faces.  There can be several <code>Font</code> objects
 * associated with a font face, each differing in size, style, transform
 * and font features.
 * <p>
 * The {@link GraphicsEnvironment#getAllFonts() getAllFonts} method
 * of the <code>GraphicsEnvironment</code> class returns an
 * array of all font faces available in the system. These font faces are
 * returned as <code>Font</code> objects with a size of 1, identity
 * transform and default font features. These
 * base fonts can then be used to derive new <code>Font</code> objects
 * with varying sizes, styles, transforms and font features via the
 * <code>deriveFont</code> methods in this class.
 *
 * <h4>Font and TextAttribute</h4>
 *
 * <p><code>Font</code> supports most
 * <code>TextAttribute</code>s.  This makes some operations, such as
 * rendering underlined text, convenient since it is not
 * necessary to explicitly construct a <code>TextLayout</code> object.
 * Attributes can be set on a Font by constructing or deriving it
 * using a <code>Map</code> of <code>TextAttribute</code> values.
 *
 * <p>The values of some <code>TextAttributes</code> are not
 * serializable, and therefore attempting to serialize an instance of
 * <code>Font</code> that has such values will not serialize them.
 * This means a Font deserialized from such a stream will not compare
 * equal to the original Font that contained the non-serializable
 * attributes.  This should very rarely pose a problem
 * since these attributes are typically used only in special
 * circumstances and are unlikely to be serialized.
 *
 * <ul>
 * <li><code>FOREGROUND</code> and <code>BACKGROUND</code> use
 * <code>Paint</code> values. The subclass <code>Color</code> is
 * serializable, while <code>GradientPaint</code> and
 * <code>TexturePaint</code> are not.</li>
 * <li><code>CHAR_REPLACEMENT</code> uses
 * <code>GraphicAttribute</code> values.  The subclasses
 * <code>ShapeGraphicAttribute</code> and
 * <code>ImageGraphicAttribute</code> are not serializable.</li>
 * <li><code>INPUT_METHOD_HIGHLIGHT</code> uses
 * <code>InputMethodHighlight</code> values, which are
 * not serializable.  See {@link java.awt.im.InputMethodHighlight}.</li>
 * </ul>
 *
 * Clients who create custom subclasses of <code>Paint</code> and
 * <code>GraphicAttribute</code> can make them serializable and
 * avoid this problem.  Clients who use input method highlights can
 * convert these to the platform-specific attributes for that
 * highlight on the current platform and set them on the Font as
 * a workaround.</p>
 *
 * <p>The <code>Map</code>-based constructor and
 * <code>deriveFont</code> APIs ignore the FONT attribute, and it is
 * not retained by the Font; the static {@link #getFont} method should
 * be used if the FONT attribute might be present.  See {@link
 * java.awt.font.TextAttribute#FONT} for more information.</p>
 *
 * <p>Several attributes will cause additional rendering overhead
 * and potentially invoke layout.  If a <code>Font</code> has such
 * attributes, the <code>{@link #hasLayoutAttributes()}</code> method
 * will return true.</p>
 *
 * <p>Note: Font rotations can cause text baselines to be rotated.  In
 * order to account for this (rare) possibility, font APIs are
 * specified to return metrics and take parameters 'in
 * baseline-relative coordinates'.  This maps the 'x' coordinate to
 * the advance along the baseline, (positive x is forward along the
 * baseline), and the 'y' coordinate to a distance along the
 * perpendicular to the baseline at 'x' (positive y is 90 degrees
 * clockwise from the baseline vector).  APIs for which this is
 * especially important are called out as having 'baseline-relative
 * coordinates.'
 */
public class Font {
	public final ScreenAdapter screenAdapter = J2SEInitor.initAdapter();
	public Typeface typeface;

	public static final String DIALOG = "Dialog";
	public static final String DIALOG_INPUT = "DialogInput";
	public static final String SANS_SERIF = "SansSerif";
	public static final String SERIF = "Serif";
	public static final String MONOSPACED = "Monospaced";

	public static final int PLAIN = 0;

	public static final int BOLD = 1;

	public static final int ITALIC = 2;

	public static final int ROMAN_BASELINE = 0;

	public static final int CENTER_BASELINE = 1;

	public static final int HANGING_BASELINE = 2;

	public static final int TRUETYPE_FONT = 0;

	public static final int TYPE1_FONT = 1;

	protected String name;
	protected int style;
	protected int size;
	protected float pointSize;
	
	public Font(final String name, final int style, final int size) {
		this.name = (name != null) ? name : "Default";
		this.style = (style & ~0x03) == 0 ? style : 0;
		this.size = size;
        this.pointSize = size;
        
		init();
	}

	public Font(final Map<? extends Attribute, ?> attributes) {
		// initFromValues(AttributeValues.fromMap(attributes, RECOGNIZED_MASK));
		// this.name = values.getFamily();
		// this.pointSize = values.getSize();
		// this.size = (int)(values.getSize() + 0.5);
		// if (values.getWeight() >= 2f) this.style |= BOLD; // not == 2f
		// if (values.getPosture() >= .2f) this.style |= ITALIC; // not == .2f
		//
		// this.nonIdentityTx = values.anyNonDefault(EXTRA_MASK);
		// this.hasLayoutAttributes = values.anyNonDefault(LAYOUT_MASK);

		this.name = (String) attributes.get(TextAttribute.FAMILY);
		this.size = (int) ((Integer) attributes.get(TextAttribute.SIZE) + 0.5);
		this.pointSize = (Integer) attributes.get(TextAttribute.SIZE);
		
		init();
	}

	public Font(final Font font) {
		this.name = font.name;
		this.style = font.style;
		this.size = font.size;
        this.pointSize = font.pointSize;
        
		init();
	}

    public Font deriveFont(final int style){
    	return this;
    }
    
	private static int convertToTypefaceStyle(final int inp_style) {
		int style = 0;
		if (inp_style == Font.BOLD) {
			style = Typeface.BOLD;
		} else if (inp_style == Font.ITALIC) {
			style = Typeface.ITALIC;
		} else if (inp_style == (Font.BOLD | Font.ITALIC)) {
			style = Typeface.BOLD_ITALIC;
		} else {
			style = Typeface.NORMAL;
		}
		return style;
	}

	private void init() {
		try {
			if (name.equals(DIALOG)) {
				typeface = Typeface.create(Typeface.DEFAULT_BOLD,
						convertToTypefaceStyle(style));
			} else if (name.equals(DIALOG_INPUT)) {
				typeface = Typeface.create(Typeface.DEFAULT,
						convertToTypefaceStyle(style));
			} else if (name.equals(SANS_SERIF)) {
				typeface = Typeface.create(Typeface.SANS_SERIF,
						convertToTypefaceStyle(style));
			} else if (name.equals(SERIF)) {
				typeface = Typeface.create(Typeface.SERIF,
						convertToTypefaceStyle(style));
			} else if (name.equals(MONOSPACED)) {
				typeface = Typeface.create(Typeface.MONOSPACE,
						convertToTypefaceStyle(style));
			} else {
				typeface = Typeface.create(name, convertToTypefaceStyle(style));
			}
		} catch (final Exception e) {
		}

		if (typeface == null) {
			name = DIALOG_INPUT;
			typeface = Typeface.create(Typeface.DEFAULT,
					convertToTypefaceStyle(style));
		}
	}

	public String getFamily() {
		return name;
	}

	public String getFamily(final Locale l) {
		return name;
	}

	public String getName() {
		return name;
	}

	public String getFontName() {
		return getFamily();
	}

	public String getFontName(final Locale l) {
		return getFamily();
	}

	public Rectangle2D getStringBounds(final String str, final FontRenderContext frc) {
		final Rect rect = getStrRect(str);
		final Rectangle2D.Float r2d = new Rectangle2D.Float(0, 0, rect.width(),
				rect.height());
		return r2d;
	}

	public Rectangle2D getStringBounds(final String str, final int beginIndex, final int limit,
			final FontRenderContext frc) {
		final String substr = str.substring(beginIndex, limit);
		return getStringBounds(substr, beginIndex, limit, frc);
	}

	public Rectangle2D getStringBounds(final char[] chars, final int beginIndex, final int limit,
			final FontRenderContext frc) {
		final String str = String.valueOf(chars, beginIndex, limit);
		return getStringBounds(str, frc);
	}

	public Rectangle2D getStringBounds(final CharacterIterator ci, final int beginIndex,
			final int limit, final FontRenderContext frc) {
		return getStringBounds(StringUtil.toString(ci, beginIndex, limit), frc);
	}

	public int getStyle() {
		return style;
	}

	public int getSize() {
		return size;
	}

	public float getSize2D() {
		return pointSize;
	}

	public boolean isPlain() {
		return style == 0;
	}

	public boolean isBold() {
		return (style & BOLD) != 0;
	}

	public boolean isItalic() {
		return (style & ITALIC) != 0;
	}

	transient int hash;

	public int hashCode() {
		if (hash == 0) {
			hash = name.hashCode() ^ style ^ size;
		}
		return hash;
	}

	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj != null) {
			try {
				final Font font = (Font) obj;
				if (size == font.size && style == font.style
						&& name.equals(font.name)) {
					return true;
				}
			} catch (final ClassCastException e) {
			}
		}
		return false;
	}

	final Rect getStrRect(final String str) {
		final Paint paint = new Paint();
		paint.setTypeface(typeface);
//		if(screenAdapter == null){
//			final String msg = "Warning : java.awt.Font should be instanced in Mlet constructor";
//			L.V = L.O ? false : LogManager.warning(msg);
//			hc.util.ClassUtil.printCurrentThreadStack(msg);
//		}
		paint.setTextSize(screenAdapter.getFontSizeInPixel(size));
		final Rect bounds = new Rect();
		paint.getTextBounds(str, 0, str.length(), bounds);
		return bounds;
	}

	public String toString() {
		return "[family=" + getFamily() + ",name=" + name + ",size=" + size + "]";
	}

	public static final int LAYOUT_LEFT_TO_RIGHT = 0;

	public static final int LAYOUT_RIGHT_TO_LEFT = 1;

	public static final int LAYOUT_NO_START_CONTEXT = 2;

	public static final int LAYOUT_NO_LIMIT_CONTEXT = 4;

}
