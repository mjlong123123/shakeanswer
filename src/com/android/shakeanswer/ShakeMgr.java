package com.android.shakeanswer;

import java.util.Calendar;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.text.format.DateFormat;
import android.util.Log;

public class ShakeMgr implements SensorEventListener {
	private static final String TAG = "dragon";
	private static final int SPEED_MY = 40;// 摇晃强度
	private static final int BREAK_COUNT = 50;// 标准的是一秒钟50个通知

	private SensorManager sensorManager;
	private Sensor sensor;

	private Sensor sensorlight;
	private SensorEventListener mSensorEventListenerForLight;

	private float light = 0.0f;
	private OnShakeListener onShakeListener;
	private boolean mFlagRes = false;

	private float x;
	private float y;
	private float z;

	private double currentspeed = 0;
	private double maxspeed = 0;
	private int breakcount = 0;

	private boolean mIsDay = false;

	public ShakeMgr(Context context, OnShakeListener listener) {
		onShakeListener = listener;
		sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);

		if (sensorManager == null) {
			Log.v(TAG, "ShakeMgr() sensorManager == null");
			return;
		}

		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		if (sensor == null) {
			Log.v(TAG, "ShakeMgr() sensor == null");
			return;
		}

		sensorlight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

		if (sensorlight == null) {
			Log.v(TAG, "ShakeMgr() sensorlight == null");
			return;
		}
	}

	public void start(Context context) {
		if (mFlagRes) {
			Log.v(TAG, "start() mFlagRes = true");
			return;
		}

		if (sensorManager == null || sensor == null || sensorlight == null) {
			Log.v(TAG, "start() sensorManager == null||sensor == null");
			return;
		}

		mIsDay = false;

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int hour = c.get(Calendar.HOUR);
		Log.v(TAG, "hour:" + hour);
		if (DateFormat.is24HourFormat(context)) {

			Log.v(TAG, "24 hours");

			if (hour > 6 && hour < 18) {
				mIsDay = true;
			} else {
				mIsDay = false;
			}
		} else {
			Log.v(TAG, "12 hours");

			if (c.get(Calendar.AM_PM) == Calendar.AM) {
				if (hour >= 6) {
					mIsDay = true;
				} else {
					mIsDay = false;
				}
			} else if (c.get(Calendar.HOUR_OF_DAY) == Calendar.PM) {
				if (hour <= 6) {
					mIsDay = true;
				} else {
					mIsDay = false;
				}
			}
		}

		sensorManager.registerListener(this, sensor,
				SensorManager.SENSOR_DELAY_GAME);

		mSensorEventListenerForLight = new SensorEventListener() {

			@Override
			public void onSensorChanged(SensorEvent event) {
				int type = event.sensor.getType();
				Log.e("dragon", "light listener type:" + type);
				Log.e("dragon", "light listener value:" + event.values[0]);
				light = event.values[0];
				
				if(light < 100){
					onShakeListener.onShake();
				}
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {

			}
		};
		sensorManager.registerListener(mSensorEventListenerForLight, sensorlight,
				SensorManager.SENSOR_DELAY_GAME);

		breakcount = 0;
		mFlagRes = true;
	}

	public void stop(Context context) {
		if (!mFlagRes) {
			Log.v(TAG, "stop() mFlagRes = false");
			return;
		}
		if (sensorManager == null) {
			Log.v(TAG, "stop() sensorManager == null");
			return;
		}
		Log.v(TAG, "stop()");
		sensorManager.unregisterListener(this);
		sensorManager.unregisterListener(mSensorEventListenerForLight);
		breakcount = 0;
		mFlagRes = false;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (breakcount > 0) {
			breakcount--;
			return;
		}
		breakcount = 0;

		x = event.values[0];
		y = event.values[1];
		z = event.values[2];

		currentspeed = Math.sqrt(x * x + y * y + z * z);

		maxspeed = currentspeed > maxspeed ? currentspeed : maxspeed;

		if (maxspeed > SPEED_MY) {
			maxspeed = 0;
			breakcount = BREAK_COUNT;
			if (mIsDay && light < 100) {
				Log.e("dragon", "light is wrong");
				return;
			}
			if (onShakeListener != null && light > 100)
				onShakeListener.onShake();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public interface OnShakeListener {
		public void onShake();
	}
}
