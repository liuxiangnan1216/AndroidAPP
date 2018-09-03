package com.example.agingmonkeytestv2.testitem;
/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-9.
 ************************************************************************/
import com.example.agingmonkeytestv2.BaseTestActivity;
import com.example.agingmonkeytestv2.R;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

public class BluetoothTestItem extends AbstractBaseTestItem{
	
    private long TEST_DURATION = 3000;
    private long WAIT_TIME = 2000;
    private BluetoothAdapter mBluetoothAdapter;
    private Switch mBluetoothSwitch;
    private String mTextOff;
    private String mTextOn;

	public BluetoothTestItem(int resLayoutId) {
		super(resLayoutId);
		// TODO Auto-generated constructor stub
	}
	
	public boolean execTest(Handler handler) {
		SystemClock.sleep(this.WAIT_TIME);
		transBluetoothDisEnable(handler);
		SystemClock.sleep(this.TEST_DURATION);
		transBluetoothDisEnable(handler);
		return true;
	}
	
	protected void initView(View view) {
		this.mBluetoothSwitch = (Switch) view.findViewById(R.id.switch_bluetooth);
		this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		this.mTextOff = this.mContext.getString(R.string.text_bluetooth_off);
		this.mTextOn = this.mContext.getString(R.string.text_bluetooth_on);
		changeBluetoothSwitchState();
	}
	
	public void tearDown() {
		super.tearDown();
		changeBluetoothSwitchState();
	}
	
	public void setUp(){
		super.setUp();
		changeBluetoothSwitchState();
	}
	
    public void onStop() {
        super.onStop();
    }
	
	private void changeBluetoothSwitchState() {
		Log.d("lxn--BluetoothTestItem", "BluetoothEnabled : " + this.mBluetoothAdapter.isEnabled());
		if (!this.mStopTest) {
			this.mBluetoothSwitch.setChecked(this.mBluetoothAdapter.isEnabled());
			this.mBluetoothSwitch.setText(this.mBluetoothAdapter.isEnabled()? this.mTextOn : this.mTextOff );
		}
	}
	
	public void transBluetoothDisEnable(Handler handler) {
		handler.sendEmptyMessage(BaseTestActivity.MSG_SETUP);
		boolean state = this.mBluetoothAdapter.isEnabled();
		if (state) {
			this.mBluetoothAdapter.disable();
		} else {
			this.mBluetoothAdapter.enable();
		}
		handler.sendEmptyMessage(BaseTestActivity.MSG_SHOW_DISMISS_DIALOG);
		do {
			SystemClock.sleep(1000);
		} while (state == this.mBluetoothAdapter.isEnabled());
		handler.sendEmptyMessage(BaseTestActivity.MSG_SHOW_DISMISS_DIALOG);
		handler.sendEmptyMessage(BaseTestActivity.MSG_TEAR_DOWN);
	}

}
