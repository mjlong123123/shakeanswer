package com.android.shakeanswer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class RroximitySonsor {
	static SensorEventListener mSensorEventListener = null;
	static CallBack mCallBack;

	static void start(Context context, CallBack callback) {
		if (context == null) {
			Log.e("dragon",
					"Proximity start context is null----------error---------");
			return;
		}

		SensorManager sm = (SensorManager) context
				.getSystemService(context.SENSOR_SERVICE);
		if (sm == null) {
			Log.e("dragon",
					"Proximity start sm is null----------error---------");
			return;
		}
		Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		if (sensor == null) {
			Log.e("dragon",
					"Proximity start sensor is null----------error---------");
			return;
		}
		if (mSensorEventListener != null) {
			sm.unregisterListener(mSensorEventListener);
			mSensorEventListener = null;
		}
		mSensorEventListener = new SensorEventListener() {
			private long mLastTime;

			@Override
			public void onSensorChanged(SensorEvent event) {
				long distime;
				if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
					Log.v("dragon", "event1:" + event.values[0]);
					distime = (System.currentTimeMillis() - mLastTime);
					mLastTime = System.currentTimeMillis();
					Log.v("dragon", "time:" + distime);
					
					if(distime < 100 && event.values[0] > 3){
						Log.v("dragon", "callbac -----------------------");
						if(mCallBack != null){
							mCallBack.notifyOpen();
						}
					}
				}

			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub

			}
		};
		mCallBack = callback;
		sm.registerListener(mSensorEventListener, sensor,
				SensorManager.SENSOR_DELAY_FASTEST);
		
	}

	static void stop(Context context) {
		if (context == null) {
			Log.e("dragon",
					"Proximity stop context is null----------error---------");
			return;
		}

		SensorManager sm = (SensorManager) context
				.getSystemService(context.SENSOR_SERVICE);
		if (sm == null) {
			Log.e("dragon", "Proximity stop sm is null----------error---------");
			return;
		}

		sm.unregisterListener(mSensorEventListener);
		mSensorEventListener = null;
		
		mCallBack = null;
	}
	
	public interface CallBack{
		void notifyOpen();
	}
}
