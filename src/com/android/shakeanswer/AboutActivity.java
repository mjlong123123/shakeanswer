package com.android.shakeanswer;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AboutActivity extends BaseActivity {
	private ListView mListView;
	private ArrayList<String> mDatas = new ArrayList<String>();
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
	
	private void initData(){
		mDatas.add("icon");
		mDatas.add("exit");
		mDatas.add("help");
		mDatas.add("feedback");
		mDatas.add("check update");
	}

	private void initView(){
		mLayoutInflater = LayoutInflater.from(this);
		mListView = (ListView)findViewById(R.id.listview);
		mListView.setAdapter(new AboutAdapter());
	}
	
	private class AboutAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mDatas.size();
		}

		@Override
		public Object getItem(int position) {
			
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = mLayoutInflater.inflate(R.layout.about_list_item, parent, false);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.textview);
			tv.setText((CharSequence) getItem(position));
			return convertView;
		}
		
	}
}
