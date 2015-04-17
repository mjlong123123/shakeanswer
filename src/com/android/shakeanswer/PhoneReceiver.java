package com.android.shakeanswer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class PhoneReceiver extends BroadcastReceiver {
	private static final String TAG = "PhoneReceiver";
	private static ShakeMgr mShakeMgr = null;

	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.v(TAG, "onReceive:");

		if (intent.getAction().equals("com.android.shakeanswer.PhoneReceiver")) {
			Bundle bundle = intent.getExtras();
			if (bundle == null) {
				return;
			}

			String state = (String) bundle.get("state");

			Log.v(TAG, "state:" + state);

			if (state.equals("RINGING")) {
				ringing(context);
			} else if (state.equals("OFFHOOK")) {
				offHook(context);
			} else if (state.equals("IDLE")) {
				idle(context);
			}
		} else if (intent.getAction().equals(
				"android.intent.action.PHONE_STATE")) {
			Bundle bundle = intent.getExtras();
			if (bundle == null) {
				return;
			}

			String state = (String) bundle.get("state");

			Log.v(TAG, "state:" + state);

			if (state.equals("RINGING")) {
				ringing(context);
			} else if (state.equals("OFFHOOK")) {
				offHook(context);
			} else if (state.equals("IDLE")) {
				idle(context);
			}
		}
	}

	private void ringing(final Context context) {
		if (mShakeMgr != null) {
			mShakeMgr.stop(context);
			mShakeMgr = null;
		}

		mShakeMgr = new ShakeMgr(context, new ShakeMgr.OnShakeListener() {

			@Override
			public void onShake() {
				Log.e("dragon", "onShake ---------");
				context.startService(new Intent(context,
						AutoAnswerIntentService.class));
			}
		});
		mShakeMgr.start(context);
	}

	private void offHook(Context context) {
		if (mShakeMgr != null) {
			mShakeMgr.stop(context);
			mShakeMgr = null;
		}
	}

	private void idle(Context context) {
		if (mShakeMgr != null) {
			mShakeMgr.stop(context);
			mShakeMgr = null;
		}
	}

}
