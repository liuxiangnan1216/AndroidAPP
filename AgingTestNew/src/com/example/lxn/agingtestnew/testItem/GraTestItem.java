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

import com.example.lxn.agingtestnew.R;

/*************************************************************************
 *   > Author: LiuXiangNan
 *   > Mail: LiuXiangnan@.com
 *   > DATE: Date on 18-12-22.
 ************************************************************************/


public class GraTestItem extends AbstractBaseTestItem implements SensorEventListener{

    private static final String TAG = "lxn-GraTestItem";

    private SensorManager mSensorManager;
    private TextView mTextView;
    private Sensor mSensor;


    public GraTestItem(int resLayoutId) {
        super(resLayoutId);
        Log.d(TAG, "GraTestItem");

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG, "onSensorChanged");
        float[] values = event.values;
        StringBuilder sb = new StringBuilder();
        sb.append("X方向的加速度：");
        sb.append(values[0]);
        sb.append("\nY方向的加速度：");
        sb.append(values[1]);
        sb.append("\nZ方向的加速度：");
        sb.append(values[2]);
        mTextView.setText(sb.toString());

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
        onTestEnd();
        return false;
    }

    public void onStop(){
        super.onStop();
    }

    @Override
    protected void initView(View view) {

        Log.d(TAG, "initView");

        mTextView = (TextView) view.findViewById(R.id.txt_value);

        // 获取传感器管理对象
        mSensorManager = (SensorManager) view.getContext().getSystemService(Context.SENSOR_SERVICE);
        // 获取传感器的类型(TYPE_ACCELEROMETER:加速度传感器)
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (mSensor == null) {
            mTextView.setText("未找到重力传感器");
        }
        // 注册监听器
        mSensorManager.registerListener((SensorEventListener) this, mSensor, SensorManager.SENSOR_DELAY_GAME);

    }
}
