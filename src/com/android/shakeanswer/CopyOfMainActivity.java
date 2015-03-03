package com.android.shakeanswer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class CopyOfMainActivity extends Activity implements SensorEventListener,
		Callback {
	private static final String TAG = "MainActivity";
	private static final boolean DEBUG = true;
	private SensorManager mSensorManager;
	private Sensor sensor;
	private SurfaceView mSurfaceView = null;
	private SurfaceHolder mSurfaceHolder = null;
	private boolean mBreak = false;

	private static final int DISTENCE = 10;
	private int mSurfaceW = 0;
	private int mSurfaceH = 0;
	private Rect mDrawingRect = new Rect();
	private static final int SCALE = 5;
	private float mStartX = 0;
	private float mStartY = 0;
	private double mLastRet = 0.0f;
	private Paint mPaint = new Paint();
	private Paint mPaint2 = new Paint();
	private boolean mClear = true;
	private ArrayList<Point> mPoints = new ArrayList<Point>();
	private Rect [] mRects = new Rect[4];
	private Rect mSelectedRect = null;
	private float mLastx = 0;
	private float mLasty = 0;
	private boolean mHaveRect = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSurfaceView = (SurfaceView) findViewById(R.id.sv);
		mSurfaceView.getHolder().addCallback(this);
		mSurfaceView.getHolder().setType(
				SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		// mSurfaceView.setZOrderOnTop(true);
		mSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		mSurfaceView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				mBreak = !mBreak;
				if(!mBreak)
					sortRect();
				
				if(mRects[0] == null)
				{
					int ww = DISTENCE*8;
					for(int i = 0; i < mRects.length; i++)
					{
						mRects[i] = new Rect(ww*i, 0, ww*(i+1), ww);
					}
				}
				reDraw();
			}
		});
		mSurfaceView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				boolean ret = false;
				if(mBreak)
				{
					int action = arg1.getAction();
					switch(action)
					{
					case MotionEvent.ACTION_DOWN:
						mLastx = arg1.getX();
						mLasty = arg1.getY();
						
						mSelectedRect = getRect(mLastx,mLasty);
						if(mSelectedRect != null)
							ret = true;
						break;

					case MotionEvent.ACTION_MOVE:
						float offsetx = arg1.getX();
						float offsety = arg1.getY();
						if(mSelectedRect != null)
						{
							mSelectedRect.offset((int)(offsetx-mLastx), (int)(offsety-mLasty));
							reDraw();
							ret = true;
						}
						mLastx = offsetx;
						mLasty = offsety;
						break;

					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						mSelectedRect = null;
						break;
						
					}
				}
				return ret;
			}
		});
	}

	@Override
	protected void onResume() {
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (mSensorManager == null) {
			Log.i("dragon", "sensor not supported");
		}
		mSensorManager.registerListener(this, sensor,
				SensorManager.SENSOR_DELAY_GAME);
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (mSensorManager != null) {
			mSensorManager.unregisterListener(this);
			mSensorManager = null;
		}
		super.onPause();
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) { // 在activity中完成该接口函数
		if (event.sensor == null) {
			return;
		}

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			if (mBreak)
				return;

			double ret = 0.0f;
			ret = Math.sqrt((event.values[0] * event.values[0])
					+ (event.values[1] * event.values[1])
					+ (event.values[2] * event.values[2]));
			ret *= SCALE;

			if (mSurfaceHolder != null) {

				if (mClear) {
					mPoints.clear();
					Canvas canvas2 = mSurfaceHolder.lockCanvas();
					canvas2.drawColor(Color.WHITE);
					mSurfaceHolder.unlockCanvasAndPost(canvas2);
					mClear = false;
				}

				mDrawingRect.left = (int) mStartX;
				mDrawingRect.top = 0;
				mDrawingRect.right = (int) (mStartX + 10);
				mDrawingRect.bottom = (int) (mSurfaceH);
				Canvas canvas = mSurfaceHolder.lockCanvas(mDrawingRect);
				canvas.drawColor(Color.WHITE);
				canvas.drawLine(mStartX, (float) (mStartY + mLastRet), mStartX
						+ DISTENCE, (float) (mStartY + ret), mPaint);
				canvas.drawLine(mStartX, 0, mStartX, mSurfaceH, mPaint);

				mPoints.add(new Point((int) mStartX, (int) (mStartY + mLastRet)));
				
				if(mRects[0] != null &&containRect(0, mPoints.size()-1))
				{
					Log.e(TAG, "ok okok okok okok okok okok okok okok okok okok okok okok okok okok ok");
				}
				
				mLastRet = ret;
				mStartX += DISTENCE;
				if (mStartX >= mSurfaceW) {
					mStartX = 0;
					mLastRet = 0;
					mClear = true;
				}
				mSurfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		mSurfaceW = arg2;
		mSurfaceH = arg3;
		if (DEBUG) {
			Log.e("dragon", String.format("surfaceChanged w:%d h:%d",
					mSurfaceW, mSurfaceH));
		}

		mStartY = mSurfaceH / 2;
		mStartX = 0;
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		mSurfaceHolder = arg0;
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.GREEN);
		mPaint.setStrokeWidth(2.0f);
		mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC));
		mPaint2.setAntiAlias(true);
		mPaint2.setColor(Color.parseColor("#5fff00ff"));
		mPaint2.setStrokeWidth(2.0f);

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
	}
	
	
	private void reDraw()
	{
		Canvas can = mSurfaceHolder.lockCanvas();
		can.drawColor(Color.WHITE);
		if(mPoints.size()>2)
		{
			Point prepoint = mPoints.get(0);
			Point curpoint = null;
			for(int i = 1; i < mPoints.size(); i++)
			{
				curpoint = mPoints.get(i);
				can.drawLine(prepoint.x, prepoint.y, curpoint.x, curpoint.y, mPaint);
				prepoint=curpoint;
			}
		}
		for(int i = 0; i < mRects.length; i++)
		{
			can.drawRect(mRects[i], mPaint2);
		}
		
		mSurfaceHolder.unlockCanvasAndPost(can);
	}
	
	
	private Rect getRect(float x, float y)
	{
		for(Rect rect:mRects)
		{
			if(rect.contains((int)x, (int)y))
			{
				return rect;
			}
		}
		return null;
	}
	
	private void sortRect()
	{
		int start = 1;
		Rect rect = null;
		for(int i=start; i < mRects.length && start < mRects.length; i++,start++)
		{
			if(mRects[i-1].left < mRects[i].left)
			{
				rect = mRects[i-1];
				mRects[i-1] = mRects[i];
				mRects[i] = rect;
			}
		}
		if(DEBUG)
		for(int i = 0; i < mRects.length; i++)
		{
			Log.e(TAG, "rect "+mRects[i].left);
		}
		mHaveRect = true;
	}
	
//	private boolean checkRect(int x, int y)
//	{
//		boolean ret = false;
//		if(mRects[0].contains((int) mStartX, (int) (mStartY + mLastRet)))
//		{
//			int dis = mRects[0].left-mRects[1].left;
//			int scal = dis/DISTENCE;
//			int position = 0;
//			position = mPoints.size() - scal;
//			if(position >= 0)
//			{
//				if(mRects[1].contains(mPoints.get(position).x, mPoints.get(position).y))
//				{
//					
//					
//					
//					
//				}
//			}
//		}
//		return ret;
//	}
	
	private boolean containRect(int startRec, int pointPosition)
	{
		boolean ret = false;
		if(startRec < 0 || startRec >= mRects.length-1)
			return true;
		if(pointPosition >= mPoints.size()||pointPosition <0)
			return ret;
		Point point = mPoints.get(pointPosition);
		
		if(mRects[startRec].contains(point.x, point.y))
		{
			int dis = mRects[startRec].left-mRects[startRec+1].left;
			int scal = dis/DISTENCE;
			int position = 0;
			position = pointPosition - scal;
			if(position >= 0)
			{
				ret = containRect(startRec+1,position);
			}
		}
		return ret;
	}
}
