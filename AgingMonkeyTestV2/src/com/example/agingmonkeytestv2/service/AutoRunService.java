package com.example.agingmonkeytestv2.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.agingmonkeytestv2.BaseTestActivity;
import com.example.agingmonkeytestv2.CompletedActivity;
import com.example.agingmonkeytestv2.R;
import com.example.agingmonkeytestv2.testitem.TestItemHolder;
import com.example.agingmonkeytestv2.util.ObjectWriterReader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-8.
 ************************************************************************/


public class AutoRunService extends Service{
    public static final String ACTION_FACTORY_TEST_NEXT = "action_factory_test_next";
    public static final String ACTION_FACTORY_TEST_STOP = "action_factory_test_stop";
    private static final int NOF_ID = 563;
    public static final String TAG = "lxn-AutoRunService";
    private static ExecutorService mExecutorService;
    private int mCurrentIndex = -1;
    private int mCurrentTimes = -1;

    private long mTestDuration;
    private boolean mTestNeedReboot;
    private int mTestTimes;
    private int mTestType;
    private boolean mTimeoutAndStop = false;
    private Timer mTimer;

    private ArrayList<TestItemHolder> mHolderArrayList;
    //private LocalBroadcastManager mLocalBroadcastManager;
    private  BroadcastReceiver mBroadcastReceiver;
    private NotificationManager mNotificationManager;
    private int mRebootTimes;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (AutoRunService.ACTION_FACTORY_TEST_NEXT.equals(intent.getAction())) {
                Log.d(AutoRunService.TAG, "onReceive next action at" + new Date().toString());
                if (AutoRunService.this.mTestType == 4) {
                    if (AutoRunService.this.mTimeoutAndStop) {
                        AutoRunService.this.onCompleted();
                        return;
                    }
                    AutoRunService.this.mCurrentIndex = AutoRunService.this.mCurrentIndex + 1;
                    AutoRunService.this.mCurrentIndex = AutoRunService.this.mCurrentIndex % AutoRunService.this.mHolderArrayList.size();
                    AutoRunService.this.start();
                } else if (AutoRunService.this.mTestType == 3) {
                    AutoRunService.this.mCurrentIndex = AutoRunService.this.mCurrentIndex + 1;
                    if (AutoRunService.this.mCurrentIndex < AutoRunService.this.mHolderArrayList.size()) {
                        AutoRunService.this.mCurrentIndex = AutoRunService.this.mCurrentIndex % AutoRunService.this.mHolderArrayList.size();
                        AutoRunService.this.start();
                    } else if (AutoRunService.this.mTestNeedReboot) {
                        AutoRunService.this.reboot();
                    } else {
                        AutoRunService.this.onCompleted();
                    }
                } else {
                    Log.d(AutoRunService.TAG, "Undefined test type");
                }
            } else if (AutoRunService.ACTION_FACTORY_TEST_STOP.equals(intent.getAction())) {
                Log.d(AutoRunService.TAG, "onReceive stop action at" + new Date().toString());
                if (AutoRunService.this.mTimer != null) {
                    AutoRunService.this.mTimer.cancel();
                    AutoRunService.this.mTimer = null;
                }
                AutoRunService.this.stopSelf();
            }
        }
    };


    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        this.mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_FACTORY_TEST_STOP);
        filter.addAction(ACTION_FACTORY_TEST_NEXT);
        registerReceiver(this.mReceiver , filter);
        //this.mLocalBroadcastManager.registerReceiver(this.mReceiver, filter);
        mExecutorService = Executors.newCachedThreadPool();
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new RuntimeException("Auto run service not implement onBind ");
    }

    public static void runTest(Runnable runnable) {
    	Log.d(TAG, "runTest");
        if (!mExecutorService.isShutdown()) {
            mExecutorService.submit(runnable);
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
    	Log.d(TAG, "onStartCommand");
        startForeground(NOF_ID, createNotification());
        init(intent);
        start();
        return flags;//TODO
    }

    private void start() {
    	Log.d(TAG, "start");
        Intent intent = new Intent();
        TestItemHolder holder = (TestItemHolder) this.mHolderArrayList.get(this.mCurrentIndex);
        holder.setType(this.mTestType);
        holder.setTimes(this.mTestTimes);
        intent.putExtras(holder.createBundle());
        intent.setClass(this, BaseTestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Log.d(TAG, "start new test activity");
    }

    private void onCompleted() {
        stopSelf();
        Intent activityCompleted = new Intent(this, CompletedActivity.class);
        activityCompleted.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activityCompleted.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(CompletedActivity.COMPLETE_MESSAGE, generateCompleteMessage());
        activityCompleted.putExtras(bundle);
        startActivity(activityCompleted);
    }


    private String generateCompleteMessage() {
        return String.format(getString(R.string.auto_run_complete_string_format), new Object[]{Integer.valueOf(this.mTestType)});
    }

    public void onDestroy() {
        stopForeground(true);
        this.mNotificationManager.cancel(NOF_ID);
        unregisterReceiver(this.mReceiver);
        //this.mLocalBroadcastManager.unregisterReceiver(this.mReceiver);
        mExecutorService.shutdown();
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
        super.onDestroy();
    }



    private Notification createNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("Factory");
        builder.setContentText("Auto Testing...");
        return builder.build();
    }

    private void init(Intent intent) {
        if (intent == null) {
            throw new RuntimeException("Error AutoRunService intent must not be null");
        }
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            throw new RuntimeException("Error AutoRunService bundle must not be null");
        }
        this.mHolderArrayList = bundle.getParcelableArrayList("ItemHolderList");
        this.mTestType = bundle.getInt("TestType", -1);
        this.mTestTimes = bundle.getInt("TestTimes", -1);
        this.mRebootTimes = bundle.getInt("RebootTimes", -1);
        this.mTestDuration = bundle.getLong("TestDuration", -1);
        this.mTestNeedReboot = bundle.getBoolean("TestNeedReboot", false);
        boolean z = this.mTestNeedReboot && this.mRebootTimes > 0;
        this.mTestNeedReboot = z;
        
        if (!this.mTestNeedReboot) {
            ObjectWriterReader.clear();
        }
        if (this.mHolderArrayList == null || this.mHolderArrayList.size() == 0) {
            throw new RuntimeException("Error AutoRunService Holder list must not be null and size >0");
        }
        Log.d(TAG, "init\t {\n\tTestType: " + this.mTestType 
        		+ "\n\tTestTimes: " + this.mTestTimes 
        		+ "\n\tRebootTimes: " + this.mRebootTimes 
        		+ "\n\tmTestDuration: " + this.mTestDuration 
        		+ "\n\tmTestNeedReboot: " + this.mTestNeedReboot 
        		+ "\n\tlistSize: " + this.mHolderArrayList.size() + "\n}");
        this.mCurrentIndex = 0;
        this.mCurrentTimes = 0;
        this.mTimeoutAndStop = false;
        if (this.mTestType == 4) {
            this.mTimer = new Timer();
            this.mTimer.schedule(new TimerTask() {
                public void run() {
                    AutoRunService.this.mTimeoutAndStop = true;
                }
            }, this.mTestDuration);
        }
    }

    private void reboot() {
        updateRebootSettings();
        Log.d(TAG, "Factory Test Need Reboot");
        ((PowerManager) getSystemService(POWER_SERVICE)).reboot("FactoryTestNeedReboot");
    }

    private void updateRebootSettings() {
        Bundle bundle = new Bundle();
        this.mRebootTimes--;
        bundle.putParcelableArrayList("ItemHolderList", this.mHolderArrayList);
        bundle.putBoolean("TestNeedReboot", this.mTestNeedReboot);
        bundle.putInt("RebootTimes", this.mRebootTimes);
        bundle.putInt("TestType", this.mTestType);
        bundle.putLong("TestDuration", this.mTestDuration);
        bundle.putInt("TestTimes", this.mTestTimes);
        ObjectWriterReader.write(bundle);
    }

}
