package com.example.agingmonkeytestv2.testitem;

import com.example.agingmonkeytestv2.R;
import com.example.agingmonkeytestv2.service.BatteryTestService;
import com.example.agingmonkeytestv2.view.CircleProgressView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-17.
 ************************************************************************/


public class BatteryTestItem extends AbstractBaseTestItem {
	
    private CircleProgressView mProgressView;
    private Intent mServiceIntent;
    private Button mStartBt;
    private Button mStopBt;
    
    
    BroadcastReceiver mBatteryChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean z = false;
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                int status = intent.getIntExtra("status", 0);
                int level = intent.getIntExtra("level", 0);
                int plugged = intent.getIntExtra("plugged", 0);
                String statusString = "";
                switch (status) {
                    case 1:
                        statusString = "unknown";
                        break;
                    case 2:
                        statusString = "charging";
                        break;
                    case 3:
                        statusString = "discharging";
                        break;
                    case 4:
                        statusString = "no charging";
                        break;
                    case 5:
                        statusString = "full";
                        break;
                    default:
                        statusString = "other";
                        break;
                }
                String acString = "";
                switch (plugged) {
                    case 1:
                        acString = "plugged ac";
                        break;
                    case 2:
                        acString = "plugged usb";
                        break;
                    default:
                        acString = "other";
                        break;
                }
                Log.d(BatteryTestItem.this.TAG, "onReceive battery info level: " + level + " plugged: " + acString + " status: " + statusString);
                CircleProgressView circleProView = BatteryTestItem.this.mProgressView;
                if (plugged > 0) {
                    z = true;
                }
                circleProView.setProgress(z, level);
            }
        }
    };

    public BatteryTestItem(int resLayoutId) {
        super(resLayoutId);
    }

    public boolean execTest(Handler handler) {
        return false;
    }

    protected void initView(View view) {
        this.mProgressView = (CircleProgressView) view.findViewById(R.id.progress);
        this.mStartBt = (Button) view.findViewById(R.id.battery_start);
        this.mServiceIntent = new Intent();
        this.mServiceIntent.setClass(this.mContext, BatteryTestService.class);
        this.mStartBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BatteryTestItem.this.start();
            }
        });
        this.mStopBt = (Button) view.findViewById(R.id.battery_stop);
        this.mStopBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BatteryTestItem.this.stop();
            }
        });
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

    private void start() {
        if (!BatteryTestService.checkSelfAlive()) {
            this.mContext.startService(this.mServiceIntent);
        }
    }

    private void stop() {
        if (BatteryTestService.checkSelfAlive()) {
            this.mContext.stopService(this.mServiceIntent);
        }
    }
}
