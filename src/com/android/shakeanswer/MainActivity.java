package com.android.shakeanswer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements View.OnClickListener {
	private Button btn1 = null;
	private ComponentName receiver = null;
	private TextView mMsg;
	private TextView mAbout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		receiver = new ComponentName(this, PhoneReceiver.class);
		findView();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void findView() {
		btn1 = (Button) findViewById(R.id.btn1);
		mMsg = (TextView)findViewById(R.id.tv_msg);
		btn1.setOnClickListener(this);
		if (checkReceiver()) {
			btn1.setText(R.string.btn_end);
			mMsg.setText(R.string.msg1);
		} else {
			btn1.setText(R.string.btn_start);
			mMsg.setText(R.string.msg2);
		}
		
        mAbout = (TextView)findViewById(R.id.tv_about);
        SpannableString sp_about = new SpannableString(getResources().getString(R.string.about));
        sp_about.setSpan(new URLSpan("") {
			
			@Override
			public void onClick(View widget) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, AboutActivity.class);
				MainActivity.this.startActivity(intent);
			}
		}, 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        mAbout.setText(sp_about);
        mAbout.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn1:

			if (checkReceiver()) {
				setReceiverEnable(false);
				btn1.setText(R.string.btn_start);
				mMsg.setText(R.string.msg2);
			} else {
				setReceiverEnable(true);
				btn1.setText(R.string.btn_end);
				mMsg.setText(R.string.msg1);
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
}
