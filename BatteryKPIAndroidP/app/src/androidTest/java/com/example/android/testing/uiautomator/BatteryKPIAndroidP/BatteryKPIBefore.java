package com.example.android.testing.uiautomator.BatteryKPIAndroidP;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.util.Log;

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
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertTrue;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 19-3-12.
 ************************************************************************/

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class BatteryKPIBefore {
    private static final String TAG = "lxn-BatteryKPIBefore";

    private static UiDevice mDevice = UiDevice.getInstance(getInstrumentation());

    //获取被测应用的Context
    static Context applicationContext = InstrumentationRegistry.getTargetContext().getApplicationContext();

    private static final String FILE_NAME = "/uiautomatorTest.txt";
    private static final String configFILE_NAME = "/myconfig.xml";

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

    @Before
    public void before() {
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
    public static void beforeClass() throws IOException {
        //测试项目开始前运行（仅一次），如清除缓存数据、安装应用等
        Log.d(TAG, "beforeClass: ");
        //获取权限
        getInstrumentation().getUiAutomation().executeShellCommand("pm grant " + InstrumentationRegistry.getTargetContext().getPackageName() + " android.permission.WRITE_EXTERNAL_STORAGE");
        getInstrumentation().getUiAutomation().executeShellCommand("pm grant " + InstrumentationRegistry.getTargetContext().getPackageName() + " android.permission.READ_EXTERNAL_STORAGE");
        getInstrumentation().getUiAutomation().executeShellCommand("pm grant " + InstrumentationRegistry.getTargetContext().getPackageName() + " android.permission.MODIFY_AUDIO_SETTINGS");
        try {
            if (!mDevice.isScreenOn()) {
                Log.e(TAG, "isScreenOn=="+mDevice.isScreenOn());
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
                                    "playVideo_time:  " + playVideo_times + "\n" +
                                    "playgames_time:  " + playgames_time + "\n" +
                                    "sendmail_count:  " + sendmail_count + "\n" +
                                    "takeVideo_time:  " + takeVideo_time + "\n" +
                                    "weiboTime:  " + weiboTime + "\n" +
                                    "qqCount:  " + qqCount + "\n" +
                                    "qqIntervals:  " + qqIntervals + "\n" +
                                    "customerNumber:  " + customerNumber + "\n" +
                                    "smsMsg:  " + smsMsg + "\n" +
                                    "qqMsg:  " + qqMsg + "\n" +
                                    "WeChatMsg:  " + WeChatMsg + "\n" +
                                    "weChatcount" + weChatcount + "\n" +
                                    "sendSMSCount" + sendSMSCount + "\n" +
                                    "weChatInterval" + weChatInterval + "\n" +
                                    "smsInterval" + smsInterval + "\n"

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
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        isEnd = true;
        assertTrue("before_test_001" , isEnd);
        Log.d(TAG, "before_test_001 assertTrue end");

    }
}
