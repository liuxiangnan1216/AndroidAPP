package com.example.lxn.agingtestnew.testItem;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
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


public class BluetoothTestItem extends AbstractBaseTestItem {
    private static final String TAG = "lxn-BluetoothTestItem";

    private BluetoothAdapter mBluetoothAdapter;
    private TextView mTvOn;
    private TextView mTvOff;
    private TextView mTvResult;

    private int errorCount = 9999999;
    private long startTime = 0;
    private int test_times = 0;
    private int testCount = 0;
    private int caseTest = 0;
    private int showOn = 1;
    private int showOff = 1;

    private boolean isOnOK = true;
    private boolean isOffOK = true;

    public BluetoothTestItem(int resLayoutId) {
        super(resLayoutId);
    }

    @Override
    public boolean execTest(Handler handler) {
        Log.d(TAG, "execTest");
        SystemClock.sleep(500);
        changeWiFiStatus();
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
        mTvOn = (TextView) view.findViewById(R.id.tv_bton);
        mTvOff = (TextView) view.findViewById(R.id.tv_btoff);
        mTvResult = (TextView) view.findViewById(R.id.tv_btresult);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        test_times = SettingsActivity.bt_times;
        Log.d(TAG, "initView test times====" + test_times);
        if (mBluetoothAdapter.isEnabled()) {
            caseTest = 2;
        } else {
            caseTest = 1;
        }
    }


    public void changeWiFiStatus() {
        Log.d(TAG, "changeWiFiStatus");
        while ( 2 * test_times > testCount) {
            testCount++;
            Log.d(TAG, "case test ====" + caseTest);
            switch (caseTest) {
                case 1:
                    isOnOK = false;
                    caseTest = caseTest + 1;
                    mBluetoothAdapter.enable();
                    long startOnTime = SystemClock.currentThreadTimeMillis();
                    long realOnTime = startOnTime;
                    while ((realOnTime - startOnTime) < 30 * 1000) {

                        Log.d(TAG, "on flag===" + mBluetoothAdapter.isEnabled());
                        realOnTime = SystemClock.currentThreadTimeMillis();
                        if (mBluetoothAdapter.isEnabled()) {
                            mHandler.sendEmptyMessage(2);
                            isOnOK = true;
                            break;
                        }
                    }
                    break;
                case 2:
                    isOffOK = false;
                    caseTest = caseTest - 1;
                    mBluetoothAdapter.disable();
                    long startOffTime = SystemClock.currentThreadTimeMillis();
                    long realOffTime = startOffTime;
                    while ((realOffTime - startOffTime) < 30 * 1000) {
                        realOffTime = SystemClock.currentThreadTimeMillis();
                        if (!mBluetoothAdapter.isEnabled()) {
                            mHandler.sendEmptyMessage(4);
                            isOffOK = true;
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
                    mTvOn.setText("BT 开启错误!");
                    CompletedActivity.bluetoothStatus = "FAIL";
                    onStop();
                    onTestEnd();
                    break;
                case 1:
                    mTvOn.setText("BT 正在开启");
                    break;
                case 2:
                    mTvOn.setText("第 " + (showOn++) +"次，BT 开启ok");
                    CompletedActivity.bluetoothStatus = "PASS";
                    break;
                case 3:
                    mTvOff.setText("BT 正在关闭");
                    break;
                case 4:
                    mTvOff.setText("第 " + (showOff++) +"次，BT 关闭ok");
                    CompletedActivity.bluetoothStatus = "PASS";
                    break;
                case 5:
                    mTvOff.setText("BT 关闭错误");
                    CompletedActivity.bluetoothStatus = "FAIL";
                    onStop();
                    onTestEnd();
                    break;
                case 6:
                    mTvResult.setText("BT 错误");
                    CompletedActivity.bluetoothStatus = "FAIL";
                    break;
            }
        }
    };

}
