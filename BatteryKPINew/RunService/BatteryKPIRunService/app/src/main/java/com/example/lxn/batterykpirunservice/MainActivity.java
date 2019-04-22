package com.example.lxn.batterykpirunservice;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.example.lxn.batterykpirunservice.broadcast.StandbyReceiver;
import com.example.lxn.batterykpirunservice.broadcast.TimeTickReceiver;
import com.example.lxn.batterykpirunservice.service.BatteryRunService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "lxn-BatteryRunService.MainActivity";

    private Context context;
    private Button runBtn;
    private static int startTime;
    private Button stopBtn;
//    public static Chronometer chronometer;

    private Intent batteryRunServiceIntent;

    private int countTimeTick = 0;


    StandbyReceiver standbyReceiver;

    TimeTickReceiver timeTickReceiver;

    public static AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        runBtn = (Button)findViewById(R.id.button);
        stopBtn = (Button)findViewById(R.id.stopBtn);
//        chronometer = (Chronometer) findViewById(R.id.chronometer);

        runBtn.setOnClickListener(this.mStartOnClickListener);
        stopBtn.setOnClickListener(this.mStopOnClickListener);

        batteryRunServiceIntent = new Intent();
        batteryRunServiceIntent.setClass(context, BatteryRunService.class);

        IntentFilter filter = new IntentFilter();
        filter.addAction("BatteryKPIAndroidP");

        standbyReceiver = new StandbyReceiver();
        registerReceiver(standbyReceiver, filter);

//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Intent.ACTION_TIME_TICK);
//
//        timeTickReceiver = new TimeTickReceiver();
//        registerReceiver(timeTickReceiver, intentFilter);

//        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

//        MainActivity.chronometer.setBase(SystemClock.elapsedRealtime());
//        MainActivity.chronometer.start();

    }

    private View.OnClickListener mStartOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            context.startService(batteryRunServiceIntent);
            context.startForegroundService(batteryRunServiceIntent);
            Log.d(TAG, "start services");
            startTime = (int) System.currentTimeMillis();
            Settings.System.putInt(context.getContentResolver(),"uiauto_startTime",  startTime);
            Log.d(TAG, "runMyUiautomator: startTime==" + startTime);

        }
    };

    private View.OnClickListener mStopOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "stop services");
            context.stopService(batteryRunServiceIntent);

        }
    };

 }
