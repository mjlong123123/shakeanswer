package com.android.shakeanswer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.android.shakeanswer.RroximitySonsor.CallBack;

public class PhoneReceiver extends BroadcastReceiver {
	private static final String TAG = "PhoneReceiver";

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
				RroximitySonsor.start(context, new CallBack() {

					@Override
					public void notifyOpen() {
						answerRingingCall(context);
					}
				});
			} else if (state.equals("OFFHOOK")) {
				RroximitySonsor.stop(context);
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
				RroximitySonsor.start(context, new CallBack() {

					@Override
					public void notifyOpen() {
//						answerRingingCall(context);
						
						context.startService(new Intent(context,AutoAnswerIntentService.class));
						
					}
				});
			} else if (state.equals("OFFHOOK")) {
				RroximitySonsor.stop(context);
			}
		}
	}

	private synchronized void answerRingingCall(Context context) {
		try {
			Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
			localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			localIntent1.putExtra("state", 1);
			localIntent1.putExtra("microphone", 1);
			localIntent1.putExtra("name", "Headset");
			context.sendOrderedBroadcast(localIntent1,
					"android.permission.CALL_PRIVILEGED");

			Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
			KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,
					KeyEvent.KEYCODE_HEADSETHOOK);
			localIntent2.putExtra("android.intent.extra.KEY_EVENT",
					localKeyEvent1);
			context.sendOrderedBroadcast(localIntent2,
					"android.permission.CALL_PRIVILEGED");

			Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
			KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,
					KeyEvent.KEYCODE_HEADSETHOOK);
			localIntent3.putExtra("android.intent.extra.KEY_EVENT",
					localKeyEvent2);
			context.sendOrderedBroadcast(localIntent3,
					"android.permission.CALL_PRIVILEGED");

			Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
			localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			localIntent4.putExtra("state", 0);
			localIntent4.putExtra("microphone", 1);
			localIntent4.putExtra("name", "Headset");
			context.sendOrderedBroadcast(localIntent4,
					"android.permission.CALL_PRIVILEGED");

		} catch (Exception e) {
			Log.e("dragon", "e:" + e);
		}
	}
}
