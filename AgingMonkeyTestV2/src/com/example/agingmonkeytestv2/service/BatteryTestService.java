package com.example.agingmonkeytestv2.service;

import java.util.Date;

import com.example.agingmonkeytestv2.R;
import com.example.agingmonkeytestv2.util.BatteryUtil;
import com.example.agingmonkeytestv2.util.ShellUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-8.
 ************************************************************************/

public class BatteryTestService extends Service {

    private static final String TAG = "lxn-BatteryTestService";

    private static final String COMMAND_DUMP_BATTERY = "dumpsys battery";
    private static int MIN_VOLTAGE = 3800;
    public static final int NOF_ID = 8756;
    public static final String SERVICE_ALIVE_COMMAND = "dumpsys activity services | grep BatteryTestService";
    public static final String SERVICE_ALIVE_MARKER = "com.example.agingmonkeytestv2/.service.BatteryTestService";
    private int MAX_LEVEL = 80;
    private int MIN_LEVEL = 70;

    public IBinder onBind(Intent intent) {
        throw new RuntimeException("BatteryTestService not implemented onBind() yet");
    }

    //TODO

    BroadcastReceiver mBatteryChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                int status = intent.getIntExtra("status", 0);
                int level = intent.getIntExtra("level", 0);
                int plugged = intent.getIntExtra("plugged", 0);
                int temperature = intent.getIntExtra("temperature", 0);
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
                boolean isVolOk = BatteryTestService.checkBatteryVoltage();
                if (level >= BatteryTestService.this.MAX_LEVEL && isVolOk) {
                    boolean z;
                    boolean charged = BatteryUtil.disableCharge() < 0;
                    String str = BatteryTestService.TAG;
                    StringBuilder append = new StringBuilder().append("Battery level > ").append(BatteryTestService.this.MAX_LEVEL).append(" or Battery status is full , disable charge result:");
                    if (charged) {
                        z = false;
                    } else {
                        z = true;
                    }
                    Log.d(str, append.append(z).toString());
                } else if (level <= BatteryTestService.this.MIN_LEVEL || !isVolOk) {
                    Log.d(BatteryTestService.TAG, "Battery level <= " + BatteryTestService.this.MIN_LEVEL + " and current not charging , enable charge result:" + (BatteryUtil.enableCharge() >= 0));
                }
                Log.d(BatteryTestService.TAG, "onReceive battery info level: " + level + " plugged: " + acString + " status: " + statusString + " temperature: " + temperature);

            }
        }
    };
    public static boolean checkSelfAlive() {
        return checkAlive(SERVICE_ALIVE_COMMAND, SERVICE_ALIVE_MARKER);
    }


    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BATTERY_CHANGED");
        registerReceiver(this.mBatteryChangeReceiver, filter);
        Log.d(TAG, "BatteryTestService onCreate at " + new Date().toString());
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "BatteryTestService onStartCommand at " + new Date().toString());
        //NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(NOF_ID, createNotification());
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        BatteryUtil.enableCharge();
        stopForeground(true);
        unregisterReceiver(this.mBatteryChangeReceiver);
    }

    private Notification createNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(getString(R.string.test_name_battery));
        builder.setContentText("BatteryTest need to be stopped!!!");
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;
        return notification;
    }


    private static boolean checkBatteryVoltage() {
        ShellUtils.CommandResult result = ShellUtils.execCommand(COMMAND_DUMP_BATTERY, false, true);
        if (result.result == 0) {
            for (String str : result.successMsg.split("\r|\n")) {
                if (!(str == null || str.contains("charging") || !str.contains("voltage"))) {
                    Log.d(TAG, "get current voltage " + str);
                    String[] vol = str.trim().split(":");
                    if (vol.length >= 2) {
                        try {
                            if (Integer.parseInt(vol[1].trim()) > MIN_VOLTAGE) {
                                return true;
                            }
                            return false;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return false;
    }

    private static boolean checkAlive(String command, String aliveMarker) {
        ShellUtils.CommandResult commandResult = ShellUtils.execCommand(command, false, true);
        String msg = commandResult.successMsg;
        Log.d(TAG, "checkAlive: " + msg);
        if (commandResult.result < 0 || msg == null) {
            return false;
        }
        return msg.contains(aliveMarker);
    }

}
