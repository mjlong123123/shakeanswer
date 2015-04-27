package com.android.shakeanswer;

import android.app.Activity;

public class BaseActivity extends Activity {
	@Override
	protected void onResume() {
		BaiduStatistic.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		BaiduStatistic.onPause(this);
		super.onPause();
	}
}
