package com.example.agingmonkeytestv2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

import com.example.agingmonkeytestv2.service.MonkeyBackgroundService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.EditText;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@sagereal.com 
 *   > DATE: Date on 18-8-7.
 ************************************************************************/


public class MonkeyEntryActivity extends Activity{

    public static final String TAG = "lxn-MonkeyEntryActivity";//MonkeyEntryActivity.class.getSimpleName();
    public static final int EVENT_COUNT = 1000000;
    public static final int EVENT_THROTTLE = 1000;
    public static final int ID_DAY = 1;
    public static final int ID_HOUR = 2;
    public static final int ID_MINUTE = 3;
    public static final int ID_SECOND = 4;

    public static final String KEY_MONKEY_COMMAND = "key_monkey_command";
    public static final String KEY_MONKEY_DURATION = "key_monkey_duration";
    public static final String KEY_MONKEY_HAS_RESTARTED = "key_monkey_has_restarted";
    public static final String KEY_MONKEY_HAS_STARTED = "key_monkey_has_started";
    public static final String KEY_MONKEY_HAS_STOPPED = "key_monkey_has_stopped";
    public static final String KEY_MONKEY_START_TIMEMILLS = "key_monkey_start_timemills";
    public static final String KEY_MONKEY_STOP_TIMEMILLS = "key_monkey_stop_timemills";
    
    public static final String MONKEY_PREFERENCES_NAME = "monkey_preferences_name";
    public static final String MTKLOG_PACKAGE_NAME = "com.mediatek.mtklogger";
    public static final String SPRD_EM_PACKAGE_NAME = "com.sprd.engineermode";
    public static final String SPRD_SETTINGS_PACKAGE_NAME = "com.android.settings";
    
    private static final String blackListFileName = "MonkeyBlackList.txt";

    private static final String blackListFilePath = (Environment.getExternalStorageDirectory().getPath() + "/");

    private int dayMax = 10;
    private int dayMin = 0;
    private int hourMax = 23;
    private int hourMin = 0;

    private EditText mDaySelectorTv;
    private EditText mHourSelectorTv;
    private EditText mMinuteSelectorTv;
    private EditText mSecondSelectorTv;
    private Button mStartBt;

    private int minuteMax = 59;
    private int minuteMin = 0;
    private int secondMax = 59;
    private int secondMin = 0;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            int min = 0;
            int max = 60;
            int current = 23;
            int id = -1;

            String title = MonkeyEntryActivity.this.getString(R.string.time_choose_title);
            String label = "";

            Log.d(TAG, "mOnClickListener=view.getId()=" + view.getId());
            switch (view.getId()){
                case R.id.time_selector_day:
                    min = MonkeyEntryActivity.this.dayMin;
                    max = MonkeyEntryActivity.this.dayMax;
                    id = 1;
                    label = MonkeyEntryActivity.this.getString(R.string.time_label_day);
                    current = Integer.parseInt(MonkeyEntryActivity.this.mDaySelectorTv.getText().toString());
                    break;
                case R.id.time_selector_hour:
                    min = MonkeyEntryActivity.this.hourMin;
                    max = MonkeyEntryActivity.this.hourMax;
                    id = 2;
                    label = MonkeyEntryActivity.this.getString(R.string.time_label_hour);
                    current = Integer.parseInt(MonkeyEntryActivity.this.mDaySelectorTv.getText().toString());
                    break;
                case R.id.time_selector_minute:
                    min = MonkeyEntryActivity.this.minuteMin;
                    max = MonkeyEntryActivity.this.minuteMax;
                    id = 3;
                    label = MonkeyEntryActivity.this.getString(R.string.time_label_minute);
                    current = Integer.parseInt(MonkeyEntryActivity.this.mMinuteSelectorTv.getText().toString());
                    break;
                case R.id.time_selector_second:
                    min = MonkeyEntryActivity.this.secondMin;
                    max = MonkeyEntryActivity.this.secondMax;
                    id = 4;
                    label = MonkeyEntryActivity.this.getString(R.string.time_label_second);
                    current = Integer.parseInt(MonkeyEntryActivity.this.mSecondSelectorTv.getText().toString());
                    break;
            }
            MonkeyEntryActivity.this.createDialog(MonkeyEntryActivity.this, title, label, id, min, max, current).show();

        }
    };

    private View.OnClickListener mStartOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Log.d(TAG, "MonkeyEntryActivity.commandStartMonkey-----");
            MonkeyEntryActivity.commandStartMonkey(MonkeyEntryActivity.this,
                    Integer.parseInt(MonkeyEntryActivity.this.mDaySelectorTv.getText().toString()),
                    Integer.parseInt(MonkeyEntryActivity.this.mHourSelectorTv.getText().toString()),
                    Integer.parseInt(MonkeyEntryActivity.this.mMinuteSelectorTv.getText().toString()),
                    Integer.parseInt(MonkeyEntryActivity.this.mSecondSelectorTv.getText().toString()),
                    null);
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monkey_entry);
        initView();
    }

    private void initView(){
        Log.d(TAG, "initView----");
        this.mDaySelectorTv = (EditText) findViewById(R.id.time_selector_day);
        this.mDaySelectorTv.setOnClickListener(this.mOnClickListener);
        this.mHourSelectorTv = (EditText) findViewById(R.id.time_selector_hour);
        this.mHourSelectorTv.setOnClickListener(this.mOnClickListener);
        this.mMinuteSelectorTv = (EditText) findViewById(R.id.time_selector_minute);
        this.mMinuteSelectorTv.setOnClickListener(this.mOnClickListener);
        this.mSecondSelectorTv = (EditText) findViewById(R.id.time_selector_second);
        this.mSecondSelectorTv.setOnClickListener(this.mOnClickListener);
        this.mStartBt = (Button) findViewById(R.id.monkey_start);
        this.mStartBt.setOnClickListener(this.mStartOnClickListener);
    }


    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy----");
    }


    private static Date getEndDate(int day, int hour, int minute, int second) {
        long timeMills = (((new Date().getTime() + ((long) ((((day * 24) * 60) * 60) * 1000))) + ((long) (((hour * 60) * 60) * 1000))) + ((long) ((minute * 60) * 1000))) + ((long) (second * 1000));
        Log.d(TAG, "getEndDate:" + new Date(timeMills).toString());
        return new Date(timeMills);
    }


    private static String getMonkeyCommand(int throttle, long count, List<String> packageUnderTest) {
        StringBuilder builder = new StringBuilder();
        Log.d(TAG, "getMonkeyCommand----");
        if (!(packageUnderTest == null || packageUnderTest.isEmpty())) {
            for (String str : packageUnderTest) {
                builder.append(" ");
                builder.append("-p");
                builder.append(" ");
                builder.append(str);
                builder.append(" ");
            }
        }
        if (packageUnderTest == null || packageUnderTest.isEmpty()) {
            return "monkey --throttle " + throttle + builder.toString() + " --ignore-timeouts --ignore-crashes --ignore-security-exceptions --ignore-native-crashes -v -v -v --pkg-blacklist-file " + blackListFilePath + blackListFileName + " " + count;
        }//TODO
        return "monkey --throttle " + throttle + builder.toString() + " --ignore-timeouts --ignore-crashes --ignore-security-exceptions --ignore-native-crashes -v -v -v " + count;
    }

    private long getMonkeyRunCount(long timeMills, int throttle) {
        return timeMills / ((long) throttle);
    }

    private static long getEndTimeMills(int day, int hour, int minute, int second) {
        return (long) (((((((day * 24) * 60) * 60) * 1000) + (((hour * 60) * 60) * 1000)) + ((minute * 60) * 1000)) + (second * 1000));
    }

    public static void commandStartMonkey(Context ctx, int day, int hour, int minute, int second, List<String> packageUnderTest, boolean fromBroadcastReceiver) {
        if (fromBroadcastReceiver) {
            Log.d(TAG, "request that start monkey come from broadcast receiver ");
        }
        commandStartMonkey(ctx, day, hour, minute, second, packageUnderTest);
    }

    public static void commandStartMonkey(Context ctx, int day, int hour, int minute, int second, List<String> packageUnderTest) {
        long timeMills = getEndTimeMills(day, hour, minute, second);
        Log.d(TAG, "onClick: timeMills: " + timeMills);
        String monkeyCommand = getMonkeyCommand(1000, 1000000, packageUnderTest);
        Log.d(TAG, "Monkey command: " + monkeyCommand);
        Log.d(TAG, "Monkey start run at: " + new Date().toString());
        Log.d(TAG, "Monkey will end run at: " + getEndDate(day, hour, minute, second).toString());
        createBlackListFile(ctx);
        Log.d(TAG, 
        		"editot.KEY_MONKEY_START_TIMEMILLS=" + SystemClock.elapsedRealtime()
        		+"\n\t KEY_MONKEY_DURATION=" + KEY_MONKEY_DURATION
        		+"\n\t monkeyCommand=" + monkeyCommand);
        SharedPreferences.Editor editor = ctx.getSharedPreferences(MONKEY_PREFERENCES_NAME, 0).edit();
        editor.clear();
        editor.putLong(KEY_MONKEY_START_TIMEMILLS, SystemClock.elapsedRealtime());
        editor.putLong(KEY_MONKEY_DURATION, timeMills);
        editor.putString(KEY_MONKEY_COMMAND, monkeyCommand);
        editor.putBoolean(KEY_MONKEY_HAS_STOPPED, false);
        editor.apply();
        Intent service = new Intent(ctx, MonkeyBackgroundService.class);
        Log.d(TAG, "MonkeyBackgroundService---");
        if (MonkeyBackgroundService.checkServiceAlive()) {
            Log.d(TAG, "onClick: check Service is alive, stop it");
            ctx.stopService(service);
        }
        ctx.startService(service);
    }


    private AlertDialog createDialog(Context context, String title, String label, final int id, int min , int max, int current) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle((CharSequence) title);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_time_picker, null, false);
        final NumberPicker numberPicker = (NumberPicker)view.findViewById(R.id.number_picker);
        ((TextView) view.findViewById(R.id.label)).setText(label);
        numberPicker.setMinValue(min);
        numberPicker.setMaxValue(max);
        numberPicker.setValue(current);

        builder.setView(view);
        builder.setNegativeButton("CANCEL", null);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (id) {
                    case 1:
                        MonkeyEntryActivity.this.mDaySelectorTv.setText(numberPicker.getValue() + "");
                        return;
                    case 2:
                        MonkeyEntryActivity.this.mHourSelectorTv.setText(numberPicker.getValue() + "");
                        return;
                    case 3:
                        MonkeyEntryActivity.this.mMinuteSelectorTv.setText(numberPicker.getValue() + "");
                        return;
                    case 4:
                        MonkeyEntryActivity.this.mSecondSelectorTv.setText(numberPicker.getValue() + "");
                        return;
                    default:
                        return;
                }
            }
        });
        return builder.create();

    }

    //TODO
    public static void createBlackListFile(Context context) {
        Log.d(TAG, "createBlackListFile--- ");
        File rootPath = new File(blackListFilePath);
        if (!rootPath.exists()) {
            rootPath.mkdir();
        }

        File whiteListFile = new File(blackListFilePath + blackListFileName);
        whiteListFile.deleteOnExit();
        try {
            whiteListFile.createNewFile();
            BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(whiteListFile)));

            String[] list = getBlackListPackageName(context);
            if (list != null && list.length > 0) {
                Log.d(TAG, "list.length==="+list.length);
                for (String  str : list) {
                	bufferedwriter.write(str + "\n");
                    Log.d(TAG, "createBlackListFile: write black list package name ===" + str);
                }

            }
            if (bufferedwriter != null) {
            	bufferedwriter.close();
            	bufferedwriter = null;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    private static String[] getBlackListPackageName(Context ctx) {
        if (FactoryApplication.isMediatek()) {
            return new String[]{ctx.getPackageName(), MTKLOG_PACKAGE_NAME, SPRD_EM_PACKAGE_NAME,};
        }
        return new String[]{ctx.getPackageName(), MTKLOG_PACKAGE_NAME, SPRD_EM_PACKAGE_NAME, SPRD_SETTINGS_PACKAGE_NAME, "com.android.dialer"};
    }
    
}
