package com.example.lxn.agingtestnew.testItem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lxn.agingtestnew.R;
import com.example.lxn.agingtestnew.service.BatteryTestService;
import com.example.lxn.agingtestnew.view.CircleProgressView;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com 
 *   > DATE: Date on 19-1-7.
 ************************************************************************/


public class ChargingTestItem extends AbstractBaseTestItem{

    private static final String TAG = "lxn-ChargingTestItem";

    private CircleProgressView mProgressView;
    private int mChargingLevel = 70;
    private Intent mServiceIntent;
    private TextView mTextView;


    BroadcastReceiver mBatteryChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                int status = intent.getIntExtra("status", 0);
                int level = intent.getIntExtra("level", 0);//电池电量
                int plugged = intent.getIntExtra("plugged", 0);//充电类型

                Log.d(TAG, "onReceive battery info level: " + level );
                CircleProgressView circleProView = ChargingTestItem.this.mProgressView;
                circleProView.setProgress(true, level);

            }
        }
    };

    public ChargingTestItem(int resLayoutId) {
        super(resLayoutId);
    }

    public boolean execTest(Handler handler) {
//        onStop();
        onTestEnd();
        return true;
    }

    protected void initView(View view) {
        this.mProgressView = (CircleProgressView) view.findViewById(R.id.progress_charging);
        this.mTextView = (TextView) view.findViewById(R.id.tv_charging);
        this.mServiceIntent = new Intent();
        this.mServiceIntent.setClass(this.mContext, BatteryTestService.class);
        startCharging();
    }

    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BATTERY_CHANGED");
        this.mContext.registerReceiver(this.mBatteryChangeReceiver, filter);


    }

    public void onStop() {
        super.onStop();
        this.mContext.unregisterReceiver(this.mBatteryChangeReceiver);
    }

    private void startCharging() {
        if (!BatteryTestService.checkSelfAlive()) {
//            this.mContext.startService(this.mServiceIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(TAG, "this mContext===" + this.mContext + "\nmServiceInetent===" + this.mServiceIntent);
                this.mContext.startForegroundService(this.mServiceIntent);
            } else {
                this.mContext.startService(this.mServiceIntent);
            }
        }
    }

    private void stopCharging() {
        if (BatteryTestService.checkSelfAlive()) {
            this.mContext.stopService(this.mServiceIntent);
        }
    }

}
