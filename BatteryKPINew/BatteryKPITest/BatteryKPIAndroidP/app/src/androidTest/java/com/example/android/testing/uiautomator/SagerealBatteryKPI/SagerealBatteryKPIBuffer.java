package com.example.android.testing.uiautomator.SagerealBatteryKPI;

import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertTrue;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 19-3-13.
 ************************************************************************/

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class SagerealBatteryKPIBuffer {
    private static final String TAG = "lxn-SagerealBatteryKPIBuffer";

    private static UiDevice mDevice = UiDevice.getInstance(getInstrumentation());


    @Before
    public void before() throws InterruptedException{
        Log.d(TAG, "before");
        // wakeup screen
        try {
            if (!mDevice.isScreenOn()) {
                Log.e(TAG, "isScreenOn=="+mDevice.isScreenOn());
                mDevice.wakeUp();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Device wakeUp exception!");
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void beforeClass()  {
        //测试项目开始前运行（仅一次），如清除缓存数据、安装应用等
        Log.d(TAG, "beforeClass: ");
        getInstrumentation().getUiAutomation().executeShellCommand("pm grant " + InstrumentationRegistry.getTargetContext().getPackageName() + " android.permission.WRITE_EXTERNAL_STORAGE");
        getInstrumentation().getUiAutomation().executeShellCommand("pm grant " + InstrumentationRegistry.getTargetContext().getPackageName() + " android.permission.READ_EXTERNAL_STORAGE");
        getInstrumentation().getUiAutomation().executeShellCommand("pm grant " + InstrumentationRegistry.getTargetContext().getPackageName() + " android.permission.MODIFY_AUDIO_SETTINGS");


    }


    /**
     * buffer
     * 清除上次测试的近期任务
     */
    @Test
    public void buffer_test_001() {
        Log.d(TAG, "buffer_test_001, nothing");
        try {
            mDevice.pressRecentApps();
            sleep(500);
            UiObject recentRemoveBtn = new UiObject(new UiSelector().resourceId("com.android.systemui:id/button_remove_all"));
            if (recentRemoveBtn.exists()) {
                recentRemoveBtn.clickAndWaitForNewWindow();
            }
            sleep(500);
            mDevice.pressHome();

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

    }
}
