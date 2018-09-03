package com.example.agingmonkeytestv2.util;


import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.SystemClock;
import android.provider.Settings;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-8.
 ************************************************************************/


public class PreconditionsUtil {
    private static final String ADB_ENABLED = "adb_enabled";
    private static final int ADB_ENABLED_OFF = 0;
    private static final int ADB_ENABLED_ON = 1;
    private static final String DEVELOPMENT_SETTINGS_ENABLED = "development_settings_enabled";
    private static final int DEVELOPMENT_SETTINGS_ENABLED_OFF = 0;
    private static final int DEVELOPMENT_SETTINGS_ENABLED_ON = 1;
    private static final String TAG = "PreconditionsUtil";
    private static final String USB_FUNCTION_MTP = "mtp";
    private static final String USB_FUNCTION_PTP = "ptp";

    public static void developerSetting(Context ctx) {
        if (Settings.Secure.getInt(ctx.getContentResolver(), DEVELOPMENT_SETTINGS_ENABLED, DEVELOPMENT_SETTINGS_ENABLED_OFF) == 0) {
            Settings.Global.putInt(ctx.getContentResolver(), DEVELOPMENT_SETTINGS_ENABLED, DEVELOPMENT_SETTINGS_ENABLED_ON);
        }
    }

    public static void usbDebug(Context ctx) {
        if (Settings.Secure.getInt(ctx.getContentResolver(), ADB_ENABLED, ADB_ENABLED_OFF) == 0) {
            Settings.Global.putInt(ctx.getContentResolver(), ADB_ENABLED, ADB_ENABLED_ON);
        }
    }

    public static void usbMtp(Context ctx) {
        UsbManager localUsbManager = (UsbManager) ctx.getSystemService(Context.USB_SERVICE);
        try {
            Class<?> mUsbManagerClass = Class.forName("android.hardware.usb.UsbManager");
            if (!((Boolean) mUsbManagerClass.getMethod("isFunctionEnabled",
                    new Class[]{String.class}).invoke(localUsbManager, new Object[]{USB_FUNCTION_MTP})).booleanValue()) {
                mUsbManagerClass.getMethod("setCurrentFunction", new Class[]{String.class}).invoke(localUsbManager, new Object[]{USB_FUNCTION_MTP});
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        }
    }

    public static void setTime(int year, int month, int day, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(calendar.MILLISECOND, 0);
        long when = calendar.getTimeInMillis();
        if (when / 1000 < 2147483647L) {
            SystemClock.setCurrentTimeMillis(when);
        }
    }

    public static boolean isNumeric(String str) {
        int i = str.length();
        do {
            i--;
            if (i < 0) {
                return true;
            }
        } while (Character.isDigit(str.charAt(i)));
        return false;
    }
}
