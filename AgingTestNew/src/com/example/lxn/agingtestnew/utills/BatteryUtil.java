package com.example.lxn.agingtestnew.utills;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com 
 *   > DATE: Date on 19-1-7.
 ************************************************************************/



public class BatteryUtil {

    private static final String TAG = "BatteryUtil";
    private static final String MTK_DISABLE_CHARGE_CMD = "echo '0 1' > proc/mtk_battery_cmd/current_cmd";
    private static final String MTK_ENABLE_CHARGE_CMD = "echo '0 0' > proc/mtk_battery_cmd/current_cmd";

    public static int enableCharge() {
        String CMD;
        CMD = MTK_ENABLE_CHARGE_CMD;
        ShellUtils.CommandResult result = ShellUtils.execCommand(CMD, false);
        Log.d("EnableCharge", "CMD: " + CMD);
        Log.d("EnableCharge", "enableCharge: " + result.result);
        return result.result;
    }

    public static int disableCharge() {
        String CMD;
        CMD = MTK_DISABLE_CHARGE_CMD;
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

