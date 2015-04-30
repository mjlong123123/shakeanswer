package com.android.shakeanswer;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends BaseActivity {
	private ListView mListView;
	private ArrayList<ItemData> mDatas = new ArrayList<ItemData>();
	private LayoutInflater mLayoutInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.about_layout);
		initData();
		initView();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initData() {
		mDatas.add(new ItemData(INDEX_ICON, R.drawable.ic_launcher, R.string.appname));
		mDatas.add(new ItemData(INDEX_HELP, 0, R.string.about_help));
		mDatas.add(new ItemData(INDEX_UPDATE, 0, R.string.about_update));
		mDatas.add(new ItemData(INDEX_RECOMMEND, 0, R.string.about_recommend));
	}

	private void initView() {
		mLayoutInflater = LayoutInflater.from(this);
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setAdapter(new AboutAdapter());
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ItemData item = (ItemData) parent.getItemAtPosition(position);

				switch (item.mIndex) {
				case INDEX_ICON:
					Toast.makeText(AboutActivity.this, getString(R.string.appname) + " V" + getVersion(),
							Toast.LENGTH_LONG).show();
					break;
				case INDEX_HELP: {
					Builder builder = new Builder(AboutActivity.this);
					builder.setTitle(R.string.about_help);
					builder.setMessage(R.string.dialog_help);
					builder.setPositiveButton(R.string.ok, null);
					builder.create().show();
				}
					break;
				case INDEX_UPDATE:
					break;
				case INDEX_RECOMMEND:
					break;
				}
			}
		});
	}

	private String getVersion(){
		String ret = "1.0.0";
		try {
			PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
			ret = pi.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return ret;
	}
	private class AboutAdapter extends BaseAdapter {

		@Override
		public int getItemViewType(int position) {
			ItemData item = getItem(position);
			if (item != null && item.mIndex == INDEX_ICON)
				return 1;
			else
				return 0;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getCount() {
			return mDatas.size();
		}

		@Override
		public ItemData getItem(int position) {

			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int type = getItemViewType(position);
			ItemData item = getItem(position);
			if (convertView == null) {
				if (type == 0) {
					convertView = mLayoutInflater.inflate(
							R.layout.about_list_item, parent, false);
				} else {
					convertView = mLayoutInflater.inflate(
							R.layout.about_list_icon_item, parent, false);
				}
			}
			TextView tv = (TextView) convertView.findViewById(R.id.textview);
			tv.setText(item.mTitleId);
			if(type == 1){
				ImageView iv = (ImageView)convertView.findViewById(R.id.imageview);
				iv.setBackgroundResource(item.mIconId);
			}
			return convertView;
		}

	}

	private static final int INDEX_ICON = 1;
	private static final int INDEX_HELP = INDEX_ICON + 1;
	private static final int INDEX_UPDATE = INDEX_HELP + 1;
	private static final int INDEX_RECOMMEND = INDEX_UPDATE + 1;

	private static class ItemData {
		public ItemData(int index, int icon, int title) {
			mIndex = index;
			mIconId = icon;
			mTitleId = title;
		}

		public int mIndex;
		public int mIconId;
		public int mTitleId;
	}
}
