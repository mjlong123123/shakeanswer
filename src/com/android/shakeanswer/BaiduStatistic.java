package com.android.shakeanswer;

import android.content.Context;

import com.baidu.mobstat.StatService;

public class BaiduStatistic {

	public static void onResume(Context context) {
		StatService.onResume(context);
	}

	public static void onPause(Context context) {
		StatService.onPause(context);
	}

	public static void onEvent(Context context, String arg1, String arg2) {
		StatService.onEvent(context, arg1, arg2);
	}
}
