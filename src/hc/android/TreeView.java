package hc.android;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TreeView extends ListActivity {
	private ArrayList<TreeElement> mPdfOutlinesCount = new ArrayList<TreeElement>();
	private TreeViewAdapter treeViewAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initialData();
		treeViewAdapter = new TreeViewAdapter(this, 0, mPdfOutlinesCount);
		setListAdapter(treeViewAdapter);
		registerForContextMenu(getListView());
	}

	private void initialData() {
		TreeElement element1 = new TreeElement("01", "关键类");
		TreeElement element2 = new TreeElement("02", "应用程序组件");
		TreeElement element3 = new TreeElement("03", "Activity和任务");
		TreeElement element4 = new TreeElement("04", "激活组件：intent");
		TreeElement element5 = new TreeElement("05", "关闭组件");
		TreeElement element6 = new TreeElement("06", "manifest文件");
		TreeElement element7 = new TreeElement("07", "Intent过滤器");
		TreeElement element8 = new TreeElement("08", "Affinity（吸引力）和新任务");
		TreeElement element9 = new TreeElement("09", "加载模式");
		TreeElement element10 = new TreeElement("10", "加载模式孩子1");
		TreeElement element11 = new TreeElement("11", "加载模式孩子2");
		TreeElement element12 = new TreeElement("12", "加载模式孩子2的孩子1");
		TreeElement element13 = new TreeElement("13", "加载模式孩子2的孩子2");
		TreeElement element14 = new TreeElement("14", "加载模式孩子1的孩子1");
		TreeElement element15 = new TreeElement("15", "加载模式孩子1的孩子2");
		TreeElement element16 = new TreeElement("16", "加载模式孩子1的孩子3");
		TreeElement element17 = new TreeElement("17", "加载模式孩子1的孩子4");
		TreeElement element18 = new TreeElement("18", "加载模式孩子1的孩子5");
		TreeElement element19 = new TreeElement("19", "加载模式孩子1的孩子6");

		mPdfOutlinesCount.add(element1);
		mPdfOutlinesCount.add(element2);
		mPdfOutlinesCount.add(element9);
		element2.addChild(element3);
		element1.addChild(element4);
		element1.addChild(element5);
		element1.addChild(element6);
		element1.addChild(element7);
		element7.addChild(element8);
		element9.addChild(element10);
		element9.addChild(element11);
		element11.addChild(element12);
		element11.addChild(element13);
		element10.addChild(element14);
		element10.addChild(element15);
		element10.addChild(element16);
		element10.addChild(element17);
		element10.addChild(element18);
		element10.addChild(element19);

	}

	@SuppressWarnings("unchecked")
	private class TreeViewAdapter extends ArrayAdapter {
		public TreeViewAdapter(Context context, int textViewResourceId, List objects) {
			super(context, textViewResourceId, objects);
			mInflater = LayoutInflater.from(context);
			mfilelist = objects;
			mIconCollapse = BitmapFactory.decodeResource(context.getResources(), 0);
			mIconExpand = BitmapFactory.decodeResource(context.getResources(), 0);
		}

		private LayoutInflater mInflater;
		private List<TreeElement> mfilelist;
		private Bitmap mIconCollapse;
		private Bitmap mIconExpand;

		public int getCount() {
			return mfilelist.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			/* if (convertView == null) { */
			convertView = mInflater.inflate(null, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(0);
			holder.icon = (ImageView) convertView.findViewById(0);
			convertView.setTag(holder);
			/*
			 * } else { holder = (ViewHolder) convertView.getTag(); }
			 */

			final TreeElement obj = mfilelist.get(position);

			holder.text.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.i("TreeView", "obj.id:" + obj.getId());
				}
			});

			int level = obj.getLevel();
			holder.icon.setPadding(25 * (level + 1), holder.icon.getPaddingTop(), 0,
					holder.icon.getPaddingBottom());
			holder.text.setText(obj.getOutlineTitle());
			if (obj.isMhasChild() && (obj.isExpanded() == false)) {
				holder.icon.setImageBitmap(mIconCollapse);
			} else if (obj.isMhasChild() && (obj.isExpanded() == true)) {
				holder.icon.setImageBitmap(mIconExpand);
			} else if (!obj.isMhasChild()) {
				holder.icon.setImageBitmap(mIconCollapse);
				holder.icon.setVisibility(View.INVISIBLE);
			}
			return convertView;
		}

		class ViewHolder {
			TextView text;
			ImageView icon;

		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.i("TreeView", "position:" + position);
		if (!mPdfOutlinesCount.get(position).isMhasChild()) {
			Toast.makeText(this, mPdfOutlinesCount.get(position).getOutlineTitle(), 2000);
			/*
			 * int pageNumber; Intent i = getIntent(); element element =
			 * mPdfOutlinesCount .get(position); pageNumber =
			 * element.getOutlineElement().pageNumber; if (pageNumber <= 0) {
			 * String name = element.getOutlineElement().destName; pageNumber =
			 * idocviewer.getPageNumberForNameForOutline(name);
			 * element.getOutlineElement().pageNumber = pageNumber;
			 * element.getOutlineElement().destName = null; }
			 * i.putExtra("PageNumber", pageNumber); setResult(RESULT_OK, i);
			 * finish();
			 */

			return;
		}

		if (mPdfOutlinesCount.get(position).isExpanded()) {
			mPdfOutlinesCount.get(position).setExpanded(false);
			TreeElement element = mPdfOutlinesCount.get(position);
			ArrayList<TreeElement> temp = new ArrayList<TreeElement>();

			for (int i = position + 1; i < mPdfOutlinesCount.size(); i++) {
				if (element.getLevel() >= mPdfOutlinesCount.get(i).getLevel()) {
					break;
				}
				temp.add(mPdfOutlinesCount.get(i));
			}

			mPdfOutlinesCount.removeAll(temp);

			treeViewAdapter.notifyDataSetChanged();
			/*
			 * fileExploreAdapter = new TreeViewAdapter(this, R.layout.outline,
			 * mPdfOutlinesCount);
			 */

			// setListAdapter(fileExploreAdapter);

		} else {
			TreeElement obj = mPdfOutlinesCount.get(position);
			obj.setExpanded(true);
			int level = obj.getLevel();
			int nextLevel = level + 1;

			for (TreeElement element : obj.getChildList()) {
				element.setLevel(nextLevel);
				element.setExpanded(false);
				mPdfOutlinesCount.add(position + 1, element);

			}
			treeViewAdapter.notifyDataSetChanged();
			/*
			 * fileExploreAdapter = new TreeViewAdapter(this, R.layout.outline,
			 * mPdfOutlinesCount);
			 */

			// setListAdapter(fileExploreAdapter);
		}
	}

}

class TreeElement {

	private String id;
	private String outlineTitle;
	private boolean mhasParent;
	private boolean mhasChild;
	private TreeElement parent;
	private int level;
	private ArrayList<TreeElement> childList = new ArrayList<TreeElement>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOutlineTitle() {
		return outlineTitle;
	}

	public void setOutlineTitle(String outlineTitle) {
		this.outlineTitle = outlineTitle;
	}

	public boolean isMhasParent() {
		return mhasParent;
	}

	public void setMhasParent(boolean mhasParent) {
		this.mhasParent = mhasParent;
	}

	public boolean isMhasChild() {
		return mhasChild;
	}

	public void setMhasChild(boolean mhasChild) {
		this.mhasChild = mhasChild;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public ArrayList<TreeElement> getChildList() {
		return childList;
	}

	public TreeElement getParent() {
		return parent;
	}

	public void setParent(TreeElement parent) {
		this.parent = parent;
	}

	// private OutlineElement outlineElement;
	private boolean expanded;

	public void addChild(TreeElement c) {
		this.childList.add(c);
		this.mhasParent = false;
		this.mhasChild = true;
		c.parent = this;
		c.level = this.level + 1;

	}

	public TreeElement(String id, String title) {
		super();
		this.id = id;
		this.outlineTitle = title;
		this.level = 0;
		this.mhasParent = true;
		this.mhasChild = false;
		this.parent = null;
	}

	public TreeElement(String id, String outlineTitle, boolean mhasParent, boolean mhasChild,
			TreeElement parent, int level, boolean expanded) {
		super();
		this.id = id;
		this.outlineTitle = outlineTitle;
		this.mhasParent = mhasParent;
		this.mhasChild = mhasChild;
		this.parent = parent;
		if (parent != null) {
			this.parent.getChildList().add(this);
		}
		this.level = level;
		this.expanded = expanded;
	}

}