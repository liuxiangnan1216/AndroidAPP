package com.example.android.testing.uiautomator.BatteryKPIAndroidP;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
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
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;
import android.view.KeyEvent;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.view.KeyEvent.KEYCODE_AT;
import static android.view.KeyEvent.KEYCODE_PERIOD;
import static java.lang.Thread.sleep;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com
 *   > DATE: Date on 19-3-11.
 ************************************************************************/


@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class BatteryKPIAndroidP1 {

    private static final String TAG = "lxn-BatteryKPIAndroidP2";

    private static final String LAUNCHERFLAG = "com.transsion.hilauncher:id/ic";//主界面 flag

    private static final int LAUNCH_TIMEOUT = 2000;//启动超时

    private final int STEPS = 10;

    private static UiDevice mDevice = UiDevice.getInstance(getInstrumentation());

    private static int testCount = 0;

    //获取被测应用的Context
    static Context applicationContext = InstrumentationRegistry.getTargetContext().getApplicationContext();

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

    private final String CONTACT = "test";


    private static final String FILE_NAME = "/uiautomatorTest.txt";

    private static final String configFILE_NAME = "/myconfig.xml";

    public static String FilesPackageName;
    public static String MusicPackageName;


    private static final int timeOut = 10000;//5秒
    private static final int fifteenminstime = 15 * 60 * 1000;//15分钟
    private static final int tenseconds = 10 * 1000;//10秒
    private static final int thirtyseconds = 30 * 1000;//10秒
    private static final int tenmins = 30 * 1000;//10 * 60 * 1000;//10分钟
    private static final int onemins = 60 * 1000;//1分钟
    private static int testcaseNo = 0; //测试case的序列号


    int displayHeight = mDevice.getDisplayHeight();
    int displayWidth = mDevice.getDisplayWidth();

    private static String CooperatingPhoneNum;
    private static String UTDPhoneNum;
    private static String targetGmail;
    private static String myGmailNumber;
    private static String mygmailpassword;
    private static String smsMsg;
    private static String customerNumber;
    private static String qqMsg;
    private static String WeChatMsg;


    private static int openWebTest_time = 0;
    private static int call_count = 0;
    private static int call_times;
    private static int endCall_count;
    private static int endCall_times;
    private static int takePicture_count;
    private static int takeVideo_time;
    private static int playMusic_time;
    private static int playVideo_time;
    private static int sendmail_count;
    private static int playgames_time;
    private static int qqtalk_time;
    private static int bootThenStandby_time;
    private static int weChatcount;
    private static int sendSMSCount;
    private static int qqCount;
    private static int weiboTime;
    private static int qqIntervals;
    private static int weChatInterval;
    private static int smsInterval;

    private static final int SCRE_WIDTH = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).getInstance().getDisplayWidth();//获取屏幕宽度

    private static final int SCRE_HEIGHT = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).getInstance().getDisplayHeight();//获取屏幕高度

    private Context context;

    @Before
    public void before() throws InterruptedException{
        Log.d(TAG, "*************** before *****************");
        // wakeup screen
        sleep(2000);
        try {
            if (!mDevice.isScreenOn()) {
                Log.d(TAG, "isScreenOn=="+mDevice.isScreenOn());
                mDevice.wakeUp();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Device wakeUp exception!");
            e.printStackTrace();
        }

//        UiObject LauncherFlag = mDevice.findObject(new UiSelector().resourceId(LAUNCHERFLAG));
//        while (!LauncherFlag.exists()) {
//            Log.d(TAG, "return home");
//            mDevice.pressKeyCode(KeyEvent.KEYCODE_HOME);
//            sleep(1000);
//        }

        mDevice.pressKeyCode(KeyEvent.KEYCODE_HOME);
        Log.d(TAG, "return home");
        sleep(1* 1000);
    }

    public void weakUp() throws InterruptedException {
        sleep(2000);
        try {
            if (!mDevice.isScreenOn()) {
                Log.d(TAG, "isScreenOn==" + mDevice.isScreenOn());
                mDevice.wakeUp();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Device wakeUp exception!");
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void beforeClass() throws UiObjectNotFoundException, IOException, InterruptedException {
        Log.d(TAG, "beforeClass");
        //获取权限
        getInstrumentation().getUiAutomation().executeShellCommand("pm grant " + InstrumentationRegistry.getTargetContext().getPackageName() + " android.permission.WRITE_EXTERNAL_STORAGE");
        getInstrumentation().getUiAutomation().executeShellCommand("pm grant " + InstrumentationRegistry.getTargetContext().getPackageName() + " android.permission.READ_EXTERNAL_STORAGE");
        getInstrumentation().getUiAutomation().executeShellCommand("pm grant " + InstrumentationRegistry.getTargetContext().getPackageName() + " android.permission.MODIFY_AUDIO_SETTINGS");
        try {
            if (!mDevice.isScreenOn()) {
                Log.d(TAG, "isScreenOn=="+mDevice.isScreenOn());
                mDevice.wakeUp();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Device wakeUp exception!");
            e.printStackTrace();
        }

        //获取包名
        getlistpackages();
//        //设置手机熄灭屏幕时间，除去熄灭屏幕解锁方式 //测试之前请手动设置
//        setDisplayTimeandScreenLock();
        AudioManager mAudioManager = (AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE);
        int streamMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,streamMaxVolume,1);
        mDevice.executeShellCommand(setbrightness_255);
        // 序号	测试项描述	测试开始时间	电池电压（mV）	电池电量(%)	测试结束时间	电池电压（mV）	电池电量(%) 单项测试时长（mins）	备注
        write("SerialNumber" + "\t" + "Description" + "\t" + "StartTime" + "\t" + "Voltage(mV)" + "\t" + "Electricity(%)" + "\t" + "EndTime" + "\t" + "Voltage(mV)" + "\t" + "Electricity(%)" + "\t" + "ItemTestTime(mins)" + "\t" + "Note" + "\t" + "\n");
        //测试开始前获取config文件信息
        getcongif();
    }

    //得到文件管理器的包名，确定是filesgo还是filemanager
    public static void getlistpackages() {
        List<PackageInfo> packages = InstrumentationRegistry.getContext().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            String tmpInfo = null;
            tmpInfo = packageInfo.packageName;
            if (tmpInfo.contains("filemanager")) {
                FilesPackageName = tmpInfo;
            } else if (tmpInfo.contains("files")) {
                FilesPackageName = tmpInfo;
            }
            if (tmpInfo.equals("com.android.music")) {
                MusicPackageName = tmpInfo;
            } else if (tmpInfo.equals("com.google.android.music")) {
                MusicPackageName = tmpInfo;
            }
            Log.d(TAG, "getlistpackages: " + tmpInfo);
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
            File targetFile = new File(sdCardDir.getCanonicalPath() + FILE_NAME);
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
     * 获取myconfig.xml文件
     */
    public static void getcongif() {
        Log.d(TAG, "getcongif: ");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 获取SD卡对应的存储目录
            File sdCardDir = Environment.getExternalStorageDirectory();
            System.out.println("----------------" + sdCardDir);
            // 获取指定文件对应的输入流
            try {
                FileInputStream fis = new FileInputStream(sdCardDir.getCanonicalPath() + configFILE_NAME);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                parseXMLWithPull(br);
                br.close();
                fis.close();
            } catch (IOException e) {
                Log.d(TAG, "getcongif: IOException e");
                e.printStackTrace();
            }
        }

    }

    /**
     * 解析xml文件
     * @param bufferedReader
     */
    public static void parseXMLWithPull(BufferedReader bufferedReader) {
        Log.d(TAG, "parseXMLWithPull: ");
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(bufferedReader /*new StringReader(xmlData)*/);
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    // 开始解析某个结点
                    case XmlPullParser.START_TAG: {
                        if ("bootThenStandby_time".equals(nodeName)) {
                            bootThenStandby_time = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("CooperatingPhoneNum".equals(nodeName)) {
                            CooperatingPhoneNum = xmlPullParser.nextText();
                        } else if ("UTDPhoneNum".equals(nodeName)) {
                            UTDPhoneNum = xmlPullParser.nextText();
                        } else if ("targetGmail".equals(nodeName)) {
                            targetGmail = xmlPullParser.nextText();
                        } else if ("myGmailNumber".equals(nodeName)) {
                            myGmailNumber = xmlPullParser.nextText();
                        } else if ("mygmailpassword".equals(nodeName)) {
                            mygmailpassword = xmlPullParser.nextText();
                        } else if ("openWebTest_time".equals(nodeName)) {
                            openWebTest_time = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("qqtalk_time".equals(nodeName)) {
                            qqtalk_time = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("call_count".equals(nodeName)) {
                            call_count = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("call_times".equals(nodeName)) {
                            call_times = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("endCall_count".equals(nodeName)) {
                            endCall_count = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("endCall_times".equals(nodeName)) {
                            endCall_times = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("playMusic_time".equals(nodeName)) {
                            playMusic_time = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("takePicture_count".equals(nodeName)) {
                            takePicture_count = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("playVideo_time".equals(nodeName)) {
                            playVideo_time = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("playgames_time".equals(nodeName)) {
                            playgames_time = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("sendmail_count".equals(nodeName)) {
                            sendmail_count = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("takeVideo_time".equals(nodeName)) {
                            takeVideo_time = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("weiboTime".equals(nodeName)) {
                            weiboTime = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("qqCount".equals(nodeName)) {
                            qqCount = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("qqIntervals".equals(nodeName)) {
                            qqIntervals = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("customerNumber".equals(nodeName)) {
                            customerNumber = xmlPullParser.nextText();
                        } else if ("smsMsg".equals(nodeName)) {
                            smsMsg = xmlPullParser.nextText();
                        } else if ("qqMsg".equals(nodeName)) {
                            qqMsg = xmlPullParser.nextText();
                        } else if ("weChatInterval".equals(nodeName)) {
                            weChatInterval = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("weChatcount".equals(nodeName)) {
                            weChatcount = Integer.parseInt(xmlPullParser.nextText());
                        } else if("WeChatMsg".equals(nodeName)) {
                            WeChatMsg = xmlPullParser.nextText();
                        } else if ("smsInterval".equals(nodeName)) {
                            smsInterval = Integer.parseInt(xmlPullParser.nextText());
                        } else if ("sendSMSCount".equals(nodeName)) {
                            sendSMSCount = Integer.parseInt(xmlPullParser.nextText());
                        }
                        break;
                    }
                    // 完成解析某个结点
                    case XmlPullParser.END_TAG: {
                        if ("resources".equals(nodeName)) {
                            Log.d(TAG, "XmlPullParser\n" +
                                    "bootThenStandby_time:  " + bootThenStandby_time + "\n" +
                                    "CooperatingPhoneNum:  " + CooperatingPhoneNum + "\n" +
                                    "UTDPhoneNum:  " + UTDPhoneNum + "\n" +
                                    "targetGmail:  " + targetGmail + "\n" +
                                    "myGmailNumber:  " + myGmailNumber + "\n" +
                                    "mygmailpassword:  " + mygmailpassword + "\n" +
                                    "openWebTest_time:  " + openWebTest_time + "\n" +
                                    "qqtalk_time:  " + qqtalk_time + "\n" +
                                    "call_count:  " + call_count + "\n" +
                                    "call_times:  " + call_times + "\n" +
                                    "endCall_count:  " + endCall_count + "\n" +
                                    "endCall_times:  " + endCall_times + "\n" +
                                    "playMusic_time:  " + playMusic_time + "\n" +
                                    "takePicture_count:  " + takePicture_count + "\n" +
                                    "playVideo_time:  " + playVideo_time + "\n" +
                                    "playgames_time:  " + playgames_time + "\n" +
                                    "sendmail_count:  " + sendmail_count + "\n" +
                                    "takeVideo_time:  " + takeVideo_time + "\n" +
                                    "weiboTime:  " + weiboTime + "\n" +
                                    "qqCount:  " + qqCount + "\n" +
                                    "qqIntervals:  " + qqIntervals + "\n" +
                                    "customerNumber:  " + customerNumber + "\n" +
                                    "smsMsg:  " + smsMsg + "\n" +
                                    "qqMsg:  " + qqMsg + "\n" +
                                    "WeChatMsg:  " + WeChatMsg + "\n"

                            );
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            Log.d(TAG, "parseXMLWithPull: Exception e");
            e.printStackTrace();
        }
    }

    /**
     * 获取时间信息
     * @return
     */
    public String getTimeInfo() {
        SimpleDateFormat format = new SimpleDateFormat("y-M-d  H:m:s");
        String time = format.format(Calendar.getInstance().getTime());
        Log.d(TAG, "完整的时间和日期： " + time);
        String testtime = time;
        return testtime;
    }

    /**
     * 获取电池信息
     * @return
     * @throws IOException
     */
    public String getBatteryInfo() throws IOException{
        Log.d(TAG, "getBatteryInfo");
        String batteryInfo = null;
        batteryInfo = mDevice.executeShellCommand("dumpsys battery");
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
     * @throws IOException
     */
    public String getBatterryElectric() throws IOException {
        Log.d(TAG, "getBatterryElectric");
        String batteryinfo = null;
        batteryinfo = mDevice.executeShellCommand("dumpsys battery");
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
     * @throws IOException
     */
    public String getBatterryVoltage() throws IOException {
        Log.d(TAG, "getBatterryVoltage");
        String batteryinfo = null;
        batteryinfo = mDevice.executeShellCommand("dumpsys battery");
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
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
    public static void adjustVolume(boolean up, int times) throws UiObjectNotFoundException, InterruptedException {
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




    /***************************************测试 CASE  begin**************************************************/


    /**
     * call
     * @throws InterruptedException
     * @throws UiObjectNotFoundException
     * @throws IOException
     */
    @Test
    public void test_001() throws InterruptedException, UiObjectNotFoundException, IOException {
        Log.d(TAG, "test_001 call call_count==" + call_count);
        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wk.acquire();
        int startTime = (int) System.currentTimeMillis();
        write("001" + "\t" + "Call test" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");

        for (int i = 0; i < call_count; i++) {
            mDevice.executeShellCommand(dialCMD + CooperatingPhoneNum);
            setSpeakerphoneOn(true);
            sleep(call_times * onemins + 10 * 1000);
            try {
                Log.d(TAG, "isScreenOn==" + mDevice.isScreenOn());
                if (!mDevice.isScreenOn()) {
                    Log.d(TAG, "isScreenOn==" + mDevice.isScreenOn());
                    mDevice.wakeUp();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Device wakeUp exception!");
                e.printStackTrace();
            }
            mDevice.executeShellCommand(endCall);
        }
        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        wk.release();

        write(getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        Log.d(TAG, "test 001 quit");

    }

    /**
     * play game
     * @throws InterruptedException
     * @throws UiObjectNotFoundException
     * @throws IOException
     */
    @Test
    public void test_002() throws InterruptedException, UiObjectNotFoundException, IOException {
        Log.d(TAG, "test 002 play game");
        mDevice.executeShellCommand("svc data disable");
        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
        wk.acquire();
        int startTime = (int) System.currentTimeMillis();
        write("\n002" + "\t" + "play game" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
        startApp(GamesPackageName, timeOut);
        sleep(5000);
        mDevice.click((int)(SCRE_WIDTH * 0.17), (int)(SCRE_WIDTH * 0.63));
        int endTime = (int) System.currentTimeMillis();
        while ((endTime - startTime) <= playgames_time * onemins) {
            int startx = (int)(Math.random() * 720);
            int starty = (int)(Math.random() * 1280);
            Log.d(TAG, "startx:" + startx + " starty:" + starty);
            mDevice.swipe(startx, starty, startx - 5, starty - 20, 10);
            sleep(5000);
            endTime = (int) System.currentTimeMillis();
            Log.d(TAG, "times=" + ((endTime - startTime)));
        }
        closeAPP(GamesPackageName);
        wk.release();

        endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        write(getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        Log.d(TAG, "test 002 quit");


    }

    /**
     * weChat
     * @throws InterruptedException
     * @throws UiObjectNotFoundException
     * @throws IOException
     */
    @Test
    public void test_003() throws InterruptedException, UiObjectNotFoundException, IOException {
        Log.d(TAG, "test 003 weChat");
        mDevice.executeShellCommand("svc data enable");
        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wk.acquire();
        int startTime = (int) System.currentTimeMillis();
        write("\n003" + "\t" + "weChat" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
        while (weChatcount > 0) {
            weChatcount--;
            weakUp();
            startApp(weChatPackageName, timeOut);
            sleep(3000);
            UiObject conBtn = new UiObject(new UiSelector().text("Contacts"));
            conBtn.clickAndWaitForNewWindow();
            UiObject test = new UiObject(new UiSelector().text("test"));
            test.clickAndWaitForNewWindow();
            UiObject sendMsg = new UiObject(new UiSelector().text("Send Message"));
            sendMsg.clickAndWaitForNewWindow();
            UiObject edit = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/amb"));
            edit.clickAndWaitForNewWindow();
            edit.setText(WeChatMsg);
            UiObject sendBtn = new UiObject(new UiSelector().text("Send"));
            sendBtn.clickAndWaitForNewWindow();
            closeAPP(weChatPackageName);
//            wk.release();
            sleep(weChatInterval * onemins);
        }

        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        write(getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        closeAPP(weChatPackageName);
        wk.release();
        Log.d(TAG, "test 003 quit");

    }


    /**
     * SMS
     * @throws InterruptedException
     * @throws UiObjectNotFoundException
     * @throws IOException
     */
    @Test
    public void test_004() throws InterruptedException, UiObjectNotFoundException, IOException {
        Log.d(TAG, "test 004 SMS");
        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wk.acquire();
        mDevice.executeShellCommand("svc data disable");
        int startTime = (int) System.currentTimeMillis();
        write("\n004" + "\t" + "SMS" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
        weakUp();
        while (sendSMSCount > 0) {
            sendSMSCount--;
            weakUp();
            mDevice.executeShellCommand(String.format("am start -a android.intent.action.SENDTO -d sms:%s --es sms_body %s", CooperatingPhoneNum, smsMsg));
            sleep(2000);
            UiObject sendBtn = new UiObject(new UiSelector().resourceId("com.google.android.apps.messaging:id/send_message_button_icon"));
            sendBtn.clickAndWaitForNewWindow();
            sleep(2000);
            closeAPP(smspackageName);
            sleep(smsInterval * onemins);
        }
        weakUp();
        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        wk.release();
        write(getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        Log.d(TAG, "test 004 quit");

    }

    /**
     * qq
     * 测试之前请将指定联系人置顶
     * @throws InterruptedException
     * @throws UiObjectNotFoundException
     * @throws IOException
     */
    @Test
    public void test_005() throws InterruptedException, UiObjectNotFoundException, IOException {
        Log.d(TAG, "test 005 qq qqCount====" + qqCount);
        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wk.acquire();
        mDevice.executeShellCommand("svc data enable");
        int startTime = (int) System.currentTimeMillis();
        write("\n005" + "\t" + "QQ" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
        weakUp();

        while (qqCount > 0) {
            Log.d(TAG, "qq count:" + qqCount);
            qqCount--;
            weakUp();
            startApp(QQPackageName, timeOut);
            sleep(5 * 1000);
            UiObject contact = new UiObject(new UiSelector().resourceId("com.tencent.mobileqq:id/relativeItem").index(0));
            contact.clickAndWaitForNewWindow();
            UiObject edit = new UiObject(new UiSelector().resourceId("com.tencent.mobileqq:id/input"));
            edit.setText(qqMsg);
            UiObject send = new UiObject(new UiSelector().resourceId("com.tencent.mobileqq:id/fun_btn"));
            send.clickAndWaitForNewWindow();
            sleep(2 * 1000);
            closeAPP(QQPackageName);
            wk.release();
            sleep(qqIntervals * onemins);
        }

        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        write(getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        closeAPP(QQPackageName);
        Log.d(TAG, "test 005 quit");


    }


    /**
     * web
     */
    @Test
    public void test_006() throws InterruptedException, UiObjectNotFoundException, IOException {
        Log.d(TAG, "test 006 web openWebTest_time==" + openWebTest_time);
        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wk.acquire();
        mDevice.executeShellCommand("svc data enable");
        int startTime = (int) System.currentTimeMillis();
        write("\n006" + "\t" + "web" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
        weakUp();
        startApp(ChormePackageName, timeOut); //启动app
        sleep(2 * 1000);
        //得到浏览器中的网页输入框
        UiObject edit = new UiObject(new UiSelector().className("android.widget.EditText"));
        if (edit.exists()) {
            edit.clickAndWaitForNewWindow();
        }
        //edit.clearTextField();
        mDevice.pressDelete();
        edit.setText("https://www.baidu.com");
        mDevice.pressEnter();//回车进行浏览，在部分手机不支持回车浏览，可以使用上面的方式得到浏览按钮在点击进行浏览
        int endTime = (int)System.currentTimeMillis();
        while ((endTime - startTime) < openWebTest_time * onemins) {
//            mDevice.swipe(300, 600, 300, 200, 10);
            slideUp();//上滑动
            sleep(5 * 1000);
            endTime = (int) System.currentTimeMillis();
            Log.d(TAG, "endTime - startTime ====" + (endTime - startTime));
        }

        endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        write(getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        closeAPP(ChormePackageName);
        wk.release();
        Log.d(TAG, "test 006 quit");


    }

    /**
     * weibo
     */
    @Test
    public void test_007() throws InterruptedException, UiObjectNotFoundException, IOException {
        Log.d(TAG, "test 007 weibo");
        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
        wk.acquire();
        mDevice.executeShellCommand("svc data enable");
        int startTime = (int) System.currentTimeMillis();
        write("\n007" + "\t" + "weibo" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
        startApp(weiboPackageName, timeOut);
        sleep(2000);
        int endTime = (int) System.currentTimeMillis();
        while (endTime - startTime < weiboTime * 60 * 1000) {
            endTime = (int) System.currentTimeMillis();
            mDevice.swipe(300, 600, 300, 200, 10);
            sleep(20 * 1000);
            Log.d(TAG, "endTime - startTime====" + (endTime - startTime));
        }
        closeAPP(weiboPackageName);
        wk.release();

        endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        write(getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        Log.d(TAG, "test 007 quit");


    }


    /**
     * photos
     * @throws InterruptedException
     * @throws UiObjectNotFoundException
     * @throws IOException
     */
    @Test
    public void test_008() throws InterruptedException, UiObjectNotFoundException, IOException {
        Log.d(TAG, "test 008 photos");
        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
        wk.acquire();

        int startTime = (int) System.currentTimeMillis();
        mDevice.executeShellCommand("svc data disable");
        write("\n008" + "\t" + "photos" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");

        startApp(CameraPackageName, timeOut);
        //后置摄像头拍10张
        UiObject backshutterButton = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/shutter_image"));
        if (backshutterButton.exists()) {
            int i;
            for (i = 0; i < takePicture_count; i++) {
                backshutterButton.clickAndWaitForNewWindow();
                sleep(60 * 1000);
            }
        } else {
            UiObject backshutterButton2 = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/shutter_button"));
            if (backshutterButton2.exists()) {
                int i;
                for (i = 0; i < takePicture_count; i++) {
                    backshutterButton2.clickAndWaitForNewWindow();
                    sleep(60 * 1000);
                }
            }
        }
        //如果有前置摄像头的话,切换摄像头，前置摄像头拍10张，
        UiObject switcherButton = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/camera_switcher"));
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
            int i;
            for (i = 0; i < takePicture_count; i++) {
                frontshutterButton.clickAndWaitForNewWindow();
                sleep(60 * 1000);
            }
        } else {
            UiObject frontshutterButton2 = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/shutter_button"));
            if (frontshutterButton2.exists()) {
                int i;
                for (i = 0; i < takePicture_count; i++) {
                    frontshutterButton2.clickAndWaitForNewWindow();
                    sleep(60 * 1000);
                }
            }
        }



        closeAPP(CameraPackageName);
        wk.release();

        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        write(getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        Log.d(TAG, "test 008 quit");


    }

    /**
     * video
     * @throws InterruptedException
     * @throws UiObjectNotFoundException
     * @throws IOException
     */
    @Test
    public void test_009() throws InterruptedException, UiObjectNotFoundException, IOException {
        Log.d(TAG, "test 009 video");
        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
        wk.acquire();

        mDevice.executeShellCommand("svc data disable");
        int startTime = (int) System.currentTimeMillis();
        write("\n009" + "\t" + "video" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
        startApp(CameraPackageName, timeOut);
        sleep(2 * 2000);
        //A15，F1
        UiObject videoshutterButton1 = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/shutter_button_video"));
        if (videoshutterButton1.exists()) {
            videoshutterButton1.clickAndWaitForNewWindow();
            sleep(takeVideo_time);
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
                sleep(takeVideo_time);
                //停止录像
                UiObject stopVideoButton = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/video_stop_shutter"));
                if (stopVideoButton.exists()) {
                    stopVideoButton.clickAndWaitForNewWindow();
                }
            } else if (videoshutterBtnf1f.exists()) {//F1F
                videoshutterBtnf1f.clickAndWaitForNewWindow();
                Log.d(TAG, "正在录制...");
                sleep(takeVideo_time);
                Log.d(TAG, "等待录制完成");

                UiObject stopVideoBtnf1f = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/video_stop_shutter"));
                if (stopVideoBtnf1f.exists()) {
                    stopVideoBtnf1f.clickAndWaitForNewWindow();
                }
                //播放
                UiObject videoBtn = new UiObject(new UiSelector().resourceId("com.mediatek.camera:id/thumbnail"));
                if (videoBtn.exists()) {
                    videoBtn.clickAndWaitForNewWindow();
                    sleep(2 * 1000);
                    UiObject playBtn = new UiObject(new UiSelector().resourceId("com.google.android.apps.photos:id/photos_videoplayer_play_button"));
                    if (playBtn.exists()) {
                        playBtn.clickAndWaitForNewWindow();
                        sleep(takeVideo_time + 5 * 1000);
                    }
                }

            }
        }


        wk.release();
        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        write(getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        Log.d(TAG, "test 009 quit");


    }




    /**
     * mp3 提前将资源文件放入 sdcard/Music/目录下
     * @throws InterruptedException
     * @throws UiObjectNotFoundException
     * @throws IOException
     */
    @Test
    public void test_010() throws InterruptedException, UiObjectNotFoundException, IOException {
        Log.d(TAG, "test 010 mp3");
        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wk.acquire();
        mDevice.executeShellCommand("svc data disable");
        int startTime = (int) System.currentTimeMillis();
        write("\n010" + "\t" + "mp3" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
        //使用扬声器外放，即使已经插入耳机
        AudioManager mAudioManager = (AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Log.d(TAG, "test_010: maxVolume=" + maxVolume);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0); //音量调到最大
        mAudioManager.setMicrophoneMute(false);
        mAudioManager.setSpeakerphoneOn(false);
        mAudioManager.setMode(AudioManager.STREAM_MUSIC);

        startApp(MusicPackageName, timeOut);
        sleep(10 * 1000);
        if (MusicPackageName.equals(googledMusicPackageName)) {
            Log.d(TAG, "test_010: open googledMusic ");
            //左滑
            mDevice.swipe(displayWidth - 1, displayHeight / 2, displayWidth / 3, displayHeight / 2, displayWidth / 9);
            mDevice.swipe(displayWidth - 1, displayHeight / 2, displayWidth / 3, displayHeight / 2, displayWidth / 9);
            UiObject Shuffle_all = new UiObject(new UiSelector().text("Shuffle all"));
            Shuffle_all.clickAndWaitForNewWindow();
        } else if (MusicPackageName.equals(androidMusicPackageName)) {
            Log.d(TAG, "test_010: open androidMusic ");
            UiObject Playlists = new UiObject(new UiSelector().text("Playlists"));
            Playlists.clickAndWaitForNewWindow();
            UiObject Recently = new UiObject(new UiSelector().text("Recently added"));
            Recently.clickAndWaitForNewWindow();
            UiObject options = new UiObject(new UiSelector().description("More options").className("android.widget.ImageButton"));
            options.clickAndWaitForNewWindow();
            UiObject shuffle = new UiObject(new UiSelector().text("Party shuffle"));
            shuffle.clickAndWaitForNewWindow();
        }

        sleep(playMusic_time * onemins);
        mDevice.pressHome();
        Log.d(TAG, "test_010: play music end");
        sleep(2 * 2000);
        closeAPP(MusicPackageName);
        wk.release();

        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        write(getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        Log.d(TAG, "test 010 quit");


    }

    /**
     * mp4 提前将资源文件放入 sdcard/Moves/目录下
     * @throws InterruptedException
     * @throws UiObjectNotFoundException
     * @throws IOException
     */
    @Test
    public void test_011() throws InterruptedException, UiObjectNotFoundException, IOException {
        Log.d(TAG, "test 011 mp4");
        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
        wk.acquire();
        mDevice.executeShellCommand("svc data disable");
        int startTime = (int) System.currentTimeMillis();
        write("\n011" + "\t" + "mp4" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
        startApp(FilesPackageName, timeOut);

        //使用扬声器外放，即使已经插入耳机   耳机、外放都有声音
        AudioManager mAudioManager = (AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Log.d(TAG, "test_011: maxVolume=" + maxVolume);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0); //音量调到最大
        mAudioManager.setMicrophoneMute(false);
        mAudioManager.setSpeakerphoneOn(true);
        mAudioManager.setMode(AudioManager.STREAM_MUSIC);

        if (FilesPackageName.contains("files")) {
            if (mDevice.hasObject(By.text("CONTINUE"))) {
                Log.d(TAG, "test_011: into CONTINUE");
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
            mDevice.swipe(displayWidth / 2, displayHeight / 4 * 3, displayWidth / 2, displayHeight / 4, displayHeight / 4);
            mDevice.swipe(displayWidth / 2, displayHeight / 4 * 3, displayWidth / 2, displayHeight / 4, displayHeight / 4);
            mDevice.swipe(displayWidth / 2, displayHeight / 4 * 3, displayWidth / 2, displayHeight / 4, displayHeight / 4);
            mDevice.swipe(displayWidth / 2, displayHeight / 4 * 3, displayWidth / 2, displayHeight / 4, displayHeight / 4);
            int flag = 0;
            UiObject VideoButton = new UiObject(new UiSelector().textContains("Videos"));
            VideoButton.clickAndWaitForNewWindow();
            //如果filesgo版本比较新，有田字型switch按钮
            if (mDevice.hasObject(By.res("com.google.android.apps.nbu.files:id/view_mode_switch"))) {
                UiObject BrowseButton = new UiObject(new UiSelector().resourceId("com.google.android.apps.nbu.files:id/view_mode_switch"));
                BrowseButton.clickAndWaitForNewWindow();
                mDevice.swipe(displayWidth / 2, displayHeight / 2, displayWidth / 2, displayHeight / 7, displayHeight / 20);
                sleep(2 * 1000);
            } else {
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
            for (int i = 0; i < (playVideo_time); i++) {
                Log.d(TAG, "test_011: playVideo_time / fifteenminstime ===" + (playVideo_time / fifteenminstime));
                flag++;
                Log.d(TAG, "flag: " + flag);
                UiObject Button720p = new UiObject(new UiSelector().textContains("720P.mp4"));
                while (!Button720p.exists()) {
                    mDevice.swipe((int)(SCRE_WIDTH * 0.5), (int)(SCRE_HEIGHT * 0.8), (int)(SCRE_WIDTH * 0.5), (int)(SCRE_HEIGHT * 0.2), 10);
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
                sleep(fifteenminstime);
                mDevice.pressBack();
            }
            closeAPP(FilesPackageName);
        }
        //如果预置的是filemanager
        else if (FilesPackageName.contains("filemanager")) {
            UiObject InternalItem = new UiObject(new UiSelector().textContains("Internal"));
            InternalItem.clickAndWaitForNewWindow();
            mDevice.swipe(displayWidth / 2, displayHeight / 4 * 3, displayWidth / 2, displayHeight / 4, displayHeight / 4);
            mDevice.swipe(displayWidth / 2, displayHeight / 4 * 3, displayWidth / 2, displayHeight / 4, displayHeight / 4);
            mDevice.swipe(displayWidth / 2, displayHeight / 4 * 3, displayWidth / 2, displayHeight / 4, displayHeight / 4);
            mDevice.swipe(displayWidth / 2, displayHeight / 4 * 3, displayWidth / 2, displayHeight / 4, displayHeight / 4);
            int i;
            int flag = 0;
            //15分钟的视频，循环播放4次
            for (i = 0; i < 4; i++) {
                flag++;
                Log.d(TAG, "flag: " + flag);
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
                sleep(fifteenminstime);
                mDevice.pressBack();
            }
            closeAPP(FilesPackageName);
        }
        wk.release();

        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        write(getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        Log.d(TAG, "test 011 quit");


    }



    /**
     * email 测试前需要提前登录相关 email 账号
     * @throws InterruptedException
     * @throws UiObjectNotFoundException
     * @throws IOException
     */
//    @Test TODO
    public void test_012() throws InterruptedException, UiObjectNotFoundException, IOException {
        Log.d(TAG, "test 012 email");
        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wk.acquire();
        mDevice.executeShellCommand("svc data enable");
        int startTime = (int) System.currentTimeMillis();
        write("\n012" + "\t" + "Send Email" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");
        weakUp();
        startApp(GmailPackageName, timeOut);
        sleep(2 * 10000);

        for (int j = 0; j < sendmail_count; j++) {
            Log.d(TAG, "test_012: j=" + j);
            weakUp();
            UiObject compose_button = new UiObject(new UiSelector().resourceId("com.google.android.gm.lite:id/compose_button"));
            compose_button.clickAndWaitForNewWindow();
            sleep(2 * 1000);
            String input = targetGmail;
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
            UiObject send = new UiObject(new UiSelector().resourceId("com.google.android.gm.lite:id/send"));
            send.clickAndWaitForNewWindow();
            sleep(5 * 1000);
        }
        closeAPP(GmailPackageName);
        wk.release();
        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        write(getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t");
        Log.d(TAG, "test 012 quit");


    }






    /**
     * standby
     * @throws InterruptedException
     * @throws UiObjectNotFoundException
     * @throws IOException
     */
    @Test
    public void test_013() throws InterruptedException, UiObjectNotFoundException, IOException {
        Log.d(TAG, "test 013 standby");
        PowerManager pm = (PowerManager) applicationContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wk.acquire();

        int startTime = (int) System.currentTimeMillis();
        write("\n013" + "\t" + "standby" + "\t" + getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t");

        sleep(bootThenStandby_time * onemins);

        int endTime = (int) System.currentTimeMillis();
        int testTime = calculateTime(endTime, startTime);
        write(getTimeInfo() + "\t" + getBatterryVoltage() + "\t" + getBatterryElectric() + "\t" + testTime + "\t" + "test finish" + "\t\n");
        Log.d(TAG, "test 013 quit");
        wk.release();

    }


    @Test
    public void test_014 () throws InterruptedException{
        //do nothing
        sleep( 10 * 1000);
    }



}