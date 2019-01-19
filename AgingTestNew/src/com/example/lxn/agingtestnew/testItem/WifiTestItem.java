package com.example.lxn.agingtestnew.testItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lxn.agingtestnew.CompletedActivity;
import com.example.lxn.agingtestnew.R;
import com.example.lxn.agingtestnew.SettingsActivity;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 18-12-22.
 ************************************************************************/


public class WifiTestItem extends AbstractBaseTestItem {
    private static final String TAG = "lxn-WifiTestItem";

    private WifiManager wifiManager;
    private TextView tv_oninfo;
    private TextView tv_offinfo;
    private int testCount  = 0;
    private int caseTest = 0;
    private int testTimes = 0;
    private int showOn = 1;
    private int showOff = 1;
    private boolean isOnOK = true;
    private boolean isOffOK = true;


    public WifiTestItem(int resLayoutId) {
        super(resLayoutId);
    }

    @Override
    public boolean execTest(Handler handler) {
        Log.d(TAG, "execTest");
        SystemClock.sleep(500);
        checkWiFi();
//        onStop();
        onTestEnd();
        return true;
    }

    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void initView(View view) {
        Log.d(TAG, "initView");
        tv_oninfo = (TextView) view.findViewById(R.id.tv_wifioninfo);
        tv_offinfo = (TextView) view.findViewById(R.id.tv_wifioffinfo);
        wifiManager = (WifiManager) this.mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        testCount = 0;
        testTimes = SettingsActivity.wifi_times;
        if (wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(false);
            caseTest = 2;
        } else {
            caseTest = 1;

        }
    }

    private void checkWiFi() {
        Log.d(TAG, "checkWiFi");
        while (2 * testTimes > testCount) {
            testCount++;
            switch (caseTest) {
                case 1 :
                    isOnOK = false;
                    caseTest = caseTest + 1;
                    wifiManager.setWifiEnabled(true);
                    long startOnTime = SystemClock.currentThreadTimeMillis();
                    long realOnTime = startOnTime;
                    while ((realOnTime - startOnTime) < 30 * 1000) {
                        Log.d(TAG, "on flag===" + wifiManager.getWifiState());
                        realOnTime = SystemClock.currentThreadTimeMillis();
                        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                            mHandler.sendEmptyMessage(1);
                        }
                        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                            mHandler.sendEmptyMessage(2);
                            isOnOK = true;
                            break;
                        }
                    }
                    break;
//                SystemClock.sleep(5000);
                case 2:
                    isOffOK = false;
                    caseTest = caseTest - 1;
                    wifiManager.setWifiEnabled(false);
                    long startOffTime = SystemClock.currentThreadTimeMillis();
                    long realOffTime = startOffTime;
                    while ((realOffTime - startOffTime) < 30 * 1000) {
                        realOffTime = SystemClock.currentThreadTimeMillis();
                        Log.d(TAG, "off flag===" + wifiManager.getWifiState());
                        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
                            mHandler.sendEmptyMessage(3);
                        }
                        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
                            isOffOK = true;
                            mHandler.sendEmptyMessage(4);
                            break;
                        }
                    }
                    break;

            }
            SystemClock.sleep(2000);
            if (!isOnOK) {
                mHandler.sendEmptyMessage(0);
            }
            if (!isOffOK) {
                mHandler.sendEmptyMessage(5);
            }
        }

    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            Log.d(TAG, "msg.what:" + msg.what);
            switch (msg.what) {
                case 0:
                    tv_oninfo.setText("wifi 开启错误!");
                    CompletedActivity.wifiStatus = "FAIL";
                    onStop();
                    onTestEnd();
                    break;
                case 1:
                    tv_oninfo.setText("wifi 正在开启");
                    break;
                case 2:
                    tv_oninfo.setText("第 " + (showOn++) +"次，wifi 开启ok");
                    CompletedActivity.wifiStatus = "PASS";
                    break;
                case 3:
                    tv_offinfo.setText("wifi 正在关闭");
                    break;
                case 4:
                    tv_offinfo.setText("第 " + (showOff++) +"次，wifi 关闭ok");
                    CompletedActivity.wifiStatus = "PASS";
                    break;
                case 5:
                    tv_offinfo.setText("wifi 关闭错误");
                    CompletedActivity.wifiStatus = "FAIL";
                    onStop();
                    onTestEnd();
                    break;
                case 6:
                    tv_offinfo.setText("wifi 错误");
                    CompletedActivity.wifiStatus = "FAIL";
                    tv_oninfo.setText("");
                    break;
            }
        }
    };

}
