package com.example.agingmonkeytestv2.testitem;

import com.example.agingmonkeytestv2.BaseTestActivity;
import com.example.agingmonkeytestv2.R;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-9.
 ************************************************************************/


public class WiFiTestItem extends AbstractBaseTestItem {
    private long TEST_DURATION = 2000;
    private String mTextOff;
    private String mTextOn;
    private WifiManager mWifiManager;
    private Switch mWifiSwitch;

    public WiFiTestItem(int resLayoutId) {
        super(resLayoutId);
    }

    public boolean execTest(Handler handler) {
        transWifiDisEnable(handler);
        SystemClock.sleep(this.TEST_DURATION);
        return true;
    }

    protected void initView(View view) {
    	Log.d("lxn--WiFiTestItem", "initView");
        this.mWifiSwitch = (Switch) view.findViewById(R.id.switch_wifi);
        this.mWifiManager = (WifiManager) this.mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        this.mTextOff = this.mContext.getString(R.string.text_wifi_off);
        this.mTextOn = this.mContext.getString(R.string.text_wifi_on);
    }

    public void setUp() {
        super.setUp();
        Log.d("lxn--WiFiTestItem", "setUp");
        changeWifiSwitchState();
    }

    public void tearDown() {
        super.tearDown();
        Log.d("lxn--WiFiTestItem", "tearDown");
        changeWifiSwitchState();
    }

    private void changeWifiSwitchState() {
    	Log.d("lxn--WiFiTestItem", "changeWifiSwitchState");
        boolean enabled = this.mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
        if (!this.mStopTest) {
            Log.d(this.TAG, "mTextOff");
            this.mWifiSwitch.setChecked(enabled);
            this.mWifiSwitch.setText(enabled ? this.mTextOn : this.mTextOff);
        }
    }

    private void transWifiDisEnable(Handler handler) {
    	handler.sendEmptyMessage(BaseTestActivity.MSG_SETUP);
        Log.d("lxn--WiFiTestItem", "transWifiDisEnable");
        boolean enabled = this.mWifiManager.isWifiEnabled();
        if (enabled) {
            this.mWifiManager.setWifiEnabled(false);
        } else {
            this.mWifiManager.setWifiEnabled(true);
        }
        handler.sendEmptyMessage(BaseTestActivity.MSG_SHOW_DISMISS_DIALOG);
        do {
            SystemClock.sleep(1000);
        } while (enabled == this.mWifiManager.isWifiEnabled());
        handler.sendEmptyMessage(BaseTestActivity.MSG_SHOW_DISMISS_DIALOG);
        handler.sendEmptyMessage(BaseTestActivity.MSG_TEAR_DOWN);
    }
}

