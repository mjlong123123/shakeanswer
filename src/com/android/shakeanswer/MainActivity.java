package com.android.shakeanswer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
	private Button btn1 = null;
	private ComponentName receiver = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		receiver = new ComponentName(this, PhoneReceiver.class);
		findView();
	}

	private void findView() {
		btn1 = (Button) findViewById(R.id.btn1);
		btn1.setOnClickListener(this);
		if (checkReceiver()) {
			btn1.setText(R.string.btn_end);
		} else {
			btn1.setText(R.string.btn_start);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn1:
			
			if(checkShake())
			{
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, CheckShakeActivity.class);
				MainActivity.this.startActivity(intent);
				break;
			}
			if (checkReceiver()) {
				setReceiverEnable(false);
				btn1.setText(R.string.btn_start);
			} else {
				setReceiverEnable(true);
				btn1.setText(R.string.btn_end);
			}
			break;
		}
	}

	private void setReceiverEnable(Boolean flag) {
		if (flag) {
			getPackageManager().setComponentEnabledSetting(receiver,
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);
			Toast.makeText(this, R.string.tost_start, Toast.LENGTH_SHORT)
					.show();
		} else {
			getPackageManager().setComponentEnabledSetting(receiver,
					PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					PackageManager.DONT_KILL_APP);
			Toast.makeText(this, R.string.tost_end, Toast.LENGTH_SHORT).show();
		}
	}

	private boolean checkReceiver() {
		boolean ret = getPackageManager().getComponentEnabledSetting(receiver) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED ? true
				: false;
		return ret;
	}
	
	
	private boolean checkShake()
	{
		boolean ret = false;
		SharedPreferences sp = getSharedPreferences("check_shake", MODE_PRIVATE);
		ret = sp.getBoolean("check_shake", true);
		return ret;
	}
	
	private void setCheckShake(boolean flag)
	{
		Editor ed = getSharedPreferences("check_shake", MODE_PRIVATE).edit();
		ed.putBoolean("check_shake", flag);
		ed.commit();
	}
}
