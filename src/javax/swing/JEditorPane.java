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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleHyperlink;
import javax.accessibility.AccessibleHypertext;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleText;
import javax.swing.event.DocumentEvent;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.CompositeView;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * A text component to edit various kinds of content. You can find how-to
 * information and examples of using editor panes in <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/components/text.html">Using
 * Text Components</a>, a section in <em>The Java Tutorial.</em>
 *
 * <p>
 * This component uses implementations of the <code>EditorKit</code> to
 * accomplish its behavior. It effectively morphs into the proper kind of text
 * editor for the kind of content it is given. The content type that editor is
 * bound to at any given time is determined by the <code>EditorKit</code>
 * currently installed. If the content is set to a new URL, its type is used to
 * determine the <code>EditorKit</code> that should be used to load the content.
 * <p>
 * By default, the following types of content are known:
 * <dl>
 * <dt><b>text/plain</b>
 * <dd>Plain text, which is the default the type given isn't recognized. The kit
 * used in this case is an extension of <code>DefaultEditorKit</code> that
 * produces a wrapped plain text view.
 * <dt><b>text/html</b>
 * <dd>HTML text. The kit used in this case is the class
 * <code>javax.swing.text.html.HTMLEditorKit</code> which provides HTML 3.2
 * support.
 * <dt><b>text/rtf</b>
 * <dd>RTF text. The kit used in this case is the class
 * <code>javax.swing.text.rtf.RTFEditorKit</code> which provides a limited
 * support of the Rich Text Format.
 * </dl>
 * <p>
 * There are several ways to load content into this component.
 * <ol>
 * <li>The {@link #setText setText} method can be used to initialize the
 * component from a string. In this case the current <code>EditorKit</code> will
 * be used, and the content type will be expected to be of this type.
 * <li>The {@link #read read} method can be used to initialize the component
 * from a <code>Reader</code>. Note that if the content type is HTML, relative
 * references (e.g. for things like images) can't be resolved unless the
 * &lt;base&gt; tag is used or the <em>Base</em> property on
 * <code>HTMLDocument</code> is set. In this case the current
 * <code>EditorKit</code> will be used, and the content type will be expected to
 * be of this type.
 * <li>The {@link #setPage setPage} method can be used to initialize the
 * component from a URL. In this case, the content type will be determined from
 * the URL, and the registered <code>EditorKit</code> for that content type will
 * be set.
 * </ol>
 * <p>
 * Some kinds of content may provide hyperlink support by generating hyperlink
 * events. The HTML <code>EditorKit</code> will generate hyperlink events if the
 * <code>JEditorPane</code> is <em>not editable</em>
 * (<code>JEditorPane.setEditable(false);</code> has been called). If HTML
 * frames are embedded in the document, the typical response would be to change
 * a portion of the current document. The following code fragment is a possible
 * hyperlink listener implementation, that treats HTML frame events specially,
 * and simply displays any other activated hyperlinks. <code><pre>

&nbsp;    class Hyperactive implements HyperlinkListener {
&nbsp;
&nbsp;        public void hyperlinkUpdate(HyperlinkEvent e) {
&nbsp;            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
&nbsp;                JEditorPane pane = (JEditorPane) e.getSource();
&nbsp;                if (e instanceof HTMLFrameHyperlinkEvent) {
&nbsp;                    HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)e;
&nbsp;                    HTMLDocument doc = (HTMLDocument)pane.getDocument();
&nbsp;                    doc.processHTMLFrameHyperlinkEvent(evt);
&nbsp;                } else {
&nbsp;                    try {
&nbsp;                        pane.setPage(e.getURL());
&nbsp;                    } catch (Throwable t) {
&nbsp;                        t.printStackTrace();
&nbsp;                    }
&nbsp;                }
&nbsp;            }
&nbsp;        }
&nbsp;    }

 * </pre></code>
 * <p>
 * For information on customizing how <b>text/html</b> is rendered please see
 * {@link #W3C_LENGTH_UNITS} and {@link #HONOR_DISPLAY_PROPERTIES}
 * <p>
 * Culturally dependent information in some documents is handled through a
 * mechanism called character encoding. Character encoding is an unambiguous
 * mapping of the members of a character set (letters, ideographs, digits,
 * symbols, or control functions) to specific numeric code values. It represents
 * the way the file is stored. Example character encodings are ISO-8859-1,
 * ISO-8859-5, Shift-jis, Euc-jp, and UTF-8. When the file is passed to an user
 * agent (<code>JEditorPane</code>) it is converted to the document character
 * set (ISO-10646 aka Unicode).
 * <p>
 * There are multiple ways to get a character set mapping to happen with
 * <code>JEditorPane</code>.
 * <ol>
 * <li>One way is to specify the character set as a parameter of the MIME type.
 * This will be established by a call to the {@link #setContentType
 * setContentType} method. If the content is loaded by the {@link #setPage
 * setPage} method the content type will have been set according to the
 * specification of the URL. It the file is loaded directly, the content type
 * would be expected to have been set prior to loading.
 * <li>Another way the character set can be specified is in the document itself.
 * This requires reading the document prior to determining the character set
 * that is desired. To handle this, it is expected that the
 * <code>EditorKit</code>.read operation throw a
 * <code>ChangedCharSetException</code> which will be caught. The read is then
 * restarted with a new Reader that uses the character set specified in the
 * <code>ChangedCharSetException</code> (which is an <code>IOException</code>).
 * </ol>
 * <p>
 * <dl>
 * <dt><b><font size=+1>Newlines</font></b>
 * <dd>For a discussion on how newlines are handled, see
 * <a href="text/DefaultEditorKit.html">DefaultEditorKit</a>.
 * </dl>
 *
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
 * @beaninfo attribute: isContainer false description: A text component to edit
 *           various types of content.
 *
 * @author Timothy Prinzing
 */
public class JEditorPane extends JTextComponent {

	/**
	 * Creates a new <code>JEditorPane</code>. The document model is set to
	 * <code>null</code>.
	 */
	public JEditorPane() {
		super();
	}

	public JEditorPane(URL initialPage) throws IOException {
		this();
		setPage(initialPage);
	}

	public JEditorPane(String url) throws IOException {
		this(new URL(url));
	}

	public JEditorPane(String type, String text) {
		this();
		setContentType(type);
		setText(text);
	}

	public synchronized void addHyperlinkListener(HyperlinkListener listener) {
		AndroidClassUtil.callEmptyMethod();
	}

	public synchronized void removeHyperlinkListener(HyperlinkListener listener) {
		AndroidClassUtil.callEmptyMethod();
	}

	public synchronized HyperlinkListener[] getHyperlinkListeners() {
		return list.getListeners(javax.swing.event.HyperlinkListener.class);
	}

	public void fireHyperlinkUpdate(HyperlinkEvent e) {
	}

	public void setPage(URL page) throws IOException {
	}

	public void read(InputStream in, Object desc) throws IOException {
	}

	void read(InputStream in, Document doc) throws IOException {
	}

	class PageLoader extends SwingWorker<URL, Object> {

		PageLoader(Document doc, InputStream in, URL old, URL page) {
			this.in = in;
			this.old = old;
			this.page = page;
			this.doc = doc;
		}

		protected URL doInBackground() {
			return null;
		}

		InputStream in;
		URL old;
		URL page;
		Document doc;
	}

	protected InputStream getStream(URL page) throws IOException {
		return null;
	}

	public void scrollToReference(String reference) {
	}

	public URL getPage() {
		return null;
	}

	public void setPage(String url) throws IOException {
	}

	public String getUIClassID() {
		return uiClassID;
	}

	protected EditorKit createDefaultEditorKit() {
		return null;
	}

	public EditorKit getEditorKit() {
		return kit;
	}

	public final String getContentType() {
		return null;
	}

	public final void setContentType(String type) {
	}

	private void setCharsetFromContentTypeParameters(String paramlist) {
	}

	public void setEditorKit(EditorKit kit) {
	}

	public EditorKit getEditorKitForContentType(String type) {
		return null;
	}

	public void setEditorKitForContentType(String type, EditorKit k) {
	}

	public void replaceSelection(String content) {
	}

	public static EditorKit createEditorKitForContentType(String type) {
		return null;
	}

	public static void registerEditorKitForContentType(String type, String classname) {
	}

	public static void registerEditorKitForContentType(String type, String classname,
			ClassLoader loader) {
	}

	public static String getEditorKitClassNameForContentType(String type) {
		return null;
	}

	public Dimension getPreferredSize() {
		return new Dimension(0, 0);
	}

	public void setText(String t) {
	}

	public String getText() {
		String txt;
		txt = null;
		return txt;
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
	}

	private EditorKit kit;
	private boolean isUserSetEditorKit;

	private Hashtable<String, Object> pageProperties;

	final static String PostDataProperty = "javax.swing.JEditorPane.postdata";

	private Hashtable<String, EditorKit> typeHandlers;

	private static final Object kitRegistryKey = new StringBuffer("JEditorPane.kitRegistry");
	private static final Object kitTypeRegistryKey = new StringBuffer(
			"JEditorPane.kitTypeRegistry");
	private static final Object kitLoaderRegistryKey = new StringBuffer(
			"JEditorPane.kitLoaderRegistry");

	private static final String uiClassID = "EditorPaneUI";

	public static final String W3C_LENGTH_UNITS = "JEditorPane.w3cLengthUnits";

	public static final String HONOR_DISPLAY_PROPERTIES = "JEditorPane.honorDisplayProperties";

	static final Map<String, String> defaultEditorKitMap = new HashMap<String, String>(0);

	protected String paramString() {

		return "";
	}

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null) {
			accessibleContext = new AndroidClassUtil().buildAccessibleContext(this);
		}
		return accessibleContext;
	}

	static class PlainEditorKit extends DefaultEditorKit implements ViewFactory {

		public ViewFactory getViewFactory() {
			return this;
		}

		public View create(Element elem) {
			return null;
		}

		View createI18N(Element elem) {
			return null;
		}

		static class PlainParagraph extends javax.swing.text.ParagraphView {

			PlainParagraph(Element elem) {
				super(elem);
			}

			protected void setPropertiesFromAttributes() {
			}

			public int getFlowSpan(int index) {
				return Integer.MAX_VALUE;
			}

			protected SizeRequirements calculateMinorAxisRequirements(int axis,
					SizeRequirements r) {
				return null;
			}

			static class LogicalView extends CompositeView {

				LogicalView(Element elem) {
					super(elem);
				}

				protected int getViewIndexAtPosition(int pos) {
					return 0;
				}

				protected boolean updateChildren(DocumentEvent.ElementChange ec, DocumentEvent e,
						ViewFactory f) {
					return false;
				}

				protected void loadChildren(ViewFactory f) {
				}

				public float getPreferredSpan(int axis) {
					return 0;
				}

				protected void forwardUpdateToView(View v, DocumentEvent e, Shape a,
						ViewFactory f) {
				}

				public void paint(Graphics g, Shape allocation) {
				}

				protected boolean isBefore(int x, int y, Rectangle alloc) {
					return false;
				}

				protected boolean isAfter(int x, int y, Rectangle alloc) {
					return false;
				}

				protected View getViewAtPoint(int x, int y, Rectangle alloc) {
					return null;
				}

				protected void childAllocation(int index, Rectangle a) {
				}
			}
		}
	}

	static class HeaderParser {

		String raw;
		String[][] tab;

		public HeaderParser(String raw) {
			this.raw = raw;
			tab = new String[10][2];
			parse();
		}

		private void parse() {
		}

		public String findKey(int i) {
			return null;
		}

		public String findValue(int i) {
			return null;
		}

		public String findValue(String key) {
			return findValue(key, null);
		}

		public String findValue(String k, String Default) {
			return Default;
		}

		public int findInt(String k, int Default) {
			return 0;
		}
	}

}
