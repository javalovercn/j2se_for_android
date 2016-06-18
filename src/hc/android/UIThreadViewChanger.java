package hc.android;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

public class UIThreadViewChanger {
	private static final Activity ACTIVITY = ActivityManager.getActivity();
	private static final Animation ENTER_ANIM = AnimationUtils.loadAnimation(ACTIVITY, HCRUtil.getResource(HCRUtil.R_anim_window_enter));
	private static final Animation EXIT_ANIM = AnimationUtils.loadAnimation(ACTIVITY, HCRUtil.getResource(HCRUtil.R_anim_window_exit));
	private static FrameLayout viewFlipper;
	
	public static View getContentView(){
		return viewFlipper;
	}
	
	public static void callInitEmpty(){
	}

	public static void removeView(final View p_view) {
		if(p_view == null || viewFlipper == null){
			return;
		}
		
		ACTIVITY.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				EXIT_ANIM.reset();
				p_view.startAnimation(EXIT_ANIM);
		   		viewFlipper.removeView(p_view);
		   		int childCount = viewFlipper.getChildCount();
		   		if(childCount > 0){
		   			final ViewGroup nextView = (ViewGroup)viewFlipper.getChildAt(childCount - 1);
					nextView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
					if(childCount == 1){
						nextView.requestFocus();//HCDesktop主按钮获得焦点
					}
		   		}
		   		return;
			}
		});
	}
	
	public static void setCurr(final View p_view) {
		ACTIVITY.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(viewFlipper == null){
					viewFlipper = new FrameLayout(ACTIVITY);
					ACTIVITY.setContentView(viewFlipper);
				}
		   		viewFlipper.removeView(p_view);
		   		ENTER_ANIM.reset();
		   		p_view.startAnimation(ENTER_ANIM);
		   		int childCount = viewFlipper.getChildCount();
		   		if(childCount > 0){
		   			((ViewGroup)viewFlipper.getChildAt(childCount - 1)).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		   		}
		   		viewFlipper.addView(p_view);
	   			((ViewGroup)p_view).setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		   	}
		});
	}

}
