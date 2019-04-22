package com.example.lxn.batterykpirunservice.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lxn.batterykpirunservice.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 19-4-2.
 ************************************************************************/


public class KeepAliveService extends Service {

    private static final String TAG = "lxn-*KeepAliveService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new RuntimeException("BatteryRunService not implemented onBind() yet");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        thread.start();
        return START_STICKY;
    }


    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Log.d(TAG, "service keep alive service :" + System.currentTimeMillis());
                    boolean isServiceAlive = BatteryRunService.isServiceRunning(KeepAliveService.this, "com.example.lxn.batterykpirunservice.service.BatteryRunService");

                    if (!isServiceAlive) {

                        Intent serviceIntent = new Intent(KeepAliveService.this, BatteryRunService.class);
                        serviceIntent.setClass(KeepAliveService.this, BatteryRunService.class);
                        startForegroundService(serviceIntent);
                    }
                }
            };

            timer.schedule(timerTask, 0, 500);
        }
    });
}
