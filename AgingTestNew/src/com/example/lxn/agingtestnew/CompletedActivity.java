package com.example.lxn.agingtestnew;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;


/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com 
 *   > DATE: Date on 19-1-11.
 ************************************************************************/


public class CompletedActivity extends Activity{
    private static final String TAG = "lxn-CompletedActivity";

    public static final String COMPLETE_MESSAGE = "COMPLETE_MESSAGE";
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private TextView tv8;
    private TextView tv9;
    private TextView tv10;
    private TextView tv11;
    private TextView tv12;
    private TextView tv13;
    private TextView tv14;
    private TextView tv15;
    private TextView tv_starttime;
    private TextView tv_endtime;



    public static String memoryStatus = "No Execute";
    public static String emmcStatus = "No Execute";
    public static String batteryStatus = "No Execute";
    public static String audioStatus = "No Execute";
    public static String vibratorStatus = "No Execute";
    public static String cameraStatus = "No Execute";
    public static String videoStatus = "No Execute";
    public static String lcdStatus = "No Execute";
    public static String gpsStatus = "No Execute";
    public static String bluetoothStatus = "No Execute";
    public static String wifiStatus = "No Execute";
    public static String backlightStatus = "No Execute";
    public static String sensorStatus = "No Execute";
    public static String earpieceStatus = "No Execute";
    public static String startTestTime = "Starting time：";
    public static String endTestTime = "Complete time：";




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        tv1 = (TextView) findViewById(R.id.tv_1);
        tv2 = (TextView) findViewById(R.id.tv_2);
        tv3 = (TextView) findViewById(R.id.tv_3);
        tv4 = (TextView) findViewById(R.id.tv_4);
        tv5 = (TextView) findViewById(R.id.tv_5);
        tv6 = (TextView) findViewById(R.id.tv_6);
        tv7 = (TextView) findViewById(R.id.tv_7);
        tv8 = (TextView) findViewById(R.id.tv_8);
        tv9 = (TextView) findViewById(R.id.tv_9);
        tv10 = (TextView) findViewById(R.id.tv_10);
        tv11 = (TextView) findViewById(R.id.tv_11);
        tv12 = (TextView) findViewById(R.id.tv_12);
        tv13 = (TextView) findViewById(R.id.tv_13);
        tv14 = (TextView) findViewById(R.id.tv_14);
        tv15 = (TextView) findViewById(R.id.tv_15);
        tv_starttime = (TextView) findViewById(R.id.tv_starttime);
        tv_endtime = (TextView) findViewById(R.id.tv_endtime);

    }


    protected void onResume() {
        super.onResume();

        String str1 = "\nMemory test result    : " + memoryStatus;
        String str2 = "\nEmmc test result      : " + emmcStatus;
        String str3 = "\nBattery test result   : " + batteryStatus;
        String str4 = "\nAudio test result     : " + audioStatus;
        String str5 = "\nVibrator test result  : " + vibratorStatus;
        String str6 = "\nCamera test result    : " + cameraStatus;
        String str7 = "\nVideo test result     : " + videoStatus;
        String str8 = "\nLCD test result       : " + lcdStatus;
        String str9 = "\nGPS test result       : " + gpsStatus;
        String str10 = "\nBluetooth test result : " + bluetoothStatus;
        String str11 = "\nWiFi test result      : " + wifiStatus;
        String str12 = "\nSensor test result    : " + sensorStatus;
        String str13 = "\nBlackLight test result: " + backlightStatus;
        String str14 = "\nEarpiece test result  : " + earpieceStatus;
        String str15 = "";


        tv_starttime.setText(startTestTime);
        tv_endtime.setText(endTestTime);
        tv1.setText(str1);
        tv2.setText(str2);
        tv3.setText(str3);
        tv4.setText(str4);
        tv5.setText(str5);
        tv6.setText(str6);
        tv7.setText(str7);
        tv8.setText(str8);
        tv9.setText(str9);
        tv10.setText(str10);
        tv11.setText(str11);
        tv12.setText(str12);
        tv13.setText(str13);
        tv14.setText(str14);
        tv15.setText(str15);



        if (memoryStatus.equals("PASS") && emmcStatus.equals("PASS") && batteryStatus.equals("PASS")
                && audioStatus.equals("PASS") && audioStatus.equals("PASS") && vibratorStatus.equals("PASS")
                && cameraStatus.equals("PASS") && videoStatus.equals("PASS") && lcdStatus.equals("PASS")
                && bluetoothStatus.equals("PASS") && wifiStatus.equals("PASS") && sensorStatus.equals("PASS")
                && backlightStatus.equals("PASS") && earpieceStatus.equals("PASS")) {
            ResultShowActivity.resut_test = "PASS";
        } else if (memoryStatus.equals("No Execute") && emmcStatus.equals("No Execute") && batteryStatus.equals("No Execute")
                && audioStatus.equals("No Execute") && audioStatus.equals("No Execute") && vibratorStatus.equals("No Execute")
                && cameraStatus.equals("No Execute") && videoStatus.equals("No Execute") && lcdStatus.equals("No Execute")
                && bluetoothStatus.equals("No Execute") && wifiStatus.equals("No Execute") && sensorStatus.equals("No Execute")
                && backlightStatus.equals("No Execute") && earpieceStatus.equals("No Execute")) {

            ResultShowActivity.resut_test = "NoExecute";
        }else{
                ResultShowActivity.resut_test = "FAIL";
        }


        Toast.makeText(CompletedActivity.this, "即将进入屏保界面！", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(CompletedActivity.this, ResultShowActivity.class);
                startActivity(intent);
            }
        }, 1000 * 10);
    }


}
