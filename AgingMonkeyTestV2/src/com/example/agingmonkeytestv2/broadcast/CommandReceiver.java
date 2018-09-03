package com.example.agingmonkeytestv2.broadcast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.example.agingmonkeytestv2.FactoryEntryActivity;
import com.example.agingmonkeytestv2.MonkeyEntryActivity;
import com.example.agingmonkeytestv2.service.BatteryTestService;
import com.example.agingmonkeytestv2.service.MonkeyBackgroundService;
import com.example.agingmonkeytestv2.util.PreconditionsUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-8.
 ************************************************************************/



public class CommandReceiver extends BroadcastReceiver {
    public static final String TAG = "lxn-CommandReceiver";

    public void onReceive(Context context, Intent intent) {
        if (CommandField.ACTION_AUTOTEST_COMMAND.equals(intent.getAction())) {
            Log.d(TAG, "CommandReceiver sagereal.autotest.command at: " + new Date().toString());
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                Log.d(TAG, "sagereal.autotest.command : code error!");
                return;
            }
            monkeyFiltering(bundle, context);
            timeFiltering(bundle);
            factoryFiltering(bundle, context);
        }
    }

    private void monkeyFiltering(Bundle bundle, Context context) {
        if (bundle.getString(CommandField.KEY_START_MONKEY) == null || !bundle.getString(CommandField.KEY_START_MONKEY).equals("1")) {
            Log.d(TAG, "error code monkey：" + bundle.getString(CommandField.KEY_START_MONKEY));
            return;
        }
        if (!BatteryTestService.checkSelfAlive()) {
            Intent mServiceIntent = new Intent();
            mServiceIntent.setClass(context, BatteryTestService.class);
            context.startService(mServiceIntent);
        }
        if (!MonkeyBackgroundService.checkServiceAlive()) {
            MonkeyEntryActivity.createBlackListFile(context);
            MonkeyEntryActivity.commandStartMonkey(context, 3, 0, 0, 0, packageFiltering(bundle), true);
        }
    }

    private void timeFiltering(Bundle bundle) {
        if (bundle.getString(CommandField.KEY_SET_TIME) != null && bundle.getString(CommandField.KEY_SET_TIME).length() == 14 && PreconditionsUtil.isNumeric(bundle.getString(CommandField.KEY_SET_TIME))) {
            String timeLine = bundle.getString(CommandField.KEY_SET_TIME);
            PreconditionsUtil.setTime(Integer.parseInt(timeLine.substring(0, 4)), Integer.parseInt(timeLine.substring(4, 6)), Integer.parseInt(timeLine.substring(6, 8)), Integer.parseInt(timeLine.substring(8, 10)), Integer.parseInt(timeLine.substring(10, 12)), Integer.parseInt(timeLine.substring(12)));
            return;
        }
        Log.d(TAG, "error code time：" + bundle.getString(CommandField.KEY_SET_TIME));
    }

    private List<String> packageFiltering(Bundle bundle) {
        ArrayList<String> packageUnderTest = bundle.getStringArrayList(CommandField.KEY_PACKAGE);
        if (packageUnderTest == null || packageUnderTest.isEmpty()) {
            return Collections.emptyList();
        } else {
            return packageUnderTest;
        }
    }

    private void factoryFiltering(Bundle bundle, Context context) {
        if (bundle.getString(CommandField.KEY_START_FACTORY) != null && bundle.getString(CommandField.KEY_START_FACTORY).equals("1")) {
            Log.d(TAG, "get factory start key: " + bundle.getString(CommandField.KEY_START_FACTORY));
            if (!BatteryTestService.checkSelfAlive()) {
                Intent mServiceIntent = new Intent();
                mServiceIntent.setClass(context, BatteryTestService.class);
                context.startService(mServiceIntent);
            }
            FactoryEntryActivity.commandFactoryTest(context, CommandField.KEY_START_FACTORY);

        } else if (bundle.getString(CommandField.KEY_START_LCD) != null && bundle.getString(CommandField.KEY_START_LCD).equals("1")) {

            Log.d(TAG, "get lcd start key: " + bundle.getString(CommandField.KEY_START_LCD));
            FactoryEntryActivity.commandFactoryTest(context, CommandField.KEY_START_LCD);

        } else if (bundle.getString(CommandField.KEY_START_WIFI) != null && bundle.getString(CommandField.KEY_START_WIFI).equals("1")) {

            Log.d(TAG, "get wifi start key: " + bundle.getString(CommandField.KEY_START_WIFI));
            FactoryEntryActivity.commandFactoryTest(context, CommandField.KEY_START_WIFI);

        } else if (bundle.getString(CommandField.KEY_START_BLUETOOTH) != null && bundle.getString(CommandField.KEY_START_BLUETOOTH).equals("1")) {

            Log.d(TAG, "get bluetooth start key: " + bundle.getString(CommandField.KEY_START_BLUETOOTH));
            FactoryEntryActivity.commandFactoryTest(context, CommandField.KEY_START_BLUETOOTH);

        } else if (bundle.getString(CommandField.KEY_START_FLASHLIGHT) != null && bundle.getString(CommandField.KEY_START_FLASHLIGHT).equals("1")) {

            Log.d(TAG, "get flashlight start key: " + bundle.getString(CommandField.KEY_START_FLASHLIGHT));
            FactoryEntryActivity.commandFactoryTest(context, CommandField.KEY_START_FLASHLIGHT);

        } else if (bundle.getString(CommandField.KEY_START_REARCAMERA_PREVIEW) != null && bundle.getString(CommandField.KEY_START_REARCAMERA_PREVIEW).equals("1")) {

            Log.d(TAG, "get rear_camera_preview start key: " + bundle.getString(CommandField.KEY_START_REARCAMERA_PREVIEW));
            FactoryEntryActivity.commandFactoryTest(context, CommandField.KEY_START_REARCAMERA_PREVIEW);

        } else if (bundle.getString(CommandField.KEY_START_FRONTCAMERA_PREVIEW) != null && bundle.getString(CommandField.KEY_START_FRONTCAMERA_PREVIEW).equals("1")) {

            Log.d(TAG, "get front_camera_preview start key: " + bundle.getString(CommandField.KEY_START_FRONTCAMERA_PREVIEW));
            FactoryEntryActivity.commandFactoryTest(context, CommandField.KEY_START_FRONTCAMERA_PREVIEW);

        } else if (bundle.getString(CommandField.KEY_START_REAR_CAMERA_VIDEO) != null && bundle.getString(CommandField.KEY_START_REAR_CAMERA_VIDEO).equals("1")) {

            Log.d(TAG, "get rear_camera_video start key: " + bundle.getString(CommandField.KEY_START_REAR_CAMERA_VIDEO));
            FactoryEntryActivity.commandFactoryTest(context, CommandField.KEY_START_REAR_CAMERA_VIDEO);

        } else if (bundle.getString(CommandField.KEY_START_FRONT_CAMERA_VIDEO) != null && bundle.getString(CommandField.KEY_START_FRONT_CAMERA_VIDEO).equals("1")) {

            Log.d(TAG, "get front_camera_video start key: " + bundle.getString(CommandField.KEY_START_FRONT_CAMERA_VIDEO));
            FactoryEntryActivity.commandFactoryTest(context, CommandField.KEY_START_FRONT_CAMERA_VIDEO);

        } else if (bundle.getString(CommandField.KEY_START_CAMERA_PREVIEW) != null && bundle.getString(CommandField.KEY_START_CAMERA_PREVIEW).equals("1")) {

            Log.d(TAG, "get camera_preview start key: " + bundle.getString(CommandField.KEY_START_CAMERA_PREVIEW));
            FactoryEntryActivity.commandFactoryTest(context, CommandField.KEY_START_CAMERA_PREVIEW);

        } else if (bundle.getString(CommandField.KEY_START_CAMERA_PICTURE) != null && bundle.getString(CommandField.KEY_START_CAMERA_PICTURE).equals("1")) {

            Log.d(TAG, "get camera_picture start key: " + bundle.getString(CommandField.KEY_START_CAMERA_PICTURE));
            FactoryEntryActivity.commandFactoryTest(context, CommandField.KEY_START_CAMERA_PICTURE);

        } else if (bundle.getString(CommandField.KEY_START_SOUNDRECORD) == null || !bundle.getString(CommandField.KEY_START_SOUNDRECORD).equals("1")) {

            Log.e(TAG, "error code ");
        } else {
            Log.d(TAG, "get sound_record start key: " + bundle.getString(CommandField.KEY_START_SOUNDRECORD));
            FactoryEntryActivity.commandFactoryTest(context, CommandField.KEY_START_SOUNDRECORD);
        }
    }
}

