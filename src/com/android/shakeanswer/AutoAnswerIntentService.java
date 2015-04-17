package com.android.shakeanswer;

import java.lang.reflect.Method;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

public class AutoAnswerIntentService extends IntentService {

	public AutoAnswerIntentService() {
		super("AutoAnswerIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Context context = getBaseContext();
		Log.e("dragon", "handle intent");
		// Load preferences
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		// Let the phone ring for a set delay
		try {
			Thread.sleep(Integer.parseInt(prefs.getString("delay", "2")) * 1000);
		} catch (InterruptedException e) {
			// We don't really care
		}

		// Check headset status right before picking up the call

		// Make sure the phone is still ringing
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getCallState() != TelephonyManager.CALL_STATE_RINGING) {

			Log.e("dragon", "handle CALL_STATE_RINGING error");
			return;
		}

		// Answer the phone
		try {
			answerPhoneAidl(context);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("dragon", "handle e:"+e);
			Log.d("AutoAnswer",
					"Error trying to answer using telephony service.  Falling back to headset.");
			answerPhoneHeadsethook(context);
		}
//		answerPhoneHeadsethook(context);
		// Enable the speakerphone
		if (prefs.getBoolean("use_speakerphone", false)) {
			enableSpeakerPhone(context);
		}
		return;
	}

	private void enableSpeakerPhone(Context context) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setSpeakerphoneOn(true);
	}

	private void answerPhoneHeadsethook(Context context) {
		// Simulate a press of the headset button to pick up the call
		Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
		buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
				KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
		context.sendOrderedBroadcast(buttonDown,
				"android.permission.CALL_PRIVILEGED");

		// froyo and beyond trigger on buttonUp instead of buttonDown
		Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
		buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
				KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
		context.sendOrderedBroadcast(buttonUp,
				"android.permission.CALL_PRIVILEGED");
		Log.e("dragon", "handle answerPhoneHeadsethook");
	}

	@SuppressWarnings("unchecked")
	private void answerPhoneAidl(Context context) throws Exception {
		// Set up communication with the telephony service (thanks to Tedd's
		// Droid Tools!)
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		Class c = Class.forName(tm.getClass().getName());
		Method m = c.getDeclaredMethod("getITelephony");
		m.setAccessible(true);
		Object telephonyService;
		telephonyService = (Object) m.invoke(tm);

		// Silence the ringer and answer the call!
		Method m2 = telephonyService.getClass().getDeclaredMethod(
				"silenceRinger");
		Method m3 = telephonyService.getClass().getDeclaredMethod(
				"answerRingingCall");

		m2.invoke(telephonyService);
		m3.invoke(telephonyService);
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

	// try {
	//
	// Log.e("Sandy", "for version 4.1 or larger");
	//
	// Intent intent = new Intent(
	// "android.intent.action.MEDIA_BUTTON");
	//
	// KeyEvent keyEvent = new KeyEvent(
	// KeyEvent.ACTION_UP,
	// KeyEvent.KEYCODE_HEADSETHOOK);
	//
	// intent.putExtra("android.intent.extra.KEY_EVENT",
	// keyEvent);
	//
	// context.sendOrderedBroadcast(intent,
	// "android.permission.CALL_PRIVILEGED");
	//
	// } catch (Exception e2) {
	//
	// Log.d("Sandy", "", e2);
	//
	// Intent meidaButtonIntent = new Intent(
	// Intent.ACTION_MEDIA_BUTTON);
	//
	// KeyEvent keyEvent = new KeyEvent(
	// KeyEvent.ACTION_UP,
	// KeyEvent.KEYCODE_HEADSETHOOK);
	//
	// meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT,
	// keyEvent);
	//
	// context.sendOrderedBroadcast(meidaButtonIntent,
	// null);
	//
	// }

}