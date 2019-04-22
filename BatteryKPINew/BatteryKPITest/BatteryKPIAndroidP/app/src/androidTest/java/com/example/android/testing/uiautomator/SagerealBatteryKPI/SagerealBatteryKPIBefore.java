package com.example.android.testing.uiautomator.SagerealBatteryKPI;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Environment;
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
import android.util.Log;

import org.junit.After;
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
import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 19-3-12.
 ************************************************************************/

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class SagerealBatteryKPIBefore {
    private static final String TAG = "lxn-SagerealBatteryKPIBefore";

    private static UiDevice mDevice = UiDevice.getInstance(getInstrumentation());

    //获取被测应用的Context
    static Context applicationContext = InstrumentationRegistry.getTargetContext().getApplicationContext();

    private static final int SCRE_WIDTH = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).getInstance().getDisplayWidth();//获取屏幕宽度
    private static final int SCRE_HEIGHT = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).getInstance().getDisplayHeight();//获取屏幕高度

    public static String FILE_NAME = "/uiautomatorTest.txt";
    private static final String configFILE_NAME = "/myconfig.xml";

    private static final String settingsPackageName = "com.android.settings";

    public static String FilesPackageName;
    public static String MusicPackageName;


    public static String CooperatingPhoneNum;
    public static String UTDPhoneNum;
    public static String targetGmail;
    public static String myGmailNumber;
    public static String mygmailpassword;
    public static String smsMsg;
    public static String customerNumber;
    public static String qqMsg;
    public static String WeChatMsg;


    public static int openWebTest_time = 0;
    public static int call_count = 0;
    public static int call_times;
    public static int endCall_count;
    public static int endCall_times;
    public static int takePicture_count;
    public static int takeVideo_time;
    public static int playMusic_time;
    public static int playVideo_times;
    public static int sendmail_count;
    public static int playgames_time;
    public static int qqtalk_time;
    public static int bootThenStandby_time;
    public static int weChatcount;
    public static int sendSMSCount;
    public static int qqCount;
    public static int weiboTime;
    public static int qqIntervals;
    public static int weChatInterval;
    public static int smsInterval;
    public static int standardtest_time;

    @Before
    public void before() {
        Log.d(TAG, "before");
        // wakeup screen
        try {
            if (!mDevice.isScreenOn()) {
                Log.e(TAG, "isScreenOn==" + mDevice.isScreenOn());
                mDevice.wakeUp();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Device wakeUp exception!");
            e.printStackTrace();
        }
    }


    @After
    public void after() {
        Log.d(TAG, "*************** after ****************");
    }


    @BeforeClass
    public static void beforeClass() throws IOException {
        //测试项目开始前运行（仅一次），如清除缓存数据、安装应用等
        Log.d(TAG, "beforeClass: ");
        //获取权限
        getInstrumentation().getUiAutomation().executeShellCommand("pm grant " + InstrumentationRegistry.getTargetContext().getPackageName() + " android.permission.WRITE_EXTERNAL_STORAGE");
        getInstrumentation().getUiAutomation().executeShellCommand("pm grant " + InstrumentationRegistry.getTargetContext().getPackageName() + " android.permission.READ_EXTERNAL_STORAGE");
        getInstrumentation().getUiAutomation().executeShellCommand("pm grant " + InstrumentationRegistry.getTargetContext().getPackageName() + " android.permission.MODIFY_AUDIO_SETTINGS");
        try {
            if (!mDevice.isScreenOn()) {
                Log.e(TAG, "isScreenOn==" + mDevice.isScreenOn());
                mDevice.wakeUp();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Device wakeUp exception!");
            e.printStackTrace();
        }
        //每次开始测试前先删除测试报告
        deletefile();

        getlistpackages();

        getcongif();

        setDisplayTimeandScreenLock();//设置屏幕超时30分钟



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
                Log.d(TAG, "config file name: " + configFILE_NAME);
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

        Log.d(TAG, "\n music app pkg name :" + MusicPackageName);
    }


    /**
     * 每次测试前删除之前测试生成的测试报告
     * @throws IOException
     */
    private static void deletefile() throws IOException {
        // 如果手机插入了SD卡，而且应用程序具有访问SD的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d(TAG, "具有访问SD的权限: ");
            // 获取SD卡的目录
            File sdCardDir = Environment.getExternalStorageDirectory();
            Log.d(TAG, "sdCardDir.getCanonicalPath(): " + sdCardDir.getCanonicalPath());
            File targetFile = new File(sdCardDir.getCanonicalPath() + FILE_NAME);
            if (targetFile.exists()) {
                Log.d(TAG, "deletefile: targetFile.exists()");
                targetFile.delete();
            }

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
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Calendar calendar = Calendar.getInstance();
            FILE_NAME = "/uiautomatorTest_" + formatter.format(calendar.getTime()) + ".txt";
            File targetFile = new File(sdCardDir.getCanonicalPath() + FILE_NAME);
            Log.d(TAG, "file name :" + FILE_NAME);

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
     *
     * @throws IOException
     */
    public static void setDisplayTimeandScreenLock() throws IOException {
        startApp(settingsPackageName, 10 * 1000);
        try {
            UiObject Display = new UiObject(new UiSelector().text("Display"));
            while (!Display.exists()) {
                mDevice.swipe((int)(SCRE_WIDTH * 0.5), (int)(SCRE_HEIGHT * 0.7), (int)(SCRE_WIDTH * 0.5), (int)(SCRE_HEIGHT * 0.4), 10);
            }
            Display.clickAndWaitForNewWindow();
            UiObject Sleep = new UiObject(new UiSelector().text("Sleep"));
            Sleep.clickAndWaitForNewWindow();

            UiObject minutes = new UiObject(new UiSelector().text("30 minutes"));
            minutes.clickAndWaitForNewWindow();

            mDevice.pressBack();

            UiScrollable RecyclerView = new UiScrollable(new UiSelector().className("android.support.v7.widget.RecyclerView"));
            RecyclerView.setMaxSearchSwipes(10);//设置最大可扫动次数
            UiObject Security = new UiObject(new UiSelector().text("Security & location"));//找到“更多”这个选项
            RecyclerView.scrollIntoView(Security);//然后点击它（更多）
            Security.click();
            UiObject Screen = new UiObject(new UiSelector().text("Screen lock"));
            Screen.clickAndWaitForNewWindow();
            UiObject None = new UiObject(new UiSelector().text("None"));
            None.clickAndWaitForNewWindow();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
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
                        } else if ("playVideo_times".equals(nodeName)) {
                            playVideo_times = Integer.parseInt(xmlPullParser.nextText());
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
                        } else if ("standardtest_time".equals(nodeName)) {
                            standardtest_time = Integer.parseInt(xmlPullParser.nextText());
                        }
                        break;
                    }
                    // 完成解析某个结点
                    case XmlPullParser.END_TAG: {
                        if ("resources".equals(nodeName)) {
                            write("XmlPullParser\n******************" +
                                    "\nCooperatingPhoneNum:  " + CooperatingPhoneNum +
                                    "\nUTDPhoneNum:  " + UTDPhoneNum + 
                                    "\ntargetGmail:  " + targetGmail + 
                                    "\nmyGmailNumber:  " + myGmailNumber + 
                                    "\nmygmailpassword:  " + mygmailpassword + 
                                    "\nopenWebTest_time:  " + openWebTest_time + 
                                    "\nqqtalk_time:  " + qqtalk_time + 
                                    "\ncall_count:  " + call_count + 
                                    "\ncall_times:  " + call_times + 
                                    "\nendCall_count:  " + endCall_count + 
                                    "\nendCall_times:  " + endCall_times + 
                                    "\nplayMusic_time:  " + playMusic_time + 
                                    "\ntakePicture_count:  " + takePicture_count +
                                    "\nplayVideo_time:  " + playVideo_times +
                                    "\nplaygames_time:  " + playgames_time +
                                    "\nsendmail_count:  " + sendmail_count +
                                    "\ntakeVideo_time:  " + takeVideo_time +
                                    "\nweiboTime:  " + weiboTime +
                                    "\nqqCount:  " + qqCount +
                                    "\nqqIntervals:  " + qqIntervals +
                                    "\ncustomerNumber:  " + customerNumber +
                                    "\nsmsMsg:  " + smsMsg +
                                    "\nqqMsg:  " + qqMsg +
                                    "\nWeChatMsg:  " + WeChatMsg +
                                    "\nstandardtest_time :" + standardtest_time +
                                    "\n******************************\n\n");
                            Log.d(TAG, "XmlPullParser" +
                                    "\nbootThenStandby_time:  " + bootThenStandby_time +
                                    "\nCooperatingPhoneNum:  " + CooperatingPhoneNum +
                                    "\nUTDPhoneNum:  " + UTDPhoneNum +
                                    "\ntargetGmail:  " + targetGmail +
                                    "\nmyGmailNumber:  " + myGmailNumber +
                                    "\nmygmailpassword:  " + mygmailpassword +
                                    "\nopenWebTest_time:  " + openWebTest_time +
                                    "\nqqtalk_time:  " + qqtalk_time +
                                    "\ncall_count:  " + call_count +
                                    "\ncall_times:  " + call_times +
                                    "\nendCall_count:  " + endCall_count +
                                    "\nendCall_times:  " + endCall_times +
                                    "\nplayMusic_time:  " + playMusic_time +
                                    "\ntakePicture_count:  " + takePicture_count +
                                    "\nplayVideo_time:  " + playVideo_times +
                                    "\nplaygames_time:  " + playgames_time +
                                    "\nsendmail_count:  " + sendmail_count +
                                    "\ntakeVideo_time:  " + takeVideo_time +
                                    "\nweiboTime:  " + weiboTime +
                                    "\nqqCount:  " + qqCount +
                                    "\nqqIntervals:  " + qqIntervals +
                                    "\ncustomerNumber:  " + customerNumber +
                                    "\nsmsMsg:  " + smsMsg +
                                    "\nqqMsg:  " + qqMsg +
                                    "\nWeChatMsg:  " + WeChatMsg +
                                    "\nweChatcount: " + weChatcount +
                                    "\nsendSMSCount: " + sendSMSCount +
                                    "\nweChatInterval: " + weChatInterval +
                                    "\nsmsInterval: " + smsInterval +
                                    "\nstandardtest_time: " + standardtest_time + "\n"

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



    @Test
    public void before_test_001() {

        Log.d(TAG, "before_test_001");
        boolean isEnd = false;
        try {
            mDevice.pressHome();
            sleep(500);
            mDevice.pressHome();
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        isEnd = true;
        assertTrue("before_test_001" , isEnd);
        Log.d(TAG, "before_test_001 assertTrue end");

    }
}
