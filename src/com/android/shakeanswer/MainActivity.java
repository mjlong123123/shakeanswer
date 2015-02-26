package com.android.shakeanswer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener
{
	private Button btn1 = null;
	private ComponentName receiver = null; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		receiver = new ComponentName(this, PhoneReceiver.class);
		findView();
	}

	private void findView()
	{
		btn1 = (Button)findViewById(R.id.btn1);
		btn1.setOnClickListener(this);
		if(checkReceiver())
		{
			btn1.setText(R.string.btn_end);
		}
		else
		{
			btn1.setText(R.string.btn_start);
		}
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.btn1:
				if(checkReceiver())
				{
					setReceiverEnable(false);
					btn1.setText(R.string.btn_start);
				}
				else
				{
					setReceiverEnable(true);
					btn1.setText(R.string.btn_end);
				}
				break;
		}
	}
	
	private void setReceiverEnable(Boolean flag)
	{
		if(flag)
		{
			getPackageManager().setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
			Toast.makeText(this, R.string.tost_start, Toast.LENGTH_SHORT).show();
		}
		else
		{
			getPackageManager().setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
			Toast.makeText(this, R.string.tost_end, Toast.LENGTH_SHORT).show();
		}
	}
	
	private boolean checkReceiver()
	{
		boolean ret = getPackageManager().getComponentEnabledSetting(receiver) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED ? true : false;
		return ret;
	}
}
