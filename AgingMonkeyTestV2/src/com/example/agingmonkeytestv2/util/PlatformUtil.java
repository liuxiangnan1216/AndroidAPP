package com.example.agingmonkeytestv2.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;

import com.example.agingmonkeytestv2.FactoryApplication;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-8.
 ************************************************************************/


public class PlatformUtil {
    public static final String TAG = "PlatformUtil";

    public static String isPlatform() {
        if (FactoryApplication.isMediatek()) {
            return "mtk";
        }
        return "sprd";
    }

    public static String getSprd_batteryFileName() {
        String platform = isPlatform();
        Log.d(TAG, "isPlatform: " + platform);
        if (platform.equals("sprd")) {
            try {
                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("ls  /sys/devices/").getInputStream()));
                do {
                    line = bufferedReader.readLine();
                    if (line != null) {
                    }
                } while (!line.contains("sprd_battery"));
                return line;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}