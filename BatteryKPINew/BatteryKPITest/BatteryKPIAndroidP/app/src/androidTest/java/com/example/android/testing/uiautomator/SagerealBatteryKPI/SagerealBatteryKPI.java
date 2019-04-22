package com.example.android.testing.uiautomator.SagerealBatteryKPI;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Environment;
import android.os.PowerManager;
import android.os.RemoteException;
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
import android.transition.Slide;
import android.util.Log;
import android.view.KeyEvent;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.view.KeyEvent.KEYCODE_AT;
import static android.view.KeyEvent.KEYCODE_PERIOD;
import static android.view.KeyEvent.KEYCODE_POWER;
import static com.example.android.testing.uiautomator.SagerealBatteryKPI.SagerealBatteryKPIBefore.openWebTest_time;
import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com
 *   > DATE: Date on 19-3-11.
 ************************************************************************/

/**
 * 电池KPI测试,使用 uiautomator 来实现
 * 测试过程中不使用系统设置中的屏幕超时来灭屏(存在和程序时间冲突问题导致部分ID找不到而报错),
 * 设置系统屏幕超时为 30 分钟或 never, 程序中使用 mDeviceSleep 来灭屏
 * 测试之前需要安装启动测试脚本的 apk (需要系统平台签名)来启动和配合资源,
 * 该类是主要的测试类, SagerealBatteryKPIBefore 类是用来在多次循环测试时进行输出文件的预备(删除可能在之前测试生成的文件等操作)
 * TestSuit类是整合测试类,统一管理测试类可以实现一个类循环测试,或者多个类整合测试.同时可以统一测试启动命令
 * 命令如下:
 * nohup am instrument -w -r   -e debug false -e class com.example.android.testing.uiautomator.BatteryKPIAndroidP.TestSuit
 * com.example.android.testing.uiautomator.BatteryKPIAndroidP.test/android.support.test.runner.AndroidJUnitRunner
 */


@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class SagerealBatteryKPI {

    private static final String TAG = "lxn-SagerealBatteryKPI";

    private static final String LAUNCHERFLAG = "com.transsion.hilauncher:id/ic";//主界面 flag

    private static final int LAUNCH_TIMEOUT = 2000;//启动超时

    private static final int STEPS = 10;

    private int KILLTIMEOUT = 8 * 60 * 1000;

    private static UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    private static int testCount = 0;

    //获取被测应用的Context
    static Context applicationContext = InstrumentationRegistry.getTargetContext().getApplicationContext();


    private static final String homePkgName = "com.transsion.hilauncher";

    private static final String setscreen_off_timeout_10mins = "settings put system screen_off_timeout 610000";
    private static final String setscreen_off_timeout_2s = "settings put system screen_off_timeout 2000";
    private static final String setscreen_off_timeout_2mins = "settings put system screen_off_timeout 120000";
    private static final String setbrightness_0 = "settings put  system screen_brightness 0";
    private static final String setbrightness_255 = "settings put  system screen_brightness 255";
    private static final String endCall = "input  keyevent  KEYCODE_ENDCALL";
    private static final String dialCMD = "am start -a android.intent.action.CALL tel:";


    private static final String ChormePackageName = "com.android.chrome";
    private static final String MessagePackageName = "com.google.android.apps.messaging";
    private static final String DialerPackageName = "com.android.dialer";
    private static final String CameraPackageName = "com.mediatek.camera";
    private static final String GmailPackageName = "com.google.android.gm.lite";
    private static final String googledMusicPackageName = "com.google.android.music";
    private static final String androidMusicPackageName = "com.android.music";
    private static final String PlayStorePackageName = "com.google.android.gms";
    private static final String GamesPackageName = "com.rovio.angrybirds";
    private static final String smspackageName = "com.google.android.apps.messaging";
    private static final String settingsPackageName = "com.android.settings";
    private static final String QQPackageName = "com.tencent.mobileqq";
    private static final String PhonePackageName = "com.android.phone";
    private static final String uiautoPackageName = "com.example.MyTest";
    private static final String weChatPackageName = "com.tencent.mm";
    private static final String weiboPackageName = "com.sina.weibo";

//    private static final String FILE_NAME = "/uiautomatorTest.txt";

    private static final String configFILE_NAME = "/myconfig.xml";

    private static final int timeOut = 10000;//5秒
    private static final int fifteenminstime = 15 * 60 * 1000;//15分钟
    private static final int onemins = 60 * 1000;//1分钟


    int displayHeight = mDevice.getDisplayHeight();
    int displayWidth = mDevice.getDisplayWidth();


    private static final int SCRE_WIDTH = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).getInstance().getDisplayWidth();//获取屏幕宽度
    private static final int SCRE_HEIGHT = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).getInstance().getDisplayHeight();//获取屏幕高度


    PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
    PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

    @Before
    public void before(){
        Log.d(TAG, "*************** before *****************");
        try {
            while (!mDevice.isScreenOn()) {
                Log.d(TAG, "isScreenOn==" + mDevice.isScreenOn());
                mDevice.wakeUp();
            }
            String appPKG = mDevice.getCurrentPackageName();
            if (!appPKG.equals(homePkgName)) {//判断是否是桌面
                mDevice.pressHome();
                sleep(500);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Device wakeUp exception!");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @After
    public void after() {
        String appPKG = mDevice.getCurrentPackageName();
        Log.d(TAG, "*************** after ****************appPKG: " + appPKG);

    }



    /**
     * 亮屏
     */
    public void mWeakUp() {
        try {
            while (!mDevice.isScreenOn()) {
                Log.d(TAG, "isScreenOn==" + mDevice.isScreenOn());
                mDevice.pressKeyCode(KEYCODE_POWER);//模拟电源键亮屏
                sleep(1 * 1000);
                if (!mDevice.isScreenOn()) {
                    mDevice.wakeUp();
                }
            }
            Log.d(TAG, "while out isScreenOn==" + mDevice.isScreenOn());
        } catch (RemoteException e) {
            Log.e(TAG, "Device wakeUp exception!");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 灭屏
     */
    public void mDeviceSleep() {
        try {
            sleep(1 * 1000);
            while (mDevice.isScreenOn()) {
                mDevice.pressKeyCode(KEYCODE_POWER);//模拟电源键灭屏
                sleep(1 * 1000);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void beforeClass() {
        Log.d(TAG, "beforeClass");

        AudioManager mAudioManager = (AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE);
        int streamMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,streamMaxVolume,1);
        // 序号	测试项描述	测试开始时间	电池电压（mV）	电池电量(%)	测试结束时间	电池电压（mV）	电池电量(%) 单项测试时长（mins）	备注
        try {

            write("\n\nSerialNumber" + "\t" + "Description" + "\t" + "StartTime" + "\t" + "Voltage(mV)" + "\t" + "Electricity(%)" + "\t" + "EndTime" + "\t" + "Voltage(mV)" + "\t" + "Electricity(%)" + "\t" + "ItemTestTime(mins)" + "\t" + "Note" + "\t" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    /**
     * log文件存储到sd卡下
     * @param content
     * @throws IOException
     */
    private static void write(String content) throws IOException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d(TAG, "write");
            File sdCardDir = Environment.getExternalStorageDirectory();
            Log.d(TAG, "sdCardDir.getCanonicalPath(): " + sdCardDir.getCanonicalPath());
            Log.d(TAG, "file name :" + SagerealBatteryKPIBefore.FILE_NAME);
            File targetFile = new File(sdCardDir.getCanonicalPath() + SagerealBatteryKPIBefore.FILE_NAME);
            // 以指定文件创建 RandomAccessFile对象
            RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
            // 将文件记录指针移动到最后
            raf.seek(targetFile.length());
            // 输出文件内容
            raf.write(content.getBytes());
            // 关闭RandomAccessFile
            raf.close();
            Log.d(TAG, "raf.close()");
        }
    }


    /**
     * 获取时间信息
     * @return
     */
    public String getTimeInfo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String time = format.format(Calendar.getInstance().getTime());
        Log.d(TAG, "完整的时间和日期： " + time);
        String testtime = time;
        return testtime;
    }

    /**
     * 获取电池信息
     * @return
     */
    public String getBatteryInfo() {
        Log.d(TAG, "getBatteryInfo");
        String batteryInfo = null;
        try {
            batteryInfo = mDevice.executeShellCommand("dumpsys battery");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getBatterryInfo: " + batteryInfo);
        int level = batteryInfo.indexOf("level");
        int scale = batteryInfo.indexOf("scale");
        int voltage = batteryInfo.lastIndexOf("voltage");
        int temperature = batteryInfo.indexOf("temperature");
        String btlevel = batteryInfo.substring(level, scale).trim();
        String btvoltage = batteryInfo.substring(voltage, temperature).trim();
        Log.d(TAG, "btlevel=" + btlevel + " " + "btvoltage=" + btvoltage);//btlevel=level:100 btvoltage=voltage:4360
        //batteryinfo = btlevel + "\n" + btvoltage ;
        batteryInfo = btlevel + "," + btvoltage;
        return batteryInfo;
    }


    /**
     * 获取电池电量
     * @return
     */
    public String getBatterryElectric() {
        Log.d(TAG, "getBatterryElectric");
        String batteryinfo = null;
        try {
            batteryinfo = mDevice.executeShellCommand("dumpsys battery");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getBatterryInfo: " + batteryinfo);
        int level = batteryinfo.indexOf("level");
        int scale = batteryinfo.indexOf("scale");
        int voltage = batteryinfo.lastIndexOf("voltage");
        int temperature = batteryinfo.indexOf("temperature");
        String btlevel = batteryinfo.substring(level, scale).trim();
        String btvoltage = batteryinfo.substring(voltage, temperature).trim();
        Log.d(TAG, "btlevel=" + btlevel + " " + "btvoltage=" + btvoltage);//btlevel=level:100 btvoltage=voltage:4360
        batteryinfo = btlevel + "%";
        String electric = btlevel.substring(6);
        return electric;

    }

    /**
     * 获取电池电压
     * @return
     */
    public String getBatterryVoltage() {
        Log.d(TAG, "getBatterryVoltage");
        String batteryinfo = null;
        try {
            batteryinfo = mDevice.executeShellCommand("dumpsys battery");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getBatterryInfo: " + batteryinfo);
        int level = batteryinfo.indexOf("level");
        int scale = batteryinfo.indexOf("scale");
        int voltage = batteryinfo.lastIndexOf("voltage");
        int temperature = batteryinfo.indexOf("temperature");
        String btlevel = batteryinfo.substring(level, scale).trim();
        String btvoltage = batteryinfo.substring(voltage, temperature).trim();
        Log.d(TAG, "btlevel=" + btlevel + " " + "btvoltage=" + btvoltage);//btlevel=level:100 btvoltage=voltage:4360
        String voltage1 = btvoltage.substring(8);
        batteryinfo = btvoltage + "mV";
        return voltage1;

    }

    /**
     * 启动APP
     * @param packageName
     * @param timeOut
     */
    public static void startApp(String packageName, int timeOut) {
        Log.d(TAG, "start app packageName====" + packageName);
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        assertThat(intent, notNullValue());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        Log.d(TAG, "start app wait...");
        mDevice.wait(Until.hasObject(By.res(packageName).depth(0)), timeOut);

    }

    /**
     * 关闭APP
     * @param packageName
     * @throws IOException
     */
    public static void closeAPP(String packageName) throws IOException {
        Log.d(TAG, "closeAPP :" + packageName);
        mDevice.executeShellCommand("am force-stop " + packageName);//通过命令行关闭app
        //mDevice.executeShellCommand("pm clear " + sPackageName);
    }

    /**
     * 调节音量
     * @param up
     * @param times
     * @throws UiObjectNotFoundException
     * @throws InterruptedException
     */
    public static void adjustVolume(boolean up, int times) throws InterruptedException {
        if (up) {
            for (int i = 0; i < times; i++) {
                mDevice.pressKeyCode(KeyEvent.KEYCODE_VOLUME_UP);
                sleep(1000);
            }
        } else {
            for (int j = 0; j < times; j++) {
                mDevice.pressKeyCode(KeyEvent.KEYCODE_VOLUME_DOWN);
                sleep(1000);
            }
        }
    }


    /**
     * 计算时间间隔，单位是min
     * @param starttime
     * @param endtime
     * @return
     */
    public static int calculateTime(int starttime, int endtime) {
        return Math.abs(endtime - starttime) / 1000 / 60;
    }

    private void setSpeakerphoneOn(boolean on) {
        AudioManager audioManager = (AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE);
        if (on) {
            audioManager.setSpeakerphoneOn(true);
        } else {
            audioManager.setSpeakerphoneOn(false);//关闭扬声器
            audioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
            //把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
    }

    public static void setMaxStreamVolume() {
        AudioManager mAudioManager = (AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE);
        int streamMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,streamMaxVolume,1);
    }

    /**
     * 上滑
     */
    public void slideUp() {
        mDevice.swipe((int)(SCRE_WIDTH * 0.5), (int)(SCRE_HEIGHT * 0.7), (int)(SCRE_WIDTH * 0.5), (int)(SCRE_HEIGHT * 0.4), STEPS);
    }



    //打开settings，设置自动灭屏时间为15s，设置Screen lock为none

    /**
     *
     * @param time 单位 秒
     * @throws IOException
     */
    public static void setDisplayTimeandScreenLock(int time) throws IOException {
        startApp(settingsPackageName, timeOut);
        try {
            UiObject Display = new UiObject(new UiSelector().text("Display"));
            while (!Display.exists()) {
                mDevice.swipe((int)(SCRE_WIDTH * 0.5), (int)(SCRE_HEIGHT * 0.7), (int)(SCRE_WIDTH * 0.5), (int)(SCRE_HEIGHT * 0.4), STEPS);
            }
            Display.clickAndWaitForNewWindow();
            UiObject Sleep = new UiObject(new UiSelector().text("Sleep"));
            Sleep.clickAndWaitForNewWindow();
            if (time == 15) {
                UiObject minutes = new UiObject(new UiSelector().text("15 seconds"));
                minutes.clickAndWaitForNewWindow();
            } else if (time == 30) {
                UiObject minutes = new UiObject(new UiSelector().text("30 seconds"));
                minutes.clickAndWaitForNewWindow();
            } else if (time == 60) {
                UiObject minutes = new UiObject(new UiSelector().text("1 minute"));
                minutes.clickAndWaitForNewWindow();
            } else if (time == 2 * 60) {
                UiObject minutes = new UiObject(new UiSelector().text("2 minutes"));
                minutes.clickAndWaitForNewWindow();
            } else if (time == 5 * 60) {
                UiObject minutes = new UiObject(new UiSelector().text("5 minutes"));
                minutes.clickAndWaitForNewWindow();
            } else if (time == 10 * 60) {
                UiObject minutes = new UiObject(new UiSelector().text("10 minutes"));
                minutes.clickAndWaitForNewWindow();
            } else if (time == 30 * 60) {
                UiObject minutes = new UiObject(new UiSelector().text("30 minutes"));
                minutes.clickAndWaitForNewWindow();
            }
            mDevice.pressBack();

//            UiScrollable RecyclerView = new UiScrollable(new UiSelector().className("android.support.v7.widget.RecyclerView"));
//            RecyclerView.setMaxSearchSwipes(10);//设置最大可扫动次数
//            UiObject Security = new UiObject(new UiSelector().text("Security & location"));//找到“更多”这个选项
//            RecyclerView.scrollIntoView(Security);//然后点击它（更多）
//            Security.click();
//            UiObject Screen = new UiObject(new UiSelector().text("Screen lock"));
//            Screen.clickAndWaitForNewWindow();
//            UiObject None = new UiObject(new UiSelector().text("None"));
//            None.clickAndWaitForNewWindow();


            closeAPP(settingsPackageName);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }




    /***************************************测试 CASE  begin**************************************************/


    /**
     * 异常不要在方法名后抛出,那样存在测试方法抛出异常时,后面的方法不能够再运行,为了保证后面的方法一定执行 抛异常时请使用 try {} catch {}抛出
     */


    /**
     * call
     */
    @Test
    public void test_001() {
        Log.d(TAG, "test_001 call call_count==" + SagerealBatteryKPIBefore.call_count);

//        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wk.acquire();
        boolean isEnd = false;
        int startTime = (int) System.currentTimeMillis();
        try {
            write("001" + "\t" + "Call test" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
            mDevice.executeShellCommand("svc data disable");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < SagerealBatteryKPIBefore.call_count; i++) {
            try {
                mDevice.executeShellCommand(dialCMD + SagerealBatteryKPIBefore.CooperatingPhoneNum);
                setSpeakerphoneOn(true);
                sleep(10 * 1000);
                mDeviceSleep();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "call..." + SagerealBatteryKPIBefore.call_times * onemins);
            try {

                int callStartTime = (int) System.currentTimeMillis();
                int callEndTime = callStartTime;
                int count = 1;

                while ((callEndTime - callStartTime) < (SagerealBatteryKPIBefore.call_times * onemins)) {
                    sleep(10 * 1000);
                    callEndTime = (int) System.currentTimeMillis();
                    Log.d(TAG, "call time: " + (callEndTime - callStartTime));

                    if ((callEndTime - callStartTime) >=  KILLTIMEOUT * count) {
                        count++;
                        mDevice.pressKeyCode(KEYCODE_POWER);//亮屏
                        sleep(1000);
                        mDevice.pressKeyCode(KEYCODE_POWER);//灭屏
                    }
                }
//                sleep(SagerealBatteryKPIBefore.call_times * onemins + 5 * 1000);
                mDevice.executeShellCommand(endCall);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
//        wk.release();
        try {
            write("|" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        wk.release();
        isEnd = true;
        Log.d(TAG, "test 001 quit");
        assertTrue("test 001 is end", isEnd);
        Log.d(TAG, "test 001 assertTrue end");

    }

    /**
     * play game
     */
    @Test
    public void test_002(){
        Log.d(TAG, "test 002 play game times:" + SagerealBatteryKPIBefore.playgames_time * onemins);
        boolean isEnd = false;
        try {
            mDevice.executeShellCommand("svc data disable");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
//        wk.acquire();
        int startTime = (int) System.currentTimeMillis();
        try {
            write("\n002" + "\t" + "play game" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
            startApp(GamesPackageName, timeOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDevice.click((int)(SCRE_WIDTH * 0.17), (int)(SCRE_WIDTH * 0.63));
        int endTime = (int) System.currentTimeMillis();
        while ((endTime - startTime) <= SagerealBatteryKPIBefore.playgames_time * onemins) {
            int startx = (int)(Math.random() * 720);
            int starty = (int)(Math.random() * 1280);
            Log.d(TAG, "startx:" + startx + " starty:" + starty);
            mDevice.swipe(startx, starty, startx - 5, starty - 20, 10);
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            endTime = (int) System.currentTimeMillis();
            Log.d(TAG, "times=" + ((endTime - startTime)));
        }
        try {
            closeAPP(GamesPackageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        wk.release();

        endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        try {
            write("|" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDeviceSleep();
//        mDevice.pressKeyCode(KEYCODE_POWER);
        Log.d(TAG, "test 002 quit");
//        if (wk.isHeld()) {
//            wk.release();
//        }
        isEnd = true;
        assertTrue("test 002 is end", isEnd);
        Log.d(TAG, "test 002 assertTrue end");


    }

    /**
     * weChat
     */
    @Test
    public void test_003() {
        Log.d(TAG, "test 003 weChat times :" + SagerealBatteryKPIBefore.weChatcount);
        boolean isEnd = false;
        try {
            mDevice.executeShellCommand("svc data enable");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
//        wk.acquire();
        int startTime = (int) System.currentTimeMillis();
        try {
            write("\n003" + "\t" + "weChat" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (SagerealBatteryKPIBefore.weChatcount > 0) {
            SagerealBatteryKPIBefore.weChatcount--;
            try {
                mWeakUp();
                startApp(weChatPackageName, timeOut);
                sleep(10 * 1000);
                UiObject conBtn = new UiObject(new UiSelector().text("Contacts"));
                if (conBtn.exists()) {
                    conBtn.clickAndWaitForNewWindow();
                } else {
                    UiObject consBtn = new UiObject(new UiSelector().text("通讯录"));
                    consBtn.clickAndWaitForNewWindow();
                }
                UiObject test = new UiObject(new UiSelector().text("test"));
                test.clickAndWaitForNewWindow();
                UiObject sendMsg = new UiObject(new UiSelector().text("Send Message"));
                sendMsg.clickAndWaitForNewWindow();
                UiObject edit = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/amb"));
                edit.clickAndWaitForNewWindow();
                edit.setText(SagerealBatteryKPIBefore.WeChatMsg);
                UiObject sendBtn = new UiObject(new UiSelector().text("Send"));
                sendBtn.clickAndWaitForNewWindow();
                closeAPP(weChatPackageName);
//                wk.release();
                mDeviceSleep();
                sleep(SagerealBatteryKPIBefore.weChatInterval * onemins);//时间间隔
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        try {
            write("|" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
            closeAPP(weChatPackageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        wk.release();
        Log.d(TAG, "test 003 quit");
//        if (wk.isHeld()) {
//            wk.release();
//        }
        isEnd = true;
        assertTrue("test 003 is end", isEnd);
        Log.d(TAG, "test 003 assertTrue end");

    }


    /**
     * SMS
     */
    @Test
    public void test_004() {
        Log.d(TAG, "test 004 SMS times :" + SagerealBatteryKPIBefore.sendSMSCount);
        boolean isEnd = false;
//        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
//        wk.acquire();
        try {
            mDevice.executeShellCommand("svc data disable");
            mWeakUp();
            int startTime = (int) System.currentTimeMillis();
            write("\n004" + "\t" + "SMS" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
        } catch (IOException e) {
            e.printStackTrace();
        }

        int startTime = (int) System.currentTimeMillis();
        while (SagerealBatteryKPIBefore.sendSMSCount > 0) {
            SagerealBatteryKPIBefore.sendSMSCount--;
            try {
//                mWeakUp();
//                startApp(smspackageName, timeOut);
                sleep(2 * 1000);

                mDevice.executeShellCommand(String.format("am start -a android.intent.action.SENDTO -d sms:%s --es sms_body %s", SagerealBatteryKPIBefore.CooperatingPhoneNum, SagerealBatteryKPIBefore.smsMsg));
                sleep(2000);
                UiObject sendBtn = new UiObject(new UiSelector().resourceId("com.google.android.apps.messaging:id/send_message_button_icon"));
                sendBtn.clickAndWaitForNewWindow();
                sleep(2000);
                closeAPP(smspackageName);
//                mDeviceSleep();
                sleep(SagerealBatteryKPIBefore.smsInterval * onemins);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {

            int endTime = (int) System.currentTimeMillis();
            int testTime = calculateTime(endTime, startTime);
//            wk.release();
            write("|" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "test 004 quit");
//        if (wk.isHeld()) {
//            wk.release();
//        }
        isEnd = true;
        assertTrue("test 004 end" , isEnd);
        Log.d(TAG, "test 004 assertTrue end");

    }

    /**
     * qq
     * 测试之前请将指定联系人置顶
     */
    @Test
    public void test_005() {
        Log.d(TAG, "test 005 qq qqCount====" + SagerealBatteryKPIBefore.qqCount);
        boolean isEnd = false;
//        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
//        wk.acquire();
        int startTime = (int) System.currentTimeMillis();
        try {
            mDevice.executeShellCommand("svc data enable");

            write("\n005" + "\t" + "QQ" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
            mWeakUp();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (SagerealBatteryKPIBefore.qqCount > 0) {
            Log.d(TAG, "qq count:" + SagerealBatteryKPIBefore.qqCount);
            SagerealBatteryKPIBefore.qqCount--;
            try {
                mWeakUp();
                startApp(QQPackageName, timeOut);
                sleep(5 * 1000);
                UiObject contact = new UiObject(new UiSelector().resourceId("com.tencent.mobileqq:id/relativeItem").index(0));
                contact.clickAndWaitForNewWindow();
                UiObject edit = new UiObject(new UiSelector().resourceId("com.tencent.mobileqq:id/input"));
                edit.setText(SagerealBatteryKPIBefore.qqMsg);
                UiObject send = new UiObject(new UiSelector().resourceId("com.tencent.mobileqq:id/fun_btn"));
                send.clickAndWaitForNewWindow();
                sleep(2 * 1000);
                closeAPP(QQPackageName);
                mDeviceSleep();
                sleep(SagerealBatteryKPIBefore.qqIntervals * onemins);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        try {
            write("|" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
            closeAPP(QQPackageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "test 005 quit");
//        if (wk.isHeld()) {
//            wk.release();
//        }
        isEnd = true;
        assertTrue("test 005 end" , isEnd);
        Log.d(TAG, "test 005 assertTrue end");


    }


    /**
     * web
     */
    @Test
    public void test_006() {
        Log.d(TAG, "test 006 web openWebTest_time==" + openWebTest_time);
        boolean isEnd = false;
//        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
//        wk.acquire();
        int startTime = (int) System.currentTimeMillis();
        try {
            mDevice.executeShellCommand("svc data enable");
            write("\n006" + "\t" + "web" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
            mWeakUp();
            startApp(ChormePackageName, timeOut); //启动app
            sleep(2 * 1000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //得到浏览器中的网页输入框
        UiObject edit = new UiObject(new UiSelector().className("android.widget.EditText"));
        if (edit.exists()) {
            try {
                edit.clickAndWaitForNewWindow();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
        }
        //edit.clearTextField();
        mDevice.pressDelete();
        try {
            edit.setText("https://www.baidu.com");
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        mDevice.pressEnter();//回车进行浏览，在部分手机不支持回车浏览，可以使用上面的方式得到浏览按钮在点击进行浏览
        int endTime = (int)System.currentTimeMillis();
        while ((endTime - startTime) < openWebTest_time * onemins) {
//            mDevice.swipe(300, 600, 300, 200, 10);
            try {
                while (!mDevice.isScreenOn()) {
                    mDevice.wakeUp();
                }
                slideUp();//上滑动
                sleep(20 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            endTime = (int) System.currentTimeMillis();
            Log.d(TAG, "endTime - startTime ====" + (endTime - startTime));
        }

        endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        try {
            write("|" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
            closeAPP(ChormePackageName);
            mDeviceSleep();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        wk.release();
//        Log.d(TAG, "test 006 quit");
//        if (wk.isHeld()) {
//            wk.release();
//        }
        isEnd = true;
        assertTrue("test 006 end" , isEnd);
        Log.d(TAG, "test 006 assertTrue end");

    }

    /**
     * weibo
     */
    @Test
    public void test_007() {
        Log.d(TAG, "test 007 weibo time:" + SagerealBatteryKPIBefore.weiboTime * onemins + "ms");
        boolean isEnd = false;
//        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
//        wk.acquire();
        int startTime = (int) System.currentTimeMillis();
        try {
            mDevice.executeShellCommand("svc data enable");
            write("\n007" + "\t" + "weibo" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
            startApp(weiboPackageName, timeOut);
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int endTime = (int) System.currentTimeMillis();
        while (endTime - startTime < SagerealBatteryKPIBefore.weiboTime * onemins) {
            try {
                endTime = (int) System.currentTimeMillis();
                mDevice.swipe(300, 600, 300, 200, 10);//上滑
                sleep(20 * 1000);
                Log.d(TAG, "endTime - startTime====" + (endTime - startTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        try {
            closeAPP(weiboPackageName);
            mDeviceSleep();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        wk.release();

        endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        try {
            write("|" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "test 007 quit");
//        if (wk.isHeld()) {
//            wk.release();
//        }
        isEnd = true;
        assertTrue("test 007 end" , isEnd);
        Log.d(TAG, "test 007 assertTrue end");


    }



    /**
     * mp3 提前将资源文件放入 sdcard/Music/目录下
     */
    @Test
    public void test_008() {
        Log.d(TAG, "test 008 mp3 time:" + SagerealBatteryKPIBefore.playMusic_time * onemins);
        boolean isEnd = false;
        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wakeLock.acquire();
        int startTime = (int) System.currentTimeMillis();
        try {
            mWeakUp();
            write("\n008" + "\t" + "mp3" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mDevice.executeShellCommand("svc data disable");
            //使用扬声器外放，即使已经插入耳机
            AudioManager mAudioManager = (AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0); //音量调到最大
            mAudioManager.setMicrophoneMute(false);
            mAudioManager.setSpeakerphoneOn(false);
            mAudioManager.setMode(AudioManager.STREAM_MUSIC);

            setDisplayTimeandScreenLock(15);//设置屏幕超时为 15s

//            SagerealBatteryKPIBefore.MusicPackageName = "com.google.android.music";//debug
//            Log.d(TAG, "SagerealBatteryKPIBefore.MusicPackageName ===" + SagerealBatteryKPIBefore.MusicPackageName);

            if (SagerealBatteryKPIBefore.MusicPackageName.equals(googledMusicPackageName)) {
                startApp(SagerealBatteryKPIBefore.MusicPackageName, timeOut);
                sleep(1 * 1000);
                mDevice.wakeUp();
                Log.d(TAG, "open googledMusic ");
                UiObject error = new UiObject(new UiSelector().text("OK"));
                Log.d(TAG, "exists error waiting click...");
                if (error.exists()) {
                    error.clickAndWaitForNewWindow();
                }


                //左滑
//                mDevice.swipe(displayWidth - 1, displayHeight / 2, displayWidth / 3, displayHeight / 2, displayWidth / 9);
//                mDevice.swipe(displayWidth - 1, displayHeight / 2, displayWidth / 3, displayHeight / 2, displayWidth / 9);
                UiObject Shuffle_all = new UiObject(new UiSelector().text("Shuffle all"));
                Shuffle_all.clickAndWaitForNewWindow();

            } else if (SagerealBatteryKPIBefore.MusicPackageName.equals(androidMusicPackageName)) {
                Log.d(TAG, " open androidMusic ");

                startApp(SagerealBatteryKPIBefore.MusicPackageName, timeOut);
                sleep(1 * 1000);
                mWeakUp();
                UiObject Playlists = new UiObject(new UiSelector().text("Playlists"));
                Playlists.clickAndWaitForNewWindow();
                UiObject Recently = new UiObject(new UiSelector().text("Recently added"));
                Recently.clickAndWaitForNewWindow();
                UiObject options = new UiObject(new UiSelector().description("More options").className("android.widget.ImageButton"));
                options.clickAndWaitForNewWindow();
                UiObject shuffle = new UiObject(new UiSelector().text("Party shuffle"));
                shuffle.clickAndWaitForNewWindow();
            }
//            wk.release();
//            sleep(10 * 1000);
//            mDeviceSleep();
//            sleep(SagerealBatteryKPIBefore.playMusic_time * onemins);
            long startPlayTime = System.currentTimeMillis();
            long currePlayTime = startPlayTime;
            int count = 1;

            while (currePlayTime - startPlayTime < SagerealBatteryKPIBefore.playMusic_time * onemins) {
                sleep((SagerealBatteryKPIBefore.playMusic_time * onemins) / 10);
                currePlayTime = System.currentTimeMillis();

                if (currePlayTime - startPlayTime >= KILLTIMEOUT * count) {
                    count++;
                    mDevice.pressKeyCode(KEYCODE_POWER);//亮屏
                    sleep(500);
                    mDevice.pressKeyCode(KEYCODE_POWER);//灭屏
                }
            }
            sleep(2 * 1000);
            mWeakUp();
//            mDevice.pressHome();

            Log.d(TAG, "test_008: play music end");
            closeAPP(SagerealBatteryKPIBefore.MusicPackageName);

            setDisplayTimeandScreenLock(30 * 60);//设置屏幕超时为 30m

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        try {
            write("|" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "test 008 quit");

        if (wakeLock.isHeld()) {
            wakeLock.release();
        }



        isEnd = true;
        assertTrue("test 008 end" , isEnd);
        Log.d(TAG, "test 008 assertTrue end");


    }

    /**
     * mp4 提前将资源文件放入 sdcard/Moves/目录下
     */
    @Test
    public void test_009() {
        Log.d(TAG, "test 009 mp4 times :" + SagerealBatteryKPIBefore.playVideo_times);
        boolean isEnd = false;
//        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
//        wk.acquire();
        int startTime = (int) System.currentTimeMillis();
        try {
            mDevice.executeShellCommand("svc data disable");
            write("\n009" + "\t" + "mp4" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
            startApp(SagerealBatteryKPIBefore.FilesPackageName, timeOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //使用扬声器外放，即使已经插入耳机   耳机、外放都有声音
            AudioManager mAudioManager = (AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            Log.d(TAG, " maxVolume=" + maxVolume);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0); //音量调到最大
            mAudioManager.setMicrophoneMute(false);
            mAudioManager.setSpeakerphoneOn(true);
            mAudioManager.setMode(AudioManager.STREAM_MUSIC);

            if (SagerealBatteryKPIBefore.FilesPackageName.contains("files")) {
                if (mDevice.hasObject(By.text("CONTINUE"))) {
                    Log.d(TAG, " into CONTINUE");
                    UiObject continueButton = new UiObject(new UiSelector().text("CONTINUE"));
                    continueButton.clickAndWaitForNewWindow();
                }
                {
                    UiObject continueButton = new UiObject(new UiSelector().text("Clean"));
                    continueButton.clickAndWaitForNewWindow();
                }
                if (mDevice.hasObject(By.text("Browse"))) {
                    UiObject BrowseButton = new UiObject(new UiSelector().text("Browse"));
                    BrowseButton.clickAndWaitForNewWindow();
                }
                //F1 old version
                if (mDevice.hasObject(By.text("Files"))) {
                    UiObject FilesButton = new UiObject(new UiSelector().text("Files"));
                    FilesButton.clickAndWaitForNewWindow();
                }
                sleep(2 * 1000);
                UiObject VideoButton = new UiObject(new UiSelector().text("Videos"));//找到
                while (!VideoButton.exists()) {
                    Log.d(TAG, "不在可见范围内上滑");
                    mDevice.swipe(displayWidth / 2, displayHeight / 4 * 3, displayWidth / 2, displayHeight / 4, displayHeight / 4);
                }
                VideoButton.clickAndWaitForNewWindow();
                int flag = 0;
//                UiObject VideoButton = new UiObject(new UiSelector().textContains("Videos"));
//                VideoButton.clickAndWaitForNewWindow();
                //如果filesgo版本比较新，有田字型switch按钮
                if (mDevice.hasObject(By.res("com.google.android.apps.nbu.files:id/view_mode_switch"))) {
                    Log.d(TAG, "存在田子格");
                    UiObject BrowseButton = new UiObject(new UiSelector().resourceId("com.google.android.apps.nbu.files:id/view_mode_switch"));
                    BrowseButton.clickAndWaitForNewWindow();
                    mDevice.swipe(displayWidth / 2, displayHeight / 2, displayWidth / 2, displayHeight / 7, displayHeight / 20);
                    sleep(2 * 1000);
                } else {
                    Log.d(TAG, "不存在田子格");
                    UiObject optionButton = new UiObject(new UiSelector().description("More options"));
                    optionButton.clickAndWaitForNewWindow();
                    UiObject switchButton = new UiObject(new UiSelector().textContains("Switch to list"));
                    if (switchButton.exists()) {
                        switchButton.clickAndWaitForNewWindow();
                    } else {
                        mDevice.pressBack();
                    }
                }
                UiObject ALL = new UiObject(new UiSelector().textContains("ALL"));
                ALL.clickAndWaitForNewWindow();
                //15分钟的视频，循环播放4次
                Log.d(TAG, "init playVideo_times: " + SagerealBatteryKPIBefore.playVideo_times);
                for (int i = 0; i < (SagerealBatteryKPIBefore.playVideo_times); i++) {
                    mWeakUp();
                    flag++;
                    Log.d(TAG, "playVideo_times: " + SagerealBatteryKPIBefore.playVideo_times);
                    UiObject Button720p = new UiObject(new UiSelector().textContains("720P.mp4"));
                    while (!Button720p.exists()) {
                        Log.d(TAG, "视频不在可见范围上滑动");
                        mDevice.swipe((int) (SCRE_WIDTH * 0.5), (int) (SCRE_HEIGHT * 0.8), (int) (SCRE_WIDTH * 0.5), (int) (SCRE_HEIGHT * 0.2), 10);
                    }
                    Button720p.clickAndWaitForNewWindow();
                    if (mDevice.hasObject(By.text("Photos"))) {
                        UiObject openwith = new UiObject(new UiSelector().text("Photos"));
                        openwith.clickAndWaitForNewWindow();
                    } else if (mDevice.hasObject(By.textContains("Video"))) {
                        UiObject openwith = new UiObject(new UiSelector().textContains("Video"));
                        openwith.clickAndWaitForNewWindow();
                    }
                    if (mDevice.hasObject(By.text("ALWAYS"))) {
                        UiObject openwithphotos = new UiObject(new UiSelector().text("ALWAYS"));
                        openwithphotos.clickAndWaitForNewWindow();
                    }
                    sleep(fifteenminstime + 20 * 1000);
                    mDevice.pressBack();
                }
                closeAPP(SagerealBatteryKPIBefore.FilesPackageName);
            }
            //如果预置的是filemanager
            else if (SagerealBatteryKPIBefore.FilesPackageName.contains("filemanager")) {
                UiObject InternalItem = new UiObject(new UiSelector().textContains("Internal"));
                InternalItem.clickAndWaitForNewWindow();
                mDevice.swipe(displayWidth / 2, displayHeight / 4 * 3, displayWidth / 2, displayHeight / 4, displayHeight / 4);
                mDevice.swipe(displayWidth / 2, displayHeight / 4 * 3, displayWidth / 2, displayHeight / 4, displayHeight / 4);
                mDevice.swipe(displayWidth / 2, displayHeight / 4 * 3, displayWidth / 2, displayHeight / 4, displayHeight / 4);
                mDevice.swipe(displayWidth / 2, displayHeight / 4 * 3, displayWidth / 2, displayHeight / 4, displayHeight / 4);
                //15分钟的视频，循环播放playVideo_times次
                for (int i = 0; i < SagerealBatteryKPIBefore.playVideo_times; i++) {
                    mWeakUp();
                    UiObject Button720p = new UiObject(new UiSelector().textContains("720P.mp4"));
                    Button720p.clickAndWaitForNewWindow();
                    if (mDevice.hasObject(By.text("Photos"))) {
                        UiObject openwith = new UiObject(new UiSelector().text("Photos"));
                        openwith.clickAndWaitForNewWindow();
                    } else if (mDevice.hasObject(By.textContains("Video"))) {
                        UiObject openwith = new UiObject(new UiSelector().textContains("Video"));
                        openwith.clickAndWaitForNewWindow();
                    }
                    if (mDevice.hasObject(By.text("ALWAYS"))) {
                        UiObject openwithphotos = new UiObject(new UiSelector().text("ALWAYS"));
                        openwithphotos.clickAndWaitForNewWindow();
                    }
                    sleep(fifteenminstime + 20 * 1000);
                    mDevice.pressBack();
                }
                closeAPP(SagerealBatteryKPIBefore.FilesPackageName);
            }
//        wk.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);

        try {
            write("|" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
            Log.d(TAG, "test 009 quit");
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        if (wk.isHeld()) {
//            wk.release();
//        }
        mDeviceSleep();

        isEnd = true;
        assertTrue("test 009 end" , isEnd);
        Log.d(TAG, "test 009 assertTrue end");


    }

    /**
     * photos
     */
    @Test
    public void test_010() {
        Log.d(TAG, "test 010 photos times " + SagerealBatteryKPIBefore.takePicture_count);
        boolean isEnd = false;
//        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
//        wk.acquire();

        int startTime = (int) System.currentTimeMillis();
        try {
            mDevice.executeShellCommand("svc data disable");
            write("\n010" + "\t" + "photos" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
        } catch (IOException e) {
            e.printStackTrace();
        }
        startApp(CameraPackageName, timeOut);
        //后置摄像头拍10张
        UiObject backshutterButton = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/shutter_image"));
        if (backshutterButton.exists()) {
            int i;
            for (i = 0; i < SagerealBatteryKPIBefore.takePicture_count; i++) {
                try {
                    backshutterButton.clickAndWaitForNewWindow();
                    sleep(60 * 1000);
                } catch (UiObjectNotFoundException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            UiObject backshutterButton2 = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/shutter_button"));
            if (backshutterButton2.exists()) {
                for (int i = 0; i < SagerealBatteryKPIBefore.takePicture_count; i++) {
                    try {
                        Log.d(TAG, "take pictures main i== " + i);
                        backshutterButton2.clickAndWaitForNewWindow();
                        sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (UiObjectNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //如果有前置摄像头的话,切换摄像头，前置摄像头拍10张，
        UiObject switcherButton = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/camera_switcher"));
        try {
            if (switcherButton.exists()) {
                switcherButton.clickAndWaitForNewWindow();
            }
            sleep(1 * 1000);
            UiObject switcherButton2 = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/onscreen_camera_picker"));
            if (switcherButton2.exists()) {
                switcherButton2.clickAndWaitForNewWindow();
                sleep(60 * 1000);
            }
            sleep(1 * 1000);
            //前置摄像头拍10张
            UiObject frontshutterButton = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/shutter_image"));
            if (frontshutterButton.exists()) {
                for (int i = 0; i < SagerealBatteryKPIBefore.takePicture_count; i++) {
                    Log.d(TAG, "take pictures sub i== " + i);
                    frontshutterButton.clickAndWaitForNewWindow();
                    sleep(60 * 1000);
                }
            } else {
                UiObject frontshutterButton2 = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/shutter_button"));
                if (frontshutterButton2.exists()) {
                    int i;
                    for (i = 0; i < SagerealBatteryKPIBefore.takePicture_count; i++) {
                        frontshutterButton2.clickAndWaitForNewWindow();
                        sleep(60 * 1000);
                    }
                }
            }
//        wk.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        try {
            closeAPP(CameraPackageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        try {
            write("|" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
            sleep(5 * 1000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "test 010 quit");
//        mDeviceSleep();


        isEnd = true;
        assertTrue("test 010 end" , isEnd);
        Log.d(TAG, "test 010 assertTrue end");


    }

    /**
     * video
     */
    @Test
    public void test_011() {
        Log.d(TAG, "test 011 video");
        boolean isEnd = false;
//        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
//        wk.acquire();

//        SagerealBatteryKPIBefore.takeVideo_time = 1;//debug
        try {
            mDevice.executeShellCommand("svc data disable");
            write("\n011" + "\t" + "video" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
            startApp(CameraPackageName, timeOut);
            sleep(2 * 2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int startTime = (int) System.currentTimeMillis();
        try {
            //A15，F1
            UiObject videoshutterButton1 = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/shutter_button_video"));
            if (videoshutterButton1.exists()) {
                videoshutterButton1.clickAndWaitForNewWindow();
                sleep(SagerealBatteryKPIBefore.takeVideo_time * onemins);
                videoshutterButton1.clickAndWaitForNewWindow();
            }

            //A63,5531
            UiObject VideoButton = new UiObject(new UiSelector().textContains("video"));
            if (VideoButton.exists()) {
                VideoButton.clickAndWaitForNewWindow();
                UiObject videoshutterButton = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/shutter_image"));
                UiObject videoshutterBtnf1f = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/shutter_button"));
                if (videoshutterButton.exists()) {
                    videoshutterButton.clickAndWaitForNewWindow();
                    sleep(SagerealBatteryKPIBefore.takeVideo_time * onemins);
                    //停止录像
                    UiObject stopVideoButton = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/video_stop_shutter"));
                    if (stopVideoButton.exists()) {
                        stopVideoButton.clickAndWaitForNewWindow();
                    }

//                    //播放
//                    UiObject videoBtn = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/thumbnail"));
//                    if (videoBtn.exists()) {
//                        videoBtn.clickAndWaitForNewWindow();
//                        sleep(2 * 1000);
//                        UiObject playBtn = new UiObject(new UiSelector().resourceId("com.google.android.apps.photos:id/photos_videoplayer_play_button"));
//                        if (playBtn.exists()) {
//                            playBtn.clickAndWaitForNewWindow();
//                            sleep(SagerealBatteryKPIBefore.takeVideo_time * onemins + 5 * 1000);
//                        }
//                    }


                } else if (videoshutterBtnf1f.exists()) {//F1F
                    videoshutterBtnf1f.clickAndWaitForNewWindow();
                    Log.d(TAG, "正在录制...");
                    sleep(SagerealBatteryKPIBefore.takeVideo_time * onemins);
                    Log.d(TAG, "等待录制完成");

                    UiObject stopVideoBtnf1f = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/video_stop_shutter"));
                    if (stopVideoBtnf1f.exists()) {
                        stopVideoBtnf1f.clickAndWaitForNewWindow();
                    }


                }
            }

            //播放
            UiObject videoBtn = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/thumbnail"));
            if (videoBtn.exists()) {
                videoBtn.clickAndWaitForNewWindow();
                sleep(2 * 1000);
                UiObject playBtn = new UiObject(new UiSelector().resourceId("com.google.android.apps.photos:id/photos_videoplayer_play_button"));
                if (playBtn.exists()) {
                    playBtn.clickAndWaitForNewWindow();
                    sleep(SagerealBatteryKPIBefore.takeVideo_time * onemins + 5 * 1000);
                }
            }

            closeAPP(CameraPackageName);

//        wk.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        try {
            write("|" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
            sleep(5 * 1000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "test 011 quit");
        mDevice.pressHome();
        mDevice.pressHome();
        mDevice.pressHome();
        mDevice.pressHome();

        mDeviceSleep();

//        if (wk.isHeld()) {
//            wk.release();
//        }
        isEnd = true;
        assertTrue("test 011 end" , isEnd);
        Log.d(TAG, "test 011 assertTrue end");
    }


    /**
     * email 测试前需要提前登录相关 email 账号
     */
    @Test
    public void test_012() {
        Log.d(TAG, "test 012 email");
        boolean isEnd = false;
//        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
//        wk.acquire();
        int startTime = (int) System.currentTimeMillis();

        try {
            mDevice.executeShellCommand("svc data enable");
            write("\n012" + "\t" + "Send Email" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
            mWeakUp();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

//            SagerealBatteryKPIBefore.sendmail_count = 5;//Test

            for (int j = 0; j < SagerealBatteryKPIBefore.sendmail_count; j++) {
                Log.d(TAG, "test_012: j=" + j);
                mWeakUp();

                startApp(GmailPackageName, timeOut);
                sleep(2 * 1000);
                Log.d(TAG, "send mail count: " + SagerealBatteryKPIBefore.sendmail_count);

                UiObject compose_button = new UiObject(new UiSelector().resourceId("com.google.android.gm.lite:id/compose_button"));
                compose_button.clickAndWaitForNewWindow();
                sleep(2 * 1000);
//                SagerealBatteryKPIBefore.targetGmail = "liuxiangnan0@126.com";//TEST
                String input = SagerealBatteryKPIBefore.targetGmail;
                Log.d(TAG, "target gmail :" + SagerealBatteryKPIBefore.targetGmail);
                sleep(2 * 1000);

                for (int i = 0; i < input.length(); i++) {
                    char c = input.charAt(i);
                    Log.d(TAG, "c=: " + (int) c);
                    if (c >= 48 && c <= 57) {
                        mDevice.pressKeyCode(c - 41);
                    } else if (c >= 97 && c <= 122) {
                        mDevice.pressKeyCode(c - 68);
                    } else if (c >= 65 && c <= 90) {
                        mDevice.pressKeyCode(59);
                        sleep(20);
                        mDevice.pressKeyCode(c - 36);
                    } else if (c == 32) {
                        mDevice.pressKeyCode(62);
                    } else if (c == 64) {
                        mDevice.pressKeyCode(KEYCODE_AT);
                    } else if (c == 46) {
                        mDevice.pressKeyCode(KEYCODE_PERIOD);
                    }
                }

                UiObject subject = new UiObject(new UiSelector().resourceId("com.google.android.gm.lite:id/subject"));
                subject.setText("battery kpi test");
                UiObject comp_email = new UiObject(new UiSelector().resourceId("com.google.android.gm.lite:id/composearea_tap_trap_bottom"));
                comp_email.clickAndWaitForNewWindow();

                String emailText = "battery KPI test email";


                for (int i = 0; i < emailText.length(); i++) {
                    char c = emailText.charAt(i);
                    Log.d(TAG, "c=: " + (int) c);
                    if (c >= 48 && c <= 57) {
                        mDevice.pressKeyCode(c - 41);
                    } else if (c >= 97 && c <= 122) {
                        mDevice.pressKeyCode(c - 68);
                    } else if (c >= 65 && c <= 90) {
                        mDevice.pressKeyCode(59);
                        sleep(20);
                        mDevice.pressKeyCode(c - 36);
                    } else if (c == 32) {
                        mDevice.pressKeyCode(62);
                    } else if (c == 64) {
                        mDevice.pressKeyCode(KEYCODE_AT);
                    } else if (c == 46) {
                        mDevice.pressKeyCode(KEYCODE_PERIOD);
                    }
                }


                sleep(1 * 1000);
                UiObject send = new UiObject(new UiSelector().resourceId("com.google.android.gm.lite:id/send"));
                send.clickAndWaitForNewWindow();
                sleep(10 * 1000);
                closeAPP(GmailPackageName);
                sleep(30 * 1000);
            }
//            closeAPP(GmailPackageName);
//            wk.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        try {
            write("|" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
            sleep(5 * 1000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "test 012 quit");
//
//        if (wk.isHeld()) {
//            wk.release();
//        }
        mDeviceSleep();

        isEnd = true;
        assertTrue("test 012 end" , isEnd);
        Log.d(TAG, "test 012 assertTrue end");
    }


    @Test
    public void test_013() {
        Log.d(TAG, "test 013 ");
        boolean isEnd = false;
        if (wk.isHeld()) {//释放 WakeLock 锁
            wk.release();
        }
        try {


            mDevice.pressRecentApps();
            sleep(500);
            UiObject recentRemoveBtn = new UiObject(new UiSelector().resourceId("com.android.systemui:id/button_remove_all"));
            if (recentRemoveBtn.exists()) {
                recentRemoveBtn.clickAndWaitForNewWindow();
            }
            sleep(500);
            mDevice.pressHome();

            sleep(1 * 1000);

            setDisplayTimeandScreenLock(15);

            String batteryRunPKG = "com.example.lxn.batterykpirunservice";
            startApp(batteryRunPKG, timeOut);

            mDeviceSleep();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        isEnd = true;
        assertTrue("test 013 end" , isEnd);
        Log.d(TAG, "test 013 assertTrue end");
    }


    @AfterClass
    public static void afterClass() {
        Log.d(TAG, "after class ");
        try {

            mDevice.executeShellCommand("am broadcast -a BatteryKPIAndroidP");//

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 判短服务是否开启
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null) {
            return false;
        }

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningServiceInfos = (ArrayList<ActivityManager.RunningServiceInfo>) activityManager.getRunningServices(30);
        for (int i = 0; i < runningServiceInfos.size(); i++) {
            if (runningServiceInfos.get(i).service.getClassName().toString().equals(ServiceName)) {
                return true;
            }
        }
        return false;

    }




}