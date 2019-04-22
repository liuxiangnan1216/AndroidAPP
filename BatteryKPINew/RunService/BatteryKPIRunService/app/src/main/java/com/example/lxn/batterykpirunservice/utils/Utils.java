package com.example.lxn.batterykpirunservice.utils;

import android.os.Environment;
import android.util.Log;

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

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 19-4-2.
 ************************************************************************/


public class Utils {

    private static final String TAG = "lxn-utils";

    private static final String configFILE_NAME = "/myconfig.xml";


    public static String RUNCMD = "nohup am instrument -w -r   -e debug false -e class " +
            "com.example.android.testing.uiautomator.SagerealBatteryKPI.TestSuit " +
            "com.example.android.testing.uiautomator.SagerealBatteryKPI.test/android.support.test.runner.AndroidJUnitRunner";

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
    public static int standardtest_time = 30;

    public String FILE_NAME = "/uiautomatorTest_standby.txt";

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
                                    "\nstandardtest_time : " + standardtest_time +
                                    "\nRUNCMD: " + RUNCMD

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

}
