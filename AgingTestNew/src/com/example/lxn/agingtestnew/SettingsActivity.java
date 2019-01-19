package com.example.lxn.agingtestnew;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lxn.agingtestnew.service.AutoRunService;
import com.example.lxn.agingtestnew.testItem.AudioTestItem;
import com.example.lxn.agingtestnew.testItem.BackLightTestItem;
import com.example.lxn.agingtestnew.testItem.BatteryTestItem;
import com.example.lxn.agingtestnew.testItem.BluetoothTestItem;
import com.example.lxn.agingtestnew.testItem.CameraTestItem;
import com.example.lxn.agingtestnew.testItem.EarpieceTestitem;
import com.example.lxn.agingtestnew.testItem.EmmcTestItem;
import com.example.lxn.agingtestnew.testItem.GpsTestItem;
import com.example.lxn.agingtestnew.testItem.LcdTestItem;
import com.example.lxn.agingtestnew.testItem.MemoryTestItem;
import com.example.lxn.agingtestnew.testItem.RebootTestItem;
import com.example.lxn.agingtestnew.testItem.SensorTestItem;
import com.example.lxn.agingtestnew.testItem.TestItemHolder;
import com.example.lxn.agingtestnew.testItem.VibratorTestItem;
import com.example.lxn.agingtestnew.testItem.VideoTestItem;
import com.example.lxn.agingtestnew.testItem.WifiTestItem;
import com.example.lxn.agingtestnew.utills.ObjectWriterReader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com 
 *   > DATE: Date on 19-1-12.
 ************************************************************************/


public class SettingsActivity extends Activity {
    private static final String TAG = "lxn-SettingActivity";

    public static int reboot_times = 60;
    public static int memory_times = 15600;
    public static int emmc_times = 24;
    public static int battery_times = 1;
    public static int audio_times = 11;
    public static long vibrator_time = 40 * 60 * 1000;
    public static int video_times = 32;
    public static int lcd_times = 1300;
    public static int gps_times = 1;
    public static int bt_times = 1;
    public static int wifi_times = 1;
    public static int sensor_times = 1;
    public static int backlight_times = 1300;
    public static long earpiece_time = 30 * 60 * 1000;
    public static int camera_times = 80;

    private EditText et_reboot;
    private EditText et_memory;
    private EditText et_emmc;
    private EditText et_battery;
    private EditText et_audio;
    private EditText et_vibmin;
    private EditText et_vibsec;
    private EditText et_video;
    private EditText et_lcd;
    private EditText et_gps;
    private EditText et_bt;
    private EditText et_wifi;
    private EditText et_sensor;
    private EditText et_backlight;
    private EditText et_earmin;
    private EditText et_earsec;
    private EditText et_camera;

    private Button btn_start;

    private ArrayList<TestItemHolder> mItemHolderList = new ArrayList();
    private ArrayList<TestItemHolder> mAutoItemHolderList = new ArrayList();
    private ArrayList<Boolean> mTestItemCheckeds = new ArrayList();
    private ArrayList<String> mTestItemNames = new ArrayList();
    private boolean isNeedReboot = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        et_reboot = (EditText) findViewById(R.id.ed_reboot);
        et_memory = (EditText) findViewById(R.id.ed_memory);
        et_emmc = (EditText) findViewById(R.id.ed_emmc);
        et_battery = (EditText) findViewById(R.id.ed_battery);
        et_audio = (EditText) findViewById(R.id.ed_audio);
        et_vibmin = (EditText) findViewById(R.id.ed_vibratormin);
        et_vibsec = (EditText) findViewById(R.id.ed_vibratorsec);
        et_video = (EditText) findViewById(R.id.ed_video);
        et_lcd = (EditText) findViewById(R.id.ed_lcd);
        et_gps = (EditText) findViewById(R.id.ed_gps);
        et_bt = (EditText) findViewById(R.id.ed_bt);
        et_wifi = (EditText) findViewById(R.id.ed_wifi);
        et_sensor = (EditText) findViewById(R.id.ed_sensor);
        et_backlight = (EditText) findViewById(R.id.ed_back);
        et_earmin = (EditText) findViewById(R.id.ed_earmin);
        et_earsec = (EditText) findViewById(R.id.ed_earsec);
        et_camera = (EditText) findViewById(R.id.ed_camera);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this.mStartOnClickListener);

        initTestItem();

        Toast.makeText(SettingsActivity.this, "自动测试前，请手动进行 Reboot 单项测试！", Toast.LENGTH_LONG).show();
    }

    public void onResume() {
        super.onResume();
        saveSettingsTimes();
    }



    private View.OnClickListener mStartOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveSettingsTimes();

            CompletedActivity.startTestTime = "Starting time： " + getTimeTag();

            Log.d(TAG, "mAutoRunOnClickListener");
            Intent intent = new Intent();
            intent.setClass(SettingsActivity.this, AutoRunService.class);
            Bundle bundle = new Bundle();
            bundle.putInt("TestType", 3);
            bundle.putInt("TestTimes", 1);
            ArrayList<TestItemHolder> tempList = new ArrayList<>();
            Log.d(TAG, "MainActivity.this.mAutoItemHolderList.size()===="+ SettingsActivity.this.mAutoItemHolderList.size());
            for (int i = 0; i < SettingsActivity.this.mAutoItemHolderList.size(); i++) {
                if (((Boolean) SettingsActivity.this.mTestItemCheckeds.get(i)).booleanValue()) {
                    tempList.add(SettingsActivity.this.mAutoItemHolderList.get(i));
                    Log.d(TAG, "88888888888" + tempList);
                }
            }
            Log.d(TAG, "tempList.size====="+tempList.size());
            bundle.putParcelableArrayList("ItemHolderList", tempList);
            bundle.putBoolean("TestNeedReboot", false);
            bundle.putInt("RebootTimes", -1);
            intent.putExtras(bundle);
            if (SettingsActivity.this.isNeedReboot) {
                ObjectWriterReader.write(bundle);
            }
            Log.d(TAG, "intent===" + intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                SettingsActivity.this.startForegroundService(intent);
            } else {
                SettingsActivity.this.startService(intent);
            }

        }
    };

    public void onPause(){
        super.onPause();
        saveSettingsTimes();
    }


    public void onStop() {
        super.onStop();
        saveSettingsTimes();
    }

    private void saveSettingsTimes(){
        Log.d(TAG, "reboot times====" + et_reboot.getText().toString());
        if (!et_reboot.getText().toString().equals("")) {
            Log.d(TAG, "set reboot times====" + et_reboot.getText());
            reboot_times = Integer.parseInt(et_reboot.getText().toString());
        }
        if (!et_memory.getText().toString().equals("")) {
            memory_times = Integer.parseInt(et_memory.getText().toString());
        }
        if (!et_emmc.getText().toString().equals("")) {
            emmc_times = Integer.parseInt(et_emmc.getText().toString());
        }
        if (!et_battery.getText().toString().equals("")) {
            battery_times = Integer.parseInt(et_battery.getText().toString());
        }
        if (!et_audio.getText().toString().equals("")) {
            audio_times = Integer.parseInt(et_audio.getText().toString());
        }
        if (!et_vibmin.getText().toString().equals("") || !et_vibsec.getText().toString().equals("")){
            int vibmin = 0;
            int vibsec = 0;
            if (!et_vibmin.getText().toString().equals("")) {
                vibmin = Integer.parseInt(et_vibmin.getText().toString());
            }
            if (!et_vibsec.getText().toString().equals("")) {
                vibsec = Integer.parseInt(et_vibsec.getText().toString());
            }

            vibrator_time = (long)((vibmin * 60 + vibsec) * 1000);
        }
        if (!et_camera.getText().toString().equals("")) {
            camera_times = Integer.parseInt(et_camera.getText().toString());
        }
        if (!et_video.getText().toString().equals("")) {
            video_times = Integer.parseInt(et_video.getText().toString());
        }
        if (!et_lcd.getText().toString().equals("")) {
            lcd_times = Integer.parseInt(et_lcd.getText().toString());
        }
        if (!et_gps.getText().toString().equals("")) {
            gps_times = Integer.parseInt(et_gps.getText().toString());
        }
        if (!et_bt.getText().toString().equals("")) {
            bt_times = Integer.parseInt(et_bt.getText().toString());
        }
        if (!et_wifi.getText().toString().equals("")) {
            wifi_times = Integer.parseInt(et_wifi.getText().toString());
        }
        if (!et_sensor.getText().toString().equals("")) {
            sensor_times = Integer.parseInt(et_sensor.getText().toString());
        }
        if (!et_backlight.getText().toString().equals("")) {
            backlight_times = Integer.parseInt(et_backlight.getText().toString());
        }
        if (!et_earmin.getText().toString().equals("") || !et_earsec.getText().toString().equals("")) {
            int earmin = 0;
            int earsec = 0;
            if (!et_earmin.getText().toString().equals("")) {
                earmin = Integer.parseInt(et_earmin.getText().toString());
            }
            if (!et_earsec.getText().toString().equals("")) {
                earsec = Integer.parseInt(et_earsec.getText().toString());
            }
            earpiece_time = (long)((earmin * 60 + earsec) * 1000);
        }

    }



    private void initTestItem() {
        this.mItemHolderList.clear();
        Log.d(TAG, "initTestItem-----------");
//        Intent intent = new Intent();
//        intent.setClass(context, AutoRunService.class);
//        Bundle bundle = new Bundle();
//        bundle.putInt("TestType", 4);
//        bundle.putLong("TestDuration", getTimeMills(3, 0, 0, 0));
        ArrayList<TestItemHolder> tempList = new ArrayList();
        TestItemHolder reboot = new TestItemHolder("Reboot", R.layout.layout_testitem_reboot, RebootTestItem.class.getName(), false, true);
        TestItemHolder memory = new TestItemHolder("Memory", R.layout.layout_testitem_memory, MemoryTestItem.class.getName(), false, true);
        TestItemHolder emmc = new TestItemHolder("Emmc", R.layout.layout_testitem_emmc, EmmcTestItem.class.getName(), false, true);
        TestItemHolder battery = new TestItemHolder("Battery", R.layout.layout_testitem_battery, BatteryTestItem.class.getName(), false, true);
        TestItemHolder audio = new TestItemHolder("Audio", R.layout.layout_testitem_audio, AudioTestItem.class.getName(), false, true);
        TestItemHolder vibrator = new TestItemHolder("Vibrator", R.layout.layout_testitem_vibrator, VibratorTestItem.class.getName(), false, true);
        TestItemHolder camera = new TestItemHolder("Camera", R.layout.layout_testitem_camera, CameraTestItem.class.getName(), false, true);
        TestItemHolder video = new TestItemHolder("Video", R.layout.layout_testitem_video, VideoTestItem.class.getName(), false, true);
        TestItemHolder lcd = new TestItemHolder("LCD", R.layout.layout_testitem_lcd, LcdTestItem.class.getName(), true, true);
        TestItemHolder gps = new TestItemHolder("GPS", R.layout.layout_testitem_gps, GpsTestItem.class.getName(), false, true);
        TestItemHolder bluetooth = new TestItemHolder("Bluetooth", R.layout.layout_testitem_bluetooth, BluetoothTestItem.class.getName(), false, true);
        TestItemHolder wifi = new TestItemHolder("Wi-Fi", R.layout.layout_testitem_wifi, WifiTestItem.class.getName(), false, true);
//        TestItemHolder ligth = new TestItemHolder("Ligth", R.layout.layout_testitem_ligth, LightTestItem.class.getName(), false, true);
//        TestItemHolder pro = new TestItemHolder("Proximity", R.layout.layout_testitem_pro, ProTestItem.class.getName(), false, true);
//        TestItemHolder gra = new TestItemHolder("Gravity", R.layout.layout_testitem_gra, GraTestItem.class.getName(), false, true);
        TestItemHolder backlight = new TestItemHolder("BlackLight", R.layout.layout_testitem_backlight, BackLightTestItem.class.getName(), true, true);
//        TestItemHolder qc = new TestItemHolder("QuickCharge", R.layout.layout_testitem_qc, QcTestItem.class.getName(), false, true);
//        TestItemHolder headphone = new TestItemHolder("Headphone", R.layout.layout_testitem_headphone, HeadphoneTestItem.class.getName(), false, true);
        TestItemHolder sensor = new TestItemHolder("Sensor", R.layout.layout_testitem_sensor, SensorTestItem.class.getName(), false, true);
//        TestItemHolder charging = new TestItemHolder("Charging", R.layout.layout_testitem_charging, ChargingTestItem.class.getName(), false, true);
        TestItemHolder earpiece = new TestItemHolder("Earpiece", R.layout.layout_testitem_earpiece, EarpieceTestitem.class.getName(), false, true);


        tempList.add(memory);
        tempList.add(emmc);
        tempList.add(battery);
        tempList.add(audio);
        tempList.add(vibrator);
        tempList.add(camera);
        tempList.add(video);
        tempList.add(lcd);
        tempList.add(gps);
        tempList.add(bluetooth);
        tempList.add(wifi);
        tempList.add(backlight);
        tempList.add(sensor);
        tempList.add(earpiece);

        this.mAutoItemHolderList.add(memory);
        this.mAutoItemHolderList.add(emmc);
        this.mAutoItemHolderList.add(battery);
        this.mAutoItemHolderList.add(audio);
        this.mAutoItemHolderList.add(vibrator);
        this.mAutoItemHolderList.add(camera);
        this.mAutoItemHolderList.add(video);
        this.mAutoItemHolderList.add(lcd);
        this.mAutoItemHolderList.add(gps);
        this.mAutoItemHolderList.add(bluetooth);
        this.mAutoItemHolderList.add(wifi);
        this.mAutoItemHolderList.add(backlight);
        this.mAutoItemHolderList.add(sensor);
        this.mAutoItemHolderList.add(earpiece);
        Iterator it = this.mAutoItemHolderList.iterator();
        while (it.hasNext()) {
            this.mTestItemNames.add(((TestItemHolder) it.next()).getItemName());
            this.mTestItemCheckeds.add(Boolean.valueOf(true));
        }

    }


    private String getTimeTag() {
        Calendar calendar = Calendar.getInstance();
        return String.format("%02d-%02d %02d:%02d:%02d", new Object[]{
                Integer.valueOf(calendar.get(Calendar.MONTH) + 1),
                Integer.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),
                Integer.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),
                Integer.valueOf(calendar.get(Calendar.MINUTE)),
                Integer.valueOf(calendar.get(Calendar.SECOND))});
    }

}
