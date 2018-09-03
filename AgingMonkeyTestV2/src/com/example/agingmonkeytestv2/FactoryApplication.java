package com.example.agingmonkeytestv2;

import java.io.IOException;

import com.example.agingmonkeytestv2.util.ShellUtils;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-8.
 ************************************************************************/


public class FactoryApplication extends Application{

    public static final String AUDIO_RESOURCE_NAME = "a";
    public static final String CMD_START_MTKLOG = "am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name start --ei cmd_target 23";
    public static final String CMD_STOP_MTKLOG = "am broadcast -a com.mediatek.mtklogger.ADB_CMD -e cmd_name stop --ei cmd_target 23";
    public static final String TAG = "lxn-FactoryApplication";
    private static boolean isNeedMtklog = true;


    public void onTerminate() {
        super.onTerminate();
        startMtkLog();
    }
    
    public static boolean isMediatek() {
        String hardware = Build.HARDWARE;
        Log.d(TAG, "Mediatek :  ro.hardware = " + hardware);
        return hardware.contains("mt");
    }

    public static void startMtkLog() {
        if (isNeedMtklog && isMediatek()) {
            runAdbCommand(CMD_START_MTKLOG);
        }
    }

    public static void stopMtkLog() {
        if (isMediatek()) {
            runAdbCommand(CMD_STOP_MTKLOG);
            ShellUtils.execCommand(CMD_STOP_MTKLOG, false);
        }
    }

    public static boolean isNeedAudioTestItem(Context context) {
        int resId = context.getResources().getIdentifier(AUDIO_RESOURCE_NAME, "raw", context.getPackageName());
        Log.d(TAG, "isNeedAudioTestItem res id " + resId);
        return resId != 0;
    }

    private static void runAdbCommand(String cmd) {
        Log.d(TAG, "run Adb Command: " + cmd);
        try {
            Runtime.getRuntime().exec(CMD_START_MTKLOG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}