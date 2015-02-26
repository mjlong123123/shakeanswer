package com.android.shakeanswer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.shakeanswer.ShakeMgr.OnShakeListener;

public class PhoneReceiver extends BroadcastReceiver
{
	private static final String TAG = "PhoneReceiver";

	/*
	 * RINGING 响铃
	 * IDLE 停止
	 * OFFHOOK 接电话
	 * (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		LogMy.v(TAG, "onReceive:");
		if (intent.getAction().equals("com.android.shakeanswer.PhoneReceiver"))
		{
			Bundle bundle = intent.getExtras();
			if (bundle == null)
			{
				return;
			}

			String state = (String) bundle.get("state");

			LogMy.v(TAG, "state:" + state);

			if (state.equals("RINGING") || state.equals("OFFHOOK"))
			{
				final Context context1 = context;

				ShakeMgr sm = null;
				sm = ShakeMgr.getInstance(context);
				LogMy.v(TAG, "RINGING OFFHOOK sm " + sm);
				sm.start();
				sm.setOnShakeListener(new OnShakeListener()
				{
					@Override
					public void onShake()
					{
						Toast.makeText(context1, "shake", Toast.LENGTH_SHORT).show();
						answerRingingCall(context1);
					}
				});
			}
			else if (state.equals("IDLE"))
			{
				ShakeMgr sm = null;
				sm = ShakeMgr.getInstance(context);
				LogMy.v(TAG, "IDLE sm " + sm);
				sm.setOnShakeListener(null);
				sm.stop();
				sm = null;
			}

		}
		else if (intent.getAction().equals("android.intent.action.PHONE_STATE"))
		{
			Bundle bundle = intent.getExtras();
			if (bundle == null)
			{
				return;
			}

			String state = (String) bundle.get("state");

			LogMy.v(TAG, "state:" + state);

			if (state.equals("RINGING"))
			{
				final Context context1 = context;

				ShakeMgr sm = null;
				sm = ShakeMgr.getInstance(context);
				LogMy.v(TAG, "RINGING OFFHOOK sm " + sm);
				sm.start();
				sm.setOnShakeListener(new OnShakeListener()
				{
					@Override
					public void onShake()
					{
						Toast.makeText(context1, "shake", Toast.LENGTH_SHORT).show();
						answerRingingCall(context1);
					}
				});
			}
			else if (state.equals("IDLE"))
			{
				ShakeMgr sm = null;
				sm = ShakeMgr.getInstance(context);
				
				LogMy.v(TAG, "IDLE sm " + sm);
				
				sm.setOnShakeListener(null);
				sm.stop();
				sm = null;
			}

		}
	}

	private synchronized void answerRingingCall(Context context)
	{
		try
		{
			Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
			localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			localIntent1.putExtra("state", 1);
			localIntent1.putExtra("microphone", 1);
			localIntent1.putExtra("name", "Headset");
			context.sendOrderedBroadcast(localIntent1, "android.permission.CALL_PRIVILEGED");

			Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
			KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
			localIntent2.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent1);
			context.sendOrderedBroadcast(localIntent2, "android.permission.CALL_PRIVILEGED");

			Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
			KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
			localIntent3.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent2);
			context.sendOrderedBroadcast(localIntent3, "android.permission.CALL_PRIVILEGED");

			Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
			localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			localIntent4.putExtra("state", 0);
			localIntent4.putExtra("microphone", 1);
			localIntent4.putExtra("name", "Headset");
			context.sendOrderedBroadcast(localIntent4, "android.permission.CALL_PRIVILEGED");

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
