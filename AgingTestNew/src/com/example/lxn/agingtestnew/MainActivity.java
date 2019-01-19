package com.example.lxn.agingtestnew;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lxn.agingtestnew.service.AutoRunService;
import com.example.lxn.agingtestnew.service.BatteryTestService;
import java.util.List;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 18-12-21.
 ************************************************************************/

public class MainActivity extends Activity {
    private static final String TAG = "lxn-Aging-MainActivity";

    private Context context;
    private Button mManuStartBtn;
    private Button mSettingBtn;
    private Button mAutoStartBtn;
    private Button mHelpBtn;
    private Button mCompleteBtn;
    private Button mStopServiceBtn;

    private TextView tv_battery;
    private int batteryL;
    private int chargePlug;

    private Intent mBatteryServiceIntent;
    private Intent mAutoRunServiceIntent;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        this.mBatteryServiceIntent = new Intent();
        this.mBatteryServiceIntent.setClass(context, BatteryTestService.class);
        if (!BatteryTestService.checkSelfAlive()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.context.startForegroundService(this.mBatteryServiceIntent);
            } else {
                this.context.startService(this.mBatteryServiceIntent);
            }
        }

//
        BatteryManager batteryManager = (BatteryManager)getSystemService(BATTERY_SERVICE);
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        batteryL = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Log.d(TAG, "battery======" + batteryL);
        chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        Log.d(TAG, " chargePlug====" + chargePlug);


        if (false) {//(batteryL < 50){//TODO
            Log.d(TAG, "System.exit");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning");
            builder.setMessage("电池电量小于50%，请确保电量大于50%,点击确定退出！");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);
                }

            });
            builder.create().show();
        }


        mSettingBtn = (Button) findViewById(R.id.button_setting);
        mManuStartBtn = (Button) findViewById(R.id.button_mau);
        mHelpBtn = (Button) findViewById(R.id.button_help);
        mCompleteBtn = (Button) findViewById(R.id.button_complete);
        tv_battery = (TextView) findViewById(R.id.tv_maininfo);
        mStopServiceBtn = (Button) findViewById(R.id.button_stopservice);



        if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
            tv_battery.setText("USB 充电中！\n当前电量: " + batteryL + "%");
        } else if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
            tv_battery.setText("充电器充电中！\n当前电量:" + batteryL +"%");
        } else {
            tv_battery.setText("未链接充电器！\n当前电量:" + batteryL +"%");
        }


        mManuStartBtn.setOnClickListener(this.mStartOnClickListener);
        mCompleteBtn.setOnClickListener(this.mCompleteOnClickListener);
        mHelpBtn.setOnClickListener(this.mHelpOnClickListener);
        mSettingBtn.setOnClickListener(this.mSettingsOnClickListener);
        mStopServiceBtn.setOnClickListener(this.mStopServiceOnClickListener);

    }



    /**
     *
     */
    protected void onResume() {
        super.onResume();
        if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
            tv_battery.setText("USB 充电中！\n当前电量: " + batteryL + "%");
        } else if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
            tv_battery.setText("充电器充电中！\n当前电量:" + batteryL +"%");
        } else {
            tv_battery.setText("未链接充电器！\n当前电量:" + batteryL +"%");
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    protected void onPause(){
        super.onPause();
        Log.d(TAG, "onPause");
    }



    private View.OnClickListener mSettingsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "Settings");
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
    };

    /**
     *手动单项测试
     */
    private View.OnClickListener mStartOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.d(TAG, "start");
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ManuTestActivity.class);
            startActivity(intent);
        }
    };


    /***
     *
     */
    private View.OnClickListener mCompleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, CompletedActivity.class);
            startActivity(intent);
        }
    };


    /**
     *
     */
    private View.OnClickListener mHelpOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, HelpActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener mStopServiceOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            stopBatteryService();
            stopAutoRunService();
        }
    };


    /**
     * stop battery test
     */
    private void stopBatteryService(){
        //if (BatteryTestService.checkSelfAlive()) {
            this.context.stopService(this.mBatteryServiceIntent);
            Log.d(TAG, "stop battery service!");
        //}
    }

    private void stopAutoRunService() {
        this.mAutoRunServiceIntent = new Intent();
        this.mAutoRunServiceIntent.setClass(context, AutoRunService.class);
        if (isAutoServiveRunning(context)) {
            this.context.stopService(this.mAutoRunServiceIntent);
        }
    }


    private static Boolean isAutoServiveRunning(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = activityManager.getRunningTasks(Integer.MAX_VALUE);
        for (ActivityManager.RunningTaskInfo info : list){
            if (info.baseActivity.getPackageName().equals("com.example.lxn.agingtestnew")) {
                return true;
            }

        }
        return false;
    }


}
