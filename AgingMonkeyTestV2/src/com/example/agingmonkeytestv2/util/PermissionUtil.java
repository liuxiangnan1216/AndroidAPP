package com.example.agingmonkeytestv2.util;

import java.util.ArrayList;
import java.util.Iterator;

import com.example.agingmonkeytestv2.broadcast.CommandField;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-7.
 ************************************************************************/



public class PermissionUtil {
    public static String[] PERMISSIONS_GROUP = new String[]{"android.permission.WRITE_SETTINGS", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA", "android.permission.RECORD_AUDIO", "android.permission.MODIFY_AUDIO_SETTINGS"};
    public static final int REQUEST_CODE_WRITE_SETTINGS = 33;
    private static final int REQUEST_PERMISSION_SETTING = 2;
    private static final int REQUEST_STATUS_CODE = 1;

    public static void checkAndRequestPermissions(Activity activity) {
        ArrayList<String> denidArray = new ArrayList();
        for (String permission : PERMISSIONS_GROUP) {
            //int grantCode = ContextCompat.checkSelfPermission(activity, permission); //TODO
            if ("android.permission.WRITE_SETTINGS".equals(permission) /*&& !Settings.System.canWrite(activity)*/) {//TODO
                requestWriteSettingPermission(activity);
            }
            if (!"android.permission.WRITE_SETTINGS".equals(permission) /*&& grantCode == -1*/) {//TODO
                denidArray.add(permission);
            }
        }
        String[] denidPermissions = (String[]) denidArray.toArray(new String[denidArray.size()]);
        if (denidPermissions.length > 0) {
            Iterator it = denidArray.iterator();
            while (it.hasNext()) {
                if (!showRationaleUI(activity, (String) it.next())) {
                    if (!isAppFirstRun(activity)) {
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts(CommandField.KEY_PACKAGE, activity.getPackageName(), null));
                        activity.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                    requestPermissions(activity, denidPermissions);
                }
            }
            requestPermissions(activity, denidPermissions);
        }
    }

    public static boolean showRationaleUI(Activity activity, String permission) {
        //return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        return true;//TODO
    }

    public static void requestPermissions(Activity activity, String[] permissions) {
        //ActivityCompat.requestPermissions(activity, permissions, REQUEST_STATUS_CODE); //TODO
    }

    private static void requestWriteSettingPermission(Activity activity) {
        activity.startActivityForResult(new Intent("android.settings.action.MANAGE_WRITE_SETTINGS", Uri.parse("package:" + activity.getPackageName())), 33);
    }

    public static boolean isAppFirstRun(Activity activity) {
        SharedPreferences sp = activity.getSharedPreferences("config", 0);
        SharedPreferences.Editor editor = sp.edit();
        if (sp.getBoolean("first_run", true)) {
            editor.putBoolean("first_run", false);
            editor.commit();
            return true;
        }
        editor.putBoolean("first_run", false);
        editor.commit();
        return false;
    }
}

