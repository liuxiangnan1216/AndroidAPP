package com.example.lxn.batterykpirunservice.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.lxn.batterykpirunservice.utils.CMDUtils;
import com.example.lxn.batterykpirunservice.R;
import com.example.lxn.batterykpirunservice.broadcast.StandbyReceiver;
import com.example.lxn.batterykpirunservice.utils.Utils;

import java.util.ArrayList;
import java.util.Date;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 19-3-21.
 ************************************************************************/


public class BatteryRunService extends Service {

    private static final String TAG = "lxn-BatteryRunService";

    private String CHANNEL_ID = "BatteryRunService_Notification";
    private String CHANNEL_NAME = "BatteryRunService";
    public static final int NOF_ID = 1;

    private Handler mHandler = new Handler();
    private long timer = 0;
    private boolean isStopCount = false;


    Intent sefService = new Intent();

    StandbyReceiver standbyReceiver;

    private static String serviceName = "com.example.lxn.batterykpirunservice.service.BatteryRunService";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new RuntimeException("BatteryRunService not implemented onBind() yet");
    }


    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "BatteryRunService");
        new UiautomatorThread().start();

        sefService.setClass(this, BatteryRunService.class);

        IntentFilter filter = new IntentFilter();
        filter.addAction("BatteryKPIAndroidP");

        standbyReceiver = new StandbyReceiver();
        registerReceiver(standbyReceiver, filter);

    }




    /**
     * 判短服务是否开启
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null) {
            return false;
        }

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningServiceInfos = (ArrayList<ActivityManager.RunningServiceInfo>) activityManager.getRunningServices(30);
        for (int i = 0; i < runningServiceInfos.size(); i++) {
            if (runningServiceInfos.get(i).service.getClassName().toString().equals(ServiceName)) {
                return true;
            }
        }
        return false;

    }


    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "BatteryRunService onDestroy : " + isServiceRunning(this, serviceName));
//        if (!isServiceRunning(this, serviceName)) {
//
////            this.startForegroundService(sefService);
//        }
//        unregisterReceiver(standbyReceiver);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "BatteryTestService onStartCommand at " + new Date().toString());
        startForeground(1, createNotification());
        return START_STICKY;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification createNotification() {
        Log.d(TAG, "createNotification");

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(mChannel);
        }

        Notification.Builder builder = new Notification.Builder(this);
        builder.setPriority(Notification.PRIORITY_MIN);// 设置该通知优先级
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("BatteryKPI");
        builder.setContentText("BatteryKPI Testing...");
        builder.setChannelId(CHANNEL_ID);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;
        return notification;
    }


    /**
     * 运行uiautomator是个费时的操作，不应该放在主线程，因此另起一个线程运行
     */
    class UiautomatorThread extends Thread {
        @Override
        public void run() {
            super.run();

            Log.d(TAG, "RUNCMD: " + Utils.RUNCMD);
            // String command = generateCommand("com.example.mytestcast", "TestOne", "demo");
//            String command = generateCommand("com.example.android.testing.uiautomator.BatteryKPIAndroidP", "Mytest");

            CMDUtils.CMD_Result rs = CMDUtils.runCMD(Utils.RUNCMD, true, true);
            //rs 是命令执行返回的Log
            Log.d(TAG, "run: " + rs.error + "-------" + rs.success);
        }

//        /**
//         * 生成命令
//         *
//         * @param pkgName 包名
//         * @param clsName 类名
//         * @return
//         */
//        private String generateCommand(String pkgName, String clsName) {
//            String command = "am instrument  --user 0 -w -r   -e debug false -e class " + pkgName + "." + clsName + " " + pkgName + ".test/android.support.test.runner.AndroidJUnitRunner";
//            Log.e("test1: ", command);
//            return command;
//        }
//
//        /**
//         * 生成命令
//         *
//         * @param pkgName 包名
//         * @param clsName 类名
//         * @param mtdName 方法名
//         * @return
//         */
//        public String generateCommand(String pkgName, String clsName, String mtdName) {
//            String command = "am instrument  --user 0 -w -r   -e debug false -e class " + pkgName + "." + clsName + "#" + mtdName + " " + pkgName + ".test/android.support.test.runner.AndroidJUnitRunner";
//            Log.e("test3: ", command);
//            return command;
//        }
    }


}
