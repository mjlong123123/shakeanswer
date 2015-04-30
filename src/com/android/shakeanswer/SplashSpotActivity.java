package com.android.shakeanswer;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;
import android.content.Intent;
import android.os.Bundle;

public class SplashSpotActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AdManager.getInstance(this).init("75f2c67eeb8b2ba5",
				"2a4b4ea5ea075ef3", false);

		SpotManager.getInstance(this).loadSplashSpotAds();
		SpotManager.getInstance(this).showSplashSpotAds(this,
				MainActivity.class);
	}

	// 请务必加上词句，否则进入网页广告后无法进去原sdk
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 10045) {
			Intent intent = new Intent(SplashSpotActivity.this,
					MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
