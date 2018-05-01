package hc.android;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import hc.App;
import hc.core.ContextManager;
import hc.core.util.LogManager;
import hc.core.util.ReturnableRunnable;
import hc.util.ClassUtil;

public class AndroidUIUtil {
	public static final int DRAW_HELP_ID = HCRUtil.getResource(HCRUtil.R_drawable_question_48);// android.R.drawable.ic_menu_help;
	public static final int DRAW_INFO_ID = HCRUtil.getResource(HCRUtil.R_drawable_info_48);// android.R.drawable.ic_dialog_info;
	public static final int DRAW_WARNING_ID = HCRUtil.getResource(HCRUtil.R_drawable_warning_48);// android.R.drawable.stat_sys_warning;
	public static final int DRAW_ERROR_ID = HCRUtil.getResource(HCRUtil.R_drawable_error_48);// android.R.drawable.stat_notify_error;

	private static final Color DARK_GRAY_COLOR = new Color(android.graphics.Color.DKGRAY);
	public static final int MIN_ICON_SIZE = 16;
	public static final int MIN_DRAWING_ICON_SIZE = MIN_ICON_SIZE;
	public static final Color transFullColor = new Color(0, 0, 0, 0);

	public static final int INDENT_PIXEL = 10;

	public static final Color WINDOW_TRANS_LAYER_COLOR = new Color(ActivityManager.applicationContext
			.getResources().getColor(HCRUtil.getResource(HCRUtil.R_color_window_trans_layer_color)),
			true);

	public static final Color WINDOW_BTN_STROKE_COLOR = new Color(ActivityManager.applicationContext
			.getResources().getColor(HCRUtil.getResource(HCRUtil.R_color_window_btn_stroke_color)));
	public static final Color WINDOW_BTN_TEXT_COLOR = new Color(ActivityManager.applicationContext
			.getResources().getColor(HCRUtil.getResource(HCRUtil.R_color_window_btn_text_color)));
	public static final Color WIN_FONT_DISABLE_COLOR = WINDOW_BTN_STROKE_COLOR;
	public static final Color WIN_BODY_BACK = new Color(ActivityManager.applicationContext.getResources()
			.getColor(HCRUtil.getResource(HCRUtil.R_color_window_body_back_color)));
	public static final Color WIN_BODY_SELECTED_BACK = new Color(
			ActivityManager.applicationContext.getResources().getColor(
					HCRUtil.getResource(HCRUtil.R_color_window_body_selected_back_color)));
	public static final Color WIN_FONT_COLOR = new Color(
			ActivityManager.applicationContext.getResources()
					.getColor(HCRUtil.getResource(HCRUtil.R_color_window_default_font_color)));
	public static final Color WINDOW_TIP_FONT_COLOR = new Color(ActivityManager.applicationContext
			.getResources().getColor(HCRUtil.getResource(HCRUtil.R_color_window_tip_font_color)));
	public static final Color WIN_UNSELECTED_FONT_COLOR = new Color(
			ActivityManager.applicationContext.getResources().getColor(
					HCRUtil.getResource(HCRUtil.R_color_window_default_unselected_font_color)));
	public static final Color WINDOW_SELECTED_BORDER_COLOR = new Color(
			ActivityManager.applicationContext.getResources()
					.getColor(HCRUtil.getResource(HCRUtil.R_color_window_selected_border_color)));
	public static final Color WINDOW_BORDER_COLOR = new Color(ActivityManager.applicationContext
			.getResources().getColor(HCRUtil.getResource(HCRUtil.R_color_window_border_color)));
	public static final Color WINDOW_ICON_COLOR = new Color(ActivityManager.applicationContext
			.getResources().getColor(HCRUtil.getResource(HCRUtil.R_color_window_icon_color)));

	public static final Color WINDOW_TITLE_FONT_COLOR = new Color(ActivityManager.applicationContext
			.getResources().getColor(HCRUtil.getResource(HCRUtil.R_color_window_title_font_color)));

	public static final Color WINDOW_DESKTOP_UP_DOWN_COLOR = new Color(
			ActivityManager.applicationContext.getResources()
					.getColor(HCRUtil.getResource(HCRUtil.R_color_window_desktop_up_down_color)));

	public static final Color WINDOW_TABLE_BODY_CELL_BORDER_COLOR = new Color(
			ActivityManager.applicationContext.getResources().getColor(
					HCRUtil.getResource(HCRUtil.R_color_window_table_body_cell_border_color)));
	public static final Color WINDOW_TABLE_HEADER_BORDER_COLOR = new Color(
			ActivityManager.applicationContext.getResources().getColor(
					HCRUtil.getResource(HCRUtil.R_color_window_table_header_border_color)));

	public static final int BUTTON_GAP_PX = ActivityManager.applicationContext.getResources()
			.getDimensionPixelSize(HCRUtil.getResource(HCRUtil.R_dimen_button_gap_width));

	// public static final Font FONT_DEFAULT = new Font(Font.DIALOG, Font.PLAIN,
	// StringUtil.adjustFontSize(screenWidth, screenHeight));

	public static final int BORDER_STROKE_WIDTH = (int) Math.ceil(ActivityManager.applicationContext
			.getResources().getDimension(HCRUtil.getResource(HCRUtil.R_dimen_border_stroke_width)));
	public static final int BORDER_RADIUS = (int) ActivityManager.applicationContext.getResources()
			.getDimension(HCRUtil.getResource(HCRUtil.R_dimen_border_radius));

	public static final int getBorderStrokeWidthInPixel() {
		return dpToPx(BORDER_STROKE_WIDTH);
	}

	public static final void showCenterToast(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(ActivityManager.applicationContext, msg, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});
	}

	public static void addView(ViewGroup parent, View view, ViewRelation viewRelation) {
		viewRelation.registerViewRelation(parent, view);
		parent.addView(removeFromParent(view));
	}

	public static void addView(ViewGroup parent, View view, int idx, LayoutParams lp,
			ViewRelation viewRelation) {
		viewRelation.registerViewRelation(parent, view);
		parent.addView(removeFromParent(view), idx, lp);
	}

	public static Object runAndWaitNotInUIThread(ReturnableRunnable run) {
		if (threadPoolToken == null) {
			threadPoolToken = App.getThreadPoolToken();
		}
		return ContextManager.getThreadPool().runAndWait(run, threadPoolToken);
	}

	public static void runDelayNotInUIThread(Runnable run) {
		if (threadPoolToken == null) {
			threadPoolToken = App.getThreadPoolToken();
		}
		ContextManager.getThreadPool().run(run, threadPoolToken);
	}

	public static ImageView getSystemErrorView() {
		ImageView iconView = new ImageView(ActivityManager.applicationContext);
		iconView.setImageDrawable(getSystemErrorDrawable());
		return iconView;
	}

	public static Bitmap autoZoomSysIconToBitmap(final int icon_id, final float zoom) {
		Bitmap bitmap = AndroidUIUtil.drawableToBitmap(
				ActivityManager.applicationContext.getResources().getDrawable(icon_id));
		int scaleSize = J2SEInitor.getAndroidServerScreenAdapter().imageSizeToScreenFloat(48, zoom);
		return ImageUtil.zoomBitmap(bitmap, scaleSize, scaleSize);
	}

	public static final int J2SE_FONT_DEFAULT_SIZE = 14;

	public static final int convertHtmlBodyWidth(int j2seBodyWidth) {
		return j2seBodyWidth * J2SEInitor.screenWidth / 1024;
	}

	public static Drawable getSystemErrorDrawable() {
		return getSystemDefaultDrawable(DRAW_ERROR_ID);
	}

	public static ImageView getSystemWarningView() {
		ImageView iconView = new ImageView(ActivityManager.applicationContext);
		iconView.setImageDrawable(getSystemWarningDrawable());
		return iconView;
	}

	public static int dpToPx(int dp) {
		float density = UICore.getDeviceDensity();
		// DisplayMetrics displayMetrics =
		// ActivityManager.applicationContext.getResources().getDisplayMetrics();
		// int px = Math.round(dp * (displayMetrics.xdpi /
		// DisplayMetrics.DENSITY_DEFAULT));
		// return px;
		return (int) (dp * density + 0.5f);
	}

	public static void printViewStructure(View view, int level) {
		if (view == null) {
			LogManager.log("null view, skip print view structure.");
		}
		printViewInfo(view, level++);
		if (view instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) view;
			int size = vg.getChildCount();
			for (int i = 0; i < size; i++) {
				printViewStructure(vg.getChildAt(i), level);
			}
		}
	}

	private static void printViewInfo(View view, int level) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++) {
			sb.append(" ");
		}
		sb.append("[" + level + "]");
		boolean isTextView = (view instanceof TextView);
		int visib = view.getVisibility();
		sb.append(view.getClass().getName());
		sb.append('@');
		sb.append(Integer.toHexString(System.identityHashCode(view)));
		if (visib == View.INVISIBLE || visib == View.GONE) {
			sb.append("[INVISIBLE|GONE]");
		}
		sb.append(":");
		if (isTextView) {
			final TextView textView = (TextView) view;
			sb.append(textView.getText());
			sb.append(" (gravity : ");
			sb.append(textView.getGravity());
			sb.append("), ");
		}
		sb.append("[w:");
		sb.append(view.getWidth());
		sb.append(",h:");
		sb.append(view.getHeight());
		sb.append("]");

		sb.append(", [mw:");
		sb.append(view.getMeasuredWidth());
		sb.append(",mh:");
		sb.append(view.getMeasuredHeight());
		sb.append("]");

		LayoutParams lp = view.getLayoutParams();
		if (lp != null) {
			sb.append(", [lpw:");
			if (lp.width == -1) {
				sb.append("MATCH");
			} else if (lp.width == -2) {
				sb.append("WRAP");
			} else {
				sb.append(lp.width);
			}
			sb.append(",lph:");
			if (lp.height == -1) {
				sb.append("MATCH");
			} else if (lp.height == -2) {
				sb.append("WRAP");
			} else {
				sb.append(lp.height);
			}
			sb.append("]");
		}

		LogManager.log(sb.toString());
	}

	public static float isAutoConvertDIPImage() {
		HardwareConfig config = HardwareConfigManager.getConfig();
		final float scale;
		if (config == null || config.getDensity() == 0) {
			scale = ActivityManager.applicationContext.getResources().getDisplayMetrics().density;
		} else {
			scale = config.getDensity();
		}
		return scale;
	}

	public static int pxToDp(int px, float density) {
		return (int) (px / density + 0.5f);
		// DisplayMetrics displayMetrics =
		// ActivityManager.applicationContext.getResources().getDisplayMetrics();
		// int dp = Math.round(px / (displayMetrics.xdpi /
		// DisplayMetrics.DENSITY_DEFAULT));
		// return dp;
	}

	public static Drawable getSystemWarningDrawable() {
		return getSystemDefaultDrawable(DRAW_WARNING_ID);
	}

	public static ImageView getSystemInformationView() {
		ImageView iconView = new ImageView(ActivityManager.applicationContext);
		iconView.setImageDrawable(getSystemInformationDrawable());
		return iconView;
	}

	public static Drawable getSystemInformationDrawable() {
		return getSystemDefaultDrawable(DRAW_INFO_ID);
	}

	public static Drawable getSystemDefaultDrawable(final int res_id) {
		return ActivityManager.applicationContext.getResources().getDrawable(res_id);
	}

	public static ImageView getSystemQuestionView() {
		ImageView iconView = new ImageView(ActivityManager.applicationContext);
		iconView.setImageDrawable(getSystemQuestionDrawable());
		return iconView;
	}

	public static Drawable getSystemQuestionDrawable() {
		return getSystemDefaultDrawable(DRAW_HELP_ID);
	}

	static ThreadGroup threadPoolToken;

	private final static ConcurrentMap<Long, WaitObject> waitMap = new ConcurrentHashMap<Long, WaitObject>();

	public static void runOnUiThread(final Runnable run) {
		if(ActivityManager.mainThread == Thread.currentThread()) {
			run.run();
		}else {
			ActivityManager.mainHandler.post(run);
		}
	}

	public static void runOnUiThreadAndWait(Runnable run) {
		if (run == null) {
			return;
		}
		
		if(ActivityManager.mainThread == Thread.currentThread()) {
			run.run();
			return;
		}
		
		Long threadID = Thread.currentThread().getId();
		WaitObject wObj = waitMap.get(threadID);
		if (wObj == null) {
			wObj = new WaitObject();
			waitMap.put(threadID, wObj);
		}

		synchronized (wObj) {
			wObj.doneDeep = false;
			wObj.isThrowException = false;
			wObj.deepRun = run;
			runOnUiThread(wObj.synchRunnable);
			if (wObj.doneDeep == false) {
				try {
					wObj.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		if (wObj.isThrowException) {
			ClassUtil.printCurrentThreadStack(
					"-----------Exception : runOnUiThreadAndWait------------", true);
		}
	}

	public static void addView(ViewGroup parent, View view, LayoutParams lp,
			ViewRelation viewRelation) {
		if (view == null) {
			return;
		}

		viewRelation.registerViewRelation(parent, view);
		parent.addView(removeFromParent(view), lp);
	}

	public static int getCheckBoxWidth() {
		if (checkBoxSample == null) {
			checkBoxSample = new CheckBox(ActivityManager.applicationContext);
			AndroidUIUtil.getViewWidthAndHeight(checkBoxSample, new Dimension());
		}

		return checkBoxSample.getMeasuredWidth();
	}

	static CheckBox checkBoxSample;
	static final int lineHeight = 4;
	static final int padWidth = 2;

	public static ImageView getExpandDrawable() {
		return new ImageView(ActivityManager.applicationContext) {
			private final Paint paint = new Paint();

			protected void onDraw(Canvas canvas) {
				super.onDraw(canvas);

				int leftTopY = (getHeight() - lineHeight) / 2;
				int leftTopX = padWidth;
				int rightBottomY = (leftTopY + lineHeight);
				int rightBottomX = (getWidth() - padWidth);

				paint.setAntiAlias(true);
				paint.setColor(WINDOW_ICON_COLOR.toAndroid());
				paint.setStyle(Paint.Style.FILL);
				canvas.drawRect(new Rect(leftTopX, leftTopY, rightBottomX, rightBottomY), paint);

				leftTopX = (getWidth() - lineHeight) / 2;
				leftTopY = padWidth;
				rightBottomX = (getWidth() + lineHeight) / 2;
				rightBottomY = (getHeight() - padWidth);

				canvas.drawRect(new Rect(leftTopX, leftTopY, rightBottomX, rightBottomY), paint);
			}
		};
	}

	public static ImageView getCollapseDrawable() {
		return new ImageView(ActivityManager.applicationContext) {
			private final Paint paint = new Paint();

			protected void onDraw(Canvas canvas) {
				super.onDraw(canvas);

				int leftTopY = (getHeight() - lineHeight) / 2;
				int leftTopX = padWidth;
				int rightBottomY = (leftTopY + lineHeight);
				int rightBottomX = (getWidth() - padWidth);

				paint.setAntiAlias(true);
				paint.setColor(WINDOW_ICON_COLOR.toAndroid());
				paint.setStyle(Paint.Style.FILL);
				canvas.drawRect(new Rect(leftTopX, leftTopY, rightBottomX, rightBottomY), paint);
			}
		};
	}

	public static void buildListenersForComponent(View view, final Component component) {
		// if(view instanceof Button || view instanceof TextView || view
		// instanceof EditText){
		view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (component instanceof JComponent) {
					if (hasFocus) {
						FocusManager.setFocusOwner(component);
					} else {
						FocusManager.setFocusOwner(null);
					}
					String tip = ((JComponent) component).getToolTipText();
					if (tip != null && tip.length() > 0) {
						ToolTipManager sharedInstance = ToolTipManager.sharedInstance();
						if (hasFocus) {
							sharedInstance.registerComponent((JComponent) component);
						} else {
							sharedInstance.unregisterComponent((JComponent) component);
						}
					}
				}

				FocusListener[] fl = component.getFocusListeners();
				if (fl != null) {
					FocusEvent event = new FocusEvent(component,
							EventIDManager.getEventID(FocusEvent.class));
					for (int i = 0; i < fl.length; i++) {
						if (hasFocus) {
							fl[i].focusGained(event);
						} else {
							fl[i].focusLost(event);
						}
					}
				}
			}
		});

		view.setOnDragListener(new View.OnDragListener() {
			@Override
			public boolean onDrag(View v, DragEvent event) {
				if (component instanceof JComponent) {
					JComponent jComponent = (JComponent) component;
					//
					MouseEvent swingMouseEvent = new MouseEvent(component, 0,
							System.currentTimeMillis(), 0, (int) event.getX(), (int) event.getY(),
							0, false, MouseEvent.BUTTON1);
					jComponent.processMouseMotionEventAdAPI(swingMouseEvent);
					return true;
				}
				return false;
			}
		});

		if ((view instanceof Spinner) == false && // Spinner专用setOnItemSelectedListener
				(view instanceof ListView) == false) {// 同上
			final View.OnClickListener clickOnView = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (component instanceof JComponent) {

						// mousePressed
						JComponent jComponent = (JComponent) component;
						{
							MouseEvent swingMouseEvent = new MouseEvent(component,
									MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 0, 0,
									0, false, MouseEvent.BUTTON1);
							jComponent.processMouseEventAdAPI(swingMouseEvent);
						}

						// actionPerformed
						if (jComponent instanceof AbstractButton) {
							ActionEvent actionEvent = new ActionEvent(jComponent,
									ActionEvent.ACTION_PERFORMED, "", System.currentTimeMillis(),
									0);
							((AbstractButton) jComponent).processActionListenerAdAPI(actionEvent);
						}

						// mouseReleased
						{
							MouseEvent swingMouseEvent = new MouseEvent(component,
									MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, 0, 0,
									0, false, MouseEvent.BUTTON1);
							jComponent.processMouseEventAdAPI(swingMouseEvent);
						}

						// mouseClicked
						{
							MouseEvent swingMouseEvent = new MouseEvent(component,
									MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0,
									0, false, MouseEvent.BUTTON1);
							jComponent.processMouseEventAdAPI(swingMouseEvent);
						}
					}
				}
			};
			// view.setOnTouchListener(new View.OnTouchListener(){
			// @Override
			// public boolean onTouch(View arg0, MotionEvent arg1) {
			// if(arg1.getAction() == KeyEvent.ACTION_UP){
			// clickOnView.onClick(arg0);
			// return true;
			// }
			// return false;
			// }
			// });
			view.setOnClickListener(clickOnView);
		}

		view.setOnHoverListener(new View.OnHoverListener() {
			@Override
			public boolean onHover(View v, MotionEvent event) {
				if (component instanceof JComponent) {
					JComponent jComponent = (JComponent) component;
					String tip = jComponent.getToolTipText();
					if (tip != null && tip.length() > 0) {
						ToolTipManager sharedInstance = ToolTipManager.sharedInstance();
						if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
							// sharedInstance.setEnabled(true);
							sharedInstance.registerComponent((JComponent) component);
						}
						if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
							sharedInstance.unregisterComponent((JComponent) component);
							// sharedInstance.setEnabled(false);
						}
					}

					if (event.getAction() == MotionEvent.ACTION_HOVER_MOVE) {
						MouseEvent swingMouseEvent = new MouseEvent(component, 0,
								System.currentTimeMillis(), 0, (int) event.getX(),
								(int) event.getY(), 0, false, MouseEvent.NOBUTTON);
						jComponent.processMouseMotionEventAdAPI(swingMouseEvent);
					} else if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
						MouseEvent swingMouseEvent = new MouseEvent(component,
								MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0,
								(int) event.getX(), (int) event.getY(), 0, false,
								MouseEvent.NOBUTTON);
						jComponent.processMouseEventAdAPI(swingMouseEvent);
					} else if (event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
						MouseEvent swingMouseEvent = new MouseEvent(component,
								MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), 0,
								(int) event.getX(), (int) event.getY(), 0, false,
								MouseEvent.NOBUTTON);
						jComponent.processMouseEventAdAPI(swingMouseEvent);
					}

					return true;
				}
				return false;
			}
		});
		// }//end if
	}

	/**
	 * 
	 * @param view
	 * @param rect
	 *            the width and height of view will be stored in rect.width,
	 *            rect.height
	 */
	public static void getViewWidthAndHeight(final View view, final Dimension dimension) {
		getViewWidthAndHeight(view, dimension, false);
	}

	public static void getViewWidthAndHeight(final View view, final Dimension dimension,
			boolean withState) {
		int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(width, height);
		if (withState) {
			dimension.width = view.getMeasuredWidthAndState();
			dimension.height = view.getMeasuredHeightAndState();
		} else {
			dimension.width = view.getMeasuredWidth();
			dimension.height = view.getMeasuredHeight();
		}
	}

	public static boolean containsHTML(String text) {
		return (text.indexOf("<html>") >= 0 || text.indexOf("<HTML>") >= 0);
	}

	public static Color getDarkGrayColor() {
		return DARK_GRAY_COLOR;
	}

	private static final Vector<FontScreenKey> tvFontScreenkeys = new Vector<FontScreenKey>(8);
	private static final Vector<Integer> tvHeight = new Vector<Integer>(8);

	public static final synchronized int getDefaultTextViewHeight(final Font font,
			final ScreenAdapter sa) {
		final int size = tvFontScreenkeys.size();
		for (int i = 0; i < size; i++) {
			FontScreenKey fsk = tvFontScreenkeys.get(i);
			if (fsk.adapter == sa) {
				Font fontsk = fsk.font;
				if (fontsk.getSize() == font.getSize() && fontsk.getStyle() == font.getStyle()
						&& fontsk.getName().equals(font.getName())) {
					return tvHeight.get(i);
				}
			}
		}

		final TextView para = new TextView(ActivityManager.applicationContext);
		runOnUiThreadAndWait(new Runnable() {
			@Override
			public void run() {
				para.setText("jQH");
				para.setTypeface(font.typeface);
				UICore.setTextSize(para, font, sa);
			}
		});

		Dimension dimension = new Dimension();
		AndroidUIUtil.getViewWidthAndHeight(para, dimension);

		final int out = dimension.height;
		tvFontScreenkeys.add(new FontScreenKey(sa, font));
		tvHeight.add(out);

		return out;
	}

	/**
	 * BitmapDrawable, ClipDrawable, ColorDrawable, DrawableContainer,
	 * GradientDrawable, InsetDrawable, LayerDrawable, NinePatchDrawable,
	 * PictureDrawable, RotateDrawable, ScaleDrawable, ShapeDrawable
	 * 
	 * @param xml
	 * @return
	 */
	public static Drawable buildDrawbleFromString(String xml) {
		// gradient -- 对应颜色渐变。 startcolor、endcolor就不多说了。 android:angle
		// 是指从哪个角度开始变。
		// solid -- 填充。
		// stroke -- 描边。
		// corners -- 圆角。
		// padding -- 定义内容离边界的距离。
		// 与android:padding_left、android:padding_right这些是一个道理。
		// +"<corners android:radius=\"1dp\" android:bottomRightRadius=\"12dp\"
		// android:bottomLeftRadius=\"12dp\" "
		// +"android:topLeftRadius=\"12dp\" android:topRightRadius=\"12dp\"/>"
		byte[] bytes;
		try {
			bytes = xml.getBytes("UTF-8");
		} catch (Exception e) {
			bytes = xml.getBytes();
		}
		InputStream is = new ByteArrayInputStream(bytes);
		try {
			return Drawable.createFromStream(is, "");
		} finally {
			try {
				is.close();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	public static Window getContainingWindow(Component c) {
		return null;
	}

	public static Bitmap getBitmap(String fileName) {
		return BitmapFactory
				.decodeStream(J2SEInitor.class.getResourceAsStream("/hc/android/res/" + fileName));
	}

	public static Color toJ2SEColor(int color) {
		return new Color(color);
	}

	public static final int vgap = 5;
	public static final int hgap = 5;

	private static final String STYLE_WIDTH = "\\s+style\\s?=\\s?['|\"]\\s?width\\s?:\\s?(\\d+)\\s?['|\"]";
	private static final Pattern pattern = Pattern.compile(STYLE_WIDTH, Pattern.CASE_INSENSITIVE);

	/**
	 * <body style='width:450' align='left'>
	 * 
	 * @param html
	 * @return 0: not found
	 */
	private static final int getBodyWidth(String html) {
		Matcher m = pattern.matcher(html);
		if (m.find()) {
			return Integer.parseInt(m.group(1));
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @param txt
	 * @param tv
	 * @return true:内含html; false:不含html
	 */
	public static boolean setTextForTextViewAdAPI(String txt, TextView tv) {
		if (AndroidUIUtil.containsHTML(txt)) {
			int htmlBodyWidth = getBodyWidth(txt);

			if (htmlBodyWidth > 0) {
				tv.setMaxWidth(AndroidUIUtil.convertHtmlBodyWidth(htmlBodyWidth));
			}
			tv.setText(Html.fromHtml(txt));
			return true;
		} else {
			tv.setText(txt);// JLabel根据返回，设置单行
			return false;
		}
	}

	public static Rect getTextWidth(String text, Paint paint) {
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, 1, bounds);
		return bounds;
	}

	public static View removeFromParent(final View p_view) {
		final ViewParent vp = p_view.getParent();
		if (vp != null) {
			if (vp instanceof ViewGroup) {
				AndroidUIUtil.runOnUiThreadAndWait(new Runnable() {
					@Override
					public void run() {
						((ViewGroup) vp).removeView(p_view);
					}
				});
			}
		}
		return p_view;
	}

	// TODO
	public static ViewGroup replaceChildOrBorderView(Container container, View oldView,
			View newView) {
		if (container == null) {
			return null;
		}

		ViewGroup parent = (ViewGroup) container.getContainerViewAdAPI();

		return substituteView(parent, oldView, newView);
	}

	public static ViewGroup substituteView(ViewGroup parent, View oldView, View newView) {
		LayoutParams lp = oldView.getLayoutParams();
		final int size = parent.getChildCount();
		int idx = -1;
		for (int i = 0; i < size; i++) {
			if (parent.getChildAt(i) == oldView) {
				idx = i;
				break;
			}
		}
		if (idx == -1) {
			return null;
		} else {
			parent.removeViewAt(idx);
			parent.addView(newView, idx, lp);
			return parent;
		}
	}

	public static FontMetrics getFontMetrics(JComponent component, Font font) {
		View view = component.getPeerAdAPI();
		if (view instanceof TextView) {
			android.graphics.Paint.FontMetrics afm = ((TextView) view).getPaint().getFontMetrics();
			return new FontMetrics(font) {
			};
		} else {
			return new FontMetrics(font) {
			};
		}
	}

	public static String ColorToString(Color color) {
		String A = Integer.toHexString(color.getAlpha());
		A = A.length() < 2 ? ('0' + A) : A;
		String R = Integer.toHexString(color.getRed());
		R = R.length() < 2 ? ('0' + R) : R;
		String B = Integer.toHexString(color.getBlue());
		B = B.length() < 2 ? ('0' + B) : B;
		String G = Integer.toHexString(color.getGreen());
		G = G.length() < 2 ? ('0' + G) : G;
		return '#' + A + R + B + G;
	}

	private static final Bitmap emptyBitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888);

	public static Bitmap getViewBitmap(View v) {
		v.invalidate();
		getViewWidthAndHeight(v, new Dimension(), false);
		int measuredWidth = v.getMeasuredWidth();
		int measuredHeight = v.getMeasuredHeight();
		if (measuredHeight <= 0 || measuredWidth <= 0) {
			return emptyBitmap;
		}
		// if(measuredWidth < 0){
		// measuredWidth = 0;
		// }
		// if(measuredHeight < 0){
		// measuredHeight = 0;
		// }
		Bitmap b = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(0, 0, measuredWidth, measuredHeight);
		v.draw(c);
		return b;
	}

	public static final int convertToGravity(int horizontalAlign, ComponentOrientation orien) {
		if (orien == null) {
			orien = ComponentOrientation.LEFT_TO_RIGHT;
		}

		if (horizontalAlign == SwingConstants.CENTER) {
			return Gravity.CENTER | Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
		} else if (horizontalAlign == SwingConstants.LEFT
				|| (orien.isLeftToRight() && horizontalAlign == SwingConstants.LEADING)
				|| (orien.isLeftToRight() == false && horizontalAlign == SwingConstants.TRAILING)) {
			return Gravity.LEFT;
		} else if (horizontalAlign == SwingConstants.RIGHT
				|| (orien.isLeftToRight() == false && horizontalAlign == SwingConstants.LEADING)
				|| (orien.isLeftToRight() && horizontalAlign == SwingConstants.TRAILING)) {
			return Gravity.RIGHT;
		}
		return 0;
	}

	public static Color getEditBackground() {
		return WIN_BODY_BACK.brighter();
	}

	public static void printComponentsInfo(Component jcomponent, int level) {
		for (int i = 1; i < level; i++) {
			System.out.print(" ");
		}
		final boolean isContain = jcomponent instanceof Container;
		final LayoutManager lm = isContain ? ((Container) jcomponent).getLayout() : null;
		final String layDesc = isContain
				? ((lm == null) ? "null"
						: (lm.toString() + " " + ((Container) jcomponent).getPeerAdAPI()))
				: "null";// getContainerViewAdAPI
		final String labelDesc = (jcomponent instanceof JLabel)
				? ("[" + ((JLabel) jcomponent).getText() + "]")
				: "";
		final String buttonDesc = (jcomponent instanceof JButton)
				? ("[" + ((JButton) jcomponent).getText() + "]")
				: "";
		System.out.print("[" + level + "]" + jcomponent.toString() + labelDesc + buttonDesc
				+ ", x : " + jcomponent.getX() + ", y : " + jcomponent.getY() + ", w : "
				+ jcomponent.getWidth() + ", h : " + jcomponent.getHeight() + ", layout : "
				+ layDesc + "\n");

		if (jcomponent instanceof JComponent) {
			int subSize = ((JComponent) jcomponent).getComponentCount();
			for (int i = 0; i < subSize; i++) {
				Component sub = ((JComponent) jcomponent).getComponent(i);
				printComponentsInfo(sub, level + 1);
			}
		}
	}

	public static final int MS_FOCUSONTOUCH = 1000;

	public static Bitmap drawableToBitmap(Drawable drawable) {
		final int w = drawable.getIntrinsicWidth();
		final int h = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		// drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
		// : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}

	public static boolean isJViewportInstance(final Component component) {
		return component instanceof JViewport;
	}

	public static void setViewHorizontalAlignment(final JComponent comp, final TextView editText,
			final int alignment, final boolean isVerticalCenter) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final int result;
				if (alignment == SwingConstants.LEFT) {
					result = Gravity.LEFT;
				} else if (alignment == SwingConstants.CENTER) {
					result = Gravity.CENTER;
				} else if (alignment == SwingConstants.RIGHT) {
					result = Gravity.RIGHT;
				} else if (alignment == SwingConstants.TRAILING) {
					if (comp.getComponentOrientation().isLeftToRight()) {
						result = Gravity.RIGHT;
					} else {
						result = Gravity.LEFT;
					}
				} else if (alignment == SwingConstants.LEADING) {
					if (comp.getComponentOrientation().isLeftToRight()) {
						result = Gravity.LEFT;
					} else {
						result = Gravity.RIGHT;
					}
				} else {
					result = Gravity.LEFT;
				}
				if (isVerticalCenter) {
					editText.setGravity(result | Gravity.CENTER_VERTICAL);
				} else {
					editText.setGravity(result);
				}
			}
		});
	}
}

class WaitObject {
	public Runnable deepRun;
	public boolean doneDeep;
	public boolean isThrowException;

	public Runnable synchRunnable = new Runnable() {
		@Override
		public void run() {
			synchronized (WaitObject.this) {
				try {
					deepRun.run();
				} catch (Throwable e) {
					isThrowException = true;
					e.printStackTrace();
				}
				doneDeep = true;
				WaitObject.this.notify();
			}
		}
	};
}
