package com.android.shakeanswer;

import android.util.Log;

public class LogMy
{
	private static boolean mFlag = true;
	
	static public void setFlag(boolean flag)
	{
		mFlag = flag;
	}
	
	static public void v(String tag,String msg)
	{
		if(mFlag)
			Log.v(tag, msg);
	}
}
