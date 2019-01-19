package com.example.lxn.agingtestnew.testItem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lxn.agingtestnew.CompletedActivity;
import com.example.lxn.agingtestnew.R;
import com.example.lxn.agingtestnew.SettingsActivity;
import com.example.lxn.agingtestnew.service.BatteryTestService;
import com.example.lxn.agingtestnew.view.CircleProgressView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 18-12-22.
 ************************************************************************/


public class BatteryTestItem extends AbstractBaseTestItem{

    private static final String TAG = "lxn-BatteryTestItem";


    private CircleProgressView mProgressView;
    private Intent mServiceIntent;
    private TextView mTextView;
    private String plugged_type = "未链接有效充电器";
    private String str_mv = " ";
    private int test_times = 0;
    private static final String CHARGER_CURRENT_NOW = "/sys/class/power_supply/battery/BatteryAverageCurrent";


    BroadcastReceiver mBatteryChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean z = false;
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                int status = intent.getIntExtra("status", 0);
                int level = intent.getIntExtra("level", 0);//电池电量
                int plugged = intent.getIntExtra("plugged", 0);//充电类型
                int voltage = intent.getIntExtra("voltage", 0);//电池电压
                int temp = intent.getIntExtra("temperature", 0);//温度
                int health = intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN);//健康状态

                String health_status = "unknown";
                switch (health) {
                    case BatteryManager.BATTERY_HEALTH_COLD:
                        health_status = "cold";
                        break;
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        health_status = "dead";
                        break;
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        health_status = "good";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        health_status = "over voltage";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        health_status = "overheat";
                        break;
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        health_status = "unknown";
                        break;
                    case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                        health_status = "unspecified failure";
                        break;
                }

                if (plugged == BatteryManager.BATTERY_PLUGGED_AC) {
                    plugged_type = "充电器充电中！";
                } else if (plugged == BatteryManager.BATTERY_PLUGGED_USB) {
                    plugged_type = "USB 充电中！";
                }
                Log.d(BatteryTestItem.this.TAG, "onReceive battery info level: " + level );
                CircleProgressView circleProView = BatteryTestItem.this.mProgressView;
                if (plugged > 0) {
                    z = true;
                }
                circleProView.setProgress(z, level);

                if (isQuickCharge()) {
                    str_mv = "快充充电中！";
                } else {
                    str_mv = "非快充充电中！";
                }

                String Bat_status = "电池电压: " + voltage
                        + "mV"+ "\n电池温度: " + (temp / 10) + "℃"
                        + "\n健康状态: " + health_status
                        + "\n" + plugged_type
                        + "\n" + str_mv;
                mTextView.setText(Bat_status);
            }
        }
    };

    public BatteryTestItem(int resLayoutId) {
        super(resLayoutId);
    }

    public boolean execTest(Handler handler) {
        SystemClock.sleep(2000);
        CompletedActivity.batteryStatus = "PASS";
//        onStop();
        onTestEnd();
        return true;
    }

    protected void initView(View view) {
        this.mProgressView = (CircleProgressView) view.findViewById(R.id.progress);
        this.mTextView = (TextView) view.findViewById(R.id.textView_batterystatus);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BATTERY_CHANGED");
        this.mContext.registerReceiver(this.mBatteryChangeReceiver, filter);

        test_times = SettingsActivity.battery_times;
        Log.d(TAG, "initView test_times====" + test_times);


//        start();


    }

    private void start() {
        if (!BatteryTestService.checkSelfAlive()) {
//            this.mContext.startService(this.mServiceIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.mContext.startForegroundService(this.mServiceIntent);
            } else {
                this.mContext.startService(this.mServiceIntent);
            }
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onStop() {
        super.onStop();
//        this.mContext.unregisterReceiver(this.mBatteryChangeReceiver);
    }


    private boolean isQuickCharge() {
        try {
            Class systemProperties = Class.forName("android.os.SystemProperties");
            Method get = systemProperties.getDeclaredMethod("get", String.class);
            String filePath = "/sys/class/power_supply/battery/device/FG_Battery_CurrentConsumption";
            Math.round(getMeanCurrentVal(filePath, 5, 0) / 10.0f);
            Log.d(TAG, "555555" + Math.round(getMeanCurrentVal(filePath, 5, 0) / 10.0f));

            int battertVol = readFile("/sys/class/power_supply/battery/batt_vol", 0);
            Log.d(TAG, "666666" + battertVol);
            if (battertVol >= 6.5 * 1000 * 1000) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取平均电流值
     * 获取 filePath 文件 totalCount 次数的平均值，每次采样间隔 intervalMs 时间
     */
    private float getMeanCurrentVal(String filePath, int totalCount, int intervalMs) {
        float meanVal = 0.0f;
        if (totalCount <= 0) {
            return 0.0f;
        }
        for (int i = 0; i < totalCount; i++) {
            try {
                float f = Float.valueOf(readFile(filePath, 0));
                meanVal += f / totalCount;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (intervalMs <= 0) {
                continue;
            }
            try {
                Thread.sleep(intervalMs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return meanVal;
    }

    private int readFile(String path, int defaultValue) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(
                    path));
            int i = Integer.parseInt(bufferedReader.readLine(), 10);
            bufferedReader.close();
            return i;
        } catch (Exception localException) {
        }
        return defaultValue;
    }

}
