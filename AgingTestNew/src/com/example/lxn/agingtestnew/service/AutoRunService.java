package com.example.lxn.agingtestnew.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.lxn.agingtestnew.BaseTestActivity;
import com.example.lxn.agingtestnew.CompletedActivity;
import com.example.lxn.agingtestnew.R;
import com.example.lxn.agingtestnew.testItem.TestItemHolder;
import com.example.lxn.agingtestnew.utills.ObjectWriterReader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com 
 *   > DATE: Date on 19-1-10.
 ************************************************************************/


public class AutoRunService extends Service{
    public static final String TAG = "lxn-AutoRunService";

    public static final String ACTION_AGING_TEST_NEXT = "action_aging_test_next";
    public static final String ACTION_AGING_TEST_STOP = "action_aging_test_stop";
    private static final int NOF_ID = 563;
    private static ExecutorService mExecutorService;
    private int mCurrentIndex = -1;
    private int mCurrentTimes = -1;
    private final int TEST_BY_TIMES = 3;
    private final int TEST_BY_DURATION = 4;

    private long mTestDuration;
    private boolean mTestNeedReboot;
    private int mTestTimes;
    private int mTestType;
    private boolean mTimeoutAndStop = false;
    private Timer mTimer;
    private String CHANNEL_ID = "AutoRunService_Notification";
    private String CHANNEL_NAME = "AutoRunService";

    private ArrayList<TestItemHolder> mHolderArrayList;
    private LocalBroadcastManager mLocalBroadcastManager;
    private NotificationManager mNotificationManager;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (AutoRunService.ACTION_AGING_TEST_NEXT.equals(intent.getAction())) {
                Log.d(AutoRunService.TAG, "onReceive next action at" + new Date().toString());
                if (AutoRunService.this.mTestType == TEST_BY_DURATION) {
                    if (AutoRunService.this.mTimeoutAndStop) {
                        AutoRunService.this.onCompleted();
                        return;
                    }
                    AutoRunService.this.mCurrentIndex = AutoRunService.this.mCurrentIndex + 1;
                    AutoRunService.this.mCurrentIndex = AutoRunService.this.mCurrentIndex % AutoRunService.this.mHolderArrayList.size();
                    AutoRunService.this.start();
                } else if (AutoRunService.this.mTestType == TEST_BY_TIMES) {
                    AutoRunService.this.mCurrentIndex = AutoRunService.this.mCurrentIndex + 1;
                    if (AutoRunService.this.mCurrentIndex < AutoRunService.this.mHolderArrayList.size()) {
                        AutoRunService.this.mCurrentIndex = AutoRunService.this.mCurrentIndex % AutoRunService.this.mHolderArrayList.size();
                        AutoRunService.this.start();
//                    } else if (AutoRunService.this.mTestNeedReboot) {
//                        AutoRunService.this.reboot();
                    } else {
                        AutoRunService.this.onCompleted();
                    }
                } else {
                    Log.d(AutoRunService.TAG, "Undefined test type");
                }
            } else if (AutoRunService.ACTION_AGING_TEST_STOP.equals(intent.getAction())) {
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
        this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_AGING_TEST_STOP);
        filter.addAction(ACTION_AGING_TEST_NEXT);
        registerReceiver(this.mReceiver , filter);
        this.mLocalBroadcastManager.registerReceiver(this.mReceiver, filter);
        mExecutorService = Executors.newCachedThreadPool();
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        throw new RuntimeException("Auto run service not implement onBind ");
    }

    public static void runTest(Runnable runnable) {
        Log.d(TAG, "runTest");
        if (!mExecutorService.isShutdown()) {
            mExecutorService.submit(runnable);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        CompletedActivity.endTestTime = "Complete time： " + getTimeTag();
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
        this.mLocalBroadcastManager.unregisterReceiver(this.mReceiver);
        mExecutorService.shutdown();
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
        super.onDestroy();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification createNotification() {

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(mChannel);
        }


        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("AgingTest");
        builder.setContentText("Auto Testing...");
        builder.setChannelId(CHANNEL_ID);
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
        //this.mRebootTimes = bundle.getInt("RebootTimes", -1);
        this.mTestDuration = bundle.getLong("TestDuration", -1);
        this.mTestNeedReboot = bundle.getBoolean("TestNeedReboot", false);
        boolean z = this.mTestNeedReboot ;//&& this.mRebootTimes > 0;
        this.mTestNeedReboot = z;

        if (!this.mTestNeedReboot) {
            ObjectWriterReader.clear();
        }
        if (this.mHolderArrayList == null || this.mHolderArrayList.size() == 0) {
            throw new RuntimeException("Error AutoRunService Holder list must not be null and size >0");
        }
        Log.d(TAG, "init\t {\n\tTestType: " + this.mTestType
                + "\n\tTestTimes: " + this.mTestTimes
                //+ "\n\tRebootTimes: " + this.mRebootTimes
                + "\n\tmTestDuration: " + this.mTestDuration
                + "\n\tmTestNeedReboot: " + this.mTestNeedReboot
                + "\n\tlistSize: " + this.mHolderArrayList.size() + "\n}");
        this.mCurrentIndex = 0;
        this.mCurrentTimes = 0;
        this.mTimeoutAndStop = false;
        if (this.mTestType == TEST_BY_DURATION) {
            this.mTimer = new Timer();
            this.mTimer.schedule(new TimerTask() {
                public void run() {
                    AutoRunService.this.mTimeoutAndStop = true;
                }
            }, this.mTestDuration);
        }
    }


    private String getTimeTag() {
        Calendar calendar = Calendar.getInstance();
        return String.format("%02d-%02d %02d:%02d:%02d", new Object[]{
                Integer.valueOf(calendar.get(Calendar.MONTH) + 1),
                Integer.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),
                Integer.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),
                Integer.valueOf(calendar.get(Calendar.MINUTE)),
                Integer.valueOf(calendar.get(Calendar.SECOND))});
    }

}

