package com.example.android.testing.uiautomator.BasicSample;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)


public class BasicSample {
    private static final String BASIC_SAMPLE_PACKAGE = "com.android.dialer";

    private static final int LAUNCH_TIMEOUT = 5000;

    private static final String STRING_TO_BE_TYPED = "UiAutomator liuxiangnan";

    private String[] idString = {"floating_action_button","zero", "one", "tow","eight","six"};

    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the blueprint app
        Context context = InstrumentationRegistry.getContext();

        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void testChangeText_sameActivity() {
        // Type text and then press the button.
        try {
            clickID(BASIC_SAMPLE_PACKAGE,0);
            sleep(500);
            clickID(BASIC_SAMPLE_PACKAGE,2);
            sleep(500);
            clickID(BASIC_SAMPLE_PACKAGE,1);
            sleep(500);
            clickID(BASIC_SAMPLE_PACKAGE,1);
            sleep(500);
            clickID(BASIC_SAMPLE_PACKAGE,4);
            sleep(500);
            clickID(BASIC_SAMPLE_PACKAGE,5);
            sleep(500);
            //mDevice.pressBack();
            sleep(500);
            //ListView listView = (ListView) mDevice.findObjects(By.res(BASIC_SAMPLE_PACKAGE, "cliv_name_textview"));
            //UiScrollable listView = new UiScrollable(new UiSelector().scrollable(true).className("android.widget.ListView"));

            UiScrollable noteList = new UiScrollable( new UiSelector().className("android.widget.ListView"));
            UiObject note = null;

            note = noteList.getChildByText(new UiSelector().className("android.widget.TextView"), "Send SMS", true);
            assertThat(note,notNullValue());

            note.longClick();


            // 通过控件内容查找 短信息输入内容
            UiObject Enter_the_content = new UiObject(new UiSelector().text(/*"Type text message"*/"liuxiangnan-text"));
            Log.e("lxn", "Enter_the_content====" + Enter_the_content);
            String text = Enter_the_content.getText();
            Enter_the_content.clickBottomRight();
            for (int i = 0; i < text.length(); i++) {
                UiDevice.getInstance().pressDelete();
            }
            Enter_the_content.setText("888888888888888888");
            //assertTrue("Enter_the_content Exception!",Enter_the_content.exists());
            sleep(500);
            Enter_the_content.click();
            sleep(1000);

            Log.e("lxn", "Enter_the_content====" + Enter_the_content);
            //Enter_the_content.setText("liuxiangnan-test02");

            //mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "compose_message_text")).setText(STRING_TO_BE_TYPED);
            //sleep(500);
            mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "send_button_sms")).click();
            sleep(500);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
//        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "dialpad_floating_action_button")).click();
        //mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "floating_action_button")).click();

    }

    /**
     * 点击ID号
     * @param packageName
     * @param id
     */
    public void clickID(String packageName, int id){
        mDevice.findObject(By.res(packageName, idString[id])).click();

    }
    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */

    public String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }
}
