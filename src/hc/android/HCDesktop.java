package hc.android;

import hc.PlatformTrayIcon;
import java.awt.Component;
import java.awt.Image;
import java.awt.TrayIcon.MessageType;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import hc.android.HCRUtil;
import hc.core.util.LogManager;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HCDesktop implements PlatformTrayIcon {
	private static final int SPLIT_LINE_HEIGHT = 1;
	JPopupMenu popupMenu;
	Image iconImage;
	String toolTip;
	ImageView statusIconView;
	TextView messageView;

	final int iconBaseHeight = 30;
	// final int titleBaseHeight = 14;
	final int gap = 3;

	public HCDesktop(final Image image, String toolTip, final JPopupMenu menu) {
		this.toolTip = toolTip;
		this.popupMenu = menu;

		statusIconView = new ImageView(ActivityManager.applicationContext);
		messageView = new TextView(ActivityManager.applicationContext);
		messageView.setLines(1);
		messageView.setTextColor(AndroidUIUtil.WINDOW_TITLE_FONT_COLOR.toAndroid());
		messageView.setEllipsize(TruncateAt.MARQUEE);// 以跑马灯的方式显示

		setImage(image);
	}

	LinearLayout messageLinear;

	public void showDesktop() {
		LinearLayout desktopLinear = new LinearLayout(ActivityManager.applicationContext);
		desktopLinear.setOrientation(LinearLayout.VERTICAL);
		{
			messageLinear = new LinearLayout(ActivityManager.applicationContext);
			messageLinear
					.setBackgroundColor(AndroidUIUtil.WINDOW_DESKTOP_UP_DOWN_COLOR.toAndroid());

			// 消息条
			{
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT, 1.0F);
				lp.gravity = (Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
				lp.leftMargin = gap;
				lp.topMargin = gap;
				lp.bottomMargin = gap;
				lp.rightMargin = gap;
				messageLinear.addView(messageView, lp);
			}

			// 状态图标
			{
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.gravity = (Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
				lp.topMargin = gap;
				lp.rightMargin = gap;
				lp.bottomMargin = gap;
				messageLinear.addView(statusIconView, lp);
			}

			// 加入主面板
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			desktopLinear.addView(messageLinear, lp);
		}

		// 加入分割线
		{
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, SPLIT_LINE_HEIGHT);
			View view = new View(ActivityManager.applicationContext);
			view.setBackgroundResource(HCRUtil.getResource(HCRUtil.R_drawable_divider_line));
			desktopLinear.addView(view, lp);
		}

		final ImageView mainIconView;
		// 添加主按钮
		{
			mainIconView = new ImageView(ActivityManager.applicationContext);
			final ImageIcon imageIcon = new ImageIcon("/hc/android/res/hc_128.png");
			mainIconView.setImageBitmap(ImageIcon.getBitmapAdAPI(imageIcon));

			mainIconView.setFocusable(true);
			mainIconView.setFocusableInTouchMode(false);
			mainIconView.setClickable(true);

			final HCHomeIcon viewComponent = new HCHomeIcon();

			viewComponent.setPeerAdAPI(mainIconView);

			final View.OnClickListener clickOrTouchOnIcon = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					AndroidUIUtil.runDelayNotInUIThread(new Runnable() {
						@Override
						public void run() {
							AndroidUIUtil.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									mainIconView.setClickable(false);
									popupMenu.show(viewComponent, 0, 0);
									mainIconView.setClickable(true);
								}
							});
						}
					});
				}
			};
			// mainIconView.setOnTouchListener(new View.OnTouchListener() {
			// @Override
			// public boolean onTouch(View arg0, MotionEvent arg1) {
			// if(arg1.getAction() == KeyEvent.ACTION_UP){
			// clickOrTouchOnIcon.onClick(arg0);
			// return true;
			// }
			// return false;
			// }
			// });
			mainIconView.setOnClickListener(clickOrTouchOnIcon);
			mainIconView.setOnKeyListener(new View.OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
						if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER) {
							popupMenu.show(viewComponent, 0, 0);
							return true;
						}
					}
					return false;
				}
			});
			mainIconView.setBackgroundResource(HCRUtil.getResource(HCRUtil.R_drawable_tree_node));

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.gravity = (Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			desktopLinear.addView(mainIconView, lp);
		}

		// 显示主面板
		desktopLinear.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		desktopLinear.setBackgroundResource(HCRUtil.getResource(HCRUtil.R_drawable_desktop_back));
		WindowManager.show(desktopLinear, mainIconView);
		AndroidUIUtil.runDelayNotInUIThread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);// 5000改为1000
				} catch (Exception e) {
				}
				AndroidUIUtil.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mainIconView.requestFocus();
					}
				});
			}
		});
	}

	@Override
	public void remove() {
	}

	@Override
	public void exit() {
	}

	@Override
	public void displayMessage(final String caption, final String message,
			final MessageType messageType) {
		AndroidUIUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				messageView.setText(message);

				LinearLayout layout = new LinearLayout(ActivityManager.applicationContext);
				layout.setOrientation(LinearLayout.VERTICAL);

				LinearLayout titleBar = new LinearLayout(ActivityManager.applicationContext);
				{
					{
						ImageView iconView = null;
						if (messageType == MessageType.ERROR) {
							iconView = AndroidUIUtil.getSystemErrorView();
						} else if (messageType == MessageType.WARNING) {
							iconView = AndroidUIUtil.getSystemWarningView();
						} else {
							iconView = AndroidUIUtil.getSystemInformationView();
						}
						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						lp.gravity = (Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
						titleBar.addView(iconView);
					}

					{
						TextView captionTV = new TextView(ActivityManager.applicationContext);
						captionTV.setText(caption);
						captionTV.getPaint().setFakeBoldText(true);
						UICore.setTextSize(captionTV, UICore.buildDefaultDialogFont(),
								J2SEInitor.getAndroidServerScreenAdapter());
						captionTV.setTextColor(Color.BLACK);
						captionTV.setBackgroundColor(0);

						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						lp.gravity = (Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
						titleBar.addView(captionTV);
					}
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					lp.gravity = (Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
					layout.addView(titleBar);
				}
				// titleBar.setBackgroundColor(UIUtil.WIN_BODY_BACK.toAndroid());

				{
					TextView messageTV = new TextView(ActivityManager.applicationContext);
					// messageTV.setBackgroundResource(R.drawable.tip);
					messageTV.setTextColor(AndroidUIUtil.WINDOW_TIP_FONT_COLOR.toAndroid());
					messageTV.setText(message);
					UICore.setTextSize(messageTV, J2SEInitor.getAndroidServerScreenAdapter());
					// messageTV.setBackgroundColor(UIUtil.WIN_BODY_BACK.toAndroid());

					layout.addView(messageTV);
				}

				Toast toast = new Toast(ActivityManager.applicationContext);
				toast.setDuration(Toast.LENGTH_LONG);

				layout.setBackgroundResource(HCRUtil.getResource(HCRUtil.R_drawable_tip));
				toast.setView(layout);
				toast.setGravity(Gravity.END | Gravity.TOP, 0, 0);
				toast.show();
			}
		});
	}

	@Override
	public void setIconToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	@Override
	public Image getImage() {
		return iconImage;
	}

	@Override
	public String getIconToolTip() {
		return toolTip;
	}

	@Override
	public void setImage(final Image image) {
		this.iconImage = image;
		AndroidUIUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// limit titleBaseHeight
				int scaleSize = J2SEInitor.getAndroidServerScreenAdapter()
						.imageSizeToScreenFloat(iconBaseHeight, 1.0F);
				statusIconView.setImageBitmap(
						ImageUtil.zoomBitmap(image.getBitmapAdAPI(), scaleSize, scaleSize));
				messageView.setTextSize(TypedValue.COMPLEX_UNIT_PX, scaleSize - 6);// 相对于状态Icon
			}
		});
	}

}
