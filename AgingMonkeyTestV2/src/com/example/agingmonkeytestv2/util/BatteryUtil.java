package com.example.agingmonkeytestv2.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.example.agingmonkeytestv2.FactoryApplication;

import android.util.Log;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-8.
 ************************************************************************/


public class BatteryUtil {

    private static final String TAG = "BatteryUtil";

    private static final String MTK_DISABLE_CHARGE_CMD = "echo '0 1' > proc/mtk_battery_cmd/current_cmd";
    private static final String MTK_ENABLE_CHARGE_CMD = "echo '0 0' > proc/mtk_battery_cmd/current_cmd";
    public static final String SPRD_CMD2 = "/power_supply/battery/stop_charge";
    public static String SPRD_DISABLE_CHARGE_CMD = null;
    public static final String SPRD_DISABLE_CMD = "echo '1' > sys/devices/";
    public static String SPRD_ENABLE_CHARGE_CMD = null;
    public static final String SPRD_ENABLE_CMD = "echo '0' > sys/devices/";
    public static final String SPRD_PUBLIC_DISABLE_CMD = "echo '1' > /sys/class/power_supply/battery/stop_charge";
    public static final String SPRD_PUBLIC_ENABLE_CMD = "echo '0' > /sys/class/power_supply/battery/stop_charge";

    public static int enableCharge() {
        String CMD;
        if (FactoryApplication.isMediatek()) {
            CMD = MTK_ENABLE_CHARGE_CMD;
        } else {
            CMD = SPRD_ENABLE_CHARGE_CMD;
        }
        ShellUtils.CommandResult result = ShellUtils.execCommand(CMD, false);
        Log.d("EnableCharge", "CMD: " + CMD);
        Log.d("EnableCharge", "enableCharge: " + result.result);
        return result.result;
    }

    public static int disableCharge() {
        String CMD;
        if (FactoryApplication.isMediatek()) {
            CMD = MTK_DISABLE_CHARGE_CMD;
        } else {
            CMD = SPRD_DISABLE_CHARGE_CMD;
        }
        ShellUtils.CommandResult result = ShellUtils.execCommand(CMD, false);
        Log.d("DisableCharge", "CMD: " + CMD);
        Log.d("DisableCharge", "disableCharge: " + result.result);
        return result.result;
    }

    public static int execCMD(String cmd) {
        String str;
        int result = -1;
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            result = process.waitFor();
            BufferedReader resultReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuffer resultString = new StringBuffer();
            StringBuffer errorString = new StringBuffer();
//TODO
            //while (true) {
                str = resultReader.readLine();
                if (str != null) {
                    resultString.append(str);
                } else {
                    errorString.append(str);
                }
            //}

            str = errorReader.readLine();
            if (str != null) {
                errorString.append(str);
            } else {
                Log.d("BatteryUtil", "exec cmd: " + cmd + "result: " + result);
                Log.d("BatteryUtil", "exec cmd: " + cmd + "resultString: " + resultString.toString());
                Log.d("BatteryUtil", "exec cmd: " + cmd + "errorString: " + errorString.toString());
                return result;
            }
//TODO
//            while (true) {
//                str = resultReader.readLine();
//                if (str != null) {
//                    resultString.append(str);
//                } else {
//                    while (true) {
//                        errorString.append(str);
//                    }
//                    Log.d(TAG, "exec cmd=:" + cmd + "result :" + result);
//                    Log.d("BatteryUtil", "exec cmd: " + cmd + "resultString: " + resultString.toString());
//                    Log.d("BatteryUtil", "exec cmd: " + cmd + "errorString: " + errorString.toString());
//                    return result;
//                }
//            }
//            if (str != null) {
//                errorString.append(str);
//            } else {
//                Log.d("BatteryUtil", "exec cmd: " + cmd + "result: " + result);
//                Log.d("BatteryUtil", "exec cmd: " + cmd + "resultString: " + resultString.toString());
//                Log.d("BatteryUtil", "exec cmd: " + cmd + "errorString: " + errorString.toString());
//                return result;
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;

    }

}

