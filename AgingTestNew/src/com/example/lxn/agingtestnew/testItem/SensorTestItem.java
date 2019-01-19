package com.example.lxn.agingtestnew.testItem;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lxn.agingtestnew.CompletedActivity;
import com.example.lxn.agingtestnew.R;
import com.example.lxn.agingtestnew.SettingsActivity;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 18-12-28.
 ************************************************************************/


public class SensorTestItem extends AbstractBaseTestItem implements SensorEventListener {
    private static final String TAG = "lxn-GraTestItem";

    private SensorManager mSensorManager;
    private TextView mTv_gra;
    private TextView mTv_lig;
    private TextView mTv_pro;

    private Sensor mSensor_gra;
    private Sensor mSensor_lig;
    private Sensor mSensor_pro;

    private Handler mHandler = new Handler();
    private long timer = 0;
    private String timeStr = "";
    private boolean isStopCount = false;
    private int testTimes = 0;
    private int testCount = 0;



    public SensorTestItem(int resLayoutId) {
        super(resLayoutId);
        Log.d(TAG, "SensorTestItem");

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(TAG, "onSensorChanged");

        if (sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float[] values_gra = sensorEvent.values;
            StringBuilder sb_gra = new StringBuilder();
            sb_gra.append("X方向的重力：");
            sb_gra.append(values_gra[0]);
            sb_gra.append("\nY方向的重力：");
            sb_gra.append(values_gra[1]);
            sb_gra.append("\nZ方向的重力：");
            sb_gra.append(values_gra[2]);
            mTv_gra.setText(sb_gra.toString());
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            float values_lig = sensorEvent.values[0];
            StringBuilder sb_lig = new StringBuilder();
            sb_lig.append("光强度为：");
            sb_lig.append(values_lig);
            mTv_lig.setText("\n" + sb_lig);

        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float values_pro = sensorEvent.values[0];
            StringBuilder sb_pro = new StringBuilder();
            sb_pro.append("临近传感器值：");
            sb_pro.append(values_pro);
            mTv_pro.setText("\n" + sb_pro);
        }
        if (testTimes != 0) {
            CompletedActivity.sensorStatus = "PASS";
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged");

    }

    public void setUp() {
        Log.d(TAG, "setUp: " + getClass().getSimpleName());
    }

    public void tearDown() {
        Log.d(TAG, "tearDown");
    }

    @Override
    public boolean execTest(Handler handler) {
        Log.d(TAG, "execTest");

        if (testCount < testTimes) {
            if (isStopCount) {
                testCount++;
            }
            return false;
        } else {
            onTestEnd();
            return true;
        }
    }


    public void onTestEnd(){
        super.onTestEnd();
    }

    public void onStop(){
        super.onStop();
    }

    @Override
    protected void initView(View view) {

        Log.d(TAG, "initView");

        mTv_gra = (TextView) view.findViewById(R.id.tv_gra);
        mTv_lig = (TextView) view.findViewById(R.id.tv_light);
        mTv_pro = (TextView) view.findViewById(R.id.tv_pro);
        testTimes = SettingsActivity.sensor_times;

        // 获取传感器管理对象
        mSensorManager = (SensorManager) view.getContext().getSystemService(Context.SENSOR_SERVICE);
        // 获取传感器的类型(TYPE_ACCELEROMETER:加速度传感器)
        mSensor_gra = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mSensor_lig = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensor_pro = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (mSensor_gra == null) {
            mTv_gra.setText("未找到重力传感器");
        } else {
            mSensorManager.registerListener((SensorEventListener) this, mSensor_gra, SensorManager.SENSOR_DELAY_GAME);
        }
        if (mSensor_lig == null) {
            mTv_gra.setText("未找到光线传感器");
        } else {
            mSensorManager.registerListener((SensorEventListener) this, mSensor_lig, SensorManager.SENSOR_DELAY_GAME);
        }
        if (mSensor_pro == null) {
            mTv_gra.setText("未找到距离传感器");
        } else {
            mSensorManager.registerListener((SensorEventListener) this, mSensor_pro, SensorManager.SENSOR_DELAY_GAME);
        }
        countTimer();

    }



    private Runnable TimerRunnable = new Runnable() {

        @Override
        public void run() {
            if (!isStopCount) {
                timer += 1000;
                timeStr = getFormatHMS(timer);
                if (timer == (5 *1000)) {
                    isStopCount = true;
                }
            }
            countTimer();
        }
    };

    private void countTimer() {
        mHandler.postDelayed(TimerRunnable, 1000);
    }

    public static String getFormatHMS(long time) {
        time = time / 1000;//总秒数
        int s = (int) (time % 60);//秒
        int m = (int) (time / 60);//分
        int h = (int) (time / 3600);//时
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
