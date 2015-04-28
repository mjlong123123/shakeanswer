package com.android.shakeanswer;

import android.os.Bundle;

public class HelpActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.help_layout);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
