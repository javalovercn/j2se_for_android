package hc.android;

import java.util.Map;

public class HCRUtil {
	public static String R_drawable_divider_line = "R.drawable.divider_line";
	public static String R_drawable_tree_node = "R.drawable.tree_node";
	public static String R_drawable_desktop_back = "R.drawable.desktop_back";
	public static String R_drawable_tip = "R.drawable.tip";
	public static String R_drawable_question_48 = "R.drawable.question_48";
	public static String R_drawable_info_48 = "R.drawable.info_48";
	public static String R_drawable_warning_48 = "R.drawable.warning_48";
	public static String R_drawable_error_48 = "R.drawable.error_48";
	public static String R_color_window_trans_layer_color = "R.color.window_trans_layer_color";
	public static String R_color_window_btn_stroke_color = "R.color.window_btn_stroke_color";
	public static String R_color_window_font_color_in_field = "R.color.window_font_color_in_field";
	public static String R_drawable_tab_item = "R.drawable.tab_item";
	public static String R_drawable_button = "R.drawable.button";
	public static String R_drawable_button_title = "R.drawable.button_title";
	public static String R_layout_simple_spinner_image_item = "R.layout.simple_spinner_image_item";
	public static String R_layout_simple_spinner_item = "R.layout.simple_spinner_item";
	public static String R_layout_simple_spinner_dropdown_image_item = "R.layout.simple_spinner_dropdown_image_item";
	public static String R_layout_simple_spinner_dropdown_item = "R.layout.simple_spinner_dropdown_item";
	public static String R_layout_simple_list_item_single_choice = "R.layout.simple_list_item_single_choice";
	public static String R_layout_simple_list_item_multiple_choice = "R.layout.simple_list_item_multiple_choice";
	public static String R_drawable_popup_window = "R.drawable.popup_window";
	public static String R_style_PopupMenuAnimation = "R.style.PopupMenuAnimation";
	public static String R_drawable_progress_bar = "R.drawable.progress_bar";
	public static String R_drawable_window_title = "R.drawable.window_title";
	public static String R_drawable_window_menu_bar = "R.drawable.window_menu_bar";
	public static String R_drawable_window_body = "R.drawable.window_body";
	public static String R_drawable_table_header = "R.drawable.table_header";
	public static String R_drawable_textfield = "R.drawable.textfield";
	public static String R_drawable_border_titled = "R.drawable.border_titled";
	public static String R_drawable_checkbox_table = "R.drawable.checkbox_table";
	public static String R_color_window_body_back_color = "R.color.window_body_back_color";
	public static String R_color_window_body_selected_back_color = "R.color.window_body_selected_back_color";
	public static String R_color_window_default_font_color = "R.color.window_default_font_color";
	public static String R_color_window_btn_text_color = "R.color.window_btn_text_color";
	public static String R_color_window_default_unselected_font_color = "R.color.window_default_unselected_font_color";
	public static String R_color_window_selected_border_color = "R.color.window_selected_border_color";
	public static String R_color_window_border_color = "R.color.window_border_color";
	public static String R_color_window_icon_color = "R.color.window_icon_color";
	public static String R_color_window_tip_font_color = "R.color.window_tip_font_color";
	public static String R_color_window_title_font_color = "R.color.window_title_font_color";
	public static String R_color_window_desktop_up_down_color = "R.color.window_desktop_up_down_color";
	public static String R_color_window_table_body_cell_border_color = "R.color.window_table_body_cell_border_color";
	public static String R_color_window_table_header_border_color = "R.color.window_table_header_border_color";
	public static String R_dimen_button_gap_width = "R.dimen.button_gap_width";
	public static String R_dimen_border_stroke_width = "R.dimen.border_stroke_width";
	public static String R_dimen_border_radius = "R.dimen.border_radius";
	public static String R_anim_window_enter = "R.anim.window_enter";
	public static String R_anim_window_exit = "R.anim.window_exit";

	public static String R_drawable_hc_48 = "R.drawable.hc_48";

	/**
	 * convert J2SE res to Android res.
	 * 
	 * @param resID
	 * @return
	 */
	public static final int getResource(final String resID) {
		return resMap.get(resID);
	}

	public static Map<String, Integer> resMap;
}
