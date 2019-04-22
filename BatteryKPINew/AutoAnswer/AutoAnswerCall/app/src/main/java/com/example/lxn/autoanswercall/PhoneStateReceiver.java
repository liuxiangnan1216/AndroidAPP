package com.example.lxn.autoanswercall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.Context.TELECOM_SERVICE;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 19-3-8.
 ************************************************************************/


public class PhoneStateReceiver extends BroadcastReceiver {
    private static final String TAG = "lxn--PhoneStateReceiver";
    public static String UTDPhoneNum;
    public static float call_count;
    public static float current_call_count;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "PhoneStateReceiver action: " + action);

        String resultData = this.getResultData();
        Log.d(TAG, "PhoneStateReceiver getResultData: " + resultData);

        UTDPhoneNum = Settings.System.getString(context.getContentResolver(), "UTDPhoneNum");
        call_count = 0.0f;
        current_call_count = 0.0f;
        try {
            call_count = Settings.System.getFloat(context.getContentResolver(), "call_count");
            current_call_count = Settings.System.getFloat(context.getContentResolver(), "current_call_count");
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "PhoneStateReceiver: onReceive UTDPhoneNum" + UTDPhoneNum);
        Log.d(TAG, "PhoneStateReceiver: onReceive call_count" + call_count);
        Log.d(TAG, "PhoneStateReceiver: onReceive current_call_count" + current_call_count);

        if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // 去电，可以用定时挂断
            // 双卡的手机可能不走这个Action
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d(TAG, "PhoneStateReceiver EXTRA_PHONE_NUMBER: " + phoneNumber);
        } else {
            // 来电去电都会走
            // 获取当前电话状态
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            Log.d(TAG, "PhoneStateReceiver onReceive state: " + state);
            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
                // 获取电话号码
                String extraIncomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                //自动接电话
                Log.d(TAG, "PhoneStateReceiver: into state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)");
                Log.d(TAG, "PhoneStateReceiver: extraIncomingNumber: " + extraIncomingNumber);
                Log.d(TAG, "PhoneStateReceiver: UTDPhoneNum" + UTDPhoneNum);
                Log.d(TAG, "PhoneStateReceiver: call_count" + call_count);
                Log.d(TAG, "PhoneStateReceiver: current_call_count" + current_call_count);
                TelecomManager telecomManager = (TelecomManager) context.getSystemService(TELECOM_SERVICE);
                Method method = null;
                if (extraIncomingNumber.equals(UTDPhoneNum) && current_call_count < call_count) {
                    try {
                        Log.d(TAG, "PhoneStateReceiver: into try");
                        method = Class.forName("android.telecom.TelecomManager").getMethod("acceptRingingCall");
                        method.invoke(telecomManager);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    current_call_count = current_call_count + 0.5f;
                    Settings.System.putFloat(context.getContentResolver(),"current_call_count",  current_call_count);
                }



               /* if (extraIncomingNumber.equals(utdPhoneNum)&&current_call_count<call_count) {
                    Log.d(TAG, "PhoneStateReceiver: into autoanswer");
                    TelecomManager telecomManager = (TelecomManager) context.getSystemService(TELECOM_SERVICE);
                    Method method = null;
                    try {
                        Log.d(TAG, "PhoneStateReceiver: into try");
                        method = Class.forName("android.telecom.TelecomManager").getMethod("acceptRingingCall");
                        method.invoke(telecomManager);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    editor = preferences.edit();
                    current_call_count = current_call_count + 0.5f;
                    editor.putFloat("current_call_count", current_call_count);
                    editor.apply();
                }*/
            }
        }

    }

    /*PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //方法必须写在super方法后面，否则incomingNumber无法获取到值。
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG, "onCallStateChanged: 电话挂断了");

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG, "onCallStateChanged: 电话接听");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //输出来电号码
                    Log.d(TAG, "onCallStateChanged: 电话响铃:来电号码"+incomingNumber);
                    break;
            }
        }
    };*/

}
