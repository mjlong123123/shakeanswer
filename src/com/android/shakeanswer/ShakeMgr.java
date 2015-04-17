package com.android.shakeanswer;

import java.util.Calendar;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class ShakeMgr implements SensorEventListener {
	private static final String TAG = "ShakeListener";
	private static final int SPEED_MY = 40;// 摇晃强度
	private static final int BREAK_COUNT = 50;// 标准的是一秒钟50个通知

	private boolean USED = true;
	private SensorManager sensorManager;
	private Sensor sensor;

	private Sensor sensorlight;
	private SensorEventListener mSensorEventListenerForLight;
	private float light = 0.0f;
	private OnShakeListener onShakeListener;
	private Context mContext;
	private boolean mFlagRes = false;

	private float x;
	private float y;
	private float z;

	private double currentspeed = 0;
	private double maxspeed = 0;
	private int breakcount = 0;

	private static ShakeMgr mInstance = null;

	public static ShakeMgr getInstance(Context c) {
		if (c == null) {
			return null;
		}
		if (mInstance == null) {
			mInstance = new ShakeMgr(c);
		}
		return mInstance;
	}

	private ShakeMgr(Context c) {

		if (!USED)
			return;

		mContext = c;

		if (mContext == null) {
			Log.v(TAG, "ShakeMgr() mContext == null");
			return;
		}

		sensorManager = (SensorManager) mContext
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
	}

	public void start() {
		if (!USED)
			return;
		if (mFlagRes) {
			Log.v(TAG, "start() mFlagRes = true");
			return;
		}

		if (sensorManager == null || sensor == null) {
			Log.v(TAG, "start() sensorManager == null||sensor == null");
			return;
		}
		sensorManager.registerListener(this, sensor,
				SensorManager.SENSOR_DELAY_GAME);
		
		mSensorEventListenerForLight = new SensorEventListener() {
			
			@Override
			public void onSensorChanged(SensorEvent event) {
				int type = event.sensor.getType();
				Log.e("dragon", "light listener type:"+type);
				Log.e("dragon", "light listener value:"+event.values[0]);
				light = event.values[0];
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				
			}
		};
		sensorManager.registerListener(mSensorEventListenerForLight , sensor, SensorManager.SENSOR_DELAY_GAME);
		
		breakcount = 0;
		mFlagRes = true;
	}

	public void stop() {
		if (!USED)
			return;
		if (!mFlagRes) {
			Log.v(TAG, "stop() mFlagRes = false");
			return;
		}
		if (sensorManager == null) {
			Log.v(TAG, "stop() sensorManager == null");
			return;
		}
		sensorManager.unregisterListener(this);
		sensorManager.unregisterListener(mSensorEventListenerForLight);
		breakcount = 0;
		mFlagRes = false;
	}

	public void setOnShakeListener(OnShakeListener listener) {
		onShakeListener = listener;
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
			if (onShakeListener != null && light > 100)
				onShakeListener.onShake();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	public interface OnShakeListener {
		public void onShake();
	}
}
